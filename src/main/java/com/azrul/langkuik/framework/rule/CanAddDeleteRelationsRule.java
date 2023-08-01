/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.rule;

import com.azrul.langkuik.framework.entity.WebEntity;
import com.azrul.langkuik.framework.entity.WebEntityType;
import com.azrul.langkuik.framework.relationship.RelationUtils;
import com.azrul.langkuik.framework.rights.RelationAccess;
import com.azrul.langkuik.framework.rights.RelationRights;
import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.framework.entity.EntityUtils;
import com.azrul.langkuik.framework.entity.WorkElement;
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
@Qualifier("CanAddDeleteRelationsRule")
public class CanAddDeleteRelationsRule implements AddDeleteRelationRule {

    @Autowired
    Workflow workflow;

    @Autowired
    RelationUtils relationUtils;

    @Autowired
    RelationUtils fieldUtils;
    
     @Autowired
    EntityUtils entityUtils;

    @Override
    public Boolean compute(Optional<? extends WorkElement> oRoot, Optional<? extends Element> oParent, String relationName, Optional<Class<? extends Element>> oChildClass, Set<? extends Element> childrenToBeDeleted, Object... otherParams) {
        final String userIdentifier = (String) VaadinSession.getCurrent().getSession().getAttribute("USER_IDENTIFIER");
        return oParent.map(parent -> {
            return oChildClass.map(childClass -> {
                return oRoot.map(root -> {
                    boolean editable = isParentEditale(parent, childClass, root, relationName, userIdentifier);
                    for (var child : childrenToBeDeleted) {
                        editable = editable && isChildDeletable(root, parent, child, userIdentifier, relationName);
                    }
                    return editable;
                }).orElseGet(() -> Boolean.FALSE);
            }).orElseGet(() -> Boolean.FALSE);
        }).orElseGet(() -> Boolean.FALSE);
    }

    private Boolean isParentEditale(Element parent, Class<? extends Element> childClass, WorkElement root, String relationName, final String username) {

        final String worklist = root.getWorkflowInfo().getWorklist();
        Set<String> roles = (Set<String>) VaadinSession.getCurrent().getSession().getAttribute("ROLES");
        if (roles.contains("REF_ADMIN")) {
            return parent.getClass().getAnnotation(WebEntity.class).type() == WebEntityType.REF;
        } else { //typical user
            RelationAccess[] relAccesses = relationUtils.getWebRelation(parent.getClass(), relationName).map(webRelation
                    -> webRelation.rights()
            ).orElse(new RelationAccess[]{});
            if (relAccesses.length == 0) {
                return false;
            }

            if ((workflow.isStartEvent(worklist))
                    && root.getCreator().equals(username)) { //if this item has just been created i.e. DRAFT, by default, allow editting unless explicitly disallowed
                for (RelationAccess relAccess : relAccesses) {
                    if (workflow.isStartEvent(relAccess.atWorklist())
                            && relAccess.currentOwner().equals(RelationRights.CAN_ONLY_READ)) {
                        return false;
                    }
                }
                return true;
            }

            //I must be the owner to add things 
            if (!root.getWorkflowInfo().getOwners().contains(username)) {
                return false;
            }

            //if I am...
            //see if I'm in the right worklist with the right access rights
            for (RelationAccess relAccess : relAccesses) {
                if (relAccess.atWorklist().equals(worklist) || relAccess.atWorklist().equals("*")) {
                    //if yes, add
                    return relAccess.currentOwner().equals(RelationRights.CAN_ADD_AND_DELETE_OWN_ITEMS);
                }
            }
            return false;

        }

    }

    private Boolean isChildDeletable(WorkElement root, Element parent, Element child, final String username, String relationName) {
        if (child.isReference()){
            return true;
        }
        if (workflow.isStartEvent(root.getWorkflowInfo().getWorklist()) ||
            root.getWorkflowInfo().getWorklist()==null) { //if I'm at the start
            return username.equals(child.getCreator()); //and I'm the creator, allow deleting
        }

        //if yes...
        String worklist = root.getWorkflowInfo().getWorklist();
        RelationAccess[] relAccesses = relationUtils.getWebRelation(parent.getClass(), relationName).map(webRelation
                -> webRelation.rights()
        ).orElse(new RelationAccess[]{});//parent.getClass().getDeclaredField(relationName).getAnnotation(WebRelation.class);

        for (RelationAccess relAccess : relAccesses) {
            //see if the current worklist matches the access rights
            if ((relAccess.atWorklist().equals(worklist) || relAccess.atWorklist().equals("*"))
                    && relAccess.currentOwner().equals(RelationRights.CAN_ADD_AND_DELETE_OWN_ITEMS)) {
                //and if user created the child, delete it
                return username.equals(child.getCreator());
            }
        }
        return Boolean.FALSE;

    }

}
