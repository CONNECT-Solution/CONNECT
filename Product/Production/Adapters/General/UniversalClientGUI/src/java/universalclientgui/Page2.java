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
import gov.hhs.fha.nhinc.adapterauthentication.proxy.AdapterAuthenticationProxy;
import gov.hhs.fha.nhinc.adapterauthentication.proxy.AdapterAuthenticationProxyObjectFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;

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

    /*private Select select;

    public Select getSelect() {
    return select;
    }

    public void setSelect(Select select) {
    this.select = select;
    }*/
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
        System.out.println("Page2.prerender Token= " + token);
        if (token == null || token.isEmpty()) {
            try {
                FacesContext context = FacesContext.getCurrentInstance();
                context.getExternalContext().redirect("Page1.jsp");
            } catch (IOException ex) {
                Logger.getLogger(Page2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.agencyLogo1.setText("Federal Agency");
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

    public String patientSearchButton_action() {
        String firstName = (String) firstNameField.getText();
        String lastName = (String) lastNameField.getText();

        StringBuffer searchInfoBuf = new StringBuffer("Searching for Patient: ");
        if (firstName != null && !firstName.isEmpty()) {
            searchInfoBuf.append(firstName + " ");
        }
        if (lastName != null && !lastName.isEmpty()) {
            searchInfoBuf.append(lastName + " ");
        }

        this.patientInfo.setText(searchInfoBuf.toString());

        List<String> data = new ArrayList<String>();
        data.add("Smith");
        data.add("Joe");
        data.add("1234");
        data.add("111-22-3333");
        data.add("06/08/1945");
        data.add("M");
        PatientSearchData patientDataJoe = new PatientSearchData(data);
        this.getPatientSearchDataList().add(patientDataJoe);

        data.clear();
        data.add("Smith");
        data.add("Sandy");
        data.add("987");
        data.add("666-55-4444");
        data.add("03/27/1949");
        data.add("F");
        PatientSearchData patientDataSandy = new PatientSearchData(data);
        this.getPatientSearchDataList().add(patientDataSandy);

        this.patientInfo.setText(" ");
        return null;
    }

    public String logOutButton_action() {
        String loginDecision = "login_required";
        getSessionBean1().setAuthToken("");
        return loginDecision;
    }

    public String subjectDiscoveryButton_action() {
        this.patientInfo.setText("Performing Subject Discovery on First Selected Patient");
        RowKey[] selectedRowKeys = getTableRowGroup1().getSelectedRowKeys();

        for (int i = 0; i < selectedRowKeys.length; i++) {
            int rowId = Integer.parseInt(selectedRowKeys[i].getRowId()) + 1;

            System.out.println("Row " + rowId + " is selected - with Key: " + selectedRowKeys[i] );

        }
        /*boolean isFound = false;
        for (PatientSearchData patient : getPatientSearchDataList()) {
            this.patientInfo.setText("Patient: " + patient.getFirstName() + " selected: " + patient.isSelectPatient());
        if (patient.getSelectPatient()) {
        if (isFound) {
        patient.setSelectPatient(false);
        } else {
        isFound = true;
        StringBuffer discoverInfoBuf = new StringBuffer();
        discoverInfoBuf.append("Name: ");
        if (!patient.getLastName().isEmpty()) {
        discoverInfoBuf.append(patient.getLastName() + ", ");
        }
        if (!patient.getFirstName().isEmpty()) {
        discoverInfoBuf.append(patient.getFirstName() + " ");
        }
        discoverInfoBuf.append("ID: ");
        if (!patient.getPatientId().isEmpty()) {
        discoverInfoBuf.append(patient.getPatientId() + " ");
        }
        discoverInfoBuf.append("SSN: ");
        if (!patient.getSsn().isEmpty()) {
        discoverInfoBuf.append(patient.getSsn() + " ");
        }
        discoverInfoBuf.append("DOB: ");
        if (!patient.getDob().isEmpty()) {
        discoverInfoBuf.append(patient.getDob() + " ");
        }
        discoverInfoBuf.append("Gender: ");
        if (!patient.getGender().isEmpty()) {
        discoverInfoBuf.append(patient.getGender() + " ");
        }
        this.patientInfo.setText(discoverInfoBuf.toString());
        }
        }*/
        
        return null;
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

