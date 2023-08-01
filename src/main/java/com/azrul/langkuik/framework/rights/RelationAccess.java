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
public @interface RelationAccess {
     public String atWorklist() default "*";
    public RelationRights currentOwner() default RelationRights.CAN_ONLY_READ;
}
