/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.workflow;

import com.azrul.langkuik.custom.EventToOpenOtherComponent;
import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.factory.SpringBeanFactory;
import com.azrul.langkuik.framework.relationship.RelationMemento;
import com.azrul.langkuik.framework.user.UserFieldRenderer;
import com.azrul.langkuik.views.pojo.PojoViewState;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.TextField;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Value;
import com.azrul.langkuik.custom.subform.SubFormRenderer;
import com.vaadin.flow.component.formlayout.FormLayout;

/**
 *
 * @author azrul
 */
public class WorkflowInfoRenderer<P extends WorkElement, R extends WorkElement> implements SubFormRenderer<P,R, WorkflowLayout>  {
    @Value("${application.lgDateTimeFormat:yyyy-MM-dd HH:mm:ss}")
    private String dateTimeFormat;
    
    @Override
    public Optional<WorkflowLayout> render(
            R root,
            P parent, 
            String relationName, 
            Optional<PojoViewState> oPojoViewState,
            Map<Integer, EventToOpenOtherComponent> eventToOpenOtherComponent, 
            Map<String, RelationMemento> relationMementos, 
            Consumer<RelationMemento> onCommit, 
            Consumer<RelationMemento> onDelete) {
        WorkflowInfo wi = parent.getWorkflowInfo();
        FormLayout form = new FormLayout();
        WorkflowLayout wrapper = new WorkflowLayout();
       
        //form.setMinWidth("40em");
       
        TextField tfWorklist = new TextField("Worklist");
        tfWorklist.setWidthFull();
        tfWorklist.setReadOnly(true);
        if (wi.getWorklist()!=null){
            tfWorklist.setValue(wi.getWorklist());
        }else{
            tfWorklist.setValue("");
        }
       
        form.add(tfWorklist);
        
        TextField tfWorklistUpdatetime = new TextField("Worklist Update Time");
        tfWorklistUpdatetime.setReadOnly(true);
        tfWorklistUpdatetime.setWidthFull();
        if (wi.getWorklistUpdateTime()!=null){
            tfWorklistUpdatetime
                    .setValue(wi.getWorklistUpdateTime()
                            .format(DateTimeFormatter
                                    .ofPattern(dateTimeFormat)));
        }else{
            tfWorklistUpdatetime.setValue("");
        }
        form.add(tfWorklistUpdatetime);

        TextField tfStatus = new TextField("Status");
        tfStatus.setReadOnly(true);
        tfStatus.setWidthFull();
        if (parent.getStatus()!=null){
            tfStatus.setValue(parent.getStatus().name());
        }else{
            tfStatus.setValue("");
        }
        form.add(tfStatus);
         
        String desc = parent.getWorkflowInfo().getStartEventDescription();
        TextField tfStartEventDesc = new TextField("Start at");
        tfStartEventDesc.setReadOnly(true);
        tfStartEventDesc.setWidthFull();
        tfStartEventDesc.setValue(desc!=null?desc:"");
        form.add(tfStartEventDesc);
        wrapper.add(form);
         
        final FormLayout formOwners = new FormLayout();
        UserFieldRenderer userRenderer = SpringBeanFactory.create(UserFieldRenderer.class);
        userRenderer.createInTable(wi, "owners").ifPresent(comp->{ 
            formOwners.addFormItem((Component)comp,"Owners");
            formOwners.setColspan((Component)comp,2);
        });
        
        
        wrapper.add(formOwners);
        
        return Optional.of(wrapper);
    }
    
    //workflow info can only be attached to root
     @Override
    public Boolean preCondition(R root, P parent, String relationName, Optional<PojoViewState> oParentState) {
       return true;
                
    }

    
}
