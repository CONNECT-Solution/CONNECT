/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.common.connectionmanager;

import gov.hhs.fha.nhinc.common.connectionmanager.dao.AssigningAuthorityHomeCommunityMappingDAO;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;

import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;

import gov.hhs.fha.nhinc.common.connectionmanagerinfo.BusinessEntitiesType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.BusinessEntityType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.ConnectionInfoEndpointType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.ConnectionInfoEndpointsType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.ConnectionInfoType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.ConnectionInfosType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.ForceRefreshInternalConnectCacheRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.ForceRefreshUDDICacheRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetAllBusinessEntitiesRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetAllBusinessEntitySetByServiceNameRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetAllCommunitiesRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetAllConnectionInfoEndpointSetByServiceNameRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetBusinessEntityByServiceNameRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetBusinessEntityRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetBusinessEntitySetByServiceNameRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetBusinessEntitySetRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetConnectionInfoEndpointByServiceNameRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetConnectionInfoEndpointSetByServiceNameRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetConnectionInfoEndpointSetRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.HomeCommunitiesWithServiceNameType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.HomeCommunityWithServiceNameType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.SuccessOrFailType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetAllConnectionInfoSetByServiceNameRequestType;

import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetAssigningAuthoritiesByHomeCommunityRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetAssigningAuthoritiesByHomeCommunityResponseType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetHomeCommunityByAssigningAuthorityRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetHomeCommunityByAssigningAuthorityResponseType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.StoreAssigningAuthorityToHomeCommunityMappingRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetConnectionInfoEndpontFromNhinTargetType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetUrlSetByServiceNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssigningAuthoritiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssigningAuthorityType;
import gov.hhs.fha.nhinc.common.nhinccommon.EPRType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlSetType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessEntities;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessEntity;
import gov.hhs.fha.nhinc.connectmgr.data.CMHomeCommunity;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfo;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfos;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.ArrayList;

/**
 * This class is a helper class to the ConnectionManagerService.  It does
 * most of the java level work so that it can be unit tested outside of
 * the web service container.
 * 
 * @author Les Westberg
 */
public class CMServiceHelper {

    private static Log log = LogFactory.getLog(CMServiceHelper.class);

    /**
     * This method returns a list of all communities that are known.
     * 
     * @param part1 The only purpose for this parameter is so that the 
     *              web service has a unique document that identifies this 
     *              operation.  The values themselves are not used.
     * @return List of all communities. 
     */
    public static HomeCommunitiesType getAllCommunities(GetAllCommunitiesRequestType part1) {
        HomeCommunitiesType oCommunities = new HomeCommunitiesType();

        List<CMHomeCommunity> oaCMHomeCommunity = null;

        try {
            oaCMHomeCommunity = ConnectionManagerCache.getAllCommunities();
        } catch (Throwable t) {
            String sErrorMessage = "Failed to retrieve home communities.  Error: " + t.getMessage();
            log.error(sErrorMessage, t);
        }

        oCommunities = CMTransform.listCMHomeCommunityToHomeCommunitiesType(oaCMHomeCommunity);
        return oCommunities;
    }

    /**
     * This method returns a list of all communities along with their demongraphic
     * information that are known.
     * 
     * @param part1 The only purpose for this parameter is so that the 
     *              web service has a unique document that identifies this 
     *              operation.  The values themselves are not used.
     * @return List of all communities. 
     */
    public static BusinessEntitiesType getAllBusinessEntities(GetAllBusinessEntitiesRequestType part1) {
        BusinessEntitiesType oBusinessEntities = new BusinessEntitiesType();

        CMBusinessEntities oCMBusinessEntities = null;

        try {
            oCMBusinessEntities = ConnectionManagerCache.getAllBusinessEntities();
        } catch (Throwable t) {
            String sErrorMessage = "Failed to retrieve home business entities.  Error: " + t.getMessage();
            log.error(sErrorMessage, t);
        }

        oBusinessEntities = CMTransform.cmBusinessEntitiesToBusinessEntitiesType(oCMBusinessEntities);
        return oBusinessEntities;
    }

    /**
     * This method retrieves the information about a single business entity.
     * 
     * @param part1 The home community ID for the business entity.
     * @return The information about the business entity.
     */
    public static BusinessEntityType getBusinessEntity(GetBusinessEntityRequestType part1) {
        BusinessEntityType oBusinessEntity = null;
        String sHomeCommunityId = "";
        if ((part1 != null) &&
                (part1.getHomeCommunity() != null) &&
                (part1.getHomeCommunity().getHomeCommunityId() != null) &&
                (part1.getHomeCommunity().getHomeCommunityId().length() > 0) &&
                (part1.getAssigningAuthority() != null) &&
                (part1.getAssigningAuthority().getAssigningAuthorityId() != null) &&
                (part1.getAssigningAuthority().getAssigningAuthorityId().length() > 0)) {
            AssigningAuthorityHomeCommunityMappingDAO mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();
            sHomeCommunityId = CMTransform.formatHomeCommunityId(part1.getHomeCommunity().getHomeCommunityId());
            mappingDao.storeMapping(sHomeCommunityId, part1.getAssigningAuthority().getAssigningAuthorityId());
        } else if ((part1 != null) &&
                (part1.getHomeCommunity() != null) &&
                (part1.getHomeCommunity().getHomeCommunityId() != null) &&
                (part1.getHomeCommunity().getHomeCommunityId().length() > 0)) {
            sHomeCommunityId = CMTransform.formatHomeCommunityId(part1.getHomeCommunity().getHomeCommunityId());
        } else if ((part1 != null) &&
                (part1.getAssigningAuthority() != null) &&
                (part1.getAssigningAuthority().getAssigningAuthorityId().length() > 0)) {
            String assigningAuthId = part1.getAssigningAuthority().getAssigningAuthorityId();
            AssigningAuthorityHomeCommunityMappingDAO mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();
            sHomeCommunityId = mappingDao.getHomeCommunityId(assigningAuthId);
        }

        // If we have a home community ID, do the retrieve ....
        //------------------------------------------------------
        if ((sHomeCommunityId != null) &&
                (sHomeCommunityId.length() > 0)) {
            CMBusinessEntity oCMBusinessEntity = null;

            try {
                oCMBusinessEntity = ConnectionManagerCache.getBusinessEntity(sHomeCommunityId);
            } catch (Throwable t) {
                String sErrorMessage = "Failed to retrieve business entity.  Error: " + t.getMessage();
                log.error(sErrorMessage, t);
            }

            oBusinessEntity = CMTransform.cmBusinessEntityToBusinessEntityType(oCMBusinessEntity);
        }

        return oBusinessEntity;
    }

