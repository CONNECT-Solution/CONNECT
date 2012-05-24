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
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.UDDI_SPEC_VERSION;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
 * This class is used to manage the Connection Manager's cache. It handles both internal connection settings and UDDI
 * connection settings. If there is a collision for a connection between the UDDI and the Internal settings, the
 * internal one will be used.
 * 
 * @author Les Westberg
 */
public class ConnectionManagerCache {

    private static Log log = LogFactory.getLog(ConnectionManagerCache.class);
    private static String UDDI_SPEC_VERSION_KEY = "uddi:nhin:versionofservice";
    private static final String HOME_COMMUNITY_PREFIX = "urn:oid:";
    // Hash maps for the UDDI connection information. This hash map is keyed by home community ID.
    // --------------------------------------------------------------------------------------------
    private HashMap<String, BusinessEntity> m_hUDDIConnectInfo = new HashMap<String, BusinessEntity>(); // Array of
                                                                                                        // connection
                                                                                                        // information
    private boolean m_bUDDILoaded = false; // TRUE if the properties have been loaded
    private long m_lUDDIFileLastModified = 0;
    // Hash maps for the Internal connection information. This hash map is keyed by home community ID.
    // --------------------------------------------------------------------------------------------------
    private HashMap<String, BusinessEntity> m_hInternalConnectInfo = new HashMap<String, BusinessEntity>(); // Array of
                                                                                                            // connection
                                                                                                            // information
    private boolean m_bInternalLoaded = false; // TRUE if the properties have been loaded
    private long m_lInternalFileLastModified = 0;
    // Variables for managing the location of the XML files.
    // -------------------------------------------------------
    private static String INTERNAL_CONNECTION_API_LEVEL_KEY = "CONNECT:adapter:apilevel";
   
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
     * This method is used to load the UDDI Connection Infomration form the uddiConnectionInfo.xml file.
     */
    private void loadUDDIConnectionInfo() throws ConnectionManagerException {

        BusinessDetail businessDetail = null;
        try {
            businessDetail = getUddiConnectionManagerDAO().loadBusinessDetail();
        } catch (Exception ex) {
            Logger.getLogger(ConnectionManagerCache.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (businessDetail != null) {
            synchronized (m_hUDDIConnectInfo) {
                m_hUDDIConnectInfo.clear();

                if ((businessDetail.getBusinessEntity() != null) && (businessDetail.getBusinessEntity() != null)
                        && (businessDetail.getBusinessEntity().size() > 0)) {
                    for (BusinessEntity oEntity : businessDetail.getBusinessEntity()) {
                    	ConnectionManagerCacheHelper helper = new ConnectionManagerCacheHelper();
                        String sHomeCommunityId = helper.getCommunityId(oEntity);
                        if ((sHomeCommunityId != null) && (sHomeCommunityId.length() > 0)) {
                            m_hUDDIConnectInfo.put(sHomeCommunityId, oEntity);
                        }
                    } // for (CMBusinessEntity oEntity : oConnInfo.getBusinessEntities().getBusinessEntity())
                } // if ((oConnInfo.getBusinessEntities() != null) && ...

                m_bUDDILoaded = true;
                m_lUDDIFileLastModified = getUddiConnectionManagerDAO().getLastModified();
            } // synchronized (m_ohUDDIConnectInfo)
        } // if (oConnInfo != null)
        else {
            log.warn("No UDDI information was found");
        }
    }

    /**
     * This method simply checks to see if the cache is loaded. If it is not, then it is loaded as a byproduct of
     * calling this method.
     * 
     * @throws gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException
     */
    private void checkLoaded() throws ConnectionManagerException {
        if (!m_bInternalLoaded) {
            forceRefreshInternalConnectCache();
        }

        if (!m_bUDDILoaded) {
            forceRefreshUDDICache();
        }

        refreshIfExpired();
    }

    public void setCommunityId(BusinessEntity businessEntity, String newId) {
    	ConnectionManagerCacheHelper helper = new ConnectionManagerCacheHelper();
        KeyedReference ref = helper.getCommunityIdKeyReference(businessEntity);
        if (ref != null) {
            ref.setKeyValue(newId);
        }
        ref = new KeyedReference();
        ref.setKeyValue(newId);
        businessEntity.getIdentifierBag().getKeyedReference().add(ref);
    }


    /**
     * This method is used to load the UDDI Connection Information form the uddiConnectionInfo.xml file.
     */
    private void loadInternalConnectionInfo() throws ConnectionManagerException {

        BusinessDetail businessDetail = null;
        try {
            businessDetail = getInternalConnectionManagerDAO().loadBusinessDetail();
        } catch (Exception ex) {
            Logger.getLogger(ConnectionManagerCache.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (businessDetail != null) {
            synchronized (m_hInternalConnectInfo) {
                m_hInternalConnectInfo.clear();

                if ((businessDetail.getBusinessEntity() != null) && (businessDetail.getBusinessEntity().size() > 0)) {
                    for (BusinessEntity businessEntity : businessDetail.getBusinessEntity()) {
                    	ConnectionManagerCacheHelper helper = new ConnectionManagerCacheHelper();
                        String sHomeCommunityId = helper.getCommunityId(businessEntity);
                        if ((sHomeCommunityId != null) && (sHomeCommunityId.length() > 0)) {
                            m_hInternalConnectInfo.put(sHomeCommunityId, businessEntity);
                        }
                    } // for (CMInternalConnectionInfo oConnInfo : oConnInfos.getInternalConnectionInfo())
                } // if ((oConnInfos.getInternalConnectionInfo() != null) &&...

                m_bInternalLoaded = true;
                m_lInternalFileLastModified = getInternalConnectionManagerDAO().getLastModified();

            } // synchronized (m_hInternalConnectInfo)
        } // if (oConnInfos != null)
        else {
            log.warn("No UDDI information was found in");
        }
    }

    /**
     * This method will cause the ConnectionManagerCache to refresh the UDDI connection data by replacing the cached
     * UDDI information with the information in the uddiConnectionInfo.xml file.
     * 
     * @throws ConnectionManagerException
     */
    public void forceRefreshUDDICache() throws ConnectionManagerException {
        loadUDDIConnectionInfo();
    }

    /**
     * This method will cause the ConnectionManagerCache to refresh the internal connection data by replacing the cached
     * internal connection information with the information in the internalConnectionInfo.xml file.
     * 
     * @throws ConnectionManagerException
     */
    public void forceRefreshInternalConnectCache() throws ConnectionManagerException {
        loadInternalConnectionInfo();
    }

    /**
     * This method checks to see if either cache has expired and forces a refresh if it has.
     * 
     */
    private void refreshIfExpired() throws ConnectionManagerException {
        long lUDDILastModified = 0;
        long lInternalLastModified = 0;

        // Find out our refresh timing from the properties file.
        // -------------------------------------------------------
        try {
            lUDDILastModified = getUddiConnectionManagerDAO().getLastModified();
            lInternalLastModified = getInternalConnectionManagerDAO().getLastModified();
        } catch (Exception e) {
            // Assume a refresh is required... But log a message.
            // ----------------------------------------------------
            String sErrorMessage = "Failed to retrieve last modified dates on the connection manager XML files."
                    + "Error: " + e.getMessage();
            log.warn(sErrorMessage, e);
        }

        // If we need to refresh the UDDI cache information.
        // --------------------------------------------------
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

    /**
     * This method will return a list of all business entities that are known by the connection manager.
     * 
     * @return The list of all business entities known by the connection manager.
     * @throws ConnectionManagerException
     */
    public List<BusinessEntity> getAllBusinessEntities() throws ConnectionManagerException {
        List<BusinessEntity> allEntities = new ArrayList<BusinessEntity>();
        ConnectionManagerCacheHelper helper = new ConnectionManagerCacheHelper();
        checkLoaded();

        // First get the information from the internal connections.
        // ---------------------------------------------------------
        for (BusinessEntity internalEntity : m_hInternalConnectInfo.values()) {
            if (NullChecker.isNotNullish(helper.getCommunityId(internalEntity))) {
                allEntities.add(internalEntity);
            }
        }

        // Next get the information from the UDDI connections -
        // If it is in the list, then merge the services. If not, then
        // add it as is.
        // -----------------------------------------------------------------------------------
        for (BusinessEntity uddiEntity : m_hUDDIConnectInfo.values()) {
            String homeCommunityId = helper.getCommunityId(uddiEntity);
            if (NullChecker.isNotNullish(homeCommunityId)) {
                BusinessEntity oExistingEntity = helper.extractBusinessEntity(allEntities, homeCommunityId);
                if (oExistingEntity != null) {
                	helper.mergeBusinessEntityServices(oExistingEntity, uddiEntity);
                	helper.replaceBusinessEntity(allEntities, oExistingEntity);
                } else {
                    allEntities.add(uddiEntity);
                }
            }
        }
        return allEntities;

    }

    /**
     * This class returns the business entity information associated with the specified home community ID.
     * 
     * @param sHomeCommunityId The home commuinity ID that is being searched for.
     * @return the business entity information for the specified home community.
     * @throws gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException
     */
    public BusinessEntity getBusinessEntity(String sHomeCommunityId) throws ConnectionManagerException {
        ConnectionManagerCacheHelper helper = new ConnectionManagerCacheHelper();
        checkLoaded();

        BusinessEntity oInternalEntity = null;
        BusinessEntity oUDDIEntity = null;

        // First look in Internal connections...
        // --------------------------------------
        if (m_hInternalConnectInfo.containsKey(sHomeCommunityId)) {
            oInternalEntity = m_hInternalConnectInfo.get(sHomeCommunityId);
        }

        // Look for a UDDI one
        // --------------------
        if (m_hUDDIConnectInfo.containsKey(sHomeCommunityId)) {
            oUDDIEntity = m_hUDDIConnectInfo.get(sHomeCommunityId);
        }

        if ((oInternalEntity != null) && (oUDDIEntity != null)) {
        	helper.mergeBusinessEntityServices(oInternalEntity, oUDDIEntity);
        } else if (oUDDIEntity != null) {
            return oUDDIEntity;
        }
        return oInternalEntity;
    }

    /**
     * This method returns the business entity information for the set of home communities.
     * 
     * @param saHomeCommunityId The set of home communities to be retrieved.
     * @return The business entities found.
     * @throws gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException
     */
    public Set<BusinessEntity> getBusinessEntitySet(List<String> saHomeCommunityId) throws ConnectionManagerException {
        Set<BusinessEntity> oEntities = new HashSet<BusinessEntity>();

        checkLoaded();

        if ((saHomeCommunityId == null) || (saHomeCommunityId.size() <= 0)) {
            return null;
        }

        // We always first look in our internal list - then in the UDDI one...
        // ---------------------------------------------------------------------
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

    /**
     * This method retrieves the business entity that containts the specific home community and service name. Also note:
     * This currently does not deal with version. If there are multiple versions of the same serviec, this will return
     * the first one it sees in the list of services. As always, it will always first look in the InternalConnectionInfo
     * cache for the business entity. If it finds the business entity there, it will not look in the UDDI cache. (This
     * means that if the internal cache contains the given business entity, but it does not contain the requested
     * service, it will behave as if the service does not exist - regardless of whether it is in the UDDI cache or not.
     * 
     * @param sHomeCommunityId The home community ID of the gateway that is being looked up.
     * @param sUniformServiceName The name of the service to locate.
     * @return The Business Entity information along with only the requested service. if the service is not found, then
     *         null is returned.
     * @throws ConnectionManagerException
     */
     public BusinessEntity getBusinessEntityByServiceName(String sHomeCommunityId, String sUniformServiceName)
            throws ConnectionManagerException {
    	 ConnectionManagerCacheHelper helper = new ConnectionManagerCacheHelper();
        // Reload remote and local if needed
        checkLoaded();

        // Validation
        if (NullChecker.isNullish(sHomeCommunityId) || NullChecker.isNullish(sUniformServiceName)) {
            return null;
        }

        BusinessEntity internalBusinessEntity = null;
        BusinessEntity uddiEntity = null;
        BusinessService bService = null;

        // check the internal connections for the service
        if (m_hInternalConnectInfo.containsKey(sHomeCommunityId)) {
            internalBusinessEntity = m_hInternalConnectInfo.get(sHomeCommunityId);
            
            bService = helper.getBusinessServiceByServiceName(internalBusinessEntity, sUniformServiceName);
            if (bService == null) {
                internalBusinessEntity = null;
            }              
        }

        // check the uddi connections for the service
        if (m_hUDDIConnectInfo.containsKey(sHomeCommunityId)) {
            uddiEntity = m_hUDDIConnectInfo.get(sHomeCommunityId);
        
            bService = helper.getBusinessServiceByServiceName(uddiEntity, sUniformServiceName);
            if (bService == null) {
                uddiEntity = null;
            }        
        }

        // Merge local and remote
        BusinessEntity oCombinedEntity = null;
        if ((internalBusinessEntity != null) && (uddiEntity != null)) {
        	helper.mergeBusinessEntityServices(internalBusinessEntity, uddiEntity);
            oCombinedEntity = internalBusinessEntity;
        } else if (internalBusinessEntity != null) {
            oCombinedEntity = internalBusinessEntity;
        } else if (uddiEntity != null) {
            oCombinedEntity = uddiEntity;
        } else {
            return null; // We found nothing...
        }

        return oCombinedEntity;

    }
         
     public BusinessEntity getBusinessEntityByHCID(String sHomeCommunityId)
                 throws ConnectionManagerException {
    	 ConnectionManagerCacheHelper helper = new ConnectionManagerCacheHelper();
         // Reload remote and local if needed
         checkLoaded();

         // Validation
         if ((sHomeCommunityId == null) || (sHomeCommunityId.length() <= 0)) {
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
             oCombinedEntity = helper.mergeBusinessEntityServices(internalBusinessEntity, oUDDIEntity);
         } else if (internalBusinessEntity != null) {
             oCombinedEntity = internalBusinessEntity;
         } else if (oUDDIEntity != null) {
             oCombinedEntity = oUDDIEntity;
         } else {
             return null; // We found nothing...
         }

         return oCombinedEntity;
     }

    /**
     * This method retrieves a set of URLs for that that service for all communities in the specified region or state.
     * 
     * @param urlSet A set of unique URLs to add state URL information to
     * @param region Region or State name to filter on.
     * @param serviceName The name of the service to locate who URL is being requested.
     * @return void.
     * @throws ConnectionManagerException
     */
    private void filterByRegion(Set<UrlInfo> urlSet, String region, String serviceName)
            throws ConnectionManagerException {
    	ConnectionManagerCacheHelper helper = new ConnectionManagerCacheHelper();
        Set<BusinessEntity> entities = getAllBusinessEntitySetByServiceName(serviceName);

        if ((entities != null)) {
            for (BusinessEntity entity : entities) {
                if (helper.getStates(entity) != null && NullChecker.isNotNullish(helper.getStates(entity))) {
                    for (String state : helper.getStates(entity)) {
                        if (state.equalsIgnoreCase(region)) {
                            String hcid = helper.getCommunityId(entity);
                            String url = getDefaultEndpointURLByServiceName(hcid, serviceName);
                            if (NullChecker.isNotNullish(url) && NullChecker.isNotNullish(hcid)) {
                                UrlInfo entry = new UrlInfo();
                                entry.setHcid(hcid);
                                entry.setUrl(url);
                                urlSet.add(entry);
                            }
                        }
                    }
                }
            }
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
     * This method retrieves a set of business entity that containts the set of home communities and service name. Also
     * note: This currently does not deal with version. If there are multiple versions of the same service, this will
     * return the first one it sees in the list of services. As always, it will always first look in the
     * InternalConnectionInfo cache for the business entity. If it finds the business entity there, it will not look in
     * the UDDI cache. (This means that if the internal cache contains the given business entity, but it does not
     * contain the requested service, it will behave as if the service does not exist - regardless of whether it is in
     * the UDDI cache or not.
     * 
     * @param saHomeCommunityId The home community IDs of the gateways that is being looked up.
     * @param sUniformServiceName The name of the service to locate.
     * @return The Business Entity information along with only the requested service. If the service is not found, it
     *         will not be returned even if the business entity information exists.
     * @throws ConnectionManagerException
     */
    public Set<BusinessEntity> getBusinessEntitySetByServiceName(List<String> saHomeCommunityId,
            String sUniformServiceName) throws ConnectionManagerException {
        Set<BusinessEntity> oEntities = new HashSet<BusinessEntity>();

        checkLoaded();

        if (NullChecker.isNullish(saHomeCommunityId) || NullChecker.isNullish(sUniformServiceName)) {
            return null;
        }

        for (String sHomeCommunityId : saHomeCommunityId) {
            BusinessEntity oEntity = getBusinessEntityByServiceName(sHomeCommunityId, sUniformServiceName);
            if (oEntity != null) {
                oEntities.add(oEntity);
            }
        }
        return (oEntities.size() > 0) ? oEntities : null;
    }

    /**
     * This method retrieves the business entity information and service information for the set of home communities
     * that contains a service by that service name. Note: This will only return the information for the specified
     * service. It will not return all services. Also note: This currently does not deal with version. If there are
     * multiple versions of the same service, this will return the first one it sees in the list of services. As always,
     * it will always first look in the InternalConnectionInfo cache for the business entity. If it finds the business
     * entity there, it will not look in the UDDI cache. (This means that if the internal cache contains the given
     * business entity, but it does not contain the requested service, it will behave as if the service does not exist -
     * regardless of whether it is in the UDDI cache or not.
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
        // ------------------------------------------------------------------
        hKeys.addAll(m_hInternalConnectInfo.keySet());
        hKeys.addAll(m_hUDDIConnectInfo.keySet());

        ArrayList<String> saHomeCommunityIds = new ArrayList<String>(hKeys);
        oEntities = getBusinessEntitySetByServiceName(saHomeCommunityIds, sUniformServiceName);

        return ((oEntities != null) && (oEntities.size() > 0)) ? oEntities : null;
    }

    public List<UDDI_SPEC_VERSION> getSpecVersions(String homeCommunityId, NhincConstants.NHIN_SERVICE_NAMES serviceName) {
    	ConnectionManagerCacheHelper helper = new ConnectionManagerCacheHelper();
    	List<UDDI_SPEC_VERSION> specVersions = new ArrayList<UDDI_SPEC_VERSION>();

        try {
            BusinessEntity businessEntity = getBusinessEntity(homeCommunityId);
            specVersions = helper.getSpecVersionsFromBusinessEntity(businessEntity,
                    serviceName);
            
        } catch (Exception ex) {
            Logger.getLogger(ConnectionManagerCache.class.getName()).log(Level.SEVERE, null, ex);
        }

        return specVersions;
    }

    public String getAdapterEndpointURL(String sHomeCommunityId, String sServiceName, ADAPTER_API_LEVEL level)
            throws ConnectionManagerException {
        ConnectionManagerCacheHelper helper = new ConnectionManagerCacheHelper();
        String endpointUrl = null;

        BusinessEntity oEntity = getBusinessEntityByServiceName(sHomeCommunityId, sServiceName);
        BindingTemplate template = helper.findBindingTemplateByCategoryBagNameValue(oEntity, sServiceName,
                INTERNAL_CONNECTION_API_LEVEL_KEY, level.toString());
        if (template != null) {
            endpointUrl = template.getAccessPoint().getValue();
        }
        
        return endpointUrl;
    }
    
    public String getAdapterEndpointURL(String sServiceName, ADAPTER_API_LEVEL level) throws ConnectionManagerException {
        String sHomeCommunityId = null;
        try {
            sHomeCommunityId = PropertyAccessor.getInstance().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                    NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.HOME_COMMUNITY_ID_PROPERTY
                    + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE, ex);
        }
        
        return getAdapterEndpointURL(sHomeCommunityId, sServiceName, level);
    }
    
    /**
     * This method returns url for a specified service and home community id .
     * 
     * @param sHomeCommunityId The home community ID of the gateway that is being looked up.
     * @param sUniformServiceName The name of the service to locate.
     * @return The URL for only the requested service at the specified home community. If the service is not found, then
     *         null is returned.
     * @throws ConnectionManagerException
     */
    public String getDefaultEndpointURLByServiceName(String sHomeCommunityId, String sUniformServiceName)
            throws ConnectionManagerException {
    	ConnectionManagerCacheHelper helper = new ConnectionManagerCacheHelper();
        log.debug("begin getEndpointURLByServiceName: " + sHomeCommunityId + " / " + sUniformServiceName);

        String sEndpointURL = "";
        BusinessEntity oEntity = getBusinessEntityByHCID(sHomeCommunityId);
        if (oEntity == null) {
        	return sEndpointURL;
        }
        BusinessService oService = helper.getBusinessServiceByServiceName(oEntity, sUniformServiceName);
        if (oService == null) {
        	return sEndpointURL;
        }
        List<UDDI_SPEC_VERSION> specVersions = helper.getSpecVersions(oService);
        if (specVersions == null) { 
        	return sEndpointURL;
        }
        UDDI_SPEC_VERSION highestSpec = helper.getHighestUDDISpecVersion(specVersions);
        if (highestSpec == null) {
        	return sEndpointURL;
        }
        
        if (log.isInfoEnabled()) {
            log.info("Attempting to find binding template with spec version (" + highestSpec.toString() + ").");
        }
        
        BindingTemplate bindingTemplate = helper.findBindingTemplateByKey(oService, UDDI_SPEC_VERSION_KEY, highestSpec.toString());
        // we have no info on which binding template/endpoint "version" to use so just take the first.
        if (bindingTemplate == null || bindingTemplate.getAccessPoint() == null)
        {
        	log.error("No binding templates found for home community: " + sHomeCommunityId + " and service name: " + sUniformServiceName);
        	return sEndpointURL;
        }
        
        sEndpointURL = bindingTemplate.getAccessPoint().getValue();

        if (log.isInfoEnabled()) {
            log.info("getEndpointURLByServiceName for home community (" + sHomeCommunityId + ") and service name ("
                    + sUniformServiceName + ") returned endpoint address: " + sEndpointURL);
        }
        log.debug("end getEndpointURLByServiceName url = " + sEndpointURL);
        return sEndpointURL;
    }

    /**
     * This method returns a local url for a specified service.
     * 
     * @param sUniformServiceName The name of the service to locate.
     * @return The URL for only the requested service at the local home community. If the service is not found, then
     *         null is returned.
     * @throws ConnectionManagerException
     */
    public String getInternalEndpointURLByServiceName(String sUniformServiceName) throws ConnectionManagerException {
        String sHomeCommunityId = null;
        String sEndpointURL = null;

        try {
            log.info("Attempting to retrieve property: " + NhincConstants.HOME_COMMUNITY_ID_PROPERTY
                    + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            sHomeCommunityId = PropertyAccessor.getInstance().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                    NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
            log.info("Retrieve local home community id: " + sHomeCommunityId);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.HOME_COMMUNITY_ID_PROPERTY
                    + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }

        if (NullChecker.isNotNullish(sHomeCommunityId)) {
            sEndpointURL = getDefaultEndpointURLByServiceName(sHomeCommunityId, sUniformServiceName);
        }

        return sEndpointURL;
    }

    /**
     * This method retrieves the URL from the contents of the NhinTargetSystem type. It will first check to see if the
     * EPR (Endpoint Reference) is already provided, if so it will extract the URL from the EPR and return it to the
     * caller. If the EPR is not provided it will check if the URL field is provided, if so it will return the URL to
     * the caller. If neither an EPR or URL are provided this method will use the home community id and service name
     * provided to lookup the URL for that service at that particular home community.
     * 
     * @param targetSystem The target system information for the community being looked up.
     * @param serviceName The name of the service to locate who URL is being requested.
     * @return The URL to the requested service.
     * @throws ConnectionManagerException
     */
    public String getEndpointURLFromNhinTarget(NhinTargetSystemType targetSystem, String serviceName)
            throws ConnectionManagerException {
        String sEndpointURL = null;

        if (targetSystem != null) {
            if (targetSystem.getEpr() != null) {
                // Extract the URL from the Endpoint Reference
                log.debug("Attempting to look up URL by EPR");
                if (targetSystem.getEpr().getEndpointReference() != null
                        && targetSystem.getEpr().getEndpointReference().getAddress() != null
                        && NullChecker.isNotNullish(targetSystem.getEpr().getEndpointReference().getAddress()
                                .getValue())) {
                    sEndpointURL = targetSystem.getEpr().getEndpointReference().getAddress().getValue();
                }
            } else if (NullChecker.isNotNullish(targetSystem.getUrl())) {
                // Echo back the URL provided
                log.debug("Attempting to look up URL by URL");
                sEndpointURL = targetSystem.getUrl();
            } else if (targetSystem.getHomeCommunity() != null
                    && NullChecker.isNotNullish(targetSystem.getHomeCommunity().getHomeCommunityId())
                    && NullChecker.isNotNullish(serviceName)) {
                // Get the URL based on Home Community Id and Service Name
                String homeCommunityId = cleanHomeCommunityId(targetSystem.getHomeCommunity().getHomeCommunityId());
                log.debug("Attempting to look up URL by home communinity id: " + homeCommunityId
                        + " and service name: " + serviceName);
                sEndpointURL = getDefaultEndpointURLByServiceName(homeCommunityId, serviceName);
            }
        }

        log.debug("Returning URL: " + sEndpointURL);
        return sEndpointURL;
    }

    /**
     * This method retrieves a set of unique URLs from the contents of the NhinTargetCommunities type. For each
     * NhinTargetCommunity type it will first check if a Home Community Id is specified. If so then it will add the URL
     * for the specified service for that home community to the List of URLs. Next it will check if a region (state) is
     * specified. If so it will obtain a list of URLs for that that service for all communities in the specified state.
     * Next it will check if a list is specified (this feature is not implemented). If there are no
     * NhinTargetCommunities specified it will return the list of URLs for the entire NHIN for that service.
     * 
     * @param targets List of targets to get URLs for.
     * @param serviceName The name of the service to locate who URL is being requested.
     * @return The set of URLs for the requested service and targets.
     * @throws ConnectionManagerException
     */
    public List<UrlInfo> getEndpointURLFromNhinTargetCommunities(NhinTargetCommunitiesType targets, String serviceName)
            throws ConnectionManagerException {
    	ConnectionManagerCacheHelper helper = new ConnectionManagerCacheHelper(); 
        Set<UrlInfo> endpointUrlSet = new HashSet<UrlInfo>();
        
        if (targets != null && NullChecker.isNotNullish(targets.getNhinTargetCommunity())) {
            for (NhinTargetCommunityType target : targets.getNhinTargetCommunity()) {
                if (target.getHomeCommunity() != null
                        && NullChecker.isNotNullish(target.getHomeCommunity().getHomeCommunityId())) {
                    log.info("Looking up URL by home community id");
                    String endpt = getDefaultEndpointURLByServiceName(target.getHomeCommunity().getHomeCommunityId(),
                            serviceName);

                    if ((NullChecker.isNotNullish(endpt)) || (NullChecker.isNullish(endpt) && (serviceName.equals(NhincConstants.DOC_QUERY_SERVICE_NAME)))) {
                        UrlInfo entry = new UrlInfo();
                        entry.setHcid(target.getHomeCommunity().getHomeCommunityId());
                        entry.setUrl(endpt);
                        endpointUrlSet.add(entry);
                    }
                }

                if (target.getRegion() != null) {
                    log.info("Looking up URL by region");
                    filterByRegion(endpointUrlSet, target.getRegion(), serviceName);
                }

                if (target.getList() != null) {
                    log.info("Looking up URL by list");
                    log.warn("The List target feature has not been implemented yet");
                }
            }
        } else {
            // This is the broadcast scenario so retrieve the entire list of URLs for the specified service
            for (BusinessEntity entity : getAllBusinessEntitySetByServiceName(serviceName)) {
                String hcid = helper.getCommunityId(entity);
                String endpt = getDefaultEndpointURLByServiceName(hcid, serviceName);

                if (NullChecker.isNotNullish(endpt)) {
                    UrlInfo entry = new UrlInfo();
                    entry.setHcid(hcid);
                    entry.setUrl(endpt);
                    endpointUrlSet.add(entry);
                }

            }
        }

        List<UrlInfo> endpointUrlList = new ArrayList<UrlInfo>(endpointUrlSet);
        if (endpointUrlList != null) {
            printURLList(endpointUrlList);
        }

        return endpointUrlList;
    }

}
