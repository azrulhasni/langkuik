/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.workflow;

import com.azrul.langkuik.custom.approval.Approval;
import com.azrul.langkuik.custom.approval.ApproverLookup;
import com.azrul.langkuik.framework.dao.DataAccessObject;
import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.framework.standard.Dual;
import com.azrul.langkuik.framework.expression.Expression;
import com.azrul.langkuik.framework.rule.PojoRule;
import com.azrul.langkuik.framework.entity.Status;
import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.script.Scripting;
import com.azrul.langkuik.framework.standard.Castor;
import com.azrul.langkuik.framework.user.UserProfile;
import com.azrul.langkuik.framework.user.UserRetrievalService;
import com.azrul.langkuik.framework.workflow.model.Activity;
import com.azrul.langkuik.framework.workflow.model.BaseActivity;
import com.azrul.langkuik.framework.workflow.model.BizProcess;
import com.azrul.langkuik.framework.workflow.model.ConditionalBranch;
import com.azrul.langkuik.framework.workflow.model.DefaultBranch;
import com.azrul.langkuik.framework.workflow.model.End;
import com.azrul.langkuik.framework.workflow.model.HumanActivity;
import com.azrul.langkuik.framework.workflow.model.ServiceActivity;
import com.azrul.langkuik.framework.workflow.model.StartEvent;
import com.azrul.langkuik.framework.workflow.model.XorActivity;
import com.azrul.langkuik.framework.workflow.model.XorAtleastOneApprovalActivity;
import com.azrul.langkuik.framework.workflow.model.XorMajorityApprovalActivity;
import com.azrul.langkuik.framework.workflow.model.XorUnanimousApprovalActivity;
import com.vaadin.flow.server.VaadinSession;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import com.azrul.langkuik.framework.user.UserIdentifierLookup;
import java.util.Collection;

/**
 *
 * @author azrul
 * @param <T>
 */
public class WorkflowImpl<T extends WorkElement> implements Workflow<T> {

    private BizProcess bizProcess;

    @Autowired
    private Scripting scripting;

    @Autowired
    private DataAccessObject dao;

    @Autowired
    private Map<String, Activity> activities;

    @Autowired(required = false)
    private List<ApproverLookup<T>> approverLookups;

    @Autowired
    private UserRetrievalService userRetrievalService;

    @Autowired
    private UserIdentifierLookup userIdentifierLookup;

    @Autowired
    private Expression<Boolean, T> expr;

    @Autowired
    @Qualifier("IsSupervisorApprovalNeeded")
    private PojoRule isSupervisorApprovalNeeded;

    @Autowired
    UserIdentifierLookup userNameLookup;

    public WorkflowImpl(BizProcess bizProcess) {
        this.bizProcess = bizProcess;
    }

    @Override
    public Map<String, List<HumanActivity>> getRoleActivityMap() {
        return getActivities()
                .values()
                .stream()
                .filter(x -> HumanActivity.class.isAssignableFrom(x.getClass()))
                .map(x -> (HumanActivity) x)
                .filter(x -> x.getHandledBy() != null)
                .collect(Collectors.groupingBy(HumanActivity::getHandledBy));
    }

    @Override
    public Boolean isApprovalActivity(String activity) {
        return isApprovalActivity(activities.get(activity));
    }

    @Override
    public Boolean isApprovalActivity(Activity activity) {
        if (activity == null) {
            return Boolean.FALSE;
        }
        return (activity.getClass().equals(XorAtleastOneApprovalActivity.class)
                || activity.getClass().equals(XorUnanimousApprovalActivity.class)
                || activity.getClass().equals(XorMajorityApprovalActivity.class));
    }