    /**
     * This retrieves the business entity set from the cache.
     * 
     * @param saHomeCommunityId The list of home community ids.
     * @return The Business entities that are retrieved.
     */
    private static CMBusinessEntities retrieveBusinessEntitySetFromCache(List<String> saHomeCommunityId) {
        CMBusinessEntities oCMBusinessEntities = null;

        if ((saHomeCommunityId != null) &&
                (saHomeCommunityId.size() > 0)) {
            try {
                oCMBusinessEntities = ConnectionManagerCache.getBusinessEntitySet(saHomeCommunityId);
            } catch (Throwable t) {
                String sErrorMessage = "Failed to retrieve business entities.  Error: " + t.getMessage();
                log.error(sErrorMessage, t);
            }
        }

        return oCMBusinessEntities;
    }

    /**
     * This method returns the connection information for the set of home communities that were passed in.
     * 
     * @param part1  The set of home communities for which the connection information is needed.
     * @return The connection info for the home communities.
     */
    public static ConnectionInfosType getConnectionInfoSet(HomeCommunitiesType part1) {
        ConnectionInfosType oConnectionInfos = null;

        if ((part1 != null) &&
                (part1.getHomeCommunity() != null) &&
                (part1.getHomeCommunity().size() > 0)) {

            List<String> saHomeCommunityId = CMTransform.listHomeCommunityToListHomeCommunityId(part1.getHomeCommunity());
            CMBusinessEntities oCMBusinessEntities = retrieveBusinessEntitySetFromCache(saHomeCommunityId);
            oConnectionInfos = CMTransform.cmBusinessEntitiesToConnectionInfosType(oCMBusinessEntities);

        }   // if ((part1 != null) &&

        return oConnectionInfos;
    }

    /**
     * This method returns the connection endpoint information for the set of home communities 
     * that were passed in.
     * 
     * @param part1  The set of home communities for which the connection information is needed.
     * @return The connection endpoint info for the home communities.
     */
    public static ConnectionInfoEndpointsType getConnectionInfoEndpointSet(GetConnectionInfoEndpointSetRequestType part1) {
        ConnectionInfoEndpointsType oConnectionInfoEndpoints = null;
        List<String> saHomeCommunityId = null;

        if ((part1 != null) &&
                (part1.getHomeCommunities() != null) &&
                (part1.getHomeCommunities().getHomeCommunity() != null) &&
                (part1.getHomeCommunities().getHomeCommunity().size() > 0)) {
            saHomeCommunityId = CMTransform.listHomeCommunityToListHomeCommunityId(part1.getHomeCommunities().getHomeCommunity());
        } else if ((part1 != null) &&
                (part1.getAssigningAuthorities() != null) &&
                (part1.getAssigningAuthorities().getAssigningAuthority() != null) &&
                (part1.getAssigningAuthorities().getAssigningAuthority().size() > 0)) {
            AssigningAuthorityHomeCommunityMappingDAO mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();
            int size = part1.getAssigningAuthorities().getAssigningAuthority().size();
            List<AssigningAuthorityType> assignIdList = part1.getAssigningAuthorities().getAssigningAuthority();
            String tempHomeCommId = "";
            saHomeCommunityId = new ArrayList();
            for (int i = 0; i < size; i++) {
                tempHomeCommId = mappingDao.getHomeCommunityId(assignIdList.get(i).getAssigningAuthorityId());
                saHomeCommunityId.add(tempHomeCommId);
            }
        }
        CMBusinessEntities oCMBusinessEntities = retrieveBusinessEntitySetFromCache(saHomeCommunityId);
        oConnectionInfoEndpoints = CMTransform.cmBusinessEntitiesToConnectionInfoEndpointsType(oCMBusinessEntities);
        return oConnectionInfoEndpoints;
    }

    /**
     * This method returns the business information along with their connection information 
     * for the set of home communities that were passed in.
     * 
     * @param part1  The set of home communities for which the connection information is needed.
     * @return The connection and business info for the home communities.
     */
    public static BusinessEntitiesType getBusinessEntitySet(GetBusinessEntitySetRequestType part1) {
        BusinessEntitiesType oBusinessEntities = null;
        CMBusinessEntities oCMBusinessEntities = null;
        List<String> saHomeCommunityId = null;

        if ((part1 != null) &&
                (part1.getHomeCommunities() != null) &&
                (part1.getHomeCommunities().getHomeCommunity() != null) &&
                (part1.getHomeCommunities().getHomeCommunity().size() > 0) &&
                (part1.getAssigningAuthorities() != null) &&
                (part1.getAssigningAuthorities().getAssigningAuthority() != null) &&
                (part1.getAssigningAuthorities().getAssigningAuthority().size() > 0)) {
            String sHomeCommId = "";
            String sAssigningAuthId = "";
            int hSize = part1.getHomeCommunities().getHomeCommunity().size();
            AssigningAuthorityHomeCommunityMappingDAO mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();
            List<HomeCommunityType> homeCommunityTypeList = part1.getHomeCommunities().getHomeCommunity();
            for (int i = 0; i < hSize; i++) {
                sHomeCommId = CMTransform.formatHomeCommunityId(homeCommunityTypeList.get(i).getHomeCommunityId());
                sAssigningAuthId = part1.getAssigningAuthorities().getAssigningAuthority().get(i).getAssigningAuthorityId();
                mappingDao.storeMapping(sHomeCommId, sAssigningAuthId);
            }
            saHomeCommunityId = CMTransform.listHomeCommunityToListHomeCommunityId(part1.getHomeCommunities().getHomeCommunity());
            oCMBusinessEntities = retrieveBusinessEntitySetFromCache(saHomeCommunityId);
            oBusinessEntities = CMTransform.cmBusinessEntitiesToBusinessEntitiesType(oCMBusinessEntities);
        } else if ((part1 != null) &&
                (part1.getHomeCommunities() != null) &&
                (part1.getHomeCommunities().getHomeCommunity() != null) &&
                (part1.getHomeCommunities().getHomeCommunity().size() > 0)) {
            saHomeCommunityId = CMTransform.listHomeCommunityToListHomeCommunityId(part1.getHomeCommunities().getHomeCommunity());
            oCMBusinessEntities = retrieveBusinessEntitySetFromCache(saHomeCommunityId);
            oBusinessEntities = CMTransform.cmBusinessEntitiesToBusinessEntitiesType(oCMBusinessEntities);
        } else if ((part1 != null) &&
                (part1.getAssigningAuthorities() != null) &&
                (part1.getAssigningAuthorities().getAssigningAuthority() != null) &&
                (part1.getAssigningAuthorities().getAssigningAuthority().size() > 0)) {
            int size = part1.getAssigningAuthorities().getAssigningAuthority().size();
            saHomeCommunityId = new ArrayList();
            String assigningAuthId = "";
            AssigningAuthorityHomeCommunityMappingDAO mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();
            for (int i = 0; i < size; i++) {
                assigningAuthId = part1.getAssigningAuthorities().getAssigningAuthority().get(i).getAssigningAuthorityId();
                saHomeCommunityId.add(mappingDao.getHomeCommunityId(assigningAuthId));
            }
            oCMBusinessEntities = retrieveBusinessEntitySetFromCache(saHomeCommunityId);
            oBusinessEntities = CMTransform.cmBusinessEntitiesToBusinessEntitiesType(oCMBusinessEntities);
        }
        return oBusinessEntities;
    }

