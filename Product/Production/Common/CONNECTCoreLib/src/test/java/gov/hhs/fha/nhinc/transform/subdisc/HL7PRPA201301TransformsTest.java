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
import org.hl7.v3.CE;
import org.hl7.v3.COCTMT090100UV01AssignedPerson;
import org.hl7.v3.COCTMT090100UV01Person;
import org.hl7.v3.COCTMT090300UV01AssignedDevice;
import org.hl7.v3.COCTMT090300UV01Device;
import org.hl7.v3.CS;
import org.hl7.v3.EDExplicit;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000100UV01Agent;
import org.hl7.v3.MCCIMT000100UV01AttentionLine;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Organization;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000300UV01Agent;
import org.hl7.v3.MCCIMT000300UV01AttentionLine;
import org.hl7.v3.MCCIMT000300UV01Device;
import org.hl7.v3.MCCIMT000300UV01Organization;
import org.hl7.v3.MCCIMT000300UV01Receiver;
import org.hl7.v3.MCCIMT000300UV01Sender;
import org.hl7.v3.MFMIMT700711UV01AuthorOrPerformer;
import org.hl7.v3.MFMIMT700711UV01DataEnterer;
import org.hl7.v3.MFMIMT700711UV01InformationRecipient;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01ControlActProcess;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201305UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01ControlActProcess;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;
import org.hl7.v3.QUQIMT021001UV01AuthorOrPerformer;
import org.hl7.v3.QUQIMT021001UV01DataEnterer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * @author achidambaram
 *
 */
public class HL7PRPA201301TransformsTest {

    @Test
    public void createPRPA201301Patient() {
        String localDeviceId = "1.1";
        String senderOID = "1.1";
        String receiverOID = "2.2";
        PRPAMT201301UV02Patient patient = createPRPAMT201301UV02Patient();
        HL7PRPA201301Transforms transform = new HL7PRPA201301Transforms();
        PRPAIN201301UV02 request = transform.createPRPA201301(patient, localDeviceId, senderOID, receiverOID);
        assertEquals(request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                .getPatient().getId().get(0).getExtension(), "1.16.17.19");
    }

    @Test
    public void createPRPA201301Request() {
        String localDeviceId = "1.1";
        HL7PRPA201301Transforms transforms = new HL7PRPA201301Transforms();
        PRPAIN201301UV02 request;
        request = transforms.createPRPA201301(createPRPAIN201305UV02(), localDeviceId);
        assertEquals(request.getAttentionLine().get(0).getRealmCode().get(0).getCode(), "CONNECT");
        assertEquals(request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue()
                .getId().get(0).getExtension(), "1.16.17.19");

    }

    @Test
    public void createPRPA201301RequestNull() {
        String localDeviceId = "1.1";
        HL7PRPA201301Transforms transforms = new HL7PRPA201301Transforms();
        PRPAIN201305UV02 req = null;
        assertNull(transforms.createPRPA201301(req, localDeviceId));
    }

    @Test
    public void createPRPA201301RequestWhenContolActIdNull() {
        String localDeviceId = "1.1";
        HL7PRPA201301Transforms transforms = new HL7PRPA201301Transforms();
        PRPAIN201305UV02 request = new PRPAIN201305UV02();
        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess controlActProcess = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();
        controlActProcess.getAuthorOrPerformer().add(createPRPAIN201305UV02AuthorOrPerformer());
        controlActProcess.setTypeId(createII());
        controlActProcess.setQueryByParameter(createQueryByParameter());
        request.setControlActProcess(controlActProcess);
        request.getControlActProcess();
        PRPAIN201301UV02 result;
        result = transforms.createPRPA201301(request, localDeviceId);
        assertTrue(result.getControlActProcess().getId().isEmpty());
    }

    @Test
    public void createPRPA201301RequestWhenContolActRealmCodeNull() {
        String localDeviceId = "1.1";
        HL7PRPA201301Transforms transforms = new HL7PRPA201301Transforms();
        PRPAIN201305UV02 request = new PRPAIN201305UV02();
        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess controlActProcess = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();
        controlActProcess.getAuthorOrPerformer().add(createPRPAIN201305UV02AuthorOrPerformer());
        controlActProcess.setTypeId(createII());
        controlActProcess.setQueryByParameter(createQueryByParameter());
        request.setControlActProcess(controlActProcess);
        PRPAIN201301UV02 result;
        result = transforms.createPRPA201301(request, localDeviceId);
        assertTrue(result.getControlActProcess().getRealmCode().isEmpty());
    }

