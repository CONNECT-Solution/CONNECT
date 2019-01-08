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
package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docquery.DQMessageGeneratorUtils;
import gov.hhs.fha.nhinc.docquery.audit.DocQueryAuditLogger;
import gov.hhs.fha.nhinc.docquery.nhin.proxy.NhinDocQueryProxyFactory;
import gov.hhs.fha.nhinc.docquery.nhin.proxy.NhinDocQueryProxyObjectFactory;
import gov.hhs.fha.nhinc.gateway.executorservice.ExecutorServiceHelper;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bhumphrey/paul
 *
 */
public abstract class OutboundDocQueryStrategy implements OrchestrationStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(OutboundDocQueryStrategy.class);
    private DocQueryAuditLogger auditLogger = null;
    private NhinDocQueryProxyFactory proxyFactory;
    private DQMessageGeneratorUtils messageGeneratorUtils;
    WebServiceProxyHelper webServiceProxyHelper;

    /**
     * Constructor.
     */
    OutboundDocQueryStrategy() {
        proxyFactory = new NhinDocQueryProxyObjectFactory();
        webServiceProxyHelper = new WebServiceProxyHelper();
        messageGeneratorUtils = DQMessageGeneratorUtils.getInstance();
    }

    /**
     *
     * @return
     */
    abstract protected String getServiceName();

    /**
     *
     * @return
     */
    abstract protected GATEWAY_API_LEVEL getAPILevel();

    /**
     * {@inheritDoc}
     *
     * @see
     * gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy#execute(gov.hhs.fha.nhinc.orchestration.Orchestratable)
     */
    @Override
    public void execute(Orchestratable message) {
        if (message instanceof OutboundDocQueryOrchestratable) {
            execute((OutboundDocQueryOrchestratable) message);
        } else {
            LOG.debug("OutboundDocQueryStrategy Orchestratable was not an OutboundDocQueryOrchestratable!!!");
        }
    }

    /**
     * @param message
     * @param ex
     */
    protected void handleError(OutboundDocQueryOrchestratable message, Exception ex) {
        String err = ExecutorServiceHelper.getFormattedExceptionInfo(ex, message.getTarget(), message.getServiceName());
        AdhocQueryResponse response = messageGeneratorUtils.createRepositoryErrorResponse(err);
        message.setResponse(response);
        LOG.debug("executeStrategy returning error response");
    }

    /**
     * @param message contains request message to execute.
     */
    public void execute(OutboundDocQueryOrchestratable message) {

        try {
            executeStrategy(message);
        } catch (Exception ex) {
            handleError(message, ex);
        }

    }

    /**
     * This method takes Orchestrated message request and returns response.
     *
     * @param message DocQueryOrchestartable message from Adapter level a0 passed.
     * @throws Exception
     * @throws IllegalArgumentException
     */
    public void executeStrategy(OutboundDocQueryOrchestratable message) throws IllegalArgumentException,
        Exception {

        AdhocQueryResponse response;
        response = proxyFactory.getNhinDocQueryProxy().respondingGatewayCrossGatewayQuery(message.getRequest(),
            message.getAssertion(), message.getTarget());

        message.setResponse(response);

        LOG.debug("executeStrategy returning response");

    }

    /**
     * @param target
     * @return @throws IllegalArgumentException
     * @throws Exception
     */
    public String getUrl(NhinTargetSystemType target) throws IllegalArgumentException, Exception {

        return webServiceProxyHelper.getUrlFromTargetSystemByGatewayAPILevel(target, getServiceName(), getAPILevel());
    }

    protected DocQueryAuditLogger getAuditLogger() {
        if (auditLogger == null) {
            auditLogger = new DocQueryAuditLogger();
        }
        return auditLogger;
    }

    protected void setMessageGeneratorUtils(DQMessageGeneratorUtils messageGeneratorUtils) {
        this.messageGeneratorUtils = messageGeneratorUtils;
    }
}
