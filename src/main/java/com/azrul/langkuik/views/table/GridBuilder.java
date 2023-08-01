/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.views.table;

import com.azrul.langkuik.custom.CustomField;
import com.azrul.langkuik.custom.CustomFieldRenderer;
import com.azrul.langkuik.custom.SimpleCustomComponentRenderer;
import com.azrul.langkuik.framework.entity.Choice;
import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.framework.entity.Status;
import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.expression.Expression;
import com.azrul.langkuik.framework.factory.SpringBeanFactory;
import com.azrul.langkuik.framework.field.FieldContainer;
import com.azrul.langkuik.framework.field.FieldUtils;
import com.azrul.langkuik.framework.format.Format;
import com.azrul.langkuik.framework.relationship.RelationContainer;
import com.azrul.langkuik.framework.relationship.RelationUtils;
import com.azrul.langkuik.framework.standard.Castor;
import com.azrul.langkuik.framework.standard.converter.ChoiceBooleanConverter;
import com.azrul.langkuik.framework.workflow.Workflow;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.ValueProvider;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.collections4.MultiValuedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author azrul
 */
@Service
public class GridBuilder<T> {

    @Autowired
    private FieldUtils fieldUtils;

    @Autowired
    private Expression expr;

    @Autowired
    private Workflow workflow;

    @Value("${application.lgDateFormat:yyyy-MM-dd}")
    private String dateFormat;

    @Value("${application.lgDateTimeFormat:yyyy-MM-dd HH:mm:ss}")
    private String dateTimeFormat;

    @Autowired
    private RelationUtils relationUtils;

    private Binder<T> binder;
    
    @Value("${application.lgElementPerPagePojo:3}")
    private Integer ELEMENTS_PER_PAGE;
    
    private ChoiceBooleanConverter choiceBooleanConv = new ChoiceBooleanConverter();
                                                    

