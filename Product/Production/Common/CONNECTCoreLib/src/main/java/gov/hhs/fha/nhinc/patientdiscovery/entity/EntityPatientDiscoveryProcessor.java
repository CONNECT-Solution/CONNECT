package gov.hhs.fha.nhinc.patientdiscovery.entity;

import gov.hhs.fha.nhinc.gateway.executorservice.ExecutorServiceHelper;
import gov.hhs.fha.nhinc.orchestration.NhinResponseProcessor;
import gov.hhs.fha.nhinc.orchestration.EntityOrchestratableMessage;
import gov.hhs.fha.nhinc.orchestration.EntityOrchestratable;
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
 * Class handles all processing of responses for PatientDiscovery
 * for all the different spec combinations:
 * 1.  CumulativeResponse is spec a0 and an individual Response is spec a0
 * 2.  CumulativeResponse is spec a0 and an individual Response is spec a1
 * 3.  CumulativeResponse is spec a1 and an individual Response is spec a0
 * 4.  CumulativeResponse is spec a1 and an individual Response is spec a1
 *
 * @author paul.eftis
 */
public class EntityPatientDiscoveryProcessor implements NhinResponseProcessor{

    private static Log log = LogFactory.getLog(EntityPatientDiscoveryProcessor.class);

    private NhincConstants.GATEWAY_API_LEVEL cumulativeSpecLevel = null;
    private int count = 0;

    public EntityPatientDiscoveryProcessor(NhincConstants.GATEWAY_API_LEVEL level){
        cumulativeSpecLevel = level;
    }

    /**
     * Handles all processing and aggregation of individual responses
     * from TaskExecutor execution of a NhinDelegate
     * 
     * @param individual
     * @param cumulativeResponse
     */
    @SuppressWarnings("static-access")
    public EntityOrchestratableMessage processNhinResponse(
            EntityOrchestratableMessage individual,
            EntityOrchestratableMessage cumulativeResponse){

        count++;
        log.debug("EntityPatientDiscoveryProcessor::processNhinResponse count=" + count);

        EntityOrchestratableMessage response = null;
        if(cumulativeResponse == null){
            switch(cumulativeSpecLevel){
                case LEVEL_g0:
                {
                    log.debug("EntityPatientDiscoveryProcessor::processNhinResponse createNewCumulativeResponse_a0");
                    cumulativeResponse = EntityPatientDiscoveryProcessorHelper.createNewCumulativeResponse_a0(
                            (EntityPatientDiscoveryOrchestratable)individual);
                    break;
                }
                case LEVEL_g1:
                {
                    log.debug("EntityPatientDiscoveryProcessor::processNhinResponse createNewCumulativeResponse_a1");
                    cumulativeResponse = EntityPatientDiscoveryProcessorHelper.createNewCumulativeResponse_a1(
                            (EntityPatientDiscoveryOrchestratable)individual);
                    break;
                }
                default:
                {
                    log.debug("EntityPatientDiscoveryProcessor::processNhinResponse unknown cumulativeSpecLevel so createNewCumulativeResponse_a1");
                    cumulativeResponse = EntityPatientDiscoveryProcessorHelper.createNewCumulativeResponse_a1(
                            (EntityPatientDiscoveryOrchestratable)individual);
                    break;
                }
            }
        }

        if(individual == null){
            // can't get here as NhinCallableRequest will always return something
            // but if we ever do, log it and return cumulativeResponse passed in
            log.error("EntityPatientDiscoveryProcessor::handleNhinResponse individual received was null!!!");
            response = cumulativeResponse;
        }else{
            EntityOrchestratableMessage individualResponse = processResponse(individual);
            response = aggregateResponse((EntityPatientDiscoveryOrchestratable)individualResponse, cumulativeResponse);
        }

        return response;
    }