    @Override
    public T run(final T work, boolean isError) {
        //Pre-run script
        String worklist = work.getWorkflowInfo().getWorklist();
        Activity currentActivity = activities.get(worklist);
        Castor.<Activity, BaseActivity>given(currentActivity).castItTo(BaseActivity.class).thenDo(ba -> {
            scripting.runScript(work, ba.getPreRunScript(), this);
        }).go();
        Castor.<Activity, StartEvent>given(currentActivity).castItTo(StartEvent.class).thenDo(s -> {
            work.setStatus(Status.IN_PROGRESS);
        }).go();
        Castor.<Activity, End>given(currentActivity).castItTo(End.class).thenDo(e -> {
            scripting.runScript(work, e.getPreRunScript(), this);
        }).go();

        if (!currentActivity.getClass().equals(End.class)) { //if not the end
            //transition to next steps
            List<Activity> nextSteps = runTransition(work);

            //process any straight through processing, might call run() again recursively
            straightThroughNextStepProcessing(nextSteps, work, isError);
        }

        //Post-run script
        Castor.<Activity, BaseActivity>given(currentActivity).castItTo(BaseActivity.class).thenDo(ba -> {
            scripting.runScript(work, ba.getPostRunScript(), this);
        }).go();
        Castor.<Activity, End>given(currentActivity).castItTo(End.class).thenDo(e -> {
            scripting.runScript(work, e.getPostRunScript(), this);
        }).go();

        return work;

    }

    private T straightThroughNextStepProcessing(List<Activity> nextSteps, T work, boolean isError) {
        for (Activity activity : nextSteps) {
            if (WorkflowImpl.this.isStartEvent(activity)) {
                //do nothing as this is startEvent
                //do nothing as this is startEvent
            } else {
                work.getWorkflowInfo().setWorklist(activity.getId());
                work.getWorkflowInfo().setWorklistUpdateTime(LocalDateTime.now());
                if (activity.getClass().equals(End.class)) {
                    //we reach the end, conclude
                    work.setStatus(Status.DONE);
                    work = run(work, isError); //for post run script exec
                    return work;
                } else if (activity.getClass().equals(ServiceActivity.class)) {
                    String script = ((ServiceActivity) activity).getScript();
                    scripting.runScript(work, script, this);
                    work = run(work, isError);
                    return work;
                } else if (activity.getClass().equals(XorActivity.class)) {
                    work = run(work, isError);
                    return work;
                } else {
                    return work; //<-- this is ok since nextStep will not contain more than 1 activity at one time
                }
            }
        }
        return work;
    }

    private List<Activity> runTransition(T work) {
        final String tenant = (String) VaadinSession.getCurrent().getSession().getAttribute("TENANT");
        final String userIdentifier = (String) VaadinSession.getCurrent().getSession().getAttribute("USER_IDENTIFIER");
        List<Activity> nextSteps = new ArrayList<>();

        String worklist = work.getWorkflowInfo().getWorklist();

        if (isStartEvent(worklist)) {//just being created
            StartEvent start = (StartEvent) getActivities().get(work.getWorkflowInfo().getStartEventId());

            if (start.getSupervisoryApprovalHierarchy().size() != 0) {//if need supervisor, stay in the same activity first
                handleSupervisorApproval(work,
                        tenant,
                        (Activity) ((BaseActivity) start).getNext(),
                        start,
                        nextSteps,
                        userIdentifier,
                        start.getSupervisoryApprovalHierarchy());

            } else {
//                String nextId = start.getNext();
//                nextSteps.put(nextId, Boolean.TRUE);
                dealWithNextStep(work,
                        tenant,
                        (Activity) start.getNext(),
                        nextSteps);
                //runSingleTransition(activity, root, tenant, nextSteps, userIdentifier);
            }
        } else {
            for (Map.Entry<String, Activity> e : getActivities().entrySet()) {
                Activity activity = e.getValue();
                if (worklist.equals(activity.getId())) {
                    runSingleTransition(activity, work, tenant, nextSteps, userIdentifier);
                }
            }
        }

        return nextSteps;
    }

