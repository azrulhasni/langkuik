/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.rule;

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
@Qualifier("CanBookRule")
public class CanUnBookRule implements PojoRule {

    @Autowired
    Workflow workflow;

    @Override
    public <T extends Element> Boolean compute(Optional<T> owork, Object... otherParams) {
        final String userIdentifier = (String) VaadinSession.getCurrent().getSession().getAttribute("USER_IDENTIFIER");

        return (Boolean) owork.map(work -> {
                    return Castor.<T, WorkElement>given(work)
                    .castItTo(WorkElement.class)
                    .thenDo(e -> {
                        return e.getWorkflowInfo().getOwners().contains(userIdentifier);
                    }).go().orElse(Boolean.FALSE);
                } //can only release what I own
        ).orElse(Boolean.FALSE);
    }

}
