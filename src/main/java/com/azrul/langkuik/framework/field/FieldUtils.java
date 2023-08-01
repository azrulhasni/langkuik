/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.field;

import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.framework.entity.WebEntity;
import com.azrul.langkuik.framework.entity.WebEntityType;
import com.azrul.langkuik.framework.expression.Expression;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.hibernate.envers.Audited;
import org.hibernate.search.engine.backend.types.Searchable;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author azrul
 */
@Component("fieldUtils")
public class FieldUtils {

    @Autowired
    Expression expr;

    public Map<String, Field> getAllFields(Class<?> type) {
        Map<String, Field> fields = new HashMap<>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            for (Field field : Arrays.asList(c.getDeclaredFields())) {
                fields.put(field.getName(), field);
            }
        }
        return fields;
    }
    
    public Map<String, Field> getAllFieldsByDisplayName(Class<?> type) {
        Map<String, Field> fields = new HashMap<>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            for (Field field : Arrays.asList(c.getDeclaredFields())) {
                getWebField(type,field.getName()).ifPresent(wf->{
                    if (wf.displayName().equals("")){
                        fields.put(field.getName(), field);
                    }else{
                         fields.put(wf.displayName(), field);
                    }
                }); 
            }
        }
        return fields;
    }

    public boolean classContainsField(Class<?> type, String fieldName) {
        return getField(type, fieldName).map(field -> {
            return Boolean.TRUE;
        }).orElse(Boolean.FALSE);
    }

    public Optional<WebField> getWebField(Class<?> type, String fieldName) {
        return getField(type, fieldName).map(field -> {
            return field.getAnnotation(WebField.class);
        }).or(() -> Optional.empty());
    }
    
    public <T> Optional<String> getReferenceOwnerField(Class<T> entityClass){
        WebEntity we = entityClass.getAnnotation(WebEntity.class);
        if (we==null) return Optional.empty();
        
        if (we.type() != WebEntityType.REF) return Optional.empty();
        
        var fields = getFieldsByOrderRegardlessVisibility(entityClass);
        for (var fc:fields.values()){
            if (fc.getWebField().referenceOwnerField()){
                return Optional.of(fc.getField().getName());
            }
        }
        return Optional.empty();
    }

    public Optional<WebEntity> getWebEntity(Class<?> type) {
        return Optional.ofNullable(type.getAnnotation(WebEntity.class));
    }

    public Optional getAnnotation(Class type, Class annotationClass) {
        return Optional.ofNullable(type.getAnnotation(annotationClass));
    }

    public Optional getAnnotation(Class<?> type, String fieldName, Class annotationClass) {
        return getField(type, fieldName).map(field -> {
            return field.getAnnotation(annotationClass);
        }).or(() -> Optional.empty());
    }

    public Optional<Field> getField(Class<?> type, String fieldName) {

        if (type == null) {
            return Optional.empty();
        }
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            Field[] fields = c.getDeclaredFields();
            for (Field f : fields) {
                if (f.getName().equals(fieldName)) {
                    return Optional.of(f);
                }
            }
        }
        return Optional.empty();

    }

    public <V, T> Optional<V> getValue(T bean, String fieldName) {
        if (bean == null) {
            return Optional.empty();
        }
        return (Optional<V>) getField(bean.getClass(), fieldName).map(field -> {
            try {
                field.setAccessible(true);
                return Optional.ofNullable((V) field.get(bean));
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(FieldUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
            return Optional.empty();
        }).orElse(Optional.empty());
    }

    public <V, T> void setValue(T bean, Field field, V value) {
        try {
            field.setAccessible(true);
            field.set(bean, value);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(FieldUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public <V, T> void setValue(T bean, String fieldName, V value) {
        if (bean == null) {
            return;
        }
        getField(bean.getClass(), fieldName).ifPresent(field -> {
            try {
                field.setAccessible(true);
                field.set(bean, value);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(FieldUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    public <T> MultiValuedMap<Integer, FieldContainer> getFieldsByOrderRegardlessVisibility(Class<T> tclass) {
        return this.getFieldsByOrder(tclass, null, false, true,false);
    }

    public <T> MultiValuedMap<Integer, FieldContainer> getFieldsByOrder(Class<T> tclass) {
        return this.getFieldsByOrder(tclass, null, false, true, true);
    }

    public <T> MultiValuedMap<Integer, FieldContainer> getFieldsByOrder(T bean) {
        return this.getFieldsByOrder((Class<T>) bean.getClass(), bean, false, false, true);
    }

    public <T> MultiValuedMap<Integer, FieldContainer> getFieldsByOrder(Class<T> tclass, T bean, boolean onlyAuditedFields, boolean forTable, boolean considerVisibility) {
        //Field store
        MultiValuedMap<Integer, FieldContainer> fieldStore = new ArrayListValuedHashMap<>();
        try {

            BeanInfo beanInfo = Introspector.getBeanInfo(tclass);
            for (java.beans.PropertyDescriptor propertyDescriptor
                    : beanInfo.getPropertyDescriptors()) {
                if ("class".equals(propertyDescriptor.getName())) {
                    continue;
                }
                if ("accessible".equals(propertyDescriptor.getName())) {
                    continue;
                }
                if ("annotatedType".equals(propertyDescriptor.getName())) {
                    continue;
                }
                if ("empty".equals(propertyDescriptor.getName())) {
                    continue;
                }

                if (onlyAuditedFields == true) {
                    boolean res = getField(tclass, propertyDescriptor.getName()).map(field -> {
                        if (!field.isAnnotationPresent(Audited.class)
                                && !propertyDescriptor.getReadMethod().isAnnotationPresent(Audited.class)) {
                            return Boolean.TRUE;
                        } else {
                            return Boolean.FALSE;
                        }
                    }).orElse(Boolean.FALSE);
                    if (Boolean.TRUE.equals(res)) {
                        continue;
                    }
                }

                Optional<WebEntity> oWebEntity = getWebEntity(tclass);

                //Get webfield annotation values
                Optional<WebField> oWebField = getWebFieldFromPropertyDescriptorOrField(propertyDescriptor, tclass);

                //Get sortable
                boolean isFieldSortable = this.getSearchFieldForSorting(tclass, propertyDescriptor.getName()).isPresent();

                //Add columns
                oWebEntity.ifPresent(webEntity -> {
                    oWebField.ifPresent(webField -> {

                        //Only display what is visible
                        Boolean showFlag = Boolean.FALSE;
                        if (considerVisibility == true) {
                            if (forTable == true) {
                                showFlag = getFieldVisibilityFromEntity(webEntity, propertyDescriptor.getName()).map(
                                        fv -> (Boolean) expr.evaluate(fv.visibleInTable(), tclass)
                                ).orElseGet(
                                        () -> (Boolean) expr.evaluate(webField.visibleInTable(), tclass)
                                );
                                //showFlag = (Boolean) expr.evaluate(webField.visibleInTable(), tclass);
                            } else {
                                showFlag = getFieldVisibilityFromEntity(webEntity, propertyDescriptor.getName()).map(
                                        fv -> (Boolean) expr.evaluate(fv.visibleInForm(), (Element) bean)
                                ).orElseGet(
                                        () -> (Boolean) expr.evaluate(webField.visibleInForm(), (Element) bean)
                                );
                            }
                        } else {
                            showFlag = Boolean.TRUE;
                        }
                        
                        
                        
                        if (showFlag == true) {

                            if (propertyDescriptor.getReadMethod() != null && propertyDescriptor.getReadMethod().isAnnotationPresent(WebField.class)) {
                                Method getter = propertyDescriptor.getReadMethod();
                                //store field to be sorted
                                FieldContainer fc = new FieldContainer(webField, getter, isFieldSortable);
                                fieldStore.put(webField.order(), fc);

                            } else if (isAnnotationPresent(tclass, propertyDescriptor.getName(), WebField.class)) {
                                getField(tclass, propertyDescriptor.getName()).ifPresent(field -> {
                                    //store field to be sorted
                                    final FieldContainer fc = new FieldContainer(webField, field, isFieldSortable);
                                    fieldStore.put(webField.order(), fc);
                                });
                            }
                        }
                    });
                });
            }

        } catch (IntrospectionException ex) {
            Logger.getLogger(FieldUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fieldStore;
    }

    public Boolean isAnnotationPresent(Class tclass, String fieldName, Class annotationClass) {
        return getField(tclass, fieldName).map(field
                -> field.isAnnotationPresent(annotationClass)
        ).orElse(Boolean.FALSE);
    }

    public Boolean isCollection(Class tclass, String field) {
        return getField(tclass, field).map(f -> Collection.class.isAssignableFrom(f.getType())).orElse(Boolean.FALSE);
    }

    public <T> Optional<String> getEquivSearchField(Class<T> entityClass, String searchFieldName) {
        return (Optional<String>) this.getField(entityClass, searchFieldName).map(field -> {
            GenericField[] genericFields = field.getAnnotationsByType(GenericField.class);
            for (GenericField genericField : genericFields) {
                if (genericField.searchable().equals(Searchable.YES)) {
                    return Optional.of(genericField.name());
                }
            }
            KeywordField[] keywordFields = field.getAnnotationsByType(KeywordField.class);
            for (KeywordField keywordField : keywordFields) {
                if (keywordField.searchable().equals(Searchable.YES)) {
                    return Optional.of(keywordField.name());
                }
            }
            return Optional.empty();
        }).orElseGet(() -> Optional.empty());

    }

    public <T> Optional<String> getSearchFieldForSorting(Class<T> entityClass, String[] searchFieldNames) {
        Class currentClass = entityClass;
        int lastIndex = searchFieldNames.length - 1;
        for (int i = 0; i < searchFieldNames.length; i++) {
            if (i == lastIndex) {
                if (getSearchFieldForSorting(currentClass, searchFieldNames[i]).isPresent()) {
                    return Optional.of(String.join(".", searchFieldNames));
                } else {
                    return Optional.empty();
                }
            } else {
                currentClass = (Class) this.getField(currentClass, searchFieldNames[i]).map(field -> field.getType()).orElseThrow();
            }
        }
        return Optional.empty();
    }

    public <T> Optional<String> getSearchFieldForSorting(Class<T> entityClass, String searchFieldName) {
        return (Optional<String>) this.getField(entityClass, searchFieldName).map(field -> {
            if (field != null) {
                if (field.isAnnotationPresent(KeywordField.class)) {
                    KeywordField f = field.getAnnotation(KeywordField.class);
                    if (f.sortable().equals(Sortable.YES)) {
                        return Optional.of(field.getName());
                    }
                } else if (field.isAnnotationPresent(GenericField.class)) {
                    GenericField f = field.getAnnotation(GenericField.class);
                    if (f.sortable().equals(Sortable.YES)) {
                        return Optional.of(field.getName());
                    }
                }
            }
            return Optional.empty();
        }).orElse(Optional.empty());

    }

    public Optional<FieldVisibility> getFieldVisibilityFromEntity(WebEntity webEntity, String fieldName) {
        if (webEntity == null) {
            return Optional.empty();
        }
        for (FieldVisibility fv : webEntity.fieldVisibility()) {
            if (fv.fieldName().equals(fieldName)) {
                return Optional.of(fv);
            }
        }
        return Optional.empty();
    }

    public <T> Optional<FieldVisibility> getFieldVisibilityFromEntity(Class<T> tclass, String fieldName) {
        if (tclass.isAnnotationPresent(WebEntity.class)) {
            WebEntity webEntity = tclass.getAnnotation(WebEntity.class);
            return getFieldVisibilityFromEntity(webEntity, fieldName);
        } else {
            return Optional.empty();
        }

    }

    private <T> Optional<WebField> getWebFieldFromPropertyDescriptorOrField(PropertyDescriptor propertyDescriptor, Class<T> tclass) {
        if (propertyDescriptor.getReadMethod() != null && propertyDescriptor.getReadMethod().isAnnotationPresent(WebField.class)) {
            return Optional.of(propertyDescriptor.getReadMethod().getAnnotation(WebField.class));

        } else {
            if (getField(tclass, propertyDescriptor.getName()) != null) {
                if (isAnnotationPresent(tclass, propertyDescriptor.getName(), WebField.class)) {
                    return getWebField(tclass, propertyDescriptor.getName());
                } else {
                    return Optional.empty();
                }
            }
        }
        return Optional.empty();

    }
}
