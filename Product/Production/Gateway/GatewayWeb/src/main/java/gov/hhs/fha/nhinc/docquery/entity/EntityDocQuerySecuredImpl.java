package gov.hhs.fha.nhinc.docquery.entity;

import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.common.auditlog.AdhocQueryResponseMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifiersType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsRequestType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsResponseType;
import gov.hhs.fha.nhinc.docquery.DocQueryAuditLog;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientcorrelationfacade.proxy.PatientCorrelationFacadeProxy;
import gov.hhs.fha.nhinc.patientcorrelationfacade.proxy.PatientCorrelationFacadeProxyObjectFactory;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import gov.hhs.fha.nhinc.gateway.aggregator.document.DocumentConstants;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfo;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfos;
import gov.hhs.fha.nhinc.gateway.aggregator.GetAggResultsDocQueryRequestType;
import gov.hhs.fha.nhinc.gateway.aggregator.GetAggResultsDocQueryResponseType;
import gov.hhs.fha.nhinc.gateway.aggregator.StartTransactionDocQueryRequestType;
import gov.hhs.fha.nhinc.gateway.aggregator.document.DocQueryAggregator;
import java.util.HashMap;

/**
 *
 *
 * @author Neil Webb
 */
public class EntityDocQuerySecuredImpl {

    private static final long SLEEP_MS = 1000;
    private static final long AGGREGATOR_TIMEOUT_MS = 40000;
    private Log log = null;
    private String localHomeCommunity = null;
    private String localAssigningAuthorityId = null;

