/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import gov.hhs.fha.nhinc.orchestration.NhinDelegate;
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
public class EntityDocRetrieveOrchestratableTest {

    public EntityDocRetrieveOrchestratableTest() {
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
     * Test of getTarget method, of class EntityDocRetrieveOrchestratable.
     */
    @Test
    public void testGetTarget() {
        System.out.println("getTarget");
        EntityDocRetrieveOrchestratable instance = new EntityDocRetrieveOrchestratable();
        NhinTargetSystemType expResult = null;
        NhinTargetSystemType result = instance.getTarget();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setTarget method, of class EntityDocRetrieveOrchestratable.
     */
    @Test
    public void testSetTarget() {
        System.out.println("setTarget");
        NhinTargetSystemType target = null;
        EntityDocRetrieveOrchestratable instance = new EntityDocRetrieveOrchestratable();
        instance.setTarget(target);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setAssertion method, of class EntityDocRetrieveOrchestratable.
     */
    @Test
    public void testSetAssertion() {
        System.out.println("setAssertion");
        AssertionType _assertion = null;
        EntityDocRetrieveOrchestratable instance = new EntityDocRetrieveOrchestratable();
        instance.setAssertion(_assertion);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRequest method, of class EntityDocRetrieveOrchestratable.
     */
    @Test
    public void testGetRequest() {
        System.out.println("getRequest");
        EntityDocRetrieveOrchestratable instance = new EntityDocRetrieveOrchestratable();
        RetrieveDocumentSetRequestType expResult = null;
        RetrieveDocumentSetRequestType result = instance.getRequest();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setRequest method, of class EntityDocRetrieveOrchestratable.
     */
    @Test
    public void testSetRequest() {
        System.out.println("setRequest");
        RetrieveDocumentSetRequestType request = null;
        EntityDocRetrieveOrchestratable instance = new EntityDocRetrieveOrchestratable();
        instance.setRequest(request);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNhinDelegate method, of class EntityDocRetrieveOrchestratable.
     */
    @Test
    public void testGetNhinDelegate() {
        System.out.println("getNhinDelegate");
        EntityDocRetrieveOrchestratable instance = new EntityDocRetrieveOrchestratable();
        NhinDelegate expResult = null;
        NhinDelegate result = instance.getNhinDelegate();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isEnabled method, of class EntityDocRetrieveOrchestratable.
     */
    @Test
    public void testIsEnabled() {
        System.out.println("isEnabled");
        EntityDocRetrieveOrchestratable instance = new EntityDocRetrieveOrchestratable();
        boolean expResult = false;
        boolean result = instance.isEnabled();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isPassthru method, of class EntityDocRetrieveOrchestratable.
     */
    @Test
    public void testIsPassthru() {
        System.out.println("isPassthru");
        EntityDocRetrieveOrchestratable instance = new EntityDocRetrieveOrchestratable();
        boolean expResult = false;
        boolean result = instance.isPassthru();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAuditTransformer method, of class EntityDocRetrieveOrchestratable.
     */
    @Test
    public void testGetAuditTransformer() {
        System.out.println("getAuditTransformer");
        EntityDocRetrieveOrchestratable instance = new EntityDocRetrieveOrchestratable();
        AuditTransformer expResult = null;
        AuditTransformer result = instance.getAuditTransformer();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPolicyTransformer method, of class EntityDocRetrieveOrchestratable.
     */
    @Test
    public void testGetPolicyTransformer() {
        System.out.println("getPolicyTransformer");
        EntityDocRetrieveOrchestratable instance = new EntityDocRetrieveOrchestratable();
        PolicyTransformer expResult = null;
        PolicyTransformer result = instance.getPolicyTransformer();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAssertion method, of class EntityDocRetrieveOrchestratable.
     */
    @Test
    public void testGetAssertion() {
        System.out.println("getAssertion");
        EntityDocRetrieveOrchestratable instance = new EntityDocRetrieveOrchestratable();
        AssertionType expResult = null;
        AssertionType result = instance.getAssertion();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getServiceName method, of class EntityDocRetrieveOrchestratable.
     */
    @Test
    public void testGetServiceName() {
        System.out.println("getServiceName");
        EntityDocRetrieveOrchestratable instance = new EntityDocRetrieveOrchestratable();
        String expResult = "";
        String result = instance.getServiceName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAggregator method, of class EntityDocRetrieveOrchestratable.
     */
    @Test
    public void testGetAggregator() {
        System.out.println("getAggregator");
        EntityDocRetrieveOrchestratable instance = new EntityDocRetrieveOrchestratable();
        NhinAggregator expResult = null;
        NhinAggregator result = instance.getAggregator();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}