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
import gov.hhs.fha.nhinc.orchestration.NhinDelegate;
import gov.hhs.fha.nhinc.orchestration.NhinResponseProcessor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201305Processor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryPolicyChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201306Transforms;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

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
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
import org.junit.Ignore;


/**
 * Runtime test class
 * @author paul.eftis
 */
@Ignore
public class EntityPatientDiscoveryOrchImplRuntimeTest{

    private Log log = LogFactory.getLog(getClass());
    private ExecutorService regularExecutor = null;
    private ExecutorService largejobExecutor = null;



    /**
     * We construct the orch impl class with references to both executor services
     * that could be used for this particular orchestration instance.
     * Determination of which executor service to use (largejob or regular) is based on
     * the size of the pdlist and configs
     */
    public EntityPatientDiscoveryOrchImplRuntimeTest(ExecutorService e, ExecutorService le){
        regularExecutor = e;
        largejobExecutor = le;
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
    public RespondingGatewayPRPAIN201306UV02ResponseType entityPatientDiscoveryOrchImplFanoutTest(
            RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion,
            List<UrlInfo> testList){

        log.debug("EntityPatientDiscoveryOrchImplRuntimeTest::entityPatientDiscoveryOrchImplFanoutTest");
        RespondingGatewayPRPAIN201306UV02ResponseType response = new RespondingGatewayPRPAIN201306UV02ResponseType();
        // quick rig for testing to switch between a0 and a1
        // note that a0 and a1 would be handled by different methods if they were different
        boolean responseIsSpecA0 = true;
        NhincConstants.GATEWAY_API_LEVEL gatewayLevel =
                ConnectionManagerCache.getInstance().getApiVersionForNhinTarget(
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
        log.debug("EntityPatientDiscoveryOrchImplRuntimeTest set responseIsSpecA0=" + responseIsSpecA0);


        try{
            List<UrlInfo> urlInfoList = getEndpoints(request.getNhinTargetCommunities());
            // loop through the communities and send request if results were not null
            if((urlInfoList == null) || (urlInfoList.isEmpty())){
                log.warn("No targets were found for the Patient Discovery Request");
                throw new Exception("No Endpoints For Communities Found!!!");
            }else{
                List<UrlInfo> targetList = urlInfoList;
                if(testList != null && testList.size() > 0){
                    // this is load test fanout to testList
                    targetList = testList;
                }
                List<NhinCallableRequest<EntityPatientDiscoveryOrchestratable>> callableList =
                        new ArrayList<NhinCallableRequest<EntityPatientDiscoveryOrchestratable>>();
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
                        NhinDelegate nd = new NhinPatientDiscoveryDelegate();
                        NhinResponseProcessor np = null;
                        if(responseIsSpecA0){
                            np = new EntityPatientDiscoveryProcessor(NhincConstants.GATEWAY_API_LEVEL.LEVEL_g0);
                        }else{
                            np = new EntityPatientDiscoveryProcessor(NhincConstants.GATEWAY_API_LEVEL.LEVEL_g1);
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

                        EntityPatientDiscoveryOrchestratable message = new EntityPatientDiscoveryOrchestratable(
                                    nd, np, null, null, assertion, NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME,
                                    target, newRequest.getPRPAIN201305UV02());
                        callableList.add(new NhinCallableRequest<EntityPatientDiscoveryOrchestratable>(message));

                        log.debug("EntityPatientDiscoveryOrchImplRuntimeTest added NhinCallableRequest"
                                + " for hcid=" + target.getHomeCommunity().getHomeCommunityId());
                    }else{
                        log.debug("EntityPatientDiscoveryOrchImplRuntimeTest Policy Check Failed for homeId="
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

                // note that if responseIsSpecA0 taskexecutor is set to return EntityPatientDiscoveryOrchestratable_a0
                // else  taskexecutor set to return EntityPatientDiscoveryOrchestratable_a1
                EntityPatientDiscoveryOrchestratable_a0 orchResponse_a0 = null;
                EntityPatientDiscoveryOrchestratable_a1 orchResponse_a1 = null;
                if(responseIsSpecA0){
                    log.debug("EntityPatientDiscoveryOrchImplRuntimeTest executing task to return spec a0 cumulative response");
                    NhinTaskExecutor<EntityPatientDiscoveryOrchestratable_a0, EntityPatientDiscoveryOrchestratable> dqexecutor =
                            new NhinTaskExecutor<EntityPatientDiscoveryOrchestratable_a0, EntityPatientDiscoveryOrchestratable>(
                            ExecutorServiceHelper.getInstance().checkExecutorTaskIsLarge(callableList.size()) ? largejobExecutor : regularExecutor,
                            callableList, transactionId);
                    dqexecutor.executeTask();
                    orchResponse_a0 = (EntityPatientDiscoveryOrchestratable_a0)dqexecutor.getFinalResponse();
                    response = orchResponse_a0.getCumulativeResponse();

                    // add any errors from policyErrList to response
                    for(CommunityPRPAIN201306UV02ResponseType policyError : policyErrList){
                        response.getCommunityResponse().add(policyError);
                    }
                }else{
                    log.debug("EntityPatientDiscoveryOrchImplRuntimeTest executing task to return spec a1 cumulative response");
                    NhinTaskExecutor<EntityPatientDiscoveryOrchestratable_a1, EntityPatientDiscoveryOrchestratable> dqexecutor =
                            new NhinTaskExecutor<EntityPatientDiscoveryOrchestratable_a1, EntityPatientDiscoveryOrchestratable>(
                            ExecutorServiceHelper.getInstance().checkExecutorTaskIsLarge(callableList.size()) ? largejobExecutor : regularExecutor,
                            callableList, transactionId);
                    dqexecutor.executeTask();
                    orchResponse_a1 = (EntityPatientDiscoveryOrchestratable_a1)dqexecutor.getFinalResponse();
                    response = orchResponse_a1.getCumulativeResponse();

                    // add any errors from policyErrList to response
                    for(CommunityPRPAIN201306UV02ResponseType policyError : policyErrList){
                        response.getCommunityResponse().add(policyError);
                    }
                }

                log.debug("EntityPatientDiscoveryOrchImplRuntimeTest taskexecutor done and received cumulative response");
            }
        }catch(Exception e){
            // generate error message and add to response
            CommunityPRPAIN201306UV02ResponseType communityResponse = new CommunityPRPAIN201306UV02ResponseType();
            communityResponse.setPRPAIN201306UV02((new HL7PRPA201306Transforms()).createPRPA201306ForErrors(request.getPRPAIN201305UV02(),
                        e.getMessage()));
            response.getCommunityResponse().add(communityResponse);
        }

        log.debug("EntityPatientDiscoveryOrchImplRuntimeTest done");
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
