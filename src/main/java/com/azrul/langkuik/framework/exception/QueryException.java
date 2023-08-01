/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.exception;

/**
 *
 * @author azrul
 */
public class QueryException extends RuntimeException{
    private String errorMessage;
    public QueryException(Throwable e, String errorMessage) {
        super(e);
        this.errorMessage=errorMessage;
    }
    
    public String getErrorMessage(){
        return errorMessage;
    }
    
}
