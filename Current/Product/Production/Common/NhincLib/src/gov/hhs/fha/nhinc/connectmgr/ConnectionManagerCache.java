package gov.hhs.fha.nhinc.connectmgr;

import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.data.CMBindingDescriptions;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.connectmgr.data.CMBindingTemplates;
import gov.hhs.fha.nhinc.connectmgr.data.CMBindingTemplate;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessEntity;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessEntities;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessDescriptions;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessNames;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessServices;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessService;
import gov.hhs.fha.nhinc.connectmgr.data.CMInternalConnectionInfo;
import gov.hhs.fha.nhinc.connectmgr.data.CMInternalConnectionInfos;
import gov.hhs.fha.nhinc.connectmgr.data.CMInternalConnInfoService;
import gov.hhs.fha.nhinc.connectmgr.data.CMInternalConnectionInfosXML;
import gov.hhs.fha.nhinc.connectmgr.data.CMUDDIConnectionInfo;
import gov.hhs.fha.nhinc.connectmgr.data.CMUDDIConnectionInfoXML;
import gov.hhs.fha.nhinc.connectmgr.data.CMHomeCommunity;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.util.StringUtil;
import java.io.File;

/**
 * This class is used to manage the Connection Manager's cache.  It handles
 * both internal connection settings and UDDI connection settings.  If there is
 * a collision for a connection between the UDDI and the Internal settings, the
 * internal one will be used.
 * 
 * @author Les Westberg
 */
public class ConnectionManagerCache {

    private static Log log = LogFactory.getLog(ConnectionManagerCache.class);
    private static final String CRLF = System.getProperty("line.separator");
    private static final String UDDI_XML_FILE_NAME = "uddiConnectionInfo.xml";
    private static final String INTERNAL_XML_FILE_NAME = "internalConnectionInfo.xml";
    private static final String NHINC_PROPERTIES_DIR = "NHINC_PROPERTIES_DIR";
    // Hash maps for the UDDI connectin information.  This hash map is keyed by home community ID.
    //--------------------------------------------------------------------------------------------
    private static HashMap<String, CMBusinessEntity> m_hUDDIConnectInfo = new HashMap<String, CMBusinessEntity>();       // Array of connection information
    private static boolean m_bUDDILoaded = false;      // TRUE if the properties have been loaded
    private static long m_lUDDIFileLastModified = 0;

    // Hash maps for the Internal connection information.  This hash map is keyed by home community ID.
    //--------------------------------------------------------------------------------------------------
    private static HashMap<String, CMInternalConnectionInfo> m_hInternalConnectInfo = new HashMap<String, CMInternalConnectionInfo>();       // Array of connection information
    private static boolean m_bInternalLoaded = false;      // TRUE if the properties have been loaded
    private static long m_lInternalFileLastModified = 0;

    // Variables for managing the location of the XML files.
    //-------------------------------------------------------
    private static String m_sPropertyFileDir = "";
    private static String m_sUDDIXMLfileDir = "";
    private static String m_sInternalXMLFileDir = "";
    private static String m_sFileSeparator = System.getProperty("file.separator");
    private static final String m_sFailedEnvVarMessage = "Unable to access environment variable: NHINC_PROPERTIES_DIR.";
    private static boolean m_bFailedToLoadEnvVar = false;


    static {
        String sValue = System.getenv(NHINC_PROPERTIES_DIR);
        if ((sValue != null) && (sValue.length() > 0)) {
            // Set it up so that we always have a "/" at the end - in case
            //------------------------------------------------------------
            if ((sValue.endsWith("/")) || (sValue.endsWith("\\"))) {
                m_sPropertyFileDir = sValue;
            } else {
                m_sPropertyFileDir = sValue + m_sFileSeparator;
            }

            m_sUDDIXMLfileDir = m_sPropertyFileDir + UDDI_XML_FILE_NAME;
            m_sInternalXMLFileDir = m_sPropertyFileDir + INTERNAL_XML_FILE_NAME;
        } else {
            log.error(m_sFailedEnvVarMessage);
            m_bFailedToLoadEnvVar = true;
        }
    }

    /**
     * This class is used for testing purposes so that the file locations can be overridden 
     * to point to a controlled location available for unit tests.
     * 
     * @param sUDDIFileName The path and file name for the UDDI XML file.
     * @param sInternalConnFileName The path and file name for the Internal Connectil File Name.
     */
    public static void overrideFileLocations(String sUDDIFileName, String sInternalConnFileName) {
        m_sUDDIXMLfileDir = sUDDIFileName;
        m_sInternalXMLFileDir = sInternalConnFileName;
    }

