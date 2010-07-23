/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsRequestType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsResponseType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCommunityMapping;
import gov.hhs.fha.nhinc.mpi.proxy.AdapterMpiProxy;
import gov.hhs.fha.nhinc.mpi.proxy.AdapterMpiProxyObjectFactory;
import gov.hhs.fha.nhinc.patientcorrelationfacade.proxy.PatientCorrelationFacadeProxy;
import gov.hhs.fha.nhinc.patientcorrelationfacade.proxy.PatientCorrelationFacadeProxyObjectFactory;
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
import java.util.TimeZone;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201310UV02OtherIDs;
import org.hl7.v3.PRPAMT201310UV02Patient;

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
    private static final String PROPERTY_FILE_KEY_AGENCY = "AgencyName";
    private static final String PROPERTY_FILE_KEY_ASSIGN_AUTH = "assigningAuthorityId";
    private static final String PROPERTY_FILE_KEY_HOME_COMMUNITY = "localHomeCommunityId";
    private static final String PROPERTY_FILE_KEY_LOCAL_DEVICE = "localDeviceId";
    private static final String SSA_OID = "2.16.840.1.113883.4.1";
    private static Log log = LogFactory.getLog(Page2.class);

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
        String token = getSessionBean1().getAuthToken();
        if (token == null || token.isEmpty()) {
            try {
                FacesContext context = FacesContext.getCurrentInstance();
                context.getExternalContext().redirect("Page1.jsp");
            } catch (IOException ex) {
                log.error("Universal Client can not prerender Page2: " + ex.getMessage());
            }
        }
        try {
            String agencyName = PropertyAccessor.getProperty(PROPERTY_FILE_NAME_UC, PROPERTY_FILE_KEY_AGENCY);
            this.agencyLogo.setText(agencyName);
        } catch (PropertyAccessException ex) {
            log.error("Universal Client can not access " + PROPERTY_FILE_KEY_AGENCY + " property: " + ex.getMessage());
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
        return loginDecision;
    }

    // Patient Search Tab Methods
    public String patientSearchTab_action() {
        this.errorMessage.setText("");
        return null;
    }

    public String patientSearchButton_action() {

        this.getPatientSearchDataList().clear();
        this.patientInfo.setText("");
        getSessionBean1().setFoundPatient(null);
        deactivateSubjectDiscoveryTab();
        deactivateDocumentTab();

        String firstName = (String) firstNameField.getText();
        String lastName = (String) lastNameField.getText();
        try {
            String assigningAuthId = PropertyAccessor.getProperty(PROPERTY_FILE_NAME_ADAPTER, PROPERTY_FILE_KEY_ASSIGN_AUTH);
            String orgId = PropertyAccessor.getProperty(PROPERTY_FILE_NAME_GATEWAY, PROPERTY_FILE_KEY_HOME_COMMUNITY);

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
                PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(HL7PatientTransforms.create201301PatientPerson(firstName, lastName, null, null, null), patId);
                PRPAIN201305UV02 searchPat = HL7PRPA201305Transforms.createPRPA201305(patient, orgId, orgId, assigningAuthId);

                AdapterMpiProxyObjectFactory mpiFactory = new AdapterMpiProxyObjectFactory();
                AdapterMpiProxy mpiProxy = mpiFactory.getAdapterMpiProxy();
                PRPAIN201306UV02 patients = mpiProxy.findCandidates(searchPat);

                List<PRPAMT201310UV02Patient> mpiPatResultList = new ArrayList<PRPAMT201310UV02Patient>();
                if (patients != null && patients.getControlActProcess() != null && patients.getControlActProcess().getSubject() != null) {
                    List<PRPAIN201306UV02MFMIMT700711UV01Subject1> subjectList = patients.getControlActProcess().getSubject();
                    log.debug("Search MPI found " + subjectList.size() + " candidates");
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
                        List<String> data = new ArrayList<String>();
                        for (PRPAMT201310UV02Patient resultPatient : mpiPatResultList) {
                            //extract first and last name
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
                                    patientFirstName = name.getGivenName();
                                }
                                data.add(patientLastName);
                                data.add(patientFirstName);

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
                                if (resultPatient.getPatientPerson().getValue().getAsOtherIDs() != null) {
                                    List<PRPAMT201310UV02OtherIDs> ssnIdsList = resultPatient.getPatientPerson().getValue().getAsOtherIDs();
                                    for (PRPAMT201310UV02OtherIDs ssnId : ssnIdsList) {
                                        if (ssnId.getId() != null && !ssnId.getId().isEmpty()) {
                                            for (II idxId : ssnId.getId()) {
                                                if (idxId != null &&
                                                        idxId.getExtension() != null &&
                                                        idxId.getRoot() != null) {
                                                    if (SSA_OID.equals(idxId.getRoot())) {
                                                        resultPatientSSN = idxId.getExtension();
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
                    log.error("No MPI candidates where found matching " + firstName + " " + lastName + " org: " + orgId + " assigning authority: " + assigningAuthId);
                }
                if (getPatientSearchDataList().isEmpty()) {
                    this.patientInfo.setText("No MPI candidates where found matching " + firstName + " " + lastName + " org: " + orgId + " assigning authority: " + assigningAuthId);
                } else {
                    this.patientInfo.setText(" ");
                }
            } else {
                this.patientInfo.setText("Please enter the patient's name.");
            }
        } catch (PropertyAccessException ex) {
            log.error("Property file assess problem: " + ex.getMessage());
        }
        return null;
    }

    public String patientSelectIdLink_action() {

        String matchPatientId = this.getPatientSelectIdLink().getText().toString();

        //Clear the document search results
        setBean("DocumentQueryResults", new DocumentQueryResults());

        PatientSearchData foundPatient = null;
        StringBuffer discoverInfoBuf = new StringBuffer();
        for (PatientSearchData testPatient : getPatientSearchDataList()) {
            if (testPatient.getPatientId().equals(matchPatientId)) {
                getSessionBean1().setFoundPatient(testPatient);
                foundPatient = testPatient;

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

                //We found it - leave the loop
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
        this.getBroadcastInfo().setText("Click to Discover New Correlations.");
        this.getBroadcastInfo2().setText("Warning: This may take several minutes.");

        performPatientCorrelation();
    }

    private void performPatientCorrelation() {

        this.getPatientCorrelationList().clear();
        try {
            String assigningAuthId = PropertyAccessor.getProperty(PROPERTY_FILE_NAME_ADAPTER, PROPERTY_FILE_KEY_ASSIGN_AUTH);

            PatientCorrelationFacadeProxyObjectFactory pcFactory = new PatientCorrelationFacadeProxyObjectFactory();
            PatientCorrelationFacadeProxy pcProxy = pcFactory.getPatientCorrelationFacadeProxy();

            QualifiedSubjectIdentifierType homeQualifiedSubjectId = new QualifiedSubjectIdentifierType();
            homeQualifiedSubjectId.setAssigningAuthorityIdentifier(assigningAuthId);
            homeQualifiedSubjectId.setSubjectIdentifier(getSessionBean1().getFoundPatient().getPatientId());
            RetrievePatientCorrelationsRequestType retrieveRequest = new RetrievePatientCorrelationsRequestType();
            retrieveRequest.setQualifiedPatientIdentifier(homeQualifiedSubjectId);
            retrieveRequest.setAssertion(getSessionBean1().getAssertionInfo());

            RetrievePatientCorrelationsResponseType pcResponse = pcProxy.retrievePatientCorrelations(retrieveRequest);
            List<QualifiedSubjectIdentifierType> retrievedPatCorrList = pcResponse.getQualifiedPatientIdentifier();

            if (retrievedPatCorrList != null && !retrievedPatCorrList.isEmpty()) {
                for (QualifiedSubjectIdentifierType qualSubject : retrievedPatCorrList) {
                    String remoteAssigningAuth = qualSubject.getAssigningAuthorityIdentifier();
                    String remotePatientId = qualSubject.getSubjectIdentifier();
                    HomeCommunityType remoteHomeCommunity = ConnectionManagerCommunityMapping.getHomeCommunityByAssigningAuthority(remoteAssigningAuth);
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
        } catch (PropertyAccessException ex) {
            log.error("Property file assess problem: " + ex.getMessage());
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

//        try {

//            EntitySubjectDiscoveryProxyObjectFactory sdFactory = new EntitySubjectDiscoveryProxyObjectFactory();
//            EntitySubjectDiscoveryProxy sdProxy = sdFactory.getEntitySubjectDiscoveryProxy();
//
//            PIXConsumerPRPAIN201301UVRequestType request = new PIXConsumerPRPAIN201301UVRequestType();
//            request.setAssertion(getSessionBean1().getAssertionInfo());
//
//            String localDeviceId = PropertyAccessor.getProperty(PROPERTY_FILE_NAME_GATEWAY, PROPERTY_FILE_KEY_LOCAL_DEVICE);
//            String orgId = PropertyAccessor.getProperty(PROPERTY_FILE_NAME_GATEWAY, PROPERTY_FILE_KEY_HOME_COMMUNITY);
//
            PatientSearchData foundPatient = getSessionBean1().getFoundPatient();
//            JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson(foundPatient.getFirstName(), foundPatient.getLastName(), foundPatient.getGender(), foundPatient.getDob(), foundPatient.getSsn());
//            //HL7PatientTransforms.create201302PatientPerson(foundPatient.getFirstName(), foundPatient.getLastName(), foundPatient.getGender(), foundPatient.getDob(), foundPatient.getSsn(), null);
//            PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, foundPatient.getPatientId(), localDeviceId);
//            PRPAIN201301UV02 prpain201301 = HL7PRPA201301Transforms.createPRPA201301(patient, localDeviceId, orgId, orgId);
//            request.setPRPAIN201301UV02(prpain201301);
//
//            MCCIIN000002UV01 sdAck = sdProxy.pixConsumerPRPAIN201301UV(request);
//
//            if (sdAck != null) {

                AssertionType assertion = getSessionBean1().getAssertionInfo();
                PatientDiscoveryClient patientDiscoveryClient = new PatientDiscoveryClient();
                patientDiscoveryClient.broadcastPatientDiscovery(assertion, foundPatient);

                performPatientCorrelation();
                this.getSubjectDiscoveryResultsInfo().setText("Broadcast Patient Discovery Results");

                GregorianCalendar cal = new GregorianCalendar();
                cal.setTimeZone(TimeZone.getTimeZone("GMT"));
                cal.setTimeInMillis(System.currentTimeMillis());

                this.getBroadcastInfo().setText("Broadcast sent: " + (cal.get(java.util.Calendar.MONTH) + 1) + "/" + cal.get(java.util.Calendar.DAY_OF_MONTH) + "/" + cal.get(java.util.Calendar.YEAR) + " " + cal.get(java.util.Calendar.HOUR_OF_DAY) + ":" + cal.get(java.util.Calendar.MINUTE) + ":" + cal.get(java.util.Calendar.SECOND) + " GMT");
                this.getBroadcastInfo2().setText("");

//            } else {
//                this.getBroadcastInfo().setText("Error in broadcast subject discovery");
//            }

//              return null;
//        } catch (PropertyAccessException ex) {
//            Logger.getLogger(Page2.class.getName()).log(Level.SEVERE, null, ex);
//        }


        return null;
    }

    /**
     * 
     * @return
     */
    public String getDocQueryResults() {

        this.errorMessage.setText("");
        //SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
        //System.out.println("Creation Date: " + dateFormatter.format(this.getCreationFromDate().getSelectedDate()));

        if(!isDocumentSearchCriteriaValid())
        {
            log.error("Error Message: " + errors);
            this.errorMessage.setText(errors);
            return null;
        }

        SearchData searchData = (SearchData) getBean("SearchData");

        PatientSearchData currentPatient = null;

        for (PatientSearchData testPatient : getPatientSearchDataList())
        {
            if (testPatient.getPatientId().equals(searchData.getPatientID()))
            {
                currentPatient = testPatient;
            }
        }
        
        //System.out.println("Patient ID: " + currentPatient.getPatientId() + "Assigning Authority ID: " + currentPatient.getAssigningAuthorityID());

        if (currentPatient == null)
        {
            this.errorMessage.setText("Patient information is not available. Please search again.");
        }

        DocumentQueryClient docQueryClient = new DocumentQueryClient();

        List<DocumentInformation> docInfoList = docQueryClient.retrieveDocumentsInformation(currentPatient, this.getCreationFromDate().getSelectedDate(),
                                                this.getCreationToDate().getSelectedDate());

        DocumentQueryResults documentQueryResults = new DocumentQueryResults();
        documentQueryResults.setDocuments(docInfoList);

        setBean("DocumentQueryResults", documentQueryResults);

        return null;
    }

    public String documentTab_action() {
        // TODO: Replace with your code
        this.errorMessage.setText("");
        return null;
    }

    private boolean isDocumentSearchCriteriaValid(){
        StringBuffer message = new StringBuffer();
        boolean isValid = true;

        if (this.creationFromDate == null || this.creationToDate == null){
            message.append("Earliest Date and Most Recent Date should not be null");
            isValid = false;
        }
        else if(this.creationFromDate.getSelectedDate() == null || this.getCreationToDate().getSelectedDate() == null){
            message.append("Earliest Date and Most Recent Date should not be null");
            isValid = false;
        }
        else if(this.creationFromDate.getSelectedDate().after(this.getCreationToDate().getSelectedDate())){
            message.append("Earliest Date should not be after Most Recent Date");
            isValid = false;
        }

        errors = message.toString();

        return isValid;
    }

    public String displayDocument() throws Exception{
        
        log.debug("Selected document ID: " + this.selectedDocumentID.getText());

        DocumentRetrieveClient docRetrieveClient = new DocumentRetrieveClient();

        String documentID = this.selectedDocumentID.getText().toString();

        DocumentQueryResults docQueryResults = (DocumentQueryResults) getBean("DocumentQueryResults");

        DocumentInformation currentDocument = null;

        for(DocumentInformation documentInformation:docQueryResults.getDocuments())
        {
            if (documentID.equals(documentInformation.getDocumentID()))
            {
                currentDocument = documentInformation;
                break;
            }
        }

        String document = docRetrieveClient.retriveDocument(currentDocument);

        if (document == null || document.isEmpty())
        {
            return "display_document_error";
        }

        InputStream xsl = getExternalContext().getResourceAsStream("/WEB-INF/CCD.xsl");

        String html = convertXMLToHTML(new ByteArrayInputStream(document.getBytes()), xsl);

        log.debug("HTML PAGE: " + html);
        
        if (html == null || html.isEmpty())
        {
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
        private String convertXMLToHTML(InputStream xml, InputStream xsl)
        {

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

}

