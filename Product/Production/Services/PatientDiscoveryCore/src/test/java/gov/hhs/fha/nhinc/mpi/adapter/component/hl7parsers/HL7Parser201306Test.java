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
package gov.hhs.fha.nhinc.mpi.adapter.component.hl7parsers;

import gov.hhs.fha.nhinc.mpi.adapter.component.TestHelper;
import gov.hhs.fha.nhinc.mpilib.Address;
import gov.hhs.fha.nhinc.mpilib.Identifier;
import gov.hhs.fha.nhinc.mpilib.Patient;
import gov.hhs.fha.nhinc.mpilib.Patients;
import gov.hhs.fha.nhinc.mpilib.PersonName;
import gov.hhs.fha.nhinc.mpilib.PhoneNumber;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.EnExplicitPrefix;
import org.hl7.v3.EnExplicitSuffix;
import org.hl7.v3.II;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAMT201310UV02Person;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dunnek
 */
public class HL7Parser201306Test {

    //CHECKSTYLE:OFF
    private static class PatientName {
        public String FirstName = "";
        public String LastName = "";
        public String MiddleName = "";
        public String Title = "";
        public String Suffix = "";
    }

    /**
     * Public constructor for the test class.
     */
    public HL7Parser201306Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    //CHECKSTYLE:ON
    /**
     * Test of BuildMessageFromMpiPatient method, of class HL7Parser201306.
     */
    @Test
    public void testBuildMessageFromMpiPatient() {
        System.out.println("BuildMessageFromMpiPatient");
        II subjectId = new II();
        subjectId.setRoot("2.16.840.1.113883.3.200");
        subjectId.setExtension("1234");

        String firstExpectedName = "Joe";
        String lastExpectedName = "Smith";
        String middleExpectedName = "Middle";
        String expectedTitle = "Title";
        String expectedSuffix = "Suffix";

        PRPAIN201305UV02 query = TestHelper.build201305(firstExpectedName, lastExpectedName, "M", "March 1, 1956",
                subjectId);

        Identifier patId = new Identifier();
        patId.setId("1234");
        patId.setOrganizationId("2.16.840.1.113883.3.200");
        Patient patient = TestHelper.createMpiPatient(firstExpectedName, lastExpectedName, middleExpectedName, "M",
                "March 1, 1956", patId);

        patient.getNames().get(0).setSuffix(expectedSuffix);
        patient.getNames().get(0).setTitle(expectedTitle);

        Patients patients = new Patients();
        patients.add(patient);

        PRPAIN201306UV02 result = HL7Parser201306.buildMessageFromMpiPatient(patients, query);

        PNExplicit pnResult = result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                .getPatient().getPatientPerson().getValue().getName().get(0);

        PatientName patientName = extractName(pnResult);

        assertEquals(lastExpectedName, patientName.LastName);
        assertEquals(firstExpectedName, patientName.FirstName);
        assertEquals(middleExpectedName, patientName.MiddleName);
        assertEquals(expectedTitle, patientName.Title);
        assertEquals(expectedSuffix, patientName.Suffix);

    }

    /**
     * Test Build Message From MPI Patient Phone Number.
     */
    @Test
    public void testBuildMessageFromMpiPatientPhoneNumber() {
        System.out.println("testBuildMessageFromMpiPatient_PhoneNumber");
        II subjectId = new II();
        subjectId.setRoot("2.16.840.1.113883.3.200");
        subjectId.setExtension("1234");

        String firstExpectedName = "Joe";
        String lastExpectedName = "Smith";
        String middleExpectedName = "Middle";
        String expectedTitle = "Title";
        String expectedSuffix = "Suffix";

        PRPAIN201305UV02 query = TestHelper.build201305(firstExpectedName, lastExpectedName, "M", "March 1, 1956",
                subjectId);

        Identifier patId = new Identifier();
        patId.setId("1234");
        patId.setOrganizationId("2.16.840.1.113883.3.200");
        Patient patient = TestHelper.createMpiPatient(firstExpectedName, lastExpectedName, middleExpectedName, "M",
                "March 1, 1956", patId);

        patient.getNames().get(0).setSuffix(expectedSuffix);
        patient.getNames().get(0).setTitle(expectedTitle);

        patient.getPhoneNumbers().add(new PhoneNumber("7031231234"));

        Patients patients = new Patients();
        patients.add(patient);

        PRPAIN201306UV02 result = HL7Parser201306.buildMessageFromMpiPatient(patients, query);

        PRPAMT201310UV02Person person = result.getControlActProcess().getSubject().get(0).getRegistrationEvent()
                .getSubject1().getPatient().getPatientPerson().getValue();

        assertEquals(1, person.getTelecom().size());
        assertEquals("7031231234", person.getTelecom().get(0).getValue());

    }

