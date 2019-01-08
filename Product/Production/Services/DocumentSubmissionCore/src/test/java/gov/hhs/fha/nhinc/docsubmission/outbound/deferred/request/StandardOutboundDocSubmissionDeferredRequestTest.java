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
package gov.hhs.fha.nhinc.docsubmission.outbound.deferred.request;

import gov.hhs.fha.nhinc.aspect.OutboundProcessingEvent;
import gov.hhs.fha.nhinc.audit.ejb.AuditEJBLogger;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.docsubmission.XDRPolicyChecker;
import gov.hhs.fha.nhinc.docsubmission.aspect.DocSubmissionArgTransformerBuilder;
import gov.hhs.fha.nhinc.docsubmission.aspect.DocSubmissionBaseEventDescriptionBuilder;
import gov.hhs.fha.nhinc.docsubmission.audit.DocSubmissionDeferredRequestAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.audit.transform.DocSubmissionDeferredRequestAuditTransforms;
import gov.hhs.fha.nhinc.docsubmission.entity.deferred.request.OutboundDocSubmissionDeferredRequestDelegate;
import gov.hhs.fha.nhinc.docsubmission.entity.deferred.request.OutboundDocSubmissionDeferredRequestOrchestratable;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.transform.policy.SubjectHelper;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.lang.reflect.Method;
import java.util.Properties;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author akong
 *
 */
public class StandardOutboundDocSubmissionDeferredRequestTest {

    private static final String SENDER_HCID = "1.1";
    private static final String RECEIVER_HCID = "2.2";
    private AuditEJBLogger mockEJBLogger;
    private XDRPolicyChecker mockPolicyChecker;
    private SubjectHelper mockSubjectHelper;
    private OutboundDocSubmissionDeferredRequestDelegate mockDelegate;

    @Before()
    public void setup() {
        mockEJBLogger = mock(AuditEJBLogger.class);
        mockPolicyChecker = mock(XDRPolicyChecker.class);
        mockSubjectHelper = mock(SubjectHelper.class);
        mockDelegate = mock(OutboundDocSubmissionDeferredRequestDelegate.class);
    }

