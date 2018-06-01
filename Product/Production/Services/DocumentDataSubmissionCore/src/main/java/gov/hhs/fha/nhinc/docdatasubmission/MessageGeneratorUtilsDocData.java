/*
 * Copyright (c) 2009-2018, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.docdatasubmission;

import gov.hhs.fha.nhinc.document.DocumentConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

public class MessageGeneratorUtilsDocData extends gov.hhs.fha.nhinc.util.MessageGeneratorUtils {

    private static MessageGeneratorUtilsDocData INSTANCE = new MessageGeneratorUtilsDocData();

    MessageGeneratorUtilsDocData() {
    };

    /**
     * Returns the singleton instance of this class.
     *
     * @return the singleton instance
     */
    public static MessageGeneratorUtilsDocData getInstance() {
        return INSTANCE;
    }

    /**
     * Create a RegistryErrorResponse with severity set to error.
     *
     * @param errorMsg - the code context value of the message
     * @param errorCode - the error code value of the message
     * @param status - the status of the message
     * @return the generated RegistryErrorResponse message
     */
    public RegistryResponseType createRegistryErrorResponse(String errorMsg, String errorCode, String status) {
        RegistryErrorList regErrList = new RegistryErrorList();
        regErrList.setHighestSeverity(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);

        RegistryError regErr = new RegistryError();
        regErrList.getRegistryError().add(regErr);
        regErr.setCodeContext(errorMsg);
        regErr.setErrorCode(errorCode);
        regErr.setSeverity(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);

        RegistryResponseType response = new RegistryResponseType();
        response.setRegistryErrorList(regErrList);
        response.setStatus(status);

        return response;
    }

    /**
     * Create a RegistryErrorResponse with severity set to error. The errorCode is set to policy check error.
     *
     * @return the generated RegistryErrorResponse message
     */
    public RegistryResponseType createFailedPolicyCheckResponse() {
        return createRegistryErrorResponse(DocumentConstants.XDR_POLICY_ERROR_CONTEXT,
            DocumentConstants.XDR_POLICY_ERROR, DocumentConstants.XDS_SUBMISSION_RESPONSE_STATUS_FAILURE);
    }

    /**
     * Create a RegistryErrorResponse with severity set to error. The errorCode is set to registry error and status set
     * to failure.
     *
     * @return the generated RegistryErrorResponse message
     */
    public RegistryResponseType createRegistryErrorResponse() {
        return createRegistryErrorResponse("Failed to retrieve document from request.",
            DocumentConstants.XDS_REGISTRY_ERROR, DocumentConstants.XDS_SUBMISSION_RESPONSE_STATUS_FAILURE);
    }

    /**
     * Create a XDRAcknowledgementType containing a RegistryErrorResponse with severity set to error. The errorCode is
     * set to registry error and status set to failure.
     *
     * @return the generated XDRAcknowledgementType message
     */
    public XDRAcknowledgementType createXDRAckWithRegistryErrorResponse() {
        XDRAcknowledgementType response = new XDRAcknowledgementType();
        response.setMessage(createRegistryErrorResponse());

        return response;
    }

    /**
     * Create a RegistryErrorResponse with severity set to error. The error code is set to missing document and status
     * set to failure.
     *
     * @return the generated RegistryResponseType message
     */
    public RegistryResponseType createMissingDocumentRegistryResponse() {
        return createRegistryErrorResponse("Failed to retrieve document for sending.",
            DocumentConstants.XDS_MISSING_DOCUMENT, DocumentConstants.XDS_SUBMISSION_RESPONSE_STATUS_FAILURE);
    }

    /**
     * Create a RegistryErrorResponse with severity set to error. The error code is set to registry error and the status
     * set to ack failure.
     *
     * @param errorMsg
     * @return the generated RegistryResponseType message
     */
    public RegistryResponseType createRegistryErrorResponseWithAckFailure(String errorMsg) {
        return createRegistryErrorResponse(errorMsg, DocumentConstants.XDS_REGISTRY_ERROR,
            NhincConstants.XDR_ACK_FAILURE_STATUS_MSG);
    }

    /**
     * Create a RegistryErrorResponse with severity set to error. The error code is set to registry busy and the status
     * set to failure.
     *
     * @param errorMsg
     * @return the generated RegistryResponseType message
     */
    public RegistryResponseType createRegistryBusyErrorResponse(String errorMsg) {
        return createRegistryErrorResponse(errorMsg, DocumentConstants.XDS_REGISTRY_BUSY,
            DocumentConstants.XDS_SUBMISSION_RESPONSE_STATUS_FAILURE);
    }

    /**
     * Create a XDRAcknowledgementType with a message containing a RegistryErrorResponse with an ack failure status.
     *
     * @param errorMsg
     * @return the generated XDRAcknowledgementType message
     */
    public XDRAcknowledgementType createRegistryErrorXDRAcknowledgementType(String errorMsg) {
        XDRAcknowledgementType response = new XDRAcknowledgementType();

        RegistryResponseType regResponse = createRegistryErrorResponseWithAckFailure(errorMsg);
        response.setMessage(regResponse);

        return response;
    }

    /**
     * Create a XDRAcknowledgementType with a message containing a RegistryErrorResponse with a missing document error
     * code.
     *
     * @return the generated XDRAcknowledgementType message
     */
    public XDRAcknowledgementType createMissingDocumentXDRAcknowledgementType() {
        XDRAcknowledgementType response = new XDRAcknowledgementType();

        RegistryResponseType regResponse = createMissingDocumentRegistryResponse();
        response.setMessage(regResponse);

        return response;
    }

    /**
     * Create a XDRAcknowledgementType with a message containing a RegistryErrorResponse with a policy check error.
     *
     * @return the generated XDRAcknowledgementType message
     */
    public XDRAcknowledgementType createFailedPolicyCheckXDRAcknowledgementType() {
        XDRAcknowledgementType response = new XDRAcknowledgementType();

        RegistryResponseType regResponse = createFailedPolicyCheckResponse();
        response.setMessage(regResponse);

        return response;
    }
}
