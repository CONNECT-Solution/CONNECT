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
package gov.hhs.fha.nhinc.admingui.services.impl;

import static gov.hhs.fha.nhinc.admingui.util.HelperUtil.buildConfigAssertion;
import static gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerHelper.getEndpointConfigurationTypeBy;
import static gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADMIN_EXCHANGE_DELETE;
import static gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADMIN_EXCHANGE_REFRESH;
import static gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADMIN_EXCHANGE_SAVE_EXCHANGE;
import static gov.hhs.fha.nhinc.nhinclib.NhincConstants.ENTITY_EXCHANGE_MANAGEMENT_SERVICE_NAME;

import gov.hhs.fha.nhinc.admingui.application.EndpointManagerCache;
import gov.hhs.fha.nhinc.admingui.model.ConnectionEndpoint;
import gov.hhs.fha.nhinc.admingui.services.ExchangeManagerService;
import gov.hhs.fha.nhinc.admingui.services.PingService;
import gov.hhs.fha.nhinc.admingui.util.HelperUtil;
import gov.hhs.fha.nhinc.common.exchangemanagement.DeleteExchangeRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.ExchangeDownloadStatusType;
import gov.hhs.fha.nhinc.common.exchangemanagement.RefreshExchangeManagerRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.RefreshExchangeManagerResponseMessageType;
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
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManager;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.messaging.client.CONNECTCXFClientFactory;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    private final ExchangeManager exchangeManager = ExchangeManager.getInstance();
    private final PingService pingService = new PingServiceImpl();
    private static final String DATE_FORMAT = "MM-dd-yy hh:mm:ss";
    private static final WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    @Override
    public boolean saveExchange(ExchangeType exchange) {
        SaveExchangeRequestMessageType request = new SaveExchangeRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setExchange(exchange);

        try {
            SimpleExchangeManagementResponseMessageType response = (SimpleExchangeManagementResponseMessageType) clientInvokePort(
                ADMIN_EXCHANGE_SAVE_EXCHANGE, request);
            LOG.debug("{}: {}, {}", ADMIN_EXCHANGE_SAVE_EXCHANGE, response.isStatus(), response.getMessage());
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
            SimpleExchangeManagementResponseMessageType response = (SimpleExchangeManagementResponseMessageType) clientInvokePort(
                ADMIN_EXCHANGE_DELETE, request);
            LOG.debug("{}: {}, {}", ADMIN_EXCHANGE_DELETE, response.isStatus(), response.getMessage());
            return response.isStatus();
        } catch (Exception e) {
            LOG.error("error during delete-exchange: {}", e.getLocalizedMessage(), e);
        }
        return false;
    }

    @Override
    public List<ExchangeType> getAllExchanges() {
        return exchangeManager.getAllExchanges();
    }

    @Override
    public List<OrganizationType> getAllOrganizations(String exchangeName) {
        return exchangeManager.getAllOrganizationsBy(exchangeName);
    }

    @Override
    public List<ConnectionEndpoint> getAllConnectionEndpoints(String exchangeName, String hcid) {
        List<EndpointType> orgEndpoints = exchangeManager.getAllEndpointTypesBy(exchangeName, hcid);
        List<ConnectionEndpoint> endpoints = new ArrayList<>();

        for (EndpointType endpoint : orgEndpoints) {
            List<EndpointConfigurationType> epConfigurations = getEndpointConfigurationTypeBy(endpoint);
            for (EndpointConfigurationType epConf : epConfigurations) {

                String timestamp = null;
                String status = "None";
                String url = epConf.getUrl();
                EndpointManagerCache.EndpointCacheInfo info = EndpointManagerCache.getInstance().getEndpointInfo(url);

                if (info != null) {
                    timestamp = HelperUtil.getDate(DATE_FORMAT, info.getTimestamp());
                    status = info.isSuccessfulPing() ? "Pass" : "Fail";
                }
                endpoints.add(
                    new ConnectionEndpoint(endpoint.getName().get(0), url, epConf.getVersion(), status, timestamp));
            }
        }
        return endpoints;
    }

    @Override
    public ExchangeInfoType getExchangeInfoView() {
        return exchangeManager.getExchangeInfoView();
    }

    @Override
    public boolean saveGeneralSetting(ExchangeInfoType exchangeInfo) {
        try {
            return exchangeManager.updateExchangeInfo(exchangeInfo.getRefreshInterval(),
                exchangeInfo.getMaxNumberOfBackups(), exchangeInfo.getDefaultExchange());
        } catch (ExchangeManagerException e) {
            LOG.error("error during delete-exchange: {}", e.getLocalizedMessage(), e);
        }
        return false;
    }

    @Override
    public List<ExchangeDownloadStatusType> refreshExchangeManager() {
        RefreshExchangeManagerRequestMessageType request = new RefreshExchangeManagerRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());

        try {
            RefreshExchangeManagerResponseMessageType response = (RefreshExchangeManagerResponseMessageType) clientInvokePort(
                ADMIN_EXCHANGE_REFRESH, request);
            LOG.debug("{}: {}", ADMIN_EXCHANGE_REFRESH, response.getExchangeDownloadStatusList().size());
            return response.getExchangeDownloadStatusList();
        } catch (Exception e) {
            LOG.error("error during refresh-exchange: {}", e.getLocalizedMessage(), e);
        }
        return new ArrayList<>();
    }

    @Override
    public boolean pingService(ConnectionEndpoint connEndpoint) {
        if (null != connEndpoint) {
            boolean status = pingService.ping(connEndpoint.getServiceUrl());
            connEndpoint.setPing(status ? "Pass" : "Fail");
            connEndpoint.setPingTimestamp(HelperUtil.getDateNow(DATE_FORMAT));
            EndpointManagerCache.getInstance().addOrUpdateEndpoint(connEndpoint.getServiceUrl(), new Date(), status);
            return true;
        }
        return false;
    }

    private static CONNECTClient<EntityExchangeManagementPortType> getClient() throws Exception {
        String url = oProxyHelper.getAdapterEndPointFromConnectionManager(ENTITY_EXCHANGE_MANAGEMENT_SERVICE_NAME);
        ServicePortDescriptor<EntityExchangeManagementPortType> portDescriptor = new ExchangeManagementPortDescriptor();
        return CONNECTCXFClientFactory.getInstance().getCONNECTClientUnsecured(portDescriptor, url,
            new AssertionType());
    }

    private static <T> Object clientInvokePort(String serviceName, T request) throws Exception {
        return getClient().invokePort(EntityExchangeManagementPortType.class, serviceName, request);
    }
}