    public EntityDocQuerySecuredImpl() {
        log = createLogger();
        localHomeCommunity = getLocalHomeCommunityId();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(EntityDocQuerySecuredImpl.class));
    }

    protected String getLocalHomeCommunityId() {
        String sHomeCommunity = null;

        if (localHomeCommunity != null) {
            sHomeCommunity = localHomeCommunity;
        } else {
            try {
                sHomeCommunity = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
            } catch (PropertyAccessException ex) {
                log.error(ex.getMessage());
            }
        }
        return sHomeCommunity;
    }

    protected DocQueryAuditLog createAuditLog() {
        return new DocQueryAuditLog();
    }

    protected DocQueryAggregator createDocQueryAggregator() {
        return new DocQueryAggregator();
    }

    protected boolean getPropertyBoolean(String sPropertiesFile, String sPropertyName) {
        boolean sPropertyValue = false;
        try {
            sPropertyValue = PropertyAccessor.getPropertyBoolean(sPropertiesFile, sPropertyName);
        } catch (PropertyAccessException ex) {
            log.error(ex.getMessage());
        }
        return sPropertyValue;
    }

    protected PatientCorrelationFacadeProxy getPatientCorrelationFacadeProxy() {
        PatientCorrelationFacadeProxyObjectFactory patCorrelationFactory = new PatientCorrelationFacadeProxyObjectFactory();
        return patCorrelationFactory.getPatientCorrelationFacadeProxy();
    }

    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQuerySecuredRequestType request, WebServiceContext context) {
        // Collect assertion
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        return respondingGatewayCrossGatewayQuery(request, assertion);
    }

    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQuerySecuredRequestType request, AssertionType assertion) {
        log.debug("Entering EntityDocQuerySecuredImpl.respondingGatewayCrossGatewayQuery...");

        AdhocQueryResponse response = null;
        CMUrlInfos urlInfoList = null;
        boolean isTargeted = false;

        DocQueryAuditLog auditLog = createAuditLog();
        auditDocQueryResquest(request, assertion, auditLog);

        try {
            DocQueryAggregator aggregator = createDocQueryAggregator();

            if (request.getNhinTargetCommunities() != null &&
                    NullChecker.isNotNullish(request.getNhinTargetCommunities().getNhinTargetCommunity())) {
                isTargeted = true;
            }

            // Obtain all the URLs for the targets being sent to
            try {
                urlInfoList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(request.getNhinTargetCommunities(), NhincConstants.DOC_QUERY_SERVICE_NAME);
            } catch (ConnectionManagerException ex) {
                log.error("Failed to obtain target URLs");
                return null;
            }

            // Validate that the message is not null
            if (request.getAdhocQueryRequest() != null &&
                    request.getAdhocQueryRequest().getAdhocQuery() != null &&
                    NullChecker.isNotNullish(request.getAdhocQueryRequest().getAdhocQuery().getSlot())) {
                List<SlotType1> slotList = request.getAdhocQueryRequest().getAdhocQuery().getSlot();

                RetrievePatientCorrelationsResponseType correlationsResult = retreiveCorrelations(slotList, urlInfoList, assertion, isTargeted);

                // Make sure the valid results back
                if (correlationsResult != null &&
                        NullChecker.isNotNullish(correlationsResult.getQualifiedPatientIdentifier())) {

                    QualifiedSubjectIdentifiersType subjectIds = new QualifiedSubjectIdentifiersType();
                    for (QualifiedSubjectIdentifierType subjectId : correlationsResult.getQualifiedPatientIdentifier()) {
                        if (subjectId != null) {
                            subjectIds.getQualifiedSubjectIdentifier().add(subjectId);
                        }
                    }

                    String transactionId = startTransaction(aggregator, subjectIds);

                    sendQueryMessages(transactionId, correlationsResult, request.getAdhocQueryRequest(), assertion);

                    response = retrieveDocQueryResults(aggregator, transactionId);
                } else {
                    log.error("No patient correlations found.");
                    response = createErrorResponse("No patient correlations found.");
                }
            } else {
                log.error("Incomplete doc query message");
                response = createErrorResponse("Incomplete doc query message");
            }
        } catch (Throwable t) {
            log.error("Error occured processing doc query on entity interface: " + t.getMessage(), t);
            response = createErrorResponse("Fault encountered processing internal document query");
        }
        auditDocQueryResponse(response, assertion, auditLog);
        log.debug("Exiting EntityDocQuerySecuredImpl.respondingGatewayCrossGatewayQuery...");
        return response;
    }

    private void sendQueryMessages(String transactionId, RetrievePatientCorrelationsResponseType correlationsResult, AdhocQueryRequest queryRequest, AssertionType assertion) {
        for (QualifiedSubjectIdentifierType subId : correlationsResult.getQualifiedPatientIdentifier()) {
            DocQuerySender querySender = new DocQuerySender(transactionId, assertion, subId, queryRequest, localAssigningAuthorityId);
            querySender.sendMessage();
        }
    }

    private AdhocQueryResponse retrieveDocQueryResults(DocQueryAggregator aggregator, String transactionId) {
        log.debug("Begin retrieveDocQueryResults");
        AdhocQueryResponse response = null;
        boolean retrieveTimedOut = false;

        // Agggregator query request message
        GetAggResultsDocQueryRequestType aggResultsRequest = new GetAggResultsDocQueryRequestType();
        aggResultsRequest.setTransactionId(transactionId);
        aggResultsRequest.setTimedOut(retrieveTimedOut);

        // Loop until responses are received
        long startTime = System.currentTimeMillis();
        while (response == null) {
            GetAggResultsDocQueryResponseType aggResultsResponse = aggregator.getAggResults(aggResultsRequest);
            String retrieveStatus = aggResultsResponse.getStatus();
            if (DocumentConstants.COMPLETE_TEXT.equals(retrieveStatus)) {
                response = aggResultsResponse.getAdhocQueryResponse();
            } else if (DocumentConstants.FAIL_TEXT.equals(retrieveStatus)) {
                log.error("Document query aggregator reports failurt - returning error");
                response = createErrorResponse("Processing internal document query");
            } else {
                retrieveTimedOut = retrieveTimedOut(startTime);
                if (retrieveTimedOut) {
                    aggResultsRequest.setTimedOut(retrieveTimedOut);
                    aggResultsResponse = aggregator.getAggResults(aggResultsRequest);
                    if (DocumentConstants.COMPLETE_TEXT.equals(retrieveStatus)) {
                        response = aggResultsResponse.getAdhocQueryResponse();
                    } else {
                        log.warn("Document query aggregation timeout - returning error.");
                        response = createErrorResponse("Processing internal document query - failure in timeout");
                    }
                } else {
                    try {
                        Thread.sleep(SLEEP_MS);
                    } catch (Throwable t) {
                    }
                }
            }
        }
        log.debug("End retrieveDocQueryResults");
        return response;
    }

    private boolean retrieveTimedOut(long startTime) {
        long timeout = startTime + AGGREGATOR_TIMEOUT_MS;
        return (timeout < System.currentTimeMillis());
    }

    private void auditDocQueryResquest(RespondingGatewayCrossGatewayQuerySecuredRequestType request, AssertionType assertion, DocQueryAuditLog auditLog) {
        if (auditLog != null) {
            auditLog.audit(request, assertion);
        }
    }

    private void auditDocQueryResponse(AdhocQueryResponse response, AssertionType assertion, DocQueryAuditLog auditLog) {
        if (auditLog != null) {
            AdhocQueryResponseMessageType auditMsg = new AdhocQueryResponseMessageType();
            auditMsg.setAdhocQueryResponse(response);
            auditMsg.setAssertion(assertion);
            auditLog.auditResponse(auditMsg, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        }
    }

    private RetrievePatientCorrelationsResponseType retreiveCorrelations(List<SlotType1> slotList, CMUrlInfos urlInfoList, AssertionType assertion, boolean isTargeted) {
        RetrievePatientCorrelationsResponseType results = null;
        RetrievePatientCorrelationsRequestType patientCorrelationReq = new RetrievePatientCorrelationsRequestType();
        QualifiedSubjectIdentifierType qualSubId = new QualifiedSubjectIdentifierType();
        boolean querySelf = false;

        // For each slot process each of the Patient Id slots
        for (SlotType1 slot : slotList) {

            // Find the Patient Id slot
            if (slot.getName().equalsIgnoreCase(NhincConstants.DOC_QUERY_XDS_PATIENT_ID_SLOT_NAME)) {
                if (slot.getValueList() != null &&
                        NullChecker.isNotNullish(slot.getValueList().getValue()) &&
                        NullChecker.isNotNullish(slot.getValueList().getValue().get(0))) {
                    qualSubId.setSubjectIdentifier(PatientIdFormatUtil.parsePatientId(slot.getValueList().getValue().get(0)));
                    localAssigningAuthorityId = PatientIdFormatUtil.parseCommunityId(slot.getValueList().getValue().get(0));
                    qualSubId.setAssigningAuthorityIdentifier(localAssigningAuthorityId);

                    log.info("Extracting subject id: " + qualSubId.getSubjectIdentifier());
                    log.info("Extracting assigning authority id: " + qualSubId.getAssigningAuthorityIdentifier());
                    patientCorrelationReq.setQualifiedPatientIdentifier(qualSubId);
                }

                // Save off the target home community ids to use in the patient correlation query
                if (urlInfoList != null &&
                        NullChecker.isNotNullish(urlInfoList.getUrlInfo())) {
                    for (CMUrlInfo target : urlInfoList.getUrlInfo()) {
                        if (NullChecker.isNotNullish(target.getHcid())) {
                            patientCorrelationReq.getTargetHomeCommunity().add(target.getHcid());

                            if (target.getHcid().equals(localHomeCommunity) &&
                                    isTargeted == true) {
                                querySelf = true;
                            }
                        }
                    }
                }

                break;
            }
        }

        if (!querySelf) {
            querySelf = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.DOC_QUERY_SELF_PROPERTY_NAME);
        }

        // Retreive Patient Correlations this patient
        PatientCorrelationFacadeProxy proxy = getPatientCorrelationFacadeProxy();

        log.debug("Setting Assertion: " + assertion.getSSN());
        patientCorrelationReq.setAssertion(assertion);

        results = proxy.retrievePatientCorrelations(patientCorrelationReq);

        // Make sure the response is valid
        if (results != null &&
                results.getQualifiedPatientIdentifier() != null) {
            // If we are querying ourselves as well then add this community to the list of correlations
            if (querySelf == true) {
                results.getQualifiedPatientIdentifier().add(patientCorrelationReq.getQualifiedPatientIdentifier());
            }
        }

        return results;
    }

    private String startTransaction(DocQueryAggregator aggregator, QualifiedSubjectIdentifiersType subjectIds) {
        StartTransactionDocQueryRequestType docQueryStartTransaction = new StartTransactionDocQueryRequestType();
        docQueryStartTransaction.setQualifiedPatientIdentifiers(subjectIds);

        HashMap<String, String> assigningAuthorityToHomeCommunityMap = new HashMap<String, String>();
        log.debug("Starting doc query transaction");
        String transactionId = aggregator.startTransaction(docQueryStartTransaction, assigningAuthorityToHomeCommunityMap);
        if (log.isDebugEnabled()) {
            log.debug("Doc query transaction id: " + transactionId);
        }
        return transactionId;
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
