/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.workflow;

import com.azrul.langkuik.framework.field.WebField;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import org.hibernate.envers.Audited;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import com.azrul.langkuik.framework.user.User;

/**
 *
 * @author azrul
 */
@Embeddable
public class WorkflowInfo{
    public WorkflowInfo(){
        worklist = null;
        owners = new HashSet<String>();
    }
    
    private String startEventId;
    
    
    @Audited
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "OWNERS")
    @GenericField(projectable = Projectable.YES, sortable = Sortable.YES)
    @WebField(displayName = "Owners",
            isReadOnly = true,
            order = 4,
            visibleInForm = "true",
            visibleInTable = "false"
    )
    @User
    private Set<String> owners;
     
    @Audited
    @GenericField(projectable = Projectable.YES, sortable = Sortable.YES)
    @Column(name = "WORKLIST")
    @WebField(displayName = "Worklist",
            isReadOnly = true,
            order = 5,
            visibleInForm = "true",
            visibleInTable = "false"
    )
    private String worklist;
    
    @Audited
    @GenericField(projectable = Projectable.YES, sortable = Sortable.YES)
    @Column(name = "START_EVENT_DESCRIPTION")
    @WebField(displayName = "Start",
            isReadOnly = true,
            order = 6,
            visibleInForm = "true",
            visibleInTable = "false"
    )
    private String startEventDescription;
    
    @Audited
    @Column(name = "WORKLIST_UPDATE_TIME")
    private LocalDateTime worklistUpdateTime;

    

    /**
     * @return the owners
     */
    public Set<String> getOwners() {
        return owners;
    }

    /**
     * @param owners the owners to set
     */
    public void setOwners(Set<String> owners) {
        this.owners = owners;
    }

    /**
     * @return the worklist
     */
    public String getWorklist() {
        return worklist;
    }

    /**
     * @param worklist the worklist to set
     */
    public void setWorklist(String worklist) {
        this.worklist = worklist;
    }

   

    /**
     * @return the worklistUpdateTime
     */
    public LocalDateTime getWorklistUpdateTime() {
        return worklistUpdateTime;
    }

    /**
     * @param worklistUpdateTime the worklistUpdateTime to set
     */
    public void setWorklistUpdateTime(LocalDateTime worklistUpdateTime) {
        this.worklistUpdateTime = worklistUpdateTime;
    }
    
    public String toString(){
        String startEventDesc = startEventDescription!=null?(":"+startEventDescription):"";
        return worklist+startEventDesc;
    }

    /**
     * @return the startActivityId
     */
    public String getStartEventId() {
        return startEventId;
    }

    /**
     * @param startActivityId the startActivityId to set
     */
    public void setStartEventId(String startActivityId) {
        this.startEventId = startActivityId;
    }

    /**
     * @return the startEventDescription
     */
    public String getStartEventDescription() {
        return startEventDescription;
    }

    /**
     * @param startEventDescription the startEventDescription to set
     */
    public void setStartEventDescription(String startEventDescription) {
        this.startEventDescription = startEventDescription;
    }
}
