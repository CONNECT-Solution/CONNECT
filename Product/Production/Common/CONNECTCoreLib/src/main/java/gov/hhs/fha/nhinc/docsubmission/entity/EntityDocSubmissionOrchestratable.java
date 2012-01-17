/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docsubmission.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.EntityOrchestratable;
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import gov.hhs.fha.nhinc.orchestration.NhinDelegate;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;

/**
 *
 * @author zmelnick
 */
public class EntityDocSubmissionOrchestratable implements EntityOrchestratable {

    protected NhinTargetSystemType target = null;
    private AssertionType assertion = null;
    private NhinDelegate nhinDelegate = null;
    private ProvideAndRegisterDocumentSetRequestType request = null;

    public EntityDocSubmissionOrchestratable() {
    }

    public EntityDocSubmissionOrchestratable(AssertionType assertion, NhinDelegate delegate) {
        this.assertion = assertion;
        this.nhinDelegate = delegate;
    }

    @Override
    public NhinDelegate getDelegate() {
        return getNhinDelegate();
    }

    public NhinDelegate getNhinDelegate() {
        return nhinDelegate;
    }

    @Override
    public NhinAggregator getAggregator() {
        throw new UnsupportedOperationException("Document Submission does not support aggregation.");
    }

    public AssertionType getAssertion() {
        return assertion;
    }

    public void setAssertion(AssertionType assertion) {
        this.assertion = assertion;
    }

    public ProvideAndRegisterDocumentSetRequestType getRequest() {
        return request;
    }

    public void setRequest(ProvideAndRegisterDocumentSetRequestType request) {
        this.request = request;
    }

    public NhinTargetSystemType getTarget() {
        return target;
    }

    public void setTarget(NhinTargetSystemType target) {
        this.target = target;
    }

    public String getServiceName() {
        return NhincConstants.DOC_SUBMISSION_SERVICE_PROP;
    }

    public boolean isEnabled() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isPassthru() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public AuditTransformer getAuditTransformer() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public PolicyTransformer getPolicyTransformer() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
