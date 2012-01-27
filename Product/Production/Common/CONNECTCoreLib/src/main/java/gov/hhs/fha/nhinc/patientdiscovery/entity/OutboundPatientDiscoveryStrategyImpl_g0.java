package gov.hhs.fha.nhinc.patientdiscovery.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.proxy.NhinPatientDiscoveryProxy;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.proxy.NhinPatientDiscoveryProxyObjectFactory;
import gov.hhs.fha.nhinc.orchestration.OutboundResponseProcessor;
import gov.hhs.fha.nhinc.gateway.executorservice.ExecutorServiceHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Implements the PatientDiscovery strategy for spec g0 endpoint
 *
 * @author paul.eftis
 */
public class OutboundPatientDiscoveryStrategyImpl_g0 extends OutboundPatientDiscoveryStrategy{

    private static Log log = LogFactory.getLog(OutboundPatientDiscoveryStrategyImpl_g0.class);
    
    public OutboundPatientDiscoveryStrategyImpl_g0(){
        
    }

    private Log getLogger(){
        return log;
    }


    /**
     * @param message contains request message to execute
     */
    @Override
    public void execute(OutboundPatientDiscoveryOrchestratable message){
        if(message instanceof OutboundPatientDiscoveryOrchestratable_a0){
            executeStrategy((OutboundPatientDiscoveryOrchestratable_a0)message);
        }else{
            // shouldn't get here
            getLogger().error("NhinPatientDiscoveryStrategyImpl_g0 EntityPatientDiscoveryOrchestratable was not an EntityPatientDiscoveryOrchestratable_a0!!!");
            // throw new Exception("OutboundPatientDiscoveryStrategyImpl_g0 OutboundPatientDiscoveryOrchestratable was not an OutboundPatientDiscoveryOrchestratable_a0!!!");
        }
    }
    
    
    @SuppressWarnings("static-access")
    public void executeStrategy(OutboundPatientDiscoveryOrchestratable_a0 message){
        getLogger().debug("NhinPatientDiscoveryStrategyImpl_g0::executeStrategy");
        auditRequestMessage(message.getRequest(), message.getAssertion(),
                message.getTarget().getHomeCommunity().getHomeCommunityId());
        try{
            NhinPatientDiscoveryProxy proxy = new NhinPatientDiscoveryProxyObjectFactory().getNhinPatientDiscoveryProxy();
            getLogger().debug("NhinPatientDiscoveryStrategyImpl_g0::executeStrategy sending nhin patient discovery request to "
                    + " target hcid=" + message.getTarget().getHomeCommunity().getHomeCommunityId());

            message.setResponse(proxy.respondingGatewayPRPAIN201305UV02(
                    message.getRequest(), message.getAssertion(), message.getTarget()));
            getLogger().debug("NhinPatientDiscoveryStrategyImpl_g0::executeStrategy returning response");
        }catch(Exception ex){
            String err = ExecutorServiceHelper.getFormattedExceptionInfo(ex, message.getTarget(),
                    message.getServiceName());
            OutboundResponseProcessor processor = message.getResponseProcessor();
            message.setResponse(((OutboundPatientDiscoveryOrchestratable_a0)processor.
                    processErrorResponse(message, err)).getResponse());
            getLogger().debug("NhinPatientDiscoveryStrategyImpl_g0::executeStrategy returning error response");
        }
        auditResponseMessage(message.getResponse(), message.getAssertion(),
                message.getTarget().getHomeCommunity().getHomeCommunityId());

    }

}
