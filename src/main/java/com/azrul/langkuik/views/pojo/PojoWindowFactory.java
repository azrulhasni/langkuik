/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.views.pojo;

import com.azrul.langkuik.framework.dao.DataAccessObject;
import com.azrul.langkuik.framework.standard.Dual;
import com.azrul.langkuik.framework.entity.EntityUtils;
import com.azrul.langkuik.framework.factory.SpringBeanFactory;
import com.azrul.langkuik.framework.relationship.RelationMemento;
import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.framework.entity.Status;
import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.workflow.Workflow;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.GeneratedVaadinDialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author azrul
 */
@Component
public class PojoWindowFactory<T extends Element> {

    @Autowired
    private DataAccessObject dao;

    @Autowired
    private Workflow<WorkElement> workflow;

    @Autowired
    private EntityUtils entityUtils;

//    @Autowired
//    @Qualifier("RefWorkflow")
//    private Workflow<T> refWorkflow;
    @Autowired
    PojoTableFactory pojoTableFactory;

    public <R extends WorkElement> void createReadOnlyPojoDialog(R root,T bean) {
        Dialog dialog2 = new Dialog();
        PojoView<R, T> pojoView = SpringBeanFactory.create(PojoView.class);
        pojoView.construct(Optional.of(root), bean, Optional.of(dialog2), PojoViewState.NOT_EDITABLE, Boolean.FALSE);
        dialog2.setModal(true);
        dialog2.setCloseOnEsc(false);
        dialog2.setCloseOnOutsideClick(false);

        //begin:enable scroll
        pojoView.setHeight("100%");
        pojoView.getStyle().set("overflow-y", "auto");
        dialog2.add(pojoView);
        dialog2.setHeightFull();
        dialog2.open();
    }
    
    public <R extends WorkElement> PojoView<R, T> createMainPojoWindow(T bean, Optional<Runnable> afterSave) {

        PojoView<R, T> pojoView = SpringBeanFactory.create(PojoView.class);
        pojoView.construct(bean, ()->{
                    final T currentBean = (T) pojoView.getCurrentBean();
                    if (null != pojoView.getReasonToClose()) {
                        switch (pojoView.getReasonToClose()) {
                            case SUBMITTED:
                                
                                if (WorkElement.class.isAssignableFrom(currentBean.getClass())) {
                                    if (currentBean.isReference()) {
                                        
                                        ((WorkElement) currentBean).setStatus(Status.DONE);
                                        ((WorkElement) currentBean).getWorkflowInfo().setWorklist("END");
                                        ((WorkElement) currentBean).getWorkflowInfo().setWorklistUpdateTime(LocalDateTime.now());
                                        
                                        dao.save(currentBean);
                                    } else {
                                        WorkElement we = (WorkElement)currentBean;
                                        dao.save(workflow.run(we, true));
                                    }
                                } else {
                                    dao.save(currentBean);
                                }
                                break;
                            case CLOSE_AS_IS:
                                dao.save(currentBean);
                                break;
                            case RELEASE:
                                if (WorkElement.class.isAssignableFrom(currentBean.getClass())) {
                                    ((WorkElement) currentBean).getWorkflowInfo().getOwners().clear(); //empty the list of owners.
                                    ((WorkElement) currentBean).setSupervisorApprovalSeeker(null);
                                    ((WorkElement) currentBean).setSupervisorApprovalLevel(null);
                                    ((WorkElement) currentBean).setStatus(Status.IN_PROGRESS);
                                }
                                dao.save(currentBean);
                                break;
                            default:
                                break;
                        }
                    }
                    
                    afterSave.ifPresent(a -> a.run());
                });
        //begin:enable scroll
        pojoView.setHeight("100%");
        pojoView.getStyle().set("overflow-y", "auto");
        return pojoView;
      
    }

    private <R extends WorkElement> void createMainPojoDialog(T bean, Optional<Runnable> afterSave) {
        Dialog dialog = new Dialog();
        PojoView<R, T> pojoView = SpringBeanFactory.create(PojoView.class);
        pojoView.construct(bean, Optional.of(dialog));
        dialog.setModal(true);
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);

        //begin:enable scroll
        pojoView.setHeight("100%");
        pojoView.getStyle().set("overflow-y", "auto");
        dialog.add(pojoView);
        dialog.setHeightFull();
        dialog.addOpenedChangeListener(new ComponentEventListener<GeneratedVaadinDialog.OpenedChangeEvent<Dialog>>() {
            @Override
            public void onComponentEvent(GeneratedVaadinDialog.OpenedChangeEvent<Dialog> event) {
                if (!event.isOpened()) {
                    final T currentBean = (T) pojoView.getCurrentBean();
                    if (null != pojoView.getReasonToClose()) {
                        switch (pojoView.getReasonToClose()) {
                            case SUBMITTED:
                                
                                if (WorkElement.class.isAssignableFrom(currentBean.getClass())) {
                                    if (currentBean.isReference()) {
                                        
                                        ((WorkElement) currentBean).setStatus(Status.DONE);
                                        ((WorkElement) currentBean).getWorkflowInfo().setWorklist("END");
                                        ((WorkElement) currentBean).getWorkflowInfo().setWorklistUpdateTime(LocalDateTime.now());
                                        
                                        dao.save(currentBean);
                                    } else {
                                        WorkElement we = (WorkElement)currentBean;
                                        dao.save(workflow.run(we, true));
                                    }
                                } else {
                                    dao.save(currentBean);
                                }
                                break;
                            case CLOSE_AS_IS:
                                dao.save(currentBean);
                                break;
                            case RELEASE:
                                if (WorkElement.class.isAssignableFrom(currentBean.getClass())) {
                                    ((WorkElement) currentBean).getWorkflowInfo().getOwners().clear(); //empty the list of owners.
                                    ((WorkElement) currentBean).setSupervisorApprovalSeeker(null);
                                    ((WorkElement) currentBean).setSupervisorApprovalLevel(null);
                                    ((WorkElement) currentBean).setStatus(Status.IN_PROGRESS);
                                }
                                dao.save(currentBean);
                                break;
                            default:
                                break;
                        }
                    }
                    
                    afterSave.ifPresent(a -> a.run());
                }
            }
        });
        dialog.open();
    }

    public <R extends WorkElement, C extends Element> void createChildPojoDialog(R root,
            T parent,
            String relationName,
            C child,
            PojoViewState parentState,
            RelationMemento relationMemento,
            Consumer<T> setCurrentBean,
            Consumer<RelationMemento> onCommit) {

        Dialog dialog = new Dialog();
        PojoView<R, C> pojoView = SpringBeanFactory.create(PojoView.class);

        //Construct first 
        pojoView.construct(Optional.of(root), child, Optional.of(dialog), parentState, Boolean.FALSE);

        dialog.setModal(true);
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);
        dialog.addOpenedChangeListener(event -> {
            if (!event.isOpened()) {
                Optional<Dual<T, C>> oresult = dao.saveAndAssociate(
                        pojoView.getCurrentBean(),
                        parent, 
                        relationName,
                        p -> onCommit.accept(relationMemento));
                Dual<T, C> result = oresult.orElseThrow();
                setCurrentBean.accept(result.getFirst());
                pojoTableFactory.searchAndRedrawTable(relationMemento);
            }
        });

        //begin:enable scroll
        pojoView.setHeight("100%");
        pojoView.getStyle().set("overflow-y", "auto");
        dialog.add(pojoView);
        dialog.setHeightFull();
        //end:enable scroll
        dialog.open();
    }
}
