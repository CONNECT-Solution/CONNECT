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
package gov.hhs.fha.nhinc.admingui.client.fhir;

import gov.hhs.fha.nhinc.util.StreamUtils;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hl7.fhir.instance.client.EFhirClientException;
import org.hl7.fhir.instance.client.FeedFormat;
import org.hl7.fhir.instance.client.ResourceAddress;
import org.hl7.fhir.instance.client.ResourceRequest;
import org.hl7.fhir.instance.client.TagParser;
import org.hl7.fhir.instance.formats.JsonParser;
import org.hl7.fhir.instance.formats.Parser;
import org.hl7.fhir.instance.formats.XmlParser;
import org.hl7.fhir.instance.model.AtomCategory;
import org.hl7.fhir.instance.model.AtomEntry;
import org.hl7.fhir.instance.model.Conformance;
import org.hl7.fhir.instance.model.OperationOutcome;
import org.hl7.fhir.instance.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Modified FHIR client taken from reference client used strictly for Conformance resource lookup.
 *
 * @see <a href="https://github.com/cnanjo/FhirJavaReferenceClient">https://github.com/cnanjo/FhirJavaReferenceClient
 *      </a>
 * @author jassmit
 */
public class ConformanceClient {

    public static final String DEFAULT_CHARSET = "utf-8";

    private static final Logger LOG = LoggerFactory.getLogger(ConformanceClient.class);

    public Conformance getConformanceStatement(String baseServiceUrl) throws URISyntaxException {
        ResourceAddress resourceAddress = new ResourceAddress(baseServiceUrl);
        return (Conformance) issueGetResourceRequest(resourceAddress.resolveMetadataUri(),
            ResourceFormat.RESOURCE_XML.getHeader()).getResource();
    }

    protected static <T extends Resource> ResourceRequest<T> issueGetResourceRequest(URI resourceUri, String format) {
        HttpGet httpget = new HttpGet(resourceUri);
        return issueResourceRequest(format, httpget);
    }

    protected static <T extends Resource> ResourceRequest<T> issueResourceRequest(String format,
        HttpUriRequest request) {

        configureFhirRequest(format, request);
        HttpResponse response = sendRequest(request);

        T resource = unmarshalResource(response, format);
        AtomEntry<T> atomEntry = buildAtomEntry(response, resource);
        return new ResourceRequest<>(atomEntry, response.getStatusLine().getStatusCode());
    }

    protected static void configureFhirRequest(String resourceFormat, HttpRequest request) {
        request.addHeader("User-Agent", "Java FHIR Client for FHIR");

        LOG.info("Resource format: " + resourceFormat + ", Feed Format: " + FeedFormat.FEED_XML.getHeader());
        request.addHeader("Accept", resourceFormat + ", " + FeedFormat.FEED_XML.getHeader());
        request.addHeader("Content-Type", ResourceFormat.RESOURCE_XML.getHeader() + ";charset=" + DEFAULT_CHARSET);
        request.addHeader("Accept-Charset", DEFAULT_CHARSET);
        // Added the following to pull conformance from HAPI.
        request.addHeader("Accept-Encoding", "deflate");
    }

    protected static HttpResponse sendRequest(HttpUriRequest request) {
        HttpResponse response;
        LOG.info("Conformance request method: {}", request.getURI().getQuery());
        try(CloseableHttpClient client = HttpClientBuilder.create().build()) {

            response = client.execute(request);
        } catch (IOException ioe) {
            throw new EFhirClientException("Error sending Http Request", ioe);
        }
        return response;
    }

    @SuppressWarnings("unchecked")
    protected static <T extends Resource> T unmarshalResource(HttpResponse response, String format) {
        T resource = null;
        InputStream instream = null;
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            try {
                instream = entity.getContent();
                resource = (T) getParser(format).parse(instream);
            } catch (IOException ioe) {
                throw new EFhirClientException("Error unmarshalling entity from Http Response", ioe);
            } catch (Exception e) {
                throw new EFhirClientException("Error parsing response message", e);
            } finally {
                StreamUtils.closeStreamSilently(instream);
            }
        }

        if (resource instanceof OperationOutcome) {
            if (CollectionUtils.isNotEmpty(((OperationOutcome) resource).getIssue())) {
                throw new EFhirClientException((OperationOutcome) resource);
            } else {
                LOG.debug(((OperationOutcome) resource).getText().getDiv().allText());
            }
        }

        return resource;
    }

    protected static Parser getParser(String format) {
        if (StringUtils.isBlank(format)) {
            format = ResourceFormat.RESOURCE_XML.getHeader();
        }
        if (format.equalsIgnoreCase("json") || format.equalsIgnoreCase(ResourceFormat.RESOURCE_JSON.getHeader())
            || format.equalsIgnoreCase(FeedFormat.FEED_JSON.getHeader())) {

            return new JsonParser();
        } else if (format.equalsIgnoreCase("xml") || format.equalsIgnoreCase(ResourceFormat.RESOURCE_XML.getHeader())
            || format.equalsIgnoreCase(FeedFormat.FEED_XML.getHeader())) {

            return new XmlParser();
        } else {
            throw new EFhirClientException("Invalid format: " + format);
        }
    }

    protected static <T extends Resource> AtomEntry<T> buildAtomEntry(HttpResponse response, T resource) {
        AtomEntry<T> entry = new AtomEntry<>();
        String location = null;

        if (response.getHeaders("location").length > 0) {// TODO Distinguish between both cases if necessary
            location = response.getHeaders("location")[0].getValue();
        } else if (response.getHeaders("content-location").length > 0) {
            location = response.getHeaders("content-location")[0].getValue();
        }

        if (location != null) {
            entry.getLinks().put("self", location);// TODO Make sure this is right.
        }

        List<AtomCategory> tags = parseTags(response);
        entry.getTags().addAll(tags);
        entry.setResource(resource);

        return entry;
    }

    protected static List<AtomCategory> parseTags(HttpResponse response) {
        List<AtomCategory> tags = new ArrayList<>();
        Header[] categoryHeaders = response.getHeaders("Category");

        for (Header categoryHeader : categoryHeaders) {
            if (categoryHeader == null || categoryHeader.getValue().trim().isEmpty()) {
                continue;
            }
            List<AtomCategory> categories = new TagParser().parse(categoryHeader.getValue());
            tags.addAll(categories);
        }

        return tags;
    }
}
