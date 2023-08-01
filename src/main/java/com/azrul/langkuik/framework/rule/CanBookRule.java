/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.rule;

import com.azrul.langkuik.framework.entity.Status;
import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.standard.Castor;
import com.azrul.langkuik.framework.workflow.Workflow;
import com.vaadin.flow.server.VaadinSession;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author azrul
 */
@Service
@Qualifier("CanBookRule")
public class CanBookRule implements PojoRule {

    @Autowired
    Workflow workflow;

    @Override
    public <T extends Element> Boolean compute(Optional<T> owork, Object... otherParams) {
        final String userIdentifier = (String) VaadinSession.getCurrent().getSession().getAttribute("USER_IDENTIFIER");

        final Set<String> roles = (Set<String>) VaadinSession.getCurrent().getSession().getAttribute("ROLES");

        return (Boolean) owork.map(work -> {
            return Castor.<T, WorkElement>given(work)
                    .castItTo(WorkElement.class)
                    .thenDo(e -> {
                        return (e.getWorkflowInfo().getOwners().isEmpty())
                                && workflow.isActivityAccessibleByRoles(e.getWorkflowInfo().getWorklist(), roles);
                    }).go().orElse(Boolean.FALSE); 
        }).orElse(Boolean.FALSE);

    }
}
