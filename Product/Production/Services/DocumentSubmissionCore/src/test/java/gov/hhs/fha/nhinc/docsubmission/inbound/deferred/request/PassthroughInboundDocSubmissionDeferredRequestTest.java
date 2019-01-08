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
package gov.hhs.fha.nhinc.docsubmission.inbound.deferred.request;

import gov.hhs.fha.nhinc.audit.ejb.AuditEJBLogger;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docsubmission.DocSubmissionUtils;
import gov.hhs.fha.nhinc.docsubmission.adapter.deferred.request.proxy.AdapterDocSubmissionDeferredRequestProxy;
import gov.hhs.fha.nhinc.docsubmission.adapter.deferred.request.proxy.AdapterDocSubmissionDeferredRequestProxyObjectFactory;
import gov.hhs.fha.nhinc.docsubmission.audit.DocSubmissionDeferredRequestAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.audit.transform.DocSubmissionDeferredRequestAuditTransforms;
import gov.hhs.fha.nhinc.largefile.LargePayloadException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.util.Properties;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author akong
 *
 */
public class PassthroughInboundDocSubmissionDeferredRequestTest {

    private final AdapterDocSubmissionDeferredRequestProxyObjectFactory adapterFactory
        = mock(AdapterDocSubmissionDeferredRequestProxyObjectFactory.class);
    private final AdapterDocSubmissionDeferredRequestProxy adapterProxy
        = mock(AdapterDocSubmissionDeferredRequestProxy.class);
    private final AuditEJBLogger mockEJBLogger = mock(AuditEJBLogger.class);
    private final DocSubmissionUtils dsUtils = mock(DocSubmissionUtils.class);

    @Test
    public void passthroughInboundDocSubmissionDeferredRequest() {
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        XDRAcknowledgementType expectedResponse = new XDRAcknowledgementType();

        Properties webContextProperties = new Properties();
        when(adapterFactory.getAdapterDocSubmissionDeferredRequestProxy()).thenReturn(adapterProxy);

        when(adapterProxy.provideAndRegisterDocumentSetBRequest(request, assertion)).thenReturn(expectedResponse);

        PassthroughInboundDocSubmissionDeferredRequest passthroughDocSubmission
            = new PassthroughInboundDocSubmissionDeferredRequest(adapterFactory, getAuditLogger(true), dsUtils);

        XDRAcknowledgementType actualResponse = passthroughDocSubmission.provideAndRegisterDocumentSetBRequest(request,
            assertion, webContextProperties);

        assertSame(expectedResponse, actualResponse);
        verify(mockEJBLogger).auditResponseMessage(eq(request), eq(actualResponse), eq(assertion), isNull(
            NhinTargetSystemType.class), eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION),
            eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.FALSE), eq(webContextProperties),
            eq(NhincConstants.NHINC_XDR_REQUEST_SERVICE_NAME), any(DocSubmissionDeferredRequestAuditTransforms.class));
    }

    @Test
    public void convertDataToFileError() throws LargePayloadException {
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        AssertionType assertion = new AssertionType();

        Properties webContextProperties = new Properties();
        doThrow(new LargePayloadException()).when(dsUtils).convertDataToFileLocationIfEnabled(request);

        PassthroughInboundDocSubmissionDeferredRequest passthroughDocSubmission = new PassthroughInboundDocSubmissionDeferredRequest(
            adapterFactory, getAuditLogger(true), dsUtils);

        XDRAcknowledgementType actualResponse = passthroughDocSubmission.provideAndRegisterDocumentSetBRequest(request,
            assertion, webContextProperties);

        assertEquals("XDSRegistryError", actualResponse.getMessage().getRegistryErrorList().getRegistryError().get(0
        ).getErrorCode());
        assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure", actualResponse.getMessage().getStatus());
        assertEquals("urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error", actualResponse.getMessage().
            getRegistryErrorList().getRegistryError().get(0).getSeverity());

        verify(mockEJBLogger).auditResponseMessage(eq(request), eq(actualResponse), eq(assertion), isNull(
            NhinTargetSystemType.class), eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION),
            eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.FALSE), eq(webContextProperties),
            eq(NhincConstants.NHINC_XDR_REQUEST_SERVICE_NAME), any(DocSubmissionDeferredRequestAuditTransforms.class));
    }

    @Test
    public void testAuditLoggingOffForDSDeferredRequest() {
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        XDRAcknowledgementType expectedResponse = new XDRAcknowledgementType();

        Properties webContextProperties = new Properties();
        when(adapterFactory.getAdapterDocSubmissionDeferredRequestProxy()).thenReturn(adapterProxy);

        when(adapterProxy.provideAndRegisterDocumentSetBRequest(request, assertion)).thenReturn(expectedResponse);

        PassthroughInboundDocSubmissionDeferredRequest passthroughDocSubmission
            = new PassthroughInboundDocSubmissionDeferredRequest(adapterFactory, getAuditLogger(false), dsUtils);

        XDRAcknowledgementType actualResponse = passthroughDocSubmission.provideAndRegisterDocumentSetBRequest(request,
            assertion, webContextProperties);

        assertSame(expectedResponse, actualResponse);
        verify(mockEJBLogger, never()).auditResponseMessage(eq(request), eq(actualResponse), eq(assertion), isNull(
            NhinTargetSystemType.class), eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION),
            eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.FALSE), eq(webContextProperties),
            eq(NhincConstants.NHINC_XDR_REQUEST_SERVICE_NAME), any(DocSubmissionDeferredRequestAuditTransforms.class));
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
