/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.custom;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author azrul
 */
public interface CustomFieldRenderer<P,L> {
   Optional<Component> createInForm(P bean, String fieldName, String displayName,  Map<String,AbstractField> fieldsInForm);
   Optional<Component> createInTable(P bean, String fieldName);
}
