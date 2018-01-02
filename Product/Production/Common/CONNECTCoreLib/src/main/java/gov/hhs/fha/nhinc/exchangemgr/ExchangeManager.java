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

import gov.hhs.fha.nhinc.connectmgr.persistance.dao.ExchangeInfoDAOFileImpl;
import gov.hhs.fha.nhinc.exchange.ExchangeInfoType;
import gov.hhs.fha.nhinc.exchange.ExchangeType;
import gov.hhs.fha.nhinc.exchange.directory.EndpointConfigurationType;
import gov.hhs.fha.nhinc.exchange.directory.EndpointType;
import gov.hhs.fha.nhinc.exchange.directory.OrganizationType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.UDDI_SPEC_VERSION;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.math.BigInteger;
import java.text.MessageFormat;
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
public class ExchangeManager extends AbstractExchangeManager<UDDI_SPEC_VERSION> {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeManager.class);
    private ExchangeInfoType exInfo = null;
    private Map<String, Map<String, OrganizationType>> exCache = new HashMap<>();
    private boolean exCacheLoaded = false;
    private long exFileLastUpdateTime;
    private static String defaultExchange = null;
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

        if (exInfo != null) {
            synchronized (exCache) {
                exCache.clear();
                if (null != exInfo.getExchanges() && CollectionUtils.isNotEmpty(exInfo.getExchanges().getExchange())) {
                    for (ExchangeType ex : exInfo.getExchanges().getExchange()) {
                        if (null != ex.getOrganizationList()
                            && CollectionUtils.isNotEmpty(ex.getOrganizationList().getOrganization())
                            && StringUtils.isNotEmpty(ex.getType()) && StringUtils.isNotEmpty(ex.getName())) {
                            Map<String, OrganizationType> innerMap = new HashMap<>();
                            for (OrganizationType org : ex.getOrganizationList().getOrganization()) {
                                innerMap.put(org.getHcid(), org);
                            }
                            exCache.put(ex.getName(), innerMap);
                        }
                    }
                    defaultExchange = exInfo.getDefaultExchange();
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
        return defaultExchange;
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
        refreshExchangeCacheIfRequired();
        String hcidWithoutPrefix = HomeCommunityMap.formatHomeCommunityId(hcid);
        String hcidWithPrefix = HomeCommunityMap.getHomeCommunityIdWithPrefix(hcid);
        if (StringUtils.isEmpty(hcidWithoutPrefix) || StringUtils.isEmpty(hcidWithPrefix)) {
            return null;
        }
        Map<String, OrganizationType> map = null;
        if (StringUtils.isNotEmpty(exchangeName)) {
            map = exCache.get(exchangeName);
        } else if (StringUtils.isNotEmpty(defaultExchange)) {
            map = exCache.get(defaultExchange);
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

    @Override
    public String getEndpointURL(String hcid, String sServiceName, UDDI_SPEC_VERSION api_spec)
        throws ExchangeManagerException {
        OrganizationType org = getOrganization(hcid);
        EndpointType epType = ExchangeManagerHelper.getServiceEndpointType(org, sServiceName);
        if (null == epType) {
            return "";
        }
        EndpointConfigurationType configType = ExchangeManagerHelper.getEndPointConfigBasedOnSpecVersion(epType,
            getApiSpec(api_spec));
        if (null == configType) {
            throw new ExchangeManagerException("No matching target endpoint for guidance: " + getApiSpec(api_spec));
        }
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
        return ExchangeManagerHelper.getOrganizationTypeAllByCache(exCache);
    }

    private void saveExchangeInfo() throws ExchangeManagerException {
        getExchangeInfoDAO().saveExchangeInfo(exInfo);
    }

    public boolean updateExchangeInfo(long refreshInterval, BigInteger maxBackups, String defaultExchange)
        throws ExchangeManagerException {
        boolean bSave = false;
        getRefreshExceptionFor("update-exchangeInfo");
        try {
            if (null == exInfo) {
                loadExchangeInfo();
            }
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
            if (null == exInfo) {
                loadExchangeInfo();
            }
            List<ExchangeType> exchanges = getAllExchanges();
            ExchangeType exchangeFound = ExchangeManagerHelper.findExchangeTypeBy(exchanges, exchangeName);
            if (null != exchangeFound) {
                exchanges.remove(exchangeFound);
            }
            saveExchangeInfo();
            bSave = true;
        } catch (ExchangeManagerException e) {
            LOG.error("unable to delete-exchange: {}", e.getLocalizedMessage(), e);
        }
        return bSave;
    }

    public boolean saveExchange(ExchangeType exchangeAdd) throws ExchangeManagerException {
        boolean bSave = false;
        getRefreshExceptionFor("save");
        if (null == exchangeAdd) {
            return bSave;
        }
        try {
            if (null == exInfo) {
                loadExchangeInfo();
            }
            List<ExchangeType> exchanges = ExchangeManagerHelper.getExchangeTypeBy(exInfo, true);
            ExchangeType exchangeFound = ExchangeManagerHelper.findExchangeTypeBy(exchanges, exchangeAdd.getName());
            if (null != exchangeFound) {
                exchanges.remove(exchangeFound);
            }
            exchanges.add(exchangeAdd);
            saveExchangeInfo();
            bSave = true;
        } catch (ExchangeManagerException e) {
            LOG.error("unable to save-exchangeType: {}", e.getLocalizedMessage(), e);
        }
        return bSave;
    }

    public List<ExchangeType> getAllExchanges() {
        try {
            if (null == exInfo) {
                loadExchangeInfo();
            }
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
            if (null == exInfo) {
                loadExchangeInfo();
            }
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
            if (null == exInfo) {
                loadExchangeInfo();
            }
            return ExchangeManagerHelper.getEndpointTypeBy(exInfo, exchangeName, hcid);
        } catch (ExchangeManagerException e) {
            LOG.error("unable to get-all-enpointTypesBy: {}", e.getLocalizedMessage(), e);
            return emptyList;
        }
    }

    public ExchangeInfoType getExchangeInfoView() {
        ExchangeInfoType view = new ExchangeInfoType();
        view.setRefreshInterval(exInfo.getRefreshInterval());
        view.setMaxNumberOfBackups(exInfo.getMaxNumberOfBackups());
        view.setDefaultExchange(exInfo.getDefaultExchange());
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
                if (exchangeFound.isDisabled() == false) {
                    exchangeFound.setDisabled(true);
                } else {
                    exchangeFound.setDisabled(false);
                }
            }
            saveExchangeInfo();
            bSave = true;
        } catch (ExchangeManagerException e) {
            LOG.error("unable to toggle-exchange-isDisable: {}", e.getLocalizedMessage(), e);
        }
        return bSave;
    }
}
