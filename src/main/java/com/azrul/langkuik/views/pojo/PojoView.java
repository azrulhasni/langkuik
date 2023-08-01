/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.views.pojo;

import com.azrul.langkuik.framework.entity.Choice;
import com.azrul.langkuik.custom.CustomField;
import com.azrul.langkuik.framework.entity.WebEntity;
import com.azrul.langkuik.framework.dao.DataAccessObject;
import com.azrul.langkuik.framework.entity.EntityUtils;
import com.azrul.langkuik.framework.dao.params.FindRelationParameter;
import com.azrul.langkuik.framework.field.FieldUtils;
import com.azrul.langkuik.framework.relationship.RelationUtils;
import com.azrul.langkuik.views.common.MainView;
import com.azrul.langkuik.framework.field.FieldContainer;
import com.azrul.langkuik.framework.relationship.RelationContainer;
import com.azrul.langkuik.framework.relationship.RelationMemento;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;//1
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;//2
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.MultiValuedMap;
import java.util.Set;
import com.azrul.langkuik.custom.CustomFieldRenderer;
import com.azrul.langkuik.custom.subform.DefaultRelationSubFormComponent;
import com.azrul.langkuik.custom.EventToOpenOtherComponent;
import com.azrul.langkuik.framework.field.WebField;
import com.azrul.langkuik.framework.format.Format;
import com.azrul.langkuik.framework.format.FormatType;
import com.azrul.langkuik.framework.factory.SpringBeanFactory;
import com.azrul.langkuik.framework.expression.Expression;
import com.azrul.langkuik.framework.rule.FieldRule;
import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.framework.entity.Status;
import com.azrul.langkuik.framework.standard.converter.StringStatusConverter;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasEnabled;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.checkbox.Checkbox;//3
import com.vaadin.flow.component.combobox.ComboBox;//4
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.vaadin.textfieldformatter.CustomStringBlockFormatter;
import org.vaadin.textfieldformatter.CustomStringBlockFormatter.ForceCase;
import org.vaadin.textfieldformatter.CustomStringBlockFormatter.Options;
import com.azrul.langkuik.framework.standard.converter.SetStringConverter;
import com.azrul.langkuik.views.common.ConfirmationDialog;
import com.vaadin.flow.component.HasComponents;
import org.springframework.beans.factory.annotation.Qualifier;
import com.azrul.langkuik.framework.rule.PojoRule;
import com.azrul.langkuik.framework.standard.ErrorType;
import com.azrul.langkuik.framework.workflow.Workflow;
import com.azrul.langkuik.views.common.TabWithIndex;
import com.azrul.langkuik.framework.standard.converter.CustomStringToLongConverter;
import com.azrul.langkuik.framework.standard.converter.IdStringConverter;
import com.azrul.langkuik.framework.findusage.view.FindUsageView;
import com.vaadin.flow.component.html.H5;
import com.azrul.langkuik.custom.subform.SubFormComponent;
import com.azrul.langkuik.custom.subform.VoidSubFormRenderer;
import com.azrul.langkuik.framework.dao.filter.AndFilters;
import com.azrul.langkuik.framework.dao.filter.FilterRelation;
import com.azrul.langkuik.framework.dao.filter.QueryFilter;
import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.standard.Dual;
import com.azrul.langkuik.framework.relationship.WebRelation;
import com.azrul.langkuik.framework.standard.Castor;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import java.math.BigDecimal;
import com.azrul.langkuik.framework.rule.AddDeleteRelationRule;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.shared.Registration;
import java.time.LocalDateTime;
import com.azrul.langkuik.custom.subform.SubFormRenderer;
import com.azrul.langkuik.framework.standard.converter.ChoiceBooleanConverter;
import java.util.EnumSet;

/**
 *
 * @author azrul
 */
