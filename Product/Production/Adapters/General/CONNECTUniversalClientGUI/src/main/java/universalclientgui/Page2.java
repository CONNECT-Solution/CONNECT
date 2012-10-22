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

import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.webui.jsf.component.Calendar;
import com.sun.webui.jsf.component.Hyperlink;
import com.sun.webui.jsf.component.StaticText;
import com.sun.webui.jsf.component.Tab;
import com.sun.webui.jsf.component.TabSet;
import com.sun.webui.jsf.component.TextField;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCommunityMapping;
import org.uddi.api_v3.BusinessEntity;
import gov.hhs.fha.nhinc.mpi.adapter.component.proxy.AdapterComponentMpiProxy;
import gov.hhs.fha.nhinc.mpi.adapter.component.proxy.AdapterComponentMpiProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.PixRetrieveBuilder;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.PatientCorrelationProxy;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.PatientCorrelationProxyObjectFactory;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.subdisc.HL7Extractors;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201305Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PatientTransforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7DataTransformHelper;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
//import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.xml.bind.JAXBElement;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.ADExplicit;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.II;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.TSExplicit;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201310UV02OtherIDs;
import org.hl7.v3.PRPAMT201310UV02Patient;
import org.hl7.v3.TELExplicit;
import org.hl7.v3.RetrievePatientCorrelationsResponseType;

import java.util.StringTokenizer;

/**
 * <p>Page bean that corresponds to a similarly named JSP page.  This
 * class contains component definitions (and initialization code) for
 * all components that you have defined on this page, as well as
 * lifecycle methods and event handlers where you may add behavior
 * to respond to incoming events.</p>
 *
 * @version Page2.java
 * @version Created on Aug 7, 2009, 6:03:45 PM
 * @author vvickers
 */
public class Page2 extends AbstractPageBean {
    // <editor-fold defaultstate="collapsed" desc="Managed Component Definition">

    private static final String PROPERTY_FILE_NAME_ADAPTER = "adapter";
    private static final String PROPERTY_FILE_NAME_UC = "universalClient";
    private static final String PROPERTY_FILE_NAME_GATEWAY = "gateway";
    private static final String PROPERTY_FILE_DOCUMENT_LOINCS = "DocumentLoincs";
    private static final String PROPERTY_FILE_KEY_AGENCY = "AgencyName";
    private static final String PROPERTY_FILE_KEY_ASSIGN_AUTH = "assigningAuthorityId";
    private static final String PROPERTY_FILE_KEY_HOME_COMMUNITY = "localHomeCommunityId";
    private static final String PROPERTY_FILE_KEY_LOCAL_DEVICE = "localDeviceId";
    private static final String PROPERTY_FILE_KEY_SSA_OID = "ssa.oid";
    private static Log log = LogFactory.getLog(Page2.class);
    //dynamic stylesheet properties
    private static final String ADAPTER_PROPERTY_FILE = "adapter";
    private static final String C32_STYLE_SHEET_PROPERTY = "C32StyleSheet";
    private static String m_sPropertyFileDir = "";
    private static String m_sFileSeparator =
        System.getProperty("file.separator");
    private static final String m_sFailedEnvVarMessage =
        "Unable to access environment variable: NHINC_PROPERTIES_DIR.";
    private static boolean m_bFailedToLoadEnvVar = false;
    private static final String HL7_DATE_FORMAT = "yyyyMMddHHmmss";
    private static final String REGULAR_DATE_FORMAT = "yyyyMMdd";

