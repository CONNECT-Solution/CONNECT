/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.common.connectionmanager;

import gov.hhs.fha.nhinc.common.connectionmanager.dao.AssigningAuthorityHomeCommunityMappingDAO;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.AssigningAuthorityIdType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.EmptyParameterType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.EndpointURLType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetAdapterEndpointURLRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetAssigningAuthoritiesByHomeCommunityResponseType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetBusinessEntityByServiceNameRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetBusinessEntitySetByServiceNameRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetDefaultEndpointURLByServiceNameRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetEndpointURLFromNhinTargetCommunitiesRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetEndpointURLFromNhinTargetCommunitiesResponseType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetEndpointURLFromNhinTargetRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.HomeCommunityIdListType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.HomeCommunityIdType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.ServiceNameType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.SuccessOrFailType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.UrlInfo;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;

import java.util.List;
import java.util.Set;

import javax.xml.ws.BindingType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uddi.api_v3.BusinessDetail;
import org.uddi.api_v3.BusinessEntity;

/**
 *
 * @author akong
 */
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhincComponentConnectionManager implements gov.hhs.fha.nhinc.nhinccomponentconnectionmanager.NhincComponentConnectionManagerPortType {

    private static final Logger LOG = LoggerFactory.getLogger(NhincComponentConnectionManager.class);

    /**
     * This method will return a list of all business entities that are known by the connection manager.
     *
     * @param request The only purpose for this parameter is so that the web service has a unique document that identifies
     *            this operation. The values themselves are not used.
     * @return The BusinessDetail which contains a list of all business entities known by the connection manager.
     */
    public BusinessDetail getAllBusinessEntities(EmptyParameterType emptyRequest) {
        BusinessDetail bDetail = new BusinessDetail();
        try {
            List<BusinessEntity> businessEntityList = ConnectionManagerCache.getInstance().getAllBusinessEntities();
            if (businessEntityList != null) {
                bDetail.getBusinessEntity().addAll(businessEntityList);
            }
        } catch (ConnectionManagerException cme) {
            LOG.error("Failed to invoke getAllBusinessEntities", cme);
        }

        return bDetail;
    }

    /**
     * This class returns the business entity information associated with the specified home community ID.
     *
     * @param sHomeCommunityId The home community ID that is being searched for.
     * @return the business entity information for the specified home community.
     */
    public BusinessEntity getBusinessEntity(HomeCommunityIdType homeCommunity) {
        BusinessEntity bEntity = new BusinessEntity();;
        try {
            bEntity = ConnectionManagerCache.getInstance().getBusinessEntity(homeCommunity.getValue());
        } catch (ConnectionManagerException cme) {
            LOG.error("Failed to invoke getBusinessEntity", cme);
        }

        return bEntity;
    }

    /**
     * This method returns a list of business entity information for the set of home communities.
     *
     * @param saHomeCommunityId The set of home communities to be retrieved.
     * @return The BusinessDetail containing the list of business entities found.
     */
    public BusinessDetail getBusinessEntitySet(HomeCommunityIdListType homeCommunityIdList) {
        BusinessDetail bDetail = new BusinessDetail();
        try {
            Set<BusinessEntity> businessEntitySet = ConnectionManagerCache.getInstance().getBusinessEntitySet(
                    homeCommunityIdList.getHomeCommunityId());
            if (businessEntitySet != null) {
                bDetail.getBusinessEntity().addAll(businessEntitySet);
            }
        } catch (ConnectionManagerException cme) {
            LOG.error("Failed to invoke getBusinessEntitySet", cme);
        }

        return bDetail;
    }

    /**
     * This method retrieves the business entity that contains the specific home community and service name.
     *
     * @param request The request containing the home community ID and the service name
     * @return The Business Entity information along with only the requested service. if the service is not found, then
     *         null is returned.
     */
    public BusinessEntity getBusinessEntityByServiceName(GetBusinessEntityByServiceNameRequestType request) {
        BusinessEntity bEntity = new BusinessEntity();
        try {
            bEntity = ConnectionManagerCache.getInstance().getBusinessEntityByServiceName(request.getHomeCommunityId(),
                    request.getServiceName());
        } catch (ConnectionManagerException cme) {
            LOG.error("Failed to invoke getBusinessEntityByServiceName", cme);
        }

        return bEntity;
    }

    /**
     * This method returns the url for a specified service and home community id .
     *
      *@param request The request containing the home community ID and the service name
     * @return The URL for only the requested service at the specified home community. If the service is not found, then
     *         null is returned.
     */
    public EndpointURLType getDefaultEndpointURLByServiceName(GetDefaultEndpointURLByServiceNameRequestType request) {
        String endpointUrl = null;
        try {
            endpointUrl = ConnectionManagerCache.getInstance().getDefaultEndpointURLByServiceName(
                    request.getHomeCommunityId(), request.getServiceName());
        } catch (ConnectionManagerException cme) {
            LOG.error("Failed to invoke getEndpointURLByServiceName", cme);
        }

        EndpointURLType response = new EndpointURLType();
        response.setValue(endpointUrl);

        return response;
    }

    /**
     * This method returns a local url for a specified service.
     *
     * @param sUniformServiceName The name of the service to locate.
     * @return The URL for only the requested service at the local home community. If the service is not found, then
     *         null is returned.
     */
    public EndpointURLType getInternalEndpointURLByServiceName(ServiceNameType serviceName) {
        String endpointUrl = null;
        try {
            endpointUrl = ConnectionManagerCache.getInstance().getInternalEndpointURLByServiceName(serviceName.getValue());
        } catch (ConnectionManagerException cme) {
            LOG.error("Failed to invoke getLocalEndpointURLByServiceName", cme);
        }

        EndpointURLType response = new EndpointURLType();
        response.setValue(endpointUrl);

        return response;
    }

    /**
     * This method retrieves the URL from the contents of the NhinTargetSystem type. It will first check to see if the
     * EPR (Endpoint Reference) is already provided, if so it will extract the URL from the EPR and return it to the
     * caller. If the EPR is not provided it will check if the URL field is provided, if so it will return the URL to
     * the caller. If neither an EPR or URL are provided this method will use the home community id and service name
     * provided to lookup the URL for that service at that particular home community.
     *
     * @param request The request containing the target system information for the community being looked up and the service name
     * @return The URL to the requested service.
     */
    public EndpointURLType getEndpointURLFromNhinTarget(GetEndpointURLFromNhinTargetRequestType request) {
        String endpointUrl = null;
        try {
            endpointUrl = ConnectionManagerCache.getInstance().getEndpointURLFromNhinTarget(
                    request.getNhinTargetSystem(), request.getServiceName());
        } catch (ConnectionManagerException cme) {
            LOG.error("Failed to invoke getEndpointURLFromNhinTarget", cme);
        }

        EndpointURLType response = new EndpointURLType();
        response.setValue(endpointUrl);

        return response;
    }

    /**
     * This method retrieves a set of unique URLs from the contents of the NhinTargetCommunities type. For each
     * NhinTargetCommunity type it will first check if a Home Community Id is specified. If so then it will add the URL
     * for the specified service for that home community to the List of URLs. Next it will check if a region (state) is
     * specified. If so it will obtain a list of URLs for that that service for all communities in the specified state.
     * Next it will check if a list is specified (this feature is not implemented). If there are no
     * NhinTargetCommunities specified it will return the list of URLs for the entire NHIN for that service.
     *
     * @param request The request containing the target system information for the community being looked up and the service name
     * @return A response containing the set of URLs for the requested service and targets.
     */
    public GetEndpointURLFromNhinTargetCommunitiesResponseType getEndpointURLFromNhinTargetCommunities(
            GetEndpointURLFromNhinTargetCommunitiesRequestType request) {

        GetEndpointURLFromNhinTargetCommunitiesResponseType response = new GetEndpointURLFromNhinTargetCommunitiesResponseType();
        try {
            List<UrlInfo> urlInfoList = ConnectionManagerCache.getInstance().getEndpointURLFromNhinTargetCommunities(
                    request.getNhinTargetCommunities(), request.getServiceName());

            String hcid, url;
            UrlInfoType urlInfoType;
            for (UrlInfo urlInfo : urlInfoList) {
                hcid = urlInfo.getHcid();
                url = urlInfo.getUrl();
                if (NullChecker.isNotNullish(hcid) && NullChecker.isNotNullish(url)) {
                    urlInfoType = new UrlInfoType();
                    urlInfoType.setUrl(url);
                    urlInfoType.setId(hcid);
                    response.getURLInfos().add(urlInfoType);
                }
            }

        } catch (ConnectionManagerException cme) {
            LOG.error("Failed to invoke getEndpointURLFromNhinTargetCommunities", cme);
        }

        return response;
    }

    /**
     * This method retrieves all the business entities that contains the given service name and home community id
     *
     * @param request The request containing the home community id and service name
     * @return A BusinessDetail containing the list of business entities
     */
    public BusinessDetail getBusinessEntitySetByServiceName(GetBusinessEntitySetByServiceNameRequestType request) {
        BusinessDetail bDetail = new BusinessDetail();
        try {
            Set<BusinessEntity> businessEntitySet = ConnectionManagerCache.getInstance().getBusinessEntitySetByServiceName(request.getHomeCommunityId(), request.getServiceName());
            if (businessEntitySet != null) {
                bDetail.getBusinessEntity().addAll(businessEntitySet);
            }
        } catch (ConnectionManagerException cme) {
            LOG.error("Failed to invoke getBusinessEntitySetByServiceName", cme);
        }

        return bDetail;
    }

    /**
     * This method retrieves all the business entities that contains the given service name
     *
     * @param sUniformServiceName The service name to lookup
     * @return A BusinessDetail containing the list of business entities
     */
    public BusinessDetail getAllBusinessEntitySetByServiceName(ServiceNameType serviceName) {
        BusinessDetail bDetail = new BusinessDetail();
        try {
            Set<BusinessEntity> businessEntitySet = ConnectionManagerCache.getInstance().getAllBusinessEntitySetByServiceName(serviceName.getValue());
            if (businessEntitySet != null) {
                bDetail.getBusinessEntity().addAll(businessEntitySet);
            }
        } catch (ConnectionManagerException cme) {
            LOG.error("Failed to invoke getAllBusinessEntitySetByServiceName", cme);
        }

        return bDetail;
    }

    /**
     * This method returns the local adapter endpoint url based on the adapter level and service name
     *
     * @param request The request containing the service name and the adapter level
     * @return The adapter endpoint url
     */
    public EndpointURLType getAdapterEndpointURL(GetAdapterEndpointURLRequestType request) {
        String endpointUrl = null;
        try {
            ADAPTER_API_LEVEL adapterLevel = ADAPTER_API_LEVEL.valueOf(request.getAdapterLevel());
            endpointUrl = ConnectionManagerCache.getInstance().getAdapterEndpointURL(request.getServiceName(), adapterLevel);
        } catch (Exception e) {
            LOG.error("Failed to invoke getAdapterEndpointURL", e);
        }

        EndpointURLType response = new EndpointURLType();
        response.setValue(endpointUrl);

        return response;
    }

    /**
     * This method causes the UDDI service information to be refreshed.
     *
     * @param request The only purpose for this parameter is so that the web service has a unique document that identifies
     *            this operation. The values themselves are not used.
     * @return Whether this succeeded or failed.
     */
    public SuccessOrFailType forceRefreshUDDICache(EmptyParameterType emptyRequest) {
        SuccessOrFailType response = new SuccessOrFailType();
        response.setSuccess(true);
        try {
            ConnectionManagerCache.getInstance().forceRefreshUDDICache();
        } catch (ConnectionManagerException cme) {
            LOG.error("Failed to invoke forceRefreshUDDICache", cme);
            response.setSuccess(false);
        }

        return response;
    }

    /**
     * This method causes the Internal Connection service information to be refreshed.
     *
     * @param request The only purpose for this parameter is so that the web service has a unique document that identifies
     *            this operation. The values themselves are not used.
     * @return Whether this succeeded or failed.
     */
    public SuccessOrFailType forceRefreshInternalConnectCache(EmptyParameterType emptyRequest) {
        SuccessOrFailType response = new SuccessOrFailType();
        response.setSuccess(true);
        try {
            ConnectionManagerCache.getInstance().forceRefreshInternalConnectCache();
        } catch (ConnectionManagerException cme) {
            LOG.error("Failed to invoke forceRefreshInternalConnectCache", cme);
            response.setSuccess(false);
        }

        return response;
    }

    /**
     * This method retrieves the assigning authorities of a given home community id
     *
     * @param homeCommunityId The hcid to be used for lookup
     * @return A response containing a list of assigning authorities associated with the hcid
     */
    public GetAssigningAuthoritiesByHomeCommunityResponseType getAssigningAuthoritiesByHomeCommunity(HomeCommunityIdType homeCommunityId) {

        GetAssigningAuthoritiesByHomeCommunityResponseType response = new GetAssigningAuthoritiesByHomeCommunityResponseType();
        if (NullChecker.isNullish(homeCommunityId.getValue())) {
            return null;
        }

        AssigningAuthorityHomeCommunityMappingDAO mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();
        List<String> aaList = mappingDao.getAssigningAuthoritiesByHomeCommunity(homeCommunityId.getValue());
        response.getAssigningAuthoritiesId().addAll(aaList);

        return response;
    }

    /**
     * This method retrieves the home community id of the given assigning authority
     *
     * @param assigningAuthorityId The assigning authority id to be used for lookup
     * @return The hcid of the assigning authority
     */
    public HomeCommunityIdType getHomeCommunityByAssigningAuthority(AssigningAuthorityIdType assigningAuthorityId) {

        String homeCommunityId;
        if (NullChecker.isNullish(assigningAuthorityId.getValue())) {
            return null;
        }

        AssigningAuthorityHomeCommunityMappingDAO mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();
        homeCommunityId = mappingDao.getHomeCommunityId(assigningAuthorityId.getValue());

        HomeCommunityIdType response = new HomeCommunityIdType();
        response.setValue(homeCommunityId);

        return response;
    }

}
