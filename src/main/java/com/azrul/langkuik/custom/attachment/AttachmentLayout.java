/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.custom.attachment;


import com.azrul.langkuik.custom.subform.SubFormComponent;
import com.vaadin.flow.component.HasEnabled;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.util.function.Consumer;

/**
 *
 * @author azrul
 */
public class AttachmentLayout extends SubFormComponent {
    private Consumer<Boolean> onEnable = (a)->{};
    
    public AttachmentLayout(){
        
    }
    
    @Override
    public void setEnabled(boolean enable){
        //super.setEnabled(enable); //we comment this out to allow table navigation
            onEnable.accept(enable);
        
    }

    /**
     * @param onEnable the onEnable to set
     */
    public void setOnEnable(Consumer<Boolean> onEnable) {
        this.onEnable = onEnable;
    }

 
   
}
