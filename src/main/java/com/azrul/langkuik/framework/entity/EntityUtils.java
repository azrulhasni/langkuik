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
package com.azrul.langkuik.framework.entity;

import com.azrul.langkuik.framework.field.FieldUtils;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author azrulm
 */
@Component("entityUtils")
public class EntityUtils implements Serializable {
    
     @Autowired
    FieldUtils fieldUtils;
    
   

    public List<Class<?>> getAllEntities(EntityManagerFactory emf) {
        List<Class<?>> managedClasses = new ArrayList<>();
        for (ManagedType<?> entity : emf.getMetamodel().getManagedTypes()) {
            Class<?> clazz = entity.getJavaType();
            if (clazz == null) {
                continue;
            }
            managedClasses.add(clazz);
        }
        return managedClasses;
    }
    
    public List<Class<?>> getAllEntitiesDescendantFrom(Class<?> rootClass,EntityManagerFactory emf) {
        List<Class<?>> managedClasses = new ArrayList<>();
        for (ManagedType<?> entity : emf.getMetamodel().getManagedTypes()) {
            Class<?> clazz = entity.getJavaType();
            if (clazz == null) {
                continue;
            }
            if (rootClass.isAssignableFrom(clazz)){
                managedClasses.add(clazz);
            }
        }
        return managedClasses;
    }

    public boolean isManagedEntity(Class<?> targetClass, EntityManager em) {
        for (ManagedType<?> entity : em.getEntityManagerFactory().getMetamodel().getManagedTypes()) {
            if (entity.getJavaType() == null) {
                continue;
            }
            if (entity.getJavaType().equals(targetClass)) {
                return true;
            }
        }
        return false;
    }

    public Optional<Class<?>> getManagedEntity(String className, EntityManagerFactory emf) {
        for (ManagedType<?> entity : emf.getMetamodel().getManagedTypes()) {
            if (entity.getJavaType() == null) {
                continue;
            }
            if (entity.getJavaType().getName().equals(className)) {
                return Optional.of(entity.getJavaType());
            }
        }
        return Optional.empty();
    }

    public WebEntityType getEntityType(Class targetClass) {
        if (targetClass.isAnnotationPresent(WebEntity.class)) {
            WebEntity webEntity = (WebEntity) targetClass.getAnnotation(WebEntity.class);
            if (webEntity != null) {
                return webEntity.type();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }


    public <C> Object getIdentifierValue(C entity, EntityManager em) {
        Object id = em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
        return (Long)id;
    }

//    public static String getIdentifierFieldName(Class<?> entityClass, EntityManagerFactory emf) {
//        EntityManager em = emf.createEntityManager();
//        try {
//            Metamodel metamodel = em.getMetamodel();
//            EntityType entity = metamodel.entity(entityClass);
//            Set<SingularAttribute> singularAttributes = entity.getSingularAttributes();
//            for (SingularAttribute singularAttribute : singularAttributes) {
//                if (singularAttribute.isId()) {
//                    return singularAttribute.getName();
//                }
//            }
//        } finally {
//            em.close();
//        }
//        return null;
//    }
    public <T> String getIdentifierFieldName(Class<T> entityClass, EntityManager em) {

        Metamodel metamodel = em.getMetamodel();
        EntityType entity = metamodel.entity(entityClass);
        Set<SingularAttribute> singularAttributes = entity.getSingularAttributes();
        for (SingularAttribute singularAttribute : singularAttributes) {
            if (singularAttribute.isId()) {
                return singularAttribute.getName();
            }
        }

        return null;
    }
    
    

    public <T> Optional<String> getIdentifierSearchFieldName(Class<T> entityClass, EntityManager em) {

        Metamodel metamodel = em.getMetamodel();
        EntityType entity = metamodel.entity(entityClass);
        Set<SingularAttribute> singularAttributes = entity.getSingularAttributes();
        for (SingularAttribute singularAttribute : singularAttributes) {
            if (singularAttribute.isId()) {
                try {
                    return fieldUtils.getSearchFieldForSorting(entityClass, singularAttribute.getName());
                } catch (SecurityException ex) {
                    Logger.getLogger(EntityUtils.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
        return Optional.empty();
    }

    public <T> T createNewObject(Class<T> tclass) {
        try {
            return tclass.getConstructor(new Class[]{}).newInstance(new Object[]{});
        } catch (InstantiationException
                | IllegalAccessException
                | IllegalArgumentException
                | InvocationTargetException
                | NoSuchMethodException
                | SecurityException ex) {
            Logger.getLogger(EntityUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public <T> List<T> createNewObject(Class<T> tclass, int number) {
        List<T> result = new ArrayList<>();
        for (int i=0;i<number;i++){
            result.add(createNewObject(tclass));
        }
        return result;
    }

//    public static <T> String getTranxId(T bean) {
//        if (bean == null) {
//            return null;
//        }
//        if (Element.class.isAssignableFrom(bean.getClass())) {
//            Element l = (Element) bean;
//            return l.getTranxId();
//        }
//        return null;
//    }
//
//    public static <T> T setTranxId(T bean) {
//        String tranxId = Generators.timeBasedGenerator().generate().toString();
//        return setTranxId(bean, tranxId);
//    }
//    public static <T> T setTranxId(T bean, String tranxId) {
//        if (Element.class.isAssignableFrom(bean.getClass())) {
//            Element l = (Element) bean;
//            if (l.getTranxId() == null) {
//                l.setTranxId(tranxId);
//                return (T) l;
//            } else {
//                return null;
//            }
//        } else {
//            return bean;
//        }
//    }
}
