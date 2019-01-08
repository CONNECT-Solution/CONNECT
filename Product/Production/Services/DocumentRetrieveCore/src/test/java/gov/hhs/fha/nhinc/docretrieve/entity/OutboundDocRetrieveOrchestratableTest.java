/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
        OutboundDocRetrieveOrchestratable instance = new OutboundStandardDocRetrieveOrchestratable();
        NhinTargetSystemType expResult = null;
        NhinTargetSystemType result = instance.getTarget();
        assertEquals(expResult, result);
    }

    /**
     * Test of setTarget method, of class OutboundDocRetrieveOrchestratable.
     */
    @Test
    public void testSetTarget() {
        NhinTargetSystemType target = new NhinTargetSystemType();
        OutboundDocRetrieveOrchestratable instance = new OutboundStandardDocRetrieveOrchestratable();
        instance.setTarget(target);
        assertEquals(target, instance.getTarget());
    }

    /**
     * Test of setAssertion method, of class OutboundDocRetrieveOrchestratable.
     */
    @Test
    public void testSetAssertion() {
        AssertionType assertion = new AssertionType();
        OutboundDocRetrieveOrchestratable instance = new OutboundStandardDocRetrieveOrchestratable();
        instance.setAssertion(assertion);
        assertEquals(assertion, instance.getAssertion());
    }

    /**
     * Test of getRequest method, of class OutboundDocRetrieveOrchestratable.
     */
    @Test
    public void testGetRequest() {
        OutboundDocRetrieveOrchestratable instance = new OutboundStandardDocRetrieveOrchestratable();
        RetrieveDocumentSetRequestType expResult = null;
        RetrieveDocumentSetRequestType result = instance.getRequest();
        assertEquals(expResult, result);
    }

    /**
     * Test of setRequest method, of class OutboundDocRetrieveOrchestratable.
     */
    @Test
    public void testSetRequest() {
        RetrieveDocumentSetRequestType request = new RetrieveDocumentSetRequestType();
        OutboundDocRetrieveOrchestratable instance = new OutboundStandardDocRetrieveOrchestratable();
        instance.setRequest(request);
        assertEquals(request, instance.getRequest());
    }

    /**
     * Test of getNhinDelegate method, of class OutboundDocRetrieveOrchestratable.
     */
    @Test
    public void testGetNhinDelegate() {
        OutboundDocRetrieveOrchestratable instance = new OutboundStandardDocRetrieveOrchestratable();
        OutboundDelegate expResult = null;
        OutboundDelegate result = instance.getNhinDelegate();
        assertEquals(expResult, result);
    }

    /**
     * Test of isPassthru method, of class OutboundDocRetrieveOrchestratable.
     */
    @Test
    public void testIsPassthruStandard() {
        OutboundDocRetrieveOrchestratable instance = new OutboundStandardDocRetrieveOrchestratable();
        boolean expResult = false;
        boolean result = instance.isPassthru();
        assertEquals(expResult, result);
    }

    /**
     * Test of isPassthru method, of class OutboundDocRetrieveOrchestratable.
     */
    @Test
    public void testIsPassthruPassthrough() {
        OutboundDocRetrieveOrchestratable instance = new OutboundPassthroughDocRetrieveOrchestratable();
        boolean expResult = true;
        boolean result = instance.isPassthru();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPolicyTransformer method, of class OutboundDocRetrieveOrchestratable.
     */
    @Test
    public void testGetPolicyTransformer() {
        OutboundDocRetrieveOrchestratable instance = new OutboundStandardDocRetrieveOrchestratable();
        PolicyTransformer expResult = null;
        PolicyTransformer result = instance.getPolicyTransformer();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAssertion method, of class OutboundDocRetrieveOrchestratable.
     */
    @Test
    public void testGetAssertion() {
        OutboundDocRetrieveOrchestratable instance = new OutboundStandardDocRetrieveOrchestratable();
        AssertionType expResult = null;
        AssertionType result = instance.getAssertion();
        assertEquals(expResult, result);
    }

    /**
     * Test of getServiceName method, of class OutboundDocRetrieveOrchestratable.
     */
    @Test
    public void testGetServiceName() {
        OutboundDocRetrieveOrchestratable instance = new OutboundStandardDocRetrieveOrchestratable();
        String expResult = NhincConstants.DOC_RETRIEVE_SERVICE_NAME;
        String result = instance.getServiceName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAggregator method, of class OutboundDocRetrieveOrchestratable.
     */
    @Test
    public void testGetAggregator() {
        OutboundDocRetrieveOrchestratable instance = new OutboundStandardDocRetrieveOrchestratable();
        NhinAggregator expResult = null;
        NhinAggregator result = instance.getAggregator();
        assertEquals(expResult, result);
    }

}
