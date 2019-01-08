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
package gov.hhs.fha.nhinc.docsubmission.inbound.deferred.response;

import gov.hhs.fha.nhinc.aspect.InboundProcessingEvent;
import gov.hhs.fha.nhinc.audit.ejb.AuditEJBLogger;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docsubmission.XDRPolicyChecker;
import gov.hhs.fha.nhinc.docsubmission.adapter.deferred.response.proxy.AdapterDocSubmissionDeferredResponseProxy;
import gov.hhs.fha.nhinc.docsubmission.adapter.deferred.response.proxy.AdapterDocSubmissionDeferredResponseProxyObjectFactory;
import gov.hhs.fha.nhinc.docsubmission.aspect.DeferredResponseDescriptionBuilder;
import gov.hhs.fha.nhinc.docsubmission.aspect.DocSubmissionArgTransformerBuilder;
import gov.hhs.fha.nhinc.docsubmission.audit.DSDeferredResponseAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.audit.transform.DSDeferredResponseAuditTransforms;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import java.lang.reflect.Method;
import java.util.Properties;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
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
public class StandardInboundDocSubmissionDeferredResponseTest {

    private final Properties webContextProperties = new Properties();
    private final AuditEJBLogger mockEJBLogger = mock(AuditEJBLogger.class);

    private final AdapterDocSubmissionDeferredResponseProxyObjectFactory adapterFactory
        = mock(AdapterDocSubmissionDeferredResponseProxyObjectFactory.class);
    AdapterDocSubmissionDeferredResponseProxy adapterProxy = mock(AdapterDocSubmissionDeferredResponseProxy.class);
    private final PropertyAccessor propertyAccessor = mock(PropertyAccessor.class);
    private final XDRPolicyChecker policyChecker = mock(XDRPolicyChecker.class);
    private final RegistryResponseType regResponse = new RegistryResponseType();
    private final AssertionType assertion = new AssertionType();

    @Test
    public void standardInboundDocSubmissionDeferredResponse() throws PropertyAccessException {
        String localHCID = "1.1";
        String senderHCID = "2.2";
        assertion.setHomeCommunity(new HomeCommunityType());
        assertion.getHomeCommunity().setHomeCommunityId(senderHCID);
        XDRAcknowledgementType expectedResponse = new XDRAcknowledgementType();

        when(adapterFactory.getAdapterDocSubmissionDeferredResponseProxy()).thenReturn(adapterProxy);

        when(adapterProxy.provideAndRegisterDocumentSetBResponse(regResponse, assertion)).thenReturn(expectedResponse);

        when(
            propertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.HOME_COMMUNITY_ID_PROPERTY)).thenReturn(localHCID);

        when(
            policyChecker.checkXDRResponsePolicy(regResponse, assertion, senderHCID, localHCID,
                NhincConstants.POLICYENGINE_INBOUND_DIRECTION)).thenReturn(true);

        StandardInboundDocSubmissionDeferredResponse standardDocSubmission
            = new StandardInboundDocSubmissionDeferredResponse(adapterFactory, policyChecker, propertyAccessor,
                getAuditLogger(true));

        XDRAcknowledgementType actualResponse = standardDocSubmission.provideAndRegisterDocumentSetBResponse(
            regResponse, assertion, webContextProperties);

        assertSame(expectedResponse, actualResponse);

