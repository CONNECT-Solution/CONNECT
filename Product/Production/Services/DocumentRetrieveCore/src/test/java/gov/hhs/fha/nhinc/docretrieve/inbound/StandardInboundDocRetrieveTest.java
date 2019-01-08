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
package gov.hhs.fha.nhinc.docretrieve.inbound;

import gov.hhs.fha.nhinc.aspect.InboundProcessingEvent;
import gov.hhs.fha.nhinc.audit.ejb.AuditEJBLogger;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetRequestTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetResponseTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.audit.DocRetrieveAuditLogger;
import gov.hhs.fha.nhinc.docretrieve.audit.transform.DocRetrieveAuditTransforms;
import gov.hhs.fha.nhinc.docretrieve.nhin.InboundDocRetrieveDelegate;
import gov.hhs.fha.nhinc.docretrieve.nhin.InboundDocRetrieveOrchestratable;
import gov.hhs.fha.nhinc.docretrieve.nhin.InboundDocRetrievePolicyTransformer_g0;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.CONNECTInboundOrchestrator;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import java.lang.reflect.Method;
import java.util.Properties;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
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
public class StandardInboundDocRetrieveTest {

    private AuditEJBLogger mockEJBLogger;

    @Before
    public void setup() {
        mockEJBLogger = mock(AuditEJBLogger.class);
    }

    @Test
    public void hasInboundProcessingEvent() throws Exception {
        Class<StandardInboundDocRetrieve> clazz = StandardInboundDocRetrieve.class;
        Method method = clazz.getMethod("respondingGatewayCrossGatewayRetrieve", RetrieveDocumentSetRequestType.class,
            AssertionType.class, Properties.class);
        InboundProcessingEvent annotation = method.getAnnotation(InboundProcessingEvent.class);
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
        RetrieveDocumentSetResponseType expectedResponse = new RetrieveDocumentSetResponseType();
        Properties webContextProperties = new Properties();

        InboundDocRetrievePolicyTransformer_g0 pt = new InboundDocRetrievePolicyTransformer_g0();
        InboundDocRetrieveDelegate ad = new InboundDocRetrieveDelegate();

        // Mocks
        CONNECTInboundOrchestrator orch = mock(CONNECTInboundOrchestrator.class);

        InboundDocRetrieveOrchestratable orchestratable = mock(InboundDocRetrieveOrchestratable.class);

        // Method Stubbing
        when(orch.process(any(InboundDocRetrieveOrchestratable.class))).thenReturn(orchestratable);

        when(orchestratable.getResponse()).thenReturn(expectedResponse);

        // Actual Invocation
        StandardInboundDocRetrieve inboundDocRetrieve = new StandardInboundDocRetrieve(pt, ad, orch,
            getAuditLogger(true));

        RetrieveDocumentSetResponseType actualResponse = inboundDocRetrieve.respondingGatewayCrossGatewayRetrieve(
            request, assertion, webContextProperties);

        // Verify response is expected
        assertSame(expectedResponse, actualResponse);

        verify(mockEJBLogger).auditResponseMessage(eq(request), eq(actualResponse), eq(assertion),
            isNull(NhinTargetSystemType.class), eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION),
            eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.FALSE), eq(webContextProperties),
            eq(NhincConstants.DOC_RETRIEVE_SERVICE_NAME), any(DocRetrieveAuditTransforms.class));

        // Verify that the orchestrator is processing the correct orchestratable
        ArgumentCaptor< InboundDocRetrieveOrchestratable> orchArgument = ArgumentCaptor
            .forClass(InboundDocRetrieveOrchestratable.class);

        verify(orch).process(orchArgument.capture());
        assertEquals(pt, orchArgument.getValue().getPolicyTransformer());
        assertEquals(ad, orchArgument.getValue().getDelegate());
        assertFalse(orchArgument.getValue().isPassthru());
    }

    @Test
    public void auditLoggingOffForInboundDR() {
        RetrieveDocumentSetRequestType request = new RetrieveDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        RetrieveDocumentSetResponseType expectedResponse = new RetrieveDocumentSetResponseType();
        Properties webContextProperties = new Properties();

        InboundDocRetrievePolicyTransformer_g0 pt = new InboundDocRetrievePolicyTransformer_g0();
        InboundDocRetrieveDelegate ad = new InboundDocRetrieveDelegate();

        // Mocks
        CONNECTInboundOrchestrator orch = mock(CONNECTInboundOrchestrator.class);

        InboundDocRetrieveOrchestratable orchestratable = mock(InboundDocRetrieveOrchestratable.class);

        // Method Stubbing
        when(orch.process(any(InboundDocRetrieveOrchestratable.class))).thenReturn(orchestratable);

        when(orchestratable.getResponse()).thenReturn(expectedResponse);

        // Actual Invocation
        StandardInboundDocRetrieve inboundDocRetrieve = new StandardInboundDocRetrieve(pt, ad, orch,
            getAuditLogger(false));

        RetrieveDocumentSetResponseType actualResponse = inboundDocRetrieve.respondingGatewayCrossGatewayRetrieve(
            request, assertion, webContextProperties);

        // Verify response is expected
        assertSame(expectedResponse, actualResponse);

        verify(mockEJBLogger, never()).auditResponseMessage(eq(request), eq(actualResponse), eq(assertion),
            isNull(NhinTargetSystemType.class), eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION),
            eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.FALSE), eq(webContextProperties),
            eq(NhincConstants.DOC_RETRIEVE_SERVICE_NAME), any(DocRetrieveAuditTransforms.class));
    }

    private DocRetrieveAuditLogger getAuditLogger(final boolean isLoggingOn) {
        return new DocRetrieveAuditLogger() {
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
}
