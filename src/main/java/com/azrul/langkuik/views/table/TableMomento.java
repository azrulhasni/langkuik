/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.views.table;

import com.azrul.langkuik.framework.dao.DAOQuery;
import com.azrul.langkuik.framework.standard.Dual;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author azrul
 */
public class TableMomento implements Serializable {
    private final Integer page;
    private final DAOQuery searchQuery;
    private final List<Dual<String[],Boolean>> sortFieldsAsc;

    public TableMomento(Integer page, DAOQuery searchQuery, List<Dual<String[],Boolean>> sortFieldsAsc) {
        this.page = page;
        this.searchQuery = searchQuery;
        this.sortFieldsAsc=sortFieldsAsc!=null?sortFieldsAsc:new ArrayList<>();
    }

    /**
     * @return the page
     */
    public Integer getPage() {
        return page;
    }

    /**
     * @return the searchQuery
     */
    public DAOQuery getSearchQuery() {
        return searchQuery;
    }

   
    /**
     * @return the sortFieldsAsc
     */
    public List<Dual<String[],Boolean>> getSortFieldsAsc() {
        return sortFieldsAsc;
    }
    
    
}
