/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.user.keycloak;

import com.azrul.langkuik.framework.user.UserIdentifierLookup;
import com.azrul.langkuik.framework.user.UserProfile;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 *
 * @author azrul
 */
@Service
public class KeycloakUserIdentifierLookup  implements UserIdentifierLookup {


    public Optional<String> lookup(Map keycloakUserMap) {
       return Optional.ofNullable((String)keycloakUserMap.get("username"));
    }


    public Optional<String> lookup(UserProfile user) {
       return Optional.ofNullable((String)user.getLoginName());
    }
    
}
