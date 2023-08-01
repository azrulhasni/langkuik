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
package com.azrul.langkuik.framework.relationship;

import com.azrul.langkuik.custom.VoidCustomComponentInTableRenderer;
import com.azrul.langkuik.framework.field.WebField;
import com.azrul.langkuik.framework.standard.Dual;
import com.azrul.langkuik.framework.entity.EntityUtils;
import com.azrul.langkuik.framework.factory.SpringBeanFactory;
import com.azrul.langkuik.framework.field.FieldUtils;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EntityManagerFactory;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.criteria.Path;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.azrul.langkuik.custom.CustomInTableRenderer;

/**
 *
 * @author azrulm
 */
@Component("relationUtils")
public class RelationUtils {

    @Autowired
    FieldUtils fieldUtils;

    @Autowired
    EntityUtils entityUtils;

    public RelationType getRelationType(Field field) {
        if (field.isAnnotationPresent(ManyToOne.class) || field.isAnnotationPresent(OneToOne.class)) {
            return RelationType.X_To_ONE;
        }
        if (field.isAnnotationPresent(ManyToMany.class) || field.isAnnotationPresent(OneToMany.class)) {
            return RelationType.X_TO_MANY;
        }
        return RelationType.NA;
    }

    public Optional<RelationType> getRelationType(Class parentClass, String relationName) {
        return fieldUtils.getField(parentClass, relationName).map(field -> {
            return Optional.of(getRelationType(field));
        }).orElse(Optional.of(RelationType.NA));
    }

    public Optional<Class> getRelationClass(Class parentClass, String relationName) {
        return (Optional<Class>) fieldUtils.getField(parentClass, relationName).map(field -> {

            if (Collection.class.isAssignableFrom(field.getType())) {
                return Optional.of((Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]);

            } else {
                return Optional.of(field.getType());
            }
        }).orElse(Optional.empty());
    }

    public Optional<WebRelation> getWebRelation(Class<?> type, String relationName) {
        return fieldUtils.getField(type, relationName).map(field -> {
            return field.getAnnotation(WebRelation.class);
        });
    }
    
    public Optional<String> getInverseRelation(Class parentClass, String relation){
        String inv = (String) fieldUtils.getField(parentClass, relation).map(field->{
            if (field.isAnnotationPresent(OneToOne.class)){
                OneToOne oo = field.getAnnotation(OneToOne.class);
                return oo.mappedBy();
            }else if (field.isAnnotationPresent(OneToMany.class)){
                OneToMany om = field.getAnnotation(OneToMany.class);
                return om.mappedBy();
            }            
            return null;
        }).orElseGet(()->{
            return null;
        });
        return Optional.ofNullable(inv);
        
    }