    static {
        String sValue = PropertyAccessor.getInstance().getPropertyFileLocation();

        if ((sValue != null) && (sValue.length() > 0)) {
            // Set it up so that we always have a "/" at the end - in case
            //------------------------------------------------------------
            if ((sValue.endsWith("/")) || (sValue.endsWith("\\"))) {
                m_sPropertyFileDir = sValue;
            } else {
                m_sPropertyFileDir = sValue + m_sFileSeparator;
            }
        } else {
            log.error(m_sFailedEnvVarMessage);
            m_bFailedToLoadEnvVar = true;
        }
    }

    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
    }
    // General Page 2 Bindings
    private StaticText agencyLogo = new StaticText();

    public StaticText getAgencyLogo() {

        try {
            String agencyName = PropertyAccessor.getInstance().getProperty(PROPERTY_FILE_NAME_UC, PROPERTY_FILE_KEY_AGENCY);
            log.info("Setting agency name: " + agencyName);
            this.agencyLogo.setText(agencyName);
        } catch (PropertyAccessException ex) {
            log.error("Universal Client can not access " + PROPERTY_FILE_KEY_AGENCY + " property: " + ex.getMessage());
        }
        return agencyLogo;
    }

    public void setAgencyLogo(StaticText st) {
        this.agencyLogo = st;
    }
    private StaticText patientInfo = new StaticText();

    public StaticText getPatientInfo() {
        return patientInfo;
    }

    public void setPatientInfo(StaticText st) {
        this.patientInfo = st;
    }
    private TabSet clientTabSet = new TabSet();

    public TabSet getClientTabSet() {
        return clientTabSet;
    }

    public void setClientTabSet(TabSet ts) {
        this.clientTabSet = ts;
    }
    private Tab patientSearchTab = new Tab();

    public Tab getPatientSearchTab() {
        return patientSearchTab;
    }

    public void setPatientSearchTab(Tab t) {
        this.patientSearchTab = t;
    }
    private Tab subjectDiscoveryTab = new Tab();

    public Tab getSubjectDiscoveryTab() {
        return subjectDiscoveryTab;
    }

    public void setSubjectDiscoveryTab(Tab t) {
        this.subjectDiscoveryTab = t;
    }
    private Tab documentTab = new Tab();

    public Tab getDocumentTab() {
        return documentTab;
    }

    public void setDocumentTab(Tab t) {
        this.documentTab = t;
    }
    // Patient Search Tab Bindings
    private TextField lastNameField = new TextField();

    public TextField getLastNameField() {
        return lastNameField;
    }

    public void setLastNameField(TextField tf) {
        this.lastNameField = tf;
    }
    private TextField firstNameField = new TextField();

    public TextField getFirstNameField() {
        return firstNameField;
    }

    public void setFirstNameField(TextField tf) {
        this.firstNameField = tf;
    }
    private TextField middleInitialField = new TextField();

    public TextField getMiddleInitialField() {
        return middleInitialField;
    }

    public void setMiddleInitialField(TextField tf) {
        this.middleInitialField = tf;
    }
    //Date of Birth calendar components
    private Calendar searchDateOfBirth = new Calendar();

    public Calendar getSearchDateOfBirth() {

        return searchDateOfBirth;
    }

    public void setSearchDateOfBirth(Calendar c) {
        this.searchDateOfBirth = c;
    }
    private List<PatientSearchData> patientSearchDataList;

    public List<PatientSearchData> getPatientSearchDataList() {
        patientSearchDataList = getSessionBean1().getPatientSearchDataList();
        return patientSearchDataList;
    }

    public void setPatientSearchDataList(List<PatientSearchData> patientSearchDataList) {

        this.patientSearchDataList = patientSearchDataList;
        getSessionBean1().setPatientSearchDataList(patientSearchDataList);
    }
    private Hyperlink patientSelectIdLink = new Hyperlink();

    public Hyperlink getPatientSelectIdLink() {
        return patientSelectIdLink;
    }

    public void setPatientSelectIdLink(Hyperlink h) {
        this.patientSelectIdLink = h;
    }
    private Hyperlink selectedDocumentID = new Hyperlink();

    public Hyperlink getSelectedDocumentID() {

        return selectedDocumentID;
    }

    public void setSelectedDocumentID(Hyperlink selectedDocumentID) {

        this.selectedDocumentID = selectedDocumentID;

    }
    // Subject Discovery Tab Bindings
    private StaticText subjectDiscoveryResultsInfo = new StaticText();

    public StaticText getSubjectDiscoveryResultsInfo() {
        return subjectDiscoveryResultsInfo;
    }

    public void setSubjectDiscoveryResultsInfo(StaticText st) {
        this.subjectDiscoveryResultsInfo = st;
    }
    private List<PatientCorrelationData> patientCorrelationList;

    public List<PatientCorrelationData> getPatientCorrelationList() {
        patientCorrelationList = getSessionBean1().getPatientCorrelationList();
        return patientCorrelationList;
    }

    public void setPatientCorrelationList(List<PatientCorrelationData> patientCorrelationList) {
        this.patientCorrelationList = patientCorrelationList;
        getSessionBean1().setPatientCorrelationList(patientCorrelationList);
    }
    private Hyperlink correlatedAuthorityLink = new Hyperlink();

    public Hyperlink getCorrelatedAuthorityLink() {
        return correlatedAuthorityLink;
    }

    public void setCorrelatedAuthorityLink(Hyperlink h) {
        this.correlatedAuthorityLink = h;
    }
    private StaticText broadcastInfo = new StaticText();

    public StaticText getBroadcastInfo() {
        return broadcastInfo;
    }

    public void setBroadcastInfo(StaticText st) {
        this.broadcastInfo = st;
    }
    private StaticText broadcastInfo2 = new StaticText();

    public StaticText getBroadcastInfo2() {
        return broadcastInfo2;
    }

    public void setBroadcastInfo2(StaticText st) {
        this.broadcastInfo2 = st;
    }
    //service time calendar components
    private Calendar serviceTimeFromDate = new Calendar();

    public Calendar getServiceTimeFromDate() {
        return serviceTimeFromDate;
    }

    public void setServiceTimeFromDate(Calendar c) {
        this.serviceTimeFromDate = c;
    }
    private Calendar serviceTimeToDate = new Calendar();

    public Calendar getServiceTimeToDate() {
        return serviceTimeToDate;
    }

    public void setServiceTimeToDate(Calendar c) {
        this.serviceTimeToDate = c;
    }
    private StaticText errorMessage = new StaticText();

    public StaticText getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(StaticText errorMessage) {
        this.errorMessage = errorMessage;
    }
    private String errors;

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    // </editor-fold>
    /**
     * <p>Construct a new Page bean instance.</p>
     */
    public Page2() {
    }
    private boolean isPostBack = false;

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
            log("Page2 Initialization Failure", e);
            throw e instanceof FacesException ? (FacesException) e : new FacesException(e);
        }


        String token = getSessionBean1().getAuthToken();
        if (token == null || token.isEmpty()) {
            try {
                FacesContext context = FacesContext.getCurrentInstance();
                context.getExternalContext().redirect("Page1.jsp");
            } catch (IOException ex) {
                log.error("Universal Client can not prerender Page2: " + ex.getMessage());
            }
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

        log.debug("Page2 Postback");

        isPostBack = true;
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

        //check for postback - if not postback, then calculate the initial view
        if (!isPostBack) {
            log.debug("Page2:prerender() - retrieving login values from Page1 login...");

            log.debug("Page2:prerender - patient id value from Page1 = " + getExternalContext().getRequestParameterMap().get("form1:hideEID"));

            //only execute when page2 does not postback...
            if (getExternalContext().getRequestParameterMap().get("form1:hideEID") != null && !getExternalContext().getRequestParameterMap().get("form1:hideEID").equals("standalone")) {
                //set the username session value - this indicates that it is a care portal user
                if (getExternalContext().getRequestParameterMap().get("form1:nameField_field").toString() != null) {
                    getSessionBean1().setUserName(getExternalContext().getRequestParameterMap().get("form1:nameField_field").toString());
                }

                if (getExternalContext().getRequestParameterMap().get("form1:hideEID").isEmpty()) {
                    //user is a careportal user who has already accessed the application and logged back in
                    initialDeactivateSubDiscTabAndDocTab();
                } else {
                    //the login contains a patient id which indicates a first time care portal user
                    firstRunCarePortal(getExternalContext().getRequestParameterMap().get("form1:hideEID").toString());
                }
            } else {
                initialDeactivateSubDiscTabAndDocTab();
            }
        }
    }

    @Override
    protected void afterRenderResponse() {
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
        // make sure this is set as first active tab
        this.getClientTabSet().setSelected("patientSearchTab");

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
    protected SessionBean1 getSessionBean1() {
        return (SessionBean1) getBean("SessionBean1");
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected ApplicationBean1 getApplicationBean1() {
        return (ApplicationBean1) getBean("ApplicationBean1");
    }

    // General Page2 methods
    public String logOutButton_action() {
        String loginDecision = "login_required";
        getSessionBean1().setAuthToken("");
        getSessionBean1().getPatientSearchDataList().clear();

        //send logout signal to Page1 and define user type
        if (getSessionBean1().getUserName() != null) {
            getRequestBean1().setCarePortalUserFlag(getSessionBean1().getUserName());
        } else {
            getRequestBean1().setCarePortalUserFlag("standaloneUser");
        }


        return loginDecision;
    }
    private String emptyDataMsg = "";

    //Set the empty data  message
    public void setEmptyDataMsg(String msg) {
        emptyDataMsg = msg;
    }

    /**
     * Get the empty data  message
     */
    public String getEmptyDataMsg() {
        return emptyDataMsg;
    }

    // Patient Search Tab Methods
    public String patientSearchTab_action() {
        this.errorMessage.setText("");
        return null;
    }

    protected PatientDiscoveryAuditLogger getPatientDiscoveryAuditLogger() {
        return new PatientDiscoveryAuditLogger();
    }

    private PRPAIN201306UV02 getPatientCarePortal(String pPatientId) {
        PRPAIN201306UV02 foundPatient = null;

        try {
            String assigningAuthId = PropertyAccessor.getInstance().getProperty(PROPERTY_FILE_NAME_ADAPTER, PROPERTY_FILE_KEY_ASSIGN_AUTH);
            String orgId = PropertyAccessor.getInstance().getProperty(PROPERTY_FILE_NAME_GATEWAY, PROPERTY_FILE_KEY_HOME_COMMUNITY);
            II patId = new II();
            patId.setExtension(pPatientId);
            patId.setRoot(orgId);
            PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(HL7PatientTransforms.create201301PatientPerson(null, null, null, null), patId);
            PRPAIN201305UV02 searchPat = HL7PRPA201305Transforms.createPRPA201305(patient, orgId, orgId, assigningAuthId);

            AdapterComponentMpiProxyObjectFactory mpiFactory = new AdapterComponentMpiProxyObjectFactory();
            AdapterComponentMpiProxy mpiProxy = mpiFactory.getAdapterComponentMpiProxy();
            AuthenticatedUserInfo authenticationInfo = Page1.getAuthenticationInfo();
            AssertionType oAssertion = authenticationInfo.getAssertions();

            //before returning a response - make audit log entry
            log.debug("Log Audit entry for patient lookup request...");
            getPatientDiscoveryAuditLogger().auditAdapter201305(searchPat, oAssertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION + ": UC Patient Lookup");

            foundPatient = mpiProxy.findCandidates(searchPat, oAssertion);

            //now that result has been returned - make audit log entry
            log.debug("Log Audit entry for patient lookup response...");
            getPatientDiscoveryAuditLogger().auditAdapter201306(foundPatient, oAssertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION + ": UC Patient Lookup");

        } catch (PropertyAccessException ex) {
            log.error("Property file access problem: " + ex.getMessage());
        }

        return foundPatient;
    }

    private PatientSearchData setUpPatientData(PRPAIN201306UV02 patients) {
        PatientSearchData patientData = null;
        try {
            List<Object> data = new ArrayList<Object>();
            String assigningAuthId = PropertyAccessor.getInstance().getProperty(PROPERTY_FILE_NAME_ADAPTER, PROPERTY_FILE_KEY_ASSIGN_AUTH);

            PRPAMT201310UV02Patient mpiPat = patients.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient();
            List<PNExplicit> patientNames = null;

            if (mpiPat.getPatientPerson() != null && mpiPat.getPatientPerson().getValue() != null &&
                mpiPat.getPatientPerson().getValue().getName() != null) {
                patientNames = mpiPat.getPatientPerson().getValue().getName();
                data.add(patientNames);
                List<ADExplicit> addr = null;

                if (mpiPat.getPatientPerson().getValue().getAddr() != null) {
                    addr = mpiPat.getPatientPerson().getValue().getAddr();
                    log.debug("**********addr size =" + addr.size());
                }
                data.add(addr);

                //adding patient ID to be displayed since this only happens when the patient ID is known, then no need to search for the ID
                data.add(mpiPat.getId().get(0).getExtension());

                //extract SSN
                String resultPatientSSN = "";
                String tempResultPatientSSN = ""; //for display purposes only
                String ssaOid = PropertyAccessor.getInstance().getProperty(PROPERTY_FILE_NAME_UC, PROPERTY_FILE_KEY_SSA_OID);
                if (mpiPat.getPatientPerson().getValue().getAsOtherIDs() != null) {
                    List<PRPAMT201310UV02OtherIDs> ssnIdsList = mpiPat.getPatientPerson().getValue().getAsOtherIDs();
                    for (PRPAMT201310UV02OtherIDs ssnId : ssnIdsList) {
                        if (ssnId.getId() != null && !ssnId.getId().isEmpty()) {
                            for (II idxId : ssnId.getId()) {
                                if (idxId != null &&
                                    idxId.getExtension() != null &&
                                    idxId.getRoot() != null) {
                                    if (ssaOid.equals(idxId.getRoot())) {
                                        resultPatientSSN = idxId.getExtension();
                                        tempResultPatientSSN = "XXX-XX-" + idxId.getExtension().substring(idxId.getExtension().length() - 4);
                                        log.debug(resultPatientSSN + " found with SSA Authority: " + idxId.getRoot());
                                    }
                                }
                            }
                        }
                    }
                }
                data.add(resultPatientSSN);

                //extract DOB
                String resultPatientDOB = "";
                if (mpiPat.getPatientPerson().getValue().getBirthTime() != null &&
                    mpiPat.getPatientPerson().getValue().getBirthTime().getValue() != null) {
                    resultPatientDOB = mpiPat.getPatientPerson().getValue().getBirthTime().getValue();
                }
                data.add(resultPatientDOB);

                //extract Gender
                String resultPatientGender = "";
                if (mpiPat.getPatientPerson().getValue().getAdministrativeGenderCode() != null &&
                    mpiPat.getPatientPerson().getValue().getAdministrativeGenderCode().getCode() != null) {
                    resultPatientGender = mpiPat.getPatientPerson().getValue().getAdministrativeGenderCode().getCode();
                }
                data.add(resultPatientGender);

                //extract phone number
                List<TELExplicit> telno = null;
                if (mpiPat.getPatientPerson().getValue().getTelecom() != null) {
                    telno = mpiPat.getPatientPerson().getValue().getTelecom();
                }
                data.add(telno);

                if (mpiPat.getPatientPerson() != null &&
                    mpiPat.getPatientPerson().getValue() != null &&
                    mpiPat.getPatientPerson().getValue().getName() != null) {
                    String patientLastName = "";
                    String patientFirstName = "";
                    PersonNameType name = HL7Extractors.translatePNListtoPersonNameType(mpiPat.getPatientPerson().getValue().getName());

                    if (name.getFamilyName() != null) {
                        patientLastName = name.getFamilyName();
                    }
                    if (name.getGivenName() != null) {

                        if (mpiPat.getPatientPerson().getValue().getName() != null) {
                            //have to distinguish first name from middle name
                            List<PNExplicit> fNameList = mpiPat.getPatientPerson().getValue().getName();

                            for (PNExplicit nameId : fNameList) {
                                if (nameId.getContent() != null && !nameId.getContent().isEmpty()) {

                                    List<Serializable> choice = nameId.getContent();
                                    Iterator<Serializable> iterSerialObjects = choice.iterator();
                                    List<String> tempList = new ArrayList();

                                    while (iterSerialObjects.hasNext()) {
                                        Serializable contentItem = iterSerialObjects.next();

                                        if (contentItem instanceof JAXBElement) {
                                            JAXBElement oJAXBElement = (JAXBElement) contentItem;
                                            log.debug("Found JAXBElement");
                                            if (oJAXBElement.getValue() instanceof EnExplicitGiven) {
                                                //add to array List
                                                tempList.add(((EnExplicitGiven) oJAXBElement.getValue()).getContent());
                                            }
                                        }

                                    }
                                    patientFirstName = tempList.get(0);
                                }
                            }
                        }
                    }
                    data.add(patientLastName);
                    data.add(patientFirstName);
                }

                data.add(tempResultPatientSSN);

                patientData = new PatientSearchData(data);
                patientData.setAssigningAuthorityID(assigningAuthId);
                this.getPatientSearchDataList().add(patientData);
                getSessionBean1().setFoundPatient(patientData);

            }
        } catch (PropertyAccessException ex) {
            log.error("Property file access problem: " + ex.getMessage());
        }

        return patientData;
    }

    private void firstRunCarePortal(String pPatientId) {
        //Determined this is first run for user coming from care portal

        PRPAIN201306UV02 patients = getPatientCarePortal(pPatientId);

        this.getPatientSearchDataList().clear();
        this.patientInfo.setText("");
        getSessionBean1().setFoundPatient(null);
        this.errorMessage.setText("");

        try {
            String assigningAuthId = PropertyAccessor.getInstance().getProperty(PROPERTY_FILE_NAME_ADAPTER, PROPERTY_FILE_KEY_ASSIGN_AUTH);
            String orgId = PropertyAccessor.getInstance().getProperty(PROPERTY_FILE_NAME_GATEWAY, PROPERTY_FILE_KEY_HOME_COMMUNITY);

            if (patients != null && patients.getControlActProcess() != null &&
                patients.getControlActProcess().getSubject() != null &&
                patients.getControlActProcess().getSubject().get(0) != null &&
                patients.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                patients.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                patients.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null &&
                patients.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId() != null &&
                patients.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0) != null) {

                //set up display for page2 search member page
                PatientSearchData patientData = setUpPatientData(patients);

                PRPAMT201310UV02Patient mpiPat = patients.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient();

                if (mpiPat.getPatientPerson() != null && mpiPat.getPatientPerson().getValue() != null &&
                    mpiPat.getPatientPerson().getValue().getName() != null) {
                    setPatientDiscoveryInfo(patientData);
                }

                initialActivateSubDiscTabAndDocTab();
                this.getClientTabSet().setSelected("subjectDiscoveryTab");
            } else {
                log.error("No MPI candidates where found matching Patient ID: " + pPatientId + " org: " + orgId + " assigning authority: " + assigningAuthId);
                this.patientInfo.setText("No MPI candidates where found matching Patient ID: " + pPatientId + " org: " + orgId + " assigning authority: " + assigningAuthId);

                initialDeactivateSubDiscTabAndDocTab();
            }
        } catch (PropertyAccessException ex) {
            log.error("Property file access problem: " + ex.getMessage());
        }
    }

    private void setPatientDiscoveryInfo(PatientSearchData patientData) {
        //Clear the document search results
        setBean("DocumentQueryResults", new DocumentQueryResults());

        StringBuffer discoverInfoBuf = new StringBuffer();
        for (PatientSearchData testPatient : getPatientSearchDataList()) {

            getSessionBean1().setFoundPatient(testPatient);
            patientData = testPatient;

            discoverInfoBuf.append("Name: ");
            if (patientData.getNames() != null) {
                PersonNameType name = HL7Extractors.translatePNListtoPersonNameType((List<PNExplicit>) patientData.getNames());
                if (name.getFamilyName() != null) {
                    //last name
                    discoverInfoBuf.append(name.getFamilyName());
                    discoverInfoBuf.append(", ");
                }
                if (name.getGivenName() != null) {
                    //first name
                    discoverInfoBuf.append(patientData.getFirstName());
                    discoverInfoBuf.append( " ");
                }
            }

            discoverInfoBuf.append("ID: ");
            if (!patientData.getPatientId().toString().isEmpty()) {
                discoverInfoBuf.append(patientData.getPatientId());
                discoverInfoBuf.append(" ");
            }
            discoverInfoBuf.append("SSN: ");
            if (!patientData.getSsn().toString().isEmpty()) {
                discoverInfoBuf.append(patientData.getDisplaySsn());
                discoverInfoBuf.append(" ");
            }
            discoverInfoBuf.append("DOB: ");
            if (!patientData.getDob().toString().isEmpty()) {
                discoverInfoBuf.append(patientData.getDob());
                discoverInfoBuf.append(" ");
            }
            discoverInfoBuf.append("Gender: ");
            if (!patientData.getGender().toString().isEmpty()) {
                discoverInfoBuf.append(patientData.getGender());
                discoverInfoBuf.append(" ");
            }
            this.patientInfo.setText(discoverInfoBuf.toString());

            SearchData searchData = (SearchData) getBean("SearchData");
            searchData.setPatientID(patientData.getPatientId().toString());
        }


    }

    private void initialActivateSubDiscTabAndDocTab() {
        if (getSubjectDiscoveryTab() != null) {
            this.getSubjectDiscoveryTab().setStyle("font-family: 'Times New Roman',Times,serif; font-size: 14px");
        }
        initializeSubjectDiscoveryTab();
        if (getDocumentTab() != null) {
            this.getDocumentTab().setStyle("font-family: 'Times New Roman',Times,serif; font-size: 14px");
        }
    }

    private void initialDeactivateSubDiscTabAndDocTab() {
        if (getSubjectDiscoveryTab() != null) {
            this.getSubjectDiscoveryTab().setStyle("color: gray; font-family: 'Times New Roman',Times,serif; font-size: 14px");
            this.getSubjectDiscoveryTab().setDisabled(true);
        }
        if (getDocumentTab() != null) {
            this.getDocumentTab().setStyle("color: gray; font-family: 'Times New Roman',Times,serif; font-size: 14px");
            this.getDocumentTab().setDisabled(true);
        }
    }

    public String patientSearchButton_action() {

        this.getPatientSearchDataList().clear();
        this.patientInfo.setText("");
        getSessionBean1().setFoundPatient(null);
        deactivateSubjectDiscoveryTab();
        deactivateDocumentTab();
        this.errorMessage.setText("");

        if (!isPatientSearchCriteriaValid()) {
            log.error("Error Message: " + errors);
            this.errorMessage.setText(errors);
            return null;
        }

        String firstName = (String) firstNameField.getText();
        String lastName = (String) lastNameField.getText();
        String middleInitial = (String) middleInitialField.getText();
        String dateOfBirth = null;

        if (searchDateOfBirth != null && searchDateOfBirth.getSelectedDate() != null) {
            dateOfBirth = formatDate(searchDateOfBirth.getSelectedDate(), REGULAR_DATE_FORMAT);
        }

        try {
            String assigningAuthId = PropertyAccessor.getInstance().getProperty(PROPERTY_FILE_NAME_ADAPTER, PROPERTY_FILE_KEY_ASSIGN_AUTH);
            String orgId = PropertyAccessor.getInstance().getProperty(PROPERTY_FILE_NAME_GATEWAY, PROPERTY_FILE_KEY_HOME_COMMUNITY);

            StringBuffer searchInfoBuf = new StringBuffer("Searching for Patient: ");
            boolean isFirstNameAvail = false;
            boolean isLastNameAvail = false;

            if (firstName != null && !firstName.isEmpty()) {
                searchInfoBuf.append(firstName);
                searchInfoBuf.append(" ");
                isFirstNameAvail = true;
            }
            if (lastName != null && !lastName.isEmpty()) {
                searchInfoBuf.append(lastName);
                searchInfoBuf.append(" ");
                isLastNameAvail = true;
            }
            if (dateOfBirth != null) {
                searchInfoBuf.append(dateOfBirth);
                searchInfoBuf.append(" ");
            }

            if (isFirstNameAvail || isLastNameAvail) {
                this.patientInfo.setText(searchInfoBuf.toString());

                II patId = new II();
                patId.setRoot(assigningAuthId);

                PNExplicit patname = null;

                log.debug("begin create Name");
                patname = HL7DataTransformHelper.CreatePNExplicit(firstName, middleInitial, lastName);

                log.debug("begin create birthTime");
                TSExplicit bday = null;
                bday = HL7DataTransformHelper.TSExplicitFactory(dateOfBirth);



                //    PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(HL7PatientTransforms.create201301PatientPerson(firstName, middleInitial, lastName, null, dateOfBirth, null), patId);
                PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(HL7PatientTransforms.create201301PatientPerson(patname, null, bday, null), patId);
                PRPAIN201305UV02 searchPat = HL7PRPA201305Transforms.createPRPA201305(patient, orgId, orgId, assigningAuthId);

                AdapterComponentMpiProxyObjectFactory mpiFactory = new AdapterComponentMpiProxyObjectFactory();
                AdapterComponentMpiProxy mpiProxy = mpiFactory.getAdapterComponentMpiProxy();
                AuthenticatedUserInfo authenticationInfo = Page1.getAuthenticationInfo();
                AssertionType oAssertion = authenticationInfo.getAssertions();

                //before returning a response - make audit log entry
                log.debug("Log Audit entry for patient lookup request...");
                getPatientDiscoveryAuditLogger().auditAdapter201305(searchPat, oAssertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION + ": UC Patient Lookup");

                PRPAIN201306UV02 patients = mpiProxy.findCandidates(searchPat, oAssertion);

                //now that result has been returned - make audit log entry
                log.debug("Log Audit entry for patient lookup response...");
                getPatientDiscoveryAuditLogger().auditAdapter201306(patients, oAssertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION + ": UC Patient Lookup");

                List<PRPAMT201310UV02Patient> mpiPatResultList = new ArrayList<PRPAMT201310UV02Patient>();
                if (patients != null && patients.getControlActProcess() != null && patients.getControlActProcess().getSubject() != null) {
                    List<PRPAIN201306UV02MFMIMT700711UV01Subject1> subjectList = patients.getControlActProcess().getSubject();
                    log.debug("Search MPI found " + subjectList.size() + " candidates");

                    //loop through search results cand create Patient object for each result
                    for (PRPAIN201306UV02MFMIMT700711UV01Subject1 subject1 : subjectList) {
                        if (subject1 != null &&
                            subject1.getRegistrationEvent() != null &&
                            subject1.getRegistrationEvent().getSubject1() != null &&
                            subject1.getRegistrationEvent().getSubject1().getPatient() != null) {
                            PRPAMT201310UV02Patient mpiPat = subject1.getRegistrationEvent().getSubject1().getPatient();

                            mpiPatResultList.add(mpiPat);
                        }
                    }
                    if (!mpiPatResultList.isEmpty()) {
                        List<Object> data = null;
                        for (PRPAMT201310UV02Patient resultPatient : mpiPatResultList) {

                            data = new ArrayList<Object>();

                            //extract multiple names
                            List<PNExplicit> patientNames = null;

                            if (resultPatient.getPatientPerson() != null && resultPatient.getPatientPerson().getValue() != null && resultPatient.getPatientPerson().getValue().getName() != null) {
                                patientNames = resultPatient.getPatientPerson().getValue().getName();

                                data.add(patientNames);

                                //extract Address
                                List<ADExplicit> addr = null;
                                if (resultPatient.getPatientPerson().getValue().getAddr() != null) {
                                    addr = resultPatient.getPatientPerson().getValue().getAddr();
                                    log.debug("**********addr size =" + addr.size());
                                }
                                data.add(addr);

                                //extract patient id for this assigning authority
                                String resultPatientId = "";
                                String patientOrgAssigningAuthorityID = null;
                                if (resultPatient.getId() != null && !resultPatient.getId().isEmpty()) {
                                    for (II idxId : resultPatient.getId()) {
                                        if (idxId != null &&
                                            idxId.getExtension() != null &&
                                            idxId.getRoot() != null) {
                                            // Get the assigning authority of the patient
                                            patientOrgAssigningAuthorityID = idxId.getRoot();
                                            if (assigningAuthId.equals(idxId.getRoot())) {
                                                resultPatientId = idxId.getExtension();
                                                log.debug(resultPatientId + " found with assigning authority: " + assigningAuthId);
                                            }
                                        }
                                    }
                                }
                                data.add(resultPatientId);

                                //extract SSN
                                String resultPatientSSN = "";
                                String tempResultPatientSSN = ""; //for display purposes only
                                String ssaOid = PropertyAccessor.getInstance().getProperty(PROPERTY_FILE_NAME_UC, PROPERTY_FILE_KEY_SSA_OID);
                                if (resultPatient.getPatientPerson().getValue().getAsOtherIDs() != null) {
                                    List<PRPAMT201310UV02OtherIDs> ssnIdsList = resultPatient.getPatientPerson().getValue().getAsOtherIDs();
                                    for (PRPAMT201310UV02OtherIDs ssnId : ssnIdsList) {
                                        if (ssnId.getId() != null && !ssnId.getId().isEmpty()) {
                                            for (II idxId : ssnId.getId()) {
                                                if (idxId != null &&
                                                    idxId.getExtension() != null &&
                                                    idxId.getRoot() != null) {
                                                    if (ssaOid.equals(idxId.getRoot())) {
                                                        resultPatientSSN = idxId.getExtension();
                                                        tempResultPatientSSN = "XXX-XX-" + idxId.getExtension().substring(idxId.getExtension().length() - 4);
                                                        log.debug(resultPatientSSN + " found with SSA Authority: " + idxId.getRoot());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                data.add(resultPatientSSN);

                                //extract DOB
                                String resultPatientDOB = "";
                                if (resultPatient.getPatientPerson().getValue().getBirthTime() != null &&
                                    resultPatient.getPatientPerson().getValue().getBirthTime().getValue() != null) {
                                    resultPatientDOB = resultPatient.getPatientPerson().getValue().getBirthTime().getValue();
                                }
                                data.add(resultPatientDOB);

                                //extract Gender
                                String resultPatientGender = "";
                                if (resultPatient.getPatientPerson().getValue().getAdministrativeGenderCode() != null &&
                                    resultPatient.getPatientPerson().getValue().getAdministrativeGenderCode().getCode() != null) {
                                    resultPatientGender = resultPatient.getPatientPerson().getValue().getAdministrativeGenderCode().getCode();
                                }
                                data.add(resultPatientGender);

                                //extract phone number
                                List<TELExplicit> telno = null;
                                if (resultPatient.getPatientPerson().getValue().getTelecom() != null) {
                                    telno = resultPatient.getPatientPerson().getValue().getTelecom();
                                }
                                data.add(telno);

                                //used for display purposes
                                if (resultPatient.getPatientPerson() != null &&
                                    resultPatient.getPatientPerson().getValue() != null &&
                                    resultPatient.getPatientPerson().getValue().getName() != null) {
                                    String patientLastName = "";
                                    String patientFirstName = "";
                                    PersonNameType name = HL7Extractors.translatePNListtoPersonNameType(resultPatient.getPatientPerson().getValue().getName());

                                    if (name.getFamilyName() != null) {

                                        patientLastName = name.getFamilyName();

                                    }
                                    if (name.getGivenName() != null) {

                                        if (resultPatient.getPatientPerson().getValue().getName() != null) {

                                            //have to distinguish first name from middle name
                                            List<PNExplicit> fNameList = resultPatient.getPatientPerson().getValue().getName();


                                            for (PNExplicit nameId : fNameList) {
                                                if (nameId.getContent() != null && !nameId.getContent().isEmpty()) {

                                                    List<Serializable> choice = nameId.getContent();
                                                    Iterator<Serializable> iterSerialObjects = choice.iterator();

                                                    List<String> tempList = new ArrayList();

                                                    while (iterSerialObjects.hasNext()) {
                                                        Serializable contentItem = iterSerialObjects.next();

                                                        if (contentItem instanceof JAXBElement) {
                                                            JAXBElement oJAXBElement = (JAXBElement) contentItem;
                                                            log.debug("Found JAXBElement");
                                                            if (oJAXBElement.getValue() instanceof EnExplicitGiven) {

                                                                //add to array List
                                                                tempList.add(((EnExplicitGiven) oJAXBElement.getValue()).getContent());
                                                            }
                                                        }

                                                    }

                                                    patientFirstName = tempList.get(0);

                                                }
                                            }
                                        }
                                    }
                                    data.add(patientLastName);
                                    data.add(patientFirstName);
                                }

                                data.add(tempResultPatientSSN);
                                //add patient Object
                                //data.add(resultPatient);

                                //add patient info to PatientSearchData object
                                PatientSearchData patientData = new PatientSearchData(data);
                                patientData.setAssigningAuthorityID(patientOrgAssigningAuthorityID);
                                this.getPatientSearchDataList().add(patientData);


                            } else {
                                log.error("Subject patientPerson has no name data.");
                            }
                        }
                    } else {
                        log.error("No subject data found in the MPI candidate");
                    }
                } else {
                    log.error("No MPI candidates where found matching " + firstName + " " + middleInitial + " " + lastName + " org: " + orgId + " assigning authority: " + assigningAuthId);
                }
                if (getPatientSearchDataList().isEmpty()) {
                    this.patientInfo.setText("No MPI candidates where found matching " + firstName + " " + middleInitial + " " + lastName + " org: " + orgId + " assigning authority: " + assigningAuthId);
                } else {
                    this.patientInfo.setText(" ");
                }
            } else {
                this.patientInfo.setText("Please enter the patient's name.");
            }
        } catch (PropertyAccessException ex) {
            log.error("Property file access problem: " + ex.getMessage());
        }
        return null;
    }

    private boolean isPatientSearchCriteriaValid() {
        StringBuffer message = new StringBuffer();
        boolean isValid = true;

        //check to see if there is a null calendar value
        if (middleInitialField.getText() != null && middleInitialField.getText() != "") {
            log.info("middle initial " + middleInitialField.getText() + ".");
            if (!Character.isLetter(((String) middleInitialField.getText()).charAt(0))) {
                message.append("Please enter valid Middle Initial ");
                isValid = false;
            }
        }

        errors = message.toString();

        return isValid;
    }

    public String patientSelectIdLink_action() {

        String matchPatientId = this.getPatientSelectIdLink().getText().toString();
        log.debug("Entering patientSelectIdLink_action method. Patient " + matchPatientId + " has been selected.");

        //Clear the document search results
        setBean("DocumentQueryResults", new DocumentQueryResults());

        log.debug("Looking for patient " + matchPatientId + " in PatientSearchDataList...");

        PatientSearchData foundPatient = null;
        StringBuffer discoverInfoBuf = new StringBuffer();
        for (PatientSearchData testPatient : getPatientSearchDataList()) {
            if (testPatient.getPatientId().equals(matchPatientId)) {

                log.debug("Match for patient " + matchPatientId + " found in PatientSearchDataList!");

                getSessionBean1().setFoundPatient(testPatient);
                foundPatient = testPatient;

                discoverInfoBuf.append("Name: ");
                if (foundPatient.getNames() != null) {

                    PersonNameType name = HL7Extractors.translatePNListtoPersonNameType((List<PNExplicit>) foundPatient.getNames());
                    if (name.getFamilyName() != null) {
                        //last name
                        discoverInfoBuf.append(name.getFamilyName());
                        discoverInfoBuf.append(", ");
                    }
                    if (name.getGivenName() != null) {
                        //first name
                        //discoverInfoBuf.append(name.getGivenName());
                        discoverInfoBuf.append(testPatient.getFirstName());
                        discoverInfoBuf.append(" ");
                    }
                }

                //check for address
                List<ADExplicit> addr = null;
                if (testPatient.getAddress() != null) {
                    addr = (List<ADExplicit>) testPatient.getAddress();
                    log.debug("******Address list size = " + addr.size());
                }

                discoverInfoBuf.append("ID: ");
                if (!foundPatient.getPatientId().toString().isEmpty()) {
                    discoverInfoBuf.append(foundPatient.getPatientId());
                    discoverInfoBuf.append(" ");
                }
                discoverInfoBuf.append("SSN: ");
                if (!foundPatient.getSsn().toString().isEmpty()) {
                    discoverInfoBuf.append(foundPatient.getDisplaySsn());
                    discoverInfoBuf.append(" ");
                }
                discoverInfoBuf.append("DOB: ");
                if (!foundPatient.getDob().toString().isEmpty()) {
                    discoverInfoBuf.append(foundPatient.getDob());
                    discoverInfoBuf.append(" ");
                }
                discoverInfoBuf.append("Gender: ");
                if (!foundPatient.getGender().toString().isEmpty()) {
                    discoverInfoBuf.append(foundPatient.getGender());
                    discoverInfoBuf.append(" ");
                }
                this.patientInfo.setText(discoverInfoBuf.toString());

                activateSubjectDiscoveryTab();
                initializeSubjectDiscoveryTab();
                activateDocumentTab();
                this.getClientTabSet().setSelected("subjectDiscoveryTab");

                //We found it - leave the loop
                break;
            }
        }
        if (foundPatient == null) {
            this.patientInfo.setText("Please select patient from the table.");
        }

        SearchData searchData = (SearchData) getBean("SearchData");

        searchData.setPatientID(foundPatient.getPatientId().toString());

        return null;
    }

    // Subject Discovery Tab Methods
    public String subjectDiscoveryTab_action() {
        this.errorMessage.setText("");
        return null;
    }

    private void activateSubjectDiscoveryTab() {
        this.getSubjectDiscoveryTab().setDisabled(false);
        String tabLabelStyle = this.getSubjectDiscoveryTab().getStyle();
        if (tabLabelStyle.contains("color: gray; ")) {
            String newStyle = tabLabelStyle.replace("color: gray; ", "");
            this.getSubjectDiscoveryTab().setStyle(newStyle);
        }
    }

    private void deactivateSubjectDiscoveryTab() {
        String tabLabelStyle = this.getSubjectDiscoveryTab().getStyle();
        if (!tabLabelStyle.contains("color: gray; ")) {
            StringBuffer newStyle = new StringBuffer(tabLabelStyle);
            newStyle.insert(0, "color: gray; ");
            this.getSubjectDiscoveryTab().setStyle(newStyle.toString());
        }
        this.getSubjectDiscoveryTab().setDisabled(true);
    }

    private void initializeSubjectDiscoveryTab() {
        this.getSubjectDiscoveryResultsInfo().setText("Patient Discovery Results (Existing Correlations)");
        this.getBroadcastInfo().setText("Click to Discover New Correlations.");
        this.getBroadcastInfo2().setText("Warning: This may take several minutes.");

        performPatientCorrelation();
    }

    private void performPatientCorrelation() {

        this.getPatientCorrelationList().clear();
        String assigningAuthId = null;
        try {
            assigningAuthId = PropertyAccessor.getInstance().getProperty(PROPERTY_FILE_NAME_ADAPTER, PROPERTY_FILE_KEY_ASSIGN_AUTH);
        } catch (PropertyAccessException ex) {
            Logger.getLogger(Page2.class.getName()).log(Level.SEVERE, null, ex);
        }

        PatientCorrelationProxyObjectFactory pcFactory = new PatientCorrelationProxyObjectFactory();
        PatientCorrelationProxy pcProxy = pcFactory.getPatientCorrelationProxy();

        QualifiedSubjectIdentifierType homeQualifiedSubjectId = new QualifiedSubjectIdentifierType();
        homeQualifiedSubjectId.setAssigningAuthorityIdentifier(assigningAuthId);
        homeQualifiedSubjectId.setSubjectIdentifier(getSessionBean1().getFoundPatient().getPatientId().toString());
        RetrievePatientCorrelationsRequestType retrieveRequest = new RetrievePatientCorrelationsRequestType();
        retrieveRequest.setQualifiedPatientIdentifier(homeQualifiedSubjectId);
        retrieveRequest.setAssertion(getSessionBean1().getAssertionInfo());

        PRPAIN201309UV02 patCorrelationRequest = PixRetrieveBuilder.createPixRetrieve(retrieveRequest);
        RetrievePatientCorrelationsResponseType response = pcProxy.retrievePatientCorrelations(patCorrelationRequest, retrieveRequest.getAssertion());
        List<QualifiedSubjectIdentifierType> retrievedPatCorrList = new ArrayList<QualifiedSubjectIdentifierType>();

        if (response != null &&
            response.getPRPAIN201310UV02() != null &&
            response.getPRPAIN201310UV02().getControlActProcess() != null &&
            NullChecker.isNotNullish(response.getPRPAIN201310UV02().getControlActProcess().getSubject()) &&
            response.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0) != null &&
            response.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
            response.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
            response.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null &&
            NullChecker.isNotNullish(response.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId())) {
            for (II id : response.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId()) {
                QualifiedSubjectIdentifierType subId = new QualifiedSubjectIdentifierType();
                subId.setAssigningAuthorityIdentifier(id.getRoot());
                subId.setSubjectIdentifier(id.getExtension());
                retrievedPatCorrList.add(subId);
            }

            if (retrievedPatCorrList != null && !retrievedPatCorrList.isEmpty()) {
                for (QualifiedSubjectIdentifierType qualSubject : retrievedPatCorrList) {
                    String remoteAssigningAuth = qualSubject.getAssigningAuthorityIdentifier();
                    String remotePatientId = qualSubject.getSubjectIdentifier();

                    log.debug("Perofrming Home Community lookup for Patient " + remotePatientId + " with an AA of " + remotePatientId);
                    HomeCommunityType remoteHomeCommunity = ConnectionManagerCommunityMapping.getHomeCommunityByAssigningAuthority(remoteAssigningAuth);
                    String remoteHomeCommunityId = remoteHomeCommunity.getHomeCommunityId();
                    HomeCommunityMap hcMapping = new HomeCommunityMap();
                    String remoteHomeCommunityName = hcMapping.getHomeCommunityName(remoteHomeCommunityId);

                    log.debug("Home Community lookup found a Community with the name of  " + remoteHomeCommunityName);

                    List<String> data = new ArrayList<String>();
                    data.add(remoteAssigningAuth);
                    data.add(remotePatientId);
                    data.add(remoteHomeCommunityName);
                    data.add(remoteHomeCommunityId);
                    PatientCorrelationData patientData = new PatientCorrelationData(data);
                    this.getPatientCorrelationList().add(patientData);
                }
            }

        }
    }

    // Document Tab Methods
    private void activateDocumentTab() {
        this.getDocumentTab().setDisabled(false);
        String tabLabelStyle = this.getDocumentTab().getStyle();
        if (tabLabelStyle.contains("color: gray; ")) {
            String newStyle = tabLabelStyle.replace("color: gray; ", "");
            this.getDocumentTab().setStyle(newStyle);
        }
    }

    private void deactivateDocumentTab() {
        String tabLabelStyle = this.getDocumentTab().getStyle();
        if (!tabLabelStyle.contains("color: gray; ")) {
            StringBuffer newStyle = new StringBuffer(tabLabelStyle);
            newStyle.insert(0, "color: gray; ");
            this.getDocumentTab().setStyle(newStyle.toString());
        }
        this.getDocumentTab().setDisabled(true);
    }

    public String broadcastSubjectDiscoveryButton_action() {

        PatientSearchData foundPatient = getSessionBean1().getFoundPatient();

        //retrieve communities from PD comboBox
        List<String> targetList = getComboVal();
        log.debug("Number of Selected Targeted Communities = " + targetList.size());

        // Obtain and format the uniquePatientId
        String ptId = (String) foundPatient.getPatientId();
        String aaId = foundPatient.getAssigningAuthorityID();
        String uniquePatientId = ptId + "^^^&" + aaId + "&ISO";

        AuthenticatedUserInfo authenticationInfo = Page1.getAuthenticationInfo();
        authenticationInfo.updateAssertedPatientId(uniquePatientId);
        AssertionType assertion = authenticationInfo.getAssertions();
        PatientDiscoveryClient patientDiscoveryClient = new PatientDiscoveryClient();
        patientDiscoveryClient.broadcastPatientDiscovery(assertion, foundPatient, targetList);

        performPatientCorrelation();
        this.getSubjectDiscoveryResultsInfo().setText("Broadcast Patient Discovery Results");

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        cal.setTimeInMillis(System.currentTimeMillis());

        this.getBroadcastInfo().setText("Broadcast sent: " + (cal.get(java.util.Calendar.MONTH) + 1) + "/" + cal.get(java.util.Calendar.DAY_OF_MONTH) + "/" + cal.get(java.util.Calendar.YEAR) + " " + cal.get(java.util.Calendar.HOUR_OF_DAY) + ":" + cal.get(java.util.Calendar.MINUTE) + ":" + cal.get(java.util.Calendar.SECOND) + " GMT");
        this.getBroadcastInfo2().setText("");

        return null;
    }

    /**
     *
     * @return
     */
    public String getDocQueryResults() {

        this.errorMessage.setText("");
        //SimpleDateFormat dateFormatter =75 new SimpleDateFormat("MM/dd/yyyy");
        //System.out.println("Creation Date: " + dateFormatter.format(this.getCreationFromDate().getSelectedDate()));

        if (!isDocumentSearchCriteriaValid()) {
            log.error("Error Message: " + errors);
            this.errorMessage.setText(errors);
            return null;
        }

        SearchData searchData = (SearchData) getBean("SearchData");

        PatientSearchData currentPatient = null;

        for (PatientSearchData testPatient : getPatientSearchDataList()) {
            if (testPatient.getPatientId().equals(searchData.getPatientID())) {
                currentPatient = testPatient;
                
                log.info("Current patient is " + currentPatient.getPatientId() + " patient name " + currentPatient.getLastName());
            }
        }
  
        if (currentPatient == null) {
            this.errorMessage.setText("Patient information is not available. Please search again.");
        }

        //retrieve communities from Doc Query comboBox
        List<String> docQueryTargetList = getDocQueryComboVal();
        log.debug("Number of Selected Doc Query Targeted Communities = " + docQueryTargetList.size());

        //retrieve the document types values from the Doc Types combo box
        List<String> docQueryDocTypesList = getDocQueryDocTypesComboVal();
        log.debug("Number of Document Types to be included in request = " + docQueryDocTypesList.size());

        DocumentQueryClient docQueryClient = new DocumentQueryClient();

        List<DocumentInformation> docInfoList = docQueryClient.retrieveDocumentsInformation(currentPatient, this.getServiceTimeFromDate().getSelectedDate(),
            this.getServiceTimeToDate().getSelectedDate(), docQueryTargetList, docQueryDocTypesList);

        //check to see if any results were returned
        if (docInfoList != null && !docInfoList.isEmpty()) {
            //create new DocumentQueryResults object
            DocumentQueryResults documentQueryResults = new DocumentQueryResults();
            documentQueryResults.setDocuments(docInfoList);

            //Replace the value of attribute stored in request, session or application scope named DocumentQueryResults
            setBean("DocumentQueryResults", documentQueryResults);
        } else {
            //clear results table if results exist from previous query
            //Replace the value of attribute stored in request, session or application scope named DocumentQueryResults
            DocumentQueryResults documentQueryResults = new DocumentQueryResults();
            setBean("DocumentQueryResults", documentQueryResults);

            //output the fact that no NHIN Query results have been returned
            setEmptyDataMsg("No Results - Query to NwHIN returned no documents.");
        }

        return null;
    }

    public String documentTab_action() {
        // TODO: Replace with your code
        setEmptyDataMsg("");  //ensure no message is displayed in results table

        this.errorMessage.setText("");
        return null;
    }

    private boolean isDocumentSearchCriteriaValid() {
        StringBuffer message = new StringBuffer();
        boolean isValid = true;

        //check to see if there is a null calendar value 
        if (this.serviceTimeFromDate.getSelectedDate() == null || this.serviceTimeToDate.getSelectedDate() == null) {
            if (this.serviceTimeFromDate.getSelectedDate() == null && this.serviceTimeToDate.getSelectedDate() != null) {
                message.append("** Please enter an Earliest Date value **");
                isValid = false;
            } else if (this.serviceTimeFromDate.getSelectedDate() != null && this.serviceTimeToDate.getSelectedDate() == null) {
                message.append("** Please enter a Most Recent Date value **");
                isValid = false;
            } else if (this.serviceTimeFromDate.getSelectedDate() == null && this.serviceTimeToDate.getSelectedDate() == null) {
                //user has chose to not include Service Time entry in request. Do nothing.
            }
        } else {
            if (this.serviceTimeFromDate.getSelectedDate().after(this.getServiceTimeToDate().getSelectedDate())) {
                message.append("** Earliest Date should not be after Most Recent Date **");
                isValid = false;
            }
        }

        errors = message.toString();

        return isValid;
    }

    /**
     *
     * @param xml
     * @param xsl
     * @return
     */
    private String convertXMLToHTML(ByteArrayInputStream xml, FileReader xsl) {

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {

            TransformerFactory tFactory = TransformerFactory.newInstance();

            Transformer transformer =
                tFactory.newTransformer(new javax.xml.transform.stream.StreamSource(xsl));

            transformer.transform(new javax.xml.transform.stream.StreamSource(xml),
                new javax.xml.transform.stream.StreamResult(output));

        } catch (Exception e) {
            log.error("Exception in transforming xml to html", e);
        }

        return output.toString();
    }
    //getter for Patient Discovery target communities combobox
    private List<String> selectItems;

    public void setComboVal(List<String> selectItems) {
        this.selectItems = selectItems;
    }

    public List<String> getComboVal() {
        return selectItems;
    }
    //getter/setter for Doc Query target communities combobox
    private List<String> docQuerySelectItems;

    public void setDocQueryComboVal(List<String> docQuerySelectItems) {
        this.docQuerySelectItems = docQuerySelectItems;
    }

    public List<String> getDocQueryComboVal() {
        return docQuerySelectItems;
    }
    //setter for list of communities from registry
    private List<SelectItem> availableComs = null;

    public void setCommunitiesFromRegistry(List<SelectItem> availableComs) {
        this.availableComs = availableComs;
    }

    //use the connection manager to gather a list of avaialble target communities
    public List<SelectItem> getCommunitiesFromRegistry() {
        //prepare a list to hold configured communities
        List<BusinessEntity> communities = new ArrayList<BusinessEntity>();
        availableComs = new ArrayList<SelectItem>();

        // Get all the Communities from ConnectionManager
        try {

            //gather communities from InternalConnectionInfo via Connection Manager
            communities = ConnectionManagerCache.getInstance().getAllBusinessEntities();

            //get the home community id for this gateway
            String homeCommunityOrgId = PropertyAccessor.getInstance().getProperty(PROPERTY_FILE_NAME_GATEWAY, PROPERTY_FILE_KEY_HOME_COMMUNITY);

            //convert to strings for display
            for (int r = 0; r < communities.size(); r++) {
                String comName = communities.get(r).getName().get(0).getValue();
                String comOID = communities.get(r).getIdentifierBag().getKeyedReference().get(0).getKeyValue();

                //Adds all available communities except for the local gateway when the user is on the subject discovery tab
                if (!(getClientTabSet().getSelectedTab().toString().contains("subjectDiscoveryTab") &&
                    comOID.equals(homeCommunityOrgId))) {
                    availableComs.add(new SelectItem(comOID, comName));
                }
            }

        } catch (Exception ex) {
            log.error(ex.getMessage());
            return null;
        }

        return availableComs;
    }
    //getter/setter for Doc Query document types combobox
    private List<String> docQuerySelectDocsItems;

    public void setDocQueryDocTypesComboVal(List<String> docQuerySelectDocsItems) {
        this.docQuerySelectDocsItems = docQuerySelectDocsItems;
    }

    public List<String> getDocQueryDocTypesComboVal() {
        return docQuerySelectDocsItems;
    }
    //setter for list of document types from properties file
    private List<SelectItem> availableDocs = null;

    public void setDocTypesFromRegistry(List<SelectItem> availableDocs) {
        this.availableDocs = availableDocs;
    }

    //use the property lookup to retrieve list of avaialble document types
    public List<SelectItem> getDocTypesFromRegistry() {
        //prepare a list to hold configured document types
        availableDocs = new ArrayList<SelectItem>();

        // Get all the Doc Types from ConnectionManager
        try {

            //gather document types from UniversalClient properties file
            String docTypeStr = PropertyAccessor.getInstance().getProperty(PROPERTY_FILE_NAME_UC, PROPERTY_FILE_DOCUMENT_LOINCS);

            log.debug("Docuemnt Loincs property value = " + docTypeStr);

            StringTokenizer st = new StringTokenizer(docTypeStr, ",");

            //convert to strings for display
            while (st.hasMoreElements()) {
                String loincDescription = st.nextToken();

                availableDocs.add(new SelectItem(loincDescription, loincDescription));
            }

        } catch (Exception ex) {
            log.error(ex.getMessage());
            return null;
        }

        return availableDocs;
    }

    public NhinTargetCommunitiesType createNhinTargetCommunities(List<String> homeCommunityIds) {
        NhinTargetCommunitiesType nhinTargetCommunities = new NhinTargetCommunitiesType();

        for (int t = 0; t < homeCommunityIds.size(); t++) {
            if (homeCommunityIds.get(t) != null && !homeCommunityIds.get(t).isEmpty()) {
                HomeCommunityType homeCommunity = new HomeCommunityType();
                homeCommunity.setHomeCommunityId(homeCommunityIds.get(t));

                log.debug("Target Community " + String.valueOf(t + 1) + " OID = " + homeCommunityIds.get(t));

                NhinTargetCommunityType nhinTargetCommunity = new NhinTargetCommunityType();
                nhinTargetCommunity.setHomeCommunity(homeCommunity);
                nhinTargetCommunities.getNhinTargetCommunity().add(nhinTargetCommunity);
            }


        }

        return nhinTargetCommunities;
    }

    private String formatDate(Date date, String format) {

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }
}

