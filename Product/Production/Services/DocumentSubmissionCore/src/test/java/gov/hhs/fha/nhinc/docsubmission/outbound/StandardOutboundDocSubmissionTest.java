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
package gov.hhs.fha.nhinc.docsubmission.outbound;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.aspect.OutboundProcessingEvent;
import gov.hhs.fha.nhinc.audit.ejb.AuditEJBLogger;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.docsubmission.XDRPolicyChecker;
import gov.hhs.fha.nhinc.docsubmission.aspect.DocSubmissionBaseEventDescriptionBuilder;
import gov.hhs.fha.nhinc.docsubmission.audit.DocSubmissionAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.audit.DocSubmissionAuditTransformsConstants;
import gov.hhs.fha.nhinc.docsubmission.audit.transform.DocSubmissionAuditTransforms;
import gov.hhs.fha.nhinc.docsubmission.entity.OutboundDocSubmissionDelegate;
import gov.hhs.fha.nhinc.docsubmission.entity.OutboundDocSubmissionOrchestratable;
import gov.hhs.fha.nhinc.document.DocumentConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.util.MessageGeneratorUtils;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.lang.reflect.Method;
import java.util.Properties;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.InternationalStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.LocalizedStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.junit.Before;
import org.junit.Test;

public class StandardOutboundDocSubmissionTest {

    private final String SUBMISSION_SET_UNIQUE_ID_SCHEME = "urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8";
    private final String PATIENT_ID_SCHEME = "urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446";
    private final String PATIENT_ID = "D123401^^^&1.1&ISO";
    private final String SUBMISSION_SET_UNIQUE_ID = "1.3.6.1.4.1.21367.2005.3.9999.33";

    private final String senderHcid = "1.1";
    private final String receiverHcid = "2.2";

    private final XDRPolicyChecker mockChecker = mock(XDRPolicyChecker.class);
    private final OutboundDocSubmissionDelegate mockDelegate = mock(OutboundDocSubmissionDelegate.class);
    private final OutboundDocSubmissionOrchestratable mockOrchestratable = mock(OutboundDocSubmissionOrchestratable.class);
    private final MessageGeneratorUtils mockUtils = mock(MessageGeneratorUtils.class);
    private final RegistryResponseType successResponse = new RegistryResponseType();
    private final AuditEJBLogger mockEJBLogger = mock(AuditEJBLogger.class);

    @Before
    public void setUp() {
        successResponse.setStatus(DocumentConstants.XDS_SUBMISSION_RESPONSE_STATUS_SUCCESS);
    }

    @Test
    public void testProvideAndRegisterDocumentSetB1() {

        NhinTargetCommunitiesType targets = createNhinTargetCommunitiesType(receiverHcid);
        NhinTargetSystemType target = createNhinTargetSystemType(receiverHcid);
        ProvideAndRegisterDocumentSetRequestType request = createProvideAndRegisterDocumentSetRequestType();

        AssertionType assertion = getAssertion(senderHcid);

        StandardOutboundDocSubmission outbound = getStandardOutboundDocSubmission(getAuditLogger(true), mockChecker,
            mockDelegate, mockOrchestratable, mockUtils, target);

        when(mockUtils.convertFirstToNhinTargetSystemType(targets)).thenReturn(target);
        when(mockChecker.checkXDRRequestPolicy(request, assertion, senderHcid, receiverHcid,
            NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION)).thenReturn(Boolean.TRUE);
        when(mockDelegate.process(mockOrchestratable)).thenReturn(mockOrchestratable);
        when(mockOrchestratable.getResponse()).thenReturn(successResponse);

        RegistryResponseType actualResponse = outbound.provideAndRegisterDocumentSetB(request,
            assertion, targets, new UrlInfoType());
        assertNotNull("Assertion MessageId is null", assertion.getMessageId());
        verify(mockEJBLogger).auditRequestMessage(eq(request), eq(assertion), eq(target),
            eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.TRUE), isNull(Properties.class), eq(NhincConstants.NHINC_XDR_SERVICE_NAME),
            any(DocSubmissionAuditTransforms.class));

