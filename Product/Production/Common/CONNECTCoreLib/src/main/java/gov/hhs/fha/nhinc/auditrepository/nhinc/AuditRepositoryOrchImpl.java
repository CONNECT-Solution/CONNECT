/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.auditrepository.nhinc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.text.SimpleDateFormat;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.hibernate.Hibernate;

import gov.hhs.fha.nhinc.hibernate.AuditRepositoryDAO;
import gov.hhs.fha.nhinc.hibernate.AuditRepositoryRecord;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType;

import com.services.nhinc.schema.auditmessage.*;
import com.services.nhinc.schema.auditmessage.AuditMessageType.ActiveParticipant;
import gov.hhs.fha.nhinc.common.auditlog.LogEventSecureRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author mflynn02
 */
public class AuditRepositoryOrchImpl {
    private static Log log = LogFactory.getLog(AuditRepositoryOrchImpl.class);
    private static AuditRepositoryDAO auditLogDao = AuditRepositoryDAO.getAuditRepositoryDAOInstance();
    private static String logStatus = "";

    /**
     * constructor
     */
    public AuditRepositoryOrchImpl() {
        log.debug("AuditRepositoryOrchImpl Initialized");
    }


    /**
     * This method is the actual implementation method for AuditLogMgr Service to Log the AuditEvents and responses the status of logging
     * @param mess
     * @return gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType
     */
    public AcknowledgementType logAudit(LogEventSecureRequestType mess, AssertionType assertion) {
        log.debug("AuditRepositoryOrchImpl.logAudit() -- Begin");
        AcknowledgementType response = null;


        ActiveParticipant activeParticipant = null;
        ParticipantObjectIdentificationType participantObjectIdentificationType = null;
        AuditRepositoryRecord auditRec = new AuditRepositoryRecord();

        Date eventTimeStamp = null;
        String eventCommunityId = null;
        String eventUserId = null;
        String eventParticipationIDTypeCode = null;
        String eventPatientID = null;
        int eventParticipationTypeCode = 0;
        int eventParticipationTypeCodeRole = 0;

        List<ActiveParticipant> activeParticipantList = mess.getAuditMessage().getActiveParticipant();
        EventIdentificationType eventIdentification = mess.getAuditMessage().getEventIdentification();
        List<ParticipantObjectIdentificationType> participantObjectIdentificationList = mess.getAuditMessage().getParticipantObjectIdentification();

        if (activeParticipantList != null && activeParticipantList.size() > 0) {
            activeParticipant = (ActiveParticipant) activeParticipantList.get(0);
            if (activeParticipant != null) {
                eventUserId = activeParticipant.getUserID();
                if (eventUserId != null && !eventUserId.equals("")) {
                    auditRec.setUserId(eventUserId);
                } else {
                    auditRec.setUserId("");
                }
            }
        }

        List<AuditSourceIdentificationType> auditSourceIdentificationList = null;
        auditSourceIdentificationList = mess.getAuditMessage().getAuditSourceIdentification();
        if (auditSourceIdentificationList != null && auditSourceIdentificationList.size() > 0) {
            AuditSourceIdentificationType auditSourceIdentification = auditSourceIdentificationList.get(0);
            eventCommunityId = auditSourceIdentification.getAuditSourceID();
            log.debug("auditSourceID : " + eventCommunityId);
            if (eventCommunityId != null && !eventCommunityId.equals("")) {
                auditRec.setCommunityId(eventCommunityId);
            } else {
                auditRec.setCommunityId("");
            }
        }

        if (participantObjectIdentificationList != null && participantObjectIdentificationList.size() > 0) {
            participantObjectIdentificationType = (ParticipantObjectIdentificationType) participantObjectIdentificationList.get(0);
            if (participantObjectIdentificationType != null) {
                eventPatientID = participantObjectIdentificationType.getParticipantObjectID();
                auditRec.setReceiverPatientId(eventPatientID);
                eventParticipationTypeCode = participantObjectIdentificationType.getParticipantObjectTypeCode();
                auditRec.setParticipationTypeCode(eventParticipationTypeCode);
                eventParticipationTypeCodeRole = participantObjectIdentificationType.getParticipantObjectTypeCodeRole();
                auditRec.setParticipationTypeCodeRole(eventParticipationTypeCodeRole);
                eventParticipationIDTypeCode = participantObjectIdentificationType.getParticipantObjectIDTypeCode().getCode();
                auditRec.setParticipationIDTypeCode(eventParticipationIDTypeCode);
            }
        }

        auditRec.setMessageType(mess.getInterface() + " " + mess.getDirection());
        auditRec.setMessage(getBlobFromAuditMessage(mess.getAuditMessage()));

        XMLGregorianCalendar XMLCalDate = eventIdentification.getEventDateTime();
        if (XMLCalDate != null) {
            eventTimeStamp = convertXMLGregorianCalendarToDate(XMLCalDate);
            auditRec.setTimeStamp(eventTimeStamp);
        }

        List<AuditRepositoryRecord> auditRecList = new ArrayList();
        auditRecList.add(auditRec);
        log.debug("AuditRepositoryOrchImpl.logAudit() -- Calling auditLogDao to insert record into database.");
        boolean result = auditLogDao.insertAuditRepository(auditRecList);
        log.debug("AuditRepositoryOrchImpl.logAudit() -- Done calling auditLogDao to insert record into database.");

        response = new AcknowledgementType();
        if (result == true) {
            response.setMessage("Created Log Message in Database...");
        } else {
            response.setMessage("Unable to create Log Message in Database...");
        }
        log.debug("AuditRepositoryOrchImpl.logAudit() -- End");
        return response;
    }

