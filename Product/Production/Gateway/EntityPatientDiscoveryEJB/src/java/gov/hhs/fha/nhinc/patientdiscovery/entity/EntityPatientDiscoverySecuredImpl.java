/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.data.CMHomeCommunity;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.nhinpatientdiscovery.proxy.NhinPatientDiscoveryProxy;
import gov.hhs.fha.nhinc.nhinpatientdiscovery.proxy.NhinPatientDiscoveryProxyObjectFactory;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLog;
import gov.hhs.fha.nhinc.patientdiscovery.proxy.NhincProxyPatientDiscoverySecuredImpl;
import gov.hhs.fha.nhinc.patientdiscovery.response.ResponseFactory;
import gov.hhs.fha.nhinc.patientdiscovery.response.ResponseParams;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import gov.hhs.fha.nhinc.policyengine.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.transform.policy.PatientDiscoveryPolicyTransformHelper;
import java.util.ArrayList;
import java.util.List;
//import javax.xml.bind.JAXB;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.CommunityPRPAIN201306UV02ResponseType;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;

/**
 *
 * @author shawc
 */
public class EntityPatientDiscoverySecuredImpl {
    private Log log = null;

    public EntityPatientDiscoverySecuredImpl() {
        log = createLogger();
    }


    
    public org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType respondingGatewayPRPAIN201305UV02(org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType request, WebServiceContext context) {
        addLogDebug("Entering EntityPatientDiscoverySecuredImpl.respondingGatewayPRPAIN201305UV02...");
        RespondingGatewayPRPAIN201306UV02ResponseType response = new RespondingGatewayPRPAIN201306UV02ResponseType();

        if (request == null)
        {
            addLogError("The incomming request was null.");
            return null;
        }

        if (context == null)
        {
            addLogError("The incomming WebServiceContext parameter was null.");
            return null;
        }

        //log the incomming request from the adapter
        logEntityPatientDiscoveryRequest(request);


        if (request.getNhinTargetCommunities() == null)
        {
            addLogError("The incomming request did not have any NhinTargetCommunities (i.e. request.getNhinTargetCommunities() was null.");
            return null;
        }

        if (NullChecker.isNullish(request.getNhinTargetCommunities().getNhinTargetCommunity()))
        {
            addLogInfo("The list of incomming NhinTargetCommunities was null. sending out to community.");
            //populate list of target communities from connectionmanager cache
            List<NhinTargetCommunityType> oNhinTargetCommunities = getDefaultTargetCommunities();
            NhinTargetCommunitiesType oNhinTargetCommunitiesType =  new NhinTargetCommunitiesType();
            oNhinTargetCommunitiesType.getNhinTargetCommunity().addAll(oNhinTargetCommunities);
            request.setNhinTargetCommunities(oNhinTargetCommunitiesType);
        }

        // Create an assertion class from the contents of the SAML token
        AssertionType assertion = getAssertionTypeFromSAMLTokenInWSContext(context);

        response = getResponseFromCommunities(request, assertion, context);

        // Audit the Patient Discovery Response Message received on the Nhin Interface
        logAggregatedResponseFromNhin(response, assertion);

        addLogDebug("Exiting EntityPatientDiscoverySecuredImpl.respondingGatewayPRPAIN201305UV02...");
        return response;
    }

