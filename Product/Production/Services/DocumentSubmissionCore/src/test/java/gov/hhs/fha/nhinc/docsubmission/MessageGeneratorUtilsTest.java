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
package gov.hhs.fha.nhinc.docsubmission;

import static org.junit.Assert.assertEquals;

import gov.hhs.fha.nhinc.document.DocumentConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.util.MessageGeneratorUtils;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.junit.Test;

/**
 * @author akong
 *
 */
public class MessageGeneratorUtilsTest {

    String errorMsg = "errorMsg";
    String errorCode = "errorCode";
    String status = "status";


    @Test
    public void testCreateRegistryErrorResponse() {
        MessageGeneratorUtils msgUtils = MessageGeneratorUtils.getInstance();

        RegistryResponseType response = msgUtils.createRegistryErrorResponse(errorMsg, errorCode, status);
        RegistryError regError = response.getRegistryErrorList().getRegistryError().get(0);

        assertEquals(response.getStatus(), status);
        assertEquals(response.getRegistryErrorList().getHighestSeverity(),
            NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);
        assertEquals(regError.getErrorCode(), errorCode);
        assertEquals(regError.getCodeContext(), errorMsg);
        assertEquals(regError.getSeverity(), NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);
    }

    @Test
    public void testCreateRegistryErrorResponse_NoArguments() {
        MessageGeneratorUtils msgUtils = MessageGeneratorUtils.getInstance();

        RegistryResponseType response = msgUtils.createRegistryErrorResponse();
        RegistryError regError = response.getRegistryErrorList().getRegistryError().get(0);

        assertEquals(response.getStatus(), DocumentConstants.XDS_SUBMISSION_RESPONSE_STATUS_FAILURE);
        assertEquals(response.getRegistryErrorList().getHighestSeverity(),
            NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);
        assertEquals(regError.getErrorCode(), DocumentConstants.XDS_REGISTRY_ERROR);
        assertEquals(regError.getSeverity(), NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);
    }

    @Test
    public void testCreateMissingDocumentRegistryResponse() {
        MessageGeneratorUtils msgUtils = MessageGeneratorUtils.getInstance();
        RegistryResponseType response = msgUtils.createMissingDocumentRegistryResponse();
        validateMissingDocumentRegistryResponse(response);
    }

    @Test
    public void testCreateMissingDocumentXDRAcknowledgementType() {
        MessageGeneratorUtils msgUtils = MessageGeneratorUtils.getInstance();
        XDRAcknowledgementType response = msgUtils.createMissingDocumentXDRAcknowledgementType();
        validateMissingDocumentRegistryResponse(response.getMessage());
    }

    @Test
    public void testCreateRegistryErrorResponseWithAckFailure() {
        MessageGeneratorUtils msgUtils = MessageGeneratorUtils.getInstance();
        RegistryResponseType response = msgUtils.createRegistryErrorResponseWithAckFailure(errorMsg);
        validateRegistryErrorResponseWithAckFailure(response);
    }

    @Test
    public void testCreateRegistryErrorXDRAcknowledgementType() {
        MessageGeneratorUtils msgUtils = MessageGeneratorUtils.getInstance();
        XDRAcknowledgementType response = msgUtils.createRegistryErrorXDRAcknowledgementType(errorMsg);
        validateRegistryErrorResponseWithAckFailure(response.getMessage());
    }

    private void validateMissingDocumentRegistryResponse(RegistryResponseType response) {
        RegistryError regError = response.getRegistryErrorList().getRegistryError().get(0);

        assertEquals(response.getStatus(), DocumentConstants.XDS_SUBMISSION_RESPONSE_STATUS_FAILURE);
        assertEquals(response.getRegistryErrorList().getHighestSeverity(),
            NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);
        assertEquals(regError.getErrorCode(), DocumentConstants.XDS_MISSING_DOCUMENT);
        assertEquals(regError.getSeverity(), NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);
    }

    private void validateRegistryErrorResponseWithAckFailure(RegistryResponseType response) {

        RegistryError regError = response.getRegistryErrorList().getRegistryError().get(0);

        assertEquals(response.getStatus(), NhincConstants.XDR_ACK_FAILURE_STATUS_MSG);
        assertEquals(response.getRegistryErrorList().getHighestSeverity(),
            NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);
        assertEquals(regError.getErrorCode(), DocumentConstants.XDS_REGISTRY_ERROR);
        assertEquals(regError.getSeverity(), NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);

    }

}
