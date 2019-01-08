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
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV;

import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers.CDHelper;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers.CSHelper;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers.Configuration;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers.Constants;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers.CreationTimeHelper;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers.IIHelper;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers.InteractionIdHelper;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers.SenderReceiverHelperMCCIMT000300UV01;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers.UniqueIdHelper;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.COCTMT090003UV01AssignedEntity;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000300UV01Acknowledgement;
import org.hl7.v3.MFMIMT700711UV01Custodian;
import org.hl7.v3.MFMIMT700711UV01QueryAck;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.PRPAIN201310UV02;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01ControlActProcess;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01Subject2;
import org.hl7.v3.PRPAMT201304UV02Patient;
import org.hl7.v3.PRPAMT201304UV02Person;
import org.hl7.v3.PRPAMT201307UV02QueryByParameter;
import org.hl7.v3.XActMoodIntentEvent;

/**
 *
 * @author rayj
 */
public class PixRetrieveResponseBuilder {

    private static String CODE = "CA";
    private static String ControlActProcessCode = "PRPA_TE201310UV";
    private static String SubjectTypeCode = "SUBJ";
    private static String StatusCodeValue = "active";
    private static String PatientClassCode = "PAT";
    private static String PATIENTPERSON_CLASSCODE = "PSN";
    private static String QUERY_RESPONSE = "OK";
    private static final String AcceptAckCodeValue = "AL";
    private static final String InteractionIdExtension = "PRPA_IN201310";
    private static final String ProcessingCodeValue = "P";
    private static final String ProcessingModeCode = "T";
    private static final String ITSVersion = "XML_1.0";

    public static PRPAIN201310UV02 createPixRetrieveResponse(PRPAIN201309UV02 retrievePatientCorrelationsRequest,
            List<II> resultListII) {
        PRPAIN201310UV02 message = createTransmissionWrapper(IIHelper.IIFactory(Configuration.getMyCommunityId(), null),
                IIHelper.IIFactoryCreateNull());
        message.getAcknowledgement().add(createAck());
        message.setControlActProcess(createControlActProcess(resultListII, retrievePatientCorrelationsRequest));
        return message;
    }

    private static MFMIMT700711UV01QueryAck createQueryAck(
            PRPAIN201310UV02MFMIMT700711UV01ControlActProcess controlAct) {

        MFMIMT700711UV01QueryAck queryAck = new MFMIMT700711UV01QueryAck();
        queryAck.setQueryId(controlAct.getQueryByParameter().getValue().getQueryId());
        queryAck.setQueryResponseCode(CSHelper.buildCS(QUERY_RESPONSE));

        return queryAck;
    }

    private static PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent createRegistrationEvent(List<II> patientIds,
            PRPAIN201309UV02 originalRetrievePatientCorrelationsRequest) {

        PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent registrationEvent = new PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent();
        registrationEvent.getId().add(IIHelper.IIFactoryCreateNull());
        registrationEvent.setStatusCode(CSHelper.buildCS(StatusCodeValue));
        PRPAIN201310UV02MFMIMT700711UV01Subject2 subject1 = createSubject1(patientIds,
                originalRetrievePatientCorrelationsRequest);

        registrationEvent.setSubject1(subject1);

        registrationEvent.setCustodian(createCustodian());
        return registrationEvent;
    }

    private static PRPAIN201310UV02MFMIMT700711UV01Subject1 createSubject(List<II> patientIds,
            PRPAIN201309UV02 originalRetrievePatientCorrelationsRequest) {

        PRPAIN201310UV02MFMIMT700711UV01Subject1 subject = new PRPAIN201310UV02MFMIMT700711UV01Subject1();
        subject.getTypeCode().add(SubjectTypeCode);
        PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent registrationEvent = createRegistrationEvent(patientIds,
                originalRetrievePatientCorrelationsRequest);

        subject.setRegistrationEvent(registrationEvent);

        return subject;
    }

