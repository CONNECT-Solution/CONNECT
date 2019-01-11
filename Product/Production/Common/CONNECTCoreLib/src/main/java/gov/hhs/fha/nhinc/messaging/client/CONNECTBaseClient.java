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

import gov.hhs.fha.nhinc.cryptostore.StoreUtil;
import gov.hhs.fha.nhinc.messaging.client.interceptor.SoapResponseInInterceptor;
import gov.hhs.fha.nhinc.messaging.service.BaseServiceEndpoint;
import gov.hhs.fha.nhinc.messaging.service.ServiceEndpoint;
import gov.hhs.fha.nhinc.messaging.service.decorator.TimeoutServiceEndpointDecorator;
import gov.hhs.fha.nhinc.messaging.service.decorator.URLServiceEndpointDecorator;
import gov.hhs.fha.nhinc.messaging.service.decorator.cxf.TLSClientServiceEndpointDecorator;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import org.apache.cxf.phase.PhaseInterceptorChain;

/**
 * @author akong
 *
 */
public abstract class CONNECTBaseClient<T> implements CONNECTClient<T> {

    private WebServiceProxyHelper proxyHelper;

    protected CONNECTBaseClient() {
        proxyHelper = new WebServiceProxyHelper();
    }

    @Override
    public abstract T getPort();

    @Override
    public Object invokePort(Class<T> portClass, String methodName, Object... operationInput) throws Exception {
        Object response = proxyHelper.invokePort(getPort(), portClass, methodName, operationInput);

        SoapResponseInInterceptor.addResponseMessageIdToContext(getPort(), PhaseInterceptorChain.getCurrentMessage());

        return response;
    }

    /**
     * Configures the given port with properties common to all ports.
     *
     * @param port
     * @param url
     * @return
     */
    protected ServiceEndpoint<T> configureBasePort(T port, String url, String exchangeName, Integer timeout) {
        ServiceEndpoint<T> serviceEndpoint = new BaseServiceEndpoint<>(port);
        serviceEndpoint = new URLServiceEndpointDecorator<>(serviceEndpoint, url);
        serviceEndpoint = new TimeoutServiceEndpointDecorator<>(serviceEndpoint, timeout);
        serviceEndpoint = new TLSClientServiceEndpointDecorator<>(serviceEndpoint,
            StoreUtil.getGatewayCertificateAlias(exchangeName), exchangeName);

        return serviceEndpoint;
    }
}