    protected RespondingGatewayPRPAIN201306UV02ResponseType getResponseFromCommunities(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion, WebServiceContext context) {
        addLogDebug("Entering EntityPatientDiscoverySecuredImpl.getResponseFromCommunities...");
        RespondingGatewayPRPAIN201306UV02ResponseType response = new RespondingGatewayPRPAIN201306UV02ResponseType();

        NhincProxyPatientDiscoverySecuredImpl proxy = new NhincProxyPatientDiscoverySecuredImpl();

        //loop through the communities and send request
        addLogDebug("Number of potential target communities to forward request to: " + request.getNhinTargetCommunities().getNhinTargetCommunity().size());
        for (NhinTargetCommunityType oTargetCommunity : request.getNhinTargetCommunities().getNhinTargetCommunity())
        {
            //don't send out a message to a target community to which we don't have connection info for
            if (!isTargetCommunityInConnectionManager(oTargetCommunity))
            {
                addLogInfo("The target community, " + oTargetCommunity.getHomeCommunity().getHomeCommunityId() +
                        ", cannot be found in the ConnectionManager. A patient discovery request message will not" +
                        " be sent to this community");
                continue;
            }
            //create a new request to send out to each target community
            RespondingGatewayPRPAIN201305UV02RequestType newRequest = createNewRequest(request, oTargetCommunity);

            //check the policy for the outgoing request to the target community
//            boolean bIsPolicyOk = isPolicyOk(newRequest); //RE-Enable me after fixing Policy Engine request message for no patient id
            boolean bIsPolicyOk = true;
            if (bIsPolicyOk)
            {
                CommunityPRPAIN201306UV02ResponseType communityResponse = new CommunityPRPAIN201306UV02ResponseType();

                NhinTargetSystemType oTargetSystemType = new NhinTargetSystemType();
                oTargetSystemType.setHomeCommunity(oTargetCommunity.getHomeCommunity());

                //format request for nhincProxyPatientDiscoveryImpl call
                ProxyPRPAIN201305UVProxySecuredRequestType oProxyPRPAIN201305UVProxySecuredRequestType =
                        new ProxyPRPAIN201305UVProxySecuredRequestType();
                oProxyPRPAIN201305UVProxySecuredRequestType.setPRPAIN201305UV02(newRequest.getPRPAIN201305UV02());
                oProxyPRPAIN201305UVProxySecuredRequestType.setNhinTargetSystem(oTargetSystemType);

                PRPAIN201306UV02 resultFromNhin = proxy.proxyPRPAIN201305UV(oProxyPRPAIN201305UVProxySecuredRequestType, assertion);

                //process the response
                ResponseParams params = new ResponseParams();
                params.context = context;
                params.origRequest = oProxyPRPAIN201305UVProxySecuredRequestType;
                params.response = resultFromNhin;

                try
                {
                    resultFromNhin = new ResponseFactory().getResponseMode().processResponse(params);
                }
                catch(Exception ex)
                {
                    addLogError(ex.getMessage(), ex);
                    resultFromNhin = new PRPAIN201306UV02();
                }

                communityResponse.setNhinTargetCommunity(oTargetCommunity);
                communityResponse.setPRPAIN201306UV02(resultFromNhin);

                addLogDebug("Adding Community Response to response object");
                response.getCommunityResponse().add(communityResponse);
            } //if (bIsPolicyOk)
             else
            {
                addLogError("The policy engine evaluated the request and denied the request.");
                
            } //else policy enging did not return a permit response
            
        } //for (NhinTargetCommunityType oTargetCommunity : request.getNhinTargetCommunities().getNhinTargetCommunity())

        addLogDebug("Exiting EntityPatientDiscoverySecuredImpl.getResponseFromCommunities...");
        return response;
    }

    protected boolean isPolicyOk(RespondingGatewayPRPAIN201305UV02RequestType newRequest) {
        boolean bPolicyOk = false;

        addLogDebug("checking the policy engine for the new request to a target community");

        PatientDiscoveryPolicyTransformHelper oPatientDiscoveryPolicyTransformHelper = new PatientDiscoveryPolicyTransformHelper();
        CheckPolicyRequestType oEntityCheckPolicyRequest = oPatientDiscoveryPolicyTransformHelper.transformPatientDiscoveryEntityToCheckPolicy(newRequest);

        /* invoke check policy */
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(oEntityCheckPolicyRequest);

        /* if response='permit' */
        if (policyResp.getResponse() != null &&
                NullChecker.isNotNullish(policyResp.getResponse().getResult()) &&
                policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT)
        {
            addLogDebug("Policy engine check returned permit.");
            bPolicyOk = true;
        }
        else
        {
            addLogDebug("Policy engine check returned deny.");
            bPolicyOk = false;
        }

        //return true if 'permit' returned, false otherwise
        return bPolicyOk;

    }

