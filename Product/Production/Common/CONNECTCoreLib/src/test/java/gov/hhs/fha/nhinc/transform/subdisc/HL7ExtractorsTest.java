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

import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.CD;
import org.hl7.v3.CE;
import org.hl7.v3.COCTMT090003UV01AssignedEntity;
import org.hl7.v3.COCTMT090003UV01Device;
import org.hl7.v3.CS;
import org.hl7.v3.ENExplicit;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000100UV01Agent;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Organization;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.MFMIMT700701UV01Custodian;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01ControlActProcess;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01Subject1;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01Subject2;
import org.hl7.v3.PRPAIN201302UV02;
import org.hl7.v3.PRPAIN201302UV02MFMIMT700701UV01ControlActProcess;
import org.hl7.v3.PRPAIN201302UV02MFMIMT700701UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201302UV02MFMIMT700701UV01Subject1;
import org.hl7.v3.PRPAIN201302UV02MFMIMT700701UV01Subject2;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201310UV02;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01ControlActProcess;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01Subject2;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.PRPAMT201302UV02Patient;
import org.hl7.v3.PRPAMT201302UV02PatientPatientPerson;
import org.hl7.v3.PRPAMT201302UV02Person;
import org.hl7.v3.PRPAMT201304UV02Patient;
import org.hl7.v3.PRPAMT201304UV02Person;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * @author achidambaram
 *
 */
public class HL7ExtractorsTest {

    @Test
    public void ExtractSubjectFromMessage() {
        HL7Extractors extractor = new HL7Extractors();
        PRPAIN201301UV02MFMIMT700701UV01Subject1 subject1 = extractor
                .ExtractSubjectFromMessage(createPRPAIN201301UV02());
        assertEquals(subject1.getTypeId().getExtension(), "D123401");
    }

    @Test
    public void ExtractSubjectFromPRPAIN201302UV02Message() {
        HL7Extractors extractor = new HL7Extractors();
        PRPAIN201302UV02MFMIMT700701UV01Subject1 subject1 = extractor
                .ExtractSubjectFromMessage(createPRPAIN201302UV02());
        assertEquals(subject1.getTypeId().getExtension(), "D123401");
    }

    @Test
    public void ExtractSubjectFromPRPAIN201310UV02Message() {
        HL7Extractors extractor = new HL7Extractors();
        PRPAIN201310UV02MFMIMT700711UV01Subject1 subject1 = extractor
                .ExtractSubjectFromMessage(createPRPAIN201310UV02());
        assertEquals(subject1.getTypeId().getExtension(), "D123401");
    }

    @Test
    public void ExtractSubjectFromMessageWhenMessageNull() {
        HL7Extractors extractor = new HL7Extractors();
        PRPAIN201301UV02 message = null;
        PRPAIN201301UV02MFMIMT700701UV01Subject1 subject1 = extractor.ExtractSubjectFromMessage(message);
        assertNull(subject1);
    }

    @Test
    public void ExtractSubjectFromMessageWhenPRPAIN201302UV02MessageNull() {
        HL7Extractors extractor = new HL7Extractors();
        PRPAIN201302UV02 message = null;
        PRPAIN201302UV02MFMIMT700701UV01Subject1 subject1 = extractor.ExtractSubjectFromMessage(message);
        assertNull(subject1);
    }

    @Test
    public void ExtractSubjectFromMessageWhenPRPAIN201310UV02MessageNull() {
        HL7Extractors extractor = new HL7Extractors();
        PRPAIN201310UV02 message = null;
        PRPAIN201310UV02MFMIMT700711UV01Subject1 subject1 = extractor.ExtractSubjectFromMessage(message);
        assertNull(subject1);
    }

    @Test
    public void ExtractSubjectFromMessageWhenControlActProcessNull() {
        HL7Extractors extractor = new HL7Extractors();
        PRPAIN201301UV02 message = new PRPAIN201301UV02();
        PRPAIN201301UV02MFMIMT700701UV01Subject1 subject1 = extractor.ExtractSubjectFromMessage(message);
        assertNull(subject1);
    }

    @Test
    public void ExtractSubjectFromMessageWhenPRPAIN201302UV02ControlActProcessNull() {
        HL7Extractors extractor = new HL7Extractors();
        PRPAIN201302UV02 message = new PRPAIN201302UV02();
        PRPAIN201302UV02MFMIMT700701UV01Subject1 subject1 = extractor.ExtractSubjectFromMessage(message);
        assertNull(subject1);
    }

    @Test
    public void ExtractSubjectFromMessageWhenPRPAIN201310UV02ControlActProcessNull() {
        HL7Extractors extractor = new HL7Extractors();
        PRPAIN201310UV02 message = new PRPAIN201310UV02();
        PRPAIN201310UV02MFMIMT700711UV01Subject1 subject1 = extractor.ExtractSubjectFromMessage(message);
        assertNull(subject1);
    }

    @Test
    public void ExtractSubjectFromMessageWhenSubjectNull() {
        HL7Extractors extractor = new HL7Extractors();
        PRPAIN201301UV02 message = new PRPAIN201301UV02();
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess controlActProcess = new PRPAIN201301UV02MFMIMT700701UV01ControlActProcess();
        controlActProcess.setTypeId(createTypeId());
        message.setControlActProcess(controlActProcess);
        PRPAIN201301UV02MFMIMT700701UV01Subject1 subject1 = extractor.ExtractSubjectFromMessage(message);
        assertNull(subject1);
    }

