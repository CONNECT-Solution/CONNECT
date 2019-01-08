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
package gov.hhs.fha.nhinc.docsubmission.outbound.deferred.response;

import gov.hhs.fha.nhinc.aspect.OutboundProcessingEvent;
import gov.hhs.fha.nhinc.audit.ejb.AuditEJBLogger;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType;
import gov.hhs.fha.nhinc.docsubmission.XDRPolicyChecker;
import gov.hhs.fha.nhinc.docsubmission.aspect.DeferredResponseDescriptionBuilder;
import gov.hhs.fha.nhinc.docsubmission.aspect.DocSubmissionArgTransformerBuilder;
import gov.hhs.fha.nhinc.docsubmission.audit.DSDeferredResponseAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.audit.transform.DSDeferredResponseAuditTransforms;
import gov.hhs.fha.nhinc.docsubmission.entity.deferred.response.OutboundDocSubmissionDeferredResponseDelegate;
import gov.hhs.fha.nhinc.docsubmission.entity.deferred.response.OutboundDocSubmissionDeferredResponseOrchestratable;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.transform.policy.SubjectHelper;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import java.lang.reflect.Method;
import java.util.Properties;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Matchers.isNull;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author akong
 *
 */
public class StandardOutboundDocSubmissionDeferredResponseTest {

    private final AuditEJBLogger mockEJBLogger = mock(AuditEJBLogger.class);
    final XDRPolicyChecker mockPolicyCheck = mock(XDRPolicyChecker.class);
    final SubjectHelper mockSubjectHelper = mock(SubjectHelper.class);
    final OutboundDocSubmissionDeferredResponseDelegate mockDelegate
        = mock(OutboundDocSubmissionDeferredResponseDelegate.class);

    private final RegistryResponseType request = new RegistryResponseType();
    private final AssertionType assertion = new AssertionType();
    private final NhinTargetCommunitiesType targets = createNhinTargetCommunitiesType();

    @Test
    public void testProvideAndRegisterDocumentSetB() {
        when(mockPolicyCheck.checkXDRResponsePolicy(Mockito.any(RegistryResponseType.class),
            Mockito.any(AssertionType.class),
            Mockito.any(String.class), Mockito.any(String.class),
            eq(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION))).thenReturn(Boolean.TRUE);

        when(mockSubjectHelper.determineSendingHomeCommunityId(Mockito.any(HomeCommunityType.class),
            Mockito.any(AssertionType.class))).thenReturn("2.2");

        when(mockDelegate.process(Mockito.any(OutboundDocSubmissionDeferredResponseOrchestratable.class))).thenReturn(
            createOutboundDocSubmissionDeferredResponseOrchestratable());

        XDRAcknowledgementType response = runProvideAndRegisterDocumentSetBAsyncRequest(getAuditLogger(true));
        assertNotNull("Assertion MessageId is null", assertion.getMessageId());
        verify(mockEJBLogger).auditRequestMessage(eq(request), eq(assertion), isNotNull(NhinTargetSystemType.class),
            eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.TRUE), isNull(Properties.class), eq(NhincConstants.NHINC_XDR_RESPONSE_SERVICE_NAME),
            any(DSDeferredResponseAuditTransforms.class));

