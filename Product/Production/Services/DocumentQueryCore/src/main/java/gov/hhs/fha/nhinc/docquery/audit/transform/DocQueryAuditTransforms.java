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
package gov.hhs.fha.nhinc.docquery.audit.transform;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import com.services.nhinc.schema.auditmessage.TypeValuePairType;
import gov.hhs.fha.nhinc.audit.transform.AuditTransforms;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docquery.audit.DocQueryAuditTransformsConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import java.io.ByteArrayOutputStream;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tjafri
 */
public class DocQueryAuditTransforms extends AuditTransforms<AdhocQueryRequest, AdhocQueryResponse> {

    private static final Logger LOG = LoggerFactory.getLogger(DocQueryAuditTransforms.class);

    @Override
    protected AuditMessageType getParticipantObjectIdentificationForRequest(AdhocQueryRequest request,
        AssertionType assertion, AuditMessageType auditMsg) {
        // ParticipantObjectIdentification for Patient is an optional element and can range from 0..1 . If PatientId is
        // not present in the request, the Audit Object will not hold ParticipantObjectIdentification for Patient.
        auditMsg = getPatientParticipantObjectIdentificationForRequest(request, auditMsg);
        try {
            auditMsg = getQueryParamsParticipantObjectIdentificationForRequest(request, auditMsg, getTarget());
        } catch (JAXBException ex) {
            LOG.error("Error while creating ParticipantObjectIdentificationQueryByParameters segment : "
                + ex.getLocalizedMessage(), ex);
        }
        return auditMsg;
    }

    @Override
    protected AuditMessageType getParticipantObjectIdentificationForResponse(AdhocQueryRequest request,
        AdhocQueryResponse response, AssertionType assertion, AuditMessageType auditMsg) {
        auditMsg = getPatientParticipantObjectIdentificationForResponse(request, auditMsg);
        try {
            auditMsg = getQueryParticipantObjectIdentificationForResponse(request, auditMsg);
        } catch (JAXBException ex) {
            LOG.error("Error while creating ParticipantObjectIdentificationQueryByParameters segment : "
                + ex.getLocalizedMessage(), ex);
        }
        return auditMsg;
    }

    @Override
    protected String getServiceEventIdCodeRequestor() {
        return DocQueryAuditTransformsConstants.EVENT_ID_CODE;
    }

