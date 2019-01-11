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
package gov.hhs.fha.nhinc.transform.subdisc;

import javax.xml.bind.JAXBElement;
import org.apache.commons.lang.StringUtils;
import org.hl7.v3.II;
import org.hl7.v3.MFMIMT700711UV01QueryAck;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAIN201310UV02;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01ControlActProcess;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01Subject2;
import org.hl7.v3.PRPAMT201304UV02Patient;
import org.hl7.v3.PRPAMT201304UV02Person;
import org.hl7.v3.PRPAMT201307UV02ParameterList;
import org.hl7.v3.PRPAMT201307UV02QueryByParameter;
import org.hl7.v3.XActMoodIntentEvent;

/**
 *
 * @author vvickers
 */
public class HL7PRPA201310Transforms {

    private static final String PATIENT_CLASS_CODE = "PAT";
    private static final String STATUS_CODE_VALUE = "active";
    private static final String PATIENT_PERSON_CLASSCODE = "PSN";
    private static final String DETERMINER_CODE = "INSTANCE";
    private static final String INTERACTION_ID_EXTENSION = "PRPA_IN201310UV";
    private static final String PROCESSING_CODE_VALUE = "P";
    private static final String PROCESSING_CODE_MODE = "T";
    private static final String ACCEPT_ACK_CODE_VALUE = "AL";
    private static final String CONTROL_CODE_CODE = "PRPA_TE201310UV";
    private static final String REG_EVENT_CLASS_CODE = "EVN";
    private static final String REG_EVENT_MOOD_CODE = "REG";
    private static final String REG_EVENT_STATUS_CODE = "active";
    private static final String CONTROL_QUERY_RESPONSE_CODE = "OK";

    private static HL7MessageIdGenerator idGenerator = new HL7MessageIdGenerator();

    public static PRPAIN201310UV02 createFaultPRPA201310() {
        return createPRPA201310("", "", "", "", "", null);
    }

    public static PRPAIN201310UV02 createFaultPRPA201310(final String senderOID, final String receiverOID) {
        return createPRPA201310("", "", "", senderOID, receiverOID, null);
    }

    public static PRPAIN201310UV02 createPRPA201310(final String patientId, final String assigningAuthorityId,
            final String localDeviceId, final String senderOID, final String receiverOID,
            final JAXBElement<PRPAMT201307UV02QueryByParameter> queryParam) {

        // Create Transmission header
        final PRPAIN201310UV02 ret201310 = createTransmissionWrapper(localDeviceId);

        // Create the Sender
        ret201310.setSender(HL7SenderTransforms.createMCCIMT000300UV01Sender(senderOID));

        // Create the Receiver
        ret201310.getReceiver().add(HL7ReceiverTransforms.createMCCIMT000300UV01Receiver(receiverOID));

        // Create the control act process
        final PRPAMT201304UV02Patient patient201304 = createPRPAMT201304UVPatient(patientId, assigningAuthorityId);
        ret201310.setControlActProcess(
                createMFMIMT700711UV01ControlActProcess(patient201304, localDeviceId, queryParam));

        return ret201310;
    }

    private static PRPAIN201310UV02 createTransmissionWrapper(final String localDeviceId) {
        final PRPAIN201310UV02 message = new PRPAIN201310UV02();

        message.setITSVersion(HL7Constants.ITS_VERSION);
        message.setId(idGenerator.generateHL7MessageId(localDeviceId));
        message.setCreationTime(HL7DataTransformHelper.creationTimeFactory());
        message.setInteractionId(
                HL7DataTransformHelper.IIFactory(HL7Constants.INTERACTION_ID_ROOT, INTERACTION_ID_EXTENSION));
        message.setProcessingCode(HL7DataTransformHelper.CSFactory(PROCESSING_CODE_VALUE));
        message.setProcessingModeCode(HL7DataTransformHelper.CSFactory(PROCESSING_CODE_MODE));
        message.setAcceptAckCode(HL7DataTransformHelper.CSFactory(ACCEPT_ACK_CODE_VALUE));

        return message;
    }

    private static PRPAMT201304UV02Patient createPRPAMT201304UVPatient(final String patientId,
            final String assigningAuthorityId) {

        final PRPAMT201304UV02Patient patient = new PRPAMT201304UV02Patient();
        patient.getClassCode().add(PATIENT_CLASS_CODE);
        if (StringUtils.isNotEmpty(patientId) && StringUtils.isNotEmpty(assigningAuthorityId)) {
            patient.getId().add(HL7DataTransformHelper.IIFactory(assigningAuthorityId, patientId));
        } else {
            patient.getId().add(HL7DataTransformHelper.IIFactoryCreateNull());
        }
        patient.setStatusCode(HL7DataTransformHelper.CSFactory(STATUS_CODE_VALUE));

        final PRPAMT201304UV02Person patientPerson = new PRPAMT201304UV02Person();
        // create patient person element
        final javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        final JAXBElement<PRPAMT201304UV02Person> patientPersonElement = new JAXBElement<>(xmlqname,
                PRPAMT201304UV02Person.class, patientPerson);
        patient.setPatientPerson(patientPersonElement);
        patientPerson.getClassCode().add(PATIENT_PERSON_CLASSCODE);

        patientPerson.setDeterminerCode(DETERMINER_CODE);

        final PNExplicit patientName = new PNExplicit();
        patientName.getNullFlavor().add(HL7Constants.NULL_FLAVOR);
        patientPerson.getName().add(patientName);
        return patient;
    }

