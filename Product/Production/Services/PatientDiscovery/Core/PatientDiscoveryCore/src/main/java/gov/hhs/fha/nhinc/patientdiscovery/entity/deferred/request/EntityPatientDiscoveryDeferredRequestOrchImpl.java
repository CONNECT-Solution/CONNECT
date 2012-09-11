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

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;

import gov.hhs.fha.nhinc.async.AsyncMessageProcessHelper;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.UrlInfo;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.dao.PDDeferredCorrelationDao;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201305Processor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryPolicyChecker;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7DataTransformHelper;

public class EntityPatientDiscoveryDeferredRequestOrchImpl implements EntityPatientDiscoveryDeferredRequestOrch {

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

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.EntityPatientDiscoveryDeferredRequestOrch#processPatientDiscoveryAsyncReq(org.hl7.v3.PRPAIN201305UV02, gov.hhs.fha.nhinc.common.nhinccommon.AssertionType, gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType)
     */
    @Override
    public MCCIIN000002UV01 processPatientDiscoveryAsyncReq(PRPAIN201305UV02 message, AssertionType assertion,
            NhinTargetCommunitiesType targets) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();
        String ackMsg = "";
        PatientDiscovery201305Processor pd201305Processor = getPatientDiscovery201305Processor();
        AsyncMessageProcessHelper asyncProcess = createAsyncProcesser();

        if (message != null && assertion != null) {

            RespondingGatewayPRPAIN201305UV02RequestType unsecureRequest = createNewRespondingGatewayRequest(message,
                    assertion, targets);

            auditRequestFromAdapter(unsecureRequest, assertion);

            // Process local unique patient id and home community id to create local aa to hcid mapping
            pd201305Processor.storeLocalMapping(unsecureRequest);

            // loop through the communities and send request if results were not null
            List<UrlInfo> urlInfoList = getTargetEndpoints(targets);
            if (urlInfoList != null) {
                II patientId = pd201305Processor.extractPatientIdFrom201305(message);
                if (patientId != null) {
                    createMessageIdToPatientIdCorrelation(assertion.getMessageId(), patientId);
                }

                for (UrlInfo urlInfo : urlInfoList) {

                    // create a new request to send out to each target community
                    PRPAIN201305UV02 new201305 = createNewPRPAIN201305UV02ForOneTargetCommunity(message,
                            urlInfo.getHcid(), pd201305Processor);
                    AssertionType newAssertion = asyncProcess.copyAssertionTypeObject(assertion);

                    RespondingGatewayPRPAIN201305UV02RequestType newRequest = createNewRespondingGatewayRequest(
                            new201305, newAssertion, targets);

                    // check the policy for the outgoing request to the target community
                    boolean bIsPolicyOk = checkPolicy(newRequest);
                    if (bIsPolicyOk) {
                        ack = sendToProxy(new201305, newAssertion, urlInfo);
                    } else {
                        ackMsg = "Policy Failed";
                        ack = HL7AckTransforms.createAckErrorFrom201305(message, ackMsg);
                    }
                    // clean up new assertion
                    newAssertion = null;
                }
            } else {
                ackMsg = "No targets were found for the Patient Discovery Request";
                log.warn(ackMsg);
                ack = HL7AckTransforms.createAckErrorFrom201305(message, ackMsg);
            }

            auditResponseToAdapter(ack, assertion);
        }

        return ack;
    }

    protected void auditRequestFromAdapter(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion) {
        PatientDiscoveryAuditor auditLog = createAuditLogger();
        auditLog.auditEntityDeferred201305(request, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_REQUEST_PROCESS);
    }

    protected void auditResponseToAdapter(MCCIIN000002UV01 ack, AssertionType assertion) {
        PatientDiscoveryAuditor auditLog = createAuditLogger();
        auditLog.auditAck(ack, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
    }

    protected void createMessageIdToPatientIdCorrelation(String messageId, II patientId) {
        String correlationMsgId = "";
        if (NullChecker.isNotNullish(messageId)) {
            correlationMsgId = messageId;
        } else {
            log.error("Failed to retrieve message id from outgoing PD deferred request.");
        }

        PDDeferredCorrelationDao pdDeferredDao = getPDDeferredCorrelationDao();
        pdDeferredDao.saveOrUpdate(correlationMsgId, patientId);
    }

    protected List<UrlInfo> getTargetEndpoints(NhinTargetCommunitiesType targetCommunities) {
        List<UrlInfo> urlInfoList = null;

        try {
            urlInfoList = ConnectionManagerCache.getInstance().getEndpointURLFromNhinTargetCommunities(
                    targetCommunities, NhincConstants.PATIENT_DISCOVERY_DEFERRED_REQ_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Failed to obtain target URLs for service "
                    + NhincConstants.PATIENT_DISCOVERY_DEFERRED_REQ_SERVICE_NAME);
            return null;
        }

        return urlInfoList;
    }

    protected PRPAIN201305UV02 createNewPRPAIN201305UV02ForOneTargetCommunity(PRPAIN201305UV02 message, String hcid,
            PatientDiscovery201305Processor pd201305Processor) {

        PRPAIN201305UV02 new201305 = pd201305Processor.createNewRequest(message, hcid);

        // Make sure the response modality and response priority codes are set as per the spec
        if (new201305.getControlActProcess() != null && new201305.getControlActProcess().getQueryByParameter() != null) {
            PRPAMT201306UV02QueryByParameter queryParams = new201305.getControlActProcess().getQueryByParameter()
                    .getValue();
            if (queryParams.getResponseModalityCode() == null) {
                queryParams.setResponseModalityCode(HL7DataTransformHelper.CSFactory("R"));
            }
            if (queryParams.getResponsePriorityCode() == null) {
                queryParams.setResponsePriorityCode(HL7DataTransformHelper.CSFactory("I"));
            }
        }

        return new201305;
    }

    protected RespondingGatewayPRPAIN201305UV02RequestType createNewRespondingGatewayRequest(PRPAIN201305UV02 message,
            AssertionType assertion, NhinTargetCommunitiesType targets) {
        RespondingGatewayPRPAIN201305UV02RequestType newRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
        newRequest.setAssertion(assertion);
        newRequest.setPRPAIN201305UV02(message);
        newRequest.setNhinTargetCommunities(targets);

        return newRequest;
    }

    protected boolean checkPolicy(RespondingGatewayPRPAIN201305UV02RequestType request) {
        return PatientDiscoveryPolicyChecker.getInstance().checkOutgoingPolicy(request);
    }

    protected MCCIIN000002UV01 sendToProxy(PRPAIN201305UV02 request, AssertionType assertion, UrlInfo urlInfo) {
        auditRequestToNhin(request, assertion);

        NhinTargetSystemType targetSystemType = createNhinTargetSystemType(urlInfo.getUrl(), urlInfo.getHcid());
        OutboundPatientDiscoveryDeferredRequestDelegate pdReqDelegate = new OutboundPatientDiscoveryDeferredRequestDelegate();
        OutboundPatientDiscoveryDeferredRequestOrchestratable pdReqOrchestratable = new OutboundPatientDiscoveryDeferredRequestOrchestratable(
                pdReqDelegate);
        pdReqOrchestratable.setAssertion(assertion);
        pdReqOrchestratable.setRequest(request);
        pdReqOrchestratable.setTarget(targetSystemType);
        MCCIIN000002UV01 resp = ((OutboundPatientDiscoveryDeferredRequestOrchestratable) pdReqDelegate
                .process(pdReqOrchestratable)).getResponse();

        auditResponseFromNhin(resp, assertion);

        return resp;
    }

    protected NhinTargetSystemType createNhinTargetSystemType(String url, String hcid) {
        NhinTargetSystemType targetSystemType = new NhinTargetSystemType();
        targetSystemType.setUrl(url);

        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(hcid);
        targetSystemType.setHomeCommunity(homeCommunity);

        return targetSystemType;
    }

    protected void auditRequestToNhin(PRPAIN201305UV02 request, AssertionType newAssertion) {
        PatientDiscoveryAuditor auditLog = new PatientDiscoveryAuditLogger();
        auditLog.auditNhinDeferred201305(request, newAssertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
    }

    protected void auditResponseFromNhin(MCCIIN000002UV01 resp, AssertionType newAssertion) {
        PatientDiscoveryAuditor auditLog = new PatientDiscoveryAuditLogger();
        auditLog.auditAck(resp, newAssertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
    }
}
