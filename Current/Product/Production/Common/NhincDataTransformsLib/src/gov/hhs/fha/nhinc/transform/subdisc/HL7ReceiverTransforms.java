/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.transform.subdisc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.CommunicationFunctionType;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000200UV01Device;
import org.hl7.v3.MCCIMT000200UV01Receiver;
import org.hl7.v3.MCCIMT000300UV01Device;
import org.hl7.v3.MCCIMT000300UV01Receiver;

/**
 *
 * @author Jon Hoppesch
 */
public class HL7ReceiverTransforms {
    
    private static Log log = LogFactory.getLog(HL7DataTransformHelper.class);
    
        public static MCCIMT000200UV01Receiver createMCCIMT000200UV01Receiver(String OID) {
        MCCIMT000200UV01Receiver receiver = new MCCIMT000200UV01Receiver();

        // Check the input parameter
        if (OID == null) {
            OID="";
        }

        receiver.setTypeCode(CommunicationFunctionType.RCV);

        MCCIMT000200UV01Device receiverDevice = new MCCIMT000200UV01Device();
        receiverDevice.setDeterminerCode(HL7Constants.RECEIVER_DETERMINER_CODE);

        log.debug("Setting receiver OID to " + OID);
        receiverDevice.getId().add(HL7DataTransformHelper.IIFactory(OID));

        receiver.setDevice(receiverDevice);

        return receiver;
    }
    
    public static MCCIMT000100UV01Receiver createMCCIMT000100UV01Receiver(String OID) {
        MCCIMT000100UV01Receiver receiver = new MCCIMT000100UV01Receiver();

        // Check the input parameter
        if (OID == null) {
            OID="";
        }

        receiver.setTypeCode(CommunicationFunctionType.RCV);

        MCCIMT000100UV01Device receiverDevice = new MCCIMT000100UV01Device();
        receiverDevice.setDeterminerCode(HL7Constants.RECEIVER_DETERMINER_CODE);

        log.debug("Setting receiver OID to " + OID);
        receiverDevice.getId().add(HL7DataTransformHelper.IIFactory(OID));

        receiver.setDevice(receiverDevice);

        return receiver;
    }

    static MCCIMT000300UV01Receiver createMCCIMT000300UV01Receiver(String OID) {
        MCCIMT000300UV01Receiver receiver = new MCCIMT000300UV01Receiver();

        // Check the input parameter
        if (OID == null) {
            OID="";
        }

        receiver.setTypeCode(CommunicationFunctionType.RCV);

        MCCIMT000300UV01Device receiverDevice = new MCCIMT000300UV01Device();
        receiverDevice.setDeterminerCode(HL7Constants.RECEIVER_DETERMINER_CODE);

        log.debug("Setting receiver OID to " + OID);
        receiverDevice.getId().add(HL7DataTransformHelper.IIFactory(OID));

        receiver.setDevice(receiverDevice);

        return receiver;
    }

}
