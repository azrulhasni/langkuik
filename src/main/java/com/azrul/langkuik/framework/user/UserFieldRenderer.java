/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.user;

import com.azrul.langkuik.custom.CustomFieldRenderer;
import com.azrul.langkuik.framework.field.FieldUtils;
import com.azrul.langkuik.framework.field.WebField;
import com.azrul.langkuik.framework.minio.MinioService;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.select.data.SelectDataView;
import com.vaadin.flow.component.select.data.SelectListDataView;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataView;

import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.server.StreamResource;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author azrul
 */
@org.springframework.stereotype.Component
public class UserFieldRenderer<P, L> implements CustomFieldRenderer<P, L> {

    @Value("${application.lgProfilePicBaseMinioDir}")
    private String profilePicBaseMinioDir;

    @Autowired
    private FieldUtils fieldUtils;

    @Autowired
    private MinioService minioService;
    
    @Autowired
    private UserNameFormatter userNameFormatter;

    private static Pattern pattern = Pattern.compile("[\\s\\.]");

    @Override
    public Optional<Component> createInTable(P bean, String fieldName) {
        return createInForm(bean, fieldName, null, new HashMap());
    }

    @Override
    public Optional<Component> createInForm(P bean, String fieldName, String displayName, Map<String, AbstractField> fieldsInForm) {
        Optional<WebField> owf = fieldUtils.getWebField(bean.getClass(), fieldName);
        if (fieldUtils.isCollection(bean.getClass(), fieldName)) {
            return (Optional<Component>)fieldUtils.getValue(bean, fieldName).map(v -> {
                Collection people = (Collection) v;
                VerticalLayout layout = new VerticalLayout();
                if (displayName != null) {
                    layout.add(new Label(displayName + ":"));
                }
                for (Object p : people) {
                    Select select = getAvatar((String) p, bean, null);
                    owf.ifPresent(wf->{
                        select.setReadOnly(wf.isReadOnly());
                    });
                    layout.add(select);
                }
                layout.setWidthFull();
                return Optional.of((Component) layout);
            }).orElse(Optional.of((Component) (new Label())));
        } else {
            return (Optional<Component>)fieldUtils.getValue(bean, fieldName).map(v -> {
                Select select = getAvatar((String) v, bean, displayName);
                owf.ifPresent(wf->{
                        select.setReadOnly(wf.isReadOnly());
                    });
                return Optional.of((Component)select);
            }).orElse(Optional.of((Component) (new Label())));
        }
    }

    private Select getAvatar(String userIdentifier, P bean, String displayName) /*throws IllegalArgumentException, IllegalAccessException*/ {
        String userName = userNameFormatter.format(userIdentifier);
        String path = "/" + profilePicBaseMinioDir + "/" + userIdentifier + ".png";
        Avatar avatar = new Avatar();
        avatar.addThemeVariants(AvatarVariant.LUMO_SMALL);
        if (minioService.exist(path)) {
            StreamResource sr = new StreamResource(userIdentifier, () -> {
                return minioService.get(path);
            });
            avatar.setImageResource(sr);
        } else {
            String[] splitUsername = pattern.split(userName);
            if (splitUsername.length<=1){
                avatar.setAbbreviation(getFirstCharacter(userName)); 
            }else{
                avatar.setAbbreviation(getFirstCharacter(splitUsername[0]) + getFirstCharacter(splitUsername[1]));
            }
        }
        
        
        Select uselect = new Select();
        
        uselect.setItems(List.of(userName));
        uselect.setValue(userName);
        uselect.getStyle().set("align-content", "center");
        uselect.setLabel(displayName);
        uselect.setId("user");
        uselect.setRenderer(new ComponentRenderer<HorizontalLayout, String>(u -> {
            HorizontalLayout hl = new HorizontalLayout();
            hl.setId("hl-avatar-user");
            hl.setMaxWidth(50, Unit.PERCENTAGE);
            hl.add(avatar, new Span(userName));
            return hl;
        }));
       
        return uselect;
    }



    private String getFirstCharacter(String s) {
        if (s == null || s.length() == 0) {
            return null;
        } else {
            return s.substring(0, 1).toUpperCase();
        }
    }

}
