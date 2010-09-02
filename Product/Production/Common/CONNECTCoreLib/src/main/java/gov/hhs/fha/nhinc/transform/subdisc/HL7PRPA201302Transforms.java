/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.subdisc;

import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201302UV02;
import org.hl7.v3.PRPAIN201302UV02MFMIMT700701UV01ControlActProcess;
import org.hl7.v3.PRPAIN201302UV02MFMIMT700701UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201302UV02MFMIMT700701UV01Subject1;
import org.hl7.v3.PRPAIN201302UV02MFMIMT700701UV01Subject2;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201310UV02Patient;
import org.hl7.v3.XActMoodIntentEvent;

/**
 *
 * @author Jon Hoppesch
 */
public class HL7PRPA201302Transforms {
    private static PRPAIN201302UV02 result = null;
    private static String localDeviceId = new String();

    public static PRPAIN201302UV02 createPRPA201302(PRPAMT201301UV02Patient patient, String remotePatId, String remoteDeviceId, String senderOID, String receiverOID) {
        result = new PRPAIN201302UV02();
        localDeviceId = patient.getId().get(0).getRoot();
        setHeaderFields(senderOID, receiverOID);

        // Create the control act process
        result.setControlActProcess(createMFMIMT700701UV01ControlActProcess(patient, remotePatId, remoteDeviceId));

        return result;
    }
    
    public static PRPAIN201302UV02 createPRPA201302(PRPAMT201310UV02Patient patient, String remotePatId, String remoteDeviceId, String senderOID, String receiverOID) {
        result = new PRPAIN201302UV02();
        localDeviceId = patient.getId().get(0).getRoot();
        setHeaderFields(senderOID, receiverOID);

        // Create the control act process
        result.setControlActProcess(createMFMIMT700701UV01ControlActProcess(patient, remotePatId, remoteDeviceId));

        return result;
    }
    
    private static void setHeaderFields (String senderOID, String receiverOID) {
        // Create the 201302 message header fields
        result.setITSVersion(HL7Constants.ITS_VERSION);
        result.setId(HL7MessageIdGenerator.GenerateHL7MessageId(localDeviceId));
        result.setCreationTime(HL7DataTransformHelper.CreationTimeFactory());
        result.setInteractionId(HL7DataTransformHelper.IIFactory(HL7Constants.INTERACTION_ID_ROOT, "PRPA_IN201302UV"));
        result.setProcessingCode(HL7DataTransformHelper.CSFactory("T"));
        result.setProcessingModeCode(HL7DataTransformHelper.CSFactory("T"));
        result.setAcceptAckCode(HL7DataTransformHelper.CSFactory("AL"));
        
        // Create the Sender
        result.setSender(HL7SenderTransforms.createMCCIMT000100UV01Sender(senderOID));

        // Create the Receiver
        result.getReceiver().add(HL7ReceiverTransforms.createMCCIMT000100UV01Receiver(receiverOID));
    }

    public static PRPAIN201302UV02MFMIMT700701UV01ControlActProcess createMFMIMT700701UV01ControlActProcess(PRPAMT201301UV02Patient patient, String remotePatId, String remoteDeviceId) {
        PRPAIN201302UV02MFMIMT700701UV01ControlActProcess controlActProcess = new PRPAIN201302UV02MFMIMT700701UV01ControlActProcess();

        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);
        controlActProcess.setCode(HL7DataTransformHelper.CDFactory("PRPA_TE201302UV"));

