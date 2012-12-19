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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.hhs.fha.nhinc.aspect.OutboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType;
import gov.hhs.fha.nhinc.docsubmission.XDRAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.XDRPolicyChecker;
import gov.hhs.fha.nhinc.docsubmission.aspect.DeferredResponseDescriptionBuilder;
import gov.hhs.fha.nhinc.docsubmission.aspect.DocSubmissionArgTransformerBuilder;
import gov.hhs.fha.nhinc.docsubmission.entity.deferred.response.OutboundDocSubmissionDeferredResponseDelegate;
import gov.hhs.fha.nhinc.docsubmission.entity.deferred.response.OutboundDocSubmissionDeferredResponseOrchestratable;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.transform.policy.SubjectHelper;
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
public class StandardOutboundDocSubmissionDeferredResponseTest {
    protected Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final XDRAuditLogger mockXDRLog = context.mock(XDRAuditLogger.class);
    final XDRPolicyChecker mockPolicyCheck = context.mock(XDRPolicyChecker.class);
    final SubjectHelper mockSubjectHelper = context.mock(SubjectHelper.class);
    final OutboundDocSubmissionDeferredResponseDelegate mockDelegate = context
            .mock(OutboundDocSubmissionDeferredResponseDelegate.class);

    @Test
    public void testProvideAndRegisterDocumentSetB() {
        expect2MockAudits();
        setMockPolicyCheck(true);
        setMockSubjectHelperToReturnValidHcid();
        setMockDelegateToReturnValidResponse();

        XDRAcknowledgementType response = runProvideAndRegisterDocumentSetBAsyncRequest();

        context.assertIsSatisfied();
        assertNotNull(response);
        assertEquals(NhincConstants.XDR_RESP_ACK_STATUS_MSG, response.getMessage().getStatus());
    }

    @Test
    public void testProvideAndRegisterDocumentSetB_policyFailure() {
        expect2MockAudits();
        setMockPolicyCheck(false);
        setMockSubjectHelperToReturnValidHcid();

        XDRAcknowledgementType response = runProvideAndRegisterDocumentSetBAsyncRequest();

        context.assertIsSatisfied();
        assertNotNull(response);
        assertEquals(NhincConstants.XDR_ACK_FAILURE_STATUS_MSG, response.getMessage().getStatus());
    }

    @Test
    public void testProvideAndRegisterDocumentSetB_emptyTargets() {
        expect2MockAudits();
        
        XDRAcknowledgementType response = runProvideAndRegisterDocumentSetBAsyncRequest_emptyTargets();

        context.assertIsSatisfied();
        assertNotNull(response);
        assertEquals(NhincConstants.XDR_ACK_FAILURE_STATUS_MSG, response.getMessage().getStatus());
    }

