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
package gov.hhs.fha.nhinc.auditquerylog.nhinc.proxy;

import gov.hhs.fha.nhinc.audit.retrieve.AuditRetrieve;
import gov.hhs.fha.nhinc.auditquerylog.nhinc.proxy.service.QueryAuditEventsUnsecuredServicePortDescriptor;
import gov.hhs.fha.nhinc.auditquerylog.nhinc.proxy.service.QueryByIdUnsecuredServicePortDescriptor;
import gov.hhs.fha.nhinc.auditquerylog.nhinc.proxy.service.QueryByMessageIdAndRelatesToUnsecuredServicePortDescriptor;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsBlobRequest;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsBlobResponse;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsRequestByRequestMessageId;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsRequestType;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsResponseType;
import gov.hhs.fha.nhinc.common.nhinccomponentauditquerylog.AuditQueryLogPortType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.messaging.client.CONNECTCXFClientFactory;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Webservices client implementation to search Audit events
 *
 * @author achidamb
 */
public class AuditQueryLogProxyWebServiceUnsecuredImpl implements AuditRetrieve {

    private static final Logger LOG = LoggerFactory.getLogger(AuditQueryLogProxyWebServiceUnsecuredImpl.class);
    private final WebServiceProxyHelper proxyHelper = new WebServiceProxyHelper();
    // method names to invoke
    private final String QUERY_AUDIT_EVENTS = "queryAuditEvents";
    private final String QUERY_AUDIT_EVENTS_BY_MSG_RELATESTO_ID = "queryAuditEventsByMessageID";
    private final String QUERY_AUDIT_EVENTS_BY_ID = "queryAuditEventsBlob";

    /**
     *
     * @param request - Request provides search params to retrieve Audit Events. If none of the elements are provided in
     *            request. If optional elements are not provided will return all audit events
     * @return QueryAuditEventsResponseType
     */
    @Override
    public QueryAuditEventsResponseType retrieveAudits(QueryAuditEventsRequestType request) {

        QueryAuditEventsResponseType response = null;
        try {

            CONNECTClient<AuditQueryLogPortType> client = getCONNECTClient(getQueryAuditEventsPortDescriptor());
            if (client != null) {
                response = (QueryAuditEventsResponseType) client.invokePort(AuditQueryLogPortType.class,
                    QUERY_AUDIT_EVENTS, request);
            }
        } catch (Exception ex) {
            LOG.error("Failed to call the web service {}: {}", NhincConstants.ADAPTER_AUDIT_QUERY_LOG_SERVICE_NAME,
                ex.getLocalizedMessage(), ex);
        }
        return response;
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
        QueryAuditEventsResponseType response = null;
        try {

            CONNECTClient<AuditQueryLogPortType> client = getCONNECTClient(
                getQueryAuditEventsByMessageIdAndRelatesToPortDescriptor());
            if (client != null) {
                response = (QueryAuditEventsResponseType) client.invokePort(AuditQueryLogPortType.class,
                    QUERY_AUDIT_EVENTS_BY_MSG_RELATESTO_ID, request);
            }
        } catch (Exception ex) {
            LOG.error("Failed to call the web service {}: {}", NhincConstants.ADAPTER_AUDIT_QUERY_LOG_SERVICE_NAME,
                ex.getLocalizedMessage(), ex);
        }
        return response;
    }

    /**
     *
     * @param request - Request provides Id and corresponding Blob will be retrieved.
     * @return QueryAuditEventsBlobResponse - Response returns Audit Blob message.
     */
    @Override
    public QueryAuditEventsBlobResponse retrieveAuditBlob(QueryAuditEventsBlobRequest request) {
        QueryAuditEventsBlobResponse response = null;
        try {

            CONNECTClient<AuditQueryLogPortType> client = getCONNECTClient(getQueryAuditEventsByIdPortDescriptor());
            if (client != null) {
                response = (QueryAuditEventsBlobResponse) client.invokePort(AuditQueryLogPortType.class,
                    QUERY_AUDIT_EVENTS_BY_ID, request);
            }
        } catch (Exception ex) {
            LOG.error("Failed to call the web service {}: {}", NhincConstants.ADAPTER_AUDIT_QUERY_LOG_SERVICE_NAME,
                ex.getLocalizedMessage(), ex);
        }
        return response;
    }

    private String getAdapterQueryLogUnsecuredSerrviceUrl() {
        String url = null;
        try {
            url = proxyHelper.getUrlLocalHomeCommunity(NhincConstants.ADAPTER_AUDIT_QUERY_LOG_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            LOG.error("Error while retrieving url for {}: {}", NhincConstants.ADAPTER_AUDIT_QUERY_LOG_SERVICE_NAME,
                ex.getLocalizedMessage(), ex);
            return url;
        } catch (Exception ex) {
            LOG.error("Failed to call the web service {}: {}", NhincConstants.ADAPTER_AUDIT_QUERY_LOG_SERVICE_NAME,
                ex.getLocalizedMessage(), ex);
        }
        return url;
    }

    private CONNECTClient<AuditQueryLogPortType> getCONNECTClient(
        ServicePortDescriptor<AuditQueryLogPortType> portDescriptor) {

        String url = getAdapterQueryLogUnsecuredSerrviceUrl();
        CONNECTClient<AuditQueryLogPortType> client = null;
        if (NullChecker.isNotNullish(url)) {
            client = CONNECTCXFClientFactory.getInstance().getCONNECTClientUnsecured(portDescriptor, url, null);
        }
        return client;

    }

    private ServicePortDescriptor<AuditQueryLogPortType> getQueryAuditEventsPortDescriptor() {
        return new QueryAuditEventsUnsecuredServicePortDescriptor();

    }

    private ServicePortDescriptor<AuditQueryLogPortType> getQueryAuditEventsByMessageIdAndRelatesToPortDescriptor() {
        return new QueryByMessageIdAndRelatesToUnsecuredServicePortDescriptor();

    }

    private ServicePortDescriptor<AuditQueryLogPortType> getQueryAuditEventsByIdPortDescriptor() {
        return new QueryByIdUnsecuredServicePortDescriptor();

    }
}
