/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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

import gov.hhs.fha.nhinc.connectmgr.persistance.dao.InternalExchangeInfoDAOFileImpl;
import gov.hhs.fha.nhinc.exchange.ExchangeInfoType;
import gov.hhs.fha.nhinc.exchange.ExchangeType;
import gov.hhs.fha.nhinc.exchange.directory.EndpointConfigurationType;
import gov.hhs.fha.nhinc.exchange.directory.EndpointType;
import gov.hhs.fha.nhinc.exchange.directory.OrganizationType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tjafri
 */
public class InternalExchangeManager extends AbstractExchangeManager<ADAPTER_API_LEVEL> {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeManager.class);
    private Map<String, Map<String, OrganizationType>> exInternalCache = new HashMap<>();
    private boolean exCacheLoaded = false;
    private long exFileLastUpdateTime;
    private static final InternalExchangeManager INSTANCE = new InternalExchangeManager();

    private InternalExchangeManager() {
    }

    public static final InternalExchangeManager getInstance() {
        return INSTANCE;
    }

    protected static InternalExchangeInfoDAOFileImpl getExchangeInfoDAO() {
        return InternalExchangeInfoDAOFileImpl.getInstance();
    }

    @Override
    protected Map<String, Map<String, OrganizationType>> getCache() throws ExchangeManagerException {
        refreshExchangeCacheIfRequired();
        return exInternalCache;
    }

    private void loadExchangeInfo() throws ExchangeManagerException {
        ExchangeInfoType exInfo = getExchangeInfoDAO().loadExchangeInfo();

        if (exInfo != null) {
            synchronized (exInternalCache) {
                exInternalCache.clear();
                if (null != exInfo.getExchanges() && CollectionUtils.isNotEmpty(exInfo.getExchanges().getExchange())) {
                    for (ExchangeType ex : exInfo.getExchanges().getExchange()) {
                        if (null != ex.getOrganizationList() && CollectionUtils.isNotEmpty(ex.getOrganizationList().
                            getOrganization()) || StringUtils.isNotEmpty(ex.getType()) || StringUtils.isNotEmpty(ex.
                                getName())) {
                            Map<String, OrganizationType> innerMap = new HashMap<>();
                            for (OrganizationType org : ex.getOrganizationList().getOrganization()) {
                                innerMap.put(org.getHcid(), org);
                            }
                            exInternalCache.put(ex.getName(), innerMap);
                        }
                    }
                    exCacheLoaded = true;
                    exFileLastUpdateTime = getExchangeInfoDAO().getLastModified();
                }
            }
        } else {
            LOG.error("No Exchange Information found");
        }
    }

    private boolean isCacheLoaded() {
        return exCacheLoaded;
    }

    private boolean isExpired() {
        return getExchangeInfoDAO().getLastModified() > exFileLastUpdateTime;
    }

    @Override
    public void refreshExchangeCacheIfRequired() throws ExchangeManagerException {
        if (!isCacheLoaded() || isExpired()) {
            LOG.info("refreshing Exchange Cache");
            loadExchangeInfo();
        }
    }

    @Override
    public OrganizationType getOrganization(String hcid) throws ExchangeManagerException {
        return getOrganization(null, hcid);
    }

    @Override
    public String getDefaultExchange() {
        return null;
    }

    @Override
    public OrganizationType getOrganization(String exchangeName, String hcid) throws ExchangeManagerException {

        refreshExchangeCacheIfRequired();
        String hcidWithoutPrefix = HomeCommunityMap.formatHomeCommunityId(hcid);
        String hcidWithPrefix = HomeCommunityMap.getHomeCommunityIdWithPrefix(hcid);
        if (StringUtils.isEmpty(hcidWithoutPrefix) || StringUtils.isEmpty(hcidWithPrefix)) {
            return null;
        }
        Map<String, OrganizationType> map = null;
        if (StringUtils.isNotEmpty(exchangeName)) {
            map = getCache().get(exchangeName);
        } else {
            map = extractHcidOrganizationMap();
        }
        if (null != map) {
            return map.get(hcidWithPrefix) != null ? map.get(hcidWithPrefix) : map.get(hcidWithoutPrefix);
        } else {
            return null;
        }
    }

    @Override
    public String getEndpointURL(String hcid, String sServiceName,
        ADAPTER_API_LEVEL api_spec) throws ExchangeManagerException {
        String endpointUrl = "";
        OrganizationType org = getOrganization(hcid);
        EndpointType epType = HELPER.getServiceEndpointType(org, sServiceName);
        EndpointConfigurationType configType = HELPER.getEndPointConfigBasedOnSpecVersion(epType, getAPI_SPEC(api_spec));
        if (null != configType) {
            endpointUrl = configType.getUrl();
        }
        return endpointUrl;
    }

    @Override
    protected String getAPI_SPEC(ADAPTER_API_LEVEL spec_level
        ) {
        return spec_level.toString();
    }

    @Override
    protected ADAPTER_API_LEVEL getAPI_SPEC_ENUM(String version) {
        return NhincConstants.ADAPTER_API_LEVEL.valueOf(version);
    }

    public static boolean updateServiceUrl(String serviceName, String url) throws Exception {
        String sHomeCommunityId = HELPER.getHomeCommunityFromPropFile();

        OrganizationType newOrganization = getInstance().getOrganization(sHomeCommunityId);
        if (null == newOrganization) {
            return false;
        }

        EndpointConfigurationType endpointUrl = HELPER.getEndPointConfigBasedOnSpecVersion(
            HELPER.getServiceEndpointType(newOrganization, serviceName),
            NhincConstants.ADAPTER_API_LEVEL.LEVEL_a0.name());
        endpointUrl.setUrl(url);

        ExchangeInfoType exchangeInfo = getExchangeInfo();
        List<OrganizationType> organizations = HELPER.getOrganizationTypeBy(exchangeInfo);

        OrganizationType oldOrganization = HELPER.findOrganizationTypeBy(organizations, sHomeCommunityId);
        organizations.remove(oldOrganization);
        organizations.add(newOrganization);

        getExchangeInfoDAO().saveExchangeInfo(exchangeInfo);

        return true;
    }

    public static ExchangeInfoType getExchangeInfo() throws ExchangeManagerException {
        return getExchangeInfoDAO().loadExchangeInfo();
    }
}
