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

import gov.hhs.fha.nhinc.gateway.aggregator.document.DocumentConstants;
import gov.hhs.fha.nhinc.gateway.executorservice.ExecutorServiceHelper;
import gov.hhs.fha.nhinc.orchestration.OutboundResponseProcessor;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratableMessage;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;

import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;

import java.math.BigInteger;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class handles all processing of responses for DocQuery for all the different spec combinations: 1. CumulativeResponse
 * is spec a0 and an individual Response is spec a0 2. CumulativeResponse is spec a0 and an individual Response is spec
 * a1 3. CumulativeResponse is spec a1 and an individual Response is spec a0 4. CumulativeResponse is spec a1 and an
 * individual Response is spec a1
 * 
 * @author paul.eftis
 */
public class OutboundDocQueryProcessor implements OutboundResponseProcessor {

    private static Log log = LogFactory.getLog(OutboundDocQueryProcessor.class);

    private NhincConstants.GATEWAY_API_LEVEL cumulativeSpecLevel = null;
    private int count = 0;

    private static final QName ExtrinsicObjectQname = new QName("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0",
            "ExtrinsicObject");

    public OutboundDocQueryProcessor() {

    }

    public OutboundDocQueryProcessor(NhincConstants.GATEWAY_API_LEVEL level) {
        cumulativeSpecLevel = level;
    }

    /**
     * Handles all processing and aggregation of individual responses from TaskExecutor execution of a NhinDelegate
     * 
     * @param individual
     * @param cumulativeResponse
     */
    public OutboundOrchestratableMessage processNhinResponse(OutboundOrchestratableMessage individual,
            OutboundOrchestratableMessage cumulativeResponse) {

        count++;
        log.debug("EntityDocQueryProcessor::processNhinResponse count=" + count);

        OutboundOrchestratableMessage response = null;
        cumulativeResponse = getCumulativeResponseBasedGLEVEL(cumulativeResponse, individual);

        if (individual == null) {
            // can't get here as NhinCallableRequest will always return something
            // but if we ever do, log it and return cumulativeResponse passed in
            log.error("EntityDocQueryProcessor::handleNhinResponse individual received was null!!!");
            response = cumulativeResponse;
        } else {
            OutboundOrchestratableMessage individualResponse = processResponse(individual);
            response = aggregateResponse((OutboundDocQueryOrchestratable) individualResponse, cumulativeResponse);
        }

        return response;
    }

    protected OutboundOrchestratableMessage getCumulativeResponseBasedGLEVEL(
            OutboundOrchestratableMessage cumulativeResponse, OutboundOrchestratableMessage individual) {
        if (cumulativeResponse == null) {
            switch (cumulativeSpecLevel) {
            case LEVEL_g0: {
                log.debug("EntityDocQueryProcessor::processNhinResponse createNewCumulativeResponse_a0");
                cumulativeResponse = OutboundDocQueryProcessorHelper
                        .createNewCumulativeResponse_a0((OutboundDocQueryOrchestratable) individual);
                break;
            }
            case LEVEL_g1: {
                log.debug("EntityDocQueryProcessor::processNhinResponse createNewCumulativeResponse_a1");
                cumulativeResponse = OutboundDocQueryProcessorHelper
                        .createNewCumulativeResponse_a1((OutboundDocQueryOrchestratable) individual);
                break;
            }
            default: {
                log.debug("EntityDocQueryProcessor::processNhinResponse unknown cumulativeSpecLevel so createNewCumulativeResponse_a1");
                cumulativeResponse = OutboundDocQueryProcessorHelper
                        .createNewCumulativeResponse_a1((OutboundDocQueryOrchestratable) individual);
                break;
            }
            }
        }
        return cumulativeResponse;

    }

    /**
     * DQ response requires no processing
     * 
     * @param individualResponse
     */
    @SuppressWarnings("static-access")
    public OutboundOrchestratableMessage processResponse(OutboundOrchestratableMessage individualResponse) {
        try {
            // DQ response requires no processing
            return individualResponse;
        } catch (Exception ex) {
            ExecutorServiceHelper.getInstance().outputCompleteException(ex);
            OutboundOrchestratableMessage response = processErrorResponse(individualResponse,
                    "Exception processing response.  Exception message=" + ex.getMessage());
            return response;
        }
    }

