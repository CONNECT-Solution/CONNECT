package gov.hhs.fha.nhinc.docquery.entity.test;

import gov.hhs.fha.nhinc.gateway.executorservice.DQProcessor;
import gov.hhs.fha.nhinc.gateway.executorservice.DQClient;
import gov.hhs.fha.nhinc.gateway.executorservice.ResponseWrapper;
import gov.hhs.fha.nhinc.gateway.executorservice.TaskExecutor;

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import gov.hhs.fha.nhinc.docquery.entity.EntityDocQueryHelper;
import gov.hhs.fha.nhinc.docquery.entity.DocQuerySender;
import gov.hhs.fha.nhinc.common.auditlog.AdhocQueryResponseMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
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

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author paul.eftis (updated 10/15/2011 to implement new concurrent request handling/fanout)
 */
public class EntityDocQueryOrchImplTest{

    private Log log = null;
    private String localHomeCommunity = null;
    private ExecutorService regularExecutor = null;
    private ExecutorService largejobExecutor = null;

    // quick rig for test
    private boolean isTest = false;
    private int requestCount = 0;
    private String testAAId = null;
    public void setTest(int requestcount, String aaid){
        requestCount = requestcount;
        testAAId = aaid;
        isTest = true;
    }


    /**
     * We construct the orch impl class with references to both executor services
     * that could be used for this particular orchestration instance.
     * Determination of which executor service to use (largejob or regular) is based on
     * the size of the correlationsResult and configs
     */
    public EntityDocQueryOrchImplTest(ExecutorService e, ExecutorService le){
        log = createLogger();
        regularExecutor = e;
        largejobExecutor = le;
    }


    protected Log createLogger(){
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }


    /**
     *
     * @param adhocQueryRequest
     * @param assertion
     * @param targets
     * @return <code>AdhocQueryResponse</code>
     */
    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest adhocQueryRequest,
            AssertionType assertion, NhinTargetCommunitiesType targets) {
        log.debug("Entering EntityDocQuerySecuredImpl.respondingGatewayCrossGatewayQuery...");

        AdhocQueryResponse response = null;
        CMUrlInfos urlInfoList = null;
        boolean isTargeted = false;

        DocQueryAuditLog auditLog = new DocQueryAuditLog();
        RespondingGatewayCrossGatewayQuerySecuredRequestType request = new RespondingGatewayCrossGatewayQuerySecuredRequestType();
        request.setAdhocQueryRequest(adhocQueryRequest);
        request.setNhinTargetCommunities(targets);
        String targetHomeCommunityId = HomeCommunityMap.getCommunityIdFromTargetCommunities(targets);
        auditDocQueryRequest(request, assertion, auditLog, targetHomeCommunityId);

        try{
            if (targets != null &&
                    NullChecker.isNotNullish(targets.getNhinTargetCommunity())){
                isTargeted = true;
            }

            // Obtain all the URLs for the targets being sent to
            try{
                urlInfoList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(targets, NhincConstants.DOC_QUERY_SERVICE_NAME);
            }catch(Exception ex){
                log.error("Failed to obtain target URLs", ex);
            }

            // Validate that the message is not null
            if (adhocQueryRequest != null &&
                    adhocQueryRequest.getAdhocQuery() != null &&
                    NullChecker.isNotNullish(adhocQueryRequest.getAdhocQuery().getSlot())){
                List<SlotType1> slotList = adhocQueryRequest.getAdhocQuery().getSlot();
                String localAA = new EntityDocQueryHelper().getLocalAssigningAuthority(slotList);
                String uniquePatientId = new EntityDocQueryHelper().getUniquePatientId(slotList);
                log.debug("respondingGatewayCrossGatewayQuery EntityDocQueryHelper uniquePatientId: " + uniquePatientId
                        + " and localAA=" + localAA);

                // for test we generate the correlationsResult rather than using
                // the list from patient correlations in db
                List<QualifiedSubjectIdentifierType> correlationsResult = new ArrayList<QualifiedSubjectIdentifierType>();
                if(isTest){
                    log.debug("EntityDocQueryOrchImpl running test");
                    for(int i = 0; i < requestCount; i++){
                        QualifiedSubjectIdentifierType subject = new QualifiedSubjectIdentifierType();
                        subject.setAssigningAuthorityIdentifier(localAA);
                        subject.setSubjectIdentifier(uniquePatientId);
                        correlationsResult.add(subject);
                    }
                }else{
                    correlationsResult = new EntityDocQueryHelper().retreiveCorrelations(slotList, urlInfoList, assertion, isTargeted, getLocalHomeCommunityId());
                }

                // Make sure the valid results back
                if(NullChecker.isNotNullish(correlationsResult)){

                    /************************************************************************
                     * We replace the current code here with the concurrent fanout impl
                     *
                    QualifiedSubjectIdentifiersType subjectIds = new QualifiedSubjectIdentifiersType();
                    for(QualifiedSubjectIdentifierType subjectId : correlationsResult){
                        if(subjectId != null){
                            subjectIds.getQualifiedSubjectIdentifier().add(subjectId);
                        }
                    }
                    String transactionId = startTransaction(aggregator, subjectIds);
                    sendQueryMessages(transactionId, correlationsResult, adhocQueryRequest, assertion, localAA, uniquePatientId);
                    response = retrieveDocQueryResults(aggregator, transactionId);
                    ***********************************************************************/

                    String transactionId = (UUID.randomUUID()).toString();
                    DQProcessor<QualifiedSubjectIdentifierType, AdhocQueryRequest, AdhocQueryResponse, AdhocQueryResponse> dqprocessor =
                            new DQProcessor<QualifiedSubjectIdentifierType, AdhocQueryRequest, AdhocQueryResponse, AdhocQueryResponse>();

                    DQClient<QualifiedSubjectIdentifierType, AdhocQueryRequest, ResponseWrapper> dqclient =
                        new DQClient<QualifiedSubjectIdentifierType, AdhocQueryRequest, ResponseWrapper>(
                            transactionId, assertion, adhocQueryRequest, localAA, uniquePatientId);

                    @SuppressWarnings("static-access")
                    TaskExecutor<QualifiedSubjectIdentifierType, AdhocQueryRequest, AdhocQueryResponse> dqexecutor =
                            new TaskExecutor<QualifiedSubjectIdentifierType, AdhocQueryRequest, AdhocQueryResponse>(
                                ExecutorServiceHelper.getInstance().checkExecutorTaskIsLarge(correlationsResult.size()) ? largejobExecutor : regularExecutor ,
                                dqprocessor, dqclient, correlationsResult,
                                adhocQueryRequest, transactionId);

                    dqexecutor.executeTask();
                    response = dqexecutor.getFinalResponse();
                    log.debug("EntityDocQueryOrchImpl taskexecutor done and received response");

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
        log.debug("Exiting EntityDocQuerySecuredImpl.respondingGatewayCrossGatewayQuery...");
        return response;
    }


    private void auditDocQueryRequest(RespondingGatewayCrossGatewayQuerySecuredRequestType request, 
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

        if(localHomeCommunity != null){
            sHomeCommunity = localHomeCommunity;
        }else{
            try{
                sHomeCommunity = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                        NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
            }catch(Exception ex){
                log.error(ex.getMessage());
            }
        }
        return sHomeCommunity;
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
