/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.custom.subform;

import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import java.util.function.Consumer;

/**
 *
 * @author azrul
 */
public class SubFormComponent extends VerticalLayout {

    protected SubFormRenderer renderer;
    protected Tab currentTab = null;
    protected MenuItem currentButton = null;
    private Dialog parentDialog = null;

    @Override
    public void setEnabled(boolean enable) {
    }

    /**
     * @param onEnable the onEnable to set
     */
    public void setOnEnable(Consumer<Boolean> onEnable) {
    }

    /**
     * @return the renderer
     */
    public SubFormRenderer getRenderer() {
        return renderer;
    }

    /**
     * @param renderer the renderer to set
     */
    public void setRenderer(SubFormRenderer renderer) {
        this.renderer = renderer;
    }

    public void onTabNavigateAway() {
    }

    public void onTabNavigateIn() {
    }

    public void onClose() {
    }

    //no need to call validate here. Validate is call  before hand
    public void beforeSaveCallBack() {
    }

    //no need to call validate here. Validate is call  before hand
    public void beforeSubmitCallBack() {
    }

    /**
     * @return the currentTab
     */
    public Tab getCurrentTab() {
        return currentTab;
    }

    /**
     * @param currentTab the currentTab to set
     */
    public void setCurrentTab(Tab currentTab) {
        this.currentTab = currentTab;
    }

    /**
     * @return the currentButton
     */
    public MenuItem getCurrentButton() {
        return currentButton;
    }

    /**
     * @param currentButton the currentButton to set
     */
    public void setCurrentButton(MenuItem currentButton) {
        this.currentButton = currentButton;
        
    }
    
    public Boolean validate(){
        return true;
    }
    


    protected void setStyle(Span span, String bgcolor, String fontcolor) {
        span.getStyle().set("background-color", bgcolor);
        span.getStyle().set("color", fontcolor);
        span.getStyle().set("border-radius", "15px");
        span.getStyle().set("width", "20%");
        span.getStyle().set("text-align", "center");
    }
    
    protected Boolean isDialogOpen(){
        if (parentDialog==null){ //if the dialog is not even present (in case we display in tab), the obviously isOpen is false
            return Boolean.FALSE;
        }
        return parentDialog.isOpened();
    }

    /**
     * @return the parentDialog
     */
    public Dialog getParentDialog() {
        return parentDialog;
    }

    /**
     * @param parentDialog the parentDialog to set
     */
    public void setParentDialog(Dialog parentDialog) {
        this.parentDialog = parentDialog;
        this.parentDialog.addDialogCloseActionListener(e->{
            validate();
        });
    }

}
