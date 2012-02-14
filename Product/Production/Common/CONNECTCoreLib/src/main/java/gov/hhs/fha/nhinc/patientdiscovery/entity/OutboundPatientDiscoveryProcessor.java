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
package gov.hhs.fha.nhinc.patientdiscovery.entity;

import gov.hhs.fha.nhinc.gateway.executorservice.ExecutorServiceHelper;
import gov.hhs.fha.nhinc.orchestration.OutboundResponseProcessor;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratableMessage;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;

import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201306Transforms;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201306Processor;
import gov.hhs.fha.nhinc.patientdiscovery.response.ResponseFactory;
import gov.hhs.fha.nhinc.patientdiscovery.response.ResponseParams;

import org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType;
import org.hl7.v3.CommunityPRPAIN201306UV02ResponseType;
import org.hl7.v3.PRPAIN201306UV02;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class handles all processing of responses for PatientDiscovery for all the different spec combinations: 1.
 * CumulativeResponse is spec a0 and an individual Response is spec a0 2. CumulativeResponse is spec a0 and an
 * individual Response is spec a1 3. CumulativeResponse is spec a1 and an individual Response is spec a0 4.
 * CumulativeResponse is spec a1 and an individual Response is spec a1
 * 
 * @author paul.eftis
 */
public class OutboundPatientDiscoveryProcessor implements OutboundResponseProcessor {

    private static Log log = LogFactory.getLog(OutboundPatientDiscoveryProcessor.class);

    private NhincConstants.GATEWAY_API_LEVEL cumulativeSpecLevel = null;
    private int count = 0;

    public OutboundPatientDiscoveryProcessor(NhincConstants.GATEWAY_API_LEVEL level) {
        cumulativeSpecLevel = level;
    }

    /**
     * Handles all processing and aggregation of individual responses from TaskExecutor execution of a NhinDelegate
     * 
     * @param individual
     * @param cumulativeResponse
     */
    @SuppressWarnings("static-access")
    public OutboundOrchestratableMessage processNhinResponse(OutboundOrchestratableMessage individual,
            OutboundOrchestratableMessage cumulativeResponse) {

        count++;
        log.debug("EntityPatientDiscoveryProcessor::processNhinResponse count=" + count);

        OutboundOrchestratableMessage response = null;
        if (cumulativeResponse == null) {
            switch (cumulativeSpecLevel) {
            case LEVEL_g0: {
                log.debug("EntityPatientDiscoveryProcessor::processNhinResponse createNewCumulativeResponse_a0");
                cumulativeResponse = OutboundPatientDiscoveryProcessorHelper
                        .createNewCumulativeResponse_a0((OutboundPatientDiscoveryOrchestratable) individual);
                break;
            }
            case LEVEL_g1: {
                log.debug("EntityPatientDiscoveryProcessor::processNhinResponse createNewCumulativeResponse_a1");
                cumulativeResponse = OutboundPatientDiscoveryProcessorHelper
                        .createNewCumulativeResponse_a1((OutboundPatientDiscoveryOrchestratable) individual);
                break;
            }
            default: {
                log.debug("EntityPatientDiscoveryProcessor::processNhinResponse unknown cumulativeSpecLevel so createNewCumulativeResponse_a1");
                cumulativeResponse = OutboundPatientDiscoveryProcessorHelper
                        .createNewCumulativeResponse_a1((OutboundPatientDiscoveryOrchestratable) individual);
                break;
            }
            }
        }

        if (individual == null) {
            // can't get here as NhinCallableRequest will always return something
            // but if we ever do, log it and return cumulativeResponse passed in
            log.error("EntityPatientDiscoveryProcessor::handleNhinResponse individual received was null!!!");
            response = cumulativeResponse;
        } else {
            OutboundOrchestratableMessage individualResponse = processResponse(individual);
            response = aggregateResponse((OutboundPatientDiscoveryOrchestratable) individualResponse,
                    cumulativeResponse);
        }

        return response;
    }

