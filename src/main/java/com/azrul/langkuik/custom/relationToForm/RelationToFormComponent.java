/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.custom.relationToForm;

import com.azrul.langkuik.custom.subform.SubFormComponent;
import com.azrul.langkuik.framework.dao.DataAccessObject;
import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.standard.Dual;
import com.azrul.langkuik.views.pojo.PojoView;
import com.azrul.langkuik.views.pojo.ReasonToClose;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author azrul
 */
public class RelationToFormComponent<R extends WorkElement, P, T> extends SubFormComponent {

    private P parentBean;
    private T currentBean;
    private R root;
    private DataAccessObject dao;
    private String relationName;
    private PojoView pojoView;

    @Override
    public void onTabNavigateAway() {
        validate();

        //save
        Optional<Dual<P, T>> oresult = dao.saveAndAssociate(
                currentBean,
                parentBean,
                relationName,
                p -> {
                });
    }
    
//    @Override
//     public Boolean validateBeforeSubmit() {
//        return validate();
//    }
//    
//    @Override
//    public Boolean validateBeforeClose() {
//        return validate();
//    }

    @Override
    public Boolean validate() {
        ReasonToClose reason = pojoView.validate();
        if (currentButton != null) {
            List<Component> spans = currentButton.getChildren()
                    .filter(c -> c.getClass().equals(Span.class)).collect(Collectors.toList());
            spans.forEach(span -> {
                currentButton.remove(span);
            });

            if (reason == ReasonToClose.ERROR) {
                Span span = new Span("-");
                setStyle(span, "red", "red");
                currentButton.add(span);
                return Boolean.FALSE;
            }else{
                return Boolean.TRUE;
            }
        } else if (currentTab !=null){
            //remove first then add back
            List<Component> spans = currentTab.getChildren()
                    .filter(c -> c.getClass().equals(Span.class)).collect(Collectors.toList());
            spans.forEach(span -> {
                currentTab.remove(span);
            });
            if (reason == ReasonToClose.ERROR) {
                Span span = new Span("-");
                setStyle(span, "red", "red");
                currentTab.add(span);
                return Boolean.FALSE;
            } else {
                return Boolean.TRUE;
            }
        } else{
            return Boolean.TRUE;
        }

    }

    @Override
    public void onTabNavigateIn() {
        validate();

    }

    @Override
    public void beforeSaveCallBack() {
        validate();
        Optional<Dual<P, T>> oresult = dao.saveAndAssociate(
                currentBean,
                parentBean,
                relationName,
                p -> {
                });
    }

    @Override
    public void beforeSubmitCallBack() {
        Optional<Dual<P, T>> oresult = dao.saveAndAssociate(
                currentBean,
                parentBean,
                relationName,
                p -> {
                });
    }

    /**
     * @return the parentBean
     */
    public P getParentBean() {
        return parentBean;
    }

    /**
     * @param parentBean the parentBean to set
     */
    public void setParentBean(P parentBean) {
        this.parentBean = parentBean;
    }

    /**
     * @return the currentBean
     */
    public T getCurrentBean() {
        return currentBean;
    }

    /**
     * @param currentBean the currentBean to set
     */
    public void setCurrentBean(T currentBean) {
        this.currentBean = currentBean;
    }

    /**
     * @return the relationName
     */
    public String getRelationName() {
        return relationName;
    }

    /**
     * @param relationName the relationName to set
     */
    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    /**
     * @return the pojoView
     */
    public PojoView getPojoView() {
        return pojoView;
    }

    /**
     * @param pojoView the pojoView to set
     */
    public void setPojoView(PojoView pojoView) {
        add(pojoView);
        this.pojoView = pojoView;
    }

    @Override
    public void setEnabled(boolean enable) {

    }

    /**
     * @return the dao
     */
    public DataAccessObject getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(DataAccessObject dao) {
        this.dao = dao;
    }

    /**
     * @return the root
     */
    public R getRoot() {
        return root;
    }

    /**
     * @param root the root to set
     */
    public void setRoot(R root) {
        this.root = root;
    }

   

}
