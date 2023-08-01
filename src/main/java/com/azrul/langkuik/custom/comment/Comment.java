/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.custom.comment;

import com.azrul.langkuik.custom.attachment.AttachmentsContainer;
import com.azrul.langkuik.framework.entity.WebEntity;
import com.azrul.langkuik.framework.field.WebField;
import com.azrul.langkuik.framework.entity.Element;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import org.hibernate.envers.Audited;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Searchable;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

/**
 *
 * @author azrul
 */
//@Indexed
@Entity
@DiscriminatorValue("_Comment")
@WebEntity(name = "Comment")
public class Comment extends Element implements Serializable {
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.id);
        return hash;
    }
    
//    @Column(name = "BY")
//    @Size(max = 30)
//    @FullTextField(analyzer="english",(  sortable = Sortable.YES, projectable = Projectable.YES)
//    @Audited
//    @WebField(displayName="By",order=100,visibleInForm = "true",visibleInTable = "true")
//    @User
//    private String by;
    
    @Audited
    @Column(name = "DATE_TIME")
    @GenericField(name="Date-time", searchable=Searchable.YES)
    @GenericField(projectable = Projectable.YES, sortable = Sortable.YES)
    @WebField(displayName = "Date-time",
            isReadOnly = true,
            order = 300,
            visibleInTable = "true",
            visibleInForm = "true"
    )
    private LocalDateTime dateTime;
    
     @Audited
    @Column(name = "VALUE",length = 1000)
    @FullTextField(analyzer="english",name="Value",searchable=Searchable.YES)
    @WebField(displayName = "Value",
            isReadOnly = true,
            order = 200,
            visibleInTable = "false",
            visibleInForm = "true"
    )
    private String value;
     
     
    @ManyToOne(fetch = FetchType.LAZY)
    private CommentsContainer commentsContainer; 

    /**
     * @return the comments
     */
    public String getValue() {
        return value;
    }

    /**
     * @param comments the comments to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the approvalTimestamp
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * @param approvalTimestamp the approvalTimestamp to set
     */
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

//    /**
//     * @return the by
//     */
//    public String getBy() {
//        return by;
//    }
//
//    /**
//     * @param by the by to set
//     */
//    public void setBy(String by) {
//        this.by = by;
//    }

//    /**
//     * @return the attachments
//     */
//    public AttachmentsContainer getAttachments() {
//        return attachments;
//    }
//
//    /**
//     * @param attachments the attachments to set
//     */
//    public void setAttachments(AttachmentsContainer attachments) {
//        this.attachments = attachments;
//    }

    /**
     * @return the comments
     */
    public CommentsContainer getCommentsContainer() {
        return commentsContainer;
    }

    /**
     * @param comments the comments to set
     */
    public void setCommentsContainer(CommentsContainer commentsContainer) {
        this.commentsContainer = commentsContainer;
    }
}
