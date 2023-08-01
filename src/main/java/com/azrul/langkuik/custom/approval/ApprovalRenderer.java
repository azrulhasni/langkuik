/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.custom.approval;

import com.azrul.langkuik.custom.EventToOpenOtherComponent;
import com.azrul.langkuik.framework.standard.Dual;
import com.azrul.langkuik.framework.dao.filter.AndFilters;
import com.azrul.langkuik.framework.dao.filter.FilterRelation;
import com.azrul.langkuik.framework.dao.filter.QueryFilter;
import com.azrul.langkuik.framework.relationship.RelationManagerFactory;
import com.azrul.langkuik.framework.relationship.RelationMemento;
import com.azrul.langkuik.framework.rule.PojoRule;
import com.azrul.langkuik.framework.workflow.Workflow;
import com.azrul.langkuik.views.pojo.PojoWindowFactory;
import com.azrul.langkuik.views.pojo.PojoTableFactory;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import javax.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import com.azrul.langkuik.framework.entity.Status;
import com.azrul.langkuik.framework.entity.WebEntity;
import com.azrul.langkuik.framework.entity.WebEntityType;
import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.views.pojo.PojoViewState;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.select.Select;
import java.util.ArrayList;
import java.util.List;
import com.azrul.langkuik.custom.subform.SubFormRenderer;
import org.apache.commons.collections4.MultiValuedMap;

/**
 *
 * @author azrul
 */
