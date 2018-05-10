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
package gov.hhs.fha.nhinc.messaging.client;

import static gov.hhs.fha.nhinc.nhinclib.NhincConstants.DISABLE_CN_CHECK;
import static gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_PROPERTY_FILE;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.messaging.service.decorator.cxf.TLSClientParametersFactory;
import gov.hhs.fha.nhinc.messaging.service.port.CachingCXFUnsecuredServicePortBuilder;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tran tang
 *
 */
public class CONNECTCXFClientBaseSecured<T> extends CONNECTCXFClient<T> {
    private static final Logger LOG = LoggerFactory.getLogger(PropertyAccessor.class);

    CONNECTCXFClientBaseSecured(ServicePortDescriptor<T> portDescriptor, String url, AssertionType assertion) {
        super(portDescriptor, url, assertion, new CachingCXFUnsecuredServicePortBuilder<>(portDescriptor));

        boolean isDisableCNCheck = false;
        try {
            isDisableCNCheck = PropertyAccessor.getInstance().getPropertyBoolean(GATEWAY_PROPERTY_FILE,
                DISABLE_CN_CHECK);
        } catch (PropertyAccessException ex) {
            LOG.error("Not able to 'disableCNCheck' from the property file: {}", ex.getLocalizedMessage(), ex);
        }

        LOG.info("gateway-property--disableCNCheck: '{}'", isDisableCNCheck);

        if (isDisableCNCheck) {
            Client client = ClientProxy.getClient(serviceEndpoint.getPort());

            HTTPConduit conduit = (HTTPConduit) client.getConduit();

            TLSClientParameters tlsPara = TLSClientParametersFactory.getInstance().getTLSClientParameters();
            conduit.setTlsClientParameters(tlsPara);
        }

        serviceEndpoint.configure();
    }

    @Override
    public T getPort() {
        return serviceEndpoint.getPort();
    }

}
