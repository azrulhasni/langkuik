/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.custom.subform;

import com.azrul.langkuik.custom.subform.SubFormRenderer;
import com.azrul.langkuik.custom.subform.VoidSubFormRenderer;

/**
 *
 * @author azrul
 */
public @interface SubForm {
    Class<? extends SubFormRenderer> subFormRenderer() default VoidSubFormRenderer.class;
    String active() default "false";
    SubFormType type() default SubFormType.IN_PARENT;
   
}
