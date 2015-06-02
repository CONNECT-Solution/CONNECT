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
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import java.util.List;

import org.apache.log4j.Logger;

/**
 *
 * @author mweaver
 */
public abstract class OutboundDocRetrieveStrategyBase implements OrchestrationStrategy {

    private static final Logger LOG = Logger.getLogger(OutboundDocRetrieveStrategyBase.class);

    @Override
    public void execute(Orchestratable message) {
        if (message instanceof OutboundDocRetrieveOrchestratable) {
            execute((OutboundDocRetrieveOrchestratable) message);
        }
    }

    public void execute(OutboundDocRetrieveOrchestratable message) {
        LOG.trace("Begin OutboundDocRetrieveStrategyBase.execute");
        if (message == null) {
            LOG.debug("NhinOrchestratable was null");
            return;
        }

        if (message instanceof OutboundDocRetrieveOrchestratable) {
            OutboundDocRetrieveOrchestratable NhinDRMessage = (OutboundDocRetrieveOrchestratable) message;
            //Append urn:oid to the home community id if its not present
            String requestCommunityID = HomeCommunityMap.getCommunityIdForRDRequest(NhinDRMessage.getRequest());

            //Assign the modified request community id value to the requests
            if (NhinDRMessage.getRequest() != null && NullChecker.isNotNullish(NhinDRMessage.getRequest().getDocumentRequest())) {
                List<DocumentRequest> documentRequestList = NhinDRMessage.getRequest().getDocumentRequest();
                //loop through the request list and set the HCID
                for (int i = 0; i < documentRequestList.size(); i++) {
                    DocumentRequest documentRequest = NhinDRMessage.getRequest().getDocumentRequest().get(i);
                    if ( documentRequest!= null){
                        documentRequest.setHomeCommunityId(HomeCommunityMap.getHomeCommunityIdWithPrefix(documentRequest.getHomeCommunityId()));
                    }
                }
            }

            LOG.debug("Calling audit log for doc retrieve request (a0) sent to nhin (g0)");
            auditRequestMessage(NhinDRMessage.getRequest(), NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                    NhincConstants.AUDIT_LOG_NHIN_INTERFACE, NhinDRMessage.getAssertion(), requestCommunityID);

            NhinDRMessage.setResponse(callProxy(NhinDRMessage));

            LOG.debug("Calling audit log for doc retrieve response received from nhin (g0)");
            auditResponseMessage(NhinDRMessage.getResponse(), NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                    NhincConstants.AUDIT_LOG_NHIN_INTERFACE, NhinDRMessage.getAssertion(), requestCommunityID);
        } else {
            LOG.error("OutboundDocRetrieveStrategyBase.execute recieved a message which was not of type NhinDocRetrieveOrchestratableImpl_g0.");
        }
        LOG.trace("End OutboundDocRetrieveStrategyBase.execute");
    }

    protected abstract RetrieveDocumentSetResponseType callProxy(OutboundDocRetrieveOrchestratable message);

    protected void auditRequestMessage(RetrieveDocumentSetRequestType request, String direction,
            String connectInterface, AssertionType assertion, String requestCommunityID) {
        gov.hhs.fha.nhinc.common.auditlog.DocRetrieveMessageType message = new gov.hhs.fha.nhinc.common.auditlog.DocRetrieveMessageType();
        message.setRetrieveDocumentSetRequest(request);
        message.setAssertion(assertion);
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logDocRetrieve(message, direction, connectInterface,
                requestCommunityID);
        if (auditLogMsg != null) {
            auditMessage(auditLogMsg, assertion);
        }
    }

    protected void auditResponseMessage(RetrieveDocumentSetResponseType response, String direction,
            String connectInterface, AssertionType assertion, String requestCommunityID) {
        gov.hhs.fha.nhinc.common.auditlog.DocRetrieveResponseMessageType message = new gov.hhs.fha.nhinc.common.auditlog.DocRetrieveResponseMessageType();
        message.setRetrieveDocumentSetResponse(response);
        message.setAssertion(assertion);
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logDocRetrieveResult(message, direction, connectInterface,
                requestCommunityID);
        if (auditLogMsg != null) {
            auditMessage(auditLogMsg, assertion);
        }
    }

    protected AcknowledgementType auditMessage(LogEventRequestType auditLogMsg, AssertionType assertion) {
        AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
        AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
        return proxy.auditLog(auditLogMsg, assertion);
    }

}
