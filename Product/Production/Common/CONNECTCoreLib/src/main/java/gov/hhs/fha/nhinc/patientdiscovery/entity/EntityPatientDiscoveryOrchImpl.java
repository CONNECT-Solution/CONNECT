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

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.UrlInfo;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.gateway.executorservice.ExecutorServiceHelper;
import gov.hhs.fha.nhinc.gateway.executorservice.NhinCallableRequest;
import gov.hhs.fha.nhinc.gateway.executorservice.NhinTaskExecutor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.OutboundResponseProcessor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201305Processor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryPolicyChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.subdisc.HL7DataTransformHelper;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201306Transforms;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.bind.JAXBElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.CS;
import org.hl7.v3.CommunityPRPAIN201306UV02ResponseType;
import org.hl7.v3.EDExplicit;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000100UV01AttentionLine;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000100UV01RespondTo;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;


/**
 * Orchestrates the Entity (i.e. from Adapter) PatientDiscovery transaction
 * @author Neil Webb
 * @author paul.eftis (updated 10/15/2011 to implement new concurrent request handling/fanout)
 * @author paul.eftis (updated 01/15/2011 to implement new multispec delegate)
 */
public class EntityPatientDiscoveryOrchImpl{

    private Log log = LogFactory.getLog(getClass());
    private ExecutorService regularExecutor = null;
    private ExecutorService largejobExecutor = null;


    /**
     * Add default constructor that is used by test cases
     * Note that implementations should always use constructor that takes
     * the executor services as input
     */
    public EntityPatientDiscoveryOrchImpl(){
        // for this default test case, we just create default executor services
        // with a thread pool of 1
        regularExecutor = Executors.newFixedThreadPool(1);
        largejobExecutor = Executors.newFixedThreadPool(1);
    }


    /**
     * We construct the orch impl class with references to both executor services
     * that could be used for this particular orchestration instance.
     * Determination of which executor service to use (largejob or regular) is based on
     * the size of the pdlist and configs
     */
    public EntityPatientDiscoveryOrchImpl(ExecutorService e, ExecutorService le){
        regularExecutor = e;
        largejobExecutor = le;
    }


    public RespondingGatewayPRPAIN201306UV02ResponseType respondingGatewayPRPAIN201305UV02(
            RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion){

        log.debug("Begin respondingGatewayPRPAIN201305UV02");
        RespondingGatewayPRPAIN201306UV02ResponseType response = new RespondingGatewayPRPAIN201306UV02ResponseType();

        try{
            if(request == null){
                log.warn("RespondingGatewayPRPAIN201305UV02RequestType was null.");
                throw new Exception("PatientDiscovery RespondingGatewayPRPAIN201305UV02RequestType request was null.");
            }else if(assertion == null){
                log.warn("AssertionType was null.");
                throw new Exception("Assertion was null.");
            }else if (request.getPRPAIN201305UV02() == null){
                log.warn("PRPAIN201305UV02 was null.");
                throw new Exception("PatientDiscovery PRPAIN201305UV02 request was null.");
            }else{
                logEntityPatientDiscoveryRequest(request, assertion);
                response = getResponseFromCommunities(request, assertion);
                logAggregatedResponseFromNhin(response, assertion);
            }
        }catch(Exception e){
            // generate error message and add to response
            CommunityPRPAIN201306UV02ResponseType communityResponse = new CommunityPRPAIN201306UV02ResponseType();
            communityResponse.setPRPAIN201306UV02((new HL7PRPA201306Transforms()).createPRPA201306ForErrors(request.getPRPAIN201305UV02(),
                        e.getMessage()));
            response.getCommunityResponse().add(communityResponse);
        }
        log.debug("End respondingGatewayPRPAIN201305UV02");
        return response;
    }



