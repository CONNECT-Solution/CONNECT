/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientdiscovery.entity.async.request.queue;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfos;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.nhinpatientdiscovery.async.response.proxy.NhinPatientDiscoveryAsyncRespProxy;
import gov.hhs.fha.nhinc.nhinpatientdiscovery.async.response.proxy.NhinPatientDiscoveryAsyncRespProxyObjectFactory;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201305Processor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02SecuredRequestType;

/**
 *
 * @author JHOPPESC
 */
public class EntityPatientDiscoverySecuredAsyncReqQueueImpl {

    private static Log log = LogFactory.getLog(EntityPatientDiscoverySecuredAsyncReqQueueImpl.class);

    public MCCIIN000002UV01 addPatientDiscoveryAsyncReq(RespondingGatewayPRPAIN201305UV02SecuredRequestType request, WebServiceContext context) {
        RespondingGatewayPRPAIN201305UV02RequestType unsecureRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
        unsecureRequest.setNhinTargetCommunities(request.getNhinTargetCommunities());
        unsecureRequest.setPRPAIN201305UV02(request.getPRPAIN201305UV02());
        unsecureRequest.setAssertion(SamlTokenExtractor.GetAssertion(context));

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (unsecureRequest != null &&
                unsecureRequest.getAssertion() != null) {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            unsecureRequest.getAssertion().setAsyncMessageId(msgIdExtractor.GetAsyncMessageId(context));
        }

        // Audit the incoming Nhin 201305 Message
        PatientDiscoveryAuditLogger auditLogger = new PatientDiscoveryAuditLogger();
        AcknowledgementType ack = auditLogger.auditEntity201305(unsecureRequest, unsecureRequest.getAssertion(), NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);

        MCCIIN000002UV01 resp = addPatientDiscoveryAsyncReq(unsecureRequest);

        // Audit the responding 201306 Message
        ack = auditLogger.auditAck(resp, unsecureRequest.getAssertion(), NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);

        return resp;
    }

    public MCCIIN000002UV01 addPatientDiscoveryAsyncReq(RespondingGatewayPRPAIN201305UV02RequestType request) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        // TODO: Add code here to "process" the request and send a response out to the Nhin.
        PatientDiscovery201305Processor msgProcessor = new PatientDiscovery201305Processor();
        PRPAIN201306UV02 resp = msgProcessor.process201305(request.getPRPAIN201305UV02(), request.getAssertion());

        ack = sendToNhin(resp, request.getAssertion(), request.getNhinTargetCommunities());

        return ack;
    }

    protected MCCIIN000002UV01 sendToNhin(PRPAIN201306UV02 respMsg, AssertionType assertion, NhinTargetCommunitiesType targets) {
        MCCIIN000002UV01 resp = new MCCIIN000002UV01();
        NhinTargetSystemType targetSystem = new NhinTargetSystemType();
        CMUrlInfos urlInfoList = null;

        if (targets != null) {
            urlInfoList = getTargets(targets);
        }

        if (urlInfoList != null &&
                NullChecker.isNotNullish(urlInfoList.getUrlInfo()) &&
                urlInfoList.getUrlInfo().get(0) != null &&
                NullChecker.isNotNullish(urlInfoList.getUrlInfo().get(0).getUrl())) {

            // Audit the Patient Discovery Request Message sent on the Nhin Interface
            PatientDiscoveryAuditLogger auditLog = new PatientDiscoveryAuditLogger();
            AcknowledgementType ack = auditLog.auditNhin201306(respMsg, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);

            targetSystem.setUrl(urlInfoList.getUrlInfo().get(0).getUrl());

            NhinPatientDiscoveryAsyncRespProxyObjectFactory patientDiscoveryFactory = new NhinPatientDiscoveryAsyncRespProxyObjectFactory();
            NhinPatientDiscoveryAsyncRespProxy proxy = patientDiscoveryFactory.getNhinPatientDiscoveryAsyncRespProxy();

            resp = proxy.respondingGatewayPRPAIN201306UV02(respMsg, assertion, targetSystem);

            // Audit the Patient Discovery Response Message received on the Nhin Interface
            ack = auditLog.auditAck(resp, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        }

        return resp;
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
}
