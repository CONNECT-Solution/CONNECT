package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.orchestration.*;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;

public class OutboundDocRetrieveOrchestrationContextBuilder_g0 implements OrchestrationContextBuilder {

    private RetrieveDocumentSetRequestType retrieveDocumentSetRequestType;
    private AssertionType assertionType;
    private PolicyTransformer policyTransformer;
    private AuditTransformer auditTransformer;
    private OutboundDelegate nhinDelegate;
    private InboundAggregator nhinAggregator;
    private NhinTargetSystemType nhinTarget;

    public RetrieveDocumentSetRequestType getRetrieveDocumentSetRequestType() {
        return retrieveDocumentSetRequestType;
    }

    public OutboundDocRetrieveOrchestrationContextBuilder_g0 setRetrieveDocumentSetRequestType(
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

    public OutboundDelegate getNhinDelegate() {
        return nhinDelegate;
    }

    public OutboundDocRetrieveOrchestrationContextBuilder_g0 setNhinDelegate(OutboundDelegate nhinDelegate) {
        this.nhinDelegate = nhinDelegate;
        return this;
    }

    public InboundAggregator getNhinAggregator() {
        return nhinAggregator;
    }

    public void setNhinAggregator(InboundAggregator nhinAggregator) {
        this.nhinAggregator = nhinAggregator;
    }

    void setTarget(NhinTargetSystemType target) {
        nhinTarget = target;
    }

    public NhinTargetSystemType getTarget()
    {
        return nhinTarget;
    }

    @Override
    public OrchestrationContext build() {
        return new OrchestrationContext(new OutboundDocRetrieveStrategyImpl_g0(), new EntityDocRetrieveOrchestratableImpl_a0(getRetrieveDocumentSetRequestType(), getAssertionType(), getPolicyTransformer(), getAuditTransformer(), getNhinDelegate(), getNhinAggregator(), getTarget()));
    }


}