    @SuppressWarnings("static-access")
    protected RespondingGatewayPRPAIN201306UV02ResponseType getResponseFromCommunities(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion) {
        log.debug("EntityPatientDiscoveryOrchImpl getResponseFromCommunities");
        RespondingGatewayPRPAIN201306UV02ResponseType response = new RespondingGatewayPRPAIN201306UV02ResponseType();

        // quick rig for testing to switch between a0 and a1
        // note that a0 and a1 would be handled by different methods if they were different
        boolean responseIsSpecA0 = true;
        NhincConstants.GATEWAY_API_LEVEL gatewayLevel =
                ConnectionManagerCache.getInstance().getApiVersion(
                getLocalHomeCommunityId(), NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME);
        switch(gatewayLevel){
            case LEVEL_g0:
            {
                responseIsSpecA0 = true;
                break;
            }
            case LEVEL_g1:
            {
                responseIsSpecA0 = false;
                break;
            }
            default:
            {
                responseIsSpecA0 = true;
                break;
            }
        }
        log.debug("EntityPatientDiscoveryOrchImpl set responseIsSpecA0=" + responseIsSpecA0);

        try{
            List<UrlInfo> urlInfoList = getEndpoints(request.getNhinTargetCommunities());
            // loop through the communities and send request if results were not null
            if((urlInfoList == null) || (urlInfoList.isEmpty())){
                log.warn("No targets were found for the Patient Discovery Request");
                throw new Exception("No Endpoints For Communities Found!!!");
            }else{
                /************************************************************************
                 * We replaced the 3.2.1 connect code here with the new 3.3 concurrent fanout impl
                 * Note that the checkPolicy is done in the PDClient
                 * and all response processing is done in the PDProcessor
                ***********************************************************************/
                List<UrlInfo> targetList = urlInfoList;
                List<NhinCallableRequest<OutboundPatientDiscoveryOrchestratable>> callableList =
                        new ArrayList<NhinCallableRequest<OutboundPatientDiscoveryOrchestratable>>();
                String transactionId = (UUID.randomUUID()).toString();

                // we hold the error messages for any failed policy checks in policyErrList
                List<CommunityPRPAIN201306UV02ResponseType> policyErrList =
                        new ArrayList<CommunityPRPAIN201306UV02ResponseType>();

                for(UrlInfo UrlInfo: targetList){
                    NhinTargetSystemType target = new NhinTargetSystemType();
                    HomeCommunityType targetCommunity = new HomeCommunityType();
                    targetCommunity.setHomeCommunityId(UrlInfo.getHcid());
                    target.setHomeCommunity(targetCommunity);

                    //create a new request to send out to each target community
                    RespondingGatewayPRPAIN201305UV02RequestType newRequest =
                            createNewRequest(request, assertion, UrlInfo);

                    if(checkPolicy(newRequest, assertion)){
                        OutboundDelegate nd = new OutboundPatientDiscoveryDelegate();
                        OutboundResponseProcessor np = null;
                        if(responseIsSpecA0){
                            np = new OutboundPatientDiscoveryProcessor(NhincConstants.GATEWAY_API_LEVEL.LEVEL_g0);
                        }else{
                            np = new OutboundPatientDiscoveryProcessor(NhincConstants.GATEWAY_API_LEVEL.LEVEL_g1);
                        }

                        // ensure target hcid is set on request
                        if(newRequest.getPRPAIN201305UV02() != null && newRequest.getPRPAIN201305UV02().getReceiver() != null
                                && newRequest.getPRPAIN201305UV02().getReceiver().get(0) != null
                                && newRequest.getPRPAIN201305UV02().getReceiver().get(0).getDevice() != null
                                && newRequest.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getId() != null
                                && newRequest.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getId().get(0) != null){

                                newRequest.getPRPAIN201305UV02().getReceiver().get(0).getDevice().
                                        getId().get(0).setRoot(UrlInfo.getHcid());
                        }

                        OutboundPatientDiscoveryOrchestratable message = new OutboundPatientDiscoveryOrchestratable(
                                    nd, np, null, null, assertion, NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME,
                                    target, newRequest.getPRPAIN201305UV02());
                        callableList.add(new NhinCallableRequest<OutboundPatientDiscoveryOrchestratable>(message));

                        log.debug("EntityPatientDiscoveryOrchImpl added NhinCallableRequest"
                                + " for hcid=" + target.getHomeCommunity().getHomeCommunityId());
                    }else{
                        log.debug("EntityPatientDiscoveryOrchImpl Policy Check Failed for homeId=" 
                                + UrlInfo.getHcid());
                        CommunityPRPAIN201306UV02ResponseType communityResponse = new CommunityPRPAIN201306UV02ResponseType();
                        NhinTargetCommunityType tc = new NhinTargetCommunityType();
                        HomeCommunityType home = new HomeCommunityType();
                        home.setHomeCommunityId(UrlInfo.getHcid());
                        tc.setHomeCommunity(home);
                        communityResponse.setNhinTargetCommunity(tc);
                        communityResponse.setPRPAIN201306UV02((new HL7PRPA201306Transforms()).createPRPA201306ForErrors(
                                request.getPRPAIN201305UV02(),
                                "EntityPatientDiscoveryOrchImpl Policy Check Failed for homeId="
                                + UrlInfo.getHcid()));
                        policyErrList.add(communityResponse);
                    }
                }

                // note that if responseIsSpecA0 taskexecutor is set to return OutboundPatientDiscoveryOrchestratable_a0
                // else  taskexecutor set to return OutboundPatientDiscoveryOrchestratable_a1
                OutboundPatientDiscoveryOrchestratable_a0 orchResponse_a0 = null;
                OutboundPatientDiscoveryOrchestratable_a1 orchResponse_a1 = null;
                if(responseIsSpecA0){
                    log.debug("EntityPatientDiscoveryOrchImpl executing task to return spec a0 cumulative response");
                    NhinTaskExecutor<OutboundPatientDiscoveryOrchestratable_a0, OutboundPatientDiscoveryOrchestratable> dqexecutor =
                            new NhinTaskExecutor<OutboundPatientDiscoveryOrchestratable_a0, OutboundPatientDiscoveryOrchestratable>(
                            ExecutorServiceHelper.getInstance().checkExecutorTaskIsLarge(callableList.size()) ? largejobExecutor : regularExecutor,
                            callableList, transactionId);
                    dqexecutor.executeTask();
                    orchResponse_a0 = (OutboundPatientDiscoveryOrchestratable_a0)dqexecutor.getFinalResponse();
                    response = orchResponse_a0.getCumulativeResponse();

                    // add any errors from policyErrList to response
                    for(CommunityPRPAIN201306UV02ResponseType policyError : policyErrList){
                        response.getCommunityResponse().add(policyError);
                    }
                }else{
                    log.debug("EntityPatientDiscoveryOrchImpl executing task to return spec a1 cumulative response");
                    NhinTaskExecutor<OutboundPatientDiscoveryOrchestratable_a1, OutboundPatientDiscoveryOrchestratable> dqexecutor =
                            new NhinTaskExecutor<OutboundPatientDiscoveryOrchestratable_a1, OutboundPatientDiscoveryOrchestratable>(
                            ExecutorServiceHelper.getInstance().checkExecutorTaskIsLarge(callableList.size()) ? largejobExecutor : regularExecutor,
                            callableList, transactionId);
                    dqexecutor.executeTask();
                    orchResponse_a1 = (OutboundPatientDiscoveryOrchestratable_a1)dqexecutor.getFinalResponse();
                    response = orchResponse_a1.getCumulativeResponse();

                    // add any errors from policyErrList to response
                    for(CommunityPRPAIN201306UV02ResponseType policyError : policyErrList){
                        response.getCommunityResponse().add(policyError);
                    }
                }

                log.debug("EntityPatientDiscoveryOrchImpl taskexecutor done and received response");
            }
        }catch(Exception e){
            // generate error message and add to response
            CommunityPRPAIN201306UV02ResponseType communityResponse = new CommunityPRPAIN201306UV02ResponseType();
            communityResponse.setPRPAIN201306UV02((new HL7PRPA201306Transforms()).createPRPA201306ForErrors(request.getPRPAIN201305UV02(),
                        e.getMessage()));
            response.getCommunityResponse().add(communityResponse);
        }

        log.debug("EntityPatientDiscoveryOrchImpl Exiting getResponseFromCommunities");
        return response;
    }