    public <L extends Component, R extends SimpleCustomComponentRenderer<T, L>>
            void build(final Class<T> currentClass,
                    final Grid<T> grid,
                    final Map<String, Grid.Column<?>> columns,
                    final Optional<R> oRenderer,
                    final ComponentEventListener<ItemClickEvent<T>> listener) {
        //date format
        final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

        //Get date time format (for dates)
        final SimpleDateFormat sdtf = new SimpleDateFormat(dateTimeFormat);

        MultiValuedMap<Integer, FieldContainer> fieldStore = fieldUtils
                .getFieldsByOrder(currentClass);

        //Deal with mappable relations
        MultiValuedMap<Integer, RelationContainer> relationStore = relationUtils
                .getRelationsByOrder(currentClass);

        //sort fields by order
        Set<Integer> orders = new TreeSet<>(fieldStore.keySet());
        orders.addAll(relationStore.keySet());

        oRenderer.ifPresentOrElse(renderer -> {
            grid.addColumn(new ComponentRenderer<L, T>(source -> {
                Optional<L> component = renderer.render(source);
                return component.get();
            }));
        },
                () -> {

                    //Coontains all components in a row
                    grid.addComponentColumn((ValueProvider<T, Component>) source -> {
                        VerticalLayout columnPanel = new VerticalLayout();
                        for (Integer order : orders) {
                            if (fieldStore.containsKey(order)) {
                                for (FieldContainer fc : fieldStore.get(order)) {
                                    //Only display what is visible
                                    if ((calculateShowInTable(currentClass, fc)).equals(Boolean.TRUE)) {

                                        //Add column
                                        Optional<Class> oCustomRendererClass = fc.getCustomComponent();
                                        oCustomRendererClass.ifPresentOrElse(customRendererClass -> {
                                            CustomField customField = (CustomField) ((Class) customRendererClass).getAnnotation(CustomField.class);
                                            Class rendererClass = customField.renderer();
                                            CustomFieldRenderer renderer = (CustomFieldRenderer) SpringBeanFactory.create(rendererClass);

                                            Optional<Component> f = renderer.createInTable(source, fc.getField().getName());

                                            columnPanel.add(f.map(c -> {
                                                if (AbstractField.class.isAssignableFrom(c.getClass())) {
                                                    AbstractField af = (AbstractField) c;
                                                    if (fc.getWebField().isReadOnly()) {
                                                        af.setReadOnly(true);
                                                    }
                                                }
                                                return c;
                                            }).orElseGet(() -> new Label()));

                                        }, () -> {

                                            if (fc.getValue(source) != null) {
                                                if (LocalDate.class.equals(fc.getReturnType())) {
                                                    Date date = Date.from(((LocalDate) fc.getValue(source)).atStartOfDay(ZoneId.systemDefault()).toInstant());
                                                    TextField tf = new TextField(fc.getWebField().displayName());
                                                    tf.setValue(sdf.format(date));
                                                    tf.setReadOnly(true);
                                                    columnPanel.add(tf);
                                                } else if (LocalDateTime.class.equals(fc.getReturnType())) {
                                                    Date date = Date.from(((LocalDateTime) fc.getValue(source)).atZone(ZoneId.systemDefault()).toInstant());
                                                    TextField tf = new TextField(fc.getWebField().displayName());
                                                    tf.setValue(sdtf.format(date));
                                                    tf.setReadOnly(true);
                                                    columnPanel.add(tf);
                                                } else if (Boolean.class.equals(fc.getReturnType())){
                                                    Choice choice = choiceBooleanConv.convertToPresentation((Boolean)fc.getValue(source),null);
                                                    TextField tf = new TextField(fc.getWebField().displayName());
                                                    tf.setValue(choiceBooleanConv.getStringRepresentation(choice));
                                                    tf.setReadOnly(true);
                                                    columnPanel.add(tf);
                                                } else { //Plain string
                                                    if (fc.getWebField().format().length > 0) {
                                                        Format format = fc.getWebField().format()[0];
                                                        TextField tf = new TextField(fc.getWebField().displayName());
                                                        tf.setValue(format.prefix() + fc.getValue(source).toString());
                                                        tf.setReadOnly(true);
                                                        columnPanel.add(tf);
                                                    } else {
                                                        TextField tf = new TextField(fc.getWebField().displayName());
                                                        tf.setValue(fc.getValue(source).toString());
                                                        tf.setReadOnly(true);
                                                        columnPanel.add(tf);
                                                    }
                                                }
                                            }

                                        });

                                    }

                                }
                            } else {
                                for (RelationContainer rc : relationStore.get(order)) {
                                    if (Boolean.TRUE.equals(calculateShowInTable(currentClass, rc))) {
                                        relationUtils.getInTableRenderer(rc).ifPresent(customRenderer -> {
                                            columnPanel.add((Component) customRenderer.render(source, rc.getRelationshipName()).orElse(new Label()));
                                        });
                                    }
                                }
                            }
                        }
                        return columnPanel;
                    }).setHeader("   Data   ").setAutoWidth(true).setVisible(false);
                    //Add column for status
                    grid.addComponentColumn((ValueProvider<T, Component>) source -> {
                        return (Component) Castor.<T, Element>given(source)
                                .castItTo(Element.class)
                                .thenDo(ext -> {
                                    Icon icon = null;
                                    if (Status.DRAFT.equals(ext.getStatus())) {
                                        icon = new Icon(VaadinIcon.EDIT);
                                    } else if (Status.IN_PROGRESS
                                            .equals(ext.getStatus())) {
                                        icon = (Icon) Castor.<T, WorkElement>given(source)
                                                .castItTo(WorkElement.class)
                                                .thenDo(ext2 -> {
                                                    if (ext2.getWorkflowInfo()
                                                            .getWorklist() == null
                                                            || workflow
                                                                    .isStartEvent(ext2.getWorkflowInfo().getWorklist())) {
                                                        return new Icon(VaadinIcon.EDIT);
                                                    } else if (workflow
                                                            .isActivitySLAExpired(
                                                                    ext2.getWorkflowInfo()
                                                                            .getWorklist(),
                                                                    ext2.getWorkflowInfo()
                                                                            .getWorklistUpdateTime())) {
                                                        return new Icon(VaadinIcon.ALARM);
                                                    } else {
                                                        return new Icon(VaadinIcon.ROTATE_RIGHT);
                                                    }
                                                }).go().orElse(new Icon(VaadinIcon.ROTATE_RIGHT));
                                    } else if (Status.RETIRED
                                            .equals(ext.getStatus())) {
                                        icon = new Icon(VaadinIcon.BAN);
                                    } else {
                                        icon = new Icon(VaadinIcon.CHECK_SQUARE_O);
                                    }
                                    icon.setSize("0.8em");
                                    Div div = new Div();
                                    if (ext.getId()!=null){
                                        div.add(icon);
                                    } //id null = non persistent filler generated by PojoDialogFactory::createDataProvider
                                    div.setTitle(ext.getStatus().toString());
                                    return div;

                                }).go().orElseGet(() -> {
                            return new Label("");
                        });
                    }).setFrozen(true).setAutoWidth(true);

                    //Add fields to grid
                    for (Integer order : orders) {
                        if (fieldStore.containsKey(order)) {
                            for (FieldContainer fc : fieldStore.get(order)) {

                                //Only display what is visible
                                if ((calculateShowInTable(currentClass, fc)).equals(Boolean.TRUE)) {

                                    //Add column
                                    Optional<Class> oCustomRendererClass = fc.getCustomComponent();
                                    oCustomRendererClass.ifPresentOrElse(customRendererClass -> {
                                        Grid.Column<?> column = grid.addComponentColumn(source -> {
                                            CustomField customField = (CustomField) ((Class) customRendererClass).getAnnotation(CustomField.class);
                                            Class rendererClass = customField.renderer();
                                            CustomFieldRenderer renderer = (CustomFieldRenderer) SpringBeanFactory.create(rendererClass);

                                            Optional<Component> f = renderer.createInTable(source, fc.getField().getName());

                                            return f.map(c -> {
                                                if (AbstractField.class.isAssignableFrom(c.getClass())) {
                                                    AbstractField af = (AbstractField) c;
                                                    if (fc.getWebField().isReadOnly()) {
                                                        af.setReadOnly(true);
                                                    }
                                                }
                                                return c;
                                            }).orElseGet(() -> new Label());

                                        }).setHeader(fc.getWebField().displayName())
                                                .setSortable(fc.isSortable())
                                                .setSortProperty(fc.getField().getName())
                                                .setAutoWidth(true)
                                                .setResizable(true);

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
                                                } else if (Boolean.class.equals(fc.getReturnType())){
                                                    Choice choice = choiceBooleanConv.convertToPresentation((Boolean)fc.getValue(source),null);
                                                    return choiceBooleanConv.getStringRepresentation(choice);
                                                }else { //Plain string
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

                                }

                            }
                        } else {
                            for (RelationContainer rc : relationStore.get(order)) {
                                if (Boolean.TRUE.equals(calculateShowInTable(currentClass, rc))) {
                                    relationUtils.getInTableRenderer(rc).ifPresent(customRenderer -> {
                                        Grid.Column<?> column = grid.addComponentColumn(source -> {

                                            return (Component) customRenderer.render(source, rc.getRelationshipName()).map(c -> c).orElse(new Label());

                                        }).setHeader(rc.getRelationshipDisplayName())
                                                .setSortable(true)
                                                .setSortProperty(rc.getField().getName())
                                                .setAutoWidth(true)
                                                .setResizable(true);

                                        columns.put(rc.getField().getName(), column);
                                    });
                                }
                            }
                        }
                    }
                });
        grid.addItemClickListener(listener);
    }

    private <T> Boolean calculateShowInTable(Class<T> tClass, FieldContainer fc) {
        return fieldUtils.getFieldVisibilityFromEntity(tClass, fc.getField().getName()).map(
                fv -> (Boolean) expr.evaluate(fv.visibleInTable(), tClass)
        ).orElseGet(
                () -> (Boolean) expr.evaluate(fc.getWebField().visibleInTable(), tClass)
        );
    }

    private <T> Boolean calculateShowInTable(Class<T> tClass, RelationContainer rc) {
        return fieldUtils.getFieldVisibilityFromEntity(tClass, rc.getField().getName()).map(
                fv -> (Boolean) expr.evaluate(fv.visibleInTable(), tClass)
        ).orElseGet(
                () -> (Boolean) expr.evaluate(rc.getWebRelation().visibleInTable(), tClass)
        );
    }

}
