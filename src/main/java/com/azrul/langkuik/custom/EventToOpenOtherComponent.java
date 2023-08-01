/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azrul.langkuik.custom;

import com.vaadin.flow.component.ComponentEventListener;

/**
 *
 * @author azrul
 */
public class EventToOpenOtherComponent {
    private String name;
    private ComponentEventListener listener;

    public static EventToOpenOtherComponent of(String name, ComponentEventListener listener){
        return new EventToOpenOtherComponent(name,listener);
    }

    private EventToOpenOtherComponent(String name, ComponentEventListener listener) {
        this.name = name;
        this.listener = listener;
    }
    
   

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the listener
     */
    public ComponentEventListener getListener() {
        return listener;
    }

    /**
     * @param listener the listener to set
     */
    public void setListener(ComponentEventListener listener) {
        this.listener = listener;
    }
    
}
