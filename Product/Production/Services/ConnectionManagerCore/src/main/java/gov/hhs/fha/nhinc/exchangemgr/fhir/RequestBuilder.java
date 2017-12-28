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
package gov.hhs.fha.nhinc.exchangemgr.fhir;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tjafri
 */
public class RequestBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(RequestBuilder.class);

    public HttpGet get(String url) throws URISyntaxException {
        return buildRequest(url, MimeType.XML);
    }

    public HttpGet get(String url, MimeType mimeType) throws URISyntaxException {
        return buildRequest(url, mimeType);
    }

    public HttpGet get(String uri, Map<String, String> queryParam, MimeType mimeType) throws URISyntaxException {
        return buildRequest(buildRequestURLWithQueryString(uri, queryParam), mimeType);
    }

    private static HttpGet buildRequest(String url, MimeType format) throws URISyntaxException {
        URI requestUri = new URI(url);
        HttpGet request = new HttpGet(requestUri);
        addRequestHeaders(format, request);
        return request;
    }

    private static void addRequestHeaders(MimeType mimeType, HttpRequest request) {
        request.addHeader("User-Agent", "CONNECT FHIR Client");

        LOG.info("Resource format: " + mimeType.getMimeType());
        request.addHeader("Accept", mimeType.getMimeType());
        request.addHeader("Content-Type", mimeType.getMimeType() + ";charset=" + FhirConstants.DEFAULT_CHARSET);
        request.addHeader("Accept-Charset", FhirConstants.DEFAULT_CHARSET);
    }

    private static String buildRequestURLWithQueryString(String uri, Map<String, String> queryParam) {
        StringBuilder builder = new StringBuilder();
        builder.append(uri);
        if (null != queryParam) {
            builder.append("/?");
            Iterator<String> it = queryParam.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                builder.append(key);
                builder.append("=");
                builder.append(queryParam.get(key));
                if (it.hasNext()) {
                    builder.append("&");
                }
            }
        }
        return builder.toString();
    }
}