        controlActProcess.getSubject().add(createPRPAIN201302UVMFMIMT700701UV01Subject1(patient, remotePatId, remoteDeviceId));
        return controlActProcess;
    }
    
    public static PRPAIN201302UV02MFMIMT700701UV01ControlActProcess createMFMIMT700701UV01ControlActProcess(PRPAMT201310UV02Patient patient, String remotePatId, String remoteDeviceId) {
        PRPAIN201302UV02MFMIMT700701UV01ControlActProcess controlActProcess = new PRPAIN201302UV02MFMIMT700701UV01ControlActProcess();

        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);

        controlActProcess.setCode(HL7DataTransformHelper.CDFactory("PRPA_TE201302UV"));

        controlActProcess.getSubject().add(createPRPAIN201302UVMFMIMT700701UV01Subject1(patient, remotePatId, remoteDeviceId));
        return controlActProcess;
    }

    public static PRPAIN201302UV02MFMIMT700701UV01Subject1 createPRPAIN201302UVMFMIMT700701UV01Subject1(PRPAMT201301UV02Patient patient, String remotePatId, String remoteDeviceId) {
        PRPAIN201302UV02MFMIMT700701UV01Subject1 subject1 = new PRPAIN201302UV02MFMIMT700701UV01Subject1();

        subject1.getTypeCode().add("SUBJ");
        subject1.setContextConductionInd(false);

        subject1.setRegistrationEvent(createPRPAIN201302UVMFMIMT700701UV01RegistrationEvent(patient, remotePatId, remoteDeviceId));

        return subject1;
    }
    
    public static PRPAIN201302UV02MFMIMT700701UV01Subject1 createPRPAIN201302UVMFMIMT700701UV01Subject1(PRPAMT201310UV02Patient patient, String remotePatId, String remoteDeviceId) {
        PRPAIN201302UV02MFMIMT700701UV01Subject1 subject1 = new PRPAIN201302UV02MFMIMT700701UV01Subject1();

        subject1.getTypeCode().add("SUBJ");
        subject1.setContextConductionInd(false);

        subject1.setRegistrationEvent(createPRPAIN201302UVMFMIMT700701UV01RegistrationEvent(patient, remotePatId, remoteDeviceId));

        return subject1;
    }

    public static PRPAIN201302UV02MFMIMT700701UV01RegistrationEvent createPRPAIN201302UVMFMIMT700701UV01RegistrationEvent(PRPAMT201301UV02Patient patient, String remotePatId, String remoteDeviceId) {
        PRPAIN201302UV02MFMIMT700701UV01RegistrationEvent regevent = new PRPAIN201302UV02MFMIMT700701UV01RegistrationEvent();

        regevent.getClassCode().add("REG");
        regevent.getMoodCode().add("EVN");

        II regId = new II();
        regId.getNullFlavor().add("NA");
        regevent.getId().add(regId);

        regevent.setStatusCode(HL7DataTransformHelper.CSFactory("active"));

        regevent.setSubject1(createPRPAIN201302UVMFMIMT700701UV01Subject2(patient, remotePatId, remoteDeviceId));
        
        regevent.setCustodian(HL7CustodianTransforms.createMFMIMT700701UV01Custodian(localDeviceId));

        return regevent;
    }
    
    public static PRPAIN201302UV02MFMIMT700701UV01RegistrationEvent createPRPAIN201302UVMFMIMT700701UV01RegistrationEvent(PRPAMT201310UV02Patient patient, String remotePatId, String remoteDeviceId) {
        PRPAIN201302UV02MFMIMT700701UV01RegistrationEvent regevent = new PRPAIN201302UV02MFMIMT700701UV01RegistrationEvent();

        regevent.getClassCode().add("REG");
        regevent.getMoodCode().add("EVN");

        II regId = new II();
        regId.getNullFlavor().add("NA");
        regevent.getId().add(regId);

        regevent.setStatusCode(HL7DataTransformHelper.CSFactory("active"));

        regevent.setSubject1(createPRPAIN201302UVMFMIMT700701UV01Subject2(patient, remotePatId, remoteDeviceId));
        
        regevent.setCustodian(HL7CustodianTransforms.createMFMIMT700701UV01Custodian(localDeviceId));

        return regevent;
    }

    public static PRPAIN201302UV02MFMIMT700701UV01Subject2 createPRPAIN201302UVMFMIMT700701UV01Subject2(PRPAMT201301UV02Patient patient, String remotePatId, String remoteDeviceId) {
        PRPAIN201302UV02MFMIMT700701UV01Subject2 subject = new PRPAIN201302UV02MFMIMT700701UV01Subject2();
        
        subject.setPatient(HL7PatientTransforms.create201302Patient(remotePatId, remoteDeviceId, patient.getPatientPerson(), patient.getId().get(0)));

        return subject;
    }
    
    public static PRPAIN201302UV02MFMIMT700701UV01Subject2 createPRPAIN201302UVMFMIMT700701UV01Subject2(PRPAMT201310UV02Patient patient, String remotePatId, String remoteDeviceId) {
        PRPAIN201302UV02MFMIMT700701UV01Subject2 subject = new PRPAIN201302UV02MFMIMT700701UV01Subject2();
        
        subject.setPatient(HL7PatientTransforms.create201302Patient(patient.getPatientPerson(), remotePatId, remoteDeviceId, patient.getId().get(0)));

        return subject;
    }
}
