package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.orchestration.*;
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
	public void setAssertionType(AssertionType assertionType) {
		this.assertionType = assertionType;
	}
	public PolicyTransformer getPolicyTransformer() {
		return policyTransformer;
	}
	public void setPolicyTransformer(PolicyTransformer policyTransformer) {
		this.policyTransformer = policyTransformer;
	}
	public AuditTransformer getAuditTransformer() {
		return auditTransformer;
	}
	public void setAuditTransformer(AuditTransformer auditTransformer) {
		this.auditTransformer = auditTransformer;
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
	public void setNhinAggregator(NhinAggregator nhinAggregator) {
		this.nhinAggregator = nhinAggregator;
	}

	@Override
	public OrchestrationContext build() {
		return new OrchestrationContext(new NhinDocRetrieveStrategyImpl_g0(), new EntityDocRetrieveOrchestratableImpl_a0(getRetrieveDocumentSetRequestType(), getAssertionType(), getPolicyTransformer(), getAuditTransformer(), getNhinDelegate(), getNhinAggregator()));
	}

    public void init(EntityOrchestratable message) {
        setAssertionType(message.getAssertion());
        setAuditTransformer(message.getAuditTransformer());
    }

}
