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

import gov.hhs.fha.nhinc.docquery.MessageGeneratorUtils;
import gov.hhs.fha.nhinc.gateway.aggregator.document.DocumentConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratableMessage;
import gov.hhs.fha.nhinc.orchestration.OutboundResponseProcessor;

import java.math.BigInteger;
import java.util.List;

import javax.xml.bind.JAXBElement;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;

import org.apache.log4j.Logger;

/**
 * Class handles all processing of responses for DocQuery for all the different spec combinations: 1. CumulativeResponse
 * is spec a0 and an individual Response is spec a0 2. CumulativeResponse is spec a0 and an individual Response is spec
 * a1 3. CumulativeResponse is spec a1 and an individual Response is spec a0 4. CumulativeResponse is spec a1 and an
 * individual Response is spec a1
 * 
 * @author paul.eftis
 */
public class OutboundDocQueryProcessor implements OutboundResponseProcessor {

    private static final Logger LOG = Logger.getLogger(OutboundDocQueryProcessor.class);

    private NhincConstants.GATEWAY_API_LEVEL cumulativeSpecLevel = null;

    AdhocQueryResponse cumlativeResponse;

    /**
     * Default Constructor.
     */
    public OutboundDocQueryProcessor() {
        cumlativeResponse = new AdhocQueryResponse();
        cumlativeResponse.setStartIndex(BigInteger.ZERO);
        cumlativeResponse.setTotalResultCount(BigInteger.ZERO);
    }

    /**
     * @param level Gateway apiLevel passed in (g0,g1).
     */
    public OutboundDocQueryProcessor(NhincConstants.GATEWAY_API_LEVEL level) {
        cumulativeSpecLevel = level;
    }

    /**
     * Handles all processing and aggregation of individual responses from TaskExecutor execution of a NhinDelegate.
     * 
     * @param individual Individual DocQueryResponse passed.
     * @param cumulativeResponse cumulativeResponse Based on Gateway apiLevel passed.
     * @return response aggregated response of individual and cumulative Response.
     */
    public OutboundOrchestratableMessage processNhinResponse(OutboundOrchestratableMessage individual,
            OutboundOrchestratableMessage cumulativeResponse) {

        OutboundOrchestratableMessage response = null;
        cumulativeResponse = getCumulativeResponseBasedGLEVEL(cumulativeResponse, individual);

        if (individual != null) {
            OutboundOrchestratableMessage individualResponse = processResponse(individual);
            response = aggregateResponse((OutboundDocQueryOrchestratable) individualResponse, cumulativeResponse);
        }

        return response;
    }

    /**
     * This method aggregates individual and cumulative response.
     * 
     * @param cumulativeResponse cumulativeresponse passed in.
     * @param individual Individual DocQuery Response passed.
     * @return Orchestratable CumulativeResponse.
     */
    protected OutboundOrchestratableMessage getCumulativeResponseBasedGLEVEL(
            OutboundOrchestratableMessage cumulativeResponse, OutboundOrchestratableMessage individual) {
        if (cumulativeResponse == null) {
            switch (cumulativeSpecLevel) {
            case LEVEL_g0:
                LOG.debug("EntityDocQueryProcessor::processNhinResponse createNewCumulativeResponse_a0");
                cumulativeResponse = OutboundDocQueryProcessorHelper
                        .createNewCumulativeResponse_a0((OutboundDocQueryOrchestratable) individual);
                break;

            case LEVEL_g1:
                LOG.debug("EntityDocQueryProcessor::processNhinResponse createNewCumulativeResponse_a1");
                cumulativeResponse = OutboundDocQueryProcessorHelper
                        .createNewCumulativeResponse_a1((OutboundDocQueryOrchestratable) individual);
                break;

            default:
                LOG.debug("EntityDocQueryProcessor::processNhinResponse unknown cumulativeSpecLevel so "
                        + "createNewCumulativeResponse_a1");
                cumulativeResponse = OutboundDocQueryProcessorHelper
                        .createNewCumulativeResponse_a1((OutboundDocQueryOrchestratable) individual);
                break;

            }
        }
        return cumulativeResponse;

    }

