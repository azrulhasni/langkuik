/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.findusage.view;

import com.azrul.langkuik.custom.CustomFieldRenderer;
import com.azrul.langkuik.framework.entity.WebEntity;
import com.azrul.langkuik.framework.relationship.WebRelation;
import com.azrul.langkuik.custom.CustomField;
import com.azrul.langkuik.framework.format.Format;
import com.azrul.langkuik.framework.dao.DataAccessObject;
import com.azrul.langkuik.framework.standard.Dual;
import com.azrul.langkuik.framework.findusage.query.FindUsageQuery;
import com.azrul.langkuik.framework.factory.SpringBeanFactory;
import com.azrul.langkuik.framework.expression.Expression;
import com.azrul.langkuik.framework.field.FieldContainer;
import com.azrul.langkuik.framework.field.FieldUtils;
import com.azrul.langkuik.framework.relationship.RelationMemento;
import com.azrul.langkuik.framework.relationship.RelationUtils;
import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.framework.entity.Status;
import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.field.FieldVisibility;
import com.azrul.langkuik.framework.standard.Castor;
import com.azrul.langkuik.views.common.MainView;
import com.azrul.langkuik.views.pojo.PojoView;
import com.azrul.langkuik.views.table.PageNav;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.provider.SortOrder;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import java.lang.reflect.Field;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.EntityManagerFactory;
import javax.validation.ValidatorFactory;
import org.apache.commons.collections4.MultiValuedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author azrul
 */
@Route(value = "FindUsageView", layout = MainView.class)
public class FindUsageView<P extends Element, T extends Element> extends Div {

    @Value("${application.lgDateFormat:yyyy-MM-dd}")
    private String dateFormat;

    @Value("${application.lgDateTimeFormat:yyyy-MM-dd HH:mm:ss}")
    private String dateTimeFormat;

    @Value("${application.lgElementPerPagePojo}")
    private int ELEMENTS_PER_PAGE;

    @Autowired
    ValidatorFactory validatorFactory;

    @Autowired
    private DataAccessObject dao;

    @Autowired
    private EntityManagerFactory emf;

    @Autowired
    RelationUtils relationUtils;

    @Autowired
    FieldUtils fieldUtils;

    @Autowired
    Expression<Boolean, T> expr;

    private Map<String, RelationMemento> relationMementos = new HashMap<>();

    public FindUsageView() {
    }

    public void construct(T current, Dialog dialog) {
        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.add(new Button("Back", e -> {
            dialog.close();
        }));
        this.add(headerLayout);
        Collection<Dual<Class<?>, Field>> parentClasses = relationUtils.getAllDependingClass(current.getClass(), emf);
        for (Dual<Class<?>, Field> parentClass : parentClasses) {
            WebEntity webEntity = parentClass.getFirst().getAnnotation(WebEntity.class);
            if (webEntity != null) {
                HorizontalLayout buttonLayout = new HorizontalLayout();
//                Button btnClearSelected = new Button("Clear selection", e -> {
//                    relationMementos.get(parentClass.getSecond()).getGrid().deselectAll();
//                });
//                buttonLayout.add(btnClearSelected);

                WebRelation webRelation = parentClass.getSecond().getAnnotation(WebRelation.class);
                if (webRelation == null) {
                    continue;
                }

                VerticalLayout verticalLayout = new VerticalLayout();
                //verticalLayout.add(new Span(webEntity.name()+ "::" + webRelation.name()));
                createTable(current,
                        parentClass.getSecond().getName(),
                        parentClass.getFirst(),
                        webEntity.name() + "::" + webRelation.name(),
                        verticalLayout,
                        relationMementos,
                        buttonLayout, e -> {
                            if (e.getClickCount() == 2) {
                                Element ext = (Element) e.getItem();
                                if (ext.getId() != null) {
                                    createPojoDialog((P) e.getItem(), parentClass.getSecond().getName(), current, relationMementos.get(parentClass.getSecond().getName()));

                                }
                            }
                        });
                this.add(verticalLayout);
            }
        }

    }

