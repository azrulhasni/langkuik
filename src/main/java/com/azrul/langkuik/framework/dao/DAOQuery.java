/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.azrul.langkuik.framework.dao;

import com.azrul.langkuik.framework.dao.params.QueryParams;
import com.azrul.langkuik.framework.exception.QueryException;
import com.azrul.langkuik.framework.standard.Dual;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author azrulm
 * @param <P>
 * @param <C>
 * @param <W>
 */
public interface DAOQuery<P,C> {
    Collection<C> doQuery(EntityManager em, 
            List<Dual<String[], Boolean>> sortFieldsAsc,
            Optional<Integer> startIndex, 
            Optional<Integer> pageSize, 
            Optional<String> tenantId, 
            Optional<QueryParams> oQueryParams) throws QueryException;
    
    Long count(EntityManager em,  
            Optional<String> tenantId, 
            Optional<QueryParams> oQueryParams) throws QueryException;
}
