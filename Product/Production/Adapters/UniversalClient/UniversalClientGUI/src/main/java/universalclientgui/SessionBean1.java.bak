/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universalclientgui;

import com.sun.rave.web.ui.appbase.AbstractSessionBean;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.universalclientgui.objects.DocumentManagerObjectManagement;
//import gov.hhs.fha.nhinc.universalclientgui.objects.RetrievedDocumentDisplayObject;
import gov.hhs.fha.nhinc.universalclientgui.objects.TabManagement;
import org.netbeans.xml.schema.docviewer.RetrievedDocumentDisplayObject;
import gov.hhs.fha.nhinc.universalclientgui.objects.TabViewerDisplayObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.FacesException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.Result;



/**
 * <p>Session scope data bean for your application.  Create properties
 *  here to represent cached data that should be made available across
 *  multiple HTTP requests for an individual user.</p>
 *
 * <p>An instance of this class will be created for you automatically,
 * the first time your application evaluates a value binding expression
 * or method binding expression that references a managed bean using
 * this class.</p>
 *
 * @version SessionBean1.java
 * @version Created on Oct 12, 2009, 4:37:57 PM
 * @author Duane DeCouteau
 */

public class SessionBean1 extends AbstractSessionBean {
    // <editor-fold defaultstate="collapsed" desc="Managed Component Definition">

    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
    }
    // </editor-fold>

    /**
     * <p>Construct a new session data bean instance.</p>
     */
    public SessionBean1() {
    }

    /**
     * <p>This method is called when this bean is initially added to
     * session scope.  Typically, this occurs as a result of evaluating
     * a value binding or method binding expression, which utilizes the
     * managed bean facility to instantiate this bean and store it into
     * session scope.</p>
     * 
     * <p>You may customize this method to initialize and cache data values
     * or resources that are required for the lifetime of a particular
     * user session.</p>
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
            log("SessionBean1 Initialization Failure", e);
            throw e instanceof FacesException ? (FacesException) e: new FacesException(e);
        }
        
        // </editor-fold>
        // Perform application initialization that must complete
        // *after* managed components are initialized
        // TODO - add your own initialization code here
    }

    /**
     * <p>This method is called when the session containing it is about to be
     * passivated.  Typically, this occurs in a distributed servlet container
     * when the session is about to be transferred to a different
     * container instance, after which the <code>activate()</code> method
     * will be called to indicate that the transfer is complete.</p>
     * 
     * <p>You may customize this method to release references to session data
     * or resources that can not be serialized with the session itself.</p>
     */
    @Override
    public void passivate() {
    }

    /**
     * <p>This method is called when the session containing it was
     * reactivated.</p>
     * 
     * <p>You may customize this method to reacquire references to session
     * data or resources that could not be serialized with the
     * session itself.</p>
     */
    @Override
    public void activate() {
    }

    /**
     * <p>This method is called when this bean is removed from
     * session scope.  Typically, this occurs as a result of
     * the session timing out or being terminated by the application.</p>
     * 
     * <p>You may customize this method to clean up resources allocated
     * during the execution of the <code>init()</code> method, or
     * at any later time during the lifetime of the application.</p>
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

    private RetrievedDocumentDisplayObject[] availableDocuments;
    //= new RetrievedDocumentDisplayObject[] {
    //    new RetrievedDocumentDisplayObject(false,"Veteran Affairs","1","Summary of Episode Note","HITSP-C32","1",false,true,"Downloading"),
    //    new RetrievedDocumentDisplayObject(false,"Kaiser Permanente","1","Summary of Episode Note","HITSP-C32","2",false,true,"Pending")
    //};

    /**
     * @return the availableDocuments
     */
    public RetrievedDocumentDisplayObject[] getAvailableDocuments() {
        return availableDocuments;
    }

    /**
     * @param availableDocuments the availableDocuments to set
     */
    public void setAvailableDocuments(RetrievedDocumentDisplayObject[] availableDocuments) {
        this.availableDocuments = availableDocuments;
    }

    private TabViewerDisplayObject[] tabDocuments = new TabViewerDisplayObject[10];

    /**
     * @return the tabDocuments
     */
    public TabViewerDisplayObject[] getTabDocuments() {
        return tabDocuments;
    }

    /**
     * @param tabDocuments the tabDocuments to set
     */
    public void setTabDocuments(TabViewerDisplayObject[] tabDocuments) {
        this.tabDocuments = tabDocuments;
    }

    public void clearAllTabDocuments() {
        TabViewerDisplayObject[] objs = new TabViewerDisplayObject[10];
        for (int x = 0; x < 10; x++) {
            objs[x] = new TabViewerDisplayObject();
        }
        setTabDocuments(objs);
    }

    public void clearSpecificTabDocument(int i) {
        TabViewerDisplayObject[] objs = getTabDocuments();
        objs[i] = new TabViewerDisplayObject();
        setTabDocuments(objs);
    }

    public String getConvertedRawXMLtoTabDoc(String rawxml) {
        String res = "";
        try {
            if (rawxml != null) {
                TransformerFactory tFactory = TransformerFactory.newInstance();
                URL url = new URL(getApplicationBean1().getC32StyleSheet());
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                Transformer transformer = tFactory.newTransformer(new StreamSource(reader));
                StringWriter out = new StringWriter();
                Result result = new StreamResult(out);
                transformer.transform(new StreamSource(new StringReader(rawxml)), result);
                res = out.toString();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }

    private DocumentManagerObjectManagement[] patientDocumentSubList;

    /**
     * @return the patientDocumentSubList
     */
    public DocumentManagerObjectManagement[] getPatientDocumentSubList() {
        return patientDocumentSubList;
    }

    /**
     * @param patientDocumentSubList the patientDocumentSubList to set
     */
    public void setPatientDocumentSubList(DocumentManagerObjectManagement[] patientDocumentSubList) {
        this.patientDocumentSubList = patientDocumentSubList;
    }

    public DocumentManagerObjectManagement[] getPatientDocumentSubList(String patientId) {
        DocumentManagerObjectManagement[] res = null;
        ArrayList tList = new ArrayList();
        List <DocumentManagerObjectManagement> mList = getApplicationBean1().getDocList();
        Iterator iter = mList.iterator();
        while (iter.hasNext()) {
            DocumentManagerObjectManagement obj = (DocumentManagerObjectManagement)iter.next();
            String patientid = obj.getPatientId();
            if (patientid.equals(patientId)) {
                tList.add(obj);
            }
        }
        res = new DocumentManagerObjectManagement[tList.size()];
        tList.toArray(res);
        setPatientDocumentSubList(res);
        return res;
    }

    private String providerId; //equivalent of userNCID or something that uniquely identifies provider globally
    private String userName;  //user login
    private String providerName; //user name
    private String providerLastName;
    private String providerFirstName;
    private String providerMiddleNameOrInitial;

    private String patientId; //equiv of patientUnitNumber
    private String patientName;
    private String patientFirstName;
    private String patientLastName;
    private String patientMiddleInitial;
    private String patientDOB;
    private String patientGender;

    //may be global in nature
    private String providerRole;
    private String purposeOfUse;

    private String selectedDocumentId;
    private String selectedRespositoryId;
    private String selectedHomeCommunityId;
    private String selectedNHINDocument;
    private String convertedNHINDocument;
    private List<TabManagement> tabList = new ArrayList();

    private AssertionType assertion;
    private boolean documentsAvailable = false;
    private String refreshRate = "30";


    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the providerName
     */
    public String getProviderName() {
        return providerName;
    }

    /**
     * @param providerName the providerName to set
     */
    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    /**
     * @return the patientName
     */
    public String getPatientName() {
        return patientName;
    }

    /**
     * @param patientName the patientName to set
     */
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    /**
     * @return the patientFirstName
     */
    public String getPatientFirstName() {
        return patientFirstName;
    }

    /**
     * @param patientFirstName the patientFirstName to set
     */
    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    /**
     * @return the patientLastName
     */
    public String getPatientLastName() {
        return patientLastName;
    }

    /**
     * @param patientLastName the patientLastName to set
     */
    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
    }

    /**
     * @return the patientMiddleInitial
     */
    public String getPatientMiddleInitial() {
        return patientMiddleInitial;
    }

    /**
     * @param patientMiddleInitial the patientMiddleInitial to set
     */
    public void setPatientMiddleInitial(String patientMiddleInitial) {
        this.patientMiddleInitial = patientMiddleInitial;
    }

    /**
     * @return the patientDOB
     */
    public String getPatientDOB() {
        return patientDOB;
    }

    /**
     * @param patientDOB the patientDOB to set
     */
    public void setPatientDOB(String patientDOB) {
        this.patientDOB = patientDOB;
    }

    /**
     * @return the providerRole
     */
    public String getProviderRole() {
        return providerRole;
    }

    /**
     * @param providerRole the providerRole to set
     */
    public void setProviderRole(String providerRole) {
        this.providerRole = providerRole;
    }

    /**
     * @return the purposeOfUse
     */
    public String getPurposeOfUse() {
        return purposeOfUse;
    }

    /**
     * @param purposeOfUse the purposeOfUse to set
     */
    public void setPurposeOfUse(String purposeOfUse) {
        this.purposeOfUse = purposeOfUse;
    }

    /**
     * @return the providerId
     */
    public String getProviderId() {
        return providerId;
    }

    /**
     * @param providerId the providerId to set
     */
    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    /**
     * @return the patientId
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * @param patientId the patientId to set
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * @return the patientGender
     */
    public String getPatientGender() {
        return patientGender;
    }

    /**
     * @param patientGender the patientGender to set
     */
    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    /**
     * @return the providerLastName
     */
    public String getProviderLastName() {
        return providerLastName;
    }

    /**
     * @param providerLastName the providerLastName to set
     */
    public void setProviderLastName(String providerLastName) {
        this.providerLastName = providerLastName;
    }

    /**
     * @return the providerFirstName
     */
    public String getProviderFirstName() {
        return providerFirstName;
    }

    /**
     * @param providerFirstName the providerFirstName to set
     */
    public void setProviderFirstName(String providerFirstName) {
        this.providerFirstName = providerFirstName;
    }

    /**
     * @return the providerMiddleNameOrInitial
     */
    public String getProviderMiddleNameOrInitial() {
        return providerMiddleNameOrInitial;
    }

    /**
     * @param providerMiddleNameOrInitial the providerMiddleNameOrInitial to set
     */
    public void setProviderMiddleNameOrInitial(String providerMiddleNameOrInitial) {
        this.providerMiddleNameOrInitial = providerMiddleNameOrInitial;
    }

    /**
     * @return the selectedDocumentId
     */
    public String getSelectedDocumentId() {
        return selectedDocumentId;
    }

    /**
     * @param selectedDocumentId the selectedDocumentId to set
     */
    public void setSelectedDocumentId(String selectedDocumentId) {
        this.selectedDocumentId = selectedDocumentId;
    }

    /**
     * @return the selectedRespositoryId
     */
    public String getSelectedRespositoryId() {
        return selectedRespositoryId;
    }

    /**
     * @param selectedRespositoryId the selectedRespositoryId to set
     */
    public void setSelectedRespositoryId(String selectedRespositoryId) {
        this.selectedRespositoryId = selectedRespositoryId;
    }

    /**
     * @return the selectedNHINDocument
     */
    public String getSelectedNHINDocument() {
        return selectedNHINDocument;
    }

    /**
     * @param selectedNHINDocument the selectedNHINDocument to set
     */
    public void setSelectedNHINDocument(String selectedNHINDocument) {
        this.selectedNHINDocument = selectedNHINDocument;
    }

    /**
     * @return the convertedNHINDocument
     */
    public String getConvertedNHINDocument() {
        String res = getConvertedRawXMLtoTabDoc(getSelectedNHINDocument());
        convertedNHINDocument = res;
        return convertedNHINDocument;
    }

    /**
     * @param convertedNHINDocument the convertedNHINDocument to set
     */
    public void setConvertedNHINDocument(String convertedNHINDocument) {
        this.convertedNHINDocument = convertedNHINDocument;
    }

    /**
     * @return the tabList
     */
    public List<TabManagement> getTabList() {
        if (tabList.size() == 0) {
            tabList.add(new TabManagement(1,"",""));
            tabList.add(new TabManagement(2,"",""));
            tabList.add(new TabManagement(3,"",""));
            tabList.add(new TabManagement(4,"",""));
            tabList.add(new TabManagement(5,"",""));
            tabList.add(new TabManagement(6,"",""));
            tabList.add(new TabManagement(7,"",""));
            tabList.add(new TabManagement(8,"",""));
            tabList.add(new TabManagement(9,"",""));
            tabList.add(new TabManagement(10,"",""));
        }
        return tabList;
    }

    /**
     * @param tabList the tabList to set
     */
    public void setTabList(List<TabManagement> tabList) {
        this.tabList = tabList;
    }

    /**
     * @return the assertion
     */
    public AssertionType getAssertion() {
        return assertion;
    }

    /**
     * @param assertion the assertion to set
     */
    public void setAssertion(AssertionType assertion) {
        this.assertion = assertion;
    }

    /**
     * @return the documentsAvailable
     */
    public boolean isDocumentsAvailable() {
        return documentsAvailable;
    }

    /**
     * @param documentsAvailable the documentsAvailable to set
     */
    public void setDocumentsAvailable(boolean documentsAvailable) {
        this.documentsAvailable = documentsAvailable;
    }

    /**
     * @return the selectedHomeCommunityId
     */
    public String getSelectedHomeCommunityId() {
        return selectedHomeCommunityId;
    }

    /**
     * @param selectedHomeCommunityId the selectedHomeCommunityId to set
     */
    public void setSelectedHomeCommunityId(String selectedHomeCommunityId) {
        this.selectedHomeCommunityId = selectedHomeCommunityId;
    }

    /**
     * @return the refreshRate
     */
    public String getRefreshRate() {
        return refreshRate;
    }

    /**
     * @param refreshRate the refreshRate to set
     */
    public void setRefreshRate(String refreshRate) {
        this.refreshRate = refreshRate;
    }

}