    private void runSingleTransition(Activity activity, T work, final String tenant, List<Activity> nextSteps, final String userIdentifier) {
        if (activity.getClass().equals(HumanActivity.class)) {
            if (((HumanActivity) activity).getSupervisoryApprovalHierarchy().size() != 0) {
                handleSupervisorApproval(work,
                        tenant,
                        (Activity) ((BaseActivity) activity).getNext(),
                        activity,
                        nextSteps,
                        userIdentifier,
                        ((HumanActivity) activity).getSupervisoryApprovalHierarchy());
            } else {

                dealWithNextStep(work,
                        tenant,
                        (Activity) ((BaseActivity) activity).getNext(),
                        nextSteps);
            }
        } else if (activity.getClass().equals(ServiceActivity.class)) {
            dealWithNextStep(work,
                    tenant,
                    (Activity) ((ServiceActivity) activity).getNext(),
                    nextSteps);
        } else if (activity.getClass().equals(XorActivity.class)) {
            //see which condition triggers and follow that branch
            boolean conditionTriggered = false;
            for (var branch : ((XorActivity) activity).getBranch()) {
                Boolean result = expr.evaluate(branch.getCondition(), work);
                if (result == true) {
                    conditionTriggered = true;
                    nextSteps.add((Activity) branch.getNext());
                    break;
                }
            }
            if (conditionTriggered == false) {//if no condition triggered, the default branch is executed
                nextSteps.add((Activity) ((XorActivity) activity).getByDefault().getNext());
            }
        } else if (activity.getClass().equals(XorUnanimousApprovalActivity.class)) {

            XorUnanimousApprovalActivity approvalActivity = (XorUnanimousApprovalActivity) activity;
            //determine if there is enough approval
            Map<Boolean, List<Approval>> approvedWork = work
                    .getApprovals()
                    .stream()
                    .filter(x -> x.getApproved() != null)
                    .collect(Collectors.groupingBy(Approval::getApproved));

            int state = 0;
            //state 0 : unanimous approval
            //state 1 : not enough vote
            //state 2 : at least 1 disapproval
            if (approvedWork.containsKey(Boolean.FALSE)) { //if one person voted to disapproved, then the whole thing is disapproved
                state = 2;
            } else {
                if (approvedWork.get(Boolean.TRUE).size() >= work.getApprovals().size()) { //unanimous approval
                    state = 0;
                } else {//not everyone voted yet
                    state = 1;
                }
            }

            if (state == 0) { //we have a unanimous approval
                archiveApprovals(work);
                //go to approved branch
                dealWithNextStep(work,
                        tenant,
                        //(Activity) ((XorUnanimousApprovalActivity) activity).getApprovedBranch(),
                        this.evaluateBranchesForNextActivty(
                                approvalActivity::getOnApproved,
                                approvalActivity::getByDefault,
                                work),
                        nextSteps);
            } else if (state == 2) {//at least one disapproval
                archiveApprovals(work);
                //go to default branch
                dealWithNextStep(work,
                        tenant,
                        //(Activity) ((XorUnanimousApprovalActivity) activity).getDefaultBranch(),
                        this.evaluateBranchesForNextActivty(
                                approvalActivity::getOnRejected,
                                approvalActivity::getByDefault,
                                work),
                        nextSteps);

            } else {
                //don't move to next step but takeout the current owners (i.e. the approvers) so that he doesn't see it in his ownership any more
                work.getWorkflowInfo().getOwners().remove(userIdentifier);
            }
            //dealWithNextStep(root, tenant, getActivities().get(activity.getNext()), nextSteps, container);
        } else if (activity.getClass().equals(XorAtleastOneApprovalActivity.class)) {
            XorAtleastOneApprovalActivity approvalActivity = (XorAtleastOneApprovalActivity) activity;

            //determine if there is enough approval
            Map<Boolean, List<Approval>> approvedWork = work
                    .getApprovals()
                    .stream().filter(x -> x.getApproved() != null)
                    .collect(Collectors.groupingBy(Approval::getApproved));
            int state = 0;
            //state 0 : unanimous disapproval
            //state 1 : not enough vote
            //state 2 : at least 1 approval

            if (!approvedWork.containsKey(Boolean.FALSE)) { //we don't even have a disapproval
                if (approvedWork.containsKey(Boolean.TRUE)) { //if we have at least 1 approval
                    state = 2;
                } else { //no approval and no disapproval=> no one voted yet
                    state = 1;
                }
            } else {
                if (approvedWork.get(Boolean.FALSE).size() >= work.getApprovals().size()) { //unanimous disapproval
                    state = 0;
                } else {//there are some voted to approved
                    state = 1;
                }

            }

            if (state == 0) { //we have a unanimous dis-approval
                archiveApprovals(work);
                //go to default branch
                dealWithNextStep(work,
                        tenant,
                        //(Activity) ((XorAtleastOneApprovalActivity) activity).getDefaultBranch(),
                        this.evaluateBranchesForNextActivty(
                                approvalActivity::getOnRejected,
                                approvalActivity::getByDefault,
                                work),
                        nextSteps);
            } else if (state == 2) {//at least one approval
                archiveApprovals(work);
                //go to approved branch
                dealWithNextStep(work,
                        tenant,
                        //(Activity) ((XorAtleastOneApprovalActivity) activity).getApprovedBranch(),
                        this.evaluateBranchesForNextActivty(
                                approvalActivity::getOnApproved,
                                approvalActivity::getByDefault,
                                work),
                        nextSteps);
            } else {
                //don't move to next step but takeout the current owner so that he doesn't see it in his ownership any more
                work.getWorkflowInfo().getOwners().remove(userIdentifier);
            }
            //dealWithNextStep(root, tenant, getActivities().get(activity.getNext()), nextSteps, container);
        } else if (activity.getClass().equals(XorMajorityApprovalActivity.class)) {
            int countApprove = 0;
            int countDisapprove = 0;
            XorMajorityApprovalActivity approvalActivity = (XorMajorityApprovalActivity) activity;

            for (Approval approval : work.getApprovals()) {
                if (approval.getApproved() != null) {
                    if (approval.getApproved().equals(Boolean.TRUE)) {
                        countApprove++;
                    } else {
                        countDisapprove++;
                    }
                }
            }

            double majorityVote = work.getApprovals().size() / 2;

            if (countApprove > majorityVote) { //must be strictly bigger than since >= could mean a tie
                archiveApprovals(work);
                //go to approved branch
                dealWithNextStep(work,
                        tenant,
                        //(Activity) ((XorMajorityApprovalActivity) activity).getApprovedBranch(),
                        this.evaluateBranchesForNextActivty(
                                approvalActivity::getOnApproved,
                                approvalActivity::getByDefault,
                                work),
                        nextSteps);
            } else if (countDisapprove > majorityVote) {
                archiveApprovals(work);
                //go to default branch
                dealWithNextStep(work,
                        tenant,
                        //(Activity) ((XorMajorityApprovalActivity) activity).getDefaultBranch(),
                        this.evaluateBranchesForNextActivty(
                                approvalActivity::getOnRejected,
                                approvalActivity::getByDefault,
                                work),
                        nextSteps);
            } else if (countDisapprove == countApprove
                    && countDisapprove + countApprove == work.getApprovals().size()) { //everyone voted and its a tie
                archiveApprovals(work);
                //go to default branch
                dealWithNextStep(work,
                        tenant,
                        (Activity) ((XorMajorityApprovalActivity) activity).getOnTieBreaker().getNext(),
                        nextSteps);
            } else {
                //don't move to next step but takeout the current owner so that he doesn't see it in his ownership any more
                work.getWorkflowInfo().getOwners().remove(userIdentifier);
            }

        }
    }

