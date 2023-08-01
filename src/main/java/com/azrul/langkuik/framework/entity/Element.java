/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.entity;

import com.azrul.langkuik.framework.field.WebField;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import com.azrul.langkuik.framework.workflow.Workflow;
import java.util.Optional;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Searchable;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.DocumentId;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import java.math.BigInteger;
import java.util.UUID;
import org.hibernate.annotations.Index;
import com.azrul.langkuik.framework.user.User;
import javax.persistence.MappedSuperclass;
import org.hibernate.annotations.Where;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.jboss.logging.Logger;

/**
 *
 * @author azrul
 */
//@Entity
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@MappedSuperclass
@XmlRootElement
public abstract class Element {

    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @WebField(displayName = "Id", order = 1, isReadOnly = true)
    @Audited
    @DocumentId
    @GenericField(name = "Id", searchable = Searchable.YES)
    @GenericField(projectable = Projectable.YES, sortable = Sortable.YES, searchable = Searchable.NO)
    protected Long id;

    @Audited
    @GenericField(projectable = Projectable.YES, sortable = Sortable.YES, searchable = Searchable.YES)
    @GenericField(name = "Creator", searchable = Searchable.YES)
    @Column(name = "CREATOR_ID")
    @WebField(displayName = "Creator",
            isReadOnly = true,
            order = 3,
            visibleInForm = "true",
            visibleInTable = "false"
    )
    @User
    protected String creator;

    @Audited
    @Column(name = "STATUS")
    @GenericField(projectable = Projectable.YES, sortable = Sortable.YES, searchable = Searchable.YES)
    @GenericField(name = "Status", searchable = Searchable.YES)
    @WebField(displayName = "Status",
            isReadOnly = true,
            order = 4,
            visibleInForm = "false",
            visibleInTable = "false"
    )
    private Status status;

    @Audited
    @GenericField(projectable = Projectable.YES, sortable = Sortable.YES)
    @Column(name = "TENANT_ID")
    protected String tenant;

  

    @Index(name = "_enum_path_idx")
    @KeywordField(searchable = Searchable.YES)
    @Column(name = "ENUM_PATH")
    private String enumPath;

    protected Element() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    /**
//     * @return the priority
//     */
//    public Priority getPriority() {
//        return priority;
//    }
//
//    /**
//     * @param priority the priority to set
//     */
//    public void setPriority(Priority priority) {
//        this.priority = priority;
//    }
    /**
     * @return the tenantId
     */
    public String getTenant() {
        return tenant;
    }

    /**
     * @param tenantId the tenantId to set
     */
    public void setTenant(String tenantId) {
        this.tenant = tenantId;
    }

    /**
     * @return the creatorId
     */
    /**
     * @return the creator
     */
    public String getCreator() {
        return creator;
    }

    /**
     * @param creator the creator to set
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Element other = (Element) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Element{" + "id=" + id + '}';
    }

    public Boolean isRoot() {
        return isTypeEquals(WebEntityType.ROOT);
    }

    public Boolean isNominal() {
        return isTypeEquals(WebEntityType.NOMINAL);
    }

    public Boolean isReference() {
        return isTypeEquals(WebEntityType.REF);
    }

    public Boolean isTypeEquals(WebEntityType wet) {
        if (this.getClass().isAnnotationPresent(WebEntity.class)) {
            WebEntity webEntity = (WebEntity) this.getClass().getAnnotation(WebEntity.class);
            if (webEntity != null) {
                return webEntity.type() == wet;
            } else {
                return Boolean.FALSE;
            }
        } else {
            Logger.getLogger(Element.class).error(this.getClass()+" is not a WebEntity");
            return Boolean.FALSE;
        }
    }

    public <V> Optional<String> customValidation(String field, Workflow workflow, V value) { //return error message
        return Optional.empty();
    }

   
    /**
     * @return the status
     */
    public Status getStatus() {
        if (status==null){
            status = Status.DRAFT;
        }
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * @return the enumPath
     */
    public String getEnumPath() {
        return enumPath;
    }

    /**
     * @param enumPath the enumPath to set
     */
    public void setEnumPath(String enumPath) {
        this.enumPath = enumPath;
    }

    public void initEnumPath() {
        if (isRoot() || isReference()) {
            this.enumPath = String.valueOf(id);
        }
    }

    public String updateEnumPath(String parentsEnumPath) {
        if (isNominal()) {
            this.enumPath = parentsEnumPath + ">" + String.valueOf(id);
        }
        return this.enumPath;
    }
}
