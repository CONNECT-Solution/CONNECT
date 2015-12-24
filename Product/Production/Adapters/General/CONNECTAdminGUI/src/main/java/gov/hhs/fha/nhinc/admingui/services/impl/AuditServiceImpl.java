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
package gov.hhs.fha.nhinc.admingui.services.impl;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.ObjectFactory;
import gov.hhs.fha.nhinc.admingui.event.model.Audit;
import gov.hhs.fha.nhinc.admingui.services.AuditService;
import gov.hhs.fha.nhinc.audit.retrieve.AuditRetrieve;
import gov.hhs.fha.nhinc.auditquerylog.nhinc.proxy.AuditQueryLogProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditquerylog.EventTypeList;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsBlobRequest;
import java.util.ArrayList;
import java.util.Date;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsRequestByRequestMessageId;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsRequestType;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsResponseType;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsResults;
import gov.hhs.fha.nhinc.common.auditquerylog.RemoteHcidList;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author achidamb
 */
public class AuditServiceImpl implements AuditService {

    private static final Logger LOG = LoggerFactory.getLogger(AuditServiceImpl.class);
    private static final String EVENT_DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";
    private AuditRetrieve auditRetrieve = null;

    public AuditServiceImpl() {
        auditRetrieve = new AuditQueryLogProxyObjectFactory().getAuditRetrieveProxy();
    }

    public String createMockAuditMessage(long id) {
        Audit audit = new Audit();
        audit.setMessage("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><AuditMessage xmlns=\"http://nhinc.services.com/schema/auditmessage\"><EventIdentification EventActionCode=\"C\" EventDateTime=\"2015-12-17T00:01:34.113Z\" EventOutcomeIndicator=\"0\"><EventID code=\"110107\" displayName=\"Import\" codeSystemName=\"DCM\"/><EventTypeCode code=\"ITI-41\" displayName=\"Provide and Register Document Set-b\" codeSystemName=\"IHE Transactions\"/></EventIdentification><ActiveParticipant UserID=\"http://www.w3.org/2005/08/addressing/anonymous\" UserName=\"Karl Skagerberg\" UserIsRequestor=\"true\" NetworkAccessPointID=\"127.0.0.1\" NetworkAccessPointTypeCode=\"2\"><RoleIDCode code=\"110153\" displayName=\"Source\" codeSystemName=\"DCM\"/></ActiveParticipant><ActiveParticipant UserID=\"https://localhost:8181/Gateway/DocumentSubmission/2_0/NhinService/XDRRequest_Service\" AlternativeUserID=\"6636@VIMEHTA-F05773\" UserIsRequestor=\"false\" NetworkAccessPointID=\"localhost\" NetworkAccessPointTypeCode=\"1\"><RoleIDCode code=\"110152\" displayName=\"Destination\" codeSystemName=\"DCM\"/></ActiveParticipant><AuditSourceIdentification AuditEnterpriseSiteID=\"DoD\" AuditSourceID=\"urn:oid:1.1\"/><ParticipantObjectIdentification ParticipantObjectID=\"SELF-5^^^&amp;1.3.6.1.4.1.21367.2005.3.7&amp;ISO\" ParticipantObjectTypeCode=\"1\" ParticipantObjectTypeCodeRole=\"1\"><ParticipantObjectIDTypeCode code=\"2\" displayName=\"Patient Number\" codeSystemName=\"RFC-3881\"/></ParticipantObjectIdentification><ParticipantObjectIdentification ParticipantObjectID=\"1.3.6.1.4.1.21367.2005.3.9999.33\" ParticipantObjectTypeCode=\"2\" ParticipantObjectTypeCodeRole=\"20\"><ParticipantObjectIDTypeCode code=\"urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd\" displayName=\"submission set classificationNode\" codeSystemName=\"IHE XDS Metadata\"/></ParticipantObjectIdentification></AuditMessage>");

        return audit.getMessage();
    }

    /**
     * This method searches Audit database based on eventoutcome, serviceTypes, userId, remoteHcid, eventstartDate and
     * eventEndDate.
     *
     * @param outcome
     * @param eventTypeList
     * @param userId
     * @param remoteHcidList
     * @param startDate
     * @param endDate
     * @param remoteHcidOrgNameMap
     * @return
     */
    @Override
    public List<Audit> searchAuditRecord(Integer outcome, List<String> eventTypeList, String userId,
        List<String> remoteHcidList, Date startDate, Date endDate, Map<String, String> remoteHcidOrgNameMap) {
        QueryAuditEventsRequestType auditRequest = new QueryAuditEventsRequestType();
        auditRequest.setEventOutcomeIndicator(outcome != null ? BigInteger.valueOf(outcome) : null);
        auditRequest.setEventTypeList(createEventTypeList(eventTypeList));
        auditRequest.setUserId(userId);
        auditRequest.setRemoteHcidList(createRemoteHcidList(remoteHcidList));
        auditRequest.setEventBeginDate(convertDateToXMLGregorianCalendar(startDate));
        auditRequest.setEventEndDate(convertDateToXMLGregorianCalendar(endDate));
        return createAuditObjects(auditRetrieve.retrieveAudits(auditRequest), remoteHcidOrgNameMap);
    }

