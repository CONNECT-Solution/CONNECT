/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.docquery.audit.transform;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import com.services.nhinc.schema.auditmessage.TypeValuePairType;
import gov.hhs.fha.nhinc.audit.transform.AuditTransforms;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docquery.audit.DocQueryAuditTransformsConstants;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.io.StringWriter;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import org.apache.log4j.Logger;
import org.apache.ws.security.util.Base64;

/**
 *
 * @author tjafri
 */
public class DocQueryAuditTransforms extends AuditTransforms<AdhocQueryRequest, AdhocQueryResponse> {

    private static final Logger LOG = Logger.getLogger(DocQueryAuditTransforms.class);
    private static final String JAXB_HL7_CONTEXT_NAME = "oasis.names.tc.ebxml_regrep.xsd.query._3";

    @Override
    protected AuditMessageType getParticipantObjectIdentificationForRequest(AdhocQueryRequest request, AssertionType assertion, AuditMessageType auditMsg) {
        // ParticipantObjectIdentification for Patient is an optional element and can range from 0..1 . If PatientId is
        // not present in the request, the Audit Object will not hold ParticipantObjectIdentification for Patient.
        auditMsg = getPatientParticipantObjectIdentificationForRequest(request, auditMsg);
        try {
            auditMsg = getQueryParamsParticipantObjectIdentificationForRequest(request, auditMsg);
        } catch (JAXBException ex) {
            LOG.error("Error while creating ParticipantObjectIdentificationQueryByParameters segment : "
                + ex.getLocalizedMessage(), ex);
        }
        return auditMsg;
    }

    @Override
    protected AuditMessageType getParticipantObjectIdentificationForResponse(AdhocQueryRequest request, AdhocQueryResponse response,
        AssertionType assertion, AuditMessageType auditMsg) {
        auditMsg = getPatientParticipantObjectIdentificationForResponse(request, response, auditMsg);
        try {
            auditMsg = getQueryParticipantObjectIdentificationForResponse(request, response, auditMsg);
        } catch (JAXBException ex) {
            LOG.error("Error while creating ParticipantObjectIdentificationQueryByParameters segment : "
                + ex.getLocalizedMessage(), ex);
        }
        return auditMsg;

    }

    @Override
    protected String getServiceEventIdCode() {
        return DocQueryAuditTransformsConstants.EVENT_ID_CODE;
    }

    @Override
    protected String getServiceEventCodeSystem() {
        return DocQueryAuditTransformsConstants.EVENT_CODE_SYSTEM;
    }

    @Override
    protected String getServiceEventDisplayRequestor() {
        return DocQueryAuditTransformsConstants.EVENT_CODE_DISPLAY_REQUESTOR;
    }

    @Override
    protected String getServiceEventDisplayResponder() {
        return DocQueryAuditTransformsConstants.EVENT_CODE_DISPLAY_RESPONDER;
    }

    @Override
    protected String getServiceEventTypeCode() {
        return DocQueryAuditTransformsConstants.EVENT_TYPE_CODE;
    }

    @Override
    protected String getServiceEventTypeCodeSystem() {
        return DocQueryAuditTransformsConstants.EVENT_TYPE_CODE_SYSTEM;
    }

    @Override
    protected String getServiceEventTypeCodeDisplayName() {
        return DocQueryAuditTransformsConstants.EVENT_TYPE_CODE_DISPLAY_NAME;
    }

    @Override
    protected String getServiceEventActionCodeRequestor() {
        return DocQueryAuditTransformsConstants.EVENT_ACTION_CODE_REQUESTOR;
    }

    @Override
    protected String getServiceEventActionCodeResponder() {
        return DocQueryAuditTransformsConstants.EVENT_ACTION_CODE_RESPONDER;
    }

    private AuditMessageType getQueryParamsParticipantObjectIdentificationForRequest(AdhocQueryRequest request, AuditMessageType auditMsg) throws JAXBException {
        ParticipantObjectIdentificationType participantObject = createQueryParticipantObjectIdentification(getQueryIdFromRequest(request));
        participantObject.setParticipantObjectQuery(getParticipantObjectQueryForRequest(request));
        auditMsg.getParticipantObjectIdentification().add(participantObject);
        return auditMsg;
    }

    // ParticipantObjectIdentification for Patient is an optional element and can range from 0..1 . If PatientId is not present in
    // the request, the Audit Object will not hold ParticipantObjectIdentification for Patient.
    private AuditMessageType getPatientParticipantObjectIdentificationForRequest(AdhocQueryRequest request, AuditMessageType auditMsg) {
        String patientId = getPatientIdFromRequest(request);
        if (patientId != null) {
            ParticipantObjectIdentificationType participantObject = createPatientParticipantObjectIdentification(patientId);
            auditMsg.getParticipantObjectIdentification().add(participantObject);
        }
        return auditMsg;
    }

    private AuditMessageType getPatientParticipantObjectIdentificationForResponse(AdhocQueryRequest request, AdhocQueryResponse response, AuditMessageType auditMsg) {
        String patientId = getPatientIdFromRequest(request);
        if (patientId != null) {
            ParticipantObjectIdentificationType participantObject = createPatientParticipantObjectIdentification(patientId);
            auditMsg.getParticipantObjectIdentification().add(participantObject);
        }
        return auditMsg;

    }

