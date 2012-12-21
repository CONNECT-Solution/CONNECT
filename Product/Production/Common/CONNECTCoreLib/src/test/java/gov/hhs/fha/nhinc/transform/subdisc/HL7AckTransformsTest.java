/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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

import static org.junit.Assert.assertEquals;

import javax.xml.bind.JAXBElement;

import org.hl7.v3.II;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.MCCIMT000100UV01Agent;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Organization;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.PRPAIN201305UV02;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

/**
 * @author achidamb
 *
 */
public class HL7AckTransformsTest {
    
    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    
    
     PRPAIN201305UV02 mockMessage = context.mock(PRPAIN201305UV02.class);
    
    /** This method verifies that Sender OID = Receiver OID in case if message failure 
     * occurs before sending to Nwhin. This unit test is created as part of Gateway-3178.
     */
    
    @Test
    public void checkOIDOfInitiatorErrorMessage() {
        
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();
        String SenderOID = "1.1";
        String ackMsg = "No targets were found for the Patient Discovery Request";
        ack = HL7AckTransforms.createAckErrorFrom201305(createPRPAIN201305UV02(SenderOID), ackMsg) ;
        assertEquals("1.1", ack.getReceiver().get(0).getDevice().getAsAgent().getValue()
                .getRepresentedOrganization().getValue().getId().get(0).getRoot());
        
    }
    
    public PRPAIN201305UV02 createPRPAIN201305UV02(String SenderOID) {
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
        return request;
    }

}
