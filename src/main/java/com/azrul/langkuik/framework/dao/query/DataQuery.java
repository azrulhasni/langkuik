/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azrul.langkuik.framework.dao.query;

import com.azrul.langkuik.custom.bizConfig.Config;
import com.azrul.langkuik.framework.dao.DAOQuery;
import com.azrul.langkuik.framework.dao.DataAccessObject;
import com.azrul.langkuik.framework.dao.filter.AndFilters;
import com.azrul.langkuik.framework.dao.filter.FilterRelation;
import com.azrul.langkuik.framework.dao.filter.QueryFilter;
import com.azrul.langkuik.framework.dao.params.FindRelationParameter;
import com.azrul.langkuik.framework.dao.query.FindAnyEntityQuery;
import com.azrul.langkuik.framework.dao.query.FindRelationQuery;
import com.azrul.langkuik.framework.entity.EntityUtils;
import com.azrul.langkuik.framework.entity.Status;
import com.azrul.langkuik.framework.entity.WebEntityType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author azrul
 */
@Service
public class DataQuery {
    @Autowired
    DataAccessObject dao;
    
    @Autowired
    EntityUtils entityUtils;
    
    @Value("${application.lgDateFormat:yyyy-MM-dd}")
    private String dateFormat;

    public <P, T> Collection<T> queryRelation(String tenant, P parent, String relationName) {
        AndFilters andFilters = AndFilters.build(
                QueryFilter.build("status", FilterRelation.NOT_EQUAL, Status.CANCELLED),
                QueryFilter.build("status", FilterRelation.NOT_EQUAL, Status.RETIRED)
        );
        FindRelationParameter<P, T> param = new FindRelationParameter(
                parent, relationName, Optional.of(andFilters));

        FindRelationQuery query = new FindRelationQuery(param);
        Collection<T> res = dao.runQuery(
                query,
                Optional.of(tenant),
                Optional.empty());
        return res;
    }

    
    public <T> T queryReference(String tenant, Class<T> refClass, Map<String, Object> filter) {
        return queryReferences(tenant,refClass,filter).iterator().next();
    }
    
    public <T> String queryBizConfig(String tenant,String field) {
        return ((Config)queryReference(tenant,Config.class,Map.of("field",field))).getValue();
    }

    public <T> Collection<T> queryReferences(String tenant, Class<T> refClass, Map<String, Object> filter) {
        if (entityUtils.getEntityType(refClass) == WebEntityType.REF) {

            AndFilters andFilters = AndFilters.build(
                    QueryFilter.build("status", FilterRelation.NOT_EQUAL, Status.CANCELLED),
                    QueryFilter.build("status", FilterRelation.NOT_EQUAL, Status.RETIRED)
            );
            for (Map.Entry<String,Object> filterEntry:filter.entrySet()){
                andFilters.addFilter(QueryFilter.build(filterEntry.getKey(), FilterRelation.EQUAL, filterEntry.getValue()));
            }
            FindAnyEntityQuery<T> query
                    = new FindAnyEntityQuery<T>(
                            refClass,
                            dateFormat,
                            List.<T>of(),
                            Optional.of(
                                    andFilters
                            )
                    );

            return dao.runQuery((DAOQuery) query, 
                    Optional.of(tenant),
                    Optional.empty());
            
        } else {
            return new ArrayList<>();
        }
    }
}
