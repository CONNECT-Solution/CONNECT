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
package gov.hhs.fha.nhinc.util;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docrepository.adapter.DocRepoConstants;
import gov.hhs.fha.nhinc.document.DocumentConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.marshallers.Marshaller;
import gov.hhs.fha.nhinc.wsa.WSAHeaderHelper;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import javax.xml.namespace.QName;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.collections.CollectionUtils;
import org.hl7.v3.PRPAIN201305UV02;
import org.w3c.dom.Element;

/**
 * @author akong
 *
 */
public class MessageGeneratorUtils {

    private static MessageGeneratorUtils INSTANCE = new MessageGeneratorUtils();

    private static final String NHINC_COMMON_CONTEXT = "gov.hhs.fha.nhinc.common.nhinccommon";
    private static final String NHINC_COMMON_URN = "urn:gov:hhs:fha:nhinc:common:nhinccommon";

    private static final String OASIS_QUERY_30_CONTEXT = "oasis.names.tc.ebxml_regrep.xsd.query._3";
    private static final String OASIS_QUERY_30_URN = "urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0";

    private static final String HL7_V3_CONTEXT = "org.hl7.v3";
    private static final String HL7_V3_URN = "urn:hl7-org:v3";

    protected MessageGeneratorUtils() {
    }

    /**
     * Returns the singleton instance of this class.
     *
     * @return the singleton instance
     */
    public static MessageGeneratorUtils getInstance() {
        return INSTANCE;
    }

    /**
     * Converts the first target into a NhinTargetSystemType format.
     *
     * @param targets
     * @return NhinTargetSystemType
     */
    public NhinTargetSystemType convertFirstToNhinTargetSystemType(NhinTargetCommunitiesType targets) {
        NhinTargetSystemType nhinTargetSystem = new NhinTargetSystemType();

        if (targets != null
            && CollectionUtils.isNotEmpty(targets.getNhinTargetCommunity())) {
            nhinTargetSystem.setHomeCommunity(targets.getNhinTargetCommunity().get(0).getHomeCommunity());
            nhinTargetSystem.setUseSpecVersion(targets.getUseSpecVersion());
            nhinTargetSystem.setExchangeName(targets.getExchangeName());
        }

        return nhinTargetSystem;
    }

    /**
     * Converts the first target into a NhinTargetSystemType format.
     *
     * @param target
     * @return NhinTargetSystemType
     */
    public NhinTargetSystemType convertToNhinTargetSystemType(NhinTargetCommunityType target) {
        NhinTargetSystemType nhinTargetSystem = new NhinTargetSystemType();

        if (target != null && target.getHomeCommunity() != null) {
            nhinTargetSystem.setHomeCommunity(target.getHomeCommunity());
        }

        return nhinTargetSystem;
    }

    /**
     * Clones the assertion object.
     *
     * @param assertion
     * @return a cloned assertion
     */
    public AssertionType clone(AssertionType assertion) {
        QName qName = new QName(NHINC_COMMON_URN, "Assertion");
        Marshaller marshaller = new Marshaller();

        Element jaxbElement = marshaller.marshal(assertion, NHINC_COMMON_CONTEXT, qName);

        return (AssertionType) marshaller.unmarshallJaxbElement(jaxbElement, NHINC_COMMON_CONTEXT);
    }

    /**
     * Clones the assertion object but with a new message id
     *
     * @param assertion
     * @return a cloned assertion but with a new message id
     */
    public AssertionType cloneWithNewMsgId(AssertionType assertion) {
        AssertionType newAssertion = clone(assertion);
        newAssertion.setMessageId(new WSAHeaderHelper().generateMessageID());

        return newAssertion;
    }

    /**
     * Clones the Adhoc Query Request.
     *
     * @param adhocQueryRequest
     * @return a cloned adhocQueryRequest
     */
    public AdhocQueryRequest clone(AdhocQueryRequest adhocQueryRequest) {
        QName qName = new QName(OASIS_QUERY_30_URN, "AdhocQueryRequest");
        Marshaller marshaller = new Marshaller();

        Element jaxbElement = marshaller.marshal(adhocQueryRequest, OASIS_QUERY_30_CONTEXT, qName);

        return (AdhocQueryRequest) marshaller.unmarshallJaxbElement(jaxbElement, OASIS_QUERY_30_CONTEXT);
    }

    /**
     * Clones the PRPAIN201305UV02.
     *
     * @param request
     * @return a cloned PRPAIN201305UV02
     */
    public PRPAIN201305UV02 clone(PRPAIN201305UV02 request) {
        QName qName = new QName(HL7_V3_URN, "PRPA_IN201305UV02");
        Marshaller marshaller = new Marshaller();

        Element jaxbElement = marshaller.marshal(request, HL7_V3_CONTEXT, qName);

        return (PRPAIN201305UV02) marshaller.unmarshallJaxbElement(jaxbElement, HL7_V3_CONTEXT);
    }

    /**
     * This message generates a messageId if it is not present and set it in the assertion object. If present it will
     * format the messsageId and return it back.
     * <p>
     * Note: AssertionType is a required element. It can never be null.
     *
     * @param assertion
     * @return messageId
     */
    public AssertionType generateMessageId(AssertionType assertion) {
        WSAHeaderHelper wsaHelper = new WSAHeaderHelper();
        if (assertion != null) {
            if (NullChecker.isNullish(assertion.getMessageId())) {
                assertion.setMessageId(wsaHelper.generateMessageID());
            } else {
                assertion.setMessageId(wsaHelper.fixMessageIDPrefix(assertion.getMessageId()));
            }
        }
        return assertion;
    }

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

    public RegistryResponseType createRegistryResponseSuccess() {
        RegistryResponseType registryResponse = new ObjectFactory().createRegistryResponseType();
        registryResponse.setStatus(DocRepoConstants.XDS_RETRIEVE_RESPONSE_STATUS_SUCCESS);
        return registryResponse;
    }

}
