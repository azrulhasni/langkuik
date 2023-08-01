/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.user;

import com.azrul.langkuik.custom.CustomField;
import com.azrul.langkuik.framework.user.UserFieldRenderer;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author azrul
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@CustomField(renderer=UserFieldRenderer.class)
public @interface User {
    
}
