package gov.hhs.fha.nhinc.docretrieve.deferred.nhin.response;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.common.auditlog.DocRetrieveResponseMessageType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.fha.nhinc.docretrieve.DocRetrieveDeferredAuditLogger;
import gov.hhs.fha.nhinc.docretrieve.deferred.adapter.proxy.response.AdapterDocRetrieveDeferredRespObjectFactory;
import gov.hhs.fha.nhinc.docretrieve.deferred.adapter.proxy.response.AdapterDocRetrieveDeferredRespProxy;
import gov.hhs.fha.nhinc.docretrieve.deferred.nhin.NhinDocRetrieveDeferred;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractorHelper;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by
 * User: ralph
 * Date: Aug 2, 2010
 * Time: 1:50:15 PM
 */
public class NhinDocRetrieveDeferredRespImpl extends NhinDocRetrieveDeferred {

    private static Log log = LogFactory.getLog(NhinDocRetrieveDeferredRespImpl.class);

    DocRetrieveAcknowledgementType sendToRespondingGateway(RetrieveDocumentSetResponseType body, AssertionType assertion) {
        DocRetrieveAcknowledgementType response = null;
        DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();
        auditLog.auditDocRetrieveDeferredResponse(body,NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, assertion);

        try {
            String homeCommunityId = SamlTokenExtractorHelper.getHomeCommunityId();
            if (isServiceEnabled()) {
                if (isInPassThroughMode()) {
                    response = sendDocRetrieveDeferredResponseToAgency(body, assertion);
                } else {
                    response = serviceDocRetrieveInternal(body, assertion, homeCommunityId);
                }
                auditLog.auditDocRetrieveDeferredAckResponse(createSuccessResponse().getMessage(),
                                                             assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
            } else {
                String  msg = "Doc retrieve service is not enabled for Home Community Id: " + homeCommunityId;
                log.debug(msg);
                auditLog.auditDocRetrieveDeferredAckResponse(createErrorResponse(msg).getMessage(), assertion,
                                                             NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
                response = createSuccessResponse();
            }
        } catch (Throwable t) {
            log.error("Error processing NHIN Doc Retrieve: " + t.getMessage(), t);
            //
            // We may be required to return a Success message when things fail, but we can at least audit it properly.
            //
//            response = createErrorResponse("Error encountered processing document retrieve message:  " + t.getMessage());
            auditLog.auditDocRetrieveDeferredAckResponse(createErrorResponse("Error encountered processing document retrieve message:  " +
                    t.getMessage()).getMessage(), assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
            response = createSuccessResponse();
        }

        return response;
    }
    /**
     *
     * @param request
     * @param assertion
     * @param homeCommunityId
     * @return
     */
    private DocRetrieveAcknowledgementType serviceDocRetrieveInternal(RetrieveDocumentSetResponseType request,
                                                                      AssertionType assertion, String homeCommunityId) {
        log.debug("Begin DocRetrieveImpl.serviceDocRetrieveInternal");
        DocRetrieveAcknowledgementType response = null;
        HomeCommunityType hcId = new HomeCommunityType();
        DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();
        auditLog.auditDocRetrieveDeferredResponse(request, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, assertion);
        String  msg = "Adapter doc retrieve deferred response policy check failed.";

        hcId.setHomeCommunityId(homeCommunityId);
        if (isPolicyValidForResponse(request, assertion, hcId)) {
            log.debug("Adapter doc retrieve deferred policy check successful");
            response = sendDocRetrieveDeferredResponseToAgency(request, assertion);
        } else {
//            response = createErrorResponse("Policy Check Failed on NHIN community : "+ homeCommunityId);

            msg += msg + "Request Id = " + request.getRegistryResponse().getRequestId() +
                    ".  Home Community ID = " + hcId + ".";
            log.error(msg);
            auditLog.auditDocRetrieveDeferredAckResponse(createErrorResponse(msg).getMessage(), assertion,
                                                         NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
            response = createSuccessResponse();
        }

        log.debug("End DocRetrieveImpl.serviceDocRetrieveInternal");

        return response;
    }

    /**
     *
     * @param request
     * @param assertion
     * @return DocRetrieveAcknowledgementType
     */
    private DocRetrieveAcknowledgementType sendDocRetrieveDeferredResponseToAgency(RetrieveDocumentSetResponseType request,
                                                                                  AssertionType assertion) {
        DocRetrieveAcknowledgementType response = null;
        DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();
        AdapterDocRetrieveDeferredRespProxy proxy;
        RespondingGatewayCrossGatewayRetrieveSecuredResponseType body;

        log.debug("Begin DocRetrieveReqImpl.sendDocRetrieveToAgency");
        auditDeferredRetrieveMessage(request, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                            NhincConstants.AUDIT_LOG_ADAPTER_ERROR_INTERFACE, assertion);

        proxy = new AdapterDocRetrieveDeferredRespObjectFactory().getDocumentDeferredResponseProxy();
        body = new RespondingGatewayCrossGatewayRetrieveSecuredResponseType();
        body.setRetrieveDocumentSetResponse(request);

        response = proxy.sendToAdapter(body, assertion);

        auditLog.auditDocRetrieveDeferredAckResponse(response.getMessage(),
                                                     assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);

        log.debug("End DocRetrieveReqImpl.sendDocRetrieveToAgency");
        return response;
    }

    /**
     *
     * @param request
     * @param direction
     * @param connectInterface
     * @param assertion
     */
    protected void auditDeferredRetrieveMessage(RetrieveDocumentSetResponseType request, String direction, String connectInterface, AssertionType assertion) {
        DocRetrieveResponseMessageType message = new DocRetrieveResponseMessageType();
        message.setRetrieveDocumentSetResponse(request);
        message.setAssertion(assertion);
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logDocRetrieveResult(message, direction, connectInterface);
        if (auditLogMsg != null) {
            auditMessage(auditLogMsg, assertion);
        }
    }

}
