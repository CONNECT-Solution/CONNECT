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
package gov.hhs.fha.nhinc.mpi.adapter.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.hl7.v3.CD;
import org.hl7.v3.CE;
import org.hl7.v3.CS;
import org.hl7.v3.CommunicationFunctionType;
import org.hl7.v3.ENExplicit;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.EnExplicitPrefix;
import org.hl7.v3.EnExplicitSuffix;
import org.hl7.v3.II;
import org.hl7.v3.IVLTSExplicit;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201305UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectAdministrativeGender;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectBirthTime;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectName;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;
import org.hl7.v3.PRPAMT201310UV02Patient;
import org.hl7.v3.PRPAMT201310UV02Person;
import org.hl7.v3.TELExplicit;
import org.hl7.v3.TSExplicit;
import org.hl7.v3.XActMoodIntentEvent;

import gov.hhs.fha.nhinc.mpilib.Identifier;
import gov.hhs.fha.nhinc.mpilib.Identifiers;
import gov.hhs.fha.nhinc.mpilib.Patient;
import gov.hhs.fha.nhinc.mpilib.PersonName;

/**
 *
 * @author Jon Hoppesch
 */
public class TestHelper {

    /**
     * Test: Patient ID's are the same.
     * @param expected expected
     * @param result result
     */
    public static void assertPatientIdsAreSame(PRPAIN201306UV02 expected, PRPAIN201306UV02 result) {
        AssertPatientNotNull(expected);
        AssertPatientNotNull(result);

        AssertPatientIdsEqual(expected.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                .getPatient(), result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                .getPatient());
    }

    /**
     * Test: Patient names are the same.
     * @param expected
     * @param result
     */
    public static void assertPatientNamesAreSame(PRPAIN201306UV02 expected, PRPAIN201306UV02 result) {
        AssertPatientPersonNotNull(expected);
        AssertPatientPersonNotNull(result);

        AssertPatientNamesEqual(expected.getControlActProcess().getSubject().get(0).getRegistrationEvent()
                .getSubject1().getPatient().getPatientPerson().getValue(), result.getControlActProcess().getSubject()
                .get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue());
    }

    public static void assertPatientGendersAreSame(PRPAIN201306UV02 expected, PRPAIN201306UV02 result) {
        AssertPatientPersonNotNull(expected);
        AssertPatientPersonNotNull(result);

        AssertPatientGendersEqual(expected.getControlActProcess().getSubject().get(0).getRegistrationEvent()
                .getSubject1().getPatient().getPatientPerson().getValue(), result.getControlActProcess().getSubject()
                .get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue());
    }

    public static void assertPatientBdaysAreSame(PRPAIN201306UV02 expected, PRPAIN201306UV02 result) {
        AssertPatientPersonNotNull(expected);
        AssertPatientPersonNotNull(result);

        AssertPatientBdaysEqual(expected.getControlActProcess().getSubject().get(0).getRegistrationEvent()
                .getSubject1().getPatient().getPatientPerson().getValue(), result.getControlActProcess().getSubject()
                .get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue());
    }

    public static void AssertPatientNamesEqual(PRPAMT201310UV02Person patient1, PRPAMT201310UV02Person patient2) {
        assertNotNull(patient1.getName());
        assertNotNull(patient1.getName().get(0));
        assertNotNull(patient1.getName().get(0).getContent());
        assertNotNull(patient2.getName());
        assertNotNull(patient2.getName().get(0));
        assertNotNull(patient2.getName().get(0).getContent());

        String pat1Name = extractName(patient1.getName().get(0));
        System.out.println("Patient 1 name:" + pat1Name);
        String pat2Name = extractName(patient2.getName().get(0));
       	System.out.println("Patient 2 name:" + pat2Name);
        assertEquals(pat1Name, pat2Name);
    }

