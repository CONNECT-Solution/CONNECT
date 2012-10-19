/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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
package gov.hhs.fha.nhinc.docretrieve.passthru;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docretrieve.DocRetrieveFileUtils;
import gov.hhs.fha.nhinc.docretrieve.MessageGenerator;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundDocRetrieveDelegate;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundDocRetrieveOrchestratableImpl;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Neil Webb
 */
public class NhincProxyDocRetrieveOrchImpl {

    private Log log = null;

    public NhincProxyDocRetrieveOrchImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(
            RetrieveDocumentSetRequestType request, AssertionType assertion, NhinTargetSystemType targetSystem) {
        log.debug("Begin NhincProxyDocRetrieveOrchImpl.respondingGatewayCrossGatewayRetrieve(...)");
        RetrieveDocumentSetResponseType response = null;

        // Note: auditing occurs in the orchestrator strategy.
        OutboundDocRetrieveDelegate delegate = new OutboundDocRetrieveDelegate();
        OutboundDocRetrieveOrchestratableImpl message = new OutboundDocRetrieveOrchestratableImpl(request, assertion,
                null, null, delegate, null, targetSystem);
        OutboundPassthruDocRetrieveOrchestratorImpl orchestrator = new OutboundPassthruDocRetrieveOrchestratorImpl();

        OutboundDocRetrieveOrchestratableImpl orchResponse = (OutboundDocRetrieveOrchestratableImpl) orchestrator
                .process(message);
        response = orchResponse.getResponse();

        try {
            DocRetrieveFileUtils.getInstance().streamDocumentsToFileSystemIfEnabled(response);
        } catch (IOException ioe) {
            log.error("Failed to save documents to file system.", ioe);
            response = MessageGenerator.getInstance().createRegistryResponseError(
                    "Adapter Document Retrieve Processing: " + ioe.getLocalizedMessage());
        }

        log.debug("End NhincProxyDocRetrieveOrchImpl.respondingGatewayCrossGatewayRetrieve(...)");
        return response;
    }
}
