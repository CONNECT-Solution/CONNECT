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
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

import javax.xml.ws.BindingProvider;

import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.log4j.Logger;

/**
 * @author bhumphrey
 * @param <T>
 * 
 */
public class TimeoutServiceEndpointDecorator<T> extends ServiceEndpointDecorator<T> {
    public static final String CONFIG_KEY_TIMEOUT = "webserviceproxy.timeout";

    private static final Logger LOG = Logger.getLogger(TimeoutServiceEndpointDecorator.class);

    /**
     * @param decorated
     */
    public TimeoutServiceEndpointDecorator(ServiceEndpoint<T> decoratedEndpoint) {
        super(decoratedEndpoint);
    }

    @Override
    public void configure() {
        super.configure();

        HTTPClientPolicy httpClientPolicy = (HTTPClientPolicy) ((BindingProvider) getPort())
                .getRequestContext().get(HTTPClientPolicy.class.getName());

        int timeout = getTimeoutFromConfig();
        httpClientPolicy.setReceiveTimeout(timeout);
        httpClientPolicy.setConnectionTimeout(timeout);
    }

    private int getTimeoutFromConfig() {
        int timeout = 0;
        try {
            String sValue = PropertyAccessor.getInstance().getProperty(CONFIG_KEY_TIMEOUT);
            if (NullChecker.isNotNullish(sValue)) {
                timeout = Integer.parseInt(sValue);
            }
        } catch (PropertyAccessException ex) {
            LOG.warn("Error occurred reading property value from config file (" + CONFIG_KEY_TIMEOUT
                    + ").  Exception: " + ex.toString());
        } catch (NumberFormatException nfe) {
            LOG.warn("Error occurred converting property value: " + CONFIG_KEY_TIMEOUT + ".  Exception: "
                    + nfe.toString());
        }
        return timeout;
    }

}
