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
package gov.hhs.fha.nhinc.corex12.docsubmission.genericbatch.request.outbound;

import gov.hhs.fha.nhinc.audit.ejb.AuditEJBLogger;
import gov.hhs.fha.nhinc.audit.ejb.impl.AuditEJBLoggerImpl;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.corex12.docsubmission.audit.CORE_X12BatchSubmissionAuditLogger;
import gov.hhs.fha.nhinc.corex12.docsubmission.audit.transform.COREX12BatchSubmissionAuditTransforms;
import gov.hhs.fha.nhinc.corex12.docsubmission.genericbatch.request.entity.OutboundCORE_X12DSGenericBatchRequestDelegate;
import gov.hhs.fha.nhinc.corex12.docsubmission.genericbatch.request.entity.OutboundCORE_X12DSGenericBatchRequestOrchestratable;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.Properties;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmission;
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
 *
 * @author tjafri
 */
public class PassthroughOutboundCORE_X12DSGenericBatchRequestTest {

    private final AuditEJBLoggerImpl mockEJBLogger = mock(AuditEJBLoggerImpl.class);
    private final COREEnvelopeBatchSubmission msg = new COREEnvelopeBatchSubmission();
    private final AssertionType assertion = new AssertionType();
    private final NhinTargetCommunitiesType targets = new NhinTargetCommunitiesType();
    private final UrlInfoType urlInfo = new UrlInfoType();
    private final OutboundCORE_X12DSGenericBatchRequestDelegate mockDelegate
        = mock(OutboundCORE_X12DSGenericBatchRequestDelegate.class);
    private final OutboundCORE_X12DSGenericBatchRequestOrchestratable mockOrch
        = mock(OutboundCORE_X12DSGenericBatchRequestOrchestratable.class);

    @Test
    public void auditLoggingOnForOutboundX12BatchRequest() {
        PassthroughOutboundCORE_X12DSGenericBatchRequest x12BatchReq = createGenericBatchRequest(getAuditLogger(true));
        when(mockDelegate.process(any(OutboundCORE_X12DSGenericBatchRequestOrchestratable.class))).thenReturn(mockOrch);
        x12BatchReq.batchSubmitTransaction(msg, assertion, targets, urlInfo);
        assertNotNull("Assertion MessageId is null", assertion.getMessageId());
        verify(mockEJBLogger).auditRequestMessage(eq(msg), eq(assertion), any(NhinTargetSystemType.class),
            eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.TRUE), isNull(Properties.class), eq(NhincConstants.CORE_X12DS_GENERICBATCH_REQUEST_SERVICE_NAME),
            any(COREX12BatchSubmissionAuditTransforms.class));
    }

    @Test
    public void auditLoggingOffForOutboundX12BatchRequest() {
        PassthroughOutboundCORE_X12DSGenericBatchRequest x12BatchReq = createGenericBatchRequest(getAuditLogger(false));
        when(mockDelegate.process(any(OutboundCORE_X12DSGenericBatchRequestOrchestratable.class))).thenReturn(mockOrch);
        x12BatchReq.batchSubmitTransaction(msg, assertion, targets, urlInfo);
        assertNotNull("Assertion MessageId is null", assertion.getMessageId());
        verify(mockEJBLogger, never()).auditRequestMessage(eq(msg), eq(assertion), any(NhinTargetSystemType.class),
            eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.TRUE), isNull(Properties.class), eq(NhincConstants.CORE_X12DS_GENERICBATCH_REQUEST_SERVICE_NAME),
            any(COREX12BatchSubmissionAuditTransforms.class));
    }

    private CORE_X12BatchSubmissionAuditLogger getAuditLogger(final boolean isLoggingOn) {
        return new CORE_X12BatchSubmissionAuditLogger() {
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

    private PassthroughOutboundCORE_X12DSGenericBatchRequest createGenericBatchRequest(
        final CORE_X12BatchSubmissionAuditLogger auditLogger) {
        return new PassthroughOutboundCORE_X12DSGenericBatchRequest(mockDelegate) {
            @Override
            protected CORE_X12BatchSubmissionAuditLogger getAuditLogger() {
                return auditLogger;
            }
        };

    }
}
