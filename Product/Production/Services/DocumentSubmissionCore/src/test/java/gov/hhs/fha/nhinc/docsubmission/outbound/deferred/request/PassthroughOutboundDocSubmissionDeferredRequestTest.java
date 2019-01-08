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

import gov.hhs.fha.nhinc.audit.ejb.AuditEJBLogger;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docsubmission.audit.DocSubmissionDeferredRequestAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.audit.transform.DocSubmissionDeferredRequestAuditTransforms;
import gov.hhs.fha.nhinc.docsubmission.entity.deferred.request.OutboundDocSubmissionDeferredRequestDelegate;
import gov.hhs.fha.nhinc.docsubmission.entity.deferred.request.OutboundDocSubmissionDeferredRequestOrchestratable;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.util.Properties;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
public class PassthroughOutboundDocSubmissionDeferredRequestTest {

    final OutboundDocSubmissionDeferredRequestDelegate mockDelegate = mock(OutboundDocSubmissionDeferredRequestDelegate.class);
    final AuditEJBLogger mockEJBLogger = mock(AuditEJBLogger.class);

    @Test
    public void testProvideAndRegisterDocumentSetB() {
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        NhinTargetCommunitiesType targetCommunities = new NhinTargetCommunitiesType();

        when(mockDelegate.process(any(OutboundDocSubmissionDeferredRequestOrchestratable.class))).thenReturn(
            createOutboundDocSubmissionDeferredRequestOrchestratable());

        XDRAcknowledgementType response = runProvideAndRegisterDocumentSetBRequest(request, assertion, targetCommunities,
            getAuditLogger(true));

        assertNotNull(response);
        assertEquals(NhincConstants.XDR_ACK_STATUS_MSG, response.getMessage().getStatus());
        assertNotNull("Assertion MessageId is null", assertion.getMessageId());
        verify(mockEJBLogger).auditRequestMessage(eq(request), eq(assertion), any(NhinTargetSystemType.class),
            eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.TRUE), isNull(Properties.class), eq(NhincConstants.NHINC_XDR_REQUEST_SERVICE_NAME),
            any(DocSubmissionDeferredRequestAuditTransforms.class));
    }

    @Test
    public void testGetters() {
        PassthroughOutboundDocSubmissionDeferredRequest passthruOrch = new PassthroughOutboundDocSubmissionDeferredRequest();

        assertNotNull(passthruOrch.getOutboundDocSubmissionDeferredRequestDelegate());
    }

    @Test
    public void testAuditLoggingOffForDSDeferredRequest() {
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        NhinTargetCommunitiesType targetCommunities = new NhinTargetCommunitiesType();

        when(mockDelegate.process(any(OutboundDocSubmissionDeferredRequestOrchestratable.class))).thenReturn(
            createOutboundDocSubmissionDeferredRequestOrchestratable());

        XDRAcknowledgementType response = runProvideAndRegisterDocumentSetBRequest(request, assertion, targetCommunities,
            getAuditLogger(false));

        assertNotNull(response);
        assertEquals(NhincConstants.XDR_ACK_STATUS_MSG, response.getMessage().getStatus());
        assertNotNull("Assertion MessageId is null", assertion.getMessageId());
        verify(mockEJBLogger, never()).auditRequestMessage(eq(request), eq(assertion), any(NhinTargetSystemType.class),
            eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.TRUE), isNull(Properties.class), eq(NhincConstants.NHINC_XDR_REQUEST_SERVICE_NAME),
            any(DocSubmissionDeferredRequestAuditTransforms.class));
    }

    private XDRAcknowledgementType runProvideAndRegisterDocumentSetBRequest(
        ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion,
        NhinTargetCommunitiesType targetCommunities, DocSubmissionDeferredRequestAuditLogger auditLogger) {

        PassthroughOutboundDocSubmissionDeferredRequest passthruOrch
            = createPassthruDocSubmissionDeferredRequestOrchImpl(auditLogger);
        return passthruOrch.provideAndRegisterDocumentSetBAsyncRequest(request, assertion, targetCommunities, null);
    }

    private OutboundDocSubmissionDeferredRequestOrchestratable createOutboundDocSubmissionDeferredRequestOrchestratable() {
        RegistryResponseType regResponse = new RegistryResponseType();
        regResponse.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);

        XDRAcknowledgementType response = new XDRAcknowledgementType();
        response.setMessage(regResponse);

        OutboundDocSubmissionDeferredRequestOrchestratable orchestratable
            = new OutboundDocSubmissionDeferredRequestOrchestratable(null);
        orchestratable.setResponse(response);

        return orchestratable;
    }

    private PassthroughOutboundDocSubmissionDeferredRequest createPassthruDocSubmissionDeferredRequestOrchImpl(
        final DocSubmissionDeferredRequestAuditLogger auditLogger) {
        return new PassthroughOutboundDocSubmissionDeferredRequest() {
            @Override
            protected OutboundDocSubmissionDeferredRequestDelegate getOutboundDocSubmissionDeferredRequestDelegate() {
                return mockDelegate;
            }

            @Override
            protected DocSubmissionDeferredRequestAuditLogger getAuditLogger() {
                return auditLogger;
            }
        };
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
