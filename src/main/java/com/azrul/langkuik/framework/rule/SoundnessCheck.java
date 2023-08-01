package com.azrul.langkuik.framework.rule;

import com.azrul.langkuik.custom.VoidCustomComponentInTableRenderer;
import com.azrul.langkuik.custom.approval.Approval;
import com.azrul.langkuik.custom.attachment.Attachment;
import com.azrul.langkuik.custom.attachment.AttachmentsContainer;
import com.azrul.langkuik.custom.comment.Comment;
import com.azrul.langkuik.custom.comment.CommentsContainer;
import com.azrul.langkuik.framework.audit.AuditMetadata;
import com.azrul.langkuik.framework.entity.EntityUtils;
import com.azrul.langkuik.framework.entity.IdGenerator;
import com.azrul.langkuik.framework.entity.WebEntity;
import com.azrul.langkuik.framework.entity.WebEntityType;
import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.field.FieldUtils;
import com.azrul.langkuik.framework.relationship.RelationUtils;
import com.azrul.langkuik.framework.user.UserProfile;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author azrul
 */
@Service
public class SoundnessCheck {

    @PersistenceUnit()
    private EntityManagerFactory emf;

    @Autowired
    private FieldUtils fieldUtils;

    @Autowired
    private RelationUtils relationUtils;

    @Autowired
    private EntityUtils entityUtils;

    public enum ERROR_TYPE {
        ROOT_IS_NOT_WORKELEMENT,
        NO_ROOT_IN_SOLUTION,
        MORE_THAN_ONE_ROOT_IN_SOLUTION,
        FIELD_ORDER_SMALLER_THAN_100,
        RELATION_ORDER_SMALLER_THAN_100,
        RELATION_MIN_BIGGER_THAN_MAX,
        RELATION_WITH_CUSTOM_COMPONENT_IN_TABLE_MUST_BE_FETCH_EAGER
    };

    public Map<ERROR_TYPE, Map<String, String>> runChecks() {
        Map<ERROR_TYPE, Map<String, String>> errorMessage = new HashMap<>();
        this.checkOneRootPerSolutionAndIsWorkElement(errorMessage);
        //this.webFieldOrdersStartFrom100(errorMessage);
        this.webRelationMinSmallerOrEqualToMax(errorMessage);
        //this.webRelationOrdersStartFrom100(errorMessage);
        this.webRelationWithCustomComponentInTableSetMustBeFetchEAGER(errorMessage);
        return errorMessage;
    }

