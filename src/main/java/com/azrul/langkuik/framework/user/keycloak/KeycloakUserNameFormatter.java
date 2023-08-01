/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.user.keycloak;

import com.azrul.langkuik.framework.user.UserNameFormatter;
import org.springframework.stereotype.Service;

/**
 *
 * @author azrul
 */
@Service
public class KeycloakUserNameFormatter implements UserNameFormatter {

    @Override
    public String format(String userIdentifier) {
        return userIdentifier;
    }
    
}
