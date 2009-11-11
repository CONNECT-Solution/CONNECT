/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.transform.subdisc;

import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01ControlActProcess;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01Subject1;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01Subject2;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.XActMoodIntentEvent;

/**
 *
 * @author Jon Hoppesch
 */
public class HL7PRPA201301Transforms {
    public static PRPAIN201301UV02 createPRPA201301 (PRPAMT201301UV02Patient patient, String localDeviceId, String senderOID, String receiverOID) {
        PRPAIN201301UV02  result = new PRPAIN201301UV02();
        
        // Create the 201301 message header fields
        result.setITSVersion(HL7Constants.ITS_VERSION);
        result.setId(HL7MessageIdGenerator.GenerateHL7MessageId(localDeviceId));
        result.setCreationTime(HL7DataTransformHelper.CreationTimeFactory());
        result.setInteractionId(HL7DataTransformHelper.IIFactory(HL7Constants.INTERACTION_ID_ROOT, "PRPA_IN201301UV"));
        result.setProcessingCode(HL7DataTransformHelper.CSFactory("T"));
        result.setProcessingModeCode(HL7DataTransformHelper.CSFactory("T"));
        result.setAcceptAckCode(HL7DataTransformHelper.CSFactory("AL"));
        
        // Create the Sender
        result.setSender(HL7SenderTransforms.createMCCIMT000100UV01Sender(senderOID));

        // Create the Receiver
        result.getReceiver().add(HL7ReceiverTransforms.createMCCIMT000100UV01Receiver(receiverOID));
        
        // Create the control act process
        result.setControlActProcess(createMFMIMT700701UV01ControlActProcess(patient, localDeviceId));
        
        return result;
    }
    
    public static PRPAIN201301UV02MFMIMT700701UV01ControlActProcess createMFMIMT700701UV01ControlActProcess (PRPAMT201301UV02Patient patient, String localDeviceId) {
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess  controlActProcess = new PRPAIN201301UV02MFMIMT700701UV01ControlActProcess();
        
        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);
        
        controlActProcess.setCode(HL7DataTransformHelper.CDFactory("PRPA_TE201301UV"));
        
        controlActProcess.getSubject().add(createPRPAIN201301UVMFMIMT700701UV01Subject1(patient, localDeviceId));
        return controlActProcess;
    }
    
    public static PRPAIN201301UV02MFMIMT700701UV01Subject1 createPRPAIN201301UVMFMIMT700701UV01Subject1(PRPAMT201301UV02Patient patient, String localDeviceId) {
        PRPAIN201301UV02MFMIMT700701UV01Subject1 subject1 = new PRPAIN201301UV02MFMIMT700701UV01Subject1();
        
        subject1.getTypeCode().add("SUBJ");
        subject1.setContextConductionInd(false);
            
        subject1.setRegistrationEvent(createPRPAIN201301UVMFMIMT700701UV01RegistrationEvent(patient, localDeviceId));
        
        return subject1;
    }
    
    public static PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent createPRPAIN201301UVMFMIMT700701UV01RegistrationEvent (PRPAMT201301UV02Patient patient, String localDeviceId) {
        PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent regevent = new PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent();
        
        regevent.getClassCode().add("REG");
        regevent.getMoodCode().add("EVN");
        
        II regId = new II();
        regId.getNullFlavor().add("NA");
        regevent.getId().add(regId);
        
        regevent.setStatusCode(HL7DataTransformHelper.CSFactory("active"));
        
        regevent.setSubject1(createPRPAIN201301UVMFMIMT700701UV01Subject2(patient));
        
        regevent.setCustodian(HL7CustodianTransforms.createMFMIMT700701UV01Custodian(localDeviceId));
        
        return regevent;
    }
    
    public static PRPAIN201301UV02MFMIMT700701UV01Subject2 createPRPAIN201301UVMFMIMT700701UV01Subject2 (PRPAMT201301UV02Patient patient) {
        PRPAIN201301UV02MFMIMT700701UV01Subject2 subject = new PRPAIN201301UV02MFMIMT700701UV01Subject2();
        
        subject.setPatient(patient);
     
        return subject;
    }

}
