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

import static gov.hhs.fha.nhinc.admingui.constant.AdminWSConstants.ADMIN_EXCHANGE_LIST_ENDPOINTS;
import static gov.hhs.fha.nhinc.admingui.constant.AdminWSConstants.ADMIN_EXCHANGE_UPDATE_ENDPOINT;
import static gov.hhs.fha.nhinc.admingui.constant.AdminWSConstants.ENTITY_INTERNAL_EXCHANGE_MANAGEMENT_SERVICE_NAME;
import static gov.hhs.fha.nhinc.admingui.util.HelperUtil.buildConfigAssertion;

import gov.hhs.fha.nhinc.admingui.services.InternalExchangeManagerService;
import gov.hhs.fha.nhinc.common.internalexchangemanagement.EndpointPropertyType;
import gov.hhs.fha.nhinc.common.internalexchangemanagement.ListEndpointsRequestMessageType;
import gov.hhs.fha.nhinc.common.internalexchangemanagement.SimpleInternalExchangeManagementResponseMessageType;
import gov.hhs.fha.nhinc.common.internalexchangemanagement.UpdateEndpointRequestMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.configuration.InternalExchangeManagementPortDescriptor;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.internalexchangemanagement.EntityInternalExchangeManagementPortType;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author ptambellini
 *
 */
@Service
public class InternalExchangeManagerServiceImpl implements InternalExchangeManagerService {

    private static final Logger LOG = LoggerFactory.getLogger(InternalExchangeManagerServiceImpl.class);
    private static final WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();
    private static CONNECTClient<EntityInternalExchangeManagementPortType> client = null;

    @Override
    public boolean updateEndpoint(EndpointPropertyType endpointProp) {
        UpdateEndpointRequestMessageType request = new UpdateEndpointRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setEndpoint(endpointProp);
        try {
            SimpleInternalExchangeManagementResponseMessageType response
            = (SimpleInternalExchangeManagementResponseMessageType) invokeClientPort(
                ADMIN_EXCHANGE_UPDATE_ENDPOINT, request);
            logDebug(ADMIN_EXCHANGE_UPDATE_ENDPOINT, response.isStatus(), response.getMessage());
            return response.isStatus();
        } catch (Exception e) {
            LOG.error("error during update-endpoint: {}", e.getLocalizedMessage(), e);
        }
        return false;
    }

    private static CONNECTClient<EntityInternalExchangeManagementPortType> getClient() throws ExchangeManagerException {
        if (null == client) {
            String url = oProxyHelper
                .getAdapterEndPointFromConnectionManager(ENTITY_INTERNAL_EXCHANGE_MANAGEMENT_SERVICE_NAME);
            ServicePortDescriptor<EntityInternalExchangeManagementPortType> portDescriptor
            = new InternalExchangeManagementPortDescriptor();
            client = CONNECTClientFactory.getInstance().getCONNECTClientUnsecured(portDescriptor, url,
                new AssertionType());
        }
        return client;
    }

    private static <T> Object invokeClientPort(String serviceName, T request) throws Exception {
        return getClient().invokePort(EntityInternalExchangeManagementPortType.class, serviceName, request);
    }

    private static void logDebug(String msg, Object... objects) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("{}: {}", msg, StringUtils.join(objects, ", "));
        }
    }

    @Override
    public List<EndpointPropertyType> getAllEndpoints() {
        ListEndpointsRequestMessageType request = new ListEndpointsRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());

        try {
            SimpleInternalExchangeManagementResponseMessageType response
            = (SimpleInternalExchangeManagementResponseMessageType) invokeClientPort(
                ADMIN_EXCHANGE_LIST_ENDPOINTS, request);
            logDebug(ADMIN_EXCHANGE_LIST_ENDPOINTS, response.getEndpointsList().size());
            return response.getEndpointsList();
        } catch (Exception e) {
            LOG.error("error during list-exchanges: {}", e.getLocalizedMessage(), e);
        }
        return new ArrayList<>();

    }
}
