/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.dao.params;

/**
 *
 * @author azrul
 */
public class CreatedByMeQueryParams implements QueryParams{
    private String creator=null;

    /**
     * @return the creatorId
     */
    public String getCreator() {
        return creator;
    }

    /**
     * @param creator the creatorId to set
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }
}
