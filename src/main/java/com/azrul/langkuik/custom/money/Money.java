/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.custom.money;

import com.azrul.langkuik.custom.lookupchoice.LookupRenderer;
import com.azrul.langkuik.custom.CustomField;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author azrul
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@CustomField(renderer=MoneyRenderer.class)
public @interface Money {
    String currency() default ""; //ISO 4217. Empty means use system locale
    int decimalPlace() default 2;
}
