package gov.hhs.fha.nhinc.docquery.entity;


/**
 * DocQuery OrchestrationContext for g0 endpoint
 * @author paul.eftis
 */
public class OutboundDocQueryOrchestrationContextBuilder_g0
            extends OutboundDocQueryOrchestrationContextBuilder{

    /**
     * @return strategy for g0
     */
    @Override
    protected OutboundDocQueryStrategy getStrategy(){
        return new OutboundDocQueryStrategyImpl_g0();
    }


    /**
     * @return OutboundDocQueryOrchestratable for g0
     */
    @Override
    protected OutboundDocQueryOrchestratable getOrchestratable(){
        OutboundDocQueryOrchestratable_a0 orch_a0 = new OutboundDocQueryOrchestratable_a0(
                        getNhinDelegate(), getProcessor(), getAuditTransformer(),
                        getPolicyTransformer(), getAssertionType(), getServiceName(),
                        getTargetSystemType(), getRequest());
        return orch_a0;
    }
}
