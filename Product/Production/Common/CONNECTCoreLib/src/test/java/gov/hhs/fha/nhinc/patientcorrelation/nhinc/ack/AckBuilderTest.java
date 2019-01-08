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
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.ack;

import org.hl7.v3.II;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.PRPAIN201301UV02;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * @author achidambaram
 *
 */
public class AckBuilderTest {

    @Test
    public void testBuildAck() {
        AckBuilder ackBuilder = new AckBuilder();
        MCCIIN000002UV01 ack = ackBuilder.buildAck(createPRPAIN201301UV02Message());
        assertEquals(ack.getReceiver().get(0).getDevice().getId().get(0).getAssigningAuthorityName(),"CONNECT");
        assertEquals(ack.getReceiver().get(0).getDevice().getId().get(0).getExtension(), "1.16.17.19");
    }

    @Test
    public void testBuildAckWhenMessageNull() {
        AckBuilder ackBuilder = new AckBuilder();
        PRPAIN201301UV02 originalMessage = null;
        MCCIIN000002UV01 ack = ackBuilder.buildAck(originalMessage);
        assertNull(ack.getReceiver().get(0).getDevice().getId().get(0));
    }

    @Test
    public void testBuildAckWhenSenderDeviceNull() {
        AckBuilder ackBuilder = new AckBuilder();
        PRPAIN201301UV02 originalMessage = new PRPAIN201301UV02();
        MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();
        MCCIMT000100UV01Device senderDevice = null;
        sender.setDevice(senderDevice);
        originalMessage.setSender(sender);
        MCCIIN000002UV01 ack = ackBuilder.buildAck(originalMessage);
        assertNull(ack.getReceiver().get(0).getDevice().getId().get(0));
    }

    @Test
    public void testBuildAckWhenSenderNull() {
        AckBuilder ackBuilder = new AckBuilder();
        PRPAIN201301UV02 originalMessage = new PRPAIN201301UV02();
        MCCIIN000002UV01 ack = ackBuilder.buildAck(originalMessage);
        assertNull(ack.getReceiver().get(0).getDevice().getId().get(0));
    }

    @Test
    public void testBuildAckWhenSenderDeviceIdNull() {
        AckBuilder ackBuilder = new AckBuilder();
        PRPAIN201301UV02 originalMessage = new PRPAIN201301UV02();
        MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();
        MCCIMT000100UV01Device senderDevice = new MCCIMT000100UV01Device();
        sender.setDevice(senderDevice);
        originalMessage.setSender(sender);
        MCCIIN000002UV01 ack = ackBuilder.buildAck(originalMessage);
        assertNull(ack.getReceiver().get(0).getDevice().getId().get(0));
    }


    private PRPAIN201301UV02 createPRPAIN201301UV02Message() {
        PRPAIN201301UV02 originalMessage = new PRPAIN201301UV02();
        originalMessage.setSender(createSender());
        return originalMessage;
    }

    private MCCIMT000100UV01Sender createSender() {
        MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();
        sender.setDevice(createSenderDevice());
        return sender;
    }

    private MCCIMT000100UV01Device createSenderDevice() {
        MCCIMT000100UV01Device senderDevice = new MCCIMT000100UV01Device();
        senderDevice.getId().add(createII());
        return senderDevice;
    }

    private II createII() {
        II ii = new II();
        ii.setAssigningAuthorityName("CONNECT");
        ii.setExtension("1.16.17.19");
        ii.setRoot("1.1");
        return ii;
    }

}
