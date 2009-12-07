/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dodcustomerpreferencesprofilegui;

import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.webui.jsf.component.Body;
import com.sun.webui.jsf.component.Button;
import com.sun.webui.jsf.component.Checkbox;
import com.sun.webui.jsf.component.Form;
import com.sun.webui.jsf.component.Head;
import com.sun.webui.jsf.component.Html;
import com.sun.webui.jsf.component.Hyperlink;
import com.sun.webui.jsf.component.Label;
import com.sun.webui.jsf.component.Page;
import com.sun.webui.jsf.component.PanelLayout;
import com.sun.webui.jsf.component.StaticText;
import com.sun.webui.jsf.component.Table;
import com.sun.webui.jsf.component.TableColumn;
import com.sun.webui.jsf.component.TableRowGroup;
import com.sun.webui.jsf.component.TextField;
import javax.faces.FacesException;

//NHIN Imports
import java.util.*;
import gov.hhs.fha.nhinc.mpilib.*;
import gov.hhs.fha.nhinc.properties.*;
import gov.hhs.fha.nhinc.policyengine.adapterpip.proxy.*;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.*;
import gov.hhs.fha.nhinc.transform.subdisc.*;
import org.hl7.v3.*;
import gov.hhs.fha.nhinc.mpi.proxy.*;
import gov.hhs.fha.nhinc.nhinclib.*;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;


/**
 * <p>Page bean that corresponds to a similarly named JSP page.  This
 * class contains component definitions (and initialization code) for
 * all components that you have defined on this page, as well as
 * lifecycle methods and event handlers where you may add behavior
 * to respond to incoming events.</p>
 *
 * @version CPPMain.java
 * @version Created on Oct 3, 2009, 8:06:29 PM
 * @author Duane DeCouteau
 */

public class CPPMain extends AbstractPageBean {
    // <editor-fold defaultstate="collapsed" desc="Managed Component Definition">

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
    private Html html1 = new Html();

    public Html getHtml1() {
        return html1;
    }

    public void setHtml1(Html h) {
        this.html1 = h;
    }
    private Head head1 = new Head();

    public Head getHead1() {
        return head1;
    }

    public void setHead1(Head h) {
        this.head1 = h;
    }
    private Body body1 = new Body();

    public Body getBody1() {
        return body1;
    }

    public void setBody1(Body b) {
        this.body1 = b;
    }
    private Form form1 = new Form();

    public Form getForm1() {
        return form1;
    }

    public void setForm1(Form f) {
        this.form1 = f;
    }
    private PanelLayout layoutPanel1 = new PanelLayout();

    public PanelLayout getLayoutPanel1() {
        return layoutPanel1;
    }

    public void setLayoutPanel1(PanelLayout pl) {
        this.layoutPanel1 = pl;
    }
    private Label label1 = new Label();

    public Label getLabel1() {
        return label1;
    }

    public void setLabel1(Label l) {
        this.label1 = l;
    }
    private TextField lastNameFLD = new TextField();

    public TextField getLastNameFLD() {
        return lastNameFLD;
    }

    public void setLastNameFLD(TextField tf) {
        this.lastNameFLD = tf;
    }
    private Label label2 = new Label();

    public Label getLabel2() {
        return label2;
    }

    public void setLabel2(Label l) {
        this.label2 = l;
    }
    private TextField firstNameFLD = new TextField();

    public TextField getFirstNameFLD() {
        return firstNameFLD;
    }

    public void setFirstNameFLD(TextField tf) {
        this.firstNameFLD = tf;
    }
    private Label label4 = new Label();

    public Label getLabel4() {
        return label4;
    }

    public void setLabel4(Label l) {
        this.label4 = l;
    }
    private TextField idFLD = new TextField();

    public TextField getIdFLD() {
        return idFLD;
    }

    public void setIdFLD(TextField tf) {
        this.idFLD = tf;
    }
    private Button searchBTN = new Button();

    public Button getSearchBTN() {
        return searchBTN;
    }

    public void setSearchBTN(Button b) {
        this.searchBTN = b;
    }
    private Table table1 = new Table();

