package gov.hhs.fha.nhinc.docquery.entity;


/**
 * DocQuery OrchestrationContext for g0 endpoint
 * @author paul.eftis
 */
public class OutboundDocQueryOrchestrationContextBuilder_g1
            extends OutboundDocQueryOrchestrationContextBuilder{

    /**
     * @return strategy for g1
     */
    @Override
    protected OutboundDocQueryStrategy getStrategy(){
        return new OutboundDocQueryStrategyImpl_g1();
    }


    /**
     * @return OutboundDocQueryOrchestratable for g1
     */
    @Override
    protected OutboundDocQueryOrchestratable getOrchestratable(){
        OutboundDocQueryOrchestratable_a1 orch_a1 = new OutboundDocQueryOrchestratable_a1(
                        getNhinDelegate(), getProcessor(), getAuditTransformer(),
                        getPolicyTransformer(), getAssertionType(), getServiceName(),
                        getTargetSystemType(), getRequest());
        return orch_a1;
    }
}