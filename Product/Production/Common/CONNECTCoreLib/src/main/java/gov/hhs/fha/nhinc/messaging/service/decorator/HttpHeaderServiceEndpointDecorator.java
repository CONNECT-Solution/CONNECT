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

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CONNECTCustomHttpHeadersType;
import gov.hhs.fha.nhinc.messaging.service.ServiceEndpoint;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.util.HashMap;
import java.util.Set;
import javax.xml.ws.BindingProvider;
import org.apache.commons.collections.MapUtils;
import org.apache.cxf.transports.http.configuration.ConnectionType;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jassmit
 * @param <T>
 */
public class HttpHeaderServiceEndpointDecorator<T> extends ServiceEndpointDecorator<T> {

    private final AssertionType assertion;

    private static final Logger LOG = LoggerFactory.getLogger(HttpHeaderServiceEndpointDecorator.class);

    public HttpHeaderServiceEndpointDecorator(ServiceEndpoint<T> decoratedEndpoint, final AssertionType assertion) {
        super(decoratedEndpoint);
        this.assertion = assertion;
    }

    @Override
    public void configure() {
        super.configure();
        final HTTPClientPolicy httpClientPolicy = getHTTPClientPolicy();
        final BindingProvider bindingProvider = (BindingProvider) getPort();

        //Clear http connection value.
        httpClientPolicy.setConnection(null);
        if (hasKeepAliveForConnection()) {
            httpClientPolicy.setConnection(ConnectionType.KEEP_ALIVE);
        }

        bindingProvider.getRequestContext().remove(NhincConstants.CUSTOM_HTTP_HEADERS);
        HashMap<String, String> httpHeaders = getCustomHeaders();

        if (MapUtils.isNotEmpty(httpHeaders)) {
            bindingProvider.getRequestContext().put(NhincConstants.CUSTOM_HTTP_HEADERS, httpHeaders);
        }
    }

    private boolean hasKeepAliveForConnection() {
        String keepAlive = assertion.getKeepAlive();
        if (NullChecker.isNullish(keepAlive)) {
            keepAlive = getKeepAliveProperty();
        }

        return "TRUE".equalsIgnoreCase(keepAlive) || "T".equalsIgnoreCase(keepAlive);
    }

    private String getKeepAliveProperty() {
        try {
            return getPropertyAccessor().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.KEEP_ALIVE_PROP);
        } catch (PropertyAccessException ex) {
            LOG.warn("Unable to access property: {} {}", NhincConstants.KEEP_ALIVE_PROP, ex.getLocalizedMessage(),ex);
            return null;
        }
    }

    private HashMap<String, String> getCustomHeaders() {
        HashMap<String, String> customHeaders = new HashMap<>();
        try {
            Set<String> allPropNames = getPropertyAccessor().getPropertyNames(NhincConstants.GATEWAY_PROPERTY_FILE);

            if (NullChecker.isNotNullish(allPropNames)) {
                addPropertiesToCustomHeaders(allPropNames, customHeaders);
            }
        } catch (PropertyAccessException ex) {
            LOG.warn("Unable to access properties for custom http headers.{}", ex.getLocalizedMessage(),ex);
        }

        for (CONNECTCustomHttpHeadersType header : assertion.getCONNECTCustomHttpHeaders()) {
            customHeaders.put(header.getHeaderName(), header.getHeaderValue());
        }

        return customHeaders;
    }

    private void addPropertiesToCustomHeaders(Set<String> allPropNames, HashMap<String, String> customHeaders) throws PropertyAccessException {
        for (String propName : allPropNames) {
            if (NullChecker.isNotNullish(propName) && propName.startsWith(NhincConstants.CUSTOM_HTTP_HEADERS)) {
                String propValue = getPropertyAccessor().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, propName);
                propName = trimPropertyName(propName);
                if (NullChecker.isNotNullish(propValue) && NullChecker.isNotNullish(propName)) {
                    customHeaders.put(propName, propValue);
                }
            }
        }
    }

    protected PropertyAccessor getPropertyAccessor() {
        return PropertyAccessor.getInstance();
    }

    private static String trimPropertyName(String propName) {
        if ((propName.length()) > (NhincConstants.CUSTOM_HTTP_HEADERS.length() + 1)) {
            return propName.substring(NhincConstants.CUSTOM_HTTP_HEADERS.length() + 1).trim();
        } else {
            return null;
        }
    }

}
