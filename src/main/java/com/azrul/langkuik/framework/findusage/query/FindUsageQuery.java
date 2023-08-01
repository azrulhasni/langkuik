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
package com.azrul.langkuik.framework.findusage.query;

import com.azrul.langkuik.framework.dao.DAOQuery;
import com.azrul.langkuik.framework.entity.EntityUtils;
import com.azrul.langkuik.framework.dao.params.QueryParams;
import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.framework.factory.SpringBeanFactory;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import com.azrul.langkuik.framework.entity.Status;
import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.relationship.RelationUtils;
import com.azrul.langkuik.framework.standard.Castor;
import com.azrul.langkuik.framework.standard.Dual;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author azrulm
 * @param <T>
 */
public class FindUsageQuery<P, C> implements DAOQuery<P, C>, Serializable {

    private C currentBean;
    private Class<P> parentClass;
    private String parentCurrentRelation;

    private RelationUtils relationUtils = SpringBeanFactory.create(RelationUtils.class);

    //private EntityManagerFactory emf;
    public FindUsageQuery(C currentBean, String parentCurrentRelation, Class<P> parentClass) {
        //this.emf = emf;
        this.currentBean = currentBean;
        this.parentClass = parentClass;
        this.parentCurrentRelation = parentCurrentRelation;
    }

    public FindUsageQuery() {

    }

    @Override
    public Collection doQuery(EntityManager em,
            List<Dual<String[], Boolean>> sortFieldsAsc,
            Optional<Integer> startIndex,
            Optional<Integer> pageSize,
            Optional<String> tenantId,
            Optional<QueryParams> oQueryParams) {
        //this.emf = emf;
        return findUsage(em,
                getCurrentBean(),
                getParentCurrentRelation(),
                getParentClass(),
                sortFieldsAsc,
                startIndex.orElse(0),
                pageSize);
    }

    @Override
    public Long count(EntityManager em,
            Optional<String> tenantId,
            Optional<QueryParams> oQueryParams) {
        // this.emf = emf;
        return countUsage(em,
                getCurrentBean(),
                getParentCurrentRelation(),
                getParentClass());
    }

    /**
     * @return the currentBean
     */
    private C getCurrentBean() {
        return currentBean;
    }

    /**
     * @param currentBean the currentBean to set
     */
    private void setCurrentBean(C currentBean) {
        this.currentBean = currentBean;
    }

    /**
     * @return the parentClass
     */
    private Class<P> getParentClass() {
        return parentClass;
    }

    /**
     * @param parentClass the parentClass to set
     */
    private void setParentClass(Class<P> parentClass) {
        this.parentClass = parentClass;
    }

