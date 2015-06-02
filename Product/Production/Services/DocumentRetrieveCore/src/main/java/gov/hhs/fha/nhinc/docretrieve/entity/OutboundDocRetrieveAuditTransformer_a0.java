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
package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.common.auditlog.DocRetrieveMessageType;
import gov.hhs.fha.nhinc.common.auditlog.DocRetrieveResponseMessageType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;

/**
 *
 * @author mweaver
 */
public class OutboundDocRetrieveAuditTransformer_a0 implements AuditTransformer {

    protected AuditRepositoryLogger getAuditRepositoryLogger() {
        return new AuditRepositoryLogger();
    }

    protected String getLocalHomeCommunityId() {
    	return HomeCommunityMap.getLocalHomeCommunityId();
    }

    public LogEventRequestType transformRequest(Orchestratable message) {
        LogEventRequestType auditLogMsg = null;
        if (message instanceof OutboundDocRetrieveOrchestratable) {
            OutboundDocRetrieveOrchestratable EntityDROrchImp_g0Message = (OutboundDocRetrieveOrchestratable) message;

            DocRetrieveMessageType DRAuditTransformerMessage = new DocRetrieveMessageType();
            DRAuditTransformerMessage.setRetrieveDocumentSetRequest(EntityDROrchImp_g0Message.getRequest());
            DRAuditTransformerMessage.setAssertion(EntityDROrchImp_g0Message.getAssertion());

            String requestCommunityID = getLocalHomeCommunityId();

            AuditRepositoryLogger auditLogger = getAuditRepositoryLogger();
            auditLogMsg = auditLogger.logDocRetrieve(DRAuditTransformerMessage,
                    NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE,
                    requestCommunityID);
        }
        return auditLogMsg;
    }

    public LogEventRequestType transformResponse(Orchestratable message) {
        LogEventRequestType auditLogMsg = null;
        if (message instanceof OutboundDocRetrieveOrchestratable) {
            OutboundDocRetrieveOrchestratable EntityDROrchImp_g0Message = (OutboundDocRetrieveOrchestratable) message;

            DocRetrieveResponseMessageType DRAuditTransformerMessage = new DocRetrieveResponseMessageType();
            DRAuditTransformerMessage.setRetrieveDocumentSetResponse(EntityDROrchImp_g0Message.getResponse());
            DRAuditTransformerMessage.setAssertion(EntityDROrchImp_g0Message.getAssertion());

            String requestCommunityID = getLocalHomeCommunityId();

            AuditRepositoryLogger auditLogger = getAuditRepositoryLogger();
            auditLogMsg = auditLogger.logDocRetrieveResult(DRAuditTransformerMessage,
                    NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE,
                    requestCommunityID);
        }
        return auditLogMsg;
    }
}