    /**
     * This method is used to load the UDDI Connection Infomration form the 
     * uddiConnectionInfo.xml file.
     */
    private static void loadUDDIConnectionInfo()
            throws ConnectionManagerException {
        // We can only proceed if we know where the files are...
        //--------------------------------------------------------
        if (m_bFailedToLoadEnvVar) {
            throw new ConnectionManagerException(m_sFailedEnvVarMessage);
        }

        String sUddiXml = "";

        try {
            sUddiXml = StringUtil.readTextFile(m_sUDDIXMLfileDir);
            File fUDDIFile = new File(m_sUDDIXMLfileDir);
            if (fUDDIFile.exists()) {
                m_lUDDIFileLastModified = fUDDIFile.lastModified();
            } else {
                m_lUDDIFileLastModified = 0;
            }
        } catch (Exception e) {
            String sErrorMessage = "Failed to read from file: '" + m_sUDDIXMLfileDir + "'.  Error: " + e.getMessage();
            log.error(sErrorMessage);
            throw new ConnectionManagerException(sErrorMessage, e);
        }

        log.debug("Setting UDDI cache to be: " + CRLF + sUddiXml);

        CMUDDIConnectionInfo oConnInfo = CMUDDIConnectionInfoXML.deserialize(sUddiXml);

        if (oConnInfo != null) {
            synchronized (m_hUDDIConnectInfo) {
                m_hUDDIConnectInfo.clear();

                if ((oConnInfo.getBusinessEntities() != null) &&
                        (oConnInfo.getBusinessEntities().getBusinessEntity() != null) &&
                        (oConnInfo.getBusinessEntities().getBusinessEntity().size() > 0)) {
                    for (CMBusinessEntity oEntity : oConnInfo.getBusinessEntities().getBusinessEntity()) {
                        String sHomeCommunityId = oEntity.getHomeCommunityId();
                        if ((sHomeCommunityId != null) && (sHomeCommunityId.length() > 0)) {
                            m_hUDDIConnectInfo.put(sHomeCommunityId, oEntity);
                        }
                    }   // for (CMBusinessEntity oEntity : oConnInfo.getBusinessEntities().getBusinessEntity())
                }   // if ((oConnInfo.getBusinessEntities() != null) && ...

                m_bUDDILoaded = true;
            }   // synchronized (m_ohUDDIConnectInfo)
        } // if (oConnInfo != null)
        else {
            log.warn("No UDDI information was found in: " + m_sUDDIXMLfileDir);
        }
    }

    /**
     * This returns true if the set of services contains a service for the given service name.
     * 
     * @param oServices The set of services to search.
     * @param sUniformServiceName The name of the service to search for.
     * @return True if the service is found.
     */
    private static boolean containsService(CMBusinessServices oServices, String sUniformServiceName) {
        boolean bFound = false;

        if ((oServices != null) &&
                (oServices.getBusinessService() != null) &&
                (oServices.getBusinessService().size() > 0) &&
                (sUniformServiceName != null) &&
                (sUniformServiceName.length() > 0)) {
            for (CMBusinessService oService : oServices.getBusinessService()) {
                if ((oService.getUniformServiceName() != null) &&
                        (oService.getUniformServiceName().equals(sUniformServiceName))) {
                    bFound = true;
                }

            }   // for (CMBusinessService oService : oServices.getBusinessService())
        }

        return bFound;

    }

    /**
     * This method merges the information from the internal connection information as well
     * as the ones from the external conenctions.   The internal information always
     * overrides the external.  When it comes to services, it does not do a piece wise compare
     * of the services.  If a service is defined internally, it will use the entire service.
     * 
     * @param oInternalEntity The internal business entitie
     * @param oUDDIEntity The UDDI entity
     * @return The combined information to be sent out.
     * @throws gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException
     */
    private static CMBusinessEntity mergeBusinessEntityServices(CMBusinessEntity oInternalEntity,
            CMBusinessEntity oUDDIEntity)
            throws ConnectionManagerException {
        if ((oInternalEntity == null) &&
                (oUDDIEntity != null)) {
            return oUDDIEntity;
        } else if ((oInternalEntity != null) &&
                (oUDDIEntity == null)) {
            return oInternalEntity;
        } else if ((oInternalEntity == null) &&
                (oUDDIEntity == null)) {
            return null;
        }

        // We are here so a merge needs to take place....
        //------------------------------------------------
        CMBusinessEntity oCombinedEntity = new CMBusinessEntity();

        // Start with the non service information from the UDDI
        //------------------------------------------------------
        oCombinedEntity.setBusinessKey(oUDDIEntity.getBusinessKey());
        oCombinedEntity.setContacts(oUDDIEntity.getContacts());
        oCombinedEntity.setDescriptions(oUDDIEntity.getDescriptions());
        oCombinedEntity.setDiscoveryURLs(oUDDIEntity.getDiscoveryURLs());
        oCombinedEntity.setFederalHIE(oUDDIEntity.isFederalHIE());
        oCombinedEntity.setHomeCommunityId(oUDDIEntity.getHomeCommunityId());
        oCombinedEntity.setNames(oUDDIEntity.getNames());
        oCombinedEntity.setPublicKey(oUDDIEntity.getPublicKey());
        oCombinedEntity.setPublicKeyURI(oUDDIEntity.getPublicKeyURI());
        oCombinedEntity.setStates(oUDDIEntity.getStates());

        // Put in all of the services from the InternalConnection one next - they are the king...
        //----------------------------------------------------------------------------------------
        oCombinedEntity.setBusinessServices(oInternalEntity.getBusinessServices());
        if (oCombinedEntity.getBusinessServices() == null) {
            oCombinedEntity.setBusinessServices(new CMBusinessServices());
        }

        // Now only add in the ones from the UDDI that we do not have
        //-------------------------------------------------------------
        if ((oUDDIEntity.getBusinessServices() != null) &&
                (oUDDIEntity.getBusinessServices().getBusinessService() != null) &&
                (oUDDIEntity.getBusinessServices().getBusinessService().size() > 0)) {
            for (CMBusinessService oService : oUDDIEntity.getBusinessServices().getBusinessService()) {
                if (!containsService(oCombinedEntity.getBusinessServices(), oService.getUniformServiceName())) {
                    oCombinedEntity.getBusinessServices().getBusinessService().add(oService);
                }
            }   // for (CMBusinessService oService : oUDDIEntity.getBusinessServices().getBusinessService())
        }   // if ((oUDDIEntity.getBusinessServices() != null) && ...


        return oCombinedEntity;
    }

