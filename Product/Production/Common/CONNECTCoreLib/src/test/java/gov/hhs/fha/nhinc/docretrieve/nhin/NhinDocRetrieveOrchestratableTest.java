/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docretrieve.nhin;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.orchestration.InboundDelegate;
import gov.hhs.fha.nhinc.orchestration.AdapterDelegateTest;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.AuditTransformerTest;
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import gov.hhs.fha.nhinc.orchestration.InboundAggregatorTest;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformerTest;
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
public class NhinDocRetrieveOrchestratableTest {

    public NhinDocRetrieveOrchestratableTest() {
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
     * Test of setAssertion method, of class NhinDocRetrieveOrchestratable.
     */
    @Test
    public void testSetAssertion() {
        AssertionType _assertion = new AssertionType();
        NhinDocRetrieveOrchestratable instance = new NhinDocRetrieveOrchestratableImpl();
        instance.setAssertion(_assertion);
    }

    /**
     * Test of getAdapterDelegate method, of class NhinDocRetrieveOrchestratable.
     */
    @Test
    public void testGetAdapterDelegate() {
        AdapterDelegateTest t = new AdapterDelegateTest();
        InboundDelegate expResult = t.new AdapterDelegateImpl();
        NhinDocRetrieveOrchestratable instance = new NhinDocRetrieveOrchestratableImpl(null, null, expResult, null);
        InboundDelegate result = instance.getAdapterDelegate();
        assertEquals(expResult, result);
    }

    /**
     * Test of isEnabled method, of class NhinDocRetrieveOrchestratable.
     */
    @Test
    public void testIsEnabled() {
        NhinDocRetrieveOrchestratable instance = new NhinDocRetrieveOrchestratableImpl();
        boolean expResult = true;
        boolean result = instance.isEnabled();
        assertEquals(expResult, result);
    }

    /**
     * Test of isPassthru method, of class NhinDocRetrieveOrchestratable.
     */
    @Test
    public void testIsPassthru() {
        NhinDocRetrieveOrchestratable instance = new NhinDocRetrieveOrchestratableImpl();
        boolean expResult = false;
        boolean result = instance.isPassthru();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAuditTransformer method, of class NhinDocRetrieveOrchestratable.
     */
    @Test
    public void testGetAuditTransformer() {
        AuditTransformerTest t = new AuditTransformerTest();
        AuditTransformer expResult = t.new AuditTransformerImpl();
        NhinDocRetrieveOrchestratable instance = new NhinDocRetrieveOrchestratableImpl(null, expResult, null, null);
        AuditTransformer result = instance.getAuditTransformer();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPolicyTransformer method, of class NhinDocRetrieveOrchestratable.
     */
    @Test
    public void testGetPolicyTransformer() {
        PolicyTransformerTest t = new PolicyTransformerTest();
        PolicyTransformer expResult = t.new PolicyTransformerImpl();
        NhinDocRetrieveOrchestratable instance = new NhinDocRetrieveOrchestratableImpl(expResult, null, null, null);
        PolicyTransformer result = instance.getPolicyTransformer();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAssertion method, of class NhinDocRetrieveOrchestratable.
     */
    @Test
    public void testGetAssertion() {
        NhinDocRetrieveOrchestratable instance = new NhinDocRetrieveOrchestratableImpl();
        AssertionType expResult = new AssertionType();
        instance.setAssertion(expResult);
        AssertionType result = instance.getAssertion();
        assertEquals(expResult, result);
    }

    /**
     * Test of getServiceName method, of class NhinDocRetrieveOrchestratable.
     */
    @Test
    public void testGetServiceName() {
        NhinDocRetrieveOrchestratable instance = new NhinDocRetrieveOrchestratableImpl();
        String expResult = "NhinDocumentRetrieve";
        String result = instance.getServiceName();
        assertEquals(expResult, result);
    }

    public class NhinDocRetrieveOrchestratableImpl extends NhinDocRetrieveOrchestratable {

        public NhinDocRetrieveOrchestratableImpl()
        {

        }

        public NhinDocRetrieveOrchestratableImpl(PolicyTransformer pt, AuditTransformer at, InboundDelegate ad, NhinAggregator na) {
            super(pt, at, ad);
        }
    }
}