    private static String extractName(PNExplicit name) {
        String nameString = "";
        Boolean hasName = false;
        List<Serializable> choice = name.getContent();
        Iterator<Serializable> iterSerialObjects = choice.iterator();

        EnExplicitFamily familyName = new EnExplicitFamily();
        EnExplicitGiven givenName = null;
        String middleName = "";
        String prefix = "";
        String suffix = "";
        while (iterSerialObjects.hasNext()) {
            Serializable contentItem = iterSerialObjects.next();

            if (contentItem instanceof JAXBElement) {
                JAXBElement oJAXBElement = (JAXBElement) contentItem;
                if (oJAXBElement.getValue() instanceof EnExplicitFamily) {
                    familyName = (EnExplicitFamily) oJAXBElement.getValue();
                    hasName = true;
                } else if (oJAXBElement.getValue() instanceof EnExplicitGiven) {

                    if (givenName == null) {
                        givenName = (EnExplicitGiven) oJAXBElement.getValue();
                    } else {
                        EnExplicitGiven middle = (EnExplicitGiven) oJAXBElement.getValue();
                        middleName = middle.getContent();
                    }
                    hasName = true;
                } else if (oJAXBElement.getValue() instanceof EnExplicitPrefix) {
                    EnExplicitPrefix prefixObj = (EnExplicitPrefix) oJAXBElement.getValue();
                    prefix = prefixObj.getContent();

                } else if (oJAXBElement.getValue() instanceof EnExplicitSuffix) {
                    EnExplicitSuffix suffixObj = (EnExplicitSuffix) oJAXBElement.getValue();
                    suffix = suffixObj.getContent();
                }
            }
        }

        if (hasName) {
            nameString = familyName.getContent() + " " + givenName.getContent();
            if (middleName != "") {
                nameString += " " + middleName;
            }
            if (prefix != "") {
                nameString += " " + prefix;
            }
            if (suffix != "") {
                nameString += " " + suffix;
            }
            System.out.println(nameString);
        }

        return nameString;
    }

    public static void AssertPatientGendersEqual(PRPAMT201310UV02Person patient1, PRPAMT201310UV02Person patient2) {
        assertNotNull(patient1.getAdministrativeGenderCode());
        assertNotNull(patient1.getAdministrativeGenderCode().getCode());
        assertNotNull(patient2.getAdministrativeGenderCode());
        assertNotNull(patient1.getAdministrativeGenderCode().getCode());

        assertEquals(patient1.getAdministrativeGenderCode().getCode(), patient1.getAdministrativeGenderCode().getCode());
    }

    public static void AssertPatientBdaysEqual(PRPAMT201310UV02Person patient1, PRPAMT201310UV02Person patient2) {
        assertNotNull(patient1.getBirthTime());
        assertNotNull(patient1.getBirthTime().getValue());
        assertNotNull(patient2.getBirthTime());
        assertNotNull(patient2.getBirthTime().getValue());

        assertEquals(patient1.getBirthTime().getValue(), patient2.getBirthTime().getValue());
    }

    public static void AssertPatientIdsEqual(PRPAMT201310UV02Patient patient1, PRPAMT201310UV02Patient patient2) {
        AssertPatientIdNotNull(patient1);
        AssertPatientIdNotNull(patient2);

        assertEquals(patient1.getId().get(0).getRoot(), patient1.getId().get(0).getRoot());
        assertEquals(patient1.getId().get(0).getExtension(), patient1.getId().get(0).getExtension());
    }

    public static void AssertPatientIdNotNull(PRPAMT201310UV02Patient patient) {
        assertNotNull(patient.getId());
        assertNotNull(patient.getId().get(0));
        assertNotNull(patient.getId().get(0).getRoot());
        assertNotNull(patient.getId().get(0).getExtension());
    }

    public static void AssertPatientPersonNotNull(PRPAIN201306UV02 queryResp) {
        AssertPatientNotNull(queryResp);
        assertNotNull(queryResp.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                .getPatient().getPatientPerson());
        assertNotNull(queryResp.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                .getPatient().getPatientPerson().getValue());
    }

    public static void AssertPatientNotNull(PRPAIN201306UV02 queryResp) {
        assertNotNull(queryResp);
        assertNotNull(queryResp.getControlActProcess());
        assertNotNull(queryResp.getControlActProcess().getSubject());
        assertNotNull(queryResp.getControlActProcess().getSubject().get(0));
        assertNotNull(queryResp.getControlActProcess().getSubject().get(0).getRegistrationEvent());
        assertNotNull(queryResp.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1());
        assertNotNull(queryResp.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                .getPatient());
    }

