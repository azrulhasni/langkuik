/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azrul.langkuik.framework.standard.converter;

import com.azrul.langkuik.framework.entity.Choice;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;


/**
 *
 * @author azrul
 */
public class ChoiceBooleanConverter implements Converter<Choice, Boolean>{

    @Override
    public Result<Boolean> convertToModel(Choice prsntn, ValueContext vc) {
        if (prsntn==null){
            return Result.ok(null);
        }
        if (prsntn.equals(Choice.FALSE)){
            return Result.ok(Boolean.FALSE);
        }else if (prsntn.equals(Choice.TRUE)){
            return Result.ok(Boolean.TRUE);
        }else{
            return Result.ok(null);
        }
        
    }

    @Override
    public Choice convertToPresentation(Boolean model, ValueContext vc) {
        if (model==Boolean.TRUE){
            return Choice.TRUE;
        }else if (model==Boolean.FALSE){
            return Choice.FALSE;
        }else{
            return Choice.UNKNOWN;
        }
    }
    
    public String getStringRepresentation(Choice e){
        return e==Choice.TRUE?"TRUE":(e==Choice.FALSE?"FALSE":"-");
    }
    
}