    @Test
    public void createPRPA201301RequestWhenContolActDataEntererNull() {
        String localDeviceId = "1.1";
        HL7PRPA201301Transforms transforms = new HL7PRPA201301Transforms();
        PRPAIN201305UV02 request = new PRPAIN201305UV02();
        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess controlActProcess = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();
        controlActProcess.getAuthorOrPerformer().add(createPRPAIN201305UV02AuthorOrPerformer());
        controlActProcess.setTypeId(createII());
        controlActProcess.setQueryByParameter(createQueryByParameter());
        request.setControlActProcess(controlActProcess);
        PRPAIN201301UV02 result;
        result = transforms.createPRPA201301(request, localDeviceId);
        assertTrue(result.getControlActProcess().getDataEnterer().isEmpty());
    }

    @Test
    public void createPRPA201301RequestWhenContolActProcessNull() {
        String localDeviceId = "1.1";
        HL7PRPA201301Transforms transforms = new HL7PRPA201301Transforms();
        PRPAIN201305UV02 request = new PRPAIN201305UV02();
        PRPAIN201301UV02 result;
        result = transforms.createPRPA201301(request, localDeviceId);
        assertNull(result.getControlActProcess());
    }

    @Test
    public void createPRPA201301RequestWhenAuthorOrPerformerNull() {
        String localDeviceId = "1.1";
        HL7PRPA201301Transforms transforms = new HL7PRPA201301Transforms();
        PRPAIN201305UV02 request = new PRPAIN201305UV02();
        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess controlActProcess = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();
        QUQIMT021001UV01AuthorOrPerformer authorOrPerformer = new QUQIMT021001UV01AuthorOrPerformer();
        authorOrPerformer.setAssignedDevice(createPRPAIN201305UV02AssignedDevice());
        authorOrPerformer.setAssignedPerson(createPRPAIN201305UV02AssignedPerson());
        controlActProcess.getAuthorOrPerformer().add(authorOrPerformer);
        controlActProcess.setTypeId(createII());
        controlActProcess.setQueryByParameter(createQueryByParameter());
        request.setControlActProcess(controlActProcess);
        PRPAIN201301UV02 result;
        result = transforms.createPRPA201301(request, localDeviceId);
        assertTrue(result.getControlActProcess().getAuthorOrPerformer().get(0).getRealmCode().isEmpty());
        assertTrue(result.getControlActProcess().getAuthorOrPerformer().get(0).getTemplateId().isEmpty());
    }

    @Test
    public void createPRPA201301forPRPAIN201306UV02Request() {
        String localDeviceId = "1.1";
        HL7PRPA201301Transforms transforms = new HL7PRPA201301Transforms();
        PRPAIN201301UV02 request;
        request = transforms.createPRPA201301(createPRPAIN201306UV02(), localDeviceId);
        assertEquals(request.getAttentionLine().get(0).getRealmCode().get(0).getCode(), "CONNECT");
        assertEquals(request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue()
                .getId().get(0).getExtension(), "1.16.17.19");
    }

    @Test
    public void createPRPA201301RequestNullforPRPAIN201306UV02() {
        String localDeviceId = "1.1";
        HL7PRPA201301Transforms transforms = new HL7PRPA201301Transforms();
        PRPAIN201306UV02 req = null;
        assertNull(transforms.createPRPA201301(req, localDeviceId));
    }

    @Test
    public void copyControlActProcess2() {
        String localDeviceId = "1.1";
        HL7PRPA201301Transforms transforms = new HL7PRPA201301Transforms();
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess cap = transforms.copyControlActProcess2(
                createPRPAIN201306UV02ControlActProcess(), localDeviceId);
        assertEquals(cap.getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId().get(0).getExtension(),
                "1.16.17.19");
    }

