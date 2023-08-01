/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.custom.approval;

import com.azrul.langkuik.framework.dao.DataAccessObject;
import com.azrul.langkuik.framework.dao.filter.AndFilters;
import com.azrul.langkuik.framework.dao.filter.FilterRelation;
import com.azrul.langkuik.framework.dao.filter.QueryFilter;
import com.azrul.langkuik.framework.dao.params.FindRelationParameter;
import com.azrul.langkuik.framework.dao.query.FindRelationQuery;
import com.azrul.langkuik.framework.entity.Status;
import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.exception.QueryException;
import com.azrul.langkuik.framework.workflow.Workflow;
import com.vaadin.flow.server.VaadinSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author azrul
 */
@Service
public class ApprovalRepo<R extends WorkElement,P extends WorkElement> {
    
    @Value("${application.lgElementPerPageApproval}")
    private int LIMIT;
    
    @Autowired
    private DataAccessObject dao;
    
    @Autowired
    private Workflow workflow;
    
    public Optional<Approval> getCurrentApproval(P parent, String relationName) throws QueryException {
        final String tenant = (String) VaadinSession.getCurrent().getSession().getAttribute("TENANT");
        final String userIdentifier = (String) VaadinSession.getCurrent().getSession().getAttribute("USER_IDENTIFIER");

        FindRelationQuery query = buildApprovalQuery(parent, relationName);

        Collection<Approval> approvals = dao.runQuery(query,
                new ArrayList<>(),
                Optional.of(0),
                Optional.of(LIMIT),
                Optional.of(tenant),
                Optional.empty());

        return approvals
                .stream()
                .filter(a -> userIdentifier.equals(a.getUsername()))
                .findFirst();
    }
    
     public Boolean hasCurrentApproval(P parent, String relationName) throws QueryException {
        final String tenant = (String) VaadinSession.getCurrent().getSession().getAttribute("TENANT");
       
        FindRelationQuery query = buildApprovalQuery(parent, relationName);

        Long count = dao.countQueryResult(query,
                Optional.of(tenant),
                Optional.empty());

        return count>0;
    }

    private FindRelationQuery buildApprovalQuery(P parent, String relationName) {
        String worklist = parent.getWorkflowInfo().getWorklist();
        AndFilters andFilters = AndFilters.build(
                QueryFilter.build(new String[]{"workflowInfo","worklist"}, FilterRelation.EQUAL, worklist),
                QueryFilter.build("status", FilterRelation.NOT_EQUAL, Status.CANCELLED),
                QueryFilter.build("status", FilterRelation.NOT_EQUAL, Status.RETIRED)
        );
        FindRelationQuery query = new FindRelationQuery(
                new FindRelationParameter(
                        parent,
                        relationName,
                        Optional.of(andFilters)
                )
        );
        return query;
    }
    
    public void saveApproval(final Optional<Approval> oCurrentApproval,ApprovalOption option, P work) {
        oCurrentApproval.ifPresent(currentApproval -> {
            //approval has 3 values
            //true: approved
            //false: not approved
            //null: not set yet
            if (option == null) {
                currentApproval.setApproved(null);
            } else {
                currentApproval.setApproved(option.getValue());
            }


            currentApproval.setStatus(Status.DONE);
            currentApproval.setApprovalTimestamp(LocalDateTime.now());

            dao.save(currentApproval).ifPresent(o -> { //save approval. if ok, save work
                Approval a = (Approval) o;
                work.replaceApproval(a);
                dao.save(work);
            });
        });
    }
}
