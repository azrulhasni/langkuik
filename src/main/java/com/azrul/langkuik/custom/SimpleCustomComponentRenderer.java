/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.custom;

import com.azrul.langkuik.framework.relationship.RelationMemento;
import com.vaadin.flow.component.Component;
import java.util.Optional;
import java.util.function.Consumer;

/**
 *
 * @author azrul
 */
public interface SimpleCustomComponentRenderer<T,R extends Component> {
    Optional<R> render(T bean);
}
