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
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000100UV01Agent;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Organization;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201305UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01ControlActProcess;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * @author achidambaram
 *
 */
public class HL7PRPA201306TransformsTest {

    @Test
    public void createPRPA201306() {
        String senderOID = "1.1";
        String receiverAAID = "2.2";
        String receiverOID = "2.2";
        String localDeviceId = "1.1";
        PRPAMT201301UV02Patient patient = createPRPAMT201301UV02Patient();
        HL7PRPA201306Transforms transforms = new HL7PRPA201306Transforms();
        PRPAIN201306UV02 result = transforms.createPRPA201306(patient, senderOID, receiverAAID, receiverOID,
                localDeviceId, createPRPAIN201305UV02());
        assertEquals(result.getAcknowledgement().get(0).getTypeId().getExtension(), "1.16.17.19");
        assertEquals(result.getReceiver().get(0).getDevice().getId().get(0).getRoot(), "2.2");
        assertEquals(result.getSender().getDevice().getId().get(0).getRoot(), "1.1");
        assertEquals(result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                .getPatient().getProviderOrganization().getValue().getId().get(0).getRoot(), "1.1");
    }

    @Test
    public void createPRPA201306ForPatientNotFoundRequestNull() {
        PRPAIN201305UV02 query = null;
        HL7PRPA201306Transforms transforms = new HL7PRPA201306Transforms();
        assertNull(transforms.createPRPA201306ForPatientNotFound(query));
    }

    @Test
    public void createPRPA201306ForPatientNotFound() {
        PRPAIN201306UV02 result;
        HL7PRPA201306Transforms transforms = new HL7PRPA201306Transforms();
        result = transforms.createPRPA201306ForPatientNotFound(createPRPAIN201305UV02());
        assertEquals(result.getAcknowledgement().get(0).getTypeId().getExtension(), "1.16.17.19");
        assertEquals(result.getReceiver().get(0).getDevice().getId().get(0).getRoot(), "1.1");
        assertEquals(result.getSender().getDevice().getId().get(0).getRoot(), "2.2");
        assertNull(result.getControlActProcess().getSubject().get(0).getRegistrationEvent());
    }

    @Test
    public void createPRPA201306ForPatientNotFoundReceiverNull() {
        PRPAIN201306UV02 result;
        HL7PRPA201306Transforms transforms = new HL7PRPA201306Transforms();
        result = transforms.createPRPA201306ForPatientNotFound(createPRPAIN201305UV02WhenReceiverNull());
        assertNull(result);
    }

    @Test
    public void createPRPA201306ForPatientNotFoundSenderNull() {
        PRPAIN201306UV02 result;
        HL7PRPA201306Transforms transforms = new HL7PRPA201306Transforms();
        result = transforms.createPRPA201306ForPatientNotFound(createPRPAIN201305UV02WhenSenderNull());
        assertNull(result);
    }

    @Test
    public void createPRPA201306ForPatientNotFoundInteractionIdNull() {
        PRPAIN201306UV02 result;
        HL7PRPA201306Transforms transforms = new HL7PRPA201306Transforms();
        result = transforms.createPRPA201306ForPatientNotFound(createPRPAIN201305UV02WhenIntercationIdNull());
        assertNull(result);
    }

    @Test
    public void createPRPA201306ForErrorsRequestNull() {
        PRPAIN201305UV02 query = null;
        String sErrorCode = "Patient Not available";
        HL7PRPA201306Transforms transforms = new HL7PRPA201306Transforms();
        assertNull(transforms.createPRPA201306ForErrors(query, sErrorCode));
    }

    @Test
    public void createPRPA201306ForErrorsSenderNull() {
        String sErrorCode = "Patient Not available";
        HL7PRPA201306Transforms transforms = new HL7PRPA201306Transforms();
        assertNull(transforms.createPRPA201306ForErrors(createPRPAIN201305UV02WhenSenderNull(), sErrorCode));
    }

    @Test
    public void createPRPA201306ForErrorsReceiverNull() {
        String sErrorCode = "Patient Not available";
        HL7PRPA201306Transforms transforms = new HL7PRPA201306Transforms();
        assertNull(transforms.createPRPA201306ForErrors(createPRPAIN201305UV02WhenReceiverNull(), sErrorCode));
    }

    @Test
    public void createPRPA201306ForErrorsIntercationIdNull() {
        String sErrorCode = "Patient Not available";
        HL7PRPA201306Transforms transforms = new HL7PRPA201306Transforms();
        assertNull(transforms.createPRPA201306ForErrors(createPRPAIN201305UV02WhenIntercationIdNull(), sErrorCode));
    }

