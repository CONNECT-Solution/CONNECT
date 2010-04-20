/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.common.eventcommon.PatDiscReqEventType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
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
public class PatientDiscoveryPolicyCheckerTest {

    public PatientDiscoveryPolicyCheckerTest() {
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
     * Test of check201305Policy method, of class PatientDiscoveryPolicyChecker.
     */
    @Test
    public void testCheck201305Policy() {
        System.out.println("testCheck201305Policy");
        PRPAIN201306UV02 message = new PRPAIN201306UV02();
        II patIdOverride = new II();
        AssertionType assertion = new AssertionType();
        
        PatientDiscoveryPolicyChecker instance = new PatientDiscoveryPolicyChecker() {
            @Override
            protected boolean invokePolicyEngine(PatDiscReqEventType policyCheckReq) {
                return true;
            }
        };

        boolean result = instance.check201305Policy(message, patIdOverride, assertion);

        assertEquals(true, result);
    }

    /**
     * Test of check201305Policy method, of class PatientDiscoveryPolicyChecker.
     */
    @Test
    public void testCheck201305PolicyFails() {
        System.out.println("testCheck201305PolicyFails");
        PRPAIN201306UV02 message = new PRPAIN201306UV02();
        II patIdOverride = new II();
        AssertionType assertion = new AssertionType();

        PatientDiscoveryPolicyChecker instance = new PatientDiscoveryPolicyChecker() {
            @Override
            protected boolean invokePolicyEngine(PatDiscReqEventType policyCheckReq) {
                return false;
            }
        };

        boolean result = instance.check201305Policy(message, patIdOverride, assertion);

        assertEquals(false, result);
    }

    /**
     * Test of checkOutgoingPolicy method, of class PatientDiscoveryPolicyChecker.
     */
    @Test
    public void testCheckOutgoingPolicy() {
        System.out.println("testCheckOutgoingPolicy");
        RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();

        PatientDiscoveryPolicyChecker instance = new PatientDiscoveryPolicyChecker() {
            @Override
            protected boolean invokePolicyEngine(CheckPolicyRequestType policyCheckReq) {
                return true;
            }
        };

        boolean result = instance.checkOutgoingPolicy(request);

        assertEquals(true, result);
    }

    /**
     * Test of checkOutgoingPolicy method, of class PatientDiscoveryPolicyChecker.
     */
    @Test
    public void testCheckOutgoingPolicyFails() {
        System.out.println("testCheckOutgoingPolicyFails");
        RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();

        PatientDiscoveryPolicyChecker instance = new PatientDiscoveryPolicyChecker() {
            @Override
            protected boolean invokePolicyEngine(CheckPolicyRequestType policyCheckReq) {
                return false;
            }
        };

        boolean result = instance.checkOutgoingPolicy(request);

        assertEquals(false, result);
    }

}