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
package gov.hhs.fha.nhinc.unsubscribe.entity;

import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy;
import gov.hhs.fha.nhinc.unsubscribe.nhin.proxy.NhinHiemUnsubscribeProxy;
import gov.hhs.fha.nhinc.unsubscribe.nhin.proxy.NhinHiemUnsubscribeProxyObjectFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;

class OutboundUnsubscribeStrategyImpl_g0 implements OrchestrationStrategy {

    private static Log log = LogFactory.getLog(OutboundUnsubscribeStrategyImpl_g0.class);

    public OutboundUnsubscribeStrategyImpl_g0() {
    }

    protected Log getLogger() {
        return log;
    }
    
    protected NhinHiemUnsubscribeProxy getNhinUnsubscribeProxy() {
        return new NhinHiemUnsubscribeProxyObjectFactory().getNhinHiemUnsubscribeProxy();
    }

    @Override
    public void execute(Orchestratable message) {
        if (message instanceof OutboundUnsubscribeOrchestratable) {
            execute((OutboundUnsubscribeOrchestratable) message);
        } else {
            getLogger().error("Not an OutboundDocSubmissionOrchestratable.");
        }
    }

    public void execute(OutboundUnsubscribeOrchestratable message) {
        getLogger().debug("Begin OutboundDocSubmissionOrchestratableImpl_g0.process");

        NhinHiemUnsubscribeProxy nhincUnsubscribe = getNhinUnsubscribeProxy();
        UnsubscribeResponse response = null;
		try {
			response = nhincUnsubscribe.unsubscribe(message.getRequest(), message.getReferenceParameters(),
			        message.getAssertion(), message.getTarget());
		} catch (Exception e) {
			log.error("Failure to process nhin Subscribe message:" + e.toString());
		}
        message.setResponse(response);

        getLogger().debug("End OutboundDocSubmissionOrchestratableImpl_g0.process");
    }
}