    /**
     * This method simply checks to see if the cache is loaded.  If it is not, then 
     * it is loaded as a byproduct of calling this method.
     * 
     * @throws gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException
     */
    private static void checkLoaded()
            throws ConnectionManagerException {
        if (!m_bInternalLoaded) {
            forceRefreshInternalConnectCache();
        }

        if (!m_bUDDILoaded) {
            forceRefreshUDDICache();
        }

        // One last check for refreshing...
        //---------------------------------
        refreshIfExpired();
    }

    /**
     * This method is used to load the UDDI Connection Infomration form the 
     * uddiConnectionInfo.xml file.
     */
    private static void loadInternalConnectionInfo()
            throws ConnectionManagerException {
        // We can only proceed if we know where the files are...
        //--------------------------------------------------------
        if (m_bFailedToLoadEnvVar) {
            throw new ConnectionManagerException(m_sFailedEnvVarMessage);
        }

        String sInternalConnXml = "";

        try {
            sInternalConnXml = StringUtil.readTextFile(m_sInternalXMLFileDir);
            File fInternalFile = new File(m_sInternalXMLFileDir);
            if (fInternalFile.exists()) {
                m_lInternalFileLastModified = fInternalFile.lastModified();
            } else {
                m_lInternalFileLastModified = 0;
            }
        } catch (Exception e) {
            String sErrorMessage = "Failed to read from file: '" + m_sInternalXMLFileDir + "'.  Error: " + e.getMessage();
            log.error(sErrorMessage);
            throw new ConnectionManagerException(sErrorMessage, e);
        }

        log.debug("Setting internal connection cache to be: " + CRLF + sInternalConnXml);

        CMInternalConnectionInfos oConnInfos = CMInternalConnectionInfosXML.deserialize(sInternalConnXml);

        if (oConnInfos != null) {
            synchronized (m_hInternalConnectInfo) {
                m_hInternalConnectInfo.clear();

                if ((oConnInfos.getInternalConnectionInfo() != null) &&
                        (oConnInfos.getInternalConnectionInfo().size() > 0)) {
                    for (CMInternalConnectionInfo oConnInfo : oConnInfos.getInternalConnectionInfo()) {
                        String sHomeCommunityId = oConnInfo.getHomeCommunityId();
                        if ((sHomeCommunityId != null) && (sHomeCommunityId.length() > 0)) {
                            m_hInternalConnectInfo.put(sHomeCommunityId, oConnInfo);
                        }
                    }   // for (CMInternalConnectionInfo oConnInfo : oConnInfos.getInternalConnectionInfo())
                }   // if ((oConnInfos.getInternalConnectionInfo() != null) &&...

                m_bInternalLoaded = true;

            }   // synchronized (m_hInternalConnectInfo)
        } // if (oConnInfos != null)
        else {
            log.warn("No UDDI information was found in: " + m_sUDDIXMLfileDir);
        }
    }

    /**
     * This method will cause the ConnectionManagerCache to refresh the UDDI connection data
     * by replacing the cached UDDI information with the information in the uddiConnectionInfo.xml file.
     * @throws ConnectionManagerException 
     */
    public static void forceRefreshUDDICache()
            throws ConnectionManagerException {
        loadUDDIConnectionInfo();
    }

    /**
     * This method will cause the ConnectionManagerCache to refresh the internal connection data
     * by replacing the cached internal connection information with the information in 
     * the internalConnectionInfo.xml file.
     * @throws ConnectionManagerException 
     */
    public static void forceRefreshInternalConnectCache()
            throws ConnectionManagerException {
        loadInternalConnectionInfo();
    }

    /**
     * This method checks to see if either cache has expired and forces a refresh if it has.
     * 
     */
    private static void refreshIfExpired()
            throws ConnectionManagerException {
        long lUDDILastModified = 0;
        long lInternalLastModified = 0;

        // Find out our refrhes timing from the properties file.
        //-------------------------------------------------------
        try {
            File fUDDIFile = new File(m_sUDDIXMLfileDir);
            if (fUDDIFile.exists()) {
                lUDDILastModified = fUDDIFile.lastModified();
            }
            File fInternalFile = new File(m_sInternalXMLFileDir);
            if (fInternalFile.exists()) {
                lInternalLastModified = fInternalFile.lastModified();
            }
        } catch (Exception e) {
            // Assume a refresh is required...  But log a message.
            //----------------------------------------------------
            String sErrorMessage = "Failed to retrieve last modified dates on the connection manager XML files." +
                    "Error: " + e.getMessage();
            log.warn(sErrorMessage, e);
        }

        // If we need to refresh the UDDI cache information.
        //--------------------------------------------------
        if (lUDDILastModified != m_lUDDIFileLastModified) {
            forceRefreshUDDICache();
            log.info("UDDI cache was refreshed based on last modified time stamp change.");
        }

        if (lInternalLastModified != m_lInternalFileLastModified) {
            forceRefreshInternalConnectCache();
            log.info("Internal connection cache was refreshed based on last modified time stamp change.");
        }
    }

    /**
     * This method extracts the home community information from the CMInternalConnectionInfo object
     * and places it into a new CMHomeCommunity object and returns it.
     * 
     * @param oConnInfo The connection information to be transformed.
     * @return The HomeCommunity information.
     */
    private static CMHomeCommunity homeCommunityFromInternalConnectionInfo(CMInternalConnectionInfo oConnInfo) {
        CMHomeCommunity oComm = new CMHomeCommunity();

        if (oConnInfo != null) {
            if (oConnInfo.getHomeCommunityId() != null) {
                oComm.setHomeCommunityId(oConnInfo.getHomeCommunityId());
            }

            if (oConnInfo.getName() != null) {
                oComm.setName(oConnInfo.getName());
            }

            if (oConnInfo.getDescription() != null) {
                oComm.setDescription(oConnInfo.getDescription());
            }
        }

        return oComm;
    }

