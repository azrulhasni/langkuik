/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.dao.query;

import com.azrul.langkuik.framework.dao.DAOQuery;
import com.azrul.langkuik.framework.dao.filter.AndFilters;
import static com.azrul.langkuik.framework.dao.filter.FilterRelation.EQUAL;
import com.azrul.langkuik.framework.entity.EntityUtils;
import com.azrul.langkuik.framework.dao.params.FindRelationParameter;
import com.azrul.langkuik.framework.dao.filter.QueryFilter;
import com.azrul.langkuik.framework.dao.params.QueryParams;
import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.framework.factory.SpringBeanFactory;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import com.azrul.langkuik.framework.field.FieldUtils;
import com.azrul.langkuik.framework.entity.Status;
import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.exception.QueryException;
import com.azrul.langkuik.framework.relationship.RelationUtils;
import com.azrul.langkuik.framework.standard.Castor;
import com.azrul.langkuik.framework.standard.Dual;
import com.diffplug.common.base.Errors;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Optional;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import org.hibernate.search.backend.elasticsearch.ElasticsearchExtension;
import org.hibernate.search.engine.search.query.dsl.SearchQueryOptionsStep;
import org.hibernate.search.engine.search.sort.dsl.CompositeSortComponentsStep;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.search.loading.dsl.SearchLoadingOptionsStep;
import org.hibernate.search.mapper.orm.session.SearchSession;

/**
 *
 * @author azrulm
 */
public class FindRelationQuery<P, C> implements DAOQuery<P, C>, Serializable {

    //private EntityManagerFactory emf;
    private FindRelationParameter<P, C> parameter;

    private RelationUtils relationUtils = SpringBeanFactory.create(RelationUtils.class);

    public FindRelationQuery(FindRelationParameter<P, C> parameter) {
        this.parameter = parameter;
    }

    @Override
    public Collection doQuery(EntityManager em,
            List<Dual<String[], Boolean>> sortFieldsAsc,
            Optional<Integer> startIndex,
            Optional<Integer> pageSize,
            Optional<String> tenantId,
            Optional<QueryParams> oQueryParams) {

        //this.emf = emf; 
        if (getParameter().getSearchText().isEmpty()) {
            return queryDependants(em,
                    getParameter().getAndFilter(),
                    getParameter().getParentObject(),
                    getParameter().getChildClass(),
                    getParameter().getParentToCurrentField(),
                    sortFieldsAsc,
                    startIndex.orElse(0),
                    pageSize,
                    tenantId);
        } else {
            return searchDependants(getParameter().getSearchText(),
                    em,
                    getParameter().getAndFilter(),
                    getParameter().getParentObject(),
                    getParameter().getChildClass(),
                    sortFieldsAsc,
                    startIndex.orElse(0),
                    pageSize,
                    tenantId);
        }
    }

    @Override
    public Long count(EntityManager em,
            Optional<String> tenantId,
            Optional<QueryParams> oQueryParams) {
        //this.emf = emf;
        if (getParameter().getSearchText().isEmpty()) {
            return countQueryDependants(em,
                    getParameter().getAndFilter(),
                    getParameter().getParentObject(),
                    getParameter().getChildClass(),
                    getParameter().getParentToCurrentField());
        } else {
            return countSearchDependants(em,
                    getParameter().getSearchText(),
                    getParameter().getAndFilter(),
                    getParameter().getParentObject(),
                    getParameter().getChildClass(),
                    tenantId,
                    oQueryParams);
        }
    }

