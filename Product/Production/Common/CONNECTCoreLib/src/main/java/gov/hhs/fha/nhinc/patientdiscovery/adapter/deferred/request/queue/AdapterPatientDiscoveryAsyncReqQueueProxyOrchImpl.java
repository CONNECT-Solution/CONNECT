/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.queue;

import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.asyncmsgs.model.AsyncMsgRecord;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;

/**
 * This class contains the business logic for the AdapterPatientDiscoveryAsyncReqQueue.
 * Currently it is assumed that this will be overridden by the adapter implementation.
 * All this does is send back an ACK.
 *
 * @author westberg
 */
public class AdapterPatientDiscoveryAsyncReqQueueProxyOrchImpl {

    private Log log = null;

    public AdapterPatientDiscoveryAsyncReqQueueProxyOrchImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    /**
     * This simulates the patient discovery async request to being added to a queue.
     * @param request The request message.
     * @return The ACK.
     */
    public MCCIIN000002UV01 addPatientDiscoveryAsyncReq(RespondingGatewayPRPAIN201305UV02RequestType request) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        String msgText = "Success";

        if (!addEntryToDatabase(request)) {
            msgText = "Failed to add the async request to async msg repository";
        }
        ack = HL7AckTransforms.createAckFrom201305(request.getPRPAIN201305UV02(), msgText);

        return ack;
    }

    private boolean addEntryToDatabase(RespondingGatewayPRPAIN201305UV02RequestType request) {
        log.debug("Begin AdapterPatientDiscoveryAsyncReqQueueProxyOrchImpl.RespondingGatewayPRPAIN201305UV02RequestType()...");
        boolean result = false;
        try {
            List<AsyncMsgRecord> asyncMsgRecs = new ArrayList<AsyncMsgRecord>();
            AsyncMsgRecord rec = new AsyncMsgRecord();
            AsyncMsgRecordDao instance = new AsyncMsgRecordDao();

            // Replace with message id from the assertion class
            rec.setMessageId(request.getAssertion().getMessageId());
            rec.setCreationTime(new Date());
            rec.setServiceName(NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME);
            rec.setDirection(AsyncMsgRecordDao.QUEUE_DIRECTION_INBOUND);
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
            rec.setStatus(AsyncMsgRecordDao.QUEUE_STATUS_REQRCVD);

            rec.setMsgData(getBlobFromPRPAIN201305UV02RequestType(request));
            asyncMsgRecs.add(rec);

            result = instance.insertRecords(asyncMsgRecs);

            if (result == false) {
                log.error("Failed to insert asynchronous record in the database");
            }
        } catch (Exception e) {
            log.error("ERROR: Failed to add the async request to async msg repository.", e);
        }

        return result;
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
}
