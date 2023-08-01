/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.standard.converter;

import com.vaadin.flow.data.converter.StringToLongConverter;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author azrul
 */
public class CustomStringToLongConverter extends StringToLongConverter {

    public CustomStringToLongConverter(String error){
        super(error);
    }

    @Override
    protected NumberFormat getFormat(Locale locale) {
        final NumberFormat format = super.getFormat(locale);
        format.setGroupingUsed(true); 
        return format;
    }
}
