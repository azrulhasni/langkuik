/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.views.table;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import java.util.List;

/**
 *
 * @author azrul
 */
public class PageNav extends HorizontalLayout {

    private Button firstPage;
    private Button finalPage;
    private Button nextPage;
    private Button previousPage;
    private Label currentPage;

    public PageNav(String uniqueDisc) {
        //this.setSpacing(false);
        firstPage = new Button("<<");
        firstPage.setId("btnFirstPage-"+uniqueDisc);
        firstPage.addThemeVariants(ButtonVariant.LUMO_SMALL);
        finalPage = new Button(">>");
        finalPage.setId("btnFinalStage-"+uniqueDisc);
        finalPage.addThemeVariants(ButtonVariant.LUMO_SMALL);
        nextPage = new Button(">");
        nextPage.setId("btnNextPage-"+uniqueDisc);
        nextPage.addThemeVariants(ButtonVariant.LUMO_SMALL);
        previousPage = new Button("<");
        previousPage.setId("btnLastPage-"+uniqueDisc);
        previousPage.addThemeVariants(ButtonVariant.LUMO_SMALL);
        currentPage = new Label();
        //currentPage.getStyle().set("font-size","12px");
        currentPage.getStyle().set("line-height", "4");
        add(firstPage);
        add(previousPage);
        add(currentPage);
        currentPage.setText("0");
        add(nextPage);
        add(finalPage);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        getThemeList().removeAll(List.of("spacing-xs", "spacing-s", "spacing", "spacing-l", "spacing-xl"));
        getThemeList().add("spacing-xs");
    }

    public void setPage(Integer page, Integer maxPageCount) {
        if (page == 1) {
            this.getFinalPage().setEnabled(true);
            this.getFirstPage().setEnabled(false);
            this.getNextPage().setEnabled(true);
            this.getPreviousPage().setEnabled(false);
        } else if (page == maxPageCount) {
            this.getFinalPage().setEnabled(false);
            this.getFirstPage().setEnabled(true);
            this.getNextPage().setEnabled(false);
            this.getPreviousPage().setEnabled(true);
        } else {
            this.getFinalPage().setEnabled(true);
            this.getFirstPage().setEnabled(true);
            this.getNextPage().setEnabled(true);
            this.getPreviousPage().setEnabled(true);

        }
        this.currentPage.setText(Integer.toString(page) + "/" + Integer.toString(maxPageCount));
    }

    /**
     * @return the firstPage
     */
    public Button getFirstPage() {
        return firstPage;
    }

    /**
     * @return the finalPage
     */
    public Button getFinalPage() {
        return finalPage;
    }

    /**
     * @return the nextPage
     */
    public Button getNextPage() {
        return nextPage;
    }

    /**
     * @return the previousPage
     */
    public Button getPreviousPage() {
        return previousPage;
    }

 

}
