package gov.hhs.fha.nhinc.patientdiscovery.entity;

import org.hl7.v3.PRPAIN201305UV02;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.OutboundResponseProcessor;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContext;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;


/**
 * @author bhumphrey/paul
 *
 */
public abstract class OutboundPatientDiscoveryOrchestrationContextBuilder implements
		OrchestrationContextBuilder{

    private NhinTargetSystemType target = null;
    private PRPAIN201305UV02 request = null;
    private AssertionType assertionType = null;
    private PolicyTransformer policyTransformer = null;
    private AuditTransformer auditTransformer = null;
    private OutboundDelegate nhinDelegate = null;
    private OutboundResponseProcessor nhinProcessor = null;
    private String serviceName = "";

    
    @Override
    public OrchestrationContext build() {
        return new OrchestrationContext(getStrategy(), getOrchestratable());
    }

    abstract protected OutboundPatientDiscoveryOrchestratable getOrchestratable();

    abstract protected OutboundPatientDiscoveryStrategy getStrategy();
    

    public void setTarget(NhinTargetSystemType t){
        this.target = t;
    }

    protected NhinTargetSystemType getTargetSystemType(){
        return this.target;
    }

    public void setRequest(PRPAIN201305UV02 pdRequest){
        this.request = pdRequest;
    }

    protected PRPAIN201305UV02 getRequest(){
        return this.request;
    }

    protected AssertionType getAssertionType(){
        return assertionType;
    }

    public void setAssertionType(AssertionType assertionType){
        this.assertionType = assertionType;
    }

    protected PolicyTransformer getPolicyTransformer(){
        return policyTransformer;
    }

    public void setPolicyTransformer(PolicyTransformer policyTransformer){
        this.policyTransformer = policyTransformer;
    }

    protected AuditTransformer getAuditTransformer(){
        return auditTransformer;
    }

    public void setAuditTransformer(AuditTransformer auditTransformer){
        this.auditTransformer = auditTransformer;
    }

    protected OutboundDelegate getNhinDelegate(){
        return nhinDelegate;
    }

    public void setNhinDelegate(OutboundDelegate nhinDelegate){
        this.nhinDelegate = nhinDelegate;
    }

    protected OutboundResponseProcessor getAggregator(){
        return nhinProcessor;
    }

    public void setAggregator(OutboundResponseProcessor processor){
        nhinProcessor = processor;
    }

    protected OutboundResponseProcessor getProcessor(){
        return nhinProcessor;
    }

    public void setProcessor(OutboundResponseProcessor processor){
        this.nhinProcessor = processor;
    }

    protected String getServiceName(){
        return serviceName;
    }

    public void setServiceName(String name){
        this.serviceName = name;
    }

}