    @Test
    public void createPRPA201306ForErrorsStringErrorNull() {
        String sErrorCode = null;
        HL7PRPA201306Transforms transforms = new HL7PRPA201306Transforms();
        assertNull(transforms.createPRPA201306ForErrors(createPRPAIN201305UV02(), sErrorCode));
    }

    @Test
    public void createPRPA201306ForErrorsWhenErrorTextPresent() {
        HL7PRPA201306Transforms transforms = new HL7PRPA201306Transforms();
        PRPAIN201306UV02 result;
        String sErrorCode = "patient not avialable";
        String sErrorText = "Internal error";
        result = transforms.createPRPA201306ForErrors(createPRPAIN201305UV02(), sErrorCode, sErrorText);
        assertEquals(result.getControlActProcess().getReasonOf().get(0).getDetectedIssueEvent().getText().getContent()
                .get(0).toString(), "Internal error");
    }

    @Test
    public void createQUQIMT021001UV01ControlActProcess() {
        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess result;
        PRPAMT201301UV02Patient patient = null;
        String localDeviceId = "1.1";
        String aaId = "1.1";
        String orgId = "2.2";
        PRPAIN201305UV02 query = new PRPAIN201305UV02();
        HL7PRPA201306Transforms transforms = new HL7PRPA201306Transforms();
        result = transforms.createQUQIMT021001UV01ControlActProcess(patient, localDeviceId, query, aaId, orgId);
        assertNull(result.getQueryByParameter());
        assertTrue(result.getSubject().isEmpty());
    }

    @Test
    public void areControlActProcessFieldsNullWhenCAPNull() {
        boolean result;
        PRPAIN201305UV02 oRequest = new PRPAIN201305UV02();
        HL7PRPA201306Transforms transforms = new HL7PRPA201306Transforms();
        result = transforms.areControlActProcessFieldsNull(oRequest);
        assertTrue(result);
    }

    @Test
    public void areControlActProcessFieldsNullWhenQueryByParameterNull() {
        boolean result;
        PRPAIN201305UV02 oRequest = new PRPAIN201305UV02();
        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess controlActProcess = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();
        oRequest.setControlActProcess(controlActProcess);
        HL7PRPA201306Transforms transforms = new HL7PRPA201306Transforms();
        result = transforms.areControlActProcessFieldsNull(oRequest);
        assertTrue(result);
    }

    @Test
    public void areControlActProcessFieldsNullWhenQueryByParameterValueNull() {
        boolean result;
        PRPAIN201305UV02 oRequest = new PRPAIN201305UV02();
        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess controlActProcess = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();
        PRPAMT201306UV02QueryByParameter parameter = new PRPAMT201306UV02QueryByParameter();
        parameter.setQueryId(createII());
        oRequest.setControlActProcess(controlActProcess);
        HL7PRPA201306Transforms transforms = new HL7PRPA201306Transforms();
        result = transforms.areControlActProcessFieldsNull(oRequest);
        assertTrue(result);
    }

