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
package gov.hhs.fha.nhinc.admingui.services.impl;

import static gov.hhs.fha.nhinc.admingui.constant.AdminWSConstants.ADMIN_EXCHANGE_DELETE;
import static gov.hhs.fha.nhinc.admingui.constant.AdminWSConstants.ADMIN_EXCHANGE_INFOVIEW;
import static gov.hhs.fha.nhinc.admingui.constant.AdminWSConstants.ADMIN_EXCHANGE_LIST_ENDPOINTS;
import static gov.hhs.fha.nhinc.admingui.constant.AdminWSConstants.ADMIN_EXCHANGE_LIST_EXCHANGES;
import static gov.hhs.fha.nhinc.admingui.constant.AdminWSConstants.ADMIN_EXCHANGE_LIST_ORGANIZATIONS;
import static gov.hhs.fha.nhinc.admingui.constant.AdminWSConstants.ADMIN_EXCHANGE_REFRESH;
import static gov.hhs.fha.nhinc.admingui.constant.AdminWSConstants.ADMIN_EXCHANGE_SAVE_CONFIG;
import static gov.hhs.fha.nhinc.admingui.constant.AdminWSConstants.ADMIN_EXCHANGE_SAVE_EXCHANGE;
import static gov.hhs.fha.nhinc.admingui.constant.AdminWSConstants.ENTITY_EXCHANGE_MANAGEMENT_SERVICE_NAME;
import static gov.hhs.fha.nhinc.admingui.services.impl.PingServiceImpl.IGNORE_DEADHOST;
import static gov.hhs.fha.nhinc.admingui.util.HelperUtil.buildConfigAssertion;
import static gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerHelper.getEndpointConfigurationTypeBy;

import gov.hhs.fha.nhinc.admingui.application.EndpointManagerCache;
import gov.hhs.fha.nhinc.admingui.model.ConnectionEndpoint;
import gov.hhs.fha.nhinc.admingui.services.ExchangeManagerService;
import gov.hhs.fha.nhinc.admingui.services.PingService;
import gov.hhs.fha.nhinc.admingui.util.HelperUtil;
import gov.hhs.fha.nhinc.common.exchangemanagement.DeleteExchangeRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.ExchangeDownloadStatusType;
import gov.hhs.fha.nhinc.common.exchangemanagement.GetExchangeInfoViewRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.GetExchangeInfoViewResponseMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.ListEndpointsRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.ListEndpointsResponseMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.ListExchangesRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.ListExchangesResponseMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.ListOrganizationsRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.RefreshExchangeManagerRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.RefreshExchangeManagerResponseMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.SaveExchangeConfigRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.SaveExchangeRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.SimpleExchangeManagementResponseMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.configuration.ExchangeManagementPortDescriptor;
import gov.hhs.fha.nhinc.exchange.ExchangeInfoType;
import gov.hhs.fha.nhinc.exchange.ExchangeType;
import gov.hhs.fha.nhinc.exchange.directory.EndpointConfigurationType;
import gov.hhs.fha.nhinc.exchange.directory.EndpointType;
import gov.hhs.fha.nhinc.exchange.directory.OrganizationType;
import gov.hhs.fha.nhinc.exchangemanagement.EntityExchangeManagementPortType;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Tran tang
 *
 */
@Service
public class ExchangeManagerServiceImpl implements ExchangeManagerService {
    private static final Logger LOG = LoggerFactory.getLogger(ExchangeManagerServiceImpl.class);
    private final PingService pingService = new PingServiceImpl();
    private static final String DATE_FORMAT = "MM-dd-yy HH:mm:ss";
    private static final WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();
    private static CONNECTClient<EntityExchangeManagementPortType> client = null;
    private EndpointManagerCache endpointCache = new EndpointManagerCache();

    @Override
    public boolean saveExchange(ExchangeType exchange, String existingExchangeName) {
        SaveExchangeRequestMessageType request = new SaveExchangeRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setExchange(exchange);
        request.setOriginalExchangeName(existingExchangeName);

        try {
            SimpleExchangeManagementResponseMessageType response = (SimpleExchangeManagementResponseMessageType) invokeClientPort(
                ADMIN_EXCHANGE_SAVE_EXCHANGE, request);
            logDebug(ADMIN_EXCHANGE_SAVE_EXCHANGE, response.isStatus(), response.getMessage());
            return response.isStatus();
        } catch (Exception e) {
            LOG.error("error during save-exchange: {}", e.getLocalizedMessage(), e);
        }
        return false;
    }