    private void handleSupervisorApproval(final T root,
            final String tenant,
            final Activity next,
            final Activity activity,
            final List<Activity> nextSteps,
            final String userIdentifier,
            final List<String> supervisorHierarchy) {
        if (isSupervisorApprovalNeeded.compute(Optional.of(root))) { //this was sent for approval before
            if (Boolean.TRUE.equals(root.getApprovals().iterator().next().getApproved())) { //approved
                //see which level is the approval on
                String currentApprLevel = root.getSupervisorApprovalLevel();
                int indexOfNextApprLevel = getArrayIndexOfValue(supervisorHierarchy, currentApprLevel) + 1;

                //if we are at the end of the hierarchy
                if (indexOfNextApprLevel == supervisorHierarchy.size()) {
                    root.setSupervisorApprovalSeeker(null);
                    root.setSupervisorApprovalLevel(null);
                    archiveApprovals(root);
                    dealWithNextStep(root, tenant, next, nextSteps);
                } else {//if we are still not at the end
                    //find next role
                    String nextRole = supervisorHierarchy.get(indexOfNextApprLevel);
                    if (approverLookups != null) {
                        approverLookups.stream().filter(a -> {
                            Qualifier qualifier = (Qualifier) a.getClass().getAnnotation(Qualifier.class);
                            if (qualifier == null) {
                                return Boolean.FALSE;
                            }
                            return nextRole.equals(qualifier.value());
                        }).findAny().ifPresent(approverLookup -> {
                            approverLookup.lookupApprover((T) root, root.getSupervisorApprovalSeeker())
                                    .ifPresent(approver -> {
                                        root.getWorkflowInfo().getOwners().clear();
                                        if (approver.getUserIdentifier() != null) {
                                            root.getWorkflowInfo().getOwners().add(approver.getUserIdentifier());
                                        } else {
                                            root.getWorkflowInfo().getOwners().add(approver.getLoginName());
                                        }
                                        root.setSupervisorApprovalLevel(nextRole);

                                        //archive first
                                        archiveApprovals(root);
                                        //then add the new approver
                                        loadUserIntoApprovalList(approver.getLoginName(), activity, tenant, root);//for apperovals, we log the current activity for supervisor approval
                                        nextSteps.add(activity); //if need supervisor, stay in the same activity first

                                    });

                        });
                    }

                }
            } else {//not approved. Reassign back to original user
                root.getWorkflowInfo().getOwners().clear();
                root.getWorkflowInfo().getOwners().add(root.getSupervisorApprovalSeeker());

                archiveApprovals(root);
                root.setSupervisorApprovalLevel(null);
                root.setSupervisorApprovalSeeker(null);
                nextSteps.add(activity);
            }
        } else { //seeking new approval
            root.setSupervisorApprovalSeeker(userIdentifier);

            String nextRole = supervisorHierarchy.get(0);
            if (approverLookups != null) {
                approverLookups.stream().filter(a -> {
                    Qualifier qualifier = (Qualifier) a.getClass().getAnnotation(Qualifier.class);
                    if (qualifier == null) {
                        return Boolean.FALSE;
                    }
                    return nextRole.equals(qualifier.value());
                }).findAny().ifPresent(approverLookup -> {
                    approverLookup.lookupApprover((T) root, userIdentifier).ifPresent(approver -> {
                        root.getWorkflowInfo().getOwners().clear();
                        if (approver.getUserIdentifier() != null) {
                            root.getWorkflowInfo().getOwners().add(approver.getUserIdentifier());
                        } else {
                            root.getWorkflowInfo().getOwners().add(approver.getLoginName());
                        }
                        root.setSupervisorApprovalLevel(nextRole);

                        //archive first
                        archiveApprovals(root);

                        //then add the new approver
                        loadUserIntoApprovalList(approver.getLoginName(), activity, tenant, root); //same logic as bbefore. we log current activity instead of next for supervisor approval
                        nextSteps.add(activity); //if need supervisor, stay in the same activity first
                    });
                });
            }

        }
    }

