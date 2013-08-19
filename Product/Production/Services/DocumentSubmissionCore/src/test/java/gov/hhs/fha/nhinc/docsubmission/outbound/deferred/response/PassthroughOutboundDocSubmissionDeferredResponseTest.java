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

package gov.hhs.fha.nhinc.docsubmission.outbound.deferred.response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import gov.hhs.fha.nhinc.aspect.OutboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.docsubmission.aspect.DeferredResponseDescriptionBuilder;
import gov.hhs.fha.nhinc.docsubmission.aspect.DocSubmissionArgTransformerBuilder;
import gov.hhs.fha.nhinc.docsubmission.entity.deferred.response.OutboundDocSubmissionDeferredResponseDelegate;
import gov.hhs.fha.nhinc.docsubmission.entity.deferred.response.OutboundDocSubmissionDeferredResponseOrchestratable;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;

import java.lang.reflect.Method;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

/**
 * @author akong
 * 
 */
public class PassthroughOutboundDocSubmissionDeferredResponseTest {
    protected Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final OutboundDocSubmissionDeferredResponseDelegate mockDelegate = context
            .mock(OutboundDocSubmissionDeferredResponseDelegate.class);

    @Test
    public void testProvideAndRegisterDocumentSetB() {
        expectMockDelegateProcessAndReturnValidResponse();

        XDRAcknowledgementType response = runProvideAndRegisterDocumentSetBResponse();

        context.assertIsSatisfied();
        assertNotNull(response);
        assertEquals(NhincConstants.XDR_ACK_STATUS_MSG, response.getMessage().getStatus());
    }

    @Test
    public void testGetters() {
        PassthroughOutboundDocSubmissionDeferredResponse passthruOrch = new PassthroughOutboundDocSubmissionDeferredResponse();

        assertNotNull(passthruOrch.getOutboundDocSubmissionDeferredResponseDelegate());        
    }

    private XDRAcknowledgementType runProvideAndRegisterDocumentSetBResponse() {
        RegistryResponseType request = new RegistryResponseType();
        AssertionType assertion = new AssertionType();
        NhinTargetCommunitiesType targetCommunities = new NhinTargetCommunitiesType();

        PassthroughOutboundDocSubmissionDeferredResponse passthruOrch = createPassthruDocSubmissionDeferredResponseOrchImpl();
        return passthruOrch.provideAndRegisterDocumentSetBAsyncResponse(request, assertion, targetCommunities);
    }

    private void expectMockDelegateProcessAndReturnValidResponse() {
        context.checking(new Expectations() {
            {
                oneOf(mockDelegate).process(with(any(OutboundDocSubmissionDeferredResponseOrchestratable.class)));
                will(returnValue(createOutboundDocSubmissionDeferredResponseOrchestratable()));
            }
        });
    }

    private OutboundDocSubmissionDeferredResponseOrchestratable createOutboundDocSubmissionDeferredResponseOrchestratable() {
        RegistryResponseType regResponse = new RegistryResponseType();
        regResponse.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);

        XDRAcknowledgementType response = new XDRAcknowledgementType();
        response.setMessage(regResponse);

        OutboundDocSubmissionDeferredResponseOrchestratable orchestratable = new OutboundDocSubmissionDeferredResponseOrchestratable(
                null);
        orchestratable.setResponse(response);

        return orchestratable;
    }

    private PassthroughOutboundDocSubmissionDeferredResponse createPassthruDocSubmissionDeferredResponseOrchImpl() {
        return new PassthroughOutboundDocSubmissionDeferredResponse() {
            protected OutboundDocSubmissionDeferredResponseDelegate getOutboundDocSubmissionDeferredResponseDelegate() {
                return mockDelegate;
            }
        };
    }

    @Test
    public void hasOutboundProcessingEvent() throws Exception {
        Class<PassthroughOutboundDocSubmissionDeferredResponse> clazz = PassthroughOutboundDocSubmissionDeferredResponse.class;
        Method method = clazz.getMethod("provideAndRegisterDocumentSetBAsyncResponse", RegistryResponseType.class,
                AssertionType.class, NhinTargetCommunitiesType.class);
        OutboundProcessingEvent annotation = method.getAnnotation(OutboundProcessingEvent.class);
        assertNotNull(annotation);
        assertEquals(DeferredResponseDescriptionBuilder.class, annotation.beforeBuilder());
        assertEquals(DocSubmissionArgTransformerBuilder.class, annotation.afterReturningBuilder());
        assertEquals("Document Submission Deferred Response", annotation.serviceType());
        assertEquals("", annotation.version());
    }
}
