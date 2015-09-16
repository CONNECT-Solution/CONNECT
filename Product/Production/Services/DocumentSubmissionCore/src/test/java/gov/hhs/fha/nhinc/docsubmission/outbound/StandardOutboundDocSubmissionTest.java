/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import gov.hhs.fha.nhinc.aspect.OutboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.docsubmission.DocSubmissionUtils;
import gov.hhs.fha.nhinc.docsubmission.MessageGeneratorUtils;
import gov.hhs.fha.nhinc.docsubmission.XDRPolicyChecker;
import gov.hhs.fha.nhinc.docsubmission.adapter.proxy.AdapterDocSubmissionProxy;
import gov.hhs.fha.nhinc.docsubmission.adapter.proxy.AdapterDocSubmissionProxyObjectFactory;
import gov.hhs.fha.nhinc.docsubmission.aspect.DocSubmissionBaseEventDescriptionBuilder;
import gov.hhs.fha.nhinc.docsubmission.audit.DocSubmissionAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.audit.DocSubmissionAuditTransformsConstants;
import gov.hhs.fha.nhinc.docsubmission.entity.OutboundDocSubmissionDelegate;
import gov.hhs.fha.nhinc.docsubmission.entity.OutboundDocSubmissionOrchestratable;
import gov.hhs.fha.nhinc.docsubmission.entity.proxy.EntityDocSubmissionProxyJavaImpl;
import gov.hhs.fha.nhinc.docsubmission.inbound.StandardInboundDocSubmission;
import gov.hhs.fha.nhinc.document.DocumentConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.policy.SubjectHelper;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
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

import org.jmock.Expectations;
import static org.jmock.Expectations.any;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.mockito.Matchers;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.mockito.Mockito;
import static org.mockito.Mockito.mock;

public class StandardOutboundDocSubmissionTest {

    private final String SUBMISSION_SET_UNIQUE_ID_SCHEME = "urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8";
    private final String PATIENT_ID_SCHEME = "urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446";
    private final String PATIENT_ID = "D123401^^^&1.1&ISO";
    private final String SUBMISSION_SET_UNIQUE_ID = "1.3.6.1.4.1.21367.2005.3.9999.33";

    /*protected Mockery context = new JUnit4Mockery() {
     {
     setImposteriser(ClassImposteriser.INSTANCE);
     }
     };*/
    //final DocSubmissionAuditLogger mockDocSubmissionLog = context.mock(DocSubmissionAuditLogger.class);
    /*final XDRPolicyChecker mockPolicyCheck = context.mock(XDRPolicyChecker.class);
     final SubjectHelper mockSubjectHelper = context.mock(SubjectHelper.class);
     final OutboundDocSubmissionDelegate mockDelegate = context.mock(OutboundDocSubmissionDelegate.class);*/
    @Test
    public void testProvideAndRegisterDocumentSetB1() throws PropertyAccessException {
        String localHCID = "1.1";
        String senderHCID = "2.2";
        ProvideAndRegisterDocumentSetRequestType request = createProvideAndRegisterDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        assertion.setHomeCommunity(new HomeCommunityType());
        assertion.getHomeCommunity().setHomeCommunityId(localHCID);

        DocSubmissionAuditLogger auditLogger = mock(DocSubmissionAuditLogger.class);
        Properties webContextProperties = null;
        StandardOutboundDocSubmission outbound = new StandardOutboundDocSubmission() {
            @Override
            protected boolean isPolicyValid(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request,
                AssertionType assertion) {
                return true;
            }
        };
        //EntityDocSubmissionProxyJavaImpl entity = new EntityDocSubmissionProxyJavaImpl(standardOutDocSubmission);
        NhinTargetCommunitiesType targets = new NhinTargetCommunitiesType(); //createNhinTargetCommunitiesType();
        RegistryResponseType actualResponse = outbound.provideAndRegisterDocumentSetB(request,
            assertion, targets, new UrlInfoType());
        NhinTargetSystemType target = null;

        /*verify(auditLogger).auditRequestMessage(eq(request), eq(assertion), eq(target),
         eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
         eq(Boolean.FALSE), eq(webContextProperties), eq(NhincConstants.NHINC_XDR_SERVICE_NAME));*/
    }