    @Test
    public void createPRPA201301RequestForPRPAIN201306WhenContolActIdNull() {
        String localDeviceId = "1.1";
        HL7PRPA201301Transforms transforms = new HL7PRPA201301Transforms();
        PRPAIN201306UV02 request = new PRPAIN201306UV02();
        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
        controlActProcess.getAuthorOrPerformer().add(createPRPAIN201306UV02AuthorOrPerformer());
        controlActProcess.setTypeId(createII());
        controlActProcess.setQueryByParameter(createQueryByParameter());
        request.setControlActProcess(controlActProcess);
        request.getControlActProcess();
        PRPAIN201301UV02 result;
        result = transforms.createPRPA201301(request, localDeviceId);
        assertTrue(result.getControlActProcess().getId().isEmpty());
    }

    @Test
    public void createPRPA201301RequestForPRPAIN201306WhenContolActRealmCodeNull() {
        String localDeviceId = "1.1";
        HL7PRPA201301Transforms transforms = new HL7PRPA201301Transforms();
        PRPAIN201306UV02 request = new PRPAIN201306UV02();
        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
        controlActProcess.getAuthorOrPerformer().add(createPRPAIN201306UV02AuthorOrPerformer());
        controlActProcess.setTypeId(createII());
        controlActProcess.setQueryByParameter(createQueryByParameter());
        request.setControlActProcess(controlActProcess);
        PRPAIN201301UV02 result;
        result = transforms.createPRPA201301(request, localDeviceId);
        assertTrue(result.getControlActProcess().getRealmCode().isEmpty());
    }

    @Test
    public void createPRPA201301RequestForPRPAIN201306WhenContolActDataEntererNull() {
        String localDeviceId = "1.1";
        HL7PRPA201301Transforms transforms = new HL7PRPA201301Transforms();
        PRPAIN201306UV02 request = new PRPAIN201306UV02();
        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
        controlActProcess.setTypeId(createII());
        controlActProcess.setQueryByParameter(createQueryByParameter());
        request.setControlActProcess(controlActProcess);
        PRPAIN201301UV02 result;
        result = transforms.createPRPA201301(request, localDeviceId);
        assertTrue(result.getControlActProcess().getDataEnterer().isEmpty());
    }

    @Test
    public void createPRPA201301RequestForPRPAIN201306WhenContolActProcessNull() {
        String localDeviceId = "1.1";
        HL7PRPA201301Transforms transforms = new HL7PRPA201301Transforms();
        PRPAIN201306UV02 request = new PRPAIN201306UV02();
        PRPAIN201301UV02 result;
        result = transforms.createPRPA201301(request, localDeviceId);
        assertNull(result.getControlActProcess());
    }

    @Test
    public void createPRPA201301RequestForPRPAIN201306WhenAuthorOrPerformerNull() {
        String localDeviceId = "1.1";
        HL7PRPA201301Transforms transforms = new HL7PRPA201301Transforms();
        PRPAIN201306UV02 request = new PRPAIN201306UV02();
        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
        MFMIMT700711UV01AuthorOrPerformer authorOrPerformer = new MFMIMT700711UV01AuthorOrPerformer();
        authorOrPerformer.setAssignedDevice(createPRPAIN201305UV02AssignedDevice());
        authorOrPerformer.setAssignedPerson(createPRPAIN201305UV02AssignedPerson());
        controlActProcess.getAuthorOrPerformer().add(authorOrPerformer);
        controlActProcess.setTypeId(createII());
        controlActProcess.setQueryByParameter(createQueryByParameter());
        request.setControlActProcess(controlActProcess);
        PRPAIN201301UV02 result;
        result = transforms.createPRPA201301(request, localDeviceId);
        assertTrue(result.getControlActProcess().getAuthorOrPerformer().get(0).getRealmCode().isEmpty());
        assertTrue(result.getControlActProcess().getAuthorOrPerformer().get(0).getTemplateId().isEmpty());
    }

    private PRPAIN201306UV02 createPRPAIN201306UV02() {
        PRPAIN201306UV02 request = new PRPAIN201306UV02();
        request.setControlActProcess(createPRPAIN201306UV02ControlActProcess());
        request.getRealmCode().add(createCS());
        request.getAttentionLine().add(createPRPAIN201306UV02AttentionLine());
        request.getReceiver().add(createMCCIMT000300UV01Receiver());
        request.setSender(createPRPAIN201306UV02Sender());
        return request;
    }

    private MCCIMT000300UV01Sender createPRPAIN201306UV02Sender() {
        String SenderOID = "1.1";
        MCCIMT000300UV01Sender sender = new MCCIMT000300UV01Sender();
        MCCIMT000300UV01Device device = new MCCIMT000300UV01Device();
        II e = new II();
        e.setRoot(SenderOID);
        device.getId().add(createII());

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
        return sender;
    }