    /**
     * DQ response requires no processing.
     * 
     * @param individualResponse individual reponse passed.
     * @return individualResponse if there is no Exception; else returns DQ Error Response.
     */
    @SuppressWarnings("static-access")
    public OutboundOrchestratableMessage processResponse(OutboundOrchestratableMessage individualResponse) {
        try {
            // DQ response requires no processing
            return individualResponse;
        } catch (Exception ex) {
            LOG.error(ex);
            OutboundOrchestratableMessage response = processErrorResponse(individualResponse,
                    "Exception processing response.  Exception message=" + ex.getMessage());
            return response;
        }
    }

    /**
     * Aggregates an individual DQ response into the cumulative response Note that all response aggregation exceptions
     * are caught here and handled by returning a DQ response with the error/exception and hcid for response.
     * 
     * @param individual is individual DQ response
     * @param cumulative is current cumulative DQ response
     * @return cumulative response with individual added
     */
    @SuppressWarnings("static-access")
    public OutboundOrchestratableMessage aggregateResponse(OutboundDocQueryOrchestratable individual,
            OutboundOrchestratableMessage cumulative) {

        try {
            if (cumulative instanceof OutboundDocQueryOrchestratable_a0) {
                // cumulative is spec_a0
                OutboundDocQueryOrchestratable_a0 cumulativeResponse = (OutboundDocQueryOrchestratable_a0) cumulative;
                if (individual instanceof OutboundDocQueryOrchestratable_a0) {
                    // individual is spec_a0 and cumulative is spec_a0, so just aggregate_a0
                    OutboundDocQueryOrchestratable_a0 individualResponse = (OutboundDocQueryOrchestratable_a0) individual;
                    aggregateResponse_a0(individualResponse, cumulativeResponse);
                } else if (individual instanceof OutboundDocQueryOrchestratable_a1) {
                    // individual is spec_a1 and cumulative is spec_a0
                    // so transform individual to spec_a0 and then aggregate_a0
                    OutboundDocQueryOrchestratable_a0 individualResponse = OutboundDocQueryProcessorHelper
                            .transformResponse_ToA0((OutboundDocQueryOrchestratable_a1) individual);
                    aggregateResponse_a0(individualResponse, cumulativeResponse);
                } else {
                    LOG.error("EntityDocQueryProcessor::aggregateResponse individualResponse received was unknown!!!");
                    throw new Exception(
                            "EntityDocQueryProcessor::aggregateResponse individualResponse received was unknown!!!");
                }

                OutboundDocQueryOrchestratable_a0 response = new OutboundDocQueryOrchestratable_a0(null, null, null,
                        null, cumulativeResponse.getAssertion(), cumulativeResponse.getServiceName(),
                        cumulativeResponse.getTarget(), cumulativeResponse.getRequest());
                response.setCumulativeResponse(cumulativeResponse.getCumulativeResponse());
                return response;
            } else if (cumulative instanceof OutboundDocQueryOrchestratable_a1) {
                // cumulative is spec_a1
                OutboundDocQueryOrchestratable_a1 cumulativeResponse = (OutboundDocQueryOrchestratable_a1) cumulative;
                if (individual instanceof OutboundDocQueryOrchestratable_a0) {
                    // individual is spec_a0 and cumulative is spec_a1
                    // so transform individual to spec_a1 and then aggregate_a1
                    OutboundDocQueryOrchestratable_a1 individualResponse = OutboundDocQueryProcessorHelper
                            .transformResponse_ToA1((OutboundDocQueryOrchestratable_a0) individual);
                    aggregateResponse_a1(individualResponse, cumulativeResponse);
                } else if (individual instanceof OutboundDocQueryOrchestratable_a1) {
                    // individual is spec_a1 and cumulative is spec_a1, so just aggregate_a1
                    OutboundDocQueryOrchestratable_a1 individualResponse = (OutboundDocQueryOrchestratable_a1) individual;
                    aggregateResponse_a1(individualResponse, cumulativeResponse);
                } else {
                    LOG.error("EntityDocQueryProcessor::aggregateResponse individualResponse received was unknown!!!");
                    throw new Exception(
                            "EntityDocQueryProcessor::aggregateResponse individualResponse received was unknown!!!");
                }

                OutboundDocQueryOrchestratable_a1 response = new OutboundDocQueryOrchestratable_a1(null, null, null,
                        null, cumulativeResponse.getAssertion(), cumulativeResponse.getServiceName(),
                        cumulativeResponse.getTarget(), cumulativeResponse.getRequest());
                response.setCumulativeResponse(cumulativeResponse.getCumulativeResponse());
                return response;
            } else {
                LOG.error("EntityDocQueryProcessor::aggregateResponse cumulativeResponse received was unknown!!!");
                throw new Exception(
                        "EntityDocQueryProcessor::aggregateResponse cumulativeResponse received was unknown!!!");
            }
        } catch (Exception e) {
            LOG.error(e);
            // add error response for exception to cumulativeResponse
            RegistryError regErr = MessageGeneratorUtils.getInstance().createRegistryError(e.getMessage(), "XDSRepositoryError");
            
            if (cumulative instanceof OutboundDocQueryOrchestratable_a0) {
                OutboundDocQueryOrchestratable_a0 cumulativeResponse = (OutboundDocQueryOrchestratable_a0) cumulative;
                if (cumulativeResponse.getCumulativeResponse().getRegistryErrorList() == null) {
                    cumulativeResponse.getCumulativeResponse().setRegistryErrorList(new RegistryErrorList());
                }
                cumulativeResponse.getCumulativeResponse().getRegistryErrorList().getRegistryError().add(regErr);
                return cumulativeResponse;
            } else if (cumulative instanceof OutboundDocQueryOrchestratable_a1) {
                OutboundDocQueryOrchestratable_a1 cumulativeResponse = (OutboundDocQueryOrchestratable_a1) cumulative;
                if (cumulativeResponse.getCumulativeResponse().getRegistryErrorList() == null) {
                    cumulativeResponse.getCumulativeResponse().setRegistryErrorList(new RegistryErrorList());
                }
                cumulativeResponse.getCumulativeResponse().getRegistryErrorList().getRegistryError().add(regErr);
                return cumulativeResponse;
            } else {
                // can do nothing if we ever get here other than return what was passed in
                return cumulative;
            }
        }
    }

