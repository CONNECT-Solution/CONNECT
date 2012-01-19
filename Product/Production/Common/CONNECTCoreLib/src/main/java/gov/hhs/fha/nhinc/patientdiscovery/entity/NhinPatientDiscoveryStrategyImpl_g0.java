package gov.hhs.fha.nhinc.patientdiscovery.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.proxy.NhinPatientDiscoveryProxy;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.proxy.NhinPatientDiscoveryProxyObjectFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Implements the PatientDiscovery strategy for spec g0 endpoint
 *
 * @author paul.eftis
 */
public class NhinPatientDiscoveryStrategyImpl_g0 extends NhinPatientDiscoveryStrategy {

    private static Log log = LogFactory.getLog(NhinPatientDiscoveryStrategyImpl_g0.class);
    
    public NhinPatientDiscoveryStrategyImpl_g0(){
        
    }

    private Log getLogger()
    {
        return log;
    }

	/**
	 * @param message
	 */
	public void execute(EntityPatientDiscoveryOrchestratable message) {
		if(message instanceof EntityPatientDiscoveryOrchestratable_a0){
		  
			EntityPatientDiscoveryOrchestratable_a0 response = execute((EntityPatientDiscoveryOrchestratable_a0)message);
		}else{
		    // shouldn't get here
		    getLogger().debug("NhinPatientDiscoveryStrategyImpl_g0 EntityOrchestratable was not an EntityDocQueryOrchestratable_a0!!!");
		    // throw new Exception("NhinDocQueryStrategyImpl_g0 EntityOrchestratable was not an EntityDocQueryOrchestratable_a0!!!");
		}
	}
    
    
    public EntityPatientDiscoveryOrchestratable_a0 execute(EntityPatientDiscoveryOrchestratable_a0 message){
        getLogger().debug("NhinPatientDiscoveryStrategyImpl_g0::executeStrategy_g0");

        EntityPatientDiscoveryOrchestratable_a0 nhinPDResponse = new EntityPatientDiscoveryOrchestratable_a0(
                null, message.getResponseProcessor(), message.getAuditTransformer(),
                message.getPolicyTransformer(), message.getAssertion(),
                message.getServiceName(), message.getTarget(), message.getRequest());

        NhinTargetSystemType targetSystem = message.getTarget();
        String requestCommunityID = targetSystem.getHomeCommunity().getHomeCommunityId();

        auditRequestMessage(message.getRequest(), message.getAssertion(), requestCommunityID);

        NhinPatientDiscoveryProxy proxy = new NhinPatientDiscoveryProxyObjectFactory().getNhinPatientDiscoveryProxy();
        getLogger().debug("NhinPatientDiscoveryStrategyImpl_g0::executeStrategy_g0 sending nhin patient discovery request to "
                + " target hcid=" + requestCommunityID);

        nhinPDResponse.setResponse(proxy.respondingGatewayPRPAIN201305UV02(
                message.getRequest(), message.getAssertion(), targetSystem));

        auditResponseMessage(nhinPDResponse.getResponse(), nhinPDResponse.getAssertion(), requestCommunityID);

        getLogger().debug("NhinPatientDiscoveryStrategyImpl_g0::executeStrategy_g0 returning response");
        
        message.setResponse(nhinPDResponse.getResponse());
        return nhinPDResponse;
    }


}
