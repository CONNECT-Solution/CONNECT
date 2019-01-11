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
package gov.hhs.fha.nhinc.orchestration;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author mweaver
 */
public class CONNECTOutboundOrchestratorTest {

    public CONNECTOutboundOrchestratorTest() {
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
     * Test of process method, of class CONNECTInboundOrchestrator.
     */
    @Test
    public void testProcess() {
        Orchestratable message = new EntityOrchestratableImpl();
        CONNECTInboundOrchestrator instance = new CONNECTInboundOrchestrator();
        instance.process(message);
        // there was no error, so success
        // TODO: make a better test
    }

    /**
     * Test of delegateToNhin method, of class CONNECTInboundOrchestrator.
     */
    @Test
    public void testDelegateToNhin() {
        OutboundOrchestratable message = new EntityOrchestratableImpl();
        CONNECTInboundOrchestrator instance = new CONNECTInboundOrchestrator();
        instance.delegate(message);
        // there was no error, so success
        // TODO: make a better test
    }

    public class EntityOrchestratableImpl implements OutboundOrchestratable {

        public EntityOrchestratableImpl() {

        }

        public OutboundDelegate getNhinDelegate() {
            InboundDelegateTest test = new InboundDelegateTest();
            return test.new NhinDelegateImpl();
        }

        @Override
        public boolean isPassthru() {
            return true;
        }

        @Override
        public PolicyTransformer getPolicyTransformer() {
            return null;
        }

        @Override
        public NhinAggregator getAggregator() {
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
        public OutboundDelegate getDelegate() {
            return getNhinDelegate();
        }
    }
}
