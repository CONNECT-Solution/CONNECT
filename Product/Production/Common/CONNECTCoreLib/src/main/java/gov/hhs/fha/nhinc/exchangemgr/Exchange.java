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
package gov.hhs.fha.nhinc.exchangemgr;

import java.util.Optional;

import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.UrlInfo;
import gov.hhs.fha.nhinc.exchange.directory.OrganizationType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.List;
import java.util.Set;

/**
 * @author tjafri
 * @param <T>
 */
public interface Exchange<T> {

    public void refreshExchangeCacheIfRequired() throws ExchangeManagerException;

    /**
     * This method will return a list of all organizations that are known by the Connection Manager(thats is, listed in
     * exchangeInfo.xml).
     *
     * @return The list of all exchanges known by the connection manager.
     * @throws gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException
     */
    public List<OrganizationType> getAllOrganizations() throws ExchangeManagerException;

    /**
     * This class returns the organization information associated with the specified home community ID.
     *
     * @param hcid The home community ID that is being searched for.
     * @return the organization information for the specified home community.
     * @throws gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException
     */
    public OrganizationType getOrganization(String hcid) throws ExchangeManagerException;

    public OrganizationType getOrganization(String exchangeName, String hcid) throws ExchangeManagerException;

    /**
     * Returns the name of the organization for the given home community id.
     *
     * @param hcid
     * @return
     * @throws gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException
     */
    public String getOrganizationName(String hcid) throws ExchangeManagerException;

    /**
     * This method returns the Organization information for the list of home communities.
     *
     * @param hcids
     * @param exchangeName
     * @return The Organizations found.
     * @throws gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException
     */
    public Set<OrganizationType> getOrganizationSet(List<String> hcids, String exchangeName)
        throws ExchangeManagerException;

    /**
     * TODO ask and fix this method This method retrieves the Organizations that contains the specific home community
     * and service name. Also note: This currently does not deal with version. If there are multiple versions of the
     * same service, this will return the first one it sees in the list of services. As always, it will always first
     * look in the InternalConnectionInfo cache for the business entity. If it finds the business entity there, it will
     * not look in the UDDI cache. (This means that if the internal cache contains the given business entity, but it
     * does not contain the requested service, it will behave as if the service does not exist - regardless of whether
     * it is in the UDDI cache or not.
     *
     * @param hcid The home community ID of the gateway that is being looked up.
     * @param sUniformServiceName The name of the service to locate.
     * @param exchangeName
     * @return Organization information along with only the requested service. if the service is not found, then null is
     *         returned.
     * @throws gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException
     */
    public OrganizationType getOrganizationByServiceName(String hcid, String sUniformServiceName, String exchangeName)
        throws ExchangeManagerException;

    /**
     * This method retrieves a set of Organization that contains the set of home communities and service name. Also
     * note: This currently does not deal with version. If there are multiple versions of the same service, this will
     * return the first one it sees in the list of services. As always, it will always first look in the
     * InternalConnectionInfo cache for the business entity. If it finds the business entity there, it will not look in
     * the UDDI cache. (This means that if the internal cache contains the given business entity, but it does not
     * contain the requested service, it will behave as if the service does not exist - regardless of whether it is in
     * the UDDI cache or not.
     *
     * @param sahcid The home community IDs of the gateways that is being looked up.
     * @param sUniformServiceName The name of the service to locate.
     * @param exchangeName
     * @return The Business Entity information along with only the requested service. If the service is not found, it
     *         will not be returned even if the business entity information exists.
     * @throws gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException
     */
    public Set<OrganizationType> getOrganizationSetByServiceNameForHCID(List<String> sahcid,
        String sUniformServiceName, String exchangeName) throws ExchangeManagerException;

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
     * @throws gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException
     */
    public Set<OrganizationType> getAllOrganizationSetByServiceName(String sUniformServiceName, String exchangeName)
        throws ExchangeManagerException;

    public List<NhincConstants.UDDI_SPEC_VERSION> getSpecVersions(String hcid,
        NhincConstants.NHIN_SERVICE_NAMES serviceName);

    /**
     * This method returns highest version url for a specified service and home community id in an exchange.
     *
     * @param exchangeName
     * @param hcid The home community ID of the gateway that is being looked up.
     * @param sUniformServiceName The name of the service to locate.
     * @return The URL for only the requested service at the specified home community. If the service is not found, then
     *         null is returned.
     * @throws gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException
     */
    public String getDefaultEndpointURL(String hcid, String sUniformServiceName, String exchangeName)
        throws ExchangeManagerException;

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
     * @throws gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException
     */
    public String getEndpointURLFromNhinTarget(NhinTargetSystemType targetSystem, String serviceName)
        throws ExchangeManagerException;

    /**
     * This method retrieves a set of unique URLs from the contents of the NhinTargetCommunities type. For each
     * NhinTargetCommunity type it will first check if a Home Community Id is specified. If so then it will add the URL
     * for the specified service for that home community to the List of URLs. Next it will check if a region (state) is
     * specified. If so it will obtain a list of URLs for that that service for all communities in the specified state.
     * Next it will check if a list is specified (this feature is not implemented). If there is an exchange specified in
     * NhinTargetCommunities, it will return list of URLs for that service in that particular exchange If there are no
     * NhinTargetCommunities specified it will return the list of URLs for that service in a default exchange.
     *
     * @param targets List of targets to get URLs for.
     * @param serviceName The name of the service to locate who URL is being requested.
     * @return The set of URLs for the requested service and targets.
     * @throws gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException
     */
    public List<UrlInfo> getEndpointURLFromNhinTargetCommunities(NhinTargetCommunitiesType targets, String serviceName)
        throws ExchangeManagerException;

    public String getDefaultExchange();

    /**
     * @param sHomeCommunityId
     * @param exchangeName
     * @param sServiceName
     * @param spec_level
     * @return
     * @throws gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException
     */
    public String getEndpointURL(String sHomeCommunityId, String sServiceName, T spec_level)
        throws ExchangeManagerException;

    public String getEndpointURL(String sServiceName, T spec_level) throws ExchangeManagerException;

    /**
     * This method returns a local url for a specified service using default exchange.
     *
     * @param sServiceName
     * @return The URL for only the requested service at the local home community. If the service is not found, then
     *         null is returned.
     * @throws gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException
     */
    public String getEndpointURL(String sServiceName) throws ExchangeManagerException;

    /*
     * This method returns a local url for a specified service.
     *
     * @param exchangeName @param sServiceName @return The URL for only the requested service at the local home
     * community. If the service is not found, then null is returned. @throws
     * gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException
     */
    public String getEndpointURL(String sServiceName, String exchangeName) throws ExchangeManagerException;

    public String getEndpointURL(String sServiceName, T apiSpec, String exchangeName) throws ExchangeManagerException;

    public String getEndpointURL(String sHomeCommunityId, String sServiceName, T apiSpec, String exchangeName)
        throws ExchangeManagerException;

    public List<OrganizationType> getAllOrganizations(String exchangeName) throws ExchangeManagerException;

    /**
     * Retrieve SNI Name based on exchangeName if exist.
     *
     * @param exchangeName exchangeName
     * @return SNI Name if exist. Otherwise, return optional element.
     */
    public Optional<String> getSNIServerName(String exchangeName);
}