    @Test
    public void areControlActProcessFieldsNullWhenQueryIdNull() {
        boolean result;
        PRPAIN201305UV02 oRequest = new PRPAIN201305UV02();
        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess controlActProcess = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();
        PRPAMT201306UV02QueryByParameter parameter = new PRPAMT201306UV02QueryByParameter();
        parameter.setParameterList(createPRPAMT201306UV02ParameterList());
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "parameter");
        JAXBElement<PRPAMT201306UV02QueryByParameter> queryByParameter = new JAXBElement<>(
                xmlqname, PRPAMT201306UV02QueryByParameter.class, parameter);
        controlActProcess.setQueryByParameter(queryByParameter);
        oRequest.setControlActProcess(controlActProcess);
        HL7PRPA201306Transforms transforms = new HL7PRPA201306Transforms();
        result = transforms.areControlActProcessFieldsNull(oRequest);
        assertTrue(result);
    }

    private PRPAIN201305UV02 createPRPAIN201305UV02() {
        PRPAIN201305UV02 query = new PRPAIN201305UV02();
        query.setInteractionId(createII());
        query.setSender(createPRPAIN201305UV02Sender());
        query.getReceiver().add(createMCCIMT000100UV01Receiver());
        query.setControlActProcess(createPRPAIN201305UV02ControlActProcess());
        return query;
    }

    private PRPAIN201305UV02 createPRPAIN201305UV02WhenIntercationIdNull() {
        PRPAIN201305UV02 query = new PRPAIN201305UV02();
        query.setSender(createPRPAIN201305UV02Sender());
        query.getReceiver().add(createMCCIMT000100UV01Receiver());
        query.setControlActProcess(createPRPAIN201305UV02ControlActProcess());
        return query;
    }

    private PRPAIN201305UV02 createPRPAIN201305UV02WhenSenderNull() {
        PRPAIN201305UV02 query = new PRPAIN201305UV02();
        query.setInteractionId(createII());
        query.getReceiver().add(createMCCIMT000100UV01Receiver());
        query.setControlActProcess(createPRPAIN201305UV02ControlActProcess());
        return query;
    }

    private PRPAIN201305UV02 createPRPAIN201305UV02WhenReceiverNull() {
        PRPAIN201305UV02 query = new PRPAIN201305UV02();
        query.setInteractionId(createII());
        query.setSender(createPRPAIN201305UV02Sender());
        query.setControlActProcess(createPRPAIN201305UV02ControlActProcess());
        return query;
    }

    private PRPAIN201305UV02QUQIMT021001UV01ControlActProcess createPRPAIN201305UV02ControlActProcess() {
        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess controlActProcess = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();
        controlActProcess.setTypeId(createII());
        controlActProcess.getId().add(createII());
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
        queryByParameter.getValue().setQueryId(createII());
        return queryByParameter;
    }

    private PRPAMT201306UV02ParameterList createPRPAMT201306UV02ParameterList() {
        PRPAMT201306UV02ParameterList parameterList = new PRPAMT201306UV02ParameterList();
        parameterList.setId(createII());
        return parameterList;
    }

    private MCCIMT000100UV01Sender createPRPAIN201305UV02Sender() {
        String SenderOID = "1.1";
        MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();
        MCCIMT000100UV01Device device = new MCCIMT000100UV01Device();
        II e = new II();
        e.setRoot(SenderOID);
        device.getId().add(createII());

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
        return sender;
    }

    private MCCIMT000100UV01Receiver createMCCIMT000100UV01Receiver() {
        MCCIMT000100UV01Receiver receiver = new MCCIMT000100UV01Receiver();
        MCCIMT000100UV01Device device = new MCCIMT000100UV01Device();
        device.setDeterminerCode("INSTANCE");
        device.setTypeId(createReceiverID());
        device.getId().add(createReceiverID());
        device.setAsAgent(createMCCIMT000100UV01Agent());
        receiver.setDevice(device);
        return receiver;
    }

    private JAXBElement<MCCIMT000100UV01Agent> createMCCIMT000100UV01Agent() {
        MCCIMT000100UV01Agent agent = new MCCIMT000100UV01Agent();
        MCCIMT000100UV01Organization org = new MCCIMT000100UV01Organization();
        org.getId().add(createReceiverID());
        org.hl7.v3.ObjectFactory JaxbObjectFactory = new org.hl7.v3.ObjectFactory();
        JAXBElement<MCCIMT000100UV01Organization> repOrgReceiver = JaxbObjectFactory
                .createMCCIMT000100UV01AgentRepresentedOrganization(org);
        repOrgReceiver.setValue(org);
        agent.setRepresentedOrganization(repOrgReceiver);
        JAXBElement<MCCIMT000100UV01Agent> agentReceiver = JaxbObjectFactory.createMCCIMT000100UV01DeviceAsAgent(agent);
        agentReceiver.setValue(agent);
        return agentReceiver;
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
        patientPerson.setAdministrativeGenderCode(createCE());

        PNExplicit patientName = new PNExplicit();
        patientName.getContent().add(getFirstName());
        patientName.getContent().add(getLastName());
        patientName.getNullFlavor().add("NA");
        patientPerson.getName().add(patientName);
        return patient;
    }

    private static String getFirstName() {
        String firstName = "Gallow";
        return firstName;
    }

    private static String getLastName() {
        String lastName = "Younger";
        return lastName;
    }

    private CE createCE() {
        CE ce = new CE();
        ce.setCode("CONNECT");
        return ce;
    }

    private II createII() {
        II ii = new II();
        ii.setAssigningAuthorityName("CONNECT");
        ii.setExtension("1.16.17.19");
        ii.setRoot("1.1");
        return ii;
    }

    private II createReceiverID() {
        II ii = new II();
        ii.setAssigningAuthorityName("CONNECT");
        ii.setExtension("2.16.17.19");
        ii.setRoot("2.2");
        return ii;
    }

}
