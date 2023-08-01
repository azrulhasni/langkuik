/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.custom.money;

import com.azrul.langkuik.custom.CustomFieldRenderer;
import com.azrul.langkuik.framework.field.FieldUtils;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.BigDecimalField;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 *
 * @author azrul
 */
class MoneyRenderer<L, P> implements CustomFieldRenderer<L, P> {

    @Autowired
    private FieldUtils fieldUtils;

    @Override
    public Optional<Component> createInForm(L bean, String fieldName, String displayName, Map<String, AbstractField> fieldsInForm) {
        return (Optional<Component>) fieldUtils.getAnnotation(bean.getClass(), fieldName, Money.class).map(a -> {

            Money money = (Money) a;
            //assert value.getClass().equals(BigDecimal.class) : "Money must be of BigDecimal type";

            //var value =
            String currency = money.currency();
            if ("".equals(currency)) {
                currency = Currency.getInstance(LocaleContextHolder.getLocale()).getCurrencyCode();
            }
            BigDecimalField uifield = new BigDecimalField(displayName);
            uifield.addValueChangeListener(e -> {
                if (e.getValue() != null) {
                    e.getValue().setScale(money.decimalPlace());
                }
            });
            Div uicurrency = new Div();
            uicurrency.setText(currency);
            uifield.setPrefixComponent(uicurrency);
            fieldUtils.getValue(bean, fieldName).ifPresent(v -> {
                uifield.setValue((BigDecimal) v);
            });
            return Optional.of(uifield);
        }).orElse(Optional.empty());

    }

    @Override
    public Optional<Component> createInTable(L bean, String fieldName) {
        return (Optional<Component>) fieldUtils.getAnnotation(bean.getClass(), fieldName, Money.class).map(a -> {

            Money money = (Money) a;
           String currency = (!"".equals(money.currency()))?money.currency():Currency.getInstance(LocaleContextHolder.getLocale()).getCurrencyCode();
            
            return fieldUtils.getValue(bean, fieldName).map(v -> {
                BigDecimal value = (BigDecimal) v;
                return Optional.of((Component)(new Label(currency + value.toPlainString())));
            }).orElse(Optional.of((Component)(new Label())));

        }).orElse(Optional.empty());
    }

}
