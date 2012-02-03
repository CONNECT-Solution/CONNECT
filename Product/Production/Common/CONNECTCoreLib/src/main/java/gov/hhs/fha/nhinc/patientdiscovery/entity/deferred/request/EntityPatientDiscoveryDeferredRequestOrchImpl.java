/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request;

import gov.hhs.fha.nhinc.async.AsyncMessageProcessHelper;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.UrlInfo;


import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.dao.PDDeferredCorrelationDao;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201305Processor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryPolicyChecker;
import gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.request.proxy.PassthruPatientDiscoveryDeferredRequestProxy;
import gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.request.proxy.PassthruPatientDiscoveryDeferredRequestProxyObjectFactory;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;
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

    protected PatientDiscoveryAuditor createAuditLogger() {
        return new PatientDiscoveryAuditLogger();
    }

    protected AsyncMessageProcessHelper createAsyncProcesser() {
        return new AsyncMessageProcessHelper();
    }

    protected PatientDiscovery201305Processor getPatientDiscovery201305Processor() {
        return new PatientDiscovery201305Processor();
    }

    protected PDDeferredCorrelationDao getPDDeferredCorrelationDao() {
        return new PDDeferredCorrelationDao();
    }

    public MCCIIN000002UV01 processPatientDiscoveryAsyncReq(PRPAIN201305UV02 message, AssertionType assertion, NhinTargetCommunitiesType targets) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();
        String ackMsg = "";
        List<UrlInfo> urlInfoList = null;
        PatientDiscovery201305Processor pd201305Processor = getPatientDiscovery201305Processor();

        if (message != null &&
                assertion != null) {

            AsyncMessageProcessHelper asyncProcess = createAsyncProcesser();

            // Audit the Patient Discovery Request Message sent on the Entity Interface
            PatientDiscoveryAuditor auditLog = createAuditLogger();

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
                    urlInfoList != null) {

                // Log the start of the performance record
                Timestamp starttime = new Timestamp(System.currentTimeMillis());
                Long logId = PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(starttime, "Deferred"+NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, HomeCommunityMap.getLocalHomeCommunityId());
         
                String messageId = "";
                if (assertion.getMessageId() != null && assertion.getMessageId().length() > 0) {
                    messageId = assertion.getMessageId();
                }
                II patientId = pd201305Processor.extractPatientIdFrom201305(message);

                PDDeferredCorrelationDao pdDeferredDao = getPDDeferredCorrelationDao();
                pdDeferredDao.saveOrUpdate(messageId, patientId);
                
                for (UrlInfo urlInfo : urlInfoList) {

                    //create a new request to send out to each target community
                    RespondingGatewayPRPAIN201305UV02RequestType newRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
                    PRPAIN201305UV02 new201305 = pd201305Processor.createNewRequest(message, urlInfo.getHcid());

                    AssertionType newAssertion = asyncProcess.copyAssertionTypeObject(assertion);
                    newRequest.setAssertion(newAssertion);
                    newRequest.setPRPAIN201305UV02(new201305);
                    newRequest.setNhinTargetCommunities(targets);

                    //check the policy for the outgoing request to the target community
                    boolean bIsPolicyOk = checkPolicy(newRequest);

                    if (bIsPolicyOk) {
                        ack = sendToProxy(new201305, newAssertion, urlInfo);
                    } else {
                        ackMsg = "Policy Failed";

                        // Set the error acknowledgement status of the deferred queue entry
                        ack = HL7AckTransforms.createAckErrorFrom201305(message, ackMsg);
                    }
                    // clean up new assertion
                    newAssertion = null;
                }

                // Log the end of the performance record
                Timestamp stoptime = new Timestamp(System.currentTimeMillis());
                PerformanceManager.getPerformanceManagerInstance().logPerformanceStop(logId, starttime, stoptime);
            } else {
                ackMsg = "No targets were found for the Patient Discovery Request";
                log.warn(ackMsg);

                // Set the error acknowledgement status of the deferred queue entry
                ack = HL7AckTransforms.createAckErrorFrom201305(message, ackMsg);
            }

            auditLog.auditAck(ack, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        }

        return ack;
    }

    protected List<UrlInfo> getTargets(NhinTargetCommunitiesType targetCommunities) {
        List<UrlInfo> urlInfoList = null;

        // Obtain all the URLs for the targets being sent to
        try {
            urlInfoList = ConnectionManagerCache.getInstance().getEndpontURLFromNhinTargetCommunities(targetCommunities, NhincConstants.PATIENT_DISCOVERY_DEFERRED_REQ_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Failed to obtain target URLs for service " + NhincConstants.PATIENT_DISCOVERY_DEFERRED_REQ_SERVICE_NAME);
            return null;
        }

        return urlInfoList;
    }

    protected boolean checkPolicy(RespondingGatewayPRPAIN201305UV02RequestType request) {
        return PatientDiscoveryPolicyChecker.getInstance().checkOutgoingPolicy(request);
    }

    protected MCCIIN000002UV01 sendToProxy(PRPAIN201305UV02 request, AssertionType newAssertion, UrlInfo urlInfo) {
        MCCIIN000002UV01 resp = new MCCIIN000002UV01();

        NhinTargetSystemType oTargetSystemType = new NhinTargetSystemType();
        oTargetSystemType.setUrl(urlInfo.getUrl());

        // Audit the Patient Discovery Request Message sent on the Nhin Interface
        PatientDiscoveryAuditor auditLog = new PatientDiscoveryAuditLogger();
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
