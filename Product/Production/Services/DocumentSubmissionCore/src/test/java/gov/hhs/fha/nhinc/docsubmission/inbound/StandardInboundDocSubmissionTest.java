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
package gov.hhs.fha.nhinc.docsubmission.inbound;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.isNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import gov.hhs.fha.nhinc.aspect.InboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docsubmission.DocSubmissionUtils;
import gov.hhs.fha.nhinc.docsubmission.XDRAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.XDRPolicyChecker;
import gov.hhs.fha.nhinc.docsubmission.adapter.proxy.AdapterDocSubmissionProxy;
import gov.hhs.fha.nhinc.docsubmission.adapter.proxy.AdapterDocSubmissionProxyObjectFactory;
import gov.hhs.fha.nhinc.docsubmission.aspect.DocSubmissionBaseEventDescriptionBuilder;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.junit.Test;

/**
 * @author akong
 * 
 */
public class StandardInboundDocSubmissionTest {

    @Test
    public void standardInboundDocSubmission() throws PropertyAccessException {
        String localHCID = "1.1";
        String senderHCID = "2.2";
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        assertion.setHomeCommunity(new HomeCommunityType());
        assertion.getHomeCommunity().setHomeCommunityId(senderHCID);
        RegistryResponseType expectedResponse = new RegistryResponseType();

        AdapterDocSubmissionProxyObjectFactory adapterFactory = mock(AdapterDocSubmissionProxyObjectFactory.class);
        AdapterDocSubmissionProxy adapterProxy = mock(AdapterDocSubmissionProxy.class);
        XDRAuditLogger auditLogger = mock(XDRAuditLogger.class);
        DocSubmissionUtils dsUtils = mock(DocSubmissionUtils.class);

        when(adapterFactory.getAdapterDocSubmissionProxy()).thenReturn(adapterProxy);

        when(adapterProxy.provideAndRegisterDocumentSetB(request, assertion)).thenReturn(expectedResponse);

        PassthroughInboundDocSubmission passthroughDocSubmission = new PassthroughInboundDocSubmission(adapterFactory,
                auditLogger, dsUtils);

        PropertyAccessor propertyAccessor = mock(PropertyAccessor.class);
        XDRPolicyChecker policyChecker = mock(XDRPolicyChecker.class);

        when(propertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.HOME_COMMUNITY_ID_PROPERTY)).thenReturn(localHCID);

        when(policyChecker.checkXDRRequestPolicy(request, assertion, senderHCID, localHCID, 
                NhincConstants.POLICYENGINE_INBOUND_DIRECTION)).thenReturn(true);

        StandardInboundDocSubmission standardDocSubmission = new StandardInboundDocSubmission(passthroughDocSubmission,
                policyChecker, propertyAccessor, auditLogger);

        RegistryResponseType actualResponse = standardDocSubmission.documentRepositoryProvideAndRegisterDocumentSetB(
                request, assertion);

        assertSame(expectedResponse, actualResponse);

        verify(auditLogger)
                .auditAdapterXDR(eq(request), eq(assertion), eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION));

        verify(auditLogger).auditAdapterXDRResponse(eq(actualResponse), eq(assertion),
                eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION));

        verify(auditLogger).auditNhinXDR(eq(request), eq(assertion), isNull(NhinTargetSystemType.class),
                eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION));

        verify(auditLogger).auditNhinXDRResponse(eq(actualResponse), eq(assertion), isNull(NhinTargetSystemType.class),
                eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(false));
    }

    @Test
    public void failedPolicyCheck() throws PropertyAccessException {
        String localHCID = "1.1";
        String senderHCID = "2.2";
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        assertion.setHomeCommunity(new HomeCommunityType());
        assertion.getHomeCommunity().setHomeCommunityId(senderHCID);

        PassthroughInboundDocSubmission passthroughDocSubmission = mock(PassthroughInboundDocSubmission.class);
        PropertyAccessor propertyAccessor = mock(PropertyAccessor.class);
        XDRPolicyChecker policyChecker = mock(XDRPolicyChecker.class);
        XDRAuditLogger auditLogger = mock(XDRAuditLogger.class);

        when(propertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, 
                NhincConstants.HOME_COMMUNITY_ID_PROPERTY)).thenReturn(localHCID);

        when(policyChecker.checkXDRRequestPolicy(request, assertion, senderHCID, localHCID,
                NhincConstants.POLICYENGINE_INBOUND_DIRECTION)).thenReturn(false);

        StandardInboundDocSubmission standardDocSubmission = new StandardInboundDocSubmission(passthroughDocSubmission,
                policyChecker, propertyAccessor, auditLogger);

        RegistryResponseType actualResponse = standardDocSubmission.documentRepositoryProvideAndRegisterDocumentSetB(
                request, assertion);

        assertEquals("CONNECTPolicyCheckFailed", actualResponse.getRegistryErrorList().getRegistryError().get(0).getErrorCode());
        assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure", actualResponse.getStatus());
        assertEquals("urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error", actualResponse.getRegistryErrorList()
                .getRegistryError().get(0).getSeverity());

        verify(auditLogger).auditNhinXDR(eq(request), eq(assertion), isNull(NhinTargetSystemType.class),
                eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION));

        verify(auditLogger).auditNhinXDRResponse(eq(actualResponse), eq(assertion), isNull(NhinTargetSystemType.class),
                eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(false));
    }
    
    @Test
    public void badIncomingAssertion() {

        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        AssertionType assertion = new AssertionType();

        PassthroughInboundDocSubmission passthroughDocSubmission = mock(PassthroughInboundDocSubmission.class);
        PropertyAccessor propertyAccessor = mock(PropertyAccessor.class);
        XDRPolicyChecker policyChecker = mock(XDRPolicyChecker.class);
        XDRAuditLogger auditLogger = mock(XDRAuditLogger.class);

        StandardInboundDocSubmission standardDocSubmission = new StandardInboundDocSubmission(passthroughDocSubmission,
                policyChecker, propertyAccessor, auditLogger);

        RegistryResponseType actualResponse = standardDocSubmission.documentRepositoryProvideAndRegisterDocumentSetB(
                request, assertion);

        assertEquals("CONNECTPolicyCheckFailed", actualResponse.getRegistryErrorList().getRegistryError().get(0).getErrorCode());
        assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure", actualResponse.getStatus());
        assertEquals("urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error", actualResponse.getRegistryErrorList()
                .getRegistryError().get(0).getSeverity());

        verify(auditLogger).auditNhinXDR(eq(request), eq(assertion), isNull(NhinTargetSystemType.class),
                eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION));

        verify(auditLogger).auditNhinXDRResponse(eq(actualResponse), eq(assertion), isNull(NhinTargetSystemType.class),
                eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(false));
    }
    
    @Test
    public void hasInboundProcessingEvent() throws Exception {
        Class<StandardInboundDocSubmission> clazz = StandardInboundDocSubmission.class;
        Method method = clazz.getMethod("documentRepositoryProvideAndRegisterDocumentSetB", 
                ProvideAndRegisterDocumentSetRequestType.class, AssertionType.class);
        InboundProcessingEvent annotation = method.getAnnotation(InboundProcessingEvent.class);
        assertNotNull(annotation);
        assertEquals(DocSubmissionBaseEventDescriptionBuilder.class, annotation.beforeBuilder());
        assertEquals(DocSubmissionBaseEventDescriptionBuilder.class, annotation.afterReturningBuilder());
        assertEquals("Document Submission", annotation.serviceType());
        assertEquals("", annotation.version());
    }

}
