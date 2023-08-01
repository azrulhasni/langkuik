/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.dao.query;

import com.azrul.langkuik.framework.dao.DAOQuery;
import com.azrul.langkuik.framework.dao.filter.AndFilters;
import static com.azrul.langkuik.framework.dao.filter.FilterRelation.EQUAL;
import com.azrul.langkuik.framework.dao.filter.QueryFilter;
import com.azrul.langkuik.framework.dao.params.StatusBasedQueryParams;
import com.azrul.langkuik.framework.factory.SpringBeanFactory;
import com.azrul.langkuik.framework.relationship.RelationUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author azrul
 */
public class LookupQuery<L, M> implements DAOQuery {

    private Class<L> lookupEntity;
    private String fieldName;
    private String filterByFieldName;
    private M matchingValue;
    private Boolean cacheReferenceData;
    private AndFilters andFilters;
    private RelationUtils relationUtils = SpringBeanFactory.create(RelationUtils.class);

    public LookupQuery(Class<L> lookupEntity,
            String fieldName,
            String filterByFieldName,
            M matchingValue,
            Boolean cacheReferenceData,
            AndFilters andFilters) {
        this.lookupEntity = lookupEntity;
        this.fieldName = fieldName;
        this.filterByFieldName = filterByFieldName;
        this.matchingValue = matchingValue;
        this.cacheReferenceData = cacheReferenceData;
        this.andFilters = andFilters;
    }

    public LookupQuery(Class<L> lookupEntity,
            String fieldName,
            Boolean cacheReferenceData,
            AndFilters andFilters
    ) {
        this.lookupEntity = lookupEntity;
        this.fieldName = fieldName;
        this.filterByFieldName = null;
        this.matchingValue = null;
        this.cacheReferenceData = cacheReferenceData;
        this.andFilters = andFilters;
    }

    public LookupQuery(Class<L> lookupEntity,
            Boolean cacheReferenceData,
            AndFilters andFilters) {
        this.lookupEntity = lookupEntity;
        this.fieldName = null;
        this.filterByFieldName = null;
        this.matchingValue = null;
        this.cacheReferenceData = cacheReferenceData;
        this.andFilters = andFilters;
    }

    @Override
    public Collection doQuery(EntityManager em,
            List sortFieldsAsc,
            Optional ostartIndex,
            Optional pageSize,
            Optional tenantId,
            Optional oQueryParams) {
        //Boolean asc = (Boolean) oasc.orElse(Boolean.TRUE);
        Integer startIndex = (Integer) ostartIndex.orElse(0);
        CriteriaBuilder cb = em.getCriteriaBuilder();

        if (fieldName != null) {

            List<Predicate> predicates = new ArrayList<>();

            javax.persistence.criteria.CriteriaQuery<String> criteria = cb.createQuery(String.class);

            Root<L> root = criteria.from(lookupEntity);
            oQueryParams.ifPresent(qp -> {
                if (qp instanceof StatusBasedQueryParams) {
                    StatusBasedQueryParams queryParams = (StatusBasedQueryParams) qp;
                    predicates.add(cb.equal(root.get("status"), queryParams.getStatus()));
                }
            });

            tenantId.ifPresent(tenant -> {
                predicates.add(cb.equal(root.get("tenant"), tenant));
            });

            if (matchingValue != null) {
                if (filterByFieldName != null) {
                    predicates.add(cb.equal(root.get(filterByFieldName), matchingValue));
                }
            }

            if (andFilters != null) {
                for (QueryFilter filter : andFilters.getFilters()) {
                    if (filter.getRelation().equals(EQUAL)) {
                        if (filter.getValue() != null) {
                            predicates.add(cb.equal(relationUtils.buildPath(root, filter.getFieldNames()), filter.getValue()));
                        } else {
                            predicates.add(cb.isNull(relationUtils.buildPath(root, filter.getFieldNames())));
                        }
                    } else {
                        if (filter.getValue() != null) {
                            predicates.add(cb.not(cb.equal(relationUtils.buildPath(root, filter.getFieldNames()), filter.getValue())));
                        } else {
                            predicates.add(cb.isNotNull(relationUtils.buildPath(root, filter.getFieldNames())));
                        }
                    }
                }
            }

            CriteriaQuery<String> query = criteria.select(root.get(fieldName))
                    .where(predicates.toArray(new Predicate[1]))
                    .distinct(true);

            return em.createQuery(query).setHint("org.hibernate.cacheable", cacheReferenceData)
                    .setFirstResult(startIndex).getResultList();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public Long count(EntityManager em,
            Optional tenantId,
            Optional oQueryParams) {
        return 0L;
    }

}