    @Test
    public void ExtractSubjectFromMessageWhenPRPAIN201302SubjectNull() {
        HL7Extractors extractor = new HL7Extractors();
        PRPAIN201302UV02 message = new PRPAIN201302UV02();
        PRPAIN201302UV02MFMIMT700701UV01ControlActProcess controlActProcess = new PRPAIN201302UV02MFMIMT700701UV01ControlActProcess();
        controlActProcess.setTypeId(createTypeId());
        message.setControlActProcess(controlActProcess);
        PRPAIN201302UV02MFMIMT700701UV01Subject1 subject1 = extractor.ExtractSubjectFromMessage(message);
        assertNull(subject1);
    }

    @Test
    public void ExtractSubjectFromMessageWhenPRPAIN201310SubjectNull() {
        HL7Extractors extractor = new HL7Extractors();
        PRPAIN201310UV02 message = new PRPAIN201310UV02();
        PRPAIN201310UV02MFMIMT700711UV01ControlActProcess controlActProcess = new PRPAIN201310UV02MFMIMT700711UV01ControlActProcess();
        controlActProcess.setTypeId(createTypeId());
        message.setControlActProcess(controlActProcess);
        PRPAIN201310UV02MFMIMT700711UV01Subject1 subject1 = extractor.ExtractSubjectFromMessage(message);
        assertNull(subject1);
    }

    @Test
    public void ExtractSubjectFromMessageWhenSubjectListSizeEmpty() {
        HL7Extractors extractor = new HL7Extractors();
        PRPAIN201301UV02 message = new PRPAIN201301UV02();
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess controlActProcess = new PRPAIN201301UV02MFMIMT700701UV01ControlActProcess();
        List<PRPAIN201301UV02MFMIMT700701UV01Subject1> subjects = new ArrayList<>();
        controlActProcess.setTypeId(createTypeId());
        controlActProcess.getSubject().addAll(subjects);
        message.setControlActProcess(controlActProcess);
        PRPAIN201301UV02MFMIMT700701UV01Subject1 subject1 = extractor.ExtractSubjectFromMessage(message);
        assertNull(subject1);
    }

    @Test
    public void ExtractSubjectFromMessageWhenPRPAIN201302UV02SubjectListSizeEmpty() {
        HL7Extractors extractor = new HL7Extractors();
        PRPAIN201302UV02 message = new PRPAIN201302UV02();
        PRPAIN201302UV02MFMIMT700701UV01ControlActProcess controlActProcess = new PRPAIN201302UV02MFMIMT700701UV01ControlActProcess();
        List<PRPAIN201302UV02MFMIMT700701UV01Subject1> subjects = new ArrayList<>();
        controlActProcess.setTypeId(createTypeId());
        controlActProcess.getSubject().addAll(subjects);
        message.setControlActProcess(controlActProcess);
        PRPAIN201302UV02MFMIMT700701UV01Subject1 subject1 = extractor.ExtractSubjectFromMessage(message);
        assertNull(subject1);
    }

    @Test
    public void ExtractHL7PatientFromMessage() {
        HL7Extractors extractor = new HL7Extractors();
        PRPAMT201301UV02Patient patient = extractor.ExtractHL7PatientFromMessage(createPRPAIN201301UV02());
        assertEquals(patient.getPatientPerson().getValue().getDeterminerCode(), "INSTANCE");
    }

    @Test
    public void ExtractHL7PatientFromPRPAIN201302UV02Message() {
        HL7Extractors extractor = new HL7Extractors();
        PRPAMT201302UV02Patient patient = extractor.ExtractHL7PatientFromMessage(createPRPAIN201302UV02());
        assertEquals(patient.getPatientPerson().getValue().getDeterminerCode(), "INSTANCE");
    }

    @Test
    public void ExtractHL7PatientFromPRPAIN201310UV02Message() {
        HL7Extractors extractor = new HL7Extractors();
        PRPAMT201304UV02Patient patient = extractor.ExtractHL7PatientFromMessage(createPRPAIN201310UV02());
        assertEquals(patient.getPatientPerson().getValue().getDeterminerCode(), "INSTANCE");
    }

    @Test
    public void ExtractHL7PatientFromMessageWhenSubjectNull() {
        HL7Extractors extractor = new HL7Extractors();
        PRPAIN201301UV02 message = new PRPAIN201301UV02();
        PRPAMT201301UV02Patient patient = extractor.ExtractHL7PatientFromMessage(message);
        assertNull(patient);
    }

    @Test
    public void ExtractHL7PatientFromPRPAIN201302UV02MessageWhenSubjectNull() {
        HL7Extractors extractor = new HL7Extractors();
        PRPAIN201302UV02 message = new PRPAIN201302UV02();
        PRPAMT201302UV02Patient patient = extractor.ExtractHL7PatientFromMessage(message);
        assertNull(patient);
    }

