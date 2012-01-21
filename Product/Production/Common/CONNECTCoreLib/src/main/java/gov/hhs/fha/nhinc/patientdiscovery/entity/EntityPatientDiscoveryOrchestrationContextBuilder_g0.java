package gov.hhs.fha.nhinc.patientdiscovery.entity;


/**
 * PatientDiscovery OrchestrationContext for g0 endpoint
 * 
 * @author paul.eftis
 */
public class EntityPatientDiscoveryOrchestrationContextBuilder_g0
        extends EntityPatientDiscoveryOrchestrationContextBuilder {


    /**
     * @return strategy for g0
     */
    @Override
    protected NhinPatientDiscoveryStrategy getStrategy(){
        return new NhinPatientDiscoveryStrategyImpl_g0();
    }


    /**
     * @return EntityPatientDiscoveryOrchestratable for g0
     */
    @Override
    protected EntityPatientDiscoveryOrchestratable getOrchestratable(){
        EntityPatientDiscoveryOrchestratable_a0 orch_a0 = new EntityPatientDiscoveryOrchestratable_a0(
                        getNhinDelegate(), getProcessor(), getAuditTransformer(),
                        getPolicyTransformer(), getAssertionType(), getServiceName(),
                        getTargetSystemType(), getRequest());
        return orch_a0;
    }

}
