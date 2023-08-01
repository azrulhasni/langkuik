/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.rule;

import com.azrul.langkuik.framework.entity.WebEntity;
import com.azrul.langkuik.framework.entity.WebEntityType;
import com.azrul.langkuik.framework.user.UserProfile;
import com.azrul.langkuik.framework.workflow.Workflow;
import com.vaadin.flow.server.VaadinSession;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author azrul
 */
@Service
@Qualifier("CanCreateNewRule")
public class CanCreateNewRule implements EntityRule {

    @Autowired
    Workflow workflow;
    
    @Override
    public Boolean compute(Optional<WebEntity> oWebEntity, Object... otherParams) {
        
//        UserProfile user = (UserProfile) VaadinSession.getCurrent().getSession().getAttribute("USER");
        Set<String> roles = (Set<String>) VaadinSession.getCurrent().getSession().getAttribute("ROLES");
        if (roles==null){
            return false;
        }
        if (roles.isEmpty()){
            return false;
        }
        return oWebEntity.map(we -> {
            if (we.type()==WebEntityType.REF){
                
                return (roles.contains("REF_ADMIN"));
            }else{
                List<String> effectiveRoles = ((Set<String>) workflow.whoCanStart()).stream().filter(
                        x -> {
                            if (roles!=null){ 
                                 return roles.contains(x);
                            }
                            else{
                                return false;
                            }
                        }
                ).collect(Collectors.toList());
                return !effectiveRoles.isEmpty();
            }
        }).orElse(Boolean.FALSE);
    }
    
}
