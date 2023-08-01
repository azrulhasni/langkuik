/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.standard;

/**
 *
 * @author azrul
 */
public class Single<T> {
    private T value;
    
    public Single(){
        value = null;
    }
    
    public Single(T value){
        this.value=value;
    }
    
    

    /**
     * @return the value
     */
    public T getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(T value) {
        this.value = value;
    }
}
