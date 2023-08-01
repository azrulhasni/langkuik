/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.rule;

import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.framework.entity.WebEntity;
import com.azrul.langkuik.framework.entity.WebEntityType;
import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.standard.Castor;
import com.azrul.langkuik.framework.workflow.Workflow;
import com.azrul.langkuik.framework.workflow.WorkflowsContainer;
import com.vaadin.flow.server.VaadinSession;
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
@Qualifier("IsWorkingOnRef")
public class IsWorkingOnRef<T extends Element> implements ClassRule<T> {

//    @Autowired
//    WorkflowsContainer workflows;

    @Override
    public Boolean compute(Class<T> tclass, Object... otherParams) {
        final Set<String> roles = (Set<String>) VaadinSession.getCurrent().getSession().getAttribute("ROLES");

        WebEntity we = tclass.getAnnotation(WebEntity.class);
        if (we == null) {
            return Boolean.FALSE;
        }
        if (WebEntityType.REF.equals(we.type())) {
            return roles.contains("REF_ADMIN");
        } else {
            return Boolean.FALSE;
        }

    }

}