    /**
     * Processes the PD response: 1. Insert correlation mapping in correlations db if ResponseMode is not pass-thru 2.
     * Insert AA ID to HCID mapping in db if it is a new mapping
     * 
     * Note that all response processing exceptions are caught here and handled by returning a PD response with the
     * error/exception and hcid for response
     * 
     * @param individualResponse
     */
    @SuppressWarnings("static-access")
    public OutboundOrchestratableMessage processResponse(OutboundOrchestratableMessage individualResponse) {
        try {
            if (individualResponse instanceof OutboundPatientDiscoveryOrchestratable_a0) {
                log.debug("EntityPatientDiscoveryProcessor::processResponse for a0 start count=" + count);
                // process spec_a0 response
                OutboundPatientDiscoveryOrchestratable_a0 individual = (OutboundPatientDiscoveryOrchestratable_a0) individualResponse;
                OutboundPatientDiscoveryOrchestratable_a0 response = new OutboundPatientDiscoveryOrchestratable_a0(
                        null, individual.getResponseProcessor(), null, null, individual.getAssertion(),
                        individual.getServiceName(), individual.getTarget(), individual.getRequest());

                // store the correlation result and handle Trust/Verify Mode
                ProxyPRPAIN201305UVProxySecuredRequestType oProxyPRPAIN201305UVProxySecuredRequestType = new ProxyPRPAIN201305UVProxySecuredRequestType();
                oProxyPRPAIN201305UVProxySecuredRequestType.setPRPAIN201305UV02(individual.getRequest());

                NhinTargetSystemType target = new NhinTargetSystemType();
                HomeCommunityType home = new HomeCommunityType();
                home.setHomeCommunityId(individual.getTarget().getHomeCommunity().getHomeCommunityId());
                target.setHomeCommunity(home);
                target.setUrl(individual.getTarget().getUrl());
                oProxyPRPAIN201305UVProxySecuredRequestType.setNhinTargetSystem(target);

                ResponseParams params = new ResponseParams();
                params.assertion = individual.getAssertion();
                params.origRequest = oProxyPRPAIN201305UVProxySecuredRequestType;
                params.response = individual.getResponse();
                // process response (store correlation and handle trust/verify mode)
                response.setResponse(new ResponseFactory().getResponseMode().processResponse(params));
                // store the AA to HCID mapping
                new PatientDiscovery201306Processor().storeMapping(response.getResponse());
                log.debug("EntityPatientDiscoveryProcessor::processResponse a0 done count=" + count);
                return response;
            } else if (individualResponse instanceof OutboundPatientDiscoveryOrchestratable_a1) {
                // process spec_a1 response
                // currently a0 and a1 are the same for patient discovery
                log.debug("EntityPatientDiscoveryProcessor::processResponse for a1 start count=" + count);
                // process spec_a1 response
                OutboundPatientDiscoveryOrchestratable_a1 individual = (OutboundPatientDiscoveryOrchestratable_a1) individualResponse;
                OutboundPatientDiscoveryOrchestratable_a1 response = new OutboundPatientDiscoveryOrchestratable_a1(
                        null, individual.getResponseProcessor(), null, null, individual.getAssertion(),
                        individual.getServiceName(), individual.getTarget(), individual.getRequest());

                // store the correlation result and handle Trust/Verify Mode
                ProxyPRPAIN201305UVProxySecuredRequestType oProxyPRPAIN201305UVProxySecuredRequestType = new ProxyPRPAIN201305UVProxySecuredRequestType();
                oProxyPRPAIN201305UVProxySecuredRequestType.setPRPAIN201305UV02(individual.getRequest());

                NhinTargetSystemType target = new NhinTargetSystemType();
                HomeCommunityType home = new HomeCommunityType();
                home.setHomeCommunityId(individual.getTarget().getHomeCommunity().getHomeCommunityId());
                target.setHomeCommunity(home);
                target.setUrl(individual.getTarget().getUrl());
                oProxyPRPAIN201305UVProxySecuredRequestType.setNhinTargetSystem(target);

                ResponseParams params = new ResponseParams();
                params.assertion = individual.getAssertion();
                params.origRequest = oProxyPRPAIN201305UVProxySecuredRequestType;
                params.response = individual.getResponse();
                // process response (store correlation and handle trust/verify mode)
                response.setResponse(new ResponseFactory().getResponseMode().processResponse(params));
                // store the AA to HCID mapping
                new PatientDiscovery201306Processor().storeMapping(response.getResponse());
                log.debug("EntityPatientDiscoveryProcessor::processResponse a1 done count=" + count);
                return response;
            } else {
                log.error("EntityPatientDiscoveryProcessor::processResponse individualResponse received was unknown!!!");
                throw new Exception(
                        "EntityPatientDiscoveryProcessor::processResponse individualResponse received was unknown!!!");
            }
        } catch (Exception ex) {
            ExecutorServiceHelper.getInstance().outputCompleteException(ex);
            if (individualResponse instanceof OutboundPatientDiscoveryOrchestratable_a0) {
                OutboundPatientDiscoveryOrchestratable_a0 individual = (OutboundPatientDiscoveryOrchestratable_a0) individualResponse;
                OutboundOrchestratableMessage response = processErrorResponse(individual,
                        "Exception processing response.  Exception message=" + ex.getMessage());
                return response;
            } else if (individualResponse instanceof OutboundPatientDiscoveryOrchestratable_a1) {
                OutboundPatientDiscoveryOrchestratable_a1 individual = (OutboundPatientDiscoveryOrchestratable_a1) individualResponse;
                OutboundOrchestratableMessage response = processErrorResponse(individual,
                        "Exception processing response.  Exception message=" + ex.getMessage());
                return response;
            } else {
                // can do nothing if we ever get here other than return what was passed in
                return individualResponse;
            }
        }
    }

