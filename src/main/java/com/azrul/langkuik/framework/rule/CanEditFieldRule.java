/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.rule;

import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.framework.field.WebField;
import com.azrul.langkuik.framework.rights.FieldAccess;
import com.azrul.langkuik.framework.rights.FieldRights;
import com.azrul.langkuik.framework.entity.EntityUtils;
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
@Qualifier("CanEditFieldRule")
public class CanEditFieldRule implements FieldRule {

    @Autowired
    Workflow workflow;

    @Autowired
    EntityUtils entityUtils;

    @Override
    public <T extends Element> Boolean compute(Optional<T> oroot, Optional<WebField> oWebField, Object... otherParams) {

        final String userIdentifier = (String) VaadinSession.getCurrent().getSession().getAttribute("USER_IDENTIFIER");

        Boolean a = oroot.map(root -> {
            Set<String> roles = (Set<String>) VaadinSession.getCurrent().getSession().getAttribute("ROLES");
            if (root.isReference()) {
                if (roles == null) {
                    return false;
                }
                if (roles.isEmpty()) {
                    return false;
                }
                return (roles.contains("REF_ADMIN"));
            } else {
                return false;
            }
        }).orElseGet(() -> Boolean.FALSE);

        if (a == true) {
            return a;
        }
        return (Boolean)oroot.map(root -> {
            return Castor.<T, WorkElement>given(root)
                    .castItTo(WorkElement.class)
                    .thenDo(we -> {

                        //if object is at start, and owner==creator, then allow to edit unless not allowed
                        boolean b = oWebField.map((var webField) -> {

                            final String worklist = we.getWorkflowInfo().getWorklist();
                            if (
                                    (
                                        workflow.isStartEvent(worklist) ||
                                        worklist == null
                                    )
                                    && we.getCreator().equals(userIdentifier)
                               ) { //if this item has just been created i.e. DRAFT, by default, allow editting unless explicitly disallowed
                                FieldAccess[] fieldAccesses = webField.rights();
                                for (FieldAccess fieldAccess : fieldAccesses) {
                                    if ((workflow.isStartEvent(fieldAccess.atWorklist())
                                            || fieldAccess.atWorklist().equals("") //<-- this means generic start
                                            )
                                            && fieldAccess.currentOwner().equals(FieldRights.CAN_ONLY_READ)) {
                                        return Boolean.FALSE;
                                    }
                                }
                                return Boolean.TRUE;
                            } else {
                                return Boolean.FALSE;
                            }

                        }).orElseGet(() -> Boolean.FALSE);

                        if (b == true) {
                            return b;
                        }

                        return oWebField.map((var webField) -> {
                            final String worklist = we.getWorkflowInfo().getWorklist();
                            //I must be the owner to edit things 
                            if (!we.getWorkflowInfo().getOwners().contains(userIdentifier)) {
                                return false;
                            }
                            //Calculate access
                            FieldAccess[] fieldAccesses = webField.rights();
                            for (FieldAccess fieldAccess : fieldAccesses) {
                                if (fieldAccess.atWorklist().equals(worklist) || fieldAccess.atWorklist().equals("*")) {
                                    return fieldAccess.currentOwner().equals(FieldRights.CAN_EDIT);
                                }
                            }

                            return Boolean.FALSE;
                        }).orElse(Boolean.FALSE);
                    }).failingWhichDo(() -> Boolean.FALSE).go().orElse(Boolean.FALSE);
        }).orElse(Boolean.FALSE);
    }

}
