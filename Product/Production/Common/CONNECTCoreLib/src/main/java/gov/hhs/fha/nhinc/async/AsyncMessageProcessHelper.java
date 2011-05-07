/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.async;

import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.asyncmsgs.model.AsyncMsgRecord;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PIXConsumerMCCIIN000002UV01RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;

/**
 * This class provides methods to manage the async message record during its lifecycle.
 *
 * @author richard.ettema
 */
public class AsyncMessageProcessHelper {

    private static Log log = LogFactory.getLog(AsyncMessageProcessHelper.class);

    /**
     * Used to add the Deferred Patient Discovery Request to the local gateway
     * asyncmsgs repository.  The direction indicates the role of the local
     * gateway; i.e. outbound == initiator, inbound == receiver/responder
     *
     * @param request
     * @param direction
     * @return true - success; false - error
     */
    public boolean addPatientDiscoveryRequest(RespondingGatewayPRPAIN201305UV02RequestType request, String direction) {
        log.debug("Begin AsyncMessageProcessHelper.addPatientDiscoveryRequest()...");

        boolean result = false;

        try {
            List<AsyncMsgRecord> asyncMsgRecs = new ArrayList<AsyncMsgRecord>();
            AsyncMsgRecord rec = new AsyncMsgRecord();
            AsyncMsgRecordDao instance = new AsyncMsgRecordDao();

            // Replace with message id from the assertion class
            rec.setMessageId(request.getAssertion().getMessageId());
            rec.setCreationTime(new Date());
            rec.setServiceName(NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME);
            rec.setDirection(direction);
            if (request.getPRPAIN201305UV02() != null &&
                    request.getPRPAIN201305UV02().getSender() != null &&
                    request.getPRPAIN201305UV02().getSender().getDevice() != null &&
                    request.getPRPAIN201305UV02().getSender().getDevice().getAsAgent() != null &&
                    request.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue() != null &&
                    request.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
                    request.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
                    request.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId() != null &&
                    request.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().size() > 0 &&
                    request.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null &&
                    request.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot() != null) {
                rec.setCommunityId(request.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot());
            }
            rec.setStatus(AsyncMsgRecordDao.QUEUE_STATUS_REQPROCESS);

            rec.setMsgData(getBlobFromPRPAIN201305UV02RequestType(request));

            asyncMsgRecs.add(rec);

            result = instance.insertRecords(asyncMsgRecs);

            if (result == false) {
                log.error("Failed to insert asynchronous record in the database");
            }
        } catch (Exception e) {
            log.error("ERROR: Failed to add the async request to async msg repository.", e);
        }

        log.debug("End AsyncMessageProcessHelper.addPatientDiscoveryRequest()...");

        return result;
    }

    /**
     * Process an acknowledgement error for the asyncmsgs record
     *
     * @param messageId
     * @param newStatus
     * @param ack
     * @return true - success; false - error
     */
    public boolean processAck(String messageId, String newStatus, String errorStatus, MCCIIN000002UV01 ack) {
        log.debug("Begin AsyncMessageProcessHelper.processAck()...");

        boolean result = false;

        try {
            if (isAckError(ack)) {
                newStatus = errorStatus;
            }
            AsyncMsgRecordDao instance = new AsyncMsgRecordDao();

            List<AsyncMsgRecord> records = instance.queryByMessageId(messageId);
            if (records != null && records.size() > 0) {
                records.get(0).setStatus(newStatus);
                records.get(0).setAckData(getBlobFromMCCIIN000002UV01(ack));
                instance.save(records.get(0));
            }

            // Success if we got this far
            result = true;
        } catch (Exception e) {
            log.error("ERROR: Failed to update the async request.", e);
        }

        log.debug("End AsyncMessageProcessHelper.processAck()...");

        return result;
    }

    /**
     * Process the new status for the asyncmsgs record
     * 
     * @param messageId
     * @param newStatus
     * @return true - success; false - error
     */
    public boolean processMessageStatus(String messageId, String newStatus) {
        log.debug("Begin AsyncMessageProcessHelper.processMessageStatus()...");

        boolean result = false;

        try {
            AsyncMsgRecordDao instance = new AsyncMsgRecordDao();

            List<AsyncMsgRecord> records = instance.queryByMessageId(messageId);
            if (records != null && records.size() > 0) {
                records.get(0).setStatus(newStatus);
                instance.save(records.get(0));
            }

            // Success if we got this far
            result = true;
        } catch (Exception e) {
            log.error("ERROR: Failed to update the async request status.", e);
        }

        log.debug("End AsyncMessageProcessHelper.processMessageStatus()...");

        return result;
    }

    private Blob getBlobFromMCCIIN000002UV01(MCCIIN000002UV01 ack) {
        Blob asyncMessage = null; //Not Implemented

        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("org.hl7.v3");
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
            PIXConsumerMCCIIN000002UV01RequestType request = factory.createPIXConsumerMCCIIN000002UV01RequestType();
            request.setMCCIIN000002UV01(ack);
            JAXBElement<PIXConsumerMCCIIN000002UV01RequestType> oJaxbElement = factory.createPIXConsumerMCCIIN000002UV01Request(request);
            baOutStrm.close();
            marshaller.marshal(oJaxbElement, baOutStrm);
            byte[] buffer = baOutStrm.toByteArray();
            asyncMessage = Hibernate.createBlob(buffer);
        } catch (Exception e) {
            log.error("Exception during Blob conversion :" + e.getMessage());
            e.printStackTrace();
        }

        return asyncMessage;
    }

    private Blob getBlobFromPRPAIN201305UV02RequestType(RespondingGatewayPRPAIN201305UV02RequestType request) {
        Blob asyncMessage = null; //Not Implemented

        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("org.hl7.v3");
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
            JAXBElement<RespondingGatewayPRPAIN201305UV02RequestType> oJaxbElement = factory.createRespondingGatewayPRPAIN201305UV02Request(request);
            baOutStrm.close();
            marshaller.marshal(oJaxbElement, baOutStrm);
            byte[] buffer = baOutStrm.toByteArray();
            asyncMessage = Hibernate.createBlob(buffer);
        } catch (Exception e) {
            log.error("Exception during Blob conversion :" + e.getMessage());
            e.printStackTrace();
        }

        return asyncMessage;
    }

    private boolean isAckError(MCCIIN000002UV01 ack) {
        boolean result = false;

        if (ack.getAcknowledgement() != null &&
                ack.getAcknowledgement().size() > 0 &&
                ack.getAcknowledgement().get(0).getTypeCode() != null &&
                ack.getAcknowledgement().get(0).getTypeCode().getCode() != null &&
                ack.getAcknowledgement().get(0).getTypeCode().getCode().equals(HL7AckTransforms.ACK_TYPE_CODE_ERROR)) {
            result = true;
        }

        return result;
    }

}