    private AuditMessageType getQueryParticipantObjectIdentificationForResponse(AdhocQueryRequest request, AdhocQueryResponse response, AuditMessageType auditMsg) throws JAXBException {
        ParticipantObjectIdentificationType participantObject = createQueryParticipantObjectIdentification(getQueryIdFromRequest(request));
        participantObject.setParticipantObjectQuery(getParticipantObjectQueryForRequest(request));
        auditMsg.getParticipantObjectIdentification().add(participantObject);
        return auditMsg;
    }

    private ParticipantObjectIdentificationType createQueryParticipantObjectIdentification(String queryId) {
        ParticipantObjectIdentificationType participantObject = createParticipantObject(
            DocQueryAuditTransformsConstants.PARTICIPANT_QUERY_OBJ_TYPE_CODE_SYSTEM,
            DocQueryAuditTransformsConstants.PARTICIPANT_QUERY_OBJ_TYPE_CODE_ROLE,
            DocQueryAuditTransformsConstants.PARTICIPANT_QUERY_OBJ_ID_TYPE_CODE,
            DocQueryAuditTransformsConstants.PARTICIPANT_QUERY_OBJ_ID_TYPE_CODE_SYSTEM,
            DocQueryAuditTransformsConstants.PARTICIPANT_QUERY_OBJ_ID_TYPE_DISPLAY_NAME);

        if (queryId != null && !queryId.isEmpty()) {
            participantObject.setParticipantObjectID(queryId);
        }
        participantObject.setParticipantObjectName(HomeCommunityMap.formatHomeCommunityId(
            HomeCommunityMap.getLocalHomeCommunityId()));
        TypeValuePairType encoding = new TypeValuePairType();
        encoding.setType(DocQueryAuditTransformsConstants.QUERY_ENCODING_TYPE);
        encoding.setValue(DocQueryAuditTransformsConstants.UTF_8.getBytes());
        participantObject.getParticipantObjectDetail().add(encoding);
        //TODO is the homeCommunityId of responding gateway ???
        TypeValuePairType homeCommunityTypeValue = new TypeValuePairType();
        homeCommunityTypeValue.setType(DocQueryAuditTransformsConstants.HOME_COMMUNITY_ID);
        homeCommunityTypeValue.setValue(HomeCommunityMap.formatHomeCommunityId(
            HomeCommunityMap.getLocalHomeCommunityId()).getBytes());
        participantObject.getParticipantObjectDetail().add(homeCommunityTypeValue);

        return participantObject;
    }

    //This is same for both Request and Response in case of DQ
    private ParticipantObjectIdentificationType createPatientParticipantObjectIdentification(String pid) {

        ParticipantObjectIdentificationType participantObject = createParticipantObject(
            DocQueryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_TYPE_CODE_SYSTEM,
            DocQueryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_TYPE_CODE_ROLE,
            DocQueryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE,
            DocQueryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE_SYSTEM,
            DocQueryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_DISPLAY_NAME);

        if (pid != null && !pid.isEmpty()) {
            participantObject.setParticipantObjectID(pid);
        }

        return participantObject;
    }

    private ParticipantObjectIdentificationType createParticipantObject(short objTypeCodeSys, short objTypeCodeRole,
        String objIdTypeCode, String objIdTypeCodeSys,
        String objIdTypeDisplayName) {

        return createParticipantObjectIdentification(objTypeCodeSys, objTypeCodeRole,
            objIdTypeCode, objIdTypeCodeSys, objIdTypeDisplayName);
    }

    private byte[] getParticipantObjectQueryForRequest(AdhocQueryRequest request) throws JAXBException {
        StringWriter sw = new StringWriter();
        if (request != null) {
            getMarshaller().marshal(request, sw);
            String encodedStr = Base64.encode(sw.toString().getBytes());
            return encodedStr.getBytes();
        }
        return null;
    }

    private Marshaller getMarshaller() throws JAXBException {
        return new JAXBContextHandler().getJAXBContext(JAXB_HL7_CONTEXT_NAME).createMarshaller();
    }

    private String getQueryIdFromRequest(AdhocQueryRequest request) {
        if (request != null
            && request.getAdhocQuery() != null
            && request.getAdhocQuery().getId() != null) {
            return request.getAdhocQuery().getId();
        }
        return null;
    }

    private String getPatientIdFromRequest(AdhocQueryRequest request) {
        if (request != null
            && request.getAdhocQuery() != null
            && request.getAdhocQuery().getSlot() != null
            && !request.getAdhocQuery().getSlot().isEmpty()) {
            for (SlotType1 slot : request.getAdhocQuery().getSlot()) {
                if (slot.getName().equals(DocQueryAuditTransformsConstants.XDS_DOCUMENT_ENTRY_PATIENT_ID)) {
                    String value = slot.getValueList().getValue().toString();
                    return value.replaceAll("\\[|\\]", "");
                }
            }
        } else {
            LOG.error("PatientId doesn't exist in the received AdhocQueryRequest message");
        }
        return null;
    }
}