    /**
     * This retrieves the business entity set from the cache by service name.
     * 
     * @param saHomeCommunityId The list of home community ids.
     * @param sServiceName The name of the service.
     * @return The Business entities that are retrieved.
     */
    private static CMBusinessEntities retrieveBusinessEntitySetByServiceNameFromCache(List<String> saHomeCommunityId,
            String sServiceName) {
        CMBusinessEntities oCMBusinessEntities = null;

        if ((saHomeCommunityId != null) &&
                (saHomeCommunityId.size() > 0)) {
            try {
                oCMBusinessEntities = ConnectionManagerCache.getBusinessEntitySetByServiceName(saHomeCommunityId, sServiceName);
            } catch (Throwable t) {
                String sErrorMessage = "Failed to retrieve business entities.  Error: " + t.getMessage();
                log.error(sErrorMessage, t);
            }
        }
        return oCMBusinessEntities;
    }

    /**
     * This method retrieves the ConnectionInformation for a specific service at a 
     * specific set of home communities
     * 
     * @param part1 This contains the home communities identification and the name of the service that the 
     *              connection info is desired.
     * @return The connection information for the service at the specified home community.
     */
    public static ConnectionInfosType getConnectionInfoSetByServiceName(HomeCommunitiesWithServiceNameType part1) {
        ConnectionInfosType oConnectionInfos = null;
        List<String> saHomeCommunityId = null;
        String sServiceName = "";

        if ((part1 != null) &&
                (part1.getHomeCommunities() != null) &&
                (part1.getHomeCommunities().getHomeCommunity() != null) &&
                (part1.getHomeCommunities().getHomeCommunity().size() > 0) &&
                (part1.getServiceName() != null) &&
                (part1.getServiceName().length() > 0)) {
            sServiceName = part1.getServiceName();
            saHomeCommunityId = CMTransform.listHomeCommunityToListHomeCommunityId(part1.getHomeCommunities().getHomeCommunity());
        } else if ((part1 != null) &&
                (part1.getAssigningAuthorities() != null) &&
                (part1.getAssigningAuthorities().getAssigningAuthority() != null) &&
                (part1.getAssigningAuthorities().getAssigningAuthority().size() > 0) &&
                (part1.getServiceName() != null) &&
                (part1.getServiceName().length() > 0)) {
            sServiceName = part1.getServiceName();
            int size = part1.getAssigningAuthorities().getAssigningAuthority().size();
            AssigningAuthorityHomeCommunityMappingDAO mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();
            saHomeCommunityId = new ArrayList();
            String assigningAuthId = "";
            List<AssigningAuthorityType> assignList = part1.getAssigningAuthorities().getAssigningAuthority();
            for (int i = 0; i < size; i++) {
                assigningAuthId = assignList.get(i).getAssigningAuthorityId();
                saHomeCommunityId.add(mappingDao.getHomeCommunityId(assigningAuthId));
            }
        }
        CMBusinessEntities oCMBusinessEntities = retrieveBusinessEntitySetByServiceNameFromCache(saHomeCommunityId, sServiceName);
        oConnectionInfos = CMTransform.cmBusinessEntitiesToConnectionInfosType(oCMBusinessEntities);
        return oConnectionInfos;
    }

    /**
     * This method retrieves the Endpoint Connection Information for a specific service at a 
     * specific set of home communities
     * 
     * @param part1 This contains the home communities identification and the name of the service that the 
     *              connection info is desired.
     * @return The endpoint connection information for the service at the specified home community.
     */
    public static ConnectionInfoEndpointsType getConnectionInfoEndpointSetByServiceName(GetConnectionInfoEndpointSetByServiceNameRequestType part1) {
        ConnectionInfoEndpointsType oConnectionInfoEndpoints = null;
        List<String> saHomeCommunityId = null;
        String sServiceName = "";
        if ((part1 != null) &&
                (part1.getHomeCommunitiesWithServiceName() != null) &&
                (part1.getHomeCommunitiesWithServiceName().getHomeCommunities() != null) &&
                (part1.getHomeCommunitiesWithServiceName().getHomeCommunities().getHomeCommunity() != null) &&
                (part1.getHomeCommunitiesWithServiceName().getHomeCommunities().getHomeCommunity().size() > 0) &&
                (part1.getHomeCommunitiesWithServiceName().getServiceName() != null) &&
                (part1.getHomeCommunitiesWithServiceName().getServiceName().length() > 0)) {
            sServiceName = part1.getHomeCommunitiesWithServiceName().getServiceName();
            List<HomeCommunityType> oaHomeCommunity = part1.getHomeCommunitiesWithServiceName().getHomeCommunities().getHomeCommunity();
            saHomeCommunityId = CMTransform.listHomeCommunityToListHomeCommunityId(oaHomeCommunity);
        } else if ((part1 != null) &&
                (part1.getHomeCommunitiesWithServiceName() != null) &&
                (part1.getHomeCommunitiesWithServiceName().getAssigningAuthorities() != null) &&
                (part1.getHomeCommunitiesWithServiceName().getAssigningAuthorities().getAssigningAuthority() != null) &&
                (part1.getHomeCommunitiesWithServiceName().getAssigningAuthorities().getAssigningAuthority().size() > 0) &&
                (part1.getHomeCommunitiesWithServiceName().getServiceName() != null) &&
                (part1.getHomeCommunitiesWithServiceName().getServiceName().length() > 0)) {
            sServiceName = part1.getHomeCommunitiesWithServiceName().getServiceName();
            List<AssigningAuthorityType> assignList = part1.getHomeCommunitiesWithServiceName().getAssigningAuthorities().getAssigningAuthority();
            String tempHomeCommId = "";
            AssigningAuthorityHomeCommunityMappingDAO mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();
            int size = part1.getHomeCommunitiesWithServiceName().getAssigningAuthorities().getAssigningAuthority().size();
            saHomeCommunityId = new ArrayList();
            for (int i = 0; i < size; i++) {
                tempHomeCommId = mappingDao.getHomeCommunityId(assignList.get(i).getAssigningAuthorityId());
                saHomeCommunityId.add(tempHomeCommId);
            }
        }
        CMBusinessEntities oCMBusinessEntities = retrieveBusinessEntitySetByServiceNameFromCache(saHomeCommunityId, sServiceName);
        oConnectionInfoEndpoints = CMTransform.cmBusinessEntitiesToConnectionInfoEndpointsType(oCMBusinessEntities);
        return oConnectionInfoEndpoints;
    }

