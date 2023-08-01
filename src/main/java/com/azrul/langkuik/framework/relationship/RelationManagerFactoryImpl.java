/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.relationship;

import com.azrul.langkuik.framework.field.FieldUtils;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author azrulm
 */
@Service
public class RelationManagerFactoryImpl implements RelationManagerFactory, Serializable {

    @Autowired
    FieldUtils fieldUtils;

    @Autowired
    RelationUtils relationUtils;

    public RelationManagerFactoryImpl() {

    }

    @SuppressWarnings("empty-statement")
    public <P, C> RelationManager<P, C> create(Class<P> parentClass,
            Class<C> currentClass) {
        RelationManager<P, C> rm = new RelationManager<P, C>() {

            @Override
            public void link(P parentObject, C currentObject,
                    String parentToCurrentField) {

                if (parentObject == null) {
                    return;
                }
                if (currentObject == null) {
                    return;
                }
                if (fieldUtils.classContainsField(parentObject.getClass(), parentToCurrentField) == false) {
                    return;
                }
                if (fieldUtils.isCollection(parentObject.getClass(), parentToCurrentField)) {
                    fieldUtils.getValue(parentObject, parentToCurrentField).ifPresentOrElse(c -> {
                        Collection collection = (Collection) c;
                        collection.add(currentObject);
                        relationUtils.getInverseRelation(parentClass, parentToCurrentField).ifPresent(invRelation -> {
                            fieldUtils.setValue(currentObject, invRelation, parentObject);
                        });

                    },
                            () -> {
                                Set<C> collection = new HashSet<>();
                                collection.add(currentObject);
                                fieldUtils.setValue(parentObject, parentToCurrentField, collection);
                                relationUtils.getInverseRelation(parentClass, parentToCurrentField).ifPresent(invRelation -> {
                                    fieldUtils.setValue(currentObject, invRelation, parentObject);
                                });
                            });
                } else {
                    fieldUtils.setValue(parentObject, parentToCurrentField, currentObject);
                    relationUtils.getInverseRelation(parentClass, parentToCurrentField).ifPresent(invRelation -> {
                        fieldUtils.setValue(currentObject, invRelation, parentObject);
                    });
                }

            }

            @Override
            public void unlink(P parentObject, C currentObject,
                    String parentToCurrentField) {

                if (parentObject == null) {
                    return;
                }
                if (currentObject == null) {
                    return;
                }
                if (fieldUtils.isCollection(parentObject.getClass(), parentToCurrentField)) {

                    fieldUtils.getValue(parentObject, parentToCurrentField).ifPresent(c -> {
                        Collection collection = (Collection) c;
                        collection.remove(currentObject);
                        relationUtils.getInverseRelation(parentClass, parentToCurrentField).ifPresent(invRelation -> {
                            fieldUtils.setValue(currentObject, invRelation,null);
                        });
                    });
                } else {
                    fieldUtils.setValue(parentObject, parentToCurrentField, null);
                    relationUtils.getInverseRelation(parentClass, parentToCurrentField).ifPresent(invRelation -> {
                        fieldUtils.setValue(currentObject, invRelation,null);
                    });
                }

            }

        };
        return rm;
    }
}
