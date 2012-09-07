/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 *
 * All other rights reserved.
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

package universalclientgui;

//opensso imports
import com.sun.identity.idsvcs.opensso.IdentityServicesImpl;
import com.sun.identity.idsvcs.opensso.IdentityServicesImplService;
import com.sun.identity.idsvcs.opensso.Token;
import com.sun.identity.idsvcs.opensso.UserDetails;
import com.sun.identity.idsvcs.opensso.Attribute;

import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.webui.jsf.component.Button;
import com.sun.webui.jsf.component.Page;
import com.sun.webui.jsf.component.PasswordField;
import com.sun.webui.jsf.component.StaticText;
import com.sun.webui.jsf.component.TextField;
import gov.hhs.fha.nhinc.adapterauthentication.proxy.AdapterAuthenticationProxy;
import gov.hhs.fha.nhinc.adapterauthentication.proxy.AdapterAuthenticationProxyObjectFactory;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AuthenticateUserRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AuthenticateUserResponseType;

import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.io.IOException;
import java.util.HashMap;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import com.sun.webui.jsf.component.HiddenField;

import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>Page bean that corresponds to a similarly named JSP page.  This
 * class contains component definitions (and initialization code) for
 * all components that you have defined on this page, as well as
 * lifecycle methods and event handlers where you may add behavior
 * to respond to incoming events.</p>
 *
 * @version Page1.java
 * @version Created on Aug 7, 2009, 5:38:42 PM
 * @author vvickers
 */
public class Page1 extends AbstractPageBean {
   
