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
package gov.hhs.fha.nhinc.corex12.ds.realtime.entity;

import gov.hhs.fha.nhinc.corex12.ds.realtime.nhin.proxy.NhinX12RealTimeProxy;
import gov.hhs.fha.nhinc.corex12.ds.realtime.nhin.proxy.NhinX12RealTimeProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author cmay
 */
class OutboundX12DSRealTimeStrategyImpl_g0 implements OrchestrationStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(OutboundX12DSRealTimeStrategyImpl_g0.class);

    protected NhinX12RealTimeProxy getNhinCOREX12DSRealTimeProxy() {
        return new NhinX12RealTimeProxyObjectFactory().getNhinCOREX12DocSubmissionProxy();
    }

    @Override
    public void execute(Orchestratable message) {
        if (message instanceof OutboundX12RealTimeOrchestratable) {
            execute((OutboundX12RealTimeOrchestratable) message);
        } else {
            LOG.error("Not an OutboundCORE_X12DSRealTimeOrchestratable.");
        }
    }

    public void execute(OutboundX12RealTimeOrchestratable message) {
        LOG.trace("Begin OutboundCORE_X12DSRealTimeOrchestratableImpl_g0.process");

        message.setResponse(getNhinCOREX12DSRealTimeProxy().realTimeTransaction(message.getRequest(),
            message.getAssertion(), message.getTarget(), NhincConstants.GATEWAY_API_LEVEL.LEVEL_g0));

        LOG.trace("End OutboundCORE_X12DSRealTimeOrchestratableImpl_g0.process");
    }
}
