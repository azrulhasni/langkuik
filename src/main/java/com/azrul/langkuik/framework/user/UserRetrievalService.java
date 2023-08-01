/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.user;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.azrul.langkuik.framework.dao.DataAccessObject;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author azrul
 */
@Service
public class UserRetrievalService {
    
    @Value("${keycloak.auth-server-url}")
    private String keycloakServerUrl;
    
    @Value("${application.keycloak-id-of-client}")
    private String idOfClient;
    
    @Value("${keycloak.realm}")
    private String realm;

    @Autowired
    private KeycloakRestTemplate keycloakRestTemplate;
    
    @Autowired
    private DataAccessObject dao;
    
    @Autowired
    private UserIdentifierLookup userIdentifierLookup;

    public List<UserProfile> getUsersByRole(String role) {
        List<UserProfile> users = new ArrayList<>();
        try {
            Map[] userMaps= keycloakRestTemplate.getForEntity(URI.create(keycloakServerUrl + "/admin/realms/"+realm+"/clients/"+idOfClient+"/roles/"+URLEncoder.encode(role,"UTF-8")+"/users"), Map[].class)
                    .getBody();
            for (Map userMap:userMaps){
                users.add(userFrom(userMap));
            }
            return users;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(UserRetrievalService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return List.of();
    }
    
    public UserProfile getUserByLoginName(String loginName) {
       Map[] users = keycloakRestTemplate.getForEntity(URI.create(keycloakServerUrl + "/admin/realms/"+realm+"/users?username="+loginName), Map[].class)
                .getBody();
       return userFrom(users[0]); //since we are looking up by username, we can guarantee the return is unique
        //return Arrays.asList(usersStr);
    }
    
    private UserProfile userFrom(Map userMap){
        UserProfile u = new UserProfile();
        u.setEmail((String) userMap.get("email"));
        userIdentifierLookup.lookup(userMap).ifPresent(username->{
            u.setLoginName(username);
        });
        u.setFirstName((String) userMap.get("firstName"));
        u.setLastName((String) userMap.get("lastName"));
        u.setEnabled((Boolean)userMap.get("enabled"));
        for (Object o : userMap.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            if (entry.getValue().getClass().equals(LinkedHashMap.class)) {
               u.getOtherAttributes().putAll((Map)entry.getValue());
            }
        }
        userIdentifierLookup.lookup(userMap).ifPresent(userId->{
            u.setUserIdentifier(userId);
        });
        return u;
    }
    
    
    

}