    public Table getTable1() {
        return table1;
    }

    public void setTable1(Table t) {
        this.table1 = t;
    }
    private Label label5 = new Label();

    public Label getLabel5() {
        return label5;
    }

    public void setLabel5(Label l) {
        this.label5 = l;
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
    private Hyperlink patientIDLink = new Hyperlink();

    public Hyperlink getPatientIDLink() {
        return patientIDLink;
    }

    public void setPatientIDLink(Hyperlink h) {
        this.patientIDLink = h;
    }
    private TableColumn tableColumn2 = new TableColumn();

    public TableColumn getTableColumn2() {
        return tableColumn2;
    }

    public void setTableColumn2(TableColumn tc) {
        this.tableColumn2 = tc;
    }
    private StaticText staticText2 = new StaticText();

    public StaticText getStaticText2() {
        return staticText2;
    }

    public void setStaticText2(StaticText st) {
        this.staticText2 = st;
    }
    private TableColumn tableColumn3 = new TableColumn();

    public TableColumn getTableColumn3() {
        return tableColumn3;
    }

    public void setTableColumn3(TableColumn tc) {
        this.tableColumn3 = tc;
    }
    private StaticText staticText3 = new StaticText();

    public StaticText getStaticText3() {
        return staticText3;
    }

    public void setStaticText3(StaticText st) {
        this.staticText3 = st;
    }
    private TableColumn tableColumn4 = new TableColumn();

    public TableColumn getTableColumn4() {
        return tableColumn4;
    }

    public void setTableColumn4(TableColumn tc) {
        this.tableColumn4 = tc;
    }
    private StaticText staticText4 = new StaticText();

    public StaticText getStaticText4() {
        return staticText4;
    }

    public void setStaticText4(StaticText st) {
        this.staticText4 = st;
    }
    private TableColumn tableColumn5 = new TableColumn();

    public TableColumn getTableColumn5() {
        return tableColumn5;
    }

    public void setTableColumn5(TableColumn tc) {
        this.tableColumn5 = tc;
    }
    private StaticText staticText5 = new StaticText();

    public StaticText getStaticText5() {
        return staticText5;
    }

    public void setStaticText5(StaticText st) {
        this.staticText5 = st;
    }
    private TableColumn tableColumn8 = new TableColumn();

    public TableColumn getTableColumn8() {
        return tableColumn8;
    }

    public void setTableColumn8(TableColumn tc) {
        this.tableColumn8 = tc;
    }
    private StaticText streetAddressCol = new StaticText();

    public StaticText getStreetAddressCol() {
        return streetAddressCol;
    }

    public void setStreetAddressCol(StaticText st) {
        this.streetAddressCol = st;
    }
    private TableColumn tableColumn7 = new TableColumn();

    public TableColumn getTableColumn7() {
        return tableColumn7;
    }

    public void setTableColumn7(TableColumn tc) {
        this.tableColumn7 = tc;
    }
    private Checkbox checkbox1 = new Checkbox();

    public Checkbox getCheckbox1() {
        return checkbox1;
    }

    public void setCheckbox1(Checkbox c) {
        this.checkbox1 = c;
    }
    private TableColumn tableColumn6 = new TableColumn();

    public TableColumn getTableColumn6() {
        return tableColumn6;
    }

    public void setTableColumn6(TableColumn tc) {
        this.tableColumn6 = tc;
    }
    private StaticText staticText1 = new StaticText();

    public StaticText getStaticText1() {
        return staticText1;
    }

    public void setStaticText1(StaticText st) {
        this.staticText1 = st;
    }

    // </editor-fold>

    /**
     * <p>Construct a new Page bean instance.</p>
     */
    public CPPMain() {
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
            log("CPPMain Initialization Failure", e);
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
    protected ApplicationBean1 getApplicationBean1() {
        return (ApplicationBean1) getBean("ApplicationBean1");
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected RequestBean1 getRequestBean1() {
        return (RequestBean1) getBean("RequestBean1");
    }

    private String token;
    private String patientFirstName;
    private String patientLastName;
    private String patientId;
    private String orgId;
    private String assigningAuthId;
    private String searchPatientId;
    private String streetAddress;

    private void setToolEnvironment() {
        try {
            token = getSessionBean1().getOpenSSOToken();
            orgId = PropertyAccessor.getProperty("gateway", "localHomeCommunityId");
            assigningAuthId = PropertyAccessor.getProperty("adapter", "assigningAuthorityId");
            System.out.println("CPPMain:Property Token "+token);
            System.out.println("CPPMain:Property OrgId "+orgId);
            System.out.println("CPPMain:Property AssigningAuthId "+assigningAuthId);
        }
        catch (PropertyAccessException px) {
            System.err.println("ERROR CPP: "+px.getMessage());
        }
    }

    private void populateSelectionTable() {
                II patId = new II();
                patId.setExtension(patientId);
                patId.setRoot(assigningAuthId);
                PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(HL7PatientTransforms.create201301PatientPerson(patientFirstName, patientLastName, null, null, null), patId);
                PRPAIN201305UV02 searchPat = HL7PRPA201305Transforms.createPRPA201305(patient, orgId, orgId, assigningAuthId);

                AdapterMpiProxyObjectFactory mpiFactory = new AdapterMpiProxyObjectFactory();
                AdapterMpiProxy mpiProxy = mpiFactory.getAdapterMpiProxy();
                PRPAIN201306UV02 patients = mpiProxy.findCandidates(searchPat);

                Patients pats = new Patients();
                Patient searchPatient = new Patient();

                  if (patients != null &&
                        patients.getControlActProcess() != null &&
                        NullChecker.isNotNullish(patients.getControlActProcess().getSubject()) &&
                        patients.getControlActProcess().getSubject().get(0) != null &&
                        patients.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                        patients.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                        patients.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null) {
                     PRPAMT201310UV02Patient mpiPatResult = patients.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient();
                     System.out.println("CPPMAIN:MPIPATRESULT: *****SUBJECT VALID******");

                     if (NullChecker.isNotNullish(mpiPatResult.getId()) &&
                             mpiPatResult.getId().get(0) != null &&
                             NullChecker.isNotNullish(mpiPatResult.getId().get(0).getExtension()) &&
                             NullChecker.isNotNullish(mpiPatResult.getId().get(0).getRoot())) {
                        searchPatient.getIdentifiers().add(mpiPatResult.getId().get(0).getExtension(), mpiPatResult.getId().get(0).getRoot());

                        System.out.println("CPPMAIN:INDENTIFIERS: ********IDENTIFIERS VALID********");
                     }

                     if (mpiPatResult.getPatientPerson() != null &&
                             mpiPatResult.getPatientPerson().getValue() != null &&
                             mpiPatResult.getPatientPerson().getValue().getName() != null) {
                         try {
                            PersonNameType name = HL7Extractors.translatePNListtoPersonNameType(mpiPatResult.getPatientPerson().getValue().getName());
                            PersonName personName = new PersonName();
                            personName.setFirstName(name.getGivenName());
                            personName.setLastName(name.getFamilyName());
                            searchPatient.setName(personName);
                            searchPatient.setFirstName(name.getGivenName());
                            searchPatient.setLastName(name.getFamilyName());
                            System.out.println("CPPMain:FIRSTNAME "+name.getGivenName());
                            System.out.println("CPPMain:LASTNAME "+name.getFamilyName());
                            System.out.println("CPPMain:STREET1 "+searchPatient.getAddress().getStreet1());
                            System.out.println("CPPMain:ADDRESS "+searchPatient.getAddress().getCity());
                            System.out.println("CPPMain:ADDRESS "+searchPatient.getAddress().getState());
                            System.out.println("CPPMain:ADDRESS "+searchPatient.getAddress().getZip());

                            System.out.println("CPPMAIN:PATIENTNAME: ***********NAME VALID***********");
                         }
                         catch (Exception ex) {
                             System.err.println("CPPMain:getPatientName "+ex.getMessage());
                         }
                     }


                     pats.add(searchPatient);
                }

                Patient[] searchList = new Patient[pats.size()];
                Iterator iter = pats.iterator();
                int x = 0;
                while (iter.hasNext()) {
                    Patient pat = (Patient)iter.next();
                    PersonName name = pat.getName();

                    pat.setOptedIn(false);

                    AdapterPIPProxyObjectFactory factory = new AdapterPIPProxyObjectFactory();
                    AdapterPIPProxy adapterPIPProxy = factory.getAdapterPIPProxy();
                    RetrievePtConsentByPtIdRequestType consentReq = new RetrievePtConsentByPtIdRequestType();

                    Identifiers newII = new Identifiers();
                    for (Identifier id : pat.getIdentifiers()) {
                        if (id.getOrganizationId().equals(assigningAuthId)) {
                            consentReq.setAssigningAuthority(id.getOrganizationId());
                            consentReq.setPatientId(id.getId());
                            RetrievePtConsentByPtIdResponseType consentResp = adapterPIPProxy.retrievePtConsentByPtId(consentReq);
                            if (consentResp.getPatientPreferences().isOptIn()) {
                                pat.setOptedIn(true);
                            }
                            newII.add(id);
                        }
                    }
                    pat.getIdentifiers().add(newII);
                    searchList[x] = pat;
                    x++;
                }
                getSessionBean1().setPatients(searchList);
    }



    /**
     * @return the searchPatientId
     */
    public String getSearchPatientId() {
        Identifiers idCol = (Identifiers)getValue("#{currentRow.value['identifiers']}");
        searchPatientId = "";
        Iterator iter = idCol.iterator();
        while (iter.hasNext()) {
            Identifier id = (Identifier)iter.next();
            searchPatientId = id.getId();
            setSearchPatientId(searchPatientId);
        }
        return searchPatientId;
    }

    /**
     * @param searchPatientId the searchPatientId to set
     */
    public void setSearchPatientId(String searchPatientId) {
        this.searchPatientId = searchPatientId;
    }

    /**
     * @return the streetAddress
     */
    public String getStreetAddress() {
        streetAddress = "";
        try {
            Address addressCol = (Address)getValue("#{currentRow.value['address']}");
            streetAddress = addressCol.getStreet1();
            setStreetAddress(streetAddress);
        }
        catch (Exception ex) {
            setStreetAddress("Unknown");
            streetAddress = "Unknown";
        }
        return streetAddress;
    }

    /**
     * @param streetAddress the streetAddress to set
     */
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String patientIDLink_action() {
        // TODO: Process the action. Return value is a navigation
        // case name where null will return to the same page.
        String pagenav = null;
        Patient p = new Patient();
        p.setAddress((Address)getValue("#{currentRow.value['address']}"));
        p.setDateOfBirth((String)getValue("#{currentRow.value['dateOfBirth']}"));
        p.setFirstName((String)getValue("#{currentRow.value['firstName']}"));
        p.setGender((String)getValue("#{currentRow.value['gender']}"));
        p.setIdentifiers((Identifiers)getValue("#{currentRow.value['identifiers']}"));
        p.setLastName((String)getValue("#{currentRow.value['lastName']}"));
        p.setOptedIn((Boolean)getValue("#{currentRow.value['optedIn']}"));
        p.setSSN((String)getValue("#{currentRow.value['lastName']}"));
        p.setName((PersonName)getValue("#{currentRow.value['name']}"));
        getSessionBean1().setSelectedPatient(p);

        //direct to option
        boolean optedIn = (Boolean)getValue("#{currentRow.value['optedIn']}");

        if (optedIn) {
            pagenav = "CPPOptOut";
        }
        else {
            pagenav = "CPPOptIn";
        }

        return pagenav;
    }

    public String searchBTN_action() {
        try {
            setToolEnvironment();
            resetFields();
            patientLastName = (String)lastNameFLD.getText();
            patientFirstName = (String)firstNameFLD.getText();
            patientId = (String)idFLD.getText();
            if (patientLastName != null && patientFirstName != null) {
                populateSelectionTable();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    private void resetFields() {
        patientLastName = "";
        patientFirstName = "";
        patientId = "";
        Patient[] emptyList = null;
        getSessionBean1().setPatients(emptyList);
    }
}

