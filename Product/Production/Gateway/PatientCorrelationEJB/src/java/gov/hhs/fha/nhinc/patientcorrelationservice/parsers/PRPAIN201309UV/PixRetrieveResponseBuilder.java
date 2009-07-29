/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientcorrelationservice.parsers.PRPAIN201309UV;

import gov.hhs.fha.nhinc.patientcorrelationservice.parsers.helpers.CDHelper;
import gov.hhs.fha.nhinc.patientcorrelationservice.parsers.helpers.CSHelper;
import gov.hhs.fha.nhinc.patientcorrelationservice.parsers.helpers.Configuration;
import gov.hhs.fha.nhinc.patientcorrelationservice.parsers.helpers.Constants;
import gov.hhs.fha.nhinc.patientcorrelationservice.parsers.helpers.CreationTimeHelper;
import gov.hhs.fha.nhinc.patientcorrelationservice.parsers.helpers.IIHelper;
import gov.hhs.fha.nhinc.patientcorrelationservice.parsers.helpers.InteractionIdHelper;
import gov.hhs.fha.nhinc.patientcorrelationservice.parsers.helpers.SenderReceiverHelperMCCIMT000100UV01;
import gov.hhs.fha.nhinc.patientcorrelationservice.parsers.helpers.SenderReceiverHelperMCCIMT000300UV01;
import gov.hhs.fha.nhinc.patientcorrelationservice.parsers.helpers.UniqueIdHelper;
import java.util.List;
import org.hl7.v3.*;
import javax.xml.bind.JAXBElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author rayj
 */
public class PixRetrieveResponseBuilder {

    private static Log log = LogFactory.getLog(PixRetrieveResponseBuilder.class);
    private static String CODE = "CA";
    private static String MoodCode = "EVN";
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

    public static PRPAIN201310UV createPixRetrieveResponse(PRPAIN201309UV retrievePatientCorrelationsRequest, List<II> resultListII) {
        PRPAIN201310UV message = createTransmissionWrapper(IIHelper.IIFactory(Configuration.getMyCommunityId(), null), IIHelper.IIFactoryCreateNull());
        message.getAcknowledgement().add(createAck());
        message.setControlActProcess(createControlActProcess(resultListII, retrievePatientCorrelationsRequest));
        return message;
    }

    private static MFMIMT700711UV01QueryAck createQueryAck(PRPAIN201310UVMFMIMT700711UV01Subject2 subject1) {

        MFMIMT700711UV01QueryAck queryAck = new MFMIMT700711UV01QueryAck();
        queryAck.setQueryId(subject1.getQueryByParameter().getQueryId());
        queryAck.setQueryResponseCode(CSHelper.buildCS(QUERY_RESPONSE));

        return queryAck;
    }

    private static PRPAIN201310UVMFMIMT700711UV01RegistrationEvent createRegistrationEvent(List<II> patientIds, PRPAIN201309UV originalRetrievePatientCorrelationsRequest) {

        PRPAIN201310UVMFMIMT700711UV01RegistrationEvent registrationEvent = new PRPAIN201310UVMFMIMT700711UV01RegistrationEvent();
        registrationEvent.getId().add(IIHelper.IIFactoryCreateNull());
        registrationEvent.setStatusCode(CSHelper.buildCS(StatusCodeValue));
        PRPAIN201310UVMFMIMT700711UV01Subject2 subject1 = createSubject1(patientIds, originalRetrievePatientCorrelationsRequest);

        registrationEvent.setSubject1(subject1);

        registrationEvent.setCustodian(createCustodian());
        return registrationEvent;
    }