    /**
     * This method extracts the fields from the CMInternalConnectionInfo object and creats a
     * CMBusinessEntity object with the data. 
     * 
     * @param oConnInfo The connection information to be transformed.
     * @return The CMBusinessEntity object from the information.
     */
    private static CMBusinessEntity businessEntityFromInternalConnectionInfo(CMInternalConnectionInfo oConnInfo) {
        CMBusinessEntity oEntity = new CMBusinessEntity();

        if (oConnInfo != null) {
            // Home Community ID
            if (oConnInfo.getHomeCommunityId() != null) {
                oEntity.setHomeCommunityId(oConnInfo.getHomeCommunityId());
            }

            // Name
            //------
            if (oConnInfo.getName() != null) {
                CMBusinessNames oNames = new CMBusinessNames();
                oEntity.setNames(oNames);
                oNames.getBusinessName().add(oConnInfo.getName());
            }

            // Description
            //-------------
            if (oConnInfo.getDescription() != null) {
                CMBusinessDescriptions oDescriptions = new CMBusinessDescriptions();
                oEntity.setDescriptions(oDescriptions);
                oDescriptions.getBusinessDescription().add(oConnInfo.getDescription());
            }

            // Services
            //----------
            if ((oConnInfo.getServices() != null) &&
                    (oConnInfo.getServices().getService() != null) &&
                    (oConnInfo.getServices().getService().size() > 0)) {
                CMBusinessServices oBusinessServices = new CMBusinessServices();
                oEntity.setBusinessServices(oBusinessServices);

                for (CMInternalConnInfoService oService : oConnInfo.getServices().getService()) {
                    CMBusinessService oBusinessService = new CMBusinessService();
                    oBusinessServices.getBusinessService().add(oBusinessService);

                    // Service Name
                    //-------------
                    if (oService.getName() != null) {
                        oBusinessService.setUniformServiceName(oService.getName());
                    }

                    // Description
                    //-------------
                    if (oService.getDescription() != null) {
                        CMBindingDescriptions oBindingDescriptions = new CMBindingDescriptions();
                        oBusinessService.setDescriptions(oBindingDescriptions);
                        oBindingDescriptions.getDescription().add(oService.getDescription());
                    }

                    // Is this External or internal?
                    //-------------------------------
                    if (oService.isExternalService()) {
                        oBusinessService.setInternalWebService(false);
                    } else {
                        oBusinessService.setInternalWebService(true);
                    }

                    // Endpoint URL
                    //--------------
                    if (oService.getEndpointURL() != null) {
                        CMBindingTemplates oTemplates = new CMBindingTemplates();
                        oBusinessService.setBindingTemplates(oTemplates);
                        CMBindingTemplate oTemplate = new CMBindingTemplate();
                        oTemplates.getBindingTemplate().add(oTemplate);
                        oTemplate.setEndpointURL(oService.getEndpointURL());
                    }   // if (oService.getEndpointURL() != null)
                }   // for (CMInternalConnInfoService oService : oConnInfo.getServices().getService())
            }   // if ((oConnInfo.getServices() != null) &&
        }   // if (oConnInfo != null)

        return oEntity;
    }

    /**
     * This method extracts the home community information from the CMBusinessEntity object
     * and places it into a new CMHomeCommunity object and returns it.
     * 
     * @param oConnInfo The connection information to be transformed.
     * @return The HomeCommunity information.
     */
    private static CMHomeCommunity homeCommunityFromBusinessEntity(CMBusinessEntity oEntity) {
        CMHomeCommunity oComm = new CMHomeCommunity();

        if (oEntity != null) {
            if (oEntity.getHomeCommunityId() != null) {
                oComm.setHomeCommunityId(oEntity.getHomeCommunityId());
            }

            // Since the UDDI can contain multiple names, we will only return the first one.
            //------------------------------------------------------------------------------
            if ((oEntity.getNames() != null) &&
                    (oEntity.getNames().getBusinessName() != null) &&
                    (oEntity.getNames().getBusinessName().size() > 0)) {
                oComm.setName(oEntity.getNames().getBusinessName().get(0));
            }

            // Since the UDDI can contain multiple descriptions, we will only return the first one.
            //-------------------------------------------------------------------------------------
            if ((oEntity.getDescriptions() != null) &&
                    (oEntity.getDescriptions().getBusinessDescription() != null) &&
                    (oEntity.getDescriptions().getBusinessDescription().size() > 0)) {
                oComm.setDescription(oEntity.getDescriptions().getBusinessDescription().get(0));
            }
        }

        return oComm;
    }

