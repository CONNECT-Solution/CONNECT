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
 * @param <T>
 * @param <K>
 */
public class PatientDiscoveryTransforms<T extends PRPAIN201305UV02, K extends PRPAIN201306UV02> extends AuditTransform<T, K> {

    private static final Logger LOG = Logger.getLogger(PatientDiscoveryTransforms.class);

    /**
     *
     * @param msg
     * @param assertion
     * @param auditMsg
     * @param response
     * @return
     */
    @Override
    protected AuditMessageType getParticipantObjectIdentification(PRPAIN201305UV02 request, PRPAIN201306UV02 response, AssertionType assertion, AuditMessageType auditMsg) {
        auditMsg = getPatientParticipantObjectIdentification(request, response, auditMsg);
        try {
            auditMsg = getQueryParamsParticipantObjectIdentification(request, response, auditMsg);
        } catch (JAXBException ex) {
            LOG.debug("Error while creating ParticipantObjectIdentificationQueryByParameters segemnt : " + ex);
        }
        return auditMsg;
    }

    private AuditMessageType getPatientParticipantObjectIdentification(PRPAIN201305UV02 request, PRPAIN201306UV02 response, AuditMessageType auditMsg) {

        // Set the Partipation Object Id (patient id)
        if (request != null) {
            II oII = getPatientIdFromRequest(request);
            if (oII != null && oII.getRoot() != null && oII.getExtension() != null && !oII.getRoot().isEmpty() && !oII.getExtension().isEmpty()) {
                createPatientParticipantObjectIdentification(auditMsg, oII.getRoot(), oII.getExtension());
            }

        } else if (response != null) {
            List<II> oII = getPatientIdFromResponse(response);
            if (oII != null && oII.size() > 0) {
                for (II entry : oII) {
                    if (entry != null && entry.getRoot() != null && entry.getExtension() != null && !entry.getRoot().isEmpty() && !entry.getExtension().isEmpty()) {
                        createPatientParticipantObjectIdentification(auditMsg, entry.getRoot(), entry.getExtension());
                    }
                }
            } else {
                createPatientParticipantObjectIdentification(auditMsg, null, null);
            }
        }
        return auditMsg;
    }

    private AuditMessageType getQueryParamsParticipantObjectIdentification(PRPAIN201305UV02 request, PRPAIN201306UV02 response, AuditMessageType auditMsg) throws JAXBException {

        ParticipantObjectIdentificationType participantObject = new ParticipantObjectIdentificationType();
        participantObject = createParticipantObjectIdentification(PatientDiscoveryTransformConstants.PARTICIPANT_QUERYPARAMS_OBJ_TYPE_CODE_SYSTEM, PatientDiscoveryTransformConstants.PARTICIPANT_QUERYPARAMS_OBJ_TYPE_CODE_ROLE, PatientDiscoveryTransformConstants.PARTICIPANT_QUERYPARAMS_OBJ_ID_TYPE_CODE, PatientDiscoveryTransformConstants.PARTICIPANT_QUERYPARAMS_OBJ_ID_TYPE_CODE_SYSTEM, PatientDiscoveryTransformConstants.PARTICIPANT_QUERYPARAMS_OBJ_ID_TYPE_DISPLAY_NAME);
        participantObject.setParticipantObjectID(createUUID());
        participantObject.setParticipantObjectName(HomeCommunityMap.formatHomeCommunityId(HomeCommunityMap.getLocalHomeCommunityId()));
        byte[] byteArray = getParticipantObjectQuery(request, response);
        if (byteArray != null) {
            participantObject.setParticipantObjectQuery(byteArray);
        }
        auditMsg.getParticipantObjectIdentification().add(participantObject);
        return auditMsg;

    }

