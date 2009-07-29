/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.transform.subdisc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.CommunicationFunctionType;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.MCCIMT000200UV01Device;
import org.hl7.v3.MCCIMT000200UV01Sender;
import org.hl7.v3.MCCIMT000300UV01Device;
import org.hl7.v3.MCCIMT000300UV01Sender;

/**
 *
 * @author Jon Hoppesch
 */
public class HL7SenderTransforms {
    
    private static Log log = LogFactory.getLog(HL7SenderTransforms.class);

        public static MCCIMT000200UV01Sender createMCCIMT000200UV01Sender(String OID) {
        MCCIMT000200UV01Sender sender = new MCCIMT000200UV01Sender();

        // Check the input parameter
        if (OID == null) {
            OID="";
        }

        sender.setTypeCode(CommunicationFunctionType.SND);

        MCCIMT000200UV01Device senderDevice = new MCCIMT000200UV01Device();
        senderDevice.setDeterminerCode(HL7Constants.SENDER_DETERMINER_CODE);

        log.debug("Setting sender OID to " + OID);
        senderDevice.getId().add(HL7DataTransformHelper.IIFactory(OID));

        sender.setDevice(senderDevice);

        return sender;
    }
    
    public static MCCIMT000100UV01Sender createMCCIMT000100UV01Sender(String OID) {
        MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();

        // Check the input parameter
        if (OID == null) {
            OID="";
        }

        sender.setTypeCode(CommunicationFunctionType.SND);

        MCCIMT000100UV01Device senderDevice = new MCCIMT000100UV01Device();
        senderDevice.setDeterminerCode(HL7Constants.SENDER_DETERMINER_CODE);

        log.debug("Setting sender OID to " + OID);
        senderDevice.getId().add(HL7DataTransformHelper.IIFactory(OID));

        sender.setDevice(senderDevice);

        return sender;
    }

    static MCCIMT000300UV01Sender createMCCIMT000300UV01Sender(String OID) {
        MCCIMT000300UV01Sender sender = new MCCIMT000300UV01Sender();

        // Check the input parameter
        if (OID == null) {
            OID="";
        }

        sender.setTypeCode(CommunicationFunctionType.SND);

        MCCIMT000300UV01Device senderDevice = new MCCIMT000300UV01Device();
        senderDevice.setDeterminerCode(HL7Constants.SENDER_DETERMINER_CODE);

        log.debug("Setting sender OID to " + OID);
        senderDevice.getId().add(HL7DataTransformHelper.IIFactory(OID));

        sender.setDevice(senderDevice);

        return sender;
    }
}
