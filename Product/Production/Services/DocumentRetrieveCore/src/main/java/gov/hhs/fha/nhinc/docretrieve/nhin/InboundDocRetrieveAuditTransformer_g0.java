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

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryDocumentRetrieveLogger;
import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docretrieve.audit.DocRetrieveAuditLogger;
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
    DocRetrieveAuditLogger docRetrieveAuditLogger;

    /**
     * default constructor.
     */
    public InboundDocRetrieveAuditTransformer_g0() {
        auditLogger = new AuditRepositoryLogger();
        docRetrieveAuditLogger = new DocRetrieveAuditLogger();
    }

    /**
     * injectablet constructor.
     *
     * @param logger a logger.
     */
    InboundDocRetrieveAuditTransformer_g0(DocRetrieveAuditLogger logger) {
        docRetrieveAuditLogger = logger;
    }

    /**
     * Transform this orchestrable to a log event.
     *
     * @param message the orchestrable to me transformed.
     */
    @Override
    public void transformRequest(Orchestratable message) {

        if (message instanceof InboundDocRetrieveOrchestratable) {
            InboundDocRetrieveOrchestratable message_InboundDocRetrieveOrchestratable
                = (InboundDocRetrieveOrchestratable) message;
            docRetrieveAuditLogger.auditRequestMessage(message_InboundDocRetrieveOrchestratable.getRequest(),
                message_InboundDocRetrieveOrchestratable.getAssertion(), null,
                NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, Boolean.FALSE,
                message_InboundDocRetrieveOrchestratable.getWebContextProperties(),
                NhincConstants.DOC_RETRIEVE_SERVICE_NAME);
        }
    }

    /**
     * Transform the message to a log event.
     *
     * @param message the orchestrable
     */
    @Override
    public void transformResponse(Orchestratable message) {
        if (message instanceof InboundDocRetrieveOrchestratable) {
            InboundDocRetrieveOrchestratable message_InboundDocRetrieveOrchestratable
                = (InboundDocRetrieveOrchestratable) message;
            docRetrieveAuditLogger.auditResponseMessage(message_InboundDocRetrieveOrchestratable.getRequest(),
                message_InboundDocRetrieveOrchestratable.getResponse(), message.getAssertion(), null,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
                Boolean.FALSE, message_InboundDocRetrieveOrchestratable.getWebContextProperties(),
                NhincConstants.DOC_RETRIEVE_SERVICE_NAME);
        }

    }

    protected String getHCIDfromAssertion(AssertionType assertion) {
        return HomeCommunityMap.getCommunityIdFromAssertion(assertion);
    }
}
