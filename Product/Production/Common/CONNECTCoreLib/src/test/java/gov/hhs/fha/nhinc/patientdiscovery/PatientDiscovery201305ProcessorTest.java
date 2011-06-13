/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
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
 * @author JHOPPESC
 */
public class PatientDiscovery201305ProcessorTest {

    public PatientDiscovery201305ProcessorTest() {
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
     * Test of process201305 method, of class PatientDiscovery201305Processor.
     */
    @Test
    public void testProcess201305() {
        System.out.println("testProcess201305");

        PatientDiscovery201305Processor instance = new PatientDiscovery201305Processor() {
            @Override
            protected void storeMapping(PRPAIN201305UV02 request) {}

            @Override
            protected PRPAIN201306UV02 queryMpi(PRPAIN201305UV02 query, AssertionType assertion) {
                JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
                PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "5678", "2.2.2");
                PRPAIN201306UV02 resp = HL7PRPA201306Transforms.createPRPA201306(patient, "2.2", "1.1.1", "1.1", "2.2.2", query);

                return resp;
            }

            @Override
            protected boolean checkPolicy(PRPAIN201306UV02 response, AssertionType assertion) {
                return true;
            }

            @Override
            protected void createPatientCorrelation(PRPAIN201306UV02 queryResult, II remotePatient, AssertionType assertion, PRPAIN201305UV02 query) {}

            @Override
            protected void createPatientCorrelation(PRPAIN201306UV02 queryResult, AssertionType assertion, PRPAIN201305UV02 query) {}
        };

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "1234", "1.1.1");
        PRPAIN201305UV02 request = HL7PRPA201305Transforms.createPRPA201305(patient, "1.1", "2.2", "1.1.1");

        AssertionType assertion = new AssertionType();

        PRPAIN201306UV02 result = instance.process201305(request, assertion);

        TestHelper.assertPatientFound(result);
    }

    /**
     * Test of process201305 method, of class PatientDiscovery201305Processor.
     */
    @Test
    public void testProcess201305PatientNotFound() {
        System.out.println("testProcess201305PatientNotFound");

        PatientDiscovery201305Processor instance = new PatientDiscovery201305Processor() {
            @Override
            protected void storeMapping(PRPAIN201305UV02 request) {}

            @Override
            protected PRPAIN201306UV02 queryMpi(PRPAIN201305UV02 query, AssertionType assertion) {
                PRPAIN201306UV02 resp = HL7PRPA201306Transforms.createPRPA201306(null, "2.2", null, "1.1", null, query);

                return resp;
            }

            @Override
            protected boolean checkPolicy(PRPAIN201306UV02 response, AssertionType assertion) {
                return false;
            }

            @Override
            protected void createPatientCorrelation(PRPAIN201306UV02 queryResult, II remotePatient, AssertionType assertion, PRPAIN201305UV02 query) {}

            @Override
            protected void createPatientCorrelation(PRPAIN201306UV02 queryResult, AssertionType assertion, PRPAIN201305UV02 query) {}
        };

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "1234", "1.1.1");
        PRPAIN201305UV02 request = HL7PRPA201305Transforms.createPRPA201305(patient, "1.1", "2.2", "1.1.1");

        AssertionType assertion = new AssertionType();

        PRPAIN201306UV02 result = instance.process201305(request, assertion);

        TestHelper.assertPatientNotFound(result);
    }

    /**
     * Test of process201305 method, of class PatientDiscovery201305Processor.
     */
    @Test
    public void testProcess201305PolicyCheckFailure() {
        System.out.println("testProcess201305PolicyCheckFailure");

        PatientDiscovery201305Processor instance = new PatientDiscovery201305Processor() {
            @Override
            protected void storeMapping(PRPAIN201305UV02 request) {}

            @Override
            protected PRPAIN201306UV02 queryMpi(PRPAIN201305UV02 query, AssertionType assertion) {
                return null;
            }

            @Override
            protected boolean checkPolicy(PRPAIN201306UV02 response, AssertionType assertion) {
                return true;
            }

            @Override
            protected void createPatientCorrelation(PRPAIN201306UV02 queryResult, II remotePatient, AssertionType assertion, PRPAIN201305UV02 query) {}

            @Override
            protected void createPatientCorrelation(PRPAIN201306UV02 queryResult, AssertionType assertion, PRPAIN201305UV02 query) {}
        };

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "1234", "1.1.1");
        PRPAIN201305UV02 request = HL7PRPA201305Transforms.createPRPA201305(patient, "1.1", "2.2", "1.1.1");

        AssertionType assertion = new AssertionType();

        PRPAIN201306UV02 result = instance.process201305(request, assertion);

        TestHelper.assertPatientNotFound(result);
    }

    /**
     * Test of createNewRequest method, of class PatientDiscovery201305Processor.
     */
    @Test
    public void testCreateNewRequest() {
        System.out.println("testCreateNewRequest");

        String targetCommunityId = "3.3";
        PatientDiscovery201305Processor instance = new PatientDiscovery201305Processor();

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "1234", "1.1.1");
        PRPAIN201305UV02 request = HL7PRPA201305Transforms.createPRPA201305(patient, "1.1", "2.2", "1.1.1");

        PRPAIN201305UV02 result = instance.createNewRequest(request, targetCommunityId);

        TestHelper.assertReceiverEquals(targetCommunityId, result);
    }

    /**
     * Test of createNewRequest method, of class PatientDiscovery201305Processor.
     */
    @Test
    public void testCreateNewRequestNullInput() {
        System.out.println("testCreateNewRequestNullInput");

        PatientDiscovery201305Processor instance = new PatientDiscovery201305Processor();

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "1234", "1.1.1");
        PRPAIN201305UV02 request = HL7PRPA201305Transforms.createPRPA201305(patient, "1.1", "2.2", "1.1.1");

        PRPAIN201305UV02 result = instance.createNewRequest(request, null);

        assertNull(result);
    }

    /**
     * Test of extractPatientIdFrom201305 method, of class PatientDiscovery201305Processor.
     */
    @Test
    public void testExtractPatientIdFrom201305() {
        System.out.println("testExtractPatientIdFrom201305");

        PatientDiscovery201305Processor instance = new PatientDiscovery201305Processor();

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "1234", "1.1.1");
        PRPAIN201305UV02 request = HL7PRPA201305Transforms.createPRPA201305(patient, "1.1", "2.2", "1.1.1");

        II result = instance.extractPatientIdFrom201305(request);

        assertNotNull(result);
        assertEquals("1234", result.getExtension());
        assertEquals("1.1.1", result.getRoot());
    }

    /**
     * Test of extractPatientIdFrom201305 method, of class PatientDiscovery201305Processor.
     */
    @Test
    public void testExtractPatientIdFrom201305NullInput() {
        System.out.println("testExtractPatientIdFrom201305NullInput");

        PatientDiscovery201305Processor instance = new PatientDiscovery201305Processor();

        II result = instance.extractPatientIdFrom201305(null);

        assertNull(result);
    }

}