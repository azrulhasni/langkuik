package com.azrul.langkuik.views.table;

//import com.azrul.langkuik.backend.Employee;
import com.azrul.langkuik.framework.entity.WebEntity;
import com.azrul.langkuik.framework.dao.DataAccessObject;
import com.azrul.langkuik.framework.dao.filter.AndFilters;
import com.azrul.langkuik.framework.dao.filter.FilterRelation;
import com.azrul.langkuik.framework.dao.filter.QueryFilter;
import com.azrul.langkuik.framework.dao.params.CreatedByMeQueryParams;
import com.azrul.langkuik.framework.dao.params.OwnedByMeQueryParams;
import com.azrul.langkuik.framework.dao.params.OwnedByMyTeamQueryParams;
import com.azrul.langkuik.framework.dao.params.QueryParams;
import com.azrul.langkuik.framework.dao.params.UnbookedWorkQueryParams;
import com.azrul.langkuik.framework.dao.query.FindAnyEntityQuery;
import com.azrul.langkuik.framework.exception.QueryException;
import com.azrul.langkuik.framework.exception.EntityIsUsedException;
import com.azrul.langkuik.framework.field.FieldUtils;
import com.azrul.langkuik.framework.relationship.RelationType;
import com.azrul.langkuik.framework.rule.EntityRule;
import com.azrul.langkuik.framework.rule.PojoRule;
import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.framework.entity.EntityUtils;
import com.azrul.langkuik.framework.entity.Status;
import com.azrul.langkuik.framework.entity.WebEntityType;
import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.standard.Castor;
import com.azrul.langkuik.framework.standard.Dual;
import com.azrul.langkuik.framework.user.UserProfile;
import com.azrul.langkuik.framework.user.UserRetrievalService;
import com.azrul.langkuik.framework.workflow.Workflow;
import com.azrul.langkuik.framework.workflow.model.StartEvent;
import com.azrul.langkuik.views.common.ConfirmationDialog;
import com.azrul.langkuik.views.common.TabWithIndex;
import com.azrul.langkuik.views.pojo.PojoWindowFactory;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.provider.SortOrder;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.Registration;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

public class TableView<T extends Element> extends Div implements AfterNavigationObserver, BeforeLeaveObserver, HasUrlParameter<String> {

    /**
     * @return the currentClass
     */
    public Class<T> getCurrentClass() {
        return currentClass;
    }

    /**
     * @param currentClass the currentClass to set
     */
    public void setCurrentClass(Class<T> currentClass) {
        this.currentClass = currentClass;
    }

    @Autowired
    private Workflow workflow;

    @Value("${application.lgDateFormat:yyyy-MM-dd}")
    private String dateFormat;

    @Value("${application.lgFullurl}")
    private String fullUrl;

    @Autowired
    ValidatorFactory validatorFactory;

    @Autowired
    private DataAccessObject<T> dao;

    @Autowired
    private FieldUtils fieldUtils;

    @Autowired
    private EntityUtils entityUtils;

    @Autowired
    private GridBuilder<T> gridBuilder;

    @Autowired
    UserRetrievalService userRetrievalService;

    @Autowired
    private PojoWindowFactory<T> pojoWindowFactory;

    @Autowired
    @Qualifier("CanDeleteOrElseUnlinkEntityInRelationshipRule")
    private PojoRule canDeleteOrElseUnlinkEntityInRelationshipRule;

    @Autowired
    @Qualifier("CanCreateNewRule")
    private EntityRule canCreateNewRule;

    public enum Mode {
        MAIN, SELECT //either this is a MAIN table or a table used to SELECT something
    }

    private Mode mode;
    private Grid<T> grid;
    private PageNav pageNav;

    @Value("${application.lgElementPerPagePojo:3}")
    private Integer ELEMENTS_PER_PAGE;

    private Integer maxPageCount = 1;