     /**
     * Policy Check verification done here....from connect code
     */
    protected boolean checkPolicy(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion) {
        if(request != null){
            request.setAssertion(assertion);
        }
        return PatientDiscoveryPolicyChecker.getInstance().checkOutgoingPolicy(request);
    }


    /**
     * Create a new RespondingGatewayPRPAIN201305UV02RequestType which has a new
     * PRPAIN201305UV02 cloned from the original
     *
     * @param request
     * @param assertion
     * @param urlInfo
     * @return new RespondingGatewayPRPAIN201305UV02RequestType
     */
    protected RespondingGatewayPRPAIN201305UV02RequestType createNewRequest(RespondingGatewayPRPAIN201305UV02RequestType request,
            AssertionType assertion, UrlInfo urlInfo) {
        RespondingGatewayPRPAIN201305UV02RequestType newRequest = new RespondingGatewayPRPAIN201305UV02RequestType();

        PRPAIN201305UV02 new201305 = new PatientDiscovery201305Processor().createNewRequest(
                cloneRequest(request.getPRPAIN201305UV02()), urlInfo.getHcid());
        
        //Make sure the response modality and response priority codes are set as per the spec
        if(new201305.getControlActProcess()!= null &&
                new201305.getControlActProcess().getQueryByParameter() != null)
        {
            PRPAMT201306UV02QueryByParameter queryParams = new201305.getControlActProcess().getQueryByParameter().getValue();
            if(queryParams.getResponseModalityCode() == null)
                queryParams.setResponseModalityCode(HL7DataTransformHelper.CSFactory("R"));
            if(queryParams.getResponsePriorityCode() == null)
                queryParams.setResponsePriorityCode(HL7DataTransformHelper.CSFactory("I"));
        }

        newRequest.setAssertion(assertion);
        newRequest.setPRPAIN201305UV02(new201305);
        newRequest.setNhinTargetCommunities(request.getNhinTargetCommunities());
        return newRequest;
    }


