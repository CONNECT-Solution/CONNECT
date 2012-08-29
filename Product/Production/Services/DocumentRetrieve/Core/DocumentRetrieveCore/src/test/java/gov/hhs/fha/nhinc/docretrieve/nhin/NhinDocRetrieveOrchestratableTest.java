/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package gov.hhs.fha.nhinc.docretrieve.nhin;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docretrieve.nhin.InboundDocRetrieveOrchestratable;
import gov.hhs.fha.nhinc.docretrieve.orchestration.AdapterDelegateTest;
import gov.hhs.fha.nhinc.docretrieve.orchestration.AuditTransformerTest;
import gov.hhs.fha.nhinc.docretrieve.orchestration.InboundAggregatorTest;
import gov.hhs.fha.nhinc.docretrieve.orchestration.PolicyTransformerTest;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.InboundDelegate;
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
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
        InboundDocRetrieveOrchestratable instance = new NhinDocRetrieveOrchestratableImpl();
        instance.setAssertion(_assertion);
    }

    /**
     * Test of getAdapterDelegate method, of class NhinDocRetrieveOrchestratable.
     */
    @Test
    public void testGetAdapterDelegate() {
        AdapterDelegateTest t = new AdapterDelegateTest();
        InboundDelegate expResult = t.new AdapterDelegateImpl();
        InboundDocRetrieveOrchestratable instance = new NhinDocRetrieveOrchestratableImpl(null, null, expResult, null);
        InboundDelegate result = instance.getAdapterDelegate();
        assertEquals(expResult, result);
    }

    /**
     * Test of isPassthru method, of class NhinDocRetrieveOrchestratable.
     */
    @Test
    public void testIsPassthru() {
        InboundDocRetrieveOrchestratable instance = new NhinDocRetrieveOrchestratableImpl();
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
        InboundDocRetrieveOrchestratable instance = new NhinDocRetrieveOrchestratableImpl(null, expResult, null, null);
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
        InboundDocRetrieveOrchestratable instance = new NhinDocRetrieveOrchestratableImpl(expResult, null, null, null);
        PolicyTransformer result = instance.getPolicyTransformer();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAssertion method, of class NhinDocRetrieveOrchestratable.
     */
    @Test
    public void testGetAssertion() {
        InboundDocRetrieveOrchestratable instance = new NhinDocRetrieveOrchestratableImpl();
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
        InboundDocRetrieveOrchestratable instance = new NhinDocRetrieveOrchestratableImpl();
        String expResult = "NhinDocumentRetrieve";
        String result = instance.getServiceName();
        assertEquals(expResult, result);
    }

    public class NhinDocRetrieveOrchestratableImpl extends InboundDocRetrieveOrchestratable {

        public NhinDocRetrieveOrchestratableImpl() {

        }

        public NhinDocRetrieveOrchestratableImpl(PolicyTransformer pt, AuditTransformer at, InboundDelegate ad,
                NhinAggregator na) {
            super(pt, at, ad);
        }
    }
}
