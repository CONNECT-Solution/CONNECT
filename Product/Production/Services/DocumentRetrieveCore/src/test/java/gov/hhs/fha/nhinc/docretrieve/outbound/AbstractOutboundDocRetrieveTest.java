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

import gov.hhs.fha.nhinc.audit.ejb.AuditEJBLogger;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.docretrieve.audit.DocRetrieveAuditLogger;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundDocRetrieveOrchestratable;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import gov.hhs.fha.nhinc.orchestration.CONNECTOutboundOrchestrator;
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author msw
 *
 */
public abstract class AbstractOutboundDocRetrieveTest {

    protected AuditEJBLogger mockEJBLogger = mock(AuditEJBLogger.class);

    /**
     * @param orchestrator
     * @param isAuditOn
     * @return
     */
    protected abstract OutboundDocRetrieve getOutboundDocRetrieve(CONNECTOutboundOrchestrator orchestrator,
        boolean isAuditOn);

    @Test
    public void test30GuidanceOnA0Interface() {
        String guidance = "3.0";

        CONNECTOutboundOrchestrator orchestrator = mock(CONNECTOutboundOrchestrator.class);
        when(orchestrator.process(any(Orchestratable.class))).thenReturn(getSuccessOrchResult());
        OutboundDocRetrieve instance = getOutboundDocRetrieve(orchestrator, true);

        RetrieveDocumentSetRequestType request = mock(RetrieveDocumentSetRequestType.class);
        AssertionType assertion = mock(AssertionType.class);
        NhinTargetCommunitiesType targets = getTargetWithGuidance(guidance);

        RetrieveDocumentSetResponseType resp = instance.respondingGatewayCrossGatewayRetrieve(request, assertion,
            targets, ADAPTER_API_LEVEL.LEVEL_a0);
        validateErrorResponse(resp, ADAPTER_API_LEVEL.LEVEL_a0.toString(), guidance);
    }

    @Test
    public void test30GuidanceOnA1Interface() {
        String guidance = "3.0";

        CONNECTOutboundOrchestrator orchestrator = mock(CONNECTOutboundOrchestrator.class);
        when(orchestrator.process(any(Orchestratable.class))).thenReturn(getSuccessOrchResult());
        OutboundDocRetrieve instance = getOutboundDocRetrieve(orchestrator, true);

        RetrieveDocumentSetRequestType request = mock(RetrieveDocumentSetRequestType.class);
        AssertionType assertion = mock(AssertionType.class);
        NhinTargetCommunitiesType targets = getTargetWithGuidance(guidance);

        RetrieveDocumentSetResponseType resp = instance.respondingGatewayCrossGatewayRetrieve(request, assertion,
            targets, ADAPTER_API_LEVEL.LEVEL_a1);
        validateSuccessResponse(resp);
    }

    @Test
    public void testNoGuidanceOnA1Interface() {
        String guidance = null;

        CONNECTOutboundOrchestrator orchestrator = mock(CONNECTOutboundOrchestrator.class);
        when(orchestrator.process(any(Orchestratable.class))).thenReturn(getSuccessOrchResult());
        OutboundDocRetrieve instance = getOutboundDocRetrieve(orchestrator, true);

        RetrieveDocumentSetRequestType request = mock(RetrieveDocumentSetRequestType.class);
        AssertionType assertion = mock(AssertionType.class);
        NhinTargetCommunitiesType targets = getTargetWithGuidance(guidance);

        RetrieveDocumentSetResponseType resp = instance.respondingGatewayCrossGatewayRetrieve(request, assertion,
            targets, ADAPTER_API_LEVEL.LEVEL_a1);
        validateSuccessResponse(resp);
    }

    @Test
    public void testNoGuidanceOnA0Interface() {
        String guidance = null;

        CONNECTOutboundOrchestrator orchestrator = mock(CONNECTOutboundOrchestrator.class);
        when(orchestrator.process(any(Orchestratable.class))).thenReturn(getSuccessOrchResult());
        OutboundDocRetrieve instance = getOutboundDocRetrieve(orchestrator, true);

        RetrieveDocumentSetRequestType request = mock(RetrieveDocumentSetRequestType.class);
        AssertionType assertion = mock(AssertionType.class);
        NhinTargetCommunitiesType targets = getTargetWithGuidance(guidance);

        RetrieveDocumentSetResponseType resp = instance.respondingGatewayCrossGatewayRetrieve(request, assertion,
            targets, ADAPTER_API_LEVEL.LEVEL_a0);
        validateSuccessResponse(resp);
    }

