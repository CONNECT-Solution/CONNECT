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
import gov.hhs.fha.nhinc.audit.transform.AuditTransform;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.patientdiscovery.audit.PatientDiscoveryTransformConstants;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.io.ByteArrayOutputStream;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.log4j.Logger;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;

/**
 *
 * @author achidamb
 */
public class PatientDiscoveryTransforms  extends AuditTransform<PRPAIN201305UV02, PRPAIN201306UV02> {

    private static final Logger LOG = Logger.getLogger(PatientDiscoveryTransforms.class);

    /**
     *
     * @param assertion
     * @param auditMsg
     * @return
     */
    @Override
    protected AuditMessageType getParticipantObjectIdentificationForRequest(PRPAIN201305UV02 request, AssertionType assertion,
        AuditMessageType auditMsg) {
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
    protected AuditMessageType getParticipantObjectIdentificationForResponse(PRPAIN201306UV02 response, AssertionType assertion,
        AuditMessageType auditMsg) {
        auditMsg = getPatientParticipantObjectIdentificationForResponse(response, auditMsg);
        try {
            auditMsg = getQueryParamsParticipantObjectIdentificationForResponse(response, auditMsg);
        } catch (JAXBException ex) {
            LOG.error("Error while creating ParticipantObjectIdentificationQueryByParameters segment : "
                + ex.getLocalizedMessage(), ex);
        }
        return auditMsg;
    }

    private AuditMessageType getPatientParticipantObjectIdentificationForRequest(PRPAIN201305UV02 request, AuditMessageType auditMsg) {
        // Set the Partipation Object Id (patient id)
        II oII = getPatientIdFromRequest(request);
        if (oII != null && oII.getRoot() != null && oII.getExtension() != null && !oII.getRoot().isEmpty()
            && !oII.getExtension().isEmpty()) {
            createPatientParticipantObjectIdentification(auditMsg, oII.getRoot(), oII.getExtension());
        }else {
            createPatientParticipantObjectIdentification(auditMsg, null, null);
        }
        return auditMsg;
    }

    private AuditMessageType getPatientParticipantObjectIdentificationForResponse(PRPAIN201306UV02 response, AuditMessageType auditMsg) {
        List<II> oII = getPatientIdFromResponse(response);
        if (oII != null && oII.size() > 0) {
            for (II entry : oII) {
                if (entry != null && entry.getRoot() != null && entry.getExtension() != null
                    && !entry.getRoot().isEmpty() && !entry.getExtension().isEmpty()) {
                    createPatientParticipantObjectIdentification(auditMsg, entry.getRoot(), entry.getExtension());
                }
            }
        } else {
            createPatientParticipantObjectIdentification(auditMsg, null, null);
        }
        return auditMsg;
    }

    private II getPatientIdFromRequest(PRPAIN201305UV02 request) {
        II oII = null;

        if (request != null && request.getControlActProcess() != null
            && request.getControlActProcess().getQueryByParameter() != null
            && request.getControlActProcess().getQueryByParameter().getValue() != null
            && request.getControlActProcess().getQueryByParameter().getValue().
            getParameterList() != null
            && request.getControlActProcess().getQueryByParameter().getValue().getParameterList().
            getLivingSubjectId() != null
            && request.getControlActProcess().getQueryByParameter().getValue().getParameterList().
            getLivingSubjectId().get(0) != null
            && request.getControlActProcess().getQueryByParameter().getValue().getParameterList().
            getLivingSubjectId().get(0).getValue() != null
            && request.getControlActProcess().getQueryByParameter().getValue().getParameterList().
            getLivingSubjectId().get(0).getValue().get(0) != null) {
            oII = request.getControlActProcess().getQueryByParameter().getValue().getParameterList().
                getLivingSubjectId().get(0).getValue().get(0);
        } else {
            LOG.error("PatientId doesn't exists in the received PRPAIN201305UV02 message");
        }
        return oII;
    }

    private List<II> getPatientIdFromResponse(PRPAIN201306UV02 response) {
        List<II> oIIs = null;
        if (response != null && response.getControlActProcess() != null
            && response.getControlActProcess().getSubject() != null) {
            List<PRPAIN201306UV02MFMIMT700711UV01Subject1> oSubject1 = response.getControlActProcess().getSubject();
            for (PRPAIN201306UV02MFMIMT700711UV01Subject1 subject : oSubject1) {
                if (subject.getRegistrationEvent() != null && subject.getRegistrationEvent().getSubject1() != null
                    && subject.getRegistrationEvent().getSubject1().getPatient() != null
                    && subject.getRegistrationEvent().getSubject1().getPatient().getId() != null) {
                    oIIs = subject.getRegistrationEvent().getSubject1().getPatient().getId();
                } else {
                    LOG.error("PatientId doesn't exists in the received PRPAIN201306UV02 message");
                }
            }
        }
        return oIIs;
    }

    private AuditMessageType createPatientParticipantObjectIdentification(AuditMessageType auditMsg, String aa,
        String patientId) {
        ParticipantObjectIdentificationType participantObject = createParticipantObjectIdentification(
            PatientDiscoveryTransformConstants.PARTICIPANT_PATIENT_OBJ_TYPE_CODE_SYSTEM,
            PatientDiscoveryTransformConstants.PARTICIPANT_PATIENT_OBJ_TYPE_CODE_ROLE,
            PatientDiscoveryTransformConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE,
            PatientDiscoveryTransformConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE_SYSTEM,
            PatientDiscoveryTransformConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_DISPLAY_NAME);
        if (aa != null && patientId != null && !aa.isEmpty() && !patientId.isEmpty()) {
            participantObject.setParticipantObjectID(createPatientId(aa, patientId));
            auditMsg.getParticipantObjectIdentification().add(participantObject);
        } else {
            auditMsg.getParticipantObjectIdentification().add(participantObject);
        }
        return auditMsg;
    }

    private static String createPatientId(String assigningAuthId, String patientId) {
        return patientId + "^^^&" + assigningAuthId + "&ISO";

    }

    private AuditMessageType getQueryParamsParticipantObjectIdentificationForRequest(PRPAIN201305UV02 request, AuditMessageType auditMsg)
        throws JAXBException {
        ParticipantObjectIdentificationType participantObject = buildBaseParticipantObjectIdentificationType();
        byte[] byteArray = getParticipantObjectQueryForRequest(request);
        if (byteArray != null) {
            participantObject.setParticipantObjectQuery(byteArray);
        }
        auditMsg.getParticipantObjectIdentification().add(participantObject);
        return auditMsg;
    }

    private AuditMessageType getQueryParamsParticipantObjectIdentificationForResponse(PRPAIN201306UV02 response, AuditMessageType auditMsg)
        throws JAXBException {
        ParticipantObjectIdentificationType participantObject = buildBaseParticipantObjectIdentificationType();
        byte[] byteArray = getParticipantObjectQueryForResponse(response);
        if (byteArray != null) {
            participantObject.setParticipantObjectQuery(byteArray);
        }
        auditMsg.getParticipantObjectIdentification().add(participantObject);
        return auditMsg;
    }

    private byte[] getParticipantObjectQueryForRequest(PRPAIN201305UV02 request) throws JAXBException {
        ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
        baOutStrm.reset();
        if (request.getControlActProcess() != null && request.getControlActProcess().
            getQueryByParameter() != null) {
            getMarshaller().marshal(request.getControlActProcess().getQueryByParameter(), baOutStrm);
        }
        return baOutStrm.toByteArray();
    }

    private byte[] getParticipantObjectQueryForResponse(PRPAIN201306UV02 response) throws JAXBException {
        ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
        baOutStrm.reset();
        if (response.getControlActProcess() != null && response.getControlActProcess().
            getQueryByParameter() != null) {
            getMarshaller().marshal(response.getControlActProcess().getQueryByParameter(), baOutStrm);
        }
        return baOutStrm.toByteArray();
    }

    private Marshaller getMarshaller() throws JAXBException {
        String JAXB_HL7_CONTEXT_NAME = "org.hl7.v3";
        JAXBContextHandler oHandler = new JAXBContextHandler();
        JAXBContext jc = oHandler.getJAXBContext(JAXB_HL7_CONTEXT_NAME);
        Marshaller marshaller = jc.createMarshaller();
        return marshaller;
    }

    private ParticipantObjectIdentificationType buildBaseParticipantObjectIdentificationType() {
        ParticipantObjectIdentificationType participantObject = new ParticipantObjectIdentificationType();
        participantObject = createParticipantObjectIdentification(
            PatientDiscoveryTransformConstants.PARTICIPANT_QUERYPARAMS_OBJ_TYPE_CODE_SYSTEM,
            PatientDiscoveryTransformConstants.PARTICIPANT_QUERYPARAMS_OBJ_TYPE_CODE_ROLE,
            PatientDiscoveryTransformConstants.PARTICIPANT_QUERYPARAMS_OBJ_ID_TYPE_CODE,
            PatientDiscoveryTransformConstants.PARTICIPANT_QUERYPARAMS_OBJ_ID_TYPE_CODE_SYSTEM,
            PatientDiscoveryTransformConstants.PARTICIPANT_QUERYPARAMS_OBJ_ID_TYPE_DISPLAY_NAME);
        participantObject.setParticipantObjectID(createUUID());
        participantObject.setParticipantObjectName(HomeCommunityMap.
            formatHomeCommunityId(HomeCommunityMap.getLocalHomeCommunityId()));
        return participantObject;
    }

}
