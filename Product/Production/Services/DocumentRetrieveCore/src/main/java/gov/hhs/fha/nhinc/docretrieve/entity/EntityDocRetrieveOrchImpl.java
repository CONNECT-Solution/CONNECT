package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EntityDocRetrieveOrchImpl {

    private static Log log = LogFactory.getLog(EntityDocRetrieveOrchImpl.class);

    public EntityDocRetrieveOrchImpl() {
    }

   
    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(
            ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body, AssertionType assertion) {

        PolicyTransformer pt = new OutboundDocRetrievePolicyTransformer_a0();
        AuditTransformer at = new OutboundDocRetrieveAuditTransformer_a0();
        OutboundDelegate nd = new OutboundDocRetrieveDelegate();
        NhinAggregator na = new OutboundDocRetrieveAggregator_a0();
        OutboundDocRetrieveOrchestratable outboundDROrchestratable = new OutboundDocRetrieveOrchestratableImpl(body,
                assertion, pt, at, nd, na, null);
        OutboundDocRetrieveOrchestratorImpl oOrchestrator = new OutboundDocRetrieveOrchestratorImpl();
        oOrchestrator.process(outboundDROrchestratable);
        return outboundDROrchestratable.getResponse();
    }
}
