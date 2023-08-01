/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.custom.priority;

import com.azrul.langkuik.custom.CustomFieldRenderer;
import com.azrul.langkuik.framework.field.FieldUtils;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author azrul
 */
public class PriorityRenderer<L, P> implements CustomFieldRenderer<L, P> {

    @Autowired
    private FieldUtils fieldUtils;

    @Override
    public Optional<Component> createInForm(L bean, 
            String fieldName, 
            String displayName, 
            Map<String, AbstractField> fieldsInForm) {
        
        return (Optional<Component>) fieldUtils.getValue(bean, fieldName).map(value -> {

            Select select = new Select();
            select.setLabel(displayName);
            select.setItems(EnumSet.allOf(Priority.class));

            if (value != null) {
                select.setValue(value);
            } else {
                select.setValue(Priority.NONE);
            }
            select.setRenderer(new ComponentRenderer<HorizontalLayout, Priority>(priority -> {

                HorizontalLayout hl = createBadge(priority);
                hl.setMaxWidth(50, Unit.PERCENTAGE);
                return hl;
            }));
            return Optional.of((Component) select);
        }).orElse(Optional.of((Component) (new Label())));
    }

    @Override
    public Optional<Component> createInTable(L bean, String fieldName) {
        return fieldUtils.getValue(bean, fieldName).map(priority -> {

            return Optional.of((Component) createBadge((Priority) priority));
        }).orElse(Optional.of((Component) (new Label())));

    }

    private HorizontalLayout createBadge(Priority priority) {
        HorizontalLayout hl = new HorizontalLayout();

        Span priorityField = new Span();

        if (priority != null) {
            priorityField.setText(priority.toString());
            if (priority == Priority.HIGHEST) {
                setStyle(priorityField, "red", "white");
            } else if (priority == Priority.HIGH) {
                setStyle(priorityField, "darksalmon", "white");
            } else if (priority == Priority.MEDIUM) {
                setStyle(priorityField, "orange", "white");
            } else if (priority == Priority.LOW) {
                setStyle(priorityField, "green", "white");
            } else if (priority == Priority.LOWEST) {
                setStyle(priorityField, "lightgreen", "black");
            } else {
                setStyle(priorityField, "grey", "white");
            }
        }
        hl.add(priorityField);
        return hl;
    }

    private void setStyle(Span span, String bgcolor, String fontcolor) {
        span.getStyle().set("background-color", bgcolor);
        span.getStyle().set("color", fontcolor);
        span.getStyle().set("border-radius", "15px");
        span.getStyle().set("width", "120%");
        span.getStyle().set("text-align", "center");
    }
}
