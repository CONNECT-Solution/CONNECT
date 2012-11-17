package gov.hhs.fha.nhinc.orchestration;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

public abstract class AbstractOrchestrable implements Orchestratable {

    private AssertionType assertion;
    private PolicyTransformer policyTransformer;
    private AuditTransformer auditTransformer;

    /**
     * default constructor.
     */
    protected AbstractOrchestrable() {
        assertion = null;
        policyTransformer = null;
        auditTransformer = null;
 
    }

    protected AbstractOrchestrable(PolicyTransformer pt, AuditTransformer at) {
        policyTransformer = pt;
        auditTransformer = at;
    }

    public AuditTransformer getAuditTransformer() {
        return auditTransformer;
    }

    public PolicyTransformer getPolicyTransformer() {
        return policyTransformer;
    }

    public AssertionType getAssertion() {
        return assertion;
    }

    /**
     * @param assertion the assertion to set
     */
    public void setAssertion(AssertionType assertion) {
        this.assertion = assertion;
    }

    /**
     * @param policyTransformer the policyTransformer to set
     */
    public void setPolicyTransformer(PolicyTransformer policyTransformer) {
        this.policyTransformer = policyTransformer;
    }

    /**
     * @param auditTransformer the auditTransformer to set
     */
    public void setAuditTransformer(AuditTransformer auditTransformer) {
        this.auditTransformer = auditTransformer;
    }
    
    
    
}
