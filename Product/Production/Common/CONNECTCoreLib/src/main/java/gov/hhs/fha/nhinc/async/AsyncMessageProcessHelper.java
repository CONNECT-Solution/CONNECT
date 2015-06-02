/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.async;

import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.asyncmsgs.model.AsyncMsgRecord;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import gov.hhs.fha.nhinc.util.JAXBUnmarshallingUtil;
import gov.hhs.fha.nhinc.util.StreamUtils;
import gov.hhs.fha.nhinc.util.StringUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamException;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PIXConsumerMCCIIN000002UV01RequestType;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;

/**
 * This class provides methods to manage the async message record during its lifecycle.
 *
 * @author richard.ettema
 */
public class AsyncMessageProcessHelper {

    private static final Logger LOG = Logger.getLogger(AsyncMessageProcessHelper.class);

    private static HashMap<String, String> statusToDirectionMap = new HashMap<String, String>();

    static {
        statusToDirectionMap.put(AsyncMsgRecordDao.QUEUE_STATUS_REQSENT, AsyncMsgRecordDao.QUEUE_DIRECTION_OUTBOUND);
        statusToDirectionMap.put(AsyncMsgRecordDao.QUEUE_STATUS_REQSENTACK, AsyncMsgRecordDao.QUEUE_DIRECTION_OUTBOUND);
        statusToDirectionMap.put(AsyncMsgRecordDao.QUEUE_STATUS_REQSENTERR, AsyncMsgRecordDao.QUEUE_DIRECTION_OUTBOUND);
        statusToDirectionMap.put(AsyncMsgRecordDao.QUEUE_STATUS_RSPRCVD, AsyncMsgRecordDao.QUEUE_DIRECTION_OUTBOUND);
        statusToDirectionMap.put(AsyncMsgRecordDao.QUEUE_STATUS_RSPRCVDACK, AsyncMsgRecordDao.QUEUE_DIRECTION_OUTBOUND);
        statusToDirectionMap.put(AsyncMsgRecordDao.QUEUE_STATUS_RSPRCVDERR, AsyncMsgRecordDao.QUEUE_DIRECTION_OUTBOUND);

        statusToDirectionMap.put(AsyncMsgRecordDao.QUEUE_STATUS_REQRCVD, AsyncMsgRecordDao.QUEUE_DIRECTION_INBOUND);
        statusToDirectionMap.put(AsyncMsgRecordDao.QUEUE_STATUS_REQRCVDACK, AsyncMsgRecordDao.QUEUE_DIRECTION_INBOUND);
        statusToDirectionMap.put(AsyncMsgRecordDao.QUEUE_STATUS_REQRCVDERR, AsyncMsgRecordDao.QUEUE_DIRECTION_INBOUND);
        statusToDirectionMap.put(AsyncMsgRecordDao.QUEUE_STATUS_RSPSENT, AsyncMsgRecordDao.QUEUE_DIRECTION_INBOUND);
        statusToDirectionMap.put(AsyncMsgRecordDao.QUEUE_STATUS_RSPSENTACK, AsyncMsgRecordDao.QUEUE_DIRECTION_INBOUND);
        statusToDirectionMap.put(AsyncMsgRecordDao.QUEUE_STATUS_RSPSENTERR, AsyncMsgRecordDao.QUEUE_DIRECTION_INBOUND);
        statusToDirectionMap.put(AsyncMsgRecordDao.QUEUE_STATUS_RSPSELECT, AsyncMsgRecordDao.QUEUE_DIRECTION_INBOUND);
    }

    /**
     * Creates a AsyncMsgRecordDao.
     *
     * @return an instance of AsyncMsgRecordDao
     */
    protected AsyncMsgRecordDao createAsyncMsgRecordDao() {
        return new AsyncMsgRecordDao();
    }