    private MCCIMT000300UV01Receiver createMCCIMT000300UV01Receiver() {
        MCCIMT000300UV01Receiver receiver = new MCCIMT000300UV01Receiver();
        MCCIMT000300UV01Device device = new MCCIMT000300UV01Device();
        device.setDeterminerCode("INSTANCE");
        device.setDesc(createEDExplicit());
        device.setTypeId(createII());
        device.getId().add(createII());
        device.setAsAgent(createMCCIMT000300UV01Agent());
        receiver.setDevice(device);
        return receiver;
    }

    private JAXBElement<MCCIMT000300UV01Agent> createMCCIMT000300UV01Agent() {
        MCCIMT000300UV01Agent agent = new MCCIMT000300UV01Agent();
        MCCIMT000300UV01Organization org = new MCCIMT000300UV01Organization();
        org.getId().add(createII());
        org.hl7.v3.ObjectFactory JaxbObjectFactory = new org.hl7.v3.ObjectFactory();
        JAXBElement<MCCIMT000300UV01Organization> repOrgReceiver = JaxbObjectFactory
                .createMCCIMT000300UV01AgentRepresentedOrganization(org);
        repOrgReceiver.setValue(org);
        agent.setRepresentedOrganization(repOrgReceiver);
        JAXBElement<MCCIMT000300UV01Agent> agentReceiver = JaxbObjectFactory.createMCCIMT000300UV01DeviceAsAgent(agent);
        agentReceiver.setValue(agent);
        return agentReceiver;
    }

    private PRPAIN201306UV02MFMIMT700711UV01ControlActProcess createPRPAIN201306UV02ControlActProcess() {
        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
        controlActProcess.getAuthorOrPerformer().add(createPRPAIN201306UV02AuthorOrPerformer());
        controlActProcess.setTypeId(createII());
        controlActProcess.getId().add(createII());
        controlActProcess.getDataEnterer().add(createPRPAIN201306UV02DataEnterer());
        controlActProcess.getRealmCode().add(createCS());
        controlActProcess.getNullFlavor().add("NA");
        controlActProcess.setQueryByParameter(createQueryByParameter());
        controlActProcess.getInformationRecipient().add(createInformationReceipent());
        return controlActProcess;
    }

    private MFMIMT700711UV01InformationRecipient createInformationReceipent() {
        MFMIMT700711UV01InformationRecipient recipient = new MFMIMT700711UV01InformationRecipient();
        recipient.setAssignedPerson(createAssignedPerson());
        return recipient;
    }

    private MFMIMT700711UV01AuthorOrPerformer createPRPAIN201306UV02AuthorOrPerformer() {
        MFMIMT700711UV01AuthorOrPerformer authorOrPerformer = new MFMIMT700711UV01AuthorOrPerformer();
        authorOrPerformer.setAssignedDevice(createPRPAIN201305UV02AssignedDevice());
        authorOrPerformer.setAssignedPerson(createPRPAIN201305UV02AssignedPerson());
        authorOrPerformer.setTypeId(createII());
        authorOrPerformer.setContextControlCode("contextContolCode");
        authorOrPerformer.setModeCode(createCE());
        authorOrPerformer.setNoteText(createEDExplicit());
        authorOrPerformer.getRealmCode().add(createCS());
        authorOrPerformer.getTemplateId().add(createII());
        return authorOrPerformer;
    }

    private MFMIMT700711UV01DataEnterer createPRPAIN201306UV02DataEnterer() {
        MFMIMT700711UV01DataEnterer dataEnterer = new MFMIMT700711UV01DataEnterer();
        dataEnterer.setAssignedPerson(createAssignedPerson());
        dataEnterer.setTypeId(createII());
        return dataEnterer;
    }

    private MCCIMT000300UV01AttentionLine createPRPAIN201306UV02AttentionLine() {
        MCCIMT000300UV01AttentionLine attentionLine = new MCCIMT000300UV01AttentionLine();
        attentionLine.getRealmCode().add(createCS());
        attentionLine.setTypeId(createII());
        return attentionLine;
    }