    private byte[] getParticipantObjectQuery(PRPAIN201305UV02 request, PRPAIN201306UV02 response) throws JAXBException {
        JAXBContextHandler oHandler = new JAXBContextHandler();
        String JAXB_HL7_CONTEXT_NAME = "org.hl7.v3";
        JAXBContext jc = oHandler.getJAXBContext(JAXB_HL7_CONTEXT_NAME);
        Marshaller marshaller = jc.createMarshaller();
        ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
        baOutStrm.reset();
        if (request != null && request.getControlActProcess() != null && request.getControlActProcess().getQueryByParameter() != null) {
            marshaller.marshal(request.getControlActProcess().getQueryByParameter(), baOutStrm);
        } else if (response != null && response.getControlActProcess() != null && response.getControlActProcess().getQueryByParameter() != null) {
            marshaller.marshal(response.getControlActProcess().getQueryByParameter(), baOutStrm);
        }

        return baOutStrm.toByteArray();
    }

    private II getPatientIdFromRequest(PRPAIN201305UV02 msg) {
        PRPAIN201305UV02 oPatientDiscoveryRequestMessage = msg;
        II oII = null;

        if (oPatientDiscoveryRequestMessage != null && oPatientDiscoveryRequestMessage.getControlActProcess() != null
            && oPatientDiscoveryRequestMessage.getControlActProcess().getQueryByParameter() != null
            && oPatientDiscoveryRequestMessage.getControlActProcess().getQueryByParameter().getValue() != null
            && oPatientDiscoveryRequestMessage.getControlActProcess().getQueryByParameter().getValue().getParameterList() != null
            && oPatientDiscoveryRequestMessage.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectId() != null
            && oPatientDiscoveryRequestMessage.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectId().get(0) != null && oPatientDiscoveryRequestMessage.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectId().get(0).getValue() != null && oPatientDiscoveryRequestMessage.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectId().get(0).getValue().get(0) != null) {
            oII = oPatientDiscoveryRequestMessage.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectId().get(0).getValue().get(0);

        } else {
            LOG.error("PatientId doesn't exists in the received PRPAIN201305UV02 message");
            return oII;
        }
        return oII;
    }

    private List<II> getPatientIdFromResponse(PRPAIN201306UV02 msg) {
        List<II> oIIs = null;
        PRPAIN201306UV02 oPatientDiscoveryResponseMessage = msg;
        if (oPatientDiscoveryResponseMessage != null && oPatientDiscoveryResponseMessage.getControlActProcess() != null
            && oPatientDiscoveryResponseMessage.getControlActProcess().getSubject() != null) {
            List<PRPAIN201306UV02MFMIMT700711UV01Subject1> oSubject1 = oPatientDiscoveryResponseMessage.getControlActProcess().getSubject();
            for (PRPAIN201306UV02MFMIMT700711UV01Subject1 subject : oSubject1) {
                if (subject.getRegistrationEvent() != null && subject.getRegistrationEvent().getSubject1() != null
                    && subject.getRegistrationEvent().getSubject1().getPatient() != null
                    && subject.getRegistrationEvent().getSubject1().getPatient().getId() != null) {
                    oIIs = subject.getRegistrationEvent().getSubject1().getPatient().getId();
                } else {
                    LOG.error("PatientId doesn't exists in the received PRPAIN201306UV02 message");
                    return oIIs;
                }
            }
        }
        return oIIs;
    }

    private AuditMessageType createPatientParticipantObjectIdentification(AuditMessageType auditMsg, String aa, String patientId) {
        ParticipantObjectIdentificationType participantObject = createParticipantObjectIdentification(PatientDiscoveryTransformConstants.PARTICIPANT_PATIENT_OBJ_TYPE_CODE_SYSTEM, PatientDiscoveryTransformConstants.PARTICIPANT_PATIENT_OBJ_TYPE_CODE_ROLE, PatientDiscoveryTransformConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE, PatientDiscoveryTransformConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE_SYSTEM, PatientDiscoveryTransformConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_DISPLAY_NAME);
        if (aa != null && patientId != null && !aa.isEmpty() && !patientId.isEmpty()) {
            participantObject.setParticipantObjectID(createPatientId(aa, patientId));
            auditMsg.getParticipantObjectIdentification().add(participantObject);
        } else {
            auditMsg.getParticipantObjectIdentification().add(participantObject);
        }
        return auditMsg;
    }

    private static String createPatientId(String assigningAuthId, String patientId) {
        String sValue = null;
        sValue = patientId + "^^^&" + assigningAuthId + "&ISO";
        return sValue;
    }

}
