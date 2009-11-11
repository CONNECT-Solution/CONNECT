/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.transform.subdisc;

import javax.xml.bind.JAXBElement;
import org.hl7.v3.PRPAIN201302UV02;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.PRPAMT201310UV02Patient;
import org.hl7.v3.PRPAMT201310UV02Person;

/**
 *
 * @author jhoppesc
 */
public class HL7PRPA201302TransformsTest {
    
    private static Log log = LogFactory.getLog(HL7PRPA201301TransformsTest.class);
    
    private String localDeviceId = "2.16.840.1.113883.3.166.4.2";
    private String remoteDeviceId = "2.16.840.1.113883.3.198.1";
    private String senderOID = "2.16.840.1.113883.3.166.4";
    private String receiverOID = "2.16.840.1.113883.3.198";
    private String patientFirstName = "Henry";
    private String patientLastName = "Washington";
    private String gender = "M";
    private String birthTime = "19440731";
    private String ssn = "777777777";
    private String localPatId = "9999";
    private String remotePatId = "8888";

    public HL7PRPA201302TransformsTest() {
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
     * Test of createPRPA201302 method, of class HL7PRPA201302Transforms.
     */
    @Test
    public void testCreatePRPA201302_From201301() {
        System.out.println("testCreatePRPA201302_From201301");
        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson(patientFirstName, patientLastName, gender, birthTime, ssn);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, localPatId, localDeviceId);
                        
        PRPAIN201302UV02 result = HL7PRPA201302Transforms.createPRPA201302(patient, remotePatId, remoteDeviceId, senderOID, receiverOID);

        TestHelper.assertReceiverIdEquals(receiverOID, result);
        TestHelper.assertSenderIdEquals(senderOID, result);
        TestHelper.assertPatientIdEquals(localPatId, localDeviceId, result);
        TestHelper.assertSsnEquals(ssn, result);
        TestHelper.assertBirthTimeEquals(birthTime, result);
        TestHelper.assertPatientNameEquals(patientFirstName, patientLastName, result);
        TestHelper.assertGenderEquals(gender, result);               
    }
    
    /**
     * Test of createPRPA201302 method, of class HL7PRPA201302Transforms.
     */
    @Test
    public void testCreatePRPA201302_From201301_NoBirthTime() {
        log.info("testCreatePRPA201302_From201301_NoBirthTime");
        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson(patientFirstName, patientLastName, gender, null, ssn);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, localPatId, localDeviceId);
        
        PRPAIN201302UV02 result = HL7PRPA201302Transforms.createPRPA201302(patient, remotePatId, remoteDeviceId, senderOID, receiverOID);
        
