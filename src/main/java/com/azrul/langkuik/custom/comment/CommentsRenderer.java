/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.custom.comment;

import com.azrul.langkuik.custom.EventToOpenOtherComponent;
import com.azrul.langkuik.framework.dao.DataAccessObject;
import com.azrul.langkuik.framework.standard.Dual;
import com.azrul.langkuik.framework.dao.params.FindRelationParameter;
import com.azrul.langkuik.framework.dao.query.FindRelationQuery;
import com.azrul.langkuik.framework.exception.QueryException;
import com.azrul.langkuik.framework.factory.SpringBeanFactory;
import com.azrul.langkuik.framework.relationship.RelationManager;
import com.azrul.langkuik.framework.relationship.RelationManagerFactory;
import com.azrul.langkuik.framework.relationship.RelationMemento;
import com.azrul.langkuik.framework.entity.Status;
import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.views.pojo.PojoTableFactory;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.server.VaadinSession;
import java.time.LocalDateTime;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.azrul.langkuik.framework.dao.filter.AndFilters;
import com.azrul.langkuik.framework.dao.filter.FilterRelation;
import com.azrul.langkuik.framework.dao.filter.QueryFilter;
import com.azrul.langkuik.framework.entity.WebEntity;
import com.azrul.langkuik.framework.entity.WebEntityType;
import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.standard.Castor;
import com.azrul.langkuik.views.pojo.PojoViewState;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import com.azrul.langkuik.custom.subform.SubFormRenderer;
import org.apache.commons.collections4.MultiValuedMap;

/**
 *
 * @author azrul
 */
public class CommentsRenderer<P, R extends Element> implements SubFormRenderer<P,R, CommentsLayout> {


    @Value("${application.lgDateTimeFormat:yyyy-MM-dd HH:mm:ss}")
    private String dateTimeFormat;

    @Autowired
    private DataAccessObject dao;

    @Autowired
    private PojoTableFactory pojoTableFactory;

    @Autowired
    private RelationManagerFactory relationMgrFactory;
    
    private final Notification notifProblemWithQuery = new Notification(
                "", 3000, Notification.Position.TOP_START);

    
    

    private CommentsContainer getOrCreateComments(P parent, 
            String relationName, 
            final String tenant, 
            final String username) throws QueryException {
        
        
        AndFilters andFilters = AndFilters.build(
                QueryFilter.build("status", FilterRelation.NOT_EQUAL, Status.CANCELLED),
                QueryFilter.build("status", FilterRelation.NOT_EQUAL, Status.RETIRED)
        );
        FindRelationParameter parameter = new FindRelationParameter(parent, relationName, Optional.of(andFilters));
        FindRelationQuery query = new FindRelationQuery(parameter);
        
        Collection<CommentsContainer> commentsFrDB = dao.runQuery(query,
                new ArrayList<>(),
                Optional.of(0),
                Optional.empty(),
                Optional.of(tenant),
                Optional.empty());

        if (commentsFrDB.isEmpty()) { //if no comments from DB, create one and attached to parent

            Optional<Dual<P, CommentsContainer>> oDualComments = dao.createAssociateAndSave(CommentsContainer.class,
                    parent,
                    relationName,
                    Optional.of(tenant),
                    Optional.empty(),
                    Optional.empty(),
                    Status.DONE,
                    username
            );

            Dual<P, CommentsContainer> dualComments = oDualComments.orElseThrow();
            CommentsContainer newComments = dualComments.getSecond();

            RelationManager relationMgr = relationMgrFactory.create(parent.getClass(), CommentsContainer.class);
            relationMgr.link(parent, newComments, relationName);//link the attachemts object back to

            return newComments;
        } else { //else, use it
            return commentsFrDB.iterator().next();
        }

    }

    @Override
    public Optional<CommentsLayout> render(R root,
            P parent, 
            String relationName, 
            Optional<PojoViewState> oPojoViewState,
            Map<Integer,EventToOpenOtherComponent> eventToOpenOtherComponent,
            Map<String, RelationMemento> relationMementos, 
            Consumer<RelationMemento> onCommit, 
            Consumer<RelationMemento> onDelete) {
        final String userIndentifier = (String) VaadinSession.getCurrent().getSession().getAttribute("USER_IDENTIFIER");
        final String tenant = (String) VaadinSession.getCurrent().getSession().getAttribute("TENANT");

        CommentsLayout commentsLayout = new CommentsLayout();

        final CommentsContainer comments = getOrCreateComments(parent, relationName, tenant, userIndentifier);
       
        VerticalLayout headerLayout = new VerticalLayout();
        
        List<Dual<String[], Boolean>> sortFieldsAsc = new ArrayList<>();
        sortFieldsAsc.add(new Dual(new String[]{"dateTime"},Boolean.TRUE));
        AndFilters andFilters = AndFilters.build(
                        QueryFilter.build("status", FilterRelation.NOT_EQUAL, Status.CANCELLED),
                        QueryFilter.build("status", FilterRelation.NOT_EQUAL, Status.RETIRED)
                );
        RelationMemento memento = relationMementos.getOrDefault("_comments",
                    pojoTableFactory.createRelationMemento(
                            Optional.empty(),
                            comments, 
                            "_comments",
                            Optional.of(andFilters), 
                            500, 
                            Optional.empty(), 
                            Optional.empty(),
                            "Comments", 
                            sortFieldsAsc));
        //relationMementos.put("_comments", memento);
         
        pojoTableFactory.createTable(
                commentsLayout,
                memento,
                Optional.of(headerLayout), 
                e -> {},
                Optional.of(Boolean.TRUE),
                Optional.empty(),
                Optional.of(SpringBeanFactory.create(CommentRenderer.class))
        );
        commentsLayout.setWidth(500, Unit.PIXELS);
        commentsLayout.setHeightFull();
        
        TextArea commentAdded = new TextArea();
        commentAdded.setId("Comments-commentsAdded");
        commentAdded.setWidthFull();
        headerLayout.add(commentAdded);
        Optional<String> parentEnumPath = Castor.<CommentsContainer, Element>given(comments)
                .castItTo(Element.class)
                .thenDo(p -> {
                    return p.getEnumPath();
                }).go();
        Button btnAdd = new Button("Add", e -> {
            Optional<Comment> ocomment = dao.createAndSave(
                    Comment.class, 
                    Optional.of(tenant), 
                    parentEnumPath,
                    Optional.empty(),
                    Optional.empty(),
                    Status.DONE, 
                    userIndentifier);
            ocomment.ifPresent(comment -> {
                comment.setValue(commentAdded.getValue());
                comment.setDateTime(LocalDateTime.now());
                comment.setTenant(tenant);

                dao.saveAndAssociate(comment, comments, "_comments",
                        p -> {
                            //onCommit.accept(relationMementos.get("commentsCollection"));
                            pojoTableFactory.searchAndRedrawTable(memento, Optional.of(Boolean.TRUE));

                        });
            });
        });
        btnAdd.setId("Comments-btnAdd");
        headerLayout.add(btnAdd);
        headerLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        
        return Optional.of(commentsLayout);
    }

  
    //only root element can have comments
     @Override
    public Boolean preCondition(R root, P parent, String relationName, Optional<PojoViewState> oParentState) {
        WebEntity we = (WebEntity)parent.getClass().getAnnotation(WebEntity.class);
        if (we==null){
            return Boolean.FALSE;
        }else{
            return WorkElement.class.isAssignableFrom(parent.getClass()) &&  WebEntityType.ROOT.equals(we.type());
        }
                
    }
}
