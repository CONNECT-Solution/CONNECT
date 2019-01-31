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
package gov.hhs.fha.nhinc.admingui.hibernate;

import gov.hhs.fha.nhinc.admingui.hibernate.dao.UserLoginDAO;
import gov.hhs.fha.nhinc.admingui.jee.jsf.UserAuthorizationListener;
import gov.hhs.fha.nhinc.admingui.model.Login;
import gov.hhs.fha.nhinc.admingui.services.LoginService;
import gov.hhs.fha.nhinc.admingui.services.PasswordService;
import gov.hhs.fha.nhinc.admingui.services.exception.UserLoginException;
import gov.hhs.fha.nhinc.admingui.services.persistence.jpa.entity.UserLogin;
import gov.hhs.fha.nhinc.admingui.services.persistence.jpa.entity.UserRole;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.util.SHA2PasswordUtil;
import gov.hhs.fha.nhinc.util.UtilException;
import java.util.List;
import java.util.Properties;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author msw
 *
 */
@Service
public class LoginServiceImpl implements LoginService {

    public static final String CONNECT_ADMIN_USER = "CONNECTAdmin";
    private static final String ROLE_PROPERTIES_FILENAME = "roleCodes";

    @Autowired
    private UserLoginDAO userLoginDAO;
    /**
     * The credential service.
     */
    @Autowired
    private PasswordService credentialService;

    /**
     * Default constructor
     */
    public LoginServiceImpl() {
        //Spring can not instantiate without default constructor
    }


    /**
     * Gets the current JPA Entity from the database if the user was successfully authenticated.
     *
     * Returns null if the credentials are invalid, or not provided.
     */
    @Override
    public UserLogin login(Login login) {

        // Credentials not provided
        if (login == null || StringUtils.isBlank(login.getPassword()) || StringUtils.isBlank(login.getUserName())) {
            return null;
        }

        UserLogin user = userLoginDAO.login(login);

        // User not found, or password was not set in the database
        if (user == null || StringUtils.isBlank(user.getSha2()) || StringUtils.isBlank(user.getSalt())) {
            return null;
        }

        // Passwords match
        boolean loggedIn = SHA2PasswordUtil.checkPassword(user.getSha2().getBytes(),
            login.getPassword().getBytes(), user.getSalt().getBytes());

        if (!loggedIn) {
            user = null;
        }

        return user;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.services.LoginService#addUser(gov.hhs.fha.nhinc.admingui.model.User)
     */
    @Override
    public UserLogin addUser(Login user, long role, String firstName, String middleName,
            String lastName, String transRoleDesc) throws UserLoginException {
        boolean isCreateUser;
        String passwordHash;
        byte[] saltValue;
        try {
            saltValue = credentialService.generateRandomSalt();
            passwordHash = new String(SHA2PasswordUtil.calculateHash(saltValue, user.getPassword().getBytes()));

        } catch (UtilException e) {
            throw new UserLoginException("Error while calculating hash.", e);
        }

        UserLogin userLoginEntity = new UserLogin();
        userLoginEntity.setUserName(user.getUserName());
        userLoginEntity.setSha2(passwordHash);
        userLoginEntity.setSalt(new String(saltValue));
        userLoginEntity.setFirstName(firstName);
        userLoginEntity.setMiddleName(middleName);
        userLoginEntity.setLastName(lastName);
        userLoginEntity.setTransactionRole(getUserRoleCode(transRoleDesc));
        userLoginEntity.setTransactionRoleDesc(transRoleDesc);

        UserRole userRole = getUserRole(role);

        if (userRole != null) {
            userRole.addLogin(userLoginEntity);
        }

        userLoginEntity.setUserRole(userRole);
        isCreateUser = userLoginDAO.createUser(userLoginEntity);

        if (isCreateUser) {
            return userLoginEntity;
        } else {
            return null;
        }
    }

    @Override
    public List<UserLogin> getAllUsers() {
        return userLoginDAO.getAllUsers();
    }

    @Override
    public void deleteUser(UserLogin user) throws UserLoginException {
        UserLogin currentUser = getCurrentUser();
        if (user.getUserName().equals(CONNECT_ADMIN_USER)) {
            throw new UserLoginException("Unable to delete " + CONNECT_ADMIN_USER + " user.");
        } else if (currentUser != null && user.getUserName().equals(currentUser.getUserName())) {
            throw new UserLoginException("Unable to delete current user: " + user.getUserName());
        } else {
            userLoginDAO.deleteUser(user);
        }
    }

    private UserRole getUserRole(long role) {
        return userLoginDAO.getRole(role);
    }

    private static UserLogin getCurrentUser() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        return (UserLogin) session.getAttribute(UserAuthorizationListener.USER_INFO_SESSION_ATTRIBUTE);
    }

    private String getUserRoleCode(String roleDesc) {
        String roleDescNoSpaces = roleDesc.replaceAll(" ", "_");
        return getPropAccessor().getProperty(ROLE_PROPERTIES_FILENAME, roleDescNoSpaces, null);
    }

    @Override
    public Properties getUserRoleList() throws PropertyAccessException {
        getPropAccessor().setPropertyFile(ROLE_PROPERTIES_FILENAME);
        return getPropAccessor().getProperties(ROLE_PROPERTIES_FILENAME);
    }

    protected PropertyAccessor getPropAccessor() {
        return PropertyAccessor.getInstance();
    }
}