    private void dealWithNextStep(T work, String tenant, Activity nextActivity, List<Activity> nextSteps) {

        work.getWorkflowInfo().getOwners().clear(); //so that the next folks can pick it up

        work.setSupervisorApprovalSeeker(null);//nullify the approval seeker too
        dao.save(work);
        //only current activity ids in wait states
        if (nextActivity == null) { //nextActivity==END
            nextSteps.add(nextActivity);
        }// else if (nextActivity.getClass().equals(XorActivity.class)) {  // sand next activity is xor
        //}
        else if (nextActivity.getClass().equals(XorUnanimousApprovalActivity.class)) {
            loadUsersIntoApprovalList(((XorUnanimousApprovalActivity) nextActivity).getHandledBy(), nextActivity, tenant, work);
            nextSteps.add(nextActivity);
        } else if (nextActivity.getClass().equals(XorAtleastOneApprovalActivity.class)) {
            loadUsersIntoApprovalList(((XorAtleastOneApprovalActivity) nextActivity).getHandledBy(), nextActivity, tenant, work);
            nextSteps.add(nextActivity);
        } else if (nextActivity.getClass().equals(XorMajorityApprovalActivity.class)) {
            loadUsersIntoApprovalList(((XorMajorityApprovalActivity) nextActivity).getHandledBy(), nextActivity, tenant, work);
            nextSteps.add(nextActivity);
        } else if (nextActivity.getClass().equals(HumanActivity.class)) { //nextActivity.getType=="human" OR nextActivity.getType=="service"
            nextSteps.add(nextActivity); //if the next step is just another wait state, make it active
        } else { //service
            nextSteps.add(nextActivity);
        }
    }

