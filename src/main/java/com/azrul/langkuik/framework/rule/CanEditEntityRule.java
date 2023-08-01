/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.rule;

import com.azrul.langkuik.framework.entity.WebEntityType;
import com.azrul.langkuik.framework.entity.EntityUtils;
import com.azrul.langkuik.framework.field.FieldUtils;
import com.azrul.langkuik.framework.relationship.RelationUtils;
import com.azrul.langkuik.framework.entity.Status;
import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.standard.Castor;
import com.azrul.langkuik.framework.workflow.Workflow;
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
@Qualifier("CanEditEntityRule")
public class CanEditEntityRule implements PojoRule {

    @Autowired
    Workflow workflow;

    @Autowired
    FieldUtils fieldUtils;

    @Autowired
    EntityUtils entityUtils;

    @Autowired
    RelationUtils relationUtils;

    @Override
    public <T extends Element> Boolean compute(Optional<T> owork, Object... otherParams) {
        final Set<String> roles = (Set<String>) VaadinSession.getCurrent().getSession().getAttribute("ROLES");

        final String username = (String) VaadinSession.getCurrent().getSession().getAttribute("USER_IDENTIFIER");

        return (Boolean) owork.map(work -> {
            return Castor.<T, Element>given(work)
                    .castItTo(Element.class)
                    .thenDo(e -> {
                        if (work.isReference()) {
                            return roles.contains("REF_ADMIN");// && e.getWorkflowInfo().getStatus() != Status.DONE;
                        } else {
                            return (Boolean)Castor.<T, WorkElement>given(work)
                                    .castItTo(WorkElement.class)
                                    .thenDo(we -> {
                                        Boolean roleCanEdit = true;
                                        if (Status.DRAFT.equals(e.getStatus())) { //newly created entity
                                            for (Object whoCanStart : workflow.whoCanStart(we)) {
                                                if (roles.contains(whoCanStart)) {
                                                    roleCanEdit = true;
                                                    break;
                                                } else {
                                                    roleCanEdit = false;
                                                }
                                            }
                                        } else {
                                            roleCanEdit = workflow.isActivityAccessibleByRoles(we.getWorkflowInfo().getWorklist(), roles);
                                        }
                                        return roleCanEdit && we.getWorkflowInfo().getOwners().contains(username);
                                    }).go().orElse(Boolean.FALSE);
                        }
                    }).go().orElse(Boolean.FALSE);
        }).orElse(Boolean.FALSE);
    }

}
