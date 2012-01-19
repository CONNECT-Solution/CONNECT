package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.gateway.executorservice.NhinCallableRequest;
import gov.hhs.fha.nhinc.gateway.executorservice.NhinTaskExecutor;

import gov.hhs.fha.nhinc.orchestration.NhinDelegate;
import gov.hhs.fha.nhinc.orchestration.NhinResponseProcessor;

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gov.hhs.fha.nhinc.common.auditlog.AdhocQueryResponseMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.docquery.DocQueryAuditLog;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfos;
import gov.hhs.fha.nhinc.gateway.executorservice.ExecutorServiceHelper;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import gov.hhs.fha.nhinc.transform.document.DocumentQueryTransform;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestEventType;
import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestMessageType;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Runtime test class
 * @author paul.eftis
 */
public class EntityDocQueryOrchImplRuntimeTest{

    private Log log = null;
    private ExecutorService regularExecutor = null;
    private ExecutorService largejobExecutor = null;


    /**
     * We construct the orch impl class with references to both executor services
     * that could be used for this particular orchestration instance.
     * Determination of which executor service to use (largejob or regular) is based on
     * the size of the correlationsResult and configs
     */
    public EntityDocQueryOrchImplRuntimeTest(ExecutorService e, ExecutorService le){
        log = createLogger();
        regularExecutor = e;
        largejobExecutor = le;
    }


    protected Log createLogger(){
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }


    /**
     * If testList is passed in (i.e. not null/empty), will fan-out DQ
     * to entire testList (i.e. will ignore correlations)
     * If testList is null/empty, will just do normal DQ fan-out using
     * correlations for patientId passed in adhocQueryRequest
     *
     * @param adhocQueryRequest
     * @param assertion
     * @param testList
     * @return <code>AdhocQueryResponse</code>
     */
    @SuppressWarnings("static-access")
    public AdhocQueryResponse entityDocQueryOrchImplFanoutTest(AdhocQueryRequest adhocQueryRequest,
            AssertionType assertion, List<QualifiedSubjectIdentifierType> testList){
        log.debug("EntityDocQueryOrchImplRuntimeTest::entityDocQueryOrchImplLoadTest");

        AdhocQueryResponse response = null;

        // quick rig for testing to switch between a0 and a1
        // note that a0 and a1 would be handled by different methods if they were different
        boolean responseIsSpecA0 = true;
        NhincConstants.GATEWAY_API_LEVEL gatewayLevel =
                ConnectionManagerCache.getApiVersionForNhinTarget(
                getLocalHomeCommunityId(), NhincConstants.DOC_QUERY_SERVICE_NAME);
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
        log.debug("EntityDocQueryOrchImplRuntimeTest set responseIsSpecA0=" + responseIsSpecA0);
        
        CMUrlInfos urlInfoList = null;
        boolean isTargeted = false;

        // audit initial request
        NhinTargetCommunitiesType targets = null;
        DocQueryAuditLog auditLog = new DocQueryAuditLog();
        RespondingGatewayCrossGatewayQuerySecuredRequestType request = new RespondingGatewayCrossGatewayQuerySecuredRequestType();
        request.setAdhocQueryRequest(adhocQueryRequest);
        request.setNhinTargetCommunities(targets);
        String targetHomeCommunityId = HomeCommunityMap.getCommunityIdFromTargetCommunities(targets);
        auditInitialEntityRequest(request, assertion, auditLog, targetHomeCommunityId);

        try{
            if (targets != null &&
                    NullChecker.isNotNullish(targets.getNhinTargetCommunity())){
                isTargeted = true;
            }

            // Obtain all the URLs for the targets being sent to
            try{
                urlInfoList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(targets, NhincConstants.DOC_QUERY_SERVICE_NAME);
            }catch(Exception ex){
                log.error("EntityDocQueryOrchImplRuntimeTest Failed to obtain target URLs", ex);
            }

            // Validate that the message is not null
            if (adhocQueryRequest != null &&
                    adhocQueryRequest.getAdhocQuery() != null &&
                    NullChecker.isNotNullish(adhocQueryRequest.getAdhocQuery().getSlot())){
                List<SlotType1> slotList = adhocQueryRequest.getAdhocQuery().getSlot();
                String localAA = new EntityDocQueryHelper().getLocalAssigningAuthority(slotList);
                String uniquePatientId = new EntityDocQueryHelper().getUniquePatientId(slotList);
                log.debug("EntityDocQueryOrchImplRuntimeTest uniquePatientId: " + uniquePatientId
                        + " and localAA=" + localAA);

                 List<QualifiedSubjectIdentifierType> correlationsResult =
                         new EntityDocQueryHelper().retreiveCorrelations(slotList, urlInfoList,
                         assertion, isTargeted, getLocalHomeCommunityId());

                if(testList != null && testList.size() > 0){
                    // this is load test fanout to testList
                    correlationsResult = testList;
                }

                if(NullChecker.isNotNullish(correlationsResult)){
                    /************************************************************************
                     * We replaced the 3.2.1 connect code here with the new 3.3 concurrent fanout impl
                     * Note that all response processing is done in the NhinResponseProcessor
                    ***********************************************************************/
                    List<NhinCallableRequest<EntityDocQueryOrchestratable>> callableList =
                            new ArrayList<NhinCallableRequest<EntityDocQueryOrchestratable>>();
                    String transactionId = (UUID.randomUUID()).toString();

                    // we hold the error messages for any failed policy checks in policyErrList
                    RegistryErrorList policyErrList = new RegistryErrorList();

                    for(QualifiedSubjectIdentifierType identifier : correlationsResult){
                        NhinTargetSystemType target = new NhinTargetSystemType();

                        HomeCommunityType targetCommunity = new EntityDocQueryHelper().lookupHomeCommunityId(
                                identifier.getAssigningAuthorityIdentifier(), localAA, getLocalHomeCommunityId());
                        if(targetCommunity != null){
                            target.setHomeCommunity(targetCommunity);
                        }

                        if(isValidPolicy(adhocQueryRequest, assertion, targetCommunity)){
                            NhinDelegate nd = new NhinDocQueryDelegate();
                            NhinResponseProcessor np = null;
                            if(responseIsSpecA0){
                                np = new EntityDocQueryProcessor(NhincConstants.GATEWAY_API_LEVEL.LEVEL_g0);
                            }else{
                                np = new EntityDocQueryProcessor(NhincConstants.GATEWAY_API_LEVEL.LEVEL_g1);
                            }
                            // Replace the patient id in the document query message
                            // and clone the original adhocQueryRequest
                            DocumentQueryTransform transform = new DocumentQueryTransform();
                            AdhocQueryRequest clonedQueryRequest = transform.replaceAdhocQueryPatientId(
                                    cloneRequest(adhocQueryRequest), getLocalHomeCommunityId(),
                                    identifier.getAssigningAuthorityIdentifier(),
                                    identifier.getSubjectIdentifier());

                            EntityDocQueryOrchestratable message = new EntityDocQueryOrchestratable(
                                        nd, np, null, null, assertion, NhincConstants.DOC_QUERY_SERVICE_NAME,
                                        target, clonedQueryRequest);
                            callableList.add(new NhinCallableRequest<EntityDocQueryOrchestratable>(message));

                            log.debug("EntityDocQueryOrchImplRuntimeTest added NhinCallableRequest"
                                    + " for hcid=" + target.getHomeCommunity().getHomeCommunityId());
                        }else{
                            log.debug("EntityDocQueryOrchImplRuntimeTest Policy Check Failed for homeId="
                                    + target.getHomeCommunity().getHomeCommunityId()
                                    + " and aaId=" + identifier.getAssigningAuthorityIdentifier());
                            RegistryError regErr = new RegistryError();
                            regErr.setCodeContext("Policy Check Failed for homeId=" + target.getHomeCommunity().getHomeCommunityId()
                                    + " and aaId=" + identifier.getAssigningAuthorityIdentifier());
                            regErr.setErrorCode("XDSRepositoryError");
                            regErr.setSeverity("Error");
                            policyErrList.getRegistryError().add(regErr);
                        }
                    }

                    // note that if responseIsSpecA0 taskexecutor is set to return EntityPatientDiscoveryOrchestratable_a0
                    // else  taskexecutor set to return EntityPatientDiscoveryOrchestratable_a1
                    EntityDocQueryOrchestratable_a0 orchResponse_g0 = null;
                    EntityDocQueryOrchestratable_a1 orchResponse_g1 = null;
                    if(responseIsSpecA0){
                        NhinTaskExecutor<EntityDocQueryOrchestratable_a0, EntityDocQueryOrchestratable> dqexecutor =
                                new NhinTaskExecutor<EntityDocQueryOrchestratable_a0, EntityDocQueryOrchestratable>(
                                ExecutorServiceHelper.getInstance().checkExecutorTaskIsLarge(correlationsResult.size()) ? largejobExecutor : regularExecutor,
                                callableList, transactionId);
                        dqexecutor.executeTask();
                        orchResponse_g0 = (EntityDocQueryOrchestratable_a0)dqexecutor.getFinalResponse();
                        response = orchResponse_g0.getCumulativeResponse();

                        // add any errors from policyErrList to response
                        if(response != null && response.getRegistryErrorList() != null
                                && response.getRegistryErrorList().getRegistryError() != null){
                            for(RegistryError re : policyErrList.getRegistryError()){
                                response.getRegistryErrorList().getRegistryError().add(re);
                            }
                        }
                    }else{
                        NhinTaskExecutor<EntityDocQueryOrchestratable_a1, EntityDocQueryOrchestratable> dqexecutor =
                                new NhinTaskExecutor<EntityDocQueryOrchestratable_a1, EntityDocQueryOrchestratable>(
                                ExecutorServiceHelper.getInstance().checkExecutorTaskIsLarge(correlationsResult.size()) ? largejobExecutor : regularExecutor,
                                callableList, transactionId);
                        dqexecutor.executeTask();
                        orchResponse_g1 = (EntityDocQueryOrchestratable_a1)dqexecutor.getFinalResponse();
                        response = orchResponse_g1.getCumulativeResponse();

                        // add any errors from policyErrList to response
                        if(response != null && response.getRegistryErrorList() != null
                                && response.getRegistryErrorList().getRegistryError() != null){
                            for(RegistryError re : policyErrList.getRegistryError()){
                                response.getRegistryErrorList().getRegistryError().add(re);
                            }
                        }
                    }

                    log.debug("EntityDocQueryOrchImplRuntimeTest taskexecutor done and received response");
                }else{
                    log.error("No patient correlations found.");
                    response = createErrorResponse("No patient correlations found.");
                }
            }else{
                log.error("Incomplete doc query message");
                response = createErrorResponse("Incomplete/empty adhocquery message");
            }
        }catch(Exception e){
            log.error("Error occured processing doc query on entity interface: " + e.getMessage(), e);
            response = createErrorResponse("Fault encountered processing internal document query"
                    + " exception=" + e.getMessage());
        }
        auditDocQueryResponse(response, assertion, auditLog, targetHomeCommunityId);
        log.debug("Exiting EntityDocQueryOrchImplRuntimeTest.respondingGatewayCrossGatewayQuery...");
        return response;
    }