    /**
     * General error handler that calls appropriate error handler based on request (i.e. if request is spec a0 will call
     * processError_a0 and if request is spec a1 will call processError_a1).
     * 
     * @param request is initial request
     * @param error is String with error message
     * @return ErrorResponse (a0 if specLevel is a0, a1 if specLevel is a1).
     */
    public OutboundOrchestratableMessage processErrorResponse(OutboundOrchestratableMessage request, String error) {
        LOG.debug("EntityDocQueryProcessor::processErrorResponse error=" + error);
        if (request instanceof OutboundDocQueryOrchestratable_a0) {
            return processError_a0((OutboundDocQueryOrchestratable) request, error);
        } else if (request instanceof OutboundDocQueryOrchestratable_a1) {
            return processError_a1((OutboundDocQueryOrchestratable) request, error);
        } else {
            LOG.error("EntityDocQueryProcessor::processErrorResponse request was unknown!!!");
            return processError_a1((OutboundDocQueryOrchestratable) request, error);
        }
    }

    /**
     * Generates an OutboundDocQueryOrchestratable_a0 response with an error response for spec a0 that contains target
     * hcid that produced error as well as error string passed in.
     * 
     * @param request is initial request
     * @param error is String with error message
     * @return OutboundDocQueryOrchestratable_a0 with error response
     */
    // CHECKSTYLE:OFF
    public OutboundDocQueryOrchestratable_a0 processError_a0(OutboundDocQueryOrchestratable request, String error) {
        // CHECKSTYLE:ON
        LOG.debug("EntityDocQueryProcessor::processError_a0 error=" + error);
        OutboundDocQueryOrchestratable_a0 response = new OutboundDocQueryOrchestratable_a0(null,
                request.getResponseProcessor(), null, null, request.getAssertion(), request.getServiceName(),
                request.getTarget(), request.getRequest());
        AdhocQueryResponse adhocresponse = MessageGeneratorUtils.getInstance().createRepositoryErrorResponse(
                error);
        response.setResponse(adhocresponse);
        return response;
    }

    /**
     * Generates an OutboundDocQueryOrchestratable_a1 response with an error response for spec a1 that contains target
     * hcid that produced error as well as error string passed in.
     * 
     * @param request is initial request.
     * @param error is String with error message.
     * @return OutboundDocQueryOrchestratable_a1 with error response.
     */
    // CHECKSTYLE:OFF
    public OutboundDocQueryOrchestratable_a1 processError_a1(OutboundDocQueryOrchestratable request, String error) {
        // CHECKSTYLE:ON
        LOG.debug("EntityDocQueryProcessor::processError_a1 error=" + error);
        OutboundDocQueryOrchestratable_a1 response = new OutboundDocQueryOrchestratable_a1(null,
                request.getResponseProcessor(), null, null, request.getAssertion(), request.getServiceName(),
                request.getTarget(), request.getRequest());
        AdhocQueryResponse adhocresponse = MessageGeneratorUtils.getInstance().createRepositoryErrorResponse(
                "Error from target homeId=" + request.getTarget().getHomeCommunity().getHomeCommunityId());
        response.setResponse(adhocresponse);
        return response;
    }

