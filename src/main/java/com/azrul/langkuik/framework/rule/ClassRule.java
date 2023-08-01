/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.rule;

import com.azrul.langkuik.framework.entity.Element;

/**
 *
 * @author azrul
 */
 public interface ClassRule<T extends Element> {
    public  Boolean compute(Class<T> tclass,Object... otherParams);
}
