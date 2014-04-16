/*******************************************************************************
 * Copyright 2013 The California Health and Human Services Agency (CHHS). All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"), you may not use this file except in compliance with the License. You may obtain a copy of the License at: http://www.apache.org/licenses/LICENSE-2.0.
 * Unless required by applicable law or agreed to in writing, content (including but not limited to software, documentation, information, and all other works distributed under the License) is distributed on an "AS IS" BASIS, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE CONTENT OR THE USE OR OTHER DEALINGS IN THE CONTENT. IN NO EVENT SHALL CHHS HAVE ANY OBLIGATION TO PROVIDE SUPPORT, UPDATES, MODIFICATIONS, AND/OR UPGRADES FOR CONTENT. See the License for the specific language governing permissions and limitations under the License.
 * This publication/product was made possible by Award Number 90HT0029 from Office of the National Coordinator for Health Information Technology (ONC), U.S. Department of Health and Human Services. Its contents are solely the responsibility of the authors and do not necessarily represent the official views of ONC or the State of California.
 ******************************************************************************/
/**
 * 
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
import gov.hhs.fha.nhinc.admingui.user.bo.UserBo;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

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

    @Autowired
    private UserBo userBo;

    public void setUserBo(UserBo userBo) {
        this.userBo = userBo;
    }

    public String printMsgFromSpring() {
        return userBo.getMessage();
    }

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
            return "Dashboard";
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
     * Gets the logged in user.
     * 
     * @return the logged in user
     */
    /*public String getLoggedInUser() {
        String userName = null;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        Login login = (Login) session.getAttribute(UserAuthorizationListener.USER_INFO_SESSION_ATTRIBUTE);
        if (login != null) {
            userName = login.getUserName();
        }
        return userName;
    }*/

    /**
     * Login.
     * 
     * @return true, if successful
     */
    private boolean login() {
        boolean loggedIn = false;
        Login login = new Login(userName, password);
        try {
            loggedIn = loginService.login(login);

            if (loggedIn) {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
                session.setAttribute(UserAuthorizationListener.USER_INFO_SESSION_ATTRIBUTE, login);
            }
        } catch (UserLoginException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return loggedIn;
    }
}
