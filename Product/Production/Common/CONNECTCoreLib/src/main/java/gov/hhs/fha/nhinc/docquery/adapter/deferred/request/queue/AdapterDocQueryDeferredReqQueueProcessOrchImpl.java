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
package gov.hhs.fha.nhinc.docquery.adapter.deferred.request.queue;

import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.asyncmsgs.model.AsyncMsgRecord;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.bind.Unmarshaller;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;

/**
 *
 * @author narendra.reddy
 */
import gov.hhs.fha.nhinc.docquery.adapter.deferred.request.queue.proxy.AdapterDocQueryDeferredRequestQueueProxyJavaImpl;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;

public class AdapterDocQueryDeferredReqQueueProcessOrchImpl {

    private static Log log = LogFactory.getLog(AdapterDocQueryDeferredReqQueueProcessOrchImpl.class);

    public AdapterDocQueryDeferredReqQueueProcessOrchImpl() {
    }

    /**
     * processDocQueryAsyncReqQueue Orchestration method for processing request queues on responding gateway
     * @param messageId
     * @return DocQueryAcknowledgementType
     */
    public DocQueryAcknowledgementType processDocQueryAsyncReqQueue(String messageId) {
        log.info("Begin AdapterDocQueryDeferredReqQueueProcessOrchImpl.processDocQueryAsyncReqQueue()....");
        DocQueryAcknowledgementType ack = new DocQueryAcknowledgementType();
      
        try {
            RespondingGatewayCrossGatewayQueryRequestType respondingGatewayCrossGatewayQueryRequestType = new RespondingGatewayCrossGatewayQueryRequestType();
            //Extract the Request from the DB for the given msgid.
            AsyncMsgRecord asyncMsgRecord = new AsyncMsgRecord();
            AsyncMsgRecordDao asyncDao = new AsyncMsgRecordDao();
            log.info("messageId: " + messageId);
            if ((messageId != null)) {
                List<AsyncMsgRecord> msgList = new ArrayList<AsyncMsgRecord>();
                msgList = asyncDao.queryByMessageIdAndDirection(messageId, AsyncMsgRecordDao.QUEUE_DIRECTION_INBOUND);
                if ((msgList != null) &&
                        (msgList.size() > 0)) {
                    log.info("msgList: " + msgList.size());
                    asyncMsgRecord = msgList.get(0);

                    // Set queue status to processing
                    asyncMsgRecord.setStatus(AsyncMsgRecordDao.QUEUE_STATUS_RSPPROCESS);
                    asyncDao.save(asyncMsgRecord);
                } else {
                    log.info("msgList: is null");
                }

            } else {
                log.info("messageId: is null");
            }


            log.info("AsyncMsgRecord - messageId: " + asyncMsgRecord.getMessageId());
            log.info("AsyncMsgRecord - serviceName: " + asyncMsgRecord.getServiceName());
            log.info("AsyncMsgRecord - creationTime: " + asyncMsgRecord.getCreationTime());

            AdapterDocQueryDeferredRequestQueueProxyJavaImpl adapterDocQueryDeferredReqQueueProxyJavaImpl = new AdapterDocQueryDeferredRequestQueueProxyJavaImpl();
           
            if (asyncMsgRecord.getMsgData() != null) {
                respondingGatewayCrossGatewayQueryRequestType = extractRespondingGatewayQueryRequestType(asyncMsgRecord.getMsgData());
            }
            if (respondingGatewayCrossGatewayQueryRequestType != null) {
                log.info("AsyncMsgRecord - messageId: " + respondingGatewayCrossGatewayQueryRequestType.getAdhocQueryRequest().getId());

                AdhocQueryRequest adhocQueryRequest = null;
                adhocQueryRequest = respondingGatewayCrossGatewayQueryRequestType.getAdhocQueryRequest();

                //Extract the SenderOID from the request which we got from db.
                String senderTargetCommunityId = extractSenderOID(adhocQueryRequest);

                //Set the Sender HomeCommunity Id in the NHINTargetCommunity to serve the Deferred Request.
                if (senderTargetCommunityId != null) {
                    log.info("SenderTargetCommunityId: " + senderTargetCommunityId);
                    NhinTargetCommunitiesType targetCommunities = new NhinTargetCommunitiesType();
                    NhinTargetCommunityType nhinTargetCommunityType = new NhinTargetCommunityType();
                    HomeCommunityType homeCommunityType = new HomeCommunityType();
                    homeCommunityType.setHomeCommunityId(senderTargetCommunityId);
                    nhinTargetCommunityType.setHomeCommunity(homeCommunityType);
                    targetCommunities.getNhinTargetCommunity().add(nhinTargetCommunityType);

                    // Generate new request queue assertion from original request message assertion
                    AssertionType assertion = respondingGatewayCrossGatewayQueryRequestType.getAssertion();

                    ack = adapterDocQueryDeferredReqQueueProxyJavaImpl.respondingGatewayCrossGatewayQuery(adhocQueryRequest, assertion, targetCommunities);

                } else {
                    log.error("Sender home is null - Unable to extract target community hcid from sender home");
                }
            }
        } catch (Exception e) {
            log.error("ERROR: Failed in accessing the async msg from repository.", e);
        }

        return ack;
    }

    /**
     *
     * @param msgData
     * @return RespondingGatewayCrossGatewayQueryRequestType
     */
    private RespondingGatewayCrossGatewayQueryRequestType extractRespondingGatewayQueryRequestType(Blob msgData) {
        log.debug("Begin AdapterDocQueryDeferredReqQueueProcessOrchImpl.extractRespondingGatewayQueryRequestType()..");
        RespondingGatewayCrossGatewayQueryRequestType respondingGatewayCrossGatewayQueryRequestType = new RespondingGatewayCrossGatewayQueryRequestType();
        try {
            byte[] msgBytes = null;
            if (msgData != null) {
                msgBytes = msgData.getBytes(1, (int) msgData.length());
                ByteArrayInputStream xmlContentBytes = new ByteArrayInputStream(msgBytes);
                JAXBContext context = JAXBContext.newInstance("gov.hhs.fha.nhinc.common.nhinccommonentity");
                Unmarshaller u = context.createUnmarshaller();
                JAXBElement<RespondingGatewayCrossGatewayQueryRequestType> root = (JAXBElement<RespondingGatewayCrossGatewayQueryRequestType>) u.unmarshal(xmlContentBytes);
                respondingGatewayCrossGatewayQueryRequestType = root.getValue();
                log.debug("End AdapterDocQueryDeferredReqQueueProcessOrchImpl.extractRespondingGatewayQueryRequestType()..");
            }
        } catch (Exception e) {
            log.error("Exception during Blob conversion :" + e.getMessage());
            e.printStackTrace();
        }
        return respondingGatewayCrossGatewayQueryRequestType;
    }

    /**
     * Extracts the home community id of the request.
     *
     * @param request
     * @return String
     */
    private String extractSenderOID(AdhocQueryRequest request) {
        String oid = null;
        if (request != null) {
            oid = HomeCommunityMap.getCommunityIdForDeferredQDRequest(request.getAdhocQuery());
        }

        return oid;
    }
}
