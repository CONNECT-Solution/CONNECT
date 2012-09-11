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
package gov.hhs.fha.nhinc.docretrieve.orchestration;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.Delegate;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;

/**
 *
 * @author mweaver
 */
public class OrchestratableTest {

    public OrchestratableTest() {
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
     * Test of isPassthru method, of class Orchestratable.
     */
    @Test
    public void testIsPassthru() {
        Orchestratable instance = new OrchestratableImpl();
        boolean expResult = false;
        boolean result = instance.isPassthru();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAuditTransformer method, of class Orchestratable.
     */
    @Test
    public void testGetAuditTransformer() {
        Orchestratable instance = new OrchestratableImpl();
        AuditTransformer expResult = null;
        AuditTransformer result = instance.getAuditTransformer();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPolicyTransformer method, of class Orchestratable.
     */
    @Test
    public void testGetPolicyTransformer() {
        Orchestratable instance = new OrchestratableImpl();
        PolicyTransformer expResult = null;
        PolicyTransformer result = instance.getPolicyTransformer();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAssertion method, of class Orchestratable.
     */
    @Test
    public void testGetAssertion() {
        Orchestratable instance = new OrchestratableImpl();
        AssertionType expResult = null;
        AssertionType result = instance.getAssertion();
        assertEquals(expResult, result);
    }

    public class OrchestratableImpl implements Orchestratable {

        @Override
        public boolean isPassthru() {
            return false;
        }

        @Override
        public AuditTransformer getAuditTransformer() {
            return null;
        }

        @Override
        public PolicyTransformer getPolicyTransformer() {
            return null;
        }

        @Override
        public AssertionType getAssertion() {
            return null;
        }

        @Override
        public String getServiceName() {
            return "";
        }

        @Override
        public Delegate getDelegate() {
            // TODO Auto-generated method stub
            return null;
        }
    }

}