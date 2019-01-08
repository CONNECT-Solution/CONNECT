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

import gov.hhs.fha.nhinc.async.AsyncMessageProcessHelper;
import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.asyncmsgs.model.AsyncMsgRecord;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.queue.proxy.AdapterPatientDiscoveryAsyncReqQueueProxyJavaImpl;
import gov.hhs.fha.nhinc.util.JAXBUnmarshallingUtil;
import gov.hhs.fha.nhinc.util.StreamUtils;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.collections.CollectionUtils;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mastan.ketha
 */
public class AdapterPatientDiscoveryDeferredReqQueueProcessOrchImpl {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterPatientDiscoveryDeferredReqQueueProcessOrchImpl.class);

    public AdapterPatientDiscoveryDeferredReqQueueProcessOrchImpl() {
    }

    protected AsyncMessageProcessHelper createAsyncProcesser() {
        return new AsyncMessageProcessHelper();
    }

    /**
     * processPatientDiscoveryDeferredReqQueue Orchestration method for processing request queues on reponding gateway
     *
     * @param messageId
     * @return org.hl7.v3.MCCIIN000002UV01
     */
    public MCCIIN000002UV01 processPatientDiscoveryDeferredReqQueue(String messageId) {
        LOG.info("Begin AdapterPatientDiscoveryDeferredReqQueueProcessOrchImpl.processPatientDiscoveryAsyncReq()....");
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();
        try {
            RespondingGatewayPRPAIN201305UV02RequestType respondingGatewayPRPAIN201305UV02RequestType = new RespondingGatewayPRPAIN201305UV02RequestType();

            AsyncMsgRecord asyncMsgRecord = new AsyncMsgRecord();
            AsyncMsgRecordDao instance = new AsyncMsgRecordDao();
            LOG.info("messageId: " + messageId);
            if (messageId != null) {
                List<AsyncMsgRecord> msgList;
                msgList = instance.queryByMessageIdAndDirection(messageId, AsyncMsgRecordDao.QUEUE_DIRECTION_INBOUND);
                if (CollectionUtils.isNotEmpty(msgList)) {
                    LOG.info("msgList: " + msgList.size());
                    asyncMsgRecord = msgList.get(0);

                    // Set queue status to processing
                    asyncMsgRecord.setStatus(AsyncMsgRecordDao.QUEUE_STATUS_RSPPROCESS);
                    instance.save(asyncMsgRecord);
                } else {
                    LOG.info("msgList: is null");
                }

            } else {
                LOG.info("messageId: is null");
            }

            LOG.debug("AsyncMsgRecord - messageId Found");
            LOG.debug("AsyncMsgRecord - serviceName Found");
            LOG.debug("AsyncMsgRecord - creationTime Found");

            AdapterPatientDiscoveryAsyncReqQueueProxyJavaImpl adapterPDAsyncReqQueueProxyJavaImpl = new AdapterPatientDiscoveryAsyncReqQueueProxyJavaImpl();

            if (asyncMsgRecord.getMsgData() != null) {
                respondingGatewayPRPAIN201305UV02RequestType = extractRespondingGatewayPRPAIN201305UV02RequestType(asyncMsgRecord
                    .getMsgData());
            }

            if (respondingGatewayPRPAIN201305UV02RequestType != null) {
                LOG.info("AsyncMsgRecord - messageId: "
                    + respondingGatewayPRPAIN201305UV02RequestType.getPRPAIN201305UV02().getITSVersion());

                PRPAIN201305UV02 pRPAIN201305UV02 = respondingGatewayPRPAIN201305UV02RequestType.getPRPAIN201305UV02();

                String senderTargetCommId = extractSenderOID(pRPAIN201305UV02);
                if (senderTargetCommId != null) {
                    LOG.info("senderTargetCommId: " + senderTargetCommId);

                    NhinTargetCommunitiesType targetCommunities = new NhinTargetCommunitiesType();
                    NhinTargetCommunityType nhinTargetCommunityType = new NhinTargetCommunityType();
                    HomeCommunityType homeCommunityType = new HomeCommunityType();
                    homeCommunityType.setHomeCommunityId(senderTargetCommId);
                    nhinTargetCommunityType.setHomeCommunity(homeCommunityType);
                    targetCommunities.getNhinTargetCommunity().add(nhinTargetCommunityType);

                    // Generate new request queue assertion from original request message assertion
                    AssertionType assertion = respondingGatewayPRPAIN201305UV02RequestType.getAssertion();

                    ack = adapterPDAsyncReqQueueProxyJavaImpl.addPatientDiscoveryAsyncReq(pRPAIN201305UV02, assertion,
                        targetCommunities);
                } else {
                    LOG.error("Sender root is null - Unable to extract target community hcid from sender root");
                }
            }
        } catch (Exception e) {
            LOG.error("ERROR: Failed in accessing the async msg from repository.", e);
        }

        return ack;
    }

    @SuppressWarnings("unchecked")
    private static RespondingGatewayPRPAIN201305UV02RequestType extractRespondingGatewayPRPAIN201305UV02RequestType(
        Blob msgData) {
        LOG.debug("Begin AdapterPatientDiscoveryDeferredReqQueueProcessOrchImpl.extractRespondingGatewayPRPAIN201305UV02RequestType()..");
        RespondingGatewayPRPAIN201305UV02RequestType respondingGatewayPRPAIN201305UV02RequestType = new RespondingGatewayPRPAIN201305UV02RequestType();
        InputStream is = null;
        try {
            byte[] msgBytes;
            if (msgData != null) {
                JAXBUnmarshallingUtil util = new JAXBUnmarshallingUtil();
                msgBytes = msgData.getBytes(1, (int) msgData.length());
                is = new ByteArrayInputStream(msgBytes);

                JAXBContext context = JAXBContext.newInstance("org.hl7.v3");
                Unmarshaller u = context.createUnmarshaller();
                JAXBElement<RespondingGatewayPRPAIN201305UV02RequestType> root = (JAXBElement<RespondingGatewayPRPAIN201305UV02RequestType>) u
                    .unmarshal(util.getSafeStreamReaderFromInputStream(is));

                respondingGatewayPRPAIN201305UV02RequestType = root.getValue();
                LOG.debug("End AdapterPatientDiscoveryDeferredReqQueueProcessOrchImpl.extractRespondingGatewayPRPAIN201305UV02RequestType()..");
            }
        } catch (Exception e) {
            LOG.error("Exception during Blob conversion :" + e.getLocalizedMessage(), e);
        } finally {
            StreamUtils.closeStreamSilently(is);
        }
        return respondingGatewayPRPAIN201305UV02RequestType;
    }

    private static String extractSenderOID(PRPAIN201305UV02 request) {
        String oid = null;

        if (request != null
            && request.getSender() != null
            && request.getSender().getDevice() != null
            && request.getSender().getDevice().getAsAgent() != null
            && request.getSender().getDevice().getAsAgent().getValue() != null
            && request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization() != null
            && request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null
            && NullChecker.isNotNullish(request.getSender().getDevice().getAsAgent().getValue()
                .getRepresentedOrganization().getValue().getId())
            && request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue()
            .getId().get(0) != null
            && NullChecker.isNotNullish(request.getSender().getDevice().getAsAgent().getValue()
                .getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
            oid = request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue()
                .getId().get(0).getRoot();
        }

        return oid;
    }
}
