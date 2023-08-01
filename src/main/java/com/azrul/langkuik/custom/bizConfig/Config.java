/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azrul.langkuik.custom.bizConfig;

import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.framework.entity.WebEntity;
import com.azrul.langkuik.framework.entity.WebEntityType;
import com.azrul.langkuik.framework.field.WebField;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import org.hibernate.annotations.Where;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Searchable;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

/**
 *
 * @author azrul
 */
@Indexed
@Entity
@Where(clause = "status<>3")
@WebEntity(name="Config",type=WebEntityType.REF)
public class Config  extends Element  {
    
    @Column(name = "FIELD")
    @GenericField(name="Field",searchable=Searchable.YES)
    @GenericField(  sortable = Sortable.YES, projectable = Projectable.YES, searchable=Searchable.NO)
    @WebField(displayName = "Field", order = 100)
    @NotEmpty(message="Field cannot be empty")
    private String field;
    
    @Column(name = "VALUE")
    @GenericField(name="Value",searchable=Searchable.YES)
    @GenericField(  sortable = Sortable.YES, projectable = Projectable.YES, searchable=Searchable.NO)
    @WebField(displayName = "Value", order = 200)
    private String value;

    /**
     * @return the field
     */
    public String getField() {
        return field;
    }

    /**
     * @param field the field to set
     */
    public void setField(String field) {
        this.field = field;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
}