    private void auditInitialEntityRequest(RespondingGatewayCrossGatewayQuerySecuredRequestType request,
            AssertionType assertion, DocQueryAuditLog auditLog, String targetHomeCommunityId){

        if(auditLog != null){
            auditLog.auditDQRequest(request.getAdhocQueryRequest(), assertion, targetHomeCommunityId, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        }
    }

    private void auditDocQueryResponse(AdhocQueryResponse response, AssertionType assertion, 
            DocQueryAuditLog auditLog, String targetHomeCommunityId){

        if (auditLog != null) {
            AdhocQueryResponseMessageType auditMsg = new AdhocQueryResponseMessageType();
            auditMsg.setAdhocQueryResponse(response);
            auditMsg.setAssertion(assertion);
            auditLog.auditDQResponse(response, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                    NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, targetHomeCommunityId);
        }
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


     /**
     * Policy Check verification done here
     * @param queryRequest
     * @param assertion
     * @return boolean
     */
    private boolean isValidPolicy(AdhocQueryRequest queryRequest, AssertionType assertion, HomeCommunityType targetCommunity) {
        boolean isValid = false;
        AdhocQueryRequestEventType checkPolicy = new AdhocQueryRequestEventType();
        AdhocQueryRequestMessageType checkPolicyMessage = new AdhocQueryRequestMessageType();
        checkPolicyMessage.setAdhocQueryRequest(queryRequest);
        checkPolicyMessage.setAssertion(assertion);
        checkPolicy.setMessage(checkPolicyMessage);
        checkPolicy.setDirection(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION);
        checkPolicy.setInterface(NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        checkPolicy.setReceivingHomeCommunity(targetCommunity);
        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        CheckPolicyRequestType policyReq = policyChecker.checkPolicyAdhocQuery(checkPolicy);
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq, assertion);
        /* if response='permit' */
        if (policyResp.getResponse().getResult().get(0).getDecision().value().equals(NhincConstants.POLICY_PERMIT)) {
            isValid = true;
        }
        return isValid;
    }


    private AdhocQueryRequest cloneRequest(AdhocQueryRequest request){
        AdhocQueryRequest newRequest = new AdhocQueryRequest();
        newRequest.setAdhocQuery(request.getAdhocQuery());
        newRequest.setComment(request.getComment());
        newRequest.setFederated(request.isFederated());
        newRequest.setFederation(request.getFederation());
        newRequest.setId(request.getId());
        newRequest.setMaxResults(request.getMaxResults());
        newRequest.setRequestSlotList(request.getRequestSlotList());
        newRequest.setResponseOption(request.getResponseOption());
        newRequest.setStartIndex(request.getStartIndex());
        return newRequest;
    }


    private AdhocQueryResponse createErrorResponse(String codeContext) {
        AdhocQueryResponse response = new AdhocQueryResponse();
        RegistryErrorList regErrList = new RegistryErrorList();
        response.setRegistryErrorList(regErrList);
        response.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
        RegistryError regErr = new RegistryError();
        regErrList.getRegistryError().add(regErr);
        regErr.setCodeContext(codeContext);
        regErr.setErrorCode("XDSRepositoryError");
        regErr.setSeverity("Error");
        return response;
    }




}
