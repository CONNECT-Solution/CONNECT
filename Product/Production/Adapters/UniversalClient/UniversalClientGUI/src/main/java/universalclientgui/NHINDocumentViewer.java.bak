/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universalclientgui;

import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.webui.jsf.component.Body;
import com.sun.webui.jsf.component.Button;
import com.sun.webui.jsf.component.Checkbox;
import com.sun.webui.jsf.component.Form;
import com.sun.webui.jsf.component.Head;
import com.sun.webui.jsf.component.Html;
import com.sun.webui.jsf.component.Hyperlink;
import com.sun.webui.jsf.component.Label;
import com.sun.webui.jsf.component.Meta;
import com.sun.webui.jsf.component.Page;
import com.sun.webui.jsf.component.PanelLayout;
import com.sun.webui.jsf.component.ProgressBar;
import com.sun.webui.jsf.component.StaticText;
import com.sun.webui.jsf.component.Tab;
import com.sun.webui.jsf.component.TabSet;
import com.sun.webui.jsf.component.Table;
import com.sun.webui.jsf.component.TableColumn;
import com.sun.webui.jsf.component.TableRowGroup;
import gov.hhs.fha.nhinc.universalclientgui.interfaces.DocumentAccessManager;
import gov.hhs.fha.nhinc.universalclientgui.objects.DocumentManagerObjectManagement;
import gov.hhs.fha.nhinc.universalclientgui.objects.TabManagement;
import ihe.iti.xds_b._2007.DocumentManagerService;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import java.util.ArrayList;
import java.util.Iterator;
import org.netbeans.xml.schema.docviewer.RetrievedDocumentDisplayObject;
import javax.faces.FacesException;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.event.ValueChangeEvent;
import java.util.List;
import javax.xml.ws.BindingProvider;
import gov.hhs.fha.nhinc.adapter.commondatalayer.DoDConnectorPortType;
import gov.hhs.fha.nhinc.adapter.commondatalayer.DoDConnectorService;
import java.net.URL;
import javax.xml.namespace.QName;
import org.hl7.v3.PatientDemographicsPRPAIN201307UV02RequestType;
import org.hl7.v3.PatientDemographicsPRPAMT201303UV02ResponseType;

/**
 * <p>Page bean that corresponds to a similarly named JSP page.  This
 * class contains component definitions (and initialization code) for
 * all components that you have defined on this page, as well as
 * lifecycle methods and event handlers where you may add behavior
 * to respond to incoming events.</p>
 *
 * @version NHINDocumentViewer.java
 * @version Created on Oct 12, 2009, 4:40:10 PM
 * @author Duane DeCouteau
 */

public class NHINDocumentViewer extends AbstractPageBean {
    private DocumentManagerService service;
    //@WebServiceRef(wsdlLocation = "WEB-INF/wsdl/client/DocViewerRequestServicesService/DocViewerRequestServicesService.wsdl")
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
    private TabSet tabSet1 = new TabSet();

    public TabSet getTabSet1() {
        return tabSet1;
    }

    public void setTabSet1(TabSet ts) {
        this.tabSet1 = ts;
    }
    private Tab inboxTab = new Tab();

    public Tab getInboxTab() {
        return inboxTab;
    }

    public void setInboxTab(Tab t) {
        this.inboxTab = t;
    }
    private PanelLayout layoutPanel1 = new PanelLayout();

    public PanelLayout getLayoutPanel1() {
        return layoutPanel1;
    }