    private <R extends WorkElement, P extends Element> void createPojoDialog(P parent, String relationName, T child, RelationMemento memento) {

        Dialog dialog2 = new Dialog();
        PojoView<R, P> pojoView = SpringBeanFactory.create(PojoView.class);
        pojoView.construct(parent, Optional.of(dialog2));
        dialog2.setModal(true);
        dialog2.setCloseOnEsc(false);
        dialog2.setCloseOnOutsideClick(false);
        dialog2.addOpenedChangeListener(event -> {
            if (!event.isOpened()) {
                if (pojoView.getCurrentBean() != null) {
                    Optional<P> oresult = dao.save(parent);
                    P result = oresult.orElseThrow();
                    redrawTables(child,
                            relationName, parent.getClass(), memento);
                }

            }
        });

        //begin:enable scroll
        pojoView.setHeight("100%");
        pojoView.getStyle().set("overflow-y", "auto");
        dialog2.add(pojoView);
        dialog2.setHeightFull();
        //end:enable scroll
        dialog2.open();

        //UI.getCurrent().navigate(PojoView.class);
    }

    private <P, T> void createTable(T currentBean,
            String parentToCurrentRelation,
            Class<P> parentClass,
            String humanReadableRelationshipName,
            VerticalLayout layout,
            Map<String, RelationMemento> relationMementos,
            Component buttonLayout,
            ComponentEventListener<ItemClickEvent<P>> rowItemClickListener) {

        String tenant = (String) VaadinSession.getCurrent().getSession().getAttribute("TENANT");

        //Get date format (for dates)
        final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        //Get date time format (for dates)
        final SimpleDateFormat sdtf = new SimpleDateFormat(dateTimeFormat);

        Map<String, Grid.Column<?>> columns = new HashMap<>();

        FindUsageQuery findUsageQuery = new FindUsageQuery(currentBean,
                parentToCurrentRelation,
                parentClass);

        Long count = dao.countQueryResult(findUsageQuery,
                Optional.of(tenant),
                Optional.empty());
        //int maxPageCount = Long.valueOf(-Math.floorDiv(-count, ELEMENTS_PER_PAGE)).intValue();

        Grid<P> grid = new Grid<>();
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightByRows(true);
        grid.setMultiSort(false);
        grid.setPageSize(ELEMENTS_PER_PAGE);

        DataProvider dataProvider = null;

        MultiValuedMap<Integer, FieldContainer> fieldStore = fieldUtils.getFieldsByOrder(parentClass);

        //sort fields by order
        List<Integer> orders = new ArrayList<>(fieldStore.keySet());
        Collections.sort(orders);

        //Add status icons
        grid.addComponentColumn((ValueProvider<P, Component>) source -> {
            
            return (Component) Castor.<P, WorkElement>given(source)
                    .castItTo(WorkElement.class)
                    .thenDo(ext -> {
                        Icon icon = null;
                        if (Status.DRAFT.equals(ext.getStatus())) {
                            icon = new Icon(VaadinIcon.EDIT);
                        } else if (Status.IN_PROGRESS.equals(ext.getStatus())) {
                            icon = new Icon(VaadinIcon.ARROW_FORWARD);
                        } else {
                            icon = new Icon(VaadinIcon.CHECK_SQUARE_O);
                        }
                        icon.setSize("0.8em");
                        Div div = new Div();
                        div.add(icon);
                        div.setTitle(ext.getStatus().toString());
                        return (Component)div;
                    }).go().orElseGet(() -> new Label(""));
        }).setFrozen(true).setAutoWidth(true);

        //Add fields to grid
        for (Integer order : orders) {
            for (FieldContainer fc : fieldStore.get(order)) {

                //Only display what is visible
                if (calculateShowInTable(parentClass, fc).equals(Boolean.TRUE)) {
                    /*if (Blob.class.equals(fc.getReturnType())) {
                            //Handle attachment
                            Grid.Column<?> column = grid.addComponentColumn(source -> {
                                Attachment attachment = (Attachment) source;
                                if (attachment.getFileName() == null) {
                                    return new Label();
                                }
                                Anchor download = new Anchor(new StreamResource(attachment.getFileName(), dao.getInputStreamFactory(attachment)), "");
                                download.getElement().setAttribute("download", true);
                                download.add(new Button(new Icon(VaadinIcon.DOWNLOAD_ALT)));
                                return download;
                            }
                            ).setResizable(true);;
                            columns.put(fc.getField().getName(), column);

                        } else {*/
                    //Add column
                    Optional<Class> oCustomRendererClass = fc.getCustomComponent();
                    oCustomRendererClass.ifPresentOrElse(customRendererClass -> {
                        Grid.Column<?> column = grid.addComponentColumn(source -> {
                            CustomField customField = (CustomField) ((Class) customRendererClass).getAnnotation(CustomField.class);
                            Class rendererClass = customField.renderer();
                            CustomFieldRenderer renderer = (CustomFieldRenderer) SpringBeanFactory.create(rendererClass);

                            Optional<Component> f = renderer.createInTable(source, fc.getField().getName());
                            return f.map(c -> c).orElseGet(() -> new Label());
                        }).setHeader(fc.getWebField().displayName())
                                .setSortable(fc.isSortable())
                                .setSortProperty(fc.getField().getName())
                                .setAutoWidth(true)
                                .setResizable(true);;
                        columns.put(fc.getField().getName(), column);
                    }, () -> {
                        Grid.Column<?> column = grid.addColumn(source -> {
                            if (fc.getValue(source) != null) {
                                if (LocalDate.class.equals(fc.getReturnType())) {
                                    Date date = Date.from(((LocalDate) fc.getValue(source)).atStartOfDay(ZoneId.systemDefault()).toInstant());
                                    return sdf.format(date);
                                } else if (LocalDateTime.class.equals(fc.getReturnType())) {
                                    Date date = Date.from(((LocalDateTime) fc.getValue(source)).atZone(ZoneId.systemDefault()).toInstant());
                                    return sdtf.format(date);
                                } else { //Plain string
                                    if (fc.getWebField().format().length > 0) {
                                        Format format = fc.getWebField().format()[0];
                                        return format.prefix() + fc.getValue(source).toString();
                                    } else {
                                        return fc.getValue(source).toString(); //apply highlighter here??
                                    }
                                }
                            }
                            return "";
                        }).setHeader(fc.getWebField().displayName())
                                .setSortable(fc.isSortable())
                                .setSortProperty(fc.getField().getName())
                                .setResizable(true);
                        columns.put(fc.getField().getName(), column);
                    });
                    //}
                }
            }
        }
        //}
        //make grid clickable
        grid.addItemClickListener(rowItemClickListener);

        //Page navigation
        
        //At this stage, if no relation mementos is present represeting this relationship in the relationMementos map, add one
        RelationMemento memento = relationMementos.get(parentToCurrentRelation);
        if (memento == null) {
            memento = new RelationMemento(grid,  1,count, ELEMENTS_PER_PAGE, count.intValue());
            relationMementos.put(parentToCurrentRelation, memento);
        }
        
        createPageNavAndAssignToMemento(parentClass, parentToCurrentRelation, memento);
       
        //Add data or not
        if (count > 0) {
            dataProvider = createDataProvider(findUsageQuery, memento);
            grid.setDataProvider(dataProvider);
        }
        
        
         
        //Put all together
        Div wrapper = new Div();
        wrapper.setId("wrapper");
        //Style set = wrapper.getStyle().set("border","1px solid red");
        wrapper.setWidthFull();
        wrapper.add(new Label(humanReadableRelationshipName), buttonLayout,memento.getPageNav(), grid);
        layout.add(wrapper);

    }