    @Test
    public void test20GuidanceOnA0Interface() {
        String guidance = "2.0";

        CONNECTOutboundOrchestrator orchestrator = mock(CONNECTOutboundOrchestrator.class);
        when(orchestrator.process(any(Orchestratable.class))).thenReturn(getSuccessOrchResult());
        OutboundDocRetrieve instance = getOutboundDocRetrieve(orchestrator, true);

        RetrieveDocumentSetRequestType request = mock(RetrieveDocumentSetRequestType.class);
        AssertionType assertion = mock(AssertionType.class);
        NhinTargetCommunitiesType targets = getTargetWithGuidance(guidance);

        RetrieveDocumentSetResponseType resp = instance.respondingGatewayCrossGatewayRetrieve(request, assertion,
            targets, ADAPTER_API_LEVEL.LEVEL_a0);
        validateSuccessResponse(resp);
    }

    @Test
    public void test20GuidanceOnA1Interface() {
        String guidance = "2.0";

        CONNECTOutboundOrchestrator orchestrator = mock(CONNECTOutboundOrchestrator.class);
        when(orchestrator.process(any(Orchestratable.class))).thenReturn(getSuccessOrchResult());
        OutboundDocRetrieve instance = getOutboundDocRetrieve(orchestrator, true);

        RetrieveDocumentSetRequestType request = mock(RetrieveDocumentSetRequestType.class);
        AssertionType assertion = mock(AssertionType.class);
        NhinTargetCommunitiesType targets = getTargetWithGuidance(guidance);

        RetrieveDocumentSetResponseType resp = instance.respondingGatewayCrossGatewayRetrieve(request, assertion,
            targets, ADAPTER_API_LEVEL.LEVEL_a1);
        validateSuccessResponse(resp);
    }

    /**
     * @return
     */
    private Orchestratable getSuccessOrchResult() {
        return new OutboundDocRetrieveOrchestratable() {
            /*
             * (non-Javadoc)
             *
             * @see gov.hhs.fha.nhinc.docretrieve.entity.OutboundDocRetrieveOrchestratable#getResponse()
             */
            @Override
            public RetrieveDocumentSetResponseType getResponse() {
                RetrieveDocumentSetResponseType resp = new RetrieveDocumentSetResponseType();
                RegistryResponseType regResp = new RegistryResponseType();
                regResp.setStatus(NhincConstants.NHINC_ADHOC_QUERY_SUCCESS_RESPONSE);
                resp.setRegistryResponse(regResp);
                return resp;
            }

            @Override
            public boolean isPassthru() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public OutboundDocRetrieveOrchestratable create(PolicyTransformer pt,
                OutboundDelegate nd, NhinAggregator na) {
                // TODO Auto-generated method stub
                return null;
            }
        };
    }

    /**
     * @param resp
     */
    private void validateErrorResponse(RetrieveDocumentSetResponseType resp, String entityAPILevel, String guidance) {
        RegistryResponseType regResp = resp.getRegistryResponse();
        assertNotNull(regResp);

        assertEquals(NhincConstants.XDR_ACK_FAILURE_STATUS_MSG, regResp.getStatus());

        RegistryErrorList registryErrorList = regResp.getRegistryErrorList();
        assertNotNull(registryErrorList);

        assertEquals(registryErrorList.getRegistryError().size(), 1);
        RegistryError regError = registryErrorList.getRegistryError().get(0);

        assertEquals("XDSRepositoryError", regError.getErrorCode());
        assertEquals("Entity Document Retrieve " + entityAPILevel, regError.getLocation());
        assertEquals("urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error", regError.getSeverity());
        assertEquals(NhincConstants.INIT_MULTISPEC_ERROR_UNSUPPORTED_GUIDANCE, regError.getCodeContext());
    }

    /**
     * @param resp
     */
    private void validateSuccessResponse(RetrieveDocumentSetResponseType resp) {
        RegistryResponseType regResp = resp.getRegistryResponse();
        assertNotNull(regResp);

        assertEquals(NhincConstants.NHINC_ADHOC_QUERY_SUCCESS_RESPONSE, regResp.getStatus());

        RegistryErrorList registryErrorList = regResp.getRegistryErrorList();
        assertEquals(null, registryErrorList);
    }

    /**
     * @param guidance value for spec version, should be 2.0 or 3.0 for DQ/DR.
     * @return
     */
    private NhinTargetCommunitiesType getTargetWithGuidance(String guidance) {
        NhinTargetCommunitiesType target = new NhinTargetCommunitiesType();
        target.setUseSpecVersion(guidance);

        NhinTargetCommunityType targetCommunity = new NhinTargetCommunityType();
        target.getNhinTargetCommunity().add(targetCommunity);
        return target;
    }

    protected DocRetrieveAuditLogger getAuditLogger(final boolean isLoggingOn) {
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
