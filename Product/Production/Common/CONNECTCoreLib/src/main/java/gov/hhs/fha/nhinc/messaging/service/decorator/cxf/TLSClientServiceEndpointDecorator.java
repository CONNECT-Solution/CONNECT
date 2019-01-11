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
package gov.hhs.fha.nhinc.messaging.service.decorator.cxf;

import gov.hhs.fha.nhinc.util.HomeCommunityMap;

import gov.hhs.fha.nhinc.messaging.service.ServiceEndpoint;
import gov.hhs.fha.nhinc.messaging.service.decorator.ServiceEndpointDecorator;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;

/**
 * @author bhumphrey
 * @param <T>
 */
public class TLSClientServiceEndpointDecorator<T> extends ServiceEndpointDecorator<T> {

    private TLSClientParametersFactory tlsClientFactory;
    protected String certificateAlias = null;
    protected String exchangeName = null;

    /**
     * Constructor.
     *
     * @param decoratored
     * @param assertion
     * @param url
     */
    public TLSClientServiceEndpointDecorator(ServiceEndpoint<T> decoratoredEndpoint, String certificateAlias,
        String exchangeName) {
        this(decoratoredEndpoint, TLSClientParametersFactory.getInstance(), certificateAlias, exchangeName);
    }

    /**
     * Constructor with dependency injection parameters.
     *
     * @param decoratoredEndpoint
     * @param paramFactory
     */
    public TLSClientServiceEndpointDecorator(ServiceEndpoint<T> decoratoredEndpoint,
        TLSClientParametersFactory tlsClientFactory, String certificateAlias, String exchangeName) {
        super(decoratoredEndpoint);
        this.tlsClientFactory = tlsClientFactory;
        this.certificateAlias = certificateAlias;
        this.exchangeName = exchangeName;
    }

    /**
     * This call is not thread safe if the port is a shared instance as it modifies the HTTP Conduit.
     */
    @Override
    public void configure() {
        super.configure();
        // get sni name based on exchnage
        getHttpConduit().setTlsClientParameters(
            tlsClientFactory.getTLSClientParameters(certificateAlias, HomeCommunityMap.getSNIName(exchangeName)));
    }

    protected HTTPConduit getHttpConduit() {
        Client client = ClientProxy.getClient(getPort());
        return (HTTPConduit) client.getConduit();
    }

    /**
     * @return the tlsClientFactory
     */
    public TLSClientParametersFactory getTlsClientFactory() {
        return tlsClientFactory;
    }

}
