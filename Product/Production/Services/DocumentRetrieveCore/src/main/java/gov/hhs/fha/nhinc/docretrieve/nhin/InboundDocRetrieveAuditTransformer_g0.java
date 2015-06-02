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
package gov.hhs.fha.nhinc.docretrieve.nhin;

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryDocumentRetrieveLogger;
import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.common.auditlog.DocRetrieveMessageType;
import gov.hhs.fha.nhinc.common.auditlog.DocRetrieveResponseMessageType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;

/**
 *
 * @author mweaver
 */
public class InboundDocRetrieveAuditTransformer_g0 implements AuditTransformer {

    AuditRepositoryDocumentRetrieveLogger auditLogger;


    /**
     * default constructor.
     */
    public InboundDocRetrieveAuditTransformer_g0() {
        auditLogger = new AuditRepositoryLogger();
    }

    /**
     * injectablet constructor.
     * @param logger a logger.
     */
    InboundDocRetrieveAuditTransformer_g0(AuditRepositoryDocumentRetrieveLogger logger) {
        auditLogger = logger;
    }

    /**
     * Transform this orchestrable to a log event.
     * @param message the orchestrable to me transformed.
     * @return the log event for a request
     */
    public LogEventRequestType transformRequest(Orchestratable message) {
        LogEventRequestType auditLogMsg = null;
        if (message instanceof InboundDocRetrieveOrchestratable) {
            auditLogMsg = transformRequest((InboundDocRetrieveOrchestratable) message);
        }
        return auditLogMsg;
    }

    /**
     * Transform an inbound document retrieve orchestrable to a log event.
     * @param message the inbound document retrieve.
     * @return the log event for a request.
     */
    public LogEventRequestType transformRequest(InboundDocRetrieveOrchestratable message) {

        RetrieveDocumentSetRequestType request = message.getRequest();
        AssertionType assertion = message.getAssertion();

        return transformRequest(request, assertion);
    }

    /**
     * Transform a retrieve document set request  and assertion to a log event.
     * @param request the retrieve request document set.
     * @param assertion the assertion
     * @return the log event
     */
    public LogEventRequestType transformRequest(RetrieveDocumentSetRequestType request, AssertionType assertion) {
        DocRetrieveMessageType docRetrieveMessage = new DocRetrieveMessageType();
        docRetrieveMessage.setRetrieveDocumentSetRequest(request);
        docRetrieveMessage.setAssertion(assertion);
        return transformRequest(docRetrieveMessage, HomeCommunityMap.getLocalHomeCommunityId());
    }

    /**
     * Creates the log event.
     * @param docRetrieveMessage the object of the request
     * @param requestCommunityID the requesting home community id
     * @return the log event.
     */
    public LogEventRequestType transformRequest(DocRetrieveMessageType docRetrieveMessage, String requestCommunityID) {
        return auditLogger.logDocRetrieve(docRetrieveMessage,
                NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
                requestCommunityID);
    }

    /**
     * Transform the message to a log event.
     * @param message the orchestrable
     * @return the log event request
     */
    public LogEventRequestType transformResponse(Orchestratable message) {
        LogEventRequestType auditLogMsg = null;
        if (message instanceof InboundDocRetrieveOrchestratable) {
            auditLogMsg = transformResponse((InboundDocRetrieveOrchestratable) message);
        }
        return auditLogMsg;
    }

    /**
     * Transform the message to log event.
     * @param message InboundDocRetrieveOrchestrable
     * @return the log event request
     */
    public LogEventRequestType transformResponse(InboundDocRetrieveOrchestratable message) {
        return transformResponse(message.getResponse(), message.getAssertion());
    }

    /**
     * Transform the response and assertion to the log event.
     * @param response the RetrieveDocumentSetResponseType
     * @param assertion AssertionType
     * @return the log event request
     */
    public LogEventRequestType transformResponse(RetrieveDocumentSetResponseType response, AssertionType assertion) {
        DocRetrieveResponseMessageType DRAuditTransformerMessage = new DocRetrieveResponseMessageType();

        DRAuditTransformerMessage.setRetrieveDocumentSetResponse(response);
        DRAuditTransformerMessage.setAssertion(assertion);

        return transformResponse(DRAuditTransformerMessage, getHCIDfromAssertion(assertion));
    }

    /**
     * @param message
     * @param message
     * @return the log event
     */
    public LogEventRequestType transformResponse(DocRetrieveResponseMessageType message, String hcid) {
        LogEventRequestType auditLogMsg;
        auditLogMsg = auditLogger.logDocRetrieveResult(message,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
                hcid);
        return auditLogMsg;
    }


    protected String getHCIDfromAssertion(AssertionType assertion)
    {
    	return HomeCommunityMap.getCommunityIdFromAssertion(assertion);
    }
}
