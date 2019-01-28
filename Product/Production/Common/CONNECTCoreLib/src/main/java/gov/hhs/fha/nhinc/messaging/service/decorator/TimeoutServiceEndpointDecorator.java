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
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import javax.xml.ws.BindingProvider;
import org.apache.cxf.endpoint.ClientImpl;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bhumphrey
 * @param <T>
 *
 */
public class TimeoutServiceEndpointDecorator<T> extends ServiceEndpointDecorator<T> {

    public static final String CONFIG_KEY_TIMEOUT = "webserviceproxy.timeout";

    private static final Logger LOG = LoggerFactory.getLogger(TimeoutServiceEndpointDecorator.class);

    private final int transactionTimeout;

    /**
     * @param decoratedEndpoint
     * @param timeout
     */
    public TimeoutServiceEndpointDecorator(ServiceEndpoint<T> decoratedEndpoint, Integer timeout) {
        super(decoratedEndpoint);
        if (timeout != null) {
            this.transactionTimeout = timeout;
        } else {
            this.transactionTimeout = -1;
        }
    }

    @Override
    public void configure() {
        super.configure();

        HTTPClientPolicy httpClientPolicy = getHTTPClientPolicy();

        int timeout;
        if (transactionTimeout > 0) {
            timeout = transactionTimeout;
        } else {
            timeout = getTimeoutFromConfig();
        }
        ((BindingProvider) getPort()).getRequestContext().put(ClientImpl.SYNC_TIMEOUT, timeout);
        httpClientPolicy.setReceiveTimeout(timeout);
        httpClientPolicy.setConnectionTimeout(timeout);
    }

    int getTimeoutFromConfig() {
        int timeout = 0;
        try {
            String sValue = PropertyAccessor.getInstance().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                CONFIG_KEY_TIMEOUT);
            if (NullChecker.isNotNullish(sValue)) {
                timeout = Integer.parseInt(sValue);
            }
        } catch (PropertyAccessException ex) {
            LOG.warn("Error occurred reading property value from config file ({}): {}", CONFIG_KEY_TIMEOUT,
                ex.getLocalizedMessage(), ex);
        } catch (NumberFormatException nfe) {
            LOG.warn("Error occurred converting property value from config file ({}): {}", CONFIG_KEY_TIMEOUT,
                nfe.getLocalizedMessage(), nfe);
        }
        return timeout;
    }

}
