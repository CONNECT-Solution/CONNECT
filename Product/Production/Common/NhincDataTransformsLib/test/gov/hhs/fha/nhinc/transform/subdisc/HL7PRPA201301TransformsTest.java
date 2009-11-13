/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.subdisc;

import javax.xml.bind.JAXBElement;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;
import org.hl7.v3.TELExplicit;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;

import org.hl7.v3.PRPAIN201305UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01ControlActProcess;

/**
 *
 * @author Jon Hoppesch
 */
public class HL7PRPA201301TransformsTest {

    private static Log log = LogFactory.getLog(HL7PRPA201301TransformsTest.class);
    
    private String localDeviceId = "2.16.840.1.113883.3.200.1";
    private String senderOID = "2.16.840.1.113883.3.200";
    private String receiverOID = "2.16.840.1.113883.3.184";
    private String patientFirstName = "Thomas";
    private String patientLastName = "Kirtland";
    private String gender = "M";
    private String birthTime = "19760307";
    private String ssn = "123456789";
    private String patId = "1234";
    

    public HL7PRPA201301TransformsTest() {
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
     * Test of createPRPA201301 method, of class HL7PRPA201301Transforms.
     */
    @Test
    public void testCreatePRPA201301() {
        log.info("testCreatePRPA201301");
        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson(patientFirstName, patientLastName, gender, birthTime, ssn);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, patId, localDeviceId);
        
        PRPAIN201301UV02 result = HL7PRPA201301Transforms.createPRPA201301(patient, localDeviceId, senderOID, receiverOID);
        
        TestHelper.assertReceiverIdEquals(receiverOID, result);
        TestHelper.assertSenderIdEquals(senderOID, result);
        TestHelper.assertPatientIdEquals(patId, localDeviceId, result);
        TestHelper.assertSsnEquals(ssn, result);
        TestHelper.assertBirthTimeEquals(birthTime, result);
        TestHelper.assertPatientNameEquals(patientFirstName, patientLastName, result);
        TestHelper.assertGenderEquals(gender, result);
    }
    
    /**
     * Test of createPRPA201301 method, of class HL7PRPA201301Transforms.
     */
    @Test
    public void testCreatePRPA201301_NoBirthTime() {
        log.info("testCreatePRPA201301_NoBirthTime");
        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson(patientFirstName, patientLastName, gender, null, ssn);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, patId, localDeviceId);
        
        PRPAIN201301UV02 result = HL7PRPA201301Transforms.createPRPA201301(patient, localDeviceId, senderOID, receiverOID);
        
