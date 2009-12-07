/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dodcustomerpreferencesprofilegui;

import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.webui.jsf.component.Body;
import com.sun.webui.jsf.component.Button;
import com.sun.webui.jsf.component.DropDown;
import com.sun.webui.jsf.component.Form;
import com.sun.webui.jsf.component.Head;
import com.sun.webui.jsf.component.Html;
import com.sun.webui.jsf.component.Label;
import com.sun.webui.jsf.component.Page;
import com.sun.webui.jsf.component.PanelLayout;
import com.sun.webui.jsf.component.TextField;
import com.sun.webui.jsf.model.SingleSelectOptionsList;
import javax.faces.FacesException;

/**
 * <p>Page bean that corresponds to a similarly named JSP page.  This
 * class contains component definitions (and initialization code) for
 * all components that you have defined on this page, as well as
 * lifecycle methods and event handlers where you may add behavior
 * to respond to incoming events.</p>
 *
 * @version ProviderProvisioning.java
 * @version Created on Oct 5, 2009, 2:08:45 AM
 * @author Duane DeCouteau
 */

public class ProviderProvisioning extends AbstractPageBean {
    // <editor-fold defaultstate="collapsed" desc="Managed Component Definition">

    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
        astmCBXDefaultOptions.setOptions(new com.sun.webui.jsf.model.Option[]{new com.sun.webui.jsf.model.Option("disabled", "Currently Disabled"), new com.sun.webui.jsf.model.Option("item2", "Item 2"), new com.sun.webui.jsf.model.Option("item3", "Item 3")});
        hl7PermCBXDefaultOptions.setOptions(new com.sun.webui.jsf.model.Option[]{new com.sun.webui.jsf.model.Option("item1", "Currently Disabled"), new com.sun.webui.jsf.model.Option("item2", "Item 2"), new com.sun.webui.jsf.model.Option("item3", "Item 3")});
        functionalCBXDefaultOptions.setOptions(new com.sun.webui.jsf.model.Option[]{new com.sun.webui.jsf.model.Option("item1", "Currently Disabled"), new com.sun.webui.jsf.model.Option("item2", "Item 2"), new com.sun.webui.jsf.model.Option("item3", "Item 3")});
    }
    private SingleSelectOptionsList astmCBXDefaultOptions = new SingleSelectOptionsList();

    public SingleSelectOptionsList getAstmCBXDefaultOptions() {
        return astmCBXDefaultOptions;
    }

    public void setAstmCBXDefaultOptions(SingleSelectOptionsList ssol) {
        this.astmCBXDefaultOptions = ssol;
    }
    private SingleSelectOptionsList functionalCBXDefaultOptions = new SingleSelectOptionsList();

    public SingleSelectOptionsList getFunctionalCBXDefaultOptions() {
        return functionalCBXDefaultOptions;
    }

    public void setFunctionalCBXDefaultOptions(SingleSelectOptionsList ssol) {
        this.functionalCBXDefaultOptions = ssol;
    }
    private SingleSelectOptionsList hl7PermCBXDefaultOptions = new SingleSelectOptionsList();

    public SingleSelectOptionsList getHl7PermCBXDefaultOptions() {
        return hl7PermCBXDefaultOptions;
    }

    public void setHl7PermCBXDefaultOptions(SingleSelectOptionsList ssol) {
        this.hl7PermCBXDefaultOptions = ssol;
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
    private TextField providerNameFLD = new TextField();

    public TextField getProviderNameFLD() {
        return providerNameFLD;
    }

    public void setProviderNameFLD(TextField tf) {
        this.providerNameFLD = tf;
    }
    private Label label2 = new Label();

    public Label getLabel2() {
        return label2;
    }

    public void setLabel2(Label l) {
        this.label2 = l;
    }
    private TextField userNameFLD = new TextField();

    public TextField getUserNameFLD() {
        return userNameFLD;
    }

    public void setUserNameFLD(TextField tf) {
        this.userNameFLD = tf;
    }
    private Label label3 = new Label();

    public Label getLabel3() {
        return label3;
    }

    public void setLabel3(Label l) {
        this.label3 = l;
    }
    private TextField userIENFLD = new TextField();

    public TextField getUserIENFLD() {
        return userIENFLD;
    }

    public void setUserIENFLD(TextField tf) {
        this.userIENFLD = tf;
    }
    private Label label4 = new Label();

    public Label getLabel4() {
        return label4;
    }

    public void setLabel4(Label l) {
        this.label4 = l;
    }
    private TextField clinicalNameFLD = new TextField();

    public TextField getClinicalNameFLD() {
        return clinicalNameFLD;
    }

    public void setClinicalNameFLD(TextField tf) {
        this.clinicalNameFLD = tf;
    }
    private Label label5 = new Label();

    public Label getLabel5() {
        return label5;
    }

    public void setLabel5(Label l) {
        this.label5 = l;
    }
    private TextField facilityNCIDFLD = new TextField();

    public TextField getFacilityNCIDFLD() {
        return facilityNCIDFLD;
    }

    public void setFacilityNCIDFLD(TextField tf) {
        this.facilityNCIDFLD = tf;
    }
    private Button searchBTN = new Button();

    public Button getSearchBTN() {
        return searchBTN;
    }

    public void setSearchBTN(Button b) {
        this.searchBTN = b;
    }
    private Label label6 = new Label();

    public Label getLabel6() {
        return label6;
    }

    public void setLabel6(Label l) {
        this.label6 = l;
    }
    private Label label7 = new Label();

    public Label getLabel7() {
        return label7;
    }

    public void setLabel7(Label l) {
        this.label7 = l;
    }
    private Label label8 = new Label();

    public Label getLabel8() {
        return label8;
    }

    public void setLabel8(Label l) {
        this.label8 = l;
    }
    private DropDown astmCBX = new DropDown();

    public DropDown getAstmCBX() {
        return astmCBX;
    }

    public void setAstmCBX(DropDown dd) {
        this.astmCBX = dd;
    }
    private DropDown functionalCBX = new DropDown();

    public DropDown getFunctionalCBX() {
        return functionalCBX;
    }

    public void setFunctionalCBX(DropDown dd) {
        this.functionalCBX = dd;
    }
    private DropDown hl7PermCBX = new DropDown();

    public DropDown getHl7PermCBX() {
        return hl7PermCBX;
    }

    public void setHl7PermCBX(DropDown dd) {
        this.hl7PermCBX = dd;
    }
    private Button createBTN = new Button();

    public Button getCreateBTN() {
        return createBTN;
    }

    public void setCreateBTN(Button b) {
        this.createBTN = b;
    }

    // </editor-fold>

    /**
     * <p>Construct a new Page bean instance.</p>
     */
    public ProviderProvisioning() {
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
            log("ProviderProvisioning Initialization Failure", e);
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
    
}

