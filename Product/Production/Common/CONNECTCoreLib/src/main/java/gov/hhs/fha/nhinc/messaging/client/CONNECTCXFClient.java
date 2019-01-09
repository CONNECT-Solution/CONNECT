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

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.cryptostore.StoreUtil;
import gov.hhs.fha.nhinc.messaging.service.ServiceEndpoint;
import gov.hhs.fha.nhinc.messaging.service.decorator.MTOMServiceEndpointDecorator;
import gov.hhs.fha.nhinc.messaging.service.decorator.cxf.WsAddressingServiceEndpointDecorator;
import gov.hhs.fha.nhinc.messaging.service.port.CXFServicePortBuilder;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortBuilder;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;

/**
 * @author bhumphrey
 *
 */
public abstract class CONNECTCXFClient<T> extends CONNECTBaseClient<T> {

    protected ServiceEndpoint<T> serviceEndpoint = null;

    protected CONNECTCXFClient(ServicePortDescriptor<T> portDescriptor, String url, String exchangeName, AssertionType assertion) {
        this(portDescriptor, url, exchangeName, assertion, new CXFServicePortBuilder<>(portDescriptor));
    }

    protected CONNECTCXFClient(ServicePortDescriptor<T> portDescriptor, String url, String exchangeName, AssertionType assertion,
        ServicePortBuilder<T> portBuilder) {

        T port = portBuilder.createPort(StoreUtil.getGatewayCertificateAlias(exchangeName), assertion);
        Integer timeout = assertion != null ? assertion.getTransactionTimeout() : null;
        serviceEndpoint = super.configureBasePort(port, url, exchangeName, timeout);
    }

    protected CONNECTCXFClient(ServicePortDescriptor<T> portDescriptor, String url, String exchangeName, AssertionType assertion,
        ServicePortBuilder<T> portBuilder, String subscriptionId) {

        T port = portBuilder.createPort(StoreUtil.getGatewayCertificateAlias(exchangeName), assertion);
        Integer timeout = assertion != null ? assertion.getTransactionTimeout() : null;
        serviceEndpoint = super.configureBasePort(port, subscriptionId, exchangeName, timeout);
    }

    @Override
    public T getPort() {
        return serviceEndpoint.getPort();
    }

    @Override
    public void enableMtom() {
        serviceEndpoint = new MTOMServiceEndpointDecorator<>(serviceEndpoint);
        serviceEndpoint.configure();
    }

    @Override
    public void enableWSA(AssertionType assertion, String wsAddressingTo, String wsAddressingActionId) {
        serviceEndpoint = new WsAddressingServiceEndpointDecorator<>(serviceEndpoint, wsAddressingTo,
            wsAddressingActionId, assertion);
        serviceEndpoint.configure();
    }
}
