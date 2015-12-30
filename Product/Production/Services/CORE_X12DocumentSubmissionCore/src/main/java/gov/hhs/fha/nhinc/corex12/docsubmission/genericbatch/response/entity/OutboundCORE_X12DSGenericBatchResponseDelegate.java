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
package gov.hhs.fha.nhinc.corex12.docsubmission.genericbatch.response.entity;

import gov.hhs.fha.nhinc.corex12.docsubmission.genericbatch.response.orchestration.OrchestrationContextFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author svalluripalli
 *
 */
public class OutboundCORE_X12DSGenericBatchResponseDelegate implements OutboundDelegate {

    private static final Logger LOG = LoggerFactory.getLogger(OutboundCORE_X12DSGenericBatchResponseDelegate.class);

    /**
     *
     * @param message
     * @return Orchestratable
     */
    @Override
    public Orchestratable process(Orchestratable message) {
        if (message instanceof OutboundOrchestratable) {
            return process((OutboundOrchestratable) message);
        }

        return null;
    }

    /**
     *
     * @param message
     * @return OutboundOrchestratable
     */
    @Override
    public OutboundOrchestratable process(OutboundOrchestratable message) {
        if (message instanceof OutboundCORE_X12DSGenericBatchResponseOrchestratable) {
            LOG.debug("Processing CORE X12 DS GenericBatchRequest Orchestratable");
            OutboundCORE_X12DSGenericBatchResponseOrchestratable dsMessage = (OutboundCORE_X12DSGenericBatchResponseOrchestratable) message;

            OrchestrationContextBuilder contextBuilder = getOrchestrationContextFactory().getBuilder(
                dsMessage.getTarget().getHomeCommunity(), NhincConstants.NHIN_SERVICE_NAMES.CORE_X12DS_GENERICBATCH_RESPONSE);

            if (contextBuilder instanceof OutboundCORE_X12DSGenericBatchResponseOrchestrationContextBuilder_g0) {
                ((OutboundCORE_X12DSGenericBatchResponseOrchestrationContextBuilder_g0) contextBuilder).init(message);
            } else {
                return null;
            }
            return (OutboundOrchestratable) contextBuilder.build().execute();
        }

        LOG.error("Message is not an instance of OutboundCORE_X12DSGenericBatchResponseOrchestratable!");
        return null;
    }

    /**
     *
     * @return OrchestrationContextFactory
     */
    protected OrchestrationContextFactory getOrchestrationContextFactory() {
        return OrchestrationContextFactory.getInstance();
    }

    /**
     *
     * @param message
     * @param error
     */
    @Override
    public void createErrorResponse(OutboundOrchestratable message, String error) {
        // TODO Auto-generated method stub
    }
}
