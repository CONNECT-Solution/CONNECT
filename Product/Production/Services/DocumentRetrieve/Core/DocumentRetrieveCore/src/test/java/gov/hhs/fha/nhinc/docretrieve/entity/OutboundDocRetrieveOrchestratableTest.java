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
package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
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
        String expResult = NhincConstants.DOC_RETRIEVE_SERVICE_NAME;
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