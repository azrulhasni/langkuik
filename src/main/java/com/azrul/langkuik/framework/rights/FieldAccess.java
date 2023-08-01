/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.rights;

/**
 *
 * @author azrul
 */
public @interface FieldAccess {
    public String atWorklist() default "*";
    public FieldRights currentOwner() default FieldRights.CAN_ONLY_READ;
}