    private PRPAIN201305UV02 createPRPAIN201305UV02() {
        PRPAIN201305UV02 request = new PRPAIN201305UV02();
        request.setControlActProcess(createPRPAIN201305UV02ControlActProcess());
        request.getRealmCode().add(createCS());
        request.getAttentionLine().add(createAttentionLine());
        request.getReceiver().add(createMCCIMT000100UV01Receiver());
        return request;
    }

    private MCCIMT000100UV01Receiver createMCCIMT000100UV01Receiver() {
        MCCIMT000100UV01Receiver receiver = new MCCIMT000100UV01Receiver();
        MCCIMT000100UV01Device device = new MCCIMT000100UV01Device();
        device.setDeterminerCode("INSTANCE");
        device.setDesc(createEDExplicit());
        device.setTypeId(createII());
        device.getId().add(createII());
        device.setAsAgent(createMCCIMT000100UV01Agent());
        receiver.setDevice(device);
        return receiver;
    }

    private JAXBElement<MCCIMT000100UV01Agent> createMCCIMT000100UV01Agent() {
        MCCIMT000100UV01Agent agent = new MCCIMT000100UV01Agent();
        MCCIMT000100UV01Organization org = new MCCIMT000100UV01Organization();
        org.getId().add(createII());
        org.hl7.v3.ObjectFactory JaxbObjectFactory = new org.hl7.v3.ObjectFactory();
        JAXBElement<MCCIMT000100UV01Organization> repOrgReceiver = JaxbObjectFactory
                .createMCCIMT000100UV01AgentRepresentedOrganization(org);
        repOrgReceiver.setValue(org);
        agent.setRepresentedOrganization(repOrgReceiver);
        JAXBElement<MCCIMT000100UV01Agent> agentReceiver = JaxbObjectFactory.createMCCIMT000100UV01DeviceAsAgent(agent);
        agentReceiver.setValue(agent);
        return agentReceiver;
    }

    private MCCIMT000100UV01AttentionLine createAttentionLine() {
        MCCIMT000100UV01AttentionLine attentionLine = new MCCIMT000100UV01AttentionLine();
        attentionLine.getRealmCode().add(createCS());
        attentionLine.setTypeId(createII());
        return attentionLine;
    }

    private CS createCS() {
        CS cs = new CS();
        cs.setCode("CONNECT");
        return cs;
    }

    private PRPAIN201305UV02QUQIMT021001UV01ControlActProcess createPRPAIN201305UV02ControlActProcess() {
        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess controlActProcess = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();
        controlActProcess.getAuthorOrPerformer().add(createPRPAIN201305UV02AuthorOrPerformer());
        controlActProcess.setTypeId(createII());
        controlActProcess.getId().add(createII());
        controlActProcess.getDataEnterer().add(createDataEnterer());
        controlActProcess.getRealmCode().add(createCS());
        controlActProcess.getNullFlavor().add("NA");
        controlActProcess.setQueryByParameter(createQueryByParameter());
        return controlActProcess;
    }