    @Test
    public void ExtractHL7PatientFromPRPAIN201310UV02MessageWhenSubjectNull() {
        HL7Extractors extractor = new HL7Extractors();
        PRPAIN201310UV02 message = new PRPAIN201310UV02();
        PRPAMT201304UV02Patient patient = extractor.ExtractHL7PatientFromMessage(message);
        assertNull(patient);
    }

    @Test
    public void ExtractHL7PatientFromMessageWhenRegistrationEventNull() {
        HL7Extractors extractor = new HL7Extractors();
        PRPAIN201301UV02 message = createMessageWhereregistrationEventNull();
        PRPAMT201301UV02Patient patient = extractor.ExtractHL7PatientFromMessage(message);
        assertNull(patient);
    }

    @Test
    public void ExtractHL7PatientFromPRPAIN201302MessageWhenRegistrationEventNull() {
        HL7Extractors extractor = new HL7Extractors();
        PRPAIN201302UV02 message = createPRPAIN201302UV02MessageWhereregistrationEventNull();
        PRPAMT201302UV02Patient patient = extractor.ExtractHL7PatientFromMessage(message);
        assertNull(patient);
    }

    @Test
    public void ExtractHL7PatientFromPRPAIN201310MessageWhenRegistrationEventNull() {
        HL7Extractors extractor = new HL7Extractors();
        PRPAIN201310UV02 message = createPRPAIN201310UV02MessageWhereregistrationEventNull();
        PRPAMT201304UV02Patient patient = extractor.ExtractHL7PatientFromMessage(message);
        assertNull(patient);
    }

    @Test
    public void ExtractHL7PatientFromPRPAIN201301MessageWhenSubject1Null() {
        HL7Extractors extractor = new HL7Extractors();
        PRPAIN201301UV02 message = createMessageWhereSubject1Null();
        PRPAMT201301UV02Patient patient = extractor.ExtractHL7PatientFromMessage(message);
        assertNull(patient);
    }

    @Test
    public void ExtractHL7PatientFromPRPAIN201302MessageWhenSubject1Null() {
        HL7Extractors extractor = new HL7Extractors();
        PRPAIN201302UV02 message = createPRPAIN201302UV02MessageWhereSubject1Null();
        PRPAMT201302UV02Patient patient = extractor.ExtractHL7PatientFromMessage(message);
        assertNull(patient);
    }

    @Test
    public void ExtractHL7PatientFromPRPAIN201310MessageWhenSubject1Null() {
        HL7Extractors extractor = new HL7Extractors();
        PRPAIN201310UV02 message = createPRPAIN201310UV02MessageWhereSubject1Null();
        PRPAMT201304UV02Patient patient = extractor.ExtractHL7PatientFromMessage(message);
        assertNull(patient);
    }

    @Test
    public void ExtractHL7PatientFromPRPAIN201301MessageWhenPatientNull() {
        HL7Extractors extractor = new HL7Extractors();
        PRPAIN201301UV02 message = createMessageWherePatientNull();
        PRPAMT201301UV02Patient patient = extractor.ExtractHL7PatientFromMessage(message);
        assertNull(patient);
    }

    @Test
    public void ExtractHL7PatientFromPRPAIN201302MessageWhenPatientNull() {
        HL7Extractors extractor = new HL7Extractors();
        PRPAIN201302UV02 message = createPRPAIN201302UV02MessageWherePatientNull();
        PRPAMT201302UV02Patient patient = extractor.ExtractHL7PatientFromMessage(message);
        assertNull(patient);
    }

    @Test
    public void ExtractHL7PatientFromPRPAIN201310MessageWhenPatientNull() {
        HL7Extractors extractor = new HL7Extractors();
        PRPAIN201310UV02 message = createPRPAIN201310UV02MessageWherePatientNull();
        PRPAMT201304UV02Patient patient = extractor.ExtractHL7PatientFromMessage(message);
        assertNull(patient);
    }

    @Test
    public void ExtractHL7PatientPersonFromHL7Patient() {
        HL7Extractors extractor = new HL7Extractors();
        PRPAMT201301UV02Person patientPerson = extractor
                .ExtractHL7PatientPersonFromHL7Patient(createPRPAMT201301UV02Patient());
        assertEquals(patientPerson.getDeterminerCode(), "INSTANCE");
    }

    @Test
    public void ExtractHL7PatientPersonFromPRPAMT201302UV02HL7Patient() {
        HL7Extractors extractor = new HL7Extractors();
        PRPAMT201302UV02Person patientPerson = extractor
                .ExtractHL7PatientPersonFromHL7Patient(createPRPAMT201302UV02Patient());
        assertEquals(patientPerson.getDeterminerCode(), "INSTANCE");
    }

    @Test
    public void ExtractHL7PatientPersonFrom201301Message() {
        HL7Extractors extractor = new HL7Extractors();
        PRPAMT201301UV02Person patientPerson = extractor
                .ExtractHL7PatientPersonFrom201301Message(createPRPAIN201301UV02());
        assertEquals(patientPerson.getDeterminerCode(), "INSTANCE");
    }

    @Test
    public void ExtractHL7PatientPersonFrom201302Message() {
        HL7Extractors extractor = new HL7Extractors();
        PRPAMT201302UV02Person patientPerson = extractor
                .ExtractHL7PatientPersonFrom201302Message(createPRPAIN201302UV02());
        assertEquals(patientPerson.getDeterminerCode(), "INSTANCE");
    }

