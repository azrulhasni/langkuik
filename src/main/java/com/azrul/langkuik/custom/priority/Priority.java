/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.custom.priority;

/**
 *
 * @author azrul
 */
public enum Priority {
    NONE("None"),
    LOWEST("Lowest"),
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High"),
    HIGHEST("Highest");
    
    private String value;
    
    Priority(String value){
        this.value=value;
    }

    @Override
    public String toString(){
        return value;
    }
    
    
    
}
