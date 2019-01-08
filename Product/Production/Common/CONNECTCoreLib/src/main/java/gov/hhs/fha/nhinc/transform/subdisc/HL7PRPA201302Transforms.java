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
    private static String localDeviceId = null;

    private static HL7MessageIdGenerator idGenerator = new HL7MessageIdGenerator();

    public static PRPAIN201302UV02 createPRPA201302(PRPAMT201301UV02Patient patient, String remotePatId,
            String remoteDeviceId, String senderOID, String receiverOID) {
        result = new PRPAIN201302UV02();
        localDeviceId = patient.getId().get(0).getRoot();
        setHeaderFields(senderOID, receiverOID);

        // Create the control act process
        result.setControlActProcess(createMFMIMT700701UV01ControlActProcess(patient, remotePatId, remoteDeviceId));

        return result;
    }

    public static PRPAIN201302UV02 createPRPA201302(PRPAMT201310UV02Patient patient, String remotePatId,
            String remoteDeviceId, String senderOID, String receiverOID) {
        result = new PRPAIN201302UV02();
        localDeviceId = patient.getId().get(0).getRoot();
        setHeaderFields(senderOID, receiverOID);

        // Create the control act process
        result.setControlActProcess(createMFMIMT700701UV01ControlActProcess(patient, remotePatId, remoteDeviceId));

        return result;
    }

    private static void setHeaderFields(String senderOID, String receiverOID) {
        // Create the 201302 message header fields
        result.setITSVersion(HL7Constants.ITS_VERSION);
        result.setId(idGenerator.generateHL7MessageId(localDeviceId));
        result.setCreationTime(HL7DataTransformHelper.creationTimeFactory());
        result.setInteractionId(HL7DataTransformHelper.IIFactory(HL7Constants.INTERACTION_ID_ROOT, "PRPA_IN201302UV"));
        result.setProcessingCode(HL7DataTransformHelper.CSFactory("T"));
        result.setProcessingModeCode(HL7DataTransformHelper.CSFactory("T"));
        result.setAcceptAckCode(HL7DataTransformHelper.CSFactory("AL"));

        // Create the Sender
        result.setSender(HL7SenderTransforms.createMCCIMT000100UV01Sender(senderOID));

        // Create the Receiver
        result.getReceiver().add(HL7ReceiverTransforms.createMCCIMT000100UV01Receiver(receiverOID));
    }

    public static PRPAIN201302UV02MFMIMT700701UV01ControlActProcess createMFMIMT700701UV01ControlActProcess(
            PRPAMT201301UV02Patient patient, String remotePatId, String remoteDeviceId) {
        PRPAIN201302UV02MFMIMT700701UV01ControlActProcess controlActProcess = new PRPAIN201302UV02MFMIMT700701UV01ControlActProcess();

        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);
        controlActProcess.setCode(HL7DataTransformHelper.CDFactory("PRPA_TE201302UV"));

        controlActProcess.getSubject()
                .add(createPRPAIN201302UVMFMIMT700701UV01Subject1(patient, remotePatId, remoteDeviceId));
        return controlActProcess;
    }

    public static PRPAIN201302UV02MFMIMT700701UV01ControlActProcess createMFMIMT700701UV01ControlActProcess(
            PRPAMT201310UV02Patient patient, String remotePatId, String remoteDeviceId) {
        PRPAIN201302UV02MFMIMT700701UV01ControlActProcess controlActProcess = new PRPAIN201302UV02MFMIMT700701UV01ControlActProcess();

        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);

        controlActProcess.setCode(HL7DataTransformHelper.CDFactory("PRPA_TE201302UV"));

        controlActProcess.getSubject()
                .add(createPRPAIN201302UVMFMIMT700701UV01Subject1(patient, remotePatId, remoteDeviceId));
        return controlActProcess;
    }

    public static PRPAIN201302UV02MFMIMT700701UV01Subject1 createPRPAIN201302UVMFMIMT700701UV01Subject1(
            PRPAMT201301UV02Patient patient, String remotePatId, String remoteDeviceId) {
        PRPAIN201302UV02MFMIMT700701UV01Subject1 subject1 = new PRPAIN201302UV02MFMIMT700701UV01Subject1();

        subject1.getTypeCode().add("SUBJ");
        subject1.setContextConductionInd(false);

        subject1.setRegistrationEvent(
                createPRPAIN201302UVMFMIMT700701UV01RegistrationEvent(patient, remotePatId, remoteDeviceId));

        return subject1;
    }

    public static PRPAIN201302UV02MFMIMT700701UV01Subject1 createPRPAIN201302UVMFMIMT700701UV01Subject1(
            PRPAMT201310UV02Patient patient, String remotePatId, String remoteDeviceId) {
        PRPAIN201302UV02MFMIMT700701UV01Subject1 subject1 = new PRPAIN201302UV02MFMIMT700701UV01Subject1();

        subject1.getTypeCode().add("SUBJ");
        subject1.setContextConductionInd(false);

        subject1.setRegistrationEvent(
                createPRPAIN201302UVMFMIMT700701UV01RegistrationEvent(patient, remotePatId, remoteDeviceId));

        return subject1;
    }

    public static PRPAIN201302UV02MFMIMT700701UV01RegistrationEvent createPRPAIN201302UVMFMIMT700701UV01RegistrationEvent(
            PRPAMT201301UV02Patient patient, String remotePatId, String remoteDeviceId) {
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

    public static PRPAIN201302UV02MFMIMT700701UV01RegistrationEvent createPRPAIN201302UVMFMIMT700701UV01RegistrationEvent(
            PRPAMT201310UV02Patient patient, String remotePatId, String remoteDeviceId) {
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

    public static PRPAIN201302UV02MFMIMT700701UV01Subject2 createPRPAIN201302UVMFMIMT700701UV01Subject2(
            PRPAMT201301UV02Patient patient, String remotePatId, String remoteDeviceId) {
        PRPAIN201302UV02MFMIMT700701UV01Subject2 subject = new PRPAIN201302UV02MFMIMT700701UV01Subject2();

        subject.setPatient(HL7PatientTransforms.create201302Patient(remotePatId, remoteDeviceId,
                patient.getPatientPerson(), patient.getId().get(0)));

        return subject;
    }

    public static PRPAIN201302UV02MFMIMT700701UV01Subject2 createPRPAIN201302UVMFMIMT700701UV01Subject2(
            PRPAMT201310UV02Patient patient, String remotePatId, String remoteDeviceId) {
        PRPAIN201302UV02MFMIMT700701UV01Subject2 subject = new PRPAIN201302UV02MFMIMT700701UV01Subject2();

        subject.setPatient(HL7PatientTransforms.create201302Patient(patient.getPatientPerson(), remotePatId,
                remoteDeviceId, patient.getId().get(0)));

        return subject;
    }

}
