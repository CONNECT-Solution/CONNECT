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
package gov.hhs.fha.nhinc.auditquerylog;

import static gov.hhs.fha.nhinc.audit.retrieve.AuditRetrieveEventsUtil.getQueryAuditEventBlobResponse;
import static gov.hhs.fha.nhinc.audit.retrieve.AuditRetrieveEventsUtil.getQueryAuditEventResponse;

import gov.hhs.fha.nhinc.auditrepository.hibernate.AuditRepositoryDAO;
import gov.hhs.fha.nhinc.common.auditquerylog.EventTypeList;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsBlobRequest;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsBlobResponse;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsRequestByRequestMessageId;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsRequestType;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsResponseType;
import gov.hhs.fha.nhinc.common.auditquerylog.RemoteHcidList;
import java.util.Date;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The AuditQueryLog impl is called from Pojo/Service implementation and passes/retrieve params from/to DAO layer.
 *
 * @author tjafri
 */
public class AuditQueryLogImpl {

    private AuditRepositoryDAO dao;
    private static final Logger LOG = LoggerFactory.getLogger(AuditQueryLogImpl.class);

    /**
     *
     * @param request - Audit Search params will be provided by this requestAuditEvents. The Request may have Event
     *            startDate, Event EndDate, UserId, Remote Org Id and ServiceName/EventType.These are optional
     *            parameters. If none of them is provided all records will be retrieved.
     * @return QueryAuditEventsResponseType - The Response will be having EventType or ServiceName, EventStatus- Success
     *         or Failure, Event Timestamp, UserId(Human who initiated transaction), Direction (Outbound/Inbound),
     *         MessageID -RequestMessageID, RelatesTo (Relates the DeferredRequests and DeferredResponses, Remote
     *         Organization Id and Audit Id.
     *
     */
    public QueryAuditEventsResponseType queryAuditEvents(QueryAuditEventsRequestType request) {
        return getQueryAuditEventResponse(
            getAuditRepositoryDao().queryByAuditOptions(getEventTypes(request.getEventTypeList()),
                request.getUserId(), getRemoteHcids(request.getRemoteHcidList()),
                getRequestDate(request.getEventBeginDate()), getRequestDate(request.getEventEndDate())));

    }

    /**
     *
     * @param request - Audit search params MessageId and RelatesTo will be provided by Request. These are optional
     *            fields in requestAuditEvents
     * @return QueryAuditEventsResponseType - The Response will be having EventType or ServiceName, EventStatus- Success
     *         or Failure, Event Timestamp, UserId(Human who initiated transaction), Direction (Outbound/Inbound),
     *         MessageID -RequestMessageID, RelatesTo (Relates the DeferredRequests and DeferredResponses, Remote
     *         Organization Id and Audit Id.
     *
     */
    public QueryAuditEventsResponseType queryAuditEventsByMessageIdAndRelatesTo(
        QueryAuditEventsRequestByRequestMessageId request) {
        return getQueryAuditEventResponse(
            getAuditRepositoryDao().queryAuditRecords(request.getRequestMessageId(), request.getRelatesTo()));
    }

    /**
     *
     * @param request - Audit search params. Audit Id will be provided by requestAuditEvents.
     * @return QueryAuditEventsBlobResponse - Response will be having only Audit Blob message.
     *
     */
    public QueryAuditEventsBlobResponse queryAuditEventsById(QueryAuditEventsBlobRequest request) {
        return getQueryAuditEventBlobResponse(getAuditRepositoryDao().queryByAuditId(request.getId()));
    }

    /**
     *
     * @return AuditRepositoryDAO Instance of AuditRepositoryDAO to query audit events.
     */
    protected AuditRepositoryDAO getAuditRepositoryDao() {
        if (dao == null) {
            dao = new AuditRepositoryDAO();
        }
        return dao;
    }

    private Date getRequestDate(XMLGregorianCalendar dateObj) {
        if (dateObj != null) {
            LOG.info("Converting XMLGregorianCalendar to a date object");
            LOG.info("{}-{}-{} {}:{}:{} {}", dateObj.getMonth(), dateObj.getDay(), dateObj.getYear(), dateObj.getHour(),
                dateObj.getMinute(), dateObj.getSecond(), dateObj.getTimezone());
            return new Date(dateObj.toGregorianCalendar().getTimeInMillis());
        }
        return null;
    }

    private List<String> getEventTypes(EventTypeList eventList) {
        if (eventList != null) {
            return eventList.getEventType();
        }
        return null;
    }

    private List<String> getRemoteHcids(RemoteHcidList remoteHcidList) {
        if (remoteHcidList != null) {
            return remoteHcidList.getRemoteHcid();
        }
        return null;
    }

}
