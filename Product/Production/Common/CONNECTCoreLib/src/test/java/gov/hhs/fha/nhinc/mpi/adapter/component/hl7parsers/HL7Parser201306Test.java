/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.mpi.adapter.component.hl7parsers;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import gov.hhs.fha.nhinc.mpi.adapter.component.TestHelper;
import org.hl7.v3.*;
import gov.hhs.fha.nhinc.mpilib.*;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author dunnek
 */
public class HL7Parser201306Test {
    private static class PatientName
    {
        public String FirstName = "";
        public String LastName = "";
        public String MiddleName = "";
        public String Title = "";
        public String Suffix = "";
    }
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

        PRPAIN201305UV02 query = TestHelper.build201305(firstExpectedName,
                lastExpectedName, "M", "March 1, 1956", subjectId);

        Identifier patId = new Identifier();
        patId.setId("1234");
        patId.setOrganizationId("2.16.840.1.113883.3.200");
        Patient patient = TestHelper.createMpiPatient(firstExpectedName,
                lastExpectedName, middleExpectedName, "M", "March 1, 1956", patId);

        patient.getName().setSuffix(expectedSuffix);
        patient.getName().setTitle(expectedTitle);

        PRPAIN201306UV02 result = HL7Parser201306.BuildMessageFromMpiPatient(patient, query);
        // TODO review the generated test code and remove the default call to fail.

        PNExplicit pnResult = result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getName().get(0);

        PatientName patientName = extractName(pnResult);