    /**
     * Test method for building a message from mpi when a patient has multiple phone numbers.
     */
    @Test
    public void testBuildMessageFromMpiPatientMultiPhoneNumber() {
        System.out.println("testBuildMessageFromMpiPatient_MultiPhoneNumber");
        II subjectId = new II();
        subjectId.setRoot("2.16.840.1.113883.3.200");
        subjectId.setExtension("1234");

        String firstExpectedName = "Joe";
        String lastExpectedName = "Smith";
        String middleExpectedName = "Middle";
        String expectedTitle = "Title";
        String expectedSuffix = "Suffix";

        PRPAIN201305UV02 query = TestHelper.build201305(firstExpectedName, lastExpectedName, "M", "March 1, 1956",
                subjectId);

        Identifier patId = new Identifier();
        patId.setId("1234");
        patId.setOrganizationId("2.16.840.1.113883.3.200");
        Patient patient = TestHelper.createMpiPatient(firstExpectedName, lastExpectedName, middleExpectedName, "M",
                "March 1, 1956", patId);

        patient.getNames().get(0).setSuffix(expectedSuffix);
        patient.getNames().get(0).setTitle(expectedTitle);

        patient.getPhoneNumbers().add(new PhoneNumber("7031231234"));
        patient.getPhoneNumbers().add(new PhoneNumber("2021231234"));

        Patients patients = new Patients();
        patients.add(patient);

        PRPAIN201306UV02 result = HL7Parser201306.buildMessageFromMpiPatient(patients, query);
        // TODO review the generated test code and remove the default call to fail.

        PRPAMT201310UV02Person person = result.getControlActProcess().getSubject().get(0).getRegistrationEvent()
                .getSubject1().getPatient().getPatientPerson().getValue();

        assertEquals(2, person.getTelecom().size());
        assertEquals("7031231234", person.getTelecom().get(0).getValue());
        assertEquals("2021231234", person.getTelecom().get(1).getValue());

    }

    /**
     *
     */
    @Test
    public void testBuildMessageFromMpiPatientAddress() {
        System.out.println("testBuildMessageFromMpiPatient_PhoneNumber");
        II subjectId = new II();
        subjectId.setRoot("2.16.840.1.113883.3.200");
        subjectId.setExtension("1234");

        String firstExpectedName = "Joe";
        String lastExpectedName = "Smith";
        String middleExpectedName = "Middle";
        String expectedTitle = "Title";
        String expectedSuffix = "Suffix";

        PRPAIN201305UV02 query = TestHelper.build201305(firstExpectedName, lastExpectedName, "M", "March 1, 1956",
                subjectId);

        Identifier patId = new Identifier();
        patId.setId("1234");
        patId.setOrganizationId("2.16.840.1.113883.3.200");
        Patient patient = TestHelper.createMpiPatient(firstExpectedName, lastExpectedName, middleExpectedName, "M",
                "March 1, 1956", patId);

        patient.getNames().get(0).setSuffix(expectedSuffix);
        patient.getNames().get(0).setTitle(expectedTitle);

        Address add = new Address();
        add.setCity("Chantilly");
        add.setState("VA");
        add.setStreet1("5155 Parkstone Drive");
        add.setStreet2("Att:Developer");
        add.setZip("20151");
        patient.getAddresses().add(add);

        Patients patients = new Patients();
        patients.add(patient);

        PRPAIN201306UV02 result = HL7Parser201306.buildMessageFromMpiPatient(patients, query);
        // TODO review the generated test code and remove the default call to fail.

        PRPAMT201310UV02Person person = result.getControlActProcess().getSubject().get(0).getRegistrationEvent()
                .getSubject1().getPatient().getPatientPerson().getValue();

        assertEquals(1, person.getAddr().size());

    }

    /**
     *
     */
    @Test
    public void testBuildMessageFromMpiPatientMultiAddress() {
        System.out.println("testBuildMessageFromMpiPatient_MultiAddress");
        II subjectId = new II();
        subjectId.setRoot("2.16.840.1.113883.3.200");
        subjectId.setExtension("1234");

        String firstExpectedName = "Joe";
        String lastExpectedName = "Smith";
        String middleExpectedName = "Middle";
        String expectedTitle = "Title";
        String expectedSuffix = "Suffix";

        PRPAIN201305UV02 query = TestHelper.build201305(firstExpectedName, lastExpectedName, "M", "March 1, 1956",
                subjectId);

        Identifier patId = new Identifier();
        patId.setId("1234");
        patId.setOrganizationId("2.16.840.1.113883.3.200");
        Patient patient = TestHelper.createMpiPatient(firstExpectedName, lastExpectedName, middleExpectedName, "M",
                "March 1, 1956", patId);

        patient.getNames().get(0).setSuffix(expectedSuffix);
        patient.getNames().get(0).setTitle(expectedTitle);

        Address add = new Address();
        add.setCity("Chantilly");
        add.setState("VA");
        add.setStreet1("5155 Parkstone Drive");
        add.setStreet2("Att:Developer");
        add.setZip("20151");

        Address add2 = new Address();
        add2.setCity("Melbourne");
        add2.setState("FL");
        add2.setStreet1("1025 West NASA Boulevard");
        add2.setStreet2("Att:Developer");
        add2.setZip("32919-0001");

        patient.getAddresses().add(add);
        patient.getAddresses().add(add2);

        Patients patients = new Patients();
        patients.add(patient);

        PRPAIN201306UV02 result = HL7Parser201306.buildMessageFromMpiPatient(patients, query);
        // TODO review the generated test code and remove the default call to fail.

        PRPAMT201310UV02Person person = result.getControlActProcess().getSubject().get(0).getRegistrationEvent()
                .getSubject1().getPatient().getPatientPerson().getValue();

        assertEquals(2, person.getAddr().size());

    }

