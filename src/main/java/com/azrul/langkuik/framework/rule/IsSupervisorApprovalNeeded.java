/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.rule;

import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.standard.Castor;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author azrul
 */
@Service
@Qualifier("IsSupervisorApprovalNeeded")
public class IsSupervisorApprovalNeeded implements PojoRule {
    

    @Override
    public <T extends Element> Boolean compute(Optional<T> owork, Object... otherParams) {
        return (Boolean) owork.map(work->{
             return Castor.<T, WorkElement>given(work)
                    .castItTo(WorkElement.class)
                    .thenDo(e -> {
            //if (WorkElement.class.isAssignableFrom(work.getClass())) {
            return e.getSupervisorApprovalSeeker() != null; //there is a need to get supervisor's approval
            }).go().orElse(Boolean.FALSE);
              
       }).orElse(Boolean.FALSE);
    }
    
}
