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
package gov.hhs.fha.nhinc.messaging.service.port;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

/**
 * @author akong
 *
 */
public class CXFServicePortBuilder<T> implements ServicePortBuilder<T> {

    protected Class<T> serviceEndpointClass;
    protected String optionalBindingId;

    public CXFServicePortBuilder(ServicePortDescriptor<T> portDescriptor) {
        this.serviceEndpointClass = portDescriptor.getPortClass();
        this.optionalBindingId = portDescriptor.getSOAPBindingVersion();
    }

    protected void configureJaxWsProxyFactory(JaxWsProxyFactoryBean factory) {
        factory.setServiceClass(serviceEndpointClass);

        setOptionalSOAPBinding(factory, optionalBindingId);

    }

    static void setOptionalSOAPBinding(JaxWsProxyFactoryBean factory, String binding) {
        if (binding != null) {
            factory.setBindingId(binding);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public T createPort(String gatewayAlias, AssertionType assertion) {
        JaxWsProxyFactoryBean clientFactory = new JaxWsProxyFactoryBean();
        configureJaxWsProxyFactory(clientFactory);
        return (T) clientFactory.create();
    }
}
