/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.custom.relationToForm;

import com.azrul.langkuik.custom.EventToOpenOtherComponent;
import com.azrul.langkuik.custom.subform.SubFormComponent;
import com.azrul.langkuik.framework.dao.DataAccessObject;
import com.azrul.langkuik.framework.dao.filter.AndFilters;
import com.azrul.langkuik.framework.dao.filter.FilterRelation;
import com.azrul.langkuik.framework.dao.filter.QueryFilter;
import com.azrul.langkuik.framework.dao.params.FindRelationParameter;
import com.azrul.langkuik.framework.dao.query.FindRelationQuery;
import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.framework.entity.Status;
import com.azrul.langkuik.framework.entity.WebEntity;
import com.azrul.langkuik.framework.entity.WebEntityType;
import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.factory.SpringBeanFactory;
import com.azrul.langkuik.framework.field.FieldUtils;
import com.azrul.langkuik.framework.relationship.RelationMemento;
import com.azrul.langkuik.framework.relationship.RelationType;
import com.azrul.langkuik.framework.relationship.RelationUtils;
import com.azrul.langkuik.framework.standard.Castor;
import com.azrul.langkuik.framework.workflow.Workflow;
import com.azrul.langkuik.views.pojo.PojoView;
import com.azrul.langkuik.views.pojo.PojoViewState;
import com.vaadin.flow.server.VaadinSession;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.azrul.langkuik.custom.subform.SubFormRenderer;
import com.azrul.langkuik.framework.standard.Dual;

/**
 *
 * @author azrul
 */
public class RelationToFormRenderer<T extends Element, P extends Element, R extends WorkElement> implements SubFormRenderer<P, R, SubFormComponent> {

    @Autowired
    private DataAccessObject dao;

    @Autowired
    private Workflow workflow;

    @Autowired
    private FieldUtils fieldUtils;

    @Autowired
    private RelationUtils relationUtils;

    @Value("${application.lgDateFormat:yyyy-MM-dd}")
    private String dateFormat;

    @Override
    public Optional<SubFormComponent> render(
            R root,
            P parent,
            String relationName,
            Optional<PojoViewState> oParentState,
            Map<Integer, EventToOpenOtherComponent> eventToOpenOtherComponent,
            Map<String, RelationMemento> relationMementos,
            Consumer<RelationMemento> onCommit,
            Consumer<RelationMemento> onDelete) {
        String tenant = (String) VaadinSession.getCurrent().getSession().getAttribute("TENANT");
        RelationMemento relationMemento = relationMementos.get(relationName);
        return (Optional<SubFormComponent>) relationUtils.getRelationType(parent.getClass(), relationName).map(relationType -> {
            if (RelationType.X_To_ONE.equals(relationType)) {
                return relationUtils.getRelationClass(parent.getClass(), relationName).map(childClass -> {
                    return oParentState.map(parentState -> {
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
                        PojoView<R, T> pojoView = SpringBeanFactory.create(
                                PojoView.class);
                        T bean;
                        if (!res.isEmpty()) {
                            bean = res.iterator().next();
                        } else {
                            bean = (T) createNewChild(parent, childClass,relationName, relationMemento);
                        }

                        pojoView.construct(Optional.of(root),
                                bean,
                                Optional.empty(),
                                parentState,
                                Boolean.TRUE);
                        RelationToFormComponent outer = new RelationToFormComponent();
                        outer.setDao(dao);
                        outer.setCurrentBean(bean);
                        outer.setParentBean(parent);
                        outer.setRoot(root);
                        outer.setRelationName(relationName);
                        outer.setPojoView(pojoView);

                        return Optional.of(outer);
                    }).orElse(Optional.empty());
                }).orElse(Optional.empty());
            } else {
                return Optional.empty();
            }
        }).orElse(Optional.empty());
    }

    //rendering relation as  aform can only be done in ROOT
    @Override
    public Boolean preCondition(R root, P parent, String relationName, Optional<PojoViewState> oParentState) {
        WebEntity we = (WebEntity) parent.getClass().getAnnotation(WebEntity.class);
        if (we == null) {
            return Boolean.FALSE;
        } else {
            return WorkElement.class.isAssignableFrom(parent.getClass()) && WebEntityType.ROOT.equals(we.type());
        }

    }

    private T createNewChild(P parentBean, Class<T> tclass, String parentToChildRelation, RelationMemento relationMemento) {
        String userIdentifier = (String) VaadinSession.getCurrent().getSession().getAttribute("USER_IDENTIFIER");
        String tenant = (String) VaadinSession.getCurrent().getSession().getAttribute("TENANT");

        Optional<Dual<P,T>> bean = dao.createAssociateAndSave(
                tclass,
                parentBean,
                parentToChildRelation,
                Optional.of(tenant),
                Optional.empty(),
                Optional.empty(),
                Status.DRAFT,
                userIdentifier);
        
        relationMemento.incrementCounter();

        T e = bean.get().getSecond();
        Castor.<T, WorkElement>given(e)
                .castItTo(WorkElement.class)
                .thenDo(ext -> {
                    ext.getWorkflowInfo()
                            .setWorklist(ext.getWorkflowInfo().getStartEventId());
                }).go();
        return e;
    }

}
