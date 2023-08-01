/*
 * To change this license headerTop, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.custom.comment;

import com.azrul.langkuik.custom.SimpleCustomComponentRenderer;
import com.azrul.langkuik.framework.dao.DataAccessObject;
import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.framework.user.UserFieldRenderer;
import com.vaadin.componentfactory.ToggleButton;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author azrul
 */
public class CommentRenderer<P extends Element> implements SimpleCustomComponentRenderer<Comment, VerticalLayout> {

    @Value("${application.lgDateTimeFormat:yyyy-MM-dd HH:mm:ss}")
    private String dateTimeFormat;

    @Autowired
    private DataAccessObject dao;

    @Autowired
    private UserFieldRenderer userFieldRenderer;

    @Override
    public Optional<VerticalLayout> render(Comment source) {

        DatePicker.DatePickerI18n singleFormatI18n
                = new DatePicker.DatePickerI18n();
        singleFormatI18n.setDateFormat(dateTimeFormat);

        String userIndentifier = (String) VaadinSession.getCurrent().getSession().getAttribute("USER_IDENTIFIER");
        FormLayout commentLayout = new FormLayout();

        Optional<Component> avatar = userFieldRenderer.createInForm(source, "creator", null, new HashMap());

        DateTimePicker dateTimePicker = new DateTimePicker();
        dateTimePicker.setDatePickerI18n(singleFormatI18n);
        dateTimePicker.setValue(source.getDateTime());
        dateTimePicker.setReadOnly(true);

        ToggleButton btnEdit = new ToggleButton("Edit");
        btnEdit.setValue(Boolean.FALSE);
        if (userIndentifier.equals(source.getCreator())) {
            btnEdit.setVisible(true);
            btnEdit.setEnabled(true);
        } else {
            btnEdit.setVisible(false);
            btnEdit.setEnabled(false);
        }
        commentLayout.add(avatar.get());
        
        commentLayout.add(btnEdit);
        
        commentLayout.add(dateTimePicker);
        commentLayout.setColspan(dateTimePicker, 2);
         
        TextArea taComments = new TextArea();
        taComments.setValue(source.getValue());
        taComments.setWidthFull();
        taComments.setReadOnly(true);
        
         commentLayout.add(taComments);
         commentLayout.setColspan(taComments, 2);
//       
        btnEdit.addValueChangeListener(evt -> {
            if (Boolean.TRUE.equals(evt.getValue())) {
                taComments.setReadOnly(false);
            } else {
                taComments.setReadOnly(true);
                source.setValue(taComments.getValue());
                dao.save(source);
            }
        });
        VerticalLayout wrapper = new VerticalLayout();
        wrapper.add(commentLayout);
        return Optional.of(wrapper);
    }

}
