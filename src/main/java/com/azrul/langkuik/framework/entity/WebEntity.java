/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.entity;

import com.azrul.langkuik.framework.field.FieldVisibility;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.hibernate.annotations.Where;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

/**
 *
 * @author azrul
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WebEntity {
    String name();
    WebEntityType type() default WebEntityType.NOMINAL;
    FieldVisibility[] fieldVisibility() default {};
}