    @Test
    public void ExtractHL7ReceiverOID() {
        String SenderOID = "1.1";
        HL7Extractors extractor = new HL7Extractors();
        String ReceiverOID = extractor.ExtractHL7ReceiverOID(createPRPAIN201305UV02(SenderOID));
        assertEquals(ReceiverOID, "2.2");
    }

    @Test
    public void ExtractHL7SenderOID() {
        String ID = "1.1";
        HL7Extractors extractor = new HL7Extractors();
        String SenderOID = extractor.ExtractHL7SenderOID(createPRPAIN201305UV02(ID));
        assertEquals(SenderOID, ID);
    }

    @Test
    public void translatePNListtoPersonNameTypeWhenNameTypeAndNameListNull() {
        HL7Extractors extractor = new HL7Extractors();
        PersonNameType person = extractor
                .translatePNListtoPersonNameType(createPNExplicitNamesListWhenNameTypeandNameListNull());
        assertNull(person.getFamilyName());
    }

    @Test
    public void translatePNListtoPersonNameType() {
        HL7Extractors extractor = new HL7Extractors();
        PersonNameType person = extractor.translatePNListtoPersonNameType(createPNExplicitNamesList());
        assertEquals(person.getFamilyName(), "Younger");
        assertEquals(person.getGivenName(), "Gallow");
    }

    @Test
    public void translateENListtoPersonNameType() {
        HL7Extractors extractor = new HL7Extractors();
        PersonNameType person = extractor.translateENListtoPersonNameType(createENExplicitList());
        assertEquals(person.getFamilyName(), "Younger");
        assertEquals(person.getGivenName(), "Gallow");
    }

    @Test
    public void translateENListtoPersonNameTypeWhenUserTypeAndUserNameListNull() {
        HL7Extractors extractor = new HL7Extractors();
        PersonNameType person = extractor
                .translateENListtoPersonNameType(createENExplicitNamesListWhenNameTypeandNameListNull());
        assertNull(person.getFamilyName());
    }

    private List<ENExplicit> createENExplicitNamesListWhenNameTypeandNameListNull() {
        List<ENExplicit> pnList = new ArrayList<>();
        ENExplicit name = new ENExplicit();
        pnList.add(name);
        return pnList;
    }

    private List<ENExplicit> createENExplicitList() {
        List<ENExplicit> pnList = new ArrayList<>();
        ENExplicit name = createENExplicit();
        pnList.add(name);
        return pnList;
    }

    private ENExplicit createENExplicit() {
        org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
        ENExplicit enName = factory.createENExplicit();
        List enNamelist = enName.getContent();
        EnExplicitFamily familyName = new EnExplicitFamily();
        familyName.setPartType("FAM");
        familyName.setContent(getLastName());
        enNamelist.add(factory.createENExplicitFamily(familyName));
        EnExplicitGiven result = new EnExplicitGiven();
        result.setPartType("GIV");
        result.setContent(getFirstName());
        enNamelist.add(factory.createENExplicitGiven(result));
        return enName;
    }

    private List<PNExplicit> createPNExplicitNamesListWhenNameTypeandNameListNull() {
        List<PNExplicit> pnList = new ArrayList<>();
        PNExplicit name = new PNExplicit();
        pnList.add(name);
        return pnList;
    }

    private static List<PNExplicit> createPNExplicitNamesList() {
        List<PNExplicit> pnList = new ArrayList<>();
        PNExplicit name = createPNExplicitName();
        pnList.add(name);
        return pnList;
    }

    private static String getFirstName() {
        String firstName = "Gallow";
        return firstName;
    }

    private static String getLastName() {
        String lastName = "Younger";
        return lastName;
    }

    private static String getNameType() {
        String nameType = "PNExplicitNameType";
        return nameType;
    }

