/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.nhin;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.InboundDelegate;
import gov.hhs.fha.nhinc.orchestration.InboundOrchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContext;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;

/**
 * 
 * @author mweaver
 */
public class InboundDocRetrieveOrchestrationContextBuilder implements OrchestrationContextBuilder,
        InboundDocRetrieveContextBuilder {

    private RetrieveDocumentSetRequestType retrieveDocumentSetRequestType;
    private AssertionType assertionType;
    private PolicyTransformer policyTransformer;
    private AuditTransformer auditTransformer;
    private InboundDelegate inboundDelegate;

    public RetrieveDocumentSetRequestType getRetrieveDocumentSetRequestType() {
        return retrieveDocumentSetRequestType;
    }

    public InboundDocRetrieveOrchestrationContextBuilder setRetrieveDocumentSetRequestType(
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

    public InboundDelegate getInboundDelegate() {
        return inboundDelegate;
    }

    public InboundDocRetrieveOrchestrationContextBuilder setInboundDelegate(InboundDelegate inboundDelegate) {
        this.inboundDelegate = inboundDelegate;
        return this;
    }

    @Override
    public OrchestrationContext build() {
        return new OrchestrationContext(new InboundDocRetrieveStrategyImpl(), new InboundDocRetrieveOrchestratableImpl(
                getRetrieveDocumentSetRequestType(), getAssertionType(), getPolicyTransformer(), getAuditTransformer(),
                getInboundDelegate()));
    }

    @Override
    public void setContextMessage(InboundOrchestratable message) {
        if (message instanceof InboundDocRetrieveOrchestratableImpl) {
            setContextMessage((InboundDocRetrieveOrchestratableImpl) message);
        }
    }

    public void setContextMessage(InboundDocRetrieveOrchestratableImpl message) {
        setAssertionType(message.getAssertion());
        setAuditTransformer(message.getAuditTransformer());
        setPolicyTransformer(message.getPolicyTransformer());
        setRetrieveDocumentSetRequestType(message.getRequest());
        setInboundDelegate(message.getAdapterDelegate());
    }

}
