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
package gov.hhs.fha.nhinc.docretrieve.outbound;

import gov.hhs.fha.nhinc.aspect.OutboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetRequestTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetResponseTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.audit.transform.DocRetrieveAuditTransforms;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundDocRetrieveOrchestratable;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import gov.hhs.fha.nhinc.orchestration.CONNECTOutboundOrchestrator;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import java.lang.reflect.Method;
import java.util.Properties;
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
 * @author achidamb
 *
 */
public class StandardOutboundDocRetrieveTest extends AbstractOutboundDocRetrieveTest {

    @Test
    public void hasOutboundProcessingEvent() throws Exception {
        Class<StandardOutboundDocRetrieve> clazz = StandardOutboundDocRetrieve.class;
        Method method = clazz.getMethod("respondingGatewayCrossGatewayRetrieve", RetrieveDocumentSetRequestType.class,
            AssertionType.class, NhinTargetCommunitiesType.class, ADAPTER_API_LEVEL.class);
        OutboundProcessingEvent annotation = method.getAnnotation(OutboundProcessingEvent.class);
        assertNotNull(annotation);
        assertEquals(RetrieveDocumentSetRequestTypeDescriptionBuilder.class, annotation.beforeBuilder());
        assertEquals(RetrieveDocumentSetResponseTypeDescriptionBuilder.class, annotation.afterReturningBuilder());
        assertEquals("Retrieve Document", annotation.serviceType());
        assertEquals("", annotation.version());
    }

    @Test
    public void invoke() {
        RetrieveDocumentSetRequestType request = new RetrieveDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        NhinTargetCommunitiesType targets = new NhinTargetCommunitiesType();
        targets.setUseSpecVersion("2.0");
        RetrieveDocumentSetResponseType expectedResponse = new RetrieveDocumentSetResponseType();

        CONNECTOutboundOrchestrator orchestrator = mock(CONNECTOutboundOrchestrator.class);
        OutboundDocRetrieveOrchestratable orchResponse = mock(OutboundDocRetrieveOrchestratable.class);

        when(orchestrator.process(any(OutboundDocRetrieveOrchestratable.class))).thenReturn(orchResponse);

        when(orchResponse.getResponse()).thenReturn(expectedResponse);

        StandardOutboundDocRetrieve outboundDocRetrieve = (StandardOutboundDocRetrieve) getOutboundDocRetrieve(
            orchestrator, true);

        RetrieveDocumentSetResponseType actualResponse = outboundDocRetrieve.respondingGatewayCrossGatewayRetrieve(
            request, assertion, targets, ADAPTER_API_LEVEL.LEVEL_a0);

        assertSame(expectedResponse, actualResponse);
        assertNotNull("Assertion MessageId is null", assertion.getMessageId());
        verify(mockEJBLogger).auditRequestMessage(eq(request), eq(assertion), any(NhinTargetSystemType.class),
            eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.TRUE), isNull(Properties.class), eq(NhincConstants.DOC_RETRIEVE_SERVICE_NAME),
            any(DocRetrieveAuditTransforms.class));

    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.docretrieve.outbound.AbstractOutboundDocRetrieveTest#getOutboundDocRetrieve(gov.hhs.fha.nhinc.orchestration.CONNECTOutboundOrchestrator)
     */
    @Override
    protected OutboundDocRetrieve getOutboundDocRetrieve(CONNECTOutboundOrchestrator orchestrator, boolean isAuditOn) {
        return new StandardOutboundDocRetrieve(orchestrator, getAuditLogger(isAuditOn));
    }

    @Test
    public void auditLoggingOffForOutboundDR() {
        RetrieveDocumentSetRequestType request = new RetrieveDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        NhinTargetCommunitiesType targets = new NhinTargetCommunitiesType();
        targets.setUseSpecVersion("2.0");
        RetrieveDocumentSetResponseType expectedResponse = new RetrieveDocumentSetResponseType();

        CONNECTOutboundOrchestrator orchestrator = mock(CONNECTOutboundOrchestrator.class);
        OutboundDocRetrieveOrchestratable orchResponse = mock(OutboundDocRetrieveOrchestratable.class);

        when(orchestrator.process(any(OutboundDocRetrieveOrchestratable.class))).thenReturn(orchResponse);

        when(orchResponse.getResponse()).thenReturn(expectedResponse);

        StandardOutboundDocRetrieve outboundDocRetrieve = (StandardOutboundDocRetrieve) getOutboundDocRetrieve(
            orchestrator, false);

        RetrieveDocumentSetResponseType actualResponse = outboundDocRetrieve.respondingGatewayCrossGatewayRetrieve(
            request, assertion, targets, ADAPTER_API_LEVEL.LEVEL_a0);

        assertSame(expectedResponse, actualResponse);
        assertNotNull("Assertion MessageId is null", assertion.getMessageId());
        verify(mockEJBLogger, never()).auditRequestMessage(eq(request), eq(assertion), any(NhinTargetSystemType.class),
            eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.TRUE), isNull(Properties.class), eq(NhincConstants.DOC_RETRIEVE_SERVICE_NAME),
            any(DocRetrieveAuditTransforms.class));
    }
}
