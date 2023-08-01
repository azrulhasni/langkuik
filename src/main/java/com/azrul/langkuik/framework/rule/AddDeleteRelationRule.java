/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.rule;

import com.azrul.langkuik.framework.relationship.WebRelation;
import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.framework.entity.WorkElement;
import java.util.Optional;
import java.util.Set;

/**
 *
 * @author azrul
 */
public interface AddDeleteRelationRule {
     public Boolean compute(Optional<? extends WorkElement> oRoot, Optional<? extends Element> oParent, String relationName,
             Optional<Class<? extends Element>> oChildClass, Set<? extends Element> childrenToBeDeleted, Object... otherParams);
}
