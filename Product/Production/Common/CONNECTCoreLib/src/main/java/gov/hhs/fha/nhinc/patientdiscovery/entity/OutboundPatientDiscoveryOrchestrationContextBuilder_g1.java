package gov.hhs.fha.nhinc.patientdiscovery.entity;


/**
 * PatientDiscovery OrchestrationContext for g1 endpoint
 * @author paul.eftis
 */
public class OutboundPatientDiscoveryOrchestrationContextBuilder_g1
        extends OutboundPatientDiscoveryOrchestrationContextBuilder{


    /**
     * @return strategy for g1
     */
    @Override
    protected OutboundPatientDiscoveryStrategy getStrategy(){
        return new OutboundPatientDiscoveryStrategyImpl_g1();
    }


    /**
     * @return OutboundPatientDiscoveryOrchestratable for g1
     */
    @Override
    protected OutboundPatientDiscoveryOrchestratable getOrchestratable(){
        OutboundPatientDiscoveryOrchestratable_a1 orch_a1 = new OutboundPatientDiscoveryOrchestratable_a1(
                        getNhinDelegate(), getProcessor(), getAuditTransformer(),
                        getPolicyTransformer(), getAssertionType(), getServiceName(),
                        getTargetSystemType(), getRequest());
        return orch_a1;
    }

}
