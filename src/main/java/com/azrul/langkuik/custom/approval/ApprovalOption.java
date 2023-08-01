/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.custom.approval;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author azrul
 */
public class ApprovalOption {

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.value);
        hash = 41 * hash + Objects.hashCode(this.display);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ApprovalOption other = (ApprovalOption) obj;
        if (!Objects.equals(this.display, other.display)) {
            return false;
        }
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }
    public static List<ApprovalOption> getOptions(){
        return List.of( 
                    ApprovalOption.of(null),
                    ApprovalOption.of(Boolean.TRUE),
                    ApprovalOption.of(Boolean.FALSE)
                );
    }
    
    public static ApprovalOption of(Boolean approved){
        if (approved==Boolean.TRUE){
            return new ApprovalOption(true,"approved");
        }else if(approved==Boolean.FALSE){
            return new ApprovalOption(false,"not approved");
        }else{
            return new ApprovalOption(null,"-");
        }
    }
    
    private Boolean value;
    private String display;

    private ApprovalOption(Boolean value, String display) {
        this.value = value;
        this.display = display;
    }

    /**
     * @return the value
     */
    public Boolean getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Boolean value) {
        this.value = value;
    }

    /**
     * @return the display
     */
    public String getDisplay() {
        return display;
    }

    /**
     * @param display the display to set
     */
    public void setDisplay(String display) {
        this.display = display;
    }
}
