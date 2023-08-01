/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.rule;

import com.azrul.langkuik.framework.entity.WebEntity;
import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.standard.Dual;
import com.azrul.langkuik.framework.workflow.Workflow;
import com.vaadin.flow.server.VaadinSession;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author azrul
 */
@Service
@Qualifier("IsWorkflowUser")
public class IsWorkflowUser<T extends WorkElement> implements WorkflowRule<T> {

    @Override
    public Boolean compute(Workflow<T> workflow, Optional<WebEntity> we, Object... otherParams) {
        Set<String> intersection = new HashSet<>((Set<String>) VaadinSession.getCurrent().getSession().getAttribute("ROLES"));
        Set<String> rolesInWorkflow = workflow.getRoleActivityMap().keySet();
        intersection.retainAll(rolesInWorkflow);
        return !intersection.isEmpty();
    }

}
