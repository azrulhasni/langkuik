/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.rule;

import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.standard.Castor;
import com.azrul.langkuik.framework.user.UserProfile;
import com.azrul.langkuik.framework.workflow.Workflow;
import com.azrul.langkuik.framework.workflow.model.Activity;
import com.vaadin.flow.server.VaadinSession;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author azrul
 */
@Service
@Qualifier("IsReleasableByRoleManager")
public class IsReleasableByRoleManager implements PojoRule {

    @Autowired
    Workflow workflow;

    @Override
    public <T extends Element> Boolean compute(Optional<T> owork, Object... otherParams) {

        UserProfile user = (UserProfile) VaadinSession.getCurrent().getSession().getAttribute("USER");
        String userIdentifier = (String) VaadinSession.getCurrent().getSession().getAttribute("USER_IDENTIFIER");
        if (user.getOtherAttributes().isEmpty()) {
            return Boolean.FALSE;
        }
        List<String> rolesManaged = (List<String>) user.getOtherAttributes().get("Manager-of-role");
        return (Boolean) owork.map(work -> {
            return Castor.<T, WorkElement>given(work)
                    .castItTo(WorkElement.class)
                    .thenDo(e -> {
                        //if (WorkElement.class.isAssignableFrom(work.getClass())) {
                        if (e.getWorkflowInfo().getOwners().contains(userIdentifier)) { //one of the owner of this work is the the current user. If he wants to release it, he can release it as EDITABLE
                            return Boolean.FALSE;
                        } else if (rolesManaged != null) {
                            Map<String, List<Activity>> roleActivityMap = workflow.getRoleActivityMap();

                            for (String managedRole : rolesManaged) {
                                List<String> activities = roleActivityMap
                                        .get(managedRole)
                                        .stream()
                                        .map(Activity::getId)
                                        .collect(Collectors.toList());

                                if (activities.contains(e.getWorkflowInfo().getWorklist())) {
                                    return Boolean.TRUE;
                                }
                            }
                            return Boolean.FALSE;
                        } else {
                            return Boolean.FALSE;
                        }
                    }).go().orElse(Boolean.FALSE);
        }).orElse(Boolean.FALSE);

    }

}
