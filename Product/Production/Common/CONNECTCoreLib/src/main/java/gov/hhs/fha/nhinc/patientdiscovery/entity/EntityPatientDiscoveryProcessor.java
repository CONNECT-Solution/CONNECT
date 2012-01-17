package gov.hhs.fha.nhinc.patientdiscovery.entity;

import gov.hhs.fha.nhinc.gateway.executorservice.ExecutorServiceHelper;
import gov.hhs.fha.nhinc.orchestration.NhinResponseProcessor;
import gov.hhs.fha.nhinc.orchestration.EntityOrchestratableMessage;
import gov.hhs.fha.nhinc.orchestration.EntityOrchestratable;

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

    int count = 0;

    public EntityPatientDiscoveryProcessor(){}

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
            if(cumulativeResponse instanceof EntityPatientDiscoveryOrchestratable_a0){
                log.debug("EntityPatientDiscoveryProcessor::processNhinResponse createNewCumulativeResponse_a0");
                cumulativeResponse = EntityPatientDiscoveryProcessorHelper.createNewCumulativeResponse_a0(
                        (EntityPatientDiscoveryOrchestratable)individual);
            }else{
                log.debug("EntityPatientDiscoveryProcessor::processNhinResponse createNewCumulativeResponse_a1");
                // currently spec_a0 is same as spec_a1
                cumulativeResponse = EntityPatientDiscoveryProcessorHelper.createNewCumulativeResponse_a0(
                        (EntityPatientDiscoveryOrchestratable)individual);
            }
        }


        if(individual == null){
            // can't get here as NhinCallableRequest will always return something
            // but if we ever do, log it and return cumulativeResponse passed in
            log.error("EntityDocQueryProcessor::handleNhinResponse individual received was null!!!");
            response = cumulativeResponse;
        }else{
            EntityOrchestratableMessage individualResponse = processResponse(individual);
            response = aggregateResponse((EntityPatientDiscoveryOrchestratable)individualResponse, cumulativeResponse);
        }

        return response;
    }


    /**
     * DQ response requires no processing
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
            }else{
                // process spec_a1 response
                // currently a0 and a1 are the same for patient discovery
                log.debug("EntityPatientDiscoveryProcessor::processResponse for a1 start count=" + count);
                // process spec_a1 response
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
                log.debug("EntityPatientDiscoveryProcessor::processResponse a1 done count=" + count);
                return response;
            }
        }catch(Exception ex){
            ExecutorServiceHelper.getInstance().outputCompleteException(ex);
            EntityOrchestratableMessage individual = processErrorResponse(individualResponse, 
                    "Exception processing response=" + ex.getMessage());
            return individual;
        }
    }


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
                }else{
                    // individual is spec_a1 and cumulative is spec_a0
                    // so transform individual to spec_a0 and then aggregate_a0
                    EntityPatientDiscoveryOrchestratable_a0 individualResponse =
                            EntityPatientDiscoveryProcessorHelper.transformResponse_a0((
                            EntityPatientDiscoveryOrchestratable_a0)individual);
                    aggregateResponse_a0(individualResponse, cumulativeResponse);
                }

                EntityPatientDiscoveryOrchestratable_a0 response = new EntityPatientDiscoveryOrchestratable_a0(
                    null, null, null, null, cumulativeResponse.getAssertion(),
                    cumulativeResponse.getServiceName(), cumulativeResponse.getTarget(),
                    cumulativeResponse.getRequest());
                response.setCumulativeResponse(cumulativeResponse.getCumulativeResponse());
                return response;
            }else{
                // cumulative is spec_a1
                // currently spec_a0 is same as spec_a1
                EntityPatientDiscoveryOrchestratable_a0 cumulativeResponse =
                        (EntityPatientDiscoveryOrchestratable_a0)cumulative;
                if(individual instanceof EntityPatientDiscoveryOrchestratable_a0){
                    // individual is spec_a0 and cumulative is spec_a1
                    // so transform individual to spec_a1 and then aggregate_a1
                    EntityPatientDiscoveryOrchestratable_a0 individualResponse =
                            EntityPatientDiscoveryProcessorHelper.transformResponse_a1((
                            EntityPatientDiscoveryOrchestratable_a0)individual);
                    aggregateResponse_a1(individualResponse, cumulativeResponse);
                }else{
                    // individual is spec_a1 and cumulative is spec_a1, so just aggregate_a1
                    EntityPatientDiscoveryOrchestratable_a0 individualResponse =
                            (EntityPatientDiscoveryOrchestratable_a0)individual;
                    aggregateResponse_a1(individualResponse, cumulativeResponse);
                }
                EntityPatientDiscoveryOrchestratable_a0 response = new EntityPatientDiscoveryOrchestratable_a0(
                    null, null, null, null, cumulativeResponse.getAssertion(),
                    cumulativeResponse.getServiceName(), cumulativeResponse.getTarget(),
                    cumulativeResponse.getRequest());
                response.setCumulativeResponse(cumulativeResponse.getCumulativeResponse());
                return response;
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
                    + individual.getTarget().getHomeCommunity().getHomeCommunityId());
                communityResponse.setPRPAIN201306UV02(pdErrorResponse);
                cumulativeResponse.getCumulativeResponse().getCommunityResponse().add(communityResponse);
                return cumulativeResponse;
            }else{
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
                    + individual.getTarget().getHomeCommunity().getHomeCommunityId());
                communityResponse.setPRPAIN201306UV02(pdErrorResponse);
                cumulativeResponse.getCumulativeResponse().getCommunityResponse().add(communityResponse);
                return cumulativeResponse;
            }
        }
    }


    public EntityOrchestratableMessage processErrorResponse(EntityOrchestratableMessage request, String error){
        log.debug("EntityPatientDiscoveryProcessor::processErrorResponse error=" + error);
        EntityPatientDiscoveryOrchestratable r = (EntityPatientDiscoveryOrchestratable)request;
        switch(r.getGatewayApiLevel()){
            case LEVEL_g0: return processError_a0(r, error);
            //case LEVEL_g1: return processError_a1(r, error);
            default: return processError_a0(r, error);
        }
    }


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
     * For PD, spec a0 is same as a1 currently, so just forward to aggregateResponse_a0
     */
    private void aggregateResponse_a1(EntityPatientDiscoveryOrchestratable_a0 individualResponse,
            EntityPatientDiscoveryOrchestratable_a0 cumulativeResponse){
        aggregateResponse_a0(individualResponse, cumulativeResponse);
    }


    // NOT USED
    public void aggregate(EntityOrchestratable individualResponse,
            EntityOrchestratable cumulativeResponse){
    }
}
