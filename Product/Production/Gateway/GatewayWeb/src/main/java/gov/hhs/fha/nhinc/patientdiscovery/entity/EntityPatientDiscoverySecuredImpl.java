/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientdiscovery.entity;

import gov.hhs.fha.nhinc.common.connectionmanager.dao.AssigningAuthorityHomeCommunityMappingDAO;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfo;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfos;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201305Processor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryPolicyChecker;
import gov.hhs.fha.nhinc.patientdiscovery.proxy.NhincProxyPatientDiscoverySecuredImpl;
import gov.hhs.fha.nhinc.patientdiscovery.response.ResponseFactory;
import gov.hhs.fha.nhinc.patientdiscovery.response.ResponseParams;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.CommunityPRPAIN201306UV02ResponseType;
import org.hl7.v3.PRPAIN201305UV02;
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

        NhincProxyPatientDiscoverySecuredImpl proxy = new NhincProxyPatientDiscoverySecuredImpl();

        // Obtain all the URLs for the targets being sent to
        try {
            urlInfoList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(request.getNhinTargetCommunities(), NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Failed to obtain target URLs");
            return null;
        }
        PatientDiscovery201305Processor pd201305Processor = new PatientDiscovery201305Processor();

        //loop through the communities and send request if results were not null
        if (urlInfoList != null &&
                urlInfoList.getUrlInfo() != null) {
            for (CMUrlInfo urlInfo : urlInfoList.getUrlInfo()) {

                //create a new request to send out to each target community
                RespondingGatewayPRPAIN201305UV02RequestType newRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
                PRPAIN201305UV02 new201305 = pd201305Processor.createNewRequest(request.getPRPAIN201305UV02(), urlInfo.getHcid());

                newRequest.setAssertion(request.getAssertion());
                newRequest.setPRPAIN201305UV02(new201305);
                newRequest.setNhinTargetCommunities(request.getNhinTargetCommunities());

                //check the policy for the outgoing request to the target community
                boolean bIsPolicyOk = checkPolicy(newRequest);

                if (bIsPolicyOk) {
                    CommunityPRPAIN201306UV02ResponseType communityResponse = new CommunityPRPAIN201306UV02ResponseType();

                    NhinTargetSystemType oTargetSystemType = new NhinTargetSystemType();
                    oTargetSystemType.setUrl(urlInfo.getUrl());

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

                    try {
                        resultFromNhin = new ResponseFactory().getResponseMode().processResponse(params);
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

    protected boolean checkPolicy(RespondingGatewayPRPAIN201305UV02RequestType request) {
        return new PatientDiscoveryPolicyChecker().checkOutgoingPolicy(request);
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
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
