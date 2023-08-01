/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.field;

/**
 *
 * @author azrul
 */
public @interface FieldVisibility {
    String fieldName() default "";
    String visibleInTable() default "true";
    String visibleInForm() default "true";
}
