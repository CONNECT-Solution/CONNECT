/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dodcustomerpreferencesprofilegui;

import com.sun.rave.web.ui.appbase.AbstractFragmentBean;
import com.sun.webui.jsf.component.Button;
import com.sun.webui.jsf.component.Label;
import com.sun.webui.jsf.component.PanelLayout;
import com.sun.webui.jsf.component.StaticText;
import com.sun.webui.jsf.component.Tab;
import com.sun.webui.jsf.component.TabSet;
import com.sun.webui.jsf.component.TextArea;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentResponseType;
import gov.hhs.fha.nhinc.mpilib.Identifier;
import gov.hhs.fha.nhinc.mpilib.Identifiers;
import gov.hhs.fha.nhinc.mpilib.Patient;
import gov.hhs.fha.nhinc.policyengine.adapterpip.proxy.AdapterPIPProxy;
import gov.hhs.fha.nhinc.policyengine.adapterpip.proxy.AdapterPIPProxyObjectFactory;
import javax.faces.FacesException;

/**
 * <p>Fragment bean that corresponds to a similarly named JSP page
 * fragment.  This class contains component definitions (and initialization
 * code) for all components that you have defined on this fragment, as well as
 * lifecycle methods and event handlers where you may add behavior
 * to respond to incoming events.</p>
 *
 * @version CPPOptInNav.java
 * @version Created on Oct 5, 2009, 12:26:04 AM
 * @author Duane DeCouteau
 */

public class CPPOptInNav extends AbstractFragmentBean {
    // <editor-fold defaultstate="collapsed" desc="Managed Component Definition">

    /**
     * <p>Automatically managed component initialization. <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
    }
    private TabSet tabSet1 = new TabSet();

    public TabSet getTabSet1() {
        return tabSet1;
    }

    public void setTabSet1(TabSet ts) {
        this.tabSet1 = ts;
    }
    private Tab optInTab = new Tab();

    public Tab getOptInTab() {
        return optInTab;
    }

    public void setOptInTab(Tab t) {
        this.optInTab = t;
    }
    private PanelLayout layoutPanel1 = new PanelLayout();

    public PanelLayout getLayoutPanel1() {
        return layoutPanel1;
    }

    public void setLayoutPanel1(PanelLayout pl) {
        this.layoutPanel1 = pl;
    }
    private StaticText staticText1 = new StaticText();

    public StaticText getStaticText1() {
        return staticText1;
    }

    public void setStaticText1(StaticText st) {
        this.staticText1 = st;
    }
    private Button consumerOptInBTN = new Button();

    public Button getConsumerOptInBTN() {
        return consumerOptInBTN;
    }

    public void setConsumerOptInBTN(Button b) {
        this.consumerOptInBTN = b;
    }
    private Tab policyTab = new Tab();

    public Tab getPolicyTab() {
        return policyTab;
    }

    public void setPolicyTab(Tab t) {
        this.policyTab = t;
    }
    private PanelLayout layoutPanel2 = new PanelLayout();

    public PanelLayout getLayoutPanel2() {
        return layoutPanel2;
    }

    public void setLayoutPanel2(PanelLayout pl) {
        this.layoutPanel2 = pl;
    }
    private Label label1 = new Label();

    public Label getLabel1() {
        return label1;
    }

    public void setLabel1(Label l) {
        this.label1 = l;
    }
    private TextArea policyPreviewTXT = new TextArea();

    public TextArea getPolicyPreviewTXT() {
        return policyPreviewTXT;
    }

    public void setPolicyPreviewTXT(TextArea ta) {
        this.policyPreviewTXT = ta;
    }
    private StaticText errorMsgTXT = new StaticText();

    public StaticText getErrorMsgTXT() {
        return errorMsgTXT;
    }

    public void setErrorMsgTXT(StaticText st) {
        this.errorMsgTXT = st;
    }
    private Button cancelBTN = new Button();

    public Button getCancelBTN() {
        return cancelBTN;
    }

    public void setCancelBTN(Button b) {
        this.cancelBTN = b;
    }
    // </editor-fold>

    public CPPOptInNav() {
    }

    /**
     * <p>Callback method that is called whenever a page containing
     * this page fragment is navigated to, either directly via a URL,
     * or indirectly via page navigation.  Override this method to acquire
     * resources that will be needed for event handlers and lifecycle methods.</p>
     * 
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void init() {
        // Perform initializations inherited from our superclass
        super.init();
        // Perform application initialization that must complete
        // *before* managed components are initialized
        // TODO - add your own initialiation code here
        
        
        // <editor-fold defaultstate="collapsed" desc="Visual-Web-managed Component Initialization">
        // Initialize automatically managed components
        // *Note* - this logic should NOT be modified
        try {
            _init();
        } catch (Exception e) {
            log("Page1 Initialization Failure", e);
            throw e instanceof FacesException ? (FacesException) e: new FacesException(e);
        }
        
        // </editor-fold>
        // Perform application initialization that must complete
        // *after* managed components are initialized
        // TODO - add your own initialization code here
    }

    /**
     * <p>Callback method that is called after rendering is completed for
     * this request, if <code>init()</code> was called.  Override this
     * method to release resources acquired in the <code>init()</code>
     * resources that will be needed for event handlers and lifecycle methods.</p>
     * 
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void destroy() {
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

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected RequestBean1 getRequestBean1() {
        return (RequestBean1) getBean("RequestBean1");
    }

    public String consumerOptInBTN_action() {
        // TODO: Process the action. Return value is a navigation
        // case name where null will return to the same page.
        String pagenav = null;
        getErrorMsgTXT().setRendered(false);
        try {

            Patient pat = getSessionBean1().getSelectedPatient();
            Identifiers ids = pat.getIdentifiers();
            for (Identifier id : pat.getIdentifiers()) {

                PatientPreferencesType patientPrefs = new PatientPreferencesType();
                patientPrefs.setAssigningAuthority(id.getOrganizationId());
                patientPrefs.setPatientId(id.getId());
                patientPrefs.setOptIn(true);

                AdapterPIPProxyObjectFactory factory = new AdapterPIPProxyObjectFactory();
                AdapterPIPProxy adapterPIPProxy = factory.getAdapterPIPProxy();
                StorePtConsentRequestType storeReq = new StorePtConsentRequestType();
                storeReq.setPatientPreferences(patientPrefs);
                StorePtConsentResponseType storeResp = adapterPIPProxy.storePtConsent(storeReq);
                if (storeResp.getStatus().equals("SUCCESS")) {
                    pagenav = "CPPMain";
                }
                else {
                    pagenav = null;
                    getErrorMsgTXT().setText(errMSG);
                    getErrorMsgTXT().setRendered(true);
                }
            }
        }
        catch (Exception ex) {
            getErrorMsgTXT().setRendered(true);
            String err = errMSG + ex.getMessage();
            getErrorMsgTXT().setText(err);
        }
        return pagenav;
    }

    private String errMSG = "An Error as occurred while attempting to save the consumers preferences";

    public String cancelBTN_action() {
        // TODO: Process the action. Return value is a navigation
        // case name where null will return to the same page.
        return "CPPMain";
    }


}