    /**
     * This method retrieves the business entity and Connection Information for a specific service at a 
     * specific set of home communities
     * 
     * @param part1 This contains the home communities identification and the name of the service that the 
     *              connection info is desired.
     * @return The endpoint connection information for the service at the specified home community.
     */
    public static BusinessEntitiesType getBusinessEntitySetByServiceName(GetBusinessEntitySetByServiceNameRequestType part1) {
        BusinessEntitiesType oBusinessEntities = null;
        List<String> saHomeCommunityId = null;
        String sServiceName = "";

        if ((part1 != null) &&
                (part1.getHomeCommunitiesWithServiceName() != null) &&
                (part1.getHomeCommunitiesWithServiceName().getHomeCommunities() != null) &&
                (part1.getHomeCommunitiesWithServiceName().getHomeCommunities().getHomeCommunity() != null) &&
                (part1.getHomeCommunitiesWithServiceName().getHomeCommunities().getHomeCommunity().size() > 0) &&
                (part1.getHomeCommunitiesWithServiceName().getAssigningAuthorities() != null) &&
                (part1.getHomeCommunitiesWithServiceName().getAssigningAuthorities().getAssigningAuthority() != null) &&
                (part1.getHomeCommunitiesWithServiceName().getAssigningAuthorities().getAssigningAuthority().size() > 0)) {
            int size = part1.getHomeCommunitiesWithServiceName().getHomeCommunities().getHomeCommunity().size();
            String sHCommId = "";
            String sAssAuthId = "";
            AssigningAuthorityHomeCommunityMappingDAO mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();
            List<HomeCommunityType> homeCommList = part1.getHomeCommunitiesWithServiceName().getHomeCommunities().getHomeCommunity();
            List<AssigningAuthorityType> assignAuthList = part1.getHomeCommunitiesWithServiceName().getAssigningAuthorities().getAssigningAuthority();
            for (int i = 0; i < size; i++) {
                sHCommId = CMTransform.formatHomeCommunityId(homeCommList.get(i).getHomeCommunityId());
                sAssAuthId = assignAuthList.get(i).getAssigningAuthorityId();
                if (sHCommId != null && sHCommId.length() > 0 && sAssAuthId != null && sAssAuthId.length() > 0) {
                    mappingDao.storeMapping(sHCommId, sAssAuthId);
                }
            }
        }

        if ((part1 != null) &&
                (part1.getHomeCommunitiesWithServiceName() != null) &&
                (part1.getHomeCommunitiesWithServiceName().getHomeCommunities() != null) &&
                (part1.getHomeCommunitiesWithServiceName().getHomeCommunities().getHomeCommunity() != null) &&
                (part1.getHomeCommunitiesWithServiceName().getHomeCommunities().getHomeCommunity().size() > 0) &&
                (part1.getHomeCommunitiesWithServiceName().getServiceName() != null) &&
                (part1.getHomeCommunitiesWithServiceName().getServiceName().length() > 0)) {
            sServiceName = part1.getHomeCommunitiesWithServiceName().getServiceName();
            List<HomeCommunityType> oaHomeCommunity = part1.getHomeCommunitiesWithServiceName().getHomeCommunities().getHomeCommunity();
            saHomeCommunityId = CMTransform.listHomeCommunityToListHomeCommunityId(oaHomeCommunity);
        } else if ((part1 != null) &&
                (part1.getHomeCommunitiesWithServiceName() != null) &&
                (part1.getHomeCommunitiesWithServiceName().getAssigningAuthorities() != null) &&
                (part1.getHomeCommunitiesWithServiceName().getAssigningAuthorities().getAssigningAuthority() != null) &&
                (part1.getHomeCommunitiesWithServiceName().getAssigningAuthorities().getAssigningAuthority().size() > 0) &&
                (part1.getHomeCommunitiesWithServiceName().getServiceName() != null) &&
                (part1.getHomeCommunitiesWithServiceName().getServiceName().length() > 0)) {
            sServiceName = part1.getHomeCommunitiesWithServiceName().getServiceName();
            AssigningAuthorityHomeCommunityMappingDAO mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();
            int size = part1.getHomeCommunitiesWithServiceName().getAssigningAuthorities().getAssigningAuthority().size();
            List<AssigningAuthorityType> assignList = part1.getHomeCommunitiesWithServiceName().getAssigningAuthorities().getAssigningAuthority();
            String sAssigningAuthorityId = "";
            String tempHomeCommunityId = "";
            saHomeCommunityId = new ArrayList();
            for (int i = 0; i < size; i++) {
                sAssigningAuthorityId = assignList.get(i).getAssigningAuthorityId();
                tempHomeCommunityId = mappingDao.getHomeCommunityId(sAssigningAuthorityId);
                saHomeCommunityId.add(tempHomeCommunityId);
            }
        }
        CMBusinessEntities oCMBusinessEntities = retrieveBusinessEntitySetByServiceNameFromCache(saHomeCommunityId, sServiceName);
        oBusinessEntities = CMTransform.cmBusinessEntitiesToBusinessEntitiesType(oCMBusinessEntities);
        return oBusinessEntities;
    }

