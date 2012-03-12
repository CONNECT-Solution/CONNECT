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

import gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author bhumphrey/paul
 * 
 */
public abstract class OutboundDocQueryStrategy implements OrchestrationStrategy {

    private static Log log = LogFactory.getLog(OutboundDocQueryStrategy.class);

    private Log getLogger() {
        return log;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy#execute(gov.hhs.fha.nhinc.orchestration.Orchestratable)
     */
    @Override
    public void execute(Orchestratable message) {
        if (message instanceof OutboundDocQueryOrchestratable) {
            execute((OutboundDocQueryOrchestratable) message);
        } else {
            // shouldn't get here
            getLogger().debug("OutboundDocQueryStrategy Orchestratable was not an OutboundDocQueryOrchestratable!!!");
            // throw new
            // Exception("OutboundDocQueryStrategy input message was not an OutboundDocQueryOrchestratable!!!");
        }
    }

    abstract public void execute(OutboundDocQueryOrchestratable message);

    protected void auditRequestMessage(AdhocQueryRequest request, String direction, String connectInterface,
            AssertionType assertion, String requestCommunityID) {

        gov.hhs.fha.nhinc.common.auditlog.AdhocQueryMessageType message = new gov.hhs.fha.nhinc.common.auditlog.AdhocQueryMessageType();
        message.setAdhocQueryRequest(request);
        message.setAssertion(assertion);
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logAdhocQuery(message, direction, connectInterface, requestCommunityID);
        if (auditLogMsg != null) {
            auditMessage(auditLogMsg, assertion);
        }
    }

    protected void auditResponseMessage(AdhocQueryResponse response, String direction, String connectInterface,
            AssertionType assertion, String requestCommunityID) {
        gov.hhs.fha.nhinc.common.auditlog.AdhocQueryResponseMessageType message = new gov.hhs.fha.nhinc.common.auditlog.AdhocQueryResponseMessageType();
        message.setAdhocQueryResponse(response);
        message.setAssertion(assertion);
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logAdhocQueryResult(message, direction, connectInterface, requestCommunityID);
        if (auditLogMsg != null) {
            auditMessage(auditLogMsg, assertion);
        }
    }

    private AcknowledgementType auditMessage(LogEventRequestType auditLogMsg, AssertionType assertion) {
        AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
        AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
        return proxy.auditLog(auditLogMsg, assertion);
    }

}