    /**
     *
     */
    @Test
    public void testBuildMessageFromMpiPatientMultiNames() {
        System.out.println("BuildMessageFromMpiPatient");
        II subjectId = new II();
        subjectId.setRoot("2.16.840.1.113883.3.200");
        subjectId.setExtension("1234");

        String firstExpectedName = "Joe";
        String lastExpectedName = "Smith";
        String middleExpectedName = "Middle";
        String expectedTitle = "Title";
        String expectedSuffix = "Suffix";

        PRPAIN201305UV02 query = TestHelper.build201305(firstExpectedName, lastExpectedName, "M", "March 1, 1956",
                subjectId);

        Identifier patId = new Identifier();
        patId.setId("1234");
        patId.setOrganizationId("2.16.840.1.113883.3.200");
        Patient patient = TestHelper.createMpiPatient(firstExpectedName, lastExpectedName, middleExpectedName, "M",
                "March 1, 1956", patId, expectedTitle, expectedSuffix);

        patient.getNames().add(new PersonName("lastname", "firstName"));

        Patients patients = new Patients();
        patients.add(patient);

        PRPAIN201306UV02 result = HL7Parser201306.buildMessageFromMpiPatient(patients, query);

        assertEquals(2, result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                .getPatient().getPatientPerson().getValue().getName().size());

        PNExplicit pnResult = result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                .getPatient().getPatientPerson().getValue().getName().get(0);
        PNExplicit pnResult2 = result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                .getPatient().getPatientPerson().getValue().getName().get(1);

        PatientName patientName = extractName(pnResult);
        PatientName patientName2 = extractName(pnResult2);

        assertEquals(lastExpectedName, patientName.LastName);
        assertEquals(firstExpectedName, patientName.FirstName);
        assertEquals(middleExpectedName, patientName.MiddleName);
        assertEquals(expectedTitle, patientName.Title);
        assertEquals(expectedSuffix, patientName.Suffix);

        assertEquals("lastname", patientName2.LastName);
        assertEquals("firstName", patientName2.FirstName);

    }

    private static PatientName extractName(PNExplicit name) {
        String nameString;
        Boolean hasName = false;
        PatientName result = new PatientName();
        List<Serializable> choice = name.getContent();
        Iterator<Serializable> iterSerialObjects = choice.iterator();

        EnExplicitFamily familyName = new EnExplicitFamily();
        EnExplicitGiven givenName = new EnExplicitGiven();

        while (iterSerialObjects.hasNext()) {
            Serializable contentItem = iterSerialObjects.next();

            if (contentItem instanceof JAXBElement) {
                JAXBElement<?> oJAXBElement = (JAXBElement<?>) contentItem;
                if (oJAXBElement.getValue() instanceof EnExplicitFamily) {
                    familyName = (EnExplicitFamily) oJAXBElement.getValue();
                    result.LastName = familyName.getContent();
                    hasName = true;
                } else if (oJAXBElement.getValue() instanceof EnExplicitGiven) {
                    givenName = (EnExplicitGiven) oJAXBElement.getValue();
                    if (result.FirstName == "") {
                        result.FirstName = givenName.getContent();
                    } else {
                        result.MiddleName = givenName.getContent();
                    }
                    hasName = true;
                } else if (oJAXBElement.getValue() instanceof EnExplicitPrefix) {
                    EnExplicitPrefix prefix = (EnExplicitPrefix) oJAXBElement.getValue();
                    result.Title = prefix.getContent();
                } else if (oJAXBElement.getValue() instanceof EnExplicitSuffix) {
                    EnExplicitSuffix suffix = (EnExplicitSuffix) oJAXBElement.getValue();
                    result.Suffix = suffix.getContent();
                }
            }
        }

        if (hasName) {
            nameString = familyName.getContent();
            System.out.println(nameString);
        }

        return result;
    }

}
