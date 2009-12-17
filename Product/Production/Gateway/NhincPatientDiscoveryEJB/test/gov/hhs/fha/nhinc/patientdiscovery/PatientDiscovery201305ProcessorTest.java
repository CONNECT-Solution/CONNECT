/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
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
    public void testProcess201305PatientNotFound() {
        System.out.println("process201305");

        PRPAIN201305UV02 request = null;
        AssertionType assertion = null;
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
            protected PRPAIN201306UV02 createEmpty201306 (String senderOID, String receiverOID, PRPAIN201305UV02 request) {
                return new PRPAIN201306UV02();
            }

        };
        PRPAIN201306UV02 expResult = new PRPAIN201306UV02();
        PRPAIN201306UV02 result = instance.process201305(request, assertion);
        assertNotNull(result);
        assertNull(result.getControlActProcess());
    }

}