    /**
     * Processes the PD response:
     * 1. Insert correlation mapping in correlations db if ResponseMode is not pass-thru
     * 2. Insert AA ID to HCID mapping in db if it is a new mapping
     *
     * Note that all response processing exceptions are caught here and handled
     * by returning a PD response with the error/exception and hcid for response
     * @param individualResponse
     */
    @SuppressWarnings("static-access")
    public EntityOrchestratableMessage processResponse(EntityOrchestratableMessage individualResponse){
        try{
            if(individualResponse instanceof EntityPatientDiscoveryOrchestratable_a0){
                log.debug("EntityPatientDiscoveryProcessor::processResponse for a0 start count=" + count);
                // process spec_a0 response
                EntityPatientDiscoveryOrchestratable_a0 individual =
                        (EntityPatientDiscoveryOrchestratable_a0)individualResponse;
                EntityPatientDiscoveryOrchestratable_a0 response = new EntityPatientDiscoveryOrchestratable_a0(
                        null, individual.getResponseProcessor(), null, null, 
                        individual.getAssertion(), individual.getServiceName(),
                        individual.getTarget(), individual.getRequest());

                // store the correlation result and handle Trust/Verify Mode
                ProxyPRPAIN201305UVProxySecuredRequestType oProxyPRPAIN201305UVProxySecuredRequestType =
                        new ProxyPRPAIN201305UVProxySecuredRequestType();
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
            }else if(individualResponse instanceof EntityPatientDiscoveryOrchestratable_a1){
                // process spec_a1 response
                // currently a0 and a1 are the same for patient discovery
                log.debug("EntityPatientDiscoveryProcessor::processResponse for a1 start count=" + count);
                // process spec_a1 response
                EntityPatientDiscoveryOrchestratable_a1 individual =
                        (EntityPatientDiscoveryOrchestratable_a1)individualResponse;
                EntityPatientDiscoveryOrchestratable_a1 response = new EntityPatientDiscoveryOrchestratable_a1(
                        null, individual.getResponseProcessor(), null, null,
                        individual.getAssertion(), individual.getServiceName(),
                        individual.getTarget(), individual.getRequest());

                // store the correlation result and handle Trust/Verify Mode
                ProxyPRPAIN201305UVProxySecuredRequestType oProxyPRPAIN201305UVProxySecuredRequestType =
                        new ProxyPRPAIN201305UVProxySecuredRequestType();
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
            }else{
                log.error("EntityPatientDiscoveryProcessor::processResponse individualResponse received was unknown!!!");
                throw new Exception("EntityPatientDiscoveryProcessor::processResponse individualResponse received was unknown!!!");
            }
        }catch(Exception ex){
            ExecutorServiceHelper.getInstance().outputCompleteException(ex);
            if(individualResponse instanceof EntityPatientDiscoveryOrchestratable_a0){
                EntityPatientDiscoveryOrchestratable_a0 individual =
                        (EntityPatientDiscoveryOrchestratable_a0)individualResponse;
                EntityOrchestratableMessage response = processErrorResponse(individual,
                        "Exception processing response.  Exception message=" + ex.getMessage());
                return response;
            }else if(individualResponse instanceof EntityPatientDiscoveryOrchestratable_a1){
                EntityPatientDiscoveryOrchestratable_a1 individual =
                        (EntityPatientDiscoveryOrchestratable_a1)individualResponse;
                EntityOrchestratableMessage response = processErrorResponse(individual,
                        "Exception processing response.  Exception message=" + ex.getMessage());
                return response;
            }else{
                // can do nothing if we ever get here other than return what was passed in
                return individualResponse;
            }
        }
    }


