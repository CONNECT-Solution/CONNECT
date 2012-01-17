package gov.hhs.fha.nhinc.patientdiscovery.entity;

import gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy;
import gov.hhs.fha.nhinc.orchestration.EntityOrchestratable;

import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.proxy.NhinPatientDiscoveryProxy;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.proxy.NhinPatientDiscoveryProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;

import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
import org.hl7.v3.CommunityPRPAIN201306UV02ResponseType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Implements the PatientDiscovery strategy for spec a0
 *
 * @author paul.eftis
 */
public class NhinPatientDiscoveryStrategyImpl_g0 implements OrchestrationStrategy{

    private static Log log = LogFactory.getLog(NhinPatientDiscoveryStrategyImpl_g0.class);
    
    public NhinPatientDiscoveryStrategyImpl_g0(){
        
    }

    private Log getLogger()
    {
        return log;
    }


    @Override
    public void execute(EntityOrchestratable message){
        
        getLogger().debug("NhinPatientDiscoveryStrategyImpl_g0::execute");
        if(message == null){
            getLogger().debug("NhinPatientDiscoveryStrategyImpl_g0 EntityOrchestratable was null!!!");
            // throw new Exception("NhinDocQueryStrategyImpl_g0 input message was null!!!");
        }
        if(message instanceof EntityPatientDiscoveryOrchestratable){
            if(message instanceof EntityPatientDiscoveryOrchestratable_a0){
                EntityPatientDiscoveryOrchestratable_a0 m = (EntityPatientDiscoveryOrchestratable_a0)message;
                EntityPatientDiscoveryOrchestratable_a0 response = executeStrategy_g0(m);
                m.setResponse(response.getResponse());
            }else{
                // shouldn't get here
                getLogger().debug("NhinPatientDiscoveryStrategyImpl_g0 EntityOrchestratable was not an EntityDocQueryOrchestratable_a0!!!");
                // throw new Exception("NhinDocQueryStrategyImpl_g0 EntityOrchestratable was not an EntityDocQueryOrchestratable_a0!!!");
            }
        }else{
            // shouldn't get here
            getLogger().debug("NhinPatientDiscoveryStrategyImpl_g0 EntityOrchestratable was not an EntityDocQueryOrchestratable!!!");
            // throw new Exception("EntityDocQueryOrchestratable input message was not an EntityDocQueryOrchestratable!!!");
        }
        
        
        
//        getLogger().debug("Begin NhinPatientDiscoveryStrategyImpl_a0.execute");
//        if(message == null){
//            getLogger().debug("EntityPatientDiscoveryOrchestratable was null!!!");
//            throw new Exception("EntityPatientDiscoveryOrchestratable input message was null!!!");
//        }
//
//        EntityPatientDiscoveryOrchestratable_a0 nhinPDResponse = new EntityPatientDiscoveryOrchestratable_a0(
//                null, new EntityPatientDiscoveryProcessor(), null, null, message.getAssertion(),
//                "EntityPatientDiscovery", message.getTarget(), message.getRequest());
//
//        NhinTargetSystemType targetSystem = message.getTarget();
//        String requestCommunityID = targetSystem.getHomeCommunity().getHomeCommunityId();
//
//        auditRequestMessage(message.getRequest(), message.getAssertion(), requestCommunityID);
//
//        NhinPatientDiscoveryProxy proxy = new NhinPatientDiscoveryProxyObjectFactory().getNhinPatientDiscoveryProxy();
//        getLogger().debug("NhinPatientDiscoveryStrategyImpl_a0 sending nhin patient discovery request to "
//                + " target hcid=" + requestCommunityID + " and target url=" + targetSystem.getUrl());
//
//        nhinPDResponse.setResponse(proxy.respondingGatewayPRPAIN201305UV02(
//                message.getRequest(), message.getAssertion(), targetSystem));
//
//        auditResponseMessage(nhinPDResponse.getResponse(), nhinPDResponse.getAssertion(), requestCommunityID);
//
//        getLogger().debug("End NhinPatientDiscoveryStrategyImpl_a0.execute");
//        return nhinPDResponse;
    }
    
    
    public EntityPatientDiscoveryOrchestratable_a0 executeStrategy_g0(EntityPatientDiscoveryOrchestratable message){
        getLogger().debug("NhinDocQueryStrategyImpl_g0::executeStrategy_g0");

        EntityPatientDiscoveryOrchestratable_a0 nhinPDResponse = new EntityPatientDiscoveryOrchestratable_a0(
                null, new EntityPatientDiscoveryProcessor(), null, null, message.getAssertion(),
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
        return nhinPDResponse;
    }


    protected void auditRequestMessage(PRPAIN201305UV02 request, AssertionType assertion, String hcid){
        RespondingGatewayPRPAIN201305UV02RequestType auditRequest =
                new RespondingGatewayPRPAIN201305UV02RequestType();
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
        new PatientDiscoveryAuditLogger().auditEntity201305(auditRequest, assertion,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
    }


    protected void auditResponseMessage(PRPAIN201306UV02 response, AssertionType assertion, String hcid){
        RespondingGatewayPRPAIN201306UV02ResponseType auditResponse =
                new RespondingGatewayPRPAIN201306UV02ResponseType();
        CommunityPRPAIN201306UV02ResponseType communityResponse =
                new CommunityPRPAIN201306UV02ResponseType();
        communityResponse.setPRPAIN201306UV02(response);
        NhinTargetCommunityType target = new NhinTargetCommunityType();
        HomeCommunityType home = new HomeCommunityType();
        home.setHomeCommunityId(hcid);
        target.setHomeCommunity(home);
        communityResponse.setNhinTargetCommunity(target);
        auditResponse.getCommunityResponse().add(communityResponse);
        new PatientDiscoveryAuditLogger().auditEntity201306(auditResponse, assertion,
                NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
    }

}
