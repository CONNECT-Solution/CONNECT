/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universalclientgui;

import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.webui.jsf.component.Hyperlink;
import com.sun.webui.jsf.component.StaticText;
import com.sun.webui.jsf.component.Tab;
import com.sun.webui.jsf.component.TabSet;
import com.sun.webui.jsf.component.TextField;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201305UV;
import org.hl7.v3.PRPAIN201306UV;
import org.hl7.v3.PRPAIN201306UVMFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAMT201301UVPatient;
import org.hl7.v3.PRPAMT201310UVOtherIDs;
import org.hl7.v3.PRPAMT201310UVPatient;

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
    private static final String PROPERTY_FILE_NAME_GATEWAY = "gateway";
    private static final String PROPERTY_FILE_KEY_AGENCY = "AgencyName";
    private static final String PROPERTY_FILE_KEY_ASSIGN_AUTH = "assigningAuthorityId";
    private static final String PROPERTY_FILE_KEY_HOME_COMMUNITY = "localHomeCommunityId";
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
            String agencyName = PropertyAccessor.getProperty(PROPERTY_FILE_NAME_ADAPTER, PROPERTY_FILE_KEY_AGENCY);
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
        return null;
    }

    public String patientSearchButton_action() {

        this.getPatientSearchDataList().clear();
        this.patientInfo.setText("");
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
                PRPAMT201301UVPatient patient = HL7PatientTransforms.create201301Patient(HL7PatientTransforms.create201301PatientPerson(firstName, lastName, null, null, null), patId);
                PRPAIN201305UV searchPat = HL7PRPA201305Transforms.createPRPA201305(patient, orgId, orgId, assigningAuthId);

                AdapterMpiProxyObjectFactory mpiFactory = new AdapterMpiProxyObjectFactory();
                AdapterMpiProxy mpiProxy = mpiFactory.getAdapterMpiProxy();
                PRPAIN201306UV patients = mpiProxy.findCandidates(searchPat);

                List<PRPAMT201310UVPatient> mpiPatResultList = new ArrayList<PRPAMT201310UVPatient>();
                if (patients != null && patients.getControlActProcess() != null && patients.getControlActProcess().getSubject() != null) {
                    List<PRPAIN201306UVMFMIMT700711UV01Subject1> subjectList = patients.getControlActProcess().getSubject();
                    log.debug("Search MPI found " + subjectList.size() + " candidates");
                    for (PRPAIN201306UVMFMIMT700711UV01Subject1 subject1 : subjectList) {
                        if (subject1 != null &&
                                subject1.getRegistrationEvent() != null &&
                                subject1.getRegistrationEvent().getSubject1() != null &&
                                subject1.getRegistrationEvent().getSubject1().getPatient() != null) {
                            PRPAMT201310UVPatient mpiPat = subject1.getRegistrationEvent().getSubject1().getPatient();
                            mpiPatResultList.add(mpiPat);
                        }
                    }
                    if (!mpiPatResultList.isEmpty()) {
                        List<String> data = new ArrayList<String>();
                        for (PRPAMT201310UVPatient resultPatient : mpiPatResultList) {
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
                                if (resultPatient.getId() != null && !resultPatient.getId().isEmpty()) {
                                    for (II idxId : resultPatient.getId()) {
                                        if (idxId != null &&
                                                idxId.getExtension() != null &&
                                                idxId.getRoot() != null) {
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
                                    List<PRPAMT201310UVOtherIDs> ssnIdsList = resultPatient.getPatientPerson().getValue().getAsOtherIDs();
                                    for (PRPAMT201310UVOtherIDs ssnId : ssnIdsList) {
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

        PatientSearchData foundPatient = null;
        StringBuffer discoverInfoBuf = new StringBuffer();
        for (PatientSearchData testPatient : getPatientSearchDataList()) {
            if (testPatient.getPatientId().equals(matchPatientId)) {
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
                initializeSubjectDiscoveryTab(foundPatient);
                this.getClientTabSet().setSelected("subjectDiscoveryTab");

                //We found it - leave the loop
                break;
            }
        }
        if (foundPatient == null) {
            this.patientInfo.setText("Please select patient from the table.");
        }
        return null;
    }

    // Subject Discovery Tab Methods
    public String subjectDiscoveryTab_action() {
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

    private void initializeSubjectDiscoveryTab(PatientSearchData foundPatient) {
        this.getSubjectDiscoveryResultsInfo().setText("Subject Discovery Results (Existing Correlations)");
        this.getBroadcastInfo().setText("Click to Discover New Correlations.");
        this.getBroadcastInfo2().setText("Warning: This may take several minutes.");
        this.getPatientCorrelationList().clear();

        try {
            String assigningAuthId = PropertyAccessor.getProperty(PROPERTY_FILE_NAME_ADAPTER, PROPERTY_FILE_KEY_ASSIGN_AUTH);

            PatientCorrelationFacadeProxyObjectFactory pcFactory = new PatientCorrelationFacadeProxyObjectFactory();
            PatientCorrelationFacadeProxy pcProxy = pcFactory.getPatientCorrelationFacadeProxy();

            QualifiedSubjectIdentifierType homeQualifiedSubjectId = new QualifiedSubjectIdentifierType();
            homeQualifiedSubjectId.setAssigningAuthorityIdentifier(assigningAuthId);
            homeQualifiedSubjectId.setSubjectIdentifier(foundPatient.getPatientId());
            RetrievePatientCorrelationsRequestType retrieveRequest = new RetrievePatientCorrelationsRequestType();
            retrieveRequest.setQualifiedPatientIdentifier(homeQualifiedSubjectId);

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

    public String correlatedAuthorityLink_action() {
        System.out.println("Clicked Org Link: " + this.getCorrelatedAuthorityLink().getText());
        return null;
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
        this.getSubjectDiscoveryResultsInfo().setText("Broadcast Subject Discovery Results");
        this.getBroadcastInfo().setText("");
        this.getBroadcastInfo2().setText("");
        return null;
    }
}