    @Override
    protected String getServiceEventIdCodeResponder() {
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

    private AuditMessageType getQueryParamsParticipantObjectIdentificationForRequest(AdhocQueryRequest request,
        AuditMessageType auditMsg, NhinTargetSystemType target) throws JAXBException {
        String respondingHCID = target != null && target.getHomeCommunity() != null ? target.getHomeCommunity()
            .getHomeCommunityId() : null;
            ParticipantObjectIdentificationType participantObj = createQueryParticipantObjectIdentification(
                getQueryIdFromRequest(request), HomeCommunityMap.getHomeCommunityIdWithPrefix(respondingHCID));

            participantObj.setParticipantObjectQuery(getParticipantObjectQueryForRequest(request));
            auditMsg.getParticipantObjectIdentification().add(participantObj);
            return auditMsg;
    }

    // ParticipantObjectIdentification for Patient is an optional element and can range from 0..1 . If PatientId is
    // not present in the request, the Audit Object will not hold ParticipantObjectIdentification for Patient.
    private AuditMessageType getPatientParticipantObjectIdentificationForRequest(AdhocQueryRequest request,
        AuditMessageType auditMsg) {
        String patientId = getPatientIdFromRequest(request);
        if (NullChecker.isNotNullish(patientId)) {
            ParticipantObjectIdentificationType participantObj = createPatientParticipantObjectIdentification(patientId);
            auditMsg.getParticipantObjectIdentification().add(participantObj);
        }
        return auditMsg;
    }

    private AuditMessageType getPatientParticipantObjectIdentificationForResponse(AdhocQueryRequest request,
        AuditMessageType auditMsg) {
        String patientId = getPatientIdFromRequest(request);
        if (NullChecker.isNotNullish(patientId)) {
            ParticipantObjectIdentificationType participantObj = createPatientParticipantObjectIdentification(patientId);
            auditMsg.getParticipantObjectIdentification().add(participantObj);
        }
        return auditMsg;
    }

    private AuditMessageType getQueryParticipantObjectIdentificationForResponse(AdhocQueryRequest request,
        AuditMessageType auditMsg) throws JAXBException {

        ParticipantObjectIdentificationType participantObj = createQueryParticipantObjectIdentification(
            getQueryIdFromRequest(request), HomeCommunityMap.getHomeCommunityIdWithPrefix(HomeCommunityMap.
                getLocalHomeCommunityId()));
        participantObj.setParticipantObjectQuery(getParticipantObjectQueryForRequest(request));
        auditMsg.getParticipantObjectIdentification().add(participantObj);
        return auditMsg;
    }

    private ParticipantObjectIdentificationType createQueryParticipantObjectIdentification(String queryId,
        String hcid) {
        ParticipantObjectIdentificationType participantObject = createParticipantObject(
            DocQueryAuditTransformsConstants.PARTICIPANT_QUERY_OBJ_TYPE_CODE_SYSTEM,
            DocQueryAuditTransformsConstants.PARTICIPANT_QUERY_OBJ_TYPE_CODE_ROLE,
            DocQueryAuditTransformsConstants.PARTICIPANT_QUERY_OBJ_ID_TYPE_CODE,
            DocQueryAuditTransformsConstants.PARTICIPANT_QUERY_OBJ_ID_TYPE_CODE_SYSTEM,
            DocQueryAuditTransformsConstants.PARTICIPANT_QUERY_OBJ_ID_TYPE_DISPLAY_NAME);

        if (NullChecker.isNotNullish(queryId)) {
            participantObject.setParticipantObjectID(queryId);
        }
        participantObject.getParticipantObjectDetail().add(getTypeValuePair(
            DocQueryAuditTransformsConstants.QUERY_ENCODING_TYPE, DocQueryAuditTransformsConstants.UTF_8.getBytes()));
        if (NullChecker.isNotNullish(hcid)) {
            participantObject.getParticipantObjectDetail().add(getTypeValuePair(
                DocQueryAuditTransformsConstants.HOME_COMMUNITY_ID, hcid.getBytes()));
        }
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

        if (NullChecker.isNotNullish(pid)) {
            participantObject.setParticipantObjectID(pid);
        }
        return participantObject;
    }

    private ParticipantObjectIdentificationType createParticipantObject(short objTypeCodeSys,
        short objTypeCodeRole,
        String objIdTypeCode, String objIdTypeCodeSys, String objIdTypeDisplayName) {

        return createParticipantObjectIdentification(objTypeCodeSys, objTypeCodeRole,
            objIdTypeCode, objIdTypeCodeSys, objIdTypeDisplayName);
    }

    private static byte[] getParticipantObjectQueryForRequest(AdhocQueryRequest request) throws JAXBException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (request != null) {
            getMarshaller().marshal(request, baos);
            return baos.toByteArray();
        }
        return null;
    }

    private static Marshaller getMarshaller() throws JAXBException {
        return new JAXBContextHandler().getJAXBContext(NhincConstants.JAXB_HL7_CONTEXT_NAME_XSD_QUERY).createMarshaller();
    }

    private static String getQueryIdFromRequest(AdhocQueryRequest request) {
        if (request != null
            && request.getAdhocQuery() != null
            && request.getAdhocQuery().getId() != null) {
            return request.getAdhocQuery().getId();
        }
        return null;
    }

    private static String getPatientIdFromRequest(AdhocQueryRequest request) {
        if (request != null
            && request.getAdhocQuery() != null
            && request.getAdhocQuery().getSlot() != null
            && !request.getAdhocQuery().getSlot().isEmpty()) {
            for (SlotType1 slot : request.getAdhocQuery().getSlot()) {
                if (slot != null && slot.getName().equals(DocQueryAuditTransformsConstants.XDS_DOCUMENT_ENTRY_PATIENT_ID)
                    && slot.getValueList() != null && NullChecker.isNotNullish(slot.getValueList().getValue())
                    && NullChecker.isNotNullish(slot.getValueList().getValue().get(0))) {
                    return PatientIdFormatUtil.stripQuotesFromPatientId(slot.getValueList().getValue().get(0));
                }
            }
        } else {
            LOG.error("PatientId doesn't exist in the received AdhocQueryRequest message");
        }
        return null;
    }

    private static TypeValuePairType getTypeValuePair(String key, byte[] value) {
        TypeValuePairType type = new TypeValuePairType();
        type.setType(key);
        type.setValue(value);
        return type;
    }
}
