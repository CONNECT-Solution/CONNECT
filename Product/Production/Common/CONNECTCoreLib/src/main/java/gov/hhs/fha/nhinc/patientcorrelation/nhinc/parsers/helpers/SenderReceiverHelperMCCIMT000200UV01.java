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
     *
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
     *
     * @param senderDeviceId
     * @return sender
     */
    public static MCCIMT000200UV01Sender CreateSender() {
        return CreateSender(null);
    }

    /**
     * Generate receiver element
     *
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
     *
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