        assertNotNull(actualResponse);
        assertEquals(actualResponse.getStatus(), successResponse.getStatus());
    }

    @Test
    public void testProvideAndRegisterDocumentSetB_policyFailure() {

        NhinTargetCommunitiesType targets = createNhinTargetCommunitiesType(receiverHcid);
        NhinTargetSystemType target = createNhinTargetSystemType(receiverHcid);
        ProvideAndRegisterDocumentSetRequestType request = createProvideAndRegisterDocumentSetRequestType();

        AssertionType assertion = getAssertion(senderHcid);

        StandardOutboundDocSubmission outbound = getStandardOutboundDocSubmission(getAuditLogger(true), mockChecker,
            mockDelegate, mockOrchestratable, mockUtils, target);

        when(mockUtils.convertFirstToNhinTargetSystemType(targets)).thenReturn(target);
        when(mockChecker.checkXDRRequestPolicy(request, assertion, senderHcid, receiverHcid,
            NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION)).thenReturn(Boolean.FALSE);

        RegistryResponseType response = outbound.provideAndRegisterDocumentSetB(request,
            assertion, targets, new UrlInfoType());
        assertNotNull("Assertion MessageId is null", assertion.getMessageId());
        verify(mockEJBLogger).auditRequestMessage(eq(request), eq(assertion), eq(target),
            eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.TRUE), isNull(Properties.class), eq(NhincConstants.NHINC_XDR_SERVICE_NAME),
            any(DocSubmissionAuditTransforms.class));

        assertNotNull(response);
        assertTrue(response.getRegistryErrorList().getRegistryError().size() > 0);
        assertEquals(DocumentConstants.XDS_SUBMISSION_RESPONSE_STATUS_FAILURE, response.getStatus());
    }

    @Test
    public void testProvideAndRegisterDocumentSetB_emptyTargets() {

        AssertionType assertion = getAssertion(senderHcid);
        ProvideAndRegisterDocumentSetRequestType request = createProvideAndRegisterDocumentSetRequestType();

        StandardOutboundDocSubmission outbound = getStandardOutboundDocSubmission(getAuditLogger(true), mockChecker,
            mockDelegate, mockOrchestratable, mockUtils, null);

        RegistryResponseType response = outbound.provideAndRegisterDocumentSetB(request,
            assertion, null, new UrlInfoType());
        assertNotNull("Assertion MessageId is null", assertion.getMessageId());
        verify(mockEJBLogger).auditRequestMessage(eq(request), eq(assertion), isNull(NhinTargetSystemType.class),
            eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.TRUE), isNull(Properties.class), eq(NhincConstants.NHINC_XDR_SERVICE_NAME),
            any(DocSubmissionAuditTransforms.class));

        assertNotNull(response);
        assertTrue(response.getRegistryErrorList().getRegistryError().size() > 0);
        assertEquals(DocumentConstants.XDS_SUBMISSION_RESPONSE_STATUS_FAILURE, response.getStatus());
    }

    @Test
    public void testHasNhinTargetHomeCommunityId() {
        StandardOutboundDocSubmission entityOrch = new StandardOutboundDocSubmission();

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

        targetCommunities = createNhinTargetCommunitiesType(receiverHcid);
        request.setNhinTargetCommunities(targetCommunities);
        assertTrue(entityOrch.hasNhinTargetHomeCommunityId(request));

        request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity().setHomeCommunityId(null);
        assertFalse(entityOrch.hasNhinTargetHomeCommunityId(request));

        request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).setHomeCommunity(null);
        assertFalse(entityOrch.hasNhinTargetHomeCommunityId(request));
    }

    @Test
    public void testGetters() {
        StandardOutboundDocSubmission entityOrch = new StandardOutboundDocSubmission();

        assertNotNull(entityOrch.getOutboundDocSubmissionDelegate());
        assertNotNull(entityOrch.getSubjectHelper());
        assertNotNull(entityOrch.getDocSubmissionAuditLogger());
        assertNotNull(entityOrch.getXDRPolicyChecker());
        assertNotNull(entityOrch.getMessageGeneratorUtils());
    }

    @Test
    public void hasOutboundProcessingEvent() throws Exception {
        Class<StandardOutboundDocSubmission> clazz = StandardOutboundDocSubmission.class;
        Method method = clazz.getMethod("provideAndRegisterDocumentSetB",
            ProvideAndRegisterDocumentSetRequestType.class, AssertionType.class, NhinTargetCommunitiesType.class,
            UrlInfoType.class);
        OutboundProcessingEvent annotation = method.getAnnotation(OutboundProcessingEvent.class);
        assertNotNull(annotation);
        assertEquals(DocSubmissionBaseEventDescriptionBuilder.class, annotation.beforeBuilder());
        assertEquals(DocSubmissionBaseEventDescriptionBuilder.class, annotation.afterReturningBuilder());
        assertEquals("Document Submission", annotation.serviceType());
        assertEquals("", annotation.version());
    }

    @Test
    public void testAuditLoggingOffForOutboundDS() {

        NhinTargetCommunitiesType targets = createNhinTargetCommunitiesType(receiverHcid);
        NhinTargetSystemType target = createNhinTargetSystemType(receiverHcid);
        ProvideAndRegisterDocumentSetRequestType request = createProvideAndRegisterDocumentSetRequestType();

        AssertionType assertion = getAssertion(senderHcid);

        StandardOutboundDocSubmission outbound = getStandardOutboundDocSubmission(getAuditLogger(false), mockChecker,
            mockDelegate, mockOrchestratable, mockUtils, target);

        when(mockUtils.convertFirstToNhinTargetSystemType(targets)).thenReturn(target);
        when(mockChecker.checkXDRRequestPolicy(request, assertion, senderHcid, receiverHcid,
            NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION)).thenReturn(Boolean.TRUE);
        when(mockDelegate.process(mockOrchestratable)).thenReturn(mockOrchestratable);
        when(mockOrchestratable.getResponse()).thenReturn(successResponse);

        RegistryResponseType actualResponse = outbound.provideAndRegisterDocumentSetB(request,
            assertion, targets, new UrlInfoType());
        assertNotNull("Assertion MessageId is null", assertion.getMessageId());
        verify(mockEJBLogger, never()).auditRequestMessage(eq(request), eq(assertion), eq(target),
            eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.TRUE), isNull(Properties.class), eq(NhincConstants.NHINC_XDR_SERVICE_NAME),
            any(DocSubmissionAuditTransforms.class));

        assertNotNull(actualResponse);
        assertEquals(actualResponse.getStatus(), successResponse.getStatus());
    }

    private StandardOutboundDocSubmission getStandardOutboundDocSubmission(final DocSubmissionAuditLogger auditLogger,
        final XDRPolicyChecker mockChecker, final OutboundDocSubmissionDelegate mockDelegate,
        final OutboundDocSubmissionOrchestratable mockOrchestratable, final MessageGeneratorUtils mockUtils,
        final NhinTargetSystemType target) {

        return new StandardOutboundDocSubmission() {

            @Override
            protected DocSubmissionAuditLogger getDocSubmissionAuditLogger() {
                return auditLogger;
            }

            @Override
            protected XDRPolicyChecker getXDRPolicyChecker() {
                return mockChecker;
            }

            @Override
            protected OutboundDocSubmissionDelegate getOutboundDocSubmissionDelegate() {
                return mockDelegate;
            }

            @Override
            protected MessageGeneratorUtils getMessageGeneratorUtils() {
                return mockUtils;
            }

            @Override
            protected OutboundDocSubmissionOrchestratable createOrchestratable(OutboundDocSubmissionDelegate delegate,
                gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request,
                AssertionType assertion) {
                return mockOrchestratable;
            }
        };
    }

    private AssertionType getAssertion(String hcid) {
        AssertionType assertion = new AssertionType();
        assertion.setHomeCommunity(new HomeCommunityType());
        assertion.getHomeCommunity().setHomeCommunityId(hcid);
        return assertion;
    }

    private NhinTargetSystemType createNhinTargetSystemType(String hcid) {
        NhinTargetSystemType target = new NhinTargetSystemType();
        target.setHomeCommunity(getHomeCommunity(hcid));
        return target;
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

    private ProvideAndRegisterDocumentSetRequestType createProvideAndRegisterDocumentSetRequestType() {
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        SubmitObjectsRequest submitObjReq = new SubmitObjectsRequest();
        submitObjReq.setRegistryObjectList(new RegistryObjectListType());
        request.setSubmitObjectsRequest(submitObjReq);
        JAXBElement<RegistryObjectType> registryPackage = createIdentifiable(new RegistryObjectType());
        registryPackage.getValue().getExternalIdentifier().add(createExternalIdentifier(PATIENT_ID_SCHEME, PATIENT_ID,
            DocSubmissionAuditTransformsConstants.XDS_SUBMISSIONSET_PATIENT_ID));
        registryPackage.getValue().getExternalIdentifier().add(createExternalIdentifier(SUBMISSION_SET_UNIQUE_ID_SCHEME,
            SUBMISSION_SET_UNIQUE_ID, DocSubmissionAuditTransformsConstants.XDS_SUBMISSIONSET_UNIQUE_ID));

        request.getSubmitObjectsRequest().getRegistryObjectList().getIdentifiable().add(registryPackage);
        return request;
    }

    private JAXBElement<RegistryObjectType> createIdentifiable(RegistryObjectType registryObjectType) {
        JAXBElement<RegistryObjectType> element = new JAXBElement<>(new QName(
            "urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "RegistryPackage"), RegistryObjectType.class,
            registryObjectType);
        return element;
    }

    private ExternalIdentifierType createExternalIdentifier(String idScheme, String value, String displayName) {
        LocalizedStringType strLocalized = new LocalizedStringType();
        strLocalized.setValue(displayName);
        InternationalStringType strInternational = new InternationalStringType();
        strInternational.getLocalizedString().add(strLocalized);
        ExternalIdentifierType extIdentifierType = new ExternalIdentifierType();
        extIdentifierType.setIdentificationScheme(idScheme);
        extIdentifierType.setValue(value);
        extIdentifierType.setName(strInternational);
        return extIdentifierType;
    }

    private DocSubmissionAuditLogger getAuditLogger(final boolean isAuditOn) {
        return new DocSubmissionAuditLogger() {
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
