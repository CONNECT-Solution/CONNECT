/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dodcustomerpreferencesprofilegui;

import com.sun.rave.web.ui.appbase.AbstractFragmentBean;
import com.sun.webui.jsf.component.Tree;
import com.sun.webui.jsf.component.TreeNode;
import javax.faces.FacesException;

/**
 * <p>Fragment bean that corresponds to a similarly named JSP page
 * fragment.  This class contains component definitions (and initialization
 * code) for all components that you have defined on this fragment, as well as
 * lifecycle methods and event handlers where you may add behavior
 * to respond to incoming events.</p>
 *
 * @version CPPNavigator.java
 * @version Created on Oct 3, 2009, 7:39:56 PM
 * @author Duane DeCouteau
 */

public class CPPNavigator extends AbstractFragmentBean {
    // <editor-fold defaultstate="collapsed" desc="Managed Component Definition">

    /**
     * <p>Automatically managed component initialization. <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
    }
    private Tree navTree = new Tree();

    public Tree getNavTree() {
        return navTree;
    }

    public void setNavTree(Tree t) {
        this.navTree = t;
    }
    private TreeNode cppNode = new TreeNode();

    public TreeNode getCppNode() {
        return cppNode;
    }

    public void setCppNode(TreeNode tn) {
        this.cppNode = tn;
    }
    private TreeNode cppOptInNode = new TreeNode();

    public TreeNode getCppOptInNode() {
        return cppOptInNode;
    }

    public void setCppOptInNode(TreeNode tn) {
        this.cppOptInNode = tn;
    }
    private TreeNode cppOptOutNode = new TreeNode();

    public TreeNode getCppOptOutNode() {
        return cppOptOutNode;
    }

    public void setCppOptOutNode(TreeNode tn) {
        this.cppOptOutNode = tn;
    }
    private TreeNode providerProvisioningNode = new TreeNode();

    public TreeNode getProviderProvisioningNode() {
        return providerProvisioningNode;
    }

    public void setProviderProvisioningNode(TreeNode tn) {
        this.providerProvisioningNode = tn;
    }
    private TreeNode auditNode = new TreeNode();

    public TreeNode getAuditNode() {
        return auditNode;
    }

    public void setAuditNode(TreeNode tn) {
        this.auditNode = tn;
    }
    // </editor-fold>

    public CPPNavigator() {
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

    public String cppNode_action() {
        // TODO: Process the action. Return value is a navigation
        // case name where null will return to the same page.
        return "CPPMain";
    }

    public String cppOptInNode_action() {
        // TODO: Process the action. Return value is a navigation
        // case name where null will return to the same page.
        return "CPPOptIn";
    }

    public String cppOptOutNode_action() {
        // TODO: Process the action. Return value is a navigation
        // case name where null will return to the same page.
        return "CPPOptOut";
    }

    public String providerProvisioningNode_action() {
        // TODO: Process the action. Return value is a navigation
        // case name where null will return to the same page.
        return "ProviderProvisioning";
    }

    public String auditNode_action() {
        // TODO: Process the action. Return value is a navigation
        // case name where null will return to the same page.
        return "AuditLogs";
    }

}