    @Test
    public void testProvideAndRegisterDocumentSetB() {
        NhinTargetCommunitiesType targets = createNhinTargetCommunitiesType(RECEIVER_HCID);
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        final AssertionType assertion = createAssertion(SENDER_HCID);
        UrlInfoType urlInfo = new UrlInfoType();

        when(mockPolicyChecker.checkXDRRequestPolicy(any(ProvideAndRegisterDocumentSetRequestType.class),
            any(AssertionType.class), any(String.class), any(String.class),
            eq(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION))).thenReturn(Boolean.TRUE);

        when(mockDelegate.process(any(OutboundDocSubmissionDeferredRequestOrchestratable.class))).thenReturn(
            createOutboundDocSubmissionDeferredRequestOrchestratable(Boolean.TRUE));

        StandardOutboundDocSubmissionDeferredRequest dsDeferredReq = createDocSubmissionDeferredRequestOrchImpl(
            getAuditLogger(true));
        XDRAcknowledgementType response = dsDeferredReq.provideAndRegisterDocumentSetBAsyncRequest(request, assertion,
            targets, urlInfo);

        assertNotNull(response);
        assertEquals(NhincConstants.XDR_ACK_STATUS_MSG, response.getMessage().getStatus());
        assertNotNull("Assertion MessageId is null", assertion.getMessageId());
        verify(mockEJBLogger).auditRequestMessage(eq(request), eq(assertion), any(NhinTargetSystemType.class),
            eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.TRUE), isNull(Properties.class), eq(NhincConstants.NHINC_XDR_REQUEST_SERVICE_NAME),
            any(DocSubmissionDeferredRequestAuditTransforms.class));
    }

    @Test
    public void testProvideAndRegisterDocumentSetB_policyFailure() {
        NhinTargetCommunitiesType targets = createNhinTargetCommunitiesType(RECEIVER_HCID);
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        final AssertionType assertion = createAssertion(SENDER_HCID);
        UrlInfoType urlInfo = new UrlInfoType();

        when(mockPolicyChecker.checkXDRRequestPolicy(any(ProvideAndRegisterDocumentSetRequestType.class),
            any(AssertionType.class), any(String.class), any(String.class),
            eq(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION))).thenReturn(Boolean.FALSE);

        when(mockDelegate.process(any(OutboundDocSubmissionDeferredRequestOrchestratable.class))).thenReturn(
            createOutboundDocSubmissionDeferredRequestOrchestratable(Boolean.FALSE));

        StandardOutboundDocSubmissionDeferredRequest dsDeferredReq = createDocSubmissionDeferredRequestOrchImpl(
            getAuditLogger(true));
        XDRAcknowledgementType response = dsDeferredReq.provideAndRegisterDocumentSetBAsyncRequest(request, assertion,
            targets, urlInfo);

        assertNotNull(response);
        assertEquals(NhincConstants.XDR_ACK_FAILURE_STATUS_MSG, response.getMessage().getStatus());
        assertNotNull("Assertion MessageId is null", assertion.getMessageId());
        verify(mockEJBLogger).auditRequestMessage(eq(request), eq(assertion), any(NhinTargetSystemType.class),
            eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.TRUE), isNull(Properties.class), eq(NhincConstants.NHINC_XDR_REQUEST_SERVICE_NAME),
            any(DocSubmissionDeferredRequestAuditTransforms.class));
    }

    @Test
    public void testProvideAndRegisterDocumentSetB_emptyTargets() {
        NhinTargetCommunitiesType targets = new NhinTargetCommunitiesType();
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        final AssertionType assertion = createAssertion(SENDER_HCID);
        UrlInfoType urlInfo = new UrlInfoType();

        when(mockPolicyChecker.checkXDRRequestPolicy(any(ProvideAndRegisterDocumentSetRequestType.class),
            any(AssertionType.class), any(String.class), any(String.class),
            eq(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION))).thenReturn(Boolean.TRUE);

        when(mockDelegate.process(any(OutboundDocSubmissionDeferredRequestOrchestratable.class))).thenReturn(
            createOutboundDocSubmissionDeferredRequestOrchestratable(Boolean.TRUE));

        StandardOutboundDocSubmissionDeferredRequest dsDeferredReq = createDocSubmissionDeferredRequestOrchImpl(
            getAuditLogger(true));
        XDRAcknowledgementType response = dsDeferredReq.provideAndRegisterDocumentSetBAsyncRequest(request, assertion,
            targets, urlInfo);

        assertNotNull(response);
        assertEquals(NhincConstants.XDR_ACK_FAILURE_STATUS_MSG, response.getMessage().getStatus());
        assertNotNull("Assertion MessageId is null", assertion.getMessageId());
        verify(mockEJBLogger).auditRequestMessage(eq(request), eq(assertion), any(NhinTargetSystemType.class),
            eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.TRUE), isNull(Properties.class), eq(NhincConstants.NHINC_XDR_REQUEST_SERVICE_NAME),
            any(DocSubmissionDeferredRequestAuditTransforms.class));
    }

    @Test
    public void testHasNhinTargetHomeCommunityId() {
        StandardOutboundDocSubmissionDeferredRequest entityOrch = createDocSubmissionDeferredRequestOrchImpl(
            getAuditLogger(true));

        assertFalse(entityOrch.hasNhinTargetHomeCommunityId(null));

        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request
            = new RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
        assertFalse(entityOrch.hasNhinTargetHomeCommunityId(request));

        NhinTargetCommunitiesType targetCommunities = new NhinTargetCommunitiesType();
        request.setNhinTargetCommunities(targetCommunities);
        assertFalse(entityOrch.hasNhinTargetHomeCommunityId(request));

        request.getNhinTargetCommunities().getNhinTargetCommunity().add(null);
        request.setNhinTargetCommunities(targetCommunities);
        assertFalse(entityOrch.hasNhinTargetHomeCommunityId(request));

        targetCommunities = createNhinTargetCommunitiesType(RECEIVER_HCID);
        request.setNhinTargetCommunities(targetCommunities);
        assertTrue(entityOrch.hasNhinTargetHomeCommunityId(request));

        request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity().setHomeCommunityId(null);
        assertFalse(entityOrch.hasNhinTargetHomeCommunityId(request));

        request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).setHomeCommunity(null);
        assertFalse(entityOrch.hasNhinTargetHomeCommunityId(request));
    }

    @Test
    public void testGetters() {
        StandardOutboundDocSubmissionDeferredRequest entityOrch = new StandardOutboundDocSubmissionDeferredRequest();

        assertNotNull(entityOrch.getOutboundDocSubmissionDeferredRequestDelegate());
        assertNotNull(entityOrch.getSubjectHelper());
        assertNotNull(entityOrch.getAuditLogger());
        assertNotNull(entityOrch.getXDRPolicyChecker());
    }

    @Test
    public void hasOutboundProcessingEvent() throws Exception {
        Class<StandardOutboundDocSubmissionDeferredRequest> clazz = StandardOutboundDocSubmissionDeferredRequest.class;
        Method method = clazz.getMethod("provideAndRegisterDocumentSetBAsyncRequest",
            ProvideAndRegisterDocumentSetRequestType.class, AssertionType.class, NhinTargetCommunitiesType.class,
            UrlInfoType.class);
        OutboundProcessingEvent annotation = method.getAnnotation(OutboundProcessingEvent.class);
        assertNotNull(annotation);
        assertEquals(DocSubmissionBaseEventDescriptionBuilder.class, annotation.beforeBuilder());
        assertEquals(DocSubmissionArgTransformerBuilder.class, annotation.afterReturningBuilder());
        assertEquals("Document Submission Deferred Request", annotation.serviceType());
        assertEquals("", annotation.version());
    }

    @Test
    public void testAuditLoggingOffForDSDeferredRequest() {
        NhinTargetCommunitiesType targets = createNhinTargetCommunitiesType(RECEIVER_HCID);
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        final AssertionType assertion = createAssertion(SENDER_HCID);
        UrlInfoType urlInfo = new UrlInfoType();

        when(mockPolicyChecker.checkXDRRequestPolicy(any(ProvideAndRegisterDocumentSetRequestType.class),
            any(AssertionType.class), any(String.class), any(String.class),
            eq(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION))).thenReturn(Boolean.TRUE);

        when(mockDelegate.process(any(OutboundDocSubmissionDeferredRequestOrchestratable.class))).thenReturn(
            createOutboundDocSubmissionDeferredRequestOrchestratable(Boolean.TRUE));

        StandardOutboundDocSubmissionDeferredRequest dsDeferredReq = createDocSubmissionDeferredRequestOrchImpl(
            getAuditLogger(false));
        XDRAcknowledgementType response = dsDeferredReq.provideAndRegisterDocumentSetBAsyncRequest(request, assertion,
            targets, urlInfo);

        assertNotNull(response);
        assertEquals(NhincConstants.XDR_ACK_STATUS_MSG, response.getMessage().getStatus());
        assertNotNull("Assertion MessageId is null", assertion.getMessageId());
        verify(mockEJBLogger, never()).auditRequestMessage(eq(request), eq(assertion), any(NhinTargetSystemType.class),
            eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.TRUE), isNull(Properties.class), eq(NhincConstants.NHINC_XDR_REQUEST_SERVICE_NAME),
            any(DocSubmissionDeferredRequestAuditTransforms.class));
    }

    private OutboundDocSubmissionDeferredRequestOrchestratable createOutboundDocSubmissionDeferredRequestOrchestratable(
        Boolean buildSuccessResponse) {

        RegistryResponseType regResponse = new RegistryResponseType();
        if (buildSuccessResponse) {
            regResponse.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
        } else {
            regResponse.setStatus(NhincConstants.XDR_ACK_FAILURE_STATUS_MSG);
        }
        XDRAcknowledgementType response = new XDRAcknowledgementType();
        response.setMessage(regResponse);

        OutboundDocSubmissionDeferredRequestOrchestratable orchestratable
            = new OutboundDocSubmissionDeferredRequestOrchestratable(mockDelegate);
        orchestratable.setResponse(response);

        return orchestratable;
    }

    private StandardOutboundDocSubmissionDeferredRequest createDocSubmissionDeferredRequestOrchImpl(
        final DocSubmissionDeferredRequestAuditLogger auditLogger) {
        return new StandardOutboundDocSubmissionDeferredRequest() {
            @Override
            protected DocSubmissionDeferredRequestAuditLogger getAuditLogger() {
                return auditLogger;
            }

            @Override
            protected XDRPolicyChecker getXDRPolicyChecker() {
                return mockPolicyChecker;
            }

            @Override
            protected SubjectHelper getSubjectHelper() {
                return mockSubjectHelper;
            }

            @Override
            protected OutboundDocSubmissionDeferredRequestDelegate getOutboundDocSubmissionDeferredRequestDelegate() {
                return mockDelegate;
            }
        };
    }

    private AssertionType createAssertion(String hcid) {
        UserType userType = new UserType();
        userType.setOrg(createHomeCommunityType(hcid));
        userType.setPersonName(createPersonNameType());
        userType.setRoleCoded(createCeType());
        userType.setUserName("Wanderson");

        AssertionType assertion = new AssertionType();
        assertion.setUserInfo(userType);
        assertion.setHomeCommunity(getHomeCommunity(hcid));
        return assertion;
    }

    private CeType createCeType() {
        CeType ceType = new CeType();
        ceType.setCode("Code");
        ceType.setCodeSystem("CodeSystem");
        ceType.setCodeSystemVersion("1.1");
        ceType.setDisplayName("DisplayName");
        return ceType;
    }

    private HomeCommunityType createHomeCommunityType(String hcid) {
        HomeCommunityType homeCommunityType = new HomeCommunityType();
        homeCommunityType.setHomeCommunityId(hcid);
        homeCommunityType.setName("DOD");
        homeCommunityType.setDescription("This is DOD Gateway");
        return homeCommunityType;
    }

    private PersonNameType createPersonNameType() {
        PersonNameType personNameType = new PersonNameType();
        personNameType.setFamilyName("Tamney");
        personNameType.setFullName("Erica");
        personNameType.setGivenName("Jasmine");
        personNameType.setPrefix("Ms");
        return personNameType;
    }

    private NhinTargetCommunitiesType createNhinTargetCommunitiesType(String hcid) {
        NhinTargetCommunityType target = new NhinTargetCommunityType();
        target.setHomeCommunity(getHomeCommunity(hcid));

        NhinTargetCommunitiesType targets = new NhinTargetCommunitiesType();
        targets.getNhinTargetCommunity().add(target);

        return targets;
    }

    private HomeCommunityType getHomeCommunity(String hcid) {
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(hcid);
        return homeCommunity;
    }

    private DocSubmissionDeferredRequestAuditLogger getAuditLogger(final boolean isAuditOn) {
        return new DocSubmissionDeferredRequestAuditLogger() {
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