    private Collection<C> queryDependants(EntityManager em,
            Optional<AndFilters> andFilters,
            P parentObject,
            Class<C> childClass,
            String parentFieldName,
            List<Dual<String[], Boolean>> sortFieldsAsc,
            Integer startIndex,
            Optional<Integer> pageSize,
            Optional<String> oTenantId) {

        RelationUtils relationUtils = SpringBeanFactory.create(RelationUtils.class);
        FieldUtils fieldUtils = SpringBeanFactory.create(FieldUtils.class);

        Optional<Collection> embeddableFields = handleEmbeddables(parentObject, parentFieldName, fieldUtils);
        if (!embeddableFields.isEmpty()) {
            return embeddableFields.get();
        }

        CriteriaBuilder cb = em.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<C> criteria = cb.createQuery(childClass);
        Root parent = criteria.from(parentObject.getClass());
        Join join = parent.join(parentFieldName);
        Collection<C> results = new ArrayList<>();

        List<Order> orders = new ArrayList<>();
        sortFieldsAsc.forEach(d -> {
            List<String> sf = List.of(d.getFirst());
            Boolean asc = d.getSecond();
            if (Boolean.TRUE.equals(asc)) {
                orders.add(cb.asc(relationUtils.buildPath(join, sf)));
            } else {
                orders.add(cb.desc(relationUtils.buildPath(join, sf)));
            }
        });

        List<Predicate> predicates = new ArrayList<>();

        if (!orders.isEmpty()) {
            Castor.<C, Element>given(childClass)
                    .castItTo(Element.class)
                    .thenDo(() -> {
                        predicates.add(cb.equal(parent, parentObject));
                        predicates.add(cb.not(cb.equal(join.get("status"), Status.CANCELLED)));
                        predicates.add(filterToPredicate(join, andFilters, cb));
                    }).failingWhichDo(
                    () -> {
                        predicates.add(cb.equal(parent, parentObject));
                        predicates.add(filterToPredicate(join, andFilters, cb));
                    }).go();
        } else {
            Castor.<C, Element>given(parameter.getChildClass())
                    .castItTo(Element.class)
                    .thenDo(() -> {
                        predicates.add(cb.equal(parent, parentObject));
                        predicates.add(cb.not(cb.equal(join.get("status"), Status.CANCELLED)));
                        predicates.add(filterToPredicate(join, andFilters, cb));
                    }).failingWhichDo(
                    () -> {
                        predicates.add(cb.equal(parent, parentObject));
                        predicates.add(filterToPredicate(join, andFilters, cb));
                    }).go();
        }

        //filter by tenant
        oTenantId.ifPresent(
                tenantId -> {
                    predicates.add(cb.equal(join.get("tenant"), tenantId));
                }
        );

        //add all predicates
        Predicate[] token = new Predicate[1];
        if (!predicates.isEmpty()) {
            criteria.select(join).where(
                    cb.and(predicates.toArray(token))).orderBy(orders);
        }

        //do query
        results = pageSize.map(o -> {
            return em.createQuery(criteria)
                    .setFirstResult(startIndex)
                    .setMaxResults(o)
                    .getResultList();
        }).orElseGet(() -> {
            return em.createQuery(criteria).getResultList();
        });
        em.close();
        return results;
    }

