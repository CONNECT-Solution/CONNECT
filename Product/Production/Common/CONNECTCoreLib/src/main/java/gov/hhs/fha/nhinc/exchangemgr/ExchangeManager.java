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

import gov.hhs.fha.nhinc.connectmgr.persistance.dao.ExchangeInfoDAOFileImpl;
import gov.hhs.fha.nhinc.cryptostore.StoreUtil;
import gov.hhs.fha.nhinc.exchange.ExchangeInfoType;
import gov.hhs.fha.nhinc.exchange.ExchangeType;
import gov.hhs.fha.nhinc.exchange.directory.EndpointConfigurationListType;
import gov.hhs.fha.nhinc.exchange.directory.EndpointConfigurationType;
import gov.hhs.fha.nhinc.exchange.directory.EndpointType;
import gov.hhs.fha.nhinc.exchange.directory.OrganizationType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.EXCHANGE_TYPE;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.UDDI_SPEC_VERSION;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tjafri
 */
public class ExchangeManager extends AbstractExchangeManager<UDDI_SPEC_VERSION> {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeManager.class);
    private ExchangeInfoType exInfo = null;
    private Map<String, Map<String, OrganizationType>> exCache = new HashMap<>();
    private ExchangeType overrideExchange;
    private boolean exCacheLoaded = false;
    private long exFileLastUpdateTime;
    private static final ExchangeManager INSTANCE = new ExchangeManager();

    protected ExchangeManager() {
    }

    protected ExchangeInfoDAOFileImpl getExchangeInfoDAO() {
        return ExchangeInfoDAOFileImpl.getInstance();
    }

    public static ExchangeManager getInstance() {
        return INSTANCE;
    }

    @Override
    protected Map<String, Map<String, OrganizationType>> getCache() throws ExchangeManagerException {
        refreshExchangeCacheIfRequired();
        return exCache;
    }

    private void loadExchangeInfo() throws ExchangeManagerException {
        exInfo = getExchangeInfoDAO().loadExchangeInfo();
        overrideExchange = null;
        if (exInfo != null) {
            synchronized (exCache) {
                exCache.clear();
                if (null != exInfo.getExchanges() && CollectionUtils.isNotEmpty(exInfo.getExchanges().getExchange())) {
                    for (ExchangeType ex : exInfo.getExchanges().getExchange()) {
                        if (isOverrideExchange(ex)) {
                            if (null != overrideExchange) {
                                LOG.warn(
                                    "Found more than one overrides exchange type .. overwriting the previous exchange");
                            }
                            overrideExchange = ex;
                        } else {
                            updateExchangeCache(ex);
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
    public String getDefaultExchange() {
        return null != exInfo ? exInfo.getDefaultExchange() : null;
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
    public OrganizationType getOrganization(String exchangeName, String hcid) throws ExchangeManagerException {
        OrganizationType org = retrieveOrganizationFromCache(exchangeName, hcid);
        return syncWithOverrides(org, hcid);
    }

    @Override
    public String getEndpointURL(String hcid, String sServiceName, UDDI_SPEC_VERSION api_spec, String exchangeName)
        throws ExchangeManagerException {
        OrganizationType org = getOrganization(exchangeName, hcid);
        EndpointType epType = ExchangeManagerHelper.getServiceEndpointType(org, sServiceName);
        if (null == epType) {
            return "";
        }
        EndpointConfigurationType configType = ExchangeManagerHelper.getEndPointConfigBasedOnSpecVersion(epType,
            getApiSpec(api_spec));
        if (null == configType) {
            throw new ExchangeManagerException("No matching target endpoint for guidance: " + getApiSpec(api_spec));
        }
        StoreUtil.addGatewayCertificateAlias(exchangeName, getGatewayAlias(exchangeName));

        return configType.getUrl();
    }

    @Override
    protected String getApiSpec(UDDI_SPEC_VERSION apispec) {
        return apispec.toString();
    }

    @Override
    protected UDDI_SPEC_VERSION getApiSpecEnum(String version) {
        return NhincConstants.UDDI_SPEC_VERSION.fromString(version);
    }

    @Override
    public List<OrganizationType> getAllOrganizations() throws ExchangeManagerException {
        refreshExchangeCacheIfRequired();
        List<OrganizationType> orgList = new ArrayList<>();
        for (Entry<String, Map<String, OrganizationType>> exEntry : exCache.entrySet()) {
            Map<String, OrganizationType> orgMap = exCache.get(exEntry.getKey());
            for (Entry<String, OrganizationType> hcidKey : orgMap.entrySet()) {
                OrganizationType org = orgMap.get(hcidKey.getKey());
                orgList.add(syncWithOverrides(org, hcidKey.getKey()));
            }
        }
        return orgList;
    }

    @Override
    public List<OrganizationType> getAllOrganizations(String exchangeName) throws ExchangeManagerException {
        refreshExchangeCacheIfRequired();
        List<OrganizationType> orgList = new ArrayList<>();
        String exName = StringUtils.isNotEmpty(exchangeName) ? exchangeName : getDefaultExchange();

        Map<String, OrganizationType> orgMap = exCache.get(exName);
        if (null != orgMap) {
            for (Entry<String, OrganizationType> hcidKey : orgMap.entrySet()) {
                orgList.add(syncWithOverrides(hcidKey.getValue(), hcidKey.getKey()));
            }
        }
        return orgList;
    }

    private void saveExchangeInfo() throws ExchangeManagerException {
        getExchangeInfoDAO().saveExchangeInfo(exInfo);
    }

    public boolean updateExchangeInfo(long refreshInterval, BigInteger maxBackups, String defaultExchange)
        throws ExchangeManagerException {
        boolean bSave = false;
        getRefreshExceptionFor("update-exchangeInfo");
        try {
            refreshExchangeCacheIfRequired();
            exInfo.setRefreshInterval(refreshInterval);
            exInfo.setMaxNumberOfBackups(maxBackups);
            exInfo.setDefaultExchange(defaultExchange);
            saveExchangeInfo();
            bSave = true;
        } catch (ExchangeManagerException e) {
            LOG.error("unable to update-exchangeInfo: {}", e.getLocalizedMessage(), e);
        }
        return bSave;
    }

    public boolean deleteExchange(String exchangeName) throws ExchangeManagerException {
        boolean bSave = false;
        getRefreshExceptionFor("delete");
        if (StringUtils.isBlank(exchangeName)) {
            return bSave;
        }
        try {
            refreshExchangeCacheIfRequired();
            if (StringUtils.equalsIgnoreCase(exchangeName, getDefaultExchange())) {
                exInfo.setDefaultExchange(null);
            }
            List<ExchangeType> exchanges = ExchangeManagerHelper.getAllExchanges(exInfo, true);

            if (exchanges.size() == 1) {
                throw new ExchangeManagerException("Cannot delete last exchange");
            }
            ExchangeType exchangeFound = ExchangeManagerHelper.findExchangeTypeBy(exchanges, exchangeName);
            if (null != exchangeFound) {
                exchanges.remove(exchangeFound);
                saveExchangeInfo();
                bSave = true;
            }
        } catch (ExchangeManagerException e) {
            LOG.error("unable to delete-exchange: {}", e.getLocalizedMessage(), e);
        }
        return bSave;
    }

    public boolean saveExchange(ExchangeType exchangeUpdate, String originalExchangeName) throws ExchangeManagerException {
        boolean bSave = false;
        getRefreshExceptionFor("save");
        if (null == exchangeUpdate) {
            return bSave;
        }
        try {
            if (null == exInfo) {
                loadExchangeInfo();
            }
            List<ExchangeType> exchanges = ExchangeManagerHelper.getAllExchanges(exInfo, true);

            String nameLookup = originalExchangeName != null ? originalExchangeName : exchangeUpdate.getName();

            ExchangeType exchangeFound = ExchangeManagerHelper.findExchangeTypeBy(exchanges, nameLookup);
            if (null != exchangeFound) {
                LOG.info("saveExchange--updated-exchangeFound");
                exchangeFound.setName(exchangeUpdate.getName());
                exchangeFound.setDisabled(exchangeUpdate.isDisabled());
                exchangeFound.setKey(exchangeUpdate.getKey());
                exchangeFound.setTLSVersions(exchangeUpdate.getTLSVersions());
                exchangeFound.setType(exchangeUpdate.getType());
                exchangeFound.setUrl(exchangeUpdate.getUrl());
                exchangeFound.setSniName(exchangeUpdate.getSniName());
                exchangeFound.setCertificateAlias(exchangeUpdate.getCertificateAlias());
            } else {
                LOG.info("saveExchange--adding-exchangeUpdate");
                exchanges.add(exchangeUpdate);
            }
            saveExchangeInfo();
            bSave = true;
        } catch (ExchangeManagerException e) {
            LOG.error("unable to save-exchangeType: {}", e.getLocalizedMessage(), e);
        }
        return bSave;
    }

    public List<ExchangeType> getAllExchanges() {
        try {
            refreshExchangeCacheIfRequired();
            return ExchangeManagerHelper.getExchangeTypeBy(exInfo);
        } catch (ExchangeManagerException e) {
            LOG.error("unable to get-all-exchanges: {}", e.getLocalizedMessage(), e);
            return new ArrayList<>();
        }
    }

    public List<OrganizationType> getAllOrganizationsBy(String exchangeName) {
        List<OrganizationType> emptyList = new ArrayList<>();
        if (StringUtils.isBlank(exchangeName)) {
            return emptyList;
        }
        try {
            refreshExchangeCacheIfRequired();
            return ExchangeManagerHelper.getOrganizationTypeBy(exInfo, exchangeName);
        } catch (ExchangeManagerException e) {
            LOG.error("unable to get-all-organizationsBy: {}", e.getLocalizedMessage(), e);
            return emptyList;
        }
    }

    public List<EndpointType> getAllEndpointTypesBy(String exchangeName, String hcid) {
        List<EndpointType> emptyList = new ArrayList<>();
        if (StringUtils.isBlank(hcid)) {
            return emptyList;
        }

        if (StringUtils.isBlank(exchangeName)) {
            return emptyList;
        }

        try {
            refreshExchangeCacheIfRequired();
            return ExchangeManagerHelper.getEndpointTypeBy(getOrganization(exchangeName, hcid));
        } catch (ExchangeManagerException e) {
            LOG.error("unable to get-all-enpointTypesBy: {}", e.getLocalizedMessage(), e);
            return emptyList;
        }
    }

    public ExchangeInfoType getExchangeInfoView() {
        ExchangeInfoType view = new ExchangeInfoType();
        try {
            refreshExchangeCacheIfRequired();
            view.setRefreshInterval(exInfo.getRefreshInterval());
            view.setMaxNumberOfBackups(exInfo.getMaxNumberOfBackups());
            view.setDefaultExchange(exInfo.getDefaultExchange());
        } catch (ExchangeManagerException e) {
            LOG.error("Error encounter: {}", e.getLocalizedMessage(), e);
        }
        return view;
    }

    private void getRefreshExceptionFor(String forAction) throws ExchangeManagerException {
        if (getExchangeInfoDAO().isRefreshLocked()) {
            throw new ExchangeManagerException(
                MessageFormat.format("ExchangeInfo.xml is being refreshed {0} is not allowed", forAction));
        }
    }

    public boolean isRefreshLocked() {
        return getExchangeInfoDAO().isRefreshLocked();
    }

    public boolean toggleIsDisabledFor(String forExchangeName) throws ExchangeManagerException {
        boolean bSave = false;
        getRefreshExceptionFor("toggle-disable");
        if (StringUtils.isBlank(forExchangeName)) {
            return bSave;
        }
        try {
            if (null == exInfo) {
                loadExchangeInfo();
            }
            List<ExchangeType> exchanges = ExchangeManagerHelper.getExchangeTypeBy(exInfo, true);
            ExchangeType exchangeFound = ExchangeManagerHelper.findExchangeTypeBy(exchanges, forExchangeName);
            if (null != exchangeFound) {
                if (exchangeFound.isDisabled()) {
                    exchangeFound.setDisabled(false);
                } else {
                    exchangeFound.setDisabled(true);
                }
            }
            saveExchangeInfo();
            bSave = true;
        } catch (ExchangeManagerException e) {
            LOG.error("unable to toggle-exchange-isDisable: {}", e.getLocalizedMessage(), e);
        }
        return bSave;
    }

    private static boolean isOverrideExchange(ExchangeType exchange) {
        return null != exchange.getOrganizationList()
            && CollectionUtils.isNotEmpty(exchange.getOrganizationList().getOrganization())
            && StringUtils.equalsIgnoreCase(EXCHANGE_TYPE.OVERRIDES.toString(), exchange.getType());
    }

    private void updateExchangeCache(ExchangeType ex) {
        if (null != ex.getOrganizationList()
            && CollectionUtils.isNotEmpty(ex.getOrganizationList().getOrganization())
            && isValidExchangeType(ex.getType()) && StringUtils.isNotEmpty(ex.getName())) {
            Map<String, OrganizationType> innerMap = new HashMap<>();
            for (OrganizationType org : ex.getOrganizationList().getOrganization()) {
                innerMap.put(org.getHcid(), org);
            }
            exCache.put(ex.getName(), innerMap);
        }
    }

    private OrganizationType lookupOrganizationInOverrideExchange(String hcid) {
        if (null != overrideExchange && null != overrideExchange.getOrganizationList() && CollectionUtils.isNotEmpty(
            overrideExchange.getOrganizationList().getOrganization())) {
            for (OrganizationType org : overrideExchange.getOrganizationList().getOrganization()) {
                if (null != org.getHcid() && verifyHcid(org.getHcid(), hcid)) {
                    return org;
                }
            }
        }
        return null;
    }

    private OrganizationType retrieveOrganizationFromCache(String exchangeName, String hcid) throws
    ExchangeManagerException {
        refreshExchangeCacheIfRequired();
        String hcidWithoutPrefix = HomeCommunityMap.formatHomeCommunityId(hcid);
        String hcidWithPrefix = HomeCommunityMap.getHomeCommunityIdWithPrefix(hcid);
        if (StringUtils.isEmpty(hcidWithoutPrefix) || StringUtils.isEmpty(hcidWithPrefix)) {
            return null;
        }
        Map<String, OrganizationType> map = null;
        if (StringUtils.isNotEmpty(exchangeName)) {
            map = exCache.get(exchangeName);
        } else if (StringUtils.isNotEmpty(getDefaultExchange())) {
            map = exCache.get(getDefaultExchange());
        } else {
            if (null != getCache().values()) {
                map = extractHcidOrganizationMap();
            }
        }
        if (null != map) {
            return map.get(hcidWithPrefix) != null ? map.get(hcidWithPrefix) : map.get(hcidWithoutPrefix);
        } else {
            return null;
        }
    }

    private OrganizationType syncWithOverrides(OrganizationType originalOrg, String hcid) {
        OrganizationType overrideOrg = lookupOrganizationInOverrideExchange(hcid);
        if (null == originalOrg && null == overrideOrg) {
            return null;
        }
        //if overrides org does not have any endpoints, return the org without sync
        if (null == overrideOrg || null == overrideOrg.getEndpointList() || CollectionUtils.
            isEmpty(overrideOrg.getEndpointList().getEndpoint())) {
            return originalOrg;
        }
        //if org does not have any endpoints, return the OverrrideOrg with out sync
        if (null == originalOrg || null == originalOrg.getEndpointList() || CollectionUtils.isEmpty(originalOrg.
            getEndpointList().getEndpoint())) {
            return overrideOrg;
        }
        //synch up the Org with overrides.
        return syncEndpoints(originalOrg, overrideOrg);
    }

    private static OrganizationType syncEndpoints(OrganizationType originalOrg, OrganizationType overrideOrg) {
        Map<String, EndpointType> overrideEpMap = buildOverrideMap(overrideOrg.getEndpointList().getEndpoint());

        for (EndpointType orEpType : originalOrg.getEndpointList().getEndpoint()) {
            String serviceName = ExchangeManagerHelper.getNhinServiceName(orEpType.getName());
            if (overrideEpMap.containsKey(serviceName)) {
                EndpointType overrideEp = overrideEpMap.get(serviceName);
                syncEndpointType(orEpType, overrideEp);
                overrideEpMap.remove(serviceName);
            }
        }
        if (!overrideEpMap.isEmpty()) {
            for (Map.Entry<String, EndpointType> obj : overrideEpMap.entrySet()) {
                originalOrg.getEndpointList().getEndpoint().add(overrideEpMap.get(obj.getKey()));
            }
        }
        return originalOrg;
    }

    private static Map<String, EndpointType> buildOverrideMap(List<EndpointType> epOverrideList) {
        Map<String, EndpointType> overrideEpMap = new HashMap<>();

        for (EndpointType overriderEpType : epOverrideList) {
            String serviceName = ExchangeManagerHelper.getNhinServiceName(overriderEpType.getName());
            if (StringUtils.isNotEmpty(serviceName) && !ExchangeManagerHelper.getSpecVersionsAndUrlMap(
                overriderEpType).isEmpty()) {
                overrideEpMap.put(serviceName, overriderEpType);
            }
        }
        return overrideEpMap;
    }

    private static EndpointType syncEndpointType(EndpointType originalEpType, EndpointType overrideEpType) {
        if (null == originalEpType || null == originalEpType.getEndpointConfigurationList()) {
            return overrideEpType;
        }
        if (null == overrideEpType || null == overrideEpType.getEndpointConfigurationList()) {
            return originalEpType;
        }
        EndpointConfigurationListType synchedEpListType = new EndpointConfigurationListType();
        List<EndpointConfigurationType> synchedEpList = new ArrayList<>();

        List<EndpointConfigurationType> overridesEpConfigList = overrideEpType.getEndpointConfigurationList().
            getEndpointConfiguration();
        List<EndpointConfigurationType> epConfigList = originalEpType.getEndpointConfigurationList().
            getEndpointConfiguration();

        if (CollectionUtils.isEmpty(epConfigList) && CollectionUtils.isNotEmpty(overridesEpConfigList)) {
            synchedEpList.addAll(overridesEpConfigList);
        } else if (CollectionUtils.isNotEmpty(epConfigList) && CollectionUtils.isEmpty(overridesEpConfigList)) {
            synchedEpList.addAll(epConfigList);
        } else {
            synchedEpList.addAll(buildEndpointConfig(epConfigList, overridesEpConfigList));
        }
        synchedEpListType.getEndpointConfiguration().addAll(synchedEpList);
        originalEpType.setEndpointConfigurationList(synchedEpListType);
        return originalEpType;
    }

    private static List<EndpointConfigurationType> buildEndpointConfig(
        List<EndpointConfigurationType> originalEpConfigList,
        List<EndpointConfigurationType> overridesEpConfigList) {
        List<EndpointConfigurationType> syncConfigList = new ArrayList<>();
        Map<String, String> overrideMap = new HashMap<>();
        for (EndpointConfigurationType config : originalEpConfigList) {
            if (StringUtils.isNotBlank(config.getVersion()) && StringUtils.isNotBlank(config.getUrl())) {
                overrideMap.put(config.getVersion(), config.getUrl());
            }
        }

        for (EndpointConfigurationType config : overridesEpConfigList) {
            overrideMap.put(config.getVersion(), config.getUrl());
        }

        for (Map.Entry<String, String> obj : overrideMap.entrySet()) {
            EndpointConfigurationType epConfig = new EndpointConfigurationType();
            epConfig.setVersion(obj.getKey());
            epConfig.setUrl(overrideMap.get(obj.getKey()));
            syncConfigList.add(epConfig);
        }
        return syncConfigList;
    }

    private static boolean verifyHcid(String overrideHcid, String requestHcid) {
        String hcidWithoutPrefix = HomeCommunityMap.formatHomeCommunityId(requestHcid);
        String hcidWithPrefix = HomeCommunityMap.getHomeCommunityIdWithPrefix(requestHcid);
        return StringUtils.equalsIgnoreCase(hcidWithoutPrefix, overrideHcid) || StringUtils.endsWithIgnoreCase(
            hcidWithPrefix, overrideHcid);
    }

    private static boolean isValidExchangeType(String type) {
        for (EXCHANGE_TYPE exType : EXCHANGE_TYPE.values()) {
            if (StringUtils.equalsIgnoreCase(exType.getValue(), type)) {
                return true;
            }
        }
        LOG.warn("Incorrect Exchange type in exchangeInfo.xml {}", type);
        return false;
    }

    @Override
    protected String getGatewayAlias(String exchangeName) {
        String defaultExchange = getDefaultExchangeIfEmpty(exchangeName);

        ExchangeType exchange = ExchangeManagerHelper
            .findExchangeTypeBy(ExchangeManagerHelper.getExchangeTypeBy(exInfo), defaultExchange);
        return null != exchange ? exchange.getCertificateAlias() : null;
    }

    /**
     * @param exchangeName
     * @return
     */
    private String getDefaultExchangeIfEmpty(String exchangeName) {
        return StringUtils.isNotBlank(exchangeName) ? exchangeName : getDefaultExchange();
    }

    @Override
    ExchangeInfoType getExchangeType(String exchangeName) {
        return exInfo;
    }

}
