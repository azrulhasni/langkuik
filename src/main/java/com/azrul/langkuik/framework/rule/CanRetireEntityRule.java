/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.rule;

import com.azrul.langkuik.framework.entity.WebEntityType;
import com.azrul.langkuik.framework.entity.EntityUtils;
import com.azrul.langkuik.framework.field.FieldUtils;
import com.azrul.langkuik.framework.relationship.RelationUtils;
import com.azrul.langkuik.framework.entity.Status;
import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.standard.Castor;
import com.azrul.langkuik.framework.workflow.Workflow;
import com.vaadin.flow.server.VaadinSession;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author azrul
 */
@Service
@Qualifier("CanRetireEntityRule")
public class CanRetireEntityRule implements PojoRule {

    @Autowired
    Workflow workflow;

    @Autowired
    FieldUtils fieldUtils;

    @Autowired
    EntityUtils entityUtils;

    @Autowired
    RelationUtils relationUtils;

    @Override
    public <T extends Element> Boolean compute(Optional<T> owork, Object... otherParams) {
        return owork.map(w->{ 
            return w.isReference();
        }).orElse(Boolean.FALSE);
    }

}
