/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.dao.query;

import com.azrul.langkuik.framework.dao.DAOQuery;
import com.azrul.langkuik.framework.entity.EntityUtils;
import com.azrul.langkuik.framework.dao.filter.AndFilters;
import static com.azrul.langkuik.framework.dao.filter.FilterRelation.EQUAL;
import com.azrul.langkuik.framework.dao.filter.QueryFilter;
import com.azrul.langkuik.framework.dao.params.CreatedByMeQueryParams;
import com.azrul.langkuik.framework.dao.params.OwnedByMeQueryParams;
import com.azrul.langkuik.framework.dao.params.OwnedByMyTeamQueryParams;
import com.azrul.langkuik.framework.dao.params.QueryParams;
import com.azrul.langkuik.framework.dao.params.StatusBasedQueryParams;
import com.azrul.langkuik.framework.dao.params.UnbookedWorkQueryParams;
import com.azrul.langkuik.framework.exception.QueryException;
import com.azrul.langkuik.framework.factory.SpringBeanFactory;
import com.azrul.langkuik.framework.field.FieldUtils;
import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.framework.entity.Status;
import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.relationship.RelationUtils;
import com.azrul.langkuik.framework.standard.Castor;
import com.azrul.langkuik.framework.standard.Dual;
import com.diffplug.common.base.Errors;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
public class FindAnyEntityQuery<T> implements DAOQuery<T, T>, Serializable {

    private Optional<String> queryString;
    private Class<T> searchClass;
    private Collection<T> exclusion;
    private String dateFormat;
    private AndFilters andFilters;

    @PersistenceUnit()
    EntityManagerFactory emf;

    private RelationUtils relationUtils = SpringBeanFactory.create(RelationUtils.class);

    //private Object parent = null;
    //private EntityManagerFactory emf;
    public FindAnyEntityQuery(Class<T> searchClass, String dateFormat) {
        this.searchClass = searchClass;
        this.queryString = Optional.empty();
        this.andFilters = null;
        this.dateFormat = dateFormat;
        this.exclusion = new ArrayList<>();
    }

    public FindAnyEntityQuery(Class<T> searchClass, String dateFormat, Collection<T> exclusion, Optional<AndFilters> oAndFilters) {
        this(searchClass, dateFormat);
        if (exclusion == null) {
            this.exclusion = new ArrayList<>();
        } else {
            this.exclusion = exclusion;
        }
        this.andFilters=oAndFilters.orElse(null);
    }

    @Override
    public Long count(EntityManager em,
            Optional<String> tenantId,
            Optional<QueryParams> oQueryParams) throws QueryException {
        EntityUtils entityUtils = SpringBeanFactory.create(EntityUtils.class);
        String currentIdField = entityUtils.<T>getIdentifierFieldName(searchClass, em);
        Long count = null;
        //if (searchTerms.isEmpty()) {

        count = queryString.map(Errors.rethrow().wrapFunction(s -> {
            return countSearch(em, (String) s, currentIdField, tenantId, oQueryParams);
        })).orElseGet(() -> {
            return countQuery(em, tenantId, oQueryParams);
        });

        if (count == null) {
            return 0L;
        } else {
            return count;
        }

    }

    private Long countSearch(EntityManager em,
            String queryString,
            String currentIdField,
            Optional<String> tenantId,
            Optional<QueryParams> oQueryParams) throws QueryException {

        SearchSession ftem = Search.session(em);
        var query = buildQueryFromQueryString(queryString,
                exclusion,
                Optional.ofNullable(andFilters),
                currentIdField,
                ftem,
                tenantId,
                oQueryParams);
        try {
            Long count =  query.fetchTotalHitCount();
            return count;
        } catch (Exception e) {
            throw new QueryException(e, "Problem with query format");
        }

    }

    private Collection<T> runQuery(EntityManager em,
            List<Dual<String[], Boolean>> sortFieldsAsc,
            Integer startIndex,
            Optional<Integer> pageSize,
            Optional<String> tenantId,
            Optional<QueryParams> oQueryParams) {
        //EntityManager em = emf.createEntityManager();

        Collection<T> results = new ArrayList<>();
        EntityUtils entityUtils = SpringBeanFactory.create(EntityUtils.class);

        String childId = entityUtils.<T>getIdentifierFieldName(searchClass, em);

        CriteriaBuilder cb = em.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<T> criteria = cb.createQuery(searchClass);

        Root<T> root = criteria.from(searchClass);

        List<Predicate> predicates = buildPredicates(tenantId, cb, root, oQueryParams);

        Predicate[] token = new Predicate[1];
        CriteriaQuery<T> query = criteria.select(root);
        if (!predicates.isEmpty()) {
            query.where(cb.and(predicates.toArray(token)));
        }

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
            query.orderBy(orders);
        } else {
            query.orderBy(cb.asc(root.get(childId)));
        }

