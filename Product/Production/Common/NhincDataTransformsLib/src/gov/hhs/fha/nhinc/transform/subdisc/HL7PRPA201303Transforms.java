/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.transform.subdisc;

import javax.xml.bind.JAXBElement;
import org.hl7.v3.*;

/**
 *
 * @author MFLYNN02
 */
public class HL7PRPA201303Transforms {
    public static PRPAIN201303UV createPRPA201303 (PRPAMT201305UVPatient patient, String senderOID, String receiverOID, String localDeviceId) {
        PRPAIN201303UV result = new PRPAIN201303UV();
        
        // Create the 201303 message header fields
        result.setITSVersion(HL7Constants.ITS_VERSION);
        result.setId(HL7MessageIdGenerator.GenerateHL7MessageId(localDeviceId));
        result.setCreationTime(HL7DataTransformHelper.CreationTimeFactory());
        result.setInteractionId(HL7DataTransformHelper.IIFactory(HL7Constants.INTERACTION_ID_ROOT, "PRPA_IN201303UV"));
        result.setProcessingCode(HL7DataTransformHelper.CSFactory("P"));
        result.setProcessingModeCode(HL7DataTransformHelper.CSFactory("R"));
        result.setAcceptAckCode(HL7DataTransformHelper.CSFactory("AL"));
        
        // Create the Sender
        result.setSender(HL7SenderTransforms.createMCCIMT000100UV01Sender(senderOID));

        // Create the Receiver
        result.getReceiver().add(HL7ReceiverTransforms.createMCCIMT000100UV01Receiver(receiverOID));
        
        result.setControlActProcess(createControlActProcess(patient, localDeviceId));
        return result;
    }
    
    public static PRPAIN201303UVMFMIMT700701UV01ControlActProcess createControlActProcess (PRPAMT201305UVPatient patient, String localDeviceId) {
        PRPAIN201303UVMFMIMT700701UV01ControlActProcess  controlActProcess = new PRPAIN201303UVMFMIMT700701UV01ControlActProcess();
        PRPAIN201303UVMFMIMT700701UV01RegistrationEvent regevent = new PRPAIN201303UVMFMIMT700701UV01RegistrationEvent();
        PRPAIN201303UVMFMIMT700701UV01Subject1 subject1 = new PRPAIN201303UVMFMIMT700701UV01Subject1();
  
        controlActProcess.setMoodCode("EVN");
        
        controlActProcess.setCode(HL7DataTransformHelper.CDFactory("PRPA_TE201303UV", HL7Constants.INTERACTION_ID_ROOT));
        
        /**
         * TODO: This needs to be broken out into separate methods.
         */
        PRPAIN201303UVMFMIMT700701UV01Subject2 subject = new PRPAIN201303UVMFMIMT700701UV01Subject2();              
        subject.setPatient(patient);
        regevent.setSubject1(subject);
        subject1.setRegistrationEvent(regevent);
        controlActProcess.getSubject().add(subject1);
        
        return controlActProcess;
    }

}
