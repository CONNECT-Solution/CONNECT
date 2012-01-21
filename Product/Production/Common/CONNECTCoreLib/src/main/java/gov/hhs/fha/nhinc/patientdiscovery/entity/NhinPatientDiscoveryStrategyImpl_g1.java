package gov.hhs.fha.nhinc.patientdiscovery.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.proxy.NhinPatientDiscoveryProxy;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.proxy.NhinPatientDiscoveryProxyObjectFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Implements the PatientDiscovery strategy for spec g1 endpoint
 * 
 * @author paul.eftis
 */
public class NhinPatientDiscoveryStrategyImpl_g1 extends NhinPatientDiscoveryStrategy{

    private static Log log = LogFactory.getLog(NhinPatientDiscoveryStrategyImpl_g1.class);

    public NhinPatientDiscoveryStrategyImpl_g1(){

    }

    private Log getLogger(){
        return log;
    }


    /**
     * @param message contains request message to execute
     */
    @Override
    public void execute(EntityPatientDiscoveryOrchestratable message){
        if(message instanceof EntityPatientDiscoveryOrchestratable_a1){
            executeStrategy((EntityPatientDiscoveryOrchestratable_a1) message);
        }else{
            // shouldn't get here
            getLogger().error("NhinPatientDiscoveryStrategyImpl_g1 EntityPatientDiscoveryOrchestratable was not an EntityPatientDiscoveryOrchestratable_a1!!!");
        }
    }


    public void executeStrategy(EntityPatientDiscoveryOrchestratable_a1 message){
        getLogger().debug("NhinPatientDiscoveryStrategyImpl_g1::executeStrategy");

        EntityPatientDiscoveryOrchestratable_a1 nhinPDResponse = new EntityPatientDiscoveryOrchestratable_a1(
                        null, message.getResponseProcessor(),
                        message.getAuditTransformer(), message.getPolicyTransformer(),
                        message.getAssertion(), message.getServiceName(),
                        message.getTarget(), message.getRequest());

        NhinTargetSystemType targetSystem = message.getTarget();
        String requestCommunityID = targetSystem.getHomeCommunity()
                        .getHomeCommunityId();

        auditRequestMessage(message.getRequest(), message.getAssertion(),
                        requestCommunityID);

        NhinPatientDiscoveryProxy proxy = new NhinPatientDiscoveryProxyObjectFactory()
                        .getNhinPatientDiscoveryProxy();
        getLogger().debug("NhinPatientDiscoveryStrategyImpl_g1::executeStrategy sending nhin " +
                "patient discovery request to target hcid=" + requestCommunityID);

        nhinPDResponse.setResponse(proxy.respondingGatewayPRPAIN201305UV02(
                        message.getRequest(), message.getAssertion(), targetSystem));

        auditResponseMessage(nhinPDResponse.getResponse(),
                        nhinPDResponse.getAssertion(), requestCommunityID);

        message.setResponse(nhinPDResponse.getResponse());

        getLogger().debug("NhinPatientDiscoveryStrategyImpl_g1::executeStrategy returning response");
    }

}
