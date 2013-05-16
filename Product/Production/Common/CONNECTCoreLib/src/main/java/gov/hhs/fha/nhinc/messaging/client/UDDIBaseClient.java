/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   * Redistributions of source code must retain the above
 *     copyright notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *   * Neither the name of the United States Government nor the
 *     names of its contributors may be used to endorse or promote products
 *     derived from this software without specific prior written permission.
 *
 *THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 *DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.messaging.client;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.messaging.service.BaseServiceEndpoint;
import gov.hhs.fha.nhinc.messaging.service.ServiceEndpoint;
import gov.hhs.fha.nhinc.messaging.service.decorator.TimeoutServiceEndpointDecorator;
import gov.hhs.fha.nhinc.messaging.service.decorator.URLServiceEndpointDecorator;
import gov.hhs.fha.nhinc.messaging.service.decorator.cxf.WsAddressingServiceEndpointDecorator;
import gov.hhs.fha.nhinc.messaging.service.port.CXFServicePortBuilder;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;

/**
 * @author bhumphrey
 * 
 */
public class UDDIBaseClient<T> implements CONNECTClient<T> {

    private WebServiceProxyHelper proxyHelper;
    private ServiceEndpoint<T> serviceEndpoint = null;

    public UDDIBaseClient(ServicePortDescriptor<T> portDescriptor, String url) {
        
        
        proxyHelper = new WebServiceProxyHelper();
        
        CXFServicePortBuilder<T> portBuilder = new CXFServicePortBuilder<T>(portDescriptor);
        
        serviceEndpoint = new BaseServiceEndpoint<T>(portBuilder.createPort());
        serviceEndpoint = new URLServiceEndpointDecorator<T>(serviceEndpoint, url);
        serviceEndpoint = new TimeoutServiceEndpointDecorator<T>(serviceEndpoint);
        
        serviceEndpoint.configure();

    }

    public T getPort() {
        return serviceEndpoint.getPort();
    }

    @Override
    public Object invokePort(Class<T> portClass, String methodName, Object operationInput) throws Exception {
        Object response = proxyHelper.invokePort(getPort(), portClass, methodName, operationInput);
        return response;
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.messaging.client.CONNECTClient#supportMtom()
     */
    @Override
    public void enableMtom() {
        // Do nothing, UDDI doesn't support Mtom.   
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.messaging.client.CONNECTClient#enableWSA(gov.hhs.fha.nhinc.common.nhinccommon.AssertionType, java.lang.String, java.lang.String)
     */
    @Override
    public void enableWSA(AssertionType assertion, String wsAddressingTo, String wsAddressingActionId) {
        serviceEndpoint = new WsAddressingServiceEndpointDecorator<T>(serviceEndpoint, wsAddressingTo, wsAddressingActionId, assertion);
    }

    
}
