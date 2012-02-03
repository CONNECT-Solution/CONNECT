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
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 *
 * @author zmelnick
 */
public class OutboundDocSubmissionOrchestratable implements OutboundOrchestratable {

    protected NhinTargetSystemType target = null;
    private AssertionType assertion = null;
    private OutboundDelegate nhinDelegate = null;
    private ProvideAndRegisterDocumentSetRequestType request = null;
    private RegistryResponseType response = null;

    public OutboundDocSubmissionOrchestratable(OutboundDelegate delegate) {
        this.nhinDelegate = delegate;
    }

    public OutboundDocSubmissionOrchestratable(OutboundDelegate delegate, ProvideAndRegisterDocumentSetRequestType request, NhinTargetSystemType target, AssertionType assertion) {
        this(delegate);
        this.assertion = assertion;
        this.request = request;
        this.target = target;
    }

    @Override
    public OutboundDelegate getDelegate() {
        return getNhinDelegate();
    }

    public OutboundDelegate getNhinDelegate() {
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

    public RegistryResponseType getResponse() {
        return response;
    }

    public void setResponse(RegistryResponseType response) {
        this.response = response;
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
