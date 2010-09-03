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

import com.sun.rave.faces.data.DefaultSelectItemsArray;
import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.webui.jsf.component.Button;
import com.sun.webui.jsf.component.DropDown;
import com.sun.webui.jsf.component.Hyperlink;
import com.sun.webui.jsf.component.PanelLayout;
import com.sun.webui.jsf.component.RadioButtonGroup;
import com.sun.webui.jsf.component.StaticText;
import com.sun.webui.jsf.component.Tab;
import com.sun.webui.jsf.component.TabSet;
import com.sun.webui.jsf.component.Table;
import com.sun.webui.jsf.component.TextField;
import com.sun.webui.jsf.model.Option;
import com.sun.webui.jsf.model.SingleSelectOptionsList;
import gov.hhs.fha.nhinc.adapter.cppgui.CPPConstants;
import gov.hhs.fha.nhinc.adapter.cppgui.servicefacade.AdapterPIPFacade;
import gov.hhs.fha.nhinc.adapter.cppgui.ConsumerPreferencesSearchCriteria;
import gov.hhs.fha.nhinc.adapter.cppgui.valueobject.PatientPreferencesVO;
import gov.hhs.fha.nhinc.adapter.cppgui.PatientSearchCriteria;
import gov.hhs.fha.nhinc.adapter.cppgui.servicefacade.PatientSearchFacade;
import gov.hhs.fha.nhinc.adapter.cppgui.valueobject.PatientVO;
import gov.hhs.fha.nhinc.adapter.cppgui.UserSession;
import gov.hhs.fha.nhinc.adapter.cppgui.valueobject.FineGrainedPolicyCriterionVO;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import javax.faces.FacesException;
import javax.faces.component.UIParameter;
import javax.faces.component.html.HtmlSelectOneRadio;
import javax.faces.event.ValueChangeEvent;
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
 * @version SearchPatient.java
 * @version Created on Sep 10, 2009, 11:08:16 PM
 * @author patlollav
 */
public class SearchPatient extends AbstractPageBean {
    // <editor-fold defaultstate="collapsed" desc="Managed Component Definition">

