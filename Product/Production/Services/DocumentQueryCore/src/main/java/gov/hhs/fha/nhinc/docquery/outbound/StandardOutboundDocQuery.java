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
package gov.hhs.fha.nhinc.docquery.outbound;

import gov.hhs.fha.nhinc.aspect.OutboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docquery.DQMessageGeneratorUtils;
import gov.hhs.fha.nhinc.docquery.DocQueryPolicyChecker;
import gov.hhs.fha.nhinc.docquery.aspect.AdhocQueryRequestDescriptionBuilder;
import gov.hhs.fha.nhinc.docquery.aspect.AdhocQueryResponseDescriptionBuilder;
import gov.hhs.fha.nhinc.docquery.audit.DocQueryAuditLogger;
import gov.hhs.fha.nhinc.docquery.entity.AggregationService;
import gov.hhs.fha.nhinc.docquery.entity.AggregationStrategy;
import gov.hhs.fha.nhinc.docquery.entity.OutboundDocQueryAggregate;
import gov.hhs.fha.nhinc.docquery.entity.OutboundDocQueryAggregator;
import gov.hhs.fha.nhinc.docquery.entity.OutboundDocQueryOrchestratable;
import gov.hhs.fha.nhinc.document.DocumentConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import static gov.hhs.fha.nhinc.util.CoreHelpUtils.logInfoServiceProcess;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.util.ArrayList;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandardOutboundDocQuery implements OutboundDocQuery {

    private static final Logger LOG = LoggerFactory.getLogger(StandardOutboundDocQuery.class);
    private AggregationStrategy strategy;
    private AggregationService fanoutService;
    private DocQueryAuditLogger auditLogger = null;
    private DocQueryPolicyChecker policyChecker;

    /**
     * Add default constructor that is used by test cases Note that implementations should always use constructor that
     * takes the executor services as input.
     */
    public StandardOutboundDocQuery() {
        this(new AggregationStrategy(), new AggregationService(), new DocQueryPolicyChecker());
    }

    /**
     * @param strategy
     */
    StandardOutboundDocQuery(AggregationStrategy strategy, AggregationService fanoutService,
        DocQueryPolicyChecker policyChecker) {
        super();
        this.strategy = strategy;
        this.fanoutService = fanoutService;
        this.policyChecker = policyChecker;
    }

    /**
     *
     * @param adhocQueryRequest The AdhocQueryRequest message received.
     * @param assertion Assertion received.
     * @param targets Target to send request.
     * @return AdhocQueryResponse from Entity Interface.
     */
    @Override
    @OutboundProcessingEvent(beforeBuilder = AdhocQueryRequestDescriptionBuilder.class, afterReturningBuilder
        = AdhocQueryResponseDescriptionBuilder.class, serviceType = "Document Query", version = "")
    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest adhocQueryRequest,
        AssertionType assertion, NhinTargetCommunitiesType targets) {
        logInfoServiceProcess(this.getClass());

        LOG.trace("EntityDocQueryOrchImpl.respondingGatewayCrossGatewayQuery...");
        AdhocQueryResponse response;

        OutboundDocQueryAggregate aggregate = new OutboundDocQueryAggregate();

        List<OutboundOrchestratable> aggregateRequests = fanoutService.createChildRequests(adhocQueryRequest,
            DQMessageGeneratorUtils.getInstance().generateMessageId(assertion), targets);

        if (aggregateRequests.isEmpty()) {
            LOG.info("no patient correlation found.");
            response = createErrorResponse("XDSUnknownPatientId", "No patient correlations found.");
        } else {
            OutboundDocQueryOrchestratable request = new OutboundDocQueryOrchestratable(
                new OutboundDocQueryAggregator(), assertion, NhincConstants.DOC_QUERY_SERVICE_NAME,
                adhocQueryRequest);

            aggregate.setRequest(request);

            // Policy check
            List<OutboundOrchestratable> permittedPerPolicy = new ArrayList<>();
            for (OutboundOrchestratable o : aggregateRequests) {
                if (o instanceof OutboundDocQueryOrchestratable) {
                    AdhocQueryRequest docQueryRequest = ((OutboundDocQueryOrchestratable) o).getRequest();
                    NhinTargetSystemType target = ((OutboundDocQueryOrchestratable) o).getTarget();
                    if (policyCheck(((OutboundDocQueryOrchestratable) o).getRequest(), o.getAssertion())) {
                        permittedPerPolicy.add(o);
                        auditRequest(docQueryRequest, o.getAssertion(), target);
                    }
                }
            }

            if (!permittedPerPolicy.isEmpty()) {
                aggregate.setAggregateRequests(permittedPerPolicy);

                strategy.execute(aggregate);

                response = request.getResponse();
            } else {
                response = new AdhocQueryResponse();
                response.setStatus(NhincConstants.NHINC_ADHOC_QUERY_SUCCESS_RESPONSE);
            }
        }

        return response;

    }

    private boolean policyCheck(AdhocQueryRequest message, AssertionType assertion) {
        return policyChecker.checkOutgoingPolicy(message, assertion);
    }

    protected DocQueryAuditLogger getAuditLogger() {
        if (auditLogger == null) {
            auditLogger = new DocQueryAuditLogger();
        }
        return auditLogger;
    }

    /**
     * This method returns Response with RegistryErrorList if there is any failure.
     *
     * @param errorCode The ErrorCode that needs to be set to the AdhocQueryResponse (Errorcodes are defined in spec).
     * @param codeContext The codecontext defines the reason of failure of AdhocQueryRequest.
     * @return AdhocQueryResponse.
     */
    private static AdhocQueryResponse createErrorResponse(String errorCode, String codeContext) {
        return DQMessageGeneratorUtils.getInstance().createAdhocQueryErrorResponse(codeContext, errorCode,
            DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE);
    }

    private void auditRequest(AdhocQueryRequest request, AssertionType assertion, NhinTargetSystemType target) {
        getAuditLogger().auditRequestMessage(request, assertion, target,
            NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
            Boolean.TRUE, null, NhincConstants.DOC_QUERY_SERVICE_NAME);

    }

    protected String getSenderHcid() {
        return HomeCommunityMap.getLocalHomeCommunityId();
    }
}
