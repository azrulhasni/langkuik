/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.relationship;

import com.azrul.langkuik.framework.dao.filter.AndFilters;
import com.azrul.langkuik.views.table.PageNav;
import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.standard.Dual;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasEnabled;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import com.azrul.langkuik.framework.rule.AddDeleteRelationRule;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Label;
import java.util.Set;

/**
 *
 * @author azrul
 */
public class RelationMemento<P extends Element, C extends Element> {

    private Grid<C> grid;
    private PageNav pageNav;
    private int page = 1;
    //private int maxPageCount;
    private long total;
    private int elementsPerPage;
    private Component title;
    private HasText errorMessage;
    private Integer max;
    private Integer min;
    private AtomicInteger counter;
    private Component btnAddLinkNew;
    private Component btnDeleteUnlink;
    private Component btnClearSelected;
    private P parent;
    private String relationName;
    private Class<C> childClass;
    private Boolean canAddOrDelete;
    private Optional<AndFilters> andFilters;
    private Optional<String> searchText;
    //private Component btnAddLinkNew;

    private List<Dual<String[], Boolean>> sortFieldAsc;
    private Map<String, Object> otherStates;

    public RelationMemento() {
        this.total = 0l;
        this.counter=new AtomicInteger(0);
    }

    public RelationMemento(Grid<C> grid,  int page, long total, int elementsPerPage,  int counter) {
        this(grid, page, total, elementsPerPage, new Text("None"), new Text("None"), Optional.empty(), Optional.empty(), counter);
    }

    public RelationMemento(Grid<C> grid, int page, long total, int elementsPerPage, Component title, HasText errorMessage, Optional<Integer> min, Optional<Integer> max, Integer counter) {
        
        this.grid = grid;
        //this.pageNav = pageNav;
        this.page = page;
        this.total = total;
        this.elementsPerPage=elementsPerPage;
        //this.maxPageCount = maxPageCount;
        this.title = title;
        this.errorMessage = errorMessage;
        this.max = max.orElse(null);
        this.min = min.orElse(null);
        this.counter = new AtomicInteger(Long.valueOf(total).intValue());
        //this.canAddOrDelete = Boolean.FALSE;
    }

    /**
     * @return the grid
     */
    public Grid<C> getGrid() {
        return grid;
    }

    /**
     * @param grid the grid to set
     */
    public void setGrid(Grid<C> grid) {
        this.grid = grid;
    }

    /**
     * @return the pageNav
     */
    public PageNav getPageNav() {
        return pageNav;
    }

    /**
     * @param pageNav the pageNav to set
     */
    public void setPageNav(PageNav pageNav) {
        this.pageNav = pageNav;
    }

    /**
     * @return the page
     */
    public int getPage() {
        return page;
    }

    /**
     * @param page the page to set
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * @return the maxPageCount
     */
    public int getMaxPageCount() {
        return Long.valueOf(-Math.floorDiv(-total, elementsPerPage)).intValue();
    }

    /**
     * @param maxPageCount the maxPageCount to set
     */
//    public void setMaxPageCount(int maxPageCount) {
//        this.maxPageCount = maxPageCount;
//    }

    public Object getOtherState(String key) {
        return otherStates.get(key);
    }

    public void setOtherState(String key, Object state) {
        this.otherStates.put(key, state);
    }

    /**
     * @return the title
     */
    public Component getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(Component title) {
        this.title = title;
    }

