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

import gov.hhs.fha.nhinc.audit.ejb.AuditEJBLogger;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docsubmission.audit.DocSubmissionAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.audit.transform.DocSubmissionAuditTransforms;
import gov.hhs.fha.nhinc.docsubmission.entity.OutboundDocSubmissionDelegate;
import gov.hhs.fha.nhinc.docsubmission.entity.OutboundDocSubmissionOrchestratable;
import gov.hhs.fha.nhinc.document.DocumentConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
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
public class PassthroughOutboundDocSubmissionTest {

    private final AuditEJBLogger mockEJBLogger = mock(AuditEJBLogger.class);
    private final OutboundDocSubmissionDelegate mockDelegate = mock(OutboundDocSubmissionDelegate.class);

    @Test
    public void testProvideAndRegisterDocumentSetB() {
        when(mockDelegate.process(any(OutboundDocSubmissionOrchestratable.class))).thenReturn(
            createOutboundDocSubmissionOrchestratable());
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        NhinTargetCommunitiesType targets = new NhinTargetCommunitiesType();

        RegistryResponseType response = runProvideAndRegisterDocumentSetB(request, assertion, targets, true);

        assertNotNull(response);
        assertEquals(DocumentConstants.XDS_SUBMISSION_RESPONSE_STATUS_SUCCESS, response.getStatus());
        assertNotNull("Assertion MessageId is null", assertion.getMessageId());
        verify(mockEJBLogger).auditRequestMessage(eq(request), eq(assertion), any(NhinTargetSystemType.class),
            eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.TRUE), isNull(Properties.class), eq(NhincConstants.NHINC_XDR_SERVICE_NAME),
            any(DocSubmissionAuditTransforms.class));
    }

    @Test
    public void testAuditLoggingOffForOutboundDS() {
        when(mockDelegate.process(any(OutboundDocSubmissionOrchestratable.class))).thenReturn(
            createOutboundDocSubmissionOrchestratable());
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        NhinTargetCommunitiesType targets = new NhinTargetCommunitiesType();

        RegistryResponseType response = runProvideAndRegisterDocumentSetB(request, assertion, targets, false);

        assertNotNull(response);
        assertEquals(DocumentConstants.XDS_SUBMISSION_RESPONSE_STATUS_SUCCESS, response.getStatus());
        assertNotNull("Assertion MessageId is null", assertion.getMessageId());
        verify(mockEJBLogger, never()).auditRequestMessage(eq(request), eq(assertion), any(NhinTargetSystemType.class),
            eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.TRUE), isNull(Properties.class), eq(NhincConstants.NHINC_XDR_SERVICE_NAME),
            any(DocSubmissionAuditTransforms.class));
    }

    private RegistryResponseType runProvideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType request,
        AssertionType assertion, NhinTargetCommunitiesType targets, boolean isAuditOn) {

        PassthroughOutboundDocSubmission passthruOrch = new PassthroughOutboundDocSubmission(getAuditLogger(isAuditOn),
            mockDelegate);
        return passthruOrch.provideAndRegisterDocumentSetB(request, assertion, targets, null);
    }

    private OutboundDocSubmissionOrchestratable createOutboundDocSubmissionOrchestratable() {
        RegistryResponseType response = new RegistryResponseType();
        response.setStatus(DocumentConstants.XDS_SUBMISSION_RESPONSE_STATUS_SUCCESS);

        OutboundDocSubmissionOrchestratable orchestratable = new OutboundDocSubmissionOrchestratable(null);
        orchestratable.setResponse(response);

        return orchestratable;
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
