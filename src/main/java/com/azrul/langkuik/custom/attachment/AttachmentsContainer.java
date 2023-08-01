/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.custom.attachment;

import com.azrul.langkuik.framework.relationship.WebRelation;
import com.azrul.langkuik.framework.rights.RelationAccess;
import com.azrul.langkuik.framework.rights.RelationRights;
import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.framework.entity.WebEntity;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.hibernate.annotations.Type;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

/**
 *
 * @author azrul
 */
@Indexed
@Entity
@DiscriminatorValue("_AttachmentContainer")
@WebEntity(name = "AttachmentsContainer")
public class AttachmentsContainer extends Element implements Serializable {

    private static final long serialVersionUID = 1L;

    public AttachmentsContainer() {

    }

//    public AttachmentsContainer(Long id) {
//        this.id = id;
//    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final AttachmentsContainer other = (AttachmentsContainer) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }


    
    @IndexedEmbedded
    @OneToMany(fetch = FetchType.LAZY, mappedBy="attachmentsContainer")
    @WebRelation(name = "Attachments", order = 10,
            rights = @RelationAccess(
                    atWorklist = "*",
                    currentOwner = RelationRights.CAN_ADD_AND_DELETE_OWN_ITEMS))
    private Set<Attachment> _attachments;

    /**
     * @return the attachments
     */
    public Set<Attachment> get_Attachments() {
        return _attachments;
    }

    /**
     * @param attachments the attachments to set
     */
    public void set_Attachments(Set<Attachment> _attachments) {
        this._attachments = _attachments;
    }

   

}