    private Optional<Collection> handleEmbeddables(P parentObject, String parentFieldName, FieldUtils fieldUtils) {
        return (Optional<Collection>) fieldUtils.getField(parentObject.getClass(), parentFieldName).map(parentField -> {
            if (relationUtils.isEmbedable(parentField.getType())) {
                try {
                    parentField.setAccessible(true);
                    var fieldContent = parentField.get(parentObject);
                    if (Collection.class.isAssignableFrom(fieldContent.getClass())) {
                        return Optional.of((Collection) fieldContent);
                    } else {
                        if (fieldContent == null) {
                            return Optional.of(List.of());
                        } else {
                            return Optional.of((Collection) List.of(fieldContent));
                        }
                    }
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(FindRelationQuery.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(FindRelationQuery.class.getName()).log(Level.SEVERE, null, ex);
                }
                return Optional.of(List.of());
            } else {
                return Optional.empty();
            }

        }).orElse(Optional.empty());
    }

    private Long countSearchDependants(EntityManager em,
            Optional<String> queryString,
            Optional<AndFilters> andFilters,
            P parentObject,
            Class<C> childClass,
            Optional<String> tenantId,
            Optional<QueryParams> oQueryParams) throws QueryException {

        SearchSession ftem = Search.session(em);
        var query = buildQueryFromQueryString(queryString,
                ftem,
                tenantId,
                andFilters,
                parentObject,
                childClass);
        try {
            return query.fetchTotalHitCount();
        } catch (Exception e) {
            throw new QueryException(e, "Problem with query format");
        }

    }

    private Long countQueryDependants(EntityManager em,
            Optional<AndFilters> andFilters,
            P parentObject,
            Class<C> childClass,
            String parentFieldName
    ) {
        //EntityManager em = emf.createEntityManager();
        EntityUtils entityUtils = SpringBeanFactory.create(EntityUtils.class);
        FieldUtils fieldUtils = SpringBeanFactory.create(FieldUtils.class);
        //RelationUtils relationUtils = SpringBeanFactory.create(RelationUtils.class);

        Optional<Collection> embeddableFields = handleEmbeddables(parentObject, parentFieldName, fieldUtils);
        if (!embeddableFields.isEmpty()) {
            return Integer.toUnsignedLong(embeddableFields.get().size());
        }

        return fieldUtils.getField(parentObject.getClass(), parentFieldName).map(parentField -> {
            Long count = null;
            try {

                //Handle 1 to 1 relationship
                if (!(parentField.getGenericType() instanceof ParameterizedType)) {
                    Class classOfField = parentField.getType();
                    String childId = entityUtils.getIdentifierFieldName(classOfField, em);

                    CriteriaBuilder cb = em.getCriteriaBuilder();
                    javax.persistence.criteria.CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
                    Root parent = criteria.from(parentObject.getClass());
                    Path countJoin = parent.join(parentFieldName);
                    Castor.<C, Element>given(childClass)
                            .castItTo(Element.class)
                            .thenDo(() -> {

                                criteria.select(
                                        cb.count(
                                                countJoin.get(childId))).where(
                                        cb.and(
                                                cb.equal(parent, parentObject),
                                                cb.not(
                                                        cb.equal(countJoin.get("status"), Status.CANCELLED)
                                                ),
                                                filterToPredicate(countJoin, andFilters, cb)
                                        )
                                );
                            }).failingWhichDo(() -> {
                        criteria.select(cb.count(countJoin.get(childId))).where(
                                cb.and(
                                        cb.equal(parent, parentObject),
                                        filterToPredicate(countJoin, andFilters, cb)
                                )
                        );
                    }).go();
                    count = em.createQuery(criteria).getSingleResult();
                    em.close();
                    if (count != null) {
                        return count;
                    } else {
                        return 0L;
                    }

                } else {
                    Class classOfField = (Class) ((ParameterizedType) parentField.getGenericType()).getActualTypeArguments()[0];

                    String childId = entityUtils.getIdentifierFieldName(classOfField, em);

                    CriteriaBuilder cb = em.getCriteriaBuilder();
                    javax.persistence.criteria.CriteriaQuery<Long> countCriteria = cb.createQuery(Long.class);
                    Root parentForCounting = countCriteria.from(parentObject.getClass());
                    Join countJoin = parentForCounting.join(parentFieldName);
                    Castor.<C, Element>given(childClass)
                            .castItTo(Element.class)
                            .thenDo(() -> {
                                countCriteria.select(
                                        cb.count(
                                                countJoin.get(childId))).where(
                                        cb.and(
                                                parentForCounting.in(parentObject),
                                                cb.not(
                                                        cb.equal(countJoin.get("status"), Status.CANCELLED)
                                                ),
                                                filterToPredicate(countJoin, andFilters, cb)
                                        )
                                );
                            }).failingWhichDo(() -> {
                        countCriteria.select(cb.count(countJoin.get(childId))).where(
                                cb.and(
                                        parentForCounting.in(parentObject),
                                        filterToPredicate(countJoin, andFilters, cb)
                                )
                        );
                    }).go();

                    count = em.createQuery(countCriteria).getSingleResult();
                    em.close();
                    if (count != null) {
                        return count;
                    } else {
                        return 0L;
                    }
                }
            } catch (Exception ex) {
                em.close();
                Logger.getLogger(FindRelationQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
            return count;
        }).orElse(0l);
    }

    private Collection<C> searchDependants(
            Optional<String> queryString,
            EntityManager em,
            Optional<AndFilters> andFilters,
            P parentObject,
            Class<C> searchClass,
            List<Dual<String[], Boolean>> sortFieldsAsc,
            Integer startIndex,
            Optional<Integer> pageSize,
            Optional<String> tenantId) throws QueryException {

        //EntityManager em = emf.createEntityManager();
        EntityUtils entityUtils = SpringBeanFactory.create(EntityUtils.class);
        FieldUtils fieldUtils = SpringBeanFactory.create(FieldUtils.class);
        String currentIdField = entityUtils.getIdentifierFieldName(searchClass, em);
        SearchSession ftem = Search.session(em);
        var query = buildQueryFromQueryString(queryString,
                ftem,
                tenantId,
                andFilters,
                parentObject,
                searchClass);

        if (!sortFieldsAsc.isEmpty()) {
            query.sort(f -> {
                CompositeSortComponentsStep comp = f.composite();
                sortFieldsAsc.forEach(d -> {
                    String sortField = String.join(".", d.getFirst());

                    Boolean asc = d.getSecond();
                    String orderBySearchField = fieldUtils.getSearchFieldForSorting(searchClass, sortField).orElse(currentIdField);
                    if (asc) {
                        comp.add(f.field(orderBySearchField).asc());
                    } else {
                        comp.add(f.field(orderBySearchField).desc());
                    }
                });
                return comp;
            });
        } else {
            query.sort(f -> f.score());
        }

        return pageSize.map(Errors.rethrow().wrapFunction(p -> {
            try {
                return query.fetchHits(startIndex, p);
            } catch (Exception e) {
                throw new QueryException(e, "Problem with query format");
            }
        })).orElse(query.fetchHits(startIndex));

    }

    /**
     * @return the parameter
     */
    public FindRelationParameter<P, C> getParameter() {
        return parameter;
    }

    private Predicate filterToPredicate(Path path,
            Optional<AndFilters> andFilters,
            CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        andFilters.ifPresent(af -> {
            for (QueryFilter filter : af.getFilters()) {
                if (filter.getRelation().equals(EQUAL)) {
                    if (filter.getValue() != null) {
                        predicates.add(cb.equal(relationUtils.buildPath(path, filter.getFieldNames()), filter.getValue()));
                    } else {
                        predicates.add(cb.isNull(relationUtils.buildPath(path, filter.getFieldNames())));
                    }
                } else {
                    if (filter.getValue() != null) {
                        predicates.add(cb.not(cb.equal(relationUtils.buildPath(path, filter.getFieldNames()), filter.getValue())));
                    } else {
                        predicates.add(cb.isNotNull(relationUtils.buildPath(path, filter.getFieldNames())));
                    }
                }
            }
        });
        return cb.and(predicates.toArray(new Predicate[]{}));

    }

    /**
     * @param parameter the parameter to set
     */
    public void setParameter(FindRelationParameter<P, C> parameter) {
        this.parameter = parameter;
    }

    private SearchQueryOptionsStep<?, C, SearchLoadingOptionsStep, ?, ?> buildQueryFromQueryString(
            Optional<String> myquery,
            SearchSession ftem,
            Optional<String> tenantId,
            Optional<AndFilters> andFilter,
            P parentObject,
            Class<C> searchClass) {

        var query = ftem.search(searchClass)
                .extension(ElasticsearchExtension.get())
                .where(f -> f.bool(
                b -> {
                    JsonObject joQuery = new JsonObject();
                    myquery.ifPresentOrElse(s
                            -> joQuery.addProperty("query", s.trim()),
                            () -> joQuery.addProperty("query", ""));

                    JsonObject joQueryStr = new JsonObject();
                    joQueryStr.add("query_string", joQuery);

                    JsonArray jaQueryStr = new JsonArray();
                    jaQueryStr.add(joQueryStr);
                    //--------
                    Castor.<P, Element>given(parentObject)
                            .castItTo(Element.class)
                            .thenDo(p -> {
                                JsonObject joQuery2 = new JsonObject();
                                joQuery2.addProperty("enumPath", p.getEnumPath() + "*");

                                JsonObject joQueryStr2 = new JsonObject();
                                joQueryStr2.add("wildcard", joQuery2);

                                jaQueryStr.add(joQueryStr2);
                            }).go();
                    //-------

                    JsonObject joMust = new JsonObject();
                    joMust.add("must", jaQueryStr);

                    JsonObject joBool = new JsonObject();
                    joBool.add("bool", joMust);

                    b.must(f.fromJson(joBool));

                    //deal with and filters
                    andFilter.ifPresent(af -> {
                        for (QueryFilter filter : af.getFilters()) {
                            if (filter.getRelation().equals(EQUAL)) {
                                if (filter.getValue() != null) {
                                    b.must(
                                            f.match().field(
                                                    relationUtils.buildPathAsString(filter.getFieldNames()))
                                                    .matching(filter.getValue()));
                                }
                            } else {
                                if (filter.getValue() != null) {
                                    b.mustNot(
                                            f.match().field(
                                                    relationUtils.buildPathAsString(filter.getFieldNames()))
                                                    .matching(filter.getValue()));
                                }
                            }
                        }
                    });

                    //deal with tenant
                    tenantId.ifPresent(t -> {
                        b.must(f.match().field("tenant").matching(t));
                    });
                    b.mustNot(f.match().field("status").matching(Status.CANCELLED));

                    //deal with path enumeration
                    //only match items belongging to parent
//                    Castor.<P, Element>given(parentObject)
//                            .castItTo(Element.class)
//                            .thenDo(p -> {
//
//                                b.must(f.match().field("enumPath").matching(p.getEnumPath() + "*"));
//                            }).go();

                }));

        return query;
    }

}
