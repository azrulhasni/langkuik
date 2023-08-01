/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.workflow;

import com.azrul.langkuik.framework.entity.WebEntity;
import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.rule.WorkflowRule;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 *
 * @author azrul
 */
@Component("workflowUtils")
public class WorkflowUtils {
//    
//    @Autowired
//    @Qualifier("CanCreateNewRule")
//    private WorkflowRule canCreateNewRule;
//    
//    @Autowired
//    @Qualifier("IsWorkflowUser")
//    WorkflowRule isWorkflowUser;
//    
//    public <T extends WorkElement> Set<Workflow<T>> getAccessibleWorkflows(WorkflowsContainer<T> workflows){
//        return (Set<Workflow<T>>) workflows
//                .getWorkflows()
//                .stream()
//                .filter(wf->isWorkflowUser.compute((Workflow)wf,Optional.empty()))
//                .collect(Collectors.toSet());
//    } 
//    
//    public <T extends WorkElement> Set<Workflow<T>> getStartableWorkflows(WorkflowsContainer<T> workflows, Class<T> rootClass){
//        WebEntity we = (WebEntity) rootClass.getAnnotation(WebEntity.class);
//        if (we==null)return Set.of();
//        
//                
//        return (Set<Workflow<T>>) workflows
//                .getWorkflows()
//                .stream()
//                .filter(wf->canCreateNewRule.compute((Workflow)wf,Optional.of(we)))
//                .collect(Collectors.toSet());
//    } 
}