    /*@Test
     public void testProvideAndRegisterDocumentSetB() {
     expect4MockAudits();
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

     RegistryResponseType response = runProvideAndRegisterDocumentSetB_emptyTargets();

     context.assertIsSatisfied();
     assertNotNull(response);
     assertTrue(response.getRegistryErrorList().getRegistryError().size() > 0);
     assertEquals(DocumentConstants.XDS_SUBMISSION_RESPONSE_STATUS_FAILURE, response.getStatus());
     }

     @Test
     public void testProvideAndRegisterDocumentSetB_failedNhinCall() {
     expect3MockAudits();
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
     StandardOutboundDocSubmission entityOrch = createStandardOutboundDocSubmission();

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
     StandardOutboundDocSubmission entityOrch = new StandardOutboundDocSubmission();

     assertNotNull(entityOrch.getOutboundDocSubmissionDelegate());
     assertNotNull(entityOrch.getSubjectHelper());
     assertNotNull(entityOrch.getDocSubmissionAuditLogger());
     assertNotNull(entityOrch.getXDRPolicyChecker());
     }
     private RegistryResponseType runProvideAndRegisterDocumentSetB() {
     ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
     AssertionType assertion = new AssertionType();
     NhinTargetCommunitiesType targets = createNhinTargetCommunitiesType();
     UrlInfoType urlInfo = new UrlInfoType();

     StandardOutboundDocSubmission entityOrch = createStandardOutboundDocSubmission();
     return entityOrch.provideAndRegisterDocumentSetB(request, assertion, targets, urlInfo);
     }

     private RegistryResponseType runProvideAndRegisterDocumentSetB_emptyTargets() {
     ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
     AssertionType assertion = new AssertionType();
     NhinTargetCommunitiesType targets = new NhinTargetCommunitiesType();
     UrlInfoType urlInfo = new UrlInfoType();

     StandardOutboundDocSubmission entityOrch = createStandardOutboundDocSubmission();
     return entityOrch.provideAndRegisterDocumentSetB(request, assertion, targets, urlInfo);
     }*/
    private NhinTargetSystemType createNhinTargetSystemType() {
        NhinTargetSystemType target = new NhinTargetSystemType();
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId("1.1");
        target.setHomeCommunity(homeCommunity);
        return target;
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
        //context.checking(new Expectations() {
            /*{
         oneOf(mockDocSubmissionLog).auditEntityXDR(
         with(any(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType.class)),
         with(any(AssertionType.class)), with(any(String.class)));

         oneOf(mockDocSubmissionLog).auditEntityXDRResponse(with(any(RegistryResponseType.class)),
         with(any(AssertionType.class)), with(any(String.class)));

         oneOf(mockDocSubmissionLog)
         .auditXDR(
         with(any(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType.class)),
         with(any(AssertionType.class)), with(any(NhinTargetSystemType.class)),
         with(any(String.class)));

         oneOf(mockDocSubmissionLog).auditNhinXDRResponse(with(any(RegistryResponseType.class)),
         with(any(AssertionType.class)), with(any(NhinTargetSystemType.class)), with(any(String.class)),
         with(equal(true)));
         }*/
        /*{
         oneOf(mockDocSubmissionLog).auditRequestMessage(
         with(any(ProvideAndRegisterDocumentSetRequestType.class)), with(any(AssertionType.class)),
         with(any(NhinTargetSystemType.class)), with(any(String.class)), with(any(String.class)),
         with(any(Boolean.class)), with(any(Properties.class)), with(any(String.class)));
         oneOf(mockDocSubmissionLog).auditResponseMessage(with(any(ProvideAndRegisterDocumentSetRequestType.class)),
         with(any(RegistryResponseType.class)), with(any(AssertionType.class)),
         with(any(NhinTargetSystemType.class)), with(any(String.class)), with(any(String.class)),
         with(any(Boolean.class)), with(any(Properties.class)), with(any(String.class)));
         }
         });*/
    }

    private void expect3MockAudits() {
        //context.checking(new Expectations() {
            /*            {
         oneOf(mockDocSubmissionLog).auditEntityXDR(
         with(any(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType.class)),
         with(any(AssertionType.class)), with(any(String.class)));

         oneOf(mockDocSubmissionLog).auditEntityXDRResponse(with(any(RegistryResponseType.class)),
         with(any(AssertionType.class)), with(any(String.class)));

         oneOf(mockDocSubmissionLog)
         .auditXDR(
         with(any(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType.class)),
         with(any(AssertionType.class)), with(any(NhinTargetSystemType.class)),
         with(any(String.class)));
         }*/
        /*{
         oneOf(mockDocSubmissionLog).auditRequestMessage(
         with(any(ProvideAndRegisterDocumentSetRequestType.class)), with(any(AssertionType.class)),
         with(any(NhinTargetSystemType.class)), with(any(String.class)), with(any(String.class)),
         with(any(Boolean.class)), with(any(Properties.class)), with(any(String.class)));
         oneOf(mockDocSubmissionLog).auditResponseMessage(with(any(ProvideAndRegisterDocumentSetRequestType.class)),
         with(any(RegistryResponseType.class)), with(any(AssertionType.class)),
         with(any(NhinTargetSystemType.class)), with(any(String.class)), with(any(String.class)),
         with(any(Boolean.class)), with(any(Properties.class)), with(any(String.class)));
         }
         });*/
    }

    private void expect2MockAudits() {
        //context.checking(new Expectations() {
            /*{
         oneOf(mockDocSubmissionLog).auditEntityXDR(
         with(any(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType.class)),
         with(any(AssertionType.class)), with(any(String.class)));

         oneOf(mockDocSubmissionLog).auditEntityXDRResponse(with(any(RegistryResponseType.class)),
         with(any(AssertionType.class)), with(any(String.class)));
         }*/
        /*{
         oneOf(mockDocSubmissionLog).auditRequestMessage(
         with(any(ProvideAndRegisterDocumentSetRequestType.class)), with(any(AssertionType.class)),
         with(any(NhinTargetSystemType.class)), with(any(String.class)), with(any(String.class)),
         with(any(Boolean.class)), with(any(Properties.class)), with(any(String.class)));
         oneOf(mockDocSubmissionLog).auditResponseMessage(with(any(ProvideAndRegisterDocumentSetRequestType.class)),
         with(any(RegistryResponseType.class)), with(any(AssertionType.class)),
         with(any(NhinTargetSystemType.class)), with(any(String.class)), with(any(String.class)),
         with(any(Boolean.class)), with(any(Properties.class)), with(any(String.class)));
         }
         });*/
    }

    /*   private void setMockPolicyCheck(final boolean allow) {
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

     private StandardOutboundDocSubmission createStandardOutboundDocSubmission() {
     return new StandardOutboundDocSubmission() {
     protected DocSubmissionAuditLogger getXDRAuditLogger() {
     return mockDocSubmissionLog;
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
     */
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
}