    /**
     * Aggregates an individual DQ response into the cumulative response Note that all response aggregation exceptions
     * are caught here and handled by returning a DQ response with the error/exception and hcid for response
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
                    log.error("EntityDocQueryProcessor::aggregateResponse individualResponse received was unknown!!!");
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
                    log.error("EntityDocQueryProcessor::aggregateResponse individualResponse received was unknown!!!");
                    throw new Exception(
                            "EntityDocQueryProcessor::aggregateResponse individualResponse received was unknown!!!");
                }

                OutboundDocQueryOrchestratable_a1 response = new OutboundDocQueryOrchestratable_a1(null, null, null,
                        null, cumulativeResponse.getAssertion(), cumulativeResponse.getServiceName(),
                        cumulativeResponse.getTarget(), cumulativeResponse.getRequest());
                response.setCumulativeResponse(cumulativeResponse.getCumulativeResponse());
                return response;
            } else {
                log.error("EntityDocQueryProcessor::aggregateResponse cumulativeResponse received was unknown!!!");
                throw new Exception(
                        "EntityDocQueryProcessor::aggregateResponse cumulativeResponse received was unknown!!!");
            }
        } catch (Exception e) {
            ExecutorServiceHelper.getInstance().outputCompleteException(e);
            // add error response for exception to cumulativeResponse
            RegistryError regErr = new RegistryError();
            regErr.setErrorCode("XDSRepositoryError");
            regErr.setCodeContext(e.getMessage());
            regErr.setValue("Exception aggregating response from target homeId="
                    + individual.getTarget().getHomeCommunity().getHomeCommunityId());
            regErr.setSeverity(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);
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
     * processError_a0 and if request is spec a1 will call processError_a1)
     * 
     * @param request is initial request
     * @param error is String with error message
     * @return
     */
    public OutboundOrchestratableMessage processErrorResponse(OutboundOrchestratableMessage request, String error) {
        log.debug("EntityDocQueryProcessor::processErrorResponse error=" + error);
        if (request instanceof OutboundDocQueryOrchestratable_a0) {
            return processError_a0((OutboundDocQueryOrchestratable) request, error);
        } else if (request instanceof OutboundDocQueryOrchestratable_a1) {
            return processError_a1((OutboundDocQueryOrchestratable) request, error);
        } else {
            log.error("EntityDocQueryProcessor::processErrorResponse request was unknown!!!");
            return processError_a1((OutboundDocQueryOrchestratable) request, error);
        }
    }

    /**
     * Generates an OutboundDocQueryOrchestratable_a0 response with an error response for spec a0 that contains target
     * hcid that produced error as well as error string passed in
     * 
     * @param request is initial request
     * @param error is String with error message
     * @return OutboundDocQueryOrchestratable_a0 with error response
     */
    public OutboundDocQueryOrchestratable_a0 processError_a0(OutboundDocQueryOrchestratable request, String error) {
        log.debug("EntityDocQueryProcessor::processError_a0 error=" + error);
        OutboundDocQueryOrchestratable_a0 response = new OutboundDocQueryOrchestratable_a0(null,
                request.getResponseProcessor(), null, null, request.getAssertion(), request.getServiceName(),
                request.getTarget(), request.getRequest());
        AdhocQueryResponse adhocresponse = new AdhocQueryResponse();
        RegistryErrorList regErrList = new RegistryErrorList();
        adhocresponse.setStatus(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE);
        RegistryError regErr = new RegistryError();
        regErr.setErrorCode("XDSRepositoryError");
        regErr.setCodeContext("Error from target homeId=" + request.getTarget().getHomeCommunity().getHomeCommunityId());
        regErr.setSeverity(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);
        regErrList.getRegistryError().add(regErr);
        adhocresponse.setRegistryErrorList(regErrList);
        response.setResponse(adhocresponse);
        return response;
    }

