package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestEventType;
import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCommunityMapping;
import gov.hhs.fha.nhinc.docquery.proxy.NhincProxyDocQuerySecuredImpl;
import gov.hhs.fha.nhinc.gateway.aggregator.SetResponseMsgDocQueryRequestType;
import gov.hhs.fha.nhinc.gateway.aggregator.document.DocQueryAggregator;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import gov.hhs.fha.nhinc.policyengine.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.document.DocumentQueryTransform;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import org.apache.commons.logging.Log;

/**
 *
 * @author Neil Webb
 */
public class DocQuerySender {

    private Log log = null;
    private String sLocalHomeCommunity = null;
    private String sLocalAssigningAuthorityId = null;
    private String sTransactionId = null;
    private AssertionType oAssertion;
    private QualifiedSubjectIdentifierType oSubjectId;
    private AdhocQueryRequest oOriginalQueryRequest;

    public DocQuerySender(String transactionId, AssertionType assertion, QualifiedSubjectIdentifierType subjectId, AdhocQueryRequest originalQueryRequest, String localAssigningAuthorityId) {
        log = createLogger();
        sLocalHomeCommunity = getLocalHomeCommunityId();
        oOriginalQueryRequest = originalQueryRequest;
        sTransactionId = transactionId;
        oAssertion = assertion;
        oSubjectId = subjectId;
        sLocalAssigningAuthorityId = localAssigningAuthorityId;
    }

    protected Log createLogger() {
        return ((log != null) ? log : org.apache.commons.logging.LogFactory.getLog(DocQuerySender.class));
    }

    protected String getLocalHomeCommunityId() {
        String sHomeCommunity = null;

        if (sLocalHomeCommunity != null) {
            sHomeCommunity = sLocalHomeCommunity;
        } else {
            try {
                sHomeCommunity = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
            } catch (PropertyAccessException ex) {
                log.error(ex.getMessage());
            }
        }
        return sHomeCommunity;
    }

    protected NhincProxyDocQuerySecuredImpl createNhincProxyDocQuerySecuredImpl() {
        return new NhincProxyDocQuerySecuredImpl();
    }

    protected HomeCommunityType lookupHomeCommunityId(String sAssigningAuthorityId) {
        HomeCommunityType targetCommunity = null;
        if ((sAssigningAuthorityId != null) && (sAssigningAuthorityId.equals(sLocalAssigningAuthorityId))) {
            /*
             * If the target is the local home community, the local
             * assigning authority may not be mapped to the local
             * home community in the community mapping. Set manually.
             */
            targetCommunity = new HomeCommunityType();
            targetCommunity.setHomeCommunityId(sLocalHomeCommunity);
            log.debug("Assigning authority was for the local home community. Set target to manual local home community id");
        } else {
            targetCommunity = ConnectionManagerCommunityMapping.getHomeCommunityByAssigningAuthority(sAssigningAuthorityId);
        }
        return targetCommunity;
    }

    protected DocumentQueryTransform createDocumentTransform() {
        return new DocumentQueryTransform();
    }

    protected DocQueryAggregator createDocQueryAggregator() {
        return new DocQueryAggregator();
    }

    public void sendMessage() {
        NhincProxyDocQuerySecuredImpl nhincDocQueryProxy = createNhincProxyDocQuerySecuredImpl();
        gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQuerySecuredRequestType docQuery = new gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQuerySecuredRequestType();
        NhinTargetSystemType targetSystem = new NhinTargetSystemType();
        String assigningAuthority = oSubjectId.getAssigningAuthorityIdentifier();
        HomeCommunityType targetCommunity = lookupHomeCommunityId(assigningAuthority);
        String sTargetHomeCommunityId = null;
        if (targetCommunity != null) {
            targetSystem.setHomeCommunity(targetCommunity);
            sTargetHomeCommunityId = targetCommunity.getHomeCommunityId();
        }
        docQuery.setNhinTargetSystem(targetSystem);
        // Replace the patient id in the document query message
        DocumentQueryTransform transform = createDocumentTransform();
        AdhocQueryRequest adhocQueryRequest = transform.replaceAdhocQueryPatientId(oOriginalQueryRequest, sLocalHomeCommunity, oSubjectId.getAssigningAuthorityIdentifier(), oSubjectId.getSubjectIdentifier());
        docQuery.setAdhocQueryRequest(adhocQueryRequest);
        AdhocQueryResponse queryResults = null;
        if (isValidPolicy(adhocQueryRequest, oAssertion, targetCommunity)) {
            try {
                queryResults = nhincDocQueryProxy.respondingGatewayCrossGatewayQuery(docQuery, oAssertion);
            } catch (Throwable t) {
                queryResults = new AdhocQueryResponse();
                RegistryErrorList regErrList = new RegistryErrorList();
                RegistryError regErr = new RegistryError();
                regErrList.getRegistryError().add(regErr);
                regErr.setCodeContext("Fault encountered processing internal document query for community " + sTargetHomeCommunityId);
                regErr.setErrorCode("XDSRegistryNotAvailable");
                regErr.setSeverity("Error");
                queryResults.setRegistryErrorList(regErrList);
                queryResults.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
            }
        } else {
            queryResults = new AdhocQueryResponse();
            RegistryErrorList regErrList = new RegistryErrorList();
            RegistryError regErr = new RegistryError();
            regErrList.getRegistryError().add(regErr);
            regErr.setCodeContext("Policy Check Failed");
            regErr.setErrorCode("XDSRepositoryError");
            regErr.setSeverity("Error");
            queryResults.setRegistryErrorList(regErrList);
            queryResults.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
        }
        registerResponseMessage(queryResults, sTargetHomeCommunityId);
    }

    private void registerResponseMessage(AdhocQueryResponse queryResults, String sTargetHomeCommunityId) {
        SetResponseMsgDocQueryRequestType oSetResponseRequest = new SetResponseMsgDocQueryRequestType();
        oSetResponseRequest.setTransactionId(sTransactionId);
        oSetResponseRequest.setHomeCommunityId(sTargetHomeCommunityId);
        oSetResponseRequest.setQualifiedPatientIdentifier(oSubjectId);
        oSetResponseRequest.setAdhocQueryResponse(queryResults);
        DocQueryAggregator aggregator = createDocQueryAggregator();
        aggregator.setResponseMsg(oSetResponseRequest);
    }

    /**
     * Policy Check verification done here...
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
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq);
        /* if response='permit' */
        if (policyResp.getResponse().getResult().get(0).getDecision().value().equals(NhincConstants.POLICY_PERMIT)) {
            isValid = true;
        }
        return isValid;
    }
}