    private static Log log = LogFactory.getLog(SearchPatient.class);

    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
    }
    private TextField firstName = new TextField();

    public TextField getFirstName() {
        return firstName;
    }

    public void setFirstName(TextField tf) {
        this.firstName = tf;
    }
    private TextField lastName = new TextField();

    public TextField getLastName() {
        return lastName;
    }

    public void setLastName(TextField tf) {
        this.lastName = tf;
    }
    private TextField identifier = new TextField();

    public TextField getIdentifier() {
        return identifier;
    }

    public void setIdentifier(TextField tf) {
        this.identifier = tf;
    }
    private Tab searchPatientTab = new Tab();

    public Tab getSearchPatientTab() {
        return searchPatientTab;
    }

    public void setSearchPatientTab(Tab searchPatientTab) {
        this.searchPatientTab = searchPatientTab;
    }
    private Tab patientPreferencesTab = new Tab();

    public Tab getPatientPreferencesTab() {
        return patientPreferencesTab;
    }

    public void setPatientPreferencesTab(Tab t) {
        this.patientPreferencesTab = t;
    }
    private StaticText patientName = new StaticText();

    public StaticText getPatientName() {
        return patientName;
    }

    public void setPatientName(StaticText st) {
        this.patientName = st;
    }
    private SingleSelectOptionsList optInDefaultOptions = new SingleSelectOptionsList();

    public SingleSelectOptionsList getOptInDefaultOptions() {
        Option option1 = new Option();
        option1.setLabel("NHIN Opt-In");
        option1.setValue("true");

        Option option2 = new Option();
        option2.setLabel("NHIN Opt-Out");
        option2.setValue("false");

        Option option3 = new Option();
        option3.setLabel("NHIN Opt-In Limited");
        option3.setValue("None");

        Option[] options = new Option[]{option1, option2, option3};
        optInDefaultOptions.setOptions(options);
        return optInDefaultOptions;
    }
    private UIParameter userSelectedPolicyOID = new UIParameter();

    public UIParameter getUserSelectedPolicyOID() {
        return userSelectedPolicyOID;
    }

    public void setUserSelectedPolicyOID(UIParameter userSelectedPolicyOID) {
        this.userSelectedPolicyOID = userSelectedPolicyOID;
    }



    public void setOptInDefaultOptions(SingleSelectOptionsList ssol) {
        this.optInDefaultOptions = ssol;
    }
    private DefaultSelectItemsArray permissionListDefaultItems = new DefaultSelectItemsArray();

    public DefaultSelectItemsArray getPermissionListDefaultItems() {
        permissionListDefaultItems.setItems(new String[]{"Permit", "Deny"});

        return permissionListDefaultItems;
    }

    public void setPermissionListDefaultItems(DefaultSelectItemsArray dsia) {
        this.permissionListDefaultItems = dsia;
    }
    private SingleSelectOptionsList documentTypeDefaultOptions = new SingleSelectOptionsList();

    public SingleSelectOptionsList getDocumentTypeDefaultOptions() {

        Option[] options = createOptionsFromPropertiesFile(CPPConstants.DOCUMENT_TYPE_CODE_PROPERTIES);
        documentTypeDefaultOptions.setOptions(options);

        return documentTypeDefaultOptions;
    }

    public void setDocumentTypeDefaultOptions(SingleSelectOptionsList ssol) {
        this.documentTypeDefaultOptions = ssol;
    }
    private SingleSelectOptionsList userRoleDefaultOptions = new SingleSelectOptionsList();

    public SingleSelectOptionsList getUserRoleDefaultOptions() {
        Option[] options = createOptionsFromPropertiesFile(CPPConstants.USER_ROLE_PROPERTIES);
        userRoleDefaultOptions.setOptions(options);

        return userRoleDefaultOptions;
    }

    public void setUserRoleDefaultOptions(SingleSelectOptionsList ssol) {
        this.userRoleDefaultOptions = ssol;
    }
    private SingleSelectOptionsList purposeOfUseDefaultOptions = new SingleSelectOptionsList();

    public SingleSelectOptionsList getPurposeOfUseDefaultOptions() {
        Option[] options = createOptionsFromPropertiesFile(CPPConstants.PURPOSE_OF_USE_PROPERTIES);
        purposeOfUseDefaultOptions.setOptions(options);

        return purposeOfUseDefaultOptions;
    }

    public void setPurposeOfUseDefaultOptions(SingleSelectOptionsList ssol) {
        this.purposeOfUseDefaultOptions = ssol;
    }
    private SingleSelectOptionsList confidentialityCodeDefaultOptions = new SingleSelectOptionsList();

    public SingleSelectOptionsList getConfidentialityCodeDefaultOptions() {
        Option[] options = createOptionsFromPropertiesFile(CPPConstants.CONFIDENTIALITY_CODE_PROPERTIES);
        confidentialityCodeDefaultOptions.setOptions(options);
        return confidentialityCodeDefaultOptions;
    }

    public void setConfidentialityCodeDefaultOptions(SingleSelectOptionsList ssol) {
        this.confidentialityCodeDefaultOptions = ssol;
    }
    private TabSet preferencesTab = new TabSet();

    public TabSet getPreferencesTab() {
        return preferencesTab;
    }

    public void setPreferencesTab(TabSet ts) {
        this.preferencesTab = ts;
    }
    private HtmlSelectOneRadio permission = new HtmlSelectOneRadio();

    public HtmlSelectOneRadio getPermission() {

        return permission;
    }

    public void setPermission(HtmlSelectOneRadio hsor) {
        this.permission = hsor;
    }
    private DropDown documentType = new DropDown();

    public DropDown getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DropDown dd) {
        this.documentType = dd;
    }
    private DropDown purposeOfUse = new DropDown();

    public DropDown getPurposeOfUse() {
        return purposeOfUse;
    }

    public void setPurposeOfUse(DropDown dd) {
        this.purposeOfUse = dd;
    }
    private DropDown userRole = new DropDown();

    public DropDown getUserRole() {
        return userRole;
    }

    public void setUserRole(DropDown dd) {
        this.userRole = dd;
    }
    private DropDown confidentialityCode = new DropDown();

    public DropDown getConfidentialityCode() {
        return confidentialityCode;
    }

    public void setConfidentialityCode(DropDown dd) {
        this.confidentialityCode = dd;
    }
    private Hyperlink policyOID = new Hyperlink();

    public Hyperlink getPolicyOID() {
        return policyOID;
    }

    public void setPolicyOID(Hyperlink policyOID) {
        this.policyOID = policyOID;
    }
    private RadioButtonGroup optIn = new RadioButtonGroup();

    public RadioButtonGroup getOptIn() {
        return optIn;
    }

    public void setOptIn(RadioButtonGroup rbg) {
        this.optIn = rbg;
    }
    private PanelLayout fineGrainedPolicyPrefPanel = new PanelLayout();

    public PanelLayout getFineGrainedPolicyPrefPanel() {
        return fineGrainedPolicyPrefPanel;
    }

    public void setFineGrainedPolicyPrefPanel(PanelLayout pl) {
        this.fineGrainedPolicyPrefPanel = pl;
    }
    private Table preferencesTable = new Table();

    public Table getPreferencesTable() {
        return preferencesTable;
    }

    public void setPreferencesTable(Table t) {
        this.preferencesTable = t;
    }
    private StaticText errorMessages = new StaticText();

    public StaticText getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(StaticText st) {
        this.errorMessages = st;
    }
    private Button savePreferences = new Button();

    public Button getSavePreferences() {
        return savePreferences;
    }

    public void setSavePreferences(Button b) {
        this.savePreferences = b;
    }

    // </editor-fold>
    /**
     * <p>Construct a new Page bean instance.</p>
     */
    public SearchPatient() {
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
            log("SearchPatient Initialization Failure", e);
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
        this.deactivatePatientPreferencesTab();
        UserSession userSession = (UserSession) getBean("UserSession");
        String token = userSession.getAuthToken();

        if (token == null || token.isEmpty()) {
            try {
                this.getExternalContext().redirect("UserLogin.jsp");
            } catch (IOException ex) {
                log.error("CPP can not prerender SearchPatient.jsp: " + ex.getMessage(), ex);
            }
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

    public String searchPatientButton_action() throws Exception {
        // TODO: Process the action. Return value is a navigation
        // case name where null will return to the same page.
        this.errorMessages.setText("");
        PatientSearchFacade patientSearchFacade = new PatientSearchFacade();

        PatientSearchCriteria criteria = createPatientSearchCriteria();

        List<PatientVO> searchResults = patientSearchFacade.searchPatient(criteria);

        UserSession userSession = (UserSession) getBean("UserSession");
        userSession.setSearchResults(searchResults);

        return null;
    }

    private PatientSearchCriteria createPatientSearchCriteria() {
        PatientSearchCriteria criteria = new PatientSearchCriteria();

        if (firstName != null && firstName.getText() != null) {
            criteria.setFirstName(firstName.getText().toString());
        }

        if (lastName != null && lastName.getText() != null) {
            criteria.setLastName(lastName.getText().toString());
        }

        if (identifier != null && identifier.getText() != null) {
            criteria.setPatientID(identifier.getText().toString());
        }


        return criteria;
    }
    private Hyperlink selectedPatientID = new Hyperlink();

    public Hyperlink getSelectedPatientID() {
        return selectedPatientID;
    }

    public void setSelectedPatientID(Hyperlink selectedPatientID) {
        this.selectedPatientID = selectedPatientID;
    }

    public String displayConsumerPreferences() {

        this.errorMessages.setText("");
        activatePatientPreferencesTab();
        this.getPreferencesTab().setSelected("patientPreferencesTab");

        String patientID = this.selectedPatientID.getText().toString();

        // Retrieve the patient from session
        UserSession userSession = (UserSession) getBean("UserSession");

        PatientVO selectedPatient = null;

        for (PatientVO patient : userSession.getSearchResults()) {
            if (patientID.contentEquals(patient.getPatientID())) {
                selectedPatient = patient;
                break;
            }
        }

        AdapterPIPFacade adapterPIPFacade = new AdapterPIPFacade();

        this.patientName.setText(selectedPatient.getFirstName() + " " + selectedPatient.getLastName());

        ConsumerPreferencesSearchCriteria criteria = createPreferencesSearchCriteria(selectedPatient.getPatientID(),
                selectedPatient.getAssigningAuthorityID());

        PatientPreferencesVO patientPreferences = adapterPIPFacade.retriveConsumerPreferences(criteria);

        selectedPatient.setPatientPreferences(patientPreferences);

        userSession.setPatient(selectedPatient);

        // Set the opt-in preferences

        if (Boolean.TRUE.equals(patientPreferences.getOptIn())) {
            this.optIn.setValue("true");
            this.preferencesTable.setVisible(false);
            this.fineGrainedPolicyPrefPanel.setVisible(false);
            this.savePreferences.setVisible(true);
        } else if (patientPreferences.getFineGrainedPolicyCriteria() != null && patientPreferences.getFineGrainedPolicyCriteria().size() > 0) {
            this.optIn.setValue("None");
            this.preferencesTable.setVisible(true);
            this.fineGrainedPolicyPrefPanel.setVisible(true);
            this.savePreferences.setVisible(false);
        } else {
            this.optIn.setValue("false");
            this.preferencesTable.setVisible(false);
            this.fineGrainedPolicyPrefPanel.setVisible(false);
            this.savePreferences.setVisible(true);
        }

        return null;
    }

    private ConsumerPreferencesSearchCriteria createPreferencesSearchCriteria(String patientID, String assigningAuthorityID) {
        ConsumerPreferencesSearchCriteria criteria = new ConsumerPreferencesSearchCriteria();
        criteria.setAssigningAuthorityID(assigningAuthorityID);
        criteria.setPatientID(patientID);

        return criteria;
    }

    public String preferencesTab_action() {
        // TODO: Process the action. Return value is a navigation
        // case name where null will return to the same page.
        this.errorMessages.setText("");
        return null;
    }

    public void optIn_processValueChange(ValueChangeEvent vce) {
        this.errorMessages.setText("");
        if ("None".equals(this.optIn.getSelected().toString())) {
            this.preferencesTable.setVisible(true);
            this.fineGrainedPolicyPrefPanel.setVisible(true);
            this.savePreferences.setVisible(false);
            this.resetFineGrainedPrefencesForm();
        } else {
            this.preferencesTable.setVisible(false);
            this.fineGrainedPolicyPrefPanel.setVisible(false);
            this.savePreferences.setVisible(true);
        }
    }

    private void activatePatientPreferencesTab() {
        this.patientPreferencesTab.setDisabled(false);
    /*
    String tabLabelStyle = this.getPatientPreferencesTab().getStyle();
    if (tabLabelStyle.contains("color: gray; ")) {
    String newStyle = tabLabelStyle.replace("color: gray; ", "");
    this.patientPreferencesTab.setStyle(newStyle);
    }
     */
    }

    private void deactivatePatientPreferencesTab() {
        /*
        String tabLabelStyle = this.patientPreferencesTab.getStyle();
        if (!tabLabelStyle.contains("color: gray; ")) {
        StringBuffer newStyle = new StringBuffer(tabLabelStyle);
        newStyle.insert(0, "color: gray; ");
        this.patientPreferencesTab.setStyle(newStyle.toString());
        }
         */
        this.patientPreferencesTab.setDisabled(true);
    }

    public String patientPreferencesTab_action() {
        // TODO: Process the action. Return value is a navigation
        // case name where null will return to the same page.
        return null;
    }

    /**
     * This saves only optIn value. It will not save fine grained policy preferences.
     * @return
     */
    public String savePreferences() {

        // TODO: Process the action. Return value is a navigation
        // case name where null will return to the same page.

        this.errorMessages.setText("");
        if (this.optIn.getSelected() == null) {
            this.errorMessages.setText("Please select an option");
            return null;
        }

        String userSelectedOptInValue = this.optIn.getSelected().toString();

        UserSession userSession = (UserSession) getBean("UserSession");
        PatientVO patientVO = userSession.getPatient();
        PatientPreferencesVO patientPreferencesVO = new PatientPreferencesVO();

        if (CPPConstants.TRUE.equalsIgnoreCase(userSelectedOptInValue)) {
            patientPreferencesVO.setOptIn(Boolean.TRUE);
        } else {
            patientPreferencesVO.setOptIn(Boolean.FALSE);
        }

        patientVO.setPatientPreferences(patientPreferencesVO);

        AdapterPIPFacade adapterPIPFacade = new AdapterPIPFacade();
        String status = adapterPIPFacade.saveOptInConsumerPreference(patientVO);

        if (CPPConstants.SUCCESS.equalsIgnoreCase(status)) {
            this.errorMessages.setText("Successfully saved Opt-In/Opt-Out preferences");
        } else {
            this.errorMessages.setText("Unable to save Opt-In/Opt-Out preferences");
        }

        // Refresh ths list
        ConsumerPreferencesSearchCriteria criteria = createPreferencesSearchCriteria(patientVO.getPatientID(),
                patientVO.getAssigningAuthorityID());
        PatientPreferencesVO patientPreferences = adapterPIPFacade.retriveConsumerPreferences(criteria);
        patientVO.setPatientPreferences(patientPreferences);

        return null;
    }

    public String addPreferences() {
        // TODO: Process the action. Return value is a navigation
        // case name where null will return to the same page.
        this.errorMessages.setText("");
        if (this.permission.getValue() == null || this.documentType.getValue() == null) {
            this.errorMessages.setText("Please select atleast Permission and Document Type to create a Fine Grained Policy");
            return null;
        }

        String selectedPermission = this.permission.getValue().toString();
        String selectedDocumentType = this.documentType.getValue().toString();

        String selectedUserRole = null;
        if (this.userRole.getValue() != null) {
            selectedUserRole = this.userRole.getValue().toString();
        }

        String selectedPurposeOfUse = null;
        if (this.purposeOfUse.getValue() != null) {
            selectedPurposeOfUse = this.purposeOfUse.getValue().toString();
        }

        String selectedConfidentialityCode = null;
        if (this.confidentialityCode.getValue() != null) {
            selectedConfidentialityCode = this.confidentialityCode.getValue().toString();
        }

        FineGrainedPolicyCriterionVO fineGrainedPolicyCriterion = new FineGrainedPolicyCriterionVO();

        fineGrainedPolicyCriterion.setPermit(selectedPermission);
        fineGrainedPolicyCriterion.setDocumentTypeCode(selectedDocumentType);
        fineGrainedPolicyCriterion.setUserRole(selectedUserRole);
        fineGrainedPolicyCriterion.setPurposeOfUse(selectedPurposeOfUse);
        fineGrainedPolicyCriterion.setConfidentialityCode(selectedConfidentialityCode);

        UserSession userSession = (UserSession) getBean("UserSession");
        PatientVO patientVO = userSession.getPatient();

        if (patientVO.getPatientPreferences() == null) {
            PatientPreferencesVO patientPreferencesVO = new PatientPreferencesVO();
            patientPreferencesVO.setOptIn(Boolean.FALSE);
            patientPreferencesVO.addFineGrainedPolicyCriterion(fineGrainedPolicyCriterion);
            patientVO.setPatientPreferences(patientPreferencesVO);

        } else {
            patientVO.getPatientPreferences().addFineGrainedPolicyCriterion(fineGrainedPolicyCriterion);
            patientVO.getPatientPreferences().setOptIn(Boolean.FALSE);
        }

        AdapterPIPFacade adapterPIPFacade = new AdapterPIPFacade();
        String status = adapterPIPFacade.saveOptInConsumerPreference(patientVO);

        if (CPPConstants.SUCCESS.equalsIgnoreCase(status)) {
            this.errorMessages.setText("Successfully saved Opt-In/Opt-Out preferences");
        } else {
            this.errorMessages.setText("Unable to save Opt-In/Opt-Out preferences");
        }

        resetFineGrainedPrefencesForm();
        
        return null;
    }

    public String deletePreferences() {
        this.errorMessages.setText("");
        UserSession userSession = (UserSession) getBean("UserSession");

        if (userSession.getSelectedPolicyOID() == null) {
            this.errorMessages.setText("Please select the Fine Grained Policy that needs to be deleted");
            return null;
        }

        String selectedPolicyOID = userSession.getSelectedPolicyOID();

        PatientVO patientVO = userSession.getPatient();

        if (patientVO.getPatientPreferences() == null ||
                patientVO.getPatientPreferences().getFineGrainedPolicyCriteria() == null ||
                patientVO.getPatientPreferences().getFineGrainedPolicyCriteria().size() < 1) {
            this.errorMessages.setText("Unable to delete this record as it does not exist");
            return null;
        } else {
            PatientPreferencesVO patientPreferences = patientVO.getPatientPreferences();

            for (FineGrainedPolicyCriterionVO fineGrainedPolicyCriterion : patientVO.getPatientPreferences().getFineGrainedPolicyCriteria()) {
                if (selectedPolicyOID.equalsIgnoreCase(fineGrainedPolicyCriterion.getPolicyOID())) {
                    patientPreferences.getFineGrainedPolicyCriteria().remove(fineGrainedPolicyCriterion);
                    break;
                }
            }
        }

        AdapterPIPFacade adapterPIPFacade = new AdapterPIPFacade();
        String status = adapterPIPFacade.saveOptInConsumerPreference(patientVO);

        if (CPPConstants.SUCCESS.equalsIgnoreCase(status)) {
            this.errorMessages.setText("Successfully saved Opt-In/Opt-Out preferences");
        } else {
            this.errorMessages.setText("Unable to save Opt-In/Opt-Out preferences");
        }

        resetFineGrainedPrefencesForm();

        return null;
    }

    public String updatePreferences() {
        // TODO: Process the action. Return value is a navigation
        // case name where null will return to the same page.
        // TODO: Process the action. Return value is a navigation
        // case name where null will return to the same page.
        this.errorMessages.setText("");
        UserSession userSession = (UserSession) getBean("UserSession");

        if (userSession.getSelectedPolicyOID() == null) {
            this.errorMessages.setText("Please select the Fine Grained Policy that needs to be updated");
            return null;
        }

        if (this.permission.getValue() == null || this.documentType.getValue() == null) {
            this.errorMessages.setText("Please select atleast Permission and Document Type to create a Fine Grained Policy");
            return null;
        }

        String selectedPolicyOID = userSession.getSelectedPolicyOID();
        String selectedPermission = this.permission.getValue().toString();
        String selectedDocumentType = this.documentType.getValue().toString();

        String selectedUserRole = null;
        if (this.userRole.getValue() != null) {
            selectedUserRole = this.userRole.getValue().toString();
        }

        String selectedPurposeOfUse = null;
        if (this.purposeOfUse.getValue() != null) {
            selectedPurposeOfUse = this.purposeOfUse.getValue().toString();
        }

        String selectedConfidentialityCode = null;
        if (this.confidentialityCode.getValue() != null) {
            selectedConfidentialityCode = this.confidentialityCode.getValue().toString();
        }


        PatientVO patientVO = userSession.getPatient();

        if (patientVO.getPatientPreferences() == null ||
                patientVO.getPatientPreferences().getFineGrainedPolicyCriteria() == null ||
                patientVO.getPatientPreferences().getFineGrainedPolicyCriteria().size() < 1) {
            this.errorMessages.setText("Unable to update this record as it does not exist");
            return null;
        } else {

            for (FineGrainedPolicyCriterionVO fineGrainedPolicyCriterion : patientVO.getPatientPreferences().getFineGrainedPolicyCriteria()) {
                if (selectedPolicyOID.equalsIgnoreCase(fineGrainedPolicyCriterion.getPolicyOID())) {
                    fineGrainedPolicyCriterion.setPolicyOID(selectedPolicyOID);
                    fineGrainedPolicyCriterion.setPermit(selectedPermission);
                    fineGrainedPolicyCriterion.setDocumentTypeCode(selectedDocumentType);
                    fineGrainedPolicyCriterion.setUserRole(selectedUserRole);
                    fineGrainedPolicyCriterion.setPurposeOfUse(selectedPurposeOfUse);
                    fineGrainedPolicyCriterion.setConfidentialityCode(selectedConfidentialityCode);
                    break;
                }
            }
        }

        AdapterPIPFacade adapterPIPFacade = new AdapterPIPFacade();
        String status = adapterPIPFacade.saveOptInConsumerPreference(patientVO);

        if (CPPConstants.SUCCESS.equalsIgnoreCase(status)) {
            this.errorMessages.setText("Successfully saved Opt-In/Opt-Out preferences");
        } else {
            this.errorMessages.setText("Unable to save Opt-In/Opt-Out preferences");
        }

        resetFineGrainedPrefencesForm();
        
        return null;
    }

    public String searchPatientTab_action() {
        // TODO: Process the action. Return value is a navigation
        // case name where null will return to the same page.
        this.errorMessages.setText("");
        return null;
    }

    public String displayFineGrainedPreferences() {
        this.errorMessages.setText("");

        //this.policyOID.setStyle("color: green; text-decoration: underline");

        //String selectedPolicyOID = this.policyOID.getText().toString();

        String selectedPolicyOID = this.userSelectedPolicyOID.getValue().toString();

        UserSession userSession = (UserSession) getBean("UserSession");
        PatientVO patientVO = userSession.getPatient();
        userSession.setSelectedPolicyOID(selectedPolicyOID);

        for (FineGrainedPolicyCriterionVO fineGrainedPolicyCriterion : patientVO.getPatientPreferences().getFineGrainedPolicyCriteria()) {
            if (selectedPolicyOID.equalsIgnoreCase(fineGrainedPolicyCriterion.getPolicyOID())) {
                this.permission.setValue(fineGrainedPolicyCriterion.getPermit());
                this.documentType.setValue(fineGrainedPolicyCriterion.getDocumentTypeCode());
                this.userRole.setValue(fineGrainedPolicyCriterion.getUserRole());
                this.purposeOfUse.setValue(fineGrainedPolicyCriterion.getPurposeOfUse());
                this.confidentialityCode.setValue(fineGrainedPolicyCriterion.getConfidentialityCode());
                break;
            }
        }

        return null;
    }

    public String logOut_action() {
        // TODO: Process the action. Return value is a navigation
        // case name where null will return to the same page.

        HttpSession session = (HttpSession) this.getExternalContext().getSession(false);
        session.invalidate();
        return "login_required";
    }

    /**
     * 
     * @param propertiesFile
     */
    private Option[] createOptionsFromPropertiesFile(String propertiesFile)
    {
        Properties properties = null;

        try
        {
            properties = PropertyAccessor.getProperties(propertiesFile);
        }
        catch (Exception e)
        {
            log.error("Exception while reading properties file: " + propertiesFile, e);
        }

        Option defaultOption = new Option();
        defaultOption.setLabel("Select an Option");
        defaultOption.setValue("");
        int index = 0;

        Option [] options = null;

        if (properties != null && properties.size()>0)
        {
            int size = properties.size() + 1;
            options = new Option[size];
            options[index] = defaultOption;

            for(String propertyKey : properties.stringPropertyNames())
            {
                Option option = new Option();
                option.setLabel(properties.getProperty(propertyKey));
                option.setValue(propertyKey);
                options[++index] = option;
            }
        }
        else
        {
            options = new Option[1];
            options[index] = defaultOption;
        }
        
        return options;
    }


    private void resetFineGrainedPrefencesForm() {
        this.permission.setValue("");
        this.documentType.setValue("");
        this.userRole.setValue("");
        this.purposeOfUse.setValue("");
        this.confidentialityCode.setValue("");
    }
}

