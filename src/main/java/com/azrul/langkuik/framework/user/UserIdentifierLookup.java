/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.user;

import java.util.Map;
import java.util.Optional;

/**
 *
 * @author azrul
 */
public interface UserIdentifierLookup {
    Optional<String> lookup(Map keycloakUserMap);
    Optional<String> lookup(UserProfile user);
}