    /**
     * This method will return a list of all home commuinities that are known by the
     * connection manager.
     * 
     * @return The list of all home communities known by the connection manager.
     * @throws ConnectionManagerException 
     */
    public static List<CMHomeCommunity> getAllCommunities()
            throws ConnectionManagerException {
        HashSet<String> hHomeCommunities = new HashSet<String>();
        ArrayList<CMHomeCommunity> oaHomeCommunities = new ArrayList<CMHomeCommunity>();

        checkLoaded();

        // First get the infomration from the internal connections.
        //---------------------------------------------------------
        Collection<CMInternalConnectionInfo> colInternConn = m_hInternalConnectInfo.values();
        for (CMInternalConnectionInfo oConnInfo : colInternConn) {
            CMHomeCommunity oComm = homeCommunityFromInternalConnectionInfo(oConnInfo);
            if ((oComm.getHomeCommunityId() != null) && (oComm.getHomeCommunityId().length() > 0)) {
                hHomeCommunities.add(oComm.getHomeCommunityId());
                oaHomeCommunities.add(oComm);
            }
        }

        // Next get the information from the UDDI connections - Only add them if we have not
        // already gotten them from the internal settings.
        //-----------------------------------------------------------------------------------
        Collection<CMBusinessEntity> colEntity = m_hUDDIConnectInfo.values();
        for (CMBusinessEntity oEntity : colEntity) {
            CMHomeCommunity oComm = homeCommunityFromBusinessEntity(oEntity);
            if ((oComm.getHomeCommunityId() != null) &&
                    (oComm.getHomeCommunityId().length() > 0) &&
                    (!hHomeCommunities.contains(oComm.getHomeCommunityId()))) // make sure it is not alrady in the list
            {
                hHomeCommunities.add(oComm.getHomeCommunityId());
                oaHomeCommunities.add(oComm);
            }
        }

        return oaHomeCommunities;

    }

    /**
     * This method looks for the entity with the given home community ID and returns it.
     * 
     * @param oEntities The entities to be searched.
     * @param sHomeCommunityId The home community ID to search for.
     * @return The business entity for that home community.
     */
    private static CMBusinessEntity extractBusinessEntity(CMBusinessEntities oEntities, String sHomeCommunityId) {
        if ((oEntities == null) ||
                (oEntities.getBusinessEntity() == null) ||
                (oEntities.getBusinessEntity().size() <= 0) ||
                (sHomeCommunityId == null) ||
                (sHomeCommunityId.length() <= 0)) {
            return null;
        }

        for (CMBusinessEntity oEntity : oEntities.getBusinessEntity()) {
            if ((oEntity.getHomeCommunityId() != null) &&
                    (oEntity.getHomeCommunityId().equals(sHomeCommunityId))) {
                return oEntity;
            }
        }

        return null;            // If we got here - we never found it.
    }

    /**
     * This method searches for the business entity in the list that has the same
     * home community Id.  If it finds it, it replaces it with this one.  If it
     * does not find it, then it adds this one to the list.
     * 
     * @param oEntities The entities to search.
     * @param oEntity The entity to replace...
     */
    private static void replaceBusinessEntity(CMBusinessEntities oEntities, CMBusinessEntity oEntity) {
        if ((oEntities == null) ||
                (oEntity == null)) {
            return;         // there is nothing to do...
        }

        int iCnt = oEntities.getBusinessEntity().size();
        if (iCnt == 0) {
            oEntities.getBusinessEntity().add(oEntity);
            return;
        }

        boolean bReplaced = false;
        for (int i = 0; i < iCnt; i++) {
            CMBusinessEntity oLocalEntity = oEntities.getBusinessEntity().get(i);
            if ((oLocalEntity.getHomeCommunityId() != null) &&
                    (oEntity.getHomeCommunityId() != null) &&
                    (oLocalEntity.getHomeCommunityId().equals(oEntity.getHomeCommunityId()))) {
                oEntities.getBusinessEntity().set(i, oEntity);
                bReplaced = true;
                break;          // We are done - get out of here.
            }
        }   // for (int i = 0; i < iCnt; i++)

        if (!bReplaced) {
            oEntities.getBusinessEntity().add(oEntity);
        }
    }

    /**
     * This method will return a list of all business entities that are known by the
     * connection manager.
     * 
     * @return The list of all business entities known by the connection manager.
     * @throws ConnectionManagerException 
     */
    public static CMBusinessEntities getAllBusinessEntities()
            throws ConnectionManagerException {
        HashSet<String> hEntities = new HashSet<String>();
        CMBusinessEntities oEntities = new CMBusinessEntities();

        checkLoaded();

        // First get the infomration from the internal connections.
        //---------------------------------------------------------
        Collection<CMInternalConnectionInfo> colInternConn = m_hInternalConnectInfo.values();
        for (CMInternalConnectionInfo oConnInfo : colInternConn) {
            CMBusinessEntity oEntity = businessEntityFromInternalConnectionInfo(oConnInfo);
            if ((oEntity.getHomeCommunityId() != null) && (oEntity.getHomeCommunityId().length() > 0)) {
                hEntities.add(oEntity.getHomeCommunityId());
                oEntities.getBusinessEntity().add(oEntity);
            }
        }

        // Next get the information from the UDDI connections - 
        // If it is in the list, then merge the services.  If not, then
        // add it as is.
        //-----------------------------------------------------------------------------------
        Collection<CMBusinessEntity> colEntity = m_hUDDIConnectInfo.values();
        for (CMBusinessEntity oEntity : colEntity) {
            if ((oEntity.getHomeCommunityId() != null) &&
                    (oEntity.getHomeCommunityId().length() > 0)) {
                if (hEntities.contains(oEntity.getHomeCommunityId())) {
                    CMBusinessEntity oExistingEntity = extractBusinessEntity(oEntities, oEntity.getHomeCommunityId());
                    if (oExistingEntity != null) {
                        oExistingEntity = mergeBusinessEntityServices(oExistingEntity, oEntity);
                        replaceBusinessEntity(oEntities, oExistingEntity);
                    } else {
                        // We should never get here - but just in case...
                        oEntities.getBusinessEntity().add(oEntity);
                    }
                } else {
                    hEntities.add(oEntity.getHomeCommunityId());
                    oEntities.getBusinessEntity().add(oEntity);
                }
            }
        }

        return oEntities;

    }

