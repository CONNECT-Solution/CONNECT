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

package gov.hhs.fha.nhinc.transform.subdisc;

import javax.xml.bind.JAXBElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
        senderDevice.setClassCode(EntityClassDevice.DEV);
        log.debug("Setting sender OID to 1.2.345.678.999");
        senderDevice.getId().add(HL7DataTransformHelper.IIFactory("1.2.345.678.999"));


        MCCIMT000200UV01Agent agent = new MCCIMT000200UV01Agent();
        MCCIMT000200UV01Organization org = new MCCIMT000200UV01Organization();
        org.setClassCode(HL7Constants.ORG_CLASS_CODE);
        org.setDeterminerCode(HL7Constants.SENDER_DETERMINER_CODE);
        II id = HL7DataTransformHelper.IIFactory(OID);
        org.getId().add(id);

        javax.xml.namespace.QName xmlqnameorg = new javax.xml.namespace.QName("urn:hl7-org:v3", "representedOrganization");
        JAXBElement<MCCIMT000200UV01Organization> orgElem = new JAXBElement<MCCIMT000200UV01Organization>(xmlqnameorg, MCCIMT000200UV01Organization.class, org);
        agent.setRepresentedOrganization(orgElem);
        agent.getClassCode().add(HL7Constants.AGENT_CLASS_CODE);

        javax.xml.namespace.QName xmlqnameagent = new javax.xml.namespace.QName("urn:hl7-org:v3", "asAgent");
        JAXBElement<MCCIMT000200UV01Agent> agentElem = new JAXBElement<MCCIMT000200UV01Agent>(xmlqnameagent, MCCIMT000200UV01Agent.class, agent);

        senderDevice.setAsAgent(agentElem);

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
        senderDevice.setClassCode(EntityClassDevice.DEV);
        log.debug("Setting sender OID to 1.2.345.678.999");
        senderDevice.getId().add(HL7DataTransformHelper.IIFactory("1.2.345.678.999"));


        MCCIMT000100UV01Agent agent = new MCCIMT000100UV01Agent();
        MCCIMT000100UV01Organization org = new MCCIMT000100UV01Organization();
        org.setClassCode(HL7Constants.ORG_CLASS_CODE);
        org.setDeterminerCode(HL7Constants.SENDER_DETERMINER_CODE);
        II id = HL7DataTransformHelper.IIFactory(OID);
        org.getId().add(id);

        javax.xml.namespace.QName xmlqnameorg = new javax.xml.namespace.QName("urn:hl7-org:v3", "representedOrganization");
        JAXBElement<MCCIMT000100UV01Organization> orgElem = new JAXBElement<MCCIMT000100UV01Organization>(xmlqnameorg, MCCIMT000100UV01Organization.class, org);
        agent.setRepresentedOrganization(orgElem);
        agent.getClassCode().add(HL7Constants.AGENT_CLASS_CODE);

        javax.xml.namespace.QName xmlqnameagent = new javax.xml.namespace.QName("urn:hl7-org:v3", "asAgent");
        JAXBElement<MCCIMT000100UV01Agent> agentElem = new JAXBElement<MCCIMT000100UV01Agent>(xmlqnameagent, MCCIMT000100UV01Agent.class, agent);

        senderDevice.setAsAgent(agentElem);

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
        senderDevice.setClassCode(EntityClassDevice.DEV);
        log.debug("Setting sender OID to 1.2.345.678.999");
        senderDevice.getId().add(HL7DataTransformHelper.IIFactory("1.2.345.678.999"));

        MCCIMT000300UV01Agent agent = new MCCIMT000300UV01Agent();
        MCCIMT000300UV01Organization org = new MCCIMT000300UV01Organization();
        org.setClassCode(HL7Constants.ORG_CLASS_CODE);
        org.setDeterminerCode(HL7Constants.SENDER_DETERMINER_CODE);
        II id = HL7DataTransformHelper.IIFactory(OID);
        org.getId().add(id);

        javax.xml.namespace.QName xmlqnameorg = new javax.xml.namespace.QName("urn:hl7-org:v3", "representedOrganization");
        JAXBElement<MCCIMT000300UV01Organization> orgElem = new JAXBElement<MCCIMT000300UV01Organization>(xmlqnameorg, MCCIMT000300UV01Organization.class, org);
        agent.setRepresentedOrganization(orgElem);
        agent.getClassCode().add(HL7Constants.AGENT_CLASS_CODE);

        javax.xml.namespace.QName xmlqnameagent = new javax.xml.namespace.QName("urn:hl7-org:v3", "asAgent");
        JAXBElement<MCCIMT000300UV01Agent> agentElem = new JAXBElement<MCCIMT000300UV01Agent>(xmlqnameagent, MCCIMT000300UV01Agent.class, agent);

        senderDevice.setAsAgent(agentElem);

        sender.setDevice(senderDevice);

        return sender;
    }
}
