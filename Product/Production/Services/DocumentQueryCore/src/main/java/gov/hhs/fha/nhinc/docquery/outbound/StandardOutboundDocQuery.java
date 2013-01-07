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
package gov.hhs.fha.nhinc.docquery.outbound;

import gov.hhs.fha.nhinc.aspect.OutboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.docquery.aspect.AdhocQueryRequestDescriptionBuilder;
import gov.hhs.fha.nhinc.docquery.aspect.AdhocQueryResponseDescriptionBuilder;
import gov.hhs.fha.nhinc.docquery.entity.AggregationService;
import gov.hhs.fha.nhinc.docquery.entity.AggregationStrategy;
import gov.hhs.fha.nhinc.docquery.entity.OutboundDocQueryAggregate;
import gov.hhs.fha.nhinc.docquery.entity.OutboundDocQueryAggregator;
import gov.hhs.fha.nhinc.docquery.entity.OutboundDocQueryOrchestratable;
import gov.hhs.fha.nhinc.gateway.aggregator.document.DocumentConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;

import java.util.List;
import java.util.concurrent.ExecutorService;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;

import org.apache.log4j.Logger;

public class StandardOutboundDocQuery implements OutboundDocQuery {

    private static final Logger LOG = Logger.getLogger(StandardOutboundDocQuery.class);
    private AggregationStrategy strategy;
    private AggregationService fanoutService;
    

    /**
     * Add default constructor that is used by test cases Note that implementations should always use constructor that
     * takes the executor services as input.
     */
    public StandardOutboundDocQuery() {
        strategy = new AggregationStrategy();
        fanoutService = new AggregationService();
    }

    /**
     * @param strategy
     */
    StandardOutboundDocQuery(AggregationStrategy strategy, AggregationService fanoutService) {
        super();
        this.strategy = strategy;
        this.fanoutService = fanoutService;
    }

    /**
     * We construct the orch impl class with references to both executor services that could be used for this particular
     * orchestration instance. Determination of which executor service to use (largejob or regular) is based on the size
     * of the correlationsResult and configs.
     * 
     * @param e regular executor.
     * @param le largejob executor.
     */
    public StandardOutboundDocQuery(ExecutorService e, ExecutorService le) {
    }

    /**
     * 
     * @param adhocQueryRequest The AdhocQueryRequest message received.
     * @param assertion Assertion received.
     * @param targets Target to send request.
     * @return AdhocQueryResponse from Entity Interface.
     */
    @Override
    @OutboundProcessingEvent(beforeBuilder = AdhocQueryRequestDescriptionBuilder.class, afterReturningBuilder = AdhocQueryResponseDescriptionBuilder.class, serviceType = "Document Query", version = "")
    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest adhocQueryRequest,
            AssertionType assertion, NhinTargetCommunitiesType targets) {
        LOG.trace("EntityDocQueryOrchImpl.respondingGatewayCrossGatewayQuery...");

        OutboundDocQueryAggregate aggregate = new OutboundDocQueryAggregate();

        List<OutboundOrchestratable> aggregateRequests = fanoutService
                .createChildRequests(adhocQueryRequest, assertion, targets);

        if (aggregateRequests.size() == 0) {
            LOG.info("no patient correlation found.");
            return createErrorResponse("XDSUnknownPatientId", "No patient correlations found.");
        }
        
        OutboundDocQueryOrchestratable request = new OutboundDocQueryOrchestratable(new OutboundDocQueryAggregator(),
                assertion, NhincConstants.DOC_QUERY_SERVICE_NAME, adhocQueryRequest);

        aggregate.setRequest(request);

        aggregate.setAggregateRequests(aggregateRequests);

        strategy.execute(aggregate);

        return request.getResponse();

    }

    /**
     * This method returns RegistryErrorList for AdhocQueryResponse.
     * 
     * @param errorCode The ErrorCode that needs to be set to the AdhocQueryResponse (Errorcodes are defined in spec).
     * @param codeContext The codecontext defines the reason of failure of AdhocQueryRequest.
     * @return registryErrorList.
     */
    private RegistryErrorList createErrorListWithError(String errorCode, String codeContext) {
        RegistryErrorList regErrList = new RegistryErrorList();
        RegistryError regErr = new RegistryError();
        regErrList.getRegistryError().add(regErr);
        regErr.setCodeContext(codeContext);
        regErr.setErrorCode(errorCode);
        regErr.setSeverity(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);
        return regErrList;
    }

    /**
     * This method returns Response with RegistryErrorList if there is any failure.
     * 
     * @param errorCode The ErrorCode that needs to be set to the AdhocQueryResponse (Errorcodes are defined in spec).
     * @param codeContext The codecontext defines the reason of failure of AdhocQueryRequest.
     * @return AdhocQueryResponse.
     */
    private AdhocQueryResponse createErrorResponse(String errorCode, String codeContext) {
        AdhocQueryResponse response = new AdhocQueryResponse();
        response.setRegistryErrorList(createErrorListWithError(errorCode, codeContext));
        response.setStatus(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE);

        RegistryObjectListType regObjectList = new RegistryObjectListType();
        response.setRegistryObjectList(regObjectList);

        return response;
    }

}
