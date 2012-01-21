package gov.hhs.fha.nhinc.docquery.entity;


/**
 * DocQuery OrchestrationContext for g0 endpoint
 * @author paul.eftis
 */
public class EntityDocQueryOrchestrationContextBuilder_g1
            extends EntityDocQueryOrchestrationContextBuilder{

    /**
     * @return strategy for g1
     */
    @Override
    protected NhinDocQueryStrategy getStrategy(){
        return new NhinDocQueryStrategyImpl_g1();
    }


    /**
     * @return EntityDocQueryOrchestratable for g1
     */
    @Override
    protected EntityDocQueryOrchestratable getOrchestratable(){
        EntityDocQueryOrchestratable_a1 orch_a1 = new EntityDocQueryOrchestratable_a1(
                        getNhinDelegate(), getProcessor(), getAuditTransformer(),
                        getPolicyTransformer(), getAssertionType(), getServiceName(),
                        getTargetSystemType(), getRequest());
        return orch_a1;
    }
}