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
package gov.hhs.fha.nhinc.patientdiscovery.entity;

import com.google.common.base.Optional;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.gateway.executorservice.ExecutorServiceHelper;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratableMessage;
import gov.hhs.fha.nhinc.orchestration.OutboundResponseProcessor;
import gov.hhs.fha.nhinc.patientdiscovery.PDMessageGeneratorUtils;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201306Processor;
import gov.hhs.fha.nhinc.patientdiscovery.response.ResponseFactory;
import gov.hhs.fha.nhinc.patientdiscovery.response.ResponseParams;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201306Transforms;
import org.apache.commons.lang.NotImplementedException;
import org.hl7.v3.CommunityPRPAIN201306UV02ResponseType;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author paul.eftis
 */
public class OutboundPatientDiscoveryProcessor implements OutboundResponseProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(OutboundPatientDiscoveryProcessor.class);

    private PDMessageGeneratorUtils msgUtils = PDMessageGeneratorUtils.getInstance();

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
     * @return
     */
    @Override
    public OutboundOrchestratableMessage processNhinResponse(OutboundOrchestratableMessage individual,
        OutboundOrchestratableMessage cumulativeResponse) {

        count++;
        LOG.debug("EntityPatientDiscoveryProcessor::processNhinResponse count={}", count);

        OutboundOrchestratableMessage response;
        if (cumulativeResponse == null) {
            switch (cumulativeSpecLevel) {
                case LEVEL_g0: {
                    LOG.debug("EntityPatientDiscoveryProcessor::processNhinResponse createNewCumulativeResponse");
                    cumulativeResponse = OutboundPatientDiscoveryProcessorHelper
                        .createNewCumulativeResponse((OutboundPatientDiscoveryOrchestratable) individual);
                    break;
                }
                default: {
                    LOG.debug("EntityPatientDiscoveryProcessor::processNhinResponse unknown cumulativeSpecLevel.");
                    cumulativeResponse = OutboundPatientDiscoveryProcessorHelper
                        .createNewCumulativeResponse((OutboundPatientDiscoveryOrchestratable) individual);
                    break;
                }
            }
        }

        if (individual == null) {
            // can't get here as NhinCallableRequest will always return something
            // but if we ever do, log it and return cumulativeResponse passed in
            LOG.error("EntityPatientDiscoveryProcessor::handleNhinResponse individual received was null!!!");
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
     * @return
     */
    public OutboundOrchestratableMessage processResponse(OutboundOrchestratableMessage individualResponse) {
        try {
            if (individualResponse instanceof OutboundPatientDiscoveryOrchestratable) {
                LOG.debug("EntityPatientDiscoveryProcessor::processResponse for start count={}", count);

                OutboundPatientDiscoveryOrchestratable individual = (OutboundPatientDiscoveryOrchestratable) individualResponse;
                OutboundPatientDiscoveryOrchestratable responseOrch = new OutboundPatientDiscoveryOrchestratable(null,
                    individual.getResponseProcessor(), null, individual.getAssertion(),
                    individual.getServiceName(), individual.getTarget(), individual.getRequest());

                ProxyPRPAIN201305UVProxySecuredRequestType request = createRequestFromOrchestratable(individual);

                PRPAIN201306UV02 response = processResponse(individual, request);

                responseOrch.setResponse(response);

                // store the AA to HCID mapping
                new PatientDiscovery201306Processor().storeMapping(responseOrch.getResponse());

                LOG.debug("EntityPatientDiscoveryProcessor::processResponse done count={}", count);
                return responseOrch;
            } else {
                LOG.error("EntityPatientDiscoveryProcessor::processResponse individualResponse received was unknown!!!");
                throw new Exception(
                    "EntityPatientDiscoveryProcessor::processResponse individualResponse received was unknown!!!");
            }
        } catch (Exception ex) {
            ExecutorServiceHelper.outputCompleteException(ex);
            if (individualResponse instanceof OutboundPatientDiscoveryOrchestratable) {
                OutboundPatientDiscoveryOrchestratable individual
                    = (OutboundPatientDiscoveryOrchestratable) individualResponse;
                return processErrorResponse(individual,
                    "Exception processing response.  Exception message=" + ex.getMessage());
            } else {
                // can do nothing if we ever get here other than return what was passed in
                return individualResponse;
            }
        }
    }

    protected PRPAIN201306UV02 processResponse(OutboundPatientDiscoveryOrchestratable orch,
        ProxyPRPAIN201305UVProxySecuredRequestType request) {

        ResponseParams params = new ResponseParams();
        params.assertion = orch.getAssertion();
        params.origRequest = request;
        params.response = orch.getResponse();

        // process response (store correlation and handle trust/verify mode)
        ResponseFactory rFactory = new ResponseFactory();
        return rFactory.getResponseMode().processResponse(params);
    }

    protected ProxyPRPAIN201305UVProxySecuredRequestType createRequestFromOrchestratable(
        OutboundPatientDiscoveryOrchestratable orch) {

        ProxyPRPAIN201305UVProxySecuredRequestType request = new ProxyPRPAIN201305UVProxySecuredRequestType();
        request.setPRPAIN201305UV02(orch.getRequest());

        NhinTargetSystemType target = new NhinTargetSystemType();
        HomeCommunityType home = new HomeCommunityType();
        home.setHomeCommunityId(orch.getTarget().getHomeCommunity().getHomeCommunityId());
        target.setHomeCommunity(home);
        target.setUrl(orch.getTarget().getUrl());
        request.setNhinTargetSystem(target);

        return request;
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
            if (cumulative instanceof OutboundPatientDiscoveryOrchestratable) {

                OutboundPatientDiscoveryOrchestratable cumulativeResponse = (OutboundPatientDiscoveryOrchestratable) cumulative;
                OutboundPatientDiscoveryOrchestratable individualResponse = individual;
                addResponseToCumulativeResponse(individualResponse, cumulativeResponse);

                OutboundPatientDiscoveryOrchestratable response = new OutboundPatientDiscoveryOrchestratable(null,
                    Optional.<OutboundResponseProcessor>absent(), null, cumulativeResponse.getAssertion(),
                    cumulativeResponse.getServiceName(), cumulativeResponse.getTarget(),
                    cumulativeResponse.getRequest());
                response.setCumulativeResponse(cumulativeResponse.getCumulativeResponse());
                return response;
            } else {
                LOG.error("EntityPatientDiscoveryProcessor::aggregateResponse cumulativeResponse received was unknown.");
                throw new Exception(
                    "EntityPatientDiscoveryProcessor::aggregateResponse cumulativeResponse received was unknown.");
            }
        } catch (Exception e) {
            ExecutorServiceHelper.getInstance().outputCompleteException(e);
            // add error response for exception to cumulativeResponse
            if (cumulative instanceof OutboundPatientDiscoveryOrchestratable) {
                OutboundPatientDiscoveryOrchestratable cumulativeResponse = (OutboundPatientDiscoveryOrchestratable) cumulative;

                String hcid = individual.getTarget().getHomeCommunity().getHomeCommunityId();
                CommunityPRPAIN201306UV02ResponseType communityResponse = createCommunityPRPAIN201306UV02ResponseType(hcid);

                PRPAIN201306UV02 pdErrorResponse = new HL7PRPA201306Transforms().createPRPA201306ForErrors(
                    individual.getRequest(), "Exception aggregating response from target homeId=" + hcid
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

    protected CommunityPRPAIN201306UV02ResponseType createCommunityPRPAIN201306UV02ResponseType(String hcid) {
        return msgUtils.createCommunityPRPAIN201306UV02ResponseType(hcid);
    }

    /**
     * General error handler that calls appropriate error handler based on request
     *
     * @param request is initial request
     * @param error is String with error message
     * @return
     */
    @Override
    public OutboundOrchestratableMessage processErrorResponse(OutboundOrchestratableMessage request, String error) {
        LOG.debug("EntityPatientDiscoveryProcessor::processErrorResponse error={}", error);
        return processError((OutboundPatientDiscoveryOrchestratable) request, error);
    }

    /**
     * Generates an OutboundPatientDiscoveryOrchestratable response with an error response that contains target hcid
     * that produced error as well as error string passed in
     *
     * @param request is initial request
     * @param error is String with error message
     * @return OutboundPatientDiscoveryOrchestratable with error response
     */
    public OutboundPatientDiscoveryOrchestratable processError(OutboundPatientDiscoveryOrchestratable request,
        String error) {
        LOG.debug("EntityPatientDiscoveryProcessor::processError error={}", error);
        OutboundPatientDiscoveryOrchestratable response = new OutboundPatientDiscoveryOrchestratable(null,
            request.getResponseProcessor(), null, request.getAssertion(), request.getServiceName(),
            request.getTarget(), request.getRequest());
        String errStr = "Error from target homeId=" + request.getTarget().getHomeCommunity().getHomeCommunityId();
        errStr += "  The error received was " + error;
        PRPAIN201306UV02 pdErrorResponse = new HL7PRPA201306Transforms().createPRPA201306ForErrors(
            request.getRequest(), errStr);
        response.setResponse(pdErrorResponse);
        return response;
    }

    /**
     * aggregates the individualResponse into the cumulativeResponse
     */
    private void addResponseToCumulativeResponse(OutboundPatientDiscoveryOrchestratable individual,
        OutboundPatientDiscoveryOrchestratable cumulativeResponse) throws Exception {

        PRPAIN201306UV02 current = individual.getResponse();
        if (current != null) {

            String hcid = individual.getTarget().getHomeCommunity().getHomeCommunityId();
            CommunityPRPAIN201306UV02ResponseType communityResponse = createCommunityPRPAIN201306UV02ResponseType(hcid);
            communityResponse.setPRPAIN201306UV02(current);

            cumulativeResponse.getCumulativeResponse().getCommunityResponse().add(communityResponse);
            LOG.debug("EntityPatientDiscoveryProcessor::aggregateResponse combine next response done cumulativeResponse count={}", count);
        } else {
            throw new Exception(
                "EntityPatientDiscoveryProcessor::aggregateResponse received a null PRPAIN201306UV02 response.");
        }
    }

    @Override
    public void aggregate(OutboundOrchestratable individualResponse, OutboundOrchestratable cumulativeResponse) {
        throw new NotImplementedException("This should not be used");
    }
}
