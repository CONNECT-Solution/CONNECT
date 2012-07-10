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

package gov.hhs.fha.messaging.service.decorator.cxf;

import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.ws.security.handler.WSHandlerConstants;

import gov.hhs.fha.messaging.service.ServiceEndpoint;
import gov.hhs.fha.messaging.service.decorator.ServiceEndpointDecorator;

/**
 * @author akong
 *
 */
public class SecurityOutInterceptorServiceEndpointDecorator<T> extends ServiceEndpointDecorator<T> {
    public SecurityOutInterceptorServiceEndpointDecorator(ServiceEndpoint<T> decoratoredEndpoint) {
        super(decoratoredEndpoint);
    }
    
    @Override
    public void configure() {
        super.configure();
        Client client = ClientProxy.getClient(getPort());
        
        Map<String,Object> outProps = new HashMap<String,Object>();
        
        outProps.put(WSHandlerConstants.ACTION, "Timestamp SAMLTokenSigned");
        outProps.put(WSHandlerConstants.TTL_TIMESTAMP, "3600");
        outProps.put(WSHandlerConstants.USER, "gateway");
        outProps.put(WSHandlerConstants.PW_CALLBACK_CLASS, "gov.hhs.fha.nhinc.callback.cxf.CXFPasswordCallbackHandler");    
        outProps.put(WSHandlerConstants.PASSWORD_TYPE, "PasswordDigest");
        outProps.put(WSHandlerConstants.SAML_PROP_FILE, "saml.properties");
        outProps.put(WSHandlerConstants.SIG_PROP_FILE, "keystore.properties");
        outProps.put(WSHandlerConstants.SIG_ALGO, "http://www.w3.org/2000/09/xmldsig#rsa-sha1");
        outProps.put(WSHandlerConstants.SIG_DIGEST_ALGO, "http://www.w3.org/2000/09/xmldsig#sha1");
        outProps.put(WSHandlerConstants.SIGNATURE_PARTS, "{Element}{http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd}Timestamp;");
                        
        WSS4JOutInterceptor outInterceptor = new WSS4JOutInterceptor(outProps);
        client.getOutInterceptors().add(outInterceptor);
    }
}
