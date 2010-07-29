package gov.hhs.fha.nhinc.patientdiscovery.entity.async.response;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfo;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfos;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinpatientdiscovery.async.response.proxy.NhinPatientDiscoveryAsyncRespProxy;
import gov.hhs.fha.nhinc.nhinpatientdiscovery.async.response.proxy.NhinPatientDiscoveryAsyncRespProxyObjectFactory;
import gov.hhs.fha.nhinc.patientdiscovery.NhinPatientDiscoveryUtils;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201306Processor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryPolicyChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02SecuredRequestType;

/**
 *
 * @author JHOPPESC
 */
public class EntityPatientDiscoverySecuredAsyncRespImpl {
    private static Log log = LogFactory.getLog(EntityPatientDiscoverySecuredAsyncRespImpl.class);

    public MCCIIN000002UV01 processPatientDiscoveryAsyncResp(RespondingGatewayPRPAIN201306UV02SecuredRequestType request, WebServiceContext context) {
        RespondingGatewayPRPAIN201306UV02RequestType unsecureRequest = new RespondingGatewayPRPAIN201306UV02RequestType();
        unsecureRequest.setNhinTargetCommunities(request.getNhinTargetCommunities());
        unsecureRequest.setPRPAIN201306UV02(request.getPRPAIN201306UV02());
        unsecureRequest.setAssertion(SamlTokenExtractor.GetAssertion(context));

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (request != null &&
                unsecureRequest.getAssertion() != null) {
            unsecureRequest.getAssertion().setAsyncMessageId(AsyncMessageIdExtractor.GetAsyncRelatesTo(context));
        }

        // Audit the Patient Discovery Request Message sent on the Entity Interface
        PatientDiscoveryAuditLogger auditLog = new PatientDiscoveryAuditLogger();
        AcknowledgementType ackMsg = auditLog.auditEntity201306(unsecureRequest, unsecureRequest.getAssertion(), NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);


        MCCIIN000002UV01 ack = processPatientDiscoveryAsyncResp(unsecureRequest);

        ackMsg = auditLog.auditAck(ack, unsecureRequest.getAssertion(), NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);

        return ack;
    }

    public MCCIIN000002UV01 processPatientDiscoveryAsyncResp(RespondingGatewayPRPAIN201306UV02RequestType request) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();
        CMUrlInfos urlInfoList = null;
        PatientDiscovery201306Processor pd201306Processor = new PatientDiscovery201306Processor();

        if (request != null &&
                request.getPRPAIN201306UV02() != null &&
                request.getAssertion() != null) {
            urlInfoList = getTargets(request.getNhinTargetCommunities());

            //loop through the communities and send request if results were not null
            if (urlInfoList != null &&
                    urlInfoList.getUrlInfo() != null) {
                for (CMUrlInfo urlInfo : urlInfoList.getUrlInfo()) {

                    //create a new request to send out to each target community
                    RespondingGatewayPRPAIN201306UV02RequestType newRequest = new RespondingGatewayPRPAIN201306UV02RequestType();
                    PRPAIN201306UV02 new201306 = pd201306Processor.createNewRequest(request.getPRPAIN201306UV02(), urlInfo.getHcid());

                    newRequest.setAssertion(request.getAssertion());
                    newRequest.setPRPAIN201306UV02(new201306);
                    newRequest.setNhinTargetCommunities(request.getNhinTargetCommunities());

                    //check the policy for the outgoing request to the target community
                    boolean bIsPolicyOk = checkPolicy(newRequest);

                    if (bIsPolicyOk) {
                        ack = sendToProxy(newRequest, urlInfo);
                    } else {
                        ack = HL7AckTransforms.createAckFrom201306(request.getPRPAIN201306UV02(), "Policy Failed");
                    }
                }
            } else {
                log.warn("No targets were found for the Patient Discovery Response");
                ack = HL7AckTransforms.createAckFrom201306(request.getPRPAIN201306UV02(), "No Targets Found");
            }
        }

        return ack;
    }

    protected CMUrlInfos getTargets(NhinTargetCommunitiesType targetCommunities) {
        CMUrlInfos urlInfoList = null;

        // Obtain all the URLs for the targets being sent to
        try {
            urlInfoList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(targetCommunities, NhincConstants.PATIENT_DISCOVERY_ASYNC_RESP_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Failed to obtain target URLs for service " + NhincConstants.PATIENT_DISCOVERY_ASYNC_RESP_SERVICE_NAME);
            return null;
        }

        return urlInfoList;
    }

    protected boolean checkPolicy(RespondingGatewayPRPAIN201306UV02RequestType request) {
        II patientId = NhinPatientDiscoveryUtils.extractPatientIdFrom201306(request.getPRPAIN201306UV02());
        return new PatientDiscoveryPolicyChecker().check201305Policy(request.getPRPAIN201306UV02(), patientId, request.getAssertion());
    }

    protected MCCIIN000002UV01 sendToProxy(RespondingGatewayPRPAIN201306UV02RequestType request, CMUrlInfo urlInfo) {
        MCCIIN000002UV01 resp = new MCCIIN000002UV01();

        NhinTargetSystemType oTargetSystemType = new NhinTargetSystemType();
        oTargetSystemType.setUrl(urlInfo.getUrl());

        // Audit the Patient Discovery Request Message sent on the Nhin Interface
        PatientDiscoveryAuditLogger auditLog = new PatientDiscoveryAuditLogger();
        auditLog.auditNhin201306(request.getPRPAIN201306UV02(), request.getAssertion(), NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);

        NhinPatientDiscoveryAsyncRespProxyObjectFactory patientDiscoveryFactory = new NhinPatientDiscoveryAsyncRespProxyObjectFactory();
        NhinPatientDiscoveryAsyncRespProxy proxy = patientDiscoveryFactory.getNhinPatientDiscoveryAsyncRespProxy();

        resp = proxy.respondingGatewayPRPAIN201306UV02(request.getPRPAIN201306UV02(), request.getAssertion(), oTargetSystemType);

        // Audit the Patient Discovery Response Message received on the Nhin Interface
        auditLog.auditAck(resp, request.getAssertion(), NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        return resp;
    }

}
