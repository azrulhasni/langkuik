/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.rule;

import com.azrul.langkuik.custom.approval.Approval;
import com.azrul.langkuik.framework.entity.WebEntity;
import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.standard.Castor;
import com.azrul.langkuik.framework.workflow.Workflow;
import com.vaadin.flow.server.VaadinSession;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author azrul
 */
@Service
@Qualifier("IsApprovalNeeded")
public class IsApprovalNeeded implements PojoRule {
    
    @Autowired
    Workflow workflow;
    
    @Autowired
    @Qualifier("IsSupervisorApprovalNeeded")
    PojoRule isSupervisorApprovalNeeded;

    @Override
    public <T extends Element> Boolean compute(Optional<T> owork, Object... otherParams) {
        return (Boolean) owork.map(work->{
            return Castor.<T, WorkElement>given(work)
                    .castItTo(WorkElement.class)
                    .thenDo(e -> {
           
                        
            return workflow.isApprovalActivity(e.getWorkflowInfo().getWorklist()) || //this worklist/activity is an approval activity
                   (
                        isSupervisorApprovalNeeded.compute(owork) && 
                        isMyApprovalNeeded(e)
                   ); //there is a need to get supervisor's approval and my approval is needed
            }).go().orElse(Boolean.FALSE);
        }).orElse(Boolean.FALSE);
    }
    
    private Boolean isMyApprovalNeeded(WorkElement we){
        final String username = (String) VaadinSession.getCurrent().getSession().getAttribute("USER_IDENTIFIER");
        for (Approval approval: we.getApprovals()){
            if (username.equals(approval.getUsername())){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
    
}
