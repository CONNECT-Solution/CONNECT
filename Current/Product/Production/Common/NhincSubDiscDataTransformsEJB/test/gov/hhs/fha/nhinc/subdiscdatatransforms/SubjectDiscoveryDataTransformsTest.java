/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.subdiscdatatransforms;

import org.hl7.v3.MCCIIN000002UV01;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.transform.subdisc.HL7Constants;
import gov.hhs.fha.nhinc.transform.subdisc.HL7DataTransformHelper;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PatientTransforms;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.CS;
import org.hl7.v3.Create201302RequestType;
import org.hl7.v3.Create201305RequestType;
import org.hl7.v3.Create201310RequestType;
import org.hl7.v3.CreateAckMsgRequestType;
import org.hl7.v3.CreateFault201310RequestType;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201302UV;
import org.hl7.v3.PRPAIN201305UV;
import org.hl7.v3.PRPAIN201310UV;
import org.hl7.v3.PRPAMT201301UVPatient;
import org.hl7.v3.PRPAMT201301UVPerson;
import org.hl7.v3.PRPAMT201307UVParameterList;
import org.hl7.v3.PRPAMT201307UVPatientIdentifier;
import org.hl7.v3.PRPAMT201307UVQueryByParameter;
import org.hl7.v3.ST;

/**
 *
 * @author Jon Hoppesch
 */
public class SubjectDiscoveryDataTransformsTest {

    private static Log log = LogFactory.getLog(SubjectDiscoveryDataTransformsTest.class);

