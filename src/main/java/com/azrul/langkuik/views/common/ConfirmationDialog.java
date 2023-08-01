/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.views.common;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import java.util.function.Consumer;

/**
 *
 * @author azrul
 */
public class ConfirmationDialog extends Dialog {

    public ConfirmationDialog(String dialogTitle,String bigMessage, String confirmBtnLabel,
            Consumer<ClickEvent<Button>> confirmLogic, String cancelBtnLabel,
            Consumer<ClickEvent<Button>> cancelLogic) {
        //this.add(dialogTitle);
        this.add(new Text(bigMessage));
        this.setModal(true);
        this.setCloseOnEsc(false);
        this.setCloseOnOutsideClick(false);
        //Span message = new Span();

        Button confirmButton = new Button("Confirm", event -> {
            confirmLogic.accept(event);
            this.close();
        });
        confirmButton.setId("btnConfirm");
        Button cancelButton = new Button("Cancel", event -> {
            cancelLogic.accept(event);
            this.close();
        });
        this.add(new HorizontalLayout(confirmButton, cancelButton));
    }
}