    private Integer page;
    private FindAnyEntityQuery<T> searchQuery;
    private List<Dual<String[], Boolean>> sortFieldsAsc;
    private Map<String, Column<?>> columns;
    private Class<T> currentClass;
    private Collection<T> exclusion;
    private Dialog dialog;
    private RelationType relationType;
    private QueryParams queryParams = null;
    private HorizontalLayout commands;
    private String worklist = null;
    private Registration listener;
    private int tabletsBreakPoint = 769;
    private int phoneBreakPoint = 480;
    private AndFilters andFilters;
    private VerticalLayout center;

    private Notification notifCannotDelete = new Notification(
            "This item cannot be deleted", 3000, Notification.Position.TOP_START);

    private Notification notifProblemWithQuery = new Notification(
            "", 3000, Notification.Position.TOP_START);

    public TableView() {
        this.page = 1;
        this.columns = new HashMap<>();
        this.exclusion = new ArrayList<>();
        this.relationType = RelationType.NA;
        this.commands = new HorizontalLayout();
        this.center = new VerticalLayout();
    }

    public TableView(Class<T> tclass, TableView.Mode mode) {
        this();
        this.currentClass = tclass;
        this.mode = mode;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe width change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            adjustVisibleGridColumns(grid, event.getWidth());
        }));
        // Adjust Grid according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserWidth = receiver.getBodyClientWidth();
            adjustVisibleGridColumns(grid, browserWidth);
        }));
    }

    private void adjustVisibleGridColumns(Grid<T> grid, int width) {
        int colCount = grid.getColumns().size();
        //boolean[] visibleCols;
        // Change which columns are visible depending on browser width

        if (width <= phoneBreakPoint) {
            grid.getStyle().set("width", "auto");
            for (int c = 0; c < colCount; c++) {
                if (c == 0) {
                    grid.getColumns().get(c).setVisible(true);
                } else {
                    grid.getColumns().get(c).setVisible(false);
                }
            }
        } else if (width > phoneBreakPoint && width < tabletsBreakPoint) {
            grid.getStyle().set("width", "auto");
            for (int c = 0; c < colCount; c++) {
                if (c == 0) {
                    grid.getColumns().get(c).setVisible(false);
                } else if (c > 4) {
                    grid.getColumns().get(c).setVisible(false);
                } else {
                    grid.getColumns().get(c).setVisible(true);
                }
            }
        } else {
            grid.getStyle().set("width", "auto");
            for (int c = 0; c < colCount; c++) {
                if (c == 0) {
                    grid.getColumns().get(c).setVisible(false);
                } else {
                    grid.getColumns().get(c).setVisible(true);
                }
            }
        }
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }

    @Override
    public void setParameter(BeforeEvent be, @OptionalParameter String t) {
        String userIdentifier = (String) VaadinSession
                .getCurrent().getSession().getAttribute("USER_IDENTIFIER");

        QueryParameters qp = be.getLocation().getQueryParameters();
        if (qp.getParameters().containsKey("worklist")) {
            worklist = qp.getParameters().get("worklist").iterator().next();
            if (qp.getParameters().containsKey("ownedByMeAndMyTeam")) {
                if (("true").equals(qp.getParameters()
                        .get("ownedByMeAndMyTeam").iterator().next())) {
                    OwnedByMyTeamQueryParams qpr = new OwnedByMyTeamQueryParams();
                    qpr.setWorklist(worklist);
                    qpr.setExcludedUsername(userIdentifier); //exclude the current users from the list of work to be managed/released. If the user wants to release, he can just edit it under his Owned work
                    setQueryParams(qpr);
                } else {
                    UnbookedWorkQueryParams qpr = new UnbookedWorkQueryParams();
                    qpr.setWorklist(worklist);
                    setQueryParams(qpr);
                }
            } else {
                UnbookedWorkQueryParams qpr = new UnbookedWorkQueryParams();
                qpr.setWorklist(worklist);
                setQueryParams(qpr);
            }
        }

        if (qp.getParameters().containsKey("ownedByMe")) {
            if (("true").equals(qp.getParameters().get("ownedByMe").iterator().next())) {
                OwnedByMeQueryParams qpr = new OwnedByMeQueryParams();
                qpr.setOwnerId(userIdentifier);
                setQueryParams(qpr);
            }
        }

        if (qp.getParameters().containsKey("") || qp.getParameters().isEmpty()) {
            //String userIdentifier = (String) VaadinSession.getCurrent().getSession().getAttribute("USER_IDENTIFIER");
            CreatedByMeQueryParams qpr = new CreatedByMeQueryParams();
            qpr.setCreator(userIdentifier);
            setQueryParams(qpr);
        }
    }

    public void constructForSelection(Class<T> tclass,
            Optional<RelationType> relationType,
            Dialog dialog,
            Collection<T> exclusion,
            Optional<AndFilters> andFilters) {
        this.dialog = dialog;
        this.setCurrentClass(tclass);
        this.mode = TableView.Mode.SELECT;
        this.exclusion = exclusion;
        this.relationType = relationType.orElse(RelationType.NA);
        this.andFilters = andFilters.orElse(AndFilters.empty());

        construct();
    }

    //construct is being called in 2 occasions
    //1) when the TableView is being created when 
    //      /main is open <--called by PostConstruct
    //2) manually thru construct(...)
    @PostConstruct //Vaadin only inject after constructor is done
    private void construct() {

        if (getCurrentClass() == null) {
            //constructing Table (using SpringBeanFactory) in 
            //'PojoTableFactory's Link Existing' means calling 
            //construct automatically (and thus call PostConstruct). 
            //This is a problem because currentClass has not been set. 
            //So, if currentClass is null (i.e. we are calling this method in
            //'PojoTableFactoory's Link Existing')
            //then, just return.
            return;
        }

        UserProfile user = (UserProfile) VaadinSession
                .getCurrent()
                .getSession()
                .getAttribute("USER");

        if (user != null) {
            if (!user.getOtherAttributes().isEmpty()) {
                List<String> rolesManaged = (List<String>) user
                        .getOtherAttributes()
                        .get("Manager-of-role");
                if (getQueryParams() instanceof OwnedByMyTeamQueryParams) {
                    if (rolesManaged == null) {
                        return;
                    }
                    if (rolesManaged.isEmpty()) {
                        return;
                    }
                }
            }
        }

        setId("master-detail-view");

        //Filter out CANCELLED and RETIRED in Select
        //but keep RETIRED for Main
        if (andFilters == null) {
            andFilters = AndFilters.empty();
        }

        andFilters.addFilter(QueryFilter.build(
                "status",
                FilterRelation.NOT_EQUAL,
                Status.CANCELLED
        ));

        if (TableView.Mode.SELECT.equals(mode)) {
            andFilters.addFilter(QueryFilter.build(
                    "status",
                    FilterRelation.NOT_EQUAL,
                    Status.RETIRED
            ));
        }

        searchQuery = new FindAnyEntityQuery<>(
                getCurrentClass(),
                dateFormat,
                exclusion,
                Optional.of(andFilters)
        );

        // Configure Grid
        grid = new Grid<>();
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.getStyle().set("width", "auto");
        //grid.getStyle().set("width", "80%");
        grid.setId("tableSearchResult");
        //grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightByRows(true);
        grid.setMultiSort(false);
        //grid.set
        grid.setPageSize(ELEMENTS_PER_PAGE);
        //grid.getStyle().set("font-size", "12px");
        if (relationType == RelationType.X_TO_MANY) {
            grid.setSelectionMode(Grid.SelectionMode.MULTI);
        } else {
            grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        }

        gridBuilder.build(getCurrentClass(), grid, columns,
                Optional.empty(), e -> {
            if (e.getClickCount() == 2) {
                if (mode == Mode.MAIN) {
                    T bean = e.getItem();
                    putPojoInCenter(bean);
                }
            }
        });

        //add buttons
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        setupButtons();

        center.add(createGridLayout(getCurrentClass(),
                notifProblemWithQuery));

        verticalLayout.add(commands,
                center);
        this.add(verticalLayout);
    }

    private void setupButtons() {
        commands.removeAll();
        if (mode.equals(Mode.SELECT)) {

            commands.add(new Button("Back", e -> {
                grid.deselectAll();
                if (dialog != null) {
                    dialog.close();
                }
            }));

            commands.add(new Button("Clear selection", e -> {
                grid.deselectAll();
            }));

            Button btnSelectAndBack = new Button("Select and back", e -> {
                if (dialog != null) {
                    dialog.close();
                }
            });
            btnSelectAndBack.setId("btnSelectAndBack-"
                    + getCurrentClass().getSimpleName());
            commands.add(btnSelectAndBack);

        } else { //Mode=MAIN, for Root

            if (canCreateNewRule.compute(Optional.ofNullable(getCurrentClass()
                    .getAnnotation(WebEntity.class)))) {
                Set<StartEvent> startEvents = workflow.getStartEvents();
                Button btnCreateNew = new Button("Create new");
                ContextMenu createNewContextMenu = new ContextMenu();
                if (entityUtils.getEntityType(currentClass) == WebEntityType.REF) {
                    btnCreateNew.addClickListener(ev -> {
                                    T bean = createNew(getCurrentClass(),
                                            Optional.empty(),
                                            Optional.empty(),
                                            Optional.empty());
                                    putPojoInCenter(bean);
                                });
                } else if (startEvents.size() == 1){
                     btnCreateNew.addClickListener(ev -> {
                                    T bean = createNew(getCurrentClass(),
                                            Optional.of(startEvents.iterator().next().getId()),
                                            Optional.of(startEvents.iterator().next().getDescription()),
                                            Optional.empty());
                                    putPojoInCenter(bean);
                                });
                } else {
                    int i = 0;
                    for (StartEvent startEvent : startEvents) {
                        createNewContextMenu.addItem(startEvent.getDescription() != null ? startEvent.getDescription()
                                : (i == 0 ? "Work element" : "Work element " + i),
                                ev -> {
                                    T bean = createNew(getCurrentClass(),
                                            Optional.of(startEvent.getId()),
                                            Optional.of(startEvent.getDescription()),
                                            Optional.empty());
                                    putPojoInCenter(bean);
                                });
                        i++;

                    }

                    createNewContextMenu.setTarget(btnCreateNew);
                    createNewContextMenu.setOpenOnClick(true);
                }
                btnCreateNew.setId("btnCreateNew");
                commands.add(btnCreateNew);
            }

            Castor.<T, WorkElement>given(getCurrentClass())
                    .castItTo(WorkElement.class)
                    .thenDo(() -> {
                        if (worklist != null) {
                            Button btnOpenHighestPriority = new Button("Open highest priority", e -> {
                                getItemWithHighestPriority().ifPresent(item -> {
                                    String userIdentifier = (String) VaadinSession.getCurrent().getSession().getAttribute("USER_IDENTIFIER");
                                    ((WorkElement) item).getWorkflowInfo().getOwners().add(userIdentifier);
                                    dao.save(item);
                                    putPojoInCenter(item);
                                });
                            });
                            btnOpenHighestPriority.setId("btnOpenHighestPriority");
                            commands.add(btnOpenHighestPriority);
                        }
                    }).go();

            commands.add(new Button("Clear selection", e -> {
                grid.deselectAll();
            }));

            if (WebEntityType.REF.equals(entityUtils.getEntityType(currentClass))) {
                Button btnRetire = new Button("Retire", e -> {

                    ConfirmationDialog confirmRetireDialog = new ConfirmationDialog("Confirm retirement of selected item",
                            "Are you sure you want to retire the selected item?", "Retire", (ce) -> {
                                Set<T> itemsToBeRetired = grid.getSelectedItems();
                                Castor.<T, Element>given(currentClass)
                                        .castItTo(Element.class)
                                        .thenDo(() -> {

                                            dao.retire(itemsToBeRetired);
                                            redoSearch();

                                        }).failingWhichDo(() -> {
                                    //if not Element, just ignore it

                                }).go();
                            },
                            "Cancel", (ce) -> {
                            });
                    confirmRetireDialog.open();
                });
                btnRetire.setId("btnRetire");
                commands.add(btnRetire);

                Button btnUnRetire = new Button("Undo retire", e -> {

                    ConfirmationDialog confirmUnRetireDialog = new ConfirmationDialog("Confirm undo retirement of selected item",
                            "Are you sure you want to undo the retirement the selected item?", "Undo retire", (ce) -> {
                                Set<T> itemsToBeRetired = grid.getSelectedItems();
                                Castor.<T, Element>given(currentClass)
                                        .castItTo(Element.class)
                                        .thenDo(() -> {
                                            dao.unretire(itemsToBeRetired);
                                            redoSearch();
                                        }).failingWhichDo(() -> {
                                    //if not Element, just ignore it

                                }).go();
                            },
                            "Cancel", (ce) -> {
                            });
                    confirmUnRetireDialog.open();
                });
                btnUnRetire.setId("btnUnRetire");
                commands.add(btnUnRetire);
            }

            Button btnDelete = new Button("Delete", e -> {
                final List<String> cannotBeDeleted = new ArrayList<>();

                ConfirmationDialog confirmDeleteDialog = new ConfirmationDialog("Confirm deletion",
                        "Are you sure you want to delete the item?", "Delete", (ce) -> {
                            Set<T> itemsToBeDeleted = grid.getSelectedItems();
                            Castor.<T, Element>given(currentClass)
                                    .castItTo(Element.class)
                                    .thenDo(() -> {
                                        Boolean canDelete = itemsToBeDeleted
                                                .stream()
                                                .anyMatch(item
                                                        -> {
                                                    return canDeleteOrElseUnlinkEntityInRelationshipRule
                                                            .compute(Optional.of((Element) item));
                                                }
                                                );
                                        if (canDelete == true) {
                                            try {
                                                dao.delete(itemsToBeDeleted);
                                                redoSearch();
                                            } catch (EntityIsUsedException ex) {
                                                Logger.getLogger(TableView.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        } else {
                                            //if we can't delete, just notify
                                            //there is no "unlink" here because we are in the MAIN table
                                            notifCannotDelete.open();
                                        }
                                    }).failingWhichDo(() -> {//if not Element, just delete it
                                try {
                                    dao.delete(itemsToBeDeleted);
                                    redoSearch();
                                } catch (EntityIsUsedException ex) {
                                    Logger.getLogger(TableView.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }).go();
                        },
                        "Cancel", (ce) -> {
                        });
                confirmDeleteDialog.open();
            });
            btnDelete.setId("btnDelete");
            commands.add(btnDelete);
        }
    }

    private void putPojoInCenter(T item) {
        commands.setVisible(false);
        center.removeAll();
        center.add(pojoWindowFactory
                .createMainPojoWindow(
                        item,
                        Optional.of(() -> {
                            redoSearch();
                            center.removeAll();
                            center.add(createGridLayout(getCurrentClass(),
                                    notifProblemWithQuery));
                            commands.setVisible(true);
                        })));
    }

    private Optional<T> getItemWithHighestPriority() {

        if (worklist != null) {
            String tenant = (String) VaadinSession.getCurrent().getSession().getAttribute("TENANT");

            UnbookedWorkQueryParams qpr = new UnbookedWorkQueryParams();
            qpr.setWorklist(worklist);
            List<Dual<String[], Boolean>> sortFields = new ArrayList<>();
            sortFields.add(new Dual(new String[]{"priority"}, false));
            sortFields.add(new Dual(new String[]{"workflowInfo", "worklistUpdateTime"}, false));
            Collection<T> items = dao.runQuery(searchQuery,
                    sortFields,
                    Optional.of(0),
                    Optional.of(1),
                    Optional.of(tenant),
                    Optional.ofNullable(getQueryParams()));
            return items.isEmpty() ? Optional.empty() : items.stream().findFirst();
        } else {
            return Optional.empty();
        }

    }

    private Component createGridLayout(Class<T> tclass, Notification notifProblemWithQuery) {
        Div wrapper = new Div();
        wrapper.setId("wrapper");
        //wrapper.getStyle().set("align-self", "center");

        //wrapper.setWidthFull();
        final SearchPanel<T> searchField = new SearchPanel("Search..."/*, (mode == Mode.MAIN)*/, tclass);
        //searchField.getStyle().set("width", "80%");
        searchField.setSearchListener(ev -> {
            page = 1;
            try {
                if (ev.getSearchFieldValue().isBlank()) {
                    doSearch(Optional.empty());
                } else {
                    doSearch(Optional.of(ev.getSearchFieldValue()));
                }
            } catch (QueryException ex) {
                notifProblemWithQuery.setText(ex.getErrorMessage());
                notifProblemWithQuery.open();
            }
            pageNav.setPage(page, maxPageCount);
        });

        //searchField.setWidthFull();
        //Div searchWrapper = new Div();
        //searchWrapper.add(searchField);
        //searchWrapper.setWidthFull();
        //searchWrapper.setMinWidth("20%");
        //add page nav
        if (pageNav == null) {
            pageNav = new PageNav(getCurrentClass().getSimpleName());
        }
        pageNav.setWidthFull();
        pageNav.setPage(page, maxPageCount);
        pageNav.getFirstPage().addClickListener(e -> {
            page = 1;
            pageNav.setPage(page, maxPageCount);
            grid.getDataProvider().refreshAll();
        });
        pageNav.getFinalPage().addClickListener(e -> {
            if (page < maxPageCount) {
                page = maxPageCount;
                pageNav.setPage(page, maxPageCount);
                grid.getDataProvider().refreshAll();
            }
        });
        pageNav.getNextPage().addClickListener(e -> {
            if (page < maxPageCount) {
                page++;
                pageNav.setPage(page, maxPageCount);
                grid.getDataProvider().refreshAll();
            }
        });
        pageNav.getPreviousPage().addClickListener(e -> {
            if (page > 1) {
                page--;
                pageNav.setPage(page, maxPageCount);
                grid.getDataProvider().refreshAll();
            }
        });

        //put all together
        wrapper.add(searchField, pageNav, grid);
        //wrapper.setAlignItems(Alignment.AUTO);
        return wrapper;
    }

    public void doSearch() {
        doSearch(Optional.empty());
    }

    private void doSearch(Optional<String> queryString) throws QueryException {
        searchQuery.setQueryString(queryString);
        doSearchByQuery(searchQuery);
    }

    private void redoSearch() {
        doSearchByQuery(searchQuery);
    }

    private void doSearchByQuery(FindAnyEntityQuery<T> searchQuery) throws QueryException {
        String tenant = (String) VaadinSession.getCurrent().getSession().getAttribute("TENANT");
        //String userIdentifier = (String) VaadinSession.getCurrent().getSession().getAttribute("USER_IDENTIFIER");
        DataProvider<T, Void> dataProvider
                = DataProvider.fromCallbacks(// First callback fetches items based on a query
                        (Query<T, Void> query) -> {
                            // Query.getOffSet must be call
                            query.getOffset();
                            int offset = (page - 1) * ELEMENTS_PER_PAGE;//query.getOffset();
                            // The number of items to load
                            int limit = query.getLimit();
                            Collection<T> data = new ArrayList<>();
                            if (query.getSortOrders().isEmpty()) {
                                //if (mode == TableView.Mode.MAIN) {
                                data = dao.runQuery(searchQuery,
                                        new ArrayList<>(),
                                        Optional.of(offset),
                                        Optional.of(limit),
                                        Optional.of(tenant),
                                        Optional.ofNullable(getQueryParams()));

                            } else {
                                sortFieldsAsc = new ArrayList<>();
                                for (SortOrder sortOrder : query.getSortOrders()) {
                                    sortFieldsAsc.add(new Dual(new String[]{sortOrder.getSorted().toString()},
                                    SortDirection.ASCENDING.equals(sortOrder.getDirection())));
                                }

                                data = dao.runQuery(searchQuery,
                                        sortFieldsAsc,
                                        Optional.of(offset),
                                        Optional.of(limit),
                                        Optional.of(tenant),
                                        Optional.ofNullable(getQueryParams()));
                            }
                            return data.stream();
                        },
                        // Second callback fetches the number of items
                        // for a query
                        (Query<T, Void> query) -> {
                            Long count = dao.countQueryResult(searchQuery,
                                    Optional.of(tenant),
                                    Optional.ofNullable(getQueryParams()));

                            if (count >= ELEMENTS_PER_PAGE) {
                                Integer finalPage = Long.valueOf(-Math.floorDiv(-count, ELEMENTS_PER_PAGE)).intValue();
                                if (page == (finalPage)) {
                                    int count4ThisPage = Long.valueOf(count % ELEMENTS_PER_PAGE).intValue();
                                    if (count4ThisPage == 0) {
                                        return ELEMENTS_PER_PAGE;
                                    } else {
                                        return count4ThisPage;
                                    }
                                } else {
                                    return ELEMENTS_PER_PAGE;
                                }
                            } else {
                                return count.intValue();
                            }
                        });

        Long count = dao.countQueryResult(
                searchQuery,
                Optional.of(tenant),
                Optional.ofNullable(getQueryParams()));

        maxPageCount = Long.valueOf(-Math.floorDiv(-count, ELEMENTS_PER_PAGE)).intValue();
        if (page > maxPageCount) { //this is the case where we deleted enough entities that a 
            //whole page is gone. So maxPageCount is updated but not page. This is where we update Page
            if (maxPageCount == 0) {
                page = 1;
            } else {
                page = maxPageCount;
            }
        }
        pageNav.setPage(page, maxPageCount);

        grid.setItems(dataProvider);
        dataProvider.refreshAll();

    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        if (VaadinSession.getCurrent().getSession().getAttribute("USER_IDENTIFIER") == null) {
            UI.getCurrent().getPage().executeJs("window.open(\"" + fullUrl + "/main\", \"_self\");");
            return;
        }
        TableMomento mem = ComponentUtil.getData(this, TableMomento.class);
        if (mem != null) {
            //dao.massIndex();
            this.page = mem.getPage();
            this.searchQuery = (FindAnyEntityQuery<T>) mem.getSearchQuery();
            this.sortFieldsAsc = mem.getSortFieldsAsc();
        }

        if (pageNav == null) {
            pageNav = new PageNav(getCurrentClass().getSimpleName());
        }

        doSearch();
        if (worklist != null) {
            setupButtons();
        }
        pageNav.setPage(page, maxPageCount);
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        //System.out.println("Parameters:"+event.getLocation()
        //.getQueryParameters().getParameters());
        TableMomento mem = new TableMomento(page, searchQuery, sortFieldsAsc);
        ComponentUtil.setData(this, TableMomento.class, mem);
    }

    public Set<T> getSelected() {
        return grid.getSelectedItems();
    }

    private T createNew(Class<T> tclass,
            Optional<String> startEventId,
            Optional<String> startEventDesc,
            Optional<String> parentEnumPath) {
        String userIdentifier = (String) VaadinSession
                .getCurrent().getSession().getAttribute("USER_IDENTIFIER");
        String tenant = (String) VaadinSession
                .getCurrent().getSession().getAttribute("TENANT");
        Status status = null;
        if (WebEntityType.REF.equals(entityUtils.getEntityType(tclass))) {
            status = Status.DONE;
        } else {
            status = Status.DRAFT;
        }
        Optional<T> bean = (Optional<T>) dao.createAndSave(
                tclass,
                Optional.of(tenant),
                parentEnumPath,
                startEventId,
                startEventDesc,
                status,
                userIdentifier);

        T e = bean.get();
        Castor.<T, WorkElement>given(e)
                .castItTo(WorkElement.class)
                .thenDo(ext -> {
                    ext.getWorkflowInfo()
                            .setWorklist(ext.getWorkflowInfo().getStartEventId());
                }).go();
        return e;
    }

    /**
     * @return the queryParams
     */
    private QueryParams getQueryParams() {
        return queryParams;
    }

    /**
     * @param queryParams the queryParams to set
     */
    private void setQueryParams(QueryParams queryParams) {
        this.queryParams = queryParams;
    }

}