    /**
     * paul added this to generate a new PRPAIN201305UV02 for every PDClient thread
     * rather than a single PRPAIN201305UV02 for all requests
     *
     * The reason is that otherwise you can get a java.util.ConcurrentModificationException
     * when the PRPAIN201305UV02 is marshalled for audit/policy etc calls in one thread
     * and updated in another thread
     * @param request is original PRPAIN201305UV02
     * @return new PRPAIN201305UV02 object with values set to original
     */
    private PRPAIN201305UV02 cloneRequest(PRPAIN201305UV02 request){
        PRPAIN201305UV02 newRequest = new PRPAIN201305UV02();

        newRequest.setAcceptAckCode(request.getAcceptAckCode());

        for(EDExplicit edex : request.getAttachmentText()){
            newRequest.getAttachmentText().add(edex);
        }
        for(MCCIMT000100UV01AttentionLine mcc : request.getAttentionLine()){
            newRequest.getAttentionLine().add(mcc);
        }
        newRequest.setControlActProcess(request.getControlActProcess());
        newRequest.setCreationTime(request.getCreationTime());
        newRequest.setITSVersion(request.getITSVersion());
        newRequest.setId(request.getId());
        newRequest.setInteractionId(request.getInteractionId());
        for(String n : request.getNullFlavor()){
            newRequest.getNullFlavor().add(n);
        }
        newRequest.setProcessingCode(request.getProcessingCode());
        newRequest.setProcessingModeCode(request.getProcessingModeCode());
        for(II ii : request.getProfileId()){
            newRequest.getProfileId().add(ii);
        }
        for(CS cs : request.getRealmCode()){
            newRequest.getRealmCode().add(cs);
        }
        for(MCCIMT000100UV01Receiver mcc : request.getReceiver()){
            newRequest.getReceiver().add(mcc);
        }
        for(MCCIMT000100UV01RespondTo mcc : request.getRespondTo()){
            newRequest.getRespondTo().add(mcc);
        }
        newRequest.setSecurityText(request.getSecurityText());
        newRequest.setSender(request.getSender());
        newRequest.setSequenceNumber(request.getSequenceNumber());
        for(II ii : request.getTemplateId()){
            newRequest.getTemplateId().add(ii);
        }
        newRequest.setTypeId(request.getTypeId());
        newRequest.setVersionCode(request.getVersionCode());

        return newRequest;
    }


    protected List<UrlInfo> getEndpoints(NhinTargetCommunitiesType targetCommunities){
        List<UrlInfo> urlInfoList = null;
        try{
            urlInfoList = ConnectionManagerCache.getInstance().getEndpontURLFromNhinTargetCommunities(targetCommunities, NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME);
        }catch (ConnectionManagerException ex){
            log.error("Failed to obtain target URLs", ex);
        }
        return urlInfoList;
    }


    protected String getLocalHomeCommunityId(){
        String sHomeCommunity = null;
        try{
            sHomeCommunity = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                    NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        }catch(Exception ex){
            log.error(ex.getMessage());
        }
        return sHomeCommunity;
    }


    protected void logEntityPatientDiscoveryRequest(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion) {
        new PatientDiscoveryAuditLogger().auditEntity201305(request, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
    }

    protected void logAggregatedResponseFromNhin(RespondingGatewayPRPAIN201306UV02ResponseType response, AssertionType assertion) {
        new PatientDiscoveryAuditLogger().auditEntity201306(response, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
    }

}
