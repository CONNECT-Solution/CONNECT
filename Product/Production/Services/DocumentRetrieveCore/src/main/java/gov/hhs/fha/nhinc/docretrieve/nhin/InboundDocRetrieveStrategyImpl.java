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
package gov.hhs.fha.nhinc.docretrieve.nhin;

import gov.hhs.fha.nhinc.docretrieve.DocRetrieveFileUtils;
import gov.hhs.fha.nhinc.docretrieve.MessageGenerator;
import gov.hhs.fha.nhinc.docretrieve.adapter.proxy.AdapterDocRetrieveProxy;
import gov.hhs.fha.nhinc.docretrieve.adapter.proxy.AdapterDocRetrieveProxyObjectFactory;
import gov.hhs.fha.nhinc.docretrieve.audit.DocRetrieveAuditLogger;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mweaver
 */
public class InboundDocRetrieveStrategyImpl implements InboundDocRetrieveStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(InboundDocRetrieveStrategyImpl.class);
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

        RetrieveDocumentSetResponseType adapterResponse = sendToAdapter(message);

        message.setResponse(adapterResponse);

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

    @Override
    public void execute(Orchestratable message) {
        if (message instanceof InboundDocRetrieveOrchestratable) {
            execute((InboundDocRetrieveOrchestratable) message);
        }
    }
}