    @Override
    public boolean deleteExchange(String exchangeName) {
        DeleteExchangeRequestMessageType request = new DeleteExchangeRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setExchangeName(exchangeName);

        try {
            SimpleExchangeManagementResponseMessageType response = (SimpleExchangeManagementResponseMessageType) invokeClientPort(
                ADMIN_EXCHANGE_DELETE, request);
            logDebug(ADMIN_EXCHANGE_DELETE, response.isStatus(), response.getMessage());

            if (response.isStatus()) {
                endpointCache.deleteCache(exchangeName);
            }

            return response.isStatus();
        } catch (Exception e) {
            LOG.error("error during delete-exchange: {}", e.getLocalizedMessage(), e);
        }
        return false;
    }

    @Override
    public List<ExchangeType> getAllExchanges() {
        ListExchangesRequestMessageType request = new ListExchangesRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());

        try {
            ListExchangesResponseMessageType response = (ListExchangesResponseMessageType) invokeClientPort(
                ADMIN_EXCHANGE_LIST_EXCHANGES, request);
            logDebug(ADMIN_EXCHANGE_LIST_EXCHANGES, response.getExchangesList().size());
            return response.getExchangesList();
        } catch (Exception e) {
            LOG.error("error during list-exchanges: {}", e.getLocalizedMessage(), e);
        }
        return new ArrayList<>();
    }

    @Override
    public List<OrganizationType> getAllOrganizations(String exchangeName) {

        ListOrganizationsRequestMessageType request = new ListOrganizationsRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setExchangeName(exchangeName);

        try {
            SimpleExchangeManagementResponseMessageType response = (SimpleExchangeManagementResponseMessageType) invokeClientPort(
                ADMIN_EXCHANGE_LIST_ORGANIZATIONS, request);

            logDebug(ADMIN_EXCHANGE_LIST_ORGANIZATIONS, getOrganizationListFrom(response).size());
            return getOrganizationListFrom(response);
        } catch (Exception e) {
            LOG.error("error during list-organizations: {}", e.getLocalizedMessage(), e);
        }
        return new ArrayList<>();
    }

    @Override
    public List<ConnectionEndpoint> getAllConnectionEndpoints(String exchangeName, String hcid) {

        List<EndpointType> orgEndpoints = new ArrayList<>();
        ListEndpointsRequestMessageType request = new ListEndpointsRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setHcid(hcid);
        request.setExchangeName(exchangeName);

        try {
            ListEndpointsResponseMessageType response = (ListEndpointsResponseMessageType) invokeClientPort(
                ADMIN_EXCHANGE_LIST_ENDPOINTS, request);
            logDebug(ADMIN_EXCHANGE_LIST_ENDPOINTS, response.getEndpointsList());
            orgEndpoints = response.getEndpointsList();
        } catch (Exception e) {
            LOG.error("error during list-endpoints: {}", e.getLocalizedMessage(), e);
        }

        List<ConnectionEndpoint> endpoints = new ArrayList<>();

        for (EndpointType endpoint : orgEndpoints) {
            List<EndpointConfigurationType> epConfigurations = getEndpointConfigurationTypeBy(endpoint);
            for (EndpointConfigurationType epConf : epConfigurations) {

                String timestamp = null;
                int httpCode = 0;
                String url = epConf.getUrl();

                EndpointManagerCache.EndpointCacheInfo info = endpointCache.getEndpointInfo(exchangeName,
                    HelperUtil.getHashCodeBy(hcid, url));

                if (info != null) {
                    timestamp = HelperUtil.getDate(DATE_FORMAT, info.getTimestamp());
                    httpCode = info.getHttpCode();
                }
                endpoints.add(
                    new ConnectionEndpoint(endpoint.getName().get(0), url, epConf.getVersion(), hcid, httpCode,
                        timestamp));

            }
        }
        return endpoints;
    }

    @Override
    public ExchangeInfoType getExchangeInfoView() {

        GetExchangeInfoViewRequestMessageType request = new GetExchangeInfoViewRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());

        try {
            GetExchangeInfoViewResponseMessageType response = (GetExchangeInfoViewResponseMessageType) invokeClientPort(
                ADMIN_EXCHANGE_INFOVIEW, request);
            logDebug(ADMIN_EXCHANGE_INFOVIEW, response.getExchangeInfo().getRefreshInterval(),
                response.getExchangeInfo().getMaxNumberOfBackups(), response.getExchangeInfo().getDefaultExchange());
            return response.getExchangeInfo();
        } catch (Exception e) {
            LOG.error("error during get-exchange-info-view: {}", e.getLocalizedMessage(), e);
        }
        return new ExchangeInfoType();
    }

    @Override
    public boolean saveGeneralSetting(ExchangeInfoType exchangeInfo) {

        SaveExchangeConfigRequestMessageType request = new SaveExchangeConfigRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setDefaultExchange(exchangeInfo.getDefaultExchange());
        request.setMaxNumberOfBackups(exchangeInfo.getMaxNumberOfBackups());
        request.setRefreshInterval(exchangeInfo.getRefreshInterval());

        try {
            SimpleExchangeManagementResponseMessageType response = (SimpleExchangeManagementResponseMessageType) invokeClientPort(
                ADMIN_EXCHANGE_SAVE_CONFIG, request);
            logDebug(ADMIN_EXCHANGE_SAVE_CONFIG, response.isStatus());
            return response.isStatus();
        } catch (Exception e) {
            LOG.error("error during save-exchange-config: {}", e.getLocalizedMessage(), e);
        }

        return false;
    }

    @Override
    public List<ExchangeDownloadStatusType> refreshExchangeManager() {
        RefreshExchangeManagerRequestMessageType request = new RefreshExchangeManagerRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());

        try {
            RefreshExchangeManagerResponseMessageType response = (RefreshExchangeManagerResponseMessageType) invokeClientPort(
                ADMIN_EXCHANGE_REFRESH, request);
            logDebug(ADMIN_EXCHANGE_REFRESH, response.getExchangeDownloadStatusList().size());
            return response.getExchangeDownloadStatusList();
        } catch (Exception e) {
            LOG.error("error during refresh-exchange: {}", e.getLocalizedMessage(), e);
        }
        return new ArrayList<>();
    }

    @Override
    public int pingService(ConnectionEndpoint connEndpoint, String exchangeName, String hcid) {
        if (null != connEndpoint) {
            connEndpoint.setResponseCode(pingService.ping(connEndpoint.getServiceUrl(), IGNORE_DEADHOST));
            connEndpoint.setPingTimestamp(HelperUtil.getDateNow(DATE_FORMAT));
            endpointCache.addOrUpdateEndpoint(
                exchangeName, HelperUtil.getHashCodeBy(hcid, connEndpoint.getServiceUrl()),
                connEndpoint.getServiceUrl(),
                new Date(),
                connEndpoint.isPingSuccessful(), connEndpoint.getResponseCode());
            return connEndpoint.getResponseCode();
        }
        return 0;
    }

    private static CONNECTClient<EntityExchangeManagementPortType> getClient() throws ExchangeManagerException {
        if (null == client) {
            String url = oProxyHelper.getAdapterEndPointFromConnectionManager(ENTITY_EXCHANGE_MANAGEMENT_SERVICE_NAME);
            ServicePortDescriptor<EntityExchangeManagementPortType> portDescriptor = new ExchangeManagementPortDescriptor();
            client = CONNECTClientFactory.getInstance().getCONNECTClientUnsecured(portDescriptor, url,
                new AssertionType());
        }
        return client;
    }

    private static <T> Object invokeClientPort(String serviceName, T request) throws Exception {
        return getClient().invokePort(EntityExchangeManagementPortType.class, serviceName, request);
    }

    private static List<OrganizationType> getOrganizationListFrom(
        SimpleExchangeManagementResponseMessageType response) {
        if (null != response && null != response.getOrganizationList()) {
            return response.getOrganizationList().getOrganization();
        }
        return new ArrayList<>();
    }

    private static void logDebug(String msg, Object... objects) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("{}: {}", msg, StringUtils.join(objects, ", "));
        }
    }
}
