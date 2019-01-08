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
package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request;

import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.request.proxy.NhinPatientDiscoveryDeferredReqProxy;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.request.proxy.NhinPatientDiscoveryDeferredReqProxyObjectFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author akong
 *
 */
public class OutboundPatientDiscoveryDeferredRequestStrategyImpl_g0 implements OrchestrationStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(OutboundPatientDiscoveryDeferredRequestStrategyImpl_g0.class);

    public OutboundPatientDiscoveryDeferredRequestStrategyImpl_g0() {
    }

    @Override
    public void execute(Orchestratable message) {
        if (message instanceof OutboundPatientDiscoveryDeferredRequestOrchestratable) {
            execute((OutboundPatientDiscoveryDeferredRequestOrchestratable) message);
        } else {
            LOG.error("Not an OutboundPatientDiscoveryDeferredRequestOrchestratable.");
        }
    }

    public void execute(OutboundPatientDiscoveryDeferredRequestOrchestratable message) {
        LOG.debug("Begin OutboundPatientDiscoveryDeferredRequestOrchestratableImpl_g0.process");
        if (message == null) {
            LOG.debug("OutboundPatientDiscoveryDeferredRequestOrchestratable was null");
            return;
        }

        if (message instanceof OutboundPatientDiscoveryDeferredRequestOrchestratable) {
            NhinPatientDiscoveryDeferredReqProxy nhinPatientDiscovery
                = new NhinPatientDiscoveryDeferredReqProxyObjectFactory().getNhinPatientDiscoveryAsyncReqProxy();

            MCCIIN000002UV01 response = nhinPatientDiscovery.respondingGatewayPRPAIN201305UV02(message.getRequest(),
                message.getAssertion(), message.getTarget());
            message.setResponse(response);
        } else {
            LOG.error(
                "OutboundPatientDiscoveryDeferredRequestStrategyImpl_g0 received a message "
                + "which was not of type OutboundPatientDiscoveryDeferredRequestOrchestratable.");
        }
        LOG.debug("End OutboundPatientDiscoveryDeferredRequestStrategyImpl_g0.process");
    }
}