    protected List<NhinTargetCommunityType> getDefaultTargetCommunities(){
        List<NhinTargetCommunityType> targets = new ArrayList<NhinTargetCommunityType>();
        List<CMHomeCommunity> communities = new ArrayList<CMHomeCommunity>();
        String localHomeCommunity = null;

        // Get the local home community id for the gateway
        try {
            localHomeCommunity = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        } catch (PropertyAccessException ex) {
            addLogError(ex.getMessage());
            return null;
        }

        // Get all the Communities from ConnectionManager
       communities = getCommunitiesFromConnectionManager();
        
        addLogDebug("EntityPatientDiscoveryImpl.getDefaultTargetCommunities -- number communities : " + communities.size());

        //loop thru and pull out HCID 2.2 for now...
        for (CMHomeCommunity h : communities)
        {
            //don't query our own gateway
            if (!h.getHomeCommunityId().equals(localHomeCommunity))
            {
                addLogDebug(h.getHomeCommunityId() + " != " + localHomeCommunity);
                HomeCommunityType homeCommunity = new HomeCommunityType();
                homeCommunity.setDescription(h.getDescription());
                homeCommunity.setHomeCommunityId(h.getHomeCommunityId());
                homeCommunity.setName(h.getName());
                NhinTargetCommunityType oNhinTargetCommunityType = new NhinTargetCommunityType();
                oNhinTargetCommunityType.setHomeCommunity(homeCommunity);
                targets.add(oNhinTargetCommunityType);
                break;
            }
        }

        return targets;
}

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    private MCCIMT000100UV01Receiver createNewReceiver(HomeCommunityType homeCommunity) {
        MCCIMT000100UV01Receiver oNewReceiver = new MCCIMT000100UV01Receiver();

        MCCIMT000100UV01Device oNewDevice = new MCCIMT000100UV01Device();
        II oII = new II();
        if (homeCommunity == null)
        {
            addLogError("Unable to create a receiver object in order to forward " +
                    "the patient discovery request on to a target community. Target" +
                    " home community object was null");
            return null;
        }

        if (homeCommunity.getHomeCommunityId() == null)
        {
            addLogError("Unable to create a receiver object in order to forward " +
                    "the patient discovery request on to a target community. Target" +
                    " homeCommunity.getHomeCommunityId() == null");
            return null;
        }

        addLogDebug("Setting the new request's receiverId to: " + homeCommunity.getHomeCommunityId());
        oII.setRoot(homeCommunity.getHomeCommunityId());

        if (homeCommunity.getName() == null)
        {
            log.info("The target homeCommunity name was null. The policy engine will still be called and the request will still be forwarded, however.");
        }
        else
        {
            oII.setAssigningAuthorityName(homeCommunity.getName());
        }

        oNewDevice.getId().add(oII);
        oNewReceiver.setDevice(oNewDevice);

        return oNewReceiver;
    }

    private RespondingGatewayPRPAIN201305UV02RequestType createNewRequest(RespondingGatewayPRPAIN201305UV02RequestType request, NhinTargetCommunityType targetCommunity)
    {
        RespondingGatewayPRPAIN201305UV02RequestType newRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
        newRequest.setPRPAIN201305UV02(request.getPRPAIN201305UV02());
        newRequest.setAssertion(request.getAssertion());

        //restrict the list of target communities to the one we are forwarding the request to
        NhinTargetCommunitiesType targetCommunities = new NhinTargetCommunitiesType();
        targetCommunities.getNhinTargetCommunity().add(targetCommunity);
        newRequest.setNhinTargetCommunities(targetCommunities);

        //the new request will have the target community as the only receiver
        newRequest.getPRPAIN201305UV02().getReceiver().clear();
        MCCIMT000100UV01Receiver oNewReceiver = createNewReceiver(targetCommunity.getHomeCommunity());
        newRequest.getPRPAIN201305UV02().getReceiver().add(oNewReceiver);
        addLogDebug("Created a new request for target communityId: " + targetCommunity.getHomeCommunity().getHomeCommunityId());

        return newRequest;
    }

    private void addLogInfo(String message){
        log.info(message);
    }

    private void addLogDebug(String message){
        log.debug(message);
    }

    private void addLogError(String message){
        log.error(message);
    }

    private void addLogError(String message, Exception ex) {
        log.error(message, ex);
    }

    protected AssertionType getAssertionTypeFromSAMLTokenInWSContext(WebServiceContext context) {
        // Create an assertion class from the contents of the SAML token
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
        return assertion;
    }

    protected void logAggregatedResponseFromNhin(RespondingGatewayPRPAIN201306UV02ResponseType response, AssertionType assertion) {
        // Audit the Patient Discovery Response Message received on the Nhin Interface
        AcknowledgementType ack = new PatientDiscoveryAuditLog().auditEntityResponse(response, assertion);
    }

    protected void logEntityPatientDiscoveryRequest(RespondingGatewayPRPAIN201305UV02RequestType request) {
        // Audit the Patient Discovery Request Message sent on the Nhin Interface
        AcknowledgementType ack = new PatientDiscoveryAuditLog().auditEntityRequest(request);
    }

    protected boolean isTargetCommunityInConnectionManager(NhinTargetCommunityType targetCommunity) {
        boolean bHaveConnectionInfo = false;

        List<CMHomeCommunity> communities = getCommunitiesFromConnectionManager();
        for (CMHomeCommunity community : communities)
        {
            if (targetCommunity.getHomeCommunity().getHomeCommunityId().equalsIgnoreCase(community.getHomeCommunityId()))
            {
                bHaveConnectionInfo = true;
                addLogInfo("Found target community in connection manager.");
                break;
            }
        }

        if (!bHaveConnectionInfo)
        {
            addLogInfo("Could not find the target community in the connection manager.");
        }
        
        return bHaveConnectionInfo;

    }

    protected List<CMHomeCommunity> getCommunitiesFromConnectionManager() {
        List<CMHomeCommunity> communities = null;
        try
        {
            communities = ConnectionManagerCache.getAllCommunities();
        }
        catch (ConnectionManagerException ex)
        {
            addLogError(ex.getMessage());
            return null;
        }

        return communities;
    }

}
