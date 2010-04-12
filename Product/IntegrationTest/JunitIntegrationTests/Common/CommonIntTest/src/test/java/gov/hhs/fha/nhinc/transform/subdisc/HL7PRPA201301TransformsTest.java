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
import org.hl7.v3.ENExplicit;
import org.hl7.v3.II;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.TELExplicit;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.PRPAMT201310UV02Person;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectBirthTime;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectName;
import org.hl7.v3.PRPAIN201305UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01ControlActProcess;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01ControlActProcess;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;
import org.hl7.v3.XActMoodIntentEvent;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject2;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent;

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
    private String phoneNumber = "7031231234";

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
        PRPAIN201301UV02 result = HL7PRPA201301Transforms.createPRPA201301Headers(localDeviceId);

        assertEquals(localDeviceId, result.getId().getRoot());
        assertEquals("T", result.getProcessingCode().getCode());
        assertEquals("T", result.getProcessingModeCode().getCode());
        assertEquals("AL", result.getAcceptAckCode().getCode());

    }
    @Test
    public void testCreat201301From201306_Null()
    {
        System.out.println("testCreat201301From201306_Null");
        PRPAIN201306UV02 value = null;

        PRPAIN201301UV02 result = HL7PRPA201301Transforms.createPRPA201301(value,localDeviceId);

        assertEquals(null,result);

    }
    @Test
    public void testCreat201301From201305_Null()
    {
        System.out.println("testCreat201301From201305_Null");
        PRPAIN201305UV02 value = null;

        PRPAIN201301UV02 result = HL7PRPA201301Transforms.createPRPA201301(value,localDeviceId);

        assertEquals(null,result);

    }
    @Test
    public void testCreat201301From201305_Headers()
    {
        System.out.println("testCreat201301From201305_Headers");
        PRPAIN201305UV02 value = new PRPAIN201305UV02();


        PRPAIN201301UV02 result = HL7PRPA201301Transforms.createPRPA201301(value,localDeviceId);

        assert201301HeaderInfo(result);

    }
    @Test
    public void testCreat201301From201306_Headers()
    {
        System.out.println("testCreat201301From201306_Headers");
        PRPAIN201306UV02 value = new PRPAIN201306UV02();


        PRPAIN201301UV02 result = HL7PRPA201301Transforms.createPRPA201301(value,localDeviceId);

        assert201301HeaderInfo(result);


    }
    @Test
    public void testCreate201301from201306ControlActProcessParams_BirthTime()
    {
        PRPAMT201306UV02LivingSubjectBirthTime subjBirthTime;

        subjBirthTime = create201306_birthTime();

        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess value = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
        PRPAMT201306UV02ParameterList paramList = new PRPAMT201306UV02ParameterList();
        PRPAMT201306UV02QueryByParameter params = new PRPAMT201306UV02QueryByParameter();

        paramList.getLivingSubjectBirthTime().add(subjBirthTime);

        params.setParameterList(paramList);

        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");

        value.setQueryByParameter(new JAXBElement<PRPAMT201306UV02QueryByParameter>(xmlqname, PRPAMT201306UV02QueryByParameter.class, params));

        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess result = HL7PRPA201301Transforms.copyControlActProcess2(value, "123");

        assertNotNull(result);

        assertEquals(birthTime,result.getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getBirthTime().getValue());


    }

    @Test
     public void testCreat201301From201306ControlActProcess_Gender()
    {
        System.out.println("testCreat201301From201306ControlActProcess_Gender");
        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess value = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();
        PRPAMT201306UV02ParameterList paramList = new PRPAMT201306UV02ParameterList();
        PRPAMT201306UV02QueryByParameter params = new PRPAMT201306UV02QueryByParameter();

        paramList.getLivingSubjectAdministrativeGender().add(HL7QueryParamsTransforms.createGender(gender));
        
        params.setParameterList(paramList);

        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
     
        value.setQueryByParameter(new JAXBElement<PRPAMT201306UV02QueryByParameter>(xmlqname, PRPAMT201306UV02QueryByParameter.class, params));
  
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess result = HL7PRPA201301Transforms.copyControlActProcess(value, localDeviceId);

        assertEquals(true, result != null);

        String actualGender = result.getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAdministrativeGenderCode().getCode();

        assertEquals(gender, actualGender);
    }
    @Test
     public void testCreat201301From201306ControlActProcessParams_Telcom()
    {
        System.out.println("testCreat201301From201306ControlActProcess");
        
        TELExplicit phone = HL7DataTransformHelper.createTELExplicit(phoneNumber);
        List<TELExplicit> phoneList = new java.util.ArrayList<TELExplicit>();
        phoneList.add(phone);
        
        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess value = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
        PRPAMT201306UV02ParameterList paramList = new PRPAMT201306UV02ParameterList();
        PRPAMT201306UV02QueryByParameter params = new PRPAMT201306UV02QueryByParameter();

        paramList.getPatientTelecom().add(null);
        paramList.getPatientTelecom().add(HL7QueryParamsTransforms.createTelecom(phoneList));

        params.setParameterList(paramList);

        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");

        value.setQueryByParameter(new JAXBElement<PRPAMT201306UV02QueryByParameter>(xmlqname, PRPAMT201306UV02QueryByParameter.class, params));

        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess result = HL7PRPA201301Transforms.copyControlActProcess2(value, localDeviceId);

        assertEquals(true, result != null);

        assertEquals(1,result.getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getTelecom().size());
        assertEquals(phoneNumber,result.getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getTelecom().get(0).getValue());

  
    }
    @Test
    public void testCreate201301from201306ControlActProcessFromParams_Name()
    {
        System.out.println("testCreate201301from201306ControlActProcess");
        
        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess value = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
        PRPAMT201306UV02ParameterList paramList = new PRPAMT201306UV02ParameterList();
        PRPAMT201306UV02QueryByParameter params = new PRPAMT201306UV02QueryByParameter();
        PRPAMT201306UV02LivingSubjectName subjName = create201306_Name();
        PNExplicit pnResult;

        paramList.getLivingSubjectName().add(subjName);
        params.setParameterList(paramList);

        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");

        value.setQueryByParameter(new JAXBElement<PRPAMT201306UV02QueryByParameter>(xmlqname, PRPAMT201306UV02QueryByParameter.class, params));

        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess result = HL7PRPA201301Transforms.copyControlActProcess2(value, "123");

        assertNotNull(result);
        assertEquals(1,result.getSubject().size());
        assertEquals(1,result.getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getName().size());

        pnResult = result.getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getName().get(0);

        assertEquals(2, pnResult.getContent().size());


    }
    @Test
    public void testCreate201301from201306ControlActProcess()
    {
        System.out.println("testCreate201301from201306ControlActProcess");

        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess value;

        value= create201306ControlActProcess();

        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess result;
        result = HL7PRPA201301Transforms.copyControlActProcess(value, localDeviceId);

        assertPRPAIN201301UV02MFMIMT700701UV01ControlActProcess(result);

    }
    @Test
    public void testCreate201301from201306()
    {
        PRPAIN201306UV02 value = new PRPAIN201306UV02();

        value.setControlActProcess(create201306ControlActProcess());
        // Create the Receiver
        value.getReceiver().add(HL7ReceiverTransforms.createMCCIMT000300UV01Receiver(receiverOID));

        // Create the Sender
        value.setSender(HL7SenderTransforms.createMCCIMT000300UV01Sender(senderOID));


        PRPAIN201301UV02 result = HL7PRPA201301Transforms.createPRPA201301(value,localDeviceId);
        
        assert201301HeaderInfo(result);
        assertPRPAIN201301UV02MFMIMT700701UV01ControlActProcess(result.getControlActProcess());
        TestHelper.assertSsnEquals(ssn,result);
        TestHelper.assertPatientIdEquals(patId, localDeviceId, result);
        TestHelper.assertGenderEquals(gender, result);
        TestHelper.assertPatientNameEquals(patientFirstName, patientLastName, result);
        TestHelper.assertBirthTimeEquals(birthTime, result);
       // TestHelper.assertReceiverIdEquals(receiverOID, result);
       // TestHelper.assertSenderIdEquals(senderOID, result);
        
    }
    private void assertPRPAIN201301UV02MFMIMT700701UV01ControlActProcess(PRPAIN201301UV02MFMIMT700701UV01ControlActProcess result)
    {
        //TelCom Assertions
        assertNotNull(result);
        assertEquals(phoneNumber,result.getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getTelecom().get(0).getValue());

        //Name Assertions
        PNExplicit pnResult;
        pnResult = result.getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getName().get(0);
        assertEquals(2, pnResult.getContent().size());

        //Gender Assertions
        String actualGender = result.getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAdministrativeGenderCode().getCode();
        assertEquals(gender, actualGender);
        assertEquals(localDeviceId, result.getSubject().get(0).getRegistrationEvent().getCustodian().getAssignedEntity().getId().get(0).getRoot());
        
        assertNotNull(result.getSubject().get(0).getRegistrationEvent().getId());
        assertEquals("NA", result.getSubject().get(0).getRegistrationEvent().getId().get(0).getNullFlavor().get(0));

    }
    private PRPAIN201306UV02MFMIMT700711UV01ControlActProcess create201306ControlActProcess()
    {
        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess result = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
        PRPAIN201306UV02MFMIMT700711UV01Subject1 subj1 = new PRPAIN201306UV02MFMIMT700711UV01Subject1();
        PRPAMT201306UV02ParameterList paramList = new PRPAMT201306UV02ParameterList();
        PRPAMT201306UV02QueryByParameter params = new PRPAMT201306UV02QueryByParameter();
        PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent regEvent= new PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent();
        PRPAIN201306UV02MFMIMT700711UV01Subject2 subj2 = new PRPAIN201306UV02MFMIMT700711UV01Subject2();
        JAXBElement<PRPAMT201310UV02Person> person;
        
        result.setMoodCode(XActMoodIntentEvent.EVN);


        paramList.getLivingSubjectName().add(create201306_Name());
        paramList.getLivingSubjectBirthTime().add(create201306_birthTime());
        paramList.getLivingSubjectAdministrativeGender().add(HL7QueryParamsTransforms.createGender(gender));

        paramList.getLivingSubjectId().add(create201306_PatientId());
        
        TELExplicit phone = HL7DataTransformHelper.createTELExplicit(phoneNumber);
        List<TELExplicit> phoneList = new java.util.ArrayList<TELExplicit>();
        phoneList.add(phone);

        paramList.getPatientTelecom().add(HL7QueryParamsTransforms.createTelecom(phoneList));

        params.setParameterList(paramList);
        
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        result.setQueryByParameter(new JAXBElement<PRPAMT201306UV02QueryByParameter>(xmlqname, PRPAMT201306UV02QueryByParameter.class, params));

        person = HL7PatientTransforms.create201310PatientPerson(patientFirstName, patientLastName, gender, birthTime, ssn);


        subj2.setPatient(HL7PatientTransforms.create201310Patient(person, HL7DataTransformHelper.IIFactory(localDeviceId, patId)));
        subj2.getPatient().getTelecom().add(phone);
        subj2.getPatient().getPatientPerson().getValue().getTelecom().add(phone);
        regEvent.setSubject1(subj2);

        subj1.setRegistrationEvent(regEvent);

        result.getSubject().add(subj1);
        
        return result;
    }
    private PRPAMT201306UV02LivingSubjectId create201306_PatientId()
    {
        PRPAMT201306UV02LivingSubjectId result = new PRPAMT201306UV02LivingSubjectId();
        II id = HL7DataTransformHelper.IIFactory(localDeviceId, patId);

        result.getValue().add(id);
        return result;
    }
    private PRPAMT201306UV02LivingSubjectName create201306_Name()
    {
        PRPAMT201306UV02LivingSubjectName result = new PRPAMT201306UV02LivingSubjectName();
        ENExplicit name = new ENExplicit();
        PNExplicit pnName;

        pnName = HL7DataTransformHelper.CreatePNExplicit(patientFirstName, patientLastName);

        name = HL7DataTransformHelper.ConvertPNToEN(pnName);

        result.getValue().add(name);

        return result;
    }
    private PRPAMT201306UV02LivingSubjectBirthTime create201306_birthTime()
    {
        PRPAMT201306UV02LivingSubjectBirthTime result= new PRPAMT201306UV02LivingSubjectBirthTime();

        result = HL7QueryParamsTransforms.createBirthTime(birthTime);

        return result;
    }
    private void assert201301HeaderInfo(PRPAIN201301UV02 result)
    {
        assertEquals(HL7Constants.ITS_VERSION,result.getITSVersion());
        assertEquals(HL7Constants.INTERACTION_ID_ROOT,result.getInteractionId().getRoot());
        assertEquals("AL", result.getAcceptAckCode().getCode());
        assertEquals("T", result.getProcessingModeCode().getCode());
        assertEquals("T", result.getProcessingCode().getCode());
        assertEquals(localDeviceId, result.getId().getRoot());
    }
}