    private void loadUserIntoApprovalList(String loginName,
            Activity nextActivity,
            final String tenant, T work) {
        UserProfile user = userRetrievalService.getUserByLoginName(loginName);
        Set<Approval> approvals = new HashSet<>();

        //work.archiveApproval();
        //dao.save(work);
//        System.out.println("=======Element id:"+work.getId()+"==============");
//        System.out.println("   Approvals:");
//        for (Approval a:work.getApprovals()){
//             System.out.println("   approval id:"+a.toString());
//        }
//        archiveApprovals(work);
        createApprovalAndAssociateToWork(tenant, nextActivity, work, user, approvals);
        work.setApprovals(approvals);

    }

    private void loadUsersIntoApprovalList(String role,
            Activity nextActivity,
            final String tenant,
            T work) {
        List<UserProfile> users = userRetrievalService.getUsersByRole(role);
        Set<Approval> approvals = new HashSet<>();

        //work.archiveApproval();
        //dao.save(work);
//        System.out.println("=======Element id:"+work.getId()+"==============");
//        System.out.println("   Approvals:");
//        for (Approval a:work.getApprovals()){
//             System.out.println("   approval id:"+a.toString());
//        }
//        archiveApprovals(work);
        dao.saveAndAssociate(work.getApprovals(),
                work,
                "historicalApprovals",
                w -> ((WorkElement) w).getApprovals().clear());
        for (UserProfile user : users) {
            createApprovalAndAssociateToWork(tenant, nextActivity, work, user, approvals);
        }
        work.setApprovals(approvals);

    }

    private void archiveApprovals(T work) {
//        System.out.println("=======Element id:"+work.getId()+"==============");
//        System.out.println("   Approvals:");
//        for (Approval a:work.getApprovals()){
//             System.out.println("   approval id:"+a.toString());
//        }
        Set hset = dao.saveAndAssociate(work.getApprovals(),
                work, "historicalApprovals",
                w -> ((WorkElement) w).getApprovals().clear());
//        for (var h:hset){
//            System.out.println("    hist approval id:"+((Dual)h).getSecond().toString());
//        }
    }

    private void createApprovalAndAssociateToWork(final String tenant,
            Activity nextActivity,
            T work,
            UserProfile user,
            Set<Approval> approvals) {

        Optional<String> parentEnumPath = Castor.<T, Element>given(work)
                .castItTo(Element.class)
                .thenDo(w -> {
                    return w.getEnumPath();
                }).go();
        Optional<Approval> oapproval = dao.createAndSave(Approval.class,
                Optional.of(tenant),
                parentEnumPath,
                Optional.empty(),
                Optional.empty(),
                Status.IN_PROGRESS,
                work.getCreator());

        if (user.getEnabled()) {

            oapproval.ifPresent(approval -> {
                mapUserToApproval(approval, user, tenant);
                approval.getWorkflowInfo().setWorklist(((BaseActivity) nextActivity).getId());
                if (work.getWorkflowInfo().getStartEventDescription() != null) {
                    approval.getWorkflowInfo().setStartEventDescription(work.getWorkflowInfo().getStartEventDescription());
                }
                if (work.getWorkflowInfo().getStartEventId() != null) {
                    approval.getWorkflowInfo().setStartEventId(work.getWorkflowInfo().getStartEventId());
                }
                dao.saveAndAssociate(approval, work, "approvals").ifPresent(d -> {
                    Approval savedApproval = (Approval) ((Dual) d).getSecond();
                    approvals.add(savedApproval);

                    //dao.saveAndAssociate(savedApproval, work, "historicalApprovals"); <--move this to ApprovalRenderer beforeSaveCallback/beforeSubmitCallback
                });
                userIdentifierLookup.lookup(user).ifPresent(userId -> {
                    work.getWorkflowInfo().getOwners().add(userId);
                });

            });

        }
    }

