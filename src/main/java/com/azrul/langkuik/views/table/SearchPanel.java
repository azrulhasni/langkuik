/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.views.table;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.TextField;
import java.util.function.Consumer;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.KeyDownEvent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

/**
 *
 * @author azrul
 */
public class SearchPanel<T> extends FlexLayout {
//    private final Button btnCreateNew;

    private final TextField tfSearchString;
    private final Button btnSearch;
    //private ComponentEventListener listener;

    public SearchPanel(String caption, Class<T> classOfSearchResult) {

        tfSearchString = new TextField();
        tfSearchString.setId("searchField");
        tfSearchString.setWidth("80%");
        tfSearchString.setPlaceholder(caption);
        btnSearch = new Button();
        btnSearch.setWidth("150px");
        btnSearch.setId("btnSearch");
        btnSearch.setText(caption);
        if (classOfSearchResult.isAnnotationPresent(Indexed.class)) {
            add(tfSearchString, btnSearch);
        }
    }

    public TextField getTextField() {
        return tfSearchString;
    }

    public Button getButton() {
        return btnSearch;
    }

    public String getValue() {
        return tfSearchString.getValue();
    }

    /**
     * @param listener the listener to set
     */
    public void setSearchListener(Consumer<SearchPanel.SearchPanelEvent> doSearch) {

        btnSearch.addClickListener((t) -> {
            SearchPanelEvent ev = new SearchPanelEvent(tfSearchString.getValue(), t);
            doSearch.accept(ev);
        });

        tfSearchString.addKeyDownListener(Key.ENTER, (KeyDownEvent e) -> {
            SearchPanelEvent ev = new SearchPanelEvent(tfSearchString.getValue(), e);
            doSearch.accept(ev);
        });
    }

    public class SearchPanelEvent {

        private String searchFieldValue;
        private ComponentEvent triggerEvent;

        /**
         * @return the searchFieldValue
         */
        public String getSearchFieldValue() {
            return searchFieldValue;
        }

        public SearchPanelEvent(String searchFieldValue, ComponentEvent triggerEvent) {
            this.searchFieldValue = searchFieldValue;
            this.triggerEvent = triggerEvent;
        }

    }
}
