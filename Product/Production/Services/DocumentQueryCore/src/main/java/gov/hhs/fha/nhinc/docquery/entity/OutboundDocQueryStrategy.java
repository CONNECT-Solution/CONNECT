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

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.docquery.MessageGeneratorUtils;
import gov.hhs.fha.nhinc.docquery.nhin.proxy.NhinDocQueryProxyFactory;
import gov.hhs.fha.nhinc.docquery.nhin.proxy.NhinDocQueryProxyObjectFactory;
import gov.hhs.fha.nhinc.gateway.aggregator.document.DocumentConstants;
import gov.hhs.fha.nhinc.gateway.executorservice.ExecutorServiceHelper;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy;
import gov.hhs.fha.nhinc.orchestration.OutboundResponseProcessor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author bhumphrey/paul
 * 
 */
public abstract class OutboundDocQueryStrategy implements OrchestrationStrategy {

    private static final Logger LOG = Logger.getLogger(OutboundDocQueryStrategy.class);

    private AuditRepositoryLogger auditLogger;
    private NhinDocQueryProxyFactory proxyFactory;
    WebServiceProxyHelper webServiceProxyHelper;

    /**
     * Constructor.
     */
    OutboundDocQueryStrategy() {
        proxyFactory = new NhinDocQueryProxyObjectFactory();
        auditLogger = new AuditRepositoryLogger();
        webServiceProxyHelper = new WebServiceProxyHelper();
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
     * @see gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy#execute(gov.hhs.fha.nhinc.orchestration.Orchestratable)
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
        OutboundResponseProcessor processor = message.getResponseProcessor();
        message.setResponse(((OutboundDocQueryOrchestratable) processor.processErrorResponse(message, err))
                .getResponse());
        LOG.debug("executeStrategy returning error response");
    }

    /**
     * @param message contains request message to execute.
     */
    public void execute(OutboundDocQueryOrchestratable message) {

        auditRequestMessage(message.getRequest(), message.getAssertion(), message.getTarget().getHomeCommunity()
                .getHomeCommunityId());
        try {
            executeStrategy(message);
        } catch (Exception ex) {
            handleError(message, ex);
        }

        auditResponseMessage(message.getResponse(), message.getAssertion(), message.getTarget().getHomeCommunity()
                .getHomeCommunityId());
    }

    /**
     * This method takes Orchestrated message request and returns response.
     * 
     * @param message DocQueryOrchestartable message from Adapter level a0 passed.
     * @throws Exception
     * @throws ConnectionManagerException
     * @throws IllegalArgumentException
     */
    public void executeStrategy(OutboundDocQueryOrchestratable message) throws IllegalArgumentException,
            ConnectionManagerException, Exception {

        final String url = getUrl(message.getTarget());
        AdhocQueryResponse response;
        if (StringUtils.isBlank(url)) {
            response = MessageGeneratorUtils.getInstance().createRepositoryErrorResponse(
                    "Unable to find any callable targets.");
        } else {
            message.getTarget().setUrl(url);
            if (LOG.isDebugEnabled()) {
                LOG.debug("executeStrategy sending nhin doc query request to " + " target hcid="
                        + message.getTarget().getHomeCommunity().getHomeCommunityId() + " at url=" + url);
            }

            response = proxyFactory.getNhinDocQueryProxy().respondingGatewayCrossGatewayQuery(message.getRequest(),
                    message.getAssertion(), message.getTarget());
        }

        message.setResponse(response);

        LOG.debug("executeStrategy returning response");

    }

    /**
     * @param message
     * @return
     * @throws IllegalArgumentException
     * @throws ConnectionManagerException
     * @throws Exception
     */
    public String getUrl(NhinTargetSystemType target) throws IllegalArgumentException, ConnectionManagerException,
            Exception {

        return webServiceProxyHelper.getUrlFromTargetSystemByGatewayAPILevel(target, getServiceName(), getAPILevel());
    }

    private void auditMessage(LogEventRequestType auditLogMsg, AssertionType assertion) {
        if (auditLogMsg != null) {
            AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
            AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
            proxy.auditLog(auditLogMsg, assertion);
        }
    }

    /**
     * @param request The AdhocQuery Request received.
     * @param assertion Assertion received.
     * @param requestCommunityID communityId passed.
     */
    protected void auditRequestMessage(AdhocQueryRequest request, AssertionType assertion, String requestCommunityID) {
        gov.hhs.fha.nhinc.common.auditlog.AdhocQueryMessageType message = new gov.hhs.fha.nhinc.common.auditlog.AdhocQueryMessageType();
        message.setAdhocQueryRequest(request);
        message.setAssertion(assertion);
        LogEventRequestType auditLogMsg = auditLogger.logAdhocQuery(message,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
                requestCommunityID);
        auditMessage(auditLogMsg, assertion);

    }

    /**
     * @param response The AdhocQUery Response received.
     * @param assertion Assertion received.
     * @param requestCommunityID CommunityId passed.
     */
    protected void auditResponseMessage(AdhocQueryResponse response, AssertionType assertion, String requestCommunityID) {
        gov.hhs.fha.nhinc.common.auditlog.AdhocQueryResponseMessageType message = new gov.hhs.fha.nhinc.common.auditlog.AdhocQueryResponseMessageType();
        message.setAdhocQueryResponse(response);
        message.setAssertion(assertion);
        LogEventRequestType auditLogMsg = auditLogger
                .logAdhocQueryResult(message, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                        NhincConstants.AUDIT_LOG_NHIN_INTERFACE, requestCommunityID);
        auditMessage(auditLogMsg, assertion);
    }

}
