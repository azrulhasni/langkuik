/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.custom.lookupchoice;

import com.azrul.langkuik.custom.CustomFieldRenderer;
import com.azrul.langkuik.framework.dao.DataAccessObject;
import com.azrul.langkuik.framework.dao.filter.AndFilters;
import com.azrul.langkuik.framework.dao.filter.FilterRelation;
import com.azrul.langkuik.framework.dao.filter.QueryFilter;
import com.azrul.langkuik.framework.dao.query.LookupQuery;
import com.azrul.langkuik.framework.field.FieldUtils;
import com.azrul.langkuik.framework.entity.Status;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.server.VaadinSession;
import java.util.Optional;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author azrul
 * @param <P>
 */
public class LookupRenderer<P, L> implements CustomFieldRenderer<P, L> {

    @Autowired
    DataAccessObject dao;

    @Autowired
    private FieldUtils fieldUtils;
    
    @Value("${application.lgCacheReferenceData}")
    private Boolean cacheReferenceData;
    

    @Override
    public Optional<Component> createInForm(P bean,
            final String fieldName, String displayName, Map<String, AbstractField> fieldsInForm) {
        //by default load all
        String tenant = (String) VaadinSession.getCurrent().getSession().getAttribute("TENANT");
        Set<String> roles = (Set<String>) VaadinSession.getCurrent().getSession().getAttribute("ROLES");

        if (roles.contains("REF_ADMIN")) {
            return Optional.of(new TextField(displayName));
        } else {
            return (Optional<Component>) fieldUtils.getAnnotation(bean.getClass(), fieldName, Lookup.class).map(a -> {
                Lookup lookup = (Lookup) a;
                Class<L> lookupEntityClass = lookup.entity();
                
                AndFilters andFilters = AndFilters.build(
                        QueryFilter.build("status", FilterRelation.NOT_EQUAL, Status.CANCELLED),
                        QueryFilter.build("status", FilterRelation.NOT_EQUAL, Status.RETIRED)
                );
                
                LookupQuery<L, String> defLookupQuery = new LookupQuery<>(lookupEntityClass,
                        lookup.field(),cacheReferenceData,andFilters);

                ComboBox comboBox = new ComboBox(displayName);
                comboBox.setDataProvider(
                        new ListDataProvider(
                                dao.runQuery(
                                        defLookupQuery,
                                        Optional.of(tenant),
                                        Optional.empty())));

                comboBox.addFocusListener(e -> {

                    LookupQuery<L, String> lookupQuery = null;
                    if ("".equals(lookup.filterBy())) {
                        lookupQuery = new LookupQuery<>(lookupEntityClass,
                                lookup.field(), cacheReferenceData, andFilters);
                    } else {
                        if (fieldsInForm.get(lookup.filterBy()) == null) {
                            lookupQuery = new LookupQuery<>(lookupEntityClass,
                                    lookup.field(), cacheReferenceData, andFilters);
                        } else {
                            lookupQuery = new LookupQuery<>(lookupEntityClass,
                                    lookup.field(), lookup.filterBy(),
                                    (String) fieldsInForm.get(lookup.filterBy()).getValue(),
                                    cacheReferenceData, andFilters);
                        }
                    }
                    comboBox.setDataProvider(
                            new ListDataProvider(
                                    dao.runQuery(lookupQuery,
                                            Optional.of(tenant),
                                            Optional.empty())));
                });

                comboBox.addValueChangeListener(e -> {
                    if (!"".equals(lookup.filterBy())) {
                        LookupQuery<L, String> mlookupQuery = new LookupQuery<>(
                                lookupEntityClass,
                                lookup.filterBy(),
                                lookup.field(),
                                (String) e.getValue(),
                                cacheReferenceData,
                                andFilters);

                        String chosen = (String) dao.runQuery(
                                mlookupQuery,
                                Optional.of(tenant),
                                Optional.empty()).iterator().next();
                        ComboBox mCombo = (ComboBox) fieldsInForm.get(lookup.filterBy());
                        mCombo.setValue(chosen);
                    }

                });
                return Optional.of(comboBox);
            }).orElse(Optional.empty());

        }

    }

    @Override
    public Optional<Component> createInTable(P bean, String fieldName) {
        return fieldUtils.getValue(bean,fieldName).map(v -> {
            return Optional.of((Component)new Label((String) v));
        }).orElse(Optional.empty());
    }

}
