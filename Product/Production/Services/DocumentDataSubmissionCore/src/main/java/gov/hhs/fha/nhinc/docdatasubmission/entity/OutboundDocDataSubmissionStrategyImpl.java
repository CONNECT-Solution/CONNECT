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
package gov.hhs.fha.nhinc.docdatasubmission.entity;

import gov.hhs.fha.nhinc.docdatasubmission.nhin.proxy.NhinDocDataSubmissionProxy;
import gov.hhs.fha.nhinc.docdatasubmission.nhin.proxy.NhinDocDataSubmissionProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class OutboundDocDataSubmissionStrategyImpl implements OrchestrationStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(OutboundDocDataSubmissionStrategyImpl.class);

    protected NhinDocDataSubmissionProxy getNhinDocDataSubmissionProxy() {
        return new NhinDocDataSubmissionProxyObjectFactory().getNhinDocDataSubmissionProxy();
    }

    @Override
    public void execute(Orchestratable message) {
        if (message instanceof OutboundDocDataSubmissionOrchestratable) {
            execute((OutboundDocDataSubmissionOrchestratable) message);
        } else {
            LOG.error("Not an OutboundDocDataSubmissionOrchestratable.");
        }
    }

    public void execute(OutboundDocDataSubmissionOrchestratable message) {
        LOG.trace("Begin OutboundDocDataSubmissionOrchestratableImpl_g0.process");

        NhinDocDataSubmissionProxy nhincDocSubmission = getNhinDocDataSubmissionProxy();
        RegistryResponseType response = nhincDocSubmission.registerDocumentSetB(message.getRequest(),
            message.getAssertion(), message.getTarget(), NhincConstants.GATEWAY_API_LEVEL.LEVEL_g0);
        message.setResponse(response);

        LOG.trace("End OutboundDocDataSubmissionOrchestratableImpl_g0.process");
    }
}
