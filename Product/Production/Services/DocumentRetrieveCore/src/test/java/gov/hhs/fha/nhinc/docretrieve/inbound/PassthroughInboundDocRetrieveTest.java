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
package gov.hhs.fha.nhinc.docretrieve.inbound;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.hhs.fha.nhinc.aspect.InboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetRequestTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetResponseTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.nhin.InboundDocRetrieveAuditTransformer_g0;
import gov.hhs.fha.nhinc.docretrieve.nhin.InboundDocRetrieveDelegate;
import gov.hhs.fha.nhinc.docretrieve.nhin.InboundDocRetrieveOrchestratable;
import gov.hhs.fha.nhinc.docretrieve.nhin.InboundDocRetrievePolicyTransformer_g0;
import gov.hhs.fha.nhinc.orchestration.CONNECTInboundOrchestrator;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

import java.lang.reflect.Method;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * @author akong
 * 
 */
public class PassthroughInboundDocRetrieveTest {

    @Test
    public void hasInboundProcessingEvent() throws Exception {
        Class<PassthroughInboundDocRetrieve> clazz = PassthroughInboundDocRetrieve.class;
        Method method = clazz.getMethod("respondingGatewayCrossGatewayRetrieve", RetrieveDocumentSetRequestType.class,
                AssertionType.class);
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

        InboundDocRetrievePolicyTransformer_g0 pt = new InboundDocRetrievePolicyTransformer_g0();
        InboundDocRetrieveAuditTransformer_g0 at = new InboundDocRetrieveAuditTransformer_g0();
        InboundDocRetrieveDelegate ad = new InboundDocRetrieveDelegate();

        // Mocks
        CONNECTInboundOrchestrator orch = mock(CONNECTInboundOrchestrator.class);

        InboundDocRetrieveOrchestratable orchestratable = mock(InboundDocRetrieveOrchestratable.class);

        // Method Stubbing
        when(orch.process(any(InboundDocRetrieveOrchestratable.class))).thenReturn(orchestratable);

        when(orchestratable.getResponse()).thenReturn(expectedResponse);

        // Actual Invocation
        PassthroughInboundDocRetrieve inboundDocRetrieve = new PassthroughInboundDocRetrieve(pt, at, ad, orch);
        RetrieveDocumentSetResponseType actualResponse = inboundDocRetrieve.respondingGatewayCrossGatewayRetrieve(
                request, assertion);

        // Verify response is expected        
        assertSame(expectedResponse, actualResponse);

        // Verify that the orchestrator is processing the correct passthru orchestratable
        ArgumentCaptor<InboundDocRetrieveOrchestratable> orchArgument = ArgumentCaptor
                .forClass(InboundDocRetrieveOrchestratable.class);

        verify(orch).process(orchArgument.capture());
        assertEquals(pt, orchArgument.getValue().getPolicyTransformer());
        assertEquals(at, orchArgument.getValue().getAuditTransformer());
        assertEquals(ad, orchArgument.getValue().getDelegate());
        assertTrue(orchArgument.getValue().isPassthru());
    }

}
