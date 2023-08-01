/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.custom.comment;

import com.azrul.langkuik.framework.relationship.WebRelation;
import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.framework.entity.WebEntity;
import java.util.Objects;
import java.util.Set;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

/**
 *
 * @author azrul
 */
@Indexed
@Entity
@DiscriminatorValue("_CommentContainer")
@WebEntity(name="CommentsContainer")
public class CommentsContainer extends Element{
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
        final CommentsContainer other = (CommentsContainer) obj;
        return Objects.equals(this.id, other.id);
    }
    
    @IndexedEmbedded
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "commentsContainer")
    @WebRelation(name="Comments",order=10, maxCount = 10)
    private Set<Comment> _comments;
 
    /**
     * @return the comments
     */
    public Set<Comment> get_Comments() {
        return _comments;
        
    }

    /**
     * @param comments the comments to set
     */
    public void set_Comments(Set<Comment> _comments) {
        this._comments = _comments;
    }


}
