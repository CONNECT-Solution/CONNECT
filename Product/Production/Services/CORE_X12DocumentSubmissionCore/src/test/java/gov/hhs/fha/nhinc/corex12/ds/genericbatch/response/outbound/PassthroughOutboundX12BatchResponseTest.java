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
package gov.hhs.fha.nhinc.corex12.ds.genericbatch.response.outbound;

import gov.hhs.fha.nhinc.audit.ejb.AuditEJBLogger;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.corex12.ds.audit.X12BatchAuditLogger;
import gov.hhs.fha.nhinc.corex12.ds.audit.transform.X12BatchAuditTransforms;
import gov.hhs.fha.nhinc.corex12.ds.genericbatch.response.entity.OutboundX12BatchResponseDelegate;
import gov.hhs.fha.nhinc.corex12.ds.genericbatch.response.entity.OutboundX12BatchResponseOrchestratable;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import java.util.Properties;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmission;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmissionResponse;
import org.junit.Test;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 *
 * @author tjafri
 */
public class PassthroughOutboundX12BatchResponseTest {

    private final AuditEJBLogger mockEJBLogger = mock(AuditEJBLogger.class);
    private final OutboundX12BatchResponseOrchestratable mockOrchestratable
        = mock(OutboundX12BatchResponseOrchestratable.class);
    private final OutboundX12BatchResponseDelegate mockDelegate
        = mock(OutboundX12BatchResponseDelegate.class);
    private final AssertionType assertion = new AssertionType();
    private final NhinTargetCommunitiesType targets = new NhinTargetCommunitiesType();
    private final UrlInfoType urlInfo = new UrlInfoType();
    private final COREEnvelopeBatchSubmission request = new COREEnvelopeBatchSubmission();
    private final COREEnvelopeBatchSubmissionResponse successResponse = new COREEnvelopeBatchSubmissionResponse();
    private PassthroughOutboundX12BatchResponse batchResp = null;

    @Test
    public void auditLoggingOffForOutboundX12BatchResponse() {
        batchResp = createPassthruOutboundBatchResponse(getAuditLogger(false));
        when(mockOrchestratable.getResponse()).thenReturn(successResponse);
        when(mockDelegate.process(any(OutboundOrchestratable.class))).thenReturn(mockOrchestratable);
        batchResp.batchSubmitTransaction(request, assertion, targets, urlInfo);
        assertNotNull("Assertion MessageId is null", assertion.getMessageId());
        verify(mockEJBLogger, never()).auditRequestMessage(eq(request), eq(assertion), any(NhinTargetSystemType.class),
            eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.TRUE),
            isNull(Properties.class), eq(NhincConstants.CORE_X12DS_GENERICBATCH_RESPONSE_SERVICE_NAME),
            any(X12BatchAuditTransforms.class));
    }

    @Test
    public void auditLoggingOnForOutboundX12BatchResponse() {
        batchResp = createPassthruOutboundBatchResponse(getAuditLogger(true));
        when(mockOrchestratable.getResponse()).thenReturn(successResponse);
        when(mockDelegate.process(any(OutboundOrchestratable.class))).thenReturn(mockOrchestratable);
        batchResp.batchSubmitTransaction(request, assertion, targets, urlInfo);
        assertNotNull("Assertion MessageId is null", assertion.getMessageId());
        verify(mockEJBLogger).auditRequestMessage(eq(request), eq(assertion), any(NhinTargetSystemType.class),
            eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.TRUE),
            isNull(Properties.class), eq(NhincConstants.CORE_X12DS_GENERICBATCH_RESPONSE_SERVICE_NAME),
            any(X12BatchAuditTransforms.class));
    }

    private X12BatchAuditLogger getAuditLogger(final boolean isLoggingOn) {
        return new X12BatchAuditLogger() {
            @Override
            protected AuditEJBLogger getAuditLogger() {
                return mockEJBLogger;
            }

            @Override
            protected boolean isAuditLoggingOn(String serviceName) {
                return isLoggingOn;
            }
        };
    }

    private PassthroughOutboundX12BatchResponse createPassthruOutboundBatchResponse(
        final X12BatchAuditLogger auditLogger) {
        return new PassthroughOutboundX12BatchResponse(mockDelegate) {
            @Override
            protected X12BatchAuditLogger getAuditLogger() {
                return auditLogger;
            }
        };
    }
}
