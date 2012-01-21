package gov.hhs.fha.nhinc.patientdiscovery.entity;


/**
 * PatientDiscovery OrchestrationContext for g1 endpoint
 * @author paul.eftis
 */
public class EntityPatientDiscoveryOrchestrationContextBuilder_g1
        extends EntityPatientDiscoveryOrchestrationContextBuilder{


    /**
     * @return strategy for g1
     */
    @Override
    protected NhinPatientDiscoveryStrategy getStrategy(){
        return new NhinPatientDiscoveryStrategyImpl_g1();
    }


    /**
     * @return EntityPatientDiscoveryOrchestratable for g1
     */
    @Override
    protected EntityPatientDiscoveryOrchestratable getOrchestratable(){
        EntityPatientDiscoveryOrchestratable_a1 orch_a1 = new EntityPatientDiscoveryOrchestratable_a1(
                        getNhinDelegate(), getProcessor(), getAuditTransformer(),
                        getPolicyTransformer(), getAssertionType(), getServiceName(),
                        getTargetSystemType(), getRequest());
        return orch_a1;
    }

}