    private JAXBElement<PRPAMT201306UV02QueryByParameter> createQueryByParameter() {
        PRPAMT201306UV02QueryByParameter parameter = new PRPAMT201306UV02QueryByParameter();
        parameter.setQueryId(createII());
        parameter.setParameterList(createPRPAMT201306UV02ParameterList());
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "parameter");
        JAXBElement<PRPAMT201306UV02QueryByParameter> queryByParameter = new JAXBElement<>(
                xmlqname, PRPAMT201306UV02QueryByParameter.class, parameter);
        return queryByParameter;
    }

    private PRPAMT201306UV02ParameterList createPRPAMT201306UV02ParameterList() {
        PRPAMT201306UV02ParameterList parameterList = new PRPAMT201306UV02ParameterList();
        parameterList.setId(createII());
        return parameterList;
    }

    private QUQIMT021001UV01DataEnterer createDataEnterer() {
        QUQIMT021001UV01DataEnterer dataEnterer = new QUQIMT021001UV01DataEnterer();
        dataEnterer.setAssignedPerson(createAssignedPerson());
        dataEnterer.setTypeId(createII());
        return dataEnterer;
    }

    private QUQIMT021001UV01AuthorOrPerformer createPRPAIN201305UV02AuthorOrPerformer() {
        QUQIMT021001UV01AuthorOrPerformer authorOrPerformer = new QUQIMT021001UV01AuthorOrPerformer();
        authorOrPerformer.setAssignedDevice(createPRPAIN201305UV02AssignedDevice());
        authorOrPerformer.setAssignedPerson(createPRPAIN201305UV02AssignedPerson());
        authorOrPerformer.setTypeId(createII());
        authorOrPerformer.setContextControlCode("contextContolCode");
        authorOrPerformer.setModeCode(createCE());
        authorOrPerformer.setNoteText(createEDExplicit());
        authorOrPerformer.getRealmCode().add(createCS());
        authorOrPerformer.getTemplateId().add(createII());
        return authorOrPerformer;
    }

    private EDExplicit createEDExplicit() {
        EDExplicit ed = new EDExplicit();
        ed.setMediaType("media");
        return ed;
    }

    private CE createCE() {
        CE ce = new CE();
        ce.setCode("CONNECT");
        ce.setCodeSystem("CONNECT Domain");
        return ce;
    }

    private JAXBElement<COCTMT090300UV01AssignedDevice> createPRPAIN201305UV02AssignedDevice() {
        COCTMT090300UV01AssignedDevice assignedDevice = new COCTMT090300UV01AssignedDevice();
        assignedDevice.setAssignedDevice(createPRPAIN201305UV02Device());
        assignedDevice.getId().add(createII());
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "assignedDevice");
        JAXBElement<COCTMT090300UV01AssignedDevice> device = new JAXBElement<>(xmlqname,
                COCTMT090300UV01AssignedDevice.class, assignedDevice);
        return device;
    }

    private JAXBElement<COCTMT090300UV01Device> createPRPAIN201305UV02Device() {
        COCTMT090300UV01Device device = new COCTMT090300UV01Device();
        device.setTypeId(createII());
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "device");
        JAXBElement<COCTMT090300UV01Device> dev = new JAXBElement<>(xmlqname,
                COCTMT090300UV01Device.class, device);
        return dev;
    }

    private JAXBElement<COCTMT090100UV01AssignedPerson> createPRPAIN201305UV02AssignedPerson() {
        COCTMT090100UV01AssignedPerson assignedPerson = new COCTMT090100UV01AssignedPerson();
        assignedPerson.setAssignedPerson(createPRPAIN201305UV02Person());
        assignedPerson.setTypeId(createII());
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "assignedPerson");
        JAXBElement<COCTMT090100UV01AssignedPerson> person = new JAXBElement<>(xmlqname,
                COCTMT090100UV01AssignedPerson.class, assignedPerson);
        return person;
    }

    private COCTMT090100UV01AssignedPerson createAssignedPerson() {
        COCTMT090100UV01AssignedPerson assignedPerson = new COCTMT090100UV01AssignedPerson();
        assignedPerson.setAssignedPerson(createPRPAIN201305UV02Person());
        assignedPerson.setTypeId(createII());
        return assignedPerson;
    }

    private JAXBElement<COCTMT090100UV01Person> createPRPAIN201305UV02Person() {
        COCTMT090100UV01Person person = new COCTMT090100UV01Person();
        person.setDeterminerCode("INSTANCE");
        person.setTypeId(createII());
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "person");
        JAXBElement<COCTMT090100UV01Person> assignedPerson = new JAXBElement<>(xmlqname,
                COCTMT090100UV01Person.class, person);
        return assignedPerson;
    }

    private PRPAMT201301UV02Patient createPRPAMT201301UV02Patient() {
        org.hl7.v3.PRPAMT201301UV02Patient patient = new PRPAMT201301UV02Patient();
        PRPAMT201301UV02Person patientPerson = new PRPAMT201301UV02Person();
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        JAXBElement<PRPAMT201301UV02Person> patientPersonElement = new JAXBElement<>(xmlqname,
                PRPAMT201301UV02Person.class, patientPerson);
        patient.setPatientPerson(patientPersonElement);
        patient.getId().add(createII());
        patientPerson.getClassCode().add("ClassCode");

        patientPerson.setDeterminerCode("INSTANCE");

        PNExplicit patientName = new PNExplicit();
        patientName.getNullFlavor().add("NA");
        return patient;
    }

    private II createII() {
        II ii = new II();
        ii.setAssigningAuthorityName("CONNECT");
        ii.setExtension("1.16.17.19");
        ii.setRoot("1.1");
        return ii;
    }

}
