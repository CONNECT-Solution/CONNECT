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
package gov.hhs.fha.nhinc.transform.subdisc;

import javax.xml.bind.JAXBElement;
import org.hl7.v3.II;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.MCCIMT000100UV01Agent;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Organization;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.MCCIMT000300UV01Agent;
import org.hl7.v3.MCCIMT000300UV01Device;
import org.hl7.v3.MCCIMT000300UV01Organization;
import org.hl7.v3.MCCIMT000300UV01Receiver;
import org.hl7.v3.MCCIMT000300UV01Sender;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * @author achidamb
 *
 */
public class HL7AckTransformsTest {

    private final static String TEST_PROPERTIES_NAME = "gatewaytest.properties";

    private HL7MessageIdGenerator idGenerator;

    @Before
    public void setUpMockGenerator(){
        idGenerator = new HL7MessageIdGenerator() {

            @Override
            protected String getDefaultLocalDeviceId() {
                return "1.1";
            }
        };
    }

    /**
     * This method verifies that Sender OID = Receiver OID in case if message
     * failure occurs before sending to Nwhin. This unit test is created as part
     * of Gateway-3178.
     */
    @Test
    public void testCreateAckFrom201305WhenRequestNull() {
        PRPAIN201305UV02 request = null;
        String ackMsgText = null;
        HL7AckTransforms ackTransform = new HL7AckTransforms(idGenerator);
        assertNull(ackTransform.createAckFrom201305(request, ackMsgText).getAcceptAckCode());
    }

    @Test
    public void testCreateAckFrom201305WhenMsgIdNull() {
        String senderOID = "1.1";
        PRPAIN201305UV02 request = createPRPAIN201305UV02(senderOID);
        String ackMsgText = null;
        HL7AckTransforms ackTransform = new HL7AckTransforms(idGenerator);
        assertNotNull(ackTransform.createAckFrom201305(request, ackMsgText).getId().getRoot());
    }

    @Test
    public void testCreateAckFrom201305() {
        String senderOID = "1.1";
        PRPAIN201305UV02 request = createPRPAIN201305UV02(senderOID);
        setMessageId(request);
        String ackMsgText = "HL7 AckMessage";
        HL7AckTransforms ackTransform = new HL7AckTransforms(idGenerator);

        MCCIIN000002UV01 ack = ackTransform.createAckFrom201305(request, ackMsgText);
        assertNotNull(ack.getAcknowledgement().get(0).getAcknowledgementDetail().get(0).getText());
        assertEquals(ack.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization()
                .getValue().getId().get(0).getRoot(), "1.1");
        assertEquals(ack.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue()
                .getId().get(0).getRoot(), "2.2");
    }

    /* Tests for createAckErrorFrom201305 method of HL7AckTransforms */
    @Test
    public void checkOIDOfInitiatorErrorMessage() {

        MCCIIN000002UV01 ack = new MCCIIN000002UV01();
        String SenderOID = "1.1";
        String ackMsg = "No targets were found for the Patient Discovery Request";

        HL7AckTransforms ackTransform = new HL7AckTransforms(idGenerator);

        ack = ackTransform.createAckErrorFrom201305(createPRPAIN201305UV02(SenderOID), ackMsg);
        assertEquals("1.1", ack.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization()
                .getValue().getId().get(0).getRoot());

    }

    @Test
    public void testCreateAckErrorFrom201305WhenRequestNull() {
        PRPAIN201305UV02 request = null;
        String ackMsgText = null;
        HL7AckTransforms ackTransform = new HL7AckTransforms(idGenerator);
        assertNull(ackTransform.createAckErrorFrom201305(request, ackMsgText).getAcceptAckCode());
    }

    @Test
    public void testCreateAckErrorFrom201305WhenMessageIdNull() {
        String senderOID = "1.1";
        PRPAIN201305UV02 request = createPRPAIN201305UV02(senderOID);
        String ackMsgText = null;

        HL7AckTransforms ackTransform = new HL7AckTransforms(idGenerator);

        assertNotNull(ackTransform.createAckErrorFrom201305(request, ackMsgText).getId().getRoot());
    }

    @Test
    public void testCreateAckErrorFrom201305WhenMsgTxtNull() {
        String senderOID = "1.1";
        PRPAIN201305UV02 request = createPRPAIN201305UV02(senderOID);
        request = setMessageId(request);
        String ackMsgText = null;

        HL7AckTransforms ackTransform = new HL7AckTransforms(idGenerator);

        assertTrue(ackTransform.createAckErrorFrom201305(request, ackMsgText).getAcknowledgement().get(0)
                .getAcknowledgementDetail().isEmpty());
    }

