/**
 * 
 */
package gov.hhs.fha.nhinc.patientdiscovery.entity;

import org.hl7.v3.PRPAIN201305UV02;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.NhinDelegate;
import gov.hhs.fha.nhinc.orchestration.NhinResponseProcessor;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContext;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;

/**
 * @author bhumphrey
 *
 */
public abstract class EntityPatientDiscoveryOrchestrationContextBuilder implements
		OrchestrationContextBuilder {

	private NhinTargetSystemType target = null;
	private PRPAIN201305UV02 request = null;
	private AssertionType assertionType = null;
	private PolicyTransformer policyTransformer = null;
	private AuditTransformer auditTransformer = null;
	private NhinDelegate nhinDelegate = null;
	private NhinResponseProcessor nhinProcessor = null;
	private String serviceName = "";
	
	@Override
	public OrchestrationContext build() {
		return new OrchestrationContext(
				getStrategy(), getOrchestratable());
	}
	
	abstract protected EntityPatientDiscoveryOrchestratable getOrchestratable();
	
	abstract protected NhinPatientDiscoveryStrategy getStrategy();

	public EntityPatientDiscoveryOrchestrationContextBuilder setTarget(NhinTargetSystemType t) {
		this.target = t;
		return this;
	}

	protected NhinTargetSystemType getTargetSystemType() {
		return this.target;
	}

	public EntityPatientDiscoveryOrchestrationContextBuilder setRequest(PRPAIN201305UV02 pdRequest) {
		this.request = pdRequest;
		return this;
	}

	protected PRPAIN201305UV02 getRequest() {
		return this.request;
	}

	protected AssertionType getAssertionType() {
		return assertionType;
	}

	public EntityPatientDiscoveryOrchestrationContextBuilder setAssertionType(AssertionType assertionType) {
		this.assertionType = assertionType;
		return this;
	}

	protected PolicyTransformer getPolicyTransformer() {
		return policyTransformer;
	}

	public EntityPatientDiscoveryOrchestrationContextBuilder setPolicyTransformer(PolicyTransformer policyTransformer) {
		this.policyTransformer = policyTransformer;
		return this;
	}

	protected AuditTransformer getAuditTransformer() {
		return auditTransformer;
	}

	public EntityPatientDiscoveryOrchestrationContextBuilder setAuditTransformer(AuditTransformer auditTransformer) {
		this.auditTransformer = auditTransformer;
		return this;
	}

	protected NhinDelegate getNhinDelegate() {
		return nhinDelegate;
	}

	public EntityPatientDiscoveryOrchestrationContextBuilder setNhinDelegate(NhinDelegate nhinDelegate) {
		this.nhinDelegate = nhinDelegate;
		return this;
	}

	protected NhinResponseProcessor getAggregator() {
		return nhinProcessor;
	}

	public EntityPatientDiscoveryOrchestrationContextBuilder setAggregator(NhinResponseProcessor processor) {
		nhinProcessor = processor;
		return this;
	}

	protected NhinResponseProcessor getProcessor() {
		return nhinProcessor;
	}

	public EntityPatientDiscoveryOrchestrationContextBuilder setProcessor(NhinResponseProcessor processor) {
		this.nhinProcessor = processor;
		return this;
	}

	protected String getServiceName() {
		return serviceName;
	}

	public EntityPatientDiscoveryOrchestrationContextBuilder setServiceName(String name) {
		this.serviceName = name;
		return this;
	}

}
