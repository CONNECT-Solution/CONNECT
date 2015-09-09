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
package gov.hhs.fha.nhinc.docquery.inbound;

import gov.hhs.fha.nhinc.aspect.InboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docquery.DocQueryPolicyChecker;
import gov.hhs.fha.nhinc.docquery.MessageGeneratorUtils;
import gov.hhs.fha.nhinc.docquery.adapter.proxy.AdapterDocQueryProxy;
import gov.hhs.fha.nhinc.docquery.adapter.proxy.AdapterDocQueryProxyObjectFactory;
import gov.hhs.fha.nhinc.docquery.aspect.AdhocQueryRequestDescriptionBuilder;
import gov.hhs.fha.nhinc.docquery.aspect.AdhocQueryResponseDescriptionBuilder;
import gov.hhs.fha.nhinc.docquery.audit.DocQueryAuditLogger;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.util.Properties;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

/**
 * @author akong
 *
 */
public class StandardInboundDocQuery extends AbstractInboundDocQuery {

    private DocQueryPolicyChecker policyChecker = new DocQueryPolicyChecker();
    private AdapterDocQueryProxyObjectFactory adapterFactory = new AdapterDocQueryProxyObjectFactory();

    public StandardInboundDocQuery() {
        super();
    }

    public StandardInboundDocQuery(DocQueryPolicyChecker policyChecker,
        AdapterDocQueryProxyObjectFactory adapterFactory, DocQueryAuditLogger auditLogger) {
        this.policyChecker = policyChecker;
        this.adapterFactory = adapterFactory;
        this.auditLogger = auditLogger;
    }

    /**
     *
     * @param msg
     * @param assertion
     * @param webContextProperties
     * @return <code>AdhocQueryResponse</code>
     */
    @Override
    @InboundProcessingEvent(beforeBuilder = AdhocQueryRequestDescriptionBuilder.class,
        afterReturningBuilder = AdhocQueryResponseDescriptionBuilder.class, serviceType = "Document Query",
        version = "")
    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest msg, AssertionType assertion,
        Properties webContextProperties) {
        String senderHcid = null;
        if (msg != null) {
            senderHcid = HomeCommunityMap.getCommunityIdFromAssertion(assertion);
        }

        auditRequestFromNhin(msg, assertion, senderHcid, webContextProperties);

        AdhocQueryResponse resp = processDocQuery(msg, assertion, getLocalHomeCommunityId(), webContextProperties);

        auditResponseToNhin(msg, resp, assertion, senderHcid, webContextProperties);

        return resp;
    }

    /**
     * Forwards the AdhocQueryRequest to this agency's adapter doc query service
     *
     * @param adhocQueryRequestMsg
     * @param requestCommunityID
     * @return
     */
    @Override
    AdhocQueryResponse processDocQuery(AdhocQueryRequest msg, AssertionType assertion, String requestCommunityID,
        Properties webContextProperties) {
        AdhocQueryResponse resp = null;

        auditRequestToAdapter(msg, assertion, webContextProperties);

        if (isPolicyValid(msg, assertion)) {
            resp = sendToAdapter(msg, assertion);
        } else {
            resp = MessageGeneratorUtils.getInstance().createPolicyErrorResponse();
        }

        auditRequestFromAdapter(msg, resp, assertion, webContextProperties);

        return resp;
    }

    private boolean isPolicyValid(AdhocQueryRequest msg, AssertionType assertion) {
        return policyChecker.checkIncomingPolicy(msg, assertion);
    }

    private AdhocQueryResponse sendToAdapter(AdhocQueryRequest msg, AssertionType assertion) {
        AdapterDocQueryProxy adapterProxy = adapterFactory.getAdapterDocQueryProxy();
        return adapterProxy.respondingGatewayCrossGatewayQuery(msg, assertion);
    }

    protected String getLocalHomeCommunityId() {
        return HomeCommunityMap.getLocalHomeCommunityId();
    }

    private void auditRequestToAdapter(AdhocQueryRequest msg, AssertionType assertion,
        Properties webContextProperties) {
        auditLogger.auditRequestMessage(msg, assertion, null,
            NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE,
            Boolean.FALSE, webContextProperties, NhincConstants.DOC_QUERY_SERVICE_NAME);
    }

    private void auditRequestFromAdapter(AdhocQueryRequest request, AdhocQueryResponse response,
        AssertionType assertion, Properties webContextProperties) {
        auditLogger.auditResponseMessage(request, response, assertion, null,
            NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE,
            Boolean.FALSE, webContextProperties, NhincConstants.DOC_QUERY_SERVICE_NAME);
    }
}