    public List<Field> getParentChildFields(Class parentClass, Class currentClass) {
        List<Field> fields = new ArrayList<>();
        if (parentClass == null) {
            return fields;
        }
        if (currentClass == null) {
            return fields;
        }
        try {
            for (Field parentField : fieldUtils.getAllFields(parentClass).values()) {
                parentField.setAccessible(true);

                if (Collection.class.isAssignableFrom(parentField.getType())) {
                    Class fieldClass = (Class) ((ParameterizedType) parentField.getGenericType()).getActualTypeArguments()[0];
                    if (fieldClass.equals(currentClass)) {
                        fields.add(parentField);
                    }
                } else {
                    if (parentField.getType().equals(currentClass)) {
                        fields.add(parentField);
                    }
                }
            }
            return fields;
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(RelationManagerFactoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fields;

    }

    public <T> Boolean isEmbedable(Class<T> tclass) {
        if (tclass.isAnnotationPresent(Embeddable.class)) {
            return true;
        } else {
            return false;
        }
    }

    public <T> MultiValuedMap<Integer, RelationContainer> getRelationsByOrder(Class<T> tclass) {
        //Field store
        MultiValuedMap<Integer, RelationContainer> fieldStore = new ArrayListValuedHashMap<>();
        try {

            //grid.
            BeanInfo beanInfo = Introspector.getBeanInfo(tclass);
            for (java.beans.PropertyDescriptor propertyDescriptor
                    : beanInfo.getPropertyDescriptors()) {
                if (escapeNonFields(propertyDescriptor)) {
                    continue;
                }

                //Get webfield annotation values
                getWebRelation(propertyDescriptor,  tclass).ifPresent(webRelation->{
                    //Add columns
                    if (propertyDescriptor.getReadMethod().isAnnotationPresent(WebRelation.class)) {
                        Method getter = propertyDescriptor.getReadMethod();
                        //store field to be sorted
                        RelationContainer rc = new RelationContainer(webRelation, getter);
                        fieldStore.put(webRelation.order(), rc);
                    } else if (fieldUtils.isAnnotationPresent(tclass, propertyDescriptor.getName(),WebRelation.class)) {
                        fieldUtils.getField(tclass, propertyDescriptor.getName()).ifPresent(field->{
                            //store field to be sorted
                            RelationContainer rc = new RelationContainer(webRelation, field);
                            fieldStore.put(webRelation.order(), rc);
                        });
                    }
                });
            }
        } catch (IntrospectionException ex) {
            Logger.getLogger(RelationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fieldStore;
    }

    private Optional<WebRelation> getWebRelation(PropertyDescriptor propertyDescriptor,  Class tclass) {
        // tclass,propertyDescriptor.getName()).setAccessible(true);
        if (propertyDescriptor.getReadMethod().isAnnotationPresent(WebRelation.class)) {
            return Optional.of(propertyDescriptor.getReadMethod().getAnnotation(WebRelation.class));
        } else {
            return (Optional<WebRelation>) fieldUtils.getField(tclass, propertyDescriptor.getName()).map(field->{
                if (fieldUtils.isAnnotationPresent(tclass, propertyDescriptor.getName(), WebRelation.class)) {
                    return Optional.of(field.getAnnotation(WebRelation.class));
                }else{
                    return Optional.empty();
                }
            }).orElse(Optional.empty());
        }
    }

    private static boolean escapeNonFields(PropertyDescriptor propertyDescriptor) {
        if ("class".equals(propertyDescriptor.getName())) {
            return true;
        }
        if ("accessible".equals(propertyDescriptor.getName())) {
            return true;
        }
        if ("annotatedType".equals(propertyDescriptor.getName())) {
            return true;
        }
        if ("empty".equals(propertyDescriptor.getName())) {
            return true;
        }
        return false;
    }

    public Optional<CustomInTableRenderer> getInTableRenderer(RelationContainer rc) {
        if (!rc.getWebRelation().customComponentInTable().equals(VoidCustomComponentInTableRenderer.class)) {
            CustomInTableRenderer ccitr = SpringBeanFactory.create(rc.getWebRelation().customComponentInTable());

            return Optional.of(ccitr);
        } else {
            return Optional.empty();
        }
    }

    public <T> Collection<Dual<Class<?>, Field>> getAllDependingClass(Class<T> child, EntityManagerFactory emf) {
        Collection<Dual<Class<?>, Field>> results = new ArrayList<>();
        List<Class<?>> allEntities = entityUtils.getAllEntities(emf);
        for (Class<?> pclass : allEntities) {
            for (Map.Entry<String, Field> mefield : fieldUtils.getAllFields(pclass).entrySet()) {
                Field pfield = mefield.getValue();
                if (Collection.class.isAssignableFrom(pfield.getType())) {
                    Class<?> pt = (Class) ((ParameterizedType) pfield.getGenericType()).getActualTypeArguments()[0];
                    if (pt.equals(child)) {
                        Dual<Class<?>, Field> dual = new Dual(pclass, pfield);
                        results.add(dual);
                    }
                } else {
                    if (pfield.getType().equals(child)) {
                        Dual<Class<?>, Field> dual = new Dual(pclass, pfield);
                        results.add(dual);
                    }
                }
            }
        }

        return results;
    }

    public <T> Path buildPath(Path<T> root, String[] fieldNames) {
        Path p = null;
        for (String fieldName : fieldNames) {
            if (p == null) {
                p = root.get(fieldName);
            } else {
                p = p.get(fieldName);
            }
        }
        return p;
    }

    public <T> Path buildPath(Path<T> root, List<String> fieldNames) {
        Path p = null;
        for (String fieldName : fieldNames) {
            if (p == null) {
                p = root.get(fieldName);
            } else {
                p = p.get(fieldName);
            }
        }
        return p;
    }
    
    public <T> String buildPathAsString(List<String> fieldNames) {
        return String.join(".",fieldNames);
    }
}
