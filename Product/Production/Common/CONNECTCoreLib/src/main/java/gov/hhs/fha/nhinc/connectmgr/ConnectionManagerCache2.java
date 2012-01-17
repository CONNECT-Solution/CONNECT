/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.connectmgr;

import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.data.CMBindingDescriptions;
import gov.hhs.fha.nhinc.connectmgr.data.CMBindingTemplate;
import gov.hhs.fha.nhinc.connectmgr.data.CMBindingTemplates;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessDescriptions;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessEntity;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessNames;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessService;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessServices;
import gov.hhs.fha.nhinc.connectmgr.data.CMInternalConnInfoService;
import gov.hhs.fha.nhinc.connectmgr.data.CMInternalConnectionInfo;
import gov.hhs.fha.nhinc.connectmgr.data.CMInternalConnectionInfoLiftProtocol;
import gov.hhs.fha.nhinc.connectmgr.data.CMInternalConnectionInfoState;
import gov.hhs.fha.nhinc.connectmgr.data.CMLiftProtocols;
import gov.hhs.fha.nhinc.connectmgr.data.CMStates;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfo;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfos;
import gov.hhs.fha.nhinc.connectmgr.persistance.dao.ConnectionManagerDAO;
import gov.hhs.fha.nhinc.connectmgr.persistance.dao.ConnectionManagerDAOFileImpl;
import gov.hhs.fha.nhinc.connectmgr.persistance.dao.InternalConnectionManagerDAOFileImplFactory;
import gov.hhs.fha.nhinc.connectmgr.persistance.dao.UddiConnectionManagerDAOFileImplFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
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
import org.uddi.api_v3.Name;

/**
 * This class is used to manage the Connection Manager's cache.  It handles
 * both internal connection settings and UDDI connection settings.  If there is
 * a collision for a connection between the UDDI and the Internal settings, the
 * internal one will be used.
 *
 * @author Les Westberg
 */
public class ConnectionManagerCache2 {

    private static Log log = LogFactory.getLog(ConnectionManagerCache2.class);
    private static final String HOME_COMMUNITY_PREFIX = "urn:oid:";
    // Hash maps for the UDDI connectin information.  This hash map is keyed by home community ID.
    //--------------------------------------------------------------------------------------------
    private static HashMap<String, BusinessEntity> m_hUDDIConnectInfo = new HashMap<String, BusinessEntity>();       // Array of connection information
    private static boolean m_bUDDILoaded = false;      // TRUE if the properties have been loaded
    private static long m_lUDDIFileLastModified = 0;
    // Hash maps for the Internal connection information.  This hash map is keyed by home community ID.
    //--------------------------------------------------------------------------------------------------
    private static HashMap<String, BusinessEntity> m_hInternalConnectInfo = new HashMap<String, BusinessEntity>();       // Array of connection information
    private static boolean m_bInternalLoaded = false;      // TRUE if the properties have been loaded
    private static long m_lInternalFileLastModified = 0;
    // Variables for managing the location of the XML files.
    //-------------------------------------------------------
    private static ConnectionManagerDAOFileImpl internalConnectionManagerDAO = InternalConnectionManagerDAOFileImplFactory.getInstance();
    private static ConnectionManagerDAOFileImpl uddiConnectionManagerDAO = UddiConnectionManagerDAOFileImplFactory.getInstance();

    /**
     * This class is used for testing purposes so that the file locations can be overridden
     * to point to a controlled location available for unit tests.
     *
     * @param sUDDIFileName The path and file name for the UDDI XML file.
     * @param sInternalConnFileName The path and file name for the Internal Connectil File Name.
     */
    public static void overrideFileLocations(String sUDDIFileName, String sInternalConnFileName) {
    	uddiConnectionManagerDAO.setFileName(sUDDIFileName);
    	internalConnectionManagerDAO.setFileName(sInternalConnFileName);
    }

