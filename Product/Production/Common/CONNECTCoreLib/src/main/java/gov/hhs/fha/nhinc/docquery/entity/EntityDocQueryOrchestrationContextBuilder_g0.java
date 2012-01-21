package gov.hhs.fha.nhinc.docquery.entity;


/**
 * DocQuery OrchestrationContext for g0 endpoint
 * @author paul.eftis
 */
public class EntityDocQueryOrchestrationContextBuilder_g0 
            extends EntityDocQueryOrchestrationContextBuilder{

    /**
     * @return strategy for g0
     */
    @Override
    protected NhinDocQueryStrategy getStrategy(){
        return new NhinDocQueryStrategyImpl_g0();
    }


    /**
     * @return EntityDocQueryOrchestratable for g0
     */
    @Override
    protected EntityDocQueryOrchestratable getOrchestratable(){
        EntityDocQueryOrchestratable_a0 orch_a0 = new EntityDocQueryOrchestratable_a0(
                        getNhinDelegate(), getProcessor(), getAuditTransformer(),
                        getPolicyTransformer(), getAssertionType(), getServiceName(),
                        getTargetSystemType(), getRequest());
        return orch_a0;
    }
}
