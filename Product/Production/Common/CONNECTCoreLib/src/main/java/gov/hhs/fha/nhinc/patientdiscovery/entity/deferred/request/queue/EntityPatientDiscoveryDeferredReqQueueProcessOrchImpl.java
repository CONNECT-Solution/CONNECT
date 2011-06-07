/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.queue;

import gov.hhs.fha.nhinc.async.AsyncMessageProcessHelper;
import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.asyncmsgs.model.AsyncMsgRecord;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.queue.proxy.EntityPatientDiscoveryDeferredReqQueueProxyJavaImpl;
import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author mastan.ketha
 */
public class EntityPatientDiscoveryDeferredReqQueueProcessOrchImpl {

    private static Log log = LogFactory.getLog(EntityPatientDiscoveryDeferredReqQueueProcessOrchImpl.class);

    public EntityPatientDiscoveryDeferredReqQueueProcessOrchImpl() {
    }

    protected AsyncMessageProcessHelper createAsyncProcesser() {
        return new AsyncMessageProcessHelper();
    }

    /**
     * processPatientDiscoveryDeferredReqQueue Orchestration method for processing request queues on reponding gateway
     * @param messageId
     * @return org.hl7.v3.MCCIIN000002UV01
     */
    public MCCIIN000002UV01 processPatientDiscoveryDeferredReqQueue(String messageId) {
        log.info("Begin EntityPatientDiscoveryDeferredReqQueueProcessOrchImpl.processPatientDiscoveryAsyncReq()....");
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();
        try {
            RespondingGatewayPRPAIN201305UV02RequestType respondingGatewayPRPAIN201305UV02RequestType = new RespondingGatewayPRPAIN201305UV02RequestType();

            AsyncMsgRecord asyncMsgRecord = new AsyncMsgRecord();
            AsyncMsgRecordDao instance = new AsyncMsgRecordDao();
            log.info("messageId: " + messageId);
            if ((messageId != null)) {
                List<AsyncMsgRecord> msgList = new ArrayList<AsyncMsgRecord>();
                msgList = instance.queryByMessageId(messageId);
                if ((msgList != null) &&
                        (msgList.size() > 0)) {
                    log.info("msgList: " + msgList.size());
                    asyncMsgRecord = msgList.get(0);

                    // Set queue status to processing
                    asyncMsgRecord.setStatus(AsyncMsgRecordDao.QUEUE_STATUS_RSPPROCESS);
                    instance.save(asyncMsgRecord);
                } else {
                    log.info("msgList: is null");
                }

            } else {
                log.info("messageId: is null");
            }

            log.info("AsyncMsgRecord - messageId: " + asyncMsgRecord.getMessageId());
            log.info("AsyncMsgRecord - serviceName: " + asyncMsgRecord.getServiceName());
            log.info("AsyncMsgRecord - creationTime: " + asyncMsgRecord.getCreationTime());

            EntityPatientDiscoveryDeferredReqQueueProxyJavaImpl entityPatientDiscoveryDeferredReqQueueProxyJavaImpl = new EntityPatientDiscoveryDeferredReqQueueProxyJavaImpl();

            if (asyncMsgRecord.getMsgData() != null) {
                respondingGatewayPRPAIN201305UV02RequestType = extractRespondingGatewayPRPAIN201305UV02RequestType(asyncMsgRecord.getMsgData());
            }

            if (respondingGatewayPRPAIN201305UV02RequestType != null) {
                log.info("AsyncMsgRecord - messageId: " + respondingGatewayPRPAIN201305UV02RequestType.getPRPAIN201305UV02().getITSVersion());

                PRPAIN201305UV02 pRPAIN201305UV02 = respondingGatewayPRPAIN201305UV02RequestType.getPRPAIN201305UV02();

                String senderTargetCommId = extractSenderOID(pRPAIN201305UV02);
                if (senderTargetCommId != null) {
                    log.info("senderTargetCommId: " + senderTargetCommId);

                    NhinTargetCommunitiesType targetCommunities = new NhinTargetCommunitiesType();
                    NhinTargetCommunityType nhinTargetCommunityType = new NhinTargetCommunityType();
                    HomeCommunityType homeCommunityType = new HomeCommunityType();
                    homeCommunityType.setHomeCommunityId(senderTargetCommId);
                    nhinTargetCommunityType.setHomeCommunity(homeCommunityType);
                    targetCommunities.getNhinTargetCommunity().add(nhinTargetCommunityType);

                    // Generate new request queue assertion from original request message assertion
                    AssertionType assertion = respondingGatewayPRPAIN201305UV02RequestType.getAssertion();

                    ack = entityPatientDiscoveryDeferredReqQueueProxyJavaImpl.addPatientDiscoveryAsyncReq(pRPAIN201305UV02, assertion, targetCommunities);
                } else {
                    log.error("Sender root is null - Unable to extract target community hcid from sender root");
                }
            }
        } catch (Exception e) {
            log.error("ERROR: Failed in accessing the async msg from repository.", e);
        }

        return ack;
    }

    @SuppressWarnings("unchecked")
    private RespondingGatewayPRPAIN201305UV02RequestType extractRespondingGatewayPRPAIN201305UV02RequestType(Blob msgData) {
        log.debug("Begin EntityPatientDiscoveryDeferredReqQueueProcessOrchImpl.extractRespondingGatewayPRPAIN201305UV02RequestType()..");
        RespondingGatewayPRPAIN201305UV02RequestType respondingGatewayPRPAIN201305UV02RequestType = new RespondingGatewayPRPAIN201305UV02RequestType();
        try {
            byte[] msgBytes = null;
            if (msgData != null) {
                msgBytes = msgData.getBytes(1, (int) msgData.length());
                ByteArrayInputStream xmlContentBytes = new ByteArrayInputStream(msgBytes);
                JAXBContext context = JAXBContext.newInstance("org.hl7.v3");
                Unmarshaller u = context.createUnmarshaller();
                JAXBElement<RespondingGatewayPRPAIN201305UV02RequestType> root = (JAXBElement<RespondingGatewayPRPAIN201305UV02RequestType>) u.unmarshal(xmlContentBytes);

                respondingGatewayPRPAIN201305UV02RequestType = root.getValue();
                log.debug("End EntityPatientDiscoveryDeferredReqQueueProcessOrchImpl.extractRespondingGatewayPRPAIN201305UV02RequestType()..");
            }
        } catch (Exception e) {
            log.error("Exception during Blob conversion :" + e.getMessage());
            e.printStackTrace();
        }
        return respondingGatewayPRPAIN201305UV02RequestType;
    }

    private String extractSenderOID(PRPAIN201305UV02 request) {
        String oid = null;

        if (request != null &&
                request.getSender() != null &&
                request.getSender().getDevice() != null &&
                request.getSender().getDevice().getAsAgent() != null &&
                request.getSender().getDevice().getAsAgent().getValue() != null &&
                request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
                request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
                NullChecker.isNotNullish(request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
                request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null &&
                NullChecker.isNotNullish(request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
            oid = request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
        }

        return oid;
    }
}