    /**
     * This method is used to load the UDDI Connection Infomration form the
     * uddiConnectionInfo.xml file.
     */
    private static void loadUDDIConnectionInfo()
            throws ConnectionManagerException {

    	BusinessDetail businessDetail = uddiConnectionManagerDAO.loadBusinessDetail();

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
            }   // synchronized (m_ohUDDIConnectInfo)
        } // if (oConnInfo != null)
        else {
            log.warn("No UDDI information was found");
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
     * This returns true if the set of states contains a state for the given state name.
     *
     * @param oStates The set of states to search.
     * @param state The name of the state to search for.
     * @return True if the state is found.
     */
    private static boolean containsState(CMStates oStates, String state) {
        boolean bFound = false;

        if ((oStates != null) &&
                (oStates.getState() != null) &&
                (oStates.getState().size() > 0) &&
                (state != null) &&
                (state.length() > 0)) {
            for (String stateName : oStates.getState()) {
                if ((state != null) &&
                        (stateName.equalsIgnoreCase(state))) {
                    bFound = true;
                }

            }
        }

        return bFound;
    }

    /**
     * This returns true if the set of protocols contains a protocol for the given protocol name.
     *
     * @param oStates The set of protocol to search.
     * @param state The name of the protocol to search for.
     * @return True if the protocol is found.
     */
    private static boolean containsProtocol(CMLiftProtocols oProtocols, String protocol) {
        boolean bFound = false;

        if ((oProtocols != null) &&
                (oProtocols.getProtocol() != null) &&
                (oProtocols.getProtocol().size() > 0) &&
                (protocol != null) &&
                (protocol.length() > 0)) {
            for (String protocolName : oProtocols.getProtocol()) {
                if ((protocol != null) &&
                        (protocolName.equalsIgnoreCase(protocol))) {
                    bFound = true;
                }

            }
        }

        return bFound;
    }

//	Temporary, local only implementation must be properly implemented. 
//	Replaces CMBusinessEntity mergeBusinessEntityServices(CMBusinessEntity, CMBusinessEntity)  
    private static BusinessEntity mergeBusinessEntityServices(BusinessEntity internalEntity,
            BusinessEntity uddiEntity)
            throws ConnectionManagerException { 
    	return internalEntity;
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

        // Put in all of the states from the InternalConnection one next - they are the king...
        //----------------------------------------------------------------------------------------
        oCombinedEntity.setStates(oInternalEntity.getStates());
        if (oCombinedEntity.getStates() == null) {
            oCombinedEntity.setStates(new CMStates());
        }

        // Now only add in the states from the UDDI that we do not have
        //-------------------------------------------------------------
        if ((oUDDIEntity.getStates() != null) &&
                (oUDDIEntity.getStates().getState() != null) &&
                (oUDDIEntity.getStates().getState().size() > 0)) {
            for (String oState : oUDDIEntity.getStates().getState()) {
                if (!containsState(oCombinedEntity.getStates(), oState)) {
                    oCombinedEntity.getStates().getState().add(oState);
                }
            }
        }

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

	private static String getCommunityId(BusinessEntity businessEntity) {
		if (businessEntity.getCategoryBag() == null) {
			return null;
		}
		for(KeyedReference reference : businessEntity.getCategoryBag().getKeyedReference()) {
			if (reference.getKeyName().equals("uddi:nhin:nhie:homecommunityid")) {
				return reference.getKeyValue();
			}
		}
		return null;
	}
    
	private static List<String> getStates(BusinessEntity businessEntity) {
		// TODO: Implement
		return null;
	}

	
    /**
     * This method is used to load the UDDI Connection Infomration form the
     * uddiConnectionInfo.xml file.
     */
    private static void loadInternalConnectionInfo()
            throws ConnectionManagerException {

    	BusinessDetail businessDetail = internalConnectionManagerDAO.loadBusinessDetail();
        
        
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
            lUDDILastModified = uddiConnectionManagerDAO.getLastModifie();
            lInternalLastModified = internalConnectionManagerDAO.getLastModifie();
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
    private static BusinessEntity homeCommunityFromInternalConnectionInfo(BusinessEntity businessEntity) {
    	return businessEntity;
    }

    /**
     * This method will return a list of all home commuinities that are known by the
     * connection manager.
     *
     * @return The list of all home communities known by the connection manager.
     * @throws ConnectionManagerException
     */
    public static List<BusinessEntity> getAllCommunities()
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
    private static BusinessEntity extractBusinessEntity(List<BusinessEntity> oEntities, String sHomeCommunityId) {
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
    private static void replaceBusinessEntity(List<BusinessEntity> oEntities, BusinessEntity oEntity) {
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
    public static List<BusinessEntity> getAllBusinessEntities()
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
    public static BusinessEntity getBusinessEntity(String sHomeCommunityId)
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
    public static Set<BusinessEntity> getBusinessEntitySet(List<String> saHomeCommunityId)
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
    
    private static BindingTemplate findCompatibleBindingTemplate(BusinessEntity businessEntity, String serviceName, GATEWAY_API_LEVEL apiLevel) {
    	if (businessEntity != null && businessEntity.getBusinessServices() != null && businessEntity.getBusinessKey() != null) {
        	for(BusinessService service : businessEntity.getBusinessServices().getBusinessService()) {
        		String name = null;
        		for(Name nameElement : service.getName()) {
        			String nameStr = nameElement.getValue();
        			if (serviceName.equals(nameStr)) {
        				name = nameStr;
        				break;
        			}
        		}
        		if (name == null) {
        			return null; // service with specified name not found 
        		}
        		if (service.getBindingTemplates() != null && service.getBindingTemplates().getBindingTemplate() != null) {
        			for(BindingTemplate template : service.getBindingTemplates().getBindingTemplate()) {
                		if (template.getCategoryBag() != null && template.getCategoryBag().getKeyedReference() != null) {
                			for(KeyedReference reference : template.getCategoryBag().getKeyedReference()) {
                				String keyName = reference.getKeyName();
                				String keyValue = reference.getKeyValue();
                				if (keyName.equals("uddi:nhin:versionofservice")) {
                					if (UddiSpecVersionRegestry.isSupported(apiLevel, keyValue)){
                						return template; // Found binding that supports specified version
                					}
                				}
                			}
                		} else {
                			return template; // no version specified, assuming it support all versions
                		}
        			}
        		}
        	}
    	}
    	return null; // no matching bindings
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
    public static BusinessEntity getBusinessEntityByServiceName(String sHomeCommunityId,
            String sUniformServiceName, GATEWAY_API_LEVEL apiLevel)
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
            //internalBusinessEntity = businessEntityFromInternalConnectionInfo(businessEntity);
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
        if(findCompatibleBindingTemplate(oCombinedEntity, sUniformServiceName, apiLevel) != null){
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
    public static String getEndpointURLByServiceName(String sHomeCommunityId,
            String sUniformServiceName,
            GATEWAY_API_LEVEL apiLevel)
            throws ConnectionManagerException {
        String sEndpointURL = "";

        BusinessEntity oEntity = getBusinessEntityByServiceName(sHomeCommunityId, sUniformServiceName, apiLevel);
        
        BindingTemplate bindingTemplate = findCompatibleBindingTemplate(oEntity, sUniformServiceName, apiLevel);
        sEndpointURL = bindingTemplate.getAccessPoint().getValue();

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
    public static String getLocalEndpointURLByServiceName(String sUniformServiceName, GATEWAY_API_LEVEL apiLevel)
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
            sEndpointURL = getEndpointURLByServiceName(sHomeCommunityId, sUniformServiceName, apiLevel);
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
    public static String getEndpontURLFromNhinTarget(NhinTargetSystemType targetSystem, String serviceName, GATEWAY_API_LEVEL apiLevel)
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
                sEndpointURL = getEndpointURLByServiceName(homeCommunityId, serviceName, apiLevel);
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
    public static CMUrlInfos getEndpontURLFromNhinTargetCommunities(NhinTargetCommunitiesType targets, String serviceName, GATEWAY_API_LEVEL apiLevel)
            throws ConnectionManagerException {
        CMUrlInfos endpointUrlList = new CMUrlInfos();

        if (targets != null &&
                NullChecker.isNotNullish(targets.getNhinTargetCommunity())) {
            for (NhinTargetCommunityType target : targets.getNhinTargetCommunity()) {
                if (target.getHomeCommunity() != null &&
                        NullChecker.isNotNullish(target.getHomeCommunity().getHomeCommunityId())) {
                    log.info("Looking up URL by home community id");
                    String endpt = getEndpointURLByServiceName(target.getHomeCommunity().getHomeCommunityId(), serviceName, apiLevel);

                    if (NullChecker.isNotNullish(endpt)) {
                        CMUrlInfo entry = new CMUrlInfo();
                        entry.setHcid(target.getHomeCommunity().getHomeCommunityId());
                        entry.setUrl(endpt);
                        endpointUrlList.getUrlInfo().add(entry);
                    }
                }

                if (target.getRegion() != null) {
                    log.info("Looking up URL by region");
                    filterByRegion(endpointUrlList, target.getRegion(), serviceName, apiLevel);
                }

                if (target.getList() != null) {
                    log.info("Looking up URL by list");
                    log.warn("The List target feature has not been implemented yet");
                }
            }
        } else {
            // This is the broadcast scenario so retrieve the entire list of URLs for the specified service
            Set<BusinessEntity> entities = getAllBusinessEntitySetByServiceName(serviceName, apiLevel);

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
    private static void filterByRegion(CMUrlInfos urlList, String region, String serviceName, GATEWAY_API_LEVEL apiLevel)
            throws ConnectionManagerException {
        Set<BusinessEntity> entities = getAllBusinessEntitySetByServiceName(serviceName, apiLevel);

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
                                CMUrlInfo entry = new CMUrlInfo();
                                entry.setHcid(hcid);
                                entry.setUrl(url);
                                urlList.getUrlInfo().add(entry);
                            }
                        }
                    }
                }
            }
        }

        return;
    }

    private static CMUrlInfos getUrlInfoFromBusinessEntities(List<BusinessEntity> businessEntityList) {
        CMUrlInfos urlList = new CMUrlInfos();

        if (NullChecker.isNotNullish(businessEntityList)) {
            for (BusinessEntity entity : businessEntityList) {
                String url = getUrl(entity);
                String hcid = getHcid(entity);

                if (NullChecker.isNotNullish(url) &&
                        NullChecker.isNotNullish(hcid)) {
                    CMUrlInfo entry = new CMUrlInfo();
                    entry.setUrl(url);
                    entry.setHcid(hcid);
                    urlList.getUrlInfo().add(entry);
                }
            }
        }

        return urlList;
    }

    private static String getUrl(BusinessEntity entity) {
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

    private static String getHcid(BusinessEntity entity) {
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
    private static void createUniqueList(CMUrlInfos urlList) {
        CMUrlInfos tempList = new CMUrlInfos();
        boolean foundDup = false;

        // Find the duplicates
        for (CMUrlInfo entry : urlList.getUrlInfo()) {
            foundDup = false;
            for (CMUrlInfo temp : tempList.getUrlInfo()) {
                if (temp.equals(entry)) {
                    foundDup = true;
                    break;
                }
            }
            if (foundDup == false) {
                tempList.getUrlInfo().add(entry);
            }
        }

        // Remove the duplicates
        urlList.clear();
        for (CMUrlInfo temp : tempList.getUrlInfo()) {
            urlList.getUrlInfo().add(temp);
        }

        return;
    }

    /**
     * This method will print out the contents of a URL list.
     *
     * @param urlList List of URLs.
     * @return void.
     */
    private static void printURLList(CMUrlInfos urlList) {
        int idx = 0;

        if (urlList != null &&
                urlList.getUrlInfo() != null) {
            log.debug("Connection Management URL Info List:");
            for (CMUrlInfo url : urlList.getUrlInfo()) {
                log.debug("   HCID: " + url.getHcid() + " URL #" + idx + ": " + url.getUrl());
                idx++;
            }
        } else {
            log.debug("Url List was Empty");
        }
    }

    private static String cleanHomeCommunityId(String homeCommunityId) {
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
    public static Set<BusinessEntity> getBusinessEntitySetByServiceName(List<String> saHomeCommunityId, String sUniformServiceName, GATEWAY_API_LEVEL apiLevel)
            throws ConnectionManagerException {
    	Set<BusinessEntity> oEntities = new HashSet<BusinessEntity>();

        checkLoaded();

        if ((saHomeCommunityId == null) || (saHomeCommunityId.size() <= 0) ||
                (sUniformServiceName == null) || (sUniformServiceName.length() <= 0)) {
            return null;
        }

        for (String sHomeCommunityId : saHomeCommunityId) {
            BusinessEntity oEntity = getBusinessEntityByServiceName(sHomeCommunityId, sUniformServiceName, apiLevel);
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
    public static Set<BusinessEntity> getAllBusinessEntitySetByServiceName(String sUniformServiceName, GATEWAY_API_LEVEL apiLevel)
            throws ConnectionManagerException {
        Set<BusinessEntity> oEntities = new HashSet<BusinessEntity>();

        checkLoaded();

        HashSet<String> hKeys = new HashSet<String>();

        // This is a slick way to add them all and remove any duplicates...
        //------------------------------------------------------------------
        hKeys.addAll(m_hInternalConnectInfo.keySet());
        hKeys.addAll(m_hUDDIConnectInfo.keySet());

        ArrayList<String> saHomeCommunityIds = new ArrayList<String>(hKeys);
        oEntities = getBusinessEntitySetByServiceName(saHomeCommunityIds, sUniformServiceName, apiLevel);

        if ((oEntities != null) && (oEntities.size() > 0)) {
            return oEntities;
        } else {
            return null;
        }
    }

    public static boolean liftProtocolSupportedForHomeCommunity(String homeCommunityId, String protocol, String service, GATEWAY_API_LEVEL apiLevel) throws ConnectionManagerException {
        boolean result = false;
        return result;
    }

    public static GATEWAY_API_LEVEL getApiVersionForNhinTarget(String homeCommunityId, String service)
    {
        try {
            return GATEWAY_API_LEVEL.valueOf(PropertyAccessor.getProperty("gateway", "GATEWAY_API_LEVEL"));
        } catch (PropertyAccessException ex) {
            Logger.getLogger(ConnectionManagerCache2.class.getName()).log(Level.SEVERE, null, ex);
        }

        return GATEWAY_API_LEVEL.LEVEL_g0;
    }

    public static String getAdapterEndpontURL(String sServiceName, ADAPTER_API_LEVEL level) {
        return "https://localhost:8181/GatewayDocumentQuery/1_0/NhinService/RespondingGateway_Query_Service/DocQuery";
    }
}