    private Set<P> findUsage(EntityManager em,
            C currentBean,
            String parentCurrentRelation,
            Class<P> parentClass,
            List<Dual<String[], Boolean>> sortFieldsAsc,
            Integer startIndex,
            Optional<Integer> pageSize) {
        EntityUtils entityUtils = SpringBeanFactory.create(EntityUtils.class);

        Set<P> results = new HashSet<>();
//        List<Field> fields = RelationUtils.getParentChildFields(parentClass, currentBean.getClass()); //order is important. Current entity is always get pointed by = child
//        if (fields.isEmpty() == false) {
//            // EntityManager em = emf.createEntityManager();
//
//            for (Field field : fields) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<P> cq = cb.createQuery(parentClass);
        Root root = cq.from(parentClass);
        Join join = root.join(parentCurrentRelation);
        Castor.<C,Element>given(currentBean)
                    .castItTo(Element.class)
                    .thenDo(e->{
        
            cq.where(
                    cb.and(
                            cb.equal(
                                    join.get(
                                            entityUtils.getIdentifierFieldName(currentBean.getClass(), em)
                                    ),
                                    entityUtils.getIdentifierValue(currentBean, em)
                            ),
                            cb.not(cb.equal(root.get("status"), Status.CANCELLED))
                    )
            );
        }).go().orElseGet(()->{
            cq.where(
                    cb.equal(
                            join.get(
                                    entityUtils.getIdentifierFieldName(currentBean.getClass(), em)
                            ),
                            entityUtils.getIdentifierValue(currentBean, em)
                    )
            );
            return null;
        });

//        orderBy.ifPresentOrElse(o -> {
//            Order order = null;
//            if (asc) {
//                order = cb.asc(root.get(o));
//            } else {
//                order = cb.desc(root.get(o));
//            }
//            cq.orderBy(order);
//        }, () -> {
//            Order order = null;
//            if (asc) {
//                order = cb.asc(root.get(EntityUtils.getIdentifierFieldName(parentClass, em)));
//            } else {
//                order = cb.desc(root.get(EntityUtils.getIdentifierFieldName(parentClass, em)));
//            }
//            cq.orderBy(order);
//        });
        List<Order> orders = new ArrayList<>();
        sortFieldsAsc.forEach(d -> {
            String[] sortField = d.getFirst();
            Boolean asc = d.getSecond();
            if (Boolean.TRUE.equals(asc)) {
                orders.add(cb.asc(relationUtils.buildPath(root, sortField)));
            } else {
                orders.add(cb.desc(relationUtils.buildPath(root, sortField)));
            }
        });

        if (!orders.isEmpty()) {
            cq.orderBy(orders);
        } else {
            cq.orderBy(cb.asc(root.get(entityUtils.getIdentifierFieldName(parentClass, em))));
        }

        //do query
        results.addAll(
                pageSize.map(o -> {
                    return em.createQuery(cq).setFirstResult(startIndex).setMaxResults(o).getResultList();
                }).orElseGet(() -> {
                    return em.createQuery(cq).setFirstResult(startIndex).getResultList();
                })
        );
//            }

        em.close();
//        }
        return results;

    }

    private Long countUsage(EntityManager em,
            C currentBean,
            String parentCurrentRelation,
            Class<P> parentClass) {
        Long count = null;
//        List<Field> fields = RelationUtils.getParentChildFields(parentClass, currentBean.getClass()); //order is important. Current entity is always get pointed by = child
//        if (fields.isEmpty() == false) {
        //EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.TYPE);
        EntityUtils entityUtils = SpringBeanFactory.create(EntityUtils.class);

        Root root = cq.from(parentClass);
        cq.select(cb.count(root.get(entityUtils.getIdentifierFieldName(parentClass, em))));

        //  for (Field field : fields) {
        Join join = root.join(parentCurrentRelation);
        Castor.<C,Element>given(currentBean)
                    .castItTo(Element.class)
                    .thenDo(e->{
            cq.where(
                    cb.and(
                            cb.equal(
                                    join.get(
                                            entityUtils.getIdentifierFieldName(currentBean.getClass(), em)
                                    ),
                                    entityUtils.getIdentifierValue(currentBean, em)
                            ),
                            cb.not(
                                    cb.or(
                                            cb.equal(root.get("status"), Status.CANCELLED)
                                    )
                            )
                    )
            );
        }).failingWhichDo(()-> {
            cq.where(
                    cb.equal(
                            join.get(
                                    entityUtils.getIdentifierFieldName(currentBean.getClass(), em)
                            ),
                            entityUtils.getIdentifierValue(currentBean, em)
                    )
            );
        }).go();
        //}

        count = em.createQuery(cq).getSingleResult();
//        }
        if (count != null) {
            return count;
        } else {
            return 0L;
        }
    }

    /**
     * @return the parentCurrentRelation
     */
    public String getParentCurrentRelation() {
        return parentCurrentRelation;
    }

    /**
     * @param parentCurrentRelation the parentCurrentRelation to set
     */
    public void setParentCurrentRelation(String parentCurrentRelation) {
        this.parentCurrentRelation = parentCurrentRelation;
    }

}
