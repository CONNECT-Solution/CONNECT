/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.transform.subdisc;

import javax.xml.bind.JAXBElement;
import org.hl7.v3.*;
        
/**
 *
 * @author Jon Hoppesch
 */
public class HL7PRPA201305Transforms {

    public static PRPAIN201305UV createPRPA201305 (PRPAMT201301UVPatient patient, String senderOID, String receiverOID, String localDeviceId) {
        PRPAIN201305UV result = new PRPAIN201305UV();
        
        // Create the 201305 message header fields
        result.setITSVersion(HL7Constants.ITS_VERSION);
        result.setId(HL7MessageIdGenerator.GenerateHL7MessageId(localDeviceId));
        result.setCreationTime(HL7DataTransformHelper.CreationTimeFactory());
        result.setInteractionId(HL7DataTransformHelper.IIFactory(HL7Constants.INTERACTION_ID_ROOT, "PRPA_IN201305UV"));
        result.setProcessingCode(HL7DataTransformHelper.CSFactory("P"));
        result.setProcessingModeCode(HL7DataTransformHelper.CSFactory("R"));
        result.setAcceptAckCode(HL7DataTransformHelper.CSFactory("AL"));
        
        // Create the Sender
        result.setSender(HL7SenderTransforms.createMCCIMT000100UV01Sender(senderOID));

        // Create the Receiver
        result.getReceiver().add(HL7ReceiverTransforms.createMCCIMT000100UV01Receiver(receiverOID));
        
        result.setControlActProcess(createQUQIMT021001UV01ControlActProcess(patient, localDeviceId));
        
        return result;
    }
    
    public static PRPAIN201305UVQUQIMT021001UV01ControlActProcess createQUQIMT021001UV01ControlActProcess (PRPAMT201301UVPatient patient, String localDeviceId) {
        PRPAIN201305UVQUQIMT021001UV01ControlActProcess  controlActProcess = new PRPAIN201305UVQUQIMT021001UV01ControlActProcess();
        
        controlActProcess.setMoodCode("EVN");
        
        controlActProcess.setCode(HL7DataTransformHelper.CDFactory("PRPA_TE201305UV", HL7Constants.INTERACTION_ID_ROOT));
        
        controlActProcess.setQueryByParameter(HL7QueryParamsTransforms.createQueryParams(patient, localDeviceId));
        
        return controlActProcess;
    }
    
    
}
