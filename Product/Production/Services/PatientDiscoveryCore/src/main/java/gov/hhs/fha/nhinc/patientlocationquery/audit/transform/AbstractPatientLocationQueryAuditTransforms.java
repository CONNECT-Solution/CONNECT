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
package gov.hhs.fha.nhinc.patientlocationquery.audit.transform;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import gov.hhs.fha.nhinc.audit.transform.AuditTransforms;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.patientlocationquery.audit.PatientLocationQueryAuditTransformsConstants;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import ihe.iti.xcpd._2009.PatientLocationQueryRequestType;
import ihe.iti.xcpd._2009.PatientLocationQueryResponseType;
import ihe.iti.xcpd._2009.PatientLocationQueryResponseType.PatientLocationResponse;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hl7.v3.II;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AbstractPatientLocationQueryAuditTransforms encapsulate the common functionality used
 *
 * @author tran tang
 * @param <T>
 * @param <K>
 */
public abstract class AbstractPatientLocationQueryAuditTransforms<T, K> extends AuditTransforms<T, K> {

    private static final String QNAME_PLQ = "urn:ihe:iti:xcpd:2009";
    private static final String PLQ_ELEMENT = "PatientLocationQueryRequest";
    private static final String JAXB_CONTEXT_PLQ = "ihe.iti.xcpd._2009";
    private static final Logger LOG = LoggerFactory.getLogger(AbstractPatientLocationQueryAuditTransforms.class);

    protected AuditMessageType createPatientParticipantObjectIdentification(AuditMessageType auditMsg, String aa,
        String patientId) {

        ParticipantObjectIdentificationType participantObject = createParticipantObjectIdentification(
            PatientLocationQueryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_TYPE_CODE_SYSTEM,
            PatientLocationQueryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_TYPE_CODE_ROLE,
            PatientLocationQueryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE,
            PatientLocationQueryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE_SYSTEM,
            PatientLocationQueryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_DISPLAY_NAME);

        if (StringUtils.isNotBlank(patientId) && StringUtils.isNotBlank(aa)) {
            participantObject.setParticipantObjectID(createPatientId(aa, patientId));
        }

        auditMsg.getParticipantObjectIdentification().add(participantObject);

        return auditMsg;
    }

    protected static String createPatientId(String assigningAuthId, String patientId) {
        return patientId + "^^^&" + assigningAuthId + "&ISO";
    }

    @Override
    protected abstract AuditMessageType getParticipantObjectIdentificationForRequest(T request, AssertionType assertion,
        AuditMessageType auditMsg);

    /**
     * Adds Participant Object Identification information to auditMsg
     *
     * @param request
     * @param response
     * @param assertion
     * @param auditMsg
     * @return
     */
    @Override
    protected abstract AuditMessageType getParticipantObjectIdentificationForResponse(T request, K response,
        AssertionType assertion, AuditMessageType auditMsg);

    protected AuditMessageType getQueryParamsParticipantObjectIdentificationForRequest(
        PatientLocationQueryRequestType request,
        AuditMessageType auditMsg) throws JAXBException {

        ParticipantObjectIdentificationType participantObject = buildBaseParticipantObjectIdentificationType(
            PatientLocationQueryAuditTransformsConstants.PARTICIPANT_QUERYPARAMS_OBJ_ID);
        participantObject.setParticipantObjectQuery(getParticipantObjectQueryForRequest(request));
        auditMsg.getParticipantObjectIdentification().add(participantObject);
        return auditMsg;
    }

