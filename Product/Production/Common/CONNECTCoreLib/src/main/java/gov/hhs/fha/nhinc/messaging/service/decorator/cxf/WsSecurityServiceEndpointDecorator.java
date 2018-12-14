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
package gov.hhs.fha.nhinc.messaging.service.decorator.cxf;

import gov.hhs.fha.nhinc.messaging.client.interceptor.HttpHeaderRequestOutInterceptor;
import gov.hhs.fha.nhinc.messaging.service.ServiceEndpoint;
import gov.hhs.fha.nhinc.messaging.service.decorator.ServiceEndpointDecorator;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.util.Map;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.apache.wss4j.policy.SPConstants;
import org.opensaml.xmlsec.signature.support.SignatureConstants;

/**
 * @author akong
 *
 */
public class WsSecurityServiceEndpointDecorator<T> extends ServiceEndpointDecorator<T> {

    private WsSecurityConfigFactory configFactory = null;
    private String gatewayAlias = null;
    private static final String ASSERTION_PROPERTY_FILE_NAME = "assertioninfo";
    private static final String SIG_ALGO = "saml.SignatureAlgorithm";
    private static final String DIG_ALGO = "saml.DigestAlgorithm";

    /**
     * Constructor.
     *
     * @param decoratoredEndpoint - endpoint instance where this decorator will be applied
     */
    public WsSecurityServiceEndpointDecorator(ServiceEndpoint<T> decoratoredEndpoint, String gatewayAlias) {
        this(decoratoredEndpoint, WsSecurityConfigFactory.getInstance());
        this.gatewayAlias = gatewayAlias;
    }

    /**
     * Constructor with dependency injection parameters.
     *
     * @param decoratoredEndpoint - endpoint instance where this decorator will be applied
     * @param configFactory - factory that produce a config map
     */
    public WsSecurityServiceEndpointDecorator(ServiceEndpoint<T> decoratoredEndpoint,
        WsSecurityConfigFactory configFactory) {
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

        Client client = ClientProxy.getClient(getPort());
        Map<String, Object> outProps = configFactory.getConfiguration(gatewayAlias);
        String salgorithm = PropertyAccessor.getInstance().getProperty(ASSERTION_PROPERTY_FILE_NAME, SIG_ALGO,
            SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1);
        String dalgorithm = PropertyAccessor.getInstance().getProperty(ASSERTION_PROPERTY_FILE_NAME, DIG_ALGO,
            SPConstants.SHA1);
        outProps.put(WSHandlerConstants.SIG_ALGO, salgorithm);
        outProps.put(WSHandlerConstants.SIG_DIGEST_ALGO, dalgorithm);
        configureWSSecurityOnClient(client, outProps);
    }

    private void configureWSSecurityOnClient(Client client, Map<String, Object> outProps) {
        WSS4JOutInterceptor outInterceptor = new WSS4JOutInterceptor(outProps);
        outInterceptor.setAllowMTOM(true);

        for (Interceptor<? extends Message> interceptor : client.getOutInterceptors()) {
            if (interceptor instanceof WSS4JOutInterceptor) {
                ((WSS4JOutInterceptor) interceptor).setProperties(outProps);
                return;
            }
        }

        client.getOutInterceptors().add(outInterceptor);
        client.getOutInterceptors().add(new HttpHeaderRequestOutInterceptor());
    }

}
