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

import org.apache.log4j.Logger;

import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docretrieve.DocRetrieveFileUtils;
import gov.hhs.fha.nhinc.docretrieve.MessageGenerator;
import gov.hhs.fha.nhinc.docretrieve.adapter.proxy.AdapterDocRetrieveProxy;
import gov.hhs.fha.nhinc.docretrieve.adapter.proxy.AdapterDocRetrieveProxyObjectFactory;
import gov.hhs.fha.nhinc.docretrieve.audit.DocRetrieveAuditLogger;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

/**
 *
 * @author mweaver
 */
public class InboundDocRetrieveStrategyImpl implements InboundDocRetrieveStrategy {

    private static final Logger LOG = Logger.getLogger(InboundDocRetrieveStrategyImpl.class);
    AdapterDocRetrieveProxy proxy;
    DocRetrieveAuditLogger docRetrieveLogger;

    /**
     * Default constructor.
     */
    public InboundDocRetrieveStrategyImpl() {
        proxy = new AdapterDocRetrieveProxyObjectFactory().getAdapterDocRetrieveProxy();
        docRetrieveLogger = new DocRetrieveAuditLogger();
    }

    /**
     * DI constructor.
     *
     * @param proxy AdapterDocRetrieveProxy
     * @param auditLogger the auditLogger
     */
    InboundDocRetrieveStrategyImpl(AdapterDocRetrieveProxy proxy, DocRetrieveAuditLogger auditLogger) {
        super();
        this.proxy = proxy;
        this.docRetrieveLogger = auditLogger;
    }

    @Override
    public void execute(InboundDocRetrieveOrchestratable message) {
        LOG.debug("Begin NhinDocRetrieveOrchestratableImpl_g0.process");
        if (message == null) {
            LOG.debug("NhinOrchestratable was null");
            return;
        }

        if (!message.isPassthru()) {
            auditOutboundRequestMessage(message);
        }

        RetrieveDocumentSetResponseType adapterResponse = sendToAdapter(message);

        message.setResponse(adapterResponse);

        if (!message.isPassthru()) {
            auditInboundResponseMessage(message);
        }

        LOG.debug("End NhinDocRetrieveOrchestratableImpl_g0.process");
    }

    /**
     * @param message
     * @return
     */
    public RetrieveDocumentSetResponseType sendToAdapter(InboundDocRetrieveOrchestratable message) {
        RetrieveDocumentSetResponseType adapterResponse = proxy.retrieveDocumentSet(message.getRequest(),
            message.getAssertion());

        try {
            DocRetrieveFileUtils.getInstance().convertFileLocationToDataIfEnabled(adapterResponse);
        } catch (Exception e) {
            LOG.error("Failed to retrieve data from the file uri in the payload." + e.getLocalizedMessage(), e);
            adapterResponse = MessageGenerator.getInstance().createRegistryResponseError(
                "Adapter Document Retrieve Processing");
        }
        return adapterResponse;
    }

    /**
     * Creates the log event for response.
     *
     * @param message
     */
    public void auditInboundResponseMessage(InboundDocRetrieveOrchestratable message) {

        LOG.debug("Calling audit log for doc retrieve response received from adapter (a0)");
        docRetrieveLogger.auditResponseMessage(message.getRequest(), message.getResponse(), message.getAssertion(),
            null, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE,
            Boolean.FALSE, message.getWebContextProperties(), NhincConstants.DOC_RETRIEVE_SERVICE_NAME);
    }

    /**
     * Creates the log event for request.
     *
     * @param message
     */
    public void auditOutboundRequestMessage(InboundDocRetrieveOrchestratable message) {

        LOG.debug("Calling audit log for doc retrieve request (g0) sent to adapter (a0)");
        docRetrieveLogger.auditRequestMessage(message.getRequest(), message.getAssertion(), null,
            NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, Boolean.FALSE,
            message.getWebContextProperties(), NhincConstants.DOC_RETRIEVE_SERVICE_NAME);
    }

    private AcknowledgementType auditMessage(LogEventRequestType auditLogMsg, AssertionType assertion) {
        AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
        AuditRepositoryProxy auditRepositoryProxy = auditRepoFactory.getAuditRepositoryProxy();
        return auditRepositoryProxy.auditLog(auditLogMsg, assertion);
    }

    @Override
    public void execute(Orchestratable message) {
        if (message instanceof InboundDocRetrieveOrchestratable) {
            execute((InboundDocRetrieveOrchestratable) message);
        }
    }
}
