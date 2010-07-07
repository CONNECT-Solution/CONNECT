/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientdiscovery.entity;

import gov.hhs.fha.nhinc.common.connectionmanager.dao.AssigningAuthorityHomeCommunityMappingDAO;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfo;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfos;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.proxy.NhincProxyPatientDiscoverySecuredImpl;
import gov.hhs.fha.nhinc.patientdiscovery.response.ResponseFactory;
import gov.hhs.fha.nhinc.patientdiscovery.response.ResponseParams;
import gov.hhs.fha.nhinc.policyengine.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.transform.policy.PatientDiscoveryPolicyTransformHelper;
import gov.hhs.fha.nhinc.transform.subdisc.HL7Constants;
import gov.hhs.fha.nhinc.transform.subdisc.HL7DataTransformHelper;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201306Transforms;
import javax.xml.bind.JAXBElement;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.CommunicationFunctionType;
import org.hl7.v3.CommunityPRPAIN201306UV02ResponseType;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000100UV01Agent;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Organization;
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

        if (request == null) {
            addLogError("The incomming request was null.");
            return null;
        }

        if (context == null) {
            addLogError("The incomming WebServiceContext parameter was null.");
            return null;
        }

        //log the incomming request from the adapter
        logEntityPatientDiscoveryRequest(request);

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
        CMUrlInfos urlInfoList = null;
        PRPAIN201306UV02 resultFromNhin = null;

        NhincProxyPatientDiscoverySecuredImpl proxy = new NhincProxyPatientDiscoverySecuredImpl();

        // Obtain all the URLs for the targets being sent to
        try {
            urlInfoList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(request.getNhinTargetCommunities(), NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Failed to obtain target URLs");
            return null;
        }

        //loop through the communities and send request if results were not null
        if (urlInfoList != null &&
                urlInfoList.getUrlInfo() != null) {
            for (CMUrlInfo urlInfo : urlInfoList.getUrlInfo()) {

                //create a new request to send out to each target community
                RespondingGatewayPRPAIN201305UV02RequestType newRequest = createNewRequest(request, urlInfo.getHcid());

                //check the policy for the outgoing request to the target community
                boolean bIsPolicyOk = isPolicyOk(newRequest);

                if (bIsPolicyOk) {
                    CommunityPRPAIN201306UV02ResponseType communityResponse = new CommunityPRPAIN201306UV02ResponseType();

                    NhinTargetSystemType oTargetSystemType = new NhinTargetSystemType();
                    oTargetSystemType.setUrl(urlInfo.getUrl());

                    //format request for nhincProxyPatientDiscoveryImpl call
                    ProxyPRPAIN201305UVProxySecuredRequestType oProxyPRPAIN201305UVProxySecuredRequestType =
                            new ProxyPRPAIN201305UVProxySecuredRequestType();
                    oProxyPRPAIN201305UVProxySecuredRequestType.setPRPAIN201305UV02(newRequest.getPRPAIN201305UV02());
                    oProxyPRPAIN201305UVProxySecuredRequestType.setNhinTargetSystem(oTargetSystemType);
                    ResponseParams params = new ResponseParams();

                    try {
                        resultFromNhin = proxy.proxyPRPAIN201305UV(oProxyPRPAIN201305UVProxySecuredRequestType, assertion);

                        params.context = context;
                        params.origRequest = oProxyPRPAIN201305UVProxySecuredRequestType;
                        params.response = resultFromNhin;
                        resultFromNhin = new ResponseFactory().getResponseMode().processResponse(params);

                    } catch (Exception ex) {
                        addLogError(ex.getMessage(), ex);
                        resultFromNhin = new HL7PRPA201306Transforms().createPRPA201306ForErrors(oProxyPRPAIN201305UVProxySecuredRequestType.getPRPAIN201305UV02(), NhincConstants.PATIENT_DISCOVERY_ANSWER_NOT_AVAIL_ERR_CODE);
                    }


                    try {
                    } catch (Exception ex) {
                        addLogError(ex.getMessage(), ex);
                        resultFromNhin = new PRPAIN201306UV02();
                    }

                    // Store AA to HCID Mapping from response
                    storeMapping(resultFromNhin);

                    communityResponse.setPRPAIN201306UV02(resultFromNhin);

                    addLogDebug("Adding Community Response to response object");
                    response.getCommunityResponse().add(communityResponse);
                } //if (bIsPolicyOk)
                else {
                    addLogError("The policy engine evaluated the request and denied the request.");

                } //else policy enging did not return a permit response

            } //for (NhinTargetCommunityType oTargetCommunity : request.getNhinTargetCommunities().getNhinTargetCommunity())
        } else {
            log.warn("No targets were found for the Patient Discovery Request");
        }

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
                policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT) {
            addLogDebug("Policy engine check returned permit.");
            bPolicyOk = true;
        } else {
            addLogDebug("Policy engine check returned deny.");
            bPolicyOk = false;
        }

        //return true if 'permit' returned, false otherwise
        return bPolicyOk;

    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    private MCCIMT000100UV01Receiver createNewReceiver(String homeCommunityId) {
        MCCIMT000100UV01Receiver oNewReceiver = new MCCIMT000100UV01Receiver();

        oNewReceiver.setTypeCode(CommunicationFunctionType.RCV);

        MCCIMT000100UV01Device oNewDevice = new MCCIMT000100UV01Device();
        II oII = new II();
        if (NullChecker.isNullish(homeCommunityId)) {
            addLogError("Unable to create a receiver object in order to forward " +
                    "the patient discovery request on to a target community. Target" +
                    " home community object was null");
            return null;
        }

        addLogDebug("Setting the new request's receiverId to: " + homeCommunityId);
        oNewDevice.setDeterminerCode(HL7Constants.RECEIVER_DETERMINER_CODE);

        log.debug("Setting receiver application to 1.2.345.678.999");
        oNewDevice.getId().add(HL7DataTransformHelper.IIFactory("1.2.345.678.999"));

        MCCIMT000100UV01Agent agent = new MCCIMT000100UV01Agent();
        MCCIMT000100UV01Organization org = new MCCIMT000100UV01Organization();
        org.setClassCode(HL7Constants.ORG_CLASS_CODE);
        org.setDeterminerCode(HL7Constants.RECEIVER_DETERMINER_CODE);
        II id = HL7DataTransformHelper.IIFactory(homeCommunityId);
        org.getId().add(id);

        javax.xml.namespace.QName xmlqnameorg = new javax.xml.namespace.QName("urn:hl7-org:v3", "representedOrganization");
        JAXBElement<MCCIMT000100UV01Organization> orgElem = new JAXBElement<MCCIMT000100UV01Organization>(xmlqnameorg, MCCIMT000100UV01Organization.class, org);
        agent.setRepresentedOrganization(orgElem);
        agent.getClassCode().add(HL7Constants.AGENT_CLASS_CODE);

        javax.xml.namespace.QName xmlqnameagent = new javax.xml.namespace.QName("urn:hl7-org:v3", "asAgent");
        JAXBElement<MCCIMT000100UV01Agent> agentElem = new JAXBElement<MCCIMT000100UV01Agent>(xmlqnameagent, MCCIMT000100UV01Agent.class, agent);

        oNewDevice.setAsAgent(agentElem);

        oNewReceiver.setDevice(oNewDevice);

        return oNewReceiver;
    }

    private RespondingGatewayPRPAIN201305UV02RequestType createNewRequest(RespondingGatewayPRPAIN201305UV02RequestType request, String targetCommunityId) {
        RespondingGatewayPRPAIN201305UV02RequestType newRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
        newRequest.setPRPAIN201305UV02(request.getPRPAIN201305UV02());
        newRequest.setAssertion(request.getAssertion());

        //the new request will have the target community as the only receiver
        newRequest.getPRPAIN201305UV02().getReceiver().clear();
        MCCIMT000100UV01Receiver oNewReceiver = createNewReceiver(targetCommunityId);
        newRequest.getPRPAIN201305UV02().getReceiver().add(oNewReceiver);
        addLogDebug("Created a new request for target communityId: " + targetCommunityId);

        return newRequest;
    }

    private void addLogInfo(String message) {
        log.info(message);
    }

    private void addLogDebug(String message) {
        log.debug(message);
    }

    private void addLogError(String message) {
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
        AcknowledgementType ack = new PatientDiscoveryAuditLogger().auditEntity201306(response, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
    }

    protected void logEntityPatientDiscoveryRequest(RespondingGatewayPRPAIN201305UV02RequestType request) {
        // Audit the Patient Discovery Request Message sent on the Nhin Interface
        AcknowledgementType ack = new PatientDiscoveryAuditLogger().auditEntity201305(request, request.getAssertion(), NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
    }

    protected void storeMapping(PRPAIN201306UV02 request) {
        AssigningAuthorityHomeCommunityMappingDAO mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();
        String hcid = null;
        String assigningAuthority = null;


        if (request != null) {
            if (request.getSender() != null &&
                    request.getSender().getDevice() != null &&
                    request.getSender().getDevice().getAsAgent() != null &&
                    request.getSender().getDevice().getAsAgent().getValue() != null &&
                    request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
                    request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
                    NullChecker.isNotNullish(request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
                    request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null &&
                    NullChecker.isNotNullish(request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
                hcid = request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
            }

            if (request.getControlActProcess() != null &&
                    NullChecker.isNotNullish(request.getControlActProcess().getAuthorOrPerformer()) &&
                    request.getControlActProcess().getAuthorOrPerformer().get(0) != null &&
                    request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice() != null &&
                    request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue() != null &&
                    NullChecker.isNotNullish(request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId()) &&
                    request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId().get(0) != null &&
                    NullChecker.isNotNullish(request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId().get(0).getRoot())) {
                assigningAuthority = request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId().get(0).getRoot();
            }

        }

        if (NullChecker.isNotNullish(hcid) &&
                NullChecker.isNotNullish(assigningAuthority)) {
            boolean result = mappingDao.storeMapping(hcid, assigningAuthority);

            if (result == false) {
                log.warn("Failed to store home community - assigning authority mapping");
            }

        }
    }
}
