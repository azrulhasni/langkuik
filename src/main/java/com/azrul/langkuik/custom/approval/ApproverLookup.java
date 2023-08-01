/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.custom.approval;

import com.azrul.langkuik.framework.user.UserProfile;
import java.util.Optional;

/**
 *
 * @author azrul
 */
public interface ApproverLookup<T> {
    Optional<UserProfile> lookupApprover(T work, String username);
}