    @Test
    public void testCreateAckErrorFrom201305WhenMsgTxtandmsgIdNull() {
        String senderOID = "1.1";
        PRPAIN201305UV02 request = createPRPAIN201305UV02(senderOID);
        String ackMsgText = null;

        HL7AckTransforms ackTransform = new HL7AckTransforms(idGenerator);

        MCCIIN000002UV01 ack = ackTransform.createAckErrorFrom201305(request, ackMsgText);
        assertTrue(ack.getAcknowledgement().isEmpty());
    }

    @Test
    public void createAckFrom201306WhenrequestNull() {
        PRPAIN201306UV02 request = null;
        String ackMsgText = null;

        HL7AckTransforms ackTransform = new HL7AckTransforms(idGenerator);

        assertNull(ackTransform.createAckFrom201306(request, ackMsgText).getAcceptAckCode());

    }

    @Test
    public void testCreateAckFrom201306WhenMessageIdNull() {
        PRPAIN201306UV02 request = createPRPAIN201306UV02();
        String ackMsgText = null;

        HL7AckTransforms ackTransform = new HL7AckTransforms(idGenerator);

        assertNotNull(ackTransform.createAckFrom201306(request, ackMsgText).getId().getRoot());
    }

    @Test
    public void testCreateAckFrom201306() {
        PRPAIN201306UV02 request = createPRPAIN201306UV02();
        setMessageId(request);
        String ackMsgText = "HL7 AckMessage";

        HL7AckTransforms ackTransform = new HL7AckTransforms(idGenerator);

        MCCIIN000002UV01 ack = ackTransform.createAckFrom201306(request, ackMsgText);
        assertNotNull(ack.getAcknowledgement().get(0).getAcknowledgementDetail().get(0).getText());
        assertEquals(ack.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue()
                .getId().get(0).getRoot(), "1.1");
        assertEquals(ack.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization()
                .getValue().getId().get(0).getRoot(), "2.2");
    }

    @Test
    public void createAckErrorFrom201306WhenrequestNull() {
        PRPAIN201306UV02 request = null;
        String ackMsgText = null;
        HL7AckTransforms ackTransform = new HL7AckTransforms(idGenerator);
        assertNull(ackTransform.createAckErrorFrom201306(request, ackMsgText).getAcceptAckCode());

    }

    @Test
    public void testCreateAckErrorFrom201306WhenMessageIdNull() {
        PRPAIN201306UV02 request = createPRPAIN201306UV02();
        String ackMsgText = null;
        HL7AckTransforms ackTransform = new HL7AckTransforms(idGenerator);
        assertNotNull(ackTransform.createAckErrorFrom201306(request, ackMsgText).getId().getRoot());
    }

    @Test
    public void testCreateAckErrorFrom201306() {
        PRPAIN201306UV02 request = createPRPAIN201306UV02();
        setMessageId(request);
        String ackMsgText = "HL7 AckMessage";
        HL7AckTransforms ackTransform = new HL7AckTransforms(idGenerator);
        assertNotNull(ackTransform.createAckErrorFrom201306(request, ackMsgText).getAcknowledgement().get(0)
                .getAcknowledgementDetail().get(0).getText());
    }