    public static PRPAIN201305UV02 build201305(String firstName, String lastName, String gender, String birthTime,
            II subjectId) {
        PRPAIN201305UV02 msg = new PRPAIN201305UV02();

        // Set up message header fields
        msg.setITSVersion("XML_1.0");

        II id = new II();
        id.setRoot("1.1");
        msg.setId(id);

        TSExplicit creationTime = new TSExplicit();
        creationTime.setValue("20090202000000");
        msg.setCreationTime(creationTime);

        II interactionId = new II();
        interactionId.setRoot("2.16.840.1.113883.1.6");
        interactionId.setExtension("PRPA_IN201305UV02");
        msg.setInteractionId(id);

        CS processingCode = new CS();
        processingCode.setCode("P");
        msg.setProcessingCode(processingCode);

        CS processingModeCode = new CS();
        processingModeCode.setCode("R");
        msg.setProcessingModeCode(processingModeCode);

        CS ackCode = new CS();
        ackCode.setCode("AL");
        msg.setAcceptAckCode(ackCode);

        // Set the receiver and sender
        msg.getReceiver().add(createReceiver());
        msg.setSender(createSender());

        msg.setControlActProcess(createControlActProcess(firstName, lastName, gender, birthTime, subjectId));

        return msg;
    }

    private static PRPAIN201305UV02QUQIMT021001UV01ControlActProcess createControlActProcess(String firstName,
            String lastName, String gender, String birthTime, II subjectId) {
        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess controlActProcess =
                new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();

        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);

        CD code = new CD();
        code.setCode("PRPA_TE201305UV");
        code.setCodeSystem("2.16.840.1.113883.1.6");
        controlActProcess.setCode(code);

        controlActProcess.setQueryByParameter(createQueryParams(firstName, lastName, gender, birthTime, subjectId));

