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
package gov.hhs.fha.nhinc.orchestration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer.Direction;

import org.apache.commons.logging.Log;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author mweaver
 */
public class CONNECTOrchestrationBaseTest {

    public CONNECTOrchestrationBaseTest() {
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
     * Test of getLogger method, of class CONNECTOrchestrationBase.
     */
    @Test
    public void testGetLogger() {
        CONNECTOrchestrationBase instance = new CONNECTOrchestrationBaseImpl();
        Log expResult = null;
        Log result = instance.getLogger();
        try {
            result.info("testing CONNECTOrchestrationBase getLogger()");
        } catch (Exception exc) {
            fail("The test case is a prototype.");
        }
    }

    /**
     * Test of audit method, of class CONNECTOrchestrationBase.
     */
    @Test
    public void testAuditRequest() {
        Orchestratable message = null;
        CONNECTOrchestrationBase instance = new CONNECTOrchestrationBaseImpl();
        instance.auditRequest(message);
        // there was no error, so success
        // TODO: make a better test
    }

    @Test
    public void testAuditResponse() {
        Orchestratable message = null;
        CONNECTOrchestrationBase instance = new CONNECTOrchestrationBaseImpl();
        instance.auditResponse(message);
        // there was no error, so success
        // TODO: make a better test
    }

    /**
     * Test of isAuditServiceEnabled method, of class CONNECTOrchestrationBase.
     */
    @Test
    public void testIsAuditServiceEnabled() {
        CONNECTOrchestrationBase instance = new CONNECTOrchestrationBaseImpl();
        boolean expResult = true;
        boolean result = instance.isAuditServiceEnabled();
        assertEquals(expResult, result);
    }

    /**
     * Test of isPolicyOk method, of class CONNECTOrchestrationBase.
     */
    @Test
    public void testIsPolicyOk() {
        Orchestratable message = null;
        Direction direction = null;
        CONNECTOrchestrationBase instance = new CONNECTOrchestrationBaseImpl();
        boolean expResult = false;
        boolean result = instance.isPolicyOk(message, direction);
        assertEquals(expResult, result);
    }

    public class CONNECTOrchestrationBaseImpl extends CONNECTOrchestrationBase {

		@Override
		protected Orchestratable processIfPolicyIsOk(Orchestratable message) {
			// TODO Auto-generated method stub
                    return null;
			
		}

		@Override
		protected boolean isPolicyOk(Orchestratable message, Direction direction) {
			
			return false;
		}
		
		
    }
}