    /**
     * @return the errorMessage
     */
    public HasText getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(HasText errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @return the max
     */
    public Optional<Integer> getMax() {
        return Optional.ofNullable(max);
    }

    /**
     * @param max the max to set
     */
    public void setMax(Integer max) {
        this.max = max;
    }

    /**
     * @return the min
     */
    public Optional<Integer> getMin() {
        return Optional.ofNullable(min);
    }

    /**
     * @param min the min to set
     */
    public void setMin(Integer min) {
        this.min = min;
    }

    /**
     * @return the counter
     */
    public Integer getCounter() {
        return counter.get();
    }

    public Integer incrementCounter() {
        return counter.incrementAndGet();
    }

    public Integer decrementCounter() {
        return counter.decrementAndGet();
    }

    /**
     * @param counter the counter to set
     */
    private void setCounter(Integer counter) {
        this.counter = new AtomicInteger(counter);
    }

    public Boolean reachedMax() {
        if (max == null) {
            return Boolean.FALSE;
        }
        if (max <= 0) {
            return Boolean.FALSE;
        }
        return max <= counter.get();
    }

    public Boolean satisfyMin() {
        if (min == null) {
            return Boolean.TRUE;
        }
        if (min <= 0) {
            return Boolean.TRUE;
        }
        return min <= counter.get();
    }

    /**
     * @return the btnAddLinkNew
     */
    public Component getBtnAddLinkNew() {
        return btnAddLinkNew;
    }

    /**
     * @param btnAddLinkNew the btnAddLinkNew to set
     */
    public void setBtnAddLinkNew(Component btnAddLinkNew) {
        this.btnAddLinkNew = btnAddLinkNew;
    }

    /**
     * @return the sortFieldAsc
     */
    public List<Dual<String[], Boolean>> getSortFieldAsc() {
        return sortFieldAsc;
    }

    /**
     * @param sortFieldAsc the sortFieldAsc to set
     */
    public void setSortFieldAsc(List<Dual<String[], Boolean>> sortFieldAsc) {
        this.sortFieldAsc = sortFieldAsc;
    }

    /**
     * @return the btnDeleteUnlink
     */
    public Component getBtnDeleteUnlink() {
        return btnDeleteUnlink;
    }

    /**
     * @param btnDeleteUnlink the btnDeleteUnlink to set
     */
    public void setBtnDeleteUnlink(Component btnDeleteUnlink) {
        this.btnDeleteUnlink = btnDeleteUnlink;
    }

    /**
     * @return the btnClearSelected
     */
    public Component getBtnClearSelected() {
        return btnClearSelected;
    }

    /**
     * @param btnClearSelected the btnClearSelected to set
     */
    public void setBtnClearSelected(Component btnClearSelected) {
        this.btnClearSelected = btnClearSelected;
    }

    /**
     * @return the parent
     */
    public P getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(P parent) {
        this.parent = parent;
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
     * @return the childClass
     */
    public Class<C> getChildClass() {
        return childClass;
    }

    /**
     * @param childClass the childClass to set
     */
    public void setChildClass(Class<C> childClass) {
        this.childClass = childClass;
    }

    /**
     * @return the canAddOrDelete
     */
    public Boolean getCanAddOrDelete() {
        return canAddOrDelete;
    }

    /**
     * @param canAddOrDelete the canAddOrDelete to set
     */
    public void setCanAddOrDelete(Boolean canAddOrDelete) {
        this.canAddOrDelete = canAddOrDelete;
    }

    public <R extends WorkElement> void calculateEditableRelation(R root, AddDeleteRelationRule canAddDeleteRelationsRule, Boolean enabledOverall) {

        if (canAddDeleteRelationsRule.compute(
                Optional.ofNullable(root),
                Optional.ofNullable(this.getParent()),
                this.getRelationName(),
                Optional.ofNullable(this.getChildClass()),
                Set.of())) {
            if (this.reachedMax()) {
                enableButton("Maximum has been reached", this.getBtnAddLinkNew(), false);
            } else {
                enableButton("", this.getBtnAddLinkNew(), enabledOverall);
            }
            enableButton("", this.getBtnDeleteUnlink(), enabledOverall);
            enableButton("", this.getBtnClearSelected(), enabledOverall);
            canAddOrDelete = enabledOverall;
        } else {
            enableButton("", this.getBtnAddLinkNew(), false);
            enableButton("", this.getBtnDeleteUnlink(), false);
            enableButton("", this.getBtnClearSelected(), false);
            canAddOrDelete = false;
        }

    }

    private void enableButton(String errorMsg, Component btn, Boolean enabled) {
        if (btn != null) {
            this.getErrorMessage().setText(errorMsg);
            if (btn instanceof HasEnabled) {
                ((HasEnabled) btn).setEnabled(enabled);
            } else {
                btn.setVisible(enabled);
            }
        }
    }

    /**
     * @return the andFilters
     */
    public Optional<AndFilters> getAndFilters() {
        return andFilters;
    }

    /**
     * @param andFilters the andFilters to set
     */
    public void setAndFilters(Optional<AndFilters> andFilters) {
        this.andFilters = andFilters;
    }

    /**
     * @return the searchText
     */
    public Optional<String> getSearchText() {
        if (searchText == null) {
            searchText = Optional.empty();
        }
        return searchText;
    }

    /**
     * @param searchText the searchText to set
     */
    public void setSearchText(Optional<String> searchText) {
        this.searchText = searchText;
    }

    public Boolean validate() {
        if (Boolean.TRUE.equals(this.getCanAddOrDelete())) {
            if (Boolean.FALSE.equals(this.satisfyMin())) {
                //error
                ((Label) this.getTitle()).getStyle().set("background", "hsla(3, 100%, 89%, 1)");
                this.getMin().ifPresent(min
                        -> this.getErrorMessage().setText("Expecting a minimal of " + min)
                );
                return Boolean.FALSE;
            } else {
                ((Label) this.getTitle()).getStyle().set("background", "white");
                return Boolean.TRUE;
            }
        }else{
            return Boolean.TRUE;
        }
    }

    /**
     * @return the total
     */
    public long getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(long total) {
        this.setCounter(Long.valueOf(total).intValue());
        this.total = total;
    }

    /**
     * @return the elementsPerPage
     */
    public int getElementsPerPage() {
        return elementsPerPage;
    }

    /**
     * @param elementsPerPage the elementsPerPage to set
     */
    public void setElementsPerPage(int elementsPerPage) {
        this.elementsPerPage = elementsPerPage;
    }

}