    /**
     * This method searches Audit database based on eventoutcome, serviceTypes, userId, remoteHcid, eventstartDate and
     * eventEndDate
     */
    /**
     * This method searches Audit database based on messageId and\or relatesTo.
     *
     * @param msgId
     * @param relatesTo
     * @param remoteHcidOrgNameMap
     * @return
     */
    @Override
    public List<Audit> searchAuditRecordBasedOnMsgIdAndRelatesToId(String msgId, String relatesTo,
        Map<String, String> remoteHcidOrgNameMap) {
        QueryAuditEventsRequestByRequestMessageId auditRequest = new QueryAuditEventsRequestByRequestMessageId();
        auditRequest.setRequestMessageId(msgId);
        auditRequest.setRelatesTo(relatesTo);
        return createAuditObjects(auditRetrieve.retrieveAuditsByMsgIdAndRelatesToId(auditRequest), remoteHcidOrgNameMap);
    }

    @Override
    public String fetchAuditBlob(long auditId) {
        QueryAuditEventsBlobRequest request = new QueryAuditEventsBlobRequest();
        request.setId(auditId);
        return marshallAuditMessage(auditRetrieve.retrieveAuditBlob(request).getAuditMessage());
    }

    private EventTypeList createEventTypeList(List<String> eventTypeList) {
        if (NullChecker.isNotNullish(eventTypeList)) {
            EventTypeList schemaEventType = new EventTypeList();
            schemaEventType.getEventType().addAll(eventTypeList);
            return schemaEventType;
        }
        return null;
    }

    private RemoteHcidList createRemoteHcidList(List<String> remoteHcidList) {
        if (NullChecker.isNotNullish(remoteHcidList)) {
            RemoteHcidList schemaHcid = new RemoteHcidList();
            schemaHcid.getRemoteHcid().addAll(remoteHcidList);
            return schemaHcid;
        }
        return null;
    }

    private XMLGregorianCalendar convertDateToXMLGregorianCalendar(Date date) {
        if (date != null) {
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(date);
            try {
                XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
                LOG.info("{}-{}-{} {}:{}:{} {}", cal.getMonth(), cal.getDay(), cal.getYear(), cal.getHour(),
                    cal.getMinute(), cal.getSecond(), cal.getTimezone());
                return cal;
            } catch (DatatypeConfigurationException ex) {
                LOG.error("Unable to convert date {} ", ex.getLocalizedMessage(), ex);
            }
        }
        return null;
    }

    private List<Audit> createAuditObjects(QueryAuditEventsResponseType result, Map<String, String> remoteOrgMap) {
        if (result != null && NullChecker.isNotNullish(result.getQueryAuditEventsResults())) {
            List<Audit> auditList = new ArrayList<>();
            for (QueryAuditEventsResults auditEvent : result.getQueryAuditEventsResults()) {
                Audit obj = new Audit();
                obj.setEventOutcomeIndicator(NhincConstants.EVENT_IDENTIFICATION_STATUS.fromDisplayString(
                    auditEvent.getEventOutcomeIndicator().toString()).name());
                obj.setEventTimestamp(formateDate(convertXMLGregorianDate(auditEvent.getEventTimestamp())));
                obj.setEventType(getEventTypeDisplayName(auditEvent.getEventType()));
                obj.setMessageId(auditEvent.getRequestMessageId());
                obj.setRemoteHcid(getRemoteHcidDisplayName(auditEvent.getRemoteHcid(), remoteOrgMap));
                obj.setUserId(auditEvent.getUserId());
                auditList.add(obj);
            }
            return auditList;
        }
        return null;
    }

    private Date convertXMLGregorianDate(XMLGregorianCalendar dateObj) {
        if (dateObj != null) {
            return dateObj.toGregorianCalendar().getTime();
        }
        return null;
    }

    private String getEventTypeDisplayName(String eventType) {
        return eventType != null ? NhincConstants.NHIN_SERVICE_NAMES.fromValueString(eventType).name() : null;
    }

    private String getRemoteHcidDisplayName(String remoteHcid, Map<String, String> remoteOrgMap) {
        return remoteOrgMap.get(HomeCommunityMap.formatHomeCommunityId(remoteHcid));
    }

    private String marshallAuditMessage(AuditMessageType mess) {
        if (mess != null) {
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
                return baOutStrm.toString();
            } catch (JAXBException | IOException e) {
                LOG.error("Exception during Blob conversion {}", e.getLocalizedMessage(), e);
            }
        }
        return null;
    }

    private String formateDate(Date date) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(EVENT_DATE_FORMAT);
            return sdf.format(date);
        }
        return null;
    }
}