    /**
     * Used to add the Deferred Patient Discovery Request to the local gateway asyncmsgs repository. The direction
     * indicates the role of the local gateway; i.e. outbound == initiator, inbound == receiver/responder
     *
     * @param request - The Request
     * @param assertion - The assertion
     * @param direction - The direction
     * @return true - success; false - error
     */
    public boolean addPatientDiscoveryRequest(PRPAIN201305UV02 request, AssertionType assertion, String direction) {
        LOG.debug("Begin AsyncMessageProcessHelper.addPatientDiscoveryRequest(assertion)...");

        RespondingGatewayPRPAIN201305UV02RequestType newRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
        newRequest.setAssertion(assertion);
        newRequest.setPRPAIN201305UV02(request);

        LOG.debug("End AsyncMessageProcessHelper.addPatientDiscoveryRequest(assertion)...");

        return addPatientDiscoveryRequest(newRequest, direction);
    }

    /**
     * Used to add the Deferred Patient Discovery Request to the local gateway asyncmsgs repository. The direction
     * indicates the role of the local gateway; i.e. outbound == initiator, inbound == receiver/responder
     *
     * @param request
     * @param direction
     * @return true - success; false - error
     */
    public boolean addPatientDiscoveryRequest(RespondingGatewayPRPAIN201305UV02RequestType request, String direction) {
        LOG.debug("Begin AsyncMessageProcessHelper.addPatientDiscoveryRequest()...");

        boolean result = false;

        try {
            List<AsyncMsgRecord> asyncMsgRecs = new ArrayList<AsyncMsgRecord>();
            AsyncMsgRecord rec = new AsyncMsgRecord();
            AsyncMsgRecordDao instance = createAsyncMsgRecordDao();

            // Replace with message id from the assertion class
            rec.setMessageId(request.getAssertion().getMessageId());
            rec.setCreationTime(new Date());
            rec.setServiceName(NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME);
            rec.setDirection(direction);
            rec.setCommunityId(getPatientDiscoveryMessageCommunityId(request, direction));
            rec.setStatus(AsyncMsgRecordDao.QUEUE_STATUS_REQPROCESS);
            rec.setResponseType(AsyncMsgRecordDao.QUEUE_RESPONSE_TYPE_AUTO);
            rec.setMsgData(getBlobFromPRPAIN201305UV02RequestType(request));

            asyncMsgRecs.add(rec);

            result = instance.insertRecords(asyncMsgRecs);

            if (!result) {
                LOG.error("Failed to insert asynchronous record in the database");
            }
        } catch (Exception e) {
            LOG.error("ERROR: Failed to add the async request to async msg repository.", e);
        }

        LOG.debug("End AsyncMessageProcessHelper.addPatientDiscoveryRequest()...");

        return result;
    }

    /**
     * Process an acknowledgement for a Deferred Patient Discovery asyncmsgs record.
     *
     * @param messageId
     * @param newStatus
     * @param errorStatus
     * @param ack
     * @return true - success; false - error
     */
    public boolean processAck(String messageId, String newStatus, String errorStatus, MCCIIN000002UV01 ack) {
        LOG.debug("Begin AsyncMessageProcessHelper.processAck()...");

        boolean result = false;

        try {
            if (isAckError(ack)) {
                newStatus = errorStatus;
            }
            AsyncMsgRecordDao instance = createAsyncMsgRecordDao();

            String direction = getInitialDirectionFromStatus(newStatus);
            List<AsyncMsgRecord> records = instance.queryByMessageIdAndDirection(messageId, direction);
            if (records != null && records.size() > 0) {
                records.get(0).setStatus(newStatus);
                records.get(0).setAckData(getBlobFromMCCIIN000002UV01(ack));
                instance.save(records.get(0));
            }

            // Success if we got this far
            result = true;
        } catch (Exception e) {
            LOG.error("ERROR: Failed to update the async request.", e);
        }

        LOG.debug("End AsyncMessageProcessHelper.processAck()...");

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
        LOG.debug("Begin AsyncMessageProcessHelper.processMessageStatus()...");

        boolean result = false;

        try {
            AsyncMsgRecordDao instance = createAsyncMsgRecordDao();
            String direction = getInitialDirectionFromStatus(newStatus);

            List<AsyncMsgRecord> records = instance.queryByMessageIdAndDirection(messageId, direction);
            if (records != null && records.size() > 0) {
                records.get(0).setStatus(newStatus);
                instance.save(records.get(0));
            }

            // Success if we got this far
            result = true;
        } catch (Exception e) {
            LOG.error("ERROR: Failed to update the async request status.", e);
        }

        LOG.debug("End AsyncMessageProcessHelper.processMessageStatus()...");

        return result;
    }

