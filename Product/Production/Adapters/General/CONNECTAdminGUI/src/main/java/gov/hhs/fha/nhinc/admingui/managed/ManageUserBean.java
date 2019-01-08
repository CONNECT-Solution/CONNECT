/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package gov.hhs.fha.nhinc.admingui.managed;

import gov.hhs.fha.nhinc.admingui.constant.NavigationConstant;
import gov.hhs.fha.nhinc.admingui.jee.jsf.UserAuthorizationListener;
import gov.hhs.fha.nhinc.admingui.model.Login;
import gov.hhs.fha.nhinc.admingui.services.LoginService;
import gov.hhs.fha.nhinc.admingui.services.exception.UserLoginException;
import gov.hhs.fha.nhinc.admingui.services.persistence.jpa.entity.UserLogin;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author sadusumilli
 */
@ManagedBean(name = "manageUserBean")
@SessionScoped
@Component
public class ManageUserBean {

    private static final Logger LOG = LoggerFactory.getLogger(ManageUserBean.class);
    private String userName = null;
    private String password = null;
    private String role;
    private String firstName;
    private String middleName;
    private String lastName;
    private String transactionRoleDesc;


    private UserLogin selectedUser;

    private List<UserLogin> users = new ArrayList<>();
    private Properties userRoleProperties;
    private List<String> userRoleList;

    /**
     * The login service.
     */
    @Autowired
    private LoginService loginService;

    
    /**
     * default constructor
     */
    public ManageUserBean() {
        
    }
    
    /**
     *
     * @param loginservice
     */
    ManageUserBean(LoginService loginservice) {
        loginService = loginservice;
    }
    
    @PostConstruct
    public void buildUserRoleList() {
        try {
            userRoleProperties = loginService.getUserRoleList();
            userRoleList = new ArrayList<>();
            for (Iterator<Object> it = userRoleProperties.keySet().iterator(); it.hasNext();) {
                String key = (String) it.next();
                userRoleList.add(key.replace("_", " "));
            }
            
        } catch (PropertyAccessException ex) {
            LOG.warn("Unable to access properties for Role Code values.", ex.getLocalizedMessage(), ex);
        }
    }

    /**
     *
     * @return string which will navigate to relative view
     */
    public String addCreateUser() {
        if (createUser()) {
            return NavigationConstant.ACCT_MGMT_PAGE;
        } else {
            return "failed for create user";
        }
    }

    /**
     *
     * @return boolean representing whether or not user creation was successful
     */
    public boolean createUser() {
        boolean createdUser = false;
        Login user = new Login(userName, password);
        try {
            UserLogin userLogin = loginService.addUser(user, Long.parseLong(role), firstName,
                    middleName, lastName, transactionRoleDesc);
            if (userLogin != null) {
                createdUser = true;
            }
        } catch (UserLoginException e) {
            FacesContext.getCurrentInstance().validationFailed();
            FacesContext.getCurrentInstance().addMessage("userAddErrors",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Can not add user: " + e.getLocalizedMessage(), ""));
            LOG.error("Error creating user: {}", e.getLocalizedMessage(), e);
        }
        userName = null;
        password = null;
        firstName = null;
        middleName = null;
        lastName = null;
        transactionRoleDesc = null;
        
        role = "1";
        return createdUser;
    }

    /**
     *
     * @return
     */
    protected HttpSession getHttpSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTransactionRoleDesc() {
        return transactionRoleDesc;
    }

    public void setTransactionRoleDesc(String transactionRoleDesc) {
        this.transactionRoleDesc = transactionRoleDesc;
    }

    public List<String> getUserRoleList() {
        return userRoleList;
    }
    
    public UserLogin getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(UserLogin selectedUser) {
        this.selectedUser = selectedUser;
    }

    public List<UserLogin> getUsers() {
        return loginService.getAllUsers();
    }

    public void deleteUser(ActionEvent event) {
        if (selectedUser != null) {
            try {
                loginService.deleteUser(selectedUser);
            } catch (UserLoginException ex) {
                FacesContext.getCurrentInstance().addMessage("userDeleteMessages",
                        new FacesMessage(FacesMessage.SEVERITY_WARN, ex.getLocalizedMessage(), ""));
                LOG.error("Error deleting user: {}", ex.getLocalizedMessage(), ex);
            }
        }
    }

    /**
     * Returns the user name in the current session.
     *
     * @return
     */
    public String getCurrentUser() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(true);
        UserLogin currentUser = (UserLogin) session.getAttribute(UserAuthorizationListener.USER_INFO_SESSION_ATTRIBUTE);
        return currentUser.getUserName();
    }
}
