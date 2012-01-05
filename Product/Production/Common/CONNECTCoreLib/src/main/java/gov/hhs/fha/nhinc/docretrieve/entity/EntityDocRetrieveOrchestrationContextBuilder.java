package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import gov.hhs.fha.nhinc.orchestration.NhinDelegate;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContext;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;

public class EntityDocRetrieveOrchestrationContextBuilder implements OrchestrationContextBuilder {
	
	private RetrieveDocumentSetRequestType retrieveDocumentSetRequestType;
	private AssertionType assertionType;
	private PolicyTransformer policyTransformer;
	private AuditTransformer auditTransformer;
	private NhinDelegate nhinDelegate;
	private NhinAggregator nhinAggregator;
	
	
	public RetrieveDocumentSetRequestType getRetrieveDocumentSetRequestType() {
		return retrieveDocumentSetRequestType;
	}
	public EntityDocRetrieveOrchestrationContextBuilder setRetrieveDocumentSetRequestType(
			RetrieveDocumentSetRequestType retrieveDocumentSetRequestType) {
		this.retrieveDocumentSetRequestType = retrieveDocumentSetRequestType;
		return this;
	}
	public AssertionType getAssertionType() {
		return assertionType;
	}
	public EntityDocRetrieveOrchestrationContextBuilder setAssertionType(AssertionType assertionType) {
		this.assertionType = assertionType;
		return this;
	}
	public PolicyTransformer getPolicyTransformer() {
		return policyTransformer;
	}
	public EntityDocRetrieveOrchestrationContextBuilder setPolicyTransformer(PolicyTransformer policyTransformer) {
		this.policyTransformer = policyTransformer;
		return this;
	}
	public AuditTransformer getAuditTransformer() {
		return auditTransformer;
	}
	public EntityDocRetrieveOrchestrationContextBuilder setAuditTransformer(AuditTransformer auditTransformer) {
		this.auditTransformer = auditTransformer;
		return this;
	}
	public NhinDelegate getNhinDelegate() {
		return nhinDelegate;
	}
	public EntityDocRetrieveOrchestrationContextBuilder setNhinDelegate(NhinDelegate nhinDelegate) {
		this.nhinDelegate = nhinDelegate;
		return this;
	}
	public NhinAggregator getNhinAggregator() {
		return nhinAggregator;
	}
	public EntityDocRetrieveOrchestrationContextBuilder setNhinAggregator(NhinAggregator nhinAggregator) {
		this.nhinAggregator = nhinAggregator;
		return this;
	}

	@Override
	public OrchestrationContext build() {
		return new OrchestrationContext(new NhinDocRetrieveStrategyImpl_g0(), new EntityDocRetrieveOrchestratableImpl_a0(getRetrieveDocumentSetRequestType(), getAssertionType(), getPolicyTransformer(), getAuditTransformer(), getNhinDelegate(), getNhinAggregator()));
	}	

}
