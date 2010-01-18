/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universalclientgui;

import com.sun.rave.web.ui.appbase.AbstractApplicationBean;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
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

    private static final String GATEWAY_PROPERTY_FILE = "gateway";
    private static final String HOME_COMMUNITY_ID_PROPERTY = "localHomeCommunityId";
    private static final String HOME_COMMUNITY_DESC_PROPERTY = "localHomeCommunityDescription";

    private static final String ADAPTER_PROPERTY_FILE = "adapter";
    private static final String ASSIGNING_AUTHORITY_ID_PROPERTY = "assigningAuthorityId";
    private static final String PURPOSE_OF_USE_CODE_PROPERTY = "PurposeOfUseCode";
    private static final String PURPOSE_OF_USE_CODE_SYSTEM_PROPERTY = "PurposeOfUseCodeSystem";
    private static final String PURPOSE_OF_USE_CODE_SYSTEM_VERSION_PROPERTY = "PurposeOfUseCodeSystemVersion";
    private static final String PURPOSE_OF_USE_CODE_SYSTEM_NAME_PROPERTY = "PurposeOfUseCodeSystemName";
    private static final String DEFAULT_PURPOSE_OF_USE_PROPERTY = "DefaultPurposeOfUse";
    private static final String USER_ORGANIZATION_PROPERTY = "UserOrganization";
    private static final String USER_ROLE_CODE_PROPERTY = "UserRoleCode";
    private static final String USER_ROLE_CODE_SYSTEM_PROPERTY = "UserRoleCodeSystem";
    private static final String USER_ROLE_CODE_SYSTEM_VERSION_PROPERTY = "UserRoleCodeSystemVersion";
    private static final String USER_ROLE_CODE_SYSTEM_NAME_PROPERTY = "UserRoleCodeSystemName";
    private static final String USER_ROLE_DISPLAY_NAME_DEFAULT_PROPERTY = "UserRoleDisplayNameDefault";
    private static final String CLAIM_FORM_REF_PROPERTY = "ClaimFormREF";
    private static final String CLAIM_FORM_STRING_PROPERTY = "ClaimFormString";
    private static final String SIGNATURE_DATE_PROPERTY = "SignatureDate";
    private static final String EXPIRATION_DATE_PROPERTY = "ExpirationDate";
    private static final String NUMBER_OF_YEARS_PROPERTY = "NumberOfYears";

    private static final String BOS_SERVICE_WSDL_PROPERTY = "BOSServiceWSDL";
    private static final String DOCVIEWER_REQUEST_SERVICE_PROPERTY = "DocViewerRequestService";
    private static final String DOC_MANAGER_SERVICE_PROPERTY = "DocManagerService";
    private static final String CAL_SERVICE_PROPERTY = "CALService";
    private static final String CAL_SERVICE_WSDL_PROPERTY = "CALServiceWSDL";
    private static final String CAL_SERVICE_QNAME_PROPERTY = "CALServiceQName";
    private static final String C32_STYLE_SHEET_PROPERTY = "C32StyleSheet";

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
            String hc = PropertyAccessor.getProperty(GATEWAY_PROPERTY_FILE, HOME_COMMUNITY_ID_PROPERTY);
            String hcd = PropertyAccessor.getProperty(GATEWAY_PROPERTY_FILE, HOME_COMMUNITY_DESC_PROPERTY);
            String hcn = PropertyAccessor.getProperty(GATEWAY_PROPERTY_FILE, HOME_COMMUNITY_DESC_PROPERTY);
            String c32 = PropertyAccessor.getProperty(ADAPTER_PROPERTY_FILE, C32_STYLE_SHEET_PROPERTY);

            String bos = PropertyAccessor.getProperty(ADAPTER_PROPERTY_FILE, BOS_SERVICE_WSDL_PROPERTY);
            String assigningauth = PropertyAccessor.getProperty(ADAPTER_PROPERTY_FILE, ASSIGNING_AUTHORITY_ID_PROPERTY);
            String defaultpou = PropertyAccessor.getProperty(ADAPTER_PROPERTY_FILE, DEFAULT_PURPOSE_OF_USE_PROPERTY);
            String purposeofusecode = PropertyAccessor.getProperty(ADAPTER_PROPERTY_FILE, PURPOSE_OF_USE_CODE_PROPERTY);
            String purposeofusesystem = PropertyAccessor.getProperty(ADAPTER_PROPERTY_FILE, PURPOSE_OF_USE_CODE_SYSTEM_PROPERTY);
            String purposeofusesystemname = PropertyAccessor.getProperty(ADAPTER_PROPERTY_FILE, PURPOSE_OF_USE_CODE_SYSTEM_NAME_PROPERTY);
            String purposeofusesystemversion = PropertyAccessor.getProperty(ADAPTER_PROPERTY_FILE, PURPOSE_OF_USE_CODE_SYSTEM_VERSION_PROPERTY);
            String userorganization = PropertyAccessor.getProperty(ADAPTER_PROPERTY_FILE, USER_ORGANIZATION_PROPERTY);
            String userrolecode = PropertyAccessor.getProperty(ADAPTER_PROPERTY_FILE, USER_ROLE_CODE_PROPERTY);
            String userrolecodesystem = PropertyAccessor.getProperty(ADAPTER_PROPERTY_FILE, USER_ROLE_CODE_SYSTEM_PROPERTY);
            String userrolecodesystemname = PropertyAccessor.getProperty(ADAPTER_PROPERTY_FILE, USER_ROLE_CODE_SYSTEM_NAME_PROPERTY);
            String userrolecodesystemversion = PropertyAccessor.getProperty(ADAPTER_PROPERTY_FILE, USER_ROLE_CODE_SYSTEM_VERSION_PROPERTY);
            String userroledisplayname = PropertyAccessor.getProperty(ADAPTER_PROPERTY_FILE, USER_ROLE_DISPLAY_NAME_DEFAULT_PROPERTY);
            String docviewerws = PropertyAccessor.getProperty(ADAPTER_PROPERTY_FILE, DOCVIEWER_REQUEST_SERVICE_PROPERTY);
            String claimformref = PropertyAccessor.getProperty(ADAPTER_PROPERTY_FILE, CLAIM_FORM_REF_PROPERTY);
            String claimformstring = PropertyAccessor.getProperty(ADAPTER_PROPERTY_FILE, CLAIM_FORM_STRING_PROPERTY);
            String signaturedate = PropertyAccessor.getProperty(ADAPTER_PROPERTY_FILE, SIGNATURE_DATE_PROPERTY);
            String expirationdate = PropertyAccessor.getProperty(ADAPTER_PROPERTY_FILE, EXPIRATION_DATE_PROPERTY);
            String numberofyearsstring = PropertyAccessor.getProperty(ADAPTER_PROPERTY_FILE, NUMBER_OF_YEARS_PROPERTY);
            String docmanagerservice = PropertyAccessor.getProperty(ADAPTER_PROPERTY_FILE, DOC_MANAGER_SERVICE_PROPERTY);

            //CAL Service Params
            String calservice = PropertyAccessor.getProperty(ADAPTER_PROPERTY_FILE, CAL_SERVICE_PROPERTY);
            String calservicewsdl = PropertyAccessor.getProperty(ADAPTER_PROPERTY_FILE, CAL_SERVICE_WSDL_PROPERTY);
            String calserviceqname = PropertyAccessor.getProperty(ADAPTER_PROPERTY_FILE, CAL_SERVICE_QNAME_PROPERTY);

            String nhindocretrieve = PropertyAccessor.getProperty(ADAPTER_PROPERTY_FILE, DOC_MANAGER_SERVICE_PROPERTY);

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
            setCALService(calservice);
            setCALServiceWSDL(calservicewsdl);
            setCALServiceQNAME(calserviceqname);
        }
        catch (Exception ex) {
            log.error("WARNING APPLICATIONBEAN "+ex.getMessage());
            log.error(ex);
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
    private String calService;
    private String calServiceWSDL;
    private String calServiceQname;


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
    public String getCALService() {
        return calService;
    }

    /**
     * @param dodConnectorService the dodConnectorService to set
     */
    public void setCALService(String dodConnectorService) {
        this.calService = dodConnectorService;
    }

     /**
     * @return the dodConnectorWSDLURL
     */
    public String getCALServiceWSDL() {
        return calServiceWSDL;
    }

    /**
     * @param dodConnectorWSDLURL the dodConnectorWSDLURL to set
     */
    public void setCALServiceWSDL(String calServiceWSDL) {
        this.calServiceWSDL = calServiceWSDL;
    }

     /**
     * @return the dodConnectorQname
     */
    public String getCALServiceQNAME() {
        return calServiceQname;
    }

    /**
     * @param dodConnectorWSDLURL the dodConnectorWSDLURL to set
     */
    public void setCALServiceQNAME(String calServiceQname) {
        this.calServiceQname = calServiceQname;
    }


}
