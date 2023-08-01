/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.custom;

import com.azrul.langkuik.framework.relationship.RelationMemento;
import com.vaadin.flow.component.Component;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author azrul
 */
public interface CustomInTableRenderer<T,C extends Component> {
    Optional<C> render(T parent, 
            String relationName);
}
