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

import gov.hhs.fha.nhinc.docretrieve.nhin.proxy.NhinDocRetrieveProxy;
import gov.hhs.fha.nhinc.docretrieve.nhin.proxy.NhinDocRetrieveProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mweaver
 */
public class OutboundDocRetrieveStrategyImpl_g0 extends OutboundDocRetrieveStrategyBase implements
        OrchestrationStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(OutboundDocRetrieveStrategyImpl_g0.class);

    @Override
    protected RetrieveDocumentSetResponseType callProxy(OutboundDocRetrieveOrchestratable message) {
        LOG.trace("Creating nhin (g0) doc retrieve proxy");
        NhinDocRetrieveProxy proxy = new NhinDocRetrieveProxyObjectFactory().getNhinDocRetrieveProxy();
        LOG.trace("Sending nhin doc retrieve to nhin (g0)");
        return proxy.respondingGatewayCrossGatewayRetrieve(message.getRequest(), message.getAssertion(),
                message.getTarget(), GATEWAY_API_LEVEL.LEVEL_g0);
    }
}
