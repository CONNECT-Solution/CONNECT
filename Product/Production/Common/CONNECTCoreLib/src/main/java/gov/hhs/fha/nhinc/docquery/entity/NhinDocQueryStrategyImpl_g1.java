package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docquery.nhin.proxy.NhinDocQueryProxy;
import gov.hhs.fha.nhinc.docquery.nhin.proxy.NhinDocQueryProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Implements the DocQuery client strategy for spec g1 endpoint
 *
 * @author paul.eftis
 */
public class NhinDocQueryStrategyImpl_g1 extends NhinDocQueryStrategy{

    private static Log log = LogFactory.getLog(NhinDocQueryStrategyImpl_g1.class);
    
    public NhinDocQueryStrategyImpl_g1(){
        
    }

    private Log getLogger(){
        return log;
    }


    /**
     * @param message contains request message to execute
     */
    @Override
    public void execute(EntityDocQueryOrchestratable message){
        getLogger().debug("NhinDocQueryStrategyImpl_g1::execute");
        if(message instanceof EntityDocQueryOrchestratable_a1){
            executeStrategy((EntityDocQueryOrchestratable_a1)message);
        }else{
            // shouldn't get here
            getLogger().debug("NhinDocQueryStrategyImpl_g1 EntityDocQueryOrchestratable was not an EntityDocQueryOrchestratable_a1!!!");
            // throw new Exception("NhinDocQueryStrategyImpl_g1 EntityDocQueryOrchestratable was not an EntityDocQueryOrchestratable_a1!!!");
        }
    }


    public void executeStrategy(EntityDocQueryOrchestratable_a1 message){
        getLogger().debug("NhinDocQueryStrategyImpl_g1::executeStrategy");

        EntityDocQueryOrchestratable_a1 nhinDQResponse = new EntityDocQueryOrchestratable_a1(
                null, message.getResponseProcessor(), message.getAuditTransformer(),
                message.getPolicyTransformer(), message.getAssertion(),
                message.getServiceName(), message.getTarget(), message.getRequest());

        NhinTargetSystemType targetSystem = message.getTarget();
        String requestCommunityID = targetSystem.getHomeCommunity().getHomeCommunityId();

        auditRequestMessage(message.getRequest(), NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
                message.getAssertion(), requestCommunityID);

        NhinDocQueryProxy proxy = new NhinDocQueryProxyObjectFactory().getNhinDocQueryProxy();
        getLogger().debug("NhinDocQueryStrategyImpl_g1::executeStrategy sending nhin doc query request to "
                + " target hcid=" + requestCommunityID);

        nhinDQResponse.setResponse(proxy.respondingGatewayCrossGatewayQuery(
                message.getRequest(), message.getAssertion(), targetSystem));

        auditResponseMessage(nhinDQResponse.getResponse(), NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
                nhinDQResponse.getAssertion(), requestCommunityID);

         message.setResponse(nhinDQResponse.getResponse());

        getLogger().debug("NhinDocQueryStrategyImpl_g1::executeStrategy returning response");
    }

}