    /**
     * Aggregates an individual PD response into the cumulative response
     * Note that all response aggregation exceptions are caught here and handled
     * by returning a PD response with the error/exception and hcid for response
     * @param individual is individual PD response
     * @param cumulative is current cumulative PD response
     * @return cumulative response with individual added
     */
    @SuppressWarnings("static-access")
    public EntityOrchestratableMessage aggregateResponse(
            EntityPatientDiscoveryOrchestratable individual,
            EntityOrchestratableMessage cumulative){

        try{
            if(cumulative instanceof EntityPatientDiscoveryOrchestratable_a0){
                // cumulative is spec_a0
                EntityPatientDiscoveryOrchestratable_a0 cumulativeResponse =
                        (EntityPatientDiscoveryOrchestratable_a0)cumulative;
                if(individual instanceof EntityPatientDiscoveryOrchestratable_a0){
                    // individual is spec_a0 and cumulative is spec_a0, so just aggregate_a0
                    EntityPatientDiscoveryOrchestratable_a0 individualResponse =
                            (EntityPatientDiscoveryOrchestratable_a0)individual;
                    aggregateResponse_a0(individualResponse, cumulativeResponse);
                }else if(individual instanceof EntityPatientDiscoveryOrchestratable_a1){
                    // individual is spec_a1 and cumulative is spec_a0
                    // so transform individual to spec_a0 and then aggregate_a0
                    EntityPatientDiscoveryOrchestratable_a0 individualResponse =
                            EntityPatientDiscoveryProcessorHelper.transformResponse_ToA0((
                            EntityPatientDiscoveryOrchestratable_a1)individual);
                    aggregateResponse_a0(individualResponse, cumulativeResponse);
                }else{
                    log.error("EntityPatientDiscoveryProcessor::aggregateResponse individualResponse received was unknown!!!");
                    throw new Exception("EntityPatientDiscoveryProcessor::aggregateResponse individualResponse received was unknown!!!");
                }

                EntityPatientDiscoveryOrchestratable_a0 response = new EntityPatientDiscoveryOrchestratable_a0(
                    null, null, null, null, cumulativeResponse.getAssertion(),
                    cumulativeResponse.getServiceName(), cumulativeResponse.getTarget(),
                    cumulativeResponse.getRequest());
                response.setCumulativeResponse(cumulativeResponse.getCumulativeResponse());
                return response;
            }else if(cumulative instanceof EntityPatientDiscoveryOrchestratable_a1){
                // cumulative is spec_a1
                // currently spec_a0 is same as spec_a1
                EntityPatientDiscoveryOrchestratable_a1 cumulativeResponse =
                        (EntityPatientDiscoveryOrchestratable_a1)cumulative;
                if(individual instanceof EntityPatientDiscoveryOrchestratable_a0){
                    // individual is spec_a0 and cumulative is spec_a1
                    // so transform individual to spec_a1 and then aggregate_a1
                    EntityPatientDiscoveryOrchestratable_a1 individualResponse =
                            EntityPatientDiscoveryProcessorHelper.transformResponse_ToA1((
                            EntityPatientDiscoveryOrchestratable_a0)individual);
                    aggregateResponse_a1(individualResponse, cumulativeResponse);
                }else if(individual instanceof EntityPatientDiscoveryOrchestratable_a1){
                    // individual is spec_a1 and cumulative is spec_a1, so just aggregate_a1
                    EntityPatientDiscoveryOrchestratable_a1 individualResponse =
                            (EntityPatientDiscoveryOrchestratable_a1)individual;
                    aggregateResponse_a1(individualResponse, cumulativeResponse);
                }else{
                    log.error("EntityPatientDiscoveryProcessor::aggregateResponse individualResponse received was unknown!!!");
                    throw new Exception("EntityPatientDiscoveryProcessor::aggregateResponse individualResponse received was unknown!!!");
                }

                EntityPatientDiscoveryOrchestratable_a1 response = new EntityPatientDiscoveryOrchestratable_a1(
                    null, null, null, null, cumulativeResponse.getAssertion(),
                    cumulativeResponse.getServiceName(), cumulativeResponse.getTarget(),
                    cumulativeResponse.getRequest());
                response.setCumulativeResponse(cumulativeResponse.getCumulativeResponse());
                return response;
            }else{
                log.error("EntityPatientDiscoveryProcessor::aggregateResponse cumulativeResponse received was unknown!!!");
                throw new Exception("EntityPatientDiscoveryProcessor::aggregateResponse cumulativeResponse received was unknown!!!");
            }
        }catch(Exception e){
            ExecutorServiceHelper.getInstance().outputCompleteException(e);
            // add error response for exception to cumulativeResponse
            if(cumulative instanceof EntityPatientDiscoveryOrchestratable_a0){
                EntityPatientDiscoveryOrchestratable_a0 cumulativeResponse =
                        (EntityPatientDiscoveryOrchestratable_a0)cumulative;

                CommunityPRPAIN201306UV02ResponseType communityResponse =
                        new CommunityPRPAIN201306UV02ResponseType();
                NhinTargetCommunityType target = new NhinTargetCommunityType();
                HomeCommunityType home = new HomeCommunityType();
                home.setHomeCommunityId(individual.getTarget().getHomeCommunity().getHomeCommunityId());
                target.setHomeCommunity(home);
                communityResponse.setNhinTargetCommunity(target);
                PRPAIN201306UV02 pdErrorResponse =
                    (new HL7PRPA201306Transforms()).createPRPA201306ForErrors(individual.getRequest(),
                    "Exception aggregating response from target homeId="
                    + individual.getTarget().getHomeCommunity().getHomeCommunityId()
                    + ".  Exception message=" + e.getMessage());
                communityResponse.setPRPAIN201306UV02(pdErrorResponse);
                cumulativeResponse.getCumulativeResponse().getCommunityResponse().add(communityResponse);
                return cumulativeResponse;
            }else if(cumulative instanceof EntityPatientDiscoveryOrchestratable_a0){
                EntityPatientDiscoveryOrchestratable_a1 cumulativeResponse =
                        (EntityPatientDiscoveryOrchestratable_a1)cumulative;

                CommunityPRPAIN201306UV02ResponseType communityResponse =
                        new CommunityPRPAIN201306UV02ResponseType();
                NhinTargetCommunityType target = new NhinTargetCommunityType();
                HomeCommunityType home = new HomeCommunityType();
                home.setHomeCommunityId(individual.getTarget().getHomeCommunity().getHomeCommunityId());
                target.setHomeCommunity(home);
                communityResponse.setNhinTargetCommunity(target);
                PRPAIN201306UV02 pdErrorResponse =
                    (new HL7PRPA201306Transforms()).createPRPA201306ForErrors(individual.getRequest(),
                    "Exception aggregating response from target homeId="
                    + individual.getTarget().getHomeCommunity().getHomeCommunityId()
                    + ".  Exception message=" + e.getMessage());
                communityResponse.setPRPAIN201306UV02(pdErrorResponse);
                cumulativeResponse.getCumulativeResponse().getCommunityResponse().add(communityResponse);
                return cumulativeResponse;
            }else{
                // can do nothing if we ever get here other than return what was passed in
                return cumulative;
            }
        }
    }