    /**
     * This retrieves the business entity from the cache by service name.
     * 
     * @param sHomeCommunityId The home community id.
     * @param sServiceName The name of the service.
     * @return The Business entities that are retrieved.
     */
    private static CMBusinessEntity retrieveBusinessEntityByServiceNameFromCache(String sHomeCommunityId,
            String sServiceName) {
        CMBusinessEntity oCMBusinessEntity = null;

        if ((sHomeCommunityId != null) &&
                (sHomeCommunityId.length() > 0) &&
                (sServiceName != null) &&
                (sServiceName.length() > 0)) {
            try {
                oCMBusinessEntity = ConnectionManagerCache.getBusinessEntityByServiceName(sHomeCommunityId, sServiceName);
            } catch (Throwable t) {
                String sErrorMessage = "Failed to retrieve business entity.  Error: " + t.getMessage();
                log.error(sErrorMessage, t);
            }
        }

        return oCMBusinessEntity;
    }

    /**
     * This method retrieves the ConnectionInformation for a specific service at a specific home community.
     * 
     * @param part1 This contains the home community identification and the name of the service that the 
     *              connection info is desired.
     * @return The connection information for the service at the specified home community.
     */
    public static ConnectionInfoType getConnectionInfoByServiceName(HomeCommunityWithServiceNameType part1) {
        ConnectionInfoType oConnectionInfo = null;
        String sServiceName = "";
        String sHomeCommunityId = "";

        if ((part1 != null) &&
                (part1.getHomeCommunity() != null) &&
                (part1.getHomeCommunity().getHomeCommunityId() != null) &&
                (part1.getHomeCommunity().getHomeCommunityId().length() > 0) &&
                (part1.getServiceName() != null) &&
                (part1.getServiceName().length() > 0)) {
            sServiceName = part1.getServiceName();
            sHomeCommunityId = CMTransform.formatHomeCommunityId(part1.getHomeCommunity().getHomeCommunityId());
        } else if ((part1 != null) &&
                (part1.getAssigningAuthority() != null) &&
                (part1.getAssigningAuthority().getAssigningAuthorityId() != null) &&
                (part1.getAssigningAuthority().getAssigningAuthorityId().length() > 0) &&
                (part1.getServiceName() != null) &&
                (part1.getServiceName().length() > 0)) {
            sServiceName = part1.getServiceName();
            String assigningAuthId = part1.getAssigningAuthority().getAssigningAuthorityId();
            AssigningAuthorityHomeCommunityMappingDAO mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();
            sHomeCommunityId = mappingDao.getHomeCommunityId(assigningAuthId);
        }

        if ((sServiceName != null) &&
                (sServiceName.length() > 0) &&
                (sHomeCommunityId != null) &&
                (sHomeCommunityId.length() > 0)) {
            CMBusinessEntity oCMBusinessEntity = retrieveBusinessEntityByServiceNameFromCache(sHomeCommunityId, sServiceName);
            if (oCMBusinessEntity != null) {
                // We already have a routine to transform a CMBusinessEntities - so use it...
                //---------------------------------------------------------------------------
                CMBusinessEntities oCMBusinessEntities = new CMBusinessEntities();
                oCMBusinessEntities.getBusinessEntity().add(oCMBusinessEntity);
                ConnectionInfosType oConnectionInfos = CMTransform.cmBusinessEntitiesToConnectionInfosType(oCMBusinessEntities);
                if ((oConnectionInfos != null) &&
                        (oConnectionInfos.getConnectionInfo() != null) &&
                        (oConnectionInfos.getConnectionInfo().size() > 0)) {
                    // We only gave it one so we should only get one back...
                    //-------------------------------------------------------
                    oConnectionInfo = oConnectionInfos.getConnectionInfo().get(0);
                }
            }
        }
        return oConnectionInfo;
    }

    /**
     * This method retrieves the endpoint Connection Information for a specific service at a 
     * specific home community.
     * 
     * @param part1 This contains the home community identification and the name of the service that the 
     *              connection info is desired.
     * @return The endpoint connection information for the service at the specified home community.
     */
    public static ConnectionInfoEndpointType getConnectionInfoEndpointByServiceName(GetConnectionInfoEndpointByServiceNameRequestType part1) {
        ConnectionInfoEndpointType oConnectionInfoEndpoint = null;
        String sServiceName = "";
        String sHomeCommunityId = "";

        if ((part1 != null) &&
                (part1.getHomeCommunityWithServiceName() != null) &&
                (part1.getHomeCommunityWithServiceName().getHomeCommunity() != null) &&
                (part1.getHomeCommunityWithServiceName().getHomeCommunity().getHomeCommunityId() != null) &&
                (part1.getHomeCommunityWithServiceName().getHomeCommunity().getHomeCommunityId().length() > 0) &&
                (part1.getHomeCommunityWithServiceName().getServiceName() != null) &&
                (part1.getHomeCommunityWithServiceName().getServiceName().length() > 0)) {
            sServiceName = part1.getHomeCommunityWithServiceName().getServiceName();
            sHomeCommunityId = CMTransform.formatHomeCommunityId(part1.getHomeCommunityWithServiceName().getHomeCommunity().getHomeCommunityId());

        } else if ((part1 != null) &&
                (part1.getHomeCommunityWithServiceName() != null) &&
                (part1.getHomeCommunityWithServiceName().getAssigningAuthority() != null) &&
                (part1.getHomeCommunityWithServiceName().getAssigningAuthority().getAssigningAuthorityId() != null) &&
                (part1.getHomeCommunityWithServiceName().getAssigningAuthority().getAssigningAuthorityId().length() > 0) &&
                (part1.getHomeCommunityWithServiceName().getServiceName() != null) &&
                (part1.getHomeCommunityWithServiceName().getServiceName().length() > 0)) {
            String assigningAuthId = part1.getHomeCommunityWithServiceName().getAssigningAuthority().getAssigningAuthorityId();
            AssigningAuthorityHomeCommunityMappingDAO mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();
            sHomeCommunityId = mappingDao.getHomeCommunityId(assigningAuthId);
            sServiceName = part1.getHomeCommunityWithServiceName().getServiceName();
        }

        if (sServiceName != null && sServiceName.length() > 0 && sHomeCommunityId != null && sHomeCommunityId.length() > 0) {
            CMBusinessEntity oCMBusinessEntity = retrieveBusinessEntityByServiceNameFromCache(sHomeCommunityId, sServiceName);
            if (oCMBusinessEntity != null) {
                // We already have a routine to transform a CMBusinessEntities - so use it...
                //---------------------------------------------------------------------------
                CMBusinessEntities oCMBusinessEntities = new CMBusinessEntities();
                oCMBusinessEntities.getBusinessEntity().add(oCMBusinessEntity);
                ConnectionInfoEndpointsType oConnectionInfoEndpoints = CMTransform.cmBusinessEntitiesToConnectionInfoEndpointsType(oCMBusinessEntities);
                if ((oConnectionInfoEndpoints != null) &&
                        (oConnectionInfoEndpoints.getConnectionInfoEndpoint() != null) &&
                        (oConnectionInfoEndpoints.getConnectionInfoEndpoint().size() > 0)) {
                    // We only gave it one so we should only get one back...
                    //-------------------------------------------------------
                    oConnectionInfoEndpoint = oConnectionInfoEndpoints.getConnectionInfoEndpoint().get(0);
                }
            }
        }
        return oConnectionInfoEndpoint;
    }

