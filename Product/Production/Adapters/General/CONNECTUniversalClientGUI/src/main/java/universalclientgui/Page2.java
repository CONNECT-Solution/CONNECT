/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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

package universalclientgui;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCommunityMapping;
import gov.hhs.fha.nhinc.mpi.adapter.component.proxy.AdapterComponentMpiProxy;
import gov.hhs.fha.nhinc.mpi.adapter.component.proxy.AdapterComponentMpiProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.PixRetrieveBuilder;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.PatientCorrelationProxy;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.PatientCorrelationProxyObjectFactory;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.subdisc.HL7Extractors;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201305Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PatientTransforms;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

import org.apache.log4j.Logger;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201310UV02OtherIDs;
import org.hl7.v3.PRPAMT201310UV02Patient;
import org.hl7.v3.RetrievePatientCorrelationsResponseType;

import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.webui.jsf.component.Calendar;
import com.sun.webui.jsf.component.Hyperlink;
import com.sun.webui.jsf.component.StaticText;
import com.sun.webui.jsf.component.Tab;
import com.sun.webui.jsf.component.TabSet;
import com.sun.webui.jsf.component.TextField;

/**
 * <p>
 * Page bean that corresponds to a similarly named JSP page. This class contains component definitions (and
 * initialization code) for all components that you have defined on this page, as well as lifecycle methods and event
 * handlers where you may add behavior to respond to incoming events.
 * </p>
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
    private static final String PROPERTY_FILE_KEY_AGENCY = "AgencyName";
    private static final String PROPERTY_FILE_KEY_ASSIGN_AUTH = "assigningAuthorityId";
    private static final String PROPERTY_FILE_KEY_HOME_COMMUNITY = "localHomeCommunityId";
    private static final String PROPERTY_FILE_KEY_LOCAL_DEVICE = "localDeviceId";
    private static final String SSA_OID = "2.16.840.1.113883.4.1";
    private static final Logger LOG = Logger.getLogger(Page2.class);

    /**
     * <p>
     * Automatically managed component initialization. <strong>WARNING:</strong> This method is automatically generated,
     * so any user-specified code inserted here is subject to being replaced.
     * </p>
     */
    private void _init() throws Exception {
    }

    // General Page 2 Bindings
    private StaticText agencyLogo = new StaticText();

    public StaticText getAgencyLogo() {
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

    private Calendar creationFromDate = new Calendar();

    public Calendar getCreationFromDate() {
        return creationFromDate;
    }

    public void setCreationFromDate(Calendar c) {
        this.creationFromDate = c;
    }

    private Calendar creationToDate = new Calendar();

    public Calendar getCreationToDate() {
        return creationToDate;
    }

    public void setCreationToDate(Calendar c) {
        this.creationToDate = c;
    }

    private StaticText errorMessage = new StaticText();

    public StaticText getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(StaticText errorMessage) {
        this.errorMessage = errorMessage;
    }

    private String errors;
    private PixRetrieveBuilder pixRetrieveBuilder = new PixRetrieveBuilder();
    private ConnectionManagerCommunityMapping connectionManagerCommunityMapping = new ConnectionManagerCommunityMapping();

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    private List<DocumentInformation> docInfoList = null;

    DocumentQueryResults documentQueryResults = new DocumentQueryResults();

    // </editor-fold>
    /**
     * <p>
     * Construct a new Page bean instance.
     * </p>
     */
    public Page2() {
    }

    /**
     * <p>
     * Callback method that is called whenever a page is navigated to, either directly via a URL, or indirectly via page
     * navigation. Customize this method to acquire resources that will be needed for event handlers and lifecycle
     * methods, whether or not this page is performing post back processing.
     * </p>
     * 
     * <p>
     * Note that, if the current request is a postback, the property values of the components do <strong>not</strong>
     * represent any values submitted with this request. Instead, they represent the property values that were saved for
     * this view when it was rendered.
     * </p>
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

        // </editor-fold>
        // Perform application initialization that must complete
        // *after* managed components are initialized
        // TODO - add your own initialization code here
    }

    /**
     * <p>
     * Callback method that is called after the component tree has been restored, but before any event processing takes
     * place. This method will <strong>only</strong> be called on a postback request that is processing a form submit.
     * Customize this method to allocate resources that will be required in your event handlers.
     * </p>
     */
    @Override
    public void preprocess() {
    }

    /**
     * <p>
     * Callback method that is called just before rendering takes place. This method will <strong>only</strong> be
     * called for the page that will actually be rendered (and not, for example, on a page that handled a postback and
     * then navigated to a different page). Customize this method to allocate resources that will be required for
     * rendering this page.
     * </p>
     */
    @Override
    public void prerender() {
        String token = getSessionBean1().getAuthToken();
        if (token == null || token.isEmpty()) {
            try {
                FacesContext context = FacesContext.getCurrentInstance();
                context.getExternalContext().redirect("Page1.jsp");
            } catch (IOException ex) {
                LOG.error("Universal Client can not prerender Page2: " + ex.getMessage());
            }
        }
        try {
            String agencyName = PropertyAccessor.getInstance().getProperty(PROPERTY_FILE_NAME_UC,
                    PROPERTY_FILE_KEY_AGENCY);
            this.agencyLogo.setText(agencyName);
        } catch (PropertyAccessException ex) {
            LOG.error("Universal Client can not access " + PROPERTY_FILE_KEY_AGENCY + " property: " + ex.getMessage());
        }
    }

    /**
     * <p>
     * Callback method that is called after rendering is completed for this request, if <code>init()</code> was called
     * (regardless of whether or not this was the page that was actually rendered). Customize this method to release
     * resources acquired in the <code>init()</code>, <code>preprocess()</code>, or <code>prerender()</code> methods (or
     * acquired during execution of an event handler).
     * </p>
     */
    @Override
    public void destroy() {
        // make sure this is set as first active tab
        this.getClientTabSet().setSelected("patientSearchTab");

    }

    /**
     * <p>
     * Return a reference to the scoped data bean.
     * </p>
     * 
     * @return reference to the scoped data bean
     */
    protected RequestBean1 getRequestBean1() {
        return (RequestBean1) getBean("RequestBean1");
    }

    /**
     * <p>
     * Return a reference to the scoped data bean.
     * </p>
     * 
     * @return reference to the scoped data bean
     */
    protected SessionBean1 getSessionBean1() {
        return (SessionBean1) getBean("SessionBean1");
    }

    /**
     * <p>
     * Return a reference to the scoped data bean.
     * </p>
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
        return loginDecision;
    }

    // Patient Search Tab Methods
    public String patientSearchTab_action() {
        this.errorMessage.setText("");
        getSessionBean1().getPatientSearchDataList().clear();
        this.firstNameField.setText("");
        this.lastNameField.setText("");
        this.lastNameField.clearInitialState();
        this.patientInfo.setText("");
        return null;
    }

    public String patientSearchButton_action() {

        this.getPatientSearchDataList().clear();
        this.patientInfo.setText("");
        this.errorMessage.setText("");
        getSessionBean1().setFoundPatient(null);
        // deactivateSubjectDiscoveryTab();
        // deactivateDocumentTab();

        String firstName = (String) firstNameField.getText();
        String lastName = (String) lastNameField.getText();
        try {
            PropertyAccessor propertyAccessor = PropertyAccessor.getInstance();
            String assigningAuthId = propertyAccessor.getProperty(PROPERTY_FILE_NAME_ADAPTER,
                    PROPERTY_FILE_KEY_ASSIGN_AUTH);
            String orgId = propertyAccessor.getProperty(PROPERTY_FILE_NAME_GATEWAY, PROPERTY_FILE_KEY_HOME_COMMUNITY);

            StringBuffer searchInfoBuf = new StringBuffer("Searching for Patient: ");
            boolean isFirstNameAvail = false;
            boolean isLastNameAvail = false;
            if (firstName != null && !firstName.isEmpty()) {
                searchInfoBuf.append(firstName + " ");
                isFirstNameAvail = true;
            }
            if (lastName != null && !lastName.isEmpty()) {
                searchInfoBuf.append(lastName + " ");
                isLastNameAvail = true;
            }
            if (isFirstNameAvail || isLastNameAvail) {
                this.patientInfo.setText(searchInfoBuf.toString());

                II patId = new II();
                patId.setRoot(assigningAuthId);
                PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(
                        HL7PatientTransforms.create201301PatientPerson(firstName, lastName, null, null, null), patId);
                PRPAIN201305UV02 searchPat = HL7PRPA201305Transforms.createPRPA201305(patient, orgId, orgId,
                        assigningAuthId);

                AdapterComponentMpiProxyObjectFactory mpiFactory = new AdapterComponentMpiProxyObjectFactory();
                AdapterComponentMpiProxy mpiProxy = mpiFactory.getAdapterComponentMpiProxy();
                AssertionCreator assertionCreator = new AssertionCreator();
                AssertionType oAssertion = assertionCreator.createAssertion();
                PRPAIN201306UV02 patients = mpiProxy.findCandidates(searchPat, oAssertion);

                List<PRPAMT201310UV02Patient> mpiPatResultList = new ArrayList<PRPAMT201310UV02Patient>();
                if (patients != null && patients.getControlActProcess() != null
                        && patients.getControlActProcess().getSubject() != null) {
                    List<PRPAIN201306UV02MFMIMT700711UV01Subject1> subjectList = patients.getControlActProcess()
                            .getSubject();
                    LOG.debug("Search MPI found " + subjectList.size() + " candidates");
                    for (PRPAIN201306UV02MFMIMT700711UV01Subject1 subject1 : subjectList) {
                        if (subject1 != null && subject1.getRegistrationEvent() != null
                                && subject1.getRegistrationEvent().getSubject1() != null
                                && subject1.getRegistrationEvent().getSubject1().getPatient() != null) {
                            PRPAMT201310UV02Patient mpiPat = subject1.getRegistrationEvent().getSubject1().getPatient();
                            mpiPatResultList.add(mpiPat);
                        }
                    }
                    if (!mpiPatResultList.isEmpty()) {
                        List<String> data = new ArrayList<String>();
                        for (PRPAMT201310UV02Patient resultPatient : mpiPatResultList) {
                            // extract first and last name
                            if (resultPatient.getPatientPerson() != null
                                    && resultPatient.getPatientPerson().getValue() != null
                                    && resultPatient.getPatientPerson().getValue().getName() != null) {
                                String patientLastName = "";
                                String patientFirstName = "";
                                PersonNameType name = HL7Extractors.translatePNListtoPersonNameType(resultPatient
                                        .getPatientPerson().getValue().getName());
                                if (name.getFamilyName() != null) {
                                    patientLastName = name.getFamilyName();
                                }
                                if (name.getGivenName() != null) {
                                    patientFirstName = name.getGivenName();
                                }
                                data.add(patientLastName);
                                data.add(patientFirstName);

                                // extract patient id for this assigning authority
                                String resultPatientId = "";
                                String patientOrgAssigningAuthorityID = null;
                                if (resultPatient.getId() != null && !resultPatient.getId().isEmpty()) {
                                    for (II idxId : resultPatient.getId()) {
                                        if (idxId != null && idxId.getExtension() != null && idxId.getRoot() != null) {
                                            // Get the assigning authority of the patient
                                            patientOrgAssigningAuthorityID = idxId.getRoot();
                                            // if (assigningAuthId.equals(idxId.getRoot())) {
                                            resultPatientId = idxId.getExtension();
                                            LOG.debug(resultPatientId + " found with assigning authority: "
                                                    + patientOrgAssigningAuthorityID);
                                            // }
                                        }
                                    }
                                }
                                data.add(resultPatientId);

                                // extract SSN
                                String resultPatientSSN = "";
                                if (resultPatient.getPatientPerson().getValue().getAsOtherIDs() != null) {
                                    List<PRPAMT201310UV02OtherIDs> ssnIdsList = resultPatient.getPatientPerson()
                                            .getValue().getAsOtherIDs();
                                    for (PRPAMT201310UV02OtherIDs ssnId : ssnIdsList) {
                                        if (ssnId.getId() != null && !ssnId.getId().isEmpty()) {
                                            for (II idxId : ssnId.getId()) {
                                                if (idxId != null && idxId.getExtension() != null
                                                        && idxId.getRoot() != null) {
                                                    if (SSA_OID.equals(idxId.getRoot())) {
                                                        resultPatientSSN = idxId.getExtension();
                                                        LOG.debug(resultPatientSSN + " found with SSA Authority: "
                                                                + idxId.getRoot());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                data.add(resultPatientSSN);

                                // extract DOB
                                String resultPatientDOB = "";
                                if (resultPatient.getPatientPerson().getValue().getBirthTime() != null
                                        && resultPatient.getPatientPerson().getValue().getBirthTime().getValue() != null) {
                                    resultPatientDOB = resultPatient.getPatientPerson().getValue().getBirthTime()
                                            .getValue();
                                }
                                data.add(resultPatientDOB);

                                // extract Gender
                                String resultPatientGender = "";
                                if (resultPatient.getPatientPerson().getValue().getAdministrativeGenderCode() != null
                                        && resultPatient.getPatientPerson().getValue().getAdministrativeGenderCode()
                                                .getCode() != null) {
                                    resultPatientGender = resultPatient.getPatientPerson().getValue()
                                            .getAdministrativeGenderCode().getCode();
                                }
                                data.add(resultPatientGender);

                                PatientSearchData patientData = new PatientSearchData(data);
                                patientData.setPatientId(resultPatientId);
                                patientData.setAssigningAuthorityID(patientOrgAssigningAuthorityID);
                                this.getPatientSearchDataList().add(patientData);

                            } else {
                                LOG.error("Subject patientPerson has no name data.");
                            }
                        }
                    } else {
                        LOG.error("No subject data found in the MPI candidate");
                    }
                } else {
                    LOG.error("No MPI candidates where found matching " + firstName + " " + lastName + " org: " + orgId
                            + " assigning authority: " + assigningAuthId);
                }
                if (getPatientSearchDataList().isEmpty()) {
                    this.patientInfo.setText("No MPI candidates where found matching " + firstName + " " + lastName
                            + " org: " + orgId + " assigning authority: " + assigningAuthId);
                } else {
                    this.patientInfo.setText("Found matching candidates.");
                }
            } else {
                this.patientInfo.setText("Please enter the patient's name.");
            }
        } catch (PropertyAccessException ex) {
            LOG.error("Property file access problem: " + ex.getMessage());
        }
        return null;
    }

    public String patientSelectIdLink_action() {

        String matchPatientId = this.getPatientSelectIdLink().getText().toString();
        FacesContext context = FacesContext.getCurrentInstance();
        Map map = context.getExternalContext().getRequestParameterMap();
        String matchAAId = (String) map.get("valueAssigningAuthority");

        PatientSearchData foundPatient = null;
        StringBuffer discoverInfoBuf = new StringBuffer();
        for (PatientSearchData testPatient : getPatientSearchDataList()) {
            if (testPatient.getPatientId().equals(matchPatientId)
                    && testPatient.getAssigningAuthorityID().equals(matchAAId)) {
                getSessionBean1().setFoundPatient(testPatient);
                foundPatient = testPatient;

                discoverInfoBuf.append("Patient Information Query: ");
                discoverInfoBuf.append("Name: ");
                if (!foundPatient.getLastName().isEmpty()) {
                    discoverInfoBuf.append(foundPatient.getLastName() + ", ");
                }
                if (!foundPatient.getFirstName().isEmpty()) {
                    discoverInfoBuf.append(foundPatient.getFirstName() + " ");
                }
                discoverInfoBuf.append("ID: ");
                if (!foundPatient.getPatientId().isEmpty()) {
                    discoverInfoBuf.append(foundPatient.getPatientId() + " ");
                }
                discoverInfoBuf.append("AssigningAuthority ID: ");
                if (!foundPatient.getAssigningAuthorityID().isEmpty()) {
                    discoverInfoBuf.append(foundPatient.getAssigningAuthorityID() + " ");
                }

                discoverInfoBuf.append("SSN: ");
                if (!foundPatient.getSsn().isEmpty()) {
                    discoverInfoBuf.append(foundPatient.getSsn() + " ");
                }
                discoverInfoBuf.append("DOB: ");
                if (!foundPatient.getDob().isEmpty()) {
                    discoverInfoBuf.append(foundPatient.getDob() + " ");
                }
                discoverInfoBuf.append("Gender: ");
                if (!foundPatient.getGender().isEmpty()) {
                    discoverInfoBuf.append(foundPatient.getGender() + " ");
                }
                this.patientInfo.setText(discoverInfoBuf.toString());

                activateSubjectDiscoveryTab();
                initializeSubjectDiscoveryTab();
                activateDocumentTab();
                this.getClientTabSet().setSelected("subjectDiscoveryTab");

                // We found it - leave the loop
                break;
            }
        }
        if (foundPatient == null) {
            this.patientInfo.setText("Please select patient from the table.");
        }

        SearchData searchData = (SearchData) getBean("SearchData");

        searchData.setPatientID(foundPatient.getPatientId());

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
        this.getBroadcastInfo2().setText("Warning: This may take several minutes.");

        performPatientCorrelation();
    }

    private void performPatientCorrelation() {

        this.getPatientCorrelationList().clear();

        PatientCorrelationProxyObjectFactory pcFactory = new PatientCorrelationProxyObjectFactory();
        PatientCorrelationProxy pcProxy = pcFactory.getPatientCorrelationProxy();

        QualifiedSubjectIdentifierType homeQualifiedSubjectId = new QualifiedSubjectIdentifierType();
        homeQualifiedSubjectId.setAssigningAuthorityIdentifier(getSessionBean1().getFoundPatient()
                .getAssigningAuthorityID());
        homeQualifiedSubjectId.setSubjectIdentifier(getSessionBean1().getFoundPatient().getPatientId());
        RetrievePatientCorrelationsRequestType retrieveRequest = new RetrievePatientCorrelationsRequestType();
        retrieveRequest.setQualifiedPatientIdentifier(homeQualifiedSubjectId);
        retrieveRequest.setAssertion(getSessionBean1().getAssertionInfo());

        PRPAIN201309UV02 patCorrelationRequest = pixRetrieveBuilder.createPixRetrieve(retrieveRequest);
        RetrievePatientCorrelationsResponseType response = pcProxy.retrievePatientCorrelations(patCorrelationRequest,
                retrieveRequest.getAssertion());
        List<QualifiedSubjectIdentifierType> retrievedPatCorrList = new ArrayList<QualifiedSubjectIdentifierType>();

        if (response != null
                && response.getPRPAIN201310UV02() != null
                && response.getPRPAIN201310UV02().getControlActProcess() != null
                && NullChecker.isNotNullish(response.getPRPAIN201310UV02().getControlActProcess().getSubject())
                && response.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0) != null
                && response.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent() != null
                && response.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent()
                        .getSubject1() != null
                && response.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent()
                        .getSubject1().getPatient() != null
                && NullChecker.isNotNullish(response.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0)
                        .getRegistrationEvent().getSubject1().getPatient().getId())) {
            for (II id : response.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0)
                    .getRegistrationEvent().getSubject1().getPatient().getId()) {
                QualifiedSubjectIdentifierType subId = new QualifiedSubjectIdentifierType();
                subId.setAssigningAuthorityIdentifier(id.getRoot());
                subId.setSubjectIdentifier(id.getExtension());
                retrievedPatCorrList.add(subId);
            }

            if (retrievedPatCorrList != null && !retrievedPatCorrList.isEmpty()) {
                for (QualifiedSubjectIdentifierType qualSubject : retrievedPatCorrList) {
                    String remoteAssigningAuth = qualSubject.getAssigningAuthorityIdentifier();
                    String remotePatientId = qualSubject.getSubjectIdentifier();
                    HomeCommunityType remoteHomeCommunity = connectionManagerCommunityMapping
                            .getHomeCommunityByAssigningAuthority(remoteAssigningAuth);
                    String remoteHomeCommunityId = remoteHomeCommunity.getHomeCommunityId();
                    HomeCommunityMap hcMapping = new HomeCommunityMap();
                    String remoteHomeCommunityName = hcMapping.getHomeCommunityName(remoteHomeCommunityId);

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

        SearchData searchData = (SearchData) getBean("SearchData");

        PatientSearchData currentPatient = null;

        for (PatientSearchData testPatient : getPatientSearchDataList()) {
            if (testPatient.getPatientId().equals(searchData.getPatientID())) {
                currentPatient = testPatient;
                AssertionType assertion = getSessionBean1().getAssertionInfo();
                PatientDiscoveryClient patientDiscoveryClient = new PatientDiscoveryClient();
                patientDiscoveryClient.broadcastPatientDiscovery(assertion, currentPatient);
                performPatientCorrelation();
                this.getSubjectDiscoveryResultsInfo().setText("Broadcast Patient Discovery Results");
            }
        }

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        cal.setTimeInMillis(System.currentTimeMillis());

        this.getBroadcastInfo().setText(
                "Broadcast sent: " + (cal.get(java.util.Calendar.MONTH) + 1) + "/"
                        + cal.get(java.util.Calendar.DAY_OF_MONTH) + "/" + cal.get(java.util.Calendar.YEAR) + " "
                        + cal.get(java.util.Calendar.HOUR_OF_DAY) + ":" + cal.get(java.util.Calendar.MINUTE) + ":"
                        + cal.get(java.util.Calendar.SECOND) + " GMT");
        this.getBroadcastInfo2().setText("");

        return null;
    }

    /**
     * 
     * @return
     */
    public String getDocQueryResults() {

        this.errorMessage.setText("");
        this.getSessionBean1().getPatientCorrelationList().clear();
        this.patientInfo.setText("");
        SearchData searchData = (SearchData) getBean("SearchData");

        PatientSearchData currentPatient = null;

        for (PatientSearchData testPatient : getPatientSearchDataList()) {
            if (testPatient.getPatientId().equals(searchData.getPatientID())) {
                currentPatient = testPatient;
            }
        }

        if (currentPatient == null) {
            this.errorMessage.setText("Patient information is not available. Please search again.");
        }

        DocumentQueryClient docQueryClient = new DocumentQueryClient();

        docInfoList = docQueryClient.retrieveDocumentsInformation(currentPatient);

        documentQueryResults.setDocuments(docInfoList);

        setBean("DocumentQueryResults", documentQueryResults);

        return null;
    }

    public String documentTab_action() {
        // TODO: Replace with your code
        this.errorMessage.setText("");
        this.docInfoList = null;
        documentQueryResults.setDocuments(docInfoList);
        setBean("DocumentQueryResults", documentQueryResults);
        return null;
    }

    public String displayDocument() throws Exception {

        LOG.debug("Selected document ID: " + this.selectedDocumentID.getText());

        DocumentRetrieveClient docRetrieveClient = new DocumentRetrieveClient();

        String documentID = this.selectedDocumentID.getText().toString();

        DocumentQueryResults docQueryResults = (DocumentQueryResults) getBean("DocumentQueryResults");

        DocumentInformation currentDocument = null;

        for (DocumentInformation documentInformation : docQueryResults.getDocuments()) {
            if (documentID.equals(documentInformation.getDocumentID())) {
                currentDocument = documentInformation;
                break;
            }
        }

        String document = docRetrieveClient.retriveDocument(currentDocument);

        if (document == null || document.isEmpty()) {
            return "display_document_error";
        }

        InputStream xsl = getExternalContext().getResourceAsStream("/WEB-INF/CCD.xsl");
        InputStream xml = new ByteArrayInputStream(document.getBytes());

        String html = convertXMLToHTML(new ByteArrayInputStream(document.getBytes()), xsl);

        xsl.close();
        xml.close();

        LOG.debug("HTML PAGE: " + html);

        if (html == null || html.isEmpty()) {
            return "display_document_error";
        }

        HttpServletResponse response = (HttpServletResponse) getExternalContext().getResponse();

        OutputStream os = response.getOutputStream();
        os.write(html.getBytes());
        os.flush();
        FacesContext.getCurrentInstance().responseComplete();

        return null;
    }

    /**
     * 
     * @param xml
     * @param xsl
     * @return
     */
    private String convertXMLToHTML(InputStream xml, InputStream xsl) {

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {

            TransformerFactory tFactory = TransformerFactory.newInstance();

            Transformer transformer = tFactory.newTransformer(new javax.xml.transform.stream.StreamSource(xsl));

            transformer.transform(new javax.xml.transform.stream.StreamSource(xml),
                    new javax.xml.transform.stream.StreamResult(output));

        } catch (Exception e) {
            LOG.error("Exception in transforming xml to html", e);
        }

        return output.toString();
    }
}
