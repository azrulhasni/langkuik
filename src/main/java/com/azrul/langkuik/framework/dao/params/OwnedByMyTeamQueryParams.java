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
public class OwnedByMyTeamQueryParams implements QueryParams{
    private String worklist=null;
    private String excludedUsername = null;

    /**
     * @return the worklist
     */
    public String getWorklist() {
        return worklist;
    }

    /**
     * @param worklist the worklist to set
     */
    public void setWorklist(String worklist) {
        this.worklist = worklist;
    }

    /**
     * @return the excludedUsername
     */
    public String getExcludedUsername() {
        return excludedUsername;
    }

    /**
     * @param excludedUsername the excludedUsername to set
     */
    public void setExcludedUsername(String excludedUsername) {
        this.excludedUsername = excludedUsername;
    }
    
}
