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
import gov.hhs.fha.nhinc.cryptostore.StoreUtil;
import gov.hhs.fha.nhinc.messaging.service.BaseServiceEndpoint;
import gov.hhs.fha.nhinc.messaging.service.ServiceEndpoint;
import java.util.HashMap;
import java.util.Map;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.addressing.WSAddressingFeature;

/**
 * @author msweaver
 *
 */
public class CachingCXFWSAServicePortBuilder<T> extends CachingCXFServicePortBuilder<T> {

    private static Map<String, Map<Class<?>, Object>> CACHED_PORTS = new HashMap<>();

    /**
     * Constructor.
     *
     * @param portDescriptor
     */
    public CachingCXFWSAServicePortBuilder(ServicePortDescriptor<T> portDescriptor) {
        super(portDescriptor);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.messaging.service.port.CachingCXFServicePortBuilder#getCache()
     */
    @Override
    protected Map<Class<?>, Object> getCache(String gatewayAlias) {
        String defaultAlias = StoreUtil.getGatewayAliasDefaultTo(gatewayAlias);
        if (CACHED_PORTS.get(defaultAlias) == null) {
            CACHED_PORTS.put(defaultAlias, new HashMap<Class<?>, Object>());
        }

        return CACHED_PORTS.get(defaultAlias);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.messaging.service.port.CachingCXFServicePortBuilder#configurePort(java.lang.Object)
     */
    @Override
    protected void configurePort(T port, AssertionType assertion) {
        super.configurePort(port, assertion);

        ServiceEndpoint<T> serviceEndpoint = new BaseServiceEndpoint<>(port);
        serviceEndpoint.configure();
    }

    @Override
    protected void configureJaxWsProxyFactory(JaxWsProxyFactoryBean factory) {
        super.configureJaxWsProxyFactory(factory);
        factory.getFeatures().add(new WSAddressingFeature());
    }
}
