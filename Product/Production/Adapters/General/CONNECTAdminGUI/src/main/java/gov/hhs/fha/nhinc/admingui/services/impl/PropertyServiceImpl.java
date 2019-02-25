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

import static gov.hhs.fha.nhinc.admingui.constant.AdminWSConstants.PROPERTIES_LIST_PROP;
import static gov.hhs.fha.nhinc.admingui.constant.AdminWSConstants.PROPERTIES_SAVE_PROP;
import static gov.hhs.fha.nhinc.admingui.constant.AdminWSConstants.PROPERTIES_SERVICE_NAME;
import static gov.hhs.fha.nhinc.admingui.util.HelperUtil.buildConfigAssertion;
import static gov.hhs.fha.nhinc.util.CoreHelpUtils.checkPropertyList;

import gov.hhs.fha.nhinc.admingui.services.PropertyService;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.propertyaccess.ListPropertiesRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.PropertyType;
import gov.hhs.fha.nhinc.common.propertyaccess.SavePropertyRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.SimplePropertyResponseType;
import gov.hhs.fha.nhinc.configuration.NhincComponentPropAccessorPortDescriptor;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.messaging.client.CONNECTCXFClientFactory;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinccomponentpropaccessor.NhincComponentPropAccessorPortType;
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
public class PropertyServiceImpl implements PropertyService {
    private static final Logger LOG = LoggerFactory.getLogger(PropertyServiceImpl.class);
    private static final WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();
    private static CONNECTClient<NhincComponentPropAccessorPortType> client = null;

    @Override
    public boolean saveProperty(String file, String name, String value) {
        if (checkPropertyList(file)) {
            SavePropertyRequestType request = new SavePropertyRequestType();

            request.setConfigAssertion(buildConfigAssertion());
            request.setFile(file);
            request.setPropertyName(name);
            request.setPropertyValue(value);

            try {
                SimplePropertyResponseType response = (SimplePropertyResponseType) clientInvokePort(
                    PROPERTIES_SAVE_PROP, request);
                logDebug(PROPERTIES_SAVE_PROP, response.isStatus(), response.getMessage());
                return response.isStatus();
            } catch (Exception e) {
                LOG.error("error during save-exchange: {}", e.getLocalizedMessage(), e);
            }
        }
        return false;
    }

    @Override
    public List<PropertyType> listProperties(String file) {
        if (checkPropertyList(file)) {
            ListPropertiesRequestType request = new ListPropertiesRequestType();
            request.setConfigAssertion(buildConfigAssertion());
            request.setFile(file);

            try {
                SimplePropertyResponseType response = (SimplePropertyResponseType) clientInvokePort(
                    PROPERTIES_LIST_PROP,
                    request);
                logDebug(PROPERTIES_LIST_PROP, response.getPropertyList().size());
                return response.getPropertyList();
            } catch (Exception e) {
                LOG.error("error during Propery List: {}", e.getLocalizedMessage(), e);
            }
        }
        return new ArrayList<>();
    }

    private static CONNECTClient<NhincComponentPropAccessorPortType> getClient() throws ExchangeManagerException {
        String url = oProxyHelper.getAdapterEndPointFromConnectionManager(PROPERTIES_SERVICE_NAME);
        ServicePortDescriptor<NhincComponentPropAccessorPortType> portDescriptor = new NhincComponentPropAccessorPortDescriptor();
        client = CONNECTCXFClientFactory.getInstance().getCONNECTClientUnsecured(portDescriptor, url,
            new AssertionType());
        return client;
    }

    private static <T> Object clientInvokePort(String serviceName, T request) throws Exception {
        return getClient().invokePort(NhincComponentPropAccessorPortType.class, serviceName, request);
    }
    private static void logDebug(String msg, Object... objects) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("{}: {}", msg, StringUtils.join(objects, ", "));
        }
    }

}
