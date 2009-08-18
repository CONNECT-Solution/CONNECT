/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universalclientgui;

import com.sun.data.provider.RowKey;
import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.webui.jsf.component.Button;
import com.sun.webui.jsf.component.Label;
import com.sun.webui.jsf.component.RadioButton;
import com.sun.webui.jsf.component.StaticText;
import com.sun.webui.jsf.component.Tab;
import com.sun.webui.jsf.component.TabSet;
import com.sun.webui.jsf.component.Table;
import com.sun.webui.jsf.component.TableColumn;
import com.sun.webui.jsf.component.TableRowGroup;
import com.sun.webui.jsf.component.TextField;
import com.sun.webui.jsf.event.TableSelectPhaseListener;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.mpi.proxy.AdapterMpiProxy;
import gov.hhs.fha.nhinc.mpi.proxy.AdapterMpiProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.subdisc.HL7Extractors;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201305Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PatientTransforms;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
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
    private TabSet subjectDiscoveryTab = new TabSet();

    public TabSet getSubjectDiscoveryTab() {
        return subjectDiscoveryTab;
    }

    public void setSubjectDiscoveryTab(TabSet ts) {
        this.subjectDiscoveryTab = ts;
    }
    private Tab subjectDiscoveryTab2 = new Tab();

    public Tab getSubjectDiscoveryTab2() {
        return subjectDiscoveryTab2;
    }

    public void setSubjectDiscoveryTab2(Tab t) {
        this.subjectDiscoveryTab2 = t;
    }
    private Tab patientSearchTab = new Tab();

    public Tab getPatientSearchTab() {
        return patientSearchTab;
    }

    public void setPatientSearchTab(Tab t) {
        this.patientSearchTab = t;
    }
    private StaticText patientInfo = new StaticText();

    public StaticText getPatientInfo() {
        return patientInfo;
    }

    public void setPatientInfo(StaticText st) {
        this.patientInfo = st;
    }
    private TextField lastNameField = new TextField();

    public TextField getLastNameField() {
        return lastNameField;
    }

    public void setLastNameField(TextField tf) {
        this.lastNameField = tf;
    }
    private Label lastNameLabel = new Label();

    public Label getLastNameLabel() {
        return lastNameLabel;
    }

    public void setLastNameLabel(Label l) {
        this.lastNameLabel = l;
    }
    private Label firstNameLabel = new Label();

    public Label getFirstNameLabel() {
        return firstNameLabel;
    }

    public void setFirstNameLabel(Label l) {
        this.firstNameLabel = l;
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
    private Table table1 = new Table();

    public Table getTable1() {
        return table1;
    }

    public void setTable1(Table t) {
        this.table1 = t;
    }
    private TableRowGroup tableRowGroup1 = new TableRowGroup();

    public TableRowGroup getTableRowGroup1() {
        return tableRowGroup1;
    }

    public void setTableRowGroup1(TableRowGroup trg) {
        this.tableRowGroup1 = trg;
    }
    private TableColumn tableColumn1 = new TableColumn();

    public TableColumn getTableColumn1() {
        return tableColumn1;
    }

    public void setTableColumn1(TableColumn tc) {
        this.tableColumn1 = tc;
    }
    private TableColumn tableColumn2 = new TableColumn();

    public TableColumn getTableColumn2() {
        return tableColumn2;
    }

    public void setTableColumn2(TableColumn tc) {
        this.tableColumn2 = tc;
    }
    private TableColumn tableColumn3 = new TableColumn();

    public TableColumn getTableColumn3() {
        return tableColumn3;
    }

    public void setTableColumn3(TableColumn tc) {
        this.tableColumn3 = tc;
    }
    private TableColumn tableColumn4 = new TableColumn();

    public TableColumn getTableColumn4() {
        return tableColumn4;
    }

    public void setTableColumn4(TableColumn tc) {
        this.tableColumn4 = tc;
    }
    private TableColumn tableColumn5 = new TableColumn();

    public TableColumn getTableColumn5() {
        return tableColumn5;
    }

    public void setTableColumn5(TableColumn tc) {
        this.tableColumn5 = tc;
    }
    private TableColumn tableColumn6 = new TableColumn();

    public TableColumn getTableColumn6() {
        return tableColumn6;
    }

    public void setTableColumn6(TableColumn tc) {
        this.tableColumn6 = tc;
    }
    private TableColumn tableColumn7 = new TableColumn();

    public TableColumn getTableColumn7() {
        return tableColumn7;
    }

    public void setTableColumn7(TableColumn tc) {
        this.tableColumn7 = tc;
    }
    private Button subjectDiscoveryButton = new Button();

    public Button getSubjectDiscoveryButton() {
        return subjectDiscoveryButton;
    }

    public void setSubjectDiscoveryButton(Button b) {
        this.subjectDiscoveryButton = b;
    }
    private RadioButton radioButton1 = new RadioButton();

    public RadioButton getRadioButton1() {
        return radioButton1;
    }

    public void setRadioButton1(RadioButton rb) {
        this.radioButton1 = rb;
    }
    private StaticText agencyLogo1 = new StaticText();

    public StaticText getAgencyLogo1() {
        return agencyLogo1;
    }

    public void setAgencyLogo1(StaticText st) {
        this.agencyLogo1 = st;
    }
    private Tab documentDiscoveryTab = new Tab();

    public Tab getDocumentDiscoveryTab() {
        return documentDiscoveryTab;
    }

    public void setDocumentDiscoveryTab(Tab t) {
        this.documentDiscoveryTab = t;
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
            this.agencyLogo1.setText(agencyName);
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
        this.getSubjectDiscoveryTab().setSelected("patientSearchTab");
        
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

    public String patientSearchTab_action() {
        return null;
    }

    public String patientSearchButton_action() {

        this.getPatientSearchDataList().clear();
        this.patientInfo.setText("");
        deactivateSubjectDiscoveryTab();

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

    public String logOutButton_action() {
        String loginDecision = "login_required";
        getSessionBean1().setAuthToken("");
        getSessionBean1().getPatientSearchDataList().clear();
        return loginDecision;
    }

    public String subjectDiscoveryButton_action() {
        this.patientInfo.setText("Performing Subject Discovery on Selected Patient");
        RowKey[] selectedRowKeys = getTableRowGroup1().getSelectedRowKeys();

        if (selectedRowKeys.length > 0) {
            PatientSearchData foundPatient = null;
            StringBuffer discoverInfoBuf = new StringBuffer();
            for (int i = 0; i < selectedRowKeys.length && foundPatient == null; i++) {
                int rowId = Integer.parseInt(selectedRowKeys[i].getRowId());
                System.out.println("Row " + rowId + " is selected - with Key: " + selectedRowKeys[i]);
                foundPatient = this.getPatientSearchDataList().get(rowId);
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
                this.getSubjectDiscoveryTab().setSelected("subjectDiscoveryTab2");
            }
        } else {
            this.patientInfo.setText("Please select patient from the table.");
        }

        return null;
    }

    private void activateDocumentsTab() {
        this.getDocumentDiscoveryTab().setDisabled(false);
        String tabLabelStyle = this.getDocumentDiscoveryTab().getStyle();
        if (tabLabelStyle.contains("color: gray; ")) {
            String newStyle = tabLabelStyle.replace("color: gray; ", "");
            this.getDocumentDiscoveryTab().setStyle(newStyle);
        }
    }

    private void deactivateDocumentsTab() {
        String tabLabelStyle = this.getDocumentDiscoveryTab().getStyle();
        if (!tabLabelStyle.contains("color: gray; ")) {
            StringBuffer newStyle = new StringBuffer(tabLabelStyle);
            newStyle.insert(0, "color: gray; ");
            this.getDocumentDiscoveryTab().setStyle(newStyle.toString());
        }
        this.getDocumentDiscoveryTab().setDisabled(true);
    }

    private void activateSubjectDiscoveryTab() {
        this.getSubjectDiscoveryTab2().setDisabled(false);
        String tabLabelStyle = this.getSubjectDiscoveryTab2().getStyle();
        if (tabLabelStyle.contains("color: gray; ")) {
            String newStyle = tabLabelStyle.replace("color: gray; ", "");
            this.getSubjectDiscoveryTab2().setStyle(newStyle);
        }
    }

    private void deactivateSubjectDiscoveryTab() {
        String tabLabelStyle = this.getSubjectDiscoveryTab2().getStyle();
        if (!tabLabelStyle.contains("color: gray; ")) {
            StringBuffer newStyle = new StringBuffer(tabLabelStyle);
            newStyle.insert(0, "color: gray; ");
            this.getSubjectDiscoveryTab2().setStyle(newStyle.toString());
        }
        this.getSubjectDiscoveryTab2().setDisabled(true);
    }

    private TableSelectPhaseListener tablePhaseListener = new TableSelectPhaseListener();

    public void setSelected(Object object) {
        RowKey rowKey = (RowKey) getValue("#{currentRow.tableRow}");
        if (rowKey != null) {
            tablePhaseListener.setSelected(rowKey, object);
        }
    }

    public Object getSelected() {
        RowKey rowKey = (RowKey) getValue("#{currentRow.tableRow}");
        return tablePhaseListener.getSelected(rowKey);
    }

    public Object getSelectedValue() {
        RowKey rowKey = (RowKey) getValue("#{currentRow.tableRow}");
        return (rowKey != null) ? rowKey.getRowId() : null;
    }

    public boolean getSelectedState() {
        RowKey rowKey = (RowKey) getValue("#{currentRow.tableRow}");
        return tablePhaseListener.isSelected(rowKey);
    }
}