        results = pageSize.map(o -> {
            return em.createQuery(query).setFirstResult(startIndex).setMaxResults(o).getResultList();
        }).orElseGet(() -> {
            return em.createQuery(query).setFirstResult(startIndex).getResultList();
        });
        //results = em.createQuery(query).setFirstResult(startIndex).setMaxResults(pageSize).getResultList();
        return results;
    }

    private List<Predicate> buildPredicates(Optional<String> tenantId, CriteriaBuilder cb, Root<T> root, Optional<QueryParams> oQueryParams) {
        //add order,tenant and worklist
        List<Predicate> predicates = new ArrayList<>();
        //deal with tenant
        tenantId.ifPresent(s -> {
            predicates.add(cb.equal(root.get("tenant"), s));
        });
        //deal with deleted
        Castor.<T,Element>given(searchClass)
                .castItTo(Element.class)
                .thenDo(()->{
//            predicates.add(cb.not(cb.equal(root.get("status"), Status.CANCELLED)));
//            predicates.add(cb.not(cb.equal(root.get("status"), Status.RETIRED)));

            //deal with worklist  
            oQueryParams.ifPresent(qp -> {
                Castor.<T,WorkElement>given(searchClass)
                .castItTo(WorkElement.class)
                .thenDo(()->{
                    if (qp instanceof OwnedByMyTeamQueryParams) {

                        OwnedByMyTeamQueryParams queryParams = (OwnedByMyTeamQueryParams) qp;
                        predicates.add(
                                cb.and(cb.equal(root.get("workflowInfo").get("worklist"), queryParams.getWorklist()),
                                        cb.isNotEmpty(root.get("workflowInfo").get("owners")),
                                        cb.isNotMember(queryParams.getExcludedUsername(), root.get("workflowInfo").get("owners"))
                                ));
                    } else if (qp instanceof OwnedByMeQueryParams) {
                        OwnedByMeQueryParams queryParams = (OwnedByMeQueryParams) qp;
                        predicates.add(cb.isMember(queryParams.getOwnerId(), root.get("workflowInfo").get("owners")));
                    } else if (qp instanceof UnbookedWorkQueryParams) {
                        UnbookedWorkQueryParams queryParams = (UnbookedWorkQueryParams) qp;
                        predicates.add(
                                cb.and(cb.equal(root.get("workflowInfo").get("worklist"), queryParams.getWorklist()),
                                        cb.isEmpty(root.get("workflowInfo").get("owners"))));
                    } 
                }).go();
                
                if (qp instanceof StatusBasedQueryParams) {
                    StatusBasedQueryParams queryParams = (StatusBasedQueryParams) qp;
                    predicates.add(cb.equal(root.get("status"), queryParams.getStatus()));
                } else if (qp instanceof CreatedByMeQueryParams){//default
                    CreatedByMeQueryParams queryParams = (CreatedByMeQueryParams) qp;
                    predicates.add(cb.equal(root.get("creator"), queryParams.getCreator()));
                }
            });
        }).go();

        //deal with exclusion
        if (!exclusion.isEmpty()) {
            predicates.add(cb.not(root.in(exclusion)));
        }

        //deal with AndFilters
        if (andFilters!=null) {
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
        return predicates;
    }

    private Long countQuery(EntityManager em,
            Optional<String> tenantId,
            Optional<QueryParams> oQueryParams) {
        //EntityManager em = emf.createEntityManager();

        //add tenant
        //--find tenant field
        EntityUtils entityUtils = SpringBeanFactory.create(EntityUtils.class);
        String idField = entityUtils.<T>getIdentifierFieldName(searchClass, em);

        CriteriaBuilder cb = em.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Long> criteria = cb.createQuery(Long.TYPE);
        Root<T> root = criteria.from(this.searchClass);

        List<Predicate> predicates = buildPredicates(tenantId, cb, root, oQueryParams);

        Predicate[] token = new Predicate[1];

        CriteriaQuery<Long> query = criteria.select(cb.count(root.get(idField)));
        if (!predicates.isEmpty()) {
            query.where(
                    cb.and(predicates.toArray(token)));
        }

        Long size = em.createQuery(query).getSingleResult();

        em.close();
        return size;
    }

    @Override
    public Collection doQuery(EntityManager em,
            List<Dual<String[], Boolean>> sortFieldsAsc,
            Optional<Integer> startIndex,
            Optional<Integer> pageSize,
            Optional<String> tenantId,
            Optional<QueryParams> oQueryParams) throws QueryException {

        Collection<T> results = queryString.map(
                Errors.rethrow().wrapFunction((String s) -> {
                    return runSearch(s,
                            em,
                            sortFieldsAsc,
                            startIndex.orElse(0),
                            pageSize,
                            tenantId,
                            oQueryParams);
                }
                )).orElseGet(() -> {
            return runQuery(em,
                    sortFieldsAsc,
                    startIndex.orElse(0),
                    pageSize,
                    tenantId,
                    oQueryParams);
        });
        if (results == null) {
            return new ArrayList<>();
        } else {
            return results;
        }
    }

    private Collection<T> runSearch(String queryString,
            EntityManager em,
            List<Dual<String[], Boolean>> sortFieldsAsc,
            Integer startIndex,
            Optional<Integer> pageSize,
            Optional<String> tenantId,
            Optional<QueryParams> oQueryParams) throws QueryException {

        //EntityManager em = emf.createEntityManager();
        EntityUtils entityUtils = SpringBeanFactory.create(EntityUtils.class);
        FieldUtils fieldUtils = SpringBeanFactory.create(FieldUtils.class);
        String currentIdField = entityUtils.getIdentifierFieldName(searchClass, em);
        SearchSession ftem = Search.session(em);
        var query = buildQueryFromQueryString(queryString,
                exclusion,
                Optional.ofNullable(andFilters),
                currentIdField,
                ftem,
                tenantId,
                oQueryParams);

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

    private SearchQueryOptionsStep<?, T, SearchLoadingOptionsStep, ?, ?> buildQueryFromQueryString(
            String myquery,
            Collection<T> exclusion,
            Optional<AndFilters> andFilter,
            String currentIdField,
            SearchSession ftem,
            Optional<String> tenantId,
            Optional<QueryParams> oQueryParams) {
        FieldUtils fieldUtils = SpringBeanFactory.create(FieldUtils.class);
        EntityUtils entityUtils = SpringBeanFactory.create(EntityUtils.class);

        String queryStr = myquery.trim();
        var query = ftem.search(this.searchClass)
                .extension(ElasticsearchExtension.get())
                .where(f -> f.bool(b -> {
                                JsonObject joQuery = new JsonObject();
                                joQuery.addProperty("query", queryStr);

                                JsonObject joQueryStr = new JsonObject();
                                joQueryStr.add("query_string", joQuery);

                                JsonArray jaQueryStr = new JsonArray();
                                jaQueryStr.add(joQueryStr);

                                JsonObject joMust = new JsonObject();
                                joMust.add("must", jaQueryStr);

                                JsonObject joBool = new JsonObject();
                                joBool.add("bool", joMust);

                                b.must(f.fromJson(joBool));

                                if (exclusion != null) {
                                    exclusion.stream().map(
                                            excluded -> entityUtils.getIdentifierValue(excluded, ftem.toEntityManager()))
                                            .forEachOrdered(excludedId -> {
                                                fieldUtils.getEquivSearchField(searchClass, currentIdField).ifPresent(idSearchField -> {
                                                    b.mustNot(f.match().field(idSearchField).matching(excludedId));
                                                });
                                            }
                                            );
                                }
                                oQueryParams.ifPresent(qp -> {
                                    
                                    Castor.<T,WorkElement>given(searchClass)
                                    .castItTo(WorkElement.class)
                                    .thenDo(()->{
                                        if (qp instanceof OwnedByMyTeamQueryParams) {
                                            OwnedByMyTeamQueryParams queryParams = (OwnedByMyTeamQueryParams) qp;
                                            b.must(f.match().field("workflowInfo.worklist").matching(queryParams.getWorklist()));
                                            //find work with owner(s)
                                            b.must(f.exists().field("workflowInfo.owners"));
                                        } else if (qp instanceof OwnedByMeQueryParams) {
                                            OwnedByMeQueryParams queryParams = (OwnedByMeQueryParams) qp;
                                            b.must(f.match().field("workflowInfo.owners").matching(queryParams.getOwnerId()));;
                                        } else if (qp instanceof UnbookedWorkQueryParams) {
                                            UnbookedWorkQueryParams queryParams = (UnbookedWorkQueryParams) qp;
                                            b.must(f.match().field("workflowInfo.worklist").matching(queryParams.getWorklist()));
                                            //find only work without owner(s)
                                            b.mustNot(f.exists().field("workflowInfo.owners"));
                                        }
                                    }).go();
                                    
                                    
                                    Castor.<T,Element>given(searchClass)
                                    .castItTo(Element.class)
                                    .thenDo(()->{
                                        if (qp instanceof StatusBasedQueryParams) {
                                            StatusBasedQueryParams queryParams = (StatusBasedQueryParams) qp;
                                            b.must(f.match().field("status").matching(queryParams.getStatus()));
                                        } else if (qp instanceof CreatedByMeQueryParams){//default
                                            CreatedByMeQueryParams queryParams = (CreatedByMeQueryParams) qp;
                                            b.must(f.match().field("creator").matching(queryParams.getCreator()));
                                        }
                                    }).go();
                                });
                            
                                //deal with and filters
                                andFilter.ifPresent(andfilter -> {
                                    for (QueryFilter filter : andfilter.getFilters()) {
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
//                                b.mustNot(f.match().field("status").matching(Status.CANCELLED));
//                                b.mustNot(f.match().field("status").matching(Status.RETIRED));
                        })
                );

        return query;
    }

    /**
     * @return the queryStr
     */
    public Optional<String> getQueryString() {
        return queryString;
    }

    /**
     * @param queryString the queryStr to set
     */
    public void setQueryString(Optional<String> queryString) {
        this.queryString = queryString;
    }

   
}
