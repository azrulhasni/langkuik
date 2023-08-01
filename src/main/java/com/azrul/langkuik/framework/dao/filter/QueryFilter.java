/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.dao.filter;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

/**
 *
 * @author azrul
 */
public class QueryFilter<T> {
    private List<String> fieldNames;
    private T value;
    private FilterRelation relation;
    
    public QueryFilter(){
        fieldNames = new ArrayList<>();
    }

    public static <T> QueryFilter build(String fieldName, FilterRelation relation, T value){
        QueryFilter<T> filter =  new QueryFilter<>();
        filter.setFieldName(fieldName);
        filter.setRelation(relation);
        filter.setValue(value);
        return filter;
    }
    
     public static <T> QueryFilter build(String[] fieldNames, FilterRelation relation, T value){
        QueryFilter<T> filter =  new QueryFilter<>();
        filter.setFieldNames(List.of(fieldNames));
        filter.setRelation(relation);
        filter.setValue(value);
        return filter;
    }
     
     
    /**
     * @return the fieldName
     */
//    public String getFieldName() {
//        return fieldNames.stream().findFirst().orElse(null);
//    }

    /**
     * @param fieldName the fieldName to set
     */
    public void setFieldName(String fieldName) {
        this.fieldNames.add(fieldName);
    }

    /**
     * @return the value
     */
    public T getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(T value) {
        this.value = value;
    }

    /**
     * @return the relation
     */
    public FilterRelation getRelation() {
        return relation;
    }

    /**
     * @param relation the relation to set
     */
    public void setRelation(FilterRelation relation) {
        this.relation = relation;
    }

    /**
     * @return the fieldNames
     */
    public List<String> getFieldNames() {
        return fieldNames;
    }

    /**
     * @param fieldNames the fieldNames to set
     */
    public void setFieldNames(List<String> fieldNames) {
        this.fieldNames = fieldNames;
    }
}