    private static PRPAIN201310UV02MFMIMT700711UV01Subject2 createSubject1(List<II> patientIds,
            PRPAIN201309UV02 originalRetrievePatientCorrelationsRequest) {

        PRPAIN201310UV02MFMIMT700711UV01Subject2 subject1 = new PRPAIN201310UV02MFMIMT700711UV01Subject2();
        PRPAMT201304UV02Patient patient = createPatient(patientIds);
        subject1.setPatient(patient);

        // TODO: add provider organization

        return subject1;
    }

    private static MCCIMT000300UV01Acknowledgement createAck() {
        MCCIMT000300UV01Acknowledgement ack = new MCCIMT000300UV01Acknowledgement();
        ack.setTypeCode(CSHelper.buildCS(CODE));
        return ack;
    }

    private static PRPAIN201310UV02MFMIMT700711UV01ControlActProcess createControlActProcess(List<II> patientIds,
            PRPAIN201309UV02 originalRetrievePatientCorrelationsRequest) {
        PRPAIN201310UV02MFMIMT700711UV01ControlActProcess controlActProcess = new PRPAIN201310UV02MFMIMT700711UV01ControlActProcess();
        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);
        controlActProcess.setCode(CDHelper.CDFactory(ControlActProcessCode, Constants.HL7_OID));

        PRPAIN201310UV02MFMIMT700711UV01Subject1 subject = createSubject(patientIds,
                originalRetrievePatientCorrelationsRequest);
        controlActProcess.getSubject().add(subject);

        JAXBElement<PRPAMT201307UV02QueryByParameter> queryByParameter = PRPAIN201309UVParser
                .extractQueryId(originalRetrievePatientCorrelationsRequest);
        controlActProcess.setQueryByParameter(queryByParameter);

        controlActProcess.setQueryAck(createQueryAck(controlActProcess));

        return controlActProcess;
    }

    private static MFMIMT700711UV01Custodian createCustodian() {

        MFMIMT700711UV01Custodian custodian = new MFMIMT700711UV01Custodian();
        COCTMT090003UV01AssignedEntity assignedEntity = new COCTMT090003UV01AssignedEntity();
        assignedEntity.getId().add(IIHelper.IIFactoryCreateNull());
        custodian.setAssignedEntity(assignedEntity);
        return custodian;
    }

    private static PRPAMT201304UV02Patient createPatient(List<II> patientIds) {
        PRPAMT201304UV02Patient patient = new PRPAMT201304UV02Patient();
        patient.getClassCode().add(PatientClassCode);
        for (II patientId : patientIds) {
            patient.getId().add(patientId);
        }

        patient.setStatusCode(CSHelper.buildCS(StatusCodeValue));

        PRPAMT201304UV02Person patientPerson = new PRPAMT201304UV02Person();
        // create patient person element
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        JAXBElement<PRPAMT201304UV02Person> patientPersonElement = new JAXBElement<>(xmlqname,
                PRPAMT201304UV02Person.class, patientPerson);
        patient.setPatientPerson(patientPersonElement);
        patientPerson.getClassCode().add(PATIENTPERSON_CLASSCODE);

        patientPerson.setDeterminerCode("INSTANCE");

        PNExplicit patientName = new PNExplicit();
        patientName.getNullFlavor().add("NA");
        patientPerson.getName().add(patientName);
        return patient;
    }

    private static PRPAIN201310UV02 createTransmissionWrapper(II senderId, II receiverId) {
        PRPAIN201310UV02 message = new PRPAIN201310UV02();

        message.setITSVersion(ITSVersion);
        message.setId(UniqueIdHelper.createUniqueId());
        message.setCreationTime(CreationTimeHelper.getCreationTime());
        message.setInteractionId(InteractionIdHelper.createInteractionId(InteractionIdExtension));

        message.setProcessingCode(CSHelper.buildCS(ProcessingCodeValue));
        message.setProcessingModeCode(CSHelper.buildCS(ProcessingModeCode));
        message.setAcceptAckCode(CSHelper.buildCS(AcceptAckCodeValue));

        message.getReceiver().add(SenderReceiverHelperMCCIMT000300UV01.CreateReceiver(receiverId));
        message.setSender(SenderReceiverHelperMCCIMT000300UV01.CreateSender(senderId));

        return message;
    }
}
