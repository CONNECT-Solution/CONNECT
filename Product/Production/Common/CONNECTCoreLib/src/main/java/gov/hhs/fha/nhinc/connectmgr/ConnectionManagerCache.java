/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package gov.hhs.fha.nhinc.connectmgr;

import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.persistance.dao.InternalConnectionInfoDAOFileImpl;
import gov.hhs.fha.nhinc.connectmgr.persistance.dao.UddiConnectionInfoDAOFileImpl;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.UDDI_SPEC_VERSION;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.uddi.api_v3.BindingTemplate;
import org.uddi.api_v3.BusinessDetail;
import org.uddi.api_v3.BusinessEntity;
import org.uddi.api_v3.BusinessService;
import org.uddi.api_v3.KeyedReference;

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
    private static final String HOME_COMMUNITY_PREFIX = "urn:oid:";
    // Hash maps for the UDDI connection information.  This hash map is keyed by home community ID.
    //--------------------------------------------------------------------------------------------
    private HashMap<String, BusinessEntity> m_hUDDIConnectInfo = new HashMap<String, BusinessEntity>();       // Array of connection information
    private boolean m_bUDDILoaded = false;      // TRUE if the properties have been loaded
    private long m_lUDDIFileLastModified = 0;
    // Hash maps for the Internal connection information.  This hash map is keyed by home community ID.
    //--------------------------------------------------------------------------------------------------
    private HashMap<String, BusinessEntity> m_hInternalConnectInfo = new HashMap<String, BusinessEntity>();       // Array of connection information
    private boolean m_bInternalLoaded = false;      // TRUE if the properties have been loaded
    private long m_lInternalFileLastModified = 0;
    // Variables for managing the location of the XML files.
    //-------------------------------------------------------
    private static String INTERNAL_CONNECTION_API_LEVEL_KEY = "apiLevel";
    private static String UDDI_SPEC_VERSION_KEY = "uddi:nhin:versionofservice";
    private static String UDDI_STATE_KEY = "uddi:uddi.org:ubr:categorization:iso3166";
    private static String UDDI_HOME_COMMUNITY_ID_KEY = "uddi:nhin:nhie:homecommunityid";
    private static String UDD_SERVICE_NAMES_KEY = "uddi:nhin:standard-servicenames";
    private static ConnectionManagerCache connectionManager = null;

    protected ConnectionManagerCache() {
    }

    public static ConnectionManagerCache getInstance() {
        if (connectionManager == null) {
            connectionManager = new ConnectionManagerCache();
        }
        return connectionManager;
    }

    protected UddiConnectionInfoDAOFileImpl getUddiConnectionManagerDAO() {
        return UddiConnectionInfoDAOFileImpl.getInstance();
    }

    protected InternalConnectionInfoDAOFileImpl getInternalConnectionManagerDAO() {
        return InternalConnectionInfoDAOFileImpl.getInstance();
    }

    /**
     * This method is used to load the UDDI Connection Infomration form the
     * uddiConnectionInfo.xml file.
     */
    private void loadUDDIConnectionInfo()
            throws ConnectionManagerException {

        BusinessDetail businessDetail = null;
        try {
            businessDetail = getUddiConnectionManagerDAO().loadBusinessDetail();
        } catch (Exception ex) {
            Logger.getLogger(ConnectionManagerCache.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (businessDetail != null) {
            synchronized (m_hUDDIConnectInfo) {
                m_hUDDIConnectInfo.clear();

                if ((businessDetail.getBusinessEntity() != null) &&
                        (businessDetail.getBusinessEntity() != null) &&
                        (businessDetail.getBusinessEntity().size() > 0)) {
                    for (BusinessEntity oEntity : businessDetail.getBusinessEntity()) {
                        String sHomeCommunityId = getCommunityId(oEntity);
                        if ((sHomeCommunityId != null) && (sHomeCommunityId.length() > 0)) {
                            m_hUDDIConnectInfo.put(sHomeCommunityId, oEntity);
                        }
                    }   // for (CMBusinessEntity oEntity : oConnInfo.getBusinessEntities().getBusinessEntity())
                }   // if ((oConnInfo.getBusinessEntities() != null) && ...

                m_bUDDILoaded = true;
                m_lUDDIFileLastModified = getUddiConnectionManagerDAO().getLastModified();
            }   // synchronized (m_ohUDDIConnectInfo)
        } // if (oConnInfo != null)
        else {
            log.warn("No UDDI information was found");
        }
    }

    private BusinessEntity mergeBusinessEntityServices(BusinessEntity internalEntity,
            BusinessEntity uddiEntity)
            throws ConnectionManagerException {
        if (getCommunityId(internalEntity).equals(getCommunityId(uddiEntity))) {
            return internalEntity;
        } else {
            return uddiEntity;
        }
    }

    /**
     * This method simply checks to see if the cache is loaded.  If it is not, then
     * it is loaded as a byproduct of calling this method.
     *
     * @throws gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException
     */
    private void checkLoaded()
            throws ConnectionManagerException {
        if (!m_bInternalLoaded) {
            forceRefreshInternalConnectCache();
        }

        if (!m_bUDDILoaded) {
            forceRefreshUDDICache();
        }

        refreshIfExpired();
    }

    public String getCommunityId(BusinessEntity businessEntity) {
        KeyedReference ref = getCommunityIdKeyReference(businessEntity);
        if (ref != null) {
            return ref.getKeyValue();
        }
        return null;
    }

    public KeyedReference getCommunityIdKeyReference(BusinessEntity businessEntity) {
        if (businessEntity.getIdentifierBag() == null) {
            return null;
        }
        for (KeyedReference reference : businessEntity.getIdentifierBag().getKeyedReference()) {
            if (reference.getTModelKey().equals(UDDI_HOME_COMMUNITY_ID_KEY)) {
                return reference;
            }
        }
        return null;
    }

    public void setCommunityId(BusinessEntity businessEntity, String newId) {
        KeyedReference ref = getCommunityIdKeyReference(businessEntity);
        if (ref != null) {
            ref.setKeyValue(newId);
        }
        ref = new KeyedReference();
        ref.setKeyValue(newId);
        businessEntity.getIdentifierBag().getKeyedReference().add(ref);
    }

    private List<String> getStates(BusinessEntity businessEntity) {
        List<String> result = new ArrayList<String>();
        for (KeyedReference reference : businessEntity.getCategoryBag().getKeyedReference()) {
            String key = reference.getTModelKey();
            String value = reference.getKeyValue();
            if (UDDI_STATE_KEY.equals(key)) {
                result.add(value);
            }
        }
        if (result.size() <= 0) {
            result = null;
        }
        return result;
    }

    /**
     * This method is used to load the UDDI Connection Infomration form the
     * uddiConnectionInfo.xml file.
     */
    private void loadInternalConnectionInfo()
            throws ConnectionManagerException {

        BusinessDetail businessDetail = null;
        try {
            businessDetail = getInternalConnectionManagerDAO().loadBusinessDetail();
        } catch (Exception ex) {
            Logger.getLogger(ConnectionManagerCache.class.getName()).log(Level.SEVERE, null, ex);
        }


        if (businessDetail != null) {
            synchronized (m_hInternalConnectInfo) {
                m_hInternalConnectInfo.clear();

                if ((businessDetail.getBusinessEntity() != null) &&
                        (businessDetail.getBusinessEntity().size() > 0)) {
                    for (BusinessEntity businessEntity : businessDetail.getBusinessEntity()) {

                        String sHomeCommunityId = getCommunityId(businessEntity);
                        if ((sHomeCommunityId != null) && (sHomeCommunityId.length() > 0)) {
                            m_hInternalConnectInfo.put(sHomeCommunityId, businessEntity);
                        }
                    }   // for (CMInternalConnectionInfo oConnInfo : oConnInfos.getInternalConnectionInfo())
                }   // if ((oConnInfos.getInternalConnectionInfo() != null) &&...

                m_bInternalLoaded = true;
                m_lInternalFileLastModified = getInternalConnectionManagerDAO().getLastModified();

            }   // synchronized (m_hInternalConnectInfo)
        } // if (oConnInfos != null)
        else {
            log.warn("No UDDI information was found in");
        }
    }

    /**
     * This method will cause the ConnectionManagerCache to refresh the UDDI connection data
     * by replacing the cached UDDI information with the information in the uddiConnectionInfo.xml file.
     * @throws ConnectionManagerException
     */
    public void forceRefreshUDDICache()
            throws ConnectionManagerException {
        loadUDDIConnectionInfo();
    }

    /**
     * This method will cause the ConnectionManagerCache to refresh the internal connection data
     * by replacing the cached internal connection information with the information in
     * the internalConnectionInfo.xml file.
     * @throws ConnectionManagerException
     */
    public void forceRefreshInternalConnectCache()
            throws ConnectionManagerException {
        loadInternalConnectionInfo();
    }

    /**
     * This method checks to see if either cache has expired and forces a refresh if it has.
     *
     */
    private void refreshIfExpired()
            throws ConnectionManagerException {
        long lUDDILastModified = 0;
        long lInternalLastModified = 0;

        // Find out our refrhes timing from the properties file.
        //-------------------------------------------------------
        try {
            lUDDILastModified = getUddiConnectionManagerDAO().getLastModified();
            lInternalLastModified = getInternalConnectionManagerDAO().getLastModified();
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
     * @param businessEntity The connection information to be transformed.
     * @return The HomeCommunity information.
     */
    private BusinessEntity homeCommunityFromInternalConnectionInfo(BusinessEntity businessEntity) {
        return businessEntity;
    }

    /**
     * This method will return a list of all home commuinities that are known by the
     * connection manager.
     *
     * @return The list of all home communities known by the connection manager.
     * @throws ConnectionManagerException
     */
    public List<BusinessEntity> getAllCommunities()
            throws ConnectionManagerException {
        HashSet<String> hHomeCommunities = new HashSet<String>();
        ArrayList<BusinessEntity> oaHomeCommunities = new ArrayList<BusinessEntity>();

        checkLoaded();

        // First get the information from the internal connections.
        //---------------------------------------------------------
        Collection<BusinessEntity> businessEntities = m_hInternalConnectInfo.values();
        for (BusinessEntity businessEntity : businessEntities) {
            BusinessEntity oComm = homeCommunityFromInternalConnectionInfo(businessEntity);
            String homeComunityId = getCommunityId(businessEntity);
            if ((homeComunityId != null) && (homeComunityId.length() > 0)) {
                hHomeCommunities.add(homeComunityId);
                oaHomeCommunities.add(oComm);
            }
        }

        // Next get the information from the UDDI connections - Only add them if we have not
        // already gotten them from the internal settings.
        //-----------------------------------------------------------------------------------
        Collection<BusinessEntity> colEntity = m_hUDDIConnectInfo.values();
        for (BusinessEntity oEntity : colEntity) {
            String homeCommunityId = getCommunityId(oEntity);
            if ((homeCommunityId != null) &&
                    (homeCommunityId.length() > 0) &&
                    (!hHomeCommunities.contains(homeCommunityId))) // make sure it is not alrady in the list
            {
                hHomeCommunities.add(homeCommunityId);
                oaHomeCommunities.add(oEntity);
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
    private BusinessEntity extractBusinessEntity(List<BusinessEntity> oEntities, String sHomeCommunityId) {
        if ((oEntities == null) ||
                (oEntities == null) ||
                (oEntities.size() <= 0) ||
                (sHomeCommunityId == null) ||
                (sHomeCommunityId.length() <= 0)) {
            return null;
        }

        for (BusinessEntity oEntity : oEntities) {
            String homeCommunityId = getCommunityId(oEntity);
            if ((homeCommunityId != null) &&
                    (homeCommunityId.equals(sHomeCommunityId))) {
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
    private void replaceBusinessEntity(List<BusinessEntity> oEntities, BusinessEntity oEntity) {
        if ((oEntities == null) ||
                (oEntity == null)) {
            return;         // there is nothing to do...
        }

        int iCnt = oEntities.size();
        if (iCnt == 0) {
            oEntities.add(oEntity);
            return;
        }
        String homeCommunityId = getCommunityId(oEntity);
        boolean bReplaced = false;
        for (int i = 0; i < iCnt; i++) {
            BusinessEntity oLocalEntity = oEntities.get(i);
            String localHomeCommunityId = getCommunityId(oLocalEntity);
            if ((localHomeCommunityId != null) &&
                    (homeCommunityId != null) &&
                    (localHomeCommunityId.equals(homeCommunityId))) {
                oEntities.set(i, oEntity);
                bReplaced = true;
                break;          // We are done - get out of here.
            }
        }   // for (int i = 0; i < iCnt; i++)

        if (!bReplaced) {
            oEntities.add(oEntity);
        }
    }

    /**
     * This method will return a list of all business entities that are known by the
     * connection manager.
     *
     * @return The list of all business entities known by the connection manager.
     * @throws ConnectionManagerException
     */
    public List<BusinessEntity> getAllBusinessEntities()
            throws ConnectionManagerException {
        HashSet<String> hEntities = new HashSet<String>();
        List<BusinessEntity> oEntities = new ArrayList<BusinessEntity>();

        checkLoaded();

        // First get the information from the internal connections.
        //---------------------------------------------------------
        Collection<BusinessEntity> colInternConn = m_hInternalConnectInfo.values();
        for (BusinessEntity oEntity : colInternConn) {
            String homeComunityId = getCommunityId(oEntity);
            if ((homeComunityId != null) && (homeComunityId.length() > 0)) {
                hEntities.add(homeComunityId);
                oEntities.add(oEntity);
            }
        }

        // Next get the information from the UDDI connections -
        // If it is in the list, then merge the services.  If not, then
        // add it as is.
        //-----------------------------------------------------------------------------------
        Collection<BusinessEntity> colEntity = m_hUDDIConnectInfo.values();
        for (BusinessEntity oEntity : colEntity) {
            String homeCommunityId = getCommunityId(oEntity);
            if ((homeCommunityId != null) &&
                    (homeCommunityId.length() > 0)) {
                if (hEntities.contains(homeCommunityId)) {
                    BusinessEntity oExistingEntity = extractBusinessEntity(oEntities, homeCommunityId);
                    if (oExistingEntity != null) {
                        oExistingEntity = mergeBusinessEntityServices(oExistingEntity, oEntity);
                        replaceBusinessEntity(oEntities, oExistingEntity);
                    } else {
                        // We should never get here - but just in case...
                        oEntities.add(oEntity);
                    }
                } else {
                    hEntities.add(homeCommunityId);
                    oEntities.add(oEntity);
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
    public BusinessEntity getBusinessEntity(String sHomeCommunityId)
            throws ConnectionManagerException {
        BusinessEntity oReturnEntity = null;

        checkLoaded();

        BusinessEntity oInternalEntity = null;
        BusinessEntity oUDDIEntity = null;

        // First look in Internal connections...
        //--------------------------------------
        if (m_hInternalConnectInfo.containsKey(sHomeCommunityId)) {
            oInternalEntity = m_hInternalConnectInfo.get(sHomeCommunityId);
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
    public Set<BusinessEntity> getBusinessEntitySet(List<String> saHomeCommunityId)
            throws ConnectionManagerException {
        Set<BusinessEntity> oEntities = new HashSet<BusinessEntity>();

        checkLoaded();

        if ((saHomeCommunityId == null) || (saHomeCommunityId.size() <= 0)) {
            return null;
        }

        // We always first look in our internal list - then in the UDDI one...
        //---------------------------------------------------------------------
        for (String sHomeCommunityId : saHomeCommunityId) {
            BusinessEntity oEntity = getBusinessEntity(sHomeCommunityId);
            if (oEntity != null) {
                oEntities.add(oEntity);
            }
        }

        if (oEntities.size() > 0) {
            return oEntities;
        } else {
            return null;
        }
    }

    private BindingTemplate findMostCompatibleBindingTemplate(BusinessEntity businessEntity, String serviceName, GATEWAY_API_LEVEL apiLevel) {
        BindingTemplate bindingTemplate = null;
        if (businessEntity != null && 
                businessEntity.getBusinessServices() != null &&
                businessEntity.getBusinessKey() != null) {
            Map<String, BindingTemplate> templatesBySpecVersion = getSpecVersionToBindingTemplateMap(businessEntity, serviceName);
            // 1.0, bindintemplate
            // 2.0, bindingtemplate
            bindingTemplate = getHighestSpecVersionSupported(apiLevel, templatesBySpecVersion);
        }
        return bindingTemplate;
    }

    private Map<String, BindingTemplate> getSpecVersionToBindingTemplateMap(BusinessEntity businessEntity, String serviceName) {
        Map<String, BindingTemplate> specVersionToTemplateMap = new HashMap<String, BindingTemplate>();
        if (businessEntity == null) {
            return specVersionToTemplateMap;
        }

        List<String> specVersionList = null;
        for (BusinessService service : businessEntity.getBusinessServices().getBusinessService()) {
            if (!isServiceNameEquals(service, serviceName)) {
                continue;
            }
            if (service.getBindingTemplates() != null && service.getBindingTemplates().getBindingTemplate() != null) {
                for (BindingTemplate template : service.getBindingTemplates().getBindingTemplate()) {
                    specVersionList = getSpecVersions(template);
                    for (String specVersion : specVersionList) {
                        specVersionToTemplateMap.put(specVersion, template);
                    }
                }
            }
        }

        return specVersionToTemplateMap;
    }

    private boolean isServiceNameEquals(BusinessService service, String serviceName) {
        List<String> snameList = getServiceNames(service);
        for (String sname: snameList) {
            if (sname.equalsIgnoreCase(serviceName)) {
                return true;
            }
        }
        return false;
    }

    private List<String> getServiceNames(BusinessService service) {
        List<String> serviceNameList = new ArrayList<String>();

        if (service.getCategoryBag() != null && service.getCategoryBag().getKeyedReference() != null) {
            for (KeyedReference reference : service.getCategoryBag().getKeyedReference()) {
                String keyName = reference.getTModelKey();
                String keyValue = reference.getKeyValue();
                if (keyName.equals(UDD_SERVICE_NAMES_KEY)) {
                    serviceNameList.add(keyValue);
                }
            }
        }
        return serviceNameList;
    }

    private List<String> getSpecVersions(BindingTemplate bindingTemplate) {
        List<String> specVersionList = new ArrayList<String>();
        if (bindingTemplate.getCategoryBag() != null && bindingTemplate.getCategoryBag().getKeyedReference() != null) {
            for (KeyedReference reference : bindingTemplate.getCategoryBag().getKeyedReference()) {
                String keyName = reference.getTModelKey();
                String specVersionValue = reference.getKeyValue();
                if (keyName.equals(UDDI_SPEC_VERSION_KEY)) {
                    specVersionList.add(specVersionValue);
                }
            }
        }

        return specVersionList;
    }

    // if api level is g0  ==> spec 1
    // if api level is g1 ==> spec 1, spec 2
    private BindingTemplate getHighestSpecVersionSupported(GATEWAY_API_LEVEL apiLevel, Map<String, BindingTemplate> specVersionToTemplateMap) {
        ArrayList<UDDI_SPEC_VERSION> supportedSpecs = UddiSpecVersionRegistry.getInstance().getSupportedSpecs(apiLevel);
        UDDI_SPEC_VERSION highestSpec = null;
        for (UDDI_SPEC_VERSION supportedSpec : supportedSpecs) {
            if (specVersionToTemplateMap.containsKey(supportedSpec.toString())) {
                if (highestSpec == null || supportedSpec.ordinal() > highestSpec.ordinal()) {
                    highestSpec = supportedSpec;
                }
            }
        }

        if (highestSpec != null) {
            return specVersionToTemplateMap.get(highestSpec.toString());
        }

        return null;
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
    public BusinessEntity getBusinessEntityByServiceName(String sHomeCommunityId,
            String sUniformServiceName)
            throws ConnectionManagerException {

        // Reload remote and local if needed
        checkLoaded();

        // Validation
        if ((sHomeCommunityId == null) || (sHomeCommunityId.length() <= 0) ||
                (sUniformServiceName == null) || (sUniformServiceName.length() <= 0)) {
            return null;
        }

        BusinessEntity internalBusinessEntity = null;
        BusinessEntity oUDDIEntity = null;

        // load internal connections
        if (m_hInternalConnectInfo.containsKey(sHomeCommunityId)) {
            BusinessEntity businessEntity = m_hInternalConnectInfo.get(sHomeCommunityId);
            internalBusinessEntity = businessEntity;
        }

        // get UDDI from cache
        if (m_hUDDIConnectInfo.containsKey(sHomeCommunityId)) {
            oUDDIEntity = m_hUDDIConnectInfo.get(sHomeCommunityId);
        }

        // Merge local and remote
        BusinessEntity oCombinedEntity = null;
        if ((internalBusinessEntity != null) && (oUDDIEntity != null)) {
            oCombinedEntity = mergeBusinessEntityServices(internalBusinessEntity, oUDDIEntity);
        } else if (internalBusinessEntity != null) {
            oCombinedEntity = internalBusinessEntity;
        } else if (oUDDIEntity != null) {
            oCombinedEntity = oUDDIEntity;
        } else {
            return null;            // We found nothing...
        }

        // Now lets see if it has the service we are looking for.
        //--------------------------------------------------------
        if (findMostCompatibleBindingTemplate(oCombinedEntity, sUniformServiceName, getApiVersion(sHomeCommunityId, sUniformServiceName)) != null) {
            return oCombinedEntity;
        }
        return null;
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
    public String getEndpointURLByServiceName(String sHomeCommunityId,
            String sUniformServiceName)
            throws ConnectionManagerException {

        GATEWAY_API_LEVEL apiLevel = getApiVersion(sHomeCommunityId, sUniformServiceName);

        String sEndpointURL = "";
        BusinessEntity oEntity = getBusinessEntityByServiceName(sHomeCommunityId, sUniformServiceName);
        BindingTemplate bindingTemplate = findMostCompatibleBindingTemplate(oEntity, sUniformServiceName, apiLevel);
        if (bindingTemplate != null) {
            sEndpointURL = bindingTemplate.getAccessPoint().getValue();
        }

        if (log.isInfoEnabled()) {
            log.info("getEndpointURLByServiceName for home community (" + sHomeCommunityId + ") and service name (" + sUniformServiceName + ") returned endpoint address: " + sEndpointURL);
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
    public String getLocalEndpointURLByServiceName(String sUniformServiceName)
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
    public String getEndpontURLFromNhinTarget(NhinTargetSystemType targetSystem, String serviceName)
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
                String homeCommunityId = cleanHomeCommunityId(targetSystem.getHomeCommunity().getHomeCommunityId());
                log.debug("Attempting to look up URL by home communinity id: " + homeCommunityId + " and service name: " + serviceName);
                sEndpointURL = getEndpointURLByServiceName(homeCommunityId, serviceName);
            }
        }

        log.debug("Returning URL: " + sEndpointURL);
        return sEndpointURL;
    }

    /**
     * This method retrieves a set of unique URLs from the contents of the NhinTargetCommunities type.  For each
     * NhinTargetCommunity type it will first check if a Home Community Id is specified.  If so then it will add the
     * URL for the specified service for that home community to the List of URLs.  Next it will check if a region
     * (state) is specified.  If so it will obtain a list of URLs for that that service for all communities in the
     * specified state.  Next it will check if a list is specified (this feature is not implemented).  If there are
     * no NhinTargetCommunities specified it will return the list of URLs for the entire NHIN for that service.
     *
     * @param targets List of targets to get URLs for.
     * @param serviceName The name of the service to locate who URL is being requested.
     * @return The set of URLs for the requested service and targets.
     * @throws ConnectionManagerException
     */
    public List<UrlInfo> getEndpontURLFromNhinTargetCommunities(NhinTargetCommunitiesType targets, String serviceName)
            throws ConnectionManagerException {
        List<UrlInfo> endpointUrlList = new ArrayList<UrlInfo>();

        if (targets != null &&
                NullChecker.isNotNullish(targets.getNhinTargetCommunity())) {
            for (NhinTargetCommunityType target : targets.getNhinTargetCommunity()) {
                if (target.getHomeCommunity() != null &&
                        NullChecker.isNotNullish(target.getHomeCommunity().getHomeCommunityId())) {
                    log.info("Looking up URL by home community id");
                    String endpt = getEndpointURLByServiceName(target.getHomeCommunity().getHomeCommunityId(), serviceName);

                    if (NullChecker.isNotNullish(endpt)) {
                        UrlInfo entry = new UrlInfo();
                        entry.setHcid(target.getHomeCommunity().getHomeCommunityId());
                        entry.setUrl(endpt);
                        endpointUrlList.add(entry);
                    }
                }

                if (target.getRegion() != null) {
                    log.info("Looking up URL by region");
                    filterByRegion(endpointUrlList, target.getRegion(), serviceName);
                }

                if (target.getList() != null) {
                    log.info("Looking up URL by list");
                    log.warn("The List target feature has not been implemented yet");
                }
            }
        } else {
            // This is the broadcast scenario so retrieve the entire list of URLs for the specified service
            Set<BusinessEntity> entities = getAllBusinessEntitySetByServiceName(serviceName);

            if (entities != null) {
                endpointUrlList = getUrlInfoFromBusinessEntities(new ArrayList<BusinessEntity>(entities));
            }
        }

        if (endpointUrlList != null) {
            createUniqueList(endpointUrlList);
            printURLList(endpointUrlList);
        }

        return endpointUrlList;
    }

    /**
     * This method retrieves a set of  URLs for that that service for all communities in the
     * specified region or state.
     *
     * @param urlList List of URLs to add state URL information to
     * @param region Region or State name to filter on.
     * @param serviceName The name of the service to locate who URL is being requested.
     * @return void.
     * @throws ConnectionManagerException
     */
    private void filterByRegion(List<UrlInfo> urlList, String region, String serviceName)
            throws ConnectionManagerException {
        Set<BusinessEntity> entities = getAllBusinessEntitySetByServiceName(serviceName);

        if ((entities != null)) {
            for (BusinessEntity entity : entities) {
                if (getStates(entity) != null &&
                        NullChecker.isNotNullish(getStates(entity))) {
                    for (String state : getStates(entity)) {
                        if (state.equalsIgnoreCase(region)) {
                            String url = getUrl(entity);
                            String hcid = getHcid(entity);

                            if (NullChecker.isNotNullish(url) &&
                                    NullChecker.isNotNullish(hcid)) {
                                UrlInfo entry = new UrlInfo();
                                entry.setHcid(hcid);
                                entry.setUrl(url);
                                urlList.add(entry);
                            }
                        }
                    }
                }
            }
        }

        return;
    }

    private List<UrlInfo> getUrlInfoFromBusinessEntities(List<BusinessEntity> businessEntityList) {
        List<UrlInfo> urlList = new ArrayList<UrlInfo>();

        if (NullChecker.isNotNullish(businessEntityList)) {
            for (BusinessEntity entity : businessEntityList) {
                String url = getUrl(entity);
                String hcid = getHcid(entity);

                if (NullChecker.isNotNullish(url) &&
                        NullChecker.isNotNullish(hcid)) {
                    UrlInfo entry = new UrlInfo();
                    entry.setUrl(url);
                    entry.setHcid(hcid);
                    urlList.add(entry);
                }
            }
        }

        return urlList;
    }

    private String getUrl(BusinessEntity entity) {
        if (entity != null &&
                (entity.getBusinessServices().getBusinessService() != null) &&
                (entity.getBusinessServices().getBusinessService().size() > 0) &&
                (entity.getBusinessServices().getBusinessService().get(0) != null) &&
                (entity.getBusinessServices().getBusinessService().get(0).getBindingTemplates() != null) &&
                (entity.getBusinessServices().getBusinessService().get(0).getBindingTemplates().getBindingTemplate() != null) &&
                (entity.getBusinessServices().getBusinessService().get(0).getBindingTemplates().getBindingTemplate().size() > 0) &&
                (entity.getBusinessServices().getBusinessService().get(0).getBindingTemplates().getBindingTemplate().get(0) != null) &&
                (entity.getBusinessServices().getBusinessService().get(0).getBindingTemplates().getBindingTemplate().get(0).getAccessPoint().getValue() != null) &&
                (entity.getBusinessServices().getBusinessService().get(0).getBindingTemplates().getBindingTemplate().get(0).getAccessPoint().getValue().trim().length() > 0)) {
            return entity.getBusinessServices().getBusinessService().get(0).getBindingTemplates().getBindingTemplate().get(0).getAccessPoint().getValue().trim();
        }

        return null;
    }

    private String getHcid(BusinessEntity entity) {
        String homeCommunityId = getCommunityId(entity);
        if (entity != null &&
                NullChecker.isNotNullish(homeCommunityId)) {
            return homeCommunityId.trim();
        }
        return null;
    }

    /**
     * This method will remove duplicate URLs from a URL list.
     *
     * @param urlList List of URLs to remove duplicates from.
     * @return The set of unique URLs.
     */
    private void createUniqueList(List<UrlInfo> urlList) {
        List<UrlInfo> tempList = new ArrayList<UrlInfo>();
        boolean foundDup = false;

        // Find the duplicates
        for (UrlInfo entry : urlList) {
            foundDup = false;
            for (UrlInfo temp : tempList) {
                if (temp.equals(entry)) {
                    foundDup = true;
                    break;
                }
            }
            if (foundDup == false) {
                tempList.add(entry);
            }
        }

        // Remove the duplicates
        urlList.clear();
        for (UrlInfo temp : tempList) {
            urlList.add(temp);
        }

        return;
    }

    /**
     * This method will print out the contents of a URL list.
     *
     * @param urlList List of URLs.
     * @return void.
     */
    private void printURLList(List<UrlInfo> urlList) {
        int idx = 0;

        if (urlList != null) {
            log.debug("Connection Management URL Info List:");
            for (UrlInfo url : urlList) {
                log.debug("   HCID: " + url.getHcid() + " URL #" + idx + ": " + url.getUrl());
                idx++;
            }
        } else {
            log.debug("Url List was Empty");
        }
    }

    private String cleanHomeCommunityId(String homeCommunityId) {
        String cleaned = homeCommunityId;
        if ((homeCommunityId != null) && (homeCommunityId.startsWith(HOME_COMMUNITY_PREFIX))) {
            cleaned = homeCommunityId.substring(HOME_COMMUNITY_PREFIX.length());
        }
        return cleaned;
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
    public Set<BusinessEntity> getBusinessEntitySetByServiceName(List<String> saHomeCommunityId, String sUniformServiceName)
            throws ConnectionManagerException {
        Set<BusinessEntity> oEntities = new HashSet<BusinessEntity>();

        checkLoaded();

        if ((saHomeCommunityId == null) || (saHomeCommunityId.size() <= 0) ||
                (sUniformServiceName == null) || (sUniformServiceName.length() <= 0)) {
            return null;
        }

        for (String sHomeCommunityId : saHomeCommunityId) {
            BusinessEntity oEntity = getBusinessEntityByServiceName(sHomeCommunityId, sUniformServiceName);
            if (oEntity != null) {
                oEntities.add(oEntity);
            }
        }

        if (oEntities.size() > 0) {
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
    public Set<BusinessEntity> getAllBusinessEntitySetByServiceName(String sUniformServiceName)
            throws ConnectionManagerException {
        Set<BusinessEntity> oEntities = new HashSet<BusinessEntity>();

        checkLoaded();

        HashSet<String> hKeys = new HashSet<String>();

        // This is a slick way to add them all and remove any duplicates...
        //------------------------------------------------------------------
        hKeys.addAll(m_hInternalConnectInfo.keySet());
        hKeys.addAll(m_hUDDIConnectInfo.keySet());

        ArrayList<String> saHomeCommunityIds = new ArrayList<String>(hKeys);
        oEntities = getBusinessEntitySetByServiceName(saHomeCommunityIds, sUniformServiceName);

        if ((oEntities != null) && (oEntities.size() > 0)) {
            return oEntities;
        } else {
            return null;
        }
    }

    public GATEWAY_API_LEVEL getApiVersion(String homeCommunityId, String serviceName) {
        GATEWAY_API_LEVEL result = null;
        try {
            Set<String> specVersions = getSpecVersions(homeCommunityId, serviceName);            
            result = getHighestGatewayApiLevelSupportedBySpec(specVersions);
        } catch (Exception ex) {
            Logger.getLogger(ConnectionManagerCache.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (result == null) {
            result = GATEWAY_API_LEVEL.LEVEL_g1;
        }
        return result;
    }

    private Set<String> getSpecVersions(String homeCommunityId, String serviceName) {
        Set<String> specVersions = new HashSet<String>();

        try {
            BusinessEntity businessEntity = getBusinessEntity(homeCommunityId);
            Map<String, BindingTemplate> templatesBySpecVersion = getSpecVersionToBindingTemplateMap(businessEntity, serviceName);
            specVersions = templatesBySpecVersion.keySet();

        } catch (Exception ex) {
            Logger.getLogger(ConnectionManagerCache.class.getName()).log(Level.SEVERE, null, ex);
        }

        return specVersions;
    }

    private GATEWAY_API_LEVEL getHighestGatewayApiLevelSupportedBySpec(Set<String> specVersions) {
        GATEWAY_API_LEVEL highestApiLevel = null;
        GATEWAY_API_LEVEL apiLevel = null;
        UddiSpecVersionRegistry specRegistry = UddiSpecVersionRegistry.getInstance();

        try {
            for (String specVersion : specVersions) {
                apiLevel = specRegistry.getSupportedGatewayAPI(UDDI_SPEC_VERSION.fromString(specVersion));
                if (highestApiLevel == null || apiLevel.ordinal() > highestApiLevel.ordinal()) {
                    highestApiLevel = apiLevel;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ConnectionManagerCache.class.getName()).log(Level.SEVERE, null, ex);
        }

        return highestApiLevel;
    }

    public String getAdapterEndpontURL(String sServiceName, ADAPTER_API_LEVEL level) throws ConnectionManagerException {

        String endpointUrl = null;
        String sHomeCommunityId = null;
        try {
            sHomeCommunityId = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.HOME_COMMUNITY_ID_PROPERTY + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE, ex);
        }
        BusinessEntity oEntity = getBusinessEntityByServiceName(sHomeCommunityId, sServiceName);
        BindingTemplate template = findBindingTemplateByCategoryBagNameValue(oEntity, sServiceName, INTERNAL_CONNECTION_API_LEVEL_KEY, level.toString());
        if (template != null) {
            endpointUrl = template.getAccessPoint().getValue();
        }
        return endpointUrl;
    }

    private BindingTemplate findBindingTemplateByCategoryBagNameValue(BusinessEntity businessEntity, String serviceName, String key, String value) {
        BindingTemplate bindingTemplate = null;
        if (businessEntity != null && businessEntity.getBusinessServices() != null && businessEntity.getBusinessKey() != null) {
            for (BusinessService service : businessEntity.getBusinessServices().getBusinessService()) {
                if (!isServiceNameEquals(service, serviceName)) {
                    continue;
                }
                bindingTemplate = findBindingTemplateByKey(service, key, value);
                if (bindingTemplate != null) {
                    return bindingTemplate;
                }
            }
        }
        return null; 
    }

    private BindingTemplate findBindingTemplateByKey(BusinessService service, String keyRefName, String keyRefValue) {
        BindingTemplate bindingTemplate = null;
        if (service.getBindingTemplates() != null && service.getBindingTemplates().getBindingTemplate() != null) {
            for (BindingTemplate template : service.getBindingTemplates().getBindingTemplate()) {
                if (template.getCategoryBag() != null && template.getCategoryBag().getKeyedReference() != null) {
                    for (KeyedReference reference : template.getCategoryBag().getKeyedReference()) {
                        String keyName = reference.getTModelKey();
                        String keyValue = reference.getKeyValue();
                        if (keyRefName.equals(keyName) && keyRefValue.equals(keyValue)) {
                            return template;
                        }
                    }
                }
            }
        }

        return bindingTemplate;
    }


}