        TestHelper.assertReceiverIdEquals(receiverOID, result);
        TestHelper.assertSenderIdEquals(senderOID, result);
        TestHelper.assertPatientIdEquals(localPatId, localDeviceId, result);
        TestHelper.assertSsnEquals(ssn, result);
        TestHelper.assertBirthTimeNull(result);
        TestHelper.assertPatientNameEquals(patientFirstName, patientLastName, result);
        TestHelper.assertGenderEquals(gender, result);
    }
    
   /**
     * Test of createPRPA201302 method, of class HL7PRPA201302Transforms.
     */
    @Test
    public void testCreatePRPA201302_From201301_NoSSN() {
        log.info("testCreatePRPA201302_From201301_NoSSN");
        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson(patientFirstName, patientLastName, gender, birthTime, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, localPatId, localDeviceId);
        
        PRPAIN201302UV02 result = HL7PRPA201302Transforms.createPRPA201302(patient, remotePatId, remoteDeviceId, senderOID, receiverOID);
        
        TestHelper.assertReceiverIdEquals(receiverOID, result);
        TestHelper.assertSenderIdEquals(senderOID, result);
        TestHelper.assertPatientIdEquals(localPatId, localDeviceId, result);
        TestHelper.assertSsnNull(result);
        TestHelper.assertBirthTimeEquals(birthTime, result);
        TestHelper.assertPatientNameEquals(patientFirstName, patientLastName, result);
        TestHelper.assertGenderEquals(gender, result);
    }

   /**
     * Test of createPRPA201302 method, of class HL7PRPA201302Transforms.
     */
    @Test
    public void testCreatePRPA201302_From201301_NoGender() {
        log.info("testCreatePRPA201302_From201301_NoGender");
        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson(patientFirstName, patientLastName, null, birthTime, ssn);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, localPatId, localDeviceId);
        
        PRPAIN201302UV02 result = HL7PRPA201302Transforms.createPRPA201302(patient, remotePatId, remoteDeviceId, senderOID, receiverOID);
        
        TestHelper.assertReceiverIdEquals(receiverOID, result);
        TestHelper.assertSenderIdEquals(senderOID, result);
        TestHelper.assertPatientIdEquals(localPatId, localDeviceId, result);
        TestHelper.assertSsnEquals(ssn, result);
        TestHelper.assertBirthTimeEquals(birthTime, result);
        TestHelper.assertPatientNameEquals(patientFirstName, patientLastName, result);
        TestHelper.assertGenderNull(result);
    }
    
        /**
     * Test of createPRPA201302 method, of class HL7PRPA201302Transforms.
     */
    @Test
    public void testCreatePRPA201302_From201310() {
        System.out.println("testCreatePRPA201302_From201310");
        JAXBElement<PRPAMT201310UV02Person> person = HL7PatientTransforms.create201310PatientPerson(patientFirstName, patientLastName, gender, birthTime, ssn);
        PRPAMT201310UV02Patient patient = HL7PatientTransforms.create201310Patient(person, localPatId, localDeviceId);
                        
        PRPAIN201302UV02 result = HL7PRPA201302Transforms.createPRPA201302(patient, remotePatId, remoteDeviceId, senderOID, receiverOID);

        TestHelper.assertReceiverIdEquals(receiverOID, result);
        TestHelper.assertSenderIdEquals(senderOID, result);
        TestHelper.assertPatientIdEquals(localPatId, localDeviceId, result);
        TestHelper.assertSsnEquals(ssn, result);
        TestHelper.assertBirthTimeEquals(birthTime, result);
        TestHelper.assertPatientNameEquals(patientFirstName, patientLastName, result);
        TestHelper.assertGenderEquals(gender, result);               
    }
    
    /**
     * Test of createPRPA201302 method, of class HL7PRPA201302Transforms.
     */
    @Test
    public void testCreatePRPA201302_From201310_NoBirthTime() {
        log.info("testCreatePRPA201302_From201310_NoBirthTime");
        JAXBElement<PRPAMT201310UV02Person> person = HL7PatientTransforms.create201310PatientPerson(patientFirstName, patientLastName, gender, null, ssn);
        PRPAMT201310UV02Patient patient = HL7PatientTransforms.create201310Patient(person, localPatId, localDeviceId);
        
        PRPAIN201302UV02 result = HL7PRPA201302Transforms.createPRPA201302(patient, remotePatId, remoteDeviceId, senderOID, receiverOID);
        
        TestHelper.assertReceiverIdEquals(receiverOID, result);
        TestHelper.assertSenderIdEquals(senderOID, result);
        TestHelper.assertPatientIdEquals(localPatId, localDeviceId, result);
        TestHelper.assertSsnEquals(ssn, result);
        TestHelper.assertBirthTimeNull(result);
        TestHelper.assertPatientNameEquals(patientFirstName, patientLastName, result);
        TestHelper.assertGenderEquals(gender, result);
    }
    
   /**
     * Test of createPRPA201302 method, of class HL7PRPA201302Transforms.
     */
    @Test
    public void testCreatePRPA201302_From201310_NoSSN() {
        log.info("testCreatePRPA201302_From201310_NoSSN");
        JAXBElement<PRPAMT201310UV02Person> person = HL7PatientTransforms.create201310PatientPerson(patientFirstName, patientLastName, gender, birthTime, null);
        PRPAMT201310UV02Patient patient = HL7PatientTransforms.create201310Patient(person, localPatId, localDeviceId);
        
        PRPAIN201302UV02 result = HL7PRPA201302Transforms.createPRPA201302(patient, remotePatId, remoteDeviceId, senderOID, receiverOID);
        
        TestHelper.assertReceiverIdEquals(receiverOID, result);
        TestHelper.assertSenderIdEquals(senderOID, result);
        TestHelper.assertPatientIdEquals(localPatId, localDeviceId, result);
        TestHelper.assertSsnNull(result);
        TestHelper.assertBirthTimeEquals(birthTime, result);
        TestHelper.assertPatientNameEquals(patientFirstName, patientLastName, result);
        TestHelper.assertGenderEquals(gender, result);
    }

   /**
     * Test of createPRPA201302 method, of class HL7PRPA201302Transforms.
     */
    @Test
    public void testCreatePRPA201302_From201310_NoGender() {
        log.info("testCreatePRPA201302_From201310_NoGender");
        JAXBElement<PRPAMT201310UV02Person> person = HL7PatientTransforms.create201310PatientPerson(patientFirstName, patientLastName, null, birthTime, ssn);
        PRPAMT201310UV02Patient patient = HL7PatientTransforms.create201310Patient(person, localPatId, localDeviceId);
        
        PRPAIN201302UV02 result = HL7PRPA201302Transforms.createPRPA201302(patient, remotePatId, remoteDeviceId, senderOID, receiverOID);
        
        TestHelper.assertReceiverIdEquals(receiverOID, result);
        TestHelper.assertSenderIdEquals(senderOID, result);
        TestHelper.assertPatientIdEquals(localPatId, localDeviceId, result);
        TestHelper.assertSsnEquals(ssn, result);
        TestHelper.assertBirthTimeEquals(birthTime, result);
        TestHelper.assertPatientNameEquals(patientFirstName, patientLastName, result);
        TestHelper.assertGenderNull(result);
    }

}