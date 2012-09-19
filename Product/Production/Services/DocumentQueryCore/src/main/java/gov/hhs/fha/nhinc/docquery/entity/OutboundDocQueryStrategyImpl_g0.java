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
package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docquery.nhin.proxy.NhinDocQueryProxy;
import gov.hhs.fha.nhinc.docquery.nhin.proxy.NhinDocQueryProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.OutboundResponseProcessor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.fha.nhinc.gateway.executorservice.ExecutorServiceHelper;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implements the DocQuery client strategy for spec g0 endpoint
 * 
 * @author paul.eftis
 */
public class OutboundDocQueryStrategyImpl_g0 extends OutboundDocQueryStrategy {

    private static Log log = LogFactory.getLog(OutboundDocQueryStrategyImpl_g0.class);

    public OutboundDocQueryStrategyImpl_g0() {

    }

    private Log getLogger() {
        return log;
    }

    /**
     * @param message contains request message to execute
     */
    @Override
    public void execute(OutboundDocQueryOrchestratable message) {
        getLogger().debug("NhinDocQueryStrategyImpl_g0::execute");
        if (message instanceof OutboundDocQueryOrchestratable_a0) {
            executeStrategy((OutboundDocQueryOrchestratable_a0) message);
        } else {
            // shouldn't get here
            getLogger()
                    .debug("NhinDocQueryStrategyImpl_g0 EntityDocQueryOrchestratable was not an EntityDocQueryOrchestratable_a0!!!");
            // throw new
            // Exception("OutboundDocQueryStrategyImpl_g0 OutboundDocQueryOrchestratable was not an OutboundDocQueryOrchestratable_a0!!!");
        }
    }

    @SuppressWarnings("static-access")
    public void executeStrategy(OutboundDocQueryOrchestratable_a0 message) {
        getLogger().debug("NhinDocQueryStrategyImpl_g0::executeStrategy");

        auditRequestMessage(message.getRequest(), NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_NHIN_INTERFACE, message.getAssertion(), message.getTarget().getHomeCommunity()
                        .getHomeCommunityId());
        try {
            NhinDocQueryProxy proxy = new NhinDocQueryProxyObjectFactory().getNhinDocQueryProxy();
            String url = (new WebServiceProxyHelper()).getUrlFromTargetSystemByGatewayAPILevel(
                    message.getTarget(), NhincConstants.DOC_QUERY_SERVICE_NAME,
                    GATEWAY_API_LEVEL.LEVEL_g0);
            message.getTarget().setUrl(url);
            getLogger().debug(
                    "NhinDocQueryStrategyImpl_g0::executeStrategy sending nhin doc query request to " + " target hcid="
                            + message.getTarget().getHomeCommunity().getHomeCommunityId() + " at url="
                            + message.getTarget().getUrl());

            message.setResponse(proxy.respondingGatewayCrossGatewayQuery(message.getRequest(), message.getAssertion(),
                    message.getTarget()));

            getLogger().debug("NhinDocQueryStrategyImpl_g0::executeStrategy returning response");
        } catch (Exception ex) {
            String err = ExecutorServiceHelper.getFormattedExceptionInfo(ex, message.getTarget(),
                    message.getServiceName());
            OutboundResponseProcessor processor = message.getResponseProcessor();
            message.setResponse(((OutboundDocQueryOrchestratable_a0) processor.processErrorResponse(message, err))
                    .getResponse());
            getLogger().debug("NhinDocQueryStrategyImpl_g0::executeStrategy returning error response");
        }
        auditResponseMessage(message.getResponse(), NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_NHIN_INTERFACE, message.getAssertion(), message.getTarget().getHomeCommunity()
                        .getHomeCommunityId());
    }

}
