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

import gov.hhs.fha.nhinc.connectmgr.persistance.dao.InternalExchangeInfoDAOFileImpl;
import gov.hhs.fha.nhinc.exchange.ExchangeInfoType;
import gov.hhs.fha.nhinc.exchange.ExchangeType;
import gov.hhs.fha.nhinc.exchange.directory.EndpointConfigurationType;
import gov.hhs.fha.nhinc.exchange.directory.EndpointType;
import gov.hhs.fha.nhinc.exchange.directory.OrganizationType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.util.ArrayList;
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
    private ExchangeInfoType exInfo = null;
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
        exInfo = getExchangeInfoDAO().loadExchangeInfo();

        if (exInfo != null) {
            synchronized (exInternalCache) {
                exInternalCache.clear();
                if (null != exInfo.getExchanges() && CollectionUtils.isNotEmpty(exInfo.getExchanges().getExchange())) {
                    for (ExchangeType ex : exInfo.getExchanges().getExchange()) {
                        if (null != ex.getOrganizationList() && CollectionUtils.isNotEmpty(ex.getOrganizationList().
                            getOrganization())) {
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
        if (exInfo == null) {
            try {
                loadExchangeInfo();
            } catch (ExchangeManagerException e) {
                LOG.error("There are some exception while loading internal exchange information ",
                    e.getLocalizedMessage(), e);
            }
        }
        return null != exInfo ? exInfo.getDefaultExchange() : null;
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

    public String getEndpointURL(String sServiceName, NhincConstants.UDDI_SPEC_VERSION version) throws
    ExchangeManagerException {
        return getEndpointURL(ExchangeManagerHelper.getHomeCommunityFromPropFile(), sServiceName, version.toString(),
            null);
    }

    private String getEndpointURL(String hcid, String sServiceName, String specVersion, String exchangeName) throws
    ExchangeManagerException {
        String endpointUrl = "";
        OrganizationType org = getOrganization(exchangeName, hcid);
        EndpointType epType = ExchangeManagerHelper.getServiceEndpointType(org, sServiceName);
        EndpointConfigurationType configType = ExchangeManagerHelper.getEndPointConfigBasedOnSpecVersion(epType,
            specVersion);
        if (null != configType) {
            endpointUrl = configType.getUrl();
        }
        return endpointUrl;
    }

    @Override
    public String getEndpointURL(String hcid, String sServiceName, ADAPTER_API_LEVEL apiSpec, String exchangeName)
        throws
        ExchangeManagerException {
        return getEndpointURL(hcid, sServiceName, apiSpec.toString(), exchangeName);
    }

    @Override
    protected String getApiSpec(ADAPTER_API_LEVEL specLevel) {
        return specLevel.toString();
    }

    @Override
    protected ADAPTER_API_LEVEL getApiSpecEnum(String version) {
        return NhincConstants.ADAPTER_API_LEVEL.valueOf(version);
    }

    public boolean updateServiceUrl(String serviceName, String url) throws ExchangeManagerException {
        return updateServiceUrl(serviceName, url, NhincConstants.ADAPTER_API_LEVEL.LEVEL_a0.name());
    }

    public boolean updateServiceUrl(String serviceName, String url, String version) throws ExchangeManagerException {
        if (null == exInfo) {
            loadExchangeInfo();
        }

        OrganizationType updateOrganization = getOrganization(ExchangeManagerHelper.getHomeCommunityFromPropFile());
        if (null == updateOrganization) {
            return false;
        }

        EndpointConfigurationType endpointUrl = ExchangeManagerHelper.getEndPointConfigBasedOnSpecVersion(
            ExchangeManagerHelper.getServiceEndpointType(updateOrganization, serviceName), version);
        endpointUrl.setUrl(url);
        getExchangeInfoDAO().saveExchangeInfo(exInfo);

        return true;
    }

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
    public List<OrganizationType> getAllOrganizations(String exchangeName) throws ExchangeManagerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getGatewayAlias(String exchangeName) {
        return null;
    }

    @Override
    ExchangeInfoType getExchangeType(String exchangeName) {
        return exInfo;
    }


}