    /**
     * This method retrieves the business entity and Connection Information for a specific service 
     * at a specific home community.
     * 
     * @param part1 This contains the home community identification and the name of the service that the 
     *              connection info is desired.
     * @return The connection information for the service at the specified home community.
     */
    public static BusinessEntityType getBusinessEntityByServiceName(GetBusinessEntityByServiceNameRequestType part1) {
        BusinessEntityType oBusinessEntity = null;
        String sServiceName = "";
        String sHomeCommunityId = "";
        String sAssigningAuthorityId = "";
        if ((part1 != null) &&
                (part1.getHomeCommunityWithServiceName() != null) &&
                (part1.getHomeCommunityWithServiceName().getHomeCommunity() != null) &&
                (part1.getHomeCommunityWithServiceName().getHomeCommunity().getHomeCommunityId() != null) &&
                (part1.getHomeCommunityWithServiceName().getHomeCommunity().getHomeCommunityId().length() > 0) &&
                (part1.getHomeCommunityWithServiceName().getServiceName() != null) &&
                (part1.getHomeCommunityWithServiceName().getServiceName().length() > 0)) {
            sServiceName = part1.getHomeCommunityWithServiceName().getServiceName();
            sHomeCommunityId = CMTransform.formatHomeCommunityId(part1.getHomeCommunityWithServiceName().getHomeCommunity().getHomeCommunityId());
        } else if ((part1 != null) &&
                (part1.getHomeCommunityWithServiceName() != null) &&
                (part1.getHomeCommunityWithServiceName().getAssigningAuthority() != null) &&
                (part1.getHomeCommunityWithServiceName().getAssigningAuthority().getAssigningAuthorityId() != null) &&
                (part1.getHomeCommunityWithServiceName().getAssigningAuthority().getAssigningAuthorityId().length() > 0) &&
                (part1.getHomeCommunityWithServiceName().getServiceName() != null) &&
                (part1.getHomeCommunityWithServiceName().getServiceName().length() > 0)) {
            sServiceName = part1.getHomeCommunityWithServiceName().getServiceName();
            sAssigningAuthorityId = part1.getHomeCommunityWithServiceName().getAssigningAuthority().getAssigningAuthorityId();
            AssigningAuthorityHomeCommunityMappingDAO mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();
            sHomeCommunityId = mappingDao.getHomeCommunityId(sAssigningAuthorityId);
        }

        if (sHomeCommunityId != null && sHomeCommunityId.length() > 0 && sServiceName != null && sServiceName.length() > 0) {
            CMBusinessEntity oCMBusinessEntity = retrieveBusinessEntityByServiceNameFromCache(sHomeCommunityId, sServiceName);
            if (oCMBusinessEntity != null) {
                // We already have a routine to transform a CMBusinessEntities - so use it...
                //---------------------------------------------------------------------------
                CMBusinessEntities oCMBusinessEntities = new CMBusinessEntities();
                oCMBusinessEntities.getBusinessEntity().add(oCMBusinessEntity);
                BusinessEntitiesType oBusinessEntities = CMTransform.cmBusinessEntitiesToBusinessEntitiesType(oCMBusinessEntities);
                if ((oBusinessEntities != null) &&
                        (oBusinessEntities.getBusinessEntity() != null) &&
                        (oBusinessEntities.getBusinessEntity().size() > 0)) {
                    // We only gave it one so we should only get one back...
                    //-------------------------------------------------------
                    oBusinessEntity = oBusinessEntities.getBusinessEntity().get(0);
                }
            }
        }
        return oBusinessEntity;
    }

    /**
     * This retrieves the business entity set from the cache by service name.
     * 
     * @param sServiceName The name of the service.
     * @return The Business entities that are retrieved.
     */
    private static CMBusinessEntities retrieveAllBusinessEntitySetByServiceNameFromCache(String sServiceName) {
        CMBusinessEntities oCMBusinessEntities = null;

        if ((sServiceName != null) &&
                (sServiceName.length() > 0)) {
            try {
                oCMBusinessEntities = ConnectionManagerCache.getAllBusinessEntitySetByServiceName(sServiceName);
            } catch (Throwable t) {
                String sErrorMessage = "Failed to retrieve business entities.  Error: " + t.getMessage();
                log.error(sErrorMessage, t);
            }
        }

        return oCMBusinessEntities;
    }

    /**
     * This method returns the connection information for all known home communities that support the specified
     * service.
     * 
     * @param part1 The name of the service that is desired.
     * @return The connection information for each known home community that supports the specified service.
     */
    public static ConnectionInfosType getAllConnectionInfoSetByServiceName(GetAllConnectionInfoSetByServiceNameRequestType part1) {
        ConnectionInfosType oConnectionInfos = null;

        if ((part1 != null) &&
                (part1.getServiceName() != null) &&
                (part1.getServiceName().length() > 0)) {
            String sServiceName = part1.getServiceName();

            CMBusinessEntities oCMBusinessEntities = retrieveAllBusinessEntitySetByServiceNameFromCache(sServiceName);
            oConnectionInfos = CMTransform.cmBusinessEntitiesToConnectionInfosType(oCMBusinessEntities);

        }   // if ((part1 != null) &&

        return oConnectionInfos;
    }

