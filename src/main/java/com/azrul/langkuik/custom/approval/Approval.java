/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.custom.approval;

import com.azrul.langkuik.custom.onetoone.OneToOneRenderer;
import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.framework.entity.WebEntity;
import com.azrul.langkuik.framework.field.WebField;
import com.azrul.langkuik.framework.field.FieldVisibility;
import com.azrul.langkuik.framework.relationship.WebRelation;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Size;
import org.hibernate.envers.Audited;
//import org.hibernate.search.mapper.pojo.mapping.definition.annotation.DocumentId;
//import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
//import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
//import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import com.azrul.langkuik.framework.user.User;
import com.azrul.langkuik.framework.workflow.WorkflowInfo;
import com.azrul.langkuik.framework.workflow.WorkflowInfoRenderer;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.OrderBy;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import com.azrul.langkuik.custom.subform.SubForm;
import com.azrul.langkuik.custom.subform.SubFormType;

/**
 *
 * @author azrul
 */
@Entity
@DiscriminatorValue("_Approval")
@WebEntity(name = "Approval", fieldVisibility={
        @FieldVisibility(fieldName="workflowInfo", visibleInTable = "true", visibleInForm = "false")
    })
public class Approval extends Element implements Serializable {

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Approval other = (Approval) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    private static final long serialVersionUID = 1L; 

    @Column(name = "USERNAME")
    @Size(max = 30)
    @Audited
    @WebField(displayName="User-name",
            isReadOnly=true,
            order=200,
            visibleInForm = "true",
            visibleInTable = "true")
    @User
    private String username;
    
    @Column(name = "APPROVED")
    @Audited
    @WebField(displayName="Approved",
            isReadOnly=true,
            order=600,
            visibleInForm = "true",
            visibleInTable = "true")
    private Boolean approved;
    
    @Audited
    @Column(name = "APPROVAL_TIMESTAMP")
    @WebField(displayName = "Approval-timestamp",
            isReadOnly = true,
            order = 700,
            visibleInTable = "true",
            visibleInForm = "true"
    )
    private LocalDateTime approvalTimestamp;
    
    @Audited
    @Embedded
    @IndexedEmbedded
    @OrderBy("worklist")
    @WebRelation(name = "Workflow-info",
            order = 8,
            visibleInForm = "current.isRoot()",
            visibleInTable="false",
            customComponentInTable = OneToOneRenderer.class,
            asSubForm = @SubForm( //force component to render even if we are not putting this in a Tab
                    subFormRenderer = WorkflowInfoRenderer.class,
                    active = "true",
                    type = SubFormType.AS_POPUP
            ))
    private WorkflowInfo workflowInfo;

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the approved
     */
    public Boolean getApproved() {
        return approved;
    }

    /**
     * @param approved the approved to set
     */
    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    

    


    /**
     * @return the approvalTimestamp
     */
    public LocalDateTime getApprovalTimestamp() {
        return approvalTimestamp;
    }

    /**
     * @param approvalTimestamp the approvalTimestamp to set
     */
    public void setApprovalTimestamp(LocalDateTime approvalTimestamp) {
        this.approvalTimestamp = approvalTimestamp;
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

}
