package com.azrul.langkuik.views.pojo;

import com.azrul.langkuik.custom.SimpleCustomComponentRenderer;
import com.azrul.langkuik.framework.dao.DAOQuery;
import com.azrul.langkuik.framework.entity.WebEntity;
import com.azrul.langkuik.framework.entity.WebEntityType;
import com.azrul.langkuik.framework.dao.DataAccessObject;
import com.azrul.langkuik.framework.standard.Dual;
import com.azrul.langkuik.framework.dao.params.FindRelationParameter;
import com.azrul.langkuik.framework.dao.query.FindRelationQuery;
import com.azrul.langkuik.framework.dao.filter.AndFilters;
import com.azrul.langkuik.framework.dao.filter.FilterRelation;
import com.azrul.langkuik.framework.dao.filter.QueryFilter;
import com.azrul.langkuik.framework.factory.SpringBeanFactory;
import com.azrul.langkuik.framework.expression.Expression;
import com.azrul.langkuik.framework.field.FieldUtils;
import com.azrul.langkuik.framework.relationship.RelationMemento;
import com.azrul.langkuik.framework.relationship.RelationUtils;
import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.framework.entity.EntityUtils;
import com.azrul.langkuik.framework.entity.Status;
import com.azrul.langkuik.framework.exception.QueryException;
import com.azrul.langkuik.views.table.GridBuilder;
import com.azrul.langkuik.views.table.PageNav;
import com.azrul.langkuik.views.table.TableView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.provider.SortOrder;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import javax.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class PojoTableFactory {

//    @Autowired
//    private Workflow workflow;
    @Autowired
    private EntityUtils entityUtils;
    
     @Autowired
    private FieldUtils fieldUtils;

    @Autowired
    private RelationUtils relationUtils;

    @Autowired
    private Expression expr;

    @Autowired
    private DataAccessObject dao;

    @Autowired
    ValidatorFactory validatorFactory;
    
    @Autowired
    GridBuilder gridBuilder;

    @Value("${application.lgDateFormat:yyyy-MM-dd}")
    private String dateFormat;

    @Value("${application.lgDateTimeFormat:yyyy-MM-dd HH:mm:ss}")
    private String dateTimeFormat;

    @Value("${application.lgElementPerPagePojo}")
    private int ELEMENTS_PER_PAGE;

    public PojoTableFactory() {

    }

    public <T extends Element, C extends Element, R extends Element> void createTableDialogForSelection(
            R root,
            T parentBean,
            String relationName,
            RelationMemento relationMemento,
            Consumer<T> setCurrentBean,
            Consumer<RelationMemento> onCommit) {

        String tenant = (String) VaadinSession.getCurrent().getSession().getAttribute("TENANT");

        Class<T> parentClass = (Class<T>) parentBean.getClass();
        
        
        relationUtils.getRelationClass(parentClass, relationName).ifPresent(childClass -> {
            relationUtils.getRelationType(parentClass, relationName).ifPresent(relationType -> {
                AndFilters exclusionAndFilters = AndFilters.build(
                        QueryFilter.build("status", FilterRelation.NOT_EQUAL, Status.CANCELLED),
                        QueryFilter.build("status", FilterRelation.NOT_EQUAL, Status.RETIRED)
                );
                FindRelationParameter<T, C> exclusionParam = new FindRelationParameter(
                        parentBean,
                        relationName,
                        Optional.of(exclusionAndFilters)
                );
                FindRelationQuery exclusionSearchQuery = new FindRelationQuery(exclusionParam);
                Collection exclusion = dao.runQuery(exclusionSearchQuery,
                        new ArrayList<>(),
                        Optional.of(0), Optional.empty(),
                        Optional.of(tenant),
                        Optional.empty());

                Dialog dialog2 = new Dialog();
                TableView<C> tableView = SpringBeanFactory.create(TableView.class);

                fieldUtils.getReferenceOwnerField(childClass).ifPresentOrElse(referenceOwnerField ->{
                    AndFilters inclusionAndFilters = AndFilters.build(
                            QueryFilter.build((String)referenceOwnerField, FilterRelation.EQUAL,parentBean.getId())
                    );

                    tableView.constructForSelection(childClass,
                            Optional.of(relationType),
                            dialog2,
                            exclusion,
                            Optional.of(inclusionAndFilters));
                },()->{
                    tableView.constructForSelection(childClass,
                            Optional.of(relationType),
                            dialog2,
                            exclusion,
                            Optional.empty());
                });
                        
                dialog2.setModal(true);
                dialog2.setCloseOnEsc(false);
                dialog2.setCloseOnOutsideClick(false);

                tableView.getStyle().set("overflow-y", "auto");
                tableView.doSearch();
                dialog2.add(tableView);
                dialog2.setHeightFull();
                dialog2.addOpenedChangeListener(event -> {
                    if (!event.isOpened()) {
                        Set selections = tableView.getSelected();

                        Set<Dual<T, C>> duals = dao.saveAndAssociate(
                                selections,
                                parentBean,
                                relationName,
                                rc -> {
                                    onCommit.accept(relationMemento);
                                });

                        if (duals.isEmpty() == false) {
                            setCurrentBean.accept(duals.iterator().next().getFirst()); //re associate the parent to current object
                        }
                        PojoTableFactory.this.searchAndRedrawTable(relationMemento);

                    }
                });

                dialog2.open();
            });
        });
    }

    public <T extends Element, C extends Element> void createTable(
            HasComponents layout,
            RelationMemento relationMemento,
            Optional<HasComponents> buttonLayout,
            ComponentEventListener<ItemClickEvent<C>> rowItemClickListener,
            ComponentEventListener<ClickEvent<Button>> addLinkButtonListener,
            ComponentEventListener<ClickEvent<Button>> linkButtonListener,
            ComponentEventListener<ClickEvent<Button>> deleteButtonListener,
            ComponentEventListener<ClickEvent<Button>> clearSelectionButtonListener) {

        createTable(
                layout,
                relationMemento,
                buttonLayout,
                rowItemClickListener,
                Optional.empty(),
                Optional.empty(),
                addLinkButtonListener,
                linkButtonListener,
                deleteButtonListener,
                clearSelectionButtonListener);
    }

    public <T extends Element, C extends Element> void createTable(
            HasComponents layout,
            RelationMemento<T, C> relationMemento,
            Optional<HasComponents> buttonLayout,
            ComponentEventListener<ItemClickEvent<C>> rowItemClickListener) {
        createTable(
                layout,
                relationMemento,
                buttonLayout,
                rowItemClickListener,
                null,
                null,
                null,
                null);
    }

    public <T extends Element, C extends Element, L extends Component, R extends SimpleCustomComponentRenderer<C, L>>
            void createTable(
                    HasComponents layout,
                    RelationMemento<T, C> relationMemento,
                    Optional<HasComponents> oButtonLayout,
                    ComponentEventListener<ItemClickEvent<C>> rowItemClickListener,
                    Optional<Boolean> goToLastPage,
                    Optional<AndFilters> andFilters,
                    Optional<R> oRenderer) {
        createTable(
                layout,
                relationMemento,
                oButtonLayout,
                rowItemClickListener,
                Optional.empty(),
                oRenderer,
                null,
                null,
                null,
                null);
    }

    private <T extends Element, C extends Element, L extends Component, R extends SimpleCustomComponentRenderer<C, L>>
            void createTable(
                    HasComponents layout,
                    RelationMemento<T, C> relationMemento,
                    Optional<HasComponents> oButtonLayout,
                    ComponentEventListener<ItemClickEvent<C>> rowItemClickListener,
                    Optional<Boolean> goToLastPage,
                    Optional<R> oRenderer,
                    ComponentEventListener<ClickEvent<Button>> addLinkButtonListener,
                    ComponentEventListener<ClickEvent<Button>> linkButtonListener,
                    ComponentEventListener<ClickEvent<Button>> deleteButtonListener,
                    ComponentEventListener<ClickEvent<Button>> clearSelectionButtonListener) {

        Map<String, Grid.Column<?>> columns = new HashMap<>();
        FindRelationQuery relationQuery = createRelationQuery(
                relationMemento.getSearchText(),
                relationMemento.getAndFilters(),
                relationMemento.getParent(),
                relationMemento.getRelationName());

        //Get date format (for dates)
        final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        //Get date time format (for dates)
        final SimpleDateFormat sdtf = new SimpleDateFormat(dateTimeFormat);

        DataProvider dataProvider = null;

        

        gridBuilder.build(
                relationMemento.getChildClass(),
                relationMemento.getGrid(),
                columns,
                oRenderer,
                rowItemClickListener
        );
        
         //Page navigation
        createPageNavAndAssociateToRelationMemento(relationMemento);
       

        //Add data or not
       if (relationMemento.getCounter() > 0) {
            dataProvider = createDataProvider(relationQuery,
                    relationMemento,
                    relationMemento
                            .getSortFieldAsc());
            relationMemento.getGrid().setItems(dataProvider);
        }
        
        

        //Deal with buttons 
        oButtonLayout.ifPresent(buttonLayout -> {

            WebEntity childWebEntity = relationMemento.getChildClass().getAnnotation(WebEntity.class);
            if (childWebEntity != null) {
                if (childWebEntity.type() == WebEntityType.NOMINAL || childWebEntity.type() == WebEntityType.ROOT) {
                    if (addLinkButtonListener != null) {
                        Button btnAddNew = new Button("Add", addLinkButtonListener);
                        btnAddNew.setId("btnAddNew-" + relationMemento.getParent().getClass().getSimpleName() + "." + relationMemento.getRelationName());
                        buttonLayout.add(btnAddNew);
                        //editableComponents.add(Triple.of(rc.getRelationshipName(), PojoTableButtonType.BTN_ADD_NEW, btnAddNew));
                        relationMemento.setBtnAddLinkNew(btnAddNew);
                    }
                } else {
                    if (linkButtonListener != null) {
                        Button btnLinkExisting = new Button("Link", linkButtonListener);
                        btnLinkExisting.setId("btnAddNew-" + relationMemento.getParent().getClass().getSimpleName() + "." + relationMemento.getRelationName());
                        buttonLayout.add(btnLinkExisting);
                        //editableComponents.add(Triple.of(rc.getRelationshipName(), PojoTableButtonType.BTN_LINK, btnLinkExisting));
                        relationMemento.setBtnAddLinkNew(btnLinkExisting);
                    }
                }
            }

            if (deleteButtonListener != null) {
                if (childWebEntity.type() == WebEntityType.NOMINAL || childWebEntity.type() == WebEntityType.ROOT) {
                    Button btnDeleteSelected = new Button("Delete", deleteButtonListener);
                    btnDeleteSelected.setId("btnDeleteSelected-" + relationMemento.getParent().getClass().getSimpleName() + "." + relationMemento.getRelationName());
                    buttonLayout.add(btnDeleteSelected);
                    relationMemento.setBtnDeleteUnlink(btnDeleteSelected);
                } else {
                    Button btnDeleteSelected = new Button("Unlink", deleteButtonListener);
                    btnDeleteSelected.setId("btnDeleteSelected-" + relationMemento.getParent().getClass().getSimpleName() + "." + relationMemento.getRelationName());
                    buttonLayout.add(btnDeleteSelected);
                    relationMemento.setBtnDeleteUnlink(btnDeleteSelected);
                }
            }

            if (clearSelectionButtonListener != null) {
                Button btnClearSelected = new Button("Clear", clearSelectionButtonListener);
                buttonLayout.add(btnClearSelected);
            }
        });

        //Put all together
        Div wrapper = new Div();
        wrapper.setId("wrapper");

        //Control width of dependency table
        wrapper.getStyle().set("width", "100%");
        wrapper.getStyle().set("align-self", "center");

        //Style set = wrapper.getStyle().set("border","1px solid red");
        oButtonLayout.ifPresentOrElse(
                buttonLayout -> wrapper.add(
                        relationMemento.getTitle(),
                        (Component) relationMemento.getErrorMessage(),
                        (Component) buttonLayout, relationMemento.getPageNav(),
                        relationMemento.getGrid()),
                () -> wrapper.add(relationMemento.getTitle(),
                        (Component) relationMemento.getErrorMessage(),
                        relationMemento.getPageNav(),
                        relationMemento.getGrid()));
        layout.add(wrapper);

        goToLastPage.ifPresent(b -> {
            if (Boolean.TRUE.equals(b)) {
                if (relationMemento.getPage() < relationMemento.getMaxPageCount()) {
                    relationMemento.setPage(relationMemento.getMaxPageCount());
                    relationMemento.getPageNav().setPage(relationMemento.getPage(),
                            relationMemento.getMaxPageCount());
                    relationMemento.getGrid().getDataProvider().refreshAll();
                    relationMemento.getGrid().scrollToEnd();
                }
            }
        });
    }

    public <T extends Element, C extends Element> RelationMemento<T, C> createRelationMemento(
            Optional<String> searchText,
            T parentBean,
            String relationName,
            Optional<AndFilters> andFilters,
            Optional<Integer> min,
            Optional<Integer> max,
            String humanReadableRelationshipName) throws QueryException {

        return createRelationMemento(
                searchText,
                parentBean,
                relationName,
                andFilters,
                150,
                min,
                max,
                humanReadableRelationshipName,
                new ArrayList<>());
    }

    public <T extends Element, C extends Element> RelationMemento<T, C> createRelationMemento(
            Optional<String> searchText,
            T parentBean,
            String relationName,
            Optional<AndFilters> andFilters,
            Integer heightInPixel,
            Optional<Integer> min,
            Optional<Integer> max,
            String humanReadableRelationshipName,
            List<Dual<String[], Boolean>> sortFieldsAsc) throws QueryException {
        String tenant = (String) VaadinSession.getCurrent().getSession().getAttribute("TENANT");

        Class<T> parentClass = (Class<T>) parentBean.getClass();

        FindRelationQuery relationQuery = createRelationQuery(
                searchText,
                andFilters,
                parentBean,
                relationName);

        Long count = dao.countQueryResult(relationQuery,
                Optional.of(tenant),
                Optional.empty());
        
        //int maxPageCount = Long.valueOf(-Math.floorDiv(-count, ELEMENTS_PER_PAGE)).intValue();
        Grid<C> grid = new Grid<>();
        
        //grid.getStyle().set("font-size", "12px");
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeight(heightInPixel.toString() + "px");
        grid.setMultiSort(false);
        grid.setPageSize(ELEMENTS_PER_PAGE);
        String minTitle = min.map((Integer min_) -> {
            return " minimum:" + Integer.toString(min_) + " ";
        }).orElse("");
        String maxTitle = max.map((Integer max_) -> {
            return " maximum:" + Integer.toString(max_) + " ";
        }).orElse("");
        Span errorMessage = new Span();
        Label title = new Label(humanReadableRelationshipName + "(" + minTitle + maxTitle + ")");
        
        final RelationMemento<T, C> relationMemento = new RelationMemento();
        relationMemento.setGrid(grid);
        relationMemento.setPage(1);
        relationMemento.setTotal(count);
        relationMemento.setElementsPerPage(ELEMENTS_PER_PAGE);
        relationMemento.setTitle(title);
        relationMemento.setErrorMessage(errorMessage);
        relationMemento.setMin(min.orElse(0));
        relationMemento.setMax(max.orElse(-1));
        //relationMemento.setCounter(count.intValue()); //set total already set counter
        relationMemento.setSortFieldAsc(sortFieldsAsc);
        relationMemento.setParent((T) parentBean);
        relationUtils.getRelationClass(parentClass, relationName).ifPresent(childClass -> {
            relationMemento.setChildClass(childClass);
        });
        relationMemento.setRelationName(relationName);
        relationMemento.setAndFilters(andFilters);
        return relationMemento;
    }

    private <T extends Element, C extends Element> FindRelationQuery createRelationQuery(
            Optional<String> searchText,
            Optional<AndFilters> andFilters,
            T parentBean,
            String relationName) {
        FindRelationParameter<T, C> param = new FindRelationParameter(
                parentBean, relationName, andFilters);
        param.setSearchText(searchText);
        FindRelationQuery searchQuery = new FindRelationQuery(param);
        return searchQuery;
    }

    private <T, C> DataProvider<C, Void> createDataProvider(
                                            DAOQuery searchQuery,
                                            RelationMemento relationMemento, 
                                            List<Dual<String[], 
                                            Boolean>> oriSortFieldsAsc) {
        String tenant = (String) VaadinSession.getCurrent().getSession().getAttribute("TENANT");
        DataProvider<C, Void> dataProvider
                = DataProvider.fromCallbacks(// Firstly, callback fetches items based on a query
                        (var query) -> {
                            // Query.getOffSet must be call
                            query.getOffset();
                            Integer offset =  (relationMemento.getPage() - 1) * ELEMENTS_PER_PAGE;//query.getOffset();
                            // The number of items to load
                            Integer limit = query.getLimit();
                            Collection<C> data = new ArrayList<>();
                            if (query.getSortOrders().isEmpty()) {
                                data = dao.runQuery(searchQuery,
                                        oriSortFieldsAsc,
                                        Optional.of(offset),
                                        Optional.of(limit),
                                        Optional.of(tenant),
                                        Optional.empty());
                            } else {
                                List<Dual<String[], Boolean>> sortFieldsAsc = new ArrayList<>();
                                for (SortOrder sortOrder : query.getSortOrders()) {
                                    sortFieldsAsc.add(new Dual(new String[]{sortOrder.getSorted().toString()},
                                    SortDirection.ASCENDING.equals(sortOrder.getDirection())));
                                }
                                data = dao.runQuery(searchQuery,
                                        sortFieldsAsc,
                                        Optional.of(offset),
                                        Optional.of(limit),
                                        Optional.of(tenant),
                                        Optional.empty());
                            }
                            
                            //guard againts deletion of data not through the Langkuik
                            //e.g. archiving while app is running
                            if (data.size()<limit){
                                data.addAll(
                                        entityUtils.createNewObject(
                                                relationMemento.getChildClass(), limit));
                            }
                            
                            if (data.isEmpty()) {
                                return Stream.<C>empty();
                            } else {
                                return data.stream();
                            }
                        },
                        // Secondly, callback fetches the number of items
                        // for a query
                        query -> {
//                            Long count = dao.countQueryResult(searchQuery,
//                                    Optional.of(tenant),
//                                    Optional.empty());
                            Long count = relationMemento.getTotal();
                            Integer result = count.intValue();
                            if (count >= ELEMENTS_PER_PAGE) {
                                Integer finalPage = relationMemento.getMaxPageCount();//Long.valueOf(-Math.floorDiv(-count, ELEMENTS_PER_PAGE)).intValue();
                                Integer currentPage = relationMemento.getPage();
                                if (currentPage.equals(finalPage)) {
                                    Integer count4ThisPage = Long.valueOf(count % ELEMENTS_PER_PAGE).intValue();
                                    if (count4ThisPage.equals(0)) {
                                        result = ELEMENTS_PER_PAGE;
                                    } else {
                                        result = count4ThisPage;
                                    }
                                } else {
                                    result = ELEMENTS_PER_PAGE;
                                }
                            } else {
                                result = count.intValue();
                            }
                            return result;
                        });
        return dataProvider;
    }

    private <T, C> void createPageNavAndAssociateToRelationMemento(RelationMemento relationMemento) {

        T parentBean = (T) relationMemento.getParent();
        int maxPageCount = relationMemento.getMaxPageCount();
        
        PageNav pageNav = new PageNav(parentBean.getClass().getSimpleName() + "." + relationMemento.getRelationName());
        pageNav.setPage(1, maxPageCount);
        pageNav.getFirstPage().addClickListener(e -> {
            relationMemento.setPage(1);
            pageNav.setPage(relationMemento.getPage(), maxPageCount);
            relationMemento.getGrid().getDataProvider().refreshAll();
        });
        pageNav.getFinalPage().addClickListener(e -> {
            if (relationMemento.getPage() < relationMemento.getMaxPageCount()) {
                relationMemento.setPage(relationMemento.getMaxPageCount());
                pageNav.setPage(relationMemento.getPage(), relationMemento.getMaxPageCount());
                relationMemento.getGrid().getDataProvider().refreshAll();
            }
        });
        pageNav.getNextPage().addClickListener(e -> {
            if (relationMemento.getPage() < relationMemento.getMaxPageCount()) {
                relationMemento.setPage(relationMemento.getPage() + 1);
                pageNav.setPage(relationMemento.getPage(), relationMemento.getMaxPageCount());
                relationMemento.getGrid().getDataProvider().refreshAll();
            }
        });
        pageNav.getPreviousPage().addClickListener(e -> {
            if (relationMemento.getPage() > 1) {
                relationMemento.setPage(relationMemento.getPage() - 1);
                pageNav.setPage(relationMemento.getPage(), relationMemento.getMaxPageCount());
                relationMemento.getGrid().getDataProvider().refreshAll();
            }
        });
        pageNav.setWidthFull();
        relationMemento.setPageNav(pageNav);
    }

    public <T, C> void searchAndRedrawTable(
            RelationMemento relationMemento) {
        searchAndRedrawTable(relationMemento, Optional.empty());
    }

    public <T, C> void searchAndRedrawTable(
            RelationMemento relationMemento,
            Optional<Boolean> goToLastPage) {
        String tenant = (String) VaadinSession.getCurrent().getSession().getAttribute("TENANT");
        FindRelationQuery searchQuery = createRelationQuery(
                relationMemento.getSearchText(),
                relationMemento.getAndFilters(),
                relationMemento.getParent(),
                relationMemento.getRelationName());

        Long count = dao.countQueryResult(searchQuery,
                Optional.of(tenant),
                Optional.empty());
       
        Grid grid = relationMemento.getGrid();
        if (grid != null) {
            relationMemento.getGrid().setItems(createDataProvider(
                    searchQuery,
                    relationMemento, relationMemento.getSortFieldAsc()));
        }
        relationMemento.setTotal(count);
        redrawPageNav(relationMemento);

        goToLastPage.ifPresent(b -> {
            if (Boolean.TRUE.equals(b)) {
                if (relationMemento.getPage() < relationMemento.getMaxPageCount()) {
                    relationMemento.setPage(relationMemento.getMaxPageCount());
                    relationMemento.getPageNav().setPage(relationMemento.getPage(), relationMemento.getMaxPageCount());
                    relationMemento.getGrid().getDataProvider().refreshAll();
                    relationMemento.getGrid().scrollToEnd();
                }
            }
        });
        relationMemento.validate();
    }

    private <C> void redrawPageNav(RelationMemento relationMemento) {
        PageNav pageNav = relationMemento.getPageNav();
        pageNav.setPage(relationMemento.getPage(),relationMemento.getMaxPageCount());
    }
}