    /**
     * This method returns the endpoint connection information for all known home communities that 
     * support the specified service.
     * 
     * @param part1 The name of the service that is desired.
     * @return The endpoint connection information for each known home community that 
     *         supports the specified service.
     */
    public static ConnectionInfoEndpointsType getAllConnectionInfoEndpointSetByServiceName(GetAllConnectionInfoEndpointSetByServiceNameRequestType part1) {
        ConnectionInfoEndpointsType oConnectionInfoEndpoints = null;

        if ((part1 != null) &&
                (part1.getServiceName() != null) &&
                (part1.getServiceName().length() > 0)) {
            String sServiceName = part1.getServiceName();

            CMBusinessEntities oCMBusinessEntities = retrieveAllBusinessEntitySetByServiceNameFromCache(sServiceName);
            oConnectionInfoEndpoints = CMTransform.cmBusinessEntitiesToConnectionInfoEndpointsType(oCMBusinessEntities);

        }   // if ((part1 != null) &&

        return oConnectionInfoEndpoints;
    }

    /**
     * This method returns the business entity and service connection information for all known 
     * home communities that support the specified service.
     * 
     * @param part1 The name of the service that is desired.
     * @return The business entity and service connection information for each known 
     *         home community that supports the specified service.
     */
    public static BusinessEntitiesType getAllBusinessEntitySetByServiceName(GetAllBusinessEntitySetByServiceNameRequestType part1) {
        BusinessEntitiesType oBusinessEntities = null;

        if ((part1 != null) &&
                (part1.getServiceName() != null) &&
                (part1.getServiceName().length() > 0)) {
            String sServiceName = part1.getServiceName();

            CMBusinessEntities oCMBusinessEntities = retrieveAllBusinessEntitySetByServiceNameFromCache(sServiceName);
            oBusinessEntities = CMTransform.cmBusinessEntitiesToBusinessEntitiesType(oCMBusinessEntities);

        }   // if ((part1 != null) &&

        return oBusinessEntities;
    }

    /**
     * This method does nothing.   The issue here is that refreshing the
     * cache now is dependent on the timestamp of the uddiConnectionInfo.xml
     * file changing.   So anytime that timestamp changes, it will automatically
     * flush.  so this method is no longer needed.
     * 
     * @param part1 The only purpose for this parameter is so that the 
     *              web service has a unique document that identifies this 
     *              operation.  The values themselves are not used.
     * @return Whether this succeeded or failed.
     */
    public static SuccessOrFailType forceRefreshUDDICache(ForceRefreshUDDICacheRequestType part1) {
        SuccessOrFailType oSuccessOrFailType = new SuccessOrFailType();
        oSuccessOrFailType.setSuccess(false);
        return oSuccessOrFailType;
    }

    /**
     * This method causes the Internal Connection service information to be refreshed.
     * 
     * @param part1 The only purpose for this parameter is so that the 
     *              web service has a unique document that identifies this 
     *              operation.  The values themselves are not used.
     * @return Whether this succeeded or failed.
     */
    public static SuccessOrFailType forceRefreshInternalConnectCache(ForceRefreshInternalConnectCacheRequestType part1) {
        SuccessOrFailType oSuccessOrFailType = new SuccessOrFailType();

        try {
            ConnectionManagerCache.forceRefreshInternalConnectCache();
            oSuccessOrFailType.setSuccess(true);
        } catch (Throwable t) {
            String sErrorMessage = "Failed to call ConnectionManagerCache.forceRefreshInternalConnectionCache()";
            log.error(sErrorMessage, t);
            oSuccessOrFailType.setSuccess(false);

        }
        return oSuccessOrFailType;
    }

    public static AcknowledgementType storeAssigningAuthorityToHomeCommunityMapping(StoreAssigningAuthorityToHomeCommunityMappingRequestType storeAssigningAuthorityToHomeCommunityMappingRequest) {
        log.debug("--Begin CMServiceHelper.storeAssigningAuthorityToHomeCommunityMapping() --");
        String success = "";
        AcknowledgementType ack = new AcknowledgementType();
        if (storeAssigningAuthorityToHomeCommunityMappingRequest == null) {
            ack.setMessage("Input mapping data not found");
            log.error("--Input mapping data not found --");
            return ack;
        }

        if (storeAssigningAuthorityToHomeCommunityMappingRequest.getAssigningAuthority() == null) {
            ack.setMessage("Set proper assigning authority");
            log.error("--Set proper assigning authority --");
            return ack;
        }

        if (storeAssigningAuthorityToHomeCommunityMappingRequest.getAssigningAuthority().getAssigningAuthorityId() == null) {
            ack.setMessage("Set proper assigning authority Id value");
            log.error("--Set proper assigning authority Id value --");
            return ack;
        }

        String assigningAuthId = storeAssigningAuthorityToHomeCommunityMappingRequest.getAssigningAuthority().getAssigningAuthorityId();

        if (storeAssigningAuthorityToHomeCommunityMappingRequest.getHomeCommunity() == null) {
            ack.setMessage("Unable to store mapping, Set home community properly");
            log.error("--Unable to store mapping, Set home community properly --");
            return ack;
        }

        if (storeAssigningAuthorityToHomeCommunityMappingRequest.getHomeCommunity().getHomeCommunityId() == null) {
            ack.setMessage("Unable to store mapping, Set proper Home Community Id");
            log.error("--Unable to store mapping, Set proper Home Community Id --");
            return ack;
        }
        String homeCommId = CMTransform.formatHomeCommunityId(storeAssigningAuthorityToHomeCommunityMappingRequest.getHomeCommunity().getHomeCommunityId());
        AssigningAuthorityHomeCommunityMappingDAO mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();
        boolean result = mappingDao.storeMapping(homeCommId, assigningAuthId);
        if (result) {
            success = "Successfully stored mapping";
            log.debug("Successfully stored mapping");
        } else {
            success = "Map not stored similar map already exists in the repository";
            log.warn("Map not stored similar map already exists in the repository");
        }
        ack.setMessage(success);
        log.debug("--End CMServiceHelper.storeAssigningAuthorityToHomeCommunityMapping() --");
        return ack;
    }