    /**
     * General error handler that calls appropriate error handler based on request
     * (i.e. if request is spec a0 will call processError_a0 and if request is
     * spec a1 will call processError_a1)
     * @param request is initial request
     * @param error is String with error message
     * @return
     */
    public EntityOrchestratableMessage processErrorResponse(EntityOrchestratableMessage request, String error){
        log.debug("EntityPatientDiscoveryProcessor::processErrorResponse error=" + error);
        if(request instanceof EntityPatientDiscoveryOrchestratable_a0){
            return processError_a0((EntityPatientDiscoveryOrchestratable)request, error);
        }else if(request instanceof EntityPatientDiscoveryOrchestratable_a1){
            return processError_a1((EntityPatientDiscoveryOrchestratable)request, error);
        }else{
            log.error("EntityPatientDiscoveryProcessor::processErrorResponse request was unknown!!!");
            return processError_a1((EntityPatientDiscoveryOrchestratable)request, error);
        }
    }


    /**
     * Generates an EntityPatientDiscoveryOrchestratable_a0 response with
     * an error response for spec a0 that contains target hcid that
     * produced error as well as error string passed in
     * @param request is initial request
     * @param error is String with error message
     * @return EntityPatientDiscoveryOrchestratable_a0 with error response
     */
    public EntityPatientDiscoveryOrchestratable_a0 processError_a0(
            EntityPatientDiscoveryOrchestratable request, String error){
        log.debug("EntityPatientDiscoveryProcessor::processError_a0 error=" + error);
        EntityPatientDiscoveryOrchestratable_a0 response = new EntityPatientDiscoveryOrchestratable_a0(
                null, request.getResponseProcessor(), null, null, request.getAssertion(),
                request.getServiceName(), request.getTarget(), request.getRequest());

        String errStr = "Error from target homeId=" + request.getTarget().getHomeCommunity().getHomeCommunityId();
        errStr += "  The error received was " + error;
        PRPAIN201306UV02 pdErrorResponse =
            (new HL7PRPA201306Transforms()).createPRPA201306ForErrors(request.getRequest(), errStr);
        response.setResponse(pdErrorResponse);
        return response;
    }


