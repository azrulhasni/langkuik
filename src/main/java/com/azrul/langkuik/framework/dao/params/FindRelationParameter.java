/*
 * Copyright 2014 azrulm.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.azrul.langkuik.framework.dao.params;

import com.azrul.langkuik.framework.dao.filter.AndFilters;
import com.azrul.langkuik.framework.factory.SpringBeanFactory;
import com.azrul.langkuik.framework.relationship.RelationManagerFactoryImpl;
import java.io.Serializable;
import com.azrul.langkuik.framework.relationship.RelationManager;
import com.azrul.langkuik.framework.relationship.RelationUtils;
import java.util.Optional;

/**
 *
 * @author azrulm
 */
public class FindRelationParameter<P,C> implements Serializable {
    private P parentObject;
    private String parentToCurrentField;
    private RelationManager<P, C> relationManager;
    private Class<C> childClass;
    private AndFilters andFilter;
    private String searchText;

    public FindRelationParameter(P parentObject, String parentToCurrentField) {
        this(parentObject,parentToCurrentField,Optional.empty());
    }
    
    public FindRelationParameter(P parentObject, String parentToCurrentField, Optional<AndFilters> andFilters) {
        RelationUtils relationUtils= SpringBeanFactory.create(RelationUtils.class);
        
        this.parentObject = parentObject;
        this.parentToCurrentField = parentToCurrentField;
        relationUtils.getRelationClass(parentObject.getClass(), parentToCurrentField).ifPresent(childClass->setChildClass(childClass));
        this.relationManager = (new RelationManagerFactoryImpl()).create((Class<P>)parentObject.getClass(), childClass);
        this.andFilter=andFilters.orElse(null);
    }

    /**
     * @return the parentObject
     */
    public P getParentObject() {
        return parentObject;
    }

    /**
     * @param parentObject the parentObject to set
     */
    public void setParentObject(P parentObject) {
        this.parentObject = parentObject;
    }

    /**
     * @return the parentToCurrentField
     */
    public String getParentToCurrentField() {
        return parentToCurrentField;
    }

    /**
     * @param parentToCurrentField the parentToCurrentField to set
     */
    public void setParentToCurrentField(String parentToCurrentField) {
        this.parentToCurrentField = parentToCurrentField;
    }
    
    

    /**
     * @return the relationManager
     */
//    public RelationManager<P, C> getRelationManager() {
//        return relationManager;
//    }
//
//    /**
//     * @param relationManager the relationManager to set
//     */
//    public void setRelationManager(RelationManager<P, C> relationManager) {
//        this.relationManager = relationManager;
//    }

    /**
     * @return the childClass
     */
    public Class<C> getChildClass() {
        return childClass;
    }

    /**
     * @param childClass the childClass to set
     */
    public void setChildClass(Class<C> childClass) {
        this.childClass = childClass;
    }

    /**
     * @return the andFilter
     */
    public Optional<AndFilters> getAndFilter() {
        return Optional.ofNullable(andFilter);
    }

    /**
     * @param andFilter the andFilter to set
     */
    public void setAndFilter(Optional<AndFilters> andFilter) {
        this.andFilter = andFilter.orElse(null);
    }

    /**
     * @return the searchText
     */
    public Optional<String> getSearchText() {
        return Optional.ofNullable(searchText);
    }

    /**
     * @param searchText the searchText to set
     */
    public void setSearchText(Optional<String> searchText) {
        this.searchText = searchText.orElse(null);
    }
}
