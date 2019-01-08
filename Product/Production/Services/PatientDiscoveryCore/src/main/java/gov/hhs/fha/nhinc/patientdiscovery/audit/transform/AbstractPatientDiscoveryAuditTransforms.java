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
package gov.hhs.fha.nhinc.patientdiscovery.audit.transform;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import gov.hhs.fha.nhinc.audit.transform.AuditTransforms;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.audit.PatientDiscoveryAuditTransformsConstants;
import gov.hhs.fha.nhinc.patientdiscovery.parser.PRPAIN201305UV02Parser;
import gov.hhs.fha.nhinc.patientdiscovery.parser.PRPAIN201306UV02Parser;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import java.io.ByteArrayOutputStream;
import java.util.List;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.commons.lang.StringUtils;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;

/**
 * AbstractPatientDiscoveryAuditTransforms encapsulate the common functionality used by both
 * PatientDiscoveryAuditTransforms, PatientDiscoveryDeferredRequestAuditTransforms and
 * PatientDiscoveryDeferredResponseAuditTransforms
 *
 * @author tjafri
 * @param <T>
 * @param <K>
 */
public abstract class AbstractPatientDiscoveryAuditTransforms<T, K> extends AuditTransforms<T, K> {

    protected AuditMessageType createPatientParticipantObjectIdentification(AuditMessageType auditMsg, String aa,
        String patientId) {

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

    protected AuditMessageType getQueryParamsParticipantObjectIdentificationForRequest(PRPAIN201305UV02 request,
        AuditMessageType auditMsg) throws JAXBException {

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
        return new JAXBContextHandler().getJAXBContext(NhincConstants.JAXB_HL7_CONTEXT_NAME_HL7_V3).createMarshaller();
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

    protected AuditMessageType getPatientParticipantObjectIdentificationForResponse(PRPAIN201306UV02 response,
        AuditMessageType auditMsg) {

        List<II> oII = PRPAIN201306UV02Parser.getPatientIds(response);
        if (oII != null && !oII.isEmpty()) {
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

    protected AuditMessageType getQueryParamsParticipantObjectIdentificationForResponse(PRPAIN201306UV02 response,
        AuditMessageType auditMsg) throws JAXBException {

        ParticipantObjectIdentificationType participantObject = buildBaseParticipantObjectIdentificationType(
            PRPAIN201306UV02Parser.getQueryId(response));
        participantObject.setParticipantObjectQuery(getParticipantObjectQueryForResponse(response));
        auditMsg.getParticipantObjectIdentification().add(participantObject);
        return auditMsg;
    }

    protected AuditMessageType getPatientParticipantObjectIdentificationForRequest(PRPAIN201305UV02 request,
        AuditMessageType auditMsg) {

        // Set the Participant Object Id (patient id)
        II oII = PRPAIN201305UV02Parser.getPatientId(request);
        if (oII != null && oII.getRoot() != null && oII.getExtension() != null && !oII.getRoot().isEmpty()
            && !oII.getExtension().isEmpty()) {

            createPatientParticipantObjectIdentification(auditMsg, oII.getRoot(), oII.getExtension());
        } else {
            createPatientParticipantObjectIdentification(auditMsg, null, null);
        }

        return auditMsg;
    }

    private byte[] getParticipantObjectQueryForResponse(PRPAIN201306UV02 response) throws JAXBException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (response != null && response.getControlActProcess() != null
            && response.getControlActProcess().getQueryByParameter() != null) {
            getMarshaller().marshal(response.getControlActProcess().getQueryByParameter(), baos);
        }
        return baos.toByteArray();
    }
}
