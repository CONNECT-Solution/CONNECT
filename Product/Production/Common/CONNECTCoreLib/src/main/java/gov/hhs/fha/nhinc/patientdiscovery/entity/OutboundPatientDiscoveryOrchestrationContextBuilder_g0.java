package gov.hhs.fha.nhinc.patientdiscovery.entity;


/**
 * PatientDiscovery OrchestrationContext for g0 endpoint
 * 
 * @author paul.eftis
 */
public class OutboundPatientDiscoveryOrchestrationContextBuilder_g0
        extends OutboundPatientDiscoveryOrchestrationContextBuilder {


    /**
     * @return strategy for g0
     */
    @Override
    protected OutboundPatientDiscoveryStrategy getStrategy(){
        return new OutboundPatientDiscoveryStrategyImpl_g0();
    }


    /**
     * @return OutboundPatientDiscoveryOrchestratable for g0
     */
    @Override
    protected OutboundPatientDiscoveryOrchestratable getOrchestratable(){
        OutboundPatientDiscoveryOrchestratable_a0 orch_a0 = new OutboundPatientDiscoveryOrchestratable_a0(
                        getNhinDelegate(), getProcessor(), getAuditTransformer(),
                        getPolicyTransformer(), getAssertionType(), getServiceName(),
                        getTargetSystemType(), getRequest());
        return orch_a0;
    }

}
