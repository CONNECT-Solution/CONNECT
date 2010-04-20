/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.async.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.patientdiscovery.testhelper.TestHelper;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201305Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PatientTransforms;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
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
public class NhinPatientDiscoveryAsyncReqImplTest {

    public NhinPatientDiscoveryAsyncReqImplTest() {
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
     * Test of respondingGatewayPRPAIN201305UV02 method, of class NhinPatientDiscoveryAsyncReqImpl.
     */
    @Test
    public void testRespondingGatewayPRPAIN201305UV02() {
        System.out.println("testRespondingGatewayPRPAIN201305UV02");

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "1234", "1.1.1");
        PRPAIN201305UV02 request = HL7PRPA201305Transforms.createPRPA201305(patient, "1.1", "2.2", "1.1.1");

        AssertionType assertion = new AssertionType();
        NhinPatientDiscoveryAsyncReqImpl instance = new NhinPatientDiscoveryAsyncReqImpl();
        
        MCCIIN000002UV01 result = instance.respondingGatewayPRPAIN201305UV02(request, assertion);

        assertNotNull(result);
        TestHelper.assertReceiverEquals("1.1", result);
        TestHelper.assertSenderEquals("2.2", result);
        TestHelper.assertAckMsgEquals("Success", result);
    }

}