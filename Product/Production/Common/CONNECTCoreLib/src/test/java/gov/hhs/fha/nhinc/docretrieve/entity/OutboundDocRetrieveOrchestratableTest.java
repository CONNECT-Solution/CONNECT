/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mweaver
 */
public class OutboundDocRetrieveOrchestratableTest {

    public OutboundDocRetrieveOrchestratableTest() {
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
     * Test of getTarget method, of class OutboundDocRetrieveOrchestratable.
     */
    @Test
    public void testGetTarget() {
        System.out.println("getTarget");
        OutboundDocRetrieveOrchestratable instance = new OutboundDocRetrieveOrchestratable();
        NhinTargetSystemType expResult = null;
        NhinTargetSystemType result = instance.getTarget();
        assertEquals(expResult, result);
    }

    /**
     * Test of setTarget method, of class OutboundDocRetrieveOrchestratable.
     */
    @Test
    public void testSetTarget() {
        System.out.println("setTarget");
        NhinTargetSystemType target = null;
        OutboundDocRetrieveOrchestratable instance = new OutboundDocRetrieveOrchestratable();
        instance.setTarget(target);
    }

    /**
     * Test of setAssertion method, of class OutboundDocRetrieveOrchestratable.
     */
    @Test
    public void testSetAssertion() {
        System.out.println("setAssertion");
        AssertionType _assertion = null;
        OutboundDocRetrieveOrchestratable instance = new OutboundDocRetrieveOrchestratable();
        instance.setAssertion(_assertion);
    }

    /**
     * Test of getRequest method, of class OutboundDocRetrieveOrchestratable.
     */
    @Test
    public void testGetRequest() {
        System.out.println("getRequest");
        OutboundDocRetrieveOrchestratable instance = new OutboundDocRetrieveOrchestratable();
        RetrieveDocumentSetRequestType expResult = null;
        RetrieveDocumentSetRequestType result = instance.getRequest();
        assertEquals(expResult, result);
    }

    /**
     * Test of setRequest method, of class OutboundDocRetrieveOrchestratable.
     */
    @Test
    public void testSetRequest() {
        System.out.println("setRequest");
        RetrieveDocumentSetRequestType request = null;
        OutboundDocRetrieveOrchestratable instance = new OutboundDocRetrieveOrchestratable();
        instance.setRequest(request);
    }

    /**
     * Test of getNhinDelegate method, of class OutboundDocRetrieveOrchestratable.
     */
    @Test
    public void testGetNhinDelegate() {
        System.out.println("getNhinDelegate");
        OutboundDocRetrieveOrchestratable instance = new OutboundDocRetrieveOrchestratable();
        OutboundDelegate expResult = null;
        OutboundDelegate result = instance.getNhinDelegate();
        assertEquals(expResult, result);
    }

    /**
     * Test of isEnabled method, of class OutboundDocRetrieveOrchestratable.
     */
    @Test
    public void testIsEnabled() {
        System.out.println("isEnabled");
        OutboundDocRetrieveOrchestratable instance = new OutboundDocRetrieveOrchestratable();
        boolean expResult = true;
        boolean result = instance.isEnabled();
        assertEquals(expResult, result);
    }

    /**
     * Test of isPassthru method, of class OutboundDocRetrieveOrchestratable.
     */
    @Test
    public void testIsPassthru() {
        System.out.println("isPassthru");
        OutboundDocRetrieveOrchestratable instance = new OutboundDocRetrieveOrchestratable();
        boolean expResult = false;
        boolean result = instance.isPassthru();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAuditTransformer method, of class OutboundDocRetrieveOrchestratable.
     */
    @Test
    public void testGetAuditTransformer() {
        System.out.println("getAuditTransformer");
        OutboundDocRetrieveOrchestratable instance = new OutboundDocRetrieveOrchestratable();
        AuditTransformer expResult = null;
        AuditTransformer result = instance.getAuditTransformer();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPolicyTransformer method, of class OutboundDocRetrieveOrchestratable.
     */
    @Test
    public void testGetPolicyTransformer() {
        System.out.println("getPolicyTransformer");
        OutboundDocRetrieveOrchestratable instance = new OutboundDocRetrieveOrchestratable();
        PolicyTransformer expResult = null;
        PolicyTransformer result = instance.getPolicyTransformer();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAssertion method, of class OutboundDocRetrieveOrchestratable.
     */
    @Test
    public void testGetAssertion() {
        System.out.println("getAssertion");
        OutboundDocRetrieveOrchestratable instance = new OutboundDocRetrieveOrchestratable();
        AssertionType expResult = null;
        AssertionType result = instance.getAssertion();
        assertEquals(expResult, result);
    }

    /**
     * Test of getServiceName method, of class OutboundDocRetrieveOrchestratable.
     */
    @Test
    public void testGetServiceName() {
        System.out.println("getServiceName");
        OutboundDocRetrieveOrchestratable instance = new OutboundDocRetrieveOrchestratable();
        String expResult = "RetrieveDocument";
        String result = instance.getServiceName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAggregator method, of class OutboundDocRetrieveOrchestratable.
     */
    @Test
    public void testGetAggregator() {
        System.out.println("getAggregator");
        OutboundDocRetrieveOrchestratable instance = new OutboundDocRetrieveOrchestratable();
        NhinAggregator expResult = null;
        NhinAggregator result = instance.getAggregator();
        assertEquals(expResult, result);
    }

}