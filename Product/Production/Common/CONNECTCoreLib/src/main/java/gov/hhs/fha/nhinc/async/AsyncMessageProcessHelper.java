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
package gov.hhs.fha.nhinc.async;

import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.asyncmsgs.model.AsyncMsgRecord;
import gov.hhs.fha.nhinc.asyncmsgs.persistence.HibernateUtil;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.persistence.HibernateUtilFactory;
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
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PIXConsumerMCCIIN000002UV01RequestType;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides methods to manage the async message record during its lifecycle.
 *
 * @author richard.ettema
 */
public class AsyncMessageProcessHelper {

    private static final Logger LOG = LoggerFactory.getLogger(AsyncMessageProcessHelper.class);

    private static HashMap<String, String> statusToDirectionMap = new HashMap<>();

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
    public boolean addPatientDiscoveryRequest(final PRPAIN201305UV02 request, final AssertionType assertion,
            final String direction) {
        LOG.debug("Begin AsyncMessageProcessHelper.addPatientDiscoveryRequest(assertion)...");

        final RespondingGatewayPRPAIN201305UV02RequestType newRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
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
    public boolean addPatientDiscoveryRequest(final RespondingGatewayPRPAIN201305UV02RequestType request,
            final String direction) {
        LOG.debug("Begin AsyncMessageProcessHelper.addPatientDiscoveryRequest()...");

        boolean result = false;

        try {
            final List<AsyncMsgRecord> asyncMsgRecs = new ArrayList<>();
            final AsyncMsgRecord rec = new AsyncMsgRecord();
            final AsyncMsgRecordDao instance = createAsyncMsgRecordDao();

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
        } catch (final Exception e) {
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
    public boolean processAck(final String messageId, String newStatus, final String errorStatus,
            final MCCIIN000002UV01 ack) {
        LOG.debug("Begin AsyncMessageProcessHelper.processAck()...");

        boolean result = false;

        try {
            if (isAckError(ack)) {
                newStatus = errorStatus;
            }
            final AsyncMsgRecordDao instance = createAsyncMsgRecordDao();

            final String direction = getInitialDirectionFromStatus(newStatus);
            final List<AsyncMsgRecord> records = instance.queryByMessageIdAndDirection(messageId, direction);
            if (records != null && !records.isEmpty()) {
                records.get(0).setStatus(newStatus);
                records.get(0).setAckData(getBlobFromMCCIIN000002UV01(ack));
                instance.save(records.get(0));
            }

            // Success if we got this far
            result = true;
        } catch (final Exception e) {
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
    public boolean processMessageStatus(final String messageId, final String newStatus) {
        LOG.debug("Begin AsyncMessageProcessHelper.processMessageStatus()...");

        boolean result = false;

        try {
            final AsyncMsgRecordDao instance = createAsyncMsgRecordDao();
            final String direction = getInitialDirectionFromStatus(newStatus);

            final List<AsyncMsgRecord> records = instance.queryByMessageIdAndDirection(messageId, direction);
            if (records != null && !records.isEmpty()) {
                records.get(0).setStatus(newStatus);
                instance.save(records.get(0));
            }

            // Success if we got this far
            result = true;
        } catch (final Exception e) {
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
    public boolean processPatientDiscoveryResponse(final String messageId, String newStatus, final String errorStatus,
            final RespondingGatewayPRPAIN201306UV02RequestType response) {
        LOG.debug("Begin AsyncMessageProcessHelper.processPatientDiscoveryResponse()...");

        boolean result = false;

        try {
            if (response == null) {
                newStatus = errorStatus;
            }
            final AsyncMsgRecordDao instance = createAsyncMsgRecordDao();
            final String direction = getInitialDirectionFromStatus(newStatus);

            final List<AsyncMsgRecord> records = instance.queryByMessageIdAndDirection(messageId, direction);
            if (records != null && !records.isEmpty()) {
                records.get(0).setResponseTime(new Date());

                // Calculate the duration in milliseconds
                Long duration = records.get(0).getResponseTime().getTime() - records.get(0).getCreationTime().getTime();
                records.get(0).setDuration(duration);

                records.get(0).setStatus(newStatus);
                records.get(0).setRspData(getBlobFromPRPAIN201306UV02RequestType(response));
                instance.save(records.get(0));

                // Success if we got this far
                result = true;
            }
        } catch (final Exception e) {
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
    public AssertionType copyAssertionTypeObject(final AssertionType orig) {
        AssertionType copy = null;
        ByteArrayOutputStream baOutStrm = null;
        ByteArrayInputStream baInStrm = null;

        try {
            final JAXBUnmarshallingUtil util = new JAXBUnmarshallingUtil();
            final JAXBContextHandler oHandler = new JAXBContextHandler();
            final JAXBContext jc = oHandler.getJAXBContext("gov.hhs.fha.nhinc.common.nhinccommon");
            final Marshaller marshaller = jc.createMarshaller();

            baOutStrm = new ByteArrayOutputStream();
            final gov.hhs.fha.nhinc.common.nhinccommon.ObjectFactory factory = new gov.hhs.fha.nhinc.common.nhinccommon.ObjectFactory();
            final JAXBElement<AssertionType> oJaxbElement = factory.createAssertion(orig);

            marshaller.marshal(oJaxbElement, baOutStrm);
            final byte[] buffer = baOutStrm.toByteArray();

            final Unmarshaller unmarshaller = jc.createUnmarshaller();
            baInStrm = new ByteArrayInputStream(buffer);
            final JAXBElement<AssertionType> oJaxbElementCopy = (JAXBElement<AssertionType>) unmarshaller
                    .unmarshal(util.getSafeStreamReaderFromInputStream(baInStrm));
            copy = oJaxbElementCopy.getValue();

        } catch (final JAXBException e) {
            LOG.error("Exception during copyAssertionTypeObject conversion :" + e, e);
        } catch (final XMLStreamException e) {
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
    public String marshalAssertionTypeObject(final AssertionType assertion) {
        String returnValue = "";

        try {
            final JAXBContextHandler oHandler = new JAXBContextHandler();
            final JAXBContext jc = oHandler.getJAXBContext("gov.hhs.fha.nhinc.common.nhinccommon");
            final Marshaller marshaller = jc.createMarshaller();
            final ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            final gov.hhs.fha.nhinc.common.nhinccommon.ObjectFactory factory = new gov.hhs.fha.nhinc.common.nhinccommon.ObjectFactory();
            final JAXBElement<AssertionType> oJaxbElement = factory.createAssertion(assertion);
            baOutStrm.close();
            marshaller.marshal(oJaxbElement, baOutStrm);
            final byte[] buffer = baOutStrm.toByteArray();
            returnValue = StringUtil.convertToStringUTF8(buffer);
        } catch (final JAXBException je) {
            LOG.error("Exception during marshalAssertionTypeObject conversion :" + je, je);
        } catch (final IOException e) {
            LOG.error("Exception during marshalAssertionTypeObject conversion :" + e, e);
        }

        return returnValue;
    }

    private static String getInitialDirectionFromStatus(final String status) {
        String direction = null;

        if (statusToDirectionMap.containsKey(status)) {
            direction = statusToDirectionMap.get(status);
        }

        return direction;
    }

    private Blob getBlobFromMCCIIN000002UV01(final MCCIIN000002UV01 ack) {
        Blob asyncMessage = null; // Not Implemented
        Session session = null;
        try {
            final JAXBContextHandler oHandler = new JAXBContextHandler();
            final JAXBContext jc = oHandler.getJAXBContext("org.hl7.v3");
            final Marshaller marshaller = jc.createMarshaller();
            final ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            final org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
            final PIXConsumerMCCIIN000002UV01RequestType request = factory
                    .createPIXConsumerMCCIIN000002UV01RequestType();
            request.setMCCIIN000002UV01(ack);
            final JAXBElement<PIXConsumerMCCIIN000002UV01RequestType> oJaxbElement = factory
                    .createPIXConsumerMCCIIN000002UV01Request(request);
            baOutStrm.close();
            marshaller.marshal(oJaxbElement, baOutStrm);
            final byte[] buffer = baOutStrm.toByteArray();

            session = getSession();
            if (session != null) {
                asyncMessage = session.getLobHelper().createBlob(buffer);
            }
        } catch (final Exception e) {
            LOG.error("Exception during Blob conversion : {}", e.getMessage(), e);
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (HibernateException e) {
                    LOG.error("Exception while closing the session: {}", e.getMessage(), e);
                }
            }
        }

        return asyncMessage;
    }

    private Blob getBlobFromPRPAIN201305UV02RequestType(final RespondingGatewayPRPAIN201305UV02RequestType request) {
        Blob asyncMessage = null; // Not Implemented
        Session session = null;

        try {
            final JAXBContextHandler oHandler = new JAXBContextHandler();
            final JAXBContext jc = oHandler.getJAXBContext("org.hl7.v3");
            final Marshaller marshaller = jc.createMarshaller();
            final ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            final org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
            final JAXBElement<RespondingGatewayPRPAIN201305UV02RequestType> oJaxbElement = factory
                    .createRespondingGatewayPRPAIN201305UV02Request(request);
            baOutStrm.close();
            marshaller.marshal(oJaxbElement, baOutStrm);
            final byte[] buffer = baOutStrm.toByteArray();
            session = getSession();
            asyncMessage = session.getLobHelper().createBlob(buffer);

        } catch (final Exception e) {
            LOG.error("Exception during Blob conversion :" + e.getMessage(), e);
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (HibernateException e) {
                    LOG.error("Exception while closing the session: {}", e.getMessage(), e);
                }
            }
        }

        return asyncMessage;
    }

    private Blob getBlobFromPRPAIN201306UV02RequestType(final RespondingGatewayPRPAIN201306UV02RequestType request) {
        Blob asyncMessage = null; // Not Implemented
        Session session = null;

        try {
            final JAXBContextHandler oHandler = new JAXBContextHandler();
            final JAXBContext jc = oHandler.getJAXBContext("org.hl7.v3");
            final Marshaller marshaller = jc.createMarshaller();
            final ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            final org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
            final JAXBElement<RespondingGatewayPRPAIN201306UV02RequestType> oJaxbElement = factory
                    .createRespondingGatewayPRPAIN201306UV02Request(request);
            baOutStrm.close();
            marshaller.marshal(oJaxbElement, baOutStrm);
            final byte[] buffer = baOutStrm.toByteArray();
            session = getSession();
            asyncMessage = session.getLobHelper().createBlob(buffer);

        } catch (final Exception e) {
            LOG.error("Exception during Blob conversion :" + e.getMessage(), e);
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (HibernateException e) {
                    LOG.error("Exception while closing the session: {}", e.getMessage(), e);
                }
            }
        }

        return asyncMessage;
    }

    private boolean isAckError(final MCCIIN000002UV01 ack) {
        boolean result = false;

        boolean ackExists = ack != null && ack.getAcknowledgement() != null && !ack.getAcknowledgement().isEmpty();
        boolean ackIsError = ackExists && ack.getAcknowledgement().get(0).getTypeCode() != null
                && ack.getAcknowledgement().get(0).getTypeCode().getCode() != null
                && ack.getAcknowledgement().get(0).getTypeCode().getCode().equals(HL7AckTransforms.ACK_TYPE_CODE_ERROR);

        if (ackExists && ackIsError) {
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
    private String getPatientDiscoveryMessageCommunityId(
            final RespondingGatewayPRPAIN201305UV02RequestType requestMessage, final String direction) {
        String communityId = "";
        boolean useReceiver = false;

        if (requestMessage != null && direction != null) {
            if (direction.equals(AsyncMsgRecordDao.QUEUE_DIRECTION_OUTBOUND)) {
                useReceiver = true;
            }

            if (useReceiver) {
                if (requestMessage.getPRPAIN201305UV02() != null
                        && requestMessage.getPRPAIN201305UV02().getReceiver() != null
                        && !requestMessage.getPRPAIN201305UV02().getReceiver().isEmpty()
                        && requestMessage.getPRPAIN201305UV02().getReceiver().get(0) != null
                        && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice() != null
                        && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getAsAgent() != null
                        && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getAsAgent()
                                .getValue() != null
                        && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getAsAgent().getValue()
                                .getRepresentedOrganization() != null
                        && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getAsAgent().getValue()
                                .getRepresentedOrganization().getValue() != null
                        && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getAsAgent().getValue()
                                .getRepresentedOrganization().getValue().getId() != null
                        && !requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getAsAgent()
                                .getValue().getRepresentedOrganization().getValue().getId().isEmpty()
                        && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getAsAgent().getValue()
                                .getRepresentedOrganization().getValue().getId().get(0).getRoot() != null) {
                    communityId = requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getAsAgent()
                            .getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
                }
                // If represented organization is empty or null, check the device id
                if (communityId == null || communityId.isEmpty()) {
                    if (requestMessage.getPRPAIN201305UV02().getReceiver() != null
                            && !requestMessage.getPRPAIN201305UV02().getReceiver().isEmpty()
                            && requestMessage.getPRPAIN201305UV02().getReceiver().get(0) != null
                            && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice() != null
                            && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getId() != null
                            && !requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getId().isEmpty()
                            && requestMessage.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getId()
                                    .get(0) != null
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
                        && !requestMessage.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue()
                                .getRepresentedOrganization().getValue().getId().isEmpty()
                        && requestMessage.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue()
                                .getRepresentedOrganization().getValue().getId().get(0).getRoot() != null) {
                    communityId = requestMessage.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue()
                            .getRepresentedOrganization().getValue().getId().get(0).getRoot();
                }
                // If represented organization is empty or null, check the device id
                if (communityId == null || communityId.isEmpty()) {
                    if (requestMessage.getPRPAIN201305UV02().getSender() != null
                            && requestMessage.getPRPAIN201305UV02().getSender().getDevice() != null
                            && requestMessage.getPRPAIN201305UV02().getSender().getDevice().getId() != null
                            && !requestMessage.getPRPAIN201305UV02().getSender().getDevice().getId().isEmpty()
                            && requestMessage.getPRPAIN201305UV02().getSender().getDevice().getId().get(0) != null
                            && requestMessage.getPRPAIN201305UV02().getSender().getDevice().getId().get(0)
                                    .getRoot() != null) {
                        communityId = requestMessage.getPRPAIN201305UV02().getSender().getDevice().getId().get(0)
                                .getRoot();
                    }
                }
            }
        }

        return communityId;
    }

    protected Session getSession() {
        Session session = null;
        HibernateUtil util = HibernateUtilFactory.getAsyncMsgsHibernateUtil();
        if (util != null) {
            session = util.getSessionFactory().openSession();
        } else {
            LOG.error("Session is null");
        }
        return session;
    }

}
