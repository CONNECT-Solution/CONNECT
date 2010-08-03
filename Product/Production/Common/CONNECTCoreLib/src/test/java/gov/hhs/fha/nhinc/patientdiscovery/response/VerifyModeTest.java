/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.response;

import gov.hhs.fha.nhinc.patientdiscovery.testhelper.TestHelper;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201305Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201306Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PatientTransforms;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.II;
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
 * @author jhoppesc
 */
public class VerifyModeTest {

    public VerifyModeTest() {
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
     * Test of convert201306to201305 method, of class VerifyMode.
     */
    @Test
    public void testConvert201306to201305() {
//        System.out.println("testConvert201306to201305");
//
//        PRPAMT201301UV02Patient patient = new PRPAMT201301UV02Patient();
//        II patId = new II();
//        patId.setExtension("1234");
//        patId.setRoot("1.1");
//        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", "0101195", "123456789");
//        patient = HL7PatientTransforms.create201301Patient(person, patId);
//        PRPAIN201305UV02 msg = HL7PRPA201305Transforms.createPRPA201305(patient, "2.2", "1.1", "1.1.1");
//
//        PRPAIN201306UV02 response = HL7PRPA201306Transforms.createPRPA201306(patient, "2.2", "1.1.1", "1.1", "1.1.1", msg);
//        VerifyMode instance = new VerifyMode();
//
//        PRPAIN201305UV02 result = instance.convert201306to201305(response);
//
//        assertNotNull (result);
//        TestHelper.assertReceiverEquals("1.1", msg);
//        TestHelper.assertSenderEquals("2.2", msg);
//        TestHelper.assertSSNEquals("123456789", msg);
    }

}