    public SubjectDiscoveryDataTransformsTest() {
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
     * Test of createAck method, of class SubjectDiscoveryDataTransforms.
     */
    @Test
    public void testCreateAck() {
        log.info("testCreateAck");

        SubjectDiscoveryDataTransforms instance = new SubjectDiscoveryDataTransforms();

        // Create the input message
        CreateAckMsgRequestType createAckRequest = new CreateAckMsgRequestType();
        createAckRequest.setLocalDeviceId("2.16.840.1.113883.3.198.1");
        createAckRequest.setMsgText("Failed - Timeout");
        createAckRequest.setReceiverOID("2.16.840.1.113883.3.198");
        createAckRequest.setSenderOID("2.16.840.1.113883.3.200");

        II id = new II();
        id.setRoot("2.16.840.1.113883.3.200.1");
        id.setExtension("123456");
        createAckRequest.setOrigMsgId(id);

        MCCIIN000002UV01 result = instance.createAck(createAckRequest);

        assertEquals(result.getId().getRoot(), "2.16.840.1.113883.3.198.1");
        assertEquals(result.getSender().getDevice().getId().get(0).getRoot(), "2.16.840.1.113883.3.200");
        assertEquals(result.getReceiver().get(0).getDevice().getId().get(0).getRoot(), "2.16.840.1.113883.3.198");
        assertEquals(result.getAcknowledgement().get(0).getTargetMessage().getId().getRoot(), "2.16.840.1.113883.3.200.1");
        assertEquals(result.getAcknowledgement().get(0).getTargetMessage().getId().getExtension(), "123456");
        assertEquals(result.getAcknowledgement().get(0).getAcknowledgementDetail().get(0).getText().getContent().get(0), "Failed - Timeout");
    }

    /**
     * Test of createAck method, default local device id
     */
    @Test
    public void testCreateAck_NoLocalDeviceId() {
        log.info("testCreateAck_NoLocalDeviceId");

        SubjectDiscoveryDataTransforms instance = new SubjectDiscoveryDataTransforms();

        // Create the input message
        CreateAckMsgRequestType createAckRequest = new CreateAckMsgRequestType();
        createAckRequest.setMsgText("Failed - Timeout");
        createAckRequest.setReceiverOID("2.16.840.1.113883.3.198");
        createAckRequest.setSenderOID("2.16.840.1.113883.3.200");

        II id = new II();
        id.setRoot("2.16.840.1.113883.3.200.1");
        id.setExtension("123456");
        createAckRequest.setOrigMsgId(id);

        MCCIIN000002UV01 result = instance.createAck(createAckRequest);

        assertEquals(result.getId().getRoot(), HL7Constants.DEFAULT_LOCAL_DEVICE_ID);
        assertEquals(result.getSender().getDevice().getId().get(0).getRoot(), "2.16.840.1.113883.3.200");
        assertEquals(result.getReceiver().get(0).getDevice().getId().get(0).getRoot(), "2.16.840.1.113883.3.198");
        assertEquals(result.getAcknowledgement().get(0).getTargetMessage().getId().getRoot(), "2.16.840.1.113883.3.200.1");
        assertEquals(result.getAcknowledgement().get(0).getTargetMessage().getId().getExtension(), "123456");
        assertEquals(result.getAcknowledgement().get(0).getAcknowledgementDetail().get(0).getText().getContent().get(0), "Failed - Timeout");
    }

    /**
     * Test of createAck method, no original message id
     */
    @Test
    public void testCreateAck_NoOrigMsgId() {
        log.info("testCreateAck_NoOrigMsgId");

        SubjectDiscoveryDataTransforms instance = new SubjectDiscoveryDataTransforms();

        // Create the input message
        CreateAckMsgRequestType createAckRequest = new CreateAckMsgRequestType();
        createAckRequest.setLocalDeviceId("2.16.840.1.113883.3.198.1");
        createAckRequest.setMsgText("Failed - Timeout");
        createAckRequest.setReceiverOID("2.16.840.1.113883.3.198");
        createAckRequest.setSenderOID("2.16.840.1.113883.3.200");

        MCCIIN000002UV01 result = instance.createAck(createAckRequest);

        assertEquals(result.getId().getRoot(), "2.16.840.1.113883.3.198.1");
        assertEquals(result.getSender().getDevice().getId().get(0).getRoot(), "2.16.840.1.113883.3.200");
        assertEquals(result.getReceiver().get(0).getDevice().getId().get(0).getRoot(), "2.16.840.1.113883.3.198");
        assertNull(result.getAcknowledgement().get(0).getTargetMessage());
        assertEquals(result.getAcknowledgement().get(0).getAcknowledgementDetail().get(0).getText().getContent().get(0), "Failed - Timeout");
    }

    /**
     * Test of createAck method, no message text
     */
    @Test
    public void testCreateAck_NoMsgText() {
        log.info("testCreateAck_NoMsgText");

        SubjectDiscoveryDataTransforms instance = new SubjectDiscoveryDataTransforms();

        // Create the input message
        CreateAckMsgRequestType createAckRequest = new CreateAckMsgRequestType();
        createAckRequest.setLocalDeviceId("2.16.840.1.113883.3.198.1");
        createAckRequest.setReceiverOID("2.16.840.1.113883.3.198");
        createAckRequest.setSenderOID("2.16.840.1.113883.3.200");

        II id = new II();
        id.setRoot("2.16.840.1.113883.3.200.1");
        id.setExtension("123456");
        createAckRequest.setOrigMsgId(id);

        MCCIIN000002UV01 result = instance.createAck(createAckRequest);

        assertEquals(result.getId().getRoot(), "2.16.840.1.113883.3.198.1");
        assertEquals(result.getSender().getDevice().getId().get(0).getRoot(), "2.16.840.1.113883.3.200");
        assertEquals(result.getReceiver().get(0).getDevice().getId().get(0).getRoot(), "2.16.840.1.113883.3.198");
        assertEquals(result.getAcknowledgement().get(0).getTargetMessage().getId().getRoot(), "2.16.840.1.113883.3.200.1");
        assertEquals(result.getAcknowledgement().get(0).getTargetMessage().getId().getExtension(), "123456");
        assertEquals(result.getAcknowledgement().get(0).getAcknowledgementDetail().size(), 0);
    }

    /**
     * Test of createAck method, no acknowledgement section
     */
    @Test
    public void testCreateAck_NoAckSection() {
        log.info("testCreateAck_NoAckSection");

        SubjectDiscoveryDataTransforms instance = new SubjectDiscoveryDataTransforms();

        // Create the input message
        CreateAckMsgRequestType createAckRequest = new CreateAckMsgRequestType();
        createAckRequest.setLocalDeviceId("2.16.840.1.113883.3.198.1");
        createAckRequest.setReceiverOID("2.16.840.1.113883.3.198");
        createAckRequest.setSenderOID("2.16.840.1.113883.3.200");

        MCCIIN000002UV01 result = instance.createAck(createAckRequest);


        assertEquals(result.getId().getRoot(), "2.16.840.1.113883.3.198.1");
        assertEquals(result.getSender().getDevice().getId().get(0).getRoot(), "2.16.840.1.113883.3.200");
        assertEquals(result.getReceiver().get(0).getDevice().getId().get(0).getRoot(), "2.16.840.1.113883.3.198");
        assertEquals(result.getAcknowledgement().size(), 0);
    }

    /**
     * Test of createAck method, no sender OID
     */
    @Test
    public void testCreateAck_NoSenderOID() {
        log.info("testCreateAck_NoSenderOID");

        SubjectDiscoveryDataTransforms instance = new SubjectDiscoveryDataTransforms();

        // Create the input message
        CreateAckMsgRequestType createAckRequest = new CreateAckMsgRequestType();
        createAckRequest.setLocalDeviceId("2.16.840.1.113883.3.198.1");
        createAckRequest.setMsgText("Failed - Timeout");
        createAckRequest.setReceiverOID("2.16.840.1.113883.3.198");

        II id = new II();
        id.setRoot("2.16.840.1.113883.3.200.1");
        id.setExtension("123456");
        createAckRequest.setOrigMsgId(id);
        
        MCCIIN000002UV01 result = instance.createAck(createAckRequest);

        assertNull(result);
    }

    /**
     * Test of createAck method, no receiver OID
     */
    @Test
    public void testCreateAck_NoReceiverOID() {
        log.info("testCreateAck_NoReceiverOID");

        SubjectDiscoveryDataTransforms instance = new SubjectDiscoveryDataTransforms();

        // Create the input message
        CreateAckMsgRequestType createAckRequest = new CreateAckMsgRequestType();
        createAckRequest.setLocalDeviceId("2.16.840.1.113883.3.198.1");
        createAckRequest.setMsgText("Failed - Timeout");
        createAckRequest.setSenderOID("2.16.840.1.113883.3.200");

        II id = new II();
        id.setRoot("2.16.840.1.113883.3.200.1");
        id.setExtension("123456");
        createAckRequest.setOrigMsgId(id);

        MCCIIN000002UV01 result = instance.createAck(createAckRequest);

        assertNull(result);
    }
    
    /**
     * Test of create201305 method
     */
    @Test
    public void testcreate201305() {
        log.info("testcreate201305");
        
        SubjectDiscoveryDataTransforms instance = new SubjectDiscoveryDataTransforms();
        
        Create201305RequestType create201305Request = new Create201305RequestType();
        create201305Request.setLocalDeviceId("2.16.840.1.113883.3.166.4.2");
        create201305Request.setReceiverOID("2.16.840.1.113883.3.166.4");
        create201305Request.setSenderOID("2.16.840.1.113883.3.166.4");
        
        JAXBElement<PRPAMT201301UVPerson> person = HL7PatientTransforms.create201301PatientPerson("Nancy", "Jackson", "F", "19690101", "125689743");
        PRPAMT201301UVPatient patient = HL7PatientTransforms.create201301Patient(person, "12345", "2.16.840.1.113883.3.166.4.2");
        create201305Request.setPRPA201301Patient(patient);
        
        PRPAIN201305UV result = instance.create201305(create201305Request);
        
        TestHelper.assertReceiverIdEquals("2.16.840.1.113883.3.166.4", result);
        TestHelper.assertSenderIdEquals("2.16.840.1.113883.3.166.4", result);
        TestHelper.assertPatientIdEquals("12345", "2.16.840.1.113883.3.166.4.2", result);
        TestHelper.assertBirthTimeEquals("19690101", result);
        TestHelper.assertPatientNameEquals("Nancy", "Jackson", result);
        TestHelper.assertGenderEquals("F", result);
        
    }
    
    /**
     * Test of create201302 method
     */
    @Test
    public void testcreate201302() {
        log.info("testcreate201302");
        
        SubjectDiscoveryDataTransforms instance = new SubjectDiscoveryDataTransforms();
        
        Create201302RequestType create201302Request = new Create201302RequestType();
        create201302Request.setRemotePatientId("192837");
        create201302Request.setRemoteDeviceId("2.16.840.1.113883.3.166.4.2");
        create201302Request.setReceiverOID("2.16.840.1.113883.3.184");
        create201302Request.setSenderOID("2.16.840.1.113883.3.166.4");
        
        JAXBElement<PRPAMT201301UVPerson> person = HL7PatientTransforms.create201301PatientPerson("Kyle", "Jackson", "M", "19991201", "654987321");
        PRPAMT201301UVPatient patient = HL7PatientTransforms.create201301Patient(person, "112233", "2.16.840.1.113883.3.184.3");
        create201302Request.setPRPA201301Patient(patient);
        
        PRPAIN201302UV result = instance.create201302(create201302Request);
        
        TestHelper.assertReceiverIdEquals("2.16.840.1.113883.3.184", result);
        TestHelper.assertSenderIdEquals("2.16.840.1.113883.3.166.4", result);
        TestHelper.assertPatientIdEquals("112233", "2.16.840.1.113883.3.184.3", result);
        TestHelper.assertBirthTimeEquals("19991201", result);
        TestHelper.assertPatientNameEquals("Kyle", "Jackson", result);
        TestHelper.assertGenderEquals("M", result);
    }
    
    /**
     * Test of create201310 method
     */
    @Test
    public void testcreateFault201310() {
        log.info("testcreateFault201310");
        
        SubjectDiscoveryDataTransforms instance = new SubjectDiscoveryDataTransforms();
        
        CreateFault201310RequestType createFault201310Request = new CreateFault201310RequestType();
        String senderOID = "Sender1";
        String receiverOID = "Receiver1";
        
        createFault201310Request.setReceiverOID(receiverOID);
        createFault201310Request.setSenderOID(senderOID);

        
        PRPAIN201310UV result = instance.createFault201310(createFault201310Request);
        
        TestHelper.assertReceiverIdEquals(receiverOID, result);
        TestHelper.assertSenderIdEquals(senderOID, result);
        TestHelper.assertPatientIdEquals("", "", HL7Constants.DEFAULT_LOCAL_DEVICE_ID, result);
        
        PRPAMT201307UVQueryByParameter queryParam = new PRPAMT201307UVQueryByParameter();
        TestHelper.assertQueryParam(queryParam, result);
    }
    
    /**
     * Test of create201310 method
     */
    @Test
    public void testcreate201310() {
        log.info("testcreate201310");
        
        SubjectDiscoveryDataTransforms instance = new SubjectDiscoveryDataTransforms();
        
        Create201310RequestType create201310Request = new Create201310RequestType();
        String patientId = "123";
        String assigningAuthorityId = "Auth.111.222.333";
        String localDeviceId = "9.8.7";
        String senderOID = "Sender1";
        String receiverOID = "Receiver1";
        
        String qid = "2.222";
        II queryId = HL7DataTransformHelper.IIFactory(qid);
        
        String qsc = "QueryCode";
        CS statusCode = HL7DataTransformHelper.CSFactory(qsc);
          
        ST semantics = new ST();
        semantics.setLanguage("TestSemantics");
        
        String pidValue = "Pat_Id";
        
        PRPAMT201307UVPatientIdentifier pid = new PRPAMT201307UVPatientIdentifier();
        pid.setSemanticsText(semantics);
        pid.getValue().add(HL7DataTransformHelper.IIFactory(pidValue));
        
        PRPAMT201307UVParameterList pList = new PRPAMT201307UVParameterList();
        pList.getPatientIdentifier().add(pid);
        
        PRPAMT201307UVQueryByParameter queryParam = new PRPAMT201307UVQueryByParameter();
        queryParam.setQueryId(queryId);
        queryParam.setStatusCode(statusCode);
        queryParam.setParameterList(pList);
        
        create201310Request.setPseudoPatientId(patientId);
        create201310Request.setPseudoAssigningAuthorityId(assigningAuthorityId);
        create201310Request.setLocalDeviceId(localDeviceId);
        create201310Request.setReceiverOID(receiverOID);
        create201310Request.setSenderOID(senderOID);
        create201310Request.setPRPA201307QueryByParameter(queryParam);
        
        PRPAIN201310UV result = instance.create201310(create201310Request);
        
        TestHelper.assertReceiverIdEquals(receiverOID, result);
        TestHelper.assertSenderIdEquals(senderOID, result);
        TestHelper.assertPatientIdEquals(patientId, assigningAuthorityId, localDeviceId, result);
        TestHelper.assertQueryParam(queryParam, result);
    }
}