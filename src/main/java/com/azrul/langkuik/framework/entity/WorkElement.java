/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.entity;

import com.azrul.langkuik.custom.approval.Approval;
import com.azrul.langkuik.custom.approval.ApprovalRenderer;
import com.azrul.langkuik.custom.comment.CommentsContainer;
import com.azrul.langkuik.custom.comment.CommentsRenderer;
import com.azrul.langkuik.custom.onetoone.OneToOneRenderer;
import com.azrul.langkuik.custom.priority.Priority;
import com.azrul.langkuik.custom.priority.PriorityField;
import com.azrul.langkuik.framework.field.WebField;
import com.azrul.langkuik.framework.relationship.WebRelation;
import com.azrul.langkuik.framework.workflow.WorkflowInfo;
import com.azrul.langkuik.framework.workflow.WorkflowInfoRenderer;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import org.hibernate.envers.Audited;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Searchable;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import com.azrul.langkuik.custom.subform.SubForm;
import com.azrul.langkuik.custom.subform.SubFormType;

/**
 *
 * @author azrul
 */
//@Entity
@MappedSuperclass
public abstract class WorkElement extends Element{
    
     @Audited
    @Column(name = "PRIORITY")
    @GenericField(projectable = Projectable.YES, sortable = Sortable.YES, searchable = Searchable.NO)
    @GenericField(name = "Priority", searchable = Searchable.YES)
    @PriorityField
    @WebField(displayName = "Priority",
            order = 2,
            visibleInForm = "false",
            visibleInTable = "false"
    )
    protected Priority priority = Priority.NONE;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commentsContainer_id", referencedColumnName = "id")
    @WebRelation(name = "Comments",
            order = 7, //<-- If we change this, make sure to change :
                        //   - ApprrovalRenderer.COMMENTS_ORDER_ID
                        //  = 7-btnCloseForm in Tests
            visibleInForm = "current.isRoot()",
            asSubForm = @SubForm( //force component to render even if we are not putting this in a Tab
                    subFormRenderer = CommentsRenderer.class,
                    active = "true",
                    type = SubFormType.AS_POPUP
            ),
            maxCount = 0)
    protected CommentsContainer commentsContainer;


    @Audited //only one of the 2 approvals fields (approvals and historicalApprovals) can be audited
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL) 
    @JoinColumn(name = "work_id", referencedColumnName = "id")
    @WebRelation(name = "Approvals",
            order = 8,  //<-- If we change this, make sure to change :
                        //  = 8-btnCloseForm in Tests
            visibleInForm = "current.isRoot()", 
            asSubForm = @SubForm( //force component to render even if we are not putting this in a Tab
                    subFormRenderer = ApprovalRenderer.class,
                    active = "true",
                    type = SubFormType.AS_POPUP
            ))
    protected Set<Approval> approvals;


    @Audited
    @Embedded
    @IndexedEmbedded
    @OrderBy("worklist")
    @WebRelation(name = "Workflow-info",
            order = 9,
            visibleInForm = "current.isRoot()",
            visibleInTable="false",
            customComponentInTable = OneToOneRenderer.class,
            asSubForm = @SubForm( //force component to render even if we are not putting this in a Tab
                    subFormRenderer = WorkflowInfoRenderer.class,
                    active = "true",
                    type = SubFormType.AS_POPUP
            ))
    protected WorkflowInfo workflowInfo;
    
    
    @Audited
    @Column(name = "SUPERVISOR_APPROVAL_SEEKER")
    protected String supervisorApprovalSeeker;

    @Audited
    @Column(name = "SUPERVISOR_APPROVAL_LEVEL")
    protected String supervisorApprovalLevel;
    
    @OneToMany(fetch = FetchType.LAZY) //do not cascade. Will create problem due to 2 fields pointing to the same type i.e. Approvals
    @JoinColumn(name = "hist_work_id", referencedColumnName = "id", nullable = true)
    protected Set<Approval> historicalApprovals;
    
   
    
     /**
     * @return the supervisorApprovalSeeker
     */
    public String getSupervisorApprovalSeeker() {
        return supervisorApprovalSeeker;
    }

    /**
     * @param supervisorApprovalSeeker the supervisorApprovalSeeker to set
     */
    public void setSupervisorApprovalSeeker(String supervisorApprovalSeeker) {
        this.supervisorApprovalSeeker = supervisorApprovalSeeker;
    }

    /**
     * @return the supervisorApprovalLevel
     */
    public String getSupervisorApprovalLevel() {
        return supervisorApprovalLevel;
    }

    /**
     * @param supervisorApprovalLevel the supervisorApprovalLevel to set
     */
    public void setSupervisorApprovalLevel(String supervisorApprovalLevel) {
        this.supervisorApprovalLevel = supervisorApprovalLevel;
    }

    /**
     * @return the approvals
     */
    public Set<Approval> getApprovals() {
        if (approvals == null) {
            approvals = new HashSet<>();
        }
        return approvals;
    }

    /**
     * @param approvals the approvals to set
     */
    public void setApprovals(Set<Approval> approvals) {
        this.approvals = approvals;
    }


//    public void archiveApproval() {
//        getApprovals().clear();
//    }

    public void replaceApproval(Approval newApproval) {
        getApprovals().removeIf(e -> e.getId().equals(newApproval.getId()));
        getApprovals().add(newApproval);
    }

    /**
     * @return the historicalApprovals
     */
    public Set<Approval> getHistoricalApprovals() {
        if (historicalApprovals==null){
            historicalApprovals=new HashSet<>();
        }
        return historicalApprovals;
    }

    /**
     * @param historicalApprovals the historicalApprovals to set
     */
    public void setHistoricalApprovals(Set<Approval> historicalApprovals) {
        this.historicalApprovals = historicalApprovals;
    }
    
   

 

    /**
     * @return the comments
     */
    public CommentsContainer getCommentsContainer() {
        return commentsContainer;
    }

    /**
     * @param comments the comments to set
     */
    public void setCommentsContainer(CommentsContainer commentsContainer) {
        this.commentsContainer = commentsContainer;
    }

    /**
     * @return the workflowInfo
     */
    public WorkflowInfo getWorkflowInfo() {
        if (workflowInfo==null){
            workflowInfo = new WorkflowInfo();
        }
        return workflowInfo;
    }

    /**
     * @param workflowInfo the workflowInfo to set
     */
    public void setWorkflowInfo(WorkflowInfo workflowInfo) {
        this.workflowInfo = workflowInfo;
    }

    /**
     * @return the priority
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    

}
