/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers;

import org.hl7.v3.CommunicationFunctionType;
import org.hl7.v3.EntityClassDevice;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000200UV01Device;
import org.hl7.v3.MCCIMT000200UV01Receiver;
import org.hl7.v3.MCCIMT000200UV01Sender;

/**
 *
 * @author rayj
 */
public class SenderReceiverHelperMCCIMT000200UV01 {

    /**
     * Generate sender element
     * @param senderDeviceId
     * @return sender
     */
    public static MCCIMT000200UV01Sender CreateSender(II senderDeviceId) {
        MCCIMT000200UV01Sender sender = new MCCIMT000200UV01Sender();
        sender.setTypeCode(CommunicationFunctionType.SND);
        sender.setDevice(createDevice(senderDeviceId));
        return sender;
    }

    /**
     * Generate empty sender element
     * @param senderDeviceId
     * @return sender
     */
    public static MCCIMT000200UV01Sender CreateSender() {
        return CreateSender(null);
    }

    /**
     * Generate receiver element
     * @param receiverDeviceId
     * @return receiver
     */
    public static MCCIMT000200UV01Receiver CreateReceiver(II receiverDeviceId) {
        MCCIMT000200UV01Receiver receiver = new MCCIMT000200UV01Receiver();
        receiver.setTypeCode(CommunicationFunctionType.RCV);
        receiver.setDevice(createDevice(receiverDeviceId));
        return receiver;
    }

    /**
     * Generate empty receiver element
     * @param receiverDeviceId
     * @return receiver
     */
    public static MCCIMT000200UV01Receiver CreateReceiver() {
        return CreateReceiver(null);
    }

    private static MCCIMT000200UV01Device createDevice(II deviceId) {

        MCCIMT000200UV01Device device = new MCCIMT000200UV01Device();
        device.setDeterminerCode(Constants.determinerCodeValue);
        device.setClassCode(EntityClassDevice.DEV);

        device.getId().add(deviceId);

        return device;
    }
}