    /**
     * This class returns the business entity information associated with
     * the specified home community ID.
     * 
     * @param sHomeCommunityId The home commuinity ID that is being searched for.
     * @return the business entity information for the specified home community.
     * @throws gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException
     */
    public static CMBusinessEntity getBusinessEntity(String sHomeCommunityId)
            throws ConnectionManagerException {
        CMBusinessEntity oReturnEntity = null;

        checkLoaded();

        CMBusinessEntity oInternalEntity = null;
        CMBusinessEntity oUDDIEntity = null;

        // First look in Internal connections...
        //--------------------------------------
        if (m_hInternalConnectInfo.containsKey(sHomeCommunityId)) {
            CMInternalConnectionInfo oConnInfo = m_hInternalConnectInfo.get(sHomeCommunityId);
            oInternalEntity = businessEntityFromInternalConnectionInfo(oConnInfo);
        }

        // Look for a UDDI one
        //--------------------
        if (m_hUDDIConnectInfo.containsKey(sHomeCommunityId)) {
            oUDDIEntity = m_hUDDIConnectInfo.get(sHomeCommunityId);
        }

        if ((oInternalEntity != null) && (oUDDIEntity != null)) {
            oReturnEntity = mergeBusinessEntityServices(oInternalEntity, oUDDIEntity);
        } else if (oInternalEntity != null) {
            oReturnEntity = oInternalEntity;
        } else if (oUDDIEntity != null) {
            oReturnEntity = oUDDIEntity;
        }

        return oReturnEntity;
    }

    /**
     * This method returns the business entity information for the set of home
     * communities.
     * 
     * @param saHomeCommunityId The set of home communities to be retrieved.
     * @return The business entities found.
     * @throws gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException
     */
    public static CMBusinessEntities getBusinessEntitySet(List<String> saHomeCommunityId)
            throws ConnectionManagerException {
        CMBusinessEntities oEntities = new CMBusinessEntities();

        checkLoaded();

        if ((saHomeCommunityId == null) || (saHomeCommunityId.size() <= 0)) {
            return null;
        }

        // We always first look in our internal list - then in the UDDI one...
        //---------------------------------------------------------------------
        for (String sHomeCommunityId : saHomeCommunityId) {
            CMBusinessEntity oEntity = getBusinessEntity(sHomeCommunityId);
            if (oEntity != null) {
                oEntities.getBusinessEntity().add(oEntity);
            }
        }

        if (oEntities.getBusinessEntity().size() > 0) {
            return oEntities;
        } else {
            return null;
        }
    }

    /**
     * This method retrieves the business entity information and service information
     * for the specific home community and service name.  Note:   This will only return
     * the information for the specified service.  It will not return all services.
     * Also note: This currently does not deal with version.  If there are multiple
     * versions of the same serviec, this will return the first one it sees in the list
     * of services.  As always, it will always first look in the InternalConnectionInfo
     * cache for the business entity.  If it finds the business entity there, it will not
     * look in the UDDI cache.  (This means that if the internal cache contains the 
     * given business entity, but it does not contain the requested service, it will 
     * behave as if the service does not exist - regardless of whether it is in the 
     * UDDI cache or not.
     * 
     * @param sHomeCommunityId The home community ID of the gateway that is being looked up.
     * @param sUniformServiceName The name of the service to locate.
     * @return The Business Entity information along with only the requested service.  if the
     *         service is not found, then null is returned.
     * @throws ConnectionManagerException
     */
    public static CMBusinessEntity getBusinessEntityByServiceName(String sHomeCommunityId,
            String sUniformServiceName)
            throws ConnectionManagerException {
        CMBusinessEntity oEntity = null;

        checkLoaded();

        if ((sHomeCommunityId == null) || (sHomeCommunityId.length() <= 0) ||
                (sUniformServiceName == null) || (sUniformServiceName.length() <= 0)) {
            return null;
        }

        CMBusinessEntity oInternalEntity = null;
        CMBusinessEntity oUDDIEntity = null;

        if (m_hInternalConnectInfo.containsKey(sHomeCommunityId)) {
            CMInternalConnectionInfo oConnInfo = m_hInternalConnectInfo.get(sHomeCommunityId);
            oInternalEntity = businessEntityFromInternalConnectionInfo(oConnInfo);
        }

        if (m_hUDDIConnectInfo.containsKey(sHomeCommunityId)) {
            oUDDIEntity = m_hUDDIConnectInfo.get(sHomeCommunityId);
        }

        CMBusinessEntity oCombinedEntity = null;
        if ((oInternalEntity != null) && (oUDDIEntity != null)) {
            oCombinedEntity = mergeBusinessEntityServices(oInternalEntity, oUDDIEntity);
        } else if (oInternalEntity != null) {
            oCombinedEntity = oInternalEntity;
        } else if (oUDDIEntity != null) {
            oCombinedEntity = oUDDIEntity;
        } else {
            return null;            // We found nothing...
        }

        // Now lets see if it has the service we are looking for.
        //--------------------------------------------------------
        CMBusinessService oTempService = null;

        if ((oCombinedEntity != null) &&
                (oCombinedEntity.getBusinessServices() != null) &&
                (oCombinedEntity.getBusinessServices().getBusinessService() != null) &&
                (oCombinedEntity.getBusinessServices().getBusinessService().size() > 0)) {
            for (CMBusinessService oService : oCombinedEntity.getBusinessServices().getBusinessService()) {
                if (oService.getUniformServiceName().equals(sUniformServiceName)) {
                    oTempService = oService;
                    break;          // We found it - get out of here...
                }
            }

            // Did we find the service we were looking for?
            //----------------------------------------------
            if (oTempService != null) {
                // Let's make a copy of the information - because we only want to return the
                // one service.
                //---------------------------------------------------------------------------
                oEntity = new CMBusinessEntity();
                oEntity.setBusinessKey(oCombinedEntity.getBusinessKey());
                oEntity.setDiscoveryURLs(oCombinedEntity.getDiscoveryURLs());
                oEntity.setNames(oCombinedEntity.getNames());
                oEntity.setDescriptions(oCombinedEntity.getDescriptions());
                oEntity.setContacts(oCombinedEntity.getContacts());
                oEntity.setHomeCommunityId(oCombinedEntity.getHomeCommunityId());
                oEntity.setStates(oCombinedEntity.getStates());
                oEntity.setFederalHIE(oCombinedEntity.isFederalHIE());
                oEntity.setPublicKeyURI(oCombinedEntity.getPublicKeyURI());
                oEntity.setPublicKey(oCombinedEntity.getPublicKey());
                oEntity.setBusinessServices(new CMBusinessServices());
                oEntity.getBusinessServices().getBusinessService().add(oTempService);
            }
        }

        return oEntity;

    }

