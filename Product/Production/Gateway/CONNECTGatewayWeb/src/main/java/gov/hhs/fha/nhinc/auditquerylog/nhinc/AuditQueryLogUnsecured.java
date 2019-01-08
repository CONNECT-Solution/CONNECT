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

import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsBlobRequest;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsBlobResponse;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsRequestByRequestMessageId;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsRequestType;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsResponseType;
import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;
import javax.xml.ws.soap.SOAPBinding;

/**
 *
 * @author achidamb
 */
@BindingType(value = SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true)
public class AuditQueryLogUnsecured implements
    gov.hhs.fha.nhinc.common.nhinccomponentauditquerylog.AuditQueryLogPortType {

    @Resource
    private WebServiceContext context;

    /**
     *
     * @return WebServiceContext
     */
    protected WebServiceContext getWebServiceContext() {
        return context;
    }

    /**
     * AuditQueryLogUnsecuredImpl makes call to AuditRepository Core modules and query Audit events from the provided
     * Request parameters
     *
     * @return AuditQueryLogUnsecuredImpl
     */
    protected AuditQueryLogUnsecuredImpl getAuditQueryImpl() {
        return new AuditQueryLogUnsecuredImpl();
    }

    /**
     *
     * @param requestById - The AuditRepository table id will be provided as an input and corresponding Blob will be
     * retrieved.
     *
     * @return QueryAuditEventsBlobResponse - Response will be having only Audit Blob message.
     */
    @Override
    public QueryAuditEventsBlobResponse queryAuditEventsBlob(QueryAuditEventsBlobRequest requestById) {
        return getAuditQueryImpl().queryAuditEventsById(requestById, context);
    }

    /**
     *
     * @param request - The AuditRepository table will be queried from the provided request parameters. The Request
     * provides search query parameters userId, Event Status, Event StartDate, Event EndDate and Organization Id to
     * query for. All these elements are optional. The query will return all records from audit tables if none of them
     * are provided.
     * @return QueryAuditEventsResponseType - The Response will be having EventType or ServiceName, EventStatus- Success
     * or Failure, Event Timestamp, UserId(Human who initiated transaction), Direction (Outbound/Inbound), MessageID
     * -RequestMessageID, RelatesTo (Relates the DeferredRequests and DeferredResponses, Remote Organization Id and
     * Audit Id.
     */
    @Override
    public QueryAuditEventsResponseType queryAuditEvents(QueryAuditEventsRequestType request) {
        return getAuditQueryImpl().queryAuditEvents(request, context);
    }

    /**
     *
     * @param requestByMessageId - The AuditRepository table can be queried from the provided request. This Request will
     * have messageID and RelatesTo elements. If search parameters are not provided then all the records will be
     * returned.
     * @return QueryAuditEventsResponseType - The Response will be having EventType or ServiceName, EventStatus- Success
     * or Failure, Event Timestamp, UserId(Human who initiated transaction), Direction (Outbound/Inbound), MessageID
     * -RequestMessageID, RelatesTo (Relates the DeferredRequests and DeferredResponses, Remote Organization Id and
     * Audit Id.
     */
    @Override
    public QueryAuditEventsResponseType queryAuditEventsByMessageID(
        QueryAuditEventsRequestByRequestMessageId requestByMessageId) {
        return getAuditQueryImpl().queryAuditEventsByMessageId(requestByMessageId, context);
    }

}
