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
package gov.hhs.fha.nhinc.patientdiscovery.messaging.builder.impl;

/**
 *
 * @author tjafri
 */
import gov.hhs.fha.nhinc.patientdiscovery.messaging.builder.PRPAIN201305UV02Builder;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.util.format.UTCDateUtil;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.ActClassControlAct;
import org.hl7.v3.COCTMT090300UV01AssignedDevice;
import org.hl7.v3.CommunicationFunctionType;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000100UV01Agent;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Organization;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201305UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.QUQIMT021001UV01AuthorOrPerformer;
import org.hl7.v3.TSExplicit;
import org.hl7.v3.XActMoodIntentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractPRPAIN201305UV02Builder implements PRPAIN201305UV02Builder {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractPRPAIN201305UV02Builder.class);

    private PRPAIN201305UV02 request = null;
    private static final String PROPERTY_FILE = "gateway";
    private static final String LOCAL_HCID_KEY = "localHomeCommunityId";

    @Override
    public void build() {
        request = new PRPAIN201305UV02();
        buildSenderReceiver();
        buildCreationTime();
        try {
            buildControlActProcess();
        } catch (PropertyAccessException ex) {
            LOG.error("Error getting the Local HCID: {}", ex.getLocalizedMessage(), ex);
        }
    }

    @Override
    public PRPAIN201305UV02 getMessage() {
        return request;
    }

    protected void buildSenderReceiver() {
        MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();
        MCCIMT000100UV01Receiver receiver = new MCCIMT000100UV01Receiver();
        sender.setTypeCode(CommunicationFunctionType.SND);
        receiver.setTypeCode(CommunicationFunctionType.RCV);
        String localHCID = null;
        try {
            localHCID = getLocalHcid();
        } catch (PropertyAccessException ex) {
            LOG.error("Error getting the Local HCID: {}", ex.getLocalizedMessage(), ex);
        }
        sender.setDevice(getDevice(localHCID));
        receiver.setDevice(getDevice(getRemoteHcid()));
        request.getReceiver().add(receiver);
        request.setSender(sender);
    }

    protected void buildCreationTime() {
        TSExplicit creationTime = new TSExplicit();
        DateFormat format = new SimpleDateFormat(UTCDateUtil.DATE_FORMAT_UTC);
        creationTime.setValue(format.format(new Date()));
        request.setCreationTime(creationTime);
    }

    protected void buildControlActProcess() throws PropertyAccessException {
        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess cap
            = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();
        cap.setMoodCode(XActMoodIntentEvent.EVN);
        cap.setClassCode(ActClassControlAct.CACT);
        QUQIMT021001UV01AuthorOrPerformer author = new QUQIMT021001UV01AuthorOrPerformer();
        COCTMT090300UV01AssignedDevice device = new COCTMT090300UV01AssignedDevice();
        II id = new II();
        id.setRoot(getLocalHcid());
        device.getId().add(id);
        JAXBElement<COCTMT090300UV01AssignedDevice> deviceJAX
            = new org.hl7.v3.ObjectFactory().createMFMIMT700701UV01AuthorOrPerformerAssignedDevice(device);
        author.setAssignedDevice(deviceJAX);
        cap.getAuthorOrPerformer().add(author);
        request.setControlActProcess(cap);
    }

    protected String getLocalHcid() throws PropertyAccessException {
        return PropertyAccessor.getInstance().getProperty(PROPERTY_FILE, LOCAL_HCID_KEY);
    }

    private MCCIMT000100UV01Device getDevice(String hcid) {
        MCCIMT000100UV01Device device = new MCCIMT000100UV01Device();
        MCCIMT000100UV01Agent asAgent = new MCCIMT000100UV01Agent();
        MCCIMT000100UV01Organization representedOrg = new MCCIMT000100UV01Organization();
        II id = new II();
        id.setRoot(hcid);
        representedOrg.getId().add(id);

        JAXBElement<MCCIMT000100UV01Organization> representedOrgJax
            = new org.hl7.v3.ObjectFactory().createMCCIMT000100UV01AgentRepresentedOrganization(representedOrg);
        asAgent.setRepresentedOrganization(representedOrgJax);
        JAXBElement<MCCIMT000100UV01Agent> asAgentJax
            = new org.hl7.v3.ObjectFactory().createMCCIMT000100UV01DeviceAsAgent(asAgent);
        device.setAsAgent(asAgentJax);
        return device;
    }

    protected abstract String getRemoteHcid();
}
