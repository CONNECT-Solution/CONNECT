package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratableMessage;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import gov.hhs.fha.nhinc.orchestration.OutboundResponseProcessor;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;



/**
 * Doc Query implementation of OutboundOrchestratableMessage
 * @author paul.eftis
 */
public class OutboundDocQueryOrchestratable
        implements OutboundOrchestratableMessage{

    private OutboundDelegate delegate = null;
    private OutboundResponseProcessor processor = null;
    private AuditTransformer auditTransformer = null;
    private PolicyTransformer policyTransformer = null;
    private AssertionType assertion = null;
    private String serviceName = null;

    private NhinTargetSystemType target = null;
    private AdhocQueryRequest request = null;


    public OutboundDocQueryOrchestratable(){}


    public OutboundDocQueryOrchestratable(OutboundDelegate d, OutboundResponseProcessor p,
            AuditTransformer at, PolicyTransformer pt, AssertionType a, String name,
            NhinTargetSystemType t, AdhocQueryRequest r){

        this.delegate = d;
        this.processor = p;
        this.auditTransformer = at;
        this.policyTransformer = pt;
        this.assertion = a;
        this.serviceName = name;

        this.target = t;
        this.request = r;
    }

    public OutboundDelegate getDelegate(){
        return delegate;
    }

    // NOT USED.......use getResponseProcessor instead
    public NhinAggregator getAggregator(){
        return null;
    }

    public OutboundResponseProcessor getResponseProcessor(){
        return processor;
    }

    public AuditTransformer getAuditTransformer(){
        return auditTransformer;
    }
    
    public PolicyTransformer getPolicyTransformer(){
        return policyTransformer;
    }
    
    public AssertionType getAssertion(){
        return assertion;
    }
    
    public String getServiceName(){
        return serviceName;
    }

    public NhinTargetSystemType getTarget(){
        return target;
    }

    public AdhocQueryRequest getRequest(){
        return request;
    }

    public boolean isEnabled(){
        return true;
    }

    public boolean isPassthru(){
        return false;
    }

}
