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
package gov.hhs.fha.nhinc.corex12.ds.genericbatch.common.entity;

import gov.hhs.fha.nhinc.corex12.ds.genericbatch.request.entity.*;
import gov.hhs.fha.nhinc.corex12.ds.genericbatch.response.entity.OutboundX12BatchResponseOrchestrationContextBuilder_g0;
import gov.hhs.fha.nhinc.corex12.ds.orchestration.OrchestrationContextFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author cmay, svalluripalli
 */
public abstract class OutboundX12BatchDelegate implements OutboundDelegate {

    private static final Logger LOG = LoggerFactory.getLogger(OutboundX12BatchDelegate.class);

    @Override
    public OutboundOrchestratable process(OutboundOrchestratable message) {
        if (message instanceof OutboundX12BatchOrchestratable) {
            LOG.trace("Preparing to process CORE X12 DS GenericBatch Orchestratable");

            OutboundX12BatchOrchestratable dsMessage = (OutboundX12BatchOrchestratable) message;

            OrchestrationContextBuilder contextBuilder = OrchestrationContextFactory.getInstance()
                .getBuilder(dsMessage.getTarget().getHomeCommunity(), getServiceName());

            if (contextBuilder instanceof OutboundX12BatchRequestOrchestrationContextBuilder_g0) {
                ((OutboundX12BatchRequestOrchestrationContextBuilder_g0) contextBuilder).init(message);
            } else if (contextBuilder instanceof OutboundX12BatchResponseOrchestrationContextBuilder_g0) {
                ((OutboundX12BatchResponseOrchestrationContextBuilder_g0) contextBuilder).init(message);
            } else {
                return null;
            }

            return (OutboundOrchestratable) contextBuilder.build().execute();
        }

        LOG.error("Message is not an instance of OutboundCORE_X12DSGenericBatchOrchestratable!");
        return null;
    }

    @Override
    public void createErrorResponse(OutboundOrchestratable message, String error) {
        // TODO Auto-generated method stub
    }

    /**
     * TODO: This method should be handled by OutboundDelegate, not here.
     *
     * @param message
     * @return
     */
    @Override
    public Orchestratable process(Orchestratable message) {
        if (message instanceof OutboundOrchestratable) {
            return process((OutboundOrchestratable) message);
        }

        return null;
    }

    protected abstract NhincConstants.NHIN_SERVICE_NAMES getServiceName();
}