    private static PRPAIN201310UV02MFMIMT700711UV01ControlActProcess createMFMIMT700711UV01ControlActProcess(
            final PRPAMT201304UV02Patient patient201304, final String localDeviceId,
            final JAXBElement<PRPAMT201307UV02QueryByParameter> queryParam) {
        final PRPAIN201310UV02MFMIMT700711UV01ControlActProcess controlActProcess = new PRPAIN201310UV02MFMIMT700711UV01ControlActProcess();

        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);
        controlActProcess.setCode(HL7DataTransformHelper.CDFactory(CONTROL_CODE_CODE));

        controlActProcess.getSubject().add(createPRPAIN201310UVMFMIMT700711UV01Subject1(patient201304, localDeviceId));

        if (queryParam != null) {
            controlActProcess.setQueryByParameter(queryParam);
        } else {
            controlActProcess.setQueryByParameter(createNullQueryByParameter());
        }

        controlActProcess
                .setQueryAck(createMFMIMT700711UV01QueryAck(controlActProcess.getQueryByParameter().getValue()));

        return controlActProcess;
    }

    private static PRPAIN201310UV02MFMIMT700711UV01Subject1 createPRPAIN201310UVMFMIMT700711UV01Subject1(
            final PRPAMT201304UV02Patient patient201304, final String localDeviceId) {
        final PRPAIN201310UV02MFMIMT700711UV01Subject1 subject1 = new PRPAIN201310UV02MFMIMT700711UV01Subject1();

        subject1.getTypeCode().add("SUBJ");
        subject1.setContextConductionInd(false);

        subject1.setRegistrationEvent(
                createPRPAIN201310UVMFMIMT700711UV01RegistrationEvent(patient201304, localDeviceId));

        return subject1;
    }

    private static PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent createPRPAIN201310UVMFMIMT700711UV01RegistrationEvent(
            final PRPAMT201304UV02Patient patient201304, final String localDeviceId) {
        final PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent regevent = new PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent();

        regevent.getClassCode().add(REG_EVENT_CLASS_CODE);
        regevent.getMoodCode().add(REG_EVENT_MOOD_CODE);

        final II regId = new II();
        regId.getNullFlavor().add(HL7Constants.NULL_FLAVOR);
        regevent.getId().add(regId);

        regevent.setStatusCode(HL7DataTransformHelper.CSFactory(REG_EVENT_STATUS_CODE));

        regevent.setSubject1(createPRPAIN201310UVMFMIMT700711UV01Subject2(patient201304));

        regevent.setCustodian(HL7CustodianTransforms.createMFMIMT700711UV01Custodian(localDeviceId));

        return regevent;
    }

    private static PRPAIN201310UV02MFMIMT700711UV01Subject2 createPRPAIN201310UVMFMIMT700711UV01Subject2(
            final PRPAMT201304UV02Patient patient201304) {
        final PRPAIN201310UV02MFMIMT700711UV01Subject2 subject = new PRPAIN201310UV02MFMIMT700711UV01Subject2();

        subject.setPatient(patient201304);

        return subject;
    }

    private static JAXBElement<PRPAMT201307UV02QueryByParameter> createNullQueryByParameter() {

        final PRPAMT201307UV02QueryByParameter queryParams = new PRPAMT201307UV02QueryByParameter();

        queryParams.setQueryId(idGenerator.generateHL7MessageId(null));
        queryParams.setStatusCode(HL7DataTransformHelper.CSFactory("new"));

        final PRPAMT201307UV02ParameterList paramList = new PRPAMT201307UV02ParameterList();
        queryParams.setParameterList(paramList);

        final javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "queryByParameter");

        return new JAXBElement<>(xmlqname, PRPAMT201307UV02QueryByParameter.class, queryParams);
    }

    private static MFMIMT700711UV01QueryAck createMFMIMT700711UV01QueryAck(
            final PRPAMT201307UV02QueryByParameter queryParam) {
        final MFMIMT700711UV01QueryAck queryAck = new MFMIMT700711UV01QueryAck();

        queryAck.setQueryId(queryParam.getQueryId());
        queryAck.setQueryResponseCode(HL7DataTransformHelper.CSFactory(CONTROL_QUERY_RESPONSE_CODE));
        return queryAck;
    }

}
