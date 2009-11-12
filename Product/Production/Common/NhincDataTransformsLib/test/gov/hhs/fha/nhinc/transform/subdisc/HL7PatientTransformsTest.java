/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.transform.subdisc;

import java.util.List;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.CE;
import org.hl7.v3.II;
import org.hl7.v3.ADExplicit;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAMT201301UV02OtherIDs;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.PRPAMT201301UVPatient;
import org.hl7.v3.PRPAMT201302UV02OtherIDs;
import org.hl7.v3.PRPAMT201302UV02Patient;
import org.hl7.v3.PRPAMT201302UVBirthPlace;
import org.hl7.v3.PRPAMT201301UVBirthPlace;
import org.hl7.v3.PRPAMT201302UVPatient;
import org.hl7.v3.PRPAMT201302UVPerson;
import org.hl7.v3.PRPAMT201310UV02OtherIDs;
import org.hl7.v3.PRPAMT201310UV02Patient;
import org.hl7.v3.PRPAMT201310UV02Person;
import org.hl7.v3.TELExplicit;
import org.hl7.v3.TSExplicit;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dunnek
 */
public class HL7PatientTransformsTest {

    public HL7PatientTransformsTest() {
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

    @Test
    public void testCreatePRPAMT201301UVPatient_Null() {
        System.out.println("testCreatePRPAMT201301UVPatient_Null");
        PRPAMT201302UVPatient patient = null;
        PRPAMT201301UVPatient expResult = null;
        PRPAMT201301UVPatient result = HL7PatientTransforms.createPRPAMT201301UVPatient(patient);
        assertEquals(expResult, result);
    }
    @Test
    public void testCreatePRPAMT201301UVPatient_addr() {
        System.out.println("testCreatePRPAMT201301UVPatient_addr");
        PRPAMT201302UVPatient patient = null;

        String street = "123 Main Street";
        String city = "Fairfax";
        String state = "VA";
        String zip = "20120";

        patient = new PRPAMT201302UVPatient();

        ADExplicit addr = HL7DataTransformHelper.CreateADExplicit(street, city, state, zip);

        patient.getAddr().add(addr);
        
        PRPAMT201301UVPatient result = HL7PatientTransforms.createPRPAMT201301UVPatient(patient);
        assertEquals(addr, result.getAddr().get(0));
    }
    @Test
    public void testCreatePRPAMT201301UVPatient_phone() {
        System.out.println("testCreatePRPAMT201301UVPatient_phone");
        PRPAMT201302UVPatient patient = null;

        String phoneNumber = "7031231234";
        patient = new PRPAMT201302UVPatient();

        
        TELExplicit phone = HL7DataTransformHelper.createTELExplicit(phoneNumber);
        patient.getTelecom().add(phone);

        PRPAMT201301UVPatient result = HL7PatientTransforms.createPRPAMT201301UVPatient(patient);
        assertEquals(phone, result.getTelecom().get(0));
    }
    @Test
    public void testCreatePRPAMT201301UVPatient_phoneMulti() {
        System.out.println("testCreatePRPAMT201301UVPatient_phoneMulti");
        PRPAMT201302UVPatient patient = null;

        String phoneNumber = "7031231234";
        String phoneNumber2 = "2021231234";
        patient = new PRPAMT201302UVPatient();


        TELExplicit phone = HL7DataTransformHelper.createTELExplicit(phoneNumber);
        TELExplicit phone2 = HL7DataTransformHelper.createTELExplicit(phoneNumber2);
        
        patient.getTelecom().add(phone);
        patient.getTelecom().add(phone2);

        PRPAMT201301UVPatient result = HL7PatientTransforms.createPRPAMT201301UVPatient(patient);

        assertEquals(2, result.getTelecom().size());
        assertEquals(phone, result.getTelecom().get(0));
        assertEquals(phone2, result.getTelecom().get(1));
    }
    @Test
    public void testCreatePRPAMT201301UVPatient_Gender() {
        System.out.println("testCreatePRPAMT201301UVPatient_Gender");
        PRPAMT201302UVPatient patient = null;
        PRPAMT201302UVPerson person = null;


        String gender = "MALE";
        CE genderCE = HL7DataTransformHelper.CEFactory(gender);
        
        patient = new PRPAMT201302UVPatient();
        person = new PRPAMT201302UVPerson();

        person.setAdministrativeGenderCode(genderCE);



        patient.setPatientPerson(create201302PersonElement(person));

        PRPAMT201301UVPatient result = HL7PatientTransforms.createPRPAMT201301UVPatient(patient);

        assertEquals(genderCE, result.getPatientPerson().getValue().getAdministrativeGenderCode());
        
    }

    @Test
    public void testCreatePRPAMT201301UVPatientPerson_addr() {
        System.out.println("testCreatePRPAMT201301UVPatientPerson_addr");
        PRPAMT201302UVPatient patient = null;
        PRPAMT201302UVPerson person = null;


        String street = "123 Main Street";
        String city = "Fairfax";
        String state = "VA";
        String zip = "20120";

        patient = new PRPAMT201302UVPatient();

        ADExplicit addr = HL7DataTransformHelper.CreateADExplicit(street, city, state, zip);

        patient.getAddr().add(addr);


        patient = new PRPAMT201302UVPatient();
        person = new PRPAMT201302UVPerson();

        person.getAddr().add(addr);

        patient.setPatientPerson(create201302PersonElement(person));

        PRPAMT201301UVPatient result = HL7PatientTransforms.createPRPAMT201301UVPatient(patient);

        assertEquals(addr, result.getPatientPerson().getValue().getAddr().get(0));

    }


    @Test
    public void testCreatePRPAMT201301UVPatientPerson_phone() {
        System.out.println("testCreatePRPAMT201301UVPatientPerson_addr");
        PRPAMT201302UVPatient patient = null;
        PRPAMT201302UVPerson person = null;


        String phoneNumber = "7031231234";
        patient = new PRPAMT201302UVPatient();


        TELExplicit phone = HL7DataTransformHelper.createTELExplicit(phoneNumber);
        patient.getTelecom().add(phone);

        patient = new PRPAMT201302UVPatient();


        patient = new PRPAMT201302UVPatient();
        person = new PRPAMT201302UVPerson();

        person.getTelecom().add(phone);

        patient.setPatientPerson(create201302PersonElement(person));

        PRPAMT201301UVPatient result = HL7PatientTransforms.createPRPAMT201301UVPatient(patient);

        assertEquals(phone, result.getPatientPerson().getValue().getTelecom().get(0));

    }

    @Test
    public void testCreatePRPAMT201301UVBirthPlace_Null() {
        System.out.println("createPRPAMT201301UVBirthPlace");
        JAXBElement<PRPAMT201302UVBirthPlace> value = null;
        JAXBElement<PRPAMT201301UVBirthPlace> expResult = null;
        JAXBElement<PRPAMT201301UVBirthPlace> result = HL7PatientTransforms.createPRPAMT201301UVBirthPlace(value);
        assertEquals(expResult, result);
    }

    private static JAXBElement<PRPAMT201302UVPerson> create201302PersonElement(PRPAMT201302UVPerson person)
    {
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        return new JAXBElement<PRPAMT201302UVPerson>(xmlqname, PRPAMT201302UVPerson.class, person);
    }
}