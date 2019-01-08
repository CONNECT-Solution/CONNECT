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
package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.docretrieve.DocRetrieveFileUtils;
import gov.hhs.fha.nhinc.docretrieve.util.DocRetrieveStatusUtil;
import gov.hhs.fha.nhinc.document.DocumentConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import java.io.IOException;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mweaver
 */
public class OutboundDocRetrieveAggregator_a0 implements NhinAggregator {

    private static final Logger LOG = LoggerFactory.getLogger(OutboundDocRetrieveAggregator_a0.class);

    /**
     * Aggregates the message in the from Orchestrable to the to Orchestratable.
     *
     * @param to The orchestratable which contains the aggregated message
     * @param from The orchestratable which contains the message to be aggregated
     */
    @Override
    public void aggregate(OutboundOrchestratable to, OutboundOrchestratable from) {
        if (to instanceof OutboundDocRetrieveOrchestratable) {

            if (from instanceof OutboundDocRetrieveOrchestratable) {
                OutboundDocRetrieveOrchestratable aggregatedResponse = (OutboundDocRetrieveOrchestratable) to;
                OutboundDocRetrieveOrchestratable fromResponse = (OutboundDocRetrieveOrchestratable) from;

                try {
                    initializeResponse(aggregatedResponse);
                    streamDocumentsToFileSystemIfEnabled(fromResponse);
                    addAllDocuments(aggregatedResponse, fromResponse);
                } catch (IOException ioe) {
                    LOG.error("Failed to save document to file system for aggregation.", ioe);

                    fromResponse.getResponse().getRegistryResponse()
                    .setStatus(DocumentConstants.XDS_RETRIEVE_RESPONSE_STATUS_FAILURE);

                    addRegistryError(aggregatedResponse.getResponse());
                }

                setAggregatedStatus(aggregatedResponse, fromResponse);

                String fromResponseStatus = fromResponse.getResponse().getRegistryResponse().getStatus();
                if (DocRetrieveStatusUtil.isStatusFailureOrPartialFailure(fromResponseStatus)) {
                    addRegistryErrors(aggregatedResponse, fromResponse);
                }
            }

        } else {
            LOG.error("This aggregator only aggregates to OutboundDocRetrieveOrchestratable.");
        }
    }

    private static void initializeResponse(OutboundDocRetrieveOrchestratable aggregatedResponse) {
        if (aggregatedResponse.getResponse() == null) {
            aggregatedResponse.setResponse(new RetrieveDocumentSetResponseType());
        }

        if (aggregatedResponse.getResponse().getRegistryResponse() == null) {
            aggregatedResponse.getResponse().setRegistryResponse(new RegistryResponseType());
        }
    }

    private static void streamDocumentsToFileSystemIfEnabled(OutboundDocRetrieveOrchestratable orchestratable)
        throws IOException {
        DocRetrieveFileUtils fileUtils = DocRetrieveFileUtils.getInstance();
        fileUtils.streamDocumentsToFileSystemIfEnabled(orchestratable.getResponse());
    }

    private static void addAllDocuments(OutboundDocRetrieveOrchestratable toResponse,
        OutboundDocRetrieveOrchestratable fromResponse) {
        if (fromResponse.getResponse() != null) {
            toResponse.getResponse().getDocumentResponse().addAll(fromResponse.getResponse().getDocumentResponse());
        }
    }

    private static void addRegistryError(RetrieveDocumentSetResponseType response) {
        RegistryError registryError = new RegistryError();
        registryError.setErrorCode(DocumentConstants.XDS_RETRIEVE_ERRORCODE_REPOSITORY_ERROR);
        registryError.setCodeContext("Failed to save document for aggregation.");
        registryError.setSeverity(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);

        response.getRegistryResponse().getRegistryErrorList().getRegistryError().add(registryError);
    }

    private static void setAggregatedStatus(OutboundDocRetrieveOrchestratable aggregatedResponse,
        OutboundDocRetrieveOrchestratable fromResponse) {
        String status = DocRetrieveStatusUtil.setResponseStatus(fromResponse.getResponse(),
            aggregatedResponse.getResponse());
        aggregatedResponse.getResponse().getRegistryResponse().setStatus(status);
    }

    /**
     * Adds the registry error to the to Orchestrable from the from Orchestratable. If none exists, then one is created.
     *
     * @param to the Orchestratrable whose response is to be modified
     * @param from the Orchestratrable whose response's registry error is to used
     */
    private static void addRegistryErrors(OutboundDocRetrieveOrchestratable to,
        OutboundDocRetrieveOrchestratable from) {
        if (to.getResponse().getRegistryResponse().getRegistryErrorList() == null) {
            to.getResponse().getRegistryResponse().setRegistryErrorList(new RegistryErrorList());
        }

        if (containsRegistryErrors(from.getResponse())) {
            to.getResponse().getRegistryResponse().getRegistryErrorList().getRegistryError()
            .addAll(from.getResponse().getRegistryResponse().getRegistryErrorList().getRegistryError());
        } else {
            RegistryError registryError = new RegistryError();
            registryError.setErrorCode(DocumentConstants.XDS_RETRIEVE_ERRORCODE_REPOSITORY_ERROR);
            registryError.setCodeContext("Received an invalid response.");
            registryError.setSeverity(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);

            to.getResponse().getRegistryResponse().getRegistryErrorList().getRegistryError().add(registryError);
        }
    }

    private static boolean containsRegistryErrors(RetrieveDocumentSetResponseType response) {
        if (response != null && response.getRegistryResponse() != null
            && response.getRegistryResponse().getRegistryErrorList() != null
            && NullChecker.isNotNullish(response.getRegistryResponse().getRegistryErrorList().getRegistryError())) {
            return true;
        }

        return false;
    }

}