    public void setLayoutPanel1(PanelLayout pl) {
        this.layoutPanel1 = pl;
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
    private Checkbox displaySelectedCHK = new Checkbox();

    public Checkbox getDisplaySelectedCHK() {
        return displaySelectedCHK;
    }

    public void setDisplaySelectedCHK(Checkbox c) {
        this.displaySelectedCHK = c;
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
    private Button viewSelectedBTN = new Button();

    public Button getViewSelectedBTN() {
        return viewSelectedBTN;
    }

    public void setViewSelectedBTN(Button b) {
        this.viewSelectedBTN = b;
    }
    private Button selectAllAndViewBTN = new Button();

    public Button getSelectAllAndViewBTN() {
        return selectAllAndViewBTN;
    }

    public void setSelectAllAndViewBTN(Button b) {
        this.selectAllAndViewBTN = b;
    }
    private Button resetDisplayBTN = new Button();

    public Button getResetDisplayBTN() {
        return resetDisplayBTN;
    }

    public void setResetDisplayBTN(Button b) {
        this.resetDisplayBTN = b;
    }
    private ProgressBar progressBar = new ProgressBar();

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar pb) {
        this.progressBar = pb;
    }
    private PanelLayout retrieveCompletePanel = new PanelLayout();

    public PanelLayout getRetrieveCompletePanel() {
        return retrieveCompletePanel;
    }

    public void setRetrieveCompletePanel(PanelLayout pl) {
        this.retrieveCompletePanel = pl;
    }
    private Hyperlink retrievalCompleteCloseLink = new Hyperlink();

    public Hyperlink getRetrievalCompleteCloseLink() {
        return retrievalCompleteCloseLink;
    }

    public void setRetrievalCompleteCloseLink(Hyperlink h) {
        this.retrievalCompleteCloseLink = h;
    }
    private PanelLayout noNewDocumentsPanel = new PanelLayout();

    public PanelLayout getNoNewDocumentsPanel() {
        return noNewDocumentsPanel;
    }

    public void setNoNewDocumentsPanel(PanelLayout pl) {
        this.noNewDocumentsPanel = pl;
    }
    private Hyperlink closeNoNewDocsLink = new Hyperlink();

    public Hyperlink getCloseNoNewDocsLink() {
        return closeNoNewDocsLink;
    }

    public void setCloseNoNewDocsLink(Hyperlink h) {
        this.closeNoNewDocsLink = h;
    }
    private Tab tab1 = new Tab();

    public Tab getTab1() {
        return tab1;
    }

    public void setTab1(Tab t) {
        this.tab1 = t;
    }
    private PanelLayout layoutPanel2 = new PanelLayout();

    public PanelLayout getLayoutPanel2() {
        return layoutPanel2;
    }

    public void setLayoutPanel2(PanelLayout pl) {
        this.layoutPanel2 = pl;
    }
    private HtmlOutputText displayArea1 = new HtmlOutputText();

    public HtmlOutputText getDisplayArea1() {
        return displayArea1;
    }

    public void setDisplayArea1(HtmlOutputText hot) {
        this.displayArea1 = hot;
    }
    private Tab tab2 = new Tab();

    public Tab getTab2() {
        return tab2;
    }

    public void setTab2(Tab t) {
        this.tab2 = t;
    }
    private PanelLayout layoutPanel5 = new PanelLayout();

    public PanelLayout getLayoutPanel5() {
        return layoutPanel5;
    }

    public void setLayoutPanel5(PanelLayout pl) {
        this.layoutPanel5 = pl;
    }
    private HtmlOutputText displayArea2 = new HtmlOutputText();

    public HtmlOutputText getDisplayArea2() {
        return displayArea2;
    }

    public void setDisplayArea2(HtmlOutputText hot) {
        this.displayArea2 = hot;
    }
    private Tab tab3 = new Tab();

    public Tab getTab3() {
        return tab3;
    }

    public void setTab3(Tab t) {
        this.tab3 = t;
    }
    private PanelLayout layoutPanel6 = new PanelLayout();

    public PanelLayout getLayoutPanel6() {
        return layoutPanel6;
    }

    public void setLayoutPanel6(PanelLayout pl) {
        this.layoutPanel6 = pl;
    }
    private HtmlOutputText displayArea3 = new HtmlOutputText();

    public HtmlOutputText getDisplayArea3() {
        return displayArea3;
    }

    public void setDisplayArea3(HtmlOutputText hot) {
        this.displayArea3 = hot;
    }
    private Tab tab4 = new Tab();

    public Tab getTab4() {
        return tab4;
    }

    public void setTab4(Tab t) {
        this.tab4 = t;
    }
    private PanelLayout layoutPanel3 = new PanelLayout();

    public PanelLayout getLayoutPanel3() {
        return layoutPanel3;
    }

    public void setLayoutPanel3(PanelLayout pl) {
        this.layoutPanel3 = pl;
    }
    private HtmlOutputText displayArea4 = new HtmlOutputText();

    public HtmlOutputText getDisplayArea4() {
        return displayArea4;
    }

    public void setDisplayArea4(HtmlOutputText hot) {
        this.displayArea4 = hot;
    }
    private Tab tab5 = new Tab();

    public Tab getTab5() {
        return tab5;
    }

    public void setTab5(Tab t) {
        this.tab5 = t;
    }
    private PanelLayout layoutPanel4 = new PanelLayout();

    public PanelLayout getLayoutPanel4() {
        return layoutPanel4;
    }

    public void setLayoutPanel4(PanelLayout pl) {
        this.layoutPanel4 = pl;
    }
    private HtmlOutputText displayArea5 = new HtmlOutputText();

    public HtmlOutputText getDisplayArea5() {
        return displayArea5;
    }

    public void setDisplayArea5(HtmlOutputText hot) {
        this.displayArea5 = hot;
    }
    private Tab tab6 = new Tab();

    public Tab getTab6() {
        return tab6;
    }

    public void setTab6(Tab t) {
        this.tab6 = t;
    }
    private PanelLayout layoutPanel7 = new PanelLayout();

    public PanelLayout getLayoutPanel7() {
        return layoutPanel7;
    }

    public void setLayoutPanel7(PanelLayout pl) {
        this.layoutPanel7 = pl;
    }
    private HtmlOutputText displayArea6 = new HtmlOutputText();

    public HtmlOutputText getDisplayArea6() {
        return displayArea6;
    }

    public void setDisplayArea6(HtmlOutputText hot) {
        this.displayArea6 = hot;
    }
    private Tab tab7 = new Tab();

    public Tab getTab7() {
        return tab7;
    }

    public void setTab7(Tab t) {
        this.tab7 = t;
    }
    private PanelLayout layoutPanel8 = new PanelLayout();

    public PanelLayout getLayoutPanel8() {
        return layoutPanel8;
    }

    public void setLayoutPanel8(PanelLayout pl) {
        this.layoutPanel8 = pl;
    }
    private HtmlOutputText displayArea7 = new HtmlOutputText();

    public HtmlOutputText getDisplayArea7() {
        return displayArea7;
    }

    public void setDisplayArea7(HtmlOutputText hot) {
        this.displayArea7 = hot;
    }
    private Tab tab8 = new Tab();

    public Tab getTab8() {
        return tab8;
    }

    public void setTab8(Tab t) {
        this.tab8 = t;
    }
    private PanelLayout layoutPanel9 = new PanelLayout();

    public PanelLayout getLayoutPanel9() {
        return layoutPanel9;
    }

    public void setLayoutPanel9(PanelLayout pl) {
        this.layoutPanel9 = pl;
    }
    private HtmlOutputText displayArea8 = new HtmlOutputText();

    public HtmlOutputText getDisplayArea8() {
        return displayArea8;
    }

    public void setDisplayArea8(HtmlOutputText hot) {
        this.displayArea8 = hot;
    }
    private Tab tab9 = new Tab();

    public Tab getTab9() {
        return tab9;
    }

    public void setTab9(Tab t) {
        this.tab9 = t;
    }
    private PanelLayout layoutPanel10 = new PanelLayout();

    public PanelLayout getLayoutPanel10() {
        return layoutPanel10;
    }

    public void setLayoutPanel10(PanelLayout pl) {
        this.layoutPanel10 = pl;
    }
    private HtmlOutputText displayArea9 = new HtmlOutputText();

    public HtmlOutputText getDisplayArea9() {
        return displayArea9;
    }

    public void setDisplayArea9(HtmlOutputText hot) {
        this.displayArea9 = hot;
    }
    private Tab tab10 = new Tab();

    public Tab getTab10() {
        return tab10;
    }

    public void setTab10(Tab t) {
        this.tab10 = t;
    }
    private PanelLayout layoutPanel11 = new PanelLayout();

    public PanelLayout getLayoutPanel11() {
        return layoutPanel11;
    }

    public void setLayoutPanel11(PanelLayout pl) {
        this.layoutPanel11 = pl;
    }
    private HtmlOutputText displayArea10 = new HtmlOutputText();

    public HtmlOutputText getDisplayArea10() {
        return displayArea10;
    }

    public void setDisplayArea10(HtmlOutputText hot) {
        this.displayArea10 = hot;
    }
    private Label progressBarStatusLBL = new Label();

    public Label getProgressBarStatusLBL() {
        return progressBarStatusLBL;
    }

    public void setProgressBarStatusLBL(Label l) {
        this.progressBarStatusLBL = l;
    }
    private Hyperlink docOrgLink = new Hyperlink();

    public Hyperlink getDocOrgLink() {
        return docOrgLink;
    }

    public void setDocOrgLink(Hyperlink h) {
        this.docOrgLink = h;
    }
    private StaticText staticText1 = new StaticText();

    public StaticText getStaticText1() {
        return staticText1;
    }

    public void setStaticText1(StaticText st) {
        this.staticText1 = st;
    }
    private PanelLayout layoutPanel12 = new PanelLayout();

    public PanelLayout getLayoutPanel12() {
        return layoutPanel12;
    }

    public void setLayoutPanel12(PanelLayout pl) {
        this.layoutPanel12 = pl;
    }
    private Meta meta1 = new Meta();

    public Meta getMeta1() {
        return meta1;
    }

    public void setMeta1(Meta m) {
        this.meta1 = m;
    }

    // </editor-fold>

    /**
     * <p>Construct a new Page bean instance.</p>
     */
    public NHINDocumentViewer() {
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
            log("NHINDocumentViewer Initialization Failure", e);
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
        try {
            String patientId = (String)getExternalContext().getRequestParameterMap().get("patientId");
            String providerId = (String)getExternalContext().getRequestParameterMap().get("providerId");

            //declare DoDConnector Service variable
            DoDConnectorService pawsservice;

            System.out.println("NHINDocumentViewer:prerender patientId: "+patientId+" providerId: "+providerId);
            // handle for automatic page refresh, progressbar, and incorrect input
            if (getSessionBean1().getPatientId() == null) {
                if (patientId != null && providerId != null) {
                    if (!patientId.equals("null") && !providerId.equals("null")) {

                        //make paws web service call here for patient and provider info

                        /**
                         * Note:  Typically, a system that calls this class will pull parameters from a parameter
                         *        list contained within the URL.
                         */

                        System.out.println("NHINDocumentViewer - Decrypting Query Parameters");

                        //decrypt query parameters
                        CryptAHLTA cipherOBJ = new CryptAHLTA();
                        patientId = cipherOBJ.decrypt(patientId);
                        providerId = cipherOBJ.decrypt(providerId);
                        
                        System.out.println("NHINDocumentViewer - Decrypted patientId = " + patientId);
                        System.out.println("NHINDocumentViewer - Decrypted providerId = " + providerId);

                        //retreieve patient info
                        System.out.println("NHINDocumentViewer - Instantiating DOD Connector Service (" + getApplicationBean1().getDoDConnectorWSDLURL() + ")...");

                        pawsservice = new DoDConnectorService(new URL(getApplicationBean1().getDoDConnectorWSDLURL()), new QName(getApplicationBean1().getDoDConnectorQNAME(), getApplicationBean1().getDoDConnectorService()));

                        System.out.println("NHINDocumentViewer - Retrieving the port from the following service: " + pawsservice.toString());

                        DoDConnectorPortType port = pawsservice.getCommonDataLayerPort();

                        //build patient info request
                        org.hl7.v3.PatientDemographicsPRPAIN201307UV02RequestType patientReqType = buildPawsRequest(patientId);
                        
                        System.out.println("NHINDocumentViewer - Invoking getPatientInfo Operation: " + pawsservice.toString());
                        PatientDemographicsPRPAMT201303UV02ResponseType patientInfoResponse = port.getPatienInfo(patientReqType);

                        //response variables
                        String patientDOB = "";
                        String patientFirstName = "";
                        String patientLastName = "";
                        String patientGender = "";

                        if (patientInfoResponse != null)
                        {
                            System.out.println("NHINDocumentViewer - getPatientInfo response returned from PAWS");

                            patientDOB = patientInfoResponse.getSubject().getPatientPerson().getValue().getBirthTime().getValue();

                            javax.xml.bind.JAXBElement<org.hl7.v3.EnExplicitGiven> eeg = (javax.xml.bind.JAXBElement<org.hl7.v3.EnExplicitGiven>) patientInfoResponse.getSubject().getPatientPerson().getValue().getName().get(0).getContent().get(1);
                            patientFirstName = eeg.getValue().getContent();

                            javax.xml.bind.JAXBElement<org.hl7.v3.EnExplicitFamily> eef = (javax.xml.bind.JAXBElement<org.hl7.v3.EnExplicitFamily>) patientInfoResponse.getSubject().getPatientPerson().getValue().getName().get(0).getContent().get(0);
                            patientLastName = eef.getValue().getContent();

                            patientGender = patientInfoResponse.getSubject().getPatientPerson().getValue().getAdministrativeGenderCode().getCode();

                            System.out.println("NHINDocumentViewer - SessionBean Patient Date of Birth = " + patientDOB);
                            System.out.println("NHINDocumentViewer - SessionBean Patient First Name = " + patientFirstName);
                            System.out.println("NHINDocumentViewer - SessionBean Patient Last Name = " + patientLastName);
                            System.out.println("NHINDocumentViewer - SessionBean Patient Gender = " + patientGender);

                        }

                        //patient
                        getSessionBean1().setPatientId(patientId);

                        if (patientDOB != null)
                            getSessionBean1().setPatientDOB(patientDOB);
                        else
                            getSessionBean1().setPatientDOB("");

                        if (patientFirstName != null)
                            getSessionBean1().setPatientFirstName(patientFirstName);
                        else
                            getSessionBean1().setPatientFirstName("");

                        if (patientLastName != null)
                            getSessionBean1().setPatientLastName(patientLastName);
                        else
                            getSessionBean1().setPatientLastName("");

                        getSessionBean1().setPatientMiddleInitial("");
                        
                        getSessionBean1().setPatientName(patientLastName + "," + patientFirstName);
                        
                        if (patientGender != null)
                            getSessionBean1().setPatientGender(patientGender);
                        else
                            getSessionBean1().setPatientGender("");

                        //end:patient info

                        //set results in sessionBean ...comment out following used for my testing
                        //provider

                        //build provider info request
                        org.hl7.v3.PatientDemographicsPRPAIN201307UV02RequestType providerReqType = buildPawsRequest(providerId);


                        System.out.println("NHINDocumentViewer - Invoking getProviderInfo Operation: " + pawsservice.toString());
                        PatientDemographicsPRPAMT201303UV02ResponseType providerInfoResponse = port.getProviderInfo(providerReqType);

                        String providerFirstName="";
                        String providerLastName = "";
                        String providerMiddleName = "";
                        String providerFullName = "";

                        if (providerInfoResponse != null)
                        {
                            System.out.println("NHINDocumentViewer - getProviderInfo response returned from PAWS");

                            //parse names
                            javax.xml.bind.JAXBElement<org.hl7.v3.EnExplicitFamily> eef = (javax.xml.bind.JAXBElement<org.hl7.v3.EnExplicitFamily>) providerInfoResponse.getSubject().getPatientPerson().getValue().getName().get(0).getContent().get(0);
                            providerLastName = eef.getValue().getContent();

                            javax.xml.bind.JAXBElement<org.hl7.v3.EnExplicitGiven> eeg = (javax.xml.bind.JAXBElement<org.hl7.v3.EnExplicitGiven>) providerInfoResponse.getSubject().getPatientPerson().getValue().getName().get(0).getContent().get(1);
                            providerFirstName = eeg.getValue().getContent();

                            javax.xml.bind.JAXBElement<org.hl7.v3.EnExplicitDelimiter> eed = (javax.xml.bind.JAXBElement<org.hl7.v3.EnExplicitDelimiter>) providerInfoResponse.getSubject().getPatientPerson().getValue().getName().get(0).getContent().get(2);
                            providerMiddleName = eed.getValue().getContent();

                            javax.xml.bind.JAXBElement<org.hl7.v3.EnExplicitSuffix> ees = (javax.xml.bind.JAXBElement<org.hl7.v3.EnExplicitSuffix>) providerInfoResponse.getSubject().getPatientPerson().getValue().getName().get(0).getContent().get(3);
                            providerFullName = ees.getValue().getContent();

                            System.out.println("NHINDocumentViewer - SessionBean Provider Last Name = " + providerLastName);
                            System.out.println("NHINDocumentViewer - SessionBean Provider First Name = " + providerFirstName);
                            System.out.println("NHINDocumentViewer - SessionBean Provider Middle Name = " + providerMiddleName);
                            System.out.println("NHINDocumentViewer - SessionBean Provider Full Name = " + providerFullName);

                        }

                        getSessionBean1().setUserName("NHINUser");
                        getSessionBean1().setProviderId(providerId);
                        
                        if (providerFullName != null)
                            getSessionBean1().setProviderName(providerFullName);
                        else
                            getSessionBean1().setProviderName("");
                        
                        if (providerFirstName != null)
                            getSessionBean1().setProviderFirstName(providerFirstName);
                        else
                            getSessionBean1().setProviderFirstName("");

                        if (providerLastName != null)
                             getSessionBean1().setProviderLastName(providerLastName);
                        else
                            getSessionBean1().setProviderLastName("");

                        if (providerMiddleName != null)
                             getSessionBean1().setProviderMiddleNameOrInitial(providerMiddleName);
                        else
                            getSessionBean1().setProviderMiddleNameOrInitial("");

                        getSessionBean1().setProviderRole(getApplicationBean1().getUserRoleDisplayNameDefault());
                        getSessionBean1().setPurposeOfUse(getApplicationBean1().getDefaultPurposeOfUse());

                       

                        // launch background thread for document request via Document AccessManager

                        String homecommunityid = getApplicationBean1().getHomeCommunityId();
                        String homecommunityname = getApplicationBean1().getHomeCommunityName();
                        String homecommunitydesc = getApplicationBean1().getHomeCommunityDesc();
                        String patientid = getSessionBean1().getPatientId();
                        String patientdob = getSessionBean1().getPatientDOB();
                        String patientfirstname = getSessionBean1().getPatientFirstName();
                        String patientlastname = getSessionBean1().getPatientLastName();
                        String patientmiddleinitial = getSessionBean1().getPatientMiddleInitial();
                        String username = getSessionBean1().getUserName();
                        String role = getSessionBean1().getProviderRole();
                        String providerid = getSessionBean1().getProviderId();
                        String providername = getSessionBean1().getProviderName();
                        String purposeofuse = getSessionBean1().getPurposeOfUse();
                        String assigningauth = getApplicationBean1().getAssigningAuthorityId();

                        String providerlastname = getSessionBean1().getProviderLastName();
                        String providerfirstname = getSessionBean1().getProviderFirstName();
                        String providermiddlename = getSessionBean1().getProviderMiddleNameOrInitial();

                        String rolecode = getApplicationBean1().getUserRoleCode();
                        String rolesystem = getApplicationBean1().getUserRoleCodeSystem();
                        String rolesystemname = getApplicationBean1().getUserRoleCodeSystemName();
                        String rolesystemversion = getApplicationBean1().getUserRoleCodeSystemVersion();

                        String purposeofusecode = getApplicationBean1().getPurposeOfUseCode();
                        String purposeofusecodesystem = getApplicationBean1().getPurposeOfUseCodeSystem();
                        String purposeofusecodesystemname = getApplicationBean1().getPurposeOfUseCodeSystemName();
                        String purposeofusecodesystemversion = getApplicationBean1().getPurposeOfUseCodeSystemVersion();

                        String claimformref = getApplicationBean1().getClaimFormRef();
                        String claimformraw = getApplicationBean1().getClaimFormString();
                        String signaturedate = getApplicationBean1().getSignatureDate();
                        String expirationdate = getApplicationBean1().getExpirationDate();

                        int numofyears = getApplicationBean1().getNumberOfYears();

                        try {
                            DocumentAccessManager da = new DocumentAccessManager(homecommunityid, homecommunityname, homecommunitydesc,
                                                   patientid, patientdob, patientfirstname, patientlastname,
                                                   patientmiddleinitial, role, username, providername,
                                                   purposeofuse, assigningauth, providerid, providerlastname, providerfirstname, providermiddlename,
                                                   rolecode, rolesystem, rolesystemname, rolesystemversion,
                                                   purposeofusecode, purposeofusecodesystem, purposeofusecodesystemname,
                                                   purposeofusecodesystemversion, claimformref, claimformraw,
                                                   signaturedate, expirationdate, numofyears);

                            //save assertion because we will use in temporarily for direct nhin viewing
                            getSessionBean1().setAssertion(da.createAssertion());
                            //remember to remove above;
                            da.start();
                        }
                        catch (Exception dx) {
                            dx.printStackTrace();
                        }
                    }
                }
            }
            //always check current activity....
            if (!getSessionBean1().isDocumentsAvailable()) {
                populateSessionArray();
            }
            if (isProgressBarVisible()) {
                //state changes here
            }

        }
        catch (Exception ep) {
            ep.printStackTrace();
            System.err.println("NHINDocViewer:prerender "+ep.getMessage());
        }
    }

    /**
     *  build getPatientInfo Request
     */
    org.hl7.v3.PatientDemographicsPRPAIN201307UV02RequestType buildPawsRequest(String pId)
    {
        //II
        org.hl7.v3.II ii = new org.hl7.v3.II();
        ii.setExtension(pId);

        //PatientIdentfier
        org.hl7.v3.PRPAMT201307UVPatientIdentifier id = new org.hl7.v3.PRPAMT201307UVPatientIdentifier();
        id.getValue().add(ii);

        //Parameter List
        org.hl7.v3.PRPAMT201307UVParameterList paramList = new org.hl7.v3.PRPAMT201307UVParameterList();
        paramList.getPatientIdentifier().add(id);

        //Query By Parameter
        org.hl7.v3.PRPAMT201307UVQueryByParameter qparams = new org.hl7.v3.PRPAMT201307UVQueryByParameter();
        qparams.setParameterList(paramList);

        //objectfactory
        org.hl7.v3.ObjectFactory obf = new org.hl7.v3.ObjectFactory();

        //control act process
        org.hl7.v3.PRPAIN201307UV02QUQIMT021001UV01ControlActProcess controlActProcess = new org.hl7.v3.PRPAIN201307UV02QUQIMT021001UV01ControlActProcess();
        controlActProcess.setQueryByParameter(obf.createPRPAIN201307UV02QUQIMT021001UV01ControlActProcessQueryByParameter(qparams));

        //message
        org.hl7.v3.PRPAIN201307UV02MCCIMT000100UV01Message patientMessage = new org.hl7.v3.PRPAIN201307UV02MCCIMT000100UV01Message();
        patientMessage.setControlActProcess(controlActProcess);

        //request and query
        PatientDemographicsPRPAIN201307UV02RequestType request = new PatientDemographicsPRPAIN201307UV02RequestType();
        request.setQuery(patientMessage);

        return request;
   
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
    protected ApplicationBean1 getApplicationBean1() {
        return (ApplicationBean1) getBean("ApplicationBean1");
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected SessionBean1 getSessionBean1() {
        return (SessionBean1) getBean("SessionBean1");
    }

    public String retrievalCompleteCloseLink_action() {
        // TODO: Process the action. Return value is a navigation
        // case name where null will return to the same page.
        return null;
    }

    public String closeNoNewDocsLink_action() {
        // TODO: Process the action. Return value is a navigation
        // case name where null will return to the same page.
        return null;
    }

    public String viewSelectedBTN_action() {
        // TODO: Process the action. Return value is a navigation
        // case name where null will return to the same page.
        try {
            removeAllDisplayTabs();
            RetrievedDocumentDisplayObject[] objs = getSessionBean1().getAvailableDocuments();
            int tabIndex = 1;
            if (objs != null && objs.length > 0) getSessionBean1().setRefreshRate("300");
            for (int x = 0; x < objs.length; x++) {
                RetrievedDocumentDisplayObject obj = (RetrievedDocumentDisplayObject)objs[x];
                String title = obj.getOrganizationName()+" "+obj.getDocumentType();
                String docid = obj.getUniqueDocumentId();
                String repid = obj.getRepositoryId();
                String home = obj.getOrgId();
                if (!obj.isHasBeenAccessed()) title = "*" + title;
                boolean displayTab = obj.isSelected();
                if (displayTab) {
                    String tabName = "tab" + tabIndex;
                    getTabSet1().findChildTab(tabName).setText(title);
                    getTabSet1().findChildTab(tabName).setVisible(true);
                    getTabSet1().findChildTab(tabName).setRendered(true);
                    TabManagement tObj = getSessionBean1().getTabList().get(tabIndex - 1);
                    tObj.setDocumentId(docid);
                    tObj.setRepositoryId(repid);
                    tObj.setHomeCommunityId(home);
                    getSessionBean1().getTabList().set(tabIndex - 1, tObj);
                    tabIndex++;
                    if (tabIndex == 10) {
                        break;
                    }
                }
            }
        }
        catch (Exception ex) {

        }
        return null;
    }

    public String selectAllAndViewBTN_action() {
        // TODO: Process the action. Return value is a navigation
        // case name where null will return to the same page.
        try {
            removeAllDisplayTabs();
            RetrievedDocumentDisplayObject[] objs = getSessionBean1().getAvailableDocuments();
            //set all to selected
            for (int i = 0; i < objs.length; i++) {
                RetrievedDocumentDisplayObject dObj = (RetrievedDocumentDisplayObject)objs[i];
                if (dObj.getDocumentStatus().equals("Available")) {
                    dObj.setSelected(true);
                    objs[i] = dObj;
                }
            }
            getSessionBean1().setAvailableDocuments(objs);
            int tabIndex = 1;
            if (objs != null && objs.length > 0) getSessionBean1().setRefreshRate("300");
            for (int x = 0; x < objs.length; x++) {
                RetrievedDocumentDisplayObject obj = (RetrievedDocumentDisplayObject)objs[x];
                String title = obj.getOrganizationName()+" "+obj.getDocumentType();
                String docid = obj.getUniqueDocumentId();
                String repid = obj.getRepositoryId();
                String home = obj.getOrgId();
                if (!obj.isHasBeenAccessed()) title = "*" + title;
                boolean displayTab = obj.isSelected();
                if (displayTab) {
                    String tabName = "tab" + tabIndex;
                    getTabSet1().findChildTab(tabName).setText(title);
                    getTabSet1().findChildTab(tabName).setVisible(true);
                    getTabSet1().findChildTab(tabName).setRendered(true);
                    TabManagement tObj = getSessionBean1().getTabList().get(tabIndex - 1);
                    tObj.setDocumentId(docid);
                    tObj.setRepositoryId(repid);
                    tObj.setHomeCommunityId(home);
                    getSessionBean1().getTabList().set(tabIndex - 1, tObj);
                    tabIndex++;
                    if (tabIndex == 10) {
                        break;
                    }
                }
            }
        }
        catch (Exception ex) {

        }
        return null;
    }

    public String resetDisplayBTN_action() {
        // TODO: Process the action. Return value is a navigation
        // case name where null will return to the same page.
        try {
            RetrievedDocumentDisplayObject[] objs = getSessionBean1().getAvailableDocuments();
            for (int x = 0; x < objs.length; x++) {
                ((RetrievedDocumentDisplayObject)objs[x]).setSelected(false);
            }
            removeAllDisplayTabs();
            getSessionBean1().setRefreshRate("30");
        }
        catch (Exception ex) {
            System.err.println("NHINDocumentViewer:resetDisplayBTN_action "+ex.getMessage());
        }
        return null;
    }
    private void removeAllDisplayTabs() {
        int tabcount = getTabSet1().getChildCount();
        for (int x = 1; x < tabcount; x++) {
            String tabName = "tab" + x;
            getTabSet1().findChildTab(tabName).setVisible(false);
            getTabSet1().findChildTab(tabName).setRendered(false);
        }
    }
    
    private void removeSpecificDisplayTab(String org, String docType, String rawxml, String docId) {
       int tabcount = getTabSet1().getChildCount();
       String tTitle = org + " - " + docType;
       for (int x = 1; x < tabcount; x++) {
           String tabName = "tab" + x;
           String tabTitle = (String)getTabSet1().findChildTab(tabName).getText();
           if (tabTitle.equals(tTitle)) {
               getTabSet1().findChildTab(tabName).setVisible(false);
               getTabSet1().findChildTab(tabName).setRendered(false);
           }
       }
    }

    private void addNewDisplayTab(String org, String docType, String rawxml, String docId) {
        
    }

    private void populateTable() {
        DocumentManagerObjectManagement[] dList = getSessionBean1().getPatientDocumentSubList(getSessionBean1().getPatientId());
        RetrievedDocumentDisplayObject[] rList = new RetrievedDocumentDisplayObject[dList.length];
        for (int x = 0; x < dList.length; x++) {
            DocumentManagerObjectManagement dObj = (DocumentManagerObjectManagement)dList[x];
            RetrievedDocumentDisplayObject rObj = new RetrievedDocumentDisplayObject();
            rObj.setAvailableInLocalStore(dObj.isProcessingComplete());
            rObj.setDocumentTitle("Unknown");
            rObj.setDocumentType("C32");
            rObj.setOrgId(dObj.getSourceHomeCommunityId());
            rObj.setOrganizationName(dObj.getSourceHomeCommunityName());
            rObj.setSelected(false);
            rObj.setUniqueDocumentId(dObj.getLocalDocumentId());
            rList[x] = rObj;
        }
        getSessionBean1().setAvailableDocuments(rList);
    }

    public void displaySelectedCHK_processValueChange(ValueChangeEvent event) {
    }

    public String docOrgLink_action() {
        // TODO: Process the action. Return value is a navigation
        // case name where null will return to the same page.
        try {
            RetrievedDocumentDisplayObject[] objs = getSessionBean1().getAvailableDocuments();
            for (int x = 0; x < objs.length; x++) {
                ((RetrievedDocumentDisplayObject)objs[x]).setSelected(false);
            }
            removeAllDisplayTabs();
            //select and add just one
            String documentId = (String)getValue("#{currentRow.value['uniqueDocumentId']}");
            for (int x = 0; x < objs.length; x++) {
                RetrievedDocumentDisplayObject obj = (RetrievedDocumentDisplayObject)objs[x];
                String docId = obj.getUniqueDocumentId();
                if (docId.equals(documentId)) {
                    ((RetrievedDocumentDisplayObject)objs[x]).setSelected(true);
                }
            }
            getSessionBean1().setAvailableDocuments(objs);
            int tabIndex = 1;
            for (int x = 0; x < objs.length; x++) {
                RetrievedDocumentDisplayObject obj = (RetrievedDocumentDisplayObject)objs[x];
                String title = obj.getOrganizationName()+" "+obj.getDocumentType();
                if (!obj.isHasBeenAccessed()) title = "*" + title;
                boolean displayTab = obj.isSelected();
                if (displayTab) {
                    String docid = obj.getUniqueDocumentId();
                    String repid = obj.getRepositoryId();
                    String home = obj.getOrgId();
                    String tabName = "tab" + tabIndex;
                    getTabSet1().findChildTab(tabName).setText(title);
                    getTabSet1().findChildTab(tabName).setVisible(true);
                    getTabSet1().findChildTab(tabName).setRendered(true);
                    TabManagement tObj = getSessionBean1().getTabList().get(tabIndex - 1);
                    tObj.setDocumentId(docid);
                    tObj.setRepositoryId(repid);
                    tObj.setHomeCommunityId(home);
                    getSessionBean1().getTabList().set(tabIndex - 1, tObj);
                    tabIndex++;
                    if (tabIndex == 10) {
                        break;
                    }
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private boolean checkBoxEnabled;

    /**
     * @return the checkBoxEnabled
     */
    public boolean isCheckBoxEnabled() {
        String dstatus = (String)getValue("#{currentRow.value['documentStatus']}");
        if ("Pending".equals(dstatus)) checkBoxEnabled = true;
        if ("Downloading".equals(dstatus)) checkBoxEnabled = true;
        if ("Available".equals(dstatus)) checkBoxEnabled = false;
        setCheckBoxEnabled(checkBoxEnabled);
        return checkBoxEnabled;
    }

    /**
     * @param checkBoxEnabled the checkBoxEnabled to set
     */
    public void setCheckBoxEnabled(boolean checkBoxEnabled) {
        this.checkBoxEnabled = checkBoxEnabled;
    }

    private boolean progressBarVisible;

    /**
     * @return the progressBarVisible
     */
    public boolean isProgressBarVisible() {
        RetrievedDocumentDisplayObject[] objs = getSessionBean1().getAvailableDocuments();
        boolean stillprocessing = false;
        boolean newdocumentsfound = false;
        boolean statusbarvisible = false;
        if (objs != null) {
            for (int x = 0; x < objs.length; x++) {
                RetrievedDocumentDisplayObject obj = (RetrievedDocumentDisplayObject)objs[x];
                String status = obj.getDocumentStatus();
                boolean hasbeenaccessed = obj.isHasBeenAccessed();
                if ("Pending".equals(status) || "Downloading".equals(status)) {
                    stillprocessing = true;
                }
                if (!hasbeenaccessed) {
                    newdocumentsfound = true;
                }
            }
            if (!stillprocessing) {
                progressBarVisible = false;
                setProgressBarVisible(false);
                getSessionBean1().setRefreshRate("300");
                getSessionBean1().setDocumentsAvailable(true);
                if (newdocumentsfound) {
                    getProgressBarStatusLBL().setText("Retrieval Complete - documents are available below.");
                }
                else {
                    getProgressBarStatusLBL().setText("No new document(s) are available on NHIN.  The most recent are available below.");
                }
            }
            else {
                progressBarVisible = true;
                setProgressBarVisible(true);
                getProgressBarStatusLBL().setText("New document(s) are being retrieved from NHIN.  Once retrieved, they will be available below.");
            }
        }
        else {
            progressBarVisible = true;
            setProgressBarVisible(true);
            getProgressBarStatusLBL().setText("New document(s) are being retrieved from NHIN.  Once retrieved, they will be available below.");
        }
        return progressBarVisible;
    }

    /**
     * @param progressBarVisible the progressBarVisible to set
     */
    public void setProgressBarVisible(boolean progressBarVisible) {
        this.progressBarVisible = progressBarVisible;
    }

    private void populateSessionArray() {
        try { // Call Web Service Operation
            String DV_SERVICE_ENDPOINT = getApplicationBean1().getDocViewerRequestService();
            gov.hhs.fha.nhinc.universalclient.ws.DocViewerRequestServicesService dvservice = new gov.hhs.fha.nhinc.universalclient.ws.DocViewerRequestServicesService();
            gov.hhs.fha.nhinc.universalclient.ws.DocViewerRequestServicesPortType port = dvservice.getDocViewerRequestServicesPort();
            ((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, DV_SERVICE_ENDPOINT);

            org.netbeans.xml.schema.docviewer.DocViewerStatusRequestType request = new org.netbeans.xml.schema.docviewer.DocViewerStatusRequestType();
            request.setPatientId(getSessionBean1().getPatientId());
            request.setUserId(getSessionBean1().getProviderId());

            org.netbeans.xml.schema.docviewer.DocViewerStatusResponseType res =  port.getNHINRequestStatus(request);

            List<org.netbeans.xml.schema.docviewer.RetrievedDocumentDisplayObject> lRes = res.getDisplayObjects();
            if (lRes == null || lRes.size() == 0) {
                RetrievedDocumentDisplayObject[] aRes = new RetrievedDocumentDisplayObject[lRes.size()];
                lRes.toArray(aRes);
                getSessionBean1().setAvailableDocuments(aRes);
            }
            else {
                //compare to existing and update
                //if session array is null then just add it
                RetrievedDocumentDisplayObject[] objs = getSessionBean1().getAvailableDocuments();
                if (objs == null) {
                    RetrievedDocumentDisplayObject[] aRes = new RetrievedDocumentDisplayObject[lRes.size()];
                    lRes.toArray(aRes);
                    getSessionBean1().setAvailableDocuments(aRes);
                }
                else {
                    List<RetrievedDocumentDisplayObject> nList = new ArrayList();
                    Iterator iter = lRes.iterator();
                    while (iter.hasNext()) {
                        RetrievedDocumentDisplayObject sObj = (RetrievedDocumentDisplayObject)iter.next();
                        String id = sObj.getUniqueDocumentId();
                        String rId = sObj.getRepositoryId();
                        String hId = sObj.getOrgId();
                        String status = sObj.getDocumentStatus();
                        String origId = sObj.getOrigDocumentId();
                        String origRepId = sObj.getOrigRespositoryId();
                        String origHC = sObj.getOrigHomeCommunityId();
                        System.out.println("NHINDocViewer DocId "+id+" "+rId+" "+hId+" "+status);

                        boolean found = false;
                        for (int i = 0; i < objs.length; i++) {
                            RetrievedDocumentDisplayObject bObj = (RetrievedDocumentDisplayObject)objs[i];
                            String bId = bObj.getUniqueDocumentId();
                            String bRId = bObj.getRepositoryId();
                            String bOId = bObj.getOrgId();
                            String bStatus = bObj.getDocumentStatus();
                            if (id.equals(bId) && rId.equals(bRId) && hId.equals(bOId)) {
                                bObj.setDocumentStatus(status);
                                nList.add(bObj);
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            //then check if we are looking at results after results are store in local respository
                            // base it on original identifiables
                            boolean found2 = false;
                            for (int i = 0; i < objs.length; i++) {
                                RetrievedDocumentDisplayObject bObj = (RetrievedDocumentDisplayObject)objs[i];
                                String oId = bObj.getUniqueDocumentId();
                                String oRId = bObj.getRepositoryId();
                                String oOId = bObj.getOrgId();
                                String bStatus = bObj.getDocumentStatus();
                                //eval original meta data
                                if (origId.equals(oId) && origRepId.equals(oRId) && origHC.equals(oOId)) {
                                    //set local inbound repository values
                                    bObj.setUniqueDocumentId(id);
                                    bObj.setRepositoryId(rId);
                                    bObj.setOrgId(hId);
                                    bObj.setDocumentStatus(status);
                                    nList.add(bObj);
                                    found2 = true;
                                    break;
                                }
                            }
                            if (!found2) {
                                nList.add(sObj);
                            }
                        }
                    }
                    RetrievedDocumentDisplayObject[] aRes = new RetrievedDocumentDisplayObject[lRes.size()];
                    nList.toArray(aRes);
                    getSessionBean1().setAvailableDocuments(aRes);
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void retrieveInboundDocument() {
        RetrieveDocumentSetRequestType request = new RetrieveDocumentSetRequestType();
        DocumentRequest dRequest = new DocumentRequest();
        dRequest.setDocumentUniqueId(getSessionBean1().getSelectedDocumentId());
        dRequest.setRepositoryUniqueId(getSessionBean1().getSelectedRespositoryId());
        dRequest.setHomeCommunityId(getSessionBean1().getSelectedHomeCommunityId());
        System.out.println("NHINDocViewer DocId "+getSessionBean1().getSelectedDocumentId()+" "+getSessionBean1().getSelectedRespositoryId()+" "+getSessionBean1().getSelectedHomeCommunityId());
        request.getDocumentRequest().add(dRequest);
        try {
            ihe.iti.xds_b._2007.DocumentManagerService dmservice = new ihe.iti.xds_b._2007.DocumentManagerService();
            ihe.iti.xds_b._2007.DocumentManagerPortType port = dmservice.getDocumentManagerPortSoap();
            ((javax.xml.ws.BindingProvider)port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getApplicationBean1().getDocumentManagerService());

            RetrieveDocumentSetResponseType res = port.documentManagerRetrieveInboundDocument(request);
            byte[] rawxml = res.getDocumentResponse().get(0).getDocument();
            getSessionBean1().setSelectedNHINDocument(new String(rawxml));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String tab1_action() {
        // TODO: Process the action. Return value is a navigation
        // case name where null will return to the same page.
        TabManagement t = getSessionBean1().getTabList().get(0);
        getSessionBean1().setSelectedDocumentId(t.getDocumentId());
        getSessionBean1().setSelectedRespositoryId(t.getRepositoryId());
        getSessionBean1().setSelectedHomeCommunityId(t.getHomeCommunityId());
        retrieveInboundDocument();
        return null;
    }

    public String tab2_action() {
        TabManagement t = getSessionBean1().getTabList().get(1);
        getSessionBean1().setSelectedDocumentId(t.getDocumentId());
        getSessionBean1().setSelectedRespositoryId(t.getRepositoryId());
        getSessionBean1().setSelectedHomeCommunityId(t.getHomeCommunityId());
        retrieveInboundDocument();
        return null;
    }

    public String tab3_action() {
        TabManagement t = getSessionBean1().getTabList().get(2);
        getSessionBean1().setSelectedDocumentId(t.getDocumentId());
        getSessionBean1().setSelectedRespositoryId(t.getRepositoryId());
        getSessionBean1().setSelectedHomeCommunityId(t.getHomeCommunityId());
        retrieveInboundDocument();
        return null;
    }

    public String tab4_action() {
        TabManagement t = getSessionBean1().getTabList().get(3);
        getSessionBean1().setSelectedDocumentId(t.getDocumentId());
        getSessionBean1().setSelectedRespositoryId(t.getRepositoryId());
        getSessionBean1().setSelectedHomeCommunityId(t.getHomeCommunityId());
        retrieveInboundDocument();
        return null;
    }

    public String tab5_action() {
        TabManagement t = getSessionBean1().getTabList().get(4);
        getSessionBean1().setSelectedDocumentId(t.getDocumentId());
        getSessionBean1().setSelectedRespositoryId(t.getRepositoryId());
        getSessionBean1().setSelectedHomeCommunityId(t.getHomeCommunityId());
        retrieveInboundDocument();
        return null;
    }

    public String tab6_action() {
        TabManagement t = getSessionBean1().getTabList().get(5);
        getSessionBean1().setSelectedDocumentId(t.getDocumentId());
        getSessionBean1().setSelectedRespositoryId(t.getRepositoryId());
        getSessionBean1().setSelectedHomeCommunityId(t.getHomeCommunityId());
        retrieveInboundDocument();
        return null;
    }

    public String tab7_action() {
        TabManagement t = getSessionBean1().getTabList().get(6);
        getSessionBean1().setSelectedDocumentId(t.getDocumentId());
        getSessionBean1().setSelectedRespositoryId(t.getRepositoryId());
        getSessionBean1().setSelectedHomeCommunityId(t.getHomeCommunityId());
        retrieveInboundDocument();
        return null;
    }

    public String tab8_action() {
        TabManagement t = getSessionBean1().getTabList().get(7);
        getSessionBean1().setSelectedDocumentId(t.getDocumentId());
        getSessionBean1().setSelectedRespositoryId(t.getRepositoryId());
        getSessionBean1().setSelectedHomeCommunityId(t.getHomeCommunityId());
        retrieveInboundDocument();
        return null;
    }

    public String tab9_action() {
        TabManagement t = getSessionBean1().getTabList().get(8);
        getSessionBean1().setSelectedDocumentId(t.getDocumentId());
        getSessionBean1().setSelectedRespositoryId(t.getRepositoryId());
        getSessionBean1().setSelectedHomeCommunityId(t.getHomeCommunityId());
        retrieveInboundDocument();
        return null;
    }

    public String tab10_action() {
        TabManagement t = getSessionBean1().getTabList().get(9);
        getSessionBean1().setSelectedDocumentId(t.getDocumentId());
        getSessionBean1().setSelectedRespositoryId(t.getRepositoryId());
        getSessionBean1().setSelectedHomeCommunityId(t.getHomeCommunityId());
        retrieveInboundDocument();
        return null;
    }

}

