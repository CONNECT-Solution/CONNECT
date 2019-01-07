/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 *  All rights reserved.
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
package gov.hhs.fha.nhinc.auditquerylog.nhinc.proxy;

import gov.hhs.fha.nhinc.audit.retrieve.AuditRetrieve;
import gov.hhs.fha.nhinc.auditquerylog.AuditQueryLogImpl;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsBlobRequest;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsBlobResponse;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsRequestByRequestMessageId;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsRequestType;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsResponseType;

/**
 * Pojo implementation to retrieve Audit events
 *
 * @author tjafri
 */
public class AuditQueryLogProxyJavaImpl implements AuditRetrieve {

    private AuditQueryLogImpl queryImpl = null;

    public AuditQueryLogProxyJavaImpl() {
        queryImpl = new AuditQueryLogImpl();
    }

    /**
     *
     * @param req - Request provides search params to retrieve Audit Events. If none of the elements are provided in
     *            request. If optional elements are not provided will return all audit events
     * @return QueryAuditEventsResponseType
     */
    @Override
    public QueryAuditEventsResponseType retrieveAudits(QueryAuditEventsRequestType req) {
        return queryImpl.queryAuditEvents(req);
    }

    /**
     *
     * @param request - Request provides search params MessageId and RelatesTo to retrieve Audit Events. If none of the
     *            elements are provided in request. If optional elements are not provided will return all audit events
     * @return QueryAuditEventsResponseType
     */
    @Override
    public QueryAuditEventsResponseType retrieveAuditsByMsgIdAndRelatesToId(
            QueryAuditEventsRequestByRequestMessageId request) {
        return queryImpl.queryAuditEventsByMessageIdAndRelatesTo(request);
    }

    /**
     *
     * @param request - Request provides Id and corresponding Blob will be retrieved.
     * @return QueryAuditEventsBlobResponse - Response returns Audit Blob message.
     */
    @Override
    public QueryAuditEventsBlobResponse retrieveAuditBlob(QueryAuditEventsBlobRequest request) {
        return queryImpl.queryAuditEventsById(request);
    }

}
