package gov.hhs.fha.nhinc.docquery.entity.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import gov.hhs.fha.nhinc.docquery.entity.EntityDocQueryOrchImpl;
//import gov.hhs.fha.nhinc.docquery.entity.EntityDocQueryOrchImplRuntimeTest;
import gov.hhs.fha.nhinc.docquery.entity.EntityDocQueryHelper;
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
import gov.hhs.fha.nhinc.connectmgr.UrlInfo;

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
public class EntityDocQueryOrchImplTest {

    private Log log = null;
    private String localHomeCommunity = null;
    private ExecutorService regularExecutor = null;
    private ExecutorService largejobExecutor = null;

    // quick rig for test
    private boolean isTest = false;
    private int requestCount = 0;
    private String testAAId = null;

    public void setTest(int requestcount, String aaid) {
        requestCount = requestcount;
        testAAId = aaid;
        isTest = true;
    }

    /**
     * We construct the orch impl class with references to both executor services that could be used for this particular
     * orchestration instance. Determination of which executor service to use (largejob or regular) is based on the size
     * of the correlationsResult and configs
     */
    public EntityDocQueryOrchImplTest(ExecutorService e, ExecutorService le) {
        log = createLogger();
        regularExecutor = e;
        largejobExecutor = le;
    }

    protected Log createLogger() {
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
        log.debug("Entering EntityDocQueryOrchImplTest.respondingGatewayCrossGatewayQuery...");

        AdhocQueryResponse response = null;
        List<UrlInfo> urlInfoList = null;
        boolean isTargeted = false;

        DocQueryAuditLog auditLog = new DocQueryAuditLog();
        RespondingGatewayCrossGatewayQuerySecuredRequestType request = new RespondingGatewayCrossGatewayQuerySecuredRequestType();
        request.setAdhocQueryRequest(adhocQueryRequest);
        request.setNhinTargetCommunities(targets);
        String targetHomeCommunityId = HomeCommunityMap.getCommunityIdFromTargetCommunities(targets);
        auditDocQueryRequest(request, assertion, auditLog, targetHomeCommunityId);

        try {
            if (targets != null && NullChecker.isNotNullish(targets.getNhinTargetCommunity())) {
                isTargeted = true;
            }

            // Obtain all the URLs for the targets being sent to
            try {
                urlInfoList = ConnectionManagerCache.getInstance().getEndpontURLFromNhinTargetCommunities(targets,
                        NhincConstants.DOC_QUERY_SERVICE_NAME);
            } catch (Exception ex) {
                log.error("Failed to obtain target URLs", ex);
            }

            // Validate that the message is not null
            if (adhocQueryRequest != null && adhocQueryRequest.getAdhocQuery() != null
                    && NullChecker.isNotNullish(adhocQueryRequest.getAdhocQuery().getSlot())) {
                List<SlotType1> slotList = adhocQueryRequest.getAdhocQuery().getSlot();
                String localAA = new EntityDocQueryHelper().getLocalAssigningAuthority(slotList);
                String uniquePatientId = new EntityDocQueryHelper().getUniquePatientId(slotList);
                log.debug("EntityDocQueryOrchImplTest uniquePatientId: " + uniquePatientId + " and localAA=" + localAA);

                // for test we generate the correlationsResult rather than using
                // the list from patient correlations in db
                List<QualifiedSubjectIdentifierType> testList = new ArrayList<QualifiedSubjectIdentifierType>();
                if (isTest) {
                    log.debug("EntityDocQueryOrchImplTest running test");
                    for (int i = 0; i < requestCount; i++) {
                        QualifiedSubjectIdentifierType subject = new QualifiedSubjectIdentifierType();
                        subject.setAssigningAuthorityIdentifier(testAAId);
                        subject.setSubjectIdentifier(uniquePatientId);
                        testList.add(subject);
                    }
                    // EntityDocQueryOrchImplRuntimeTest was removed from core lib
                    // EntityDocQueryOrchImplRuntimeTest orchestrator =
                    // new EntityDocQueryOrchImplRuntimeTest(regularExecutor, largejobExecutor);
                    // response = orchestrator.entityDocQueryOrchImplFanoutTest(
                    // adhocQueryRequest, assertion, testList);
                } else {
                    // just do normal test
                    EntityDocQueryOrchImpl orchestrator = new EntityDocQueryOrchImpl(regularExecutor, largejobExecutor);
                    response = orchestrator.respondingGatewayCrossGatewayQuery(adhocQueryRequest, assertion, targets);
                }
                log.debug("EntityDocQueryOrchImplTest received response");

            } else {
                log.error("Incomplete doc query message");
                response = createErrorResponse("Incomplete/empty adhocquery message");
            }
        } catch (Exception e) {
            log.error("Error occured processing doc query on entity interface: " + e.getMessage(), e);
            response = createErrorResponse("Fault encountered processing internal document query" + " exception="
                    + e.getMessage());
        }
        auditDocQueryResponse(response, assertion, auditLog, targetHomeCommunityId);
        log.debug("Exiting EntityDocQueryOrchImplTest.respondingGatewayCrossGatewayQuery...");
        return response;
    }

    private void auditDocQueryRequest(RespondingGatewayCrossGatewayQuerySecuredRequestType request,
            AssertionType assertion, DocQueryAuditLog auditLog, String targetHomeCommunityId) {

        if (auditLog != null) {
            auditLog.auditDQRequest(request.getAdhocQueryRequest(), assertion, targetHomeCommunityId,
                    NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        }
    }

    private void auditDocQueryResponse(AdhocQueryResponse response, AssertionType assertion, DocQueryAuditLog auditLog,
            String targetHomeCommunityId) {

        if (auditLog != null) {
            AdhocQueryResponseMessageType auditMsg = new AdhocQueryResponseMessageType();
            auditMsg.setAdhocQueryResponse(response);
            auditMsg.setAssertion(assertion);
            auditLog.auditDQResponse(response, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                    NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, targetHomeCommunityId);
        }
    }

    protected String getLocalHomeCommunityId() {
        String sHomeCommunity = null;

        if (localHomeCommunity != null) {
            sHomeCommunity = localHomeCommunity;
        } else {
            try {
                sHomeCommunity = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                        NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
            } catch (Exception ex) {
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
