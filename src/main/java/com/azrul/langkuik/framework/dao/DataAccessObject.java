/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.dao;

//import com.azrul.langkuik.framework.rule.Rule1Input;
//import com.azrul.langkuik.framework.rule.Rule3Inputs;
import com.azrul.langkuik.framework.standard.Dual;
import com.azrul.langkuik.framework.dao.params.FindRelationParameter;
import com.azrul.langkuik.framework.dao.params.QueryParams;
import java.util.Collection;
import com.azrul.langkuik.framework.exception.EntityIsUsedException;
import com.azrul.langkuik.framework.exception.QueryException;
import com.azrul.langkuik.framework.entity.Status;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import org.springframework.stereotype.Repository;

/**
 *
 * @author azrulm
 *
 * @param <T>
 */
@Repository
public interface DataAccessObject<T> {
    
   

    Optional<T> createAndSave(Class<T> c,
            Optional<String> tenantId,
            Optional<String> parentEnumPath,
            Optional<String> startEventId,
            Optional<String> startEventDesc,
            Status status,
            String creatorId);

    <P> Optional<Dual<P, T>> createAssociateAndSave(Class<T> childClass,
            P parentBean,
            String parentToNewBeanField,
            Optional<String> tenantId,
            Optional<String> startEventId,
            Optional<String> startEventDesc,
            Status status,
            String creatorId);

    //Save
    Optional<T> save(T newObject);

    //update
    <P> Optional<Dual<P, T>> saveAndAssociate(T newBean,
            P parentBean,
            String parentToNewBeanField);

    <P> Optional<Dual<P, T>> saveAndAssociate(T newBean,
            P parentBean,
            String parentToNewBeanField,
            Consumer<P> onCommit);

    <P> Set<Dual<P, T>> saveAndAssociate(Set<T> newBeans,
            P parentBean,
            String parentToNewBeanField,
            Consumer<P> onCommit);

    //Delete / unlink
    //used exclusively in PojoView to unlink and delete items  (REF)in a relationship
    <P> Optional<P> unlink(FindRelationParameter<P, T> frParam,
            Collection<T> oldBeans,
            Consumer2Inputs<P, T> onCommit);

    //delete from relationship
    //used exclusively in PojoView and AttachmentsRenderer to unlink and delete items (ROOT or NOMINAL) in a relationship
    <P> Optional<P> unlinkAndDelete(FindRelationParameter<P, T> frParam,
            Collection< T> oldBeans,
            Consumer2Inputs<P, T> onCommit);

    //delete from root (called exclusively by TableView Delete button which appears if TableView.mode==Mode.MAIN
    void delete(Collection<T> entities) throws EntityIsUsedException;

    //retire REF object (called exclusively by TableView Retire button which appears if entities are WebEntityType.REFERENCE
    void retire(Collection<T> entities);

    void unretire(Collection<T> entities);

    //search
    <P> Long countQueryResult(DAOQuery<P, T> query,
            Optional<String> tenantId,
            Optional<QueryParams> oQueryParams) throws QueryException;

    //LIST UNBOOKED WORK FROM WORKLIST
    //if worklist!=null
    //   if ownerId==null AND worklist==y, find all work in worklist y
    //LIST OWNED BY ME
    //if worklist==null
    //   if ownerId!=null, 
    //      if ownerId = x, find all work owned by x
    //LIST CREATED BY ME
    //if worklist==null
    //   if creatorId!=null, 
    //      if creatorId=x, find all work created by x
    //LIST OWNED BY ME AND MY TEAM
    //if worklist!=null
    //   if ownerId != null
    //      if worklist=y, find all work in worklist y
    <P> Collection<T> runQuery(DAOQuery<P, T> query,
            List<Dual<String[], Boolean>> sortFieldsAsc,
            Optional<Integer> startIndex,
            Optional<Integer> pageSize,
            Optional<String> tenantId,
            Optional<QueryParams> oQueryParams) throws QueryException;

    <P> Collection<T> runQuery(DAOQuery<P, T> query,
            Optional<String> tenantId,
            Optional<QueryParams> queryParams) throws QueryException;
    
    
    void massIndex();
}