    public static GetAssigningAuthoritiesByHomeCommunityResponseType getAssigningAuthoritiesByHomeCommunity(GetAssigningAuthoritiesByHomeCommunityRequestType requestType) {
        log.debug("-- Begin CMServiceHelper.getAssigningAuthoritiesByHomeCommunity() --");
        GetAssigningAuthoritiesByHomeCommunityResponseType responseType = null;
        if (requestType == null) {
            return null;
        }
        HomeCommunityType hcType = requestType.getHomeCommunity();
        if (hcType == null) {
            return null;
        }
        String hcId = CMTransform.formatHomeCommunityId(hcType.getHomeCommunityId());
        if (hcId != null && !hcId.equals("")) {
            AssigningAuthorityHomeCommunityMappingDAO mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();
            responseType = new GetAssigningAuthoritiesByHomeCommunityResponseType();
            AssigningAuthoritiesType aaType = new AssigningAuthoritiesType();
            List<String> aaList = mappingDao.getAssigningAuthoritiesByHomeCommunity(hcId);
            if(aaList!=null && aaList.size()>0)
            {
                AssigningAuthorityType aayType = null;   
                for(String sAA : aaList)
                {
                    log.debug("Assiging Authorities for ");
                    aayType = new AssigningAuthorityType();
                    aayType.setAssigningAuthorityId(sAA);
                    aaType.getAssigningAuthority().add(aayType);    
                }
            }
            responseType.setAssigningAuthoritiesId(aaType);
        }
        log.debug("-- Begin CMServiceHelper.getAssigningAuthoritiesByHomeCommunity() --");
        return responseType;
    }

    /**
     * 
     * @param requestType
     * @return GetHomeCommunityByAssigningAuthorityResponseType
     */
    public static GetHomeCommunityByAssigningAuthorityResponseType getHomeCommunityByAssigningAuthority(GetHomeCommunityByAssigningAuthorityRequestType requestType) {
        if (requestType == null) {
            return null;
        }
        if (requestType.getAssigningAuthorities() == null) {
            return null;
        }
        if (requestType.getAssigningAuthorities().getAssigningAuthorityId() == null) {
            return null;
        }
        String assigningAuthId = requestType.getAssigningAuthorities().getAssigningAuthorityId();
        AssigningAuthorityHomeCommunityMappingDAO mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();
        GetHomeCommunityByAssigningAuthorityResponseType resType = new GetHomeCommunityByAssigningAuthorityResponseType();
        HomeCommunityType hc = new HomeCommunityType();
        hc.setDescription("");
        hc.setHomeCommunityId(mappingDao.getHomeCommunityId(assigningAuthId));
        hc.setName("");
        resType.setHomeCommunity(hc);
        return resType;
    }

    /**
     * 
     * @param requestType
     * @return GetHomeCommunityByAssigningAuthorityResponseType
     */
    public static EPRType getConnectionInfoEndpontFromNhinTarget(GetConnectionInfoEndpontFromNhinTargetType message) {
        ConnectionInfoEndpointType oConnectionInfoEndpoint = null;
        EPRType endPtRef = null;

        if (message != null &&
                message.getNhinTargetSystem() != null) {
            if (message.getNhinTargetSystem().getEpr() != null) {
                // Just echo back the Endpoint Reference that was sent into this method
                endPtRef = message.getNhinTargetSystem().getEpr();
            } else if (NullChecker.isNotNullish(message.getNhinTargetSystem().getUrl()) &&
                    NullChecker.isNotNullish(message.getServiceName())) {
                // Get the Endpoint Reference based on URL and Service Name
                endPtRef = CMTransform.createEPR(message.getServiceName(), message.getNhinTargetSystem().getUrl());
            } else if (message.getNhinTargetSystem().getHomeCommunity() != null &&
                    NullChecker.isNotNullish(message.getNhinTargetSystem().getHomeCommunity().getHomeCommunityId()) &&
                    NullChecker.isNotNullish(message.getServiceName())) {
                // Get the Endpoint Reference based on Home Community Id and Service Name
                CMBusinessEntity oCMBusinessEntity = retrieveBusinessEntityByServiceNameFromCache(message.getNhinTargetSystem().getHomeCommunity().getHomeCommunityId(), message.getServiceName());
                
                if (oCMBusinessEntity != null) {
                    CMBusinessEntities oCMBusinessEntities = new CMBusinessEntities();
                    oCMBusinessEntities.getBusinessEntity().add(oCMBusinessEntity);
                    ConnectionInfoEndpointsType oConnectionInfoEndpoints = CMTransform.cmBusinessEntitiesToConnectionInfoEndpointsType(oCMBusinessEntities);
                    
                    if ((oConnectionInfoEndpoints != null) &&
                            NullChecker.isNotNullish(oConnectionInfoEndpoints.getConnectionInfoEndpoint())) {
                        oConnectionInfoEndpoint = oConnectionInfoEndpoints.getConnectionInfoEndpoint().get(0);
                        
                        if (oConnectionInfoEndpoint.getServiceConnectionInfoEndpoints() != null &&
                                NullChecker.isNotNullish(oConnectionInfoEndpoint.getServiceConnectionInfoEndpoints().getServiceConnectionInfoEndpoint()) &&
                                oConnectionInfoEndpoint.getServiceConnectionInfoEndpoints().getServiceConnectionInfoEndpoint().get(0).getEPR() != null) {
                            endPtRef = oConnectionInfoEndpoint.getServiceConnectionInfoEndpoints().getServiceConnectionInfoEndpoint().get(0).getEPR();
                        }
                    }
                }
            }
        }

        return endPtRef;
    }

    public static UrlSetType getUrlSetFromNhinTargetCommunities(GetUrlSetByServiceNameType getConnectionInfoEndpontFromNhinTargetRequest) {
        UrlSetType urlSet = new UrlSetType();
        log.info("In getUrlSetFromNhinTargetCommunities...");

        try {
            CMUrlInfos urlInfoList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(getConnectionInfoEndpontFromNhinTargetRequest.getNhinTargetCommunities(), getConnectionInfoEndpontFromNhinTargetRequest.getService());

            if (urlInfoList != null &&
                    urlInfoList.getUrlInfo() != null) {
                for (CMUrlInfo entry : urlInfoList.getUrlInfo()) {
                    urlSet.getUrl().add(entry.getUrl());
                }
            }
        } catch (ConnectionManagerException ex) {
            log.error("Failed to retrieve URL Set from Nhin Target Community");
            return null;
        }

        return urlSet;
    }
}
