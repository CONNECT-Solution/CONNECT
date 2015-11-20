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
package gov.hhs.fha.nhinc.auditrepository.nhinc;

import gov.hhs.fha.nhinc.common.auditlog.LogEventSecureRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType;
import gov.hhs.fha.nhinc.auditrepository.hibernate.AuditRepositoryDAO;
import gov.hhs.fha.nhinc.auditrepository.hibernate.AuditRepositoryRecord;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.fha.nhinc.util.JAXBUnmarshallingUtil;
import gov.hhs.fha.nhinc.util.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType;
import com.services.nhinc.schema.auditmessage.FindAuditEventsType;
import com.services.nhinc.schema.auditmessage.ObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.util.MessageGeneratorUtils;
import java.io.IOException;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

/**
 *
 * @author mflynn02
 */
public class AuditRepositoryOrchImpl {

    private static final Logger LOG = Logger.getLogger(AuditRepositoryOrchImpl.class);
    private static final AuditRepositoryDAO auditLogDao = AuditRepositoryDAO.getAuditRepositoryDAOInstance();
    private static String logStatus = "";

    /**
     * constructor.
     */
    public AuditRepositoryOrchImpl() {
        LOG.trace("AuditRepositoryOrchImpl Initialized");
    }

    /**
     * This method is the actual implementation method for AuditLogMgr Service to Log the AuditEvents and responses the
     * status of logging.
     *
     * @param mess the message
     * @param assertion the assertion
     * @return AcknowledgementType
     */
    public AcknowledgementType logAudit(LogEventSecureRequestType mess, AssertionType assertion) {

        AcknowledgementType response = new AcknowledgementType();
        List<AuditRepositoryRecord> auditRecList = new ArrayList<>();
        auditRecList.add(createDBAuditObj(mess, assertion));
        LOG.trace("AuditRepositoryOrchImpl.logAudit() -- Calling auditLogDao to insert record into database.");
        boolean result = auditLogDao.insertAuditRepository(auditRecList);
        LOG.trace("AuditRepositoryOrchImpl.logAudit() -- Done calling auditLogDao to insert record into database.");

        if (result) {
            response.setMessage("Created Log Message in Database...");
        } else {
            response.setMessage("Unable to create Log Message in Database...");
        }

        return response;
    }

