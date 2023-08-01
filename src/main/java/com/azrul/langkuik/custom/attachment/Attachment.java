/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.azrul.langkuik.custom.attachment;

import com.azrul.langkuik.framework.field.WebField;
import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.framework.entity.WebEntity;
import com.azrul.langkuik.framework.search.TikaField;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import org.hibernate.envers.Audited;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Searchable;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;


/**
 *
 * @author azrulm
 */


@Indexed
@Entity
@DiscriminatorValue("_Attachment")
@WebEntity(name="Attachment")
public class Attachment extends Element implements Serializable {
    private static final long serialVersionUID = 1L;

   
    @Column(name = "FILE_NAME")
    @Size(max = 30)
    @GenericField(projectable = Projectable.YES, sortable = Sortable.YES, searchable = Searchable.NO)
    @FullTextField(analyzer="english",name = "File-name", searchable = Searchable.YES)
    @Audited
    @WebField(displayName="File-name",order=200,visibleInTable = "true")
    private String fileName;
    
    @Column(name = "MIME_TYPE")
    private String mimeType;
    
    @Column(name = "RELATIVE_LOCATION")
    @TikaField(name = "document")
    @FullTextField(analyzer="english", searchable = Searchable.YES)
    @WebField(displayName="File",order=300,visibleInTable = "true")
    @FileAttachment
    private String relativeLocation;
    
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private AttachmentsContainer attachmentsContainer;


    public Attachment() {
    }

    public Attachment(Long id) {
        this.id = id;
    }

    
    public String getFileName() {
        return fileName;
    }

  
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

  
    public String getRelativeLocation() {
        return relativeLocation;
    }

    
    public void setRelativeLocation(String relativeLocation) {
        this.relativeLocation = relativeLocation;
    }

    
    public Date getCreationDate() {
        return creationDate;
    }

    
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    
    public boolean equals(Object object) {
       if (!(object instanceof Attachment)) {
            return false;
        }
        Attachment other = (Attachment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    
    public String toString() {
        return fileName;
    }

    /**
     * @return the mimeType
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * @param mimeType the mimeType to set
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * @return the attachmentsContainer
     */
    public AttachmentsContainer getAttachmentsContainer() {
        return attachmentsContainer;
    }

    /**
     * @param attachmentsContainer the attachmentsContainer to set
     */
    public void setAttachmentsContainer(AttachmentsContainer attachmentsContainer) {
        this.attachmentsContainer = attachmentsContainer;
    }

    
}
