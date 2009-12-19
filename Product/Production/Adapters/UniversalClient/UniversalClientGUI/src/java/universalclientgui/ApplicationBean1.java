/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universalclientgui;

import com.sun.rave.web.ui.appbase.AbstractApplicationBean;
import gov.hhs.fha.nhinc.universalclientgui.interfaces.DocumentAccessManager;
import gov.hhs.fha.nhinc.universalclientgui.objects.DocumentManagerObjectManagement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.faces.FacesException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <p>Application scope data bean for your application.  Create properties
 *  here to represent cached data that should be made available to all users
 *  and pages in the application.</p>
 *
 * <p>An instance of this class will be created for you automatically,
 * the first time your application evaluates a value binding expression
 * or method binding expression that references a managed bean using
 * this class.</p>
 *
 * @version ApplicationBean1.java
 * @version Created on Oct 12, 2009, 4:37:57 PM
 * @author Duane DeCouteau
 */

public class ApplicationBean1 extends AbstractApplicationBean {
    private static Log log = LogFactory.getLog(ApplicationBean1.class);

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
     * <p>Construct a new application data bean instance.</p>
     */
    public ApplicationBean1() {
    }

    /**
     * <p>This method is called when this bean is initially added to
     * application scope.  Typically, this occurs as a result of evaluating
     * a value binding or method binding expression, which utilizes the
     * managed bean facility to instantiate this bean and store it into
     * application scope.</p>
     * 
     * <p>You may customize this method to initialize and cache application wide
     * data values (such as the lists of valid options for dropdown list
     * components), or to allocate resources that are required for the
     * lifetime of the application.</p>
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
            String hc = this.getExternalContext().getInitParameter("HOME_COMMUNITY_ID");
            String hcd = this.getExternalContext().getInitParameter("HOME_COMMUNITY_DESC");
            String hcn = this.getExternalContext().getInitParameter("HOME_COMMUNITY_NAME");
            String c32 = this.getExternalContext().getInitParameter("C32_STYLE_SHEET");

            String bos = this.getExternalContext().getInitParameter("BOS_SERVICE_ENPOINT");
            String assigningauth = this.getExternalContext().getInitParameter("ASSIGNING_AUTHORITY_ID");
            String defaultpou = this.getExternalContext().getInitParameter("DEFAULT_PURPOSE_OF_USE");
            String purposeofusecode = this.getExternalContext().getInitParameter("PURPOSE_OF_USE_CODE");
            String purposeofusesystem = this.getExternalContext().getInitParameter("PURPOSE_OF_USE_CODE_SYSTEM");
            String purposeofusesystemname = this.getExternalContext().getInitParameter("PURPOSE_OF_USE_CODE_SYSTEM_NAME");
            String purposeofusesystemversion = this.getExternalContext().getInitParameter("PURPOSE_OF_USE_CODE_SYSTEM_VERSION");
            String userorganization = this.getExternalContext().getInitParameter("USER_ORGANIZATION");
            String userrolecode = this.getExternalContext().getInitParameter("USER_ROLE_CODE");
            String userrolecodesystem = this.getExternalContext().getInitParameter("USER_ROLE_CODE_SYSTEM");
            String userrolecodesystemname = this.getExternalContext().getInitParameter("USER_ROLE_CODE_SYSTEM_NAME");
            String userrolecodesystemversion = this.getExternalContext().getInitParameter("USER_ROLE_CODE_SYSTEM_VERSION");
            String userroledisplayname = this.getExternalContext().getInitParameter("USER_ROLE_DISPLAY_NAME_DEFAULT");
            String docviewerws = this.getExternalContext().getInitParameter("DOCVIEWER_REQUEST_SERVICE");
            String claimformref = this.getExternalContext().getInitParameter("CLAIM_FORM_REF");
            String claimformstring = this.getExternalContext().getInitParameter("CLAIM_FORM_STRING");
            String signaturedate = this.getExternalContext().getInitParameter("SIGNATURE_DATE");
            String expirationdate = this.getExternalContext().getInitParameter("EXPIRATION_DATE");
            String numberofyearsstring = this.getExternalContext().getInitParameter("NUMBER_OF_YEARS");
            String docmanagerservice = this.getExternalContext().getInitParameter("DOC_MANAGER_SERVICE");

            //DoDConnector Params
            String dodconnectorwsdlurl = this.getExternalContext().getInitParameter("DODCONNECTOR_WSDL");
            String dodconnectorqname = this.getExternalContext().getInitParameter("DODCONNECTOR_QNAME");
            String dodconnectorservice = this.getExternalContext().getInitParameter("DODCONNECTOR_SERVICE");

	String nhindocretrieve = this.getExternalContext().getInitParameter("NHIN_DOC_RETRIEVE");
            setHomeCommunityId(hc);
            setHomeCommunityDesc(hcd);
            setHomeCommunityName(hcn);
            setC32StyleSheet(c32);
            setBOS(bos);
            setAssigningAuthorityId(assigningauth);
            setDefaultPurposeOfUse(defaultpou);
            setPurposeOfUseCode(purposeofusecode);
            setPurposeOfUseCodeSystem(purposeofusesystem);
            setPurposeOfUseCodeSystemName(purposeofusesystemname);
            setPurposeOfUseCodeSystemVersion(purposeofusesystemversion);
            setUserOrganization(userorganization);
            setUserRoleCode(userrolecode);
            setUserRoleCodeSystem(userrolecodesystem);
            setUserRoleCodeSystemName(userrolecodesystemname);
            setUserRoleCodeSystemVersion(userrolecodesystemversion);
            setUserRoleDisplayNameDefault(userroledisplayname);
            setDocViewerRequestService(docviewerws);
            setClaimFormRef(claimformref);
            setClaimFormString(claimformstring);
            setSignatureDate(signaturedate);
            setExpirationDate(expirationdate);
            Integer num = new Integer(numberofyearsstring);
            setNumberOfYears(num.intValue());
            setDocumentManagerService(docmanagerservice);
            setNhinDocRetrieveService(nhindocretrieve);
            //DoDConnector setters
            setDoDConnectorService(dodconnectorservice);
            setDoDConnectorWSDLURL(dodconnectorwsdlurl);
            setDoDConnectorQNAME(dodconnectorqname);
        }
        catch (Exception ex) {
            System.err.println("WARNING APPLICATIONBEAN "+ex.getMessage());
            ex.printStackTrace();
        }
        try {
            _init();
        } catch (Exception e) {
            log("ApplicationBean1 Initialization Failure", e);
            throw e instanceof FacesException ? (FacesException) e: new FacesException(e);
        }
        
        // </editor-fold>
        // Perform application initialization that must complete
        // *after* managed components are initialized
        // TODO - add your own initialization code here
    }

    /**
     * <p>This method is called when this bean is removed from
     * application scope.  Typically, this occurs as a result of
     * the application being shut down by its owning container.</p>
     * 
     * <p>You may customize this method to clean up resources allocated
     * during the execution of the <code>init()</code> method, or
     * at any later time during the lifetime of the application.</p>
     */
    @Override
    public void destroy() {
    }

