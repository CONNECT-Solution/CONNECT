package gov.hhs.fha.nhinc.patientdiscovery.entity;

import org.hl7.v3.CommunityPRPAIN201306UV02ResponseType;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author bhumphrey/paul
 *
 */
public abstract class NhinPatientDiscoveryStrategy implements OrchestrationStrategy{
	
    private static Log log = LogFactory.getLog(NhinPatientDiscoveryStrategy.class);

    private Log getLogger(){
         return log;
    }
    

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy#execute(gov.hhs.fha.nhinc.orchestration.Orchestratable)
     */
    @Override
    public void execute(Orchestratable message){
         if(message == null){
            getLogger().error("NhinPatientDiscoveryStrategy Orchestratable was null!!!");
            // throw new Exception("NhinDocQueryStrategyImpl_g0 input message was null!!!");
        }
        if(message instanceof EntityPatientDiscoveryOrchestratable){
            execute((EntityPatientDiscoveryOrchestratable)message);
        }else{
            // shouldn't get here
            getLogger().error("NhinPatientDiscoveryStrategy input Orchestratable was not an EntityDocQueryOrchestratable!!!");
            // throw new Exception("EntityDocQueryOrchestratable input message was not an EntityDocQueryOrchestratable!!!");
        }
    }

    abstract public void execute(EntityPatientDiscoveryOrchestratable message);


    protected void auditRequestMessage(PRPAIN201305UV02 request,
                    AssertionType assertion, String hcid){

        RespondingGatewayPRPAIN201305UV02RequestType auditRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
        auditRequest.setAssertion(assertion);
        // set targetList with individual target for hcid
        NhinTargetCommunitiesType targetList = new NhinTargetCommunitiesType();
        NhinTargetCommunityType target = new NhinTargetCommunityType();
        HomeCommunityType home = new HomeCommunityType();
        home.setHomeCommunityId(hcid);
        target.setHomeCommunity(home);
        targetList.getNhinTargetCommunity().add(target);
        auditRequest.setNhinTargetCommunities(targetList);
        auditRequest.setPRPAIN201305UV02(request);
        new PatientDiscoveryAuditLogger().auditEntity201305(auditRequest,
                        assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
    }


    protected void auditResponseMessage(PRPAIN201306UV02 response,
                    AssertionType assertion, String hcid){

        RespondingGatewayPRPAIN201306UV02ResponseType auditResponse = new RespondingGatewayPRPAIN201306UV02ResponseType();
        CommunityPRPAIN201306UV02ResponseType communityResponse = new CommunityPRPAIN201306UV02ResponseType();
        communityResponse.setPRPAIN201306UV02(response);
        NhinTargetCommunityType target = new NhinTargetCommunityType();
        HomeCommunityType home = new HomeCommunityType();
        home.setHomeCommunityId(hcid);
        target.setHomeCommunity(home);
        communityResponse.setNhinTargetCommunity(target);
        auditResponse.getCommunityResponse().add(communityResponse);
        new PatientDiscoveryAuditLogger().auditEntity201306(auditResponse,
                        assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
    }

}
