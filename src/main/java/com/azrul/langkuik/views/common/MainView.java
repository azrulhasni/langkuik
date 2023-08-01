package com.azrul.langkuik.views.common;

import com.azrul.langkuik.custom.bizConfig.ConfigMDV;
import com.azrul.langkuik.framework.entity.WebEntityType;
import com.azrul.langkuik.framework.dao.DataAccessObject;
import com.azrul.langkuik.framework.dao.DataAccessObjectImpl;
import com.azrul.langkuik.framework.entity.EntityUtils;
import com.azrul.langkuik.framework.factory.SpringBeanFactory;
import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.rule.SoundnessCheck;
import com.azrul.langkuik.framework.user.UserProfile;
import com.azrul.langkuik.framework.user.UserProfileDialogFactory;
import com.azrul.langkuik.framework.user.UserRetrievalService;
import com.azrul.langkuik.framework.workflow.Workflow;
import com.azrul.langkuik.framework.workflow.model.Activity;
import com.azrul.langkuik.views.pojo.PojoView;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.RouterLink;

import com.azrul.langkuik.views.table.TableView;
import com.azrul.langkuik.framework.user.UserIdentifierLookup;
import com.vaadin.componentfactory.ToggleButton;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouteData;
import com.vaadin.flow.server.VaadinServletService;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * The main view is a top-level placeholder for other views.
 */
//@JsModule("./styles/shared-styles.js")
//@PWA(name = "Langkuik", shortName = "Langkuik", startPath = "main")

@Theme(themeClass = Lumo.class, variant = Lumo.LIGHT)
@CssImport(value = "./langkuik-global-styles.css")
@CssImport(value = "./langkuik-global-styles.css", themeFor = "vaadin-tab")
@CssImport(value = "./langkuik-global-styles.css", themeFor = "vaadin-text-field")
@CssImport(value = "./langkuik-global-styles.css", themeFor = "vaadin-button")
@CssImport(value = "./langkuik-global-styles.css", themeFor = "vaadin-combo-box")
@CssImport(value = "./langkuik-global-styles.css", themeFor = "vaadin-checkbox")
@CssImport(value = "./langkuik-global-styles.css", themeFor = "vaadin-combo-box-item")
@CssImport(value = "./langkuik-global-styles.css", themeFor = "vaadin-accordion-panel")
@CssImport(value = "./langkuik-global-styles.css", themeFor = "vaadin-upload")
@CssImport(value = "./langkuik-global-styles.css", themeFor = "vaadin-grid-cell-content")
@CssImport(value = "./langkuik-global-styles.css", themeFor = "vaadin-grid-sorter")
public class MainView<T extends WorkElement> extends AppLayout implements AppShellConfigurator{

    private Class<? extends Component> rootClassMDV;// = ApplicationMDV.class;
    private Class<? extends Component> rootClassAuditMDV;
    private final List<Class<? extends Component>> refClassMDVList;

    @Autowired
    private DataAccessObject<T> dao;

    //private final Tabs menu;
    @Autowired
    private Workflow<T> workflow;

    @Autowired
    private EntityUtils entityUtils;
    
    @Value("${application.lgFullurl}")
    private String fullUrl;

    @Value("${keycloak.resource}")
    private String keycloakClient;

    @Autowired
    UserIdentifierLookup userIdentifierLookup;

    @Autowired
    private UserProfileDialogFactory userProfileDialogFactory;

    @Autowired
    UserRetrievalService userRetrievalService;
    


    public MainView() {
        refClassMDVList = new ArrayList<>();
          
    }

