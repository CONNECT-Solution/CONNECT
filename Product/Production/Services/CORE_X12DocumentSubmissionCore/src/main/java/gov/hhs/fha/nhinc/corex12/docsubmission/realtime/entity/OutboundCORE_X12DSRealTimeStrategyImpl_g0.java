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
package gov.hhs.fha.nhinc.corex12.docsubmission.realtime.entity;

import gov.hhs.fha.nhinc.corex12.docsubmission.realtime.nhin.proxy.NhinCORE_X12DSRealTimeProxy;
import gov.hhs.fha.nhinc.corex12.docsubmission.realtime.nhin.proxy.NhinCORE_X12DSRealTimeProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy;

import org.apache.log4j.Logger;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeResponse;

/**
 * @author cmay
 *
 */
class OutboundCORE_X12DSRealTimeStrategyImpl_g0 implements OrchestrationStrategy {

    private static final Logger LOG = Logger.getLogger(OutboundCORE_X12DSRealTimeStrategyImpl_g0.class);

    protected NhinCORE_X12DSRealTimeProxy getNhinCORE_X12DSRealTimeProxy() {
        return new NhinCORE_X12DSRealTimeProxyObjectFactory().getNhinCORE_X12DocSubmissionProxy();
    }

    @Override
    public void execute(Orchestratable message) {
        if (message instanceof OutboundCORE_X12DSRealTimeOrchestratable) {
            execute((OutboundCORE_X12DSRealTimeOrchestratable) message);
        } else {
            LOG.error("Not an OutboundCORE_X12DSRealTimeOrchestratable.");
        }
    }

    public void execute(OutboundCORE_X12DSRealTimeOrchestratable message) {
        LOG.trace("Begin OutboundCORE_X12DSRealTimeOrchestratableImpl_g0.process");

        NhinCORE_X12DSRealTimeProxy nhincCORE_X12DSRealTime = getNhinCORE_X12DSRealTimeProxy();
        COREEnvelopeRealTimeResponse response = nhincCORE_X12DSRealTime.realTimeTransaction(message.getRequest(),
            message.getAssertion(), message.getTarget(), NhincConstants.GATEWAY_API_LEVEL.LEVEL_g0);
        message.setResponse(response);

        LOG.trace("End OutboundCORE_X12DSRealTimeOrchestratableImpl_g0.process");
    }
}
