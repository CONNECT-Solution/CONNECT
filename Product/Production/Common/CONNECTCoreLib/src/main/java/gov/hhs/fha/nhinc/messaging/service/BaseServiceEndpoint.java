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
package gov.hhs.fha.nhinc.messaging.service;

import java.util.Map;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.SOAPBinding;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

/**
 * @author akong
 *
 */
public class BaseServiceEndpoint<T> implements ServiceEndpoint<T> {

    protected T port;

    public BaseServiceEndpoint(T port) {
        this.port = port;
    }

    @Override
    public void configure() {
        // DO NOTHING
    }

    @Override
    public T getPort() {
        return this.port;
    }

    @Override
    final public HTTPClientPolicy getHTTPClientPolicy() {
        return getHTTPClientPolicy((BindingProvider) getPort());
    }

    @Override
    final public SOAPBinding getSOAPBinding() {
        return (SOAPBinding) ((BindingProvider) getPort()).getBinding();
    }

    /**
     * Get the {@link HTTPClientPolicy} from the request context of bindingProvider, creates the
     * {@link HTTPClientPolicy} if null.
     *
     * @param bindingProvider provider to get the {@link HTTPClientPolicy} from
     * @return the existing {@link HTTPClientPolicy} or a new one.
     */
    HTTPClientPolicy getHTTPClientPolicy(BindingProvider bindingProvider) {
        Map<String, Object> requestContext = bindingProvider.getRequestContext();
        HTTPClientPolicy httpClientPolicy = (HTTPClientPolicy) requestContext.get(HTTPClientPolicy.class.getName());

        if (httpClientPolicy == null) {
            httpClientPolicy = new HTTPClientPolicy();
            requestContext.put(HTTPClientPolicy.class.getName(), httpClientPolicy);
        }

        return httpClientPolicy;
    }

}
