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
package gov.hhs.fha.nhinc.docsubmission.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docsubmission.nhin.proxy.NhinDocSubmissionProxy;
import gov.hhs.fha.nhinc.document.DocumentConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import static org.junit.Assert.*;
import org.junit.Test;


public class OutboundDocSubmissionStrategyTest {
    protected Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final NhinDocSubmissionProxy mockProxy = context.mock(NhinDocSubmissionProxy.class);

    @Test
    public void testExecute_g0() {
        expectMockProxyInvokeWithG0();

        testExecute(createOutboundDocSubmissionStrategyImpl_g0());

        context.assertIsSatisfied();
    }

    @Test
    public void testExecute_g1() {
        expectMockProxyInvokeWithG1();

        testExecute(createOutboundDocSubmissionStrategyImpl_g1());

        context.assertIsSatisfied();
    }

    public void testExecute(OrchestrationStrategy strategy) {
        strategy.execute(null);
        // Nothing happens when null is passed so can't assert anything

        OutboundDocSubmissionOrchestratable message = new OutboundDocSubmissionOrchestratable(null);
        strategy.execute(message);
        assertEquals(DocumentConstants.XDS_SUBMISSION_RESPONSE_STATUS_SUCCESS, message.getResponse().getStatus());
    }

    @Test
    public void testGetters() {
        OutboundDocSubmissionStrategyImpl_g0 strategy_g0 = createOutboundDocSubmissionStrategyImpl_g0();
        assertNotNull(strategy_g0.getNhinDocSubmissionProxy());

        OutboundDocSubmissionStrategyImpl_g1 strategy_g1 = createOutboundDocSubmissionStrategyImpl_g1();
        assertNotNull(strategy_g1.getNhinDocSubmissionProxy());
    }

    private void expectMockProxyInvokeWithG0() {
        context.checking(new Expectations() {
            {
                oneOf(mockProxy).provideAndRegisterDocumentSetB(with(aNull(ProvideAndRegisterDocumentSetRequestType.class)),
                        with(aNull(AssertionType.class)), with(aNull(NhinTargetSystemType.class)), with(equal(NhincConstants.GATEWAY_API_LEVEL.LEVEL_g0)));
                will(returnValue(createRegistryResponseType()));
            }
        });
    }

    private void expectMockProxyInvokeWithG1() {
        context.checking(new Expectations() {
            {
                oneOf(mockProxy).provideAndRegisterDocumentSetB(with(aNull(ProvideAndRegisterDocumentSetRequestType.class)),
                        with(aNull(AssertionType.class)), with(aNull(NhinTargetSystemType.class)), with(equal(NhincConstants.GATEWAY_API_LEVEL.LEVEL_g1)));
                will(returnValue(createRegistryResponseType()));
            }
        });
    }

    private RegistryResponseType createRegistryResponseType() {
        RegistryResponseType response = new RegistryResponseType();
        response.setStatus(DocumentConstants.XDS_SUBMISSION_RESPONSE_STATUS_SUCCESS);

        return response;
    }

    private OutboundDocSubmissionStrategyImpl_g0 createOutboundDocSubmissionStrategyImpl_g0() {
        return new OutboundDocSubmissionStrategyImpl_g0() {
            @Override
            protected NhinDocSubmissionProxy getNhinDocSubmissionProxy() {
                return mockProxy;
            }
        };
    }

    private OutboundDocSubmissionStrategyImpl_g1 createOutboundDocSubmissionStrategyImpl_g1() {
        return new OutboundDocSubmissionStrategyImpl_g1() {
            @Override
            protected NhinDocSubmissionProxy getNhinDocSubmissionProxy() {
                return mockProxy;
            }
        };
    }
}
