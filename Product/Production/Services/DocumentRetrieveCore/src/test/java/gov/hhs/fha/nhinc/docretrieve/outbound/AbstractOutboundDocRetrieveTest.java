/**
 * 
 */
package gov.hhs.fha.nhinc.docretrieve.outbound;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundDocRetrieveOrchestratable;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
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

import org.junit.Test;

/**
 * @author msw
 * 
 */
public abstract class AbstractOutboundDocRetrieveTest {
    /**
     * @param orchestrator
     * @return
     */
    protected abstract OutboundDocRetrieve getOutboundDocRetrieve(CONNECTOutboundOrchestrator orchestrator);

    @Test
    public void test30GuidanceOnA0Interface() {
        String guidance = "3.0";

        CONNECTOutboundOrchestrator orchestrator = mock(CONNECTOutboundOrchestrator.class);
        when(orchestrator.process(any(Orchestratable.class))).thenReturn(getSuccessOrchResult());
        OutboundDocRetrieve instance = getOutboundDocRetrieve(orchestrator);

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
        OutboundDocRetrieve instance = getOutboundDocRetrieve(orchestrator);

        RetrieveDocumentSetRequestType request = mock(RetrieveDocumentSetRequestType.class);
        AssertionType assertion = mock(AssertionType.class);
        NhinTargetCommunitiesType targets = getTargetWithGuidance(guidance);

        RetrieveDocumentSetResponseType resp = instance.respondingGatewayCrossGatewayRetrieve(request, assertion,
                targets, ADAPTER_API_LEVEL.LEVEL_a1);
        validateSuccessResponse(resp);
    }

    @Test
    public void test20GuidanceOnA0Interface() {
        String guidance = "2.0";

        CONNECTOutboundOrchestrator orchestrator = mock(CONNECTOutboundOrchestrator.class);
        when(orchestrator.process(any(Orchestratable.class))).thenReturn(getSuccessOrchResult());
        OutboundDocRetrieve instance = getOutboundDocRetrieve(orchestrator);

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
        OutboundDocRetrieve instance = getOutboundDocRetrieve(orchestrator);

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
        Orchestratable orch = new OutboundDocRetrieveOrchestratable() {
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
            public OutboundDocRetrieveOrchestratable create(PolicyTransformer pt, AuditTransformer at,
                    OutboundDelegate nd, NhinAggregator na) {
                // TODO Auto-generated method stub
                return null;
            }
        };
        return orch;
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
}