    /**
     * This method retrieves the business entity information and service information
     * for the specific home community and service name.  Note:   This will only return
     * the information for the specified service.  It will not return all services.
     * Also note: This currently does not deal with version.  If there are multiple
     * versions of the same service, this will return the first one it sees in the list
     * of services.  As always, it will always first look in the InternalConnectionInfo
     * cache for the business entity.  If it finds the business entity there, it will not
     * look in the UDDI cache.  This means that if the internal cache contains the 
     * given business entity, but it does not contain the requested service, it will 
     * behave as if the service does not exist - regardless of whether it is in the 
     * UDDI cache or not.
     * 
     * @param sHomeCommunityId The home community ID of the gateway that is being looked up.
     * @param sUniformServiceName The name of the service to locate.
     * @return The URL for only the requested service at the specified home community.
     *         If the service is not found, then null is returned.
     * @throws ConnectionManagerException
     */
    public static String getEndpointURLByServiceName(String sHomeCommunityId,
            String sUniformServiceName)
            throws ConnectionManagerException {
        String sEndpointURL = "";

        CMBusinessEntity oEntity = getBusinessEntityByServiceName(sHomeCommunityId, sUniformServiceName);

        if ((oEntity != null) &&
                (oEntity.getBusinessServices() != null) &&
                (oEntity.getBusinessServices().getBusinessService() != null) &&
                (oEntity.getBusinessServices().getBusinessService().size() > 0) &&
                (oEntity.getBusinessServices().getBusinessService().get(0) != null) &&
                (oEntity.getBusinessServices().getBusinessService().get(0).getBindingTemplates() != null) &&
                (oEntity.getBusinessServices().getBusinessService().get(0).getBindingTemplates().getBindingTemplate() != null) &&
                (oEntity.getBusinessServices().getBusinessService().get(0).getBindingTemplates().getBindingTemplate().size() > 0) &&
                (oEntity.getBusinessServices().getBusinessService().get(0).getBindingTemplates().getBindingTemplate().get(0) != null) &&
                (oEntity.getBusinessServices().getBusinessService().get(0).getBindingTemplates().getBindingTemplate().get(0).getEndpointURL() != null) &&
                (oEntity.getBusinessServices().getBusinessService().get(0).getBindingTemplates().getBindingTemplate().get(0).getEndpointURL().trim().length() > 0)) {
            sEndpointURL = oEntity.getBusinessServices().getBusinessService().get(0).getBindingTemplates().getBindingTemplate().get(0).getEndpointURL().trim();
        }

        return sEndpointURL;
    }