    private void mapUserToApproval(Approval approval, UserProfile user, final String tenant) {
        userNameLookup.lookup(user).ifPresent(userIdentifier -> {
            approval.setUsername(userIdentifier);
        });
        approval.setTenant(tenant);
    }

    /**
     * @return the bizProcess
     */
    public BizProcess getBizProcess() {
        return bizProcess;
    }

    /**
     * @param bizProcess the bizProcess to set
     */
    public void setBizProcess(BizProcess bizProcess) {
        this.bizProcess = bizProcess;
    }

    /**
     * @return the activities
     */
    public Map<String, Activity> getActivities() {
        return activities;
    }

    public Boolean isStartEvent(Activity activity) {
        return bizProcess.getStartEvents().stream().anyMatch(se -> se.getId().equals(activity.getId()));
    }

    public Boolean isStartEvent(String activityId) {
        return bizProcess.getStartEvents().stream().anyMatch(se -> se.getId().equals(activityId));
    }

    public Boolean isActivityAccessibleByRoles(String activityid, Set<String> inroles) {
        return isActivityAccessibleByRoles(activities.get(activityid), inroles);
    }

    public Boolean isActivityAccessibleByRoles(Activity activity, Set<String> inroles) {

        if (activity == null || activity.getClass().equals(StartEvent.class)) {
            return whoCanStart().stream().anyMatch(inroles::contains);
        } else if (HumanActivity.class.isAssignableFrom(activity.getClass())) {
            return inroles.contains(((HumanActivity) activity).getHandledBy());
        } else {
            return Boolean.FALSE;
        }

    }

    @Override
    public Set<String> whoCanStart() {
        return new HashSet<String>(bizProcess
                .getStartEvents()
                .stream()
                .map(StartEvent::getCanBeStartedBy)
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));
    }

    @Override
    public Set<String> whoCanStart(T currentActivity) {
        String startEventId = currentActivity.getWorkflowInfo().getStartEventId();
        StartEvent startEvent = (StartEvent) activities.get(startEventId);
        return new HashSet<String>(startEvent.getCanBeStartedBy());
    }

    @Override
    public Set<StartEvent> getStartEvents() {
        return new HashSet<StartEvent>(bizProcess.getStartEvents());
    }

    private int getArrayIndexOfValue(List<String> array, String value) {
        int f = 0;
        for (String v : array) {
            if (v.equals(value)) {
                return f;
            }
            f++;
        }
        return f;
    }

    @Override
    public Boolean isActivitySLAExpired(String activity, LocalDateTime workSLAUpdateTime) {
        if (activity == null) { //start event
            return Boolean.FALSE;
        }
        if (activities.get(activity) == null) { //cater for updating workflow id in workflow.xml but data is old
            return Boolean.FALSE;
        }
        if (HumanActivity.class.isAssignableFrom(activities.get(activity).getClass())) {
            return isActivitySLAExpired((HumanActivity) activities.get(activity), workSLAUpdateTime);
        } else {
            return Boolean.FALSE;
        }
    }

    @Override
    public Boolean isActivitySLAExpired(HumanActivity activity, LocalDateTime workSLAUpdateTime) {
        if (activity.getSlaInHours() != null && workSLAUpdateTime != null) {
            Duration diff = Duration.between(workSLAUpdateTime, LocalDateTime.now());
            return (diff.getSeconds() / 3600) > activity.getSlaInHours();
        } else {
            return Boolean.FALSE;
        }
    }

    private Activity evaluateBranchesForNextActivty(Supplier<List<? extends ConditionalBranch>> getBranches, Supplier<DefaultBranch> getDefaultBranch, T root) {

        //state=0: no activity has condition == true
        //state=1: 1 activity has condition == true
        //state=2: multiple activities has condition == true (in this case, we have no guarantee which branch is executed)
        Activity nextStep = null;
        for (var branch : getBranches.get()) {
            Boolean result = expr.evaluate(branch.getCondition(), root);
            if (result == true) {
                nextStep = (Activity) branch.getNext(); //deal with state=1
                break; //deal with state=2
            }

        }
        //deal with state=0
        if (nextStep == null) {//if no condition triggered, the default branch is executed
            nextStep = (Activity) (getDefaultBranch.get()).getNext();
        }
        return nextStep;
    }
}
