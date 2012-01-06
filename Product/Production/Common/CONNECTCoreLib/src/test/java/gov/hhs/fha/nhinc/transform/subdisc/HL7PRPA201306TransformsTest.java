/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.subdisc;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject2;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.MFMIMT700711UV01QueryAck;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201305UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01ControlActProcess;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;

/**
 *
 * @author jhoppesc
 */
public class HL7PRPA201306TransformsTest {
    
    private static Log log = LogFactory.getLog(HL7PRPA201306TransformsTest.class);

    public HL7PRPA201306TransformsTest() {
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
     * Test of testCreatePRPA201306 method, of class HL7PRPA201306Transforms.
     */
    @Test
    public void testCreatePRPA201306() {
        log.info("testCreatePRPA201306");

        PRPAMT201301UV02Patient patient = new PRPAMT201301UV02Patient();
        PRPAMT201301UV02Person person = new PRPAMT201301UV02Person();
        PNExplicit name = new PNExplicit();        
        person.getName().add(name);
        
        patient.setPatientPerson(new JAXBElement(new QName("http://v3.hl7.org","PRPAMT201301UV02Person"), PRPAMT201301UV02Person.class, person ));

        String senderOID = "2.16.840.1.113883.3.198";
        String AAID = "2.16.840.1.113883.3.198";
        String receiverOID = "2.16.840.1.113883.3.198";
        String localDeviceId = "2.16.840.1.113883.3.198";
        PRPAIN201305UV02 query = new PRPAIN201305UV02();

        PRPAIN201306UV02 result = HL7PRPA201306Transforms.createPRPA201306(patient, senderOID, AAID, receiverOID, localDeviceId, query);

        assertEquals(result.getAcknowledgement().get(0).getTypeCode().getCode(), "AA" );
        assertNotNull(result.getReceiver().get(0));
        assertNotNull(result.getSender());
        assertNotNull(result.getControlActProcess());

    }

    /**
     * Test of testCreateMCCIMT000200UV01Receiver method, of class HL7PRPA201306Transforms
     * with no input.
     */
    @Test
    public void testCreatePRPA201306ForPatientNotFound() {
        log.info("testCreatePRPA201306ForPatientNotFound");

        HL7PRPA201306Transforms hl7 = new HL7PRPA201306Transforms();
        PRPAIN201306UV02 result = null;

        //Test with null input
        result = hl7.createPRPA201306ForPatientNotFound(null);
        assertNull(result);

        //Test with null fields
        PRPAIN201305UV02 request = new PRPAIN201305UV02();
        result = hl7.createPRPA201306ForPatientNotFound(request);
        assertNull(result);

        //Test with proper request
        MCCIMT000100UV01Receiver receiver = new MCCIMT000100UV01Receiver();
        MCCIMT000100UV01Device device = new MCCIMT000100UV01Device();
        II id = new II();
        id.setRoot("1.1");
        device.getId().add(id);
        receiver.setDevice(device);
        request.getReceiver().add(receiver);
        
        MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();
        MCCIMT000100UV01Device senderdevice = new MCCIMT000100UV01Device();
        II senderid = new II();
        senderid.setRoot("2.2");
        senderdevice.getId().add(senderid);
        sender.setDevice(senderdevice);
        request.setSender(sender);

        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess cap = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();
        PRPAMT201306UV02QueryByParameter query = new  PRPAMT201306UV02QueryByParameter();
        query.setQueryId(new II());

        QName qname = new QName("http://v3.hl7.org", "PRPAMT201306UV02QueryByParameter");
        JAXBElement jaxb = new JAXBElement(qname, PRPAMT201306UV02QueryByParameter.class, query);
        cap.setQueryByParameter(jaxb);
        request.setControlActProcess(cap);

        II ii = new II();
        ii.setRoot("2.16.840.1.113883.1.6");
        ii.setExtension("PRPA_IN201305UV02");
        request.setInteractionId(ii);

        result = hl7.createPRPA201306ForPatientNotFound(request);
        assertNotNull(result);
    }
    
     /**
     * Test of testCreateMCCIMT000100UV01Receiver method, of class HL7PRPA201306Transforms.
     */
    @Test
    public void testCreatePRPA201306ForErrors() {
        log.info("testCreatePRPA201306ForErrors");

        HL7PRPA201306Transforms hl7 = new HL7PRPA201306Transforms();
        PRPAIN201306UV02 result = null;
        String errorCode = "1.3.6.1.4.1.19376.1.2.27.2";

        //Test with null input
        result = hl7.createPRPA201306ForErrors(null,errorCode);
        assertNull(result);

        //Test with null fields
        PRPAIN201305UV02 request = new PRPAIN201305UV02();
        result = hl7.createPRPA201306ForErrors(request, errorCode);
        assertNull(result);

        //Test with proper request
        MCCIMT000100UV01Receiver receiver = new MCCIMT000100UV01Receiver();
        MCCIMT000100UV01Device device = new MCCIMT000100UV01Device();
        II id = new II();
        id.setRoot("1.1");
        device.getId().add(id);
        receiver.setDevice(device);
        request.getReceiver().add(receiver);

        MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();
        MCCIMT000100UV01Device senderdevice = new MCCIMT000100UV01Device();
        II senderid = new II();
        senderid.setRoot("2.2");
        senderdevice.getId().add(senderid);
        sender.setDevice(senderdevice);
        request.setSender(sender);

        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess cap = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();
        PRPAMT201306UV02QueryByParameter query = new  PRPAMT201306UV02QueryByParameter();
        query.setQueryId(new II());

        QName qname = new QName("http://v3.hl7.org", "PRPAMT201306UV02QueryByParameter");
        JAXBElement jaxb = new JAXBElement(qname, PRPAMT201306UV02QueryByParameter.class, query);
        cap.setQueryByParameter(jaxb);
        request.setControlActProcess(cap);

        II ii = new II();
        ii.setRoot("2.16.840.1.113883.1.6");
        ii.setExtension("PRPA_IN201305UV02");
        request.setInteractionId(ii);

        result = hl7.createPRPA201306ForErrors(request, null);
        assertNull(result);

        result = hl7.createPRPA201306ForErrors(request, errorCode);
        assertNotNull(result);

        //Test with errorText Parameter
        String sErrorText = "URL COULD NOT BE FOUND";
        result = hl7.createPRPA201306ForErrors(request, errorCode, sErrorText);
        assertNotNull(result);
        assertNotNull(result.getControlActProcess().getReasonOf().get(0).getDetectedIssueEvent());
        assertEquals(result.getControlActProcess().getReasonOf().get(0).getDetectedIssueEvent().getText().getContent().get(0).toString(), sErrorText);

    }

    /**
     * Test of testCreateMCCIMT000100UV01Receiver method, of class HL7PRPA201306Transforms
     * with no input.
     */
    @Test
    public void testCreateQUQIMT021001UV01ControlActProcess() {
        log.info("testCreateQUQIMT021001UV01ControlActProcess");

        PRPAMT201301UV02Patient patient = new PRPAMT201301UV02Patient();
        PRPAMT201301UV02Person person = new PRPAMT201301UV02Person();
        PNExplicit name = new PNExplicit();
        person.getName().add(name);

        patient.setPatientPerson(new JAXBElement(new QName("http://v3.hl7.org","PRPAMT201301UV02Person"), PRPAMT201301UV02Person.class, person ));
        
        II patientId = new II();
        patientId.setRoot("2.2");
        patientId.setExtension("000001");
        patient.getId().add(patientId);

        String senderOID = "2.16.840.1.113883.3.198";
        String AAID = "2.16.840.1.113883.3.198";
        String localDeviceId = "2.16.840.1.113883.3.198";
        PRPAIN201305UV02 query = new PRPAIN201305UV02();
        
        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess cap = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();
        PRPAMT201306UV02QueryByParameter queryByParameter = new  PRPAMT201306UV02QueryByParameter();
        queryByParameter.setQueryId(new II());

        QName qname = new QName("http://v3.hl7.org", "PRPAMT201306UV02QueryByParameter");
        JAXBElement jaxb = new JAXBElement(qname, PRPAMT201306UV02QueryByParameter.class, queryByParameter);
        cap.setQueryByParameter(jaxb);
        query.setControlActProcess(cap);

        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess result = HL7PRPA201306Transforms.createQUQIMT021001UV01ControlActProcess(patient, localDeviceId, query, AAID, senderOID);
        assertNotNull(result);
    }

    /**
     * Test of testCreateQUQIMT021001UV01ControlActProcessWithNoRegistrationEvent method, of class HL7PRPA201306Transforms.
     */
    @Test
    public void testCreateQUQIMT021001UV01ControlActProcessWithNoRegistrationEvent() {
        log.info("testCreateQUQIMT021001UV01ControlActProcessWithNoRegistrationEvent");

        PRPAIN201305UV02 query = new PRPAIN201305UV02();

        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess cap = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();
        PRPAMT201306UV02QueryByParameter queryByParameter = new  PRPAMT201306UV02QueryByParameter();
        queryByParameter.setQueryId(new II());

        QName qname = new QName("http://v3.hl7.org", "PRPAMT201306UV02QueryByParameter");
        JAXBElement jaxb = new JAXBElement(qname, PRPAMT201306UV02QueryByParameter.class, queryByParameter);
        cap.setQueryByParameter(jaxb);
        query.setControlActProcess(cap);

        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess result = HL7PRPA201306Transforms.createQUQIMT021001UV01ControlActProcessWithNoRegistrationEvent(query);
        assertNotNull(result);
    }

    /**
     * Test of testCreateQueryAck method, of class HL7PRPA201306Transforms
     * with no input.
     */
    @Test
    public void testCreateQueryAck() {
        log.info("testCreateQueryAck");

        PRPAIN201305UV02 query = new PRPAIN201305UV02();

        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess cap = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();
        PRPAMT201306UV02QueryByParameter queryByParameter = new  PRPAMT201306UV02QueryByParameter();
        queryByParameter.setQueryId(new II());

        QName qname = new QName("http://v3.hl7.org", "PRPAMT201306UV02QueryByParameter");
        JAXBElement jaxb = new JAXBElement(qname, PRPAMT201306UV02QueryByParameter.class, queryByParameter);
        cap.setQueryByParameter(jaxb);
        query.setControlActProcess(cap);

        MFMIMT700711UV01QueryAck result = HL7PRPA201306Transforms.createQueryAck(query);
        assertNotNull(result);
    }

    /**
     * Test of testCreateSubject method, of class HL7PRPA201306Transforms
     * with no input.
     */
    @Test
    public void testCreateSubject() {
        log.info("testCreateSubject");

        PRPAMT201301UV02Patient patient = new PRPAMT201301UV02Patient();
        PRPAMT201301UV02Person person = new PRPAMT201301UV02Person();
        PNExplicit name = new PNExplicit();
        person.getName().add(name);

        patient.setPatientPerson(new JAXBElement(new QName("http://v3.hl7.org","PRPAMT201301UV02Person"), PRPAMT201301UV02Person.class, person ));
        
        String patientId = "000001";

        String senderOID = "2.16.840.1.113883.3.198";
        String AAID = "2.16.840.1.113883.3.198";
        String localDeviceId = "2.16.840.1.113883.3.198";
        PRPAIN201305UV02 query = new PRPAIN201305UV02();

        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess cap = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();
        PRPAMT201306UV02QueryByParameter queryByParameter = new  PRPAMT201306UV02QueryByParameter();
        queryByParameter.setQueryId(new II());

        QName qname = new QName("http://v3.hl7.org", "PRPAMT201306UV02QueryByParameter");
        JAXBElement jaxb = new JAXBElement(qname, PRPAMT201306UV02QueryByParameter.class, queryByParameter);
        cap.setQueryByParameter(jaxb);
        query.setControlActProcess(cap);

        PRPAIN201306UV02MFMIMT700711UV01Subject2 result = HL7PRPA201306Transforms.createSubject2(patient,query,patientId, senderOID);
        assertNotNull(result);
    }

}