        assertEquals(lastExpectedName,patientName.LastName);
        assertEquals(firstExpectedName, patientName.FirstName);
        assertEquals(middleExpectedName, patientName.MiddleName);
        assertEquals(expectedTitle, patientName.Title);
        assertEquals(expectedSuffix, patientName.Suffix);
        
    }

    @Test
    public void testBuildMessageFromMpiPatient_PhoneNumber() {
        System.out.println("testBuildMessageFromMpiPatient_PhoneNumber");
        II subjectId = new II();
        subjectId.setRoot("2.16.840.1.113883.3.200");
        subjectId.setExtension("1234");

        String firstExpectedName = "Joe";
        String lastExpectedName = "Smith";
        String middleExpectedName = "Middle";
        String expectedTitle = "Title";
        String expectedSuffix = "Suffix";

        PRPAIN201305UV02 query = TestHelper.build201305(firstExpectedName,
                lastExpectedName, "M", "March 1, 1956", subjectId);

        Identifier patId = new Identifier();
        patId.setId("1234");
        patId.setOrganizationId("2.16.840.1.113883.3.200");
        Patient patient = TestHelper.createMpiPatient(firstExpectedName,
                lastExpectedName, middleExpectedName, "M", "March 1, 1956", patId);

        patient.getName().setSuffix(expectedSuffix);
        patient.getName().setTitle(expectedTitle);

        patient.getPhoneNumbers().add(new PhoneNumber("7031231234"));
        
        PRPAIN201306UV02 result = HL7Parser201306.BuildMessageFromMpiPatient(patient, query);
        // TODO review the generated test code and remove the default call to fail.

        PRPAMT201310UV02Person person = result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue();


        assertEquals(1, person.getTelecom().size());
        assertEquals("7031231234", person.getTelecom().get(0).getValue());
        
    }
    @Test
    public void testBuildMessageFromMpiPatient_MultiPhoneNumber() {
        System.out.println("testBuildMessageFromMpiPatient_MultiPhoneNumber");
        II subjectId = new II();
        subjectId.setRoot("2.16.840.1.113883.3.200");
        subjectId.setExtension("1234");

        String firstExpectedName = "Joe";
        String lastExpectedName = "Smith";
        String middleExpectedName = "Middle";
        String expectedTitle = "Title";
        String expectedSuffix = "Suffix";

        PRPAIN201305UV02 query = TestHelper.build201305(firstExpectedName,
                lastExpectedName, "M", "March 1, 1956", subjectId);

        Identifier patId = new Identifier();
        patId.setId("1234");
        patId.setOrganizationId("2.16.840.1.113883.3.200");
        Patient patient = TestHelper.createMpiPatient(firstExpectedName,
                lastExpectedName, middleExpectedName, "M", "March 1, 1956", patId);

        patient.getName().setSuffix(expectedSuffix);
        patient.getName().setTitle(expectedTitle);

        patient.getPhoneNumbers().add(new PhoneNumber("7031231234"));
        patient.getPhoneNumbers().add(new PhoneNumber("2021231234"));

        PRPAIN201306UV02 result = HL7Parser201306.BuildMessageFromMpiPatient(patient, query);
        // TODO review the generated test code and remove the default call to fail.

        PRPAMT201310UV02Person person = result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue();


        assertEquals(2, person.getTelecom().size());
        assertEquals("7031231234", person.getTelecom().get(0).getValue());
        assertEquals("2021231234", person.getTelecom().get(1).getValue());

    }

    @Test
    public void testBuildMessageFromMpiPatient_Address() {
        System.out.println("testBuildMessageFromMpiPatient_PhoneNumber");
        II subjectId = new II();
        subjectId.setRoot("2.16.840.1.113883.3.200");
        subjectId.setExtension("1234");

        String firstExpectedName = "Joe";
        String lastExpectedName = "Smith";
        String middleExpectedName = "Middle";
        String expectedTitle = "Title";
        String expectedSuffix = "Suffix";

        PRPAIN201305UV02 query = TestHelper.build201305(firstExpectedName,
                lastExpectedName, "M", "March 1, 1956", subjectId);

        Identifier patId = new Identifier();
        patId.setId("1234");
        patId.setOrganizationId("2.16.840.1.113883.3.200");
        Patient patient = TestHelper.createMpiPatient(firstExpectedName,
                lastExpectedName, middleExpectedName, "M", "March 1, 1956", patId);

        patient.getName().setSuffix(expectedSuffix);
        patient.getName().setTitle(expectedTitle);

        Address add = new Address();
        add.setCity("Chantilly");
        add.setState("VA");
        add.setStreet1("5155 Parkstone Drive");
        add.setStreet2("Att:Developer");
        add.setZip("20151");
        patient.getAddresses().add(add);
        

        PRPAIN201306UV02 result = HL7Parser201306.BuildMessageFromMpiPatient(patient, query);
        // TODO review the generated test code and remove the default call to fail.

        PRPAMT201310UV02Person person = result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue();


        assertEquals(1, person.getAddr().size());


    }

    @Test
    public void testBuildMessageFromMpiPatient_MultiAddress() {
        System.out.println("testBuildMessageFromMpiPatient_MultiAddress");
        II subjectId = new II();
        subjectId.setRoot("2.16.840.1.113883.3.200");
        subjectId.setExtension("1234");

        String firstExpectedName = "Joe";
        String lastExpectedName = "Smith";
        String middleExpectedName = "Middle";
        String expectedTitle = "Title";
        String expectedSuffix = "Suffix";

        PRPAIN201305UV02 query = TestHelper.build201305(firstExpectedName,
                lastExpectedName, "M", "March 1, 1956", subjectId);

        Identifier patId = new Identifier();
        patId.setId("1234");
        patId.setOrganizationId("2.16.840.1.113883.3.200");
        Patient patient = TestHelper.createMpiPatient(firstExpectedName,
                lastExpectedName, middleExpectedName, "M", "March 1, 1956", patId);

        patient.getName().setSuffix(expectedSuffix);
        patient.getName().setTitle(expectedTitle);

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
        PRPAIN201306UV02 result = HL7Parser201306.BuildMessageFromMpiPatient(patient, query);
        // TODO review the generated test code and remove the default call to fail.

        PRPAMT201310UV02Person person = result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue();


        assertEquals(2, person.getAddr().size());


    }

    @Test
    public void testBuildMessageFromMpiPatient_MultiNames() {
        System.out.println("BuildMessageFromMpiPatient");
        II subjectId = new II();
        subjectId.setRoot("2.16.840.1.113883.3.200");
        subjectId.setExtension("1234");

        String firstExpectedName = "Joe";
        String lastExpectedName = "Smith";
        String middleExpectedName = "Middle";
        String expectedTitle = "Title";
        String expectedSuffix = "Suffix";

        PRPAIN201305UV02 query = TestHelper.build201305(firstExpectedName,
                lastExpectedName, "M", "March 1, 1956", subjectId);

        Identifier patId = new Identifier();
        patId.setId("1234");
        patId.setOrganizationId("2.16.840.1.113883.3.200");
        Patient patient = TestHelper.createMpiPatient(firstExpectedName,
                lastExpectedName, middleExpectedName, "M", "March 1, 1956", patId, expectedTitle, expectedSuffix);

        patient.getNames().add(new PersonName("lastname", "firstName"));


        PRPAIN201306UV02 result = HL7Parser201306.BuildMessageFromMpiPatient(patient, query);
        // TODO review the generated test code and remove the default call to fail.

        assertEquals(2, result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getName().size());

        PNExplicit pnResult = result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getName().get(0);
        PNExplicit pnResult2 = result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getName().get(1);

        PatientName patientName = extractName(pnResult);
        PatientName patientName2 = extractName(pnResult2);
        
        assertEquals(lastExpectedName,patientName.LastName);
        assertEquals(firstExpectedName, patientName.FirstName);
        assertEquals(middleExpectedName, patientName.MiddleName);
        assertEquals(expectedTitle, patientName.Title);
        assertEquals(expectedSuffix, patientName.Suffix);

        assertEquals("lastname", patientName2.LastName);
        assertEquals("firstName", patientName2.FirstName);

    }
    private static PatientName extractName (PNExplicit name) {
        String nameString = "";
        Boolean hasName = false;
        PatientName result = new PatientName();
        List<Serializable> choice = name.getContent();
        Iterator<Serializable> iterSerialObjects = choice.iterator();

        EnExplicitFamily familyName = new EnExplicitFamily();
        EnExplicitGiven givenName = new EnExplicitGiven();

        while (iterSerialObjects.hasNext()) {
            Serializable contentItem = iterSerialObjects.next();

            if (contentItem instanceof JAXBElement) {
                JAXBElement oJAXBElement = (JAXBElement) contentItem;
                if (oJAXBElement.getValue() instanceof EnExplicitFamily) {
                    familyName = (EnExplicitFamily) oJAXBElement.getValue();
                    result.LastName = familyName.getContent();
                    hasName = true;
                } else if (oJAXBElement.getValue() instanceof EnExplicitGiven) {
                    givenName = (EnExplicitGiven) oJAXBElement.getValue();
                    if(result.FirstName == "")
                    {
                        result.FirstName = givenName.getContent();  ;
                    }
                    else
                    {
                        result.MiddleName = givenName.getContent();  ;
                    }
                    hasName = true;
                }
                else if (oJAXBElement.getValue() instanceof EnExplicitPrefix) {
                    EnExplicitPrefix prefix = (EnExplicitPrefix) oJAXBElement.getValue();
                    result.Title = prefix.getContent();
                }else if (oJAXBElement.getValue() instanceof EnExplicitSuffix) {
                    EnExplicitSuffix suffix = (EnExplicitSuffix) oJAXBElement.getValue();
                    result.Suffix = suffix.getContent();
                }
            }
        }

        if (hasName == true) {
            nameString = familyName.getContent();
            System.out.println(nameString);
        }

        return result;
    }

}