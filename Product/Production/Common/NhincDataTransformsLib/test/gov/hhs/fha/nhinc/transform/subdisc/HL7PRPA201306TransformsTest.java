/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.subdisc;

import javax.xml.bind.JAXBElement;
import org.hl7.v3.PRPAIN201305UV;
import org.hl7.v3.PRPAIN201306UV;
import org.hl7.v3.PRPAMT201301UVPatient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;
import org.hl7.v3.PRPAMT201301UVPerson;

/**
 *
 * @author dunnek
 */
public class HL7PRPA201306TransformsTest {

    private static Log log = LogFactory.getLog(HL7PRPA201306TransformsTest.class);
    private String localDeviceId = "2.16.840.1.113883.3.200.1";
    private String senderOID = "2.16.840.1.113883.3.200";
    private String receiverOID = "2.16.840.1.113883.3.184";
    private String patientFirstName = "Thomas";
    private String patientLastName = "Kirtland";
    private String gender = "M";
    private String birthTime = "19261225";
    private String ssn = "134679852";
    private String patId = "46821564";

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
    public void testCreatePRPA201306() {

        log.info("testCreatePRPA201306");

        JAXBElement<PRPAMT201301UVPerson> person = HL7PatientTransforms.create201301PatientPerson(patientFirstName, patientLastName, gender, birthTime, ssn);
        PRPAMT201301UVPatient patient = HL7PatientTransforms.create201301Patient(person, patId, localDeviceId);

        PRPAIN201305UV query = HL7PRPA201305Transforms.createPRPA201305(patient, senderOID, receiverOID, localDeviceId);
        PRPAIN201306UV result = HL7PRPA201306Transforms.createPRPA201306(patient, senderOID, receiverOID, receiverOID, localDeviceId, query);

        TestHelper.assertReceiverIdEquals(receiverOID, result);
        TestHelper.assertSenderIdEquals(senderOID, result);
    /*
    TestHelper.assertPatientIdEquals(patId, localDeviceId, result);
    TestHelper.assertBirthTimeEquals(birthTime, result);
    TestHelper.assertPatientNameEquals(patientFirstName, patientLastName, result);
    TestHelper.assertGenderEquals(gender, result);
     */
    }
}