    /**
     * This method retrieves the business entity information and service information
     * for the local home community and service name.  Note:   This will only return
     * the information for the specified service.  It will not return all services.
     * Also note: This currently does not deal with version.  If there are multiple
     * versions of the same service, this will return the first one it sees in the list
     * of services.  As always, it will always first look in the InternalConnectionInfo
     * cache for the business entity.  If it finds the business entity there, it will not
     * look in the UDDI cache.  This means that if the internal cache contains the
     * given business entity, but it does not contain the requested service, it will
     * behave as if the service does not exist - regardless of whether it is in the
     * UDDI cache or not.
     *
     * @param sUniformServiceName The name of the service to locate.
     * @return The URL for only the requested service at the local home community.
     *         If the service is not found, then null is returned.
     * @throws ConnectionManagerException
     */
    public static String getLocalEndpointURLByServiceName(String sUniformServiceName)
            throws ConnectionManagerException {
        String sHomeCommunityId = null;
        String sEndpointURL = null;

        try {
            log.info("Attempting to retrieve property: " + NhincConstants.HOME_COMMUNITY_ID_PROPERTY + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            sHomeCommunityId = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
            log.info("Retrieve local home community id: " + sHomeCommunityId);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.HOME_COMMUNITY_ID_PROPERTY + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }

        if (NullChecker.isNotNullish(sHomeCommunityId)) {
            sEndpointURL = getEndpointURLByServiceName(sHomeCommunityId, sUniformServiceName);
        }

        return sEndpointURL;
    }

    /**
     * This method retrieves the URL from the contents of the NhinTargetSystem type.  It will first check to
     * see if the EPR (Endpoint Reference) is already provided, if so it will extract the URL from the EPR
     * and return it to the caller.  If the EPR is not provided it will check if the URL field is provided,
     * if so it will return the URL to the caller.  If neither an EPR or URL are provided this method will
     * use the home community id and service name provided to lookup the URL for that service at that particular
     * home community.
     *
     * @param targetSystem The target system information for the community being looked up.
     * @param serviceName The name of the service to locate who URL is being requested.
     * @return The URL to the requested service.
     * @throws ConnectionManagerException
     */
    public static String getEndpontURLFromNhinTarget(NhinTargetSystemType targetSystem, String serviceName)
            throws ConnectionManagerException {
        String sEndpointURL = null;

        if (targetSystem != null) {
            if (targetSystem.getEpr() != null) {
                // Extract the URL from the Endpoint Reference
                log.debug("Attempting to look up URL by EPR");
                if (targetSystem.getEpr().getEndpointReference() != null &&
                        targetSystem.getEpr().getEndpointReference().getAddress() != null &&
                        NullChecker.isNotNullish(targetSystem.getEpr().getEndpointReference().getAddress().getValue())) {
                    sEndpointURL = targetSystem.getEpr().getEndpointReference().getAddress().getValue();
                }
            } else if (NullChecker.isNotNullish(targetSystem.getUrl())) {
                // Echo back the URL provided
                log.debug("Attempting to look up URL by URL");
                sEndpointURL = targetSystem.getUrl();
            } else if (targetSystem.getHomeCommunity() != null &&
                    NullChecker.isNotNullish(targetSystem.getHomeCommunity().getHomeCommunityId()) &&
                    NullChecker.isNotNullish(serviceName)) {
                // Get the URL based on Home Community Id and Service Name
                log.debug("Attempting to look up URL by home communinity id: " + targetSystem.getHomeCommunity().getHomeCommunityId() + " and service name: " + serviceName);
                sEndpointURL = ConnectionManagerCache.getEndpointURLByServiceName(targetSystem.getHomeCommunity().getHomeCommunityId(), serviceName);
            }
        }

        log.debug("Returning URL: " + sEndpointURL);
        return sEndpointURL;
    }

    /**
     * This method retrieves the business entity information and service information
     * for the set of home communities and service name.  Note:   This will only return
     * the information for the specified service.  It will not return all services.
     * Also note: This currently does not deal with version.  If there are multiple
     * versions of the same service, this will return the first one it sees in the list
     * of services.  As always, it will always first look in the InternalConnectionInfo
     * cache for the business entity.  If it finds the business entity there, it will not
     * look in the UDDI cache.  (This means that if the internal cache contains the 
     * given business entity, but it does not contain the requested service, it will 
     * behave as if the service does not exist - regardless of whether it is in the 
     * UDDI cache or not.
     * 
     * @param saHomeCommunityId The home community IDs of the gateways that is being looked up.
     * @param sUniformServiceName The name of the service to locate.
     * @return The Business Entity information along with only the requested service.  If the
     *         service is not found, it will not be returned even if the business entity
     *         information exists.
     * @throws ConnectionManagerException
     */
    public static CMBusinessEntities getBusinessEntitySetByServiceName(List<String> saHomeCommunityId, String sUniformServiceName)
            throws ConnectionManagerException {
        CMBusinessEntities oEntities = new CMBusinessEntities();

        checkLoaded();

        if ((saHomeCommunityId == null) || (saHomeCommunityId.size() <= 0) ||
                (sUniformServiceName == null) || (sUniformServiceName.length() <= 0)) {
            return null;
        }

        for (String sHomeCommunityId : saHomeCommunityId) {
            CMBusinessEntity oEntity = getBusinessEntityByServiceName(sHomeCommunityId, sUniformServiceName);
            if (oEntity != null) {
                oEntities.getBusinessEntity().add(oEntity);
            }
        }

        if (oEntities.getBusinessEntity().size() > 0) {
            return oEntities;
        } else {
            return null;
        }
    }

    /**
     * This method retrieves the business entity information and service information
     * for the set of home communities that contains a service by that service name.  
     * Note:   This will only return the information for the specified service.  It will 
     * not return all services. Also note: This currently does not deal with version.  If 
     * there are multiple versions of the same service, this will return the first one it sees in the list
     * of services.  As always, it will always first look in the InternalConnectionInfo
     * cache for the business entity.  If it finds the business entity there, it will not
     * look in the UDDI cache.  (This means that if the internal cache contains the 
     * given business entity, but it does not contain the requested service, it will 
     * behave as if the service does not exist - regardless of whether it is in the 
     * UDDI cache or not.
     * 
     * @param sUniformServiceName The name of the service being searched for.
     * @return The business entities that have this service defined.
     * @throws gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException
     */
    public static CMBusinessEntities getAllBusinessEntitySetByServiceName(String sUniformServiceName)
            throws ConnectionManagerException {
        CMBusinessEntities oEntities = new CMBusinessEntities();

        checkLoaded();

        HashSet<String> hKeys = new HashSet<String>();

        // This is a slick way to add them all and remove any duplicates...
        //------------------------------------------------------------------
        hKeys.addAll(m_hInternalConnectInfo.keySet());
        hKeys.addAll(m_hUDDIConnectInfo.keySet());

        ArrayList<String> saHomeCommunityIds = new ArrayList<String>(hKeys);
        oEntities = getBusinessEntitySetByServiceName(saHomeCommunityIds, sUniformServiceName);

        if ((oEntities != null) && (oEntities.getBusinessEntity().size() > 0)) {
            return oEntities;
        } else {
            return null;
        }
    }
}