        assertNotNull(response);
        assertEquals(NhincConstants.XDR_RESP_ACK_STATUS_MSG, response.getMessage().getStatus());
    }

    @Test
    public void testProvideAndRegisterDocumentSetB_policyFailure() {
        when(mockPolicyCheck.checkXDRResponsePolicy(Mockito.any(RegistryResponseType.class),
            Mockito.any(AssertionType.class),
            Mockito.any(String.class), Mockito.any(String.class),
            eq(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION))).thenReturn(Boolean.FALSE);

        when(mockSubjectHelper.determineSendingHomeCommunityId(Mockito.any(HomeCommunityType.class),
            Mockito.any(AssertionType.class))).thenReturn("2.2");

        XDRAcknowledgementType response = runProvideAndRegisterDocumentSetBAsyncRequest(getAuditLogger(true));
        assertNotNull("Assertion MessageId is null", assertion.getMessageId());
        verify(mockEJBLogger).auditRequestMessage(eq(request), eq(assertion), isNotNull(NhinTargetSystemType.class),
            eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.TRUE), isNull(Properties.class), eq(NhincConstants.NHINC_XDR_RESPONSE_SERVICE_NAME),
            any(DSDeferredResponseAuditTransforms.class));

        assertNotNull(response);
        assertEquals(NhincConstants.XDR_ACK_FAILURE_STATUS_MSG, response.getMessage().getStatus());
    }

    @Test
    public void testProvideAndRegisterDocumentSetB_emptyTargets() {

        RegistryResponseType requestLocal = new RegistryResponseType();
        AssertionType assertionLocal = new AssertionType();
        XDRAcknowledgementType response = runProvideAndRegisterDocumentSetBAsyncRequest_emptyTargets(requestLocal,
            assertionLocal, getAuditLogger(true));
        assertNotNull("Assertion MessageId is null", assertionLocal.getMessageId());
        verify(mockEJBLogger).auditRequestMessage(eq(requestLocal), eq(assertionLocal),
            isNotNull(NhinTargetSystemType.class), eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION),
            eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.TRUE), isNull(Properties.class),
            eq(NhincConstants.NHINC_XDR_RESPONSE_SERVICE_NAME), any(DSDeferredResponseAuditTransforms.class));
        assertNotNull(response);
        assertEquals(NhincConstants.XDR_ACK_FAILURE_STATUS_MSG, response.getMessage().getStatus());
    }

    @Test
    public void testHasNhinTargetHomeCommunityId() {
        StandardOutboundDocSubmissionDeferredResponse entityOrch = createEntityDocSubmissionDeferredResponseOrchImpl(
            getAuditLogger(true));

        assertFalse(entityOrch.hasNhinTargetHomeCommunityId(null));

        RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request
            = new RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType();
        assertFalse(entityOrch.hasNhinTargetHomeCommunityId(request));

        NhinTargetCommunitiesType targetCommunities = new NhinTargetCommunitiesType();
        request.setNhinTargetCommunities(targetCommunities);
        assertFalse(entityOrch.hasNhinTargetHomeCommunityId(request));

        request.getNhinTargetCommunities().getNhinTargetCommunity().add(null);
        request.setNhinTargetCommunities(targetCommunities);
        assertFalse(entityOrch.hasNhinTargetHomeCommunityId(request));

        targetCommunities = createNhinTargetCommunitiesType();
        request.setNhinTargetCommunities(targetCommunities);
        assertTrue(entityOrch.hasNhinTargetHomeCommunityId(request));

        request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity().setHomeCommunityId(null);
        assertFalse(entityOrch.hasNhinTargetHomeCommunityId(request));

        request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).setHomeCommunity(null);
        assertFalse(entityOrch.hasNhinTargetHomeCommunityId(request));
    }

    @Test
    public void testGetters() {
        StandardOutboundDocSubmissionDeferredResponse entityOrch = new StandardOutboundDocSubmissionDeferredResponse();

        assertNotNull(entityOrch.getSubjectHelper());
        assertNotNull(entityOrch.getAuditLogger());
        assertNotNull(entityOrch.getXDRPolicyChecker());
    }

    @Test
    public void testAuditLoggingOffForDSDeferredResponse() {
        when(mockPolicyCheck.checkXDRResponsePolicy(Mockito.any(RegistryResponseType.class),
            Mockito.any(AssertionType.class),
            Mockito.any(String.class), Mockito.any(String.class),
            eq(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION))).thenReturn(Boolean.TRUE);

        when(mockSubjectHelper.determineSendingHomeCommunityId(Mockito.any(HomeCommunityType.class),
            Mockito.any(AssertionType.class))).thenReturn("2.2");

        when(mockDelegate.process(Mockito.any(OutboundDocSubmissionDeferredResponseOrchestratable.class))).thenReturn(
            createOutboundDocSubmissionDeferredResponseOrchestratable());

        XDRAcknowledgementType response = runProvideAndRegisterDocumentSetBAsyncRequest(getAuditLogger(false));
        verify(mockEJBLogger, never()).auditRequestMessage(eq(request), eq(assertion), isNotNull(NhinTargetSystemType.class),
            eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.TRUE), isNull(Properties.class), eq(NhincConstants.NHINC_XDR_RESPONSE_SERVICE_NAME),
            any(DSDeferredResponseAuditTransforms.class));

        assertNotNull(response);
        assertEquals(NhincConstants.XDR_RESP_ACK_STATUS_MSG, response.getMessage().getStatus());
    }

    private XDRAcknowledgementType runProvideAndRegisterDocumentSetBAsyncRequest(
        DSDeferredResponseAuditLogger auditLogger) {

        StandardOutboundDocSubmissionDeferredResponse entityOrch = createEntityDocSubmissionDeferredResponseOrchImpl(
            auditLogger);
        return entityOrch.provideAndRegisterDocumentSetBAsyncResponse(request, assertion, targets);
    }

    private XDRAcknowledgementType runProvideAndRegisterDocumentSetBAsyncRequest_emptyTargets(
        RegistryResponseType requestLocal, AssertionType assertionLocal, DSDeferredResponseAuditLogger auditLogger) {
        NhinTargetCommunitiesType targetLocal = new NhinTargetCommunitiesType();
        StandardOutboundDocSubmissionDeferredResponse entityOrch = createEntityDocSubmissionDeferredResponseOrchImpl(
            auditLogger);
        return entityOrch.provideAndRegisterDocumentSetBAsyncResponse(requestLocal, assertionLocal, targetLocal);
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

    private OutboundDocSubmissionDeferredResponseOrchestratable
        createOutboundDocSubmissionDeferredResponseOrchestratable() {
        RegistryResponseType regResponse = new RegistryResponseType();
        regResponse.setStatus(NhincConstants.XDR_RESP_ACK_STATUS_MSG);

        XDRAcknowledgementType response = new XDRAcknowledgementType();
        response.setMessage(regResponse);

        OutboundDocSubmissionDeferredResponseOrchestratable orchestratable
            = new OutboundDocSubmissionDeferredResponseOrchestratable(null);
        orchestratable.setResponse(response);

        return orchestratable;
    }

    private StandardOutboundDocSubmissionDeferredResponse createEntityDocSubmissionDeferredResponseOrchImpl(
        final DSDeferredResponseAuditLogger auditLogger) {
        return new StandardOutboundDocSubmissionDeferredResponse() {

            @Override
            protected XDRPolicyChecker getXDRPolicyChecker() {
                return mockPolicyCheck;
            }

            @Override
            protected SubjectHelper getSubjectHelper() {
                return mockSubjectHelper;
            }

            @Override
            protected OutboundDocSubmissionDeferredResponseDelegate getOutboundDocSubmissionDeferredResponseDelegate() {
                return mockDelegate;
            }

            @Override
            protected DSDeferredResponseAuditLogger getAuditLogger() {
                return auditLogger;
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

    private DSDeferredResponseAuditLogger getAuditLogger(final boolean isAuditOn) {
        return new DSDeferredResponseAuditLogger() {
            @Override
            protected AuditEJBLogger getAuditLogger() {
                return mockEJBLogger;
            }

            @Override
            protected boolean isAuditLoggingOn(String serviceName) {
                return isAuditOn;
            }
        };
    }
}