//@Component
@Route(value = "PojoView", layout = MainView.class)
public class PojoView<R extends WorkElement, T extends Element>
        extends VerticalLayout
        implements AfterNavigationObserver, BeforeLeaveObserver {

    @Value("${application.lgDateFormat:yyyy-MM-dd}")
    private String dateFormat;

    @Value("${application.lgDateTimeFormat:yyyy-MM-dd HH:mm:ss}")
    private String dateTimeFormat;

    @Value("${application.lgFullurl}")
    private String fullUrl;

    @Autowired
    private DataAccessObject dao;

    @Autowired
    private FieldUtils fieldUtils;

    @Autowired
    private RelationUtils relationUtils;

    @Autowired
    private EntityUtils entityUtils;

    @Autowired
    private ValidatorFactory validatorFactory;

    @Autowired
    private PojoTableFactory pojoTableFactory;

    @Autowired
    private PojoWindowFactory<T> pojoDialogFactory;

    @Autowired
    private Workflow<WorkElement> workflow;

    @Autowired
    private Expression expr;

    @Autowired
    @Qualifier("CanEditEntityRule")
    private PojoRule canEditEntityRule;

    @Autowired
    @Qualifier("CanDeleteOrElseUnlinkEntityInRelationshipRule")
    private PojoRule canDeleteOrElseUnlinkEntityInRelationshipRule;

    @Autowired
    @Qualifier("CanBookRule")
    private PojoRule canBookRule;

    @Autowired
    @Qualifier("CanBookRule")
    private PojoRule canUnBookRule;

    @Autowired
    @Qualifier("CanEditFieldRule")
    private FieldRule canEditFieldRule;

    @Autowired
    @Qualifier("IsApprovalNeeded")
    private PojoRule isApprovalNeeded;

    @Autowired
    @Qualifier("IsSupervisorApprovalNeeded")
    private PojoRule isSupervisorApprovalNeeded;

    @Autowired
    @Qualifier("IsReleasableByRoleManager")
    private PojoRule isReleasableByRoleManager;

    @Autowired
    @Qualifier("CanAddDeleteRelationsRule")
    private AddDeleteRelationRule canAddDeleteRelationsRule;
    
    @Autowired
    @Qualifier("CanApproveRule")
    private PojoRule canApproveRule;

    private Binder<T> binder;
    private R root;
    private final Map<String, RelationMemento> relationMementos;
    private Class<T> currentClass;
    private ReasonToClose reasonToClose;
    private final List<Dual<String, Component>> editableComponents;
    private final Map<Integer, RendererMemento> subFormRenderers;
    private final Map<Integer, SubFormComponent> subFormComponents;

    private MenuBar headerBar;
    private MenuItem miCloseAsIs;
    private MenuItem miSaveSubmit;
    private MenuItem miBook;
    private MenuItem miUnBook;
    private MenuItem miFindUsage;
    private MenuItem miDone;
    private Tabs tabs;
    private Registration listener;
    private int tabletsBreakPoint = 769;
    private final int phoneBreakPoint = 480;

    private PojoViewState state;
    private PojoViewState parentState;

    private final Notification notifSaveAsDraft = new Notification(
            "Saving current data", 3000, Notification.Position.TOP_START);

    private final Notification notifInvalidForm = new Notification(
            "Certain fields are not valid. Please check",
            3000, Notification.Position.TOP_START);

    private final Notification notifCheckMinRequirements = new Notification(
            "Certain data need minimal entry. "
            + "Please check highlighted fields",
            3000, Notification.Position.TOP_START);

    private final Notification notifCannotDelete = new Notification(
            "The item cannot be deleted",
            3000, Notification.Position.TOP_START);

    public PojoView() {
        this.relationMementos = new HashMap<>();
        this.editableComponents = new ArrayList<>();
        this.subFormComponents = new HashMap<>();
        this.subFormRenderers = new HashMap<>();
        this.reasonToClose = ReasonToClose.CLOSE_AS_IS;
    }

    public void construct(T bean, Optional<Dialog> oDialog) {
        construct(Optional.empty(),
                bean,
                oDialog,
                PojoViewState.NONE,
                Boolean.FALSE);
    }
    
    public void construct(
            Optional<R> aroot,
            T bean,
            Optional<Dialog> oDialog,
            PojoViewState parentState,
            Boolean embedded) {
          oDialog.ifPresent(dialog -> {
            dialog.setWidth("100%"); //width of a dialog (main)
            dialog.setResizable(true);
            dialog.setId("pojo-view-dialog-" + bean.getClass().getSimpleName());
        });
        construct(aroot,
                bean,
                parentState,
                embedded,
                ()->oDialog.ifPresent(dialog -> dialog.close()));
    }
    
     public void construct(
            T bean,
            Runnable doneListener) {
         construct(Optional.empty(),
                bean,
                PojoViewState.NONE,
                Boolean.FALSE,
                doneListener);
     }
    
   

    private void construct(
            Optional<R> aroot,
            T bean,
            PojoViewState parentState,
            Boolean embedded,
            Runnable doneListener) {

       

        if (bean == null) {
            return;
        }
        if (parentState != null) {
            this.parentState = parentState;
        } else {
            this.parentState = PojoViewState.NONE;
        }

        this.root = aroot.map(r -> {
            Castor.<T, WorkElement>given(bean)
                    .castItTo(WorkElement.class)
                    .thenDo(e -> {
                        e.getWorkflowInfo()
                                .setWorklist(r.getWorkflowInfo()
                                        .getWorklist());

                    }).go();
            return r;
        }).orElseGet(() -> {
            if (bean.isReference()) {
                return null;
            } else {
                return (R) bean;
            }
        });

        //read input
        Class<T> tclass = (Class<T>) bean.getClass();
        this.binder = new Binder<>(tclass);
        this.setCurrentBean((T) bean);
        this.currentClass = (Class<T>) bean.getClass();
        Optional<WebEntity> oCurrentWebEntity
                = Optional.ofNullable((WebEntity) currentClass.getAnnotation(WebEntity.class));

        //declare components
        headerBar = new MenuBar();
        headerBar.setId("headerBar-" + bean.getClass().getSimpleName());
        headerBar.setWidthFull();
        VerticalLayout headerAndFormLayout = new VerticalLayout();
        Div mainForm = new Div();

        //compute state to be used in construction
        computeState();

        Validator validator = validatorFactory.getValidator();
        MultiValuedMap<Integer, FieldContainer> fieldStore = fieldUtils
                .getFieldsByOrder(bean);

        DatePicker.DatePickerI18n dateI18NFormat
                = new DatePicker.DatePickerI18n();
        dateI18NFormat.setDateFormat(dateFormat);

        DatePicker.DatePickerI18n dateTimeI18NFormat
                = new DatePicker.DatePickerI18n();
        dateI18NFormat.setDateFormat(dateTimeFormat);

        Map<String, AbstractField> vaadinFieldStore = new HashMap<>();
        //Relations to be displayed on main tab
        List<Component> relationsOnMainTab = new ArrayList<>();
        FormLayout mainTab = new FormLayout();
        Map<Integer, String> tabStore
                = new HashMap<>();
        Map<Integer, String> popupStore
                = new HashMap<>();
        Map<Integer, EventToOpenOtherComponent> eventToOpenOtherPopups
                = new HashMap<>();

        //-------------deal with fields-------------------------------------
        //sort fields by order
        List<Integer> orderOfFields = new ArrayList<>(fieldStore.keySet());
        Collections.sort(orderOfFields);
        //Add fields to grid
        for (Integer order : orderOfFields) {
            Collection<FieldContainer> fcs = fieldStore.get(order);
            for (FieldContainer fc : fcs) {
                if (fc.getCustomComponent().isPresent()) {
                    continue;
                }

                //Only display what is visible
                AbstractField field = null;

                if (calculateVisibilityForField(oCurrentWebEntity, fc.getWebField(), bean, fc.getField().getName()).equals(Boolean.TRUE)) {
                    if (LocalDate.class.equals(fc.getReturnType())) {
                        DatePicker datePicker = new DatePicker(fc.getWebField().displayName());
                        datePicker.setAutoOpen(false);
                        datePicker.setI18n(dateI18NFormat);

                        field = datePicker;
                        binder.forField(field)
                                .withValidator(new com.vaadin.flow.data.binder.Validator<LocalDate>() {
                                    @Override
                                    public ValidationResult apply(LocalDate value, ValueContext context) {
                                        String errorMsg = validator.validateProperty(getDraftCurrentBean(), fc.getField().getName())
                                                .stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", "));
                                        if (errorMsg.isEmpty()) {
                                            return getCurrentBean().customValidation(fc.getField().getName(), workflow, value).map(err -> {
                                                return ValidationResult.error(err);
                                            }).orElse(ValidationResult.ok());
                                        } else {
                                            return ValidationResult.error(errorMsg);
                                        }
                                    }
                                })
                                .bind(fc.getField().getName());

                    }
                    if (LocalDate.class.equals(fc.getReturnType())) {
                        DatePicker datePicker = new DatePicker(fc.getWebField().displayName());
                        datePicker.setAutoOpen(false);
                        datePicker.setI18n(dateI18NFormat);

                        field = datePicker;
                        binder.forField(field)
                                .withValidator(new com.vaadin.flow.data.binder.Validator<LocalDate>() {
                                    @Override
                                    public ValidationResult apply(LocalDate value, ValueContext context) {
                                        String errorMsg = validator.validateProperty(getDraftCurrentBean(), fc.getField().getName())
                                                .stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", "));
                                        if (errorMsg.isEmpty()) {
                                            return getCurrentBean().customValidation(fc.getField().getName(), workflow, value).map(err -> {
                                                return ValidationResult.error(err);
                                            }).orElse(ValidationResult.ok());
                                        } else {
                                            return ValidationResult.error(errorMsg);
                                        }
                                    }
                                })
                                .bind(fc.getField().getName());

                    } else if (LocalDateTime.class.equals(fc.getReturnType())) {
                        DateTimePicker datePicker = new DateTimePicker(fc.getWebField().displayName());
                        datePicker.setDatePickerI18n(dateTimeI18NFormat);
                        datePicker.setAutoOpen(false);
                        field = datePicker;
                        binder.forField(field)
                                .withValidator(new com.vaadin.flow.data.binder.Validator<LocalDateTime>() {

                                    @Override
                                    public ValidationResult apply(LocalDateTime value, ValueContext context) {
                                        String errorMsg = validator.validateProperty(getDraftCurrentBean(), fc.getField().getName())
                                                .stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", "));
                                        if (errorMsg.isEmpty()) {
                                            return getCurrentBean().customValidation(fc.getField().getName(), workflow, value).map(err -> {
                                                return ValidationResult.error(err);
                                            }).orElse(ValidationResult.ok());
                                        } else {
                                            return ValidationResult.error(errorMsg);
                                        }
                                    }
                                })
                                .bind(fc.getField().getName());
                    } else if (Long.class.equals(fc.getReturnType())) {
                        field = new TextField(fc.getWebField().displayName());

                        binder.forField(field)
                                .withConverter(getStringToLongConverter("Please enter a number", fc))
                                .withValidator(new com.vaadin.flow.data.binder.Validator<Long>() {

                                    @Override
                                    public ValidationResult apply(Long value, ValueContext context) {
                                        String errorMsg = validator.validateProperty(getDraftCurrentBean(), fc.getField().getName())
                                                .stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", "));
                                        if (errorMsg.isEmpty()) {
                                            return getCurrentBean().customValidation(fc.getField().getName(), workflow, value).map(err -> {
                                                return ValidationResult.error(err);
                                            }).orElse(ValidationResult.ok());
                                        } else {
                                            return ValidationResult.error(errorMsg);
                                        }
                                    }
                                })
                                .withNullRepresentation(0l)
                                .bind(fc.getField().getName());
                    } else if (Integer.class.equals(fc.getReturnType())) {
                        field = new TextField(fc.getWebField().displayName());

                        binder.forField(field)
                                .withConverter(new StringToIntegerConverter("Please enter a number"))
                                .withValidator(new com.vaadin.flow.data.binder.Validator<Integer>() {

                                    @Override
                                    public ValidationResult apply(Integer value, ValueContext context) {
                                        String errorMsg = validator.validateProperty(getDraftCurrentBean(), fc.getField().getName())
                                                .stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", "));
                                        if (errorMsg.isEmpty()) {
                                            return getCurrentBean().customValidation(fc.getField().getName(), workflow, value).map(err -> {
                                                return ValidationResult.error(err);
                                            }).orElse(ValidationResult.ok());
                                        } else {
                                            return ValidationResult.error(errorMsg);
                                        }
                                    }
                                })
                                .withNullRepresentation(0)
                                .bind(fc.getField().getName());
                    } else if (BigDecimal.class.equals(fc.getReturnType())) {
                        field = new TextField(fc.getWebField().displayName());
                        binder.forField(field)
                                .withConverter(new StringToBigDecimalConverter("Please enter a number"))
                                .withValidator(new com.vaadin.flow.data.binder.Validator<BigDecimal>() {

                                    @Override
                                    public ValidationResult apply(BigDecimal value, ValueContext context) {
                                        String errorMsg = validator.validateProperty(getDraftCurrentBean(), fc.getField().getName())
                                                .stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", "));
                                        if (errorMsg.isEmpty()) {
                                            return getCurrentBean().customValidation(fc.getField().getName(), workflow, value).map(err -> {
                                                return ValidationResult.error(err);
                                            }).orElse(ValidationResult.ok());
                                        } else {
                                            return ValidationResult.error(errorMsg);
                                        }
                                    }
                                })
                                .withNullRepresentation(0)
                                .bind(fc.getField().getName());
                    } else if (Boolean.class.equals(fc.getReturnType())) {
                        //field = new Checkbox(fc.getWebField().displayName());
                        ChoiceBooleanConverter conv = new ChoiceBooleanConverter();
                        ComboBox c = new ComboBox(fc.getWebField().displayName());
                        c.setItems(EnumSet.allOf(Choice.class));
                        
                        c.setItemLabelGenerator(e->conv.getStringRepresentation((Choice)e));
                        field = c;
                        
                        binder.forField(field)
                                .withConverter(conv)
                                .withValidator(new com.vaadin.flow.data.binder.Validator<Boolean>() {

                                    @Override
                                    public ValidationResult apply(Boolean value, ValueContext context) {
                                        String errorMsg = validator.validateProperty(getDraftCurrentBean(), fc.getField().getName())
                                                .stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", "));
                                        if (errorMsg.isEmpty()) {
                                            return getCurrentBean().customValidation(fc.getField().getName(), workflow, value).map(err -> {
                                                return ValidationResult.error(err);
                                            }).orElse(ValidationResult.ok());
                                        } else {
                                            return ValidationResult.error(errorMsg);
                                        }
                                    }
                                })
                                .bind(fc.getField().getName());
                    } else if (Status.class.equals(fc.getReturnType())) {
                        field = new TextField(fc.getWebField().displayName());

                        binder.forField(field)
                                .withConverter(new StringStatusConverter())
                                .withValidator(new com.vaadin.flow.data.binder.Validator<Status>() {
                                    @Override
                                    public ValidationResult apply(Status value, ValueContext context) {
                                        String errorMsg = validator.validateProperty(getDraftCurrentBean(), fc.getField().getName())
                                                .stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", "));
                                        if (errorMsg.isEmpty()) {
                                            return getCurrentBean().customValidation(fc.getField().getName(), workflow, value).map(err -> {
                                                return ValidationResult.error(err);
                                            }).orElse(ValidationResult.ok());
                                        } else {
                                            return ValidationResult.error(errorMsg);
                                        }
                                    }
                                })
                                .withNullRepresentation("")
                                .bind(fc.getField().getName());
                    } else if (Set.class.equals(fc.getReturnType())) {
                        field = new TextField(fc.getWebField().displayName());
                        field.setReadOnly(true);
                        binder.forField(field)
                                .withConverter(new SetStringConverter())
                                .withNullRepresentation("")
                                .bind(fc.getField().getName());
                    } else { //must be String
                        field = new TextField(fc.getWebField().displayName());
                        //see if formatting is there
                        if (fc.getWebField().format().length > 0) {
                            Format format = fc.getWebField().format()[0];
                            Options options = new Options();

                            options.setBlocks(format.blocks());
                            options.setNumericOnly(format.type() == FormatType.NUMERIC);
                            options.setDelimiters(format.delimiters());
                            if (format.type() == FormatType.UPPER_CASE) {
                                options.setForceCase(ForceCase.UPPER);
                            }
                            CustomStringBlockFormatter formatter = new CustomStringBlockFormatter(options);
                            formatter.extend((TextField) field);
                            ((TextField) field).setPrefixComponent(new Span(format.prefix()));
                        }
                        //bin
                        binder.forField(field)
                                .withValidator(new com.vaadin.flow.data.binder.Validator<String>() {

                                    @Override
                                    public ValidationResult apply(String value, ValueContext context) {
                                        String errorMsg = validator.validateProperty(getDraftCurrentBean(), fc.getField().getName())
                                                .stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", "));
                                        if (errorMsg.isEmpty()) {
                                            return getCurrentBean().customValidation(fc.getField().getName(), workflow, value).map(err -> {
                                                return ValidationResult.error(err);
                                            }).orElse(ValidationResult.ok());
                                        } else {
                                            return ValidationResult.error(errorMsg);
                                        }
                                    }
                                })
                                .withNullRepresentation("")
                                .bind(fc.getField().getName());
                    }
                }
                if (field != null) {
                    //for testing purpose
                    if (embedded == Boolean.FALSE) {
                        field.setId(fc.getField().getName());
                    } else {
                        field.setId(getCurrentClass().getSimpleName() + "-" + fc.getField().getName());
                    }

                    //set if something readonly, if not track it as editableComponent to be enabled/disabled as needed
                    if (fc.isId() || fc.getWebField().isReadOnly()) {
                        field.setReadOnly(true);
                    } else {
                        editableComponents.add(new Dual(fc.getField().getName(), field));
                    }

                    vaadinFieldStore.put(fc.getField().getName(), field);

                }
            }
        }

        //---------------buttons--------------------------------------------
        Span errorMessage = new Span();
        //HorizontalLayout headerLayout = new HorizontalLayout();

        if (getCurrentBean().isRoot() || getCurrentBean().isReference()) {
            miCloseAsIs = headerBar.addItem("Close as is", e -> {
                setReasonToClose(ReasonToClose.CLOSE_AS_IS);
                callCustomComponentBeforeSaveCallback();
                //oDialog.ifPresent(dialog -> dialog.close());
                doneListener.run();
            });
            miCloseAsIs.setId("btnCloseAsIs-" + getCurrentClass().getSimpleName());
            //headerLayout.add(btnCloseAsIs);
            Castor.<T, WorkElement>given(getCurrentBean())
                    .castItTo(WorkElement.class)
                    .thenDo(ext -> {
                        miSaveSubmit = headerBar.addItem("Save and submit", e -> {
                            if (canEditEntityRule.compute(Optional.ofNullable(root))
                                    || isSupervisorApprovalNeeded.compute(Optional.ofNullable(root))) {
                                
                                ReasonToClose reason = validate();
                                if (ReasonToClose.SUBMITTED.equals(reason)) {
                                    setReasonToClose(reason);
                                    callCustomComponentBeforeSubmitCallback();
                                    //oDialog.ifPresent(dialog -> dialog.close());
                                    doneListener.run();
                                } else {
                                    //ignore error as the validate() method already warn users
                                }
                            } else {
                                setReasonToClose(ReasonToClose.CLOSE_AS_IS);
                                //oDialog.ifPresent(dialog -> dialog.close());
                                doneListener.run();
                            }
                        });
                        miSaveSubmit.setId("btnSaveSubmit-" + getCurrentClass().getSimpleName());
                        //only show these to entities under modification

                        miBook = headerBar.addItem("Book this work", e -> {
                            if (canBookRule.compute(Optional.of(getCurrentBean()))) {

                                String userIdentifier = (String) VaadinSession.getCurrent().getSession().getAttribute("USER_IDENTIFIER");

                                ext.getWorkflowInfo().getOwners().add(userIdentifier);

                                notifSaveAsDraft.open();
                                dao.save(getCurrentBean());
                                computeAndImplementState();
                                initializeTabsOrPopups(mainTab,
                                        popupStore,
                                        tabStore,
                                        mainForm,
                                        headerBar,
                                        headerAndFormLayout,
                                        eventToOpenOtherPopups);
                            }

                        });
                        miBook.setId("btnBook-" + getCurrentClass().getSimpleName());
                        //headerLayout.add(btnBook);
                        miUnBook = headerBar.addItem("Release", e -> {

                            if (canUnBookRule.compute(Optional.of(getCurrentBean()))
                                    || isReleasableByRoleManager.compute(Optional.of(getCurrentBean()))) {
                                setReasonToClose(ReasonToClose.RELEASE);
                                //oDialog.ifPresent(dialog -> dialog.close());
                                doneListener.run();
                            }

                        });

                        miUnBook.setId("btnUnBook-" + getCurrentClass().getSimpleName());
                        //headerLayout.add(btnUnBook);
                    }).go();

            miFindUsage = headerBar.addItem("Find Usage", e -> {
                FindUsageView findUsageView = SpringBeanFactory.create(FindUsageView.class);

                Dialog dialog2 = new Dialog();
                findUsageView.construct(getCurrentBean(), dialog2);
                dialog2.add(findUsageView);
                dialog2.setModal(true);
                dialog2.setCloseOnEsc(false);
                dialog2.setCloseOnOutsideClick(false);
                dialog2.open();
            });
            miFindUsage.setId("btnFindUsage-" + getCurrentClass().getSimpleName());
            //headerLayout.add(btnFindUsage);

        } else {
            if (!Boolean.TRUE.equals(embedded)) {
                miDone = headerBar.addItem("Done", e -> {
                    ReasonToClose reason = validate();
                    if (reason == ReasonToClose.SUBMITTED) {
                        setReasonToClose(reason);
                        //oDialog.ifPresent(dialog -> dialog.close());
                        doneListener.run();
                    } else {
                        notifInvalidForm.open();
                    }
                });
                miDone.setId("btnDone-" + getCurrentClass().getSimpleName());
            }
            //headerLayout.add(btnDone);
        }

        MultiValuedMap<Integer, RelationContainer> relationStore = relationUtils.getRelationsByOrder(getCurrentClass());

        //sort fields by order
        List<Integer> orderOfRelations = new ArrayList<>(relationStore.keySet());
        Collections.sort(orderOfRelations);

        //---------------relationship--------------------------------------------
        tabStore.put(0, "Main");
        //Add fields to table
        for (Integer order : orderOfRelations) {
            Collection<RelationContainer> rcs = relationStore.asMap().get(order);

            for (RelationContainer rc : rcs) {
                if (calculateVisibilityForRelation(oCurrentWebEntity, rc.getWebRelation(), bean, rc.getField().getName()).equals(Boolean.TRUE)) {
                    //only CANCELLED items are filtered out. 
                    //RETIRED items that was already 
                    //assigned previously should be shown
                    AndFilters andFilters = AndFilters.build(
                            QueryFilter.build("status", FilterRelation.NOT_EQUAL, Status.CANCELLED)
                    );
                    RelationMemento relationMemento = relationMementos.getOrDefault(
                            rc.getRelationshipName(),
                            pojoTableFactory.createRelationMemento(
                                    Optional.empty(),
                                    getCurrentBean(),
                                    rc.getRelationshipName(),
                                    Optional.of(andFilters),
                                    rc.getWebRelation().minCount() > 0
                                    ? Optional.of(rc.getWebRelation().minCount())
                                    : Optional.empty(),
                                    rc.getWebRelation().maxCount() > 0
                                    ? Optional.of(rc.getWebRelation().maxCount())
                                    : (rc.isOneToOne()
                                    ? Optional.of(1)
                                    : Optional.empty()),
                                    rc.getRelationshipDisplayName()));
                    relationMementos.put(rc.getRelationshipName(), relationMemento);

                    //group by tab
                    int s = 0; //not in tab (subform inactive)
                    //render inline and immediate as a table
                    Boolean subFormIsActive = (Boolean) expr.evaluate(rc.getWebRelation().asSubForm().active(), bean);
                    if (subFormIsActive == true) {
                        switch (rc.getWebRelation().asSubForm().type()) {
                            case AS_TAB:
                                if (rc.getWebRelation().asSubForm().subFormRenderer()
                                        == VoidSubFormRenderer.class) {
                                    s = 1; //render in tab as table immediately (put in formStore)
                                } else {
                                    s = 2; //in tab and render lazily
                                }
                                break;
                            case AS_POPUP:
                                if (rc.getWebRelation().asSubForm().subFormRenderer()
                                        == VoidSubFormRenderer.class) {
                                    s = 3; //render in as popup as table immediately (put in formStore)
                                } else {
                                    s = 4; //as popup and render lazily
                                }
                                break;
                            default:
                                //in line
                                if (rc.getWebRelation().asSubForm().subFormRenderer()
                                        == VoidSubFormRenderer.class) {
                                    s = 0; //render inline and immediate as a table
                                    //s=0 because this is no different than having subform.active()=false
                                } else {
                                    s = 5; //render inline and immediate using renderer
                                }
                                break;
                        }
                    }

                    if (s == 0) {//render inline and immediate as a table
                        var layout = createRelationTable(relationMemento, rc);
                        relationsOnMainTab.add(layout);
                        //formStore.put(rc.getWebRelation().asSubForm().index(), layout);
                    } else if (s == 1) {//render in tab as table immediately (put in formStore)
                        var layout = createRelationTable(relationMemento, rc);
                        FormLayout form = new FormLayout();
                        form.add(layout);
                        subFormComponents.put(rc.getWebRelation().order(), layout);
                        //formStore.put(rc.getWebRelation().asSubForm().index(), form);
                        tabStore.put(rc.getWebRelation().order(),
                                rc.getWebRelation().name());
                    } else if (s == 3) {//render in as popup as table immediately (put in formStore)
                        var layout = createRelationTable(relationMemento, rc);
                        FormLayout form = new FormLayout();
                        form.add(layout);
                        subFormComponents.put(rc.getWebRelation().order(), layout);
                        //formStore.put(rc.getWebRelation().asSubForm().index(), form);
                        popupStore.put(rc.getWebRelation().order(),
                                rc.getWebRelation().name());
                    } else if (s == 2) { //in tab and render lazily
                        SubFormRenderer<T, R, SubFormComponent> renderer = SpringBeanFactory.create(rc.getWebRelation().asSubForm().subFormRenderer());
                        RendererMemento capture = new RendererMemento(
                                renderer,
                                rc,
                                eventToOpenOtherPopups);
                        tabStore.put(rc.getWebRelation().order(),
                                rc.getWebRelation().name());
                        subFormRenderers.put(
                                rc.getWebRelation().order(), capture);
                    } else if (s == 4) { //as popup and render lazily
                        SubFormRenderer<T, R, SubFormComponent> renderer = SpringBeanFactory.create(rc.getWebRelation().asSubForm().subFormRenderer());
                        RendererMemento capture = new RendererMemento(
                                renderer,
                                rc,
                                eventToOpenOtherPopups);
                        popupStore.put(rc.getWebRelation().order(),
                                rc.getWebRelation().name());
                        subFormRenderers.put(
                                rc.getWebRelation().order(), capture);
                    } else { //render inline and immediate using renderer
                        SubFormRenderer<T, R, SubFormComponent> renderer = SpringBeanFactory.create(rc.getWebRelation().asSubForm().subFormRenderer());
                        RendererMemento capture = new RendererMemento(
                                renderer,
                                rc,
                                eventToOpenOtherPopups);
                        
                        renderSubForm(capture)
                                .ifPresent(layout -> {
                                    subFormComponents.put(rc.getWebRelation().order(),
                                            layout);
                                    relationsOnMainTab.add(layout);
                                });
                    }
                }

            }
        }

        headerAndFormLayout.add(errorMessage);
        headerAndFormLayout.setMargin(true);
        headerAndFormLayout.setPadding(false);
        oCurrentWebEntity.ifPresent(currentWebEntity
                -> headerAndFormLayout.add(new H5(currentWebEntity.name())));
        headerAndFormLayout.add(headerBar);

        //---------------build main form (main tab)--------------------------------------------
        //this form is going to be redisplayed when we switch from one tab to another
        for (Integer order : orderOfFields) {
            for (FieldContainer fc : fieldStore.get(order)) {
                if (calculateVisibilityForField(oCurrentWebEntity, fc.getWebField(), bean, fc.getField().getName()).equals(Boolean.TRUE)) {

                    //build form
                    Optional<Class> customRendererClass = fc.getCustomComponent();
                    customRendererClass.ifPresentOrElse(customCompClass -> {
                        CustomField customField = (CustomField) ((Class) customCompClass).getAnnotation(CustomField.class);
                        Class rendererClass = customField.renderer();
                        CustomFieldRenderer renderer = (CustomFieldRenderer) SpringBeanFactory.create(rendererClass);

                        Optional<Component> f = renderer.createInForm(getCurrentBean(), fc.getField().getName(), fc.getWebField().displayName(), vaadinFieldStore);

                        f.ifPresent(af -> af.setId(fc.getField().getName()));

                        //render or keep
                        f.ifPresent(c -> {
                            if (AbstractField.class.isAssignableFrom(c.getClass())) {
                                AbstractField af = (AbstractField) c;
                                if (fc.isId() || fc.getWebField().isReadOnly()) {
                                    af.setReadOnly(true);
                                } else {
                                    binder.forField(af).bind(fc.getField().getName());//when userfield renderer is not working, this might be the culprit
                                    vaadinFieldStore.put(fc.getField().getName(), af);
                                    editableComponents.add(new Dual(fc.getField().getName(), af));
                                }

                                mainTab.add(af);
                            } else {
                                mainTab.add(c);
                            }
                        });
                    }, () -> {
                        if (vaadinFieldStore.containsKey(fc.getField().getName())) {
                            mainTab.add(vaadinFieldStore.get(fc.getField().getName()));
                        }
                    });
                }
            }
        }

        for (Component relLayout : relationsOnMainTab) {

            mainTab.add(relLayout);
            mainTab.setColspan(relLayout, 2);
        }

        //--------------tabs--------------------------------------------
        initializeTabsOrPopups(mainTab,
                popupStore,
                tabStore,
                mainForm,
                headerBar,
                headerAndFormLayout,
                eventToOpenOtherPopups);

        //-------------put everything together--------------------------------------------
        headerAndFormLayout.add(mainForm);

        //headerAndFormLayout.setWidth("50%");
        headerAndFormLayout.setId("header-and-form");
        if (embedded == Boolean.FALSE) {
            headerAndFormLayout.setWidth("70%");
            headerAndFormLayout.getStyle().set("align-self", "center");
        } else {
            headerAndFormLayout.setWidth("100%");
            headerAndFormLayout.getStyle().set("align-self", "flex-start");
        }
        //headerAndFormLayout.getStyle().set("align-self", "center");
        this.add(headerAndFormLayout);

        this.setId("pojo-view-" + bean.getClass().getSimpleName());
        this.setWidth("auto");

        this.setPadding(true);
        computeAndImplementState();
    }

    private DefaultRelationSubFormComponent createRelationTable(RelationMemento relationMemento, RelationContainer rc) {
        //Build table
        HorizontalLayout buttonLayout = new HorizontalLayout();
        DefaultRelationSubFormComponent layout = new DefaultRelationSubFormComponent();
        layout.setMemento(relationMemento);
        pojoTableFactory.createTable(layout,
                relationMemento,
                Optional.of(buttonLayout),
                e -> { //create a PojoView containing the clicked item on the table
                    if (e.getClickCount() == 2) {
                        Element ext = (Element) e.getItem();
                        if (ext.getId() != null) {

                            //if (validateForm().isEmpty()) {
                            dao.save(getCurrentBean());
                            pojoDialogFactory.createChildPojoDialog(
                                    root,
                                    getCurrentBean(),
                                    rc.getRelationshipName(),
                                    (Element) e.getItem(),
                                    state,
                                    relationMemento,
                                    this::setCurrentBean,
                                    rm -> {
                                        rm.incrementCounter();
                                        if (rm.reachedMax()) {
                                            ((HasEnabled) rm.getBtnAddLinkNew()).setEnabled(false);
                                            rm.getErrorMessage().setText("Maximum has been reached");
                                        }
                                    }
                            );

                        }
                    }
                },
                e -> {
                    notifSaveAsDraft.open();
                    dao.save(getCurrentBean());
                    var childBean = createNew(rc.getChildType(), getCurrentBean());
                    //save the current objects (as draft) before going to another page
                    pojoDialogFactory.createChildPojoDialog(
                            root,
                            getCurrentBean(),
                            rc.getRelationshipName(),
                            (Element) childBean,
                            state,
                            relationMemento,
                            this::setCurrentBean,
                            rm -> {
                                rm.incrementCounter();
                                if (rm.reachedMax()) {
                                    ((HasEnabled) rm.getBtnAddLinkNew()).setEnabled(false);
                                    rm.getErrorMessage().setText("Maximum has been reached");
                                    //((H6) rm.getTitle()).getStyle().set("background", "hsla(3, 100%, 89%, 1)");
                                }
                            }
                    );
                },
                e -> {
                    notifSaveAsDraft.open();
                    dao.save(getCurrentBean());
                    pojoTableFactory.createTableDialogForSelection(
                            root,
                            getCurrentBean(),
                            rc.getRelationshipName(),
                            relationMemento,
                            this::setCurrentBean,
                            rm -> {
                                rm.incrementCounter();
                                if (rm.reachedMax()) {
                                    ((HasEnabled) rm.getBtnAddLinkNew()).setEnabled(false);
                                    rm.getErrorMessage().setText("Maximum has been reached");
                                    //((H6) rm.getTitle()).getStyle().set("background", "hsla(3, 100%, 89%, 1)");
                                }
                            }
                    );
                },
                e -> {
                    ConfirmationDialog confirmDeleteDialog = new ConfirmationDialog("Confirm unlinking/deletion",
                            "Are you sure you want to unlink/delete this item?",
                            "Delete/Unlink",
                            (ce) -> {
                                Grid grid = relationMemento.getGrid();
                                Set itemsToBeDeleted = grid.getSelectedItems();
                                FindRelationParameter param = new FindRelationParameter(getCurrentBean(),
                                        rc.getRelationshipName());
                                List<String> cannotBeDeleted = new ArrayList<>();

                                //see if we can delete the actual object, if not just unlink
                                if (canEditEntityRule.compute(Optional.ofNullable(root))
                                && canAddDeleteRelationsRule.compute(Optional.ofNullable(root), Optional.of(getCurrentBean()), rc.getRelationshipName(), Optional.of(rc.getChildType()), itemsToBeDeleted)) {
                                    Boolean canDelete = itemsToBeDeleted
                                            .stream()
                                            .anyMatch(item -> canDeleteOrElseUnlinkEntityInRelationshipRule
                                            .compute(Optional.of((Element) item))
                                            );

                                    if (canDelete == true) {
                                        dao.unlinkAndDelete(param, itemsToBeDeleted,
                                                (p, c) -> {
                                                    relationMemento.decrementCounter();
                                                    if (relationMemento.reachedMax() == false) {
                                                        if (relationMemento.getBtnAddLinkNew() != null) {
                                                            relationMemento.getErrorMessage().setText("");
                                                            ((HasEnabled) relationMemento.getBtnAddLinkNew()).setEnabled(true);
                                                        }
                                                    }
                                                }).ifPresent(p -> {
                                                    setCurrentBean((T) p);
                                                }); //need to reassign parent (setCurrentBean), if not, there will be lingering deleted item
                                    } else {
                                        dao.unlink(param, itemsToBeDeleted,
                                                (p, c) -> {
                                                    relationMemento.decrementCounter();
                                                    if (relationMemento.reachedMax() == false) {
                                                        if (relationMemento.getBtnAddLinkNew() != null) {
                                                            relationMemento.getErrorMessage().setText("");
                                                            ((HasEnabled) relationMemento.getBtnAddLinkNew()).setEnabled(true);
                                                        }
                                                    }
                                                }).ifPresent(p -> {
                                                    setCurrentBean((T) p);
                                                }); //need to reassign parent (setCurrentBean), if not, there will be lingering deleted item
                                    }

                                } else {
                                    notifCannotDelete.open();
                                }
                                StringBuilder textHtml = new StringBuilder("<div><div>The items below cannot be deleted</div> <ul>");

                                if (!cannotBeDeleted.isEmpty()) {
                                    for (String item : cannotBeDeleted) {
                                        textHtml.append("<li>" + item + "</li>");
                                    }
                                    textHtml.append("</ul></div>");
                                    Html hc = new Html(textHtml.toString());

                                    Notification errorNotif = new Notification(hc);
                                    errorNotif.setPosition(Position.TOP_START);
                                    errorNotif.setDuration(3000);
                                    errorNotif.open();
                                }
                                pojoTableFactory.searchAndRedrawTable(relationMemento);
                            },
                            "Cancel", (ce) -> {
                            });
                    confirmDeleteDialog.open();

                },
                e -> {
                    relationMemento.getGrid().deselectAll();
                }
        );
        return layout;
    }

    private Optional<SubFormComponent> renderSubForm(RendererMemento capture) {
        if (capture == null) {
            return Optional.empty();
        }

        //deal with custom component
        SubFormComponent defaultComp = new SubFormComponent();
        SubFormComponent subFormComponent = capture.renderer.render(
                root,
                getCurrentBean(),
                capture.rc.getRelationshipName(),
                Optional.of(getState()),
                capture.eventToOpenOtherPopups,
                relationMementos,
                rm -> {
                    if (rm == null) {
                        return;
                    }
                    rm.incrementCounter();
                    if (rm.reachedMax()) {
                        rm.getErrorMessage().setText("Maximum has been reached");
                        if (rm.getBtnAddLinkNew() instanceof HasEnabled) {
                            ((HasEnabled) rm.getBtnAddLinkNew()).setEnabled(false);
                        } else {
                            rm.getBtnAddLinkNew().setVisible(false);
                        }
                    }
                },
                rm -> {
                    rm.decrementCounter();
                    if (!rm.reachedMax()) {
                        rm.getErrorMessage().setText("");
                        if (rm.getBtnAddLinkNew() instanceof HasEnabled) {
                            ((HasEnabled) rm.getBtnAddLinkNew()).setEnabled(true);
                        } else {
                            rm.getBtnAddLinkNew().setVisible(true);
                        }
                    }
                }
        ).orElse(defaultComp);
        //save the layout
        editableComponents.add(new Dual(capture.rc.getRelationshipName(), subFormComponent));
        subFormComponent.setCurrentTab(capture.tab);
        subFormComponent.setCurrentButton(capture.menuItem);
        subFormComponent.setId("customComponentContainer");
        return Optional.of(subFormComponent);
    }

    private void instantiateAllSubForms() {
        for (var rendererEntry : subFormRenderers.entrySet()) {
            if (!subFormComponents.containsKey(rendererEntry.getKey())) {
                renderSubForm(rendererEntry.getValue()).ifPresent(customComp -> {
                    subFormComponents.put(rendererEntry.getKey(), customComp);
                });

            }
        }
    }

    public ReasonToClose validate() {

        Boolean res = Boolean.TRUE;
        //if some custom-components-in-form is not instantiated, we cannot validate them
        //so instantiate them first
        instantiateAllSubForms();

        //validate relations, form and custom components 
        if (!validateRelations()) {
            notifCheckMinRequirements.open();
            res = Boolean.FALSE;
        }
        if (!validateForm().isEmpty()) {
            notifInvalidForm.open();
            res = Boolean.FALSE;
        }
        if (!validateSubForm()) {
            notifInvalidForm.open();
            res = Boolean.FALSE;
        }
        if (res) {
            return ReasonToClose.SUBMITTED;
        } else {
            return ReasonToClose.ERROR;
        }
    }

    private ComponentEventListener<ClickEvent<MenuItem>> createEventToPopFormUp(Integer key, RendererMemento capture, MenuItem mi) {

        return (e -> {
            if (getCurrentBean().isRoot()) {
               
                if (capture != null) {
                    var form = subFormComponents.get(key);
                    if (form == null) {
                        form = renderSubForm(capture).map(f -> {
                            //f.setCurrentButton(e.getSource());
                            f.setCurrentButton(mi);
                            subFormComponents.put(key, f);
                            return f;
                        }).orElse(new SubFormComponent());
                    }
                    showSubFormDialog(key, 
                            form);
                }
            }
        });
    }

    private void showSubFormDialog(Integer key, Component form) {
        Dialog dialog = new Dialog();
        dialog.setResizable(true);
        dialog.setDraggable(true);
        dialog.setWidth("50%");
        dialog.setId("subform-dialog-"+key);
        dialog.setModal(true);
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);
        
         Castor.<Component, SubFormComponent>given(form)
                    .castItTo(SubFormComponent.class).thenDo(sf -> {
                sf.setParentDialog(dialog);
                
            }).go();
        
        Button closeButton = new Button("Close", e2 -> {
            Castor.<Component, SubFormComponent>given(form)
                    .castItTo(SubFormComponent.class).thenDo(sf -> {
                if (sf.validate()) {
                    dialog.close();
                } else {
                    notifInvalidForm.open();
                }
            }).failingWhichDo(() -> {
                dialog.close();//this is a table - validation will be done on the table form
            }).go();

        });
        closeButton.setId(key + "-btnCloseForm");
        dialog.add(closeButton);
        dialog.add(form);
        dialog.setHeightFull();
        dialog.open();
    }

    private void initializeTabsOrPopups(
            HasComponents mainTab,
            Map<Integer, String> popupStore,
            Map<Integer, String> tabStore,
            Div mainForm,
            MenuBar headerBar,
            VerticalLayout headerAndFormLayout,
            Map<Integer, EventToOpenOtherComponent> eventToOpenOtherPopups)
            throws NumberFormatException {
        //create tabs
//        int tabIndex = -1;
        if (tabs != null) {//remove existing tabs first before we initialize (case for Book this work)
//            tabIndex = headerAndFormLayout.indexOf(tabs);
            headerAndFormLayout.remove(tabs);
        }
        tabs = new Tabs();

        //add main Tab
        if (!tabStore.isEmpty()) {
            var tabName = tabStore.get(0);
            TabWithIndex maintab = new TabWithIndex();
            maintab.setId("tab-" + tabName);
            maintab.setIndex(0);
            maintab.add(new Label(tabName));
            tabs.addComponentAtIndex(0, maintab);
            tabs.setSelectedTab(maintab);

        }
        //display tabs
        for (Map.Entry<Integer, String> entry : tabStore.entrySet()) {
            if (entry.getKey() == 0) {
                continue;
            }
            TabWithIndex tab = new TabWithIndex();
            RendererMemento capture = subFormRenderers.get(entry.getKey());
            if (capture != null) {
                capture.tab = tab;
                tab.setId("tab-" + tabStore.get(entry.getKey()));
                tab.setIndex(entry.getKey());
                tab.add(new Label(tabStore.get(entry.getKey())));
                tabs.add(tab);
            } else { //no renderer
                tab.setId("tab-" + tabStore.get(entry.getKey()));
                tab.setIndex(entry.getKey());
                tab.add(new Label(tabStore.get(entry.getKey())));
                tabs.add(tab);
            }
        }
        //display popups
        for (Map.Entry<Integer, String> entry : popupStore.entrySet()) {
            RendererMemento capture = subFormRenderers.get(entry.getKey());
            if (capture != null) { //has renderer = to be displayed lazily upon button click
                MenuItem mi = headerBar.addItem(entry.getValue());
                capture.menuItem = mi;
                var event = createEventToPopFormUp(entry.getKey(), capture, mi);
                mi.addClickListener(event);
                mi.setId("btn" + entry.getValue());
                eventToOpenOtherPopups.put(entry.getKey(),
                        EventToOpenOtherComponent.of(entry.getValue(), event));
            } else { //no renderer = use table to display = eager
                ComponentEventListener<ClickEvent<MenuItem>> event = e -> {
                    SubFormComponent sfComp = subFormComponents.get(entry.getKey());
                    if (sfComp != null) {
                        showSubFormDialog(entry.getKey(), sfComp);
                    } else {
                        showSubFormDialog(entry.getKey(), (Component) mainTab);
                    }
                };
                MenuItem mi = headerBar.addItem(entry.getValue(), event);
                subFormComponents.get(entry.getKey()).setCurrentButton(mi);
                mi.setId("btn" + entry.getValue());
                eventToOpenOtherPopups.put(entry.getKey(),
                        EventToOpenOtherComponent.of(entry.getValue(), event));
            }
        }

//        if (tabIndex == -1) {
        headerAndFormLayout.add(tabs);
//        } else {
//            headerAndFormLayout.addComponentAtIndex(tabIndex, tabs);
//        }
        mainForm.setId("main-form");
        if (VaadinSession.getCurrent().getBrowser().isFirefox()) {
            mainForm.getStyle().set("width", "-moz-available");
        } else {
            mainForm.getStyle().set("width", "-webkit-fill-available");
        }
        mainForm.add((Component) mainTab);

        tabs.addSelectedChangeListener(e -> {
            int ind = ((TabWithIndex) e.getSelectedTab()).getIndex();
            var selectedComp = subFormComponents.get(ind);

            RendererMemento capture = subFormRenderers.get(ind);
            if (selectedComp == null) {
                selectedComp = renderSubForm(capture).
                        map(comp -> {
                            subFormComponents.put(ind, comp);
                            //comp.setCurrentTab(capture.tab);
                            return comp;
                        }).orElse(null);

            }
            if (selectedComp != null) {
                selectedComp.setCurrentTab(e.getSelectedTab());
                selectedComp.onTabNavigateAway();
                mainForm.removeAll();
                mainForm.add(selectedComp);
                selectedComp.onTabNavigateIn();
            } else {
                //if selectedComp does not exist
                //or tab == inactive, that means our index point to the big tab itself (like attachment)
                //(we've filtered out buttons-bound custom coponent earlier)
                //so in these cases, we take a form by selecting from formStore
                //no need to render
                mainForm.removeAll();
                mainForm.add((Component) mainTab);
            }
        });

    }

    private Optional<ErrorType> validateForm() {
        BinderValidationStatus<T> status = binder.validate();

        if (status.isOk()) {
            return Optional.empty();
        } else {
            return Optional.of(ErrorType.ERROR);
        }

    }

    private Boolean validateSubForm() {
        Boolean res = Boolean.TRUE;
        for (SubFormComponent c : subFormComponents.values()) {
            Boolean valid = c.validate();
            res = res && valid;
        }
        return res;
    }


    private Boolean validateRelations() {
        Boolean noErrorFlag = Boolean.TRUE;
        for (Map.Entry<String, RelationMemento> e : relationMementos.entrySet()) {
            RelationMemento rm = e.getValue();
            Boolean validate = rm.validate();
            noErrorFlag = noErrorFlag && validate;
        }
        return noErrorFlag;
    }

    /**
     * @return the current
     */
    public T getCurrentBean() {
        return binder.getBean();
    }

    private T getDraftCurrentBean() {
        T nbean = entityUtils.createNewObject(currentClass);
        binder.writeBeanAsDraft(nbean, true);
        return nbean;
    }

    private Class<T> getCurrentClass() {
        //return current;
        return currentClass;
    }

    /**
     * @param current the current to set
     */
    private void setCurrentBean(T current) {
        //this.current = current;
        binder.setBean(current);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent arg0) {
        if (VaadinSession.getCurrent().getSession().getAttribute("USER_IDENTIFIER") == null) {
            UI.getCurrent().getPage().executeJs("window.open(\"" + fullUrl + "/main\", \"_self\");");
        }

    }

    @Override
    public void beforeLeave(BeforeLeaveEvent arg0) {
    }

    private <C> C createNew(Class<C> childClass, T parentBean) {

        String userIdentifier = (String) VaadinSession.getCurrent().getSession().getAttribute("USER_IDENTIFIER");
        //Set<String> roles = (Set<String>) VaadinSession.getCurrent().getSession().getAttribute("ROLES");
        String tenant = (String) VaadinSession.getCurrent().getSession().getAttribute("TENANT");
        Optional<String> parentEnumPath = Castor.<T, Element>given(parentBean)
                .castItTo(Element.class)
                .thenDo(w -> {
                    return w.getEnumPath();
                }).go();

        return (C) dao.createAndSave(childClass,
                Optional.of(tenant),
                parentEnumPath,
                Optional.empty(),
                Optional.empty(),
                Status.DRAFT,
                userIdentifier).orElseThrow();

    }

    /**
     * @return the reasonToClose
     */
    public ReasonToClose getReasonToClose() {
        return reasonToClose;
    }

    /**
     * @param reasonToClose the reasonToClose to set
     */
    private void setReasonToClose(ReasonToClose reasonToClose) {
        this.reasonToClose = reasonToClose;
    }

    /**
     * @return the state
     */
    private PojoViewState getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    private void setState(PojoViewState state) {
        this.state = state;
    }