    /**
     * <p>Return an appropriate character encoding based on the
     * <code>Locale</code> defined for the current JavaServer Faces
     * view.  If no more suitable encoding can be found, return
     * "UTF-8" as a general purpose default.</p>
     *
     * <p>The default implementation uses the implementation from
     * our superclass, <code>AbstractApplicationBean</code>.</p>
     */
    @Override
    public String getLocaleCharacterEncoding() {
        return super.getLocaleCharacterEncoding();
    }

    private String c32StyleSheet;

    /**
     * @return the c32StyleSheet
     */
    public String getC32StyleSheet() {
        return c32StyleSheet;
    }

    /**
     * @param c32StyleSheet the c32StyleSheet to set
     */
    public void setC32StyleSheet(String c32StyleSheet) {
        this.c32StyleSheet = c32StyleSheet;
    }

    private ArrayList docArray = new ArrayList();

    private List<DocumentManagerObjectManagement> docList = Collections.synchronizedList(docArray);

    /**
     * @return the docList
     */
    public List<DocumentManagerObjectManagement> getDocList() {
        return docList;
    }

    /**
     * @param docList the docList to set
     */
    public void setDocList(List<DocumentManagerObjectManagement> docList) {
        this.docList = docList;
    }

    private String homeCommunityId;
    private String homeCommunityName;
    private String homeCommunityDesc;

    /**
     * @return the homeCommunityId
     */
    public String getHomeCommunityId() {
        return homeCommunityId;
    }

    /**
     * @param homeCommunityId the homeCommunityId to set
     */
    public void setHomeCommunityId(String homeCommunityId) {
        this.homeCommunityId = homeCommunityId;
    }

    /**
     * @return the homeCommunityName
     */
    public String getHomeCommunityName() {
        return homeCommunityName;
    }

    /**
     * @param homeCommunityName the homeCommunityName to set
     */
    public void setHomeCommunityName(String homeCommunityName) {
        this.homeCommunityName = homeCommunityName;
    }

    /**
     * @return the homeCommunityDesc
     */
    public String getHomeCommunityDesc() {
        return homeCommunityDesc;
    }

    /**
     * @param homeCommunityDesc the homeCommunityDesc to set
     */
    public void setHomeCommunityDesc(String homeCommunityDesc) {
        this.homeCommunityDesc = homeCommunityDesc;
    }


