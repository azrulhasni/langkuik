/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.custom.subform;

import com.azrul.langkuik.custom.EventToOpenOtherComponent;
import com.azrul.langkuik.framework.relationship.RelationMemento;
import com.azrul.langkuik.framework.standard.Dual;
import com.azrul.langkuik.views.pojo.PojoViewState;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import org.apache.commons.collections4.MultiValuedMap;

/**
 *
 * @author azrul
 */
public interface SubFormRenderer<T,R,C extends SubFormComponent> {
    Optional<C> render(R root,
            T parent, 
            String relationName,
            Optional<PojoViewState> oParentState,
            Map<Integer,EventToOpenOtherComponent> eventToOpenOtherComponent,
            Map<String, RelationMemento> relationMementos,
            Consumer<RelationMemento> onCommit,
            Consumer<RelationMemento> onDelete);
    
    Boolean preCondition(R root,
            T parent, 
            String relationName,
            Optional<PojoViewState> oParentState);
    
    
}
