/*
 * Copyright (c) 2009-2018, United States Government, as represented by the Secretary of Health and Human Services.
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

import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.UrlInfo;
import gov.hhs.fha.nhinc.exchange.directory.EndpointType;
import gov.hhs.fha.nhinc.exchange.directory.OrganizationType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tjafri
 * @param <T>
 */
public abstract class AbstractExchangeManager<T> implements Exchange<T> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractExchangeManager.class);

    protected abstract Map<String, Map<String, OrganizationType>> getCache() throws ExchangeManagerException;

    protected abstract String getApiSpec(T apispec);

    protected abstract T getApiSpecEnum(String version);

    @Override
    public List<OrganizationType> getAllOrganizations() throws ExchangeManagerException {
        refreshExchangeCacheIfRequired();
        List<OrganizationType> orgList = new ArrayList<>();
        for (Map<String, OrganizationType> aMap : getCache().values()) {
            orgList.addAll(aMap.values());
        }
        return orgList;
    }

    @Override
    public String getOrganizationName(String hcid) throws ExchangeManagerException {
        OrganizationType org = getOrganization(hcid);
        return org != null ? org.getName() : null;
    }

    @Override
    public Set<OrganizationType> getOrganizationSet(List<String> hcids) throws ExchangeManagerException {
        if (hcids == null) {
            return null;
        }
        Set<OrganizationType> set = new HashSet<>();
        for (String hcid : hcids) {
            OrganizationType org = getOrganization(hcid);
            if (org != null) {
                set.add(org);
            }
        }
        return set;
    }

    @Override
    public OrganizationType getOrganizationByServiceName(String hcid, String sUniformServiceName) throws
        ExchangeManagerException {
        OrganizationType org = getOrganization(hcid);
        if (null != org && ExchangeManagerHelper.organizationHasService(org, sUniformServiceName)) {
            return org;
        }
        return null;
    }

    @Override
    public Set<OrganizationType> getOrganizationSetByServiceNameForHCID(List<String> hcids,
        String sUniformServiceName) throws ExchangeManagerException {
        if (hcids == null) {
            return null;
        }
        Set<OrganizationType> set = new HashSet<>();
        for (String hcid : hcids) {
            OrganizationType org = getOrganization(hcid);
            if (org != null && ExchangeManagerHelper.organizationHasService(org, sUniformServiceName)) {
                set.add(org);
            }
        }
        return set;
    }

    @Override
    public Set<OrganizationType> getAllOrganizationSetByServiceName(String sUniformServiceName) throws
        ExchangeManagerException {
        Set<OrganizationType> set = null;
        List<OrganizationType> orgList = getAllOrganizations();
        if (CollectionUtils.isNotEmpty(orgList)) {
            set = new HashSet<>();
            for (OrganizationType org : orgList) {
                if (null != org && ExchangeManagerHelper.organizationHasService(org, sUniformServiceName)) {
                    set.add(org);
                }
            }
        }
        return set;
    }

    @Override
    public List<NhincConstants.UDDI_SPEC_VERSION> getSpecVersions(String hcid,
        NhincConstants.NHIN_SERVICE_NAMES serviceName) {
        List<NhincConstants.UDDI_SPEC_VERSION> specVersions = null;
        try {
            OrganizationType org = getOrganization(hcid);
            specVersions = new ArrayList<>();
            if (null != org && org.getEndpointList() != null && CollectionUtils.isNotEmpty(org.getEndpointList().
                getEndpoint())) {
                for (EndpointType epType : org.getEndpointList().getEndpoint()) {
                    if (ExchangeManagerHelper.hasService(epType, serviceName.getUDDIServiceName())) {
                        specVersions = ExchangeManagerHelper.getSpecVersions(epType);
                    }
                }
            }

        } catch (ExchangeManagerException ex) {
            LOG.error("Unable toread the Exchange information {}", ex.getLocalizedMessage(), ex);
        }
        return specVersions;
    }

    @Override
    public String getDefaultEndpointURL(String exchangeName, String hcid, String sUniformServiceName)
        throws ExchangeManagerException {
        OrganizationType org = this.getOrganization(exchangeName, hcid);
        if (null == org) {
            return "";
        }
        //lookup the service url based on highest spec version
        if (org.getEndpointList() != null && CollectionUtils.isNotEmpty(org.getEndpointList().getEndpoint())) {
            for (EndpointType epType : org.getEndpointList().getEndpoint()) {
                if (ExchangeManagerHelper.hasService(epType, sUniformServiceName)) {
                    NhincConstants.UDDI_SPEC_VERSION highestVersion = ExchangeManagerHelper.getHighestUDDISpecVersion(
                        ExchangeManagerHelper.
                            getSpecVersions(epType));
                    return ExchangeManagerHelper.getEndpointURLBySpecVersion(epType.getEndpointConfigurationList(),
                        highestVersion);
                }
            }
        }
        return "";
    }

    @Override
    public abstract String getEndpointURL(String hcid, String sServiceName, T api_spec) throws ExchangeManagerException;

    @Override
    public String getEndpointURL(String sUniformServiceName) throws
        ExchangeManagerException {
        String sHomeCommunityId;
        String sEndpointURL = null;
        sHomeCommunityId = ExchangeManagerHelper.getHomeCommunityFromPropFile();

        if (StringUtils.isNotEmpty(sHomeCommunityId)) {
            sEndpointURL = getDefaultEndpointURL(null, sHomeCommunityId, sUniformServiceName);
        }
        return sEndpointURL;
    }

    @Override
    public String getEndpointURL(String sServiceName, T api_spec) throws
        ExchangeManagerException {
        return getEndpointURL(ExchangeManagerHelper.getHomeCommunityFromPropFile(), sServiceName, api_spec);
    }

    @Override
    public String getEndpointURLFromNhinTarget(NhinTargetSystemType targetSystem, String serviceName) throws
        ExchangeManagerException {
        String sEndpointURL = null;

        if (targetSystem != null) {
            if (targetSystem.getEpr() != null) {
                // Extract the URL from the Endpoint Reference
                LOG.debug("Attempting to look up URL by EPR");
                if (targetSystem.getEpr() != null && targetSystem.getEpr().getAddress() != null
                    && StringUtils.isNotEmpty(targetSystem.getEpr().getAddress().getValue())) {
                    sEndpointURL = targetSystem.getEpr().getAddress().getValue();
                }
            } else if (StringUtils.isNotEmpty(targetSystem.getUrl())) {
                // Echo back the URL provided
                LOG.debug("Attempting to look up URL by URL");
                sEndpointURL = targetSystem.getUrl();
            } else if (targetSystem.getHomeCommunity() != null
                && StringUtils.isNotEmpty(targetSystem.getHomeCommunity().getHomeCommunityId())
                && StringUtils.isNotEmpty(serviceName)) {
                // Get the URL based on Home Community Id and Service Name
                String homeCommunityId = HomeCommunityMap
                    .formatHomeCommunityId(targetSystem.getHomeCommunity().getHomeCommunityId());
                final String userSpecVersion = targetSystem.getUseSpecVersion();
                if (!StringUtils.isEmpty(userSpecVersion)) {
                    final T version = getApiSpecEnum(userSpecVersion);
                    LOG.debug(
                        "Attempting to look up URL by home communinity id:{}, and service name: {}, and version {}",
                        homeCommunityId, serviceName, version.toString());
                    sEndpointURL = getEndpointURL(homeCommunityId, serviceName, version);
                } else {
                    LOG.debug("Retrieve endpoint from service Name {}", serviceName);
                    sEndpointURL = getDefaultEndpointURL(targetSystem.getExchangeName(), homeCommunityId,
                        serviceName);
                }
            }
        }
        LOG.debug("Returning URL: {}", sEndpointURL);
        return sEndpointURL;
    }

    @Override
    public List<UrlInfo> getEndpointURLFromNhinTargetCommunities(NhinTargetCommunitiesType targets, String serviceName)
        throws ExchangeManagerException {
        Set<UrlInfo> endpointUrlSet = new HashSet<>();

        if (targets != null && CollectionUtils.isNotEmpty(targets.getNhinTargetCommunity())) {
            for (NhinTargetCommunityType target : targets.getNhinTargetCommunity()) {
                if (target.getHomeCommunity() != null
                    && StringUtils.isNotEmpty(target.getHomeCommunity().getHomeCommunityId())) {
                    LOG.debug("Looking up URL by home community id");
                    String endpt = getDefaultEndpointURL(targets.getExchangeName(),
                        target.getHomeCommunity().getHomeCommunityId(), serviceName);

                    if (StringUtils.isNotEmpty(endpt) || NullChecker.isNullish(endpt)
                        && serviceName.equals(NhincConstants.DOC_QUERY_SERVICE_NAME)) {
                        UrlInfo entry = new UrlInfo();
                        entry.setHcid(target.getHomeCommunity().getHomeCommunityId());
                        entry.setUrl(endpt);
                        endpointUrlSet.add(entry);
                    }
                }
                if (target.getRegion() != null) {
                    LOG.debug("Looking up URL by region");
                    filterByRegion(targets.getExchangeName(), endpointUrlSet, target.getRegion(), serviceName);
                }

                if (target.getList() != null) {
                    LOG.debug("Looking up URL by list");
                    LOG.warn("The List target feature has not been implemented yet");
                }
            }
        } else {
            // This is the broadcast scenario so retrieve the entire list of URLs for the specified service
            for (OrganizationType org : getAllOrganizationSetByServiceName(serviceName)) {
                String hcid = org.getHcid();
                String endpt = getDefaultEndpointURL(null, hcid, serviceName);

                if (StringUtils.isNotEmpty(endpt)) {
                    UrlInfo entry = new UrlInfo();
                    entry.setHcid(hcid);
                    entry.setUrl(endpt);
                    endpointUrlSet.add(entry);
                }

            }
        }
        List<UrlInfo> endpointUrlList = new ArrayList<>(endpointUrlSet);

        logURLList(endpointUrlList);

        return endpointUrlList;
    }

    private void filterByRegion(String exchangeName, Set<UrlInfo> urlSet, String region, String serviceName)
        throws ExchangeManagerException {
        Set<OrganizationType> orgSet = getAllOrganizationSetByServiceName(serviceName);
        if (orgSet != null) {
            for (OrganizationType org : orgSet) {
                if (CollectionUtils.isNotEmpty(org.getTargetRegion())) {
                    for (String state : org.getTargetRegion()) {
                        if (state.equalsIgnoreCase(region)) {
                            String hcid = org.getHcid();
                            String url = getDefaultEndpointURL(exchangeName, hcid, serviceName);
                            if (StringUtils.isNotEmpty(url) && StringUtils.isNotEmpty(hcid)) {
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
    }

    private void logURLList(List<UrlInfo> urlList) {
        int idx = 0;

        if (urlList != null) {
            LOG.debug("ExchangeManager URL Info List:");
            for (UrlInfo url : urlList) {
                LOG.debug("   HCID: {}  URL # {} :{}", url.getHcid(), idx, url.getUrl());
                idx++;
            }
        } else {
            LOG.debug("Url List is Empty");
        }
    }

    protected Map<String, OrganizationType> extractHcidOrganizationMap() {
        Map<String, OrganizationType> innerMap = null;
        try {
            Map<String, Map<String, OrganizationType>> map = getCache();
            innerMap = new HashMap<>();
            for (Entry<String, Map<String, OrganizationType>> entry : map.entrySet()) {
                innerMap.putAll(entry.getValue());
            }
        } catch (ExchangeManagerException ex) {
            LOG.error("Unable to read exchange file {}", ex.getLocalizedMessage(), ex);
        }
        return innerMap;
    }
}