    private String bOS;
    private String assigningAuthorityId;
    private String defaultPurposeOfUse;
    private String purposeOfUseCode;
    private String purposeOfUseCodeSystem;
    private String purposeOfUseCodeSystemName;
    private String purposeOfUseCodeSystemVersion;
    private String userOrganization;
    private String userRoleCode;
    private String userRoleCodeSystem;
    private String userRoleCodeSystemName;
    private String userRoleCodeSystemVersion;
    private String userRoleDisplayNameDefault;
    private String docViewerRequestService;
    private String claimFormRef;
    private String claimFormString;
    private String signatureDate;
    private String expirationDate;
    private int numberOfYears;
    private String documentManagerService;
	private String nhinDocRetrieveService;
    //DoDConnector Params
    private String dodConnectorService;
    private String dodConnectorWSDLURL;
    private String dodConnectorQname;


    /**
     * @return the bOS
     */
    public String getBOS() {
        return bOS;
    }

    /**
     * @param bOS the bOS to set
     */
    public void setBOS(String bOS) {
        this.bOS = bOS;
    }

    /**
     * @return the assigningAuthorityId
     */
    public String getAssigningAuthorityId() {
        return assigningAuthorityId;
    }

    /**
     * @param assigningAuthorityId the assigningAuthorityId to set
     */
    public void setAssigningAuthorityId(String assigningAuthorityId) {
        this.assigningAuthorityId = assigningAuthorityId;
    }

    /**
     * @return the defaultPurposeOfUse
     */
    public String getDefaultPurposeOfUse() {
        return defaultPurposeOfUse;
    }

    /**
     * @param defaultPurposeOfUse the defaultPurposeOfUse to set
     */
    public void setDefaultPurposeOfUse(String defaultPurposeOfUse) {
        this.defaultPurposeOfUse = defaultPurposeOfUse;
    }

    /**
     * @return the purposeOfUseCodeSystem
     */
    public String getPurposeOfUseCodeSystem() {
        return purposeOfUseCodeSystem;
    }

    /**
     * @param purposeOfUseCodeSystem the purposeOfUseCodeSystem to set
     */
    public void setPurposeOfUseCodeSystem(String purposeOfUseCodeSystem) {
        this.purposeOfUseCodeSystem = purposeOfUseCodeSystem;
    }

    /**
     * @return the purposeOfUseCodeSystemName
     */
    public String getPurposeOfUseCodeSystemName() {
        return purposeOfUseCodeSystemName;
    }

    /**
     * @param purposeOfUseCodeSystemName the purposeOfUseCodeSystemName to set
     */
    public void setPurposeOfUseCodeSystemName(String purposeOfUseCodeSystemName) {
        this.purposeOfUseCodeSystemName = purposeOfUseCodeSystemName;
    }

    /**
     * @return the userOrganization
     */
    public String getUserOrganization() {
        return userOrganization;
    }

    /**
     * @param userOrganization the userOrganization to set
     */
    public void setUserOrganization(String userOrganization) {
        this.userOrganization = userOrganization;
    }

    /**
     * @return the userRoleCodeSystem
     */
    public String getUserRoleCodeSystem() {
        return userRoleCodeSystem;
    }

    /**
     * @param userRoleCodeSystem the userRoleCodeSystem to set
     */
    public void setUserRoleCodeSystem(String userRoleCodeSystem) {
        this.userRoleCodeSystem = userRoleCodeSystem;
    }

    /**
     * @return the userRoleCodeSystemName
     */
    public String getUserRoleCodeSystemName() {
        return userRoleCodeSystemName;
    }

    /**
     * @param userRoleCodeSystemName the userRoleCodeSystemName to set
     */
    public void setUserRoleCodeSystemName(String userRoleCodeSystemName) {
        this.userRoleCodeSystemName = userRoleCodeSystemName;
    }

    /**
     * @return the userRoleDisplayNameDefault
     */
    public String getUserRoleDisplayNameDefault() {
        return userRoleDisplayNameDefault;
    }

    /**
     * @param userRoleDisplayNameDefault the userRoleDisplayNameDefault to set
     */
    public void setUserRoleDisplayNameDefault(String userRoleDisplayNameDefault) {
        this.userRoleDisplayNameDefault = userRoleDisplayNameDefault;
    }

    /**
     * @return the docViewerRequestService
     */
    public String getDocViewerRequestService() {
        return docViewerRequestService;
    }

    /**
     * @param docViewerRequestService the docViewerRequestService to set
     */
    public void setDocViewerRequestService(String docViewerRequestService) {
        this.docViewerRequestService = docViewerRequestService;
    }

