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
package gov.hhs.fha.nhinc.exchangemgr.fhir;

import org.apache.commons.collections.CollectionUtils;
import org.hl7.fhir.dstu3.formats.IParser;
import org.hl7.fhir.dstu3.formats.JsonParser;
import org.hl7.fhir.dstu3.formats.XmlParser;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Resource;

/**
 *
 * @author tjafri
 */
public class ResponseBuilder {

    private ResponseBuilder() {
    }

    public static Bundle build(String response, MimeType format) throws FHIRDataParserException {
        Resource resource = parseResource(response, format);
        if (null != resource) {
            if (resource instanceof OperationOutcome) {
                throw new FHIRDataParserException("Response returned is not valid exchange data {}"
                    + getErrorMessageFromOperationOutcome((OperationOutcome) resource));
            }
            return (Bundle) resource;
        }
        throw new FHIRDataParserException("Unable to parse, response returned is not valid exchange data {}"
            + getErrorMessageFromOperationOutcome((OperationOutcome) resource));
    }

    private static Resource parseResource(String response, MimeType format) throws FHIRDataParserException {
        try {
            IParser parser = getParser(format);
            return parser.parse(response);
        } catch (Exception ex) { //catching general exception to avoid sceanrios where we have service running but no response
            throw new FHIRDataParserException("Unable to parse response,  " + ex.getLocalizedMessage(), ex);
        }
    }

    private static IParser getParser(MimeType format) {
        if (MimeType.JSON.equals(format)) {
            return new JsonParser();
        }
        return new XmlParser();
    }

    private static String getErrorMessageFromOperationOutcome(OperationOutcome op) {
        if (null != op && CollectionUtils.isNotEmpty(op.getIssue()) && null != op.getIssue().get(0) && null != op.
            getIssue().get(0).getDetails()) {
            return op.getIssue().get(0).getDetails().getText();
        }
        return "";
    }
}
