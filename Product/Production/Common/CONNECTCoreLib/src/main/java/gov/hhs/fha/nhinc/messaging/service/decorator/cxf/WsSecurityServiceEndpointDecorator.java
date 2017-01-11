/*
 * Copyright (c) 2009-2016, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.messaging.service.decorator.cxf;

import gov.hhs.fha.nhinc.messaging.client.interceptor.HeaderRequestOutInterceptor;

import gov.hhs.fha.nhinc.messaging.service.ServiceEndpoint;
import gov.hhs.fha.nhinc.messaging.service.decorator.ServiceEndpointDecorator;
import java.util.Map;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;

/**
 * @author akong
 *
 */
public class WsSecurityServiceEndpointDecorator<T> extends ServiceEndpointDecorator<T> {

    private WsSecurityConfigFactory configFactory = null;

    /**
     * Constructor.
     *
     * @param decoratoredEndpoint - endpoint instance where this decorator will be applied
     */
    public WsSecurityServiceEndpointDecorator(final ServiceEndpoint<T> decoratoredEndpoint) {
        this(decoratoredEndpoint, WsSecurityConfigFactory.getInstance());
    }

    /**
     * Constructor with dependency injection parameters.
     *
     * @param decoratoredEndpoint - endpoint instance where this decorator will be applied
     * @param configFactory - factory that produce a config map
     */
    public WsSecurityServiceEndpointDecorator(final ServiceEndpoint<T> decoratoredEndpoint,
            final WsSecurityConfigFactory configFactory) {
        super(decoratoredEndpoint);
        this.configFactory = configFactory;
    }

    /**
     * Configures the endpoint for WS-Security. This call is not thread safe if the port is a shared instance as it adds
     * interceptors to the CXF client.
     */
    @Override
    public void configure() {
        super.configure();

        final Client client = ClientProxy.getClient(getPort());
        final Map<String, Object> outProps = configFactory.getConfiguration();

        configureWSSecurityOnClient(client, outProps);
    }

    private void configureWSSecurityOnClient(final Client client, final Map<String, Object> outProps) {
        final WSS4JOutInterceptor outInterceptor = new WSS4JOutInterceptor(outProps);
        outInterceptor.setAllowMTOM(true);

        for (final Interceptor<? extends Message> interceptor : client.getOutInterceptors()) {
            if (interceptor instanceof WSS4JOutInterceptor) {
                ((WSS4JOutInterceptor) interceptor).setProperties(outProps);
                return;
            }
        }

        client.getOutInterceptors().add(outInterceptor);
        client.getOutInterceptors().add(new HeaderRequestOutInterceptor());
    }

}
