/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import org.apache.commons.logging.Log;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mastan.ketha
 */
public class TrustModeTest {

    public TrustModeTest() {
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
     * Test of processResponse method, of class TrustMode.
     */
    @Test
    public void testProcessResponse_ResponseParams() {
        System.out.println("processResponse");
        ResponseParams params = null;
        TrustMode instance = new TrustMode();
        PRPAIN201306UV02 expResult = null;
        PRPAIN201306UV02 result = instance.processResponse(params);
        assertEquals(expResult, result);        
    }

    /**
     * Test of processResponse method, of class TrustMode.
     */
    @Test
    public void testProcessResponse_3args() {
        System.out.println("processResponse");
        PRPAIN201306UV02 response = null;
        AssertionType assertion = null;
        II localPatId = null;
        TrustMode instance = new TrustMode();
        PRPAIN201306UV02 expResult = null;
        PRPAIN201306UV02 result = instance.processResponse(response, assertion, localPatId);
        assertEquals(expResult, result);        
    }

    /**
     * Test of sendToPatientCorrelationComponent method, of class TrustMode.
     */
    @Test
    public void testSendToPatientCorrelationComponent() {
        System.out.println("sendToPatientCorrelationComponent");
        II localPatId = null;
        II remotePatId = null;
        AssertionType assertion = null;
        PRPAIN201306UV02 response = null;
        TrustMode instance = new TrustMode();
        instance.sendToPatientCorrelationComponent(localPatId, remotePatId, assertion, response);       
    }

    /**
     * Test of requestHasLivingSubjectId method, of class TrustMode.
     */
    @Test
    public void testRequestHasLivingSubjectId() {
        System.out.println("requestHasLivingSubjectId");
        PRPAIN201305UV02 request = null;
        TrustMode instance = new TrustMode();
        boolean expResult = false;
        boolean result = instance.requestHasLivingSubjectId(request);
        assertEquals(expResult, result);        
    }

    /**
     * Test of getPatientId method, of class TrustMode.
     */
    @Test
    public void testGetPatientId_PRPAIN201305UV02() {
        System.out.println("getPatientId");
        PRPAIN201305UV02 request = null;
        TrustMode instance = new TrustMode();
        II expResult = null;
        II result = instance.getPatientId(request);
        assertEquals(expResult, result);        
    }

    /**
     * Test of getPatientId method, of class TrustMode.
     */
    @Test
    public void testGetPatientId_PRPAIN201306UV02() {
        System.out.println("getPatientId");
        PRPAIN201306UV02 request = null;
        TrustMode instance = new TrustMode();
        II expResult = null;
        II result = instance.getPatientId(request);
        assertEquals(expResult, result);        
    }
   
    /**
     * Test of createPRPA201301 method, of class TrustMode.
     */
    @Test
    public void testCreatePRPA201301() {
        System.out.println("createPRPA201301");
        PRPAIN201306UV02 input = null;
        TrustMode instance = new TrustMode();
        PRPAIN201301UV02 expResult = null;
        PRPAIN201301UV02 result = instance.createPRPA201301(input);
        assertEquals(expResult, result);        
    }    

}