public class ApprovalRenderer
        <P extends WorkElement, R extends WorkElement> 
        implements SubFormRenderer<P, R, ApprovalLayout> {

    private final int COMMENTS_ORDER_ID=7;
    
    @Autowired
    private ValidatorFactory validatorFactory;

    @Autowired
    private PojoTableFactory pojoTableFactory;

    @Autowired
    private PojoWindowFactory pojoDialogFactory;

    @Autowired
    private ApprovalRepo<R, P> approvalRepo;

    @Autowired
    @Qualifier("CanApproveRule")
    private PojoRule canApproveRule;

    @Autowired
    private RelationManagerFactory relationMgrFactory;

    @Autowired
    private Workflow workflow;

    public ApprovalRenderer() {

    }

    //Requester - no need to see approval
    //Approver - All approvers (wl=current wl, rel=historical approvals)
    //Corrector - Approver(s) who rejected this and their input (wl=any, rel=approvals)
    //Approver after correction - My earlier input  (wl=current wl, rel=historical approvals)
    //Creator (wl=any, rel=historical approvals)
    @Override
    public Optional<ApprovalLayout> render(R root,
            P parent,
            String relationName,
            Optional<PojoViewState> oParentState,
            Map<Integer, EventToOpenOtherComponent> eventToOpenOtherComponent,
            Map<String, RelationMemento> relationMementos,
            Consumer<RelationMemento> onCommit,
            Consumer<RelationMemento> onDelete) {

        Select<ApprovalOption> cbApprove = new Select();
        cbApprove.setLabel("This work is hereby");
        cbApprove.setItems(ApprovalOption.getOptions());
        cbApprove.setItemLabelGenerator(ApprovalOption::getDisplay);

        //cbApprove.setEmptySelectionAllowed(true);
        //btnComments.setWidth("20%");
        String worklist = parent.getWorkflowInfo().getWorklist();

        AndFilters onlyFindCurrentWorklistFilter = AndFilters.build(
                QueryFilter.build(new String[]{"workflowInfo", "worklist"}, FilterRelation.EQUAL, worklist));

        ApprovalLayout approvalLayout = new ApprovalLayout(approvalRepo, parent, relationName);

        VerticalLayout approvalCBLayout = new VerticalLayout();

        approvalRepo.getCurrentApproval(parent, relationName).map(currentApproval -> {
            MenuBar menuBar = new MenuBar();
            menuBar.setId("headerBar-Approvals");
            MenuItem btnComments = menuBar.addItem("Comments");

            btnComments.setId("Approval-btnComments");
            approvalCBLayout.add(menuBar);

            cbApprove.setId("cbApprove");

            approvalLayout
                    .setSelectAndValue(cbApprove,
                            ApprovalOption.of(currentApproval.getApproved()));
            
            if (cbApprove.getValue().getValue()==null){
                cbApprove.setInvalid(true);
            }else{
                cbApprove.setInvalid(false);
            }
            

            approvalCBLayout.addAndExpand(cbApprove);
            approvalCBLayout.setAlignItems(FlexComponent.Alignment.BASELINE);

            if (canApproveRule.compute(Optional.of(parent))) {
                cbApprove.setReadOnly(false);
                btnComments.setEnabled(true);
            } else {
                cbApprove.setReadOnly(true);
                btnComments.setEnabled(false);
            }

            displayApprovalsTable(root,
                    parent,
                    approvalLayout,
                    relationMementos,
                    approvalCBLayout,
                    onlyFindCurrentWorklistFilter);

            btnComments.addClickListener(e -> {
                try {
                   
                    eventToOpenOtherComponent.get(COMMENTS_ORDER_ID).getListener().onComponentEvent(e);
                } catch (NullPointerException ex) {

                }
            });

            return approvalLayout;
        }).orElseGet(() -> {
            displayApprovalsTable(root,
                    parent,
                    approvalLayout,
                    relationMementos,
                    approvalCBLayout,
                    onlyFindCurrentWorklistFilter);
            return approvalLayout;
        });

        //approvalLayout.setCbApprove(cbApprove);
        return Optional.of(approvalLayout);

    }

    private void displayApprovalsTable(R root,
            P parent,
            ApprovalLayout approvalLayout,
            Map<String, RelationMemento> relationMementos,
            VerticalLayout approvalCBLayout,
            AndFilters andFilters) {
        final String userIdentifier = (String) VaadinSession.getCurrent().getSession().getAttribute("USER_IDENTIFIER");
        AndFilters af = AndFilters.build(
                QueryFilter.build("status", FilterRelation.NOT_EQUAL, Status.CANCELLED),
                QueryFilter.build("status", FilterRelation.NOT_EQUAL, Status.RETIRED)
        );
        if (!canApproveRule.compute(Optional.of(parent))) {
            if (parent.getCreator().equals(userIdentifier)) { //creator
                createListOfApprovalsTable(
                        root,
                        parent,
                        "approvals",
                        "Approvals",
                        approvalLayout,
                        relationMementos,
                        Optional.empty(),
                        af);
                createListOfApprovalsTable(
                        root,
                        parent,
                        "historicalApprovals",
                        "Historical Approvals",
                        approvalLayout,
                        relationMementos,
                        Optional.empty(),
                        af);
            } else {//corrector
                createListOfApprovalsTable(
                        root,
                        parent,
                        "approvals",
                        "Approvals",
                        approvalLayout,
                        relationMementos,
                        Optional.empty(),
                        af);
                createListOfApprovalsTable(
                        root,
                        parent,
                        "historicalApprovals",
                        "Past approvals",
                        approvalLayout,
                        relationMementos,
                        Optional.empty(),
                        af);
            }
        } else {//approver
            createListOfApprovalsTable(
                    root,
                    parent,
                    "approvals",
                    "Approvals",
                    approvalLayout,
                    relationMementos,
                    Optional.of(approvalCBLayout),
                    af);
            createListOfApprovalsTable(
                    root,
                    parent,
                    "historicalApprovals",
                    "Past approvals",
                    approvalLayout,
                    relationMementos,
                    Optional.empty(),
                    af);
        }
    }

    private void createListOfApprovalsTable(R root,
            P parent,
            String fieldName,
            String humanReadableRelationName,
            ApprovalLayout approvalLayout,
            Map<String, RelationMemento> relationMementos,
            Optional<HasComponents> approvalCBLayout,
            AndFilters andFilters) {
        List<Dual<String[], Boolean>> sortField = new ArrayList<>();
        sortField.add(new Dual(new String[]{"id"}, false));

        //if memento is missing, chances are, 
        //we are trying to render historicalApprovals which is not a WebRelation
        //So, create a relationMemento for it but do not store it - since no validation needed for it
        RelationMemento relationMemento = relationMementos.getOrDefault(fieldName,
                pojoTableFactory.createRelationMemento(
                        Optional.empty(),
                        parent,
                        fieldName,
                        Optional.of(andFilters),
                        200,
                        Optional.empty(),//no limit to people approving
                        Optional.empty(),//no limit to people approving 
                        humanReadableRelationName,
                        sortField));

        //relationMementos.put(fieldName, relationMemento); 
        pojoTableFactory.createTable(
                approvalLayout,
                relationMemento,
                approvalCBLayout, e -> { //create a PojoView containing the clicked item on the table
                    if (e.getClickCount() == 2) {
                        Approval approval = (Approval) e.getItem();
                        showApprovalInfo(root, approval);
                    }
                },
                null, null, null, null);
    }

    private void showApprovalInfo(R root, Approval approval) {
        pojoDialogFactory.createReadOnlyPojoDialog(root, approval);
    }

    //only root element can have approvals
    @Override
    public Boolean preCondition(R root, P parent, String relationName, Optional<PojoViewState> oParentState) {
        WebEntity we = (WebEntity) parent.getClass().getAnnotation(WebEntity.class);
        if (we == null) {
            return Boolean.FALSE;
        } else {
            return WorkElement.class.isAssignableFrom(parent.getClass()) && WebEntityType.ROOT.equals(we.type());
        }

    }

}