    /**
     * Generates an EntityPatientDiscoveryOrchestratable_a1 response with
     * an error response for spec a0 that contains target hcid that
     * produced error as well as error string passed in
     * @param request is initial request
     * @param error is String with error message
     * @return EntityPatientDiscoveryOrchestratable_a1 with error response
     */
    public EntityPatientDiscoveryOrchestratable_a1 processError_a1(
            EntityPatientDiscoveryOrchestratable request, String error){
        log.debug("EntityPatientDiscoveryProcessor::processError_a1 error=" + error);
        EntityPatientDiscoveryOrchestratable_a1 response = new EntityPatientDiscoveryOrchestratable_a1(
                null, request.getResponseProcessor(), null, null, request.getAssertion(),
                request.getServiceName(), request.getTarget(), request.getRequest());

        String errStr = "Error from target homeId=" + request.getTarget().getHomeCommunity().getHomeCommunityId();
        errStr += "  The error received was " + error;
        PRPAIN201306UV02 pdErrorResponse =
            (new HL7PRPA201306Transforms()).createPRPA201306ForErrors(request.getRequest(), errStr);
        response.setResponse(pdErrorResponse);
        return response;
    }


    /**
     * aggregates an a0 spec individualResponse into an a0 spec cumulativeResponse
     */
    @SuppressWarnings("static-access")
    private void aggregateResponse_a0(EntityPatientDiscoveryOrchestratable_a0 individual,
            EntityPatientDiscoveryOrchestratable_a0 cumulativeResponse){

        PRPAIN201306UV02 current = individual.getResponse();
        try{
            // aggregate the response
            CommunityPRPAIN201306UV02ResponseType communityResponse =
                    new CommunityPRPAIN201306UV02ResponseType();

            NhinTargetCommunityType target = new NhinTargetCommunityType();
            HomeCommunityType home = new HomeCommunityType();
            home.setHomeCommunityId(individual.getTarget().getHomeCommunity().getHomeCommunityId());
            target.setHomeCommunity(home);
            communityResponse.setNhinTargetCommunity(target);
            communityResponse.setPRPAIN201306UV02(current);
            cumulativeResponse.getCumulativeResponse().getCommunityResponse().add(communityResponse);
            log.debug("EntityPatientDiscoveryProcessor::aggregateResponse_a0 combine next response done cumulativeResponse count="
                + count);
        }catch(Exception ex){
            ExecutorServiceHelper.getInstance().outputCompleteException(ex);

        }
    }

    
    /**
     * aggregates an a1 spec individualResponse into an a1 spec cumulativeResponse
     */
    @SuppressWarnings("static-access")
    private void aggregateResponse_a1(EntityPatientDiscoveryOrchestratable_a1 individual,
            EntityPatientDiscoveryOrchestratable_a1 cumulativeResponse){

        PRPAIN201306UV02 current = individual.getResponse();
        try{
            // aggregate the response
            CommunityPRPAIN201306UV02ResponseType communityResponse =
                    new CommunityPRPAIN201306UV02ResponseType();

            NhinTargetCommunityType target = new NhinTargetCommunityType();
            HomeCommunityType home = new HomeCommunityType();
            home.setHomeCommunityId(individual.getTarget().getHomeCommunity().getHomeCommunityId());
            target.setHomeCommunity(home);
            communityResponse.setNhinTargetCommunity(target);
            communityResponse.setPRPAIN201306UV02(current);
            cumulativeResponse.getCumulativeResponse().getCommunityResponse().add(communityResponse);
            log.debug("EntityPatientDiscoveryProcessor::aggregateResponse_a1 combine next response done count="
                + count);
        }catch(Exception ex){
            ExecutorServiceHelper.getInstance().outputCompleteException(ex);

        }
    }


    /**
     * NOT USED
     */
    public void aggregate(EntityOrchestratable individualResponse,
            EntityOrchestratable cumulativeResponse){
    }
}
