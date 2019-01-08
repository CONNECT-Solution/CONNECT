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
package gov.hhs.fha.nhinc.messaging.service.decorator;

import gov.hhs.fha.nhinc.messaging.service.ServiceEndpoint;
import javax.xml.ws.soap.SOAPBinding;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

/**
 * @author akong
 *
 */
public abstract class ServiceEndpointDecorator<T> implements ServiceEndpoint<T> {
    protected ServiceEndpoint<T> decoratedEndpoint;

    public ServiceEndpointDecorator(ServiceEndpoint<T> decoratedEndpoint) {
        this.decoratedEndpoint = decoratedEndpoint;
    }

    /**
     * This method will configure the service endpoint according to the decorator. Any implementation of this method
     * must take into account that the endpoint may be a static resource.
     */
    @Override
    public void configure() {
        decoratedEndpoint.configure();
    }

    @Override
    public T getPort() {
        return decoratedEndpoint.getPort();
    }

    @Override
    public HTTPClientPolicy getHTTPClientPolicy() {
        return decoratedEndpoint.getHTTPClientPolicy();
    }

    @Override
    public SOAPBinding getSOAPBinding() {
        return decoratedEndpoint.getSOAPBinding();
    }

}
