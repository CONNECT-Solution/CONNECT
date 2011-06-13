/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request;

import gov.hhs.fha.nhinc.async.AsyncMessageIdCreator;
import gov.hhs.fha.nhinc.async.AsyncMessageProcessHelper;
import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfo;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfos;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201305Processor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryPolicyChecker;
import gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.request.proxy.PassthruPatientDiscoveryDeferredRequestProxy;
import gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.request.proxy.PassthruPatientDiscoveryDeferredRequestProxyObjectFactory;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.sql.Timestamp;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;

public class EntityPatientDiscoveryDeferredRequestOrchImpl {

    private Log log = null;

    public EntityPatientDiscoveryDeferredRequestOrchImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected PatientDiscoveryAuditLogger createAuditLogger() {
        return new PatientDiscoveryAuditLogger();
    }

    protected AsyncMessageProcessHelper createAsyncProcesser() {
        return new AsyncMessageProcessHelper();
    }

    protected PatientDiscovery201305Processor getPatientDiscovery201305Processor() {
        return new PatientDiscovery201305Processor();
    }

    public MCCIIN000002UV01 processPatientDiscoveryAsyncReq(PRPAIN201305UV02 message, AssertionType assertion, NhinTargetCommunitiesType targets) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();
        String ackMsg = "";
        CMUrlInfos urlInfoList = null;
        PatientDiscovery201305Processor pd201305Processor = getPatientDiscovery201305Processor();

        if (message != null &&
                assertion != null) {

            AsyncMessageProcessHelper asyncProcess = createAsyncProcesser();

            // Audit the Patient Discovery Request Message sent on the Entity Interface
            PatientDiscoveryAuditLogger auditLog = createAuditLogger();

            RespondingGatewayPRPAIN201305UV02RequestType unsecureRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
            unsecureRequest.setNhinTargetCommunities(targets);
            unsecureRequest.setPRPAIN201305UV02(message);
            unsecureRequest.setAssertion(assertion);
            auditLog.auditEntityDeferred201305(unsecureRequest, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_REQUEST_PROCESS);

            // Process local unique patient id and home community id to create local aa to hcid mapping
            pd201305Processor.storeLocalMapping(unsecureRequest);

            urlInfoList = getTargets(targets);

            // loop through the communities and send request if results were not null
            if (urlInfoList != null &&
                    urlInfoList.getUrlInfo() != null) {

                // Log the start of the performance record
                Timestamp starttime = new Timestamp(System.currentTimeMillis());
                Long logId = PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(starttime, "Deferred"+NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, HomeCommunityMap.getLocalHomeCommunityId());

                for (CMUrlInfo urlInfo : urlInfoList.getUrlInfo()) {

                    //create a new request to send out to each target community
                    RespondingGatewayPRPAIN201305UV02RequestType newRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
                    PRPAIN201305UV02 new201305 = pd201305Processor.createNewRequest(message, urlInfo.getHcid());

                    // Each new request must generate its own unique assertion Message ID
                    AssertionType newAssertion = asyncProcess.copyAssertionTypeObject(assertion);

                    newAssertion.setMessageId(AsyncMessageIdCreator.generateMessageId());

                    newRequest.setAssertion(newAssertion);
                    newRequest.setPRPAIN201305UV02(new201305);
                    newRequest.setNhinTargetCommunities(targets);

                    // The new request is ready for processing, add a new outbound PD entry to the local deferred queue
                    boolean bIsQueueOk = asyncProcess.addPatientDiscoveryRequest(newRequest, AsyncMsgRecordDao.QUEUE_DIRECTION_OUTBOUND);

                    // check for valid queue entry
                    if (bIsQueueOk) {
                        //check the policy for the outgoing request to the target community
                        boolean bIsPolicyOk = checkPolicy(newRequest);

                        if (bIsPolicyOk) {
                            ack = sendToProxy(new201305, newAssertion, urlInfo);
                        } else {
                            ackMsg = "Policy Failed";

                            // Set the error acknowledgement status of the deferred queue entry
                            ack = HL7AckTransforms.createAckErrorFrom201305(message, ackMsg);
                            asyncProcess.processAck(newAssertion.getMessageId(), AsyncMsgRecordDao.QUEUE_STATUS_REQSENTERR, AsyncMsgRecordDao.QUEUE_STATUS_REQSENTERR, ack);
                        }
                        // clean up new assertion
                        newAssertion = null;
                    } else {
                        ackMsg = "Deferred Patient Discovery request processing halted; deferred queue repository error encountered";

                        // Set the error acknowledgement status
                        // break processing loop in order to return immediately - fatal error with deferred queue repository
                        ack = HL7AckTransforms.createAckErrorFrom201305(message, ackMsg);
                        asyncProcess.processAck(newAssertion.getMessageId(), AsyncMsgRecordDao.QUEUE_STATUS_REQSENTERR, AsyncMsgRecordDao.QUEUE_STATUS_REQSENTERR, ack);
                        break;
                    }
                }

                // Log the end of the performance record
                Timestamp stoptime = new Timestamp(System.currentTimeMillis());
                PerformanceManager.getPerformanceManagerInstance().logPerformanceStop(logId, starttime, stoptime);
            } else {
                ackMsg = "No targets were found for the Patient Discovery Request";
                log.warn(ackMsg);
                // Add deferred queue record based on entity message
                asyncProcess.addPatientDiscoveryRequest(unsecureRequest, AsyncMsgRecordDao.QUEUE_DIRECTION_OUTBOUND);

                // Set the error acknowledgement status of the deferred queue entry
                ack = HL7AckTransforms.createAckErrorFrom201305(message, ackMsg);
                asyncProcess.processAck(unsecureRequest.getAssertion().getMessageId(), AsyncMsgRecordDao.QUEUE_STATUS_REQSENTERR, AsyncMsgRecordDao.QUEUE_STATUS_REQSENTERR, ack);
            }

            auditLog.auditAck(ack, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        }

        return ack;
    }

