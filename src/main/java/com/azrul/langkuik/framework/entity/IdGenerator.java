/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.entity;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

/**
 *
 * @author azrulhasni
 */
@Entity
@Table(name = "ID_GENERATOR")
@SequenceGenerator(name = "id_seq_gen", sequenceName = "id_seq_gen",  initialValue = 10000)
public class IdGenerator implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "id_seq_gen")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IdGenerator)) {
            return false;
        }
        IdGenerator other = (IdGenerator) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.azrul.loanorigsystem.model.IdGenerator[ id=" + id + " ]";
    }

    public Long getValue() {
        return id;
    }

    

}
