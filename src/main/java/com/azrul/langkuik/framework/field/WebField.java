/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.field;

import com.azrul.langkuik.framework.format.Format;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.azrul.langkuik.framework.rights.FieldAccess;

/**
 *
 * @author azrul
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WebField {
    String displayName() default ""; 
    String visibleInTable() default "true";
    String visibleInForm() default "true";
    boolean isReadOnly() default false;
    int order() default 0;
    Format[] format() default {}; //workaround to make format optional
    FieldAccess[] rights() default {@FieldAccess()};
    boolean referenceOwnerField() default false;
    
}
