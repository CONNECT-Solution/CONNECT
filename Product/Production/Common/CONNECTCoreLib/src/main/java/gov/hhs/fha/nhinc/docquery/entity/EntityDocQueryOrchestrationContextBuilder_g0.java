package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.orchestration.NhinResponseProcessor;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.NhinDelegate;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContext;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;


/**
 * Builds the DocQuery OrchestrationContext
 * @author paul.eftis
 */
public class EntityDocQueryOrchestrationContextBuilder_g0 implements OrchestrationContextBuilder{

        private NhinTargetSystemType target = null;
	private AdhocQueryRequest request = null;
	private AssertionType assertionType = null;
	private PolicyTransformer policyTransformer = null;
	private AuditTransformer auditTransformer = null;
	private NhinDelegate nhinDelegate = null;
	private NhinResponseProcessor nhinProcessor = null;
        private String serviceName = "";


        public void init(EntityDocQueryOrchestratable message) {
            setAssertionType(message.getAssertion());
            setRequest(message.getRequest());
            setTarget(message.getTarget());
            setServiceName(message.getServiceName());
            setPolicyTransformer(message.getPolicyTransformer());
            setAuditTransformer(message.getAuditTransformer());
            setProcessor(message.getResponseProcessor());
        }


        public NhinTargetSystemType getTarget(){
            return target;
        }

        public void setTarget(NhinTargetSystemType t){
            this.target = t;
        }
	
	public AdhocQueryRequest getRequest() {
            return request;
	}

	public void setRequest(AdhocQueryRequest docQueryRequest){
            this.request = docQueryRequest;
	}

	public AssertionType getAssertionType() {
            return assertionType;
	}

	public void setAssertionType(AssertionType assertionType){
            this.assertionType = assertionType;
	}

	public PolicyTransformer getPolicyTransformer() {
            return policyTransformer;
	}

	public void setPolicyTransformer(PolicyTransformer policyTransformer){
            this.policyTransformer = policyTransformer;
	}

	public AuditTransformer getAuditTransformer(){
            return auditTransformer;
	}

	public void setAuditTransformer(AuditTransformer auditTransformer){
            this.auditTransformer = auditTransformer;
	}

	public NhinDelegate getNhinDelegate(){
            return nhinDelegate;
	}

	public void setNhinDelegate(NhinDelegate nhinDelegate){
            this.nhinDelegate = nhinDelegate;
	}

	public NhinResponseProcessor getAggregator() {
            return nhinProcessor;
	}

	public void setAggregator(NhinResponseProcessor processor){
            nhinProcessor = processor;
	}

	public NhinResponseProcessor getProcessor() {
            return nhinProcessor;
	}

	public void setProcessor(NhinResponseProcessor processor){
            this.nhinProcessor = processor;
	}

        public String getServiceName(){
            return serviceName;
        }

        public void setServiceName(String name){
            this.serviceName = name;
        }

	@Override
	public OrchestrationContext build(){
            EntityDocQueryOrchestratable_a0 orch_a0 = new EntityDocQueryOrchestratable_a0(
                    getNhinDelegate(), getProcessor(), getAuditTransformer(),
                     getPolicyTransformer(), getAssertionType(), getServiceName(),
                     getTarget(), getRequest());
            return new OrchestrationContext(new NhinDocQueryStrategyImpl_g0(), orch_a0);
	}	

}
