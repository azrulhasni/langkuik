/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.dao;

/**
 *
 * @author azrul
 */
public interface Consumer2Inputs<P,C> {
    void accept(P parentBean, C childBean);
}