        verify(mockEJBLogger).auditResponseMessage(eq(regResponse), eq(actualResponse), eq(assertion),
            isNull(NhinTargetSystemType.class), eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION),
            eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.FALSE), eq(webContextProperties),
            eq(NhincConstants.NHINC_XDR_RESPONSE_SERVICE_NAME), any(DSDeferredResponseAuditTransforms.class));
    }

    @Test
    public void failedPolicyCheck() throws PropertyAccessException {
        String localHCID = "1.1";
        String senderHCID = "2.2";
        assertion.setHomeCommunity(new HomeCommunityType());
        assertion.getHomeCommunity().setHomeCommunityId(senderHCID);

        when(
            propertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.HOME_COMMUNITY_ID_PROPERTY)).thenReturn(localHCID);

        when(
            policyChecker.checkXDRResponsePolicy(regResponse, assertion, senderHCID, localHCID,
                NhincConstants.POLICYENGINE_INBOUND_DIRECTION)).thenReturn(false);

        StandardInboundDocSubmissionDeferredResponse standardDocSubmission
            = new StandardInboundDocSubmissionDeferredResponse(adapterFactory, policyChecker, propertyAccessor,
                getAuditLogger(true));

        XDRAcknowledgementType actualResponse = standardDocSubmission.provideAndRegisterDocumentSetBResponse(
            regResponse, assertion, webContextProperties);

        assertEquals("CONNECTPolicyCheckFailed", actualResponse.getMessage().getRegistryErrorList().getRegistryError()
            .get(0).getErrorCode());
        assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure", actualResponse.getMessage()
            .getStatus());
        assertEquals("urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error", actualResponse.getMessage()
            .getRegistryErrorList().getRegistryError().get(0).getSeverity());

        verify(mockEJBLogger).auditResponseMessage(eq(regResponse), eq(actualResponse), eq(assertion),
            isNull(NhinTargetSystemType.class), eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION),
            eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.FALSE), eq(webContextProperties),
            eq(NhincConstants.NHINC_XDR_RESPONSE_SERVICE_NAME), any(DSDeferredResponseAuditTransforms.class));
    }

    @Test
    public void badIncomingAssertion() {

        StandardInboundDocSubmissionDeferredResponse standardDocSubmission
            = new StandardInboundDocSubmissionDeferredResponse(adapterFactory, policyChecker, propertyAccessor,
                getAuditLogger(true));

        XDRAcknowledgementType actualResponse = standardDocSubmission.provideAndRegisterDocumentSetBResponse(
            regResponse, assertion, webContextProperties);

        assertEquals("CONNECTPolicyCheckFailed", actualResponse.getMessage().getRegistryErrorList().getRegistryError()
            .get(0).getErrorCode());
        assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure", actualResponse.getMessage()
            .getStatus());
        assertEquals("urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error", actualResponse.getMessage()
            .getRegistryErrorList().getRegistryError().get(0).getSeverity());

        verify(mockEJBLogger).auditResponseMessage(eq(regResponse), eq(actualResponse), eq(assertion),
            isNull(NhinTargetSystemType.class), eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION),
            eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.FALSE), eq(webContextProperties),
            eq(NhincConstants.NHINC_XDR_RESPONSE_SERVICE_NAME), any(DSDeferredResponseAuditTransforms.class));
    }

    @Test
    public void hasInboundProcessingEvent() throws Exception {
        Class<StandardInboundDocSubmissionDeferredResponse> clazz = StandardInboundDocSubmissionDeferredResponse.class;
        Method method = clazz.getMethod("provideAndRegisterDocumentSetBResponse", RegistryResponseType.class,
            AssertionType.class, Properties.class);
        InboundProcessingEvent annotation = method.getAnnotation(InboundProcessingEvent.class);
        assertNotNull(annotation);
        assertEquals(DeferredResponseDescriptionBuilder.class, annotation.beforeBuilder());
        assertEquals(DocSubmissionArgTransformerBuilder.class, annotation.afterReturningBuilder());
        assertEquals("Document Submission Deferred Response", annotation.serviceType());
        assertEquals("", annotation.version());
    }

    @Test
    public void testAuditLoggingOffForDSDeferredResponse() throws PropertyAccessException {
        String localHCID = "1.1";
        String senderHCID = "2.2";
        assertion.setHomeCommunity(new HomeCommunityType());
        assertion.getHomeCommunity().setHomeCommunityId(senderHCID);
        XDRAcknowledgementType expectedResponse = new XDRAcknowledgementType();

        when(adapterFactory.getAdapterDocSubmissionDeferredResponseProxy()).thenReturn(adapterProxy);

        when(adapterProxy.provideAndRegisterDocumentSetBResponse(regResponse, assertion)).thenReturn(expectedResponse);

        when(
            propertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.HOME_COMMUNITY_ID_PROPERTY)).thenReturn(localHCID);

        when(
            policyChecker.checkXDRResponsePolicy(regResponse, assertion, senderHCID, localHCID,
                NhincConstants.POLICYENGINE_INBOUND_DIRECTION)).thenReturn(true);

        StandardInboundDocSubmissionDeferredResponse standardDocSubmission
            = new StandardInboundDocSubmissionDeferredResponse(adapterFactory, policyChecker, propertyAccessor,
                getAuditLogger(false));

        XDRAcknowledgementType actualResponse = standardDocSubmission.provideAndRegisterDocumentSetBResponse(
            regResponse, assertion, webContextProperties);

        assertSame(expectedResponse, actualResponse);

        verify(mockEJBLogger, never()).auditResponseMessage(eq(regResponse), eq(actualResponse), eq(assertion),
            isNull(NhinTargetSystemType.class), eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION),
            eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.FALSE), eq(webContextProperties),
            eq(NhincConstants.NHINC_XDR_RESPONSE_SERVICE_NAME), any(DSDeferredResponseAuditTransforms.class));
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
