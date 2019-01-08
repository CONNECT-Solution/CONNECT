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
package gov.hhs.fha.nhinc.docretrieve._20.outbound;

import gov.hhs.fha.nhinc.aspect.OutboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetRequestTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetResponseTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.audit.DocRetrieveAuditLogger;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundDocRetrieveOrchestratable;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import gov.hhs.fha.nhinc.orchestration.CONNECTOutboundOrchestrator;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import java.lang.reflect.Method;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author akong
 *
 */
public class PassthroughOutboundDocRetrieveTest {

    DocRetrieveAuditLogger logger;

    @Before
    public void setup() {
        logger = mock(DocRetrieveAuditLogger.class);
    }

    @Test
    public void hasOutboundProcessingEvent() throws Exception {
        Class<PassthroughOutboundDocRetrieve> clazz = PassthroughOutboundDocRetrieve.class;
        Method method = clazz.getMethod("respondingGatewayCrossGatewayRetrieve", RetrieveDocumentSetRequestType.class,
            AssertionType.class, NhinTargetCommunitiesType.class, ADAPTER_API_LEVEL.class);
        OutboundProcessingEvent annotation = method.getAnnotation(OutboundProcessingEvent.class);
        assertNotNull(annotation);
        assertEquals(RetrieveDocumentSetRequestTypeDescriptionBuilder.class, annotation.beforeBuilder());
        assertEquals(RetrieveDocumentSetResponseTypeDescriptionBuilder.class, annotation.afterReturningBuilder());
        assertEquals("Retrieve Document", annotation.serviceType());
        assertEquals("2.0", annotation.version());
    }

    @Test
    public void invoke() {

        RetrieveDocumentSetRequestType request = new RetrieveDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        NhinTargetCommunitiesType targets = new NhinTargetCommunitiesType();
        RetrieveDocumentSetResponseType dr30Response = createDR30Response();

        // Mocks
        CONNECTOutboundOrchestrator orchestrator = mock(CONNECTOutboundOrchestrator.class);
        OutboundDocRetrieveOrchestratable orchResponse = mock(OutboundDocRetrieveOrchestratable.class);
        // Method Stubbing
        when(orchestrator.process(any(OutboundDocRetrieveOrchestratable.class))).thenReturn(orchResponse);

        when(orchResponse.getResponse()).thenReturn(dr30Response);

        // Actual invocation
        PassthroughOutboundDocRetrieve outboundDocRetrieve = new PassthroughOutboundDocRetrieve(orchestrator, logger);

        RetrieveDocumentSetResponseType actualResponse = outboundDocRetrieve.respondingGatewayCrossGatewayRetrieve(
            request, assertion, targets, ADAPTER_API_LEVEL.LEVEL_a0);

        // Verify that the response is DR20 spec compliant
        assertEquals(2, actualResponse.getDocumentResponse().size());
        assertNull(actualResponse.getDocumentResponse().get(0).getNewDocumentUniqueId());
        assertNull(actualResponse.getDocumentResponse().get(0).getNewRepositoryUniqueId());
        assertNull(actualResponse.getDocumentResponse().get(1).getNewDocumentUniqueId());
        assertNull(actualResponse.getDocumentResponse().get(1).getNewRepositoryUniqueId());
    }

    private RetrieveDocumentSetResponseType createDR30Response() {
        RetrieveDocumentSetResponseType dr30Response = new RetrieveDocumentSetResponseType();
        dr30Response.getDocumentResponse().add(new DocumentResponse());
        dr30Response.getDocumentResponse().add(new DocumentResponse());

        dr30Response.getDocumentResponse().get(0).setNewDocumentUniqueId("docId0");
        dr30Response.getDocumentResponse().get(0).setNewRepositoryUniqueId("repoId0");

        dr30Response.getDocumentResponse().get(1).setNewDocumentUniqueId("docId1");
        dr30Response.getDocumentResponse().get(1).setNewRepositoryUniqueId("repoId1");

        return dr30Response;
    }

}
