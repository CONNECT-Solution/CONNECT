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
package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContext;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextFactory;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author mweaver
 */
public class OutboundDocRetrieveDelegate implements OutboundDelegate {

    private static Log log = LogFactory.getLog(OutboundDocRetrieveDelegate.class);

    public OutboundDocRetrieveDelegate() {
    }

    @Override
    public Orchestratable process(Orchestratable message) {
        if (message instanceof OutboundOrchestratable) {
            return process((OutboundOrchestratable) message);
        }
        return null;
    }

    public OutboundOrchestratable process(OutboundOrchestratable message) {
        OutboundOrchestratable resp = null;
        if (message instanceof OutboundDocRetrieveOrchestratable) {
            OutboundDocRetrieveOrchestratable DRMessage = (OutboundDocRetrieveOrchestratable) message;
            try {
                OutboundDocRetrieveContextBuilder contextBuilder = (OutboundDocRetrieveContextBuilder) OrchestrationContextFactory
                        .getInstance().getBuilder(DRMessage.getTarget().getHomeCommunity(),
                                NhincConstants.NHIN_SERVICE_NAMES.DOCUMENT_RETRIEVE);

                contextBuilder.setContextMessage(message);
                OrchestrationContext context = ((OrchestrationContextBuilder) contextBuilder).build();

                resp = (OutboundOrchestratable) context.execute();
                checkResult(resp);
            } catch (Throwable t) {
                log.error("Error occured sending doc query to NHIN target: " + t.getMessage(), t);
                createErrorResponse(DRMessage, "XDSRepositoryError", "Processing NHIN Proxy document retrieve");
            }
        } else {
            getLogger().error("message is not an instance of NhinDocRetrieveOrchestratable!");
        }
        return resp;
    }

    /**
     * Check for document retrieve response result.
     * 
     * @param response
     * @return response or error response
     */
    private void checkResult(OutboundOrchestratable message) {
        if (message instanceof OutboundDocRetrieveOrchestratableImpl) {
            RetrieveDocumentSetResponseType response = ((OutboundDocRetrieveOrchestratableImpl) message).getResponse();
            if (response.getDocumentResponse() == null || response.getDocumentResponse().isEmpty()) {
                createErrorResponse(message, "XDSDocumentUniqueIdError", "Document id not found");
            }
        }
    }

    protected void createErrorResponse(OutboundOrchestratable message, String errorCode, String errorContext) {
        if (message == null) {
            getLogger().debug("NhinOrchestratable was null");
            return;
        }

        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        RegistryResponseType responseType = new RegistryResponseType();
        response.setRegistryResponse(responseType);
        responseType.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
        RegistryErrorList regErrList = new RegistryErrorList();
        responseType.setRegistryErrorList(regErrList);
        RegistryError regErr = new RegistryError();
        regErrList.getRegistryError().add(regErr);
        regErr.setCodeContext(errorContext);
        regErr.setErrorCode(errorCode);
        regErr.setSeverity("Error");
        ((OutboundDocRetrieveOrchestratableImpl) message).setResponse(response);
    }

    private Log getLogger() {
        return log;
    }
}
