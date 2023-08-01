/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.custom.onetoone;

import com.azrul.langkuik.framework.field.FieldUtils;
import com.azrul.langkuik.framework.relationship.RelationMemento;
import com.azrul.langkuik.framework.relationship.RelationUtils;
import com.azrul.langkuik.framework.entity.Element;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import com.azrul.langkuik.custom.CustomInTableRenderer;

/**
 *
 * @author azrul
 */
public class OneToOneRenderer<T extends Element> implements CustomInTableRenderer<T,TextField> {
    
    @Autowired
    FieldUtils fieldUtils;
   

    @Override
    public Optional<TextField> render(T parent, String relationName) {
        return fieldUtils.getValue(parent, relationName).map(v->{
        
            TextField tf = new TextField();
            if (v.toString()!=null){
                tf.setValue(v.toString());
            }
            tf.setReadOnly(true);
            return Optional.of(tf);
        
        }).orElseGet(()->{
            TextField tf = new TextField();
            tf.setReadOnly(true);
            return Optional.of(tf);
        });
    }

    
    
}