    /**
     * Process an acknowledgement error for the Patient Discovery asyncmsgs record
     *
     * @param messageId
     * @param newStatus
     * @param errorStatus
     * @param ack
     * @return true - success; false - error
     */
    public boolean processPatientDiscoveryResponse(String messageId, String newStatus, String errorStatus,
            RespondingGatewayPRPAIN201306UV02RequestType response) {
        LOG.debug("Begin AsyncMessageProcessHelper.processPatientDiscoveryResponse()...");

        boolean result = false;

        try {
            if (response == null) {
                newStatus = errorStatus;
            }
            AsyncMsgRecordDao instance = createAsyncMsgRecordDao();
            String direction = getInitialDirectionFromStatus(newStatus);

            List<AsyncMsgRecord> records = instance.queryByMessageIdAndDirection(messageId, direction);
            if (records != null && records.size() > 0) {
                records.get(0).setResponseTime(new Date());

                // Calculate the duration in milliseconds
                Long duration = null;
                duration = records.get(0).getResponseTime().getTime() - records.get(0).getCreationTime().getTime();
                records.get(0).setDuration(duration);

                records.get(0).setStatus(newStatus);
                records.get(0).setRspData(getBlobFromPRPAIN201306UV02RequestType(response));
                instance.save(records.get(0));

                // Success if we got this far
                result = true;
            }
        } catch (Exception e) {
            LOG.error("ERROR: Failed to update the async response.", e);
        }

        LOG.debug("End AsyncMessageProcessHelper.processPatientDiscoveryResponse()...");

        return result;
    }

    /**
     * Copy the original AssertionType using JAXB
     *
     * @param orig
     * @return copy of AssertionType
     */
    public AssertionType copyAssertionTypeObject(AssertionType orig) {
        AssertionType copy = null;
        ByteArrayOutputStream baOutStrm = null;
        ByteArrayInputStream baInStrm = null;

        try {
            JAXBUnmarshallingUtil util = new JAXBUnmarshallingUtil();
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("gov.hhs.fha.nhinc.common.nhinccommon");
            Marshaller marshaller = jc.createMarshaller();

            baOutStrm = new ByteArrayOutputStream();
            gov.hhs.fha.nhinc.common.nhinccommon.ObjectFactory factory = new gov.hhs.fha.nhinc.common.nhinccommon.ObjectFactory();
            JAXBElement<AssertionType> oJaxbElement = factory.createAssertion(orig);

            marshaller.marshal(oJaxbElement, baOutStrm);
            byte[] buffer = baOutStrm.toByteArray();

            Unmarshaller unmarshaller = jc.createUnmarshaller();
            baInStrm = new ByteArrayInputStream(buffer);
            JAXBElement<AssertionType> oJaxbElementCopy =
                    (JAXBElement<AssertionType>) unmarshaller.unmarshal(util.getSafeStreamReaderFromInputStream(baInStrm));
            copy = oJaxbElementCopy.getValue();

        } catch (JAXBException e) {
            LOG.error("Exception during copyAssertionTypeObject conversion :" + e, e);
        } catch (XMLStreamException e) {
            LOG.error("Exception during copyAssertionTypeObject conversion :" + e, e);
        } finally {
            StreamUtils.closeReaderSilently(baOutStrm);
            StreamUtils.closeStreamSilently(baInStrm);
        }

        return copy;
    }

