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
package gov.hhs.fha.nhinc.docsubmission.entity.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.document.DocumentConstants;
import gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author akong
 *
 */
public class OutboundDocSubmissionDeferredRequestStrategyTest {
    protected Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final gov.hhs.fha.nhinc.docsubmission.nhin.deferred.request.proxy11.NhinDocSubmissionDeferredRequestProxy mockProxy11 = context
    .mock(gov.hhs.fha.nhinc.docsubmission.nhin.deferred.request.proxy11.NhinDocSubmissionDeferredRequestProxy.class, "G0 Proxy");

    final gov.hhs.fha.nhinc.docsubmission.nhin.deferred.request.proxy20.NhinDocSubmissionDeferredRequestProxy mockProxy20 = context
            .mock(gov.hhs.fha.nhinc.docsubmission.nhin.deferred.request.proxy20.NhinDocSubmissionDeferredRequestProxy.class, "G1 Proxy");

    @Test
    public void testExecute_g0() {
        expectMockProxyInvokeWith11();

        testExecute(createOutboundDocSubmissionDeferredRequestStrategyImpl_g0());
    }

    @Test
    public void testExecute_g1() {
        expectMockProxyInvokeWith20();

        testExecute(createOutboundDocSubmissionDeferredRequestStrategyImpl_g1());
    }

    @Test
    public void testGetters() {
        OutboundDocSubmissionDeferredRequestStrategyImpl_g0 strategy_g0 = createOutboundDocSubmissionDeferredRequestStrategyImpl_g0();
        assertNotNull(strategy_g0.getNhinDocSubmissionDeferredRequestProxy());

        OutboundDocSubmissionDeferredRequestStrategyImpl_g1 strategy_g1 = createOutboundDocSubmissionDeferredRequestStrategyImpl_g1();
        assertNotNull(strategy_g1.getNhinDocSubmissionDeferredRequestProxy());
    }

    public void testExecute(OrchestrationStrategy strategy) {
        strategy.execute(null);

        OutboundDocSubmissionDeferredRequestOrchestratable message = new OutboundDocSubmissionDeferredRequestOrchestratable(
                null);
        strategy.execute(message);
        assertEquals(DocumentConstants.XDS_SUBMISSION_RESPONSE_STATUS_SUCCESS, message.getResponse().getMessage()
                .getStatus());
    }

    private void expectMockProxyInvokeWith11() {
        context.checking(new Expectations() {
            {
                oneOf(mockProxy11).provideAndRegisterDocumentSetBRequest11(
                        with(aNull(ProvideAndRegisterDocumentSetRequestType.class)), with(aNull(AssertionType.class)),
                        with(aNull(NhinTargetSystemType.class)));
                will(returnValue(createXDRAcknowledgementType()));
            }
        });
    }

    private void expectMockProxyInvokeWith20() {
        context.checking(new Expectations() {
            {
                oneOf(mockProxy20).provideAndRegisterDocumentSetBRequest20(
                        with(aNull(ProvideAndRegisterDocumentSetRequestType.class)), with(aNull(AssertionType.class)),
                        with(aNull(NhinTargetSystemType.class)));
                will(returnValue(createRegistryResponseType()));
            }
        });
    }

    private XDRAcknowledgementType createXDRAcknowledgementType() {
        RegistryResponseType regResponse = createRegistryResponseType();

        XDRAcknowledgementType response = new XDRAcknowledgementType();
        response.setMessage(regResponse);

        return response;
    }

    private RegistryResponseType createRegistryResponseType() {
        RegistryResponseType regResponse = new RegistryResponseType();
        regResponse.setStatus(DocumentConstants.XDS_SUBMISSION_RESPONSE_STATUS_SUCCESS);

        return regResponse;
    }

    private OutboundDocSubmissionDeferredRequestStrategyImpl_g0 createOutboundDocSubmissionDeferredRequestStrategyImpl_g0() {
        return new OutboundDocSubmissionDeferredRequestStrategyImpl_g0() {
            @Override
            protected gov.hhs.fha.nhinc.docsubmission.nhin.deferred.request.proxy11.NhinDocSubmissionDeferredRequestProxy getNhinDocSubmissionDeferredRequestProxy() {
                return mockProxy11;
            }
        };
    }

    private OutboundDocSubmissionDeferredRequestStrategyImpl_g1 createOutboundDocSubmissionDeferredRequestStrategyImpl_g1() {
        return new OutboundDocSubmissionDeferredRequestStrategyImpl_g1() {
            @Override
            protected gov.hhs.fha.nhinc.docsubmission.nhin.deferred.request.proxy20.NhinDocSubmissionDeferredRequestProxy getNhinDocSubmissionDeferredRequestProxy() {
                return mockProxy20;
            }
        };
    }
}