    public void checkOneRootPerSolutionAndIsWorkElement(Map<ERROR_TYPE, Map<String, String>> errorMessage) {
        Set systemClasses = Set.of(
                Approval.class,
                Attachment.class,
                AttachmentsContainer.class,
                Comment.class,
                CommentsContainer.class,
                AuditMetadata.class,
                IdGenerator.class,
                UserProfile.class);
        int rootCount = 0;
        for (var entity : entityUtils.getAllEntities(emf)) {
            if (systemClasses.contains(entity)) {
                continue;
            }
            WebEntity we = entity.getAnnotation(WebEntity.class);
            if (we == null) {
                continue;
            }        
            if (!we.type().equals(WebEntityType.ROOT)) {
                continue;
            }
            
            rootCount++;
            if (rootCount > 1) {
                errorMessage.put(ERROR_TYPE.MORE_THAN_ONE_ROOT_IN_SOLUTION, Map.of(
                        "root" + rootCount, entity.getName()
                ));
            }
            if (!WorkElement.class.isAssignableFrom(entity)) {
                errorMessage.put(ERROR_TYPE.ROOT_IS_NOT_WORKELEMENT, Map.of(
                        "root", entity.getName()
                ));
            }
                   
            
        }
        if (rootCount == 0) {
            errorMessage.put(ERROR_TYPE.NO_ROOT_IN_SOLUTION, Map.of());
        }

    }

   
//    public void webFieldOrdersStartFrom100(Map<ERROR_TYPE, Map<String, String>> errorMessage) {
//        Set systemClasses = Set.of(
//                Approval.class,
//                Attachment.class,
//                AttachmentsContainer.class,
//                Comment.class,
//                CommentsContainer.class,
//                AuditMetadata.class,
//                IdGenerator.class,
//                UserProfile.class);
//        for (var entity : entityUtils.getAllEntities(emf)) {
//            if (systemClasses.contains(entity)) {
//                continue;
//            }
//            var fields = fieldUtils.getFieldsByOrderRegardlessVisibility(entity);
//            if (fields.isEmpty()) {
//                continue;
//            }
//            int smallest = Collections.min(fields.keySet());
//            if (smallest < 100) {
//                errorMessage.put(ERROR_TYPE.FIELD_ORDER_SMALLER_THAN_100, Map.of("entity", entity.getName()));
//            }
//        }
//
//    }
//
//    public void webRelationOrdersStartFrom100(Map<ERROR_TYPE, Map<String, String>> errorMessage) {
//        Set systemClasses = Set.of(
//                Approval.class,
//                Attachment.class,
//                AttachmentsContainer.class,
//                Comment.class,
//                CommentsContainer.class,
//                AuditMetadata.class,
//                IdGenerator.class,
//                UserProfile.class);
//        for (var entity : entityUtils.getAllEntities(emf)) {
//            if (systemClasses.contains(entity)) {
//                continue;
//            }
//            var relations = relationUtils.getRelationsByOrder(entity);
//            if (relations.isEmpty()) {
//                continue;
//            }
//            int smallest = Collections.min(relations.keySet());
//            if (smallest < 100) {
//                errorMessage.put(ERROR_TYPE.RELATION_ORDER_SMALLER_THAN_100, Map.of("entity", entity.getName()));
//            }
//            
//        }
//
//    }

    public void webRelationMinSmallerOrEqualToMax(Map<ERROR_TYPE, Map<String, String>> errorMessage) {
        Set systemClasses = Set.of(
                Approval.class,
                Attachment.class,
                AttachmentsContainer.class,
                Comment.class,
                CommentsContainer.class,
                AuditMetadata.class,
                IdGenerator.class,
                UserProfile.class);
        for (var entity : entityUtils.getAllEntities(emf)) {
            if (systemClasses.contains(entity)) {
                continue;
            }
            var relations = relationUtils.getRelationsByOrder(entity);
            if (!relations.isEmpty()) {
                continue;
            }
            for (var relation : relations.entries()) {
                var wr = relation.getValue().getWebRelation();
                if (wr.maxCount() < wr.minCount()) {
                    errorMessage.put(ERROR_TYPE.RELATION_MIN_BIGGER_THAN_MAX, Map.of(
                            "entity", entity.getName(),
                            "relation", relation.getValue().getField().getName()));
                }
            }
        }
    }

    public void webRelationWithCustomComponentInTableSetMustBeFetchEAGER(Map<ERROR_TYPE, Map<String, String>> errorMessage) {
        Set systemClasses = Set.of(
                Approval.class,
                Attachment.class,
                AttachmentsContainer.class,
                Comment.class,
                CommentsContainer.class,
                AuditMetadata.class,
                IdGenerator.class,
                UserProfile.class);
        for (var entity : entityUtils.getAllEntities(emf)) {
            if (systemClasses.contains(entity)) {
                continue;
            }
            var relations = relationUtils.getRelationsByOrder(entity);
            for (var relation : relations.entries()) {
                var wr = relation.getValue().getWebRelation();
                if (wr.customComponentInTable() == VoidCustomComponentInTableRenderer.class) {
                    continue;
                }
                ManyToOne m2o = relation.getValue().getField().getAnnotation(ManyToOne.class);
                if (m2o == null) {
                    continue;
                }
                if (FetchType.LAZY.equals(m2o.fetch())) {
                    errorMessage.put(ERROR_TYPE.RELATION_WITH_CUSTOM_COMPONENT_IN_TABLE_MUST_BE_FETCH_EAGER, Map.of(
                            "entity", entity.getName(),
                            "relation", relation.getValue().getField().getName()));
                }
            }
        }

    }
}
