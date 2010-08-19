/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.response;


import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.patientdiscovery.response.ResponseFactory;
import gov.hhs.fha.nhinc.patientdiscovery.testhelper.TestHelper;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201305Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201306Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PatientTransforms;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.MCCIIN000002UV01;
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
public class NhinPatientDiscoveryAsyncRespImplTest {

    public NhinPatientDiscoveryAsyncRespImplTest() {
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
    public void testRespondingGatewayPRPAIN201306UV02() {
        System.out.println("testRespondingGatewayPRPAIN201306UV02");
    }
    /**
     * Test of respondingGatewayPRPAIN201306UV02 method, of class NhinPatientDiscoveryAsyncRespImpl.
     */
    /*@Test
    public void testRespondingGatewayPRPAIN201306UV02() {
        System.out.println("testRespondingGatewayPRPAIN201306UV02");

        NhinPatientDiscoveryAsyncRespImpl instance = new NhinPatientDiscoveryAsyncRespImpl() {
            @Override
            protected boolean checkPolicy(PRPAIN201306UV02 response, AssertionType assertion) {
                return true;
            }

            @Override
            protected int getResponseMode () {
                return ResponseFactory.PASSTHRU_MODE;
            }

            @Override
            protected boolean isServiceEnabled() {
                return true;
            }

            @Override
            protected void processRespVerifyMode(PRPAIN201306UV02 body, AssertionType assertion) {
                return;
            }

            @Override
            protected void processRespTrustMode(PRPAIN201306UV02 body, AssertionType assertion) {
                return;
            }

            @Override
            protected void storeMapping (PRPAIN201306UV02 msg) {
                return;
            }

            @Override
            protected MCCIIN000002UV01 sendToAdapter(PRPAIN201306UV02 body, AssertionType assertion) {
                return HL7AckTransforms.createAckFrom201306(body, "Success");
            }
        };

        JAXBElement<PRPAMT201301UV02Person> queryPerson = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
        PRPAMT201301UV02Patient queryPatient = HL7PatientTransforms.create201301Patient(queryPerson, "1234", "1.1.1");
        PRPAIN201305UV02 query = HL7PRPA201305Transforms.createPRPA201305(queryPatient, "1.1", "2.2", "1.1.1");

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "5678", "2.2.2");
        PRPAIN201306UV02 resp = HL7PRPA201306Transforms.createPRPA201306(patient, "2.2", "1.1.1", "1.1", "2.2.2", query);

        AssertionType assertion = new AssertionType();
        
        MCCIIN000002UV01 result = instance.respondingGatewayPRPAIN201306UV02(resp, assertion);

        assertNotNull(result);
        TestHelper.assertReceiverEquals("2.2", result);
        TestHelper.assertSenderEquals("1.1", result);
        TestHelper.assertAckMsgEquals("Success", result);
    }*/

    /**
     * Test of respondingGatewayPRPAIN201306UV02 method, of class NhinPatientDiscoveryAsyncRespImpl.
     * Trust Mode
     */
    /*@Test
    public void testRespondingGatewayPRPAIN201306UV02Trust() {
        System.out.println("testRespondingGatewayPRPAIN201306UV02Trust");

        NhinPatientDiscoveryAsyncRespImpl instance = new NhinPatientDiscoveryAsyncRespImpl() {
            @Override
            protected boolean checkPolicy(PRPAIN201306UV02 response, AssertionType assertion) {
                return true;
            }

            @Override
            protected int getResponseMode () {
                return ResponseFactory.TRUST_MODE;
            }

            @Override
            protected boolean isServiceEnabled() {
                return true;
            }

            @Override
            protected void processRespVerifyMode(PRPAIN201306UV02 body, AssertionType assertion) {
                return;
            }

            @Override
            protected void processRespTrustMode(PRPAIN201306UV02 body, AssertionType assertion) {
                return;
            }

            @Override
            protected void storeMapping (PRPAIN201306UV02 msg) {
                return;
            }

            @Override
            protected MCCIIN000002UV01 sendToAdapter(PRPAIN201306UV02 body, AssertionType assertion) {
                return HL7AckTransforms.createAckFrom201306(body, "SuccessTrust");
            }
        };

        JAXBElement<PRPAMT201301UV02Person> queryPerson = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
        PRPAMT201301UV02Patient queryPatient = HL7PatientTransforms.create201301Patient(queryPerson, "1234", "1.1.1");
        PRPAIN201305UV02 query = HL7PRPA201305Transforms.createPRPA201305(queryPatient, "1.1", "2.2", "1.1.1");

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "5678", "2.2.2");
        PRPAIN201306UV02 resp = HL7PRPA201306Transforms.createPRPA201306(patient, "2.2", "1.1.1", "1.1", "2.2.2", query);

        AssertionType assertion = new AssertionType();

        MCCIIN000002UV01 result = instance.respondingGatewayPRPAIN201306UV02(resp, assertion);

        assertNotNull(result);
        TestHelper.assertReceiverEquals("2.2", result);
        TestHelper.assertSenderEquals("1.1", result);
        TestHelper.assertAckMsgEquals("SuccessTrust", result);
    }
*/
    /**
     * Test of respondingGatewayPRPAIN201306UV02 method, of class NhinPatientDiscoveryAsyncRespImpl.
     * Trust Mode
     */
   /* @Test
    public void testRespondingGatewayPRPAIN201306UV02Verify() {
        System.out.println("testRespondingGatewayPRPAIN201306UV02Verify");

        NhinPatientDiscoveryAsyncRespImpl instance = new NhinPatientDiscoveryAsyncRespImpl() {
            @Override
            protected boolean checkPolicy(PRPAIN201306UV02 response, AssertionType assertion) {
                return true;
            }

            @Override
            protected int getResponseMode () {
                return ResponseFactory.VERIFY_MODE;
            }

            @Override
            protected boolean isServiceEnabled() {
                return true;
            }

            @Override
            protected void processRespVerifyMode(PRPAIN201306UV02 body, AssertionType assertion) {
                return;
            }

            @Override
            protected void processRespTrustMode(PRPAIN201306UV02 body, AssertionType assertion) {
                return;
            }

            @Override
            protected void storeMapping (PRPAIN201306UV02 msg) {
                return;
            }

            @Override
            protected MCCIIN000002UV01 sendToAdapter(PRPAIN201306UV02 body, AssertionType assertion) {
                return HL7AckTransforms.createAckFrom201306(body, "SuccessVerify");
            }
        };

        JAXBElement<PRPAMT201301UV02Person> queryPerson = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
        PRPAMT201301UV02Patient queryPatient = HL7PatientTransforms.create201301Patient(queryPerson, "1234", "1.1.1");
        PRPAIN201305UV02 query = HL7PRPA201305Transforms.createPRPA201305(queryPatient, "1.1", "2.2", "1.1.1");

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "5678", "2.2.2");
        PRPAIN201306UV02 resp = HL7PRPA201306Transforms.createPRPA201306(patient, "2.2", "1.1.1", "1.1", "2.2.2", query);

        AssertionType assertion = new AssertionType();

        MCCIIN000002UV01 result = instance.respondingGatewayPRPAIN201306UV02(resp, assertion);

        assertNotNull(result);
        TestHelper.assertReceiverEquals("2.2", result);
        TestHelper.assertSenderEquals("1.1", result);
        TestHelper.assertAckMsgEquals("SuccessVerify", result);
    }*/

    /**
     * Test of respondingGatewayPRPAIN201306UV02 method, of class NhinPatientDiscoveryAsyncRespImpl.
     * Service Not Enabled
     */
    /*@Test
    public void testRespondingGatewayPRPAIN201306UV02NotEnabled() {
        System.out.println("testRespondingGatewayPRPAIN201306UV02NotEnabled");

        NhinPatientDiscoveryAsyncRespImpl instance = new NhinPatientDiscoveryAsyncRespImpl() {
            @Override
            protected boolean checkPolicy(PRPAIN201306UV02 response, AssertionType assertion) {
                return true;
            }

            @Override
            protected int getResponseMode () {
                return ResponseFactory.PASSTHRU_MODE;
            }

            @Override
            protected boolean isServiceEnabled() {
                return false;
            }

            @Override
            protected void processRespVerifyMode(PRPAIN201306UV02 body, AssertionType assertion) {
                return;
            }

            @Override
            protected void processRespTrustMode(PRPAIN201306UV02 body, AssertionType assertion) {
                return;
            }

            @Override
            protected void storeMapping (PRPAIN201306UV02 msg) {
                return;
            }

            @Override
            protected MCCIIN000002UV01 sendToAdapter(PRPAIN201306UV02 body, AssertionType assertion) {
                return HL7AckTransforms.createAckFrom201306(body, "Patient Discovery Async Response Service Not Enabled");
            }
        };

        JAXBElement<PRPAMT201301UV02Person> queryPerson = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
        PRPAMT201301UV02Patient queryPatient = HL7PatientTransforms.create201301Patient(queryPerson, "1234", "1.1.1");
        PRPAIN201305UV02 query = HL7PRPA201305Transforms.createPRPA201305(queryPatient, "1.1", "2.2", "1.1.1");

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "5678", "2.2.2");
        PRPAIN201306UV02 resp = HL7PRPA201306Transforms.createPRPA201306(patient, "2.2", "1.1.1", "1.1", "2.2.2", query);

        AssertionType assertion = new AssertionType();

        MCCIIN000002UV01 result = instance.respondingGatewayPRPAIN201306UV02(resp, assertion);

        assertNotNull(result);
        TestHelper.assertReceiverEquals("2.2", result);
        TestHelper.assertSenderEquals("1.1", result);
        TestHelper.assertAckMsgEquals("Patient Discovery Async Response Service Not Enabled", result);
    }*/

    /**
     * Test of respondingGatewayPRPAIN201306UV02 method, of class NhinPatientDiscoveryAsyncRespImpl.
     * Policy Check Failed
     */
    /*@Test
    public void testRespondingGatewayPRPAIN201306UV02PolicyFailed() {
        System.out.println("testRespondingGatewayPRPAIN201306UV02PolicyFailed");

        NhinPatientDiscoveryAsyncRespImpl instance = new NhinPatientDiscoveryAsyncRespImpl() {
            @Override
            protected boolean checkPolicy(PRPAIN201306UV02 response, AssertionType assertion) {
                return false;
            }

            @Override
            protected int getResponseMode () {
                return ResponseFactory.PASSTHRU_MODE;
            }

            @Override
            protected boolean isServiceEnabled() {
                return true;
            }

            @Override
            protected void processRespVerifyMode(PRPAIN201306UV02 body, AssertionType assertion) {
                return;
            }

            @Override
            protected void processRespTrustMode(PRPAIN201306UV02 body, AssertionType assertion) {
                return;
            }

            @Override
            protected void storeMapping (PRPAIN201306UV02 msg) {
                return;
            }

            @Override
            protected MCCIIN000002UV01 sendToAdapter(PRPAIN201306UV02 body, AssertionType assertion) {
                return HL7AckTransforms.createAckFrom201306(body, "Policy Check Failed");
            }
        };

        JAXBElement<PRPAMT201301UV02Person> queryPerson = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
        PRPAMT201301UV02Patient queryPatient = HL7PatientTransforms.create201301Patient(queryPerson, "1234", "1.1.1");
        PRPAIN201305UV02 query = HL7PRPA201305Transforms.createPRPA201305(queryPatient, "1.1", "2.2", "1.1.1");

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "5678", "2.2.2");
        PRPAIN201306UV02 resp = HL7PRPA201306Transforms.createPRPA201306(patient, "2.2", "1.1.1", "1.1", "2.2.2", query);

        AssertionType assertion = new AssertionType();

        MCCIIN000002UV01 result = instance.respondingGatewayPRPAIN201306UV02(resp, assertion);

        assertNotNull(result);
        TestHelper.assertReceiverEquals("2.2", result);
        TestHelper.assertSenderEquals("1.1", result);
        TestHelper.assertAckMsgEquals("Policy Check Failed", result);
    }*/

}