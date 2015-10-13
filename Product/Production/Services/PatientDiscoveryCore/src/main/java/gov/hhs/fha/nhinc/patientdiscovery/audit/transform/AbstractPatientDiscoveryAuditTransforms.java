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
package gov.hhs.fha.nhinc.patientdiscovery.audit.transform;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import gov.hhs.fha.nhinc.audit.transform.AuditTransforms;
import gov.hhs.fha.nhinc.patientdiscovery.audit.PatientDiscoveryAuditTransformsConstants;
import gov.hhs.fha.nhinc.patientdiscovery.parser.PRPAIN201305UV02Parser;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import java.io.ByteArrayOutputStream;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.log4j.Logger;
import org.hl7.v3.PRPAIN201305UV02;

/**
 * AbstractPatientDiscoveryAuditTransforms encapsulate the common functionality used by both
 * PatientDiscoveryAuditTransforms and PatientDiscoveryDeferredRequestAuditTransforms
 *
 * @author tjafri
 */
public abstract class AbstractPatientDiscoveryAuditTransforms<T, K> extends AuditTransforms<T, K> {

    private static final Logger LOG = Logger.getLogger(AbstractPatientDiscoveryAuditTransforms.class);
    private static final String JAXB_HL7_CONTEXT_NAME = "org.hl7.v3";

    protected AuditMessageType createPatientParticipantObjectIdentification(AuditMessageType auditMsg, String aa,
        String patientId) {
        LOG.info("createPatientParticipantObjectIdentification()");
        ParticipantObjectIdentificationType participantObject = createParticipantObjectIdentification(
            PatientDiscoveryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_TYPE_CODE_SYSTEM,
            PatientDiscoveryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_TYPE_CODE_ROLE,
            PatientDiscoveryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE,
            PatientDiscoveryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE_SYSTEM,
            PatientDiscoveryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_DISPLAY_NAME);

        if (aa != null && patientId != null && !aa.isEmpty() && !patientId.isEmpty()) {
            participantObject.setParticipantObjectID(createPatientId(aa, patientId));
        }

        auditMsg.getParticipantObjectIdentification().add(participantObject);

        return auditMsg;
    }

    protected static String createPatientId(String assigningAuthId, String patientId) {
        LOG.info("createPatientId()");
        return patientId + "^^^&" + assigningAuthId + "&ISO";
    }

    protected AuditMessageType getQueryParamsParticipantObjectIdentificationForRequest(PRPAIN201305UV02 request,
        AuditMessageType auditMsg) throws JAXBException {
        LOG.info("getQueryParamsParticipantObjectIdentificationForRequest()");
        ParticipantObjectIdentificationType participantObject = buildBaseParticipantObjectIdentificationType(
            PRPAIN201305UV02Parser.getQueryId(request));
        participantObject.setParticipantObjectQuery(getParticipantObjectQueryForRequest(request));
        auditMsg.getParticipantObjectIdentification().add(participantObject);
        return auditMsg;
    }

    protected byte[] getParticipantObjectQueryForRequest(PRPAIN201305UV02 request) throws JAXBException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (request.getControlActProcess() != null && request.getControlActProcess().getQueryByParameter() != null) {
            getMarshaller().marshal(request.getControlActProcess().getQueryByParameter(), baos);
        }
        return baos.toByteArray();
    }

    protected ParticipantObjectIdentificationType buildBaseParticipantObjectIdentificationType(
        String participantObjectId) {
        LOG.info("buildBaseParticipantObjectIdentificationType()");
        ParticipantObjectIdentificationType participantObject = createParticipantObjectIdentification(
            PatientDiscoveryAuditTransformsConstants.PARTICIPANT_QUERYPARAMS_OBJ_TYPE_CODE_SYSTEM,
            PatientDiscoveryAuditTransformsConstants.PARTICIPANT_QUERYPARAMS_OBJ_TYPE_CODE_ROLE,
            PatientDiscoveryAuditTransformsConstants.PARTICIPANT_QUERYPARAMS_OBJ_ID_TYPE_CODE,
            PatientDiscoveryAuditTransformsConstants.PARTICIPANT_QUERYPARAMS_OBJ_ID_TYPE_CODE_SYSTEM,
            PatientDiscoveryAuditTransformsConstants.PARTICIPANT_QUERYPARAMS_OBJ_ID_TYPE_DISPLAY_NAME);
        participantObject.setParticipantObjectID(participantObjectId);
        return participantObject;
    }

    protected Marshaller getMarshaller() throws JAXBException {
        return new JAXBContextHandler().getJAXBContext(JAXB_HL7_CONTEXT_NAME).createMarshaller();
    }

    @Override
    protected String getServiceEventIdCodeRequestor() {
        return PatientDiscoveryAuditTransformsConstants.EVENT_ID_CODE;
    }

    @Override
    protected String getServiceEventIdCodeResponder() {
        return PatientDiscoveryAuditTransformsConstants.EVENT_ID_CODE;
    }

    @Override
    protected String getServiceEventCodeSystem() {
        return PatientDiscoveryAuditTransformsConstants.EVENT_CODE_SYSTEM;
    }

    @Override
    protected String getServiceEventDisplayRequestor() {
        return PatientDiscoveryAuditTransformsConstants.EVENT_CODE_DISPLAY_REQUESTOR;
    }

    @Override
    protected String getServiceEventDisplayResponder() {
        return PatientDiscoveryAuditTransformsConstants.EVENT_CODE_DISPLAY_RESPONDER;
    }

    @Override
    protected String getServiceEventTypeCode() {
        return PatientDiscoveryAuditTransformsConstants.EVENT_TYPE_CODE;
    }

    @Override
    protected String getServiceEventTypeCodeSystem() {
        return PatientDiscoveryAuditTransformsConstants.EVENT_TYPE_CODE_SYSTEM;
    }

    @Override
    protected String getServiceEventTypeCodeDisplayName() {
        return PatientDiscoveryAuditTransformsConstants.EVENT_TYPE_CODE_DISPLAY_NAME;
    }

    @Override
    protected String getServiceEventActionCodeRequestor() {
        return PatientDiscoveryAuditTransformsConstants.EVENT_ACTION_CODE_REQUESTOR;
    }

    @Override
    protected String getServiceEventActionCodeResponder() {
        return PatientDiscoveryAuditTransformsConstants.EVENT_ACTION_CODE_RESPONDER;
    }
}
