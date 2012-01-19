package gov.hhs.fha.nhinc.patientdiscovery.entity;



/**
 * Builds the PatientDiscovery OrchestrationContext for g1 endpoint
 * @author paul.eftis
 */
public class EntityPatientDiscoveryOrchestrationContextBuilder_g1 extends EntityPatientDiscoveryOrchestrationContextBuilder{


	@Override
	protected EntityPatientDiscoveryOrchestratable getOrchestratable() {
		return new EntityPatientDiscoveryOrchestratable_a1(
                getNhinDelegate(), getProcessor(), getAuditTransformer(),
                getPolicyTransformer(), getAssertionType(), getServiceName(),
                getTargetSystemType(), getRequest());
	}

	@Override
	protected NhinPatientDiscoveryStrategy getStrategy() {
		return new NhinPatientDiscoveryStrategyImpl_g1();
	}	

}