    private static PNExplicit createPNExplicitName() {
        org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
        PNExplicit name = factory.createPNExplicit();
        List namelist = name.getContent();
        EnExplicitFamily familyName = new EnExplicitFamily();
        familyName.setPartType("FAM");
        familyName.setContent(getLastName());
        namelist.add(factory.createPNExplicitFamily(familyName));
        EnExplicitGiven givenName = new EnExplicitGiven();
        givenName.setPartType("GIV");
        givenName.setContent(getFirstName());
        namelist.add(factory.createPNExplicitGiven(givenName));
        name.getUse().add(getNameType());
        return name;
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

    private PRPAIN201310UV02 createPRPAIN201310UV02MessageWherePatientNull() {
        PRPAIN201310UV02 message = new PRPAIN201310UV02();
        PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent registrationevent = new PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent();
        registrationevent.setTypeId(createTypeId());
        PRPAIN201310UV02MFMIMT700711UV01Subject2 subject1 = new PRPAIN201310UV02MFMIMT700711UV01Subject2();
        subject1.setTypeId(createTypeId());
        registrationevent.setSubject1(subject1);
        List<PRPAIN201310UV02MFMIMT700711UV01Subject1> subjects = new ArrayList<>();

        PRPAIN201310UV02MFMIMT700711UV01Subject1 subject = new PRPAIN201310UV02MFMIMT700711UV01Subject1();
        subject.setTypeId(createTypeId());
        subject.setRegistrationEvent(registrationevent);
        subjects.add(subject);
        PRPAIN201310UV02MFMIMT700711UV01ControlActProcess controlActProcess = new PRPAIN201310UV02MFMIMT700711UV01ControlActProcess();
        controlActProcess.setTypeId(createTypeId());
        controlActProcess.setCode(createCD());
        controlActProcess.getSubject().addAll(subjects);
        message.setControlActProcess(controlActProcess);
        return message;
    }

    private PRPAIN201302UV02 createPRPAIN201302UV02MessageWherePatientNull() {
        PRPAIN201302UV02 message = new PRPAIN201302UV02();
        PRPAIN201302UV02MFMIMT700701UV01RegistrationEvent registrationevent = new PRPAIN201302UV02MFMIMT700701UV01RegistrationEvent();
        registrationevent.setTypeId(createTypeId());
        PRPAIN201302UV02MFMIMT700701UV01Subject2 subject1 = new PRPAIN201302UV02MFMIMT700701UV01Subject2();
        subject1.setTypeId(createTypeId());
        registrationevent.setSubject1(subject1);
        List<PRPAIN201302UV02MFMIMT700701UV01Subject1> subjects = new ArrayList<>();

        PRPAIN201302UV02MFMIMT700701UV01Subject1 subject = new PRPAIN201302UV02MFMIMT700701UV01Subject1();
        subject.setTypeId(createTypeId());
        subject.setRegistrationEvent(registrationevent);
        subjects.add(subject);
        PRPAIN201302UV02MFMIMT700701UV01ControlActProcess controlActProcess = new PRPAIN201302UV02MFMIMT700701UV01ControlActProcess();
        controlActProcess.setTypeId(createTypeId());
        controlActProcess.setCode(createCD());
        controlActProcess.getSubject().addAll(subjects);
        message.setControlActProcess(controlActProcess);
        return message;
    }

    private PRPAIN201301UV02 createMessageWherePatientNull() {
        PRPAIN201301UV02 message = new PRPAIN201301UV02();
        PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent registrationevent = new PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent();
        registrationevent.setTypeId(createTypeId());
        PRPAIN201301UV02MFMIMT700701UV01Subject2 subject1 = new PRPAIN201301UV02MFMIMT700701UV01Subject2();
        subject1.setTypeId(createTypeId());
        registrationevent.setSubject1(subject1);
        List<PRPAIN201301UV02MFMIMT700701UV01Subject1> subjects = new ArrayList<>();

        PRPAIN201301UV02MFMIMT700701UV01Subject1 subject = new PRPAIN201301UV02MFMIMT700701UV01Subject1();
        subject.setTypeId(createTypeId());
        subject.setRegistrationEvent(registrationevent);
        subjects.add(subject);
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess controlActProcess = new PRPAIN201301UV02MFMIMT700701UV01ControlActProcess();
        controlActProcess.setTypeId(createTypeId());
        controlActProcess.setCode(createCD());
        controlActProcess.getSubject().addAll(subjects);
        message.setControlActProcess(controlActProcess);
        return message;
    }

    private PRPAIN201310UV02 createPRPAIN201310UV02MessageWhereSubject1Null() {
        PRPAIN201310UV02 message = new PRPAIN201310UV02();
        PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent registrationevent = new PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent();
        List<PRPAIN201310UV02MFMIMT700711UV01Subject1> subjects = new ArrayList<>();
        registrationevent.setTypeId(createTypeId());
        PRPAIN201310UV02MFMIMT700711UV01Subject1 subject = new PRPAIN201310UV02MFMIMT700711UV01Subject1();
        subject.setTypeId(createTypeId());
        subject.setRegistrationEvent(registrationevent);
        subjects.add(subject);
        PRPAIN201310UV02MFMIMT700711UV01ControlActProcess controlActProcess = new PRPAIN201310UV02MFMIMT700711UV01ControlActProcess();
        controlActProcess.setTypeId(createTypeId());
        controlActProcess.setCode(createCD());
        controlActProcess.getSubject().addAll(subjects);
        message.setControlActProcess(controlActProcess);
        return message;
    }

    private PRPAIN201302UV02 createPRPAIN201302UV02MessageWhereSubject1Null() {
        PRPAIN201302UV02 message = new PRPAIN201302UV02();
        PRPAIN201302UV02MFMIMT700701UV01RegistrationEvent registrationevent = new PRPAIN201302UV02MFMIMT700701UV01RegistrationEvent();
        List<PRPAIN201302UV02MFMIMT700701UV01Subject1> subjects = new ArrayList<>();
        registrationevent.setTypeId(createTypeId());
        PRPAIN201302UV02MFMIMT700701UV01Subject1 subject = new PRPAIN201302UV02MFMIMT700701UV01Subject1();
        subject.setTypeId(createTypeId());
        subject.setRegistrationEvent(registrationevent);
        subjects.add(subject);
        PRPAIN201302UV02MFMIMT700701UV01ControlActProcess controlActProcess = new PRPAIN201302UV02MFMIMT700701UV01ControlActProcess();
        controlActProcess.setTypeId(createTypeId());
        controlActProcess.setCode(createCD());
        controlActProcess.getSubject().addAll(subjects);
        message.setControlActProcess(controlActProcess);
        return message;
    }

    private PRPAIN201301UV02 createMessageWhereSubject1Null() {
        PRPAIN201301UV02 message = new PRPAIN201301UV02();
        PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent registrationevent = new PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent();
        List<PRPAIN201301UV02MFMIMT700701UV01Subject1> subjects = new ArrayList<>();
        registrationevent.setTypeId(createTypeId());
        PRPAIN201301UV02MFMIMT700701UV01Subject1 subject = new PRPAIN201301UV02MFMIMT700701UV01Subject1();
        subject.setTypeId(createTypeId());
        subject.setRegistrationEvent(registrationevent);
        subjects.add(subject);
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess controlActProcess = new PRPAIN201301UV02MFMIMT700701UV01ControlActProcess();
        controlActProcess.setTypeId(createTypeId());
        controlActProcess.setCode(createCD());
        controlActProcess.getSubject().addAll(subjects);
        message.setControlActProcess(controlActProcess);
        return message;
    }

    private PRPAIN201302UV02 createPRPAIN201302UV02MessageWhereregistrationEventNull() {
        PRPAIN201302UV02 message = new PRPAIN201302UV02();
        List<PRPAIN201302UV02MFMIMT700701UV01Subject1> subjects = new ArrayList<>();
        PRPAIN201302UV02MFMIMT700701UV01Subject1 subject = new PRPAIN201302UV02MFMIMT700701UV01Subject1();
        subject.setTypeId(createTypeId());
        subjects.add(subject);
        PRPAIN201302UV02MFMIMT700701UV01ControlActProcess controlActProcess = new PRPAIN201302UV02MFMIMT700701UV01ControlActProcess();
        controlActProcess.setTypeId(createTypeId());
        controlActProcess.setCode(createCD());
        controlActProcess.getSubject().addAll(subjects);
        message.setControlActProcess(controlActProcess);
        return message;
    }

    private PRPAIN201310UV02 createPRPAIN201310UV02MessageWhereregistrationEventNull() {
        PRPAIN201310UV02 message = new PRPAIN201310UV02();
        List<PRPAIN201310UV02MFMIMT700711UV01Subject1> subjects = new ArrayList<>();
        PRPAIN201310UV02MFMIMT700711UV01Subject1 subject = new PRPAIN201310UV02MFMIMT700711UV01Subject1();
        subject.setTypeId(createTypeId());
        subjects.add(subject);
        PRPAIN201310UV02MFMIMT700711UV01ControlActProcess controlActProcess = new PRPAIN201310UV02MFMIMT700711UV01ControlActProcess();
        controlActProcess.setTypeId(createTypeId());
        controlActProcess.setCode(createCD());
        controlActProcess.getSubject().addAll(subjects);
        message.setControlActProcess(controlActProcess);
        return message;
    }

    private PRPAIN201301UV02 createMessageWhereregistrationEventNull() {
        PRPAIN201301UV02 message = new PRPAIN201301UV02();
        List<PRPAIN201301UV02MFMIMT700701UV01Subject1> subjects = new ArrayList<>();
        PRPAIN201301UV02MFMIMT700701UV01Subject1 subject = new PRPAIN201301UV02MFMIMT700701UV01Subject1();
        subject.setTypeId(createTypeId());
        subjects.add(subject);
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess controlActProcess = new PRPAIN201301UV02MFMIMT700701UV01ControlActProcess();
        controlActProcess.setTypeId(createTypeId());
        controlActProcess.setCode(createCD());
        controlActProcess.getSubject().addAll(subjects);
        message.setControlActProcess(controlActProcess);
        return message;
    }

    private PRPAIN201301UV02 createPRPAIN201301UV02() {
        PRPAIN201301UV02 message = new PRPAIN201301UV02();
        message.setControlActProcess(createPRPAIN201301UV02MFMIMT700701UV01ControlActProcess());
        message.setId(createTypeId());
        message.setTypeId(createTypeId());
        message.setProcessingCode(createCS());
        return message;
    }

    private PRPAIN201302UV02 createPRPAIN201302UV02() {
        PRPAIN201302UV02 message = new PRPAIN201302UV02();
        message.setControlActProcess(createPRPAIN201302UV02MFMIMT700701UV01ControlActProcess());
        message.setId(createTypeId());
        message.setTypeId(createTypeId());
        message.setProcessingCode(createCS());
        return message;
    }

    private PRPAIN201310UV02 createPRPAIN201310UV02() {
        PRPAIN201310UV02 message = new PRPAIN201310UV02();
        message.setControlActProcess(createPRPAIN201310UV02MFMIMT700711UV01ControlActProcess());
        message.setId(createTypeId());
        message.setTypeId(createTypeId());
        message.setProcessingCode(createCS());
        return message;
    }

    private CS createCS() {
        CS code = new CS();
        code.setCode("CONNECT");
        code.setCodeSystem("CONNECTSystem");
        code.setCodeSystemName("CONNECTDomain");
        code.setDisplayName("CONNECT4.0");
        code.setCodeSystemVersion("4.0");
        return code;
    }

    private PRPAIN201301UV02MFMIMT700701UV01ControlActProcess createPRPAIN201301UV02MFMIMT700701UV01ControlActProcess() {
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess controlActProcess = new PRPAIN201301UV02MFMIMT700701UV01ControlActProcess();
        controlActProcess.setTypeId(createTypeId());
        controlActProcess.setCode(createCD());
        controlActProcess.getSubject().addAll(createPRPAIN201301UV02MFMIMT700701UV01Subject1());
        return controlActProcess;
    }

    private PRPAIN201310UV02MFMIMT700711UV01ControlActProcess createPRPAIN201310UV02MFMIMT700711UV01ControlActProcess() {
        PRPAIN201310UV02MFMIMT700711UV01ControlActProcess controlActProcess = new PRPAIN201310UV02MFMIMT700711UV01ControlActProcess();
        controlActProcess.setTypeId(createTypeId());
        controlActProcess.setCode(createCD());
        controlActProcess.getSubject().addAll(createPRPAIN201310UV02MFMIMT700711UV01Subject1());
        return controlActProcess;
    }

    private PRPAIN201302UV02MFMIMT700701UV01ControlActProcess createPRPAIN201302UV02MFMIMT700701UV01ControlActProcess() {
        PRPAIN201302UV02MFMIMT700701UV01ControlActProcess controlActProcess = new PRPAIN201302UV02MFMIMT700701UV01ControlActProcess();
        controlActProcess.setTypeId(createTypeId());
        controlActProcess.setCode(createCD());
        controlActProcess.getSubject().addAll(createPRPAIN201302UV02MFMIMT700701UV01Subject1());
        return controlActProcess;
    }

    private List<PRPAIN201301UV02MFMIMT700701UV01Subject1> createPRPAIN201301UV02MFMIMT700701UV01Subject1() {
        List<PRPAIN201301UV02MFMIMT700701UV01Subject1> subjects = new ArrayList<>();
        PRPAIN201301UV02MFMIMT700701UV01Subject1 subject = new PRPAIN201301UV02MFMIMT700701UV01Subject1();
        subject.setTypeId(createTypeId());
        subject.setRegistrationEvent(createPRPAIN201301UV02MFMIMT700701UV01RegistrationEvent());
        subjects.add(subject);
        return subjects;
    }

    private List<PRPAIN201302UV02MFMIMT700701UV01Subject1> createPRPAIN201302UV02MFMIMT700701UV01Subject1() {
        List<PRPAIN201302UV02MFMIMT700701UV01Subject1> subjects = new ArrayList<>();
        PRPAIN201302UV02MFMIMT700701UV01Subject1 subject = new PRPAIN201302UV02MFMIMT700701UV01Subject1();
        subject.setTypeId(createTypeId());
        subject.setRegistrationEvent(createPRPAIN201302UV02MFMIMT700701UV01RegistrationEvent());
        subjects.add(subject);
        return subjects;
    }

    private List<PRPAIN201310UV02MFMIMT700711UV01Subject1> createPRPAIN201310UV02MFMIMT700711UV01Subject1() {
        List<PRPAIN201310UV02MFMIMT700711UV01Subject1> subjects = new ArrayList<>();
        PRPAIN201310UV02MFMIMT700711UV01Subject1 subject = new PRPAIN201310UV02MFMIMT700711UV01Subject1();
        subject.setTypeId(createTypeId());
        subject.setRegistrationEvent(createPRPAIN201310UV02MFMIMT700711UV01RegistrationEvent());
        subjects.add(subject);
        return subjects;
    }

    private PRPAIN201301UV02MFMIMT700701UV01Subject2 createSubject2() {
        PRPAIN201301UV02MFMIMT700701UV01Subject2 subject = new PRPAIN201301UV02MFMIMT700701UV01Subject2();
        subject.setPatient(createPRPAMT201301UV02Patient());
        subject.setTypeId(createTypeId());
        return subject;
    }

    private PRPAIN201302UV02MFMIMT700701UV01Subject2 createPRPAIN201302UV02Subject2() {
        PRPAIN201302UV02MFMIMT700701UV01Subject2 subject = new PRPAIN201302UV02MFMIMT700701UV01Subject2();
        subject.setPatient(createPRPAMT201302UV02Patient());
        subject.setTypeId(createTypeId());
        return subject;
    }

    private PRPAIN201310UV02MFMIMT700711UV01Subject2 createPRPAIN201310UV02Subject2() {
        PRPAIN201310UV02MFMIMT700711UV01Subject2 subject = new PRPAIN201310UV02MFMIMT700711UV01Subject2();
        subject.setPatient(createPRPAMT201304UV02Patient());
        subject.setTypeId(createTypeId());
        return subject;
    }

    private PRPAMT201301UV02Patient createPRPAMT201301UV02Patient() {
        org.hl7.v3.PRPAMT201301UV02Patient patient = new PRPAMT201301UV02Patient();
        PRPAMT201301UV02Person patientPerson = new PRPAMT201301UV02Person();
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        JAXBElement<PRPAMT201301UV02Person> patientPersonElement = new JAXBElement<>(xmlqname,
                PRPAMT201301UV02Person.class, patientPerson);
        patient.setPatientPerson(patientPersonElement);
        patient.getId().add(createTypeId());
        patientPerson.getClassCode().add("ClassCode");

        patientPerson.setDeterminerCode("INSTANCE");

        PNExplicit patientName = new PNExplicit();
        patientName.getNullFlavor().add("NA");
        return patient;
    }

    private PRPAMT201302UV02Patient createPRPAMT201302UV02Patient() {
        org.hl7.v3.PRPAMT201302UV02Patient patient = new PRPAMT201302UV02Patient();
        PRPAMT201302UV02PatientPatientPerson patientPerson = new PRPAMT201302UV02PatientPatientPerson();
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        JAXBElement<PRPAMT201302UV02PatientPatientPerson> patientPersonElement = new JAXBElement<>(
                xmlqname, PRPAMT201302UV02PatientPatientPerson.class, patientPerson);
        patient.setPatientPerson(patientPersonElement);
        patientPerson.getClassCode().add("ClassCode");
        patientPerson.setDeterminerCode("INSTANCE");

        PNExplicit patientName = new PNExplicit();
        patientName.getNullFlavor().add("NA");
        return patient;
    }

    private PRPAMT201304UV02Patient createPRPAMT201304UV02Patient() {
        org.hl7.v3.PRPAMT201304UV02Patient patient = new PRPAMT201304UV02Patient();
        PRPAMT201304UV02Person patientPerson = new PRPAMT201304UV02Person();
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        JAXBElement<PRPAMT201304UV02Person> patientPersonElement = new JAXBElement<>(xmlqname,
                PRPAMT201304UV02Person.class, patientPerson);
        patient.setPatientPerson(patientPersonElement);
        patientPerson.getClassCode().add("ClassCode");
        patientPerson.setDeterminerCode("INSTANCE");

        PNExplicit patientName = new PNExplicit();
        patientName.getNullFlavor().add("NA");
        return patient;
    }

    private PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent createPRPAIN201301UV02MFMIMT700701UV01RegistrationEvent() {
        PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent regEvent = new PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent();
        regEvent.setStatusCode(createCS());
        regEvent.setTypeId(createTypeId());
        regEvent.setCustodian(createMFMIMT700701UV01Custodian());
        regEvent.setSubject1(createSubject2());
        return regEvent;
    }

    private PRPAIN201302UV02MFMIMT700701UV01RegistrationEvent createPRPAIN201302UV02MFMIMT700701UV01RegistrationEvent() {
        PRPAIN201302UV02MFMIMT700701UV01RegistrationEvent regEvent = new PRPAIN201302UV02MFMIMT700701UV01RegistrationEvent();
        regEvent.setStatusCode(createCS());
        regEvent.setTypeId(createTypeId());
        regEvent.setCustodian(createMFMIMT700701UV01Custodian());
        regEvent.setSubject1(createPRPAIN201302UV02Subject2());
        return regEvent;
    }

    private PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent createPRPAIN201310UV02MFMIMT700711UV01RegistrationEvent() {
        PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent regEvent = new PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent();
        regEvent.setStatusCode(createCS());
        regEvent.setTypeId(createTypeId());
        regEvent.setSubject1(createPRPAIN201310UV02Subject2());
        return regEvent;
    }

    private MFMIMT700701UV01Custodian createMFMIMT700701UV01Custodian() {
        MFMIMT700701UV01Custodian custodian = new MFMIMT700701UV01Custodian();
        custodian.setTypeId(createTypeId());
        custodian.setContextControlCode("ContextControlCode");
        custodian.setAssignedEntity(createCOCTM090003UV01AssignedEntity());
        return custodian;
    }

    private COCTMT090003UV01AssignedEntity createCOCTM090003UV01AssignedEntity() {
        COCTMT090003UV01AssignedEntity assignedEntity = new COCTMT090003UV01AssignedEntity();
        assignedEntity.setClassCode("ClassCode");
        assignedEntity.setTypeId(createTypeId());
        assignedEntity.setCode(createCE());
        assignedEntity.getId().add(createTypeId());
        assignedEntity.setAssignedDevice(createAssignedDevice());
        return assignedEntity;
    }

    private JAXBElement<COCTMT090003UV01Device> createAssignedDevice() {
        COCTMT090003UV01Device device = new COCTMT090003UV01Device();
        device.setDeterminerCode("INSTANCE");
        device.setTypeId(createTypeId());
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "device");
        JAXBElement<COCTMT090003UV01Device> assignedDevice = new JAXBElement<>(xmlqname,
                COCTMT090003UV01Device.class, device);
        return assignedDevice;
    }

    private CE createCE() {
        CE ce = new CE();
        ce.setCode("CONNECT");
        ce.setCodeSystem("CONNECTSystem");
        ce.setCodeSystemName("CONNECTDomain");
        ce.setDisplayName("CONNECT4.0");
        ce.setCodeSystemVersion("4.0");
        return ce;
    }

    private CD createCD() {
        CD cd = new CD();
        cd.setCode("CONNECT");
        cd.setCodeSystem("CONNECTSystem");
        cd.setCodeSystemName("CONNECTDomain");
        cd.setDisplayName("CONNECT4.0");
        cd.setCodeSystemVersion("4.0");
        return cd;
    }

    private II createTypeId() {
        II typeId = new II();
        typeId.setAssigningAuthorityName("1.1");
        typeId.setExtension("D123401");
        typeId.setRoot("1.1");
        return typeId;
    }

}