    /**
     * Generates an OutboundDocQueryOrchestratable_a1 response with an error response for spec a1 that contains target
     * hcid that produced error as well as error string passed in
     * 
     * @param request is initial request
     * @param error is String with error message
     * @return OutboundDocQueryOrchestratable_a1 with error response
     */
    public OutboundDocQueryOrchestratable_a1 processError_a1(OutboundDocQueryOrchestratable request, String error) {
        log.debug("EntityDocQueryProcessor::processError_a1 error=" + error);
        OutboundDocQueryOrchestratable_a1 response = new OutboundDocQueryOrchestratable_a1(null,
                request.getResponseProcessor(), null, null, request.getAssertion(), request.getServiceName(),
                request.getTarget(), request.getRequest());
        AdhocQueryResponse adhocresponse = new AdhocQueryResponse();
        RegistryErrorList regErrList = new RegistryErrorList();
        adhocresponse.setStatus(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE);
        RegistryError regErr = new RegistryError();
        regErr.setErrorCode("XDSRepositoryError");
        regErr.setCodeContext("Error from target homeId=" + request.getTarget().getHomeCommunity().getHomeCommunityId());
        regErr.setSeverity(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);
        regErrList.getRegistryError().add(regErr);
        adhocresponse.setRegistryErrorList(regErrList);
        response.setResponse(adhocresponse);
        return response;
    }

    /**
     * aggregates an a0 spec individualResponse into an a0 spec cumulativeResponse exception will throw out and be
     * caught by aggregateResponse
     */
    @SuppressWarnings("static-access")
    protected void aggregateResponse_a0(OutboundDocQueryOrchestratable_a0 individual,
            OutboundDocQueryOrchestratable_a0 cumulativeResponse) throws Exception {

        AdhocQueryResponse current = individual.getResponse();
        if (current != null) {
            // handle status first
            if (cumulativeResponse.getCumulativeResponse() != null) {
                if (cumulativeResponse.getCumulativeResponse().getStatus() == null) {
                    cumulativeResponse.getCumulativeResponse().setStatus(current.getStatus());
                } else {
                    // there are only 3 cases
                    // 1) either are partial success
                    // 2) they are different
                    // 3) they are the same (we do nothing)
                    if (cumulativeResponse.getCumulativeResponse().getStatus()
                            .equalsIgnoreCase(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS)
                            || current.getStatus().equalsIgnoreCase(
                                    DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS)) {
                        cumulativeResponse.getCumulativeResponse().setStatus(
                                DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS);
                    } else if (!cumulativeResponse.getCumulativeResponse().getStatus()
                            .equalsIgnoreCase(current.getStatus())) {
                        cumulativeResponse.getCumulativeResponse().setStatus(
                                DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS);
                    }

                }
            }
            // add the responses from registry object list
            if (current.getRegistryObjectList() != null) {
                List<JAXBElement<? extends IdentifiableType>> identifiableList = current.getRegistryObjectList()
                        .getIdentifiable();
                if (identifiableList != null && identifiableList.size() > 0) {
                    log.debug("individual Response has ObjectList");
                    if (cumulativeResponse.getCumulativeResponse().getRegistryObjectList() == null) {
                        SlotListType slots = new SlotListType();
                        cumulativeResponse.getCumulativeResponse().setResponseSlotList(slots);
                        RegistryObjectListType regObj = new RegistryObjectListType();
                        cumulativeResponse.getCumulativeResponse().setRegistryObjectList(regObj);
                    }
                    cumulativeResponse.getCumulativeResponse().getRegistryObjectList().getIdentifiable()
                            .addAll(identifiableList);
                }
            }

            // add any registry errors
            if (current.getRegistryErrorList() != null && current.getRegistryErrorList().getRegistryError() != null
                    && current.getRegistryErrorList().getRegistryError().size() > 0) {
                if (cumulativeResponse.getCumulativeResponse().getRegistryErrorList() == null
                        || cumulativeResponse.getCumulativeResponse().getRegistryErrorList().getRegistryError() == null) {
                    cumulativeResponse.getCumulativeResponse().setRegistryErrorList(new RegistryErrorList());
                }
                cumulativeResponse.getCumulativeResponse().getRegistryErrorList().getRegistryError()
                        .addAll(current.getRegistryErrorList().getRegistryError());
            }

            // add any slotlist response data
            if (current.getResponseSlotList() != null && current.getResponseSlotList().getSlot() != null
                    && current.getResponseSlotList().getSlot().size() > 0) {
                cumulativeResponse.getCumulativeResponse().getResponseSlotList().getSlot()
                        .addAll(current.getResponseSlotList().getSlot());
            }

            cumulativeResponse.getCumulativeResponse().setTotalResultCount(
                    cumulativeResponse.getCumulativeResponse().getTotalResultCount().add(BigInteger.ONE));
            log.debug("EntityDocQueryProcessor::aggregateResponse_a0 combine next response done cumulativeResponse count="
                    + cumulativeResponse.getCumulativeResponse().getTotalResultCount().toString());
        } else {
            throw new Exception(
                    "EntityDocQueryProcessor::aggregateResponse_a0 received a null AdhocQueryResponse response!!!");
        }
    }

