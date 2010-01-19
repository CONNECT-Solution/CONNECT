/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.jmock.Mockery;
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
public class PatientDiscovery201305ProcessorTest {

    private Mockery context;

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
    public void testPatientNotFoundRequestNull() {
        System.out.println("Patient Not Found - Request Null");

        PRPAIN201305UV02 request = null;
        PatientDiscovery201305Processor instance = new PatientDiscovery201305Processor() {

            @Override
            protected void storeMapping(PRPAIN201305UV02 request) {
                return;
            }

            @Override
            protected PRPAIN201306UV02 queryMpi(PRPAIN201305UV02 query, AssertionType assertion) {
                return null;
            }

            @Override
            protected PRPAIN201306UV02 createEmpty201306(String senderOID, String receiverOID, PRPAIN201305UV02 request) {
                return new PRPAIN201306UV02();
            }
        };
        PRPAIN201306UV02 result = instance.process201305(request, null);
        assertNotNull(result);
        assertNull(result.getControlActProcess());
    }

    /**
     * Test of process201305 method, of class PatientDiscovery201305Processor.
     */
    @Test
    public void testPatientNotFoundMismatch() {
        System.out.println("Patient Not Found - Mismatch");

        PRPAIN201305UV02 request = TestHelper.create201305("Joe", "Smith", null, null, null, "1.1", "2.2");
        PatientDiscovery201305Processor instance = new PatientDiscovery201305Processor() {

            @Override
            protected void storeMapping(PRPAIN201305UV02 request) {
                return;
            }

            @Override
            protected PRPAIN201306UV02 queryMpi(PRPAIN201305UV02 query, AssertionType assertion) {
                return new  PRPAIN201306UV02();
            }

            @Override
            protected PRPAIN201306UV02 createEmpty201306(String senderOID, String receiverOID, PRPAIN201305UV02 request) {
                return new PRPAIN201306UV02();
            }
        };
        PRPAIN201306UV02 result = instance.process201305(request, null);
        assertNotNull(result);
        assertNull(result.getControlActProcess());
    }

    /**
     * Test of process201305 method, of class PatientDiscovery201305Processor.
     */
    @Test
    public void testPatientFoundPolicyOk() {
        System.out.println("Patient Found - Name Only");

        PRPAIN201305UV02 request = TestHelper.create201305("Joe", "Smith", null, null, null, "1.1", "2.2");
        PatientDiscovery201305Processor instance = new PatientDiscovery201305Processor() {

            @Override
            protected void storeMapping(PRPAIN201305UV02 request) {
                return;
            }

            @Override
            protected PRPAIN201306UV02 queryMpi(PRPAIN201305UV02 query, AssertionType assertion) {
                return TestHelper.create201306("1.1", "2.2", query);
            }

            @Override
            protected PRPAIN201306UV02 createEmpty201306(String senderOID, String receiverOID, PRPAIN201305UV02 request) {
                return new PRPAIN201306UV02();
            }

            @Override
            protected boolean checkPolicy(PRPAIN201306UV02 response, II patIdOverride, AssertionType assertion) {
                return true;
            }
        };

        PRPAIN201306UV02 result = instance.process201305(request, null);
        assertNotNull(result);
        TestHelper.assertPatientMatch(request, result);
    }

    /**
     * Test of process201305 method, of class PatientDiscovery201305Processor.
     */
    @Test
    public void testPatientFoundPolicyFailed() {
        System.out.println("Patient Found - Name Only");

        PRPAIN201305UV02 request = TestHelper.create201305("Joe", "Smith", null, null, null, "1.1", "2.2");
        PatientDiscovery201305Processor instance = new PatientDiscovery201305Processor() {

            @Override
            protected void storeMapping(PRPAIN201305UV02 request) {
                return;
            }

            @Override
            protected PRPAIN201306UV02 queryMpi(PRPAIN201305UV02 query, AssertionType assertion) {
                return TestHelper.create201306("1.1", "2.2", query);
            }

            @Override
            protected PRPAIN201306UV02 createEmpty201306(String senderOID, String receiverOID, PRPAIN201305UV02 request) {
                return new PRPAIN201306UV02();
            }

            @Override
            protected boolean checkPolicy(PRPAIN201306UV02 response, II patIdOverride, AssertionType assertion) {
                return false;
            }
        };

        PRPAIN201306UV02 result = instance.process201305(request, null);
        assertNotNull(result);
        assertNull(result.getControlActProcess());
    }
}