    /**
     * Marshal AssertionType using JAXB
     *
     * @param orig
     * @return copy of AssertionType
     */
    public String marshalAssertionTypeObject(AssertionType assertion) {
        String returnValue = "";

        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("gov.hhs.fha.nhinc.common.nhinccommon");
            Marshaller marshaller = jc.createMarshaller();
            // marshaller.setProperty("jaxb.formatted.output", new Boolean(true));
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            gov.hhs.fha.nhinc.common.nhinccommon.ObjectFactory factory = new gov.hhs.fha.nhinc.common.nhinccommon.ObjectFactory();
            JAXBElement<AssertionType> oJaxbElement = factory.createAssertion(assertion);
            baOutStrm.close();
            marshaller.marshal(oJaxbElement, baOutStrm);
            byte[] buffer = baOutStrm.toByteArray();
            returnValue = StringUtil.convertToStringUTF8(buffer);
        } catch (JAXBException je) {
            LOG.error("Exception during marshalAssertionTypeObject conversion :" + je, je);
        } catch (IOException e) {
            LOG.error("Exception during marshalAssertionTypeObject conversion :" + e, e);
        }

        return returnValue;
    }

    private static String getInitialDirectionFromStatus(String status) {
        String direction = null;

        if (statusToDirectionMap.containsKey(status)) {
            direction = statusToDirectionMap.get(status);
        }

        return direction;
    }

    private Blob getBlobFromMCCIIN000002UV01(MCCIIN000002UV01 ack) {
        Blob asyncMessage = null; // Not Implemented

        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("org.hl7.v3");
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
            PIXConsumerMCCIIN000002UV01RequestType request = factory.createPIXConsumerMCCIIN000002UV01RequestType();
            request.setMCCIIN000002UV01(ack);
            JAXBElement<PIXConsumerMCCIIN000002UV01RequestType> oJaxbElement = factory
                    .createPIXConsumerMCCIIN000002UV01Request(request);
            baOutStrm.close();
            marshaller.marshal(oJaxbElement, baOutStrm);
            byte[] buffer = baOutStrm.toByteArray();
            asyncMessage = Hibernate.createBlob(buffer);
        } catch (Exception e) {
            LOG.error("Exception during Blob conversion :" + e.getMessage(), e);
        }

        return asyncMessage;
    }

    private Blob getBlobFromPRPAIN201305UV02RequestType(RespondingGatewayPRPAIN201305UV02RequestType request) {
        Blob asyncMessage = null; // Not Implemented

        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("org.hl7.v3");
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
            JAXBElement<RespondingGatewayPRPAIN201305UV02RequestType> oJaxbElement = factory
                    .createRespondingGatewayPRPAIN201305UV02Request(request);
            baOutStrm.close();
            marshaller.marshal(oJaxbElement, baOutStrm);
            byte[] buffer = baOutStrm.toByteArray();
            asyncMessage = Hibernate.createBlob(buffer);
        } catch (Exception e) {
            LOG.error("Exception during Blob conversion :" + e.getMessage(), e);
        }

        return asyncMessage;
    }

    private Blob getBlobFromPRPAIN201306UV02RequestType(RespondingGatewayPRPAIN201306UV02RequestType request) {
        Blob asyncMessage = null; // Not Implemented

        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("org.hl7.v3");
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
            JAXBElement<RespondingGatewayPRPAIN201306UV02RequestType> oJaxbElement = factory
                    .createRespondingGatewayPRPAIN201306UV02Request(request);
            baOutStrm.close();
            marshaller.marshal(oJaxbElement, baOutStrm);
            byte[] buffer = baOutStrm.toByteArray();
            asyncMessage = Hibernate.createBlob(buffer);
        } catch (Exception e) {
            LOG.error("Exception during Blob conversion :" + e.getMessage(), e);
        }

        return asyncMessage;
    }

    private boolean isAckError(MCCIIN000002UV01 ack) {
        boolean result = false;

        if (ack != null && ack.getAcknowledgement() != null && ack.getAcknowledgement().size() > 0
                && ack.getAcknowledgement().get(0).getTypeCode() != null
                && ack.getAcknowledgement().get(0).getTypeCode().getCode() != null
                && ack.getAcknowledgement().get(0).getTypeCode().getCode().equals(HL7AckTransforms.ACK_TYPE_CODE_ERROR)) {
            result = true;
        }

        return result;
    }

    /**
     * Get the home community id of the communicating gateway
     *
     * @param requestMessage
     * @param direction
     * @return String
     */
    private String getPatientDiscoveryMessageCommunityId(RespondingGatewayPRPAIN201305UV02RequestType requestMessage,
            String direction) {
        String communityId = "";
        boolean useReceiver = false;

        if (requestMessage != null && direction != null) {
            if (direction.equals(AsyncMsgRecordDao.QUEUE_DIRECTION_OUTBOUND)) {
                useReceiver = true;
            }

            if (useReceiver) {
                if (requestMessage.getPRPAIN201305UV02() != null
                        && requestMessage.getPRPAIN201305UV02().getReceiver() != null
                        && requestMessage.getPRPAIN201305UV02().getReceiver().size() > 0
                        && requestMessage.getPRPAIN201305UV02().getReceiver().get(0) != null
                        && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice() != null
                        && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getAsAgent() != null
                        && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getAsAgent()
                                .getValue() != null
                        && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getAsAgent()
                                .getValue().getRepresentedOrganization() != null
                        && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getAsAgent()
                                .getValue().getRepresentedOrganization().getValue() != null
                        && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getAsAgent()
                                .getValue().getRepresentedOrganization().getValue().getId() != null
                        && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getAsAgent()
                                .getValue().getRepresentedOrganization().getValue().getId().size() > 0
                        && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getAsAgent()
                                .getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot() != null) {
                    communityId = requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getAsAgent()
                            .getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
                }
                // If represented organization is empty or null, check the device id
                if (communityId == null || communityId.equals("")) {
                    if (requestMessage.getPRPAIN201305UV02().getReceiver() != null
                            && requestMessage.getPRPAIN201305UV02().getReceiver().size() > 0
                            && requestMessage.getPRPAIN201305UV02().getReceiver().get(0) != null
                            && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice() != null
                            && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getId() != null
                            && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getId().size() > 0
                            && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getId().get(0) != null
                            && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getId().get(0)
                                    .getRoot() != null) {
                        communityId = requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getId()
                                .get(0).getRoot();
                    }
                }
            } else {
                if (requestMessage.getPRPAIN201305UV02().getSender() != null
                        && requestMessage.getPRPAIN201305UV02().getSender().getDevice() != null
                        && requestMessage.getPRPAIN201305UV02().getSender().getDevice().getAsAgent() != null
                        && requestMessage.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue() != null
                        && requestMessage.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue()
                                .getRepresentedOrganization() != null
                        && requestMessage.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue()
                                .getRepresentedOrganization().getValue() != null
                        && requestMessage.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue()
                                .getRepresentedOrganization().getValue().getId() != null
                        && requestMessage.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue()
                                .getRepresentedOrganization().getValue().getId().size() > 0
                        && requestMessage.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue()
                                .getRepresentedOrganization().getValue().getId().get(0).getRoot() != null) {
                    communityId = requestMessage.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue()
                            .getRepresentedOrganization().getValue().getId().get(0).getRoot();
                }
                // If represented organization is empty or null, check the device id
                if (communityId == null || communityId.equals("")) {
                    if (requestMessage.getPRPAIN201305UV02().getSender() != null
                            && requestMessage.getPRPAIN201305UV02().getSender().getDevice() != null
                            && requestMessage.getPRPAIN201305UV02().getSender().getDevice() != null
                            && requestMessage.getPRPAIN201305UV02().getSender().getDevice().getId() != null
                            && requestMessage.getPRPAIN201305UV02().getSender().getDevice().getId().size() > 0
                            && requestMessage.getPRPAIN201305UV02().getSender().getDevice().getId().get(0) != null
                            && requestMessage.getPRPAIN201305UV02().getSender().getDevice().getId().get(0).getRoot() != null) {
                        communityId = requestMessage.getPRPAIN201305UV02().getSender().getDevice().getId().get(0)
                                .getRoot();
                    }
                }
            }
        }

        return communityId;
    }

}
