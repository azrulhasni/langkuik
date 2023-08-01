/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.workflow;

import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.factory.SpringBeanFactory;
import com.azrul.langkuik.framework.rule.IsWorkflowUser;
import com.azrul.langkuik.framework.standard.Dual;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author azrul
 */
public class WorkflowsContainer<T extends WorkElement> {
    
    private  Map<Dual<String, String>, Workflow<T>> workflows;

    /**
     * @return the workflows
     */
    public Collection<Workflow<T>> getWorkflows() {
        Set<Workflow> allowedWorkflows = new HashSet<>();
        if (workflows==null){
            workflows=new HashMap<>();
        }
        return workflows.values();
    }

    /**
     * @param workflows the workflows to set
     */
    public void setWorkflows(Map<Dual<String, String>, Workflow<T>> workflows) {
        this.workflows = workflows;
    }
    
    public Workflow get(Dual<String, String> key){
        return this.workflows.get(key);
    }
   
    
    
}
