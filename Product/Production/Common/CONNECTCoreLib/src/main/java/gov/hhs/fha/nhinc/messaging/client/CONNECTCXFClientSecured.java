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
package gov.hhs.fha.nhinc.messaging.client;

import gov.hhs.fha.nhinc.exchangemgr.InternalExchangeManager;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.cryptostore.StoreUtil;
import gov.hhs.fha.nhinc.messaging.service.decorator.HttpHeaderServiceEndpointDecorator;
import gov.hhs.fha.nhinc.messaging.service.decorator.SAMLServiceEndpointDecorator;
import gov.hhs.fha.nhinc.messaging.service.decorator.cxf.WsAddressingServiceEndpointDecorator;
import gov.hhs.fha.nhinc.messaging.service.decorator.cxf.WsSecurityServiceEndpointDecorator;
import gov.hhs.fha.nhinc.messaging.service.port.CachingCXFSecuredServicePortBuilder;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author akong
 */
public class CONNECTCXFClientSecured<T> extends CONNECTCXFClient<T> {

    private static final Logger LOG = LoggerFactory.getLogger(CONNECTCXFClientSecured.class);

    CONNECTCXFClientSecured(ServicePortDescriptor<T> portDescriptor, String url, String exchangeName,
        AssertionType assertion) {
        super(portDescriptor, url, exchangeName, assertion, new CachingCXFSecuredServicePortBuilder<>(portDescriptor,
            exchangeName));
        decorateEndpoint(assertion, url, portDescriptor.getWSAddressingAction(), null, null);

        serviceEndpoint.configure();
    }

    CONNECTCXFClientSecured(ServicePortDescriptor<T> portDescriptor, AssertionType assertion, String url,
        NhinTargetSystemType target, String serviceName) {

        super(portDescriptor, url, target.getExchangeName(), assertion, new CachingCXFSecuredServicePortBuilder<>(
            portDescriptor, target.getExchangeName()));
        decorateEndpoint(assertion, url, portDescriptor.getWSAddressingAction(), target, serviceName);
        serviceEndpoint.configure();
    }

    @Override
    public T getPort() {
        return serviceEndpoint.getPort();
    }

    private void decorateEndpoint(AssertionType assertion, String wsAddressingTo, String wsAddressingActionId,
        NhinTargetSystemType target, String serviceName) {
        serviceEndpoint = new SAMLServiceEndpointDecorator<>(serviceEndpoint, assertion, target, serviceName);
        serviceEndpoint = new WsAddressingServiceEndpointDecorator<>(serviceEndpoint, wsAddressingTo,
            wsAddressingActionId, assertion);
        serviceEndpoint = new HttpHeaderServiceEndpointDecorator<>(serviceEndpoint, assertion);

        String exchangeName = target != null ? target.getExchangeName() : InternalExchangeManager.getInstance()
            .getDefaultExchange();
        serviceEndpoint = new WsSecurityServiceEndpointDecorator<>(serviceEndpoint,
            StoreUtil.getGatewayCertificateAlias(exchangeName), assertion);
    }

    @Override
    public void enableWSA(AssertionType assertion, String wsAddressingTo, String wsAddressingActionId) {
        LOG.warn("Web Service Addressing is already enabled on secure clients - No action taken.");
    }
}