    @Test
    public void testHasNhinTargetHomeCommunityId() {
        StandardOutboundDocSubmissionDeferredResponse entityOrch = createEntityDocSubmissionDeferredResponseOrchImpl();

        boolean hasTargets = entityOrch.hasNhinTargetHomeCommunityId(null);
        assertFalse(hasTargets);

        RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request = new RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType();
        hasTargets = entityOrch.hasNhinTargetHomeCommunityId(request);
        assertFalse(hasTargets);

        NhinTargetCommunitiesType targetCommunities = new NhinTargetCommunitiesType();
        request.setNhinTargetCommunities(targetCommunities);
        hasTargets = entityOrch.hasNhinTargetHomeCommunityId(request);
        assertFalse(hasTargets);

        request.getNhinTargetCommunities().getNhinTargetCommunity().add(null);
        request.setNhinTargetCommunities(targetCommunities);
        hasTargets = entityOrch.hasNhinTargetHomeCommunityId(request);
        assertFalse(hasTargets);

        targetCommunities = createNhinTargetCommunitiesType();
        request.setNhinTargetCommunities(targetCommunities);
        hasTargets = entityOrch.hasNhinTargetHomeCommunityId(request);
        assertTrue(hasTargets);

        request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity().setHomeCommunityId(null);
        hasTargets = entityOrch.hasNhinTargetHomeCommunityId(request);
        assertFalse(hasTargets);

        request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).setHomeCommunity(null);
        hasTargets = entityOrch.hasNhinTargetHomeCommunityId(request);
        assertFalse(hasTargets);
    }

    @Test
    public void testGetters() {
        StandardOutboundDocSubmissionDeferredResponse entityOrch = new StandardOutboundDocSubmissionDeferredResponse();

        assertNotNull(entityOrch.getSubjectHelper());
        assertNotNull(entityOrch.getXDRAuditLogger());
        assertNotNull(entityOrch.getXDRPolicyChecker());
    }

    private XDRAcknowledgementType runProvideAndRegisterDocumentSetBAsyncRequest() {
        RegistryResponseType request = new RegistryResponseType();
        AssertionType assertion = new AssertionType();
        NhinTargetCommunitiesType targets = createNhinTargetCommunitiesType();

        StandardOutboundDocSubmissionDeferredResponse entityOrch = createEntityDocSubmissionDeferredResponseOrchImpl();
        return entityOrch.provideAndRegisterDocumentSetBAsyncResponse(request, assertion, targets);
    }

    private XDRAcknowledgementType runProvideAndRegisterDocumentSetBAsyncRequest_emptyTargets() {
        RegistryResponseType request = new RegistryResponseType();
        AssertionType assertion = new AssertionType();
        NhinTargetCommunitiesType targets = new NhinTargetCommunitiesType();

        StandardOutboundDocSubmissionDeferredResponse entityOrch = createEntityDocSubmissionDeferredResponseOrchImpl();
        return entityOrch.provideAndRegisterDocumentSetBAsyncResponse(request, assertion, targets);
    }

    private NhinTargetCommunitiesType createNhinTargetCommunitiesType() {
        NhinTargetCommunityType target = new NhinTargetCommunityType();
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId("1.1");
        target.setHomeCommunity(homeCommunity);

        NhinTargetCommunitiesType targets = new NhinTargetCommunitiesType();
        targets.getNhinTargetCommunity().add(target);

        return targets;
    }

    private void expect2MockAudits() {
        context.checking(new Expectations() {
            {
                oneOf(mockXDRLog).auditEntityXDRResponseRequest(
                        with(any(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType.class)),
                        with(any(AssertionType.class)), with(equal(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION)));

                oneOf(mockXDRLog).auditEntityAcknowledgement(with(any(XDRAcknowledgementType.class)),
                        with(any(AssertionType.class)), with(equal(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION)),
                        with(equal(NhincConstants.XDR_RESPONSE_ACTION)));
            }
        });
    }

    private void setMockPolicyCheck(final boolean allow) {
        context.checking(new Expectations() {
            {
                oneOf(mockPolicyCheck).checkXDRResponsePolicy(with(any(RegistryResponseType.class)),
                        with(any(AssertionType.class)), with(any(String.class)), with(any(String.class)),
                        with(equal(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION)));
                will(returnValue(allow));
            }
        });
    }

    private void setMockSubjectHelperToReturnValidHcid() {
        context.checking(new Expectations() {
            {
                oneOf(mockSubjectHelper).determineSendingHomeCommunityId(with(any(HomeCommunityType.class)),
                        with(any(AssertionType.class)));
                will(returnValue("2.2"));
            }
        });
    }

    private void setMockDelegateToReturnValidResponse() {
        context.checking(new Expectations() {
            {
                oneOf(mockDelegate).process(with(any(OutboundDocSubmissionDeferredResponseOrchestratable.class)));
                will(returnValue(createOutboundDocSubmissionDeferredResponseOrchestratable()));
            }
        });
    }

    private OutboundDocSubmissionDeferredResponseOrchestratable createOutboundDocSubmissionDeferredResponseOrchestratable() {
        RegistryResponseType regResponse = new RegistryResponseType();
        regResponse.setStatus(NhincConstants.XDR_RESP_ACK_STATUS_MSG);

        XDRAcknowledgementType response = new XDRAcknowledgementType();
        response.setMessage(regResponse);

        OutboundDocSubmissionDeferredResponseOrchestratable orchestratable = new OutboundDocSubmissionDeferredResponseOrchestratable(
                null);
        orchestratable.setResponse(response);

        return orchestratable;
    }

    private StandardOutboundDocSubmissionDeferredResponse createEntityDocSubmissionDeferredResponseOrchImpl() {
        return new StandardOutboundDocSubmissionDeferredResponse() {
            protected XDRAuditLogger getXDRAuditLogger() {
                return mockXDRLog;
            }

            protected XDRPolicyChecker getXDRPolicyChecker() {
                return mockPolicyCheck;
            }

            protected SubjectHelper getSubjectHelper() {
                return mockSubjectHelper;
            }

            protected OutboundDocSubmissionDeferredResponseDelegate getOutboundDocSubmissionDeferredResponseDelegate() {
                return mockDelegate;
            }
        };
    }

    @Test
    public void hasOutboundProcessingEvent() throws Exception {
        Class<StandardOutboundDocSubmissionDeferredResponse> clazz = StandardOutboundDocSubmissionDeferredResponse.class;
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
