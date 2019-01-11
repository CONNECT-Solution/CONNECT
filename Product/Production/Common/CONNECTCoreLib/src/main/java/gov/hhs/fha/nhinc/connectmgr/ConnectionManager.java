/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.UDDI_SPEC_VERSION;
import java.util.List;
import java.util.Set;
import org.uddi.api_v3.BusinessEntity;

/**
 *
 * @author msw
 */
public interface ConnectionManager {

    /**
     * This method will return a list of all business entities that are known by the connection manager.
     *
     * @return The list of all business entities known by the connection manager.
     * @throws ConnectionManagerException
     */
    public List<BusinessEntity> getAllBusinessEntities() throws ConnectionManagerException;

    /**
     * This class returns the business entity information associated with the specified home community ID.
     *
     * @param sHomeCommunityId The home commuinity ID that is being searched for.
     * @return the business entity information for the specified home community.
     * @throws gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException
     */
    public BusinessEntity getBusinessEntity(String sHomeCommunityId) throws ConnectionManagerException;

    /**
     * Returns the name of the entity for the given home community id.
     * 
     * @param homeCommunityId
     * @return
     * @throws ConnectionManagerException
     */
    public String getBusinessEntityName(String homeCommunityId) throws ConnectionManagerException;

    /**
     * This method returns the business entity information for the set of home communities.
     *
     * @param saHomeCommunityId The set of home communities to be retrieved.
     * @return The business entities found.
     * @throws gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException
     */
    public Set<BusinessEntity> getBusinessEntitySet(List<String> saHomeCommunityId) throws ConnectionManagerException;

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
            throws ConnectionManagerException;

    public BusinessEntity getBusinessEntityByHCID(String sHomeCommunityId) throws ConnectionManagerException;

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
            String sUniformServiceName) throws ConnectionManagerException;

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
            throws ConnectionManagerException;

    public List<UDDI_SPEC_VERSION> getSpecVersions(String homeCommunityId,
            NhincConstants.NHIN_SERVICE_NAMES serviceName);

    public String getAdapterEndpointURL(String sHomeCommunityId, String sServiceName, ADAPTER_API_LEVEL level)
            throws ConnectionManagerException;

    public String getAdapterEndpointURL(String sServiceName, ADAPTER_API_LEVEL level) throws ConnectionManagerException;

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
            throws ConnectionManagerException;

    /**
     * This method returns url for a specified service and home community id .
     *
     * @param sHomeCommunityId The home community ID of the gateway that is being looked up.
     * @param sUniformServiceName The name of the service to locate.
     * @param version The version of the service to locate.
     * @return The URL for only the requested service at the specified home community. If the service is not found, then
     *         null is returned.
     * @throws ConnectionManagerException
     */
    public String getEndpointURLByServiceNameSpecVersion(String sHomeCommunityId, String sUniformServiceName,
            UDDI_SPEC_VERSION version) throws ConnectionManagerException;

    /**
     * This method returns a local url for a specified service.
     *
     * @param sUniformServiceName The name of the service to locate.
     * @return The URL for only the requested service at the local home community. If the service is not found, then
     *         null is returned.
     * @throws ConnectionManagerException
     */
    public String getInternalEndpointURLByServiceName(String sUniformServiceName) throws ConnectionManagerException;

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
            throws ConnectionManagerException;

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
            throws ConnectionManagerException;

}
