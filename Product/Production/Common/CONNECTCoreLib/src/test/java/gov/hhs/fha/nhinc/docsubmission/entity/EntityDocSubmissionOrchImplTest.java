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

package gov.hhs.fha.nhinc.docsubmission.entity;

import static org.junit.Assert.*;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.docsubmission.XDRAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.XDRPolicyChecker;
import gov.hhs.fha.nhinc.gateway.aggregator.document.DocumentConstants;
import gov.hhs.fha.nhinc.transform.policy.SubjectHelper;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

public class EntityDocSubmissionOrchImplTest {

    protected Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
    final XDRAuditLogger mockXDRLog = context.mock(XDRAuditLogger.class);
    final XDRPolicyChecker mockPolicyCheck = context.mock(XDRPolicyChecker.class);
    final SubjectHelper mockSubjectHelper = context.mock(SubjectHelper.class);
    final OutboundDocSubmissionDelegate mockDelegate = context.mock(OutboundDocSubmissionDelegate.class);

    @Test
    public void testProvideAndRegisterDocumentSetB() {
        expect4MockAudits();
        allowAnyMockLogging();
        setMockPolicyCheck(true);
        setMockSubjectHelperToReturnValidHcid();
        setMockDelegateToReturnValidResponse();

        RegistryResponseType response = runProvideAndRegisterDocumentSetB();

        context.assertIsSatisfied();
        assertNotNull(response);
        assertEquals(DocumentConstants.XDS_SUBMISSION_RESPONSE_STATUS_SUCCESS, response.getStatus());
    }

    @Test
    public void testProvideAndRegisterDocumentSetB_policyFailure() {
        expect2MockAudits();
        allowAnyMockLogging();
        setMockPolicyCheck(false);
        setMockSubjectHelperToReturnValidHcid();

        RegistryResponseType response = runProvideAndRegisterDocumentSetB();

        context.assertIsSatisfied();
        assertNotNull(response);
        assertTrue(response.getRegistryErrorList().getRegistryError().size() > 0);
        assertEquals(DocumentConstants.XDS_SUBMISSION_RESPONSE_STATUS_FAILURE, response.getStatus());
    }

    @Test
    public void testProvideAndRegisterDocumentSetB_emptyTargets() {
        expect2MockAudits();
        allowAnyMockLogging();
        
        RegistryResponseType response = runProvideAndRegisterDocumentSetB_emptyTargets();

        context.assertIsSatisfied();
        assertNotNull(response);
        assertTrue(response.getRegistryErrorList().getRegistryError().size() > 0);
        assertEquals(DocumentConstants.XDS_SUBMISSION_RESPONSE_STATUS_FAILURE, response.getStatus());
    }

    @Test
    public void testProvideAndRegisterDocumentSetB_failedNhinCall() {
        expect3MockAudits();
        allowAnyMockLogging();
        setMockPolicyCheck(true);
        setMockSubjectHelperToReturnValidHcid();
        setMockDelegateToThrowException();

        RegistryResponseType response = runProvideAndRegisterDocumentSetB();

        context.assertIsSatisfied();
        assertNotNull(response);
        assertTrue(response.getRegistryErrorList().getRegistryError().size() > 0);
        assertEquals(DocumentConstants.XDS_SUBMISSION_RESPONSE_STATUS_FAILURE, response.getStatus());
    }

    @Test
    public void testHasNhinTargetHomeCommunityId() {
        EntityDocSubmissionOrchImpl entityOrch = createEntityDocSubmissionOrchImpl();

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
        EntityDocSubmissionOrchImpl entityOrch = new EntityDocSubmissionOrchImpl();

        assertNotNull(entityOrch.getLogger());
        assertNotNull(entityOrch.getOutboundDocSubmissionDelegate());
        assertNotNull(entityOrch.getSubjectHelper());
        assertNotNull(entityOrch.getXDRAuditLogger());
        assertNotNull(entityOrch.getXDRPolicyChecker());
    }

    private RegistryResponseType runProvideAndRegisterDocumentSetB() {
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        NhinTargetCommunitiesType targets = createNhinTargetCommunitiesType();
        UrlInfoType urlInfo = new UrlInfoType();

        EntityDocSubmissionOrchImpl entityOrch = createEntityDocSubmissionOrchImpl();
        return entityOrch.provideAndRegisterDocumentSetB(request, assertion, targets, urlInfo);
    }