    protected byte[] getParticipantObjectQueryForRequest(PatientLocationQueryRequestType request) throws JAXBException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (request != null) {
            QName xmlqnamePLQ = new javax.xml.namespace.QName(QNAME_PLQ, PLQ_ELEMENT);
            JAXBElement<PatientLocationQueryRequestType> jaxbRequest = new JAXBElement<>(xmlqnamePLQ,
                PatientLocationQueryRequestType.class, request);
            getMarshaller().marshal(jaxbRequest, baos);
        }
        return baos.toByteArray();
    }

    protected ParticipantObjectIdentificationType buildBaseParticipantObjectIdentificationType(
        String participantObjectId) {

        ParticipantObjectIdentificationType participantObject = createParticipantObjectIdentification(
            PatientLocationQueryAuditTransformsConstants.PARTICIPANT_QUERYPARAMS_OBJ_TYPE_CODE_SYSTEM,
            PatientLocationQueryAuditTransformsConstants.PARTICIPANT_QUERYPARAMS_OBJ_TYPE_CODE_ROLE,
            PatientLocationQueryAuditTransformsConstants.PARTICIPANT_QUERYPARAMS_OBJ_ID_TYPE_CODE,
            PatientLocationQueryAuditTransformsConstants.PARTICIPANT_QUERYPARAMS_OBJ_ID_TYPE_CODE_SYSTEM,
            PatientLocationQueryAuditTransformsConstants.PARTICIPANT_QUERYPARAMS_OBJ_ID_TYPE_DISPLAY_NAME);
        participantObject.setParticipantObjectID(participantObjectId);
        return participantObject;
    }

    private static II getPatientIdFromRequest(PatientLocationQueryRequestType request) {
        II livingSubjectId = null;
        if (request != null && request.getRequestedPatientId() != null) {
            livingSubjectId = request.getRequestedPatientId();
        } else {
            LOG.error("PatientId doesn't exist in the received PRPAIN201305UV02 message");
        }

        return livingSubjectId;
    }

    protected AuditMessageType getPatientParticipantObjectIdentificationForRequest(
        PatientLocationQueryRequestType request,
        AuditMessageType auditMsg) {

        // Set the Participant Object Id (patient id)
        II oII = getPatientIdFromRequest(request);
        if (oII != null && StringUtils.isNotBlank(oII.getRoot()) && StringUtils.isNotBlank(oII.getExtension())) {
            createPatientParticipantObjectIdentification(auditMsg, oII.getRoot(), oII.getExtension());
        } else {
            createPatientParticipantObjectIdentification(auditMsg, null, null);
        }

        return auditMsg;
    }

    protected Marshaller getMarshaller() throws JAXBException {
        return new JAXBContextHandler().getJAXBContext(JAXB_CONTEXT_PLQ).createMarshaller();
    }

    @Override
    protected String getServiceEventIdCodeRequestor() {
        return PatientLocationQueryAuditTransformsConstants.EVENT_ID_CODE;
    }

    @Override
    protected String getServiceEventIdCodeResponder() {
        return PatientLocationQueryAuditTransformsConstants.EVENT_ID_CODE;
    }

    @Override
    protected String getServiceEventCodeSystem() {
        return PatientLocationQueryAuditTransformsConstants.EVENT_CODE_SYSTEM;
    }

    @Override
    protected String getServiceEventDisplayRequestor() {
        return PatientLocationQueryAuditTransformsConstants.EVENT_CODE_DISPLAY_REQUESTOR;
    }

    @Override
    protected String getServiceEventDisplayResponder() {
        return PatientLocationQueryAuditTransformsConstants.EVENT_CODE_DISPLAY_RESPONDER;
    }

    @Override
    protected String getServiceEventTypeCode() {
        return PatientLocationQueryAuditTransformsConstants.EVENT_TYPE_CODE;
    }

    @Override
    protected String getServiceEventTypeCodeSystem() {
        return PatientLocationQueryAuditTransformsConstants.EVENT_TYPE_CODE_SYSTEM;
    }

    @Override
    protected String getServiceEventTypeCodeDisplayName() {
        return PatientLocationQueryAuditTransformsConstants.EVENT_TYPE_CODE_DISPLAY_NAME;
    }

    @Override
    protected String getServiceEventActionCodeRequestor() {
        return PatientLocationQueryAuditTransformsConstants.EVENT_ACTION_CODE_REQUESTOR;
    }

    @Override
    protected String getServiceEventActionCodeResponder() {
        return PatientLocationQueryAuditTransformsConstants.EVENT_ACTION_CODE_RESPONDER;
    }

    private static List<II> getPatientIds(PatientLocationQueryResponseType response) {
        List<II> oIIs = new ArrayList<>();
        if (response != null && CollectionUtils.isNotEmpty(response.getPatientLocationResponse())) {
            for (PatientLocationResponse subject : response.getPatientLocationResponse()) {
                if (subject != null && subject.getCorrespondingPatientId() != null) {
                    oIIs.add(subject.getCorrespondingPatientId());
                }
            }
        } else {
            LOG.error("No PatientId found in the received PatientLocationQueryResponse-message");
        }
        return oIIs;
    }

    protected AuditMessageType getPatientParticipantObjectIdentificationForResponse(
        PatientLocationQueryResponseType response, AuditMessageType auditMsg) {

        List<II> oII = getPatientIds(response);
        if (CollectionUtils.isNotEmpty(oII)) {
            for (II entry : oII) {
                if (entry == null) {
                    createPatientParticipantObjectIdentification(auditMsg, null, null);
                } else {
                    createPatientParticipantObjectIdentification(auditMsg,
                        StringUtils.isNotBlank(entry.getRoot()) ? entry.getRoot().trim() : null,
                        StringUtils.isNotBlank(entry.getExtension()) ? entry.getExtension().trim() : null);
                }
            }
        } else {
            createPatientParticipantObjectIdentification(auditMsg, null, null);
        }

        return auditMsg;
    }
}
