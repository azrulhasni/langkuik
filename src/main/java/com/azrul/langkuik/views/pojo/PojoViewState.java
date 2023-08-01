/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.views.pojo;

/**
 *
 * @author azrul
 */
public enum PojoViewState {
    NONE,
    NOT_EDITABLE,
    NOT_EDITABLE_REF,
    BOOKABLE, 
    RELEASABLE_BY_ROLE_MGR,
    EDITABLE, 
    CORRECTABLE, 
    EDITABLE_NOTRELEASABLE,
    EDITABLE_NOTRELEASABLE_REF, 
    TO_BE_APPROVED
}
