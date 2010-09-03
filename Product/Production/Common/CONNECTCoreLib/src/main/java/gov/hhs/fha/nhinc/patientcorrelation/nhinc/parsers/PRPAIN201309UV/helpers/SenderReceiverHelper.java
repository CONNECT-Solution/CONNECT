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
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.helpers;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import org.hl7.v3.CommunicationFunctionType;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.TELExplicit;

/**
 *
 * @author rayj
 */
public class SenderReceiverHelper {

    public static MCCIMT000100UV01Sender CreateSender(String senderDeviceId) {
        MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();
        sender.setTypeCode(CommunicationFunctionType.SND);
        sender.setDevice(createDevice(senderDeviceId));
        return sender;
    }

    public static MCCIMT000100UV01Sender CreateSender() {
        return CreateSender(null);
    }

    public static MCCIMT000100UV01Receiver CreateReceiver(String receiverDeviceId) {
        MCCIMT000100UV01Receiver receiver = new MCCIMT000100UV01Receiver();
        receiver.setTypeCode(CommunicationFunctionType.RCV);
        receiver.setDevice(createDevice(receiverDeviceId));
        return receiver;
    }

    public static MCCIMT000100UV01Receiver CreateReceiver() {
        return CreateReceiver(null);
    }

    private static MCCIMT000100UV01Device createDevice(String deviceIdRoot) {

        MCCIMT000100UV01Device device = new MCCIMT000100UV01Device();
        device.setDeterminerCode(Constants.determinerCodeValue);
        II deviceId = null;

        if (NullChecker.isNullish(deviceIdRoot)) {
            deviceId = IIHelper.IIFactoryCreateNull();
        } else {
            deviceId = IIHelper.IIFactory(deviceIdRoot, null);
        }
        device.getId().add(deviceId);

//        TELExplicit tel = new TELExplicit();
//        tel.getNullFlavor().add("NA");
//        device.getTelecom().add(tel);

        return device;
    }
}
