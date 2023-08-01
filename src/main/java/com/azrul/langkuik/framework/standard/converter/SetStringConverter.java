/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.standard.converter;

import com.azrul.langkuik.framework.entity.Status;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author azrul
 */
public class SetStringConverter  implements Converter<String, Set<String>> {

    @Override
    public Result<Set<String>> convertToModel(String prsntn, ValueContext vc) {
        if ("".equals(prsntn)){
            Result.ok(null); //nullify result if empty string. If not, an empty string is going to be stored in DB
        }else{
            return Result.ok(new HashSet<String>(Arrays.asList(prsntn.split(",", -1))));
        }
        return Result.ok(null);
    }

    @Override
    public String convertToPresentation(Set<String> model, ValueContext vc) {
       return String.join(",", model);
    }
    
}
