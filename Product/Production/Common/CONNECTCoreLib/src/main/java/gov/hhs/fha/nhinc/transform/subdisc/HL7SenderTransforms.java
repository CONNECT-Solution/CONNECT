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
import org.hl7.v3.CommunicationFunctionType;
import org.hl7.v3.EntityClassDevice;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000100UV01Agent;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Organization;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.MCCIMT000200UV01Agent;
import org.hl7.v3.MCCIMT000200UV01Device;
import org.hl7.v3.MCCIMT000200UV01Organization;
import org.hl7.v3.MCCIMT000200UV01Sender;
import org.hl7.v3.MCCIMT000300UV01Agent;
import org.hl7.v3.MCCIMT000300UV01Device;
import org.hl7.v3.MCCIMT000300UV01Organization;
import org.hl7.v3.MCCIMT000300UV01Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jon Hoppesch
 */
public class HL7SenderTransforms {

    private static final Logger LOG = LoggerFactory.getLogger(HL7SenderTransforms.class);

    /**
     * Create sender element. The passed OID will be used as the HL7 device application id and the organization home
     * community id.
     *
     * @param OID
     * @return sender
     */
    public static MCCIMT000200UV01Sender createMCCIMT000200UV01Sender(String OID) {
        MCCIMT000200UV01Sender sender = new MCCIMT000200UV01Sender();

        // Check the input parameter
        if (OID == null) {
            OID = "";
        }

        sender.setTypeCode(CommunicationFunctionType.SND);

        MCCIMT000200UV01Device senderDevice = new MCCIMT000200UV01Device();
        senderDevice.setDeterminerCode(HL7Constants.SENDER_DETERMINER_CODE);
        senderDevice.setClassCode(EntityClassDevice.DEV);
        LOG.debug("Setting sender device id (applicationId) to " + OID);
        senderDevice.getId().add(HL7DataTransformHelper.IIFactory(OID));

        MCCIMT000200UV01Agent agent = new MCCIMT000200UV01Agent();
        MCCIMT000200UV01Organization org = new MCCIMT000200UV01Organization();
        org.setClassCode(HL7Constants.ORG_CLASS_CODE);
        org.setDeterminerCode(HL7Constants.SENDER_DETERMINER_CODE);
        II id = HL7DataTransformHelper.IIFactory(OID);
        org.getId().add(id);

        javax.xml.namespace.QName xmlqnameorg = new javax.xml.namespace.QName("urn:hl7-org:v3",
                "representedOrganization");
        JAXBElement<MCCIMT000200UV01Organization> orgElem = new JAXBElement<>(xmlqnameorg,
                MCCIMT000200UV01Organization.class, org);
        agent.setRepresentedOrganization(orgElem);
        agent.getClassCode().add(HL7Constants.AGENT_CLASS_CODE);

        javax.xml.namespace.QName xmlqnameagent = new javax.xml.namespace.QName("urn:hl7-org:v3", "asAgent");
        JAXBElement<MCCIMT000200UV01Agent> agentElem = new JAXBElement<>(xmlqnameagent, MCCIMT000200UV01Agent.class,
                agent);

        senderDevice.setAsAgent(agentElem);

        sender.setDevice(senderDevice);

        return sender;
    }

    /**
     * Create sender element. The passed OID will be used as the HL7 device application id and the organization home
     * community id.
     *
     * @param OID
     * @return sender
     */
    public static MCCIMT000100UV01Sender createMCCIMT000100UV01Sender(String OID) {
        MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();

        // Check the input parameter
        if (OID == null) {
            OID = "";
        }

        sender.setTypeCode(CommunicationFunctionType.SND);

        MCCIMT000100UV01Device senderDevice = new MCCIMT000100UV01Device();
        senderDevice.setDeterminerCode(HL7Constants.SENDER_DETERMINER_CODE);
        senderDevice.setClassCode(EntityClassDevice.DEV);
        senderDevice.getId().add(HL7DataTransformHelper.IIFactory(OID));

        MCCIMT000100UV01Agent agent = new MCCIMT000100UV01Agent();
        MCCIMT000100UV01Organization org = new MCCIMT000100UV01Organization();
        org.setClassCode(HL7Constants.ORG_CLASS_CODE);
        org.setDeterminerCode(HL7Constants.SENDER_DETERMINER_CODE);
        II id = HL7DataTransformHelper.IIFactory(OID);
        org.getId().add(id);

        javax.xml.namespace.QName xmlqnameorg = new javax.xml.namespace.QName("urn:hl7-org:v3",
                "representedOrganization");
        JAXBElement<MCCIMT000100UV01Organization> orgElem = new JAXBElement<>(xmlqnameorg,
                MCCIMT000100UV01Organization.class, org);
        agent.setRepresentedOrganization(orgElem);
        agent.getClassCode().add(HL7Constants.AGENT_CLASS_CODE);

        javax.xml.namespace.QName xmlqnameagent = new javax.xml.namespace.QName("urn:hl7-org:v3", "asAgent");
        JAXBElement<MCCIMT000100UV01Agent> agentElem = new JAXBElement<>(xmlqnameagent, MCCIMT000100UV01Agent.class,
                agent);

        senderDevice.setAsAgent(agentElem);

        sender.setDevice(senderDevice);

        return sender;
    }

    /**
     * Create sender element. The passed OID will be used as the HL7 device application id and the organization home
     * community id.
     *
     * @param OID
     * @return sender
     */
    public static MCCIMT000300UV01Sender createMCCIMT000300UV01Sender(String OID) {
        MCCIMT000300UV01Sender sender = new MCCIMT000300UV01Sender();

        // Check the input parameter
        if (OID == null) {
            OID = "";
        }

        sender.setTypeCode(CommunicationFunctionType.SND);

        MCCIMT000300UV01Device senderDevice = new MCCIMT000300UV01Device();
        senderDevice.setDeterminerCode(HL7Constants.SENDER_DETERMINER_CODE);
        senderDevice.setClassCode(EntityClassDevice.DEV);
        senderDevice.getId().add(HL7DataTransformHelper.IIFactory(OID));

        MCCIMT000300UV01Agent agent = new MCCIMT000300UV01Agent();
        MCCIMT000300UV01Organization org = new MCCIMT000300UV01Organization();
        org.setClassCode(HL7Constants.ORG_CLASS_CODE);
        org.setDeterminerCode(HL7Constants.SENDER_DETERMINER_CODE);
        II id = HL7DataTransformHelper.IIFactory(OID);
        org.getId().add(id);

        javax.xml.namespace.QName xmlqnameorg = new javax.xml.namespace.QName("urn:hl7-org:v3",
                "representedOrganization");
        JAXBElement<MCCIMT000300UV01Organization> orgElem = new JAXBElement<>(xmlqnameorg,
                MCCIMT000300UV01Organization.class, org);
        agent.setRepresentedOrganization(orgElem);
        agent.getClassCode().add(HL7Constants.AGENT_CLASS_CODE);

        javax.xml.namespace.QName xmlqnameagent = new javax.xml.namespace.QName("urn:hl7-org:v3", "asAgent");
        JAXBElement<MCCIMT000300UV01Agent> agentElem = new JAXBElement<>(xmlqnameagent, MCCIMT000300UV01Agent.class,
                agent);

        senderDevice.setAsAgent(agentElem);

        sender.setDevice(senderDevice);

        return sender;
    }
}