//    public void setParentState(PojoViewState parentState) {
//        this.parentState = parentState;
//    }
    private StringToLongConverter getStringToLongConverter(String errorMessage, FieldContainer fc) {
        if (fc.isId()) {
            return new IdStringConverter(errorMessage);
        } else {
            return new CustomStringToLongConverter(errorMessage);
        }
    }

    private void implementState() {
        if (null != state) {
            switch (state) {
                case RELEASABLE_BY_ROLE_MGR:
                    if (miSaveSubmit != null) {
                        miSaveSubmit.setVisible(false);
                    }
                    if (miFindUsage != null) {
                        miFindUsage.setVisible(false);
                    }
                    if (miBook != null) {
                        miBook.setVisible(false);
                    }
                    if (miUnBook != null) {
                        miUnBook.setVisible(true);
                    }
                    setEnableEditableComponents(false);
                    break;
                case BOOKABLE:
                    if (miSaveSubmit != null) {
                        miSaveSubmit.setEnabled(false);
                    }
                    if (miFindUsage != null) {
                        miFindUsage.setVisible(false);
                    }
                    if (miBook != null) {
                        miBook.setEnabled(true);
                    }
                    if (miUnBook != null) {
                        miUnBook.setEnabled(false);
                    }
                    setEnableEditableComponents(false);
                    break;
                case EDITABLE:
                case CORRECTABLE:

                    if (miSaveSubmit != null) {
                        miSaveSubmit.setEnabled(true);
                    }
                    if (miFindUsage != null) {
                        miFindUsage.setVisible(false);
                    }
                    if (miBook != null) {
                        miBook.setEnabled(false);
                    }
                    if (miUnBook != null) {
                        miUnBook.setEnabled(true);
                    }
                    setEnableEditableComponents(true);
                    break;
                case EDITABLE_NOTRELEASABLE://for newly created object
                    if (miSaveSubmit != null) {
                        miSaveSubmit.setEnabled(true);
                    }
                    if (miFindUsage != null) {
                        miFindUsage.setVisible(false);
                    }
                    if (miBook != null) {
                        miBook.setEnabled(false);
                    }
                    if (miUnBook != null) {
                        miUnBook.setEnabled(false);
                    }
                    setEnableEditableComponents(true);
                    break;
                case EDITABLE_NOTRELEASABLE_REF:
                    if (miSaveSubmit != null) {
                        miSaveSubmit.setEnabled(true);
                    }
                    if (miFindUsage != null) {
                        miFindUsage.setVisible(true);
                    }
                    if (miBook != null) {
                        miBook.setEnabled(false);
                    }
                    if (miUnBook != null) {
                        miUnBook.setEnabled(false);
                    }
                    setEnableEditableComponents(true);
                    break;
                case NOT_EDITABLE:
                    if (miSaveSubmit != null) {
                        miSaveSubmit.setVisible(false);
                    }
                    if (miFindUsage != null) {
                        miFindUsage.setVisible(false);
                    }
                    if (miBook != null) {
                        miBook.setVisible(false);
                    }
                    if (miUnBook != null) {
                        miUnBook.setVisible(false);
                    }
                    setEnableEditableComponents(false);
                    break;
                case NOT_EDITABLE_REF:
                    if (miSaveSubmit != null) {
                        miSaveSubmit.setVisible(false);
                    }
                    if (miFindUsage != null) {
                        miFindUsage.setVisible(true);
                    }
                    if (miBook != null) {
                        miBook.setVisible(false);
                    }
                    if (miUnBook != null) {
                        miUnBook.setVisible(false);
                    }
                    setEnableEditableComponents(false);

                    break;
                case TO_BE_APPROVED:
                    if (miSaveSubmit != null) {
                        miSaveSubmit.setVisible(true);
                    }
                    if (miFindUsage != null) {
                        miFindUsage.setVisible(false);
                    }
                    if (miBook != null) {
                        miBook.setVisible(false);
                    }
                    if (miUnBook != null) {
                        miUnBook.setVisible(false);
                    }
                    //setEnableEditableComponents(false, true);
                    setEnableEditableComponents(false);
                    break;
                default:
                    break;
            }
        }
    }

    private void setEnableEditableComponents(boolean enabledOverall) {

        for (RelationMemento memento : relationMementos.values()) {
            memento.calculateEditableRelation(root, canAddDeleteRelationsRule, enabledOverall);
        }
        //String worklist = root.getWorkflowInfo().getWorklist();
        for (Dual<String, Component> p : editableComponents) {
            Component c = p.getSecond();

            Optional<WebField> wf = fieldUtils.getWebField(currentClass, p.getFirst());
            boolean enb = false;
            if (wf.isEmpty()) { //relationship
                //skip relationship since we already processed this using memento
            } else {
                if (getCurrentBean().isReference()) { //if we are editing REF, evaluate rule againts current object instead
                    enb = enabledOverall
                            && canEditFieldRule.compute(Optional.ofNullable(getCurrentBean()), wf);
                } else {
                    enb = enabledOverall
                            && canEditFieldRule.compute(Optional.ofNullable(root), wf);
                }
                if (AbstractField.class.isAssignableFrom(c.getClass())) {

                    if (ComboBox.class.isAssignableFrom(c.getClass())) {
                        ComboBox cb = (ComboBox) c;
                        cb.setReadOnly(!enb);
                    } else {
                        AbstractField f = (AbstractField) c;
                        f.setReadOnly(!enb);
                    }

                } else {
                    HasEnabled h = (HasEnabled) c;
                    h.setEnabled(enb);
                }
            }
        }
    }

    private void computeState() {
        // see if a role can access a particular worklist

        //see if this PojoView should be disabled or not
        if (null == entityUtils.getEntityType(currentClass)) { //nominal
            setState(parentState);
        } else //int s = 0; //s==0: Can save, s==1: Need booking
        {
            switch (entityUtils.getEntityType(currentClass)) {
                case ROOT:
                    Castor.<T, WorkElement>given(getCurrentBean())
                            .castItTo(WorkElement.class)
                            .thenDo(ext -> {
                                if (isReleasableByRoleManager.compute(Optional.of(getCurrentBean()))) {
                                    setState(PojoViewState.RELEASABLE_BY_ROLE_MGR);
                                } else {
                                    if (isApprovalNeeded.compute(Optional.of(getCurrentBean())) &&
                                            canApproveRule.compute(Optional.of(getCurrentBean()))) {
                                        setState(PojoViewState.TO_BE_APPROVED);
                                    }

                                    if (getState() != PojoViewState.TO_BE_APPROVED) {
                                        if (Status.DRAFT.equals(ext.getStatus())) { //newly created entity
                                            setState(PojoViewState.EDITABLE_NOTRELEASABLE);
                                        } else {
                                            if (canBookRule.compute(Optional.of(getCurrentBean()))) {
                                                setState(PojoViewState.BOOKABLE);
                                            } else if (canEditEntityRule.compute(Optional.ofNullable(root))) { //evaluate editability againts root
                                                if (ext.getApprovals().isEmpty()) {
                                                    setState(PojoViewState.EDITABLE);
                                                } else {
                                                    setState(PojoViewState.CORRECTABLE);
                                                }
                                            } else {
                                                setState(PojoViewState.NOT_EDITABLE);
                                            }
                                        }
                                    }
                                }
                            }).go();
                    break;
                case REF:
                    if (canEditEntityRule.compute(Optional.ofNullable(getCurrentBean()))) { //evaluate editability againts current object. There is no root when editing REF
                        setState(PojoViewState.EDITABLE_NOTRELEASABLE_REF);
                    } else {
                        setState(PojoViewState.NOT_EDITABLE_REF);
                    }
                    break;
                default:
                    //nominal
                    setState(parentState);
                    break;
            }
        }

    }

    private void computeAndImplementState() {
        computeState();
        implementState();
    }

    private void callCustomComponentBeforeSaveCallback() {
        for (SubFormComponent c : subFormComponents.values()) {
            c.beforeSaveCallBack();
        }
    }

    private void callCustomComponentBeforeSubmitCallback() {
        for (SubFormComponent c : subFormComponents.values()) {
            c.beforeSubmitCallBack();
        }
    }

    private Boolean calculateVisibilityForField(Optional<WebEntity> oWebEntity,
            WebField webField,
            T bean,
            String fieldName) {
        return oWebEntity.map(webEntity -> fieldUtils.getFieldVisibilityFromEntity(webEntity, fieldName).map(
                fv -> (Boolean) expr.evaluate(fv.visibleInForm(), bean)
        ).orElseGet(
                () -> (Boolean) expr.evaluate(webField.visibleInForm(), bean)
        )).orElse(Boolean.FALSE);
    }

    private Boolean calculateVisibilityForRelation(Optional<WebEntity> oWebEntity,
            WebRelation webRelation,
            T bean,
            String relationName) {
        return oWebEntity.map(webEntity -> fieldUtils.getFieldVisibilityFromEntity(webEntity, relationName).map(
                fv -> (Boolean) expr.evaluate(fv.visibleInForm(), bean)
        ).orElseGet(
                () -> (Boolean) expr.evaluate(webRelation.visibleInForm(), bean)
        )).orElse(Boolean.FALSE);
    }

    private class RendererMemento {

        private SubFormRenderer<T, R, SubFormComponent> renderer;
        private RelationContainer rc;
        private Map<Integer, EventToOpenOtherComponent> eventToOpenOtherPopups;
        //private HasComponents form;
        //private List<Component> relationsOnMainTab;
        private Tab tab;
        private MenuItem menuItem;

        private RendererMemento(SubFormRenderer<T, R, SubFormComponent> renderer,
                RelationContainer rc,
                Map<Integer, EventToOpenOtherComponent> eventToOpenOtherPopups
        /*,HasComponents form,
                List<Component> relationsOnMainTab*/) {
            this.renderer = renderer;
            this.rc = rc;
            this.eventToOpenOtherPopups = eventToOpenOtherPopups;
            //this.form = form;
            //this.relationsOnMainTab = relationsOnMainTab;
        }

    }

}
