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
package gov.hhs.fha.nhinc.messaging.service.decorator;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.messaging.service.ServiceEndpoint;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import javax.xml.ws.BindingProvider;
import org.apache.cxf.transports.http.configuration.ConnectionType;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class will allow users to turn on/off KeepAlive feature in http header by detecing keepalive tag inside
 * assertion block. This also allow users to turn on/off KeepAlive inside gateway.properties. However, the assertion
 * element will take precedence over the gateway.properties. All values are in seconds. To turn off, either remove
 * keepAlive elements or using integer < 1
 *
 * @author mpnguyen
 */
public class KeepAliveServiceEndpointDecorator<T> extends ServiceEndpointDecorator<T> {
    private static final String KEEP_ALIVE = "keepAliveDuration";
    private final AssertionType assertion;
    private final String serviceName;
    private static final Logger LOG = LoggerFactory.getLogger(KeepAliveServiceEndpointDecorator.class);
    /**
     * @param decoratedEndpoint
     */
    public KeepAliveServiceEndpointDecorator(final ServiceEndpoint<T> decoratedEndpoint, final AssertionType assertion,
            final String serviceName) {
        super(decoratedEndpoint);
        this.assertion = assertion;
        this.serviceName = serviceName;
        LOG.debug("Initialize KeepAliveServiceEndpointDecorator {}", serviceName);
    }
    @Override
    public void configure() {
        super.configure();
        final HTTPClientPolicy httpClientPolicy = getHTTPClientPolicy();
        final BindingProvider bindingProvider = (BindingProvider) getPort();
        // reset keepAlive duration
        bindingProvider.getRequestContext().remove(NhincConstants.KEEPALIVE_DURATION);
        //keepalive from assertion block has precedence over the default properties.
        // KeepAlive Null means user is not aware of this feature. Will turn on if default indicates so.
        // KeepAlive < 0 mean user explicitly wants to turn off regardless default value
        // KeepAlive > 0 mean user explicitly wants to turn on with indicated value.
        final Integer keepAliveAssertion = assertion.getKeepAlive();
        final StringBuilder logMsgBuilder = new StringBuilder("KeepAlive statement");
        logMsgBuilder.append(" for ");
        logMsgBuilder.append(this.serviceName);
        logMsgBuilder.append(" service ");
        if (keepAliveAssertion == null) {// msg comes empty
            final int kAFromProp = getDefaultKeepAliveFromConfig(serviceName);
            logMsgBuilder.append("Detect Empty Keep-Alive from assertion ");
            if (kAFromProp > 0) {
                httpClientPolicy.setConnection(ConnectionType.KEEP_ALIVE);
                logMsgBuilder.append("but default properties wants to have Keep Alive with duration ");
                logMsgBuilder.append(kAFromProp);
                logMsgBuilder.append(" seconds");
                bindingProvider.getRequestContext().put(NhincConstants.KEEPALIVE_DURATION, kAFromProp);
            } else {
                httpClientPolicy.setConnection(null);
                logMsgBuilder.append(" and default properties also wants it off.");
            }
        } else if (keepAliveAssertion.intValue() < 0) { // user wants it off
            httpClientPolicy.setConnection(null);
            logMsgBuilder.append("user explicitly wants to turn off regardless default value ");
        } else {
            httpClientPolicy.setConnection(ConnectionType.KEEP_ALIVE);
            logMsgBuilder.append("user explicitly wants to turn on with duration ");
            logMsgBuilder.append(keepAliveAssertion.intValue());
            logMsgBuilder.append(" seconds");
            bindingProvider.getRequestContext().put(NhincConstants.KEEPALIVE_DURATION, keepAliveAssertion);
        }

        LOG.debug(logMsgBuilder.toString());
    }

    /**
     * Retrieve keepAliveDuration for each service in gateway properties.
     *
     * @param service Service name
     * @return keepAliveDuration (in seconds) if found. Otherwise, return 0
     */
    public final int getDefaultKeepAliveFromConfig(final String service) {
        int defaultKeepAlive = 0;
        final StringBuilder keepAliveKey = new StringBuilder();
        try {
            keepAliveKey.append(service);
            keepAliveKey.append(".");
            keepAliveKey.append(KEEP_ALIVE);
            final String sValue = PropertyAccessor.getInstance().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                    keepAliveKey.toString());
            if (NullChecker.isNotNullish(sValue)) {
                defaultKeepAlive = Integer.parseInt(sValue);
            }
        } catch (final PropertyAccessException ex) {
            LOG.warn("Error occurred reading property value from config file ({}): {}", service,
                    ex.getLocalizedMessage(), ex);
        } catch (final NumberFormatException nfe) {
            LOG.warn("Error occurred converting property value from config file ({}): {}", keepAliveKey,
                    nfe.getLocalizedMessage(), nfe);
        }
        return defaultKeepAlive;
    }

}
