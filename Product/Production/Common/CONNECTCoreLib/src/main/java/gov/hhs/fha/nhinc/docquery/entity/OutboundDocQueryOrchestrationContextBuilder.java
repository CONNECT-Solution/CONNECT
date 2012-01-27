package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.OutboundResponseProcessor;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContext;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;


/**
 * @author bhumphrey/paul
 *
 */
public abstract class OutboundDocQueryOrchestrationContextBuilder implements
		OrchestrationContextBuilder{

    private NhinTargetSystemType target = null;
    private AdhocQueryRequest request = null;
    private AssertionType assertionType = null;
    private PolicyTransformer policyTransformer = null;
    private AuditTransformer auditTransformer = null;
    private OutboundDelegate nhinDelegate = null;
    private OutboundResponseProcessor nhinProcessor = null;
    private String serviceName = "";


    @Override
    public OrchestrationContext build(){
        return new OrchestrationContext(getStrategy(), getOrchestratable());
    }

    abstract protected OutboundDocQueryOrchestratable getOrchestratable();

    abstract protected OutboundDocQueryStrategy getStrategy();


    public void setTarget(NhinTargetSystemType t){
        this.target = t;
    }

    protected NhinTargetSystemType getTargetSystemType(){
        return this.target;
    }

    public void setRequest(AdhocQueryRequest dqRequest){
        this.request = dqRequest;
    }

    protected AdhocQueryRequest getRequest(){
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
