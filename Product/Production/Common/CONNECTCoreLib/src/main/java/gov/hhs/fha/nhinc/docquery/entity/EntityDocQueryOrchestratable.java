package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.orchestration.EntityOrchestratableMessage;
import gov.hhs.fha.nhinc.orchestration.NhinDelegate;
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import gov.hhs.fha.nhinc.orchestration.NhinResponseProcessor;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;



/**
 *
 * @author paul.eftis
 */
public class EntityDocQueryOrchestratable
        implements EntityOrchestratableMessage{

    private NhinDelegate delegate = null;
    private NhinResponseProcessor processor = null;
    private AuditTransformer auditTransformer = null;
    private PolicyTransformer policyTransformer = null;
    private AssertionType assertion = null;
    private String serviceName = null;

    private NhinTargetSystemType target = null;
    private AdhocQueryRequest request = null;


    public EntityDocQueryOrchestratable(){}


    public EntityDocQueryOrchestratable(NhinDelegate d, NhinResponseProcessor p,
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

    public NhinDelegate getDelegate(){
        return delegate;
    }

    // NOT USED.......use getResponseProcessor instead
    public NhinAggregator getAggregator(){
        return null;
    }

    public NhinResponseProcessor getResponseProcessor(){
        return processor;
    }

    public NhincConstants.GATEWAY_API_LEVEL getGatewayApiLevel(){
        return ConnectionManagerCache.getApiVersionForNhinTarget(
                target.getHomeCommunity().getHomeCommunityId(), serviceName);
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