    private PRPAIN201306UV02 createPRPAIN201306UV02() {
        String SenderOID = "2.2";
        PRPAIN201306UV02 request = new PRPAIN201306UV02();
        MCCIMT000300UV01Sender sender = new MCCIMT000300UV01Sender();
        MCCIMT000300UV01Device device = new MCCIMT000300UV01Device();
        MCCIMT000300UV01Receiver receiver = new MCCIMT000300UV01Receiver();
        II e = new II();
        e.setRoot(SenderOID);

        MCCIMT000300UV01Agent agentVal = new MCCIMT000300UV01Agent();

        MCCIMT000300UV01Organization repOrgVal = new MCCIMT000300UV01Organization();
        repOrgVal.getId().add(e);

        org.hl7.v3.ObjectFactory oJaxbObjectFactory = new org.hl7.v3.ObjectFactory();
        JAXBElement<MCCIMT000300UV01Organization> repOrg = oJaxbObjectFactory
                .createMCCIMT000300UV01AgentRepresentedOrganization(repOrgVal);
        repOrg.setValue(repOrgVal);
        agentVal.setRepresentedOrganization(repOrg);

        JAXBElement<MCCIMT000300UV01Agent> agent = oJaxbObjectFactory.createMCCIMT000300UV01DeviceAsAgent(agentVal);
        device.setAsAgent(agent);
        sender.setDevice(device);
        request.setSender(sender);

        MCCIMT000300UV01Device receiverDevice = new MCCIMT000300UV01Device();
        II rec = new II();
        rec.setRoot("1.1");
        MCCIMT000300UV01Agent agentValReceiver = new MCCIMT000300UV01Agent();
        MCCIMT000300UV01Organization repOrgValReceiver = new MCCIMT000300UV01Organization();
        repOrgValReceiver.getId().add(rec);
        org.hl7.v3.ObjectFactory JaxbObjectFactory = new org.hl7.v3.ObjectFactory();
        JAXBElement<MCCIMT000300UV01Organization> repOrgReceiver = JaxbObjectFactory
                .createMCCIMT000300UV01AgentRepresentedOrganization(repOrgValReceiver);
        repOrgReceiver.setValue(repOrgValReceiver);
        agentValReceiver.setRepresentedOrganization(repOrgReceiver);

        JAXBElement<MCCIMT000300UV01Agent> agentReceiver = oJaxbObjectFactory
                .createMCCIMT000300UV01DeviceAsAgent(agentValReceiver);
        receiverDevice.setAsAgent(agentReceiver);
        receiver.setDevice(receiverDevice);
        request.getReceiver().add(receiver);
        return request;
    }

    private PRPAIN201305UV02 createPRPAIN201305UV02(String SenderOID) {
        PRPAIN201305UV02 request = new PRPAIN201305UV02();
        MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();
        MCCIMT000100UV01Device device = new MCCIMT000100UV01Device();
        II e = new II();
        e.setRoot(SenderOID);

        MCCIMT000100UV01Agent agentVal = new MCCIMT000100UV01Agent();

        MCCIMT000100UV01Organization repOrgVal = new MCCIMT000100UV01Organization();
        repOrgVal.getId().add(e);

        org.hl7.v3.ObjectFactory oJaxbObjectFactory = new org.hl7.v3.ObjectFactory();
        JAXBElement<MCCIMT000100UV01Organization> repOrg = oJaxbObjectFactory
                .createMCCIMT000100UV01AgentRepresentedOrganization(repOrgVal);
        repOrg.setValue(repOrgVal);
        agentVal.setRepresentedOrganization(repOrg);

        JAXBElement<MCCIMT000100UV01Agent> agent = oJaxbObjectFactory.createMCCIMT000100UV01DeviceAsAgent(agentVal);
        device.setAsAgent(agent);
        sender.setDevice(device);
        request.setSender(sender);

        MCCIMT000100UV01Device receiverDevice = new MCCIMT000100UV01Device();
        II rec = new II();
        rec.setRoot("2.2");
        MCCIMT000100UV01Agent agentValReceiver = new MCCIMT000100UV01Agent();
        MCCIMT000100UV01Organization repOrgValReceiver = new MCCIMT000100UV01Organization();
        repOrgValReceiver.getId().add(rec);
        org.hl7.v3.ObjectFactory JaxbObjectFactory = new org.hl7.v3.ObjectFactory();
        JAXBElement<MCCIMT000100UV01Organization> repOrgReceiver = JaxbObjectFactory
                .createMCCIMT000100UV01AgentRepresentedOrganization(repOrgValReceiver);
        repOrgReceiver.setValue(repOrgValReceiver);
        agentValReceiver.setRepresentedOrganization(repOrgReceiver);

        JAXBElement<MCCIMT000100UV01Agent> agentReceiver = oJaxbObjectFactory
                .createMCCIMT000100UV01DeviceAsAgent(agentValReceiver);
        receiverDevice.setAsAgent(agentReceiver);
        MCCIMT000100UV01Receiver receiver = new MCCIMT000100UV01Receiver();
        receiver.setDevice(receiverDevice);
        request.getReceiver().add(receiver);
        return request;
    }

    private II createII() {
        II ii = new II();
        ii.setAssigningAuthorityName("CONNECT");
        ii.setExtension("D123401");
        ii.setRoot("1.1");
        return ii;
    }

    private PRPAIN201305UV02 setMessageId(PRPAIN201305UV02 request) {
        request.setId(createII());
        return request;
    }

    private PRPAIN201306UV02 setMessageId(PRPAIN201306UV02 request) {
        request.setId(createII());
        return request;
    }

}