    @PostConstruct
    private void construct() {
        if (!RouteConfiguration.forSessionScope().isRouteRegistered(ConfigMDV.class)){
            RouteConfiguration.forSessionScope().setAnnotatedRoute(ConfigMDV.class);
        }
        //Determine root mdv class
        List<RouteData> routes = RouteConfiguration.forSessionScope().getAvailableRoutes();
        try {
            

            for (RouteData r : routes) {
                if (TableView.class.isAssignableFrom(r.getNavigationTarget())) {
                    Class<TableView> ctv = (Class<TableView>) r.getNavigationTarget();
                    TableView tv = ctv.getDeclaredConstructor().newInstance();

                    WebEntityType we = entityUtils.getEntityType(tv.getCurrentClass());
                    if (we != null) {
                        if (we.equals(WebEntityType.ROOT)) {
                            rootClassMDV = ctv;
                            //break;
                        } else if (we.equals(WebEntityType.REF)) {
                            refClassMDVList.add(ctv);
                        }
                    }
                } /*else if (AuditView.class.isAssignableFrom(r.getNavigationTarget())) {
                    //getCurrentAuditedClass
                    Class<AuditView> ctv = (Class<AuditView>) r.getNavigationTarget();
                    AuditView tv = ctv.getDeclaredConstructor().newInstance();
                    WebEntity we = (WebEntity) tv.getCurrentAuditedClass().getAnnotation(WebEntity.class);
                    if (we != null) {
                        if (we.type().equals(WebEntityType.ROOT)) {
                            rootClassAuditMDV = ctv;
                            //break;
                        }
                    }
                }*/
            }
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Authentication
        Optional<KeycloakPrincipal> principal = getKeycloakPrinciple();
        principal.ifPresentOrElse((p) -> {
            populateUserInfo(p);
            setPrimarySection(Section.DRAWER);
            DrawerToggle toggle = new DrawerToggle();
            toggle.setId("openDrawer");
            addToNavbar(true, toggle);

            Button btnLogout = new Button("Logout", e -> {
                UI.getCurrent().getPage().setLocation(fullUrl);
                UI.getCurrent().getSession().close();
                UI.getCurrent().getSession().getSession().invalidate();
                try {
                    VaadinServletService.getCurrentServletRequest().logout();
                } catch (ServletException ex) {
                    Logger.getLogger(TableView.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            btnLogout.setId("btnLogout");
            HorizontalLayout topContainer = new HorizontalLayout();

            String givenName = (String) VaadinSession.getCurrent().getSession().getAttribute("GIVENNAME");
            String familyName = (String) VaadinSession.getCurrent().getSession().getAttribute("FAMILYNAME");
            Set<String> roles = (Set<String>) VaadinSession.getCurrent().getSession().getAttribute("ROLES");


            ToggleButton btnToggleDark = new ToggleButton("Go Dark");
            btnToggleDark.addValueChangeListener(e->{
                 ThemeList themeList = UI.getCurrent().getElement().getThemeList(); 
                if (Boolean.TRUE.equals(e.getValue())){
                     themeList.add(Lumo.DARK);
                }else{
                    themeList.remove(Lumo.DARK);
                }
            });
            
            Button btnProfile = new Button("Profile", e -> {
                userProfileDialogFactory.createDialog();
            });
            topContainer.add(btnToggleDark,btnProfile);
            TextField name = new TextField();
            name.setReadOnly(true);
            name.setValue(givenName + " " + familyName + " ");
            topContainer.add(name);

            
            topContainer.add(/*btnUsers,*/btnLogout);

            topContainer.getStyle().set("margin-left", "auto");
            topContainer.getStyle().set("margin-right", "1%");
            topContainer.getStyle().set("padding", "1px");

            addToNavbar(topContainer);

            //auditor
            if (roles.contains("Auditor") && roles.size() == 1) { //an auditor can have only that role
                addToDrawer(getAudit());
            } else if (roles.contains("REF_ADMIN") && roles.size() == 1) { //a ref admin can only have that role
                //REF ADMIN
                addToDrawer(getRef());

            } else {
                //normal users
                addToDrawer(getMyWork());

                List<Accordion> accordions = getWorkflowTabs(workflow);
                for (Accordion accordion : accordions) {
                    accordion.close();
                    addToDrawer(accordion);
                }
                addToDrawer(new Hr());

                UserProfile user = (UserProfile) VaadinSession.getCurrent().getSession().getAttribute("USER");
                if (!user.getOtherAttributes().isEmpty()) {
                    List<String> rolesManaged = (List<String>) user.getOtherAttributes().get("Manager-of-role");
                    if (rolesManaged != null) {
                        VerticalLayout teamsWorkContainer = new VerticalLayout();
                        teamsWorkContainer.add(new H4("Team's work"));

                        List<Accordion> teamsWork = getMyTeamsWork(workflow, rolesManaged);
                        for (Accordion accordion : teamsWork) {
                            accordion.close();
                            teamsWorkContainer.add(accordion);
                        }
                        addToDrawer(teamsWorkContainer);
                    }

                }

            }

        }, () -> {
            UI.getCurrent().getPage().executeJs("window.open(\"" + fullUrl + "\", \"_self\");");
        });
    }

    public Optional<KeycloakPrincipal> getKeycloakPrinciple() {
        if (!SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal().equals("anonymousUser")) {
            KeycloakPrincipal principal
                    = (KeycloakPrincipal) SecurityContextHolder.getContext()
                            .getAuthentication().getPrincipal();

            return Optional.ofNullable(principal);
        } else {
            return Optional.empty();
        }
    }

    public void populateUserInfo(KeycloakPrincipal principal) {
        KeycloakSecurityContext keycloakSecurityContext
                = principal.getKeycloakSecurityContext();

        Set<String> roles = principal.getKeycloakSecurityContext().getToken().getResourceAccess(keycloakClient).getRoles();
        String realm = principal.getKeycloakSecurityContext().getRealm();
        String preferredUsername = keycloakSecurityContext.getIdToken().getPreferredUsername();
        String givenName = keycloakSecurityContext.getIdToken().getGivenName();
        String familyName = keycloakSecurityContext.getIdToken().getFamilyName();
        String email = keycloakSecurityContext.getIdToken().getEmail();

        VaadinSession.getCurrent().getSession().setAttribute("ROLES", roles != null ? roles : new HashSet<>());
        VaadinSession.getCurrent().getSession().setAttribute("LOGINNAME", preferredUsername);
        VaadinSession.getCurrent().getSession().setAttribute("TENANT", realm);
        VaadinSession.getCurrent().getSession().setAttribute("GIVENNAME", givenName);
        VaadinSession.getCurrent().getSession().setAttribute("FAMILYNAME", familyName);
        VaadinSession.getCurrent().getSession().setAttribute("EMAIL", email);

        for (Map.Entry<String, Object> e : principal.getKeycloakSecurityContext().getToken().getOtherClaims().entrySet()) {
            VaadinSession.getCurrent().getSession().setAttribute(e.getKey(), e.getValue());
        }

        UserProfile user = userRetrievalService.getUserByLoginName(preferredUsername);
        userIdentifierLookup.lookup(user).ifPresent(userId->{
            VaadinSession.getCurrent().getSession().setAttribute("USER_IDENTIFIER",userId );
        });
        VaadinSession.getCurrent().getSession().setAttribute("USER", user != null ? user : new HashMap<>());
    }

    private Accordion getMyWork() {
        Accordion accordion = new Accordion();

        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        Tab createdByMe = createWorkflowTab(
                "Created by me",
                "createdByMe",
                rootClassMDV,
                Optional.empty(),
                OwnershipType.CREATED_BY_ME);
        createdByMe.setId("tabCreatedByMe");
        tabs.add(createdByMe);
        Tab ownedByMe = createWorkflowTab(
                "Owned by me",
                "ownedByMe",
                rootClassMDV,
                Optional.empty(),
                OwnershipType.OWNED_BY_ME);
        ownedByMe.setId("tabOwnedByMe");
        tabs.add(ownedByMe);
        accordion.add("My work", tabs);
        return accordion;
    }

    private List<Accordion> getMyTeamsWork(Workflow wf, List<String> rolesManaged) {
        final List<Accordion> accordions = new ArrayList<>();
        Map<String, List<Activity>> roleActivity = wf.getRoleActivityMap();

        for (String currentRole : rolesManaged) {

            final Tabs tabs = new Tabs();
            tabs.setOrientation(Tabs.Orientation.VERTICAL);
            tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);

            List<Activity> activs = roleActivity.get(currentRole);
            for (Activity activ : activs) {
                tabs.add(createWorkflowTab(
                        activ.getDescription(),
                        "activity" + activ.getId(),
                        rootClassMDV,
                        Optional.of(activ.getId()),
                        OwnershipType.OWNED_BY_MY_TEAM_AND_ME));
            }
            Accordion accordion = new Accordion();
            accordion.setId("team_" + currentRole);
            accordion.add(currentRole, tabs);
            accordions.add(accordion);
        }
        return accordions;
    }

    private Accordion getAudit() {
        Accordion accordion = new Accordion();
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        Tab auditTrail = createWorkflowTab("Audit Trail",
                "auditTrail",
                rootClassAuditMDV,
                Optional.empty(),
                OwnershipType.ANY_OWNERSHIP);
        auditTrail.setId("tabAuditTrail");
        tabs.add(auditTrail);
        accordion.add("Main Audit Trail", tabs);
        return accordion;
    }

    private Accordion getRef() {
        Accordion accordion = new Accordion();
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        for (Class<? extends Component> c : refClassMDVList) {
            PageTitle title = c.getAnnotation(PageTitle.class);
            Tab ref = createWorkflowTab(
                    title.value(),
                    "ref" + c.getSimpleName(),
                    c,
                    Optional.empty(),
                    OwnershipType.OWNED_BY_ME);
            ref.setId("tabReference");
            tabs.add(ref);
        }
        accordion.add("Reference Management", tabs);
        return accordion;
    }

    private List<Accordion> getWorkflowTabs(Workflow wf) {
        Set<String> currentRoles = (Set<String>) VaadinSession.getCurrent().getSession().getAttribute("ROLES");
        final List<Accordion> accordions = new ArrayList<>();
        Map<String, List<Activity>> roles = wf.getRoleActivityMap();

        //Map<String, List<Activity>> roleActivity = wf.getRoleActivityMap();
        for (String currentRole : currentRoles) {

            final Tabs tabs = new Tabs();
            tabs.setOrientation(Tabs.Orientation.VERTICAL);
            tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);

            if (roles.containsKey(currentRole)) {
                List<Activity> activs = roles.get(currentRole);
                for (Activity activ : activs) {
                    tabs.add(createWorkflowTab(activ.getDescription(), "activity" + activ.getId(), rootClassMDV, Optional.of(activ.getId()), OwnershipType.ANY_OWNERSHIP));
                }
                Accordion accordion = new Accordion();
                accordion.add(currentRole, tabs)/*.addThemeVariants(DetailsVariant.SMALL)*/;
                accordions.add(accordion);
            }
        }
        return accordions;
    }

    private static <T> Tab createWorkflowTab(String title,
            String id,
            Class<? extends Component> viewClass,
            Optional<String> myWorklist,
            OwnershipType ownership) {

        RouterLink link = new RouterLink(viewClass);
        link.setId(id);
        link.getStyle().set("margin-left", "0");
        Map<String, String> params = new HashMap<>();

        myWorklist.ifPresent((String activityId) -> {
            params.put("worklist", activityId);
        });

        if (ownership == OwnershipType.OWNED_BY_ME) {
            params.put("ownedByMe", "true");
        }

        if (ownership == OwnershipType.OWNED_BY_MY_TEAM_AND_ME) {
            params.put("ownedByMeAndMyTeam", "true");
        }

        //no params needed for CREATED_BY_ME: default behaviour
        if (!params.isEmpty()) {
            link.setQueryParameters(QueryParameters.simple(params));
        }
        return createTab(populateLink(link, title));
    }

    private static Tab createTab(Component content) {
        final Tab tab = new Tab();
        tab.add(content);
        return tab;
    }

    private static <T extends HasComponents> T populateLink(T a, String title) {
        a.add(title);
        return a;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
    }

    private void createUserProfileDialog(UserProfile up) {
        Dialog dialog2 = new Dialog();
        PojoView pojoView = SpringBeanFactory.create(PojoView.class);
        pojoView.construct(up, Optional.of(dialog2));

    }

}