    private Blob getBlobFromAuditMessage(com.services.nhinc.schema.auditmessage.AuditMessageType mess) {
        Blob eventMessage = null; // Not Implemented
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("com.services.nhinc.schema.auditmessage");
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            ObjectFactory factory = new ObjectFactory();
            JAXBElement<AuditMessageType> oJaxbElement = factory.createAuditMessage(mess);
            baOutStrm.close();
            marshaller.marshal(oJaxbElement, baOutStrm);
            byte[] buffer = baOutStrm.toByteArray();
            eventMessage = Hibernate.createBlob(buffer);
        } catch (JAXBException | IOException e) {
            LOG.error("Exception during Blob conversion :" + e.getLocalizedMessage(), e);
        }
        return eventMessage;
    }

    /**
     * This is the actual implementation for AuditLogMgr Service for AuditQuery returns the AuditEventsReponse.
     *
     * @param query the query
     * @param assertion the assertion
     * @return the found FindAuditEventsResponseType
     */
    public FindCommunitiesAndAuditEventsResponseType findAudit(FindAuditEventsType query, AssertionType assertion) {

        if (logStatus.equals("")) {
            logStatus = "on";
        }

        if (logStatus.equalsIgnoreCase("off")) {
            LOG.info("Enable Audit Logging Before Making Query by changing the "
                + "value in 'auditlogchoice' properties file");
            return null;
        }
        FindCommunitiesAndAuditEventsResponseType auditEvents = new FindCommunitiesAndAuditEventsResponseType();
        String patientId = query.getPatientId();
        String userId = query.getUserId();
        Date beginDate = null;
        Date endDate = null;
        XMLGregorianCalendar xmlBeginDate = query.getBeginDateTime();
        XMLGregorianCalendar xmlEndDate = query.getEndDateTime();

        if (xmlBeginDate != null) {
            beginDate = convertXMLGregorianCalendarToDate(xmlBeginDate);
        }
        if (xmlEndDate != null) {
            endDate = convertXMLGregorianCalendarToDate(xmlEndDate);
        }

        List<AuditRepositoryRecord> responseList = auditLogDao.queryAuditRepositoryOnCriteria(userId, patientId,
            beginDate, endDate);
        LOG.debug("after query call to logDAO.");
        LOG.debug("responseList is not NULL ");
        auditEvents = buildAuditReponseType(responseList);

        return auditEvents;
    }

    protected final AuditRepositoryRecord createDBAuditObj(LogEventSecureRequestType mess, AssertionType assertion) {
        AuditRepositoryRecord auditRec = new AuditRepositoryRecord();
        String eventCommunityId = mess.getRemoteHCID();
        LOG.info("auditSourceID : " + eventCommunityId);
        if (NullChecker.isNotNullish(eventCommunityId)) {
            auditRec.setRemoteHcid(eventCommunityId);
        } else {
            auditRec.setRemoteHcid("");
        }

        auditRec.setDirection(mess.getDirection());
        auditRec.setMessage(getBlobFromAuditMessage(mess.getAuditMessage()));

        XMLGregorianCalendar xMLCalDate = mess.getEventTimestamp();
        if (xMLCalDate != null) {
            auditRec.setEventTimeStamp(convertXMLGregorianCalendarToDate(xMLCalDate));
        }
        auditRec.setOutcome(mess.getEventOutcomeIndicator().intValue());
        auditRec.setEventType(mess.getEventType());
        auditRec.setEventId(mess.getEventID());
        auditRec.setMessageId(MessageGeneratorUtils.getInstance().generateMessageId(assertion));
        auditRec.setRelatesTo(mess.getRelatesTo());
        auditRec.setUserId(mess.getUserId());

        return auditRec;
    }

    /**
     * This method builds the Actual Response from each of the EventLogList coming from Database.
     *
     * @param eventsList
     * @return CommunitiesAndFindAdutiEventResponse
     */
    private FindCommunitiesAndAuditEventsResponseType buildAuditReponseType(List<AuditRepositoryRecord> auditRecList) {

        FindCommunitiesAndAuditEventsResponseType auditResType = new FindCommunitiesAndAuditEventsResponseType();
        FindAuditEventsResponseType response = new FindAuditEventsResponseType();
        AuditMessageType auditMessageType;
        Blob blobMessage;

        int size = auditRecList.size();
        AuditRepositoryRecord eachRecord;
        for (int i = 0; i < size; i++) {
            eachRecord = auditRecList.get(i);
            auditMessageType = new AuditMessageType();
            blobMessage = eachRecord.getMessage();
            if (blobMessage != null) {
                try {
                    auditMessageType = unMarshallBlobToAuditMess(blobMessage);
                    response.getFindAuditEventsReturn().add(auditMessageType);

                    if (auditMessageType.getAuditSourceIdentification().size() > 0
                        && auditMessageType.getAuditSourceIdentification().get(0) != null
                        && auditMessageType.getAuditSourceIdentification().get(0).getAuditSourceID() != null
                        && auditMessageType.getAuditSourceIdentification().get(0).getAuditSourceID().length() > 0) {
                        String tempCommunity = auditMessageType.getAuditSourceIdentification().get(0).getAuditSourceID();
                        if (!auditResType.getCommunities().contains(tempCommunity)) {
                            auditResType.getCommunities().add(tempCommunity);
                            LOG.debug("Adding community " + tempCommunity);
                        }
                    }
                } finally {
                    try {
                        blobMessage.free();
                    } catch (SQLException e) {
                        LOG.error("Could not free Blob: " + e.getMessage());
                    }
                }
            }
        }

        auditResType.setFindAuditEventResponse(response);
        return auditResType;
    }

    /**
     * This method unmarshalls XML Blob to AuditMessage
     *
     * @param auditBlob
     * @return AuditMessageType
     */
    private AuditMessageType unMarshallBlobToAuditMess(Blob auditBlob) {

        AuditMessageType auditMessageType = null;
        InputStream in = null;
        try {
            if (auditBlob != null && ((int) auditBlob.length()) > 0) {
                JAXBUnmarshallingUtil util = new JAXBUnmarshallingUtil();
                in = auditBlob.getBinaryStream();
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext("com.services.nhinc.schema.auditmessage");
                Unmarshaller unmarshaller = jc.createUnmarshaller();
                JAXBElement jaxEle = (JAXBElement) unmarshaller.unmarshal(util.getSafeStreamReaderFromInputStream(in));
                auditMessageType = (AuditMessageType) jaxEle.getValue();
            }
        } catch (SQLException | JAXBException | XMLStreamException e) {
            LOG.error("Blob to Audit Message Conversion Error : " + e.getLocalizedMessage(), e);
        } finally {
            StreamUtils.closeStreamSilently(in);
        }

        return auditMessageType;
    }

    /**
     * This method converts an XMLGregorianCalendar date to java.util.Date
     *
     * @param xmlCalDate
     * @return java.util.Date
     */
    private Date convertXMLGregorianCalendarToDate(XMLGregorianCalendar xmlCalDate) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        LOG.info("cal.getTime() -> " + cal.getTime());
        cal.setTime(xmlCalDate.toGregorianCalendar().getTime());
        Date eventDate = cal.getTime();
        LOG.info("eventDate -> " + eventDate);
        return eventDate;
    }
}