    //declare variables
    private static final String PROPERTY_FILE_NAME_UNIVERSAL_CLIENT = "universalClient";
    private static final String PROPERTY_FILE_KEY_AGENCY = "AgencyName";
    private static final String PROPERTY_FILE_KEY_DISCLAIMER = "DisclaimerText";
    private static final String PROPERTY_FILE_KEY_ENCRYPTION = "EncryptionType";
    private boolean isPostBack = false;
    private static Log log = LogFactory.getLog(Page1.class);

    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
    }
    private Page page1 = new Page();

    public Page getPage1() {
        return page1;
    }

    public void setPage1(Page p) {
        this.page1 = p;
    }
    private TextField nameField = new TextField();

    public TextField getNameField() {
        return nameField;
    }

    public void setNameField(TextField tf) {
        this.nameField = tf;
    }
    private PasswordField passField = new PasswordField();

    public PasswordField getPassField() {
        return passField;
    }

    public void setPassField(PasswordField pf) {
        this.passField = pf;
    }
    private Button loginButton = new Button();

    public Button getLoginButton() {
        return loginButton;
    }

    public void setLoginButton(Button b) {
        this.loginButton = b;
    }
    private StaticText verifyMsg = new StaticText();

    public StaticText getVerifyMsg() {
        return verifyMsg;
    }

    public void setVerifyMsg(StaticText st) {
        this.verifyMsg = st;
    }

    //Agency logo
    private StaticText agencyLogo = new StaticText();

    public StaticText getAgencyLogo() {

        try
        {
            //get agency Name property
            String agencyName = PropertyAccessor.getInstance().getProperty(PROPERTY_FILE_NAME_UNIVERSAL_CLIENT, PROPERTY_FILE_KEY_AGENCY);
            log.info("Setting agency name: " + agencyName);
            this.agencyLogo.setText(agencyName);
        }
        catch (PropertyAccessException ex) {
            log.error("Universal Client can not access " + PROPERTY_FILE_KEY_AGENCY + " property: " + ex.getMessage());
        }

        return agencyLogo;
    }

    public void setAgencyLogo(StaticText st) {
        this.agencyLogo = st;
    }

    //disclaimer Info
    private StaticText disclaimerInfo = new StaticText();

    public StaticText getDisclaimerInfo() {

        try
        {
            //get disclaimer property
            String disclaimerText = PropertyAccessor.getInstance().getProperty(PROPERTY_FILE_NAME_UNIVERSAL_CLIENT, PROPERTY_FILE_KEY_DISCLAIMER);
            log.info("Setting disclaimer text: " + disclaimerText);
            this.disclaimerInfo.setText(disclaimerText);

        } catch (PropertyAccessException ex) {
            log.error("Universal Client can not access " + PROPERTY_FILE_KEY_AGENCY + " property: " + ex.getMessage());
        }

        return disclaimerInfo;
    }

    public void setDisclaimerInfo(StaticText st) {
        this.disclaimerInfo = st;
    }

    //hidden EID form field
    public HiddenField hideEIDField = new HiddenField();

    public HiddenField getHideEIDField()
    {
        return hideEIDField;
    }
    public void setHideEIDField(HiddenField pHideEIDField)
    {
        hideEIDField = pHideEIDField;
    }

    // </editor-fold>
    /**
     * <p>Construct a new Page bean instance.</p>
     */
    public Page1() {
     }

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
            log("Page1 Initialization Failure", e);
            throw e instanceof FacesException ? (FacesException) e : new FacesException(e);
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
        isPostBack = true; //flag indicating post back has occured
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

        //Session check - see if the user has an existing session from failing to logout
        HttpSession session = (HttpSession)getExternalContext().getSession(false);

        if (session != null)
        {
            //an existing session has been detected
            log.debug("A Session DOES exist for this browser. It was created on " + new java.util.Date(session.getCreationTime()));

            //check if user is posting back or entering the page from elsewhere
            if (isPostBack)
            {
                //user has failed to login
                log.debug("Postback = true. The user failed to login");
            }
            else
            {
                //user is coming from outside the app with existing sesison or from Page2 logout
                log.debug("Postback = false. The user is from Page2 or outside the GUI");

                //check if user is coming from Page2
                if (getRequestBean1().getCarePortalUserFlag() != null)
                {
                    //user hit the logout button on Page2
                    log.debug("An existing user, " + getRequestBean1().getCarePortalUserFlag() + ", has pressed the logout button - issue a new session");

                    //invalidate the old session
                    session.invalidate();

                    // Create a new session for the user.
                    session = (HttpSession)getExternalContext().getSession(true);

                    //check user type from Page2 logout action
                    if (!getRequestBean1().getCarePortalUserFlag().equals("standaloneUser"))
                    {
                        //care portal user - set GUI label to username
                        displayCarePortalView(getRequestBean1().getCarePortalUserFlag(),"");
                    }
                    else
                    {
                        //standalone user - dispaly standalone view
                        displayStandaloneView();
                    }
                }
                else
                {
                    //user is a new user with an existing session - we need to kill session and issue a new one
                    log.debug("An existing session (not from Page2) has been detected - issue new session");

                    //invalidate the old session
                    session.invalidate();

                    // Create a new session for the user.
                    session = (HttpSession)getExternalContext().getSession(true);

                    //check for care portal or stand alone user
                    checkGUIUserType();
                }
            }
        }
        else
        {
            //no existing session is present
            log.debug("A Session does NOT exist for this browser");
            log.debug("The user has entered the GUI for the first time w/new browser session");

            //check for care portal or stand alone user
            checkGUIUserType();
        }

        //The following no-op code is not used in production - as a valid username and password are required
   /*
            //there is currently no local token. Check for no-op implementation
            AdapterAuthenticationProxyObjectFactory factory = new AdapterAuthenticationProxyObjectFactory();
            AdapterAuthenticationProxy adapterAuthenticationProxy = factory.getAdapterAuthenticationProxy();
            AuthenticateUserRequestType authRequest = new AuthenticateUserRequestType();
            authRequest.setUserName("Default");
            authRequest.setPassword("Default");
            AuthenticateUserResponseType authResp = adapterAuthenticationProxy.authenticateUser(authRequest);
            log.debug("Page1.prerender Authentication Service " +  adapterAuthenticationProxy + " Avail: " + authResp.isIsAuthenticationAvailable() + " Token: " + authResp.getAuthenticationToken());

            if (authResp != null && !authResp.isIsAuthenticationAvailable() && authResp.getAuthenticationToken().isEmpty()) {
                try {
                    getSessionBean1().setAuthToken("NoOpToken");
                    FacesContext context = FacesContext.getCurrentInstance();
                    context.getExternalContext().redirect("faces/Page2.jsp");
                } catch (IOException ex) {
                    log.error("Universal Client can not prerender Page1: " + ex.getMessage());
                }
            }
  */
  
    }

    private void checkGUIUserType()
    {
        if((getExternalContext().getRequestParameterMap().get("chsuname")!=null && getExternalContext().getRequestParameterMap().get("chseid")!=null))
            {
                //care portal user
                log.debug("Care Portal User detected");

                //check for encrypted parameter values
                String tempUserName = getExternalContext().getRequestParameterMap().get("chsuname");
                String tempPatientId = getExternalContext().getRequestParameterMap().get("chseid");

                try
                {
                     String encryptionType = PropertyAccessor.getInstance().getProperty(PROPERTY_FILE_NAME_UNIVERSAL_CLIENT, PROPERTY_FILE_KEY_ENCRYPTION);

                     if(encryptionType!=null && encryptionType.equals("CHS"))//CHS encrypted url
                     {
                         tempUserName = decodeUserName(tempUserName);
                         tempPatientId = decodePatientId(tempPatientId);
                     }

                     //set care portal view 
                     displayCarePortalView(tempUserName, tempPatientId);

                 }
                 catch (PropertyAccessException ex)
                 {
                    log.error("Universal Client can not access " + PROPERTY_FILE_KEY_AGENCY + " property: " + ex.getMessage());
                 }
            }
            else
            {
                //stand alone user
                log.debug("Stand alone user detected");

                displayStandaloneView();
            }
    }

    private void displayCarePortalView(String pUserName, String pEIDVal)
    {
        log.debug("diplayCarePortalView method has been called... rendering Care Portal view");

        //set the username label text
        nameField.setText(pUserName);

        //set username field to read-only
        nameField.setReadOnly(true);

        //set hidden EID field
        hideEIDField.setText(pEIDVal);
    }

     private void displayStandaloneView()
    {
        log.debug("displayStandaloneView method has been called... rendering Standalone view");

        //set username field to read-only
        nameField.setReadOnly(false);

        //set hidden EID field
        hideEIDField.setText("standalone");
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

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected SessionBean1 getSessionBean1() {
        return (SessionBean1) getBean("SessionBean1");
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected RequestBean1 getRequestBean1() {
        return (RequestBean1) getBean("RequestBean1");
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected ApplicationBean1 getApplicationBean1() {
        return (ApplicationBean1) getBean("ApplicationBean1");
    }

    // Establish default SAML constants
    private static AuthenticatedUserInfo authenticationInfo = new AuthenticatedUserInfo();

    public static AuthenticatedUserInfo getAuthenticationInfo()
    {
        return authenticationInfo;
    }

    public String loginButton_action() {

        String loginDecision = "login_required";
        String name = "";
        String pass = "";

        log.debug("Username value submitted = " + nameField.getText());
        log.debug("EID value submitted = " + hideEIDField.getText());
        
        name = (String) nameField.getText();
        pass = (String) passField.getText();

        //Authenticate User
        AdapterAuthenticationProxyObjectFactory factory = new AdapterAuthenticationProxyObjectFactory();
        AdapterAuthenticationProxy adapterAuthenticationProxy = factory.getAdapterAuthenticationProxy();
        AuthenticateUserRequestType authRequest = new AuthenticateUserRequestType();
        authRequest.setUserName(name);
        authRequest.setPassword(pass);
        AuthenticateUserResponseType authResp = adapterAuthenticationProxy.authenticateUser(authRequest);

        //check authentication response
        if (authResp != null && authResp.isIsAuthenticationAvailable())
        {
            //grab the user's assigned token
            String authToken = authResp.getAuthenticationToken();

            //ensure token has valid value
            if (!authToken.isEmpty())
            {
                //the user has been successfully authenticated - but not authorized. Check authorization.
                log.debug("User: " + name + " has been successfully AUTHENTICATED");

                try
                {
                    //get OpenSSO Identity services
                    IdentityServicesImplService idService = new IdentityServicesImplService();
                    IdentityServicesImpl idPort = idService.getIdentityServicesImplPort();

                    //create subject object needed to call attributes endpoint
                    Token subject = new Token();
                    subject.setId(authToken);

                    //invoke the user attributes web service
                    UserDetails uDetails = idPort.attributes(null, subject);

                     if (uDetails != null)
                     {
                         //create a hashmap to hold each user attribute and the corresponding values.
                         HashMap attributesHash = new HashMap();

                         //loop through the results and fill the hashmap
                         for (Attribute uAttribute : uDetails.getAttributes())
                         {
                            //build out the HashMap with values - attributes can have multiple values
                            attributesHash.put(uAttribute.getName(), uAttribute.getValues());

                            log.debug("Retrieved User Attribute: " + uAttribute.getName());
                         }

                         //check businessCategory attribute for authorization
                         if (attributesHash.get("businesscategory").toString().contains("ucgui"))
                         {
                            log.debug("User has ucgui credentials and is permitted to enter GUI");

                            //user is allowed to view this GUI
                            loginDecision = "login_success";
                            getSessionBean1().setAuthToken(authToken);
                            isPostBack = false; //resetting post back flag since it's not needed for sucessful logins.

                            //add the dynamic user attributes hash to AuthenticatedUserInfo
                            authenticationInfo.setLocalUserData(attributesHash);
                            authenticationInfo.establishSAMLHeaderValues();

                         }
                         else
                         {
                            log.debug("User is NOT permitted to enter GUI");
                            verifyMsg.setText("User: " + name + " is known, but not authorized to use this application.");
                         }
                     }
                }
                catch(Exception ex)
                {
                    log.error("Exception during user authorization" + ex);
                }
            }
            else
            {
                log.debug("User: " + name + " has NOT been successfully AUTHENTICATED");
                verifyMsg.setText("User: " + name + " is unknown, or password is invalid. Try again.");
            }
        }
        else
        {
            verifyMsg.setText("User authentication service is unavailable.");
        }
        return loginDecision;
    }

    private String decodePatientId(String dPatientId) {
       CryptAHLTA cipherOBJ = new CryptAHLTA();
       String patientUnitNumber = cipherOBJ.decrypt(dPatientId);
       log.debug("DocumentAccessManager - Decrypted patientId = " + patientUnitNumber);
       return patientUnitNumber;
    }

    private String decodeUserName(String dUserName) {
       CryptAHLTA cipherOBJ = new CryptAHLTA();
       String userName = cipherOBJ.decrypt(dUserName);
       log.debug("DocumentAccessManager - Decrypted userName = " + userName);
       return userName;
    }
}

