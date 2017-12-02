/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.fhir;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.SSLContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tjafri
 */
public class FhirClient {

    private static final Logger LOG = LoggerFactory.getLogger(FhirClient.class);
    private static final String SEPARATOR = ",";

    public HttpResponse sendRequest(HttpUriRequest request, List<String> tlsVersion) throws FhirClientException {
        LOG.info("sendRequest: {}", request);
        HttpResponse response;
        try {
            HttpClientBuilder client = HttpClientBuilder.create().setSSLSocketFactory(createSSLConnectionSocketFactory(
                tlsVersion));
            response = client.build().execute(request);

        } catch (IOException | PropertyAccessException | NoSuchAlgorithmException ex) {
            throw new FhirClientException("Error sending Http Request: " + ex.getLocalizedMessage(), ex);
        }
        return response;
    }

    private String[] getTLSVersionsFromProperties() {
        try {
            String versions = PropertyAccessor.getInstance().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.FHIR_TLS);
            return versions.split(SEPARATOR);
        } catch (PropertyAccessException ex) {
            LOG.error("Unable to retrieve {} from {}: {}", NhincConstants.FHIR_TLS,
                NhincConstants.GATEWAY_PROPERTY_FILE, ex.getLocalizedMessage(), ex);
        }
        return null;
    }

    private SSLConnectionSocketFactory createSSLConnectionSocketFactory(List<String> tlsVersion) throws
        PropertyAccessException, NoSuchAlgorithmException {
        String[] tlsArray;
        if (CollectionUtils.isNotEmpty(tlsVersion)) {
            tlsArray = Arrays.copyOf(tlsVersion.toArray(), tlsVersion.size(), String[].class);
        } else {
            tlsArray = getTLSVersionsFromProperties();
        }
        SSLContext sslContext = SSLContexts.createDefault();
        if (null == tlsArray) {
            tlsArray = new String[]{sslContext.getProtocol()};
        }
        SSLConnectionSocketFactory sslFactory = new SSLConnectionSocketFactory(sslContext, tlsArray, null,
            new NoopHostnameVerifier());
        return sslFactory;
    }
}
