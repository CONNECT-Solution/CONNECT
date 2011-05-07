/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.transform.subdisc;

import javax.xml.bind.JAXBElement;
import org.hl7.v3.II;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.MCCIMT000200UV01Acknowledgement;
import org.hl7.v3.MCCIMT000200UV01AcknowledgementDetail;
import org.hl7.v3.MCCIMT000200UV01TargetMessage;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author JHOPPESC
 */
public class HL7AckTransformsTest {

    public HL7AckTransformsTest() {
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
     * Test of createAckFrom201305 method, of class HL7AckTransforms.
     */
    @Test
    public void testCreateAckFrom201305() {
        System.out.println("testCreateAckFrom201305");

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "1234", "1.1.1");
        PRPAIN201305UV02 request = HL7PRPA201305Transforms.createPRPA201305(patient, "1.1", "2.2", null);
        String ackMsgText = "Success";

        II origMsgId = request.getId();

        MCCIIN000002UV01 result = HL7AckTransforms.createAckFrom201305(request, ackMsgText);

        assertNotNull(result);
        TestHelper.assertReceiverIdEquals("1.1", result);
        TestHelper.assertSenderIdEquals("2.2", result);
        TestHelper.assertAckMsgEquals(ackMsgText, result);
        TestHelper.assertAckMsgIdEquals(origMsgId, result);
    }

    /**
     * Test of createAckFrom201305 method, of class HL7AckTransforms.
     */
    @Test
    public void testCreateAckFrom201306() {
        System.out.println("testCreateAckFrom201306");

        JAXBElement<PRPAMT201301UV02Person> queryPerson = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
        PRPAMT201301UV02Patient queryPatient = HL7PatientTransforms.create201301Patient(queryPerson, "1234", "1.1.1");
        PRPAIN201305UV02 query = HL7PRPA201305Transforms.createPRPA201305(queryPatient, "1.1", "2.2", "1.1.1");

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, null, null);
        PRPAIN201306UV02 request = HL7PRPA201306Transforms.createPRPA201306(patient, "2.2", "1.1.1", "1.1", "2.2.2", query);
        String ackMsgText = "Success";

        II origMsgId = request.getId();

        MCCIIN000002UV01 result = HL7AckTransforms.createAckFrom201306(request, ackMsgText);

        assertNotNull(result);
        TestHelper.assertReceiverIdEquals("2.2", result);
        TestHelper.assertSenderIdEquals("1.1", result);
        TestHelper.assertAckMsgEquals(ackMsgText, result);
        TestHelper.assertAckMsgIdEquals(origMsgId, result);
    }

    /**
     * Test of createAckMessage method, of class HL7AckTransforms.
     */
    @Test
    public void testCreateAckMessage() {
        System.out.println("createAckMessage");
        String localDeviceId = "3.3.3";
        II origMsgId = new II();
        origMsgId.setExtension("5678");
        origMsgId.setRoot("3.3");
        String msgText = "Failure";
        String senderOID = "3.3";
        String receiverOID = "3.3";

        MCCIIN000002UV01 result = HL7AckTransforms.createAckMessage(localDeviceId, origMsgId, "CA", msgText, senderOID, receiverOID);

        assertNotNull(result);
        TestHelper.assertReceiverIdEquals(receiverOID, result);
        TestHelper.assertSenderIdEquals(senderOID, result);
        TestHelper.assertAckMsgEquals(msgText, result);
        TestHelper.assertAckMsgIdEquals(origMsgId, result);
    }

    /**
     * Test of createAcknowledgement method, of class HL7AckTransforms.
     */
    @Test
    public void testCreateAcknowledgement() {
        System.out.println("createAcknowledgement");
        II msgId = new II();
        msgId.setExtension("1234");
        msgId.setRoot("1.1");
        String msgText = "Neutral";

        MCCIMT000200UV01Acknowledgement result = HL7AckTransforms.createAcknowledgement(msgId, "CA", msgText);

        assertNotNull(result);
        TestHelper.assertAckMsgIdEquals(msgId, result);
        TestHelper.assertAckMsgEquals(msgText, result);
    }

    /**
     * Test of createTargetMessage method, of class HL7AckTransforms.
     */
    @Test
    public void testCreateTargetMessage() {
        System.out.println("createTargetMessage");
        II msgId = new II();
        msgId.setExtension("7777");
        msgId.setRoot("8.8");

        MCCIMT000200UV01TargetMessage result = HL7AckTransforms.createTargetMessage(msgId);

        assertNotNull(result);
        TestHelper.assertAckMsgIdEquals(msgId, result);
    }

    /**
     * Test of createAckDetail method, of class HL7AckTransforms.
     */
    @Test
    public void testCreateAckDetail() {
        System.out.println("createAckDetail");
        String msgText = "Test";

        MCCIMT000200UV01AcknowledgementDetail result = HL7AckTransforms.createAckDetail("CA", msgText);

        assertNotNull(result);
        TestHelper.assertAckMsgEquals(msgText, result);
    }

}