    /**
     * aggregates an a1 spec individualResponse into an a1 spec cumulativeResponse exception will throw out and be
     * caught by aggregateResponse
     */
    @SuppressWarnings("static-access")
    private void aggregateResponse_a1(OutboundDocQueryOrchestratable_a1 individual,
            OutboundDocQueryOrchestratable_a1 cumulativeResponse) throws Exception {

        AdhocQueryResponse current = individual.getResponse();
        if (current != null) {
            // add the responses from registry object list
            if (current.getRegistryObjectList() != null) {
                List<JAXBElement<? extends IdentifiableType>> identifiableList = current.getRegistryObjectList()
                        .getIdentifiable();
                if (identifiableList != null && identifiableList.size() > 0) {
                    log.debug("individual Response has ObjectList");
                    if (cumulativeResponse.getCumulativeResponse().getRegistryObjectList() == null) {
                        SlotListType slots = new SlotListType();
                        cumulativeResponse.getCumulativeResponse().setResponseSlotList(slots);
                        RegistryObjectListType regObj = new RegistryObjectListType();
                        cumulativeResponse.getCumulativeResponse().setRegistryObjectList(regObj);
                    }
                    cumulativeResponse.getCumulativeResponse().getRegistryObjectList().getIdentifiable()
                            .addAll(identifiableList);
                }
            }

            // add any registry errors
            if (current.getRegistryErrorList() != null && current.getRegistryErrorList().getRegistryError() != null
                    && current.getRegistryErrorList().getRegistryError().size() > 0) {
                if (cumulativeResponse.getCumulativeResponse().getRegistryErrorList() == null
                        || cumulativeResponse.getCumulativeResponse().getRegistryErrorList().getRegistryError() == null) {
                    cumulativeResponse.getCumulativeResponse().setRegistryErrorList(new RegistryErrorList());
                }
                cumulativeResponse.getCumulativeResponse().getRegistryErrorList().getRegistryError()
                        .addAll(current.getRegistryErrorList().getRegistryError());
            }

            // add any slotlist response data
            if (current.getResponseSlotList() != null && current.getResponseSlotList().getSlot() != null
                    && current.getResponseSlotList().getSlot().size() > 0) {
                cumulativeResponse.getCumulativeResponse().getResponseSlotList().getSlot()
                        .addAll(current.getResponseSlotList().getSlot());
            }

            cumulativeResponse.getCumulativeResponse().setTotalResultCount(
                    cumulativeResponse.getCumulativeResponse().getTotalResultCount().add(BigInteger.ONE));
            log.debug("EntityDocQueryProcessor::aggregateResponse_a1 combine next response done cumulativeResponse count="
                    + cumulativeResponse.getCumulativeResponse().getTotalResultCount().toString());
        } else {
            throw new Exception(
                    "EntityDocQueryProcessor::aggregateResponse_a1 received a null AdhocQueryResponse response!!!");
        }
    }

    private <T extends IdentifiableType> T cast(JAXBElement<? extends IdentifiableType> identifiable, Class<T> type) {
        if ((identifiable.getDeclaredType() == type) || identifiable.getValue().getClass() == type) {
            return type.cast(identifiable.getValue());
        }
        return null;
    }

    /**
     * NOT USED
     */
    public void aggregate(OutboundOrchestratable individualResponse, OutboundOrchestratable cumulativeResponse) {
    }
}