    private <T, C> DataProvider<C, Void> createDataProvider(FindUsageQuery usageQuery,
            RelationMemento relationMemento) {
        String tenant = (String) VaadinSession.getCurrent().getSession().getAttribute("TENANT");
        DataProvider<C, Void> dataProvider
                = DataProvider.fromCallbacks(// Firstly, callback fetches items based on a query
                        (var query) -> {
                            // Query.getOffSet must be call
                            query.getOffset();
                            Integer offset = offset = (relationMemento.getPage() - 1) * ELEMENTS_PER_PAGE;
                            // The number of items to load
                            Integer limit = query.getLimit();
                            Collection<C> data = new ArrayList<>();
                            if (query.getSortOrders().isEmpty()) {
                                data = dao.runQuery(usageQuery,
                                        new ArrayList<>(),
                                        Optional.of(offset),
                                        Optional.of(limit),
                                        Optional.of(tenant),
                                        Optional.empty());
                            } else {
                                List<Dual<String, Boolean>> sortFieldsAsc = new ArrayList<>();
                                for (SortOrder sortOrder : query.getSortOrders()) {
                                    sortFieldsAsc.add(new Dual(sortOrder.getSorted().toString(),
                                            SortDirection.ASCENDING.equals(sortOrder.getDirection())));
                                }
                                data = dao.runQuery(usageQuery,
                                        sortFieldsAsc,
                                        Optional.of(offset),
                                        Optional.of(limit),
                                        Optional.of(tenant),
                                        Optional.empty());
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
//                            Long count = dao.countQueryResult(usageQuery,
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
                            //return result + (result - result%PojoConst.ELEMENTS_PER_PAGE);
                        });
        return dataProvider;
    }

    private <P, C> void createPageNavAndAssignToMemento(Class<P> parentClass, String relationName,
             RelationMemento relationMemento){
        Grid<C> grid = relationMemento.getGrid();
        int maxPageCount = relationMemento.getMaxPageCount();
        PageNav pageNav = new PageNav(parentClass.getSimpleName() + "." + relationName);
        pageNav.setPage(1, maxPageCount);
        pageNav.getFirstPage().addClickListener(e -> {
            relationMemento.setPage(1);
            pageNav.setPage(relationMemento.getPage(), maxPageCount);
            grid.getDataProvider().refreshAll();
        });
        pageNav.getFinalPage().addClickListener(e -> {
            if (relationMemento.getPage() < relationMemento.getMaxPageCount()) {
                relationMemento.setPage(relationMemento.getMaxPageCount());
                pageNav.setPage(relationMemento.getPage(), relationMemento.getMaxPageCount());
                grid.getDataProvider().refreshAll();
            }
        });
        pageNav.getNextPage().addClickListener(e -> {
            if (relationMemento.getPage() < relationMemento.getMaxPageCount()) {
                relationMemento.setPage(relationMemento.getPage() + 1);
                pageNav.setPage(relationMemento.getPage(), relationMemento.getMaxPageCount());
                grid.getDataProvider().refreshAll();
            }
        });
        pageNav.getPreviousPage().addClickListener(e -> {
            if (relationMemento.getPage() > 1) {
                relationMemento.setPage(relationMemento.getPage() - 1);
                pageNav.setPage(relationMemento.getPage(), relationMemento.getMaxPageCount());
                grid.getDataProvider().refreshAll();
            }
        });
        pageNav.setWidthFull();
        relationMemento.setPageNav(pageNav);
        //return pageNav;
    }

    private <P, T> void redrawTables(T currentBean,
            String parentToCurrentRelation,
            Class<P> parentClass,
            RelationMemento relationMemento) {
        String tenant = (String) VaadinSession.getCurrent().getSession().getAttribute("TENANT");
        FindUsageQuery findUsageQuery = new FindUsageQuery(currentBean,
                parentToCurrentRelation,
                parentClass);

        Long count = dao.countQueryResult(findUsageQuery,
                Optional.of(tenant),
                Optional.empty());
        //int maxPageCount = Long.valueOf(-Math.floorDiv(-count, ELEMENTS_PER_PAGE)).intValue();

        Grid grid = relationMemento.getGrid();
        if (grid != null) {
            grid.setDataProvider(createDataProvider(findUsageQuery, 
                            relationMemento));
        }
        relationMemento.setTotal(count);
        redrawPageNav(relationMemento);

    }

    private <C> void redrawPageNav(RelationMemento relationMemento) {
        PageNav pageNav = relationMemento.getPageNav();
        pageNav.setPage(relationMemento.getPage(),relationMemento.getMaxPageCount());

    }

    private <P> Boolean calculateShowInTable(Class<P> parentClass, FieldContainer fc) {
        return fieldUtils.getFieldVisibilityFromEntity(parentClass, fc.getField().getName()).map(
                fv -> (Boolean) expr.evaluate(fv.visibleInTable(), parentClass)
        ).orElseGet(
                () -> (Boolean) expr.evaluate(fc.getWebField().visibleInTable(), parentClass)
        );
    }

//    private <P> Optional<FieldVisibility> getFieldVisibilityFromEntity(Class<P> tclass, String fieldName) {
//        if (tclass.isAnnotationPresent(WebEntity.class)) {
//            WebEntity webEntity = tclass.getAnnotation(WebEntity.class);
//            for (FieldVisibility fc : webEntity.fieldVisibility()) {
//                if (fc.fieldName().equals(fieldName)) {
//                    return Optional.of(fc);
//                }
//            }
//            return Optional.empty();
//        } else {
//            return Optional.empty();
//        }
//
//    }
}
