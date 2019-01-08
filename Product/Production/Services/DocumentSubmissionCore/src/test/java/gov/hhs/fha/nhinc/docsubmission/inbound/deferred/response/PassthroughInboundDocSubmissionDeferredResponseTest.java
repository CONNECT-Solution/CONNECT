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

import gov.hhs.fha.nhinc.audit.ejb.AuditEJBLogger;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docsubmission.adapter.deferred.response.proxy.AdapterDocSubmissionDeferredResponseProxy;
import gov.hhs.fha.nhinc.docsubmission.adapter.deferred.response.proxy.AdapterDocSubmissionDeferredResponseProxyObjectFactory;
import gov.hhs.fha.nhinc.docsubmission.audit.DSDeferredResponseAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.audit.transform.DSDeferredResponseAuditTransforms;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import java.util.Properties;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
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
public class PassthroughInboundDocSubmissionDeferredResponseTest {

    private final Properties webContextProperties = new Properties();
    private final AuditEJBLogger mockEJBLogger = mock(AuditEJBLogger.class);
    XDRAcknowledgementType expectedResponse = new XDRAcknowledgementType();

    @Test
    public void passthroughInboundDocSubmissionDeferredResponse() {
        RegistryResponseType regResponse = new RegistryResponseType();
        AssertionType assertion = new AssertionType();

        AdapterDocSubmissionDeferredResponseProxyObjectFactory adapterFactory
            = mock(AdapterDocSubmissionDeferredResponseProxyObjectFactory.class);
        AdapterDocSubmissionDeferredResponseProxy adapterProxy = mock(AdapterDocSubmissionDeferredResponseProxy.class);

        when(adapterFactory.getAdapterDocSubmissionDeferredResponseProxy()).thenReturn(adapterProxy);

        when(adapterProxy.provideAndRegisterDocumentSetBResponse(regResponse, assertion)).thenReturn(expectedResponse);
        PassthroughInboundDocSubmissionDeferredResponse passthroughDocSubmission
            = new PassthroughInboundDocSubmissionDeferredResponse(adapterFactory, getAuditLogger(true)) {
                @Override
                protected DSDeferredResponseAuditLogger getAuditLogger() {
                    return new DSDeferredResponseAuditLogger();
                }
            };

        XDRAcknowledgementType actualResponse = passthroughDocSubmission.provideAndRegisterDocumentSetBResponse(
            regResponse, assertion, webContextProperties);

        assertSame(expectedResponse, actualResponse);

        verify(mockEJBLogger).auditResponseMessage(eq(regResponse), eq(actualResponse), eq(assertion),
            isNull(NhinTargetSystemType.class), eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION),
            eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.FALSE), eq(webContextProperties),
            eq(NhincConstants.NHINC_XDR_RESPONSE_SERVICE_NAME), any(DSDeferredResponseAuditTransforms.class));
    }

    @Test
    public void testAuditLoggingOffForDSDeferredResponse() {
        RegistryResponseType regResponse = new RegistryResponseType();
        AssertionType assertion = new AssertionType();

        AdapterDocSubmissionDeferredResponseProxyObjectFactory adapterFactory
            = mock(AdapterDocSubmissionDeferredResponseProxyObjectFactory.class);
        AdapterDocSubmissionDeferredResponseProxy adapterProxy = mock(AdapterDocSubmissionDeferredResponseProxy.class);

        when(adapterFactory.getAdapterDocSubmissionDeferredResponseProxy()).thenReturn(adapterProxy);

        when(adapterProxy.provideAndRegisterDocumentSetBResponse(regResponse, assertion)).thenReturn(expectedResponse);
        PassthroughInboundDocSubmissionDeferredResponse passthroughDocSubmission
            = new PassthroughInboundDocSubmissionDeferredResponse(adapterFactory, getAuditLogger(false)) {
                @Override
                protected DSDeferredResponseAuditLogger getAuditLogger() {
                    return new DSDeferredResponseAuditLogger();
                }
            };

        XDRAcknowledgementType actualResponse = passthroughDocSubmission.provideAndRegisterDocumentSetBResponse(
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