        return controlActProcess;
    }

    private static JAXBElement<PRPAMT201306UV02QueryByParameter> createQueryParams(String firstName, String lastName,
            String gender, String birthTime, II subjectId) {
        PRPAMT201306UV02QueryByParameter params = new PRPAMT201306UV02QueryByParameter();

        II id = new II();
        id.setRoot("12345");
        params.setQueryId(id);

        CS statusCode = new CS();
        statusCode.setCode("new");
        params.setStatusCode(statusCode);

        params.setParameterList(createParamList(firstName, lastName, gender, birthTime, subjectId));

        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "queryByParameter");
        JAXBElement<PRPAMT201306UV02QueryByParameter> queryParams =
                new JAXBElement<PRPAMT201306UV02QueryByParameter>(xmlqname, PRPAMT201306UV02QueryByParameter.class,
                        params);

        return queryParams;
    }

    private static PRPAMT201306UV02ParameterList createParamList(String firstName, String lastName, String gender,
            String birthTime, II subjectId) {
        PRPAMT201306UV02ParameterList paramList = new PRPAMT201306UV02ParameterList();

        // Set the Subject Gender Code
        paramList.getLivingSubjectAdministrativeGender().add(createGender(gender));

        // Set the Subject Birth Time
        paramList.getLivingSubjectBirthTime().add(createBirthTime(birthTime));

        // Set the Subject Name
        paramList.getLivingSubjectName().add(createName(firstName, lastName));

        // Set the subject Id
        paramList.getLivingSubjectId().add(createSubjectId(subjectId));

        return paramList;
    }

    private static PRPAMT201306UV02LivingSubjectId createSubjectId(II subjectId) {
        PRPAMT201306UV02LivingSubjectId id = new PRPAMT201306UV02LivingSubjectId();
        if (subjectId != null) {
            id.getValue().add(subjectId);
        }

        return id;
    }

    private static PRPAMT201306UV02LivingSubjectName createName(String firstName, String lastName) {
        PRPAMT201306UV02LivingSubjectName subjectName = new PRPAMT201306UV02LivingSubjectName();
        org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
        ENExplicit name = (factory.createENExplicit());
        List namelist = name.getContent();

        if (lastName != null && lastName.length() > 0) {
            EnExplicitFamily familyName = new EnExplicitFamily();
            familyName.setPartType("FAM");
            familyName.setContent(lastName);

            namelist.add(factory.createENExplicitFamily(familyName));
        }

        if (firstName != null && firstName.length() > 0) {
            EnExplicitGiven givenName = new EnExplicitGiven();
            givenName.setPartType("GIV");
            givenName.setContent(firstName);

            namelist.add(factory.createENExplicitGiven(givenName));
        }

        subjectName.getValue().add(name);

        return subjectName;
    }

    private static PRPAMT201306UV02LivingSubjectBirthTime createBirthTime(String birthTime) {
        PRPAMT201306UV02LivingSubjectBirthTime subjectBirthTime = new PRPAMT201306UV02LivingSubjectBirthTime();
        IVLTSExplicit bday = new IVLTSExplicit();

        if (birthTime != null && birthTime.length() > 0) {
            bday.setValue(birthTime);
            subjectBirthTime.getValue().add(bday);
        }

        return subjectBirthTime;
    }

    private static PRPAMT201306UV02LivingSubjectAdministrativeGender createGender(String gender) {
        PRPAMT201306UV02LivingSubjectAdministrativeGender adminGender =
                new PRPAMT201306UV02LivingSubjectAdministrativeGender();
        CE genderCode = new CE();

        if (gender != null && gender.length() > 0) {
            genderCode.setCode(gender);
            adminGender.getValue().add(genderCode);
        }

        return adminGender;
    }

    private static MCCIMT000100UV01Receiver createReceiver() {
        MCCIMT000100UV01Receiver receiver = new MCCIMT000100UV01Receiver();

        receiver.setTypeCode(CommunicationFunctionType.RCV);

        MCCIMT000100UV01Device device = new MCCIMT000100UV01Device();
        device.setDeterminerCode("INSTANCE");

        II id = new II();
        id.setRoot("2.16.840.1.113883.3.200");
        device.getId().add(id);

        TELExplicit url = new TELExplicit();
        url.setValue("http://localhost:9080/NhinConnect/AdapterComponentMpiService");
        device.getTelecom().add(url);

        receiver.setDevice(device);

        return receiver;
    }

    private static MCCIMT000100UV01Sender createSender() {
        MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();

        sender.setTypeCode(CommunicationFunctionType.SND);

        MCCIMT000100UV01Device device = new MCCIMT000100UV01Device();
        device.setDeterminerCode("INSTANCE");

        II id = new II();
        id.setRoot("2.16.840.1.113883.3.200");
        device.getId().add(id);

        sender.setDevice(device);

        return sender;
    }

    public static Patient createMpiPatient(String firstName, String lastName, String gender, String birthTime,
            Identifier subjectId) {
        Patient result = new Patient();

        // Set the patient name
        PersonName name = new PersonName();
        name.setFirstName(firstName);
        name.setLastName(lastName);
        result.getNames().add(name);

        // Set the patient gender
        result.setGender(gender);

        // Set the patient birth time
        result.setDateOfBirth(birthTime);

        // Set the patient Id
        Identifiers ids = new Identifiers();
        ids.add(subjectId);
        result.setIdentifiers(ids);

        return result;
    }

    public static Patient createMpiPatient(String firstName, String lastName, String middleName, String gender,
            String birthTime, Identifier subjectId) {
        Patient result = new Patient();

        // Set the patient name
        PersonName name = new PersonName();
        name.setFirstName(firstName);
        name.setLastName(lastName);
        name.setMiddleName(middleName);
        result.getNames().add(name);

        // Set the patient gender
        result.setGender(gender);

        // Set the patient birth time
        result.setDateOfBirth(birthTime);

        // Set the patient Id
        Identifiers ids = new Identifiers();
        ids.add(subjectId);
        result.setIdentifiers(ids);

        return result;
    }

    public static Patient createMpiPatient(String firstName, String lastName, String middleName, String gender,
            String birthTime, Identifier subjectId, String title, String suffix) {
        Patient result = new Patient();

        // Set the patient name
        PersonName name = new PersonName();
        name.setFirstName(firstName);
        name.setLastName(lastName);
        name.setMiddleName(middleName);
        name.setTitle(title);
        name.setSuffix(suffix);

        result.getNames().add(name);

        // Set the patient gender
        result.setGender(gender);

        // Set the patient birth time
        result.setDateOfBirth(birthTime);

        // Set the patient Id
        Identifiers ids = new Identifiers();
        ids.add(subjectId);
        result.setIdentifiers(ids);

        return result;
    }
}
