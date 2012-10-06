/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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
import gov.hhs.fha.nhinc.messaging.service.decorator.SAMLServiceEndpointDecorator;
import gov.hhs.fha.nhinc.messaging.service.decorator.cxf.SecurityOutInterceptorServiceEndpointDecorator;
import gov.hhs.fha.nhinc.messaging.service.decorator.cxf.SoapHeaderServiceEndPointDecorator;
import gov.hhs.fha.nhinc.messaging.service.decorator.cxf.TLSClientServiceEndpointDecorator;
import gov.hhs.fha.nhinc.messaging.service.decorator.cxf.WsAddressingServiceEndpointDecorator;
import gov.hhs.fha.nhinc.messaging.service.port.CXFServicePortBuilderWithAddressing;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;

/**
 * @author akong
 * 
 */
public class CONNECTCXFClientSecured<T> extends CONNECTCXFClient<T> {

    CONNECTCXFClientSecured(ServicePortDescriptor<T> portDescriptor, String url, AssertionType assertion) {
        super(portDescriptor, url, assertion, new CXFServicePortBuilderWithAddressing<T>(portDescriptor));
        String SoapHeader = null;
        decorateEndpoint(assertion, url, portDescriptor.getWSAddressingAction(), SoapHeader);

        serviceEndpoint.configure();
    }

    CONNECTCXFClientSecured(ServicePortDescriptor<T> portDescriptor, String url, AssertionType assertion,
            String wsAddressingTo) {
        super(portDescriptor, url, assertion, new CXFServicePortBuilderWithAddressing<T>(portDescriptor));
        String SoapHeader = null;
        decorateEndpoint(assertion, wsAddressingTo, portDescriptor.getWSAddressingAction(), SoapHeader);

        serviceEndpoint.configure();
    }

    CONNECTCXFClientSecured(ServicePortDescriptor<T> portDescriptor, String url, AssertionType assertion,
            String wsAddressingTo, String SoapHeader) {
        super(portDescriptor, url, assertion, new CXFServicePortBuilderWithAddressing<T>(portDescriptor));

        decorateEndpoint(assertion, wsAddressingTo, portDescriptor.getWSAddressingAction(), SoapHeader);

        serviceEndpoint.configure();
    }

    public T getPort() {
        return serviceEndpoint.getPort();
    }

    private void decorateEndpoint(AssertionType assertion, String wsAddressingTo, String wsAddressingActionId,
            String subscriptionId) {
        serviceEndpoint = new SAMLServiceEndpointDecorator<T>(serviceEndpoint, assertion);
        serviceEndpoint = new TLSClientServiceEndpointDecorator<T>(serviceEndpoint);
        serviceEndpoint = new SecurityOutInterceptorServiceEndpointDecorator<T>(serviceEndpoint);
        serviceEndpoint = new WsAddressingServiceEndpointDecorator<T>(serviceEndpoint, wsAddressingTo,
                wsAddressingActionId, assertion);
        if (subscriptionId != null) {
            serviceEndpoint = new SoapHeaderServiceEndPointDecorator<T>(serviceEndpoint, subscriptionId);
        }
    }

}
