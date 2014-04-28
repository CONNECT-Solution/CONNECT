/*
 *   /* 
 *    * Copyright (c) 2009-2014, United States Government, as represented by the Secretary of Health and Human Services.
 *    * All rights reserved. 
 *    *
 *    * Redistribution and use in source and binary forms, with or without  
 *    * modification, are permitted provided that the following conditions are met:  
 *    *     * Redistributions of source code must retain the above  
 *    *       copyright notice, this list of conditions and the following disclaimer. 
 *    *     * Redistributions in binary form must reproduce the above copyright  
 *    *       notice, this list of conditions and the following disclaimer in the documentation  
 *    *       and/or other materials provided with the distribution.  
 *    *     * Neither the name of the United States Government nor the  
 *    *       names of its contributors may be used to endorse or promote products  
 *    *       derived from this software without specific prior written permission.  
 *    * 
 *    * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND  
 *    * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED  
 *    * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  
 *    * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY  
 *    * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  
 *    * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  
 *    * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND  
 *    * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  
 *    * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  
 *    * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *    */
 

package gov.hhs.fha.nhinc.admingui.managed;
import gov.hhs.fha.nhinc.admingui.jee.jsf.UserAuthorizationListener;
import gov.hhs.fha.nhinc.admingui.model.Login;
import gov.hhs.fha.nhinc.admingui.services.LoginService;
import gov.hhs.fha.nhinc.admingui.services.exception.UserLoginException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
 
/**
 *
 * @author sadusumilli
 */
@ManagedBean(name = "createuserBean")
@SessionScoped
@Component
public class CreateuserBean {

    private String userName;
    private String password;
    private String role;
    public Boolean isCreated = false;
    
    /** The login service. */
    @Autowired
    private LoginService loginService;
    
    /**
     * 
     * @return string which will navigate to relative view 
     */
    public String addCreateUser() {        
        if (createUser()) {
            this.isCreated = true;
            return "Login";
        } else {
            this.isCreated = false;
            return "failed for create user";
        }

    }
    /**
     * 
     * @return boolean flag value
     */
    public boolean createUser() {
        boolean createdUser = false;
        if (this.getUserName() != null && this.getPassword() != null) {
        }
        Login user = new Login(userName, password);
        try {
            createdUser = loginService.addUser(user);
            if (createdUser) {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
                session.setAttribute(UserAuthorizationListener.USER_INFO_SESSION_ATTRIBUTE, user);
            }
        } catch (UserLoginException e) {
            e.printStackTrace();
        }
        return createdUser;
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
}
