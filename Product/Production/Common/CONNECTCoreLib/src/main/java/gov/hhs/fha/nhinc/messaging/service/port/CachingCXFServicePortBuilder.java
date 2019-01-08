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
import gov.hhs.fha.nhinc.messaging.service.BaseServiceEndpoint;
import gov.hhs.fha.nhinc.messaging.service.ServiceEndpoint;
import gov.hhs.fha.nhinc.messaging.service.decorator.cxf.SoapResponseServiceEndpointDecorator;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bhumphrey
 * @param <T>
 *
 */
public abstract class CachingCXFServicePortBuilder<T> extends CXFServicePortBuilder<T> {
    private static final Logger LOG = LoggerFactory.getLogger(CachingCXFServicePortBuilder.class);

    /**
     * This is a CXF configuration that will ensure that the request context are thread local and will not be shared
     * between sessions. See Apache CXF FAQ about ports being thread-safe:
     *
     * http://cxf.apache.org/faq.html
     */
    private static String THREAD_LOCAL_REQUEST_CONTEXT = "thread.local.request.context";

    /**
     * Returns the cache map of the ports.
     *
     * @return the cache map
     */
    protected abstract Map<Class<?>, Object> getCache(String gatewayAlias);

    /**
     * Constructor.
     *
     * @param portDescriptor
     */
    public CachingCXFServicePortBuilder(ServicePortDescriptor<T> portDescriptor) {
        super(portDescriptor);
    }

    /**
     * Configures the given port for all configuration that is not invocation specific. Since the port used is going to
     * be cached and reused, this method should configure everything on the port that is only needed to be applied once.
     * All non-thread safe configuration should be done here.
     *
     * The following configuration to the port are considered not thread safe as they will be shared between all
     * threads: 1. Interceptors 2. Modifying the HTTP Conduit
     *
     * The following configuration to the port are considered local and are thread safe if configured properly: 1. The
     * request context if configured for thread local 2. HTTPClientPolicy BUT only through the request context
     *
     * @param port The port to be configured
     * @param assertion
     */
    protected void configurePort(T port, AssertionType assertion) {
        ServiceEndpoint<T> serviceEndpoint = new BaseServiceEndpoint<>(port);
        serviceEndpoint = new SoapResponseServiceEndpointDecorator<>(serviceEndpoint);
        serviceEndpoint.configure();
    }

    /**
     * Returns a new port or one from the cache. The port will be configured for thread safety and reuse.
     */
    @SuppressWarnings("unchecked")
    @Override
    public synchronized T createPort(String gatewayAlias, AssertionType assertion) {
        LOG.debug("createPort-gatewayAlias: {}", gatewayAlias);
        T port = (T) getCache(gatewayAlias).get(serviceEndpointClass);
        if (port == null) {
            port = super.createPort(gatewayAlias, assertion);
            ((BindingProvider) port).getRequestContext().put(THREAD_LOCAL_REQUEST_CONTEXT, Boolean.TRUE);

            configurePort(port, assertion);

            getCache(gatewayAlias).put(serviceEndpointClass, port);
        }

        return port;
    }
}
