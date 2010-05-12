package gov.hhs.fha.nhinc.xdr.request.entity;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import javax.xml.ws.WebServiceContext;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.transform.policy.SubjectHelper;
import gov.hhs.fha.nhinc.xdr.XDRPolicyChecker;
import gov.hhs.fha.nhinc.xdr.XDRAuditLogger;
import gov.hhs.fha.nhinc.xdr.request.proxy.NhincProxyXDRRequestSecuredImpl;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class EntityXDRRequestSecuredImpl {

    private Log log = null;
    private XDRAuditLogger auditLogger = null;

    public EntityXDRRequestSecuredImpl() {
        log = createLogger();
        auditLogger = createAuditLogger();
    }

    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType provideAndRegisterRequestRequest, WebServiceContext context) {
        log.info("Begin provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType, WebServiceContext)");
        XDRAcknowledgementType response = null;
        AssertionType assertion = extractAssertion(context);

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            assertion.setAsyncMessageId(extractMessageId(context));
        }

        response = provideAndRegisterDocumentSetBRequest(provideAndRegisterRequestRequest, assertion);
        log.info("End provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType, WebServiceContext)");
        return response;
    }

    private XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType provideAndRegisterRequestRequest, AssertionType assertion) {
        log.info("Begin provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType, AssertionType)");
        XDRAcknowledgementType response = new XDRAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
        response.setMessage(regResp);

        logRequest(provideAndRegisterRequestRequest, assertion);

        if (provideAndRegisterRequestRequest != null &&
                provideAndRegisterRequestRequest.getNhinTargetCommunities() != null &&
                NullChecker.isNotNullish(provideAndRegisterRequestRequest.getNhinTargetCommunities().getNhinTargetCommunity()) &&
                provideAndRegisterRequestRequest.getNhinTargetCommunities().getNhinTargetCommunity().get(0) != null &&
                provideAndRegisterRequestRequest.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity() != null &&
                NullChecker.isNotNullish(provideAndRegisterRequestRequest.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity().getHomeCommunityId())) {

            if (checkPolicy(provideAndRegisterRequestRequest, assertion)) {
                log.info("Policy check successful");

                NhinTargetSystemType targetSystemType = new NhinTargetSystemType();
                targetSystemType.setHomeCommunity(provideAndRegisterRequestRequest.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity());

                gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType proxyRequest = new gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
                proxyRequest.setProvideAndRegisterDocumentSetRequest(provideAndRegisterRequestRequest.getProvideAndRegisterDocumentSetRequest());
                proxyRequest.setNhinTargetSystem(targetSystemType);

                NhincProxyXDRRequestSecuredImpl proxy = createNhinProxy();

                log.debug("Sending request from entity service to NHIN proxy service");
                response = proxy.provideAndRegisterDocumentSetBRequest(proxyRequest, assertion);
            } else {
                log.error("Policy check unsuccessful");
            }
        }

        logResponse(response, assertion);

        log.info("End provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType, AssertionType)");
        return response;
    }

    private void logRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, AssertionType assertion) {
        log.debug("Begin logRequest");
        auditLogger.auditEntityXDR(request, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
        log.debug("End logRequest");
    }

    private void logResponse(XDRAcknowledgementType response, AssertionType assertion) {
        log.debug("Beging logResponse");
        auditLogger.auditEntityAcknowledgement(response, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.XDR_REQUEST_ACTION);
        log.debug("End logResponse");
    }

    protected XDRAuditLogger createAuditLogger() {
        return new XDRAuditLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected NhincProxyXDRRequestSecuredImpl createNhinProxy() {
        return new NhincProxyXDRRequestSecuredImpl();
    }

    protected AssertionType extractAssertion(WebServiceContext context) {
        log.debug("Begin extractAssertion");
        AssertionType assertion = null;
        if (context != null) {
            assertion = SamlTokenExtractor.GetAssertion(context);
        } else {
            log.warn("Attempted to extract assertion from null web service context.");
        }
        log.debug("End extractAssertion");
        return assertion;
    }

    protected boolean checkPolicy(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, AssertionType assertion) {
        log.debug("Begin checkPolicy");
        boolean bPolicyOk = false;

        if (request != null &&
                request.getNhinTargetCommunities() != null &&
                NullChecker.isNotNullish(request.getNhinTargetCommunities().getNhinTargetCommunity()) &&
                request.getNhinTargetCommunities().getNhinTargetCommunity().get(0) != null &&
                request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity() != null &&
                NullChecker.isNotNullish(request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity().getHomeCommunityId())) {

            SubjectHelper subjHelp = new SubjectHelper();
            String senderHCID = subjHelp.determineSendingHomeCommunityId(assertion.getHomeCommunity(), assertion);
            String receiverHCID = request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity().getHomeCommunityId();
            String direction = NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION;
            log.debug("Checking the policy engine for the " + direction + " request from " + senderHCID + " to " + receiverHCID);

            //return true if 'permit' returned, false otherwise
            XDRPolicyChecker policyChecker = new XDRPolicyChecker();
            bPolicyOk = policyChecker.checkXDRRequestPolicy(request.getProvideAndRegisterDocumentSetRequest(), assertion, senderHCID, receiverHCID, direction);
        } else {
            log.warn("EntityXDRRequestSecuredImpl check on policy requires a non null receiving home community ID specified in the RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType");
        }
        log.debug("EntityXDRRequestSecuredImpl check on policy returns: " + bPolicyOk);
        return bPolicyOk;
    }

    protected String extractMessageId (WebServiceContext context) {
        AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
        return msgIdExtractor.GetAsyncMessageId(context);
    }
}
