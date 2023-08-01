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



/**
 *
 * @author azrul
 */
public class StringStatusConverter  implements Converter<String, Status> {

    @Override
    public Result<Status> convertToModel(String prsntn, ValueContext vc) {
        return Result.ok(Status.valueOf(prsntn)); 
    }

    @Override
    public String convertToPresentation(Status model, ValueContext vc) {
        return model.name();
    }

    
    
}
