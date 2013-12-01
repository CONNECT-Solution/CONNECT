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

package gov.hhs.fha.nhinc.messaging.service.decorator;

import gov.hhs.fha.nhinc.messaging.service.ServiceEndpoint;

import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.log4j.Logger;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.handler.WSHandlerConstants;

public class DigestAuthenticationServiceEndpointDecorator<T> extends ServiceEndpointDecorator<T> {

	private static final Logger LOG = Logger.getLogger(DigestAuthenticationServiceEndpointDecorator.class);
	
	/** 
	 * This is the key to the (Java) system property that contains the password for securing the 
	 * communication between the end point and Connect 
	 * **/
	public static final String WEB_SERVICE_PASSWORD_SYSTEM_PROPERTY = "exchange.gateway.web.service.password";
	/** 
	 * The user name used for web service calls between the end point and Connect
	 */
	public static final String WEB_SERVICE_USER = "ExchangeGatewayUser"; 
	
    public DigestAuthenticationServiceEndpointDecorator(ServiceEndpoint<T> decoratedEndpoint) {
        super(decoratedEndpoint);
    }

    /**
     * Provides digest web service authentication for end points connecting to a remote end point.  
     * The user name is hard coded and the password come from a system property
     * <p>
     * If things go pear shaped, it is possible to disable the digest authentication by removing 
     * the system property or setting it to an empty string  
     */
    @Override
    public void configure() {
		super.configure();
		
		final String webservicePassword = System.getProperty(WEB_SERVICE_PASSWORD_SYSTEM_PROPERTY);
		if (StringUtils.isBlank(webservicePassword)){
			LOG.warn(String.format("The web service system property (%s) was not configured. There will be no security header in the web service request"
					, WEB_SERVICE_PASSWORD_SYSTEM_PROPERTY)); 
			return;
		}
		
		final Client client1 = ClientProxy.getClient(getPort());
		final Endpoint cxfEndpoint = client1.getEndpoint();
		
		final Map<String, Object> outProps = new HashMap<String, Object>();
		outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
		outProps.put(WSHandlerConstants.USER, WEB_SERVICE_USER);
		outProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_DIGEST);
		
		final Map<String, Object> requestContext = ((BindingProvider) getPort()).getRequestContext();
		requestContext.put("password", webservicePassword);
		final WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);
		cxfEndpoint.getOutInterceptors().add(wssOut);
    }
}