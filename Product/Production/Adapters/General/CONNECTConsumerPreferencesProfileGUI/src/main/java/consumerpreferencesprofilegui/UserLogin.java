/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package consumerpreferencesprofilegui;

import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.webui.jsf.component.Button;
import com.sun.webui.jsf.component.Page;
import com.sun.webui.jsf.component.PasswordField;
import com.sun.webui.jsf.component.StaticText;
import com.sun.webui.jsf.component.TextField;
import gov.hhs.fha.nhinc.adapter.cppgui.UserSession;
import gov.hhs.fha.nhinc.adapterauthentication.proxy.AdapterAuthenticationProxy;
import gov.hhs.fha.nhinc.adapterauthentication.proxy.AdapterAuthenticationProxyObjectFactory;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AuthenticateUserRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AuthenticateUserResponseType;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.io.IOException;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>Page bean that corresponds to a similarly named JSP page.  This
 * class contains component definitions (and initialization code) for
 * all components that you have defined on this page, as well as
 * lifecycle methods and event handlers where you may add behavior
 * to respond to incoming events.</p>
 *
 * @version UserLogin.java
 * @version Created on Sep 10, 2009, 10:29:56 PM
 * @author patlollav
 */

public class UserLogin extends AbstractPageBean {
    // <editor-fold defaultstate="collapsed" desc="Managed Component Definition">

    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
    }

    private static final String PROPERTY_FILE_NAME_ADAPTER = "adapter";
    private static final String PROPERTY_FILE_KEY_AGENCY = "AgencyName";
    private static Log log = LogFactory.getLog(UserLogin.class);

    // </editor-fold>

    /**
     * <p>Construct a new Page bean instance.</p>
     */
    public UserLogin() {
    }

    private Page userLoginPage = new Page();

    public Page getUserLoginPage() {
        return userLoginPage;
    }

    public void setUserLoginPage(Page userLoginPage) {
        this.userLoginPage = userLoginPage;
    }

    private TextField userName = new TextField();
    private PasswordField password = new PasswordField();
    private Button loginButton = new Button();
    private StaticText verifyMsg = new StaticText();
    private StaticText agencyLogo = new StaticText();
    
    /**
     * <p>Callback method that is called whenever a page is navigated to,
     * either directly via a URL, or indirectly via page navigation.
     * Customize this method to acquire resources that will be needed
     * for event handlers and lifecycle methods, whether or not this
     * page is performing post back processing.</p>
     * 
     * <p>Note that, if the current request is a postback, the property
     * values of the components do <strong>not</strong> represent any
     * values submitted with this request.  Instead, they represent the
     * property values that were saved for this view when it was rendered.</p>
     */
    @Override
    public void init() {
        // Perform initializations inherited from our superclass
        super.init();
        // Perform application initialization that must complete
        // *before* managed components are initialized
        // TODO - add your own initialiation code here
        
        // <editor-fold defaultstate="collapsed" desc="Managed Component Initialization">
        // Initialize automatically managed components
        // *Note* - this logic should NOT be modified
        try {
            _init();
        } catch (Exception e) {
            log("UserLogin Initialization Failure", e);
            throw e instanceof FacesException ? (FacesException) e: new FacesException(e);
        }
        
        // </editor-fold>
        // Perform application initialization that must complete
        // *after* managed components are initialized
        // TODO - add your own initialization code here
    }

    /**
     * <p>Callback method that is called after the component tree has been
     * restored, but before any event processing takes place.  This method
     * will <strong>only</strong> be called on a postback request that
     * is processing a form submit.  Customize this method to allocate
     * resources that will be required in your event handlers.</p>
     */
    @Override
    public void preprocess() {
    }

    /**
     * <p>Callback method that is called just before rendering takes place.
     * This method will <strong>only</strong> be called for the page that
     * will actually be rendered (and not, for example, on a page that
     * handled a postback and then navigated to a different page).  Customize
     * this method to allocate resources that will be required for rendering
     * this page.</p>
     */
    @Override
    public void prerender() {
                // If no authentication service is available set up a default token and redirect to page2
        AdapterAuthenticationProxyObjectFactory factory = new AdapterAuthenticationProxyObjectFactory();
        AdapterAuthenticationProxy adapterAuthenticationProxy = factory.getAdapterAuthenticationProxy();
        AuthenticateUserRequestType authRequest = new AuthenticateUserRequestType();
        authRequest.setUserName("Default");
        authRequest.setPassword("Default");
        AuthenticateUserResponseType authResp = adapterAuthenticationProxy.authenticateUser(authRequest);
        log.debug("UserLogin.prerender Authentication Service " +  adapterAuthenticationProxy + " Avail: " + authResp.isIsAuthenticationAvailable());
        if (authResp != null && !authResp.isIsAuthenticationAvailable()) {
            try {
                getUserSession().setAuthToken("NoOpToken");
                FacesContext context = FacesContext.getCurrentInstance();
                context.getExternalContext().redirect("faces/SearchPatient.jsp");
            } catch (IOException ex) {
                log.error("CPP GUI can not prerender UserLogin: ",ex);
            }
        }

        try {
            String agencyName = PropertyAccessor.getProperty(PROPERTY_FILE_NAME_ADAPTER, PROPERTY_FILE_KEY_AGENCY);
            this.agencyLogo.setText(agencyName);
        } catch (PropertyAccessException ex) {
            log.error("CPP GUI can not access " + PROPERTY_FILE_KEY_AGENCY + " property: ", ex);
        }
    }

    /**
     * <p>Callback method that is called after rendering is completed for
     * this request, if <code>init()</code> was called (regardless of whether
     * or not this was the page that was actually rendered).  Customize this
     * method to release resources acquired in the <code>init()</code>,
     * <code>preprocess()</code>, or <code>prerender()</code> methods (or
     * acquired during execution of an event handler).</p>
     */
    @Override
    public void destroy() {
    }

        public String loginButton_action() {

        String loginDecision = "login_required";
        String name = (String) userName.getText();
        String pass = (String) password.getText();

        AdapterAuthenticationProxyObjectFactory factory = new AdapterAuthenticationProxyObjectFactory();
        AdapterAuthenticationProxy adapterAuthenticationProxy = factory.getAdapterAuthenticationProxy();
        AuthenticateUserRequestType authRequest = new AuthenticateUserRequestType();
        authRequest.setUserName(name);
        authRequest.setPassword(pass);
        AuthenticateUserResponseType authResp = adapterAuthenticationProxy.authenticateUser(authRequest);

        if (authResp != null && authResp.isIsAuthenticationAvailable()) {
            String authToken = authResp.getAuthenticationToken();
            if (!authToken.isEmpty()) {
                loginDecision = "login_success";
                getUserSession().setAuthToken(authToken);
            } else {
                verifyMsg.setText("User Name: " + name + " is unknown, or password is invalid. Please try again.");
            }
        } else {
            verifyMsg.setText("User authentication service is unavailable.");
        }
        return loginDecision;
    }

    public StaticText getAgencyLogo() {
        return agencyLogo;
    }

    public void setAgencyLogo(StaticText agencyLogo) {
        this.agencyLogo = agencyLogo;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public void setLoginButton(Button loginButton) {
        this.loginButton = loginButton;
    }

    public PasswordField getPassword() {
        return password;
    }

    public void setPassword(PasswordField password) {
        this.password = password;
    }

    public TextField getUserName() {
        return userName;
    }

    public void setUserName(TextField userName) {
        this.userName = userName;
    }

    public StaticText getVerifyMsg() {
        return verifyMsg;
    }

    public void setVerifyMsg(StaticText verifyMsg) {
        this.verifyMsg = verifyMsg;
    }
    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected UserSession getUserSession() {
        return (UserSession) getBean("UserSession");
    }

}