    /**
     * Aggregates an individual PD response into the cumulative response Note that all response aggregation exceptions
     * are caught here and handled by returning a PD response with the error/exception and hcid for response
     * 
     * @param individual is individual PD response
     * @param cumulative is current cumulative PD response
     * @return cumulative response with individual added
     */
    @SuppressWarnings("static-access")
    public OutboundOrchestratableMessage aggregateResponse(OutboundPatientDiscoveryOrchestratable individual,
            OutboundOrchestratableMessage cumulative) {

        try {
            if (cumulative instanceof OutboundPatientDiscoveryOrchestratable_a0) {
                // cumulative is spec_a0
                OutboundPatientDiscoveryOrchestratable_a0 cumulativeResponse = (OutboundPatientDiscoveryOrchestratable_a0) cumulative;
                if (individual instanceof OutboundPatientDiscoveryOrchestratable_a0) {
                    // individual is spec_a0 and cumulative is spec_a0, so just aggregate_a0
                    OutboundPatientDiscoveryOrchestratable_a0 individualResponse = (OutboundPatientDiscoveryOrchestratable_a0) individual;
                    aggregateResponse_a0(individualResponse, cumulativeResponse);
                } else if (individual instanceof OutboundPatientDiscoveryOrchestratable_a1) {
                    // individual is spec_a1 and cumulative is spec_a0
                    // so transform individual to spec_a0 and then aggregate_a0
                    OutboundPatientDiscoveryOrchestratable_a0 individualResponse = OutboundPatientDiscoveryProcessorHelper
                            .transformResponse_ToA0((OutboundPatientDiscoveryOrchestratable_a1) individual);
                    aggregateResponse_a0(individualResponse, cumulativeResponse);
                } else {
                    log.error("EntityPatientDiscoveryProcessor::aggregateResponse individualResponse received was unknown!!!");
                    throw new Exception(
                            "EntityPatientDiscoveryProcessor::aggregateResponse individualResponse received was unknown!!!");
                }

                OutboundPatientDiscoveryOrchestratable_a0 response = new OutboundPatientDiscoveryOrchestratable_a0(
                        null, null, null, null, cumulativeResponse.getAssertion(), cumulativeResponse.getServiceName(),
                        cumulativeResponse.getTarget(), cumulativeResponse.getRequest());
                response.setCumulativeResponse(cumulativeResponse.getCumulativeResponse());
                return response;
            } else if (cumulative instanceof OutboundPatientDiscoveryOrchestratable_a1) {
                // cumulative is spec_a1
                // currently spec_a0 is same as spec_a1
                OutboundPatientDiscoveryOrchestratable_a1 cumulativeResponse = (OutboundPatientDiscoveryOrchestratable_a1) cumulative;
                if (individual instanceof OutboundPatientDiscoveryOrchestratable_a0) {
                    // individual is spec_a0 and cumulative is spec_a1
                    // so transform individual to spec_a1 and then aggregate_a1
                    OutboundPatientDiscoveryOrchestratable_a1 individualResponse = OutboundPatientDiscoveryProcessorHelper
                            .transformResponse_ToA1((OutboundPatientDiscoveryOrchestratable_a0) individual);
                    aggregateResponse_a1(individualResponse, cumulativeResponse);
                } else if (individual instanceof OutboundPatientDiscoveryOrchestratable_a1) {
                    // individual is spec_a1 and cumulative is spec_a1, so just aggregate_a1
                    OutboundPatientDiscoveryOrchestratable_a1 individualResponse = (OutboundPatientDiscoveryOrchestratable_a1) individual;
                    aggregateResponse_a1(individualResponse, cumulativeResponse);
                } else {
                    log.error("EntityPatientDiscoveryProcessor::aggregateResponse individualResponse received was unknown!!!");
                    throw new Exception(
                            "EntityPatientDiscoveryProcessor::aggregateResponse individualResponse received was unknown!!!");
                }

                OutboundPatientDiscoveryOrchestratable_a1 response = new OutboundPatientDiscoveryOrchestratable_a1(
                        null, null, null, null, cumulativeResponse.getAssertion(), cumulativeResponse.getServiceName(),
                        cumulativeResponse.getTarget(), cumulativeResponse.getRequest());
                response.setCumulativeResponse(cumulativeResponse.getCumulativeResponse());
                return response;
            } else {
                log.error("EntityPatientDiscoveryProcessor::aggregateResponse cumulativeResponse received was unknown!!!");
                throw new Exception(
                        "EntityPatientDiscoveryProcessor::aggregateResponse cumulativeResponse received was unknown!!!");
            }
        } catch (Exception e) {
            ExecutorServiceHelper.getInstance().outputCompleteException(e);
            // add error response for exception to cumulativeResponse
            if (cumulative instanceof OutboundPatientDiscoveryOrchestratable_a0) {
                OutboundPatientDiscoveryOrchestratable_a0 cumulativeResponse = (OutboundPatientDiscoveryOrchestratable_a0) cumulative;

                CommunityPRPAIN201306UV02ResponseType communityResponse = new CommunityPRPAIN201306UV02ResponseType();
                NhinTargetCommunityType target = new NhinTargetCommunityType();
                HomeCommunityType home = new HomeCommunityType();
                home.setHomeCommunityId(individual.getTarget().getHomeCommunity().getHomeCommunityId());
                target.setHomeCommunity(home);
                communityResponse.setNhinTargetCommunity(target);
                PRPAIN201306UV02 pdErrorResponse = (new HL7PRPA201306Transforms()).createPRPA201306ForErrors(
                        individual.getRequest(), "Exception aggregating response from target homeId="
                                + individual.getTarget().getHomeCommunity().getHomeCommunityId()
                                + ".  Exception message=" + e.getMessage());
                communityResponse.setPRPAIN201306UV02(pdErrorResponse);
                cumulativeResponse.getCumulativeResponse().getCommunityResponse().add(communityResponse);
                return cumulativeResponse;
            } else if (cumulative instanceof OutboundPatientDiscoveryOrchestratable_a0) {
                OutboundPatientDiscoveryOrchestratable_a1 cumulativeResponse = (OutboundPatientDiscoveryOrchestratable_a1) cumulative;

                CommunityPRPAIN201306UV02ResponseType communityResponse = new CommunityPRPAIN201306UV02ResponseType();
                NhinTargetCommunityType target = new NhinTargetCommunityType();
                HomeCommunityType home = new HomeCommunityType();
                home.setHomeCommunityId(individual.getTarget().getHomeCommunity().getHomeCommunityId());
                target.setHomeCommunity(home);
                communityResponse.setNhinTargetCommunity(target);
                PRPAIN201306UV02 pdErrorResponse = (new HL7PRPA201306Transforms()).createPRPA201306ForErrors(
                        individual.getRequest(), "Exception aggregating response from target homeId="
                                + individual.getTarget().getHomeCommunity().getHomeCommunityId()
                                + ".  Exception message=" + e.getMessage());
                communityResponse.setPRPAIN201306UV02(pdErrorResponse);
                cumulativeResponse.getCumulativeResponse().getCommunityResponse().add(communityResponse);
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
        log.debug("EntityPatientDiscoveryProcessor::processErrorResponse error=" + error);
        if (request instanceof OutboundPatientDiscoveryOrchestratable_a0) {
            return processError_a0((OutboundPatientDiscoveryOrchestratable) request, error);
        } else if (request instanceof OutboundPatientDiscoveryOrchestratable_a1) {
            return processError_a1((OutboundPatientDiscoveryOrchestratable) request, error);
        } else {
            log.error("EntityPatientDiscoveryProcessor::processErrorResponse request was unknown!!!");
            return processError_a1((OutboundPatientDiscoveryOrchestratable) request, error);
        }
    }

    /**
     * Generates an OutboundPatientDiscoveryOrchestratable_a0 response with an error response for spec a0 that contains
     * target hcid that produced error as well as error string passed in
     * 
     * @param request is initial request
     * @param error is String with error message
     * @return OutboundPatientDiscoveryOrchestratable_a0 with error response
     */
    public OutboundPatientDiscoveryOrchestratable_a0 processError_a0(OutboundPatientDiscoveryOrchestratable request,
            String error) {
        log.debug("EntityPatientDiscoveryProcessor::processError_a0 error=" + error);
        OutboundPatientDiscoveryOrchestratable_a0 response = new OutboundPatientDiscoveryOrchestratable_a0(null,
                request.getResponseProcessor(), null, null, request.getAssertion(), request.getServiceName(),
                request.getTarget(), request.getRequest());
        String errStr = "Error from target homeId=" + request.getTarget().getHomeCommunity().getHomeCommunityId();
        errStr += "  The error received was " + error;
        PRPAIN201306UV02 pdErrorResponse = (new HL7PRPA201306Transforms()).createPRPA201306ForErrors(
                request.getRequest(), errStr);
        response.setResponse(pdErrorResponse);
        return response;
    }

    /**
     * Generates an OutboundPatientDiscoveryOrchestratable_a1 response with an error response for spec a0 that contains
     * target hcid that produced error as well as error string passed in
     * 
     * @param request is initial request
     * @param error is String with error message
     * @return OutboundPatientDiscoveryOrchestratable_a1 with error response
     */
    public OutboundPatientDiscoveryOrchestratable_a1 processError_a1(OutboundPatientDiscoveryOrchestratable request,
            String error) {
        log.debug("EntityPatientDiscoveryProcessor::processError_a1 error=" + error);
        OutboundPatientDiscoveryOrchestratable_a1 response = new OutboundPatientDiscoveryOrchestratable_a1(null,
                request.getResponseProcessor(), null, null, request.getAssertion(), request.getServiceName(),
                request.getTarget(), request.getRequest());

        String errStr = "Error from target homeId=" + request.getTarget().getHomeCommunity().getHomeCommunityId();
        errStr += "  The error received was " + error;
        PRPAIN201306UV02 pdErrorResponse = (new HL7PRPA201306Transforms()).createPRPA201306ForErrors(
                request.getRequest(), errStr);
        response.setResponse(pdErrorResponse);
        return response;
    }

    /**
     * aggregates an a0 spec individualResponse into an a0 spec cumulativeResponse exception will throw out and be
     * caught by aggregateResponse
     */
    @SuppressWarnings("static-access")
    private void aggregateResponse_a0(OutboundPatientDiscoveryOrchestratable_a0 individual,
            OutboundPatientDiscoveryOrchestratable_a0 cumulativeResponse) throws Exception {

        PRPAIN201306UV02 current = individual.getResponse();
        if (current != null) {
            // aggregate the response
            CommunityPRPAIN201306UV02ResponseType communityResponse = new CommunityPRPAIN201306UV02ResponseType();

            NhinTargetCommunityType target = new NhinTargetCommunityType();
            HomeCommunityType home = new HomeCommunityType();
            home.setHomeCommunityId(individual.getTarget().getHomeCommunity().getHomeCommunityId());
            target.setHomeCommunity(home);
            communityResponse.setNhinTargetCommunity(target);
            communityResponse.setPRPAIN201306UV02(current);
            cumulativeResponse.getCumulativeResponse().getCommunityResponse().add(communityResponse);
            log.debug("EntityPatientDiscoveryProcessor::aggregateResponse_a0 combine next response done cumulativeResponse count="
                    + count);
        } else {
            throw new Exception(
                    "EntityPatientDiscoveryProcessor::aggregateResponse_a0 received a null PRPAIN201306UV02 response!!!");
        }
    }

    /**
     * aggregates an a1 spec individualResponse into an a1 spec cumulativeResponse exception will throw out and be
     * caught by aggregateResponse
     */
    @SuppressWarnings("static-access")
    private void aggregateResponse_a1(OutboundPatientDiscoveryOrchestratable_a1 individual,
            OutboundPatientDiscoveryOrchestratable_a1 cumulativeResponse) throws Exception {

        PRPAIN201306UV02 current = individual.getResponse();
        if (current != null) {

            // aggregate the response
            CommunityPRPAIN201306UV02ResponseType communityResponse = new CommunityPRPAIN201306UV02ResponseType();

            NhinTargetCommunityType target = new NhinTargetCommunityType();
            HomeCommunityType home = new HomeCommunityType();
            home.setHomeCommunityId(individual.getTarget().getHomeCommunity().getHomeCommunityId());
            target.setHomeCommunity(home);
            communityResponse.setNhinTargetCommunity(target);
            communityResponse.setPRPAIN201306UV02(current);
            cumulativeResponse.getCumulativeResponse().getCommunityResponse().add(communityResponse);
            log.debug("EntityPatientDiscoveryProcessor::aggregateResponse_a1 combine next response done count=" + count);
        } else {
            throw new Exception(
                    "EntityPatientDiscoveryProcessor::aggregateResponse_a1 received a null PRPAIN201306UV02 response!!!");
        }
    }

    /**
     * NOT USED
     */
    public void aggregate(OutboundOrchestratable individualResponse, OutboundOrchestratable cumulativeResponse) {
    }
}
