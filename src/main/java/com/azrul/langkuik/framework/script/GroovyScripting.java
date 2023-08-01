/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azrul.langkuik.framework.script;

import com.azrul.langkuik.framework.dao.DataAccessObject;
import com.azrul.langkuik.framework.dao.query.DataQuery;
import com.azrul.langkuik.framework.entity.EntityUtils;
import com.azrul.langkuik.framework.entity.WebEntityType;
import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.factory.SpringBeanFactory;
import com.azrul.langkuik.framework.user.UserProfile;
import com.azrul.langkuik.framework.workflow.Workflow;
import com.azrul.langkuik.framework.workflow.WorkflowImpl;
import com.vaadin.flow.server.VaadinSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author azrul
 */
@Service
public class GroovyScripting implements Scripting {
    ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("groovy");
    
    @Autowired
    private DataAccessObject dao;
    
     @Autowired
    private EntityManagerFactory emf;

    
    public <T extends WorkElement> void runScript(T work, String script, Workflow workflow) {
        if (script==null){
            return;
        }
        if (work==null){
            return;
        }
        try {
            final String userIdentifier = (String) VaadinSession.getCurrent().getSession().getAttribute("USER_IDENTIFIER");
            final UserProfile user = (UserProfile) VaadinSession.getCurrent().getSession().getAttribute("USER");
            final String tenant = (String) VaadinSession.getCurrent().getSession().getAttribute("TENANT");
            final Set<String> roles = (Set<String>) VaadinSession.getCurrent().getSession().getAttribute("ROLES");
            

            scriptEngine.put("current", work);
            if (work != null) {
                scriptEngine.put("currentClass", work.getClass());
            }
            scriptEngine.put("tenant", tenant);
            scriptEngine.put("userIdentifier", userIdentifier);
            scriptEngine.put("user", user);
            scriptEngine.put("roles", roles);
            scriptEngine.put("dao", dao);
            scriptEngine.put("workflow", workflow);
            scriptEngine.put("dataQuery", SpringBeanFactory.create(DataQuery.class));
            Map<String, Class> REF = new HashMap<>();
            EntityUtils entityUtils = SpringBeanFactory.create(EntityUtils.class);
            for (Class c : entityUtils.getAllEntities(emf)) {
                if (entityUtils.getEntityType(c) == WebEntityType.REF) {
                    REF.put(c.getSimpleName(), c);
                }
            }
            scriptEngine.put("REF",REF);

            scriptEngine.eval(script);
        } catch (ScriptException ex) {
            Logger.getLogger(WorkflowImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
