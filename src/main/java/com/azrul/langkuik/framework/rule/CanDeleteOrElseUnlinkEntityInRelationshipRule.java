/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.rule;

import com.azrul.langkuik.framework.dao.DataAccessObject;
import com.azrul.langkuik.framework.standard.Dual;
import com.azrul.langkuik.framework.entity.EntityUtils;
import com.azrul.langkuik.framework.findusage.query.FindUsageQuery;
import com.azrul.langkuik.framework.field.FieldUtils;
import com.azrul.langkuik.framework.relationship.RelationUtils;
import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.framework.entity.Status;
import com.azrul.langkuik.framework.workflow.Workflow;
import com.vaadin.flow.server.VaadinSession;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import javax.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author azrul
 */
@Service
@Qualifier("CanDeleteOrElseUnlinkEntityInRelationshipRule")
public class CanDeleteOrElseUnlinkEntityInRelationshipRule implements PojoRule {

    @Autowired
    Workflow workflow;

    @Autowired
    private EntityManagerFactory emf;

    @Autowired
    private DataAccessObject dao;

    @Autowired
    FieldUtils fieldUtils;

    @Autowired
    EntityUtils entityUtils;

    @Autowired
    RelationUtils relationUtils;

    /**
     *
     * @param work
     * @return
     */
    public <T extends Element> Boolean compute(Optional<T> owork, Object... otherParams) {
        final Set<String> roles = (Set<String>) VaadinSession.getCurrent().getSession().getAttribute("ROLES");

        final String tenant = (String) VaadinSession.getCurrent().getSession().getAttribute("TENANT");

        return (Boolean) owork.map(work -> {

            if (roles.contains("WORKFLOW_ADMIN")) {
                return Boolean.TRUE;
            } else if (roles.contains("REF_ADMIN")) {
                if (work.isReference()) {
                    //Long count = 0L;
                    Collection<Dual<Class<?>, Field>> parentClasses = relationUtils.getAllDependingClass(work.getClass(), emf);
                    for (Dual<Class<?>, Field> parentClass : parentClasses) {
                        FindUsageQuery fq = new FindUsageQuery(work,
                                parentClass.getSecond().getName(),
                                parentClass.getFirst());
                        if (dao.countQueryResult(fq, Optional.of(tenant), Optional.empty()) > 0) {
                            return Boolean.FALSE;
                        }
                    }
                    return Boolean.TRUE;
                } else {
                    return Boolean.FALSE;
                }
            } else { //typical user
                if (work.isReference()) {
                    return Boolean.FALSE;
                } else { //NOMINAL or ROOT
                    return  Boolean.TRUE;
                    //if the status of work is allowed to be deleted
//                    return fieldUtils.getWebEntity(work.getClass()).map(we -> {
//                        Status[] statuses = we.statusWhereDeletionIsEnabled();
//                        Arrays.sort(statuses);
//                        return Arrays.binarySearch(statuses, work.getStatus()) //return true
//                                >= 0 ? true : false;
//                    }).orElse(Boolean.FALSE);
                }
            }
        }).orElse(Boolean.FALSE);
    }
}
