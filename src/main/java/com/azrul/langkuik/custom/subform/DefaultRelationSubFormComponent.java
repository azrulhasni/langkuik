/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azrul.langkuik.custom.subform;

import com.azrul.langkuik.custom.subform.SubFormComponent;
import com.azrul.langkuik.framework.relationship.RelationMemento;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author azrul
 */
public class DefaultRelationSubFormComponent extends SubFormComponent {

    private RelationMemento memento;

    /**
     * @return the memento
     */
    public RelationMemento getMemento() {
        return memento;
    }

    /**
     * @param memento the memento to set
     */
    public void setMemento(RelationMemento memento) {
        this.memento = memento;
    }

    @Override
    public void onTabNavigateAway() {
        validate();
    }

    
    @Override
    public Boolean validate() {
        Boolean isValid = memento.validate();
        if (currentButton != null) {
            List<Component> spans = currentButton.getChildren()
                    .filter(c -> c.getClass().equals(Span.class)).collect(Collectors.toList());
            spans.forEach(span -> {
                currentButton.remove(span);
            });

            if (isValid == Boolean.FALSE) {
                Span span = new Span("-");
                setStyle(span, "red", "red");
                currentButton.add(span);
            }

        } else {
            //remove first then add back
            List<Component> spans = currentTab.getChildren()
                    .filter(c -> c.getClass().equals(Span.class)).collect(Collectors.toList());
            spans.forEach(span -> {
                currentTab.remove(span);
            });
            if (isValid == Boolean.FALSE) {
                Span span = new Span("-");
                setStyle(span, "red", "red");
                currentTab.add(span);
                return Boolean.FALSE;
            }
        }
        return isValid;

    }

    @Override
    public void onTabNavigateIn() {
        validate();
    }

    
//    @Override
//     public Boolean validateBeforeSubmit() {
//        return validate();
//    }
//    
//    @Override
//    public Boolean validateBeforeClose() {
//        return validate();
//    }
}
