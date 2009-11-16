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
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201302UV02OtherIDs;
import org.hl7.v3.PRPAMT201302UV02Patient;
import org.hl7.v3.PRPAMT201302UV02BirthPlace;
import org.hl7.v3.PRPAMT201301UV02BirthPlace;
import org.hl7.v3.PRPAMT201302UV02Patient;
import org.hl7.v3.PRPAMT201302UV02Person;
import org.hl7.v3.PRPAMT201302UV02PatientPatientPerson;
import org.hl7.v3.PRPAMT201310UV02OtherIDs;
import org.hl7.v3.PRPAMT201310UV02Patient;
import org.hl7.v3.PRPAMT201310UV02Person;
import org.hl7.v3.PRPAMT201306UV02PatientAddress;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectBirthTime;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectName;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
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
    public void testcreatePRPAMT201301UVPatient_Null() {
        System.out.println("testcreatePRPAMT201301UVPatient_Null");
        PRPAMT201302UV02Patient patient = null;
        PRPAMT201301UV02Patient expResult = null;
        PRPAMT201301UV02Patient result = HL7PatientTransforms.createPRPAMT201301UVPatient(patient);
        assertEquals(expResult, result);
    }
    @Test
    public void testcreatePRPAMT201301UVPatient_addr() {
        System.out.println("testcreatePRPAMT201301UVPatient_addr");
        PRPAMT201302UV02Patient patient = null;

        String street = "123 Main Street";
        String city = "Fairfax";
        String state = "VA";
        String zip = "20120";

        patient = new PRPAMT201302UV02Patient();

        ADExplicit addr = HL7DataTransformHelper.CreateADExplicit(street, city, state, zip);

        patient.getAddr().add(addr);
        
        PRPAMT201301UV02Patient result = HL7PatientTransforms.createPRPAMT201301UVPatient(patient);
        assertEquals(addr, result.getAddr().get(0));
    }
    @Test
    public void testcreatePRPAMT201301UVPatient_phone() {
        System.out.println("testcreatePRPAMT201301UVPatient_phone");
        PRPAMT201302UV02Patient patient = null;

        String phoneNumber = "7031231234";
        patient = new PRPAMT201302UV02Patient();

        
        TELExplicit phone = HL7DataTransformHelper.createTELExplicit(phoneNumber);
        patient.getTelecom().add(phone);

        PRPAMT201301UV02Patient result = HL7PatientTransforms.createPRPAMT201301UVPatient(patient);
        assertEquals(phone, result.getTelecom().get(0));
    }
    @Test
    public void testcreatePRPAMT201301UVPatient_phoneMulti() {
        System.out.println("testcreatePRPAMT201301UVPatient_phoneMulti");
        PRPAMT201302UV02Patient patient = null;

        String phoneNumber = "7031231234";
        String phoneNumber2 = "2021231234";
        patient = new PRPAMT201302UV02Patient();


        TELExplicit phone = HL7DataTransformHelper.createTELExplicit(phoneNumber);
        TELExplicit phone2 = HL7DataTransformHelper.createTELExplicit(phoneNumber2);
        
        patient.getTelecom().add(phone);
        patient.getTelecom().add(phone2);

        PRPAMT201301UV02Patient result = HL7PatientTransforms.createPRPAMT201301UVPatient(patient);

        assertEquals(2, result.getTelecom().size());
        assertEquals(phone, result.getTelecom().get(0));
        assertEquals(phone2, result.getTelecom().get(1));
    }
    @Test
    public void testcreatePRPAMT201301UVPatient_Gender() {
        System.out.println("testcreatePRPAMT201301UVPatient_Gender");
        PRPAMT201302UV02Patient patient = null;
        PRPAMT201302UV02PatientPatientPerson person = null;


        String gender = "MALE";
        CE genderCE = HL7DataTransformHelper.CEFactory(gender);
        
        patient = new PRPAMT201302UV02Patient();
        person = new PRPAMT201302UV02PatientPatientPerson();

        person.setAdministrativeGenderCode(genderCE);

        patient.setPatientPerson(create201302PersonElement(person));

        PRPAMT201301UV02Patient result = HL7PatientTransforms.createPRPAMT201301UVPatient(patient);

        assertEquals(genderCE, result.getPatientPerson().getValue().getAdministrativeGenderCode());
        
    }

    @Test
    public void testCopy201306To201301_Gender()
    {
        PRPAMT201306UV02ParameterList value = new PRPAMT201306UV02ParameterList();
        String gender = "MALE";
        value.getLivingSubjectAdministrativeGender().add(HL7QueryParamsTransforms.createGender(gender));

        PRPAMT201301UV02Patient result = HL7PatientTransforms.create201301Patient(value);

        assertEquals(gender, result.getPatientPerson().getValue().getAdministrativeGenderCode().getCode());

    }
    @Test
    public void testCopy201306To201301_Telcom()
    {
        PRPAMT201306UV02ParameterList value = new PRPAMT201306UV02ParameterList();
        String phoneNumber = "7031231234";
        TELExplicit phone = HL7DataTransformHelper.createTELExplicit(phoneNumber);
        List<TELExplicit> phoneList = new java.util.ArrayList<TELExplicit>();
        phoneList.add(phone);

        value.getPatientTelecom().add(HL7QueryParamsTransforms.createTelecom(phoneList));

        PRPAMT201301UV02Patient result = HL7PatientTransforms.create201301Patient(value);

        assertEquals(1,result.getPatientPerson().getValue().getTelecom().size());
        assertEquals(phoneNumber,result.getPatientPerson().getValue().getTelecom().get(0).getValue());

    }
    @Test
    public void testCopy201306To201301_addr()
    {
        PRPAMT201306UV02ParameterList value = new PRPAMT201306UV02ParameterList();
        PRPAMT201306UV02PatientAddress patAddr = new PRPAMT201306UV02PatientAddress();

        String street = "123 Main Street";
        String city = "Fairfax";
        String state = "VA";
        String zip = "20120";

        ADExplicit addrObject = HL7DataTransformHelper.CreateADExplicit(street, city, state, zip);

        patAddr.getValue().add(addrObject);

        value.getPatientAddress().add(patAddr);

        PRPAMT201301UV02Patient result = HL7PatientTransforms.create201301Patient(value);

        assertEquals(1, result.getPatientPerson().getValue().getAddr().size());
        assertEquals(addrObject, result.getPatientPerson().getValue().getAddr().get(0));
    }
    @Test
    public void testcreatePRPAMT201301UVPatientPerson_addr() {
        System.out.println("testcreatePRPAMT201301UVPatientPerson_addr");
        PRPAMT201302UV02Patient patient = null;
        PRPAMT201302UV02PatientPatientPerson person = null;


        String street = "123 Main Street";
        String city = "Fairfax";
        String state = "VA";
        String zip = "20120";

        patient = new PRPAMT201302UV02Patient();

        ADExplicit addr = HL7DataTransformHelper.CreateADExplicit(street, city, state, zip);

        patient.getAddr().add(addr);


        patient = new PRPAMT201302UV02Patient();
        person = new PRPAMT201302UV02PatientPatientPerson();

        person.getAddr().add(addr);

        patient.setPatientPerson(create201302PersonElement(person));

        PRPAMT201301UV02Patient result = HL7PatientTransforms.createPRPAMT201301UVPatient(patient);

        assertEquals(addr, result.getPatientPerson().getValue().getAddr().get(0));

    }


    @Test
    public void testcreatePRPAMT201301UVPatientPerson_phone() {
        System.out.println("testcreatePRPAMT201301UVPatientPerson_addr");
        PRPAMT201302UV02Patient patient = null;
        PRPAMT201302UV02PatientPatientPerson person = null;


        String phoneNumber = "7031231234";
        patient = new PRPAMT201302UV02Patient();


        TELExplicit phone = HL7DataTransformHelper.createTELExplicit(phoneNumber);
        patient.getTelecom().add(phone);

        patient = new PRPAMT201302UV02Patient();


        patient = new PRPAMT201302UV02Patient();
        person = new PRPAMT201302UV02PatientPatientPerson();

        person.getTelecom().add(phone);

        patient.setPatientPerson(create201302PersonElement(person));

        PRPAMT201301UV02Patient result = HL7PatientTransforms.createPRPAMT201301UVPatient(patient);

        assertEquals(phone, result.getPatientPerson().getValue().getTelecom().get(0));

    }

    @Test
    public void testCreatePRPAMT201301UVBirthPlace_Null() {
        System.out.println("createPRPAMT201301UVBirthPlace");
        JAXBElement<PRPAMT201302UV02BirthPlace> value = null;
        JAXBElement<PRPAMT201301UV02BirthPlace> expResult = null;
        JAXBElement<PRPAMT201301UV02BirthPlace> result = HL7PatientTransforms.createPRPAMT201301UVBirthPlace(value);
        assertEquals(expResult, result);
    }

    @Test
    public void testCreateBirthTime_Null()
    {
        System.out.println("begin testCreateBirthTime_Null");
        TSExplicit result = HL7PatientTransforms.createBirthTime(null);

        assertEquals(null, result);
    }
    @Test
    public void testCreateBirthTime_PRPAMT201306UV02LivingSubjectBirthTime()
    {
        System.out.println("begin testCreateBirthTime_PRPAMT201306UV02LivingSubjectBirthTime");
        String birthTime = "19000101";
        PRPAMT201306UV02LivingSubjectBirthTime subjBirthTime = HL7QueryParamsTransforms.createBirthTime(birthTime);
        TSExplicit result = HL7PatientTransforms.createBirthTime(subjBirthTime);

        assertEquals(birthTime, result.getValue());
    }

    private static JAXBElement<PRPAMT201302UV02PatientPatientPerson> create201302PersonElement(PRPAMT201302UV02PatientPatientPerson person)
    {
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        return new JAXBElement<PRPAMT201302UV02PatientPatientPerson>(xmlqname, PRPAMT201302UV02PatientPatientPerson.class, person);
    }
}