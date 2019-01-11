/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.queue;

import gov.hhs.fha.nhinc.async.AddressingHeaderCreator;
import gov.hhs.fha.nhinc.async.AsyncMessageProcessHelper;
import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.connectmgr.UrlInfo;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManager;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201305Processor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryException;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryProcessor;
import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.response.proxy.EntityPatientDiscoveryDeferredResponseProxy;
import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.response.proxy.EntityPatientDiscoveryDeferredResponseProxyObjectFactory;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.util.List;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author JHOPPESC
 */
public class AdapterPatientDiscoveryDeferredReqQueueOrchImpl {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterPatientDiscoveryDeferredReqQueueOrchImpl.class);

    protected AsyncMessageProcessHelper createAsyncProcesser() {
        return new AsyncMessageProcessHelper();
    }

    public MCCIIN000002UV01 addPatientDiscoveryAsyncReq(PRPAIN201305UV02 request, AssertionType assertion,
        NhinTargetCommunitiesType targets) {
        MCCIIN000002UV01 resp;
        RespondingGatewayPRPAIN201305UV02RequestType unsecureRequest
            = new RespondingGatewayPRPAIN201305UV02RequestType();
        unsecureRequest.setAssertion(assertion);
        unsecureRequest.setNhinTargetCommunities(targets);
        unsecureRequest.setPRPAIN201305UV02(request);

        // ASYNCMSG PROCESSING - RSPPROCESS
        AsyncMessageProcessHelper asyncProcess = createAsyncProcesser();
        String messageId = assertion.getMessageId();
        boolean bIsQueueOk = asyncProcess.processMessageStatus(messageId, AsyncMsgRecordDao.QUEUE_STATUS_RSPPROCESS);

        // check for valid queue entry
        if (bIsQueueOk) {
            resp = addPatientDiscoveryAsyncReq(unsecureRequest);

            asyncProcess.processAck(messageId, AsyncMsgRecordDao.QUEUE_STATUS_RSPSENTACK,
                AsyncMsgRecordDao.QUEUE_STATUS_RSPSENTERR, resp);
        } else {
            String ackMsg
                = "Deferred Patient Discovery response processing halted; deferred queue repository error encountered";

            // Set the error acknowledgement status
            // fatal error with deferred queue repository
            resp = HL7AckTransforms.createAckErrorFrom201305(request, ackMsg);
        }
        return resp;
    }

    protected MCCIIN000002UV01 addPatientDiscoveryAsyncReq(RespondingGatewayPRPAIN201305UV02RequestType request) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        // "process" the request and send a response out to the Nhin.
        PatientDiscoveryProcessor msgProcessor = new PatientDiscovery201305Processor();
        PRPAIN201306UV02 resp;
        try {
            resp = msgProcessor.process201305(request.getPRPAIN201305UV02(), request.getAssertion());

            // Generate a new response assertion
            AsyncMessageProcessHelper asyncProcess = createAsyncProcesser();
            AssertionType newAssertion = asyncProcess.copyAssertionTypeObject(request.getAssertion());
            // Original request message id is now set as the relates to id
            newAssertion.getRelatesToList().add(request.getAssertion().getMessageId());
            // Generate a new unique response assertion Message ID
            newAssertion.setMessageId(AddressingHeaderCreator.generateMessageId());
            // Set user info homeCommunity
            String homeCommunityId = HomeCommunityMap.getLocalHomeCommunityId();
            HomeCommunityType homeCommunityType = new HomeCommunityType();
            homeCommunityType.setHomeCommunityId(homeCommunityId);
            homeCommunityType.setName(homeCommunityId);
            newAssertion.setHomeCommunity(homeCommunityType);
            if (newAssertion.getUserInfo() != null && newAssertion.getUserInfo().getOrg() != null) {
                newAssertion.getUserInfo().getOrg().setHomeCommunityId(homeCommunityId);
                newAssertion.getUserInfo().getOrg().setName(homeCommunityId);
            }

            ack = sendToNhin(resp, newAssertion, request.getNhinTargetCommunities());
        } catch (PatientDiscoveryException e) {
            LOG.error("Error occurred while processing Patient Discovery Deferred Request", e);
        }

        return ack;
    }

    protected MCCIIN000002UV01 sendToNhin(PRPAIN201306UV02 respMsg, AssertionType assertion,
        NhinTargetCommunitiesType targets) {
        MCCIIN000002UV01 resp = new MCCIIN000002UV01();
        java.util.List<UrlInfo> urlInfoList;

        if (targets != null) {
            urlInfoList = getTargetEndpoints(targets);

            if (NullChecker.isNotNullish(urlInfoList) && urlInfoList.get(0) != null
                && NullChecker.isNotNullish(urlInfoList.get(0).getUrl())) {

                EntityPatientDiscoveryDeferredResponseProxyObjectFactory patientDiscoveryFactory
                    = new EntityPatientDiscoveryDeferredResponseProxyObjectFactory();
                EntityPatientDiscoveryDeferredResponseProxy proxy = patientDiscoveryFactory.
                    getNhincPatientDiscoveryProxy();

                resp = proxy.processPatientDiscoveryAsyncResp(respMsg, assertion, targets);
            } else {
                LOG.error("Failed to send response to the Nhin as no target endpoints can be found.");
            }
        }

        return resp;
    }

    protected List<UrlInfo> getTargetEndpoints(NhinTargetCommunitiesType targetCommunities) {
        List<UrlInfo> urlInfoList;

        // Obtain all the URLs for the targets being sent to
        try {
            urlInfoList = ExchangeManager.getInstance().getEndpointURLFromNhinTargetCommunities(
                targetCommunities, NhincConstants.PATIENT_DISCOVERY_DEFERRED_RESP_SERVICE_NAME);

        } catch (ExchangeManagerException ex) {
            LOG.error("Failed to obtain target URLs for service {}: {}",
                NhincConstants.PATIENT_DISCOVERY_DEFERRED_RESP_SERVICE_NAME, ex.getLocalizedMessage(), ex);
            return null;
        }

        return urlInfoList;
    }
}