    /**
     * aggregates an a0 spec individualResponse into an a0 spec cumulativeResponse exception will throw out and be
     * caught by aggregateResponse.
     * 
     * @param individual individual response passed in (if specLevel is a0).
     * @param cumulativeResponse CumulativeResponse is passed in (if specLevel is a0).
     * @throws Exception java.lang.Exception.
     * 
     */
    @SuppressWarnings("static-access")
    // CHECKSTYLE:OFF
    protected void aggregateResponse_a0(OutboundDocQueryOrchestratable individual,
            OutboundDocQueryOrchestratable_a0 cumulativeResponse) throws Exception {
        // CHECKSTYLE:ON
        AdhocQueryResponse current = individual.getResponse();
        AdhocQueryResponse cumulativeResponse2 = cumulativeResponse.getCumulativeResponse();
        if (current != null) {

            aggregate(current, cumulativeResponse2);

        } else {
            throw new Exception(
                    "EntityDocQueryProcessor::aggregateResponse_a1 received a null AdhocQueryResponse response!!!");
        }
    }

    /**
     * @param current
     * @throws Exception
     */
    protected void collect(AdhocQueryResponse current) {
        aggregate(current, cumlativeResponse);

    }

    protected void collect(OutboundDocQueryOrchestratable individual) throws Exception {
        AdhocQueryResponse response = individual.getResponse();
        if (response == null) {
            response = MessageGeneratorUtils.getInstance().createAdhocQueryErrorResponse(
                    "received a null AdhocQueryResponse response", DocumentConstants.XDS_ERRORCODE_REPOSITORY_ERROR,
                    DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE);
        }
        collect(response);
    }

    /**
     * aggregates an a1 spec individualResponse into an a1 spec cumulativeResponse exception will throw out and be
     * caught by aggregateResponse.
     */
    @SuppressWarnings("static-access")
    // CHECKSTYLE:OFF
    private void aggregateResponse_a1(OutboundDocQueryOrchestratable individual,
            OutboundDocQueryOrchestratable_a1 cumulativeResponse) throws Exception {
        // CHECKSTYLE:ON
        AdhocQueryResponse current = individual.getResponse();
        AdhocQueryResponse cumulativeResponse2 = cumulativeResponse.getCumulativeResponse();
        if (current != null) {

            aggregate(current, cumulativeResponse2);

        } else {
            throw new Exception(
                    "EntityDocQueryProcessor::aggregateResponse_a1 received a null AdhocQueryResponse response!!!");
        }
    }

    /**
     * @param individualResponse
     * @param aggregatedResponse
     */
    protected void aggregateRegistryErrors(AdhocQueryResponse individualResponse, AdhocQueryResponse aggregatedResponse) {
        // add any registry errors
        if (individualResponse.getRegistryErrorList() != null
                && individualResponse.getRegistryErrorList().getRegistryError() != null
                && individualResponse.getRegistryErrorList().getRegistryError().size() > 0) {

            if (aggregatedResponse.getRegistryErrorList() == null
                    || aggregatedResponse.getRegistryErrorList().getRegistryError() == null) {
                aggregatedResponse.setRegistryErrorList(new RegistryErrorList());
            }
            aggregatedResponse.getRegistryErrorList().getRegistryError()
                    .addAll(individualResponse.getRegistryErrorList().getRegistryError());
        }
    }

    /**
     * @param individualResponse
     * @param aggregatedResponse
     */
    protected void aggregateSlotlistResponse(AdhocQueryResponse individualResponse,
            AdhocQueryResponse aggregatedResponse) {
        // add any slotlist response data
        SlotListType individualResponseSlotList = individualResponse.getResponseSlotList();
        SlotListType collectedResponseSlotList = aggregatedResponse.getResponseSlotList();

        if (individualResponseSlotList != null && individualResponseSlotList.getSlot() != null
                && individualResponseSlotList.getSlot().size() > 0) {

            if (collectedResponseSlotList == null) {
                collectedResponseSlotList = new SlotListType();
            }

            collectedResponseSlotList.getSlot().addAll(individualResponseSlotList.getSlot());

            aggregatedResponse.setResponseSlotList(collectedResponseSlotList);
        }
    }