    private Blob getBlobFromAuditMessage(com.services.nhinc.schema.auditmessage.AuditMessageType mess) {
        Blob eventMessage = null; //Not Implemented
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("com.services.nhinc.schema.auditmessage");
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            com.services.nhinc.schema.auditmessage.ObjectFactory factory = new com.services.nhinc.schema.auditmessage.ObjectFactory();
            JAXBElement oJaxbElement = factory.createAuditMessage(mess);
            baOutStrm.close();
            marshaller.marshal(oJaxbElement, baOutStrm);
            byte[] buffer = baOutStrm.toByteArray();
            eventMessage = Hibernate.createBlob(buffer);
        } catch (Exception e) {
            log.error("Exception during Blob conversion :" + e.getMessage());
            e.printStackTrace();
        }
        return eventMessage;
    }

    /**
     * This is the actual implementation for AuditLogMgr Service for AuditQuery returns the AuditEventsReponse
     * @param query
     * @return FindAuditEventsResponseType
     */
    public FindCommunitiesAndAuditEventsResponseType findAudit(FindAuditEventsType query, AssertionType assertion) {
        log.debug("AuditRepositoryOrchImpl.findAudit() -- Begin");

        if (logStatus.equals("")) {
            logStatus = "on";
        }

        if (logStatus.equalsIgnoreCase("off")) {
            log.info("Enable Audit Logging Before Making Query by changing the " +
                    "value in 'auditlogchoice' properties file");
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

        List<AuditRepositoryRecord> responseList = auditLogDao.queryAuditRepositoryOnCriteria(userId, patientId, beginDate, endDate);
        log.debug ("after query call to logDAO.");
     /*   if (responseList != null && responseList.size() > 0) {*/
            log.debug("responseList is not NULL ");
            auditEvents = buildAuditReponseType(responseList);
      /*  }*/

        log.debug("AuditRepositoryOrchImpl.findAudit() -- End");
        return auditEvents;
    }

    /**
     * This method builds the Actual Response from each of the EventLogList coming from Database
     * @param eventsList
     * @return CommunitiesAndFindAdutiEventResponse
     */
    private FindCommunitiesAndAuditEventsResponseType buildAuditReponseType(List<AuditRepositoryRecord> auditRecList) {
        log.debug("AuditRepositoryOrchImpl.buildAuditResponseType -- Begin");
        FindCommunitiesAndAuditEventsResponseType auditResType = new FindCommunitiesAndAuditEventsResponseType();
        FindAuditEventsResponseType response = new FindAuditEventsResponseType();
        AuditMessageType auditMessageType = null;
        Blob blobMessage = null;

        int size = auditRecList.size();
        AuditRepositoryRecord eachRecord = null;
        for (int i = 0; i < size; i++) {
            eachRecord = auditRecList.get(i);
            auditMessageType = new AuditMessageType();
            blobMessage = eachRecord.getMessage();
            if (blobMessage != null) {
                auditMessageType = unMarshallBlobToAuditMess(blobMessage);
                ActiveParticipant act = (ActiveParticipant) auditMessageType.getActiveParticipant().get(0);
                response.getFindAuditEventsReturn().add(auditMessageType);

                if (auditMessageType.getAuditSourceIdentification().size() > 0 &&
                        auditMessageType.getAuditSourceIdentification().get(0) != null &&
                        auditMessageType.getAuditSourceIdentification().get(0).getAuditSourceID() != null &&
                        auditMessageType.getAuditSourceIdentification().get(0).getAuditSourceID().length() > 0) {
                   String tempCommunity = auditMessageType.getAuditSourceIdentification().get(0).getAuditSourceID();
                   if (!auditResType.getCommunities().contains(tempCommunity) ) {
                       auditResType.getCommunities().add(tempCommunity);
                       log.debug("Adding community " + tempCommunity);
                   }
                }
            }
        }

        auditResType.setFindAuditEventResponse(response);
        log.debug("AuditRepositoryOrchImpl.buildAuditResponseType -- End");
        return auditResType;
    }

    /**
     * This method unmarshalls XML Blob to AuditMessage
     * @param auditBlob
     * @return AuditMessageType
     */
    private AuditMessageType unMarshallBlobToAuditMess(Blob auditBlob) {
        log.debug("AuditRepositoryOrchImpl.unMarshallBlobToAuditMess -- Begin");
        AuditMessageType auditMessageType = null;
        try {
            if (auditBlob != null && ((int) auditBlob.length()) > 0) {
                InputStream in = auditBlob.getBinaryStream();
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext("com.services.nhinc.schema.auditmessage");
                Unmarshaller unmarshaller = jc.createUnmarshaller();
                JAXBElement jaxEle = (JAXBElement) unmarshaller.unmarshal(in);
                auditMessageType = (AuditMessageType) jaxEle.getValue();
            }
        } catch (Exception e) {
            log.error("Blob to Audit Message Conversion Error : " + e.getMessage());
            e.printStackTrace();
        }
        log.debug("AuditRepositoryOrchImpl.unMarshallBlobToAuditMess -- End");
        return auditMessageType;
    }

    /**
     * This method converts an XMLGregorianCalendar date to java.util.Date
     * @param xmlCalDate
     * @return java.util.Date
     */
    private Date convertXMLGregorianCalendarToDate(XMLGregorianCalendar xmlCalDate) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        log.info("cal.getTime() -> "+cal.getTime());
        cal.setTime(xmlCalDate.toGregorianCalendar().getTime());
        Date eventDate = cal.getTime();
        log.info("eventDate -> "+eventDate);
        return eventDate;
    }

    /**
     * This method converts an XMLGregorianCalendar date to java.util.Date
     * @param xmlCalDate
     * @return String
     */
    private String convertXMLGregorianCalendarToString(XMLGregorianCalendar xmlCalDate) {
        GregorianCalendar calDate = xmlCalDate.toGregorianCalendar();
        Date eventDate = calDate.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(eventDate);
        return strDate;
    }

}
