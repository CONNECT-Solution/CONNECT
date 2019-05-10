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
package gov.hhs.fha.nhinc.deferredresults.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryResponseSecuredType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryResponseType;
import gov.hhs.fha.nhinc.deferredresults.adapter.proxy.AdapterDocQueryDeferredProxy;
import gov.hhs.fha.nhinc.deferredresults.adapter.proxy.AdapterDocQueryDeferredProxyObjectFactory;
import gov.hhs.fha.nhinc.docquery.audit.DocQueryDeferredResponseAuditLogger;
import gov.hhs.fha.nhinc.messaging.server.BaseService;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.util.MessageGeneratorUtils;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

public class EntityDeferredResultsImpl extends BaseService {

    public RegistryResponseType respondingGatewayCrossGatewayQuerySecured(
        RespondingGatewayCrossGatewayQueryResponseSecuredType request, WebServiceContext context) {
        return respondingGatewayCrossGatewayQuery(request.getAdhocQueryResponse(), getAssertion(context, null),
            getTargetFrom(request.getNhinTargetCommunities()));
    }

    public RegistryResponseType respondingGatewayCrossGatewayQueryUnsecured(RespondingGatewayCrossGatewayQueryResponseType request) {
        return respondingGatewayCrossGatewayQuery(request.getAdhocQueryResponse(), request.getAssertion(),
            getTargetFrom(request.getNhinTargetCommunities()));
    }

    private RegistryResponseType respondingGatewayCrossGatewayQuery(AdhocQueryResponse requestMsg,
        AssertionType assertion, NhinTargetSystemType target) {
        auditRequest(requestMsg, assertion, target);
        return geteAdapterProxy().respondingGatewayCrossGatewayQueryResults(requestMsg, assertion);
    }

    private AdapterDocQueryDeferredProxy geteAdapterProxy() {
        return new AdapterDocQueryDeferredProxyObjectFactory().getAdapterDocQueryProxy();
    }

    private void auditRequest(AdhocQueryResponse request, AssertionType assertion, NhinTargetSystemType target) {
        getAuditLogger().auditRequestMessage(request, assertion, target,
            NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
            Boolean.TRUE, null, NhincConstants.DOC_QUERY_DEFERRED_RESULTS_SERVICE_NAME);
    }

    private NhinTargetSystemType getTargetFrom(NhinTargetCommunitiesType communitites) {
        return MessageGeneratorUtils.getInstance().convertFirstToNhinTargetSystemType(communitites);
    }

    protected DocQueryDeferredResponseAuditLogger getAuditLogger() {
        return new DocQueryDeferredResponseAuditLogger();
    }
}