        TestHelper.assertReceiverIdEquals(receiverOID, result);
        TestHelper.assertSenderIdEquals(senderOID, result);
        TestHelper.assertPatientIdEquals(patId, localDeviceId, result);
        TestHelper.assertSsnEquals(ssn, result);
        TestHelper.assertBirthTimeNull(result);
        TestHelper.assertPatientNameEquals(patientFirstName, patientLastName, result);
        TestHelper.assertGenderEquals(gender, result);
    }
    
   /**
     * Test of createPRPA201301 method, of class HL7PRPA201301Transforms.
     */
    @Test
    public void testCreatePRPA201301_NoSSN() {
        log.info("testCreatePRPA201301_NoSSN");
        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson(patientFirstName, patientLastName, gender, birthTime, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, patId, localDeviceId);
        
        PRPAIN201301UV02 result = HL7PRPA201301Transforms.createPRPA201301(patient, localDeviceId, senderOID, receiverOID);
        
        TestHelper.assertReceiverIdEquals(receiverOID, result);
        TestHelper.assertSenderIdEquals(senderOID, result);
        TestHelper.assertPatientIdEquals(patId, localDeviceId, result);
        TestHelper.assertSsnNull(result);
        TestHelper.assertBirthTimeEquals(birthTime, result);
        TestHelper.assertPatientNameEquals(patientFirstName, patientLastName, result);
        TestHelper.assertGenderEquals(gender, result);
    }

   /**
     * Test of createPRPA201301 method, of class HL7PRPA201301Transforms.
     */
    @Test
    public void testCreatePRPA201301_NoGender() {
        log.info("testCreatePRPA201301_NoGender");
        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson(patientFirstName, patientLastName, null, birthTime, ssn);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, patId, localDeviceId);
        
        PRPAIN201301UV02 result = HL7PRPA201301Transforms.createPRPA201301(patient, localDeviceId, senderOID, receiverOID);
        
        TestHelper.assertReceiverIdEquals(receiverOID, result);
        TestHelper.assertSenderIdEquals(senderOID, result);
        TestHelper.assertPatientIdEquals(patId, localDeviceId, result);
        TestHelper.assertSsnEquals(ssn, result);
        TestHelper.assertBirthTimeEquals(birthTime, result);
        TestHelper.assertPatientNameEquals(patientFirstName, patientLastName, result);
        TestHelper.assertGenderNull(result);
    }
    @Test
    public void testCreatePRPA201301Headers()
    {
        System.out.println("testCreatePRPA201301Headers");
        PRPAIN201301UV02 result = HL7PRPA201301Transforms.createPRPA201301Headers("123");

        assertEquals("123", result.getId().getRoot());
        assertEquals("T", result.getProcessingCode().getCode());
        assertEquals("T", result.getProcessingModeCode().getCode());
        assertEquals("AL", result.getAcceptAckCode().getCode());

    }
    @Test
    public void testCreat201301From201306_Null()
    {
        System.out.println("testCreat201301From201306_Null");
        PRPAIN201306UV02 value = null;

        PRPAIN201301UV02 result = HL7PRPA201301Transforms.createPRPA201301(value,"123");

        assertEquals(null,result);

    }
    @Test
    public void testCreat201301From201305_Null()
    {
        System.out.println("testCreat201301From201305_Null");
        PRPAIN201305UV02 value = null;

        PRPAIN201301UV02 result = HL7PRPA201301Transforms.createPRPA201301(value,"123");

        assertEquals(null,result);

    }
    @Test
    public void testCreat201301From201305_Headers()
    {
        System.out.println("testCreat201301From201305_Headers");
        PRPAIN201305UV02 value = new PRPAIN201305UV02();


        PRPAIN201301UV02 result = HL7PRPA201301Transforms.createPRPA201301(value,"123");

        assertEquals(HL7Constants.ITS_VERSION,result.getITSVersion());
        assertEquals(HL7Constants.INTERACTION_ID_ROOT,result.getInteractionId().getRoot());
        assertEquals("AL", result.getAcceptAckCode().getCode());
        assertEquals("T", result.getProcessingModeCode().getCode());
        assertEquals("T", result.getProcessingCode().getCode());
        assertEquals("123", result.getId().getRoot());

    }
    @Test
    public void testCreat201301From201306_Headers()
    {
        System.out.println("testCreat201301From201306_Headers");
        PRPAIN201306UV02 value = new PRPAIN201306UV02();


        PRPAIN201301UV02 result = HL7PRPA201301Transforms.createPRPA201301(value,"123");

        assertEquals(HL7Constants.ITS_VERSION,result.getITSVersion());
        assertEquals(HL7Constants.INTERACTION_ID_ROOT,result.getInteractionId().getRoot());
        assertEquals("AL", result.getAcceptAckCode().getCode());
        assertEquals("T", result.getProcessingModeCode().getCode());
        assertEquals("T", result.getProcessingCode().getCode());
        assertEquals("123", result.getId().getRoot());

    }
    @Test
     public void testCreat201301From201306ControlActProcess_Gender()
    {
        System.out.println("testCreat201301From201306ControlActProcess");
        String gender = "MALE";
        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess value = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();
        PRPAMT201306UV02ParameterList paramList = new PRPAMT201306UV02ParameterList();
        PRPAMT201306UV02QueryByParameter params = new PRPAMT201306UV02QueryByParameter();

        paramList.getLivingSubjectAdministrativeGender().add(HL7QueryParamsTransforms.createGender(gender));
        
        params.setParameterList(paramList);

        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
     
        value.setQueryByParameter(new JAXBElement<PRPAMT201306UV02QueryByParameter>(xmlqname, PRPAMT201306UV02QueryByParameter.class, params));
  
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess result = HL7PRPA201301Transforms.copyControlActProcess(value, "123");

        assertEquals(true, result != null);

        String actualGender = result.getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAdministrativeGenderCode().getCode();

        assertEquals(gender, actualGender);
    }
    @Test
     public void testCreat201301From201306ControlActProcess_Telcom()
    {
        System.out.println("testCreat201301From201306ControlActProcess");
        String phoneNumber = "7031231234";
        TELExplicit phone = HL7DataTransformHelper.createTELExplicit(phoneNumber);
        List<TELExplicit> phoneList = new java.util.ArrayList<TELExplicit>();
        phoneList.add(phone);
        
        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess value = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();
        PRPAMT201306UV02ParameterList paramList = new PRPAMT201306UV02ParameterList();
        PRPAMT201306UV02QueryByParameter params = new PRPAMT201306UV02QueryByParameter();

        paramList.getPatientTelecom().add(null);
        paramList.getPatientTelecom().add(HL7QueryParamsTransforms.createTelecom(phoneList));

        params.setParameterList(paramList);

        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");

        value.setQueryByParameter(new JAXBElement<PRPAMT201306UV02QueryByParameter>(xmlqname, PRPAMT201306UV02QueryByParameter.class, params));

        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess result = HL7PRPA201301Transforms.copyControlActProcess(value, "123");

        assertEquals(true, result != null);

        assertEquals(1,result.getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getTelecom().size());
        assertEquals(phoneNumber,result.getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getTelecom().get(0).getValue());

  
    }

}