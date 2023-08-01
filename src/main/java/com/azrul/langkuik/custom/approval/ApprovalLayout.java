/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.custom.approval;

import com.azrul.langkuik.custom.subform.SubFormComponent;
import com.azrul.langkuik.framework.entity.WorkElement;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.select.Select;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author azrul
 */
public class ApprovalLayout<P extends WorkElement> extends SubFormComponent {

    private final ApprovalRepo approvalRepo;
    private final P parentEntity;
    private final String relationName;
    private ApprovalOption selectedOption;

    public ApprovalLayout(
            ApprovalRepo repo,
            P parentEntity,
            String relationName
    ) {
        setId("approval-layout");
        setWidth("48em");
        this.approvalRepo = repo;
        this.parentEntity = parentEntity;
        this.relationName = relationName;
    }

    @Override
    public void beforeSaveCallBack() {
        approvalRepo.getCurrentApproval(parentEntity, relationName).ifPresent(currentApproval -> {
            approvalRepo.saveApproval(Optional.ofNullable(currentApproval), selectedOption, parentEntity);
        });
    }

    @Override
    public void beforeSubmitCallBack() {
        approvalRepo.getCurrentApproval(parentEntity, relationName).ifPresent(currentApproval -> {
            approvalRepo.saveApproval(Optional.ofNullable(currentApproval), selectedOption, parentEntity);
        });
    }

    /**
     * @param cbApprove the cbApprove to set
     */
    public void setSelectAndValue(Select<ApprovalOption> cbApprove,
            ApprovalOption selectedOption) {
        this.selectedOption = selectedOption;
        cbApprove.setValue(this.selectedOption);
        cbApprove.addValueChangeListener(e -> {
            this.selectedOption = (ApprovalOption) e.getValue();
            if (this.selectedOption.getValue() == null) {
                cbApprove.setInvalid(true);
            } else {
                cbApprove.setInvalid(false);
            }
        });

    }

    @Override
    public Boolean validate() {

        if (selectedOption == null) { //no approval from the current user needed (e.g. a Corrector)
            return Boolean.TRUE;
        }
        Boolean res = selectedOption.getValue();
        if (currentButton != null) {
            //Button
            //------
            List<Component> spans = currentButton.getChildren()
                    .filter(c -> c.getClass().equals(Span.class)).collect(Collectors.toList());
            spans.forEach(span -> {
                currentButton.remove(span);
            });
            if (res == null) {
                Span span = new Span("-");
                setStyle(span, "red", "red");
                currentButton.add(span);

                if (isDialogOpen()) {
                    //We are validating in a dialog. 
                    //Return valid since the decision to give correct decision might need deeper thinking / discussion
                    //That veing said, if there are errors, still indicate that on the button
                    return Boolean.TRUE;
                } else {
                    return Boolean.FALSE;
                }
            } else {
                //if res is not null (either res is true or false is irrelevent. Both are valid approval decisions)
                //confirm that the this approval is valid
                return Boolean.TRUE;
            }
        } else {
            //Tab
            //----
            //remove first then add back
            List<Component> spans = currentTab.getChildren()
                    .filter(c -> c.getClass().equals(Span.class)).collect(Collectors.toList());
            spans.forEach(span -> {
                currentTab.remove(span);
            });
            if (res == null) {
                Span span = new Span("-");
                setStyle(span, "red", "red");
                currentTab.add(span);
                return Boolean.FALSE;
            } else {
                //if res is not null (either res is true or false is irrelevent. Both are valid approval decisions)
                //confirm that the this approval is valid
                return Boolean.TRUE;
            }
        }
    }

}
