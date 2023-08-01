/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.user;

import com.azrul.langkuik.framework.entity.Element;
import com.azrul.langkuik.framework.entity.WebEntity;
import com.azrul.langkuik.framework.field.WebField;
import com.azrul.langkuik.framework.entity.WebEntityType;
import com.azrul.langkuik.framework.entity.WorkElement;
import com.azrul.langkuik.framework.field.FieldVisibility;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import org.hibernate.annotations.Index;

/**
 *
 * @author azrul
 */
@Entity
@DiscriminatorValue("_UserProfile")
@WebEntity(name = "UserProfile", type=WebEntityType.ROOT,fieldVisibility =  {
        @FieldVisibility(fieldName="priority", visibleInForm = "false", visibleInTable = "false"),
        @FieldVisibility(fieldName="creator", visibleInForm = "false", visibleInTable = "false"),
        @FieldVisibility(fieldName="workflowInfo", visibleInTable = "false")
    })
public class UserProfile  extends WorkElement{
    
    @Index(name="userprofile_name_idx")
    @WebField(displayName="User name",order=100,visibleInForm = "true",visibleInTable = "false", isReadOnly = true)
    @User
    private String userIdentifier;
    
//    @WebField(displayName="Login name",order=200,visibleInForm = "true",visibleInTable = "false", isReadOnly = true)
    private String loginName;
    
    @WebField(displayName="First name",order=300,visibleInForm = "true",visibleInTable = "false", isReadOnly = true)
    private String firstName;
    
    @WebField(displayName="Last name",order=400,visibleInForm = "true",visibleInTable = "false", isReadOnly = true)
    private String lastName;
    
    @WebField(displayName="Email",order=500,visibleInForm = "true",visibleInTable = "false", isReadOnly = true)
    private String email;
    
    @WebField(displayName="Roles",order=600,visibleInForm = "true",visibleInTable = "false", isReadOnly = true)
    private String roles;
    
   
    
    private String profilePicURL;
    
    @Transient
    private Boolean enabled;
    
    
    @Transient
    private Map<String,String> otherAttributes;

    /**
     * @return the loginName
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * @param username the loginName to set
     */
    public void setLoginName(String username) {
        this.loginName = username;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the roles
     */
    public String getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(String roles) {
        this.roles = roles;
    }

    /**
     * @return the profilePicURL
     */
    public String getProfilePicURL() {
        return profilePicURL;
    }

    /**
     * @param profilePicURL the profilePicURL to set
     */
    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }

    /**
     * @return the userIdentifier
     */
    public String getUserIdentifier() {
        return userIdentifier;
    }

    /**
     * @param userIdentifier the userIdentifier to set
     */
    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    /**
     * @return the otherAttributes
     */
    public Map getOtherAttributes() {
        if (otherAttributes==null){
            otherAttributes = new HashMap<>();
        }
        return otherAttributes;
    }

    /**
     * @param otherAttributes the otherAttributes to set
     */
    public void setOtherAttributes(Map otherAttributes) {
        this.otherAttributes = otherAttributes;
    }

    /**
     * @return the enabled
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    
    
     
}
