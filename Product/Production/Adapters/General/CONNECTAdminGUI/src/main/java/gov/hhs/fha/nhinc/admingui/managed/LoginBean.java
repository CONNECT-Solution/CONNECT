/*
 *  Copyright (c) 2009-2014, United States Government, as represented by the Secretary of Health and Human Services.
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *      * Redistributions of source code must retain the above
 *        copyright notice, this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the documentation
 *        and/or other materials provided with the distribution.
 *      * Neither the name of the United States Government nor the
 *        names of its contributors may be used to endorse or promote products
 *        derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 *  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.admingui.managed;

/*import gov.hhs.fha.nhinc.admingui.jee.jsf.UserAuthorizationListener;
import gov.hhs.fha.nhinc.admingui.model.Login;
import gov.hhs.fha.nhinc.admingui.services.LoginService;
import gov.hhs.fha.nhinc.admingui.services.exception.UserLoginException;*/

import gov.hhs.fha.nhinc.admingui.jee.jsf.UserAuthorizationListener;
import gov.hhs.fha.nhinc.admingui.model.Login;
import gov.hhs.fha.nhinc.admingui.services.LoginService;
import gov.hhs.fha.nhinc.admingui.services.exception.UserLoginException;
import gov.hhs.fha.nhinc.admingui.services.persistence.jpa.entity.UserLogin;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The Class LoginBean.
 * 
 * @author sadusumilli
 */
@ManagedBean
@SessionScoped
@Component
public class LoginBean {

    /** The user name. */
    private String userName;

    /** The password. */
    private String password;

    /** The is correct. */
    public Boolean isCorrect = false;

    /** The msg. */
    private FacesMessage msg;

    /** The login service. */
    @Autowired
    private LoginService loginService;
    
    private static final Logger LOG = Logger.getLogger(LoginBean.class);

    /**
     * Gets the msg.
     * 
     * @return the msg
     */
    public FacesMessage getMsg() {
        return msg;
    }

    /**
     * Sets the msg.
     * 
     * @param msg the new msg
     */
    public void setMsg(FacesMessage msg) {
        this.msg = msg;
    }

    /**
     * Gets the user name.
     * 
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user name.
     * 
     * @param userName the new user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets the password.
     * 
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     * 
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Instantiates a new login bean.
     */
    public LoginBean() {
        // Injector injector = Guice.createInjector(new LoginServiceModule());

        // loginService = injector.getInstance(LoginService.class);
    }

    /**
     * New message.
     * 
     * @return the string
     */
    public String newMessage() {
        return "It is from managed bean call";
    }

    /**
     * Invoke patient.
     * 
     * @return the string
     */
    public String invokePatient() {
        System.out.println("Login user details from NewFile page" + userName);
        System.out.println("Login password details from NewFile page" + password);
        if (login()) {
            this.isCorrect = true;
            //return "PatientSearch";
            return "status";
        } else {
            this.isCorrect = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "User details are not valid...!!!", userName);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "Login";
        }

    }

    /**
     * Logout.
     * 
     * @return the string
     */
    public String logout() {
        userName = null;
        password = null;

        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "Login";
    }

    /**
     * Login.
     * 
     * @return true, if successful
     */
    private boolean login() {
        boolean loggedIn = false;
        Login login = new Login(userName, password);
        try {
            UserLogin user = loginService.login(login);

            if (user != null) {
                loggedIn = true;
                FacesContext facesContext = FacesContext.getCurrentInstance();
                HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
                session.setAttribute(UserAuthorizationListener.USER_INFO_SESSION_ATTRIBUTE, login);
            }
        } catch (UserLoginException e) {
            LOG.error(e, e);
        }
        return loggedIn;
    }
}