    protected CMUrlInfos getTargets(NhinTargetCommunitiesType targetCommunities) {
        CMUrlInfos urlInfoList = null;

        // Obtain all the URLs for the targets being sent to
        try {
            urlInfoList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(targetCommunities, NhincConstants.PATIENT_DISCOVERY_ASYNC_REQ_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Failed to obtain target URLs for service " + NhincConstants.PATIENT_DISCOVERY_ASYNC_REQ_SERVICE_NAME);
            return null;
        }

        return urlInfoList;
    }

    protected boolean checkPolicy(RespondingGatewayPRPAIN201305UV02RequestType request) {
        return new PatientDiscoveryPolicyChecker().checkOutgoingPolicy(request);
    }

    protected MCCIIN000002UV01 sendToProxy(PRPAIN201305UV02 request, AssertionType newAssertion, CMUrlInfo urlInfo) {
        MCCIIN000002UV01 resp = new MCCIIN000002UV01();

        NhinTargetSystemType oTargetSystemType = new NhinTargetSystemType();
        oTargetSystemType.setUrl(urlInfo.getUrl());

        // Audit the Patient Discovery Request Message sent on the Nhin Interface
        PatientDiscoveryAuditLogger auditLog = new PatientDiscoveryAuditLogger();
        AcknowledgementType ack = auditLog.auditNhinDeferred201305(request, newAssertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);

        PassthruPatientDiscoveryDeferredRequestProxyObjectFactory patientDiscoveryFactory = new PassthruPatientDiscoveryDeferredRequestProxyObjectFactory();
        PassthruPatientDiscoveryDeferredRequestProxy proxy = patientDiscoveryFactory.getPassthruPatientDiscoveryDeferredRequestProxy();

        log.debug("Invoking " + proxy + ".processPatientDiscoveryAsyncReq with " + request + " assertion: " + newAssertion + " and target " + oTargetSystemType + " url: " + oTargetSystemType.getUrl());
        resp = proxy.processPatientDiscoveryAsyncReq(request, newAssertion, oTargetSystemType);

        // Audit the Patient Discovery Response Message received on the Nhin Interface
        auditLog.auditAck(resp, newAssertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        return resp;
    }

}
