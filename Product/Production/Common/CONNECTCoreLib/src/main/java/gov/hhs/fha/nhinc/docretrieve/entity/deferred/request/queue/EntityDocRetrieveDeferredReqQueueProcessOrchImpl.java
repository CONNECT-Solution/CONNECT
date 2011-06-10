/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.docretrieve.entity.deferred.request.queue;

import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.asyncmsgs.model.AsyncMsgRecord;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType;
import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author narendra.reddy
 */
import gov.hhs.fha.nhinc.docretrieve.entity.deferred.request.queue.proxy.EntityDocRetrieveDeferredRequestQueueProxyJavaImpl;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;

public class EntityDocRetrieveDeferredReqQueueProcessOrchImpl {

    private static Log log = LogFactory.getLog(EntityDocRetrieveDeferredReqQueueProcessOrchImpl.class);

    public EntityDocRetrieveDeferredReqQueueProcessOrchImpl() {
    }

    /**
     * processDocRetrieveDeferredReqQueue Orchestration method for processing request queues on responding gateway
     * @param messageId
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType processDocRetrieveDeferredReqQueue(String messageId) {
        log.info("Begin EntityDocRetrieveDeferredReqQueueProcessOrchImpl.processDocRetrieveDeferredReqQueueWWW()....");
        // RetrieveDocumentSetResponseType ack = new RetrieveDocumentSetResponseType();
        DocRetrieveAcknowledgementType ack = new DocRetrieveAcknowledgementType();
        RetrieveDocumentSetRequestType retrieveDocumentSetRequestType = null;
        try {

            RespondingGatewayCrossGatewayRetrieveRequestType respondingGatewayCrossGatewayRetrieveRequestType = new RespondingGatewayCrossGatewayRetrieveRequestType();
            //Extract the Request from the DB for the given msgid.
            AsyncMsgRecord asyncMsgRecordFromDb = new AsyncMsgRecord();
            AsyncMsgRecordDao asyncDao = new AsyncMsgRecordDao();
            log.info("messageId: " + messageId);
            if ((messageId != null)) {
                List<AsyncMsgRecord> msgList = new ArrayList<AsyncMsgRecord>();
                msgList = asyncDao.queryByMessageId(messageId);
                if ((msgList != null) &&
                        (msgList.size() > 0)) {
                    log.info("msgList: " + msgList.size());
                    asyncMsgRecordFromDb = msgList.get(0);
                } else {
                    log.info("msgList: is null");
                }

            } else {
                log.info("messageId: is null");
            }


            log.info("AsyncMsgRecord - messageId: " + asyncMsgRecordFromDb.getMessageId());
            log.info("AsyncMsgRecord - serviceName: " + asyncMsgRecordFromDb.getServiceName());
            log.info("AsyncMsgRecord - creationTime: " + asyncMsgRecordFromDb.getCreationTime());

            EntityDocRetrieveDeferredRequestQueueProxyJavaImpl entityDocRetrieveDeferredRequestQueueProxyJavaImpl = new EntityDocRetrieveDeferredRequestQueueProxyJavaImpl();

            if (asyncMsgRecordFromDb.getMsgData() != null) {
                respondingGatewayCrossGatewayRetrieveRequestType = extractRespondingGatewayRetrieveRequestType(asyncMsgRecordFromDb.getMsgData());
            }
            if (respondingGatewayCrossGatewayRetrieveRequestType != null) {
                log.info("AsyncMsgRecord - messageId: " + respondingGatewayCrossGatewayRetrieveRequestType.getAssertion().getMessageId());


                retrieveDocumentSetRequestType = respondingGatewayCrossGatewayRetrieveRequestType.getRetrieveDocumentSetRequest();

                //Extract the Sender Gateway OID from the assertion which we got from db.
                //String senderTargetCommunityId = extractSenderOID(retrieveDocumentSetRequestType);
                String senderTargetCommunityId = HomeCommunityMap.getCommunityIdFromAssertion(respondingGatewayCrossGatewayRetrieveRequestType.getAssertion());

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
                    AssertionType assertion = respondingGatewayCrossGatewayRetrieveRequestType.getAssertion();

                    ack = entityDocRetrieveDeferredRequestQueueProxyJavaImpl.crossGatewayRetrieveResponse(retrieveDocumentSetRequestType, assertion, targetCommunities);

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
     * @return RespondingGatewayCrossGatewayRetrieveRequestType
     */
    private RespondingGatewayCrossGatewayRetrieveRequestType extractRespondingGatewayRetrieveRequestType(Blob msgData) {
        log.debug("Begin EntityDocRetrieveDeferredReqQueueProcessOrchImpl.extractRespondingGatewayRetrieveRequestType()..");
        RespondingGatewayCrossGatewayRetrieveRequestType respondingGatewayCrossGatewayRetrieveRequestType = new RespondingGatewayCrossGatewayRetrieveRequestType();
        try {
            byte[] msgBytes = null;
            if (msgData != null) {
                msgBytes = msgData.getBytes(1, (int) msgData.length());
                ByteArrayInputStream xmlContentBytes = new ByteArrayInputStream(msgBytes);
                JAXBContext context = JAXBContext.newInstance("gov.hhs.fha.nhinc.common.nhinccommonentity");
                Unmarshaller u = context.createUnmarshaller();
                JAXBElement<RespondingGatewayCrossGatewayRetrieveRequestType> root = (JAXBElement<RespondingGatewayCrossGatewayRetrieveRequestType>) u.unmarshal(xmlContentBytes);
                respondingGatewayCrossGatewayRetrieveRequestType = root.getValue();
                log.debug("End EntityDocRetrieveDeferredReqQueueProcessOrchImpl.extractRespondingGatewayQueryRequestType()..");
            }
        } catch (Exception e) {
            log.error("Exception during Blob conversion :" + e.getMessage());
            e.printStackTrace();
        }
        return respondingGatewayCrossGatewayRetrieveRequestType;
    }

    /**
     *
     * @param request
     * @return String
     */
    private String extractSenderOID(RetrieveDocumentSetRequestType request) {
        String oid = null;

        if (request != null &&
                request.getDocumentRequest() != null &&
                request.getDocumentRequest().get(0) != null &&
                request.getDocumentRequest().get(0).getHomeCommunityId() != null) {
            oid = request.getDocumentRequest().get(0).getHomeCommunityId();
        }

        return oid;
    }
}
