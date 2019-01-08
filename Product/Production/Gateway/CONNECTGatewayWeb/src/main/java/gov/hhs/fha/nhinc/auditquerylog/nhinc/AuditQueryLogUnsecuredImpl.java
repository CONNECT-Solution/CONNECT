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
package gov.hhs.fha.nhinc.auditquerylog.nhinc;

import gov.hhs.fha.nhinc.auditquerylog.AuditQueryLogImpl;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsBlobRequest;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsBlobResponse;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsRequestByRequestMessageId;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsRequestType;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsResponseType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author achidamb
 */
public class AuditQueryLogUnsecuredImpl {

    /**
     *
     * @return AuditQueryLogImpl - The AuditRepository jar will be called from the AuditQueryLog service to query the
     * Audit events.
     */
    protected AuditQueryLogImpl getAuditQueryLogImpl() {
        return new AuditQueryLogImpl();
    }

    /**
     *
     * @param requestAuditEvents - The Request provides search query parameters userId, Event Status, Event StartDate,
     * Event EndDate and Organization Id to query for. All these elements are optional. The query will return all
     * records from audit tables if none of them are provided.
     * @param context - WebserviceContext. Will be used if we load assertion.
     * @return QueryAuditEventsResponseType
     */
    public QueryAuditEventsResponseType queryAuditEvents(QueryAuditEventsRequestType requestAuditEvents,
        WebServiceContext context) {

        QueryAuditEventsResponseType response = null;
        if (requestAuditEvents != null) {
            response = getAuditQueryLogImpl().queryAuditEvents(requestAuditEvents);
        }
        return response;

    }

    /**
     *
     * @param requestAuditEvents-The AuditRepository table can be queried from the provided request. This Request will
     * have messageID and RelatesTo elements. If search parameters are not provided then all the audit records will be
     * returned.
     * @param context-WebserviceContext. Will be used if we load assertion.
     * @return QueryAuditEventsResponseType
     */
    public QueryAuditEventsResponseType queryAuditEventsByMessageId(
        QueryAuditEventsRequestByRequestMessageId requestAuditEvents, WebServiceContext context) {

        QueryAuditEventsResponseType response = null;
        if (requestAuditEvents != null) {
            response = getAuditQueryLogImpl().queryAuditEventsByMessageIdAndRelatesTo(requestAuditEvents);
        }
        return response;

    }

    /**
     *
     * @param requestAuditEvents The AuditRepository table id will be provided as an input and corresponding Blob will
     * be retrieved.
     * @param context-WebserviceContext. Will be used if we load assertion.
     * @return QueryAuditEventsBlobResponse
     */
    public QueryAuditEventsBlobResponse queryAuditEventsById(QueryAuditEventsBlobRequest requestAuditEvents,
        WebServiceContext context) {

        QueryAuditEventsBlobResponse response = null;
        if (requestAuditEvents != null) {
            response = getAuditQueryLogImpl().queryAuditEventsById(requestAuditEvents);
        }
        return response;
    }

}
