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

import gov.hhs.fha.nhinc.util.StreamUtils;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.hl7.fhir.dstu3.formats.IParser;
import org.hl7.fhir.dstu3.formats.JsonParser;
import org.hl7.fhir.dstu3.formats.XmlParser;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

/**
 *
 * @author tjafri
 */
public class ResponseBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(ResponseBuilder.class);

    public static Bundle build(HttpResponse response, MimeType format) throws FhirClientException {
        return processResource(response, format);
    }

    private static Bundle processResource(HttpResponse response, MimeType format) throws FhirClientException {
        Resource resource = unmarshallResponse(response, format);
        Bundle bundle = null;
        if (resource != null) {
            if (resource instanceof OperationOutcome) {
                throw new FhirClientException("Unexpected error received from FHIR Server: {}"
                    + ((OperationOutcome) resource).getIssue().get(0).getDetails().getText());
            } else {
                bundle = (Bundle) resource;
            }
        } else {
            throw new FhirClientException("Null resourcfe returned by FHIR server");
        }
        return bundle;
    }

    private static Resource unmarshallResponse(HttpResponse response, MimeType format) throws FhirClientException {
        InputStream instream = null;
        Resource resource = null;
        if (response != null) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                HttpEntity entity = response.getEntity();
                try {
                    if (null != entity && null != entity.getContent()) {
                        IParser parser = getParser(format);
                        instream = entity.getContent();
                        resource = parser.parse(instream);
                        if (null != resource) {
                            LOG.info("Resource Type received in response: {}", resource.getResourceType());
                        }
                    }
                } catch (FHIRFormatError | IOException ex) {
                    throw new FhirClientException("Unable to parse response, " + ex.getLocalizedMessage(), ex);
                } finally {
                    StreamUtils.closeStreamSilently(instream);
                }
            } else {
                throw new FhirClientException("Null response returned by the server");
            }
        }
        return resource;
    }

    private static IParser getParser(MimeType format) {
        if (MimeType.JSON.equals(format)) {
            return new JsonParser();
        }
        return new XmlParser();
    }

    private ResponseBuilder() {
    }

}
