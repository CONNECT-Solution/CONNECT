package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.orchestration.*;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;

public class OutboundDocRetrieveOrchestrationContextBuilder_g1 implements OrchestrationContextBuilder,
        OutboundDocRetrieveContextBuilder {

    private RetrieveDocumentSetRequestType retrieveDocumentSetRequestType;
    private AssertionType assertionType;
    private PolicyTransformer policyTransformer;
    private AuditTransformer auditTransformer;
    private OutboundDelegate outboundDelegate;
    private NhinAggregator nhinAggregator;
    private NhinTargetSystemType nhinTarget;

    public RetrieveDocumentSetRequestType getRetrieveDocumentSetRequestType() {
        return retrieveDocumentSetRequestType;
    }

    public OutboundDocRetrieveOrchestrationContextBuilder_g1 setRetrieveDocumentSetRequestType(
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

    public OutboundDelegate getOutboundDelegate() {
        return outboundDelegate;
    }

    public OutboundDocRetrieveOrchestrationContextBuilder_g1 setOutboundDelegate(OutboundDelegate outboundDelegate) {
        this.outboundDelegate = outboundDelegate;
        return this;
    }

    public NhinAggregator getNhinAggregator() {
        return nhinAggregator;
    }

    public void setNhinAggregator(NhinAggregator nhinAggregator) {
        this.nhinAggregator = nhinAggregator;
    }

    void setTarget(NhinTargetSystemType target) {
        nhinTarget = target;
    }

    public NhinTargetSystemType getTarget() {
        return nhinTarget;
    }

    @Override
    public OrchestrationContext build() {
        return new OrchestrationContext(new OutboundDocRetrieveStrategyImpl_g1(),
                new OutboundDocRetrieveOrchestratableImpl(getRetrieveDocumentSetRequestType(), getAssertionType(),
                        getPolicyTransformer(), getAuditTransformer(), getOutboundDelegate(), getNhinAggregator(),
                        getTarget()));
    }

    @Override
    public void setContextMessage(OutboundOrchestratable message) {
        if (message instanceof OutboundDocRetrieveOrchestratable) {
            setContextMessage((OutboundDocRetrieveOrchestratable) message);
        }
    }

    public void setContextMessage(OutboundDocRetrieveOrchestratable message) {
        setAssertionType(message.getAssertion());
        setAuditTransformer(message.getAuditTransformer());
        setNhinAggregator(message.getAggregator());
        setPolicyTransformer(message.getPolicyTransformer());
        setTarget(message.getTarget());
        setRetrieveDocumentSetRequestType(message.getRequest());
        setOutboundDelegate(message.getNhinDelegate());
    }
}
