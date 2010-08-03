package gov.hhs.fha.nhinc.docretrievedeferred.nhin.request;

import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveEventType;
import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.docretrievedeferred.DocRetrieveDeferredAuditLogger;
import gov.hhs.fha.nhinc.docretrievedeferred.nhin.proxy.request.NhinDocRetrieveDeferredReqObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import gov.hhs.fha.nhinc.policyengine.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractorHelper;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.ws.WebServiceContext;

/**
 * Created by
 * User: ralph
 * Date: Aug 2, 2010
 * Time: 1:50:15 PM
 */
public class NhinDocRetrieveDeferredReqImpl {

    private static Log log = LogFactory.getLog(NhinDocRetrieveDeferredReqImpl.class);

    DocRetrieveAcknowledgementType sendToRespondingGateway(RespondingGatewayCrossGatewayRetrieveSecuredRequestType body, WebServiceContext context) {
        NhinDocRetrieveDeferredReqObjectFactory  objectFactory;
        DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();
        NhinTargetSystemType        targetSystem;
        RetrieveDocumentSetRequestType nhinRequest;

        nhinRequest = body.getRetrieveDocumentSetRequest();
        targetSystem = body.getNhinTargetSystem();

        DocRetrieveAcknowledgementType response = null;
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        objectFactory = new NhinDocRetrieveDeferredReqObjectFactory();

        auditLog.auditDocRetrieveDeferredRequest(nhinRequest, assertion);

        if(isPolicyValid(nhinRequest, assertion, targetSystem.getHomeCommunity())) {
            response = objectFactory.getDocumentDeferredRequestProxy().sendToRespondingGateway(body, assertion);
        }
        else {
            //
            // Call error interface on recieving adapter here.
            //
        }

        return response;
    }

    private boolean isPolicyValid(RetrieveDocumentSetRequestType oEachNhinRequest, AssertionType oAssertion, HomeCommunityType targetCommunity) {
        boolean isValid = false;
        DocRetrieveEventType checkPolicy = new DocRetrieveEventType();
        DocRetrieveMessageType checkPolicyMessage = new DocRetrieveMessageType();
        checkPolicyMessage.setRetrieveDocumentSetRequest(oEachNhinRequest);
        checkPolicyMessage.setAssertion(oAssertion);
        checkPolicy.setMessage(checkPolicyMessage);
        checkPolicy.setDirection(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION);
        checkPolicy.setInterface(NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        checkPolicy.setReceivingHomeCommunity(targetCommunity);
        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        CheckPolicyRequestType policyReq = policyChecker.checkPolicyDocRetrieve(checkPolicy);
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