    private static PRPAIN201310UVMFMIMT700711UV01Subject1 createSubject(List<II> patientIds, PRPAIN201309UV originalRetrievePatientCorrelationsRequest) {

        PRPAIN201310UVMFMIMT700711UV01Subject1 subject = new PRPAIN201310UVMFMIMT700711UV01Subject1();
        subject.getTypeCode().add(SubjectTypeCode);
        PRPAIN201310UVMFMIMT700711UV01RegistrationEvent registrationEvent = createRegistrationEvent(patientIds, originalRetrievePatientCorrelationsRequest);

        subject.setRegistrationEvent(registrationEvent);


        return subject;
    }

    private static PRPAIN201310UVMFMIMT700711UV01Subject2 createSubject1(List<II> patientIds, PRPAIN201309UV originalRetrievePatientCorrelationsRequest) {

        PRPAIN201310UVMFMIMT700711UV01Subject2 subject1 = new PRPAIN201310UVMFMIMT700711UV01Subject2();
        PRPAMT201304UVPatient patient = createPatient(patientIds);
        subject1.setPatient(patient);

        //TODO: add provider organization

        PRPAMT201307UVQueryByParameter queryByParameter = PRPAIN201309UVParser.ExtractQueryId(originalRetrievePatientCorrelationsRequest);
        subject1.setQueryByParameter(queryByParameter);

        return subject1;
    }

    private static MCCIMT000300UV01Acknowledgement createAck() {
        MCCIMT000300UV01Acknowledgement ack = new MCCIMT000300UV01Acknowledgement();
        ack.setTypeCode(CSHelper.buildCS(CODE));
        return ack;
    }

    private static PRPAIN201310UVMFMIMT700711UV01ControlActProcess createControlActProcess(List<II> patientIds, PRPAIN201309UV originalRetrievePatientCorrelationsRequest) {
        PRPAIN201310UVMFMIMT700711UV01ControlActProcess controlActProcess = new PRPAIN201310UVMFMIMT700711UV01ControlActProcess();
        controlActProcess.setMoodCode(MoodCode);
        controlActProcess.setCode(CDHelper.CDFactory(ControlActProcessCode, Constants.HL7_OID));

        PRPAIN201310UVMFMIMT700711UV01Subject1 subject = createSubject(patientIds, originalRetrievePatientCorrelationsRequest);
        controlActProcess.getSubject().add(subject);

        controlActProcess.setQueryAck(createQueryAck(subject.getRegistrationEvent().getSubject1()));

        return controlActProcess;
    }

    private static MFMIMT700711UV01Custodian createCustodian() {

        MFMIMT700711UV01Custodian custodian = new MFMIMT700711UV01Custodian();
        COCTMT090003UVAssignedEntity assignedEntity = new COCTMT090003UVAssignedEntity();
        assignedEntity.getId().add(IIHelper.IIFactoryCreateNull());
        custodian.setAssignedEntity(assignedEntity);
        return custodian;
    }

    private static PRPAMT201304UVPatient createPatient(List<II> patientIds) {
        PRPAMT201304UVPatient patient = new PRPAMT201304UVPatient();
        patient.setClassCode(PatientClassCode);
        for (II patientId : patientIds) {
            patient.getId().add(patientId);
        }

        patient.setStatusCode(CSHelper.buildCS(StatusCodeValue));

        PRPAMT201304UVPerson patientPerson = new PRPAMT201304UVPerson();
        // create patient person element
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        JAXBElement<PRPAMT201304UVPerson> patientPersonElement = new JAXBElement<PRPAMT201304UVPerson>(xmlqname, PRPAMT201304UVPerson.class, patientPerson);
        patient.setPatientPerson(patientPersonElement);
        patientPerson.setClassCode(PATIENTPERSON_CLASSCODE);

        patientPerson.setDeterminerCode("INSTANCE");

        PNExplicit patientName = new PNExplicit();
        patientName.getNullFlavor().add("NA");
        patientPerson.getName().add(patientName);
        return patient;
    }

    private static PRPAIN201310UV createTransmissionWrapper(II senderId, II receiverId) {
        PRPAIN201310UV message = new PRPAIN201310UV();

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
