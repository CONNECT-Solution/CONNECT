/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientdiscovery;

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
 * @author JHOPPESC
 */
public class NhinPatientDiscoveryUtilsTest {

    public NhinPatientDiscoveryUtilsTest() {
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
     * Test of extractPatientIdFrom201306 method, of class NhinPatientDiscoveryUtils.
     */
    @Test
    public void testExtractPatientIdFrom201306() {
        System.out.println("testExtractPatientIdFrom201306");

        JAXBElement<PRPAMT201301UV02Person> queryPerson = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
        PRPAMT201301UV02Patient queryPatient = HL7PatientTransforms.create201301Patient(queryPerson, "1234", "1.1.1");
        PRPAIN201305UV02 query = new PRPAIN201305UV02();
        query = HL7PRPA201305Transforms.createPRPA201305(queryPatient, "1.1", "2.2", "1.1.1");

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "5678", "2.2.2");
        PRPAIN201306UV02 msg = new PRPAIN201306UV02();
        msg = HL7PRPA201306Transforms.createPRPA201306(patient, "2.2", "1.1.1", "1.1", "2.2.2", query);

        II result = NhinPatientDiscoveryUtils.extractPatientIdFrom201306(msg);

        assertEquals("5678", result.getExtension());
        assertEquals("2.2.2", result.getRoot());
    }

    /**
     * Test of extractPatientIdFrom201306 method, of class NhinPatientDiscoveryUtils.
     */
    @Test
    public void testExtractNullPatientIdFrom201306() {
        System.out.println("testExtractNullPatientIdFrom201306");

        JAXBElement<PRPAMT201301UV02Person> queryPerson = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
        PRPAMT201301UV02Patient queryPatient = HL7PatientTransforms.create201301Patient(queryPerson, "1234", "1.1.1");
        PRPAIN201305UV02 query = HL7PRPA201305Transforms.createPRPA201305(queryPatient, "1.1", "2.2", "1.1.1");

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, null, null);
        PRPAIN201306UV02 msg = HL7PRPA201306Transforms.createPRPA201306(patient, "2.2", "1.1.1", "1.1", "2.2.2", query);

        II result = NhinPatientDiscoveryUtils.extractPatientIdFrom201306(msg);

        assertNull(result);
    }
}
