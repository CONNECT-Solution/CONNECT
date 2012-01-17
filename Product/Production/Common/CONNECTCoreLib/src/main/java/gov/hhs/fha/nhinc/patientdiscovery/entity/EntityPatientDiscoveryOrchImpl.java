package gov.hhs.fha.nhinc.patientdiscovery.entity;

import gov.hhs.fha.nhinc.gateway.executorservice.NhinCallableRequest;
import gov.hhs.fha.nhinc.gateway.executorservice.NhinTaskExecutor;

import gov.hhs.fha.nhinc.orchestration.NhinDelegate;
import gov.hhs.fha.nhinc.orchestration.NhinResponseProcessor;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryPolicyChecker;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201305Processor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfo;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfos;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.gateway.executorservice.ExecutorServiceHelper;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.transform.document.DocumentQueryTransform;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201306Transforms;

import org.hl7.v3.CommunityPRPAIN201306UV02ResponseType;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
import org.hl7.v3.CS;
import org.hl7.v3.EDExplicit;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000100UV01AttentionLine;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000100UV01RespondTo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


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


    /**
     * If testList is passed in (i.e. not null/empty), will fan-out PD
     * to entire testList (i.e. will ignore urlInfoList)
     * If testList is null/empty, will just do normal PD fan-out using
     * urlInfoList for patient passed in RespondingGatewayPRPAIN201305UV02RequestType
     * @param request
     * @param assertion
     * @return
     */
    @SuppressWarnings("static-access")
    public RespondingGatewayPRPAIN201306UV02ResponseType entityPatientDiscoveryOrchImplLoadTest(
            RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion,
            List<CMUrlInfo> testList){

        log.debug("EntityPatientDiscoveryOrchImpl::entityPatientDiscoveryOrchImplLoadTest");
        RespondingGatewayPRPAIN201306UV02ResponseType response = new RespondingGatewayPRPAIN201306UV02ResponseType();
        boolean responseIsSpecA0 = true;

        try{
            CMUrlInfos urlInfoList = getEndpoints(request.getNhinTargetCommunities());
            // loop through the communities and send request if results were not null
            if((urlInfoList == null) || (urlInfoList.getUrlInfo().isEmpty())){
                log.warn("No targets were found for the Patient Discovery Request");
                throw new Exception("No Endpoints For Communities Found!!!");
            }else{
                List<CMUrlInfo> targetList = urlInfoList.getUrlInfo();
                if(testList != null && testList.size() > 0){
                    // this is load test fanout to testList
                    targetList = testList;
                }
                List<NhinCallableRequest<EntityPatientDiscoveryOrchestratable>> callableList =
                        new ArrayList<NhinCallableRequest<EntityPatientDiscoveryOrchestratable>>();
                String transactionId = (UUID.randomUUID()).toString();
                NhinDelegate nd = new NhinPatientDiscoveryDelegate();
                NhinResponseProcessor np = new EntityPatientDiscoveryProcessor();

                // we hold the error messages for any failed policy checks in policyErrList
                List policyErrList = new ArrayList();

                for(CMUrlInfo cmurlinfo: targetList){
                    NhinTargetSystemType target = new NhinTargetSystemType();
                    HomeCommunityType targetCommunity = new HomeCommunityType();
                    targetCommunity.setHomeCommunityId(cmurlinfo.getHcid());
                    target.setHomeCommunity(targetCommunity);

                    //create a new request to send out to each target community
                    RespondingGatewayPRPAIN201305UV02RequestType newRequest =
                            createNewRequest(request, assertion, cmurlinfo);

                    if(checkPolicy(newRequest, assertion)){
                        // ensure target hcid is set on request
                        if(newRequest.getPRPAIN201305UV02() != null && newRequest.getPRPAIN201305UV02().getReceiver() != null
                                && newRequest.getPRPAIN201305UV02().getReceiver().get(0) != null
                                && newRequest.getPRPAIN201305UV02().getReceiver().get(0).getDevice() != null
                                && newRequest.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getId() != null
                                && newRequest.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getId().get(0) != null){

                                newRequest.getPRPAIN201305UV02().getReceiver().get(0).getDevice().
                                        getId().get(0).setRoot(cmurlinfo.getHcid());
                        }

                        EntityPatientDiscoveryOrchestratable message = new EntityPatientDiscoveryOrchestratable(
                                    nd, np, null, null, assertion, NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME,
                                    target, newRequest.getPRPAIN201305UV02());
                        callableList.add(new NhinCallableRequest<EntityPatientDiscoveryOrchestratable>(message));

                        log.debug("EntityPatientDiscoveryOrchImpl added NhinCallableRequest"
                                + " for hcid=" + target.getHomeCommunity().getHomeCommunityId());
                    }else{
                        log.debug("EntityPatientDiscoveryOrchImpl Policy Check Failed for homeId=" + target.getHomeCommunity().getHomeCommunityId());
                    }
                }

                // note that this impl sets taskexecutor to return EntityPatientDiscoveryOrchestratable_a0
                // you can change this to EntityPatientDiscoveryOrchestratable_a1 to return new spec response
                EntityPatientDiscoveryOrchestratable_a0 orchResponse = null;
//                    EntityPatientDiscoveryOrchestratable_a1 orchResponse = null;
                if(responseIsSpecA0){
                    @SuppressWarnings("static-access")
                    NhinTaskExecutor<EntityPatientDiscoveryOrchestratable_a0, EntityPatientDiscoveryOrchestratable> dqexecutor =
                            new NhinTaskExecutor<EntityPatientDiscoveryOrchestratable_a0, EntityPatientDiscoveryOrchestratable>(
                            ExecutorServiceHelper.getInstance().checkExecutorTaskIsLarge(callableList.size()) ? largejobExecutor : regularExecutor,
                            callableList, transactionId);
                    dqexecutor.executeTask();
                    orchResponse = (EntityPatientDiscoveryOrchestratable_a0)dqexecutor.getFinalResponse();
                }else{
//                    @SuppressWarnings("static-access")
//                    NhinTaskExecutor<EntityPatientDiscoveryOrchestratable_a0, EntityPatientDiscoveryOrchestratable> dqexecutor =
//                            new NhinTaskExecutor<EntityPatientDiscoveryOrchestratable_a0, EntityPatientDiscoveryOrchestratable>(
//                            ExecutorServiceHelper.getInstance().checkExecutorTaskIsLarge(callableList.size()) ? largejobExecutor : regularExecutor,
//                            callableList, transactionId);
//                    dqexecutor.executeTask();
//                    orchResponse = (EntityPatientDiscoveryOrchestratable_a0)dqexecutor.getFinalResponse();
                }

                response = orchResponse.getCumulativeResponse();

                // add any errors from policyErrList to response

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


    @SuppressWarnings("static-access")
    protected RespondingGatewayPRPAIN201306UV02ResponseType getResponseFromCommunities(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion) {
        log.debug("EntityPatientDiscoveryOrchImpl getResponseFromCommunities");
        RespondingGatewayPRPAIN201306UV02ResponseType response = new RespondingGatewayPRPAIN201306UV02ResponseType();
        boolean responseIsSpecA0 = true;

        try{
            CMUrlInfos urlInfoList = getEndpoints(request.getNhinTargetCommunities());
            // loop through the communities and send request if results were not null
            if((urlInfoList == null) || (urlInfoList.getUrlInfo().isEmpty())){
                log.warn("No targets were found for the Patient Discovery Request");
                throw new Exception("No Endpoints For Communities Found!!!");
            }else{
                /************************************************************************
                 * We replaced the 3.2.1 connect code here with the new 3.3 concurrent fanout impl
                 * Note that the checkPolicy is done in the PDClient
                 * and all response processing is done in the PDProcessor
                ***********************************************************************/
                List<CMUrlInfo> targetList = urlInfoList.getUrlInfo();
                List<NhinCallableRequest<EntityPatientDiscoveryOrchestratable>> callableList =
                        new ArrayList<NhinCallableRequest<EntityPatientDiscoveryOrchestratable>>();
                String transactionId = (UUID.randomUUID()).toString();
                NhinDelegate nd = new NhinPatientDiscoveryDelegate();
                NhinResponseProcessor np = new EntityPatientDiscoveryProcessor();

                // we hold the error messages for any failed policy checks in policyErrList
                List policyErrList = new ArrayList();

                for(CMUrlInfo cmurlinfo: targetList){
                    NhinTargetSystemType target = new NhinTargetSystemType();
                    HomeCommunityType targetCommunity = new HomeCommunityType();
                    targetCommunity.setHomeCommunityId(cmurlinfo.getHcid());
                    target.setHomeCommunity(targetCommunity);

                    //create a new request to send out to each target community
                    RespondingGatewayPRPAIN201305UV02RequestType newRequest =
                            createNewRequest(request, assertion, cmurlinfo);

                    if(checkPolicy(newRequest, assertion)){
                        // ensure target hcid is set on request
                        if(newRequest.getPRPAIN201305UV02() != null && newRequest.getPRPAIN201305UV02().getReceiver() != null
                                && newRequest.getPRPAIN201305UV02().getReceiver().get(0) != null
                                && newRequest.getPRPAIN201305UV02().getReceiver().get(0).getDevice() != null
                                && newRequest.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getId() != null
                                && newRequest.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getId().get(0) != null){

                                newRequest.getPRPAIN201305UV02().getReceiver().get(0).getDevice().
                                        getId().get(0).setRoot(cmurlinfo.getHcid());
                        }

                        EntityPatientDiscoveryOrchestratable message = new EntityPatientDiscoveryOrchestratable(
                                    nd, np, null, null, assertion, NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME,
                                    target, newRequest.getPRPAIN201305UV02());
                        callableList.add(new NhinCallableRequest<EntityPatientDiscoveryOrchestratable>(message));

                        log.debug("EntityPatientDiscoveryOrchImpl added NhinCallableRequest"
                                + " for hcid=" + target.getHomeCommunity().getHomeCommunityId());
                    }else{
                        log.debug("EntityPatientDiscoveryOrchImpl Policy Check Failed for homeId=" + target.getHomeCommunity().getHomeCommunityId());
                    }
                }

                // note that this impl sets taskexecutor to return EntityPatientDiscoveryOrchestratable_a0
                // you can change this to EntityPatientDiscoveryOrchestratable_a1 to return new spec response
                EntityPatientDiscoveryOrchestratable_a0 orchResponse = null;
//                    EntityPatientDiscoveryOrchestratable_a1 orchResponse = null;
                if(responseIsSpecA0){
                    @SuppressWarnings("static-access")
                    NhinTaskExecutor<EntityPatientDiscoveryOrchestratable_a0, EntityPatientDiscoveryOrchestratable> dqexecutor =
                            new NhinTaskExecutor<EntityPatientDiscoveryOrchestratable_a0, EntityPatientDiscoveryOrchestratable>(
                            ExecutorServiceHelper.getInstance().checkExecutorTaskIsLarge(callableList.size()) ? largejobExecutor : regularExecutor,
                            callableList, transactionId);
                    dqexecutor.executeTask();
                    orchResponse = (EntityPatientDiscoveryOrchestratable_a0)dqexecutor.getFinalResponse();
                }else{
//                    @SuppressWarnings("static-access")
//                    NhinTaskExecutor<EntityPatientDiscoveryOrchestratable_a0, EntityPatientDiscoveryOrchestratable> dqexecutor =
//                            new NhinTaskExecutor<EntityPatientDiscoveryOrchestratable_a0, EntityPatientDiscoveryOrchestratable>(
//                            ExecutorServiceHelper.getInstance().checkExecutorTaskIsLarge(callableList.size()) ? largejobExecutor : regularExecutor,
//                            callableList, transactionId);
//                    dqexecutor.executeTask();
//                    orchResponse = (EntityPatientDiscoveryOrchestratable_a0)dqexecutor.getFinalResponse();
                }

                response = orchResponse.getCumulativeResponse();

                // add any errors from policyErrList to response

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
        return new PatientDiscoveryPolicyChecker().checkOutgoingPolicy(request);
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
            AssertionType assertion, CMUrlInfo urlInfo) {
        RespondingGatewayPRPAIN201305UV02RequestType newRequest = new RespondingGatewayPRPAIN201305UV02RequestType();

        PRPAIN201305UV02 new201305 = new PatientDiscovery201305Processor().createNewRequest(
                cloneRequest(request.getPRPAIN201305UV02()), urlInfo.getHcid());

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


    protected CMUrlInfos getEndpoints(NhinTargetCommunitiesType targetCommunities){
        CMUrlInfos urlInfoList = null;
        try{
            urlInfoList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(targetCommunities, NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME);
        }catch (ConnectionManagerException ex){
            log.error("Failed to obtain target URLs", ex);
        }
        return urlInfoList;
    }


    protected void logEntityPatientDiscoveryRequest(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion) {
        new PatientDiscoveryAuditLogger().auditEntity201305(request, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
    }

    protected void logAggregatedResponseFromNhin(RespondingGatewayPRPAIN201306UV02ResponseType response, AssertionType assertion) {
        new PatientDiscoveryAuditLogger().auditEntity201306(response, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
    }

}