    /**
     * @return the purposeOfUseCodeSystemVersion
     */
    public String getPurposeOfUseCodeSystemVersion() {
        return purposeOfUseCodeSystemVersion;
    }

    /**
     * @param purposeOfUseCodeSystemVersion the purposeOfUseCodeSystemVersion to set
     */
    public void setPurposeOfUseCodeSystemVersion(String purposeOfUseCodeSystemVersion) {
        this.purposeOfUseCodeSystemVersion = purposeOfUseCodeSystemVersion;
    }

    /**
     * @return the userRoleCodeSystemVersion
     */
    public String getUserRoleCodeSystemVersion() {
        return userRoleCodeSystemVersion;
    }

    /**
     * @param userRoleCodeSystemVersion the userRoleCodeSystemVersion to set
     */
    public void setUserRoleCodeSystemVersion(String userRoleCodeSystemVersion) {
        this.userRoleCodeSystemVersion = userRoleCodeSystemVersion;
    }

    /**
     * @return the purposeOfUseCode
     */
    public String getPurposeOfUseCode() {
        return purposeOfUseCode;
    }

    /**
     * @param purposeOfUseCode the purposeOfUseCode to set
     */
    public void setPurposeOfUseCode(String purposeOfUseCode) {
        this.purposeOfUseCode = purposeOfUseCode;
    }

    /**
     * @return the userRoleCode
     */
    public String getUserRoleCode() {
        return userRoleCode;
    }

    /**
     * @param userRoleCode the userRoleCode to set
     */
    public void setUserRoleCode(String userRoleCode) {
        this.userRoleCode = userRoleCode;
    }

    /**
     * @return the claimFormRef
     */
    public String getClaimFormRef() {
        return claimFormRef;
    }

    /**
     * @param claimFormRef the claimFormRef to set
     */
    public void setClaimFormRef(String claimFormRef) {
        this.claimFormRef = claimFormRef;
    }

    /**
     * @return the claimFormString
     */
    public String getClaimFormString() {
        return claimFormString;
    }

    /**
     * @param claimFormString the claimFormString to set
     */
    public void setClaimFormString(String claimFormString) {
        this.claimFormString = claimFormString;
    }

    /**
     * @return the signatureDate
     */
    public String getSignatureDate() {
        return signatureDate;
    }

    /**
     * @param signatureDate the signatureDate to set
     */
    public void setSignatureDate(String signatureDate) {
        this.signatureDate = signatureDate;
    }

    /**
     * @return the expirationDate
     */
    public String getExpirationDate() {
        return expirationDate;
    }

    /**
     * @param expirationDate the expirationDate to set
     */
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * @return the numberOfYears
     */
    public int getNumberOfYears() {
        return numberOfYears;
    }

    /**
     * @param numberOfYears the numberOfYears to set
     */
    public void setNumberOfYears(int numberOfYears) {
        this.numberOfYears = numberOfYears;
    }

    /**
     * @return the documentManagerService
     */
    public String getDocumentManagerService() {
        return documentManagerService;
    }

    /**
     * @param documentManagerService the documentManagerService to set
     */
    public void setDocumentManagerService(String documentManagerService) {
        this.documentManagerService = documentManagerService;
    }


    /**
     * @return the nhinDocRetrieveService
     */
    public String getNhinDocRetrieveService() {
        return nhinDocRetrieveService;
    }

    /**
     * @param nhinDocRetrieveService the nhinDocRetrieveService to set
     */
    public void setNhinDocRetrieveService(String nhinDocRetrieveService) {
        this.nhinDocRetrieveService = nhinDocRetrieveService;
    }
    //DoDConnector

    /**
     * @return the dodConnectorService
     */
    public String getDoDConnectorService() {
        return dodConnectorService;
    }

    /**
     * @param dodConnectorService the dodConnectorService to set
     */
    public void setDoDConnectorService(String dodConnectorService) {
        this.dodConnectorService = dodConnectorService;
    }

     /**
     * @return the dodConnectorWSDLURL
     */
    public String getDoDConnectorWSDLURL() {
        return dodConnectorWSDLURL;
    }

    /**
     * @param dodConnectorWSDLURL the dodConnectorWSDLURL to set
     */
    public void setDoDConnectorWSDLURL(String dodConnectorWSDLURL) {
        this.dodConnectorWSDLURL = dodConnectorWSDLURL;
    }

     /**
     * @return the dodConnectorQname
     */
    public String getDoDConnectorQNAME() {
        return dodConnectorQname;
    }

    /**
     * @param dodConnectorWSDLURL the dodConnectorWSDLURL to set
     */
    public void setDoDConnectorQNAME(String dodConnectorQname) {
        this.dodConnectorQname = dodConnectorQname;
    }


}
