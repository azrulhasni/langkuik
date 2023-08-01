/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.dao.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author azrul
 */
public class AndFilters {
    private List<QueryFilter> filters;
    
    public AndFilters(){
        filters=new ArrayList<>();
    }
    
     public static AndFilters empty(){
        AndFilters andFilters = new AndFilters();
        return andFilters;
     }
    
    public static AndFilters build(QueryFilter... filters){
        AndFilters andFilters = new AndFilters();
        andFilters.getFilters().addAll(Arrays.asList(filters));
        return andFilters;
    }
    
    public void addFilter(QueryFilter filter){
        if (filters==null){
            filters=new ArrayList<>();
        }
        filters.add(filter);
    }

    /**
     * @return the filters
     */
    public List<QueryFilter> getFilters() {
        return filters;
    }

    /**
     * @param filters the filters to set
     */
    public void setFilters(List<QueryFilter> filters) {
        this.filters = filters;
    }
}
