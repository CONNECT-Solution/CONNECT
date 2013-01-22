/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2009-2013, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *      copyright notice, this list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *    * Neither the name of the United States Government nor the
 *      names of its contributors may be used to endorse or promote products
 *      derived from this software without specific prior written permission.
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
package gov.hhs.fha.nhinc.transform.audit;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.hl7.v3.II;
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
import org.junit.Assert;
import org.junit.Test;

/**
 * @author msw
 * 
 */
public class PatientDiscoveryTransformsGetHCIDTest {

    protected static final String SENDER = "sender";
    protected static final String RECEIVER = "receiver";

    /**
     * Test method for
     * {@link gov.hhs.fha.nhinc.transform.audit.PatientDiscoveryTransforms#getHCIDFromReceiver(org.hl7.v3.PRPAIN201305UV02)}
     * .
     */
    @Test
    public void testGetHCIDFromReceiverPRPAIN201305UV02() {
        PRPAIN201305UV02 message = create0001ReceiverRequest(false);

        String expected = RECEIVER;

        PatientDiscoveryTransforms instance = getPatientDiscoveryTransforms();
        long startTime = System.currentTimeMillis();
        String result = instance.getHCIDFromReceiver(message);
        long endTime = System.currentTimeMillis();
        System.out.println("milliseconds to execute: " + String.valueOf(endTime - startTime));

        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for
     * {@link gov.hhs.fha.nhinc.transform.audit.PatientDiscoveryTransforms#getHCIDFromReceiver(org.hl7.v3.PRPAIN201305UV02)}
     * .
     */
    @Test
    public void testGetHCIDFromReceiverPRPAIN201305UV02Alt() {
        PRPAIN201305UV02 message = create0001ReceiverRequest(true);

        String expected = RECEIVER;

        PatientDiscoveryTransforms instance = getPatientDiscoveryTransforms();
        String result = instance.getHCIDFromReceiver(message);

        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for
     * {@link gov.hhs.fha.nhinc.transform.audit.PatientDiscoveryTransforms#getHCIDFromSender(org.hl7.v3.PRPAIN201305UV02)}
     * .
     */
    @Test
    public void testGetHCIDFromSenderPRPAIN201305UV02() {
        PRPAIN201305UV02 message = create0001SenderRequest(false);

        String expected = SENDER;

        PatientDiscoveryTransforms instance = getPatientDiscoveryTransforms();

        long startTime = System.currentTimeMillis();
        String result = instance.getHCIDFromSender(message);
        long endTime = System.currentTimeMillis();
        System.out.println("milliseconds to execute: " + String.valueOf(endTime - startTime));

        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for
     * {@link gov.hhs.fha.nhinc.transform.audit.PatientDiscoveryTransforms#getHCIDFromSender(org.hl7.v3.PRPAIN201305UV02)}
     * .
     */
    @Test
    public void testGetHCIDFromSenderPRPAIN201305UV02Alt() {
        PRPAIN201305UV02 message = create0001SenderRequest(true);

        String expected = SENDER;

        PatientDiscoveryTransforms instance = getPatientDiscoveryTransforms();
        String result = instance.getHCIDFromSender(message);

        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for
     * {@link gov.hhs.fha.nhinc.transform.audit.PatientDiscoveryTransforms#getHCIDFromSender(org.hl7.v3.PRPAIN201306UV02)}
     * .
     */
    @Test
    public void testGetHCIDFromSenderPRPAIN201306UV02() {
        PRPAIN201306UV02 message = create0003SenderResponse(false);

        String expected = SENDER;

        long startTime = System.currentTimeMillis();
        PatientDiscoveryTransforms instance = getPatientDiscoveryTransforms();
        String result = instance.getHCIDFromSender(message);
        long endTime = System.currentTimeMillis();
        System.out.println("milliseconds to execute: " + String.valueOf(endTime - startTime));
        
        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for
     * {@link gov.hhs.fha.nhinc.transform.audit.PatientDiscoveryTransforms#getHCIDFromSender(org.hl7.v3.PRPAIN201306UV02)}
     * .
     */
    @Test
    public void testGetHCIDFromSenderPRPAIN201306UV02Alt() {
        PRPAIN201306UV02 message = create0003SenderResponse(true);

        String expected = SENDER;

        PatientDiscoveryTransforms instance = getPatientDiscoveryTransforms();
        String result = instance.getHCIDFromSender(message);

        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for
     * {@link gov.hhs.fha.nhinc.transform.audit.PatientDiscoveryTransforms#getHCIDFromReceiver(org.hl7.v3.PRPAIN201306UV02)}
     * .
     */
    @Test
    public void testGetHCIDFromReceiverPRPAIN201306UV02() {
        PRPAIN201306UV02 message = create0003ReceiverResponse(false);

        String expected = RECEIVER;

        PatientDiscoveryTransforms instance = getPatientDiscoveryTransforms();
        long startTime = System.currentTimeMillis();
        String result = instance.getHCIDFromReceiver(message);
        long endTime = System.currentTimeMillis();
        System.out.println("milliseconds to execute: " + String.valueOf(endTime - startTime));

        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for
     * {@link gov.hhs.fha.nhinc.transform.audit.PatientDiscoveryTransforms#getHCIDFromReceiver(org.hl7.v3.PRPAIN201306UV02)}
     * .
     */
    @Test
    public void testGetHCIDFromReceiverPRPAIN201306UV02Alt() {
        PRPAIN201306UV02 message = create0003ReceiverResponse(true);

        String expected = RECEIVER;

        PatientDiscoveryTransforms instance = getPatientDiscoveryTransforms();
        String result = instance.getHCIDFromReceiver(message);

        Assert.assertEquals(expected, result);
    }

    /**
     * @return
     */
    private PatientDiscoveryTransforms getPatientDiscoveryTransforms() {
        return new PatientDiscoveryTransforms();
    }

    private PRPAIN201305UV02 create0001SenderRequest(boolean withId) {
        PRPAIN201305UV02 requestMessage = new PRPAIN201305UV02();
        MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();

        sender.setDevice(withId ? create0001DeviceWithId(SENDER) : create0001Device(SENDER));
        requestMessage.setSender(sender);

        return requestMessage;
    }

    private PRPAIN201305UV02 create0001ReceiverRequest(boolean withId) {
        PRPAIN201305UV02 requestMessage = new PRPAIN201305UV02();
        MCCIMT000100UV01Receiver receiver = new MCCIMT000100UV01Receiver();

        receiver.setDevice(withId ? create0001DeviceWithId(RECEIVER) : create0001Device(RECEIVER));
        requestMessage.getReceiver().add(receiver);

        return requestMessage;
    }

    private MCCIMT000100UV01Device create0001Device(String value) {
        MCCIMT000100UV01Device device = new MCCIMT000100UV01Device();
        MCCIMT000100UV01Agent agent = new MCCIMT000100UV01Agent();
        JAXBElement<MCCIMT000100UV01Agent> asAgent = new JAXBElement<MCCIMT000100UV01Agent>(new QName("org.hl7.v3"),
                MCCIMT000100UV01Agent.class, agent);
        MCCIMT000100UV01Organization organization = new MCCIMT000100UV01Organization();
        JAXBElement<MCCIMT000100UV01Organization> org = new JAXBElement<MCCIMT000100UV01Organization>(new QName(
                "org.hl7.v3"), MCCIMT000100UV01Organization.class, organization);
        II senderII = new II();

        senderII.setRoot(value);
        organization.getId().add(senderII);
        agent.setRepresentedOrganization(org);
        device.setAsAgent(asAgent);
        return device;
    }

    private MCCIMT000100UV01Device create0001DeviceWithId(String value) {
        MCCIMT000100UV01Device device = new MCCIMT000100UV01Device();
        II senderII = new II();

        senderII.setRoot(value);
        device.getId().add(senderII);
        return device;
    }

    private PRPAIN201306UV02 create0003SenderResponse(boolean withId) {
        PRPAIN201306UV02 responseMessage = new PRPAIN201306UV02();
        MCCIMT000300UV01Sender sender = new MCCIMT000300UV01Sender();

        sender.setDevice(withId ? create0003DeviceWithId(SENDER) : create0003Device(SENDER));
        responseMessage.setSender(sender);

        return responseMessage;
    }

    private PRPAIN201306UV02 create0003ReceiverResponse(boolean withId) {
        PRPAIN201306UV02 responseMessage = new PRPAIN201306UV02();
        MCCIMT000300UV01Receiver receiver = new MCCIMT000300UV01Receiver();

        receiver.setDevice(withId ? create0003DeviceWithId(RECEIVER) : create0003Device(RECEIVER));
        responseMessage.getReceiver().add(receiver);

        return responseMessage;
    }

    private MCCIMT000300UV01Device create0003Device(String value) {
        MCCIMT000300UV01Device device = new MCCIMT000300UV01Device();
        MCCIMT000300UV01Agent agent = new MCCIMT000300UV01Agent();
        JAXBElement<MCCIMT000300UV01Agent> asAgent = new JAXBElement<MCCIMT000300UV01Agent>(new QName("org.hl7.v3"),
                MCCIMT000300UV01Agent.class, agent);
        MCCIMT000300UV01Organization organization = new MCCIMT000300UV01Organization();
        JAXBElement<MCCIMT000300UV01Organization> org = new JAXBElement<MCCIMT000300UV01Organization>(new QName(
                "org.hl7.v3"), MCCIMT000300UV01Organization.class, organization);
        II senderII = new II();

        senderII.setRoot(value);
        organization.getId().add(senderII);
        agent.setRepresentedOrganization(org);
        device.setAsAgent(asAgent);
        return device;
    }

    private MCCIMT000300UV01Device create0003DeviceWithId(String value) {
        MCCIMT000300UV01Device device = new MCCIMT000300UV01Device();
        II senderII = new II();

        senderII.setRoot(value);
        device.getId().add(senderII);
        return device;
    }
}