    /**
     * @param individualResponse
     * @param aggregatedResponse
     */
    protected void collectRegistryObjectResponses(List<JAXBElement<? extends IdentifiableType>> identifiableList,
            AdhocQueryResponse aggregatedResponse) {
        LOG.debug("individual Response has ObjectList");
        RegistryObjectListType registryObjectList = aggregatedResponse.getRegistryObjectList();
        if (registryObjectList == null) {
            registryObjectList = new RegistryObjectListType();
            aggregatedResponse.setRegistryObjectList(registryObjectList);
        }
        registryObjectList.getIdentifiable().addAll(identifiableList);
    }

    /**
     * @param individualResponse
     * @param aggregatedResponse
     */
    protected void aggregateRegistryObjectList(AdhocQueryResponse individualResponse,
            AdhocQueryResponse aggregatedResponse) {
        // add the responses from registry object list
        RegistryObjectListType registryObjectList = individualResponse.getRegistryObjectList();
        if (registryObjectList != null) {
            List<JAXBElement<? extends IdentifiableType>> identifiableList = registryObjectList.getIdentifiable();
            collectRegistryObjectResponses(identifiableList, aggregatedResponse);
        }
    }

    /**
     * @param collectedStatus
     * @param individualStatus
     * @return
     */
    protected boolean isEitherParitalSuccess(String collectedStatus, String individualStatus) {
        return collectedStatus.equalsIgnoreCase(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS)
                || individualStatus.equalsIgnoreCase(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS);
    }

    /**
     * @param individualStatus
     * @param collectedStatus
     * @return
     */
    protected boolean areTheStatusesDifferent(String individualStatus, String collectedStatus) {
        return !collectedStatus.equalsIgnoreCase(individualStatus);
    }

    /**
     * @param collectedStatus
     * @param individualStatus
     * @return
     */
    protected String determineCollectedStatus(String individualStatus, String collectedStatus) {
        if (collectedStatus == null) {
            collectedStatus = individualStatus;
        } else {
            if (isEitherParitalSuccess(collectedStatus, individualStatus)) {
                collectedStatus = DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS;
            } else {
                if (areTheStatusesDifferent(individualStatus, collectedStatus)) {
                    collectedStatus = DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS;
                }
            }

        }
        return collectedStatus;
    }

    /**
     * @param individualResponse
     * @param aggregatedResponse
     */
    protected void aggregateStatus(AdhocQueryResponse individualResponse, AdhocQueryResponse aggregatedResponse) {
        aggregatedResponse.setStatus(determineCollectedStatus(individualResponse.getStatus(),
                aggregatedResponse.getStatus()));
    }

    /**
     * @param individualResponse
     * @param aggregatedResponse
     * @throws Exception
     */
    protected void aggregate(AdhocQueryResponse individualResponse, AdhocQueryResponse aggregatedResponse) {

        aggregateStatus(individualResponse, aggregatedResponse);

        aggregateRegistryObjectList(individualResponse, aggregatedResponse);

        aggregateRegistryErrors(individualResponse, aggregatedResponse);

        aggregateSlotlistResponse(individualResponse, aggregatedResponse);

        aggregatedResponse.setTotalResultCount(aggregatedResponse.getTotalResultCount().add(BigInteger.ONE));

        if (LOG.isDebugEnabled()) {
            LOG.debug("EntityDocQueryProcessor::aggregateResponse_a1 combine next response done cumulativeResponse "
                    + "count=" + aggregatedResponse.getTotalResultCount().toString());
        }

    }

    private <T extends IdentifiableType> T cast(JAXBElement<? extends IdentifiableType> identifiable, Class<T> type) {
        if ((identifiable.getDeclaredType() == type) || identifiable.getValue().getClass() == type) {
            return type.cast(identifiable.getValue());
        }
        return null;
    }

    /**
     * Does not process anything.
     * 
     * @param individualResponse Individual DocQuery Response.
     * @param cumulativeResponse cumulativeResponse passed in.
     * 
     */
    public void aggregate(OutboundOrchestratable individualResponse, OutboundOrchestratable cumulativeResponse) {
    }
}
