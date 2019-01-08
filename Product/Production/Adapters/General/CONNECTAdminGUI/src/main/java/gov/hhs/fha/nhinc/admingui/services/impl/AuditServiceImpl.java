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
package gov.hhs.fha.nhinc.admingui.services.impl;

import static gov.hhs.fha.nhinc.util.CoreHelpUtils.getDate;
import static gov.hhs.fha.nhinc.util.CoreHelpUtils.getXMLGregorianCalendarFrom;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.ObjectFactory;
import gov.hhs.fha.nhinc.admingui.model.Audit;
import gov.hhs.fha.nhinc.admingui.services.AuditService;
import gov.hhs.fha.nhinc.admingui.util.GUIConstants;
import gov.hhs.fha.nhinc.audit.retrieve.AuditRetrieve;
import gov.hhs.fha.nhinc.auditquerylog.nhinc.proxy.AuditQueryLogProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditquerylog.EventTypeList;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsBlobRequest;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsRequestByRequestMessageId;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsRequestType;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsResponseType;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsResults;
import gov.hhs.fha.nhinc.common.auditquerylog.RemoteHcidList;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import gov.hhs.fha.nhinc.util.StreamUtils;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author achidamb
 */
public class AuditServiceImpl implements AuditService {

    private static final Logger LOG = LoggerFactory.getLogger(AuditServiceImpl.class);
    private static final String EVENT_DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";
    private static final String AUDIT_MESSAGE_NAMESPACE = "com.services.nhinc.schema.auditmessage";
    private AuditRetrieve auditRetrieve = null;

    public AuditServiceImpl() {
        auditRetrieve = new AuditQueryLogProxyObjectFactory().getAuditRetrieveProxy();
    }

    /**
     * This method searches Audit database based on serviceTypes, userId, remoteHcid, eventstartDate and eventEndDate.
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
    public List<Audit> searchAuditRecord(List<String> eventTypeList, String userId,
        List<String> remoteHcidList, Date startDate, Date endDate, Map<String, String> remoteHcidOrgNameMap) {
        QueryAuditEventsRequestType auditRequest = new QueryAuditEventsRequestType();
        auditRequest.setEventTypeList(createEventTypeList(eventTypeList));
        auditRequest.setUserId(userId);
        auditRequest.setRemoteHcidList(createRemoteHcidList(remoteHcidList));
        auditRequest.setEventBeginDate(getXMLGregorianCalendarFrom(startDate));
        auditRequest.setEventEndDate(getXMLGregorianCalendarFrom(endDate));
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
    public List<Audit> searchAuditRecordBasedOnMsgIdAndRelatesTo(String msgId, String relatesTo,
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

    private static EventTypeList createEventTypeList(List<String> eventTypeList) {
        if (NullChecker.isNotNullish(eventTypeList)) {
            EventTypeList schemaEventType = new EventTypeList();
            schemaEventType.getEventType().addAll(eventTypeList);
            return schemaEventType;
        }
        return null;
    }

    private static RemoteHcidList createRemoteHcidList(List<String> remoteHcidList) {
        if (NullChecker.isNotNullish(remoteHcidList)) {
            RemoteHcidList schemaHcid = new RemoteHcidList();
            schemaHcid.getRemoteHcid().addAll(remoteHcidList);
            return schemaHcid;
        }
        return null;
    }

    private static List<Audit> createAuditObjects(QueryAuditEventsResponseType result,
        Map<String, String> remoteOrgMap) {
        if (result != null && NullChecker.isNotNullish(result.getQueryAuditEventsResults())) {
            List<Audit> auditList = new ArrayList<>();
            for (QueryAuditEventsResults auditEvent : result.getQueryAuditEventsResults()) {
                Audit obj = new Audit();

                obj.setEventTimestamp(formateDate(getDate(auditEvent.getEventTimestamp())));
                obj.setEventType(getEventTypeDisplayName(auditEvent.getEventType()));
                obj.setMessageId(auditEvent.getRequestMessageId());
                obj.setRemoteHcid(getRemoteHcidDisplayName(auditEvent.getRemoteHcid(), remoteOrgMap));
                obj.setUserId(auditEvent.getUserId());
                obj.setDirection(auditEvent.getDirection());
                obj.setRelatesTo(auditEvent.getRelatesTo());
                obj.setId(auditEvent.getId());
                auditList.add(obj);
            }
            return auditList;
        }
        return null;
    }

    private static String getEventTypeDisplayName(String eventType) {
        return GUIConstants.EVENT_NAMES.fromServiceName(eventType).getAbbServiceName();
    }

    private static String getRemoteHcidDisplayName(String remoteHcid, Map<String, String> remoteOrgMap) {
        return remoteOrgMap.get(HomeCommunityMap.formatHomeCommunityId(remoteHcid));
    }

    private static String marshallAuditMessage(AuditMessageType mess) {
        if (mess != null) {
            ByteArrayOutputStream baOutStrm = null;
            try {
                JAXBContext jc = new JAXBContextHandler().getJAXBContext(AUDIT_MESSAGE_NAMESPACE);
                Marshaller marshaller = jc.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                baOutStrm = new ByteArrayOutputStream();
                baOutStrm.reset();
                ObjectFactory factory = new ObjectFactory();
                JAXBElement<AuditMessageType> oJaxbElement = factory.createAuditMessage(mess);
                marshaller.marshal(oJaxbElement, baOutStrm);
                return baOutStrm.toString();
            } catch (JAXBException e) {
                LOG.error("Exception during Blob conversion {}", e.getLocalizedMessage(), e);
            } finally {
                StreamUtils.closeReaderSilently(baOutStrm);
            }
        }
        return null;
    }

    private static String formateDate(Date date) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(EVENT_DATE_FORMAT);
            return sdf.format(date);
        }
        return null;
    }
}