    private RegistryResponseType runProvideAndRegisterDocumentSetB_emptyTargets() {
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        NhinTargetCommunitiesType targets = new NhinTargetCommunitiesType();
        UrlInfoType urlInfo = new UrlInfoType();

        EntityDocSubmissionOrchImpl entityOrch = createEntityDocSubmissionOrchImpl();
        return entityOrch.provideAndRegisterDocumentSetB(request, assertion, targets, urlInfo);
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

    private OutboundDocSubmissionOrchestratable createOutboundDocSubmissionOrchestratable() {
        RegistryResponseType response = new RegistryResponseType();
        response.setStatus(DocumentConstants.XDS_SUBMISSION_RESPONSE_STATUS_SUCCESS);

        OutboundDocSubmissionOrchestratable orchestratable = new OutboundDocSubmissionOrchestratable(null);
        orchestratable.setResponse(response);

        return orchestratable;
    }

    private void expect4MockAudits() {
        context.checking(new Expectations() {
            {
                oneOf(mockXDRLog).auditEntityXDR(
                        with(any(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType.class)),
                        with(any(AssertionType.class)), with(any(String.class)));

                oneOf(mockXDRLog).auditEntityXDRResponse(with(any(RegistryResponseType.class)),
                        with(any(AssertionType.class)), with(any(String.class)));

                oneOf(mockXDRLog)
                        .auditXDR(
                                with(any(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType.class)),
                                with(any(AssertionType.class)), with(any(String.class)));

                oneOf(mockXDRLog).auditNhinXDRResponse(with(any(RegistryResponseType.class)),
                        with(any(AssertionType.class)), with(any(String.class)));
            }
        });
    }
    
    private void expect3MockAudits() {
        context.checking(new Expectations() {
            {
                oneOf(mockXDRLog).auditEntityXDR(
                        with(any(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType.class)),
                        with(any(AssertionType.class)), with(any(String.class)));

                oneOf(mockXDRLog).auditEntityXDRResponse(with(any(RegistryResponseType.class)),
                        with(any(AssertionType.class)), with(any(String.class)));

                oneOf(mockXDRLog)
                        .auditXDR(
                                with(any(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType.class)),
                                with(any(AssertionType.class)), with(any(String.class)));
            }
        });
    }
    
    private void expect2MockAudits() {
        context.checking(new Expectations() {
            {
                oneOf(mockXDRLog).auditEntityXDR(
                        with(any(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType.class)),
                        with(any(AssertionType.class)), with(any(String.class)));

                oneOf(mockXDRLog).auditEntityXDRResponse(with(any(RegistryResponseType.class)),
                        with(any(AssertionType.class)), with(any(String.class)));
            }
        });
    }

    private void allowAnyMockLogging() {
        context.checking(new Expectations() {
            {
                ignoring(mockLog);
            }
        });
    }

    private void setMockPolicyCheck(final boolean allow) {
        context.checking(new Expectations() {
            {
                oneOf(mockPolicyCheck).checkXDRRequestPolicy(with(any(ProvideAndRegisterDocumentSetRequestType.class)),
                        with(any(AssertionType.class)), with(any(String.class)), with(any(String.class)),
                        with(any(String.class)));
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
                oneOf(mockDelegate).process(with(any(OutboundDocSubmissionOrchestratable.class)));
                will(returnValue(createOutboundDocSubmissionOrchestratable()));
            }
        });
    }

    private void setMockDelegateToThrowException() {
        context.checking(new Expectations() {
            {
                oneOf(mockDelegate).process(with(any(OutboundDocSubmissionOrchestratable.class)));
                will(throwException(new Exception()));
            }
        });
    }

    private EntityDocSubmissionOrchImpl createEntityDocSubmissionOrchImpl() {
        return new EntityDocSubmissionOrchImpl() {
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

            protected OutboundDocSubmissionDelegate getOutboundDocSubmissionDelegate() {
                return mockDelegate;
            }
        };
    }

}
