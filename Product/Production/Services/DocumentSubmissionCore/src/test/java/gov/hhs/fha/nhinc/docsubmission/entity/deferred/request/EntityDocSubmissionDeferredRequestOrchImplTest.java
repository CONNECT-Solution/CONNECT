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

package gov.hhs.fha.nhinc.docsubmission.entity.deferred.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.docsubmission.XDRAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.XDRPolicyChecker;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.transform.policy.SubjectHelper;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

/**
 * @author akong
 *
 */
public class EntityDocSubmissionDeferredRequestOrchImplTest {
    
    protected Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
    final XDRAuditLogger mockXDRLog = context.mock(XDRAuditLogger.class);
    final XDRPolicyChecker mockPolicyCheck = context.mock(XDRPolicyChecker.class);
    final SubjectHelper mockSubjectHelper = context.mock(SubjectHelper.class);
    final OutboundDocSubmissionDeferredRequestDelegate mockDelegate = context.mock(OutboundDocSubmissionDeferredRequestDelegate.class);
    
    @Test
    public void testProvideAndRegisterDocumentSetB() {
        expect2MockAudits();
        allowAnyMockLogging();
        setMockPolicyCheck(true);
        setMockSubjectHelperToReturnValidHcid();
        setMockDelegateToReturnValidResponse();

        XDRAcknowledgementType response = runProvideAndRegisterDocumentSetBAsyncRequest();

        context.assertIsSatisfied();
        assertNotNull(response);
        assertEquals(NhincConstants.XDR_ACK_STATUS_MSG, response.getMessage().getStatus());
    }
    
    @Test
    public void testProvideAndRegisterDocumentSetB_policyFailure() {
        expect2MockAudits();
        allowAnyMockLogging();
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
        allowAnyMockLogging();

        XDRAcknowledgementType response = runProvideAndRegisterDocumentSetBAsyncRequest_emptyTargets();

        context.assertIsSatisfied();
        assertNotNull(response);
        assertEquals(NhincConstants.XDR_ACK_FAILURE_STATUS_MSG, response.getMessage().getStatus());
    }
    
    @Test
    public void testHasNhinTargetHomeCommunityId() {
        EntityDocSubmissionDeferredRequestOrchImpl entityOrch = createEntityDocSubmissionDeferredRequestOrchImpl();

        boolean hasTargets = entityOrch.hasNhinTargetHomeCommunityId(null);
        assertFalse(hasTargets);

        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request = new RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();        
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
        EntityDocSubmissionDeferredRequestOrchImpl entityOrch = new EntityDocSubmissionDeferredRequestOrchImpl();

        assertNotNull(entityOrch.getLogger());
        assertNotNull(entityOrch.getOutboundDocSubmissionDeferredRequestDelegate());
        assertNotNull(entityOrch.getSubjectHelper());
        assertNotNull(entityOrch.getXDRAuditLogger());
        assertNotNull(entityOrch.getXDRPolicyChecker());
    }
    
    private XDRAcknowledgementType runProvideAndRegisterDocumentSetBAsyncRequest() {
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        NhinTargetCommunitiesType targets = createNhinTargetCommunitiesType();
        UrlInfoType urlInfo = new UrlInfoType();

        EntityDocSubmissionDeferredRequestOrchImpl entityOrch = createEntityDocSubmissionDeferredRequestOrchImpl();
        return entityOrch.provideAndRegisterDocumentSetBAsyncRequest(request, assertion, targets, urlInfo);
    }
    
    private XDRAcknowledgementType runProvideAndRegisterDocumentSetBAsyncRequest_emptyTargets() {
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        NhinTargetCommunitiesType targets = new NhinTargetCommunitiesType();
        UrlInfoType urlInfo = new UrlInfoType();

        EntityDocSubmissionDeferredRequestOrchImpl entityOrch = createEntityDocSubmissionDeferredRequestOrchImpl();
        return entityOrch.provideAndRegisterDocumentSetBAsyncRequest(request, assertion, targets, urlInfo);
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
    
    private void allowAnyMockLogging() {
        context.checking(new Expectations() {
            {
                ignoring(mockLog);
            }
        });
    }
    
    private void expect2MockAudits() {
        context.checking(new Expectations() {
            {
                oneOf(mockXDRLog).auditEntityXDR(
                        with(any(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType.class)),
                        with(any(AssertionType.class)), with(any(String.class)));

                oneOf(mockXDRLog).auditEntityAcknowledgement(with(any(XDRAcknowledgementType.class)),
                        with(any(AssertionType.class)), with(any(String.class)), with(any(String.class)));
            }
        });
    }

    private void setMockPolicyCheck(final boolean allow) {
        context.checking(new Expectations() {
            {
                oneOf(mockPolicyCheck).checkXDRRequestPolicy(with(any(ProvideAndRegisterDocumentSetRequestType.class)),
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
                oneOf(mockDelegate).process(with(any(OutboundDocSubmissionDeferredRequestOrchestratable.class)));
                will(returnValue(createOutboundDocSubmissionDeferredRequestOrchestratable()));
            }
        });
    }
    
    private OutboundDocSubmissionDeferredRequestOrchestratable createOutboundDocSubmissionDeferredRequestOrchestratable() {
        RegistryResponseType regResponse = new RegistryResponseType();
        regResponse.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
        
        XDRAcknowledgementType response = new XDRAcknowledgementType();
        response.setMessage(regResponse);

        OutboundDocSubmissionDeferredRequestOrchestratable orchestratable = new OutboundDocSubmissionDeferredRequestOrchestratable(null);
        orchestratable.setResponse(response);

        return orchestratable;
    }
    
    private EntityDocSubmissionDeferredRequestOrchImpl createEntityDocSubmissionDeferredRequestOrchImpl() {
        return new EntityDocSubmissionDeferredRequestOrchImpl() {
            protected Log getLogger() {
                return mockLog;
            }

            protected XDRAuditLogger getXDRAuditLogger() {
                return mockXDRLog;
            }

            protected XDRPolicyChecker getXDRPolicyChecker() {
                return mockPolicyCheck;
            }

            protected SubjectHelper getSubjectHelper() {
                return mockSubjectHelper;
            }

            protected OutboundDocSubmissionDeferredRequestDelegate getOutboundDocSubmissionDeferredRequestDelegate() {
                return mockDelegate;
            }
        };
    }
}
