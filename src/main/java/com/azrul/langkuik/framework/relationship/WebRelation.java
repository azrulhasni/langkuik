/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.relationship;

import com.azrul.langkuik.custom.subform.SubForm;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.azrul.langkuik.framework.rights.RelationAccess;
import com.azrul.langkuik.custom.VoidCustomComponentInTableRenderer;
import com.azrul.langkuik.custom.CustomInTableRenderer;

/**
 *
 * @author azrul
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WebRelation {
    String name();
    int order() default 0;
    int maxCount() default -1;
    int minCount() default 0;
    String visibleInForm() default "true";
    String visibleInTable() default "true";
    Class<? extends CustomInTableRenderer> customComponentInTable() default VoidCustomComponentInTableRenderer.class;
    SubForm asSubForm() default @SubForm;
    RelationAccess[] rights() default {@RelationAccess()};
}
