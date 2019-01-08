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
package gov.hhs.fha.nhinc.internalexchangemanagement;

import gov.hhs.fha.nhinc.common.internalexchangemanagement.EndpointPropertyType;
import gov.hhs.fha.nhinc.common.internalexchangemanagement.ListEndpointsRequestMessageType;
import gov.hhs.fha.nhinc.common.internalexchangemanagement.SimpleInternalExchangeManagementResponseMessageType;
import gov.hhs.fha.nhinc.common.internalexchangemanagement.UpdateEndpointRequestMessageType;
import gov.hhs.fha.nhinc.exchange.directory.EndpointConfigurationType;
import gov.hhs.fha.nhinc.exchange.directory.EndpointType;
import gov.hhs.fha.nhinc.exchange.directory.OrganizationType;
import gov.hhs.fha.nhinc.exchangemanagement.ExchangeManagement;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.exchangemgr.InternalExchangeManager;
import gov.hhs.fha.nhinc.util.CoreHelpUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ptambellini
 */

public class InternalExchangeManagement implements EntityInternalExchangeManagementPortType {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeManagement.class);
    private static final String ACT_SUCCESSFUL = "successful";
    private static final String ACT_FAIL = "fail";

    @Override
    public SimpleInternalExchangeManagementResponseMessageType updateEndpoint(
        UpdateEndpointRequestMessageType request) {
        LOG.trace("Update Endpoint--call");
        if (null != request.getEndpoint() && (StringUtils.isBlank(request.getEndpoint().getName())
            || StringUtils.isBlank(request.getEndpoint().getUrl())
            || StringUtils.isBlank(request.getEndpoint().getVersion()))) {
            return buildSimpleResponse(Boolean.FALSE, "Name, Url and Version are required");
        }

        try {
            return buildSimpleResponse(
                getInternalExchangeManager().updateServiceUrl(request.getEndpoint().getName(),
                    request.getEndpoint().getUrl(), request.getEndpoint().getVersion()),
                ACT_SUCCESSFUL);
        } catch (ExchangeManagerException e) {
            LOG.error("error during update endpoint: {}", e.getLocalizedMessage(), e);
            return buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
        }
    }

    @Override
    public SimpleInternalExchangeManagementResponseMessageType listEndpoints(ListEndpointsRequestMessageType request) {
        LOG.trace("listEndpoints--call");

        SimpleInternalExchangeManagementResponseMessageType response = new SimpleInternalExchangeManagementResponseMessageType();
        try {
            List<OrganizationType> orgList = getInternalExchangeManager().getAllOrganizations();
            response.getEndpointsList().addAll(getEndpointProperty(orgList));
        } catch (ExchangeManagerException e) {
            LOG.error("error during list endpoints: {}", e.getLocalizedMessage(), e);
            return buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
        }
        return response;
    }

    private static List<EndpointPropertyType> getEndpointProperty(List<OrganizationType> orgList) {
        List<EndpointPropertyType> endpointList = new ArrayList<>();
        for (OrganizationType org : orgList) {
            for (EndpointType end : org.getEndpointList().getEndpoint()) {
                for (EndpointConfigurationType endConfig : end.getEndpointConfigurationList()
                    .getEndpointConfiguration()) {
                    EndpointPropertyType newEndpoint = new EndpointPropertyType();
                    newEndpoint.setName(end.getName().get(0));
                    newEndpoint.setVersion(endConfig.getVersion());
                    newEndpoint.setUrl(endConfig.getUrl());
                    endpointList.add(newEndpoint);
                }

            }
        }
        return CoreHelpUtils.getUniqueList(endpointList);
    }

    private static SimpleInternalExchangeManagementResponseMessageType buildSimpleResponse(Boolean status,
        String message) {
        SimpleInternalExchangeManagementResponseMessageType retMsg = new SimpleInternalExchangeManagementResponseMessageType();
        retMsg.setStatus(status);
        retMsg.setMessage(message);
        return retMsg;
    }

    private static InternalExchangeManager getInternalExchangeManager() {
        return InternalExchangeManager.getInstance();
    }
}
