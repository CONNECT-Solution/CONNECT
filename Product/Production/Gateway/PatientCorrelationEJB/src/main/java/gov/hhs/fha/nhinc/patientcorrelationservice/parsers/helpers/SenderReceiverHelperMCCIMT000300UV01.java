/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientcorrelationservice.parsers.helpers;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import org.hl7.v3.CommunicationFunctionType;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000300UV01Device;
import org.hl7.v3.MCCIMT000300UV01Receiver;
import org.hl7.v3.MCCIMT000300UV01Sender;
import org.hl7.v3.MCCIMT000300UV01Sender;
import org.hl7.v3.TELExplicit;

/**
 *
 * @author rayj
 */
public class SenderReceiverHelperMCCIMT000300UV01 {

    public static MCCIMT000300UV01Sender CreateSender(II senderDeviceId) {
        MCCIMT000300UV01Sender sender = new MCCIMT000300UV01Sender();
        sender.setTypeCode(CommunicationFunctionType.SND);
        sender.setDevice(createDevice(senderDeviceId));
        return sender;
    }

    public static MCCIMT000300UV01Sender CreateSender() {
        return CreateSender(null);
    }

    public static MCCIMT000300UV01Receiver CreateReceiver(II receiverDeviceId) {
        MCCIMT000300UV01Receiver receiver = new MCCIMT000300UV01Receiver();
        receiver.setTypeCode(CommunicationFunctionType.RCV);
        receiver.setDevice(createDevice(receiverDeviceId));
        return receiver;
    }

    public static MCCIMT000300UV01Receiver CreateReceiver() {
        return CreateReceiver(null);
    }

    private static MCCIMT000300UV01Device createDevice(II deviceId) {

        MCCIMT000300UV01Device device = new MCCIMT000300UV01Device();
        device.setDeterminerCode(Constants.determinerCodeValue);

        device.getId().add(deviceId);

//        TELExplicit tel = new TELExplicit();
//        tel.getNullFlavor().add("NA");
//        device.getTelecom().add(tel);

        return device;
    }
}
