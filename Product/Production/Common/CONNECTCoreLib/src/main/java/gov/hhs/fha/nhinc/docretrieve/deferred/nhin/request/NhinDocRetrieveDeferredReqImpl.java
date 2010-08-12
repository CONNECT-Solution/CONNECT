package gov.hhs.fha.nhinc.docretrieve.deferred.nhin.request;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterDocumentRetrieveDeferredRequestErrorSecuredType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.docretrieve.DocRetrieveDeferredAuditLogger;
import gov.hhs.fha.nhinc.docretrieve.deferred.adapter.proxy.error.AdapterDocRetrieveDeferredReqErrorObjectFactory;
import gov.hhs.fha.nhinc.docretrieve.deferred.adapter.proxy.error.AdapterDocRetrieveDeferredReqErrorProxy;
import gov.hhs.fha.nhinc.docretrieve.deferred.adapter.proxy.request.AdapterDocRetrieveDeferredReqObjectFactory;
import gov.hhs.fha.nhinc.docretrieve.deferred.adapter.proxy.request.AdapterDocRetrieveDeferredReqProxy;
import gov.hhs.fha.nhinc.docretrieve.deferred.nhin.NhinDocRetrieveDeferred;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractorHelper;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by
 * User: ralph
 * Date: Aug 2, 2010
 * Time: 1:50:15 PM
 */
public class NhinDocRetrieveDeferredReqImpl extends NhinDocRetrieveDeferred {

    private static Log log = LogFactory.getLog(NhinDocRetrieveDeferredReqImpl.class);

    /**
     *
     * @param body
     * @param context
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType sendToRespondingGateway(RetrieveDocumentSetRequestType body, AssertionType assertion) {
        DocRetrieveAcknowledgementType response = null;
        DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();
        auditLog.auditDocRetrieveDeferredRequest(body, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, assertion);

        try {
            String homeCommunityId = SamlTokenExtractorHelper.getHomeCommunityId();
            if (isServiceEnabled()) {
                if (isInPassThroughMode()) {
                    response = sendDocRetrieveDeferredRequestToAgency(body, assertion);
                } else {
                    response = serviceDocRetrieveInternal(body, assertion, homeCommunityId);
                }
                auditLog.auditDocRetrieveDeferredAckResponse(createSuccessResponse().getMessage(),
                                                             assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
            } else {
                String  msg = "Doc retrieve service is not enabled for Home Community Id: " + homeCommunityId;
                log.debug(msg);
                response = createErrorResponse(msg);
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
    private DocRetrieveAcknowledgementType serviceDocRetrieveInternal(RetrieveDocumentSetRequestType request,
                                                                      AssertionType assertion, String homeCommunityId) {
        log.debug("Begin DocRetrieveImpl.serviceDocRetrieveInternal");
        DocRetrieveAcknowledgementType response = null;
        HomeCommunityType hcId = new HomeCommunityType();

        hcId.setHomeCommunityId(homeCommunityId);
        if (isPolicyValidForRequest(request, assertion, hcId)) {
            log.debug("Adapter doc retrieve deferred policy check successful");
            response = sendDocRetrieveDeferredRequestToAgency(request, assertion);
        } else {
//            response = createErrorResponse("Policy Check Failed on NHIN community : "+ homeCommunityId);

            response = sendToAgencyErrorInterface(request, assertion);
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
    private DocRetrieveAcknowledgementType sendDocRetrieveDeferredRequestToAgency(RetrieveDocumentSetRequestType request,
                                                                                  AssertionType assertion) {
        DocRetrieveAcknowledgementType response = null;
        DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();
        AdapterDocRetrieveDeferredReqProxy proxy;
        RespondingGatewayCrossGatewayRetrieveSecuredRequestType body;

        log.debug("Begin DocRetrieveReqImpl.sendDocRetrieveToAgency");
        auditDeferredRetrieveMessage(request, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                            NhincConstants.AUDIT_LOG_ADAPTER_ERROR_INTERFACE, assertion);

        proxy = new AdapterDocRetrieveDeferredReqObjectFactory().getDocumentDeferredRequestProxy();
        body = new RespondingGatewayCrossGatewayRetrieveSecuredRequestType();
        body.setRetrieveDocumentSetRequest(request);

        response = proxy.sendToAdapter(body, assertion);

        auditLog.auditDocRetrieveDeferredAckResponse(response.getMessage(),
                                                     assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);

        log.debug("End DocRetrieveReqImpl.sendDocRetrieveToAgency");
        return response;
    }

    /**
     *
     * @param request
     * @param assertion
     * @return DocRetrieveAcknowledgementType
     */
    private DocRetrieveAcknowledgementType sendToAgencyErrorInterface(RetrieveDocumentSetRequestType request,
                                                                                  AssertionType assertion) {
        DocRetrieveAcknowledgementType response = null;
        DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();
        AdapterDocRetrieveDeferredReqErrorProxy proxy;
        AdapterDocumentRetrieveDeferredRequestErrorSecuredType body;
        String  msg = "Adapter doc retrieve deferred request policy check failed.";

        log.debug("Begin DocRetrieveReqImpl.sendToAgencyErrorInterface");
        auditDeferredRetrieveMessage(request, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                                     NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, assertion);

        proxy = new AdapterDocRetrieveDeferredReqErrorObjectFactory().getDocumentDeferredRequestProxy();
        body = new AdapterDocumentRetrieveDeferredRequestErrorSecuredType();
        body.setRetrieveDocumentSetRequest(request);
        body.setErrorMsg(msg);
        log.error(msg);

        response = proxy.sendToAdapter(body, assertion);

        auditLog.auditDocRetrieveDeferredAckResponse(response.getMessage(), assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);

        log.debug("End DocRetrieveReqImpl.sendToAgencyErrorInterface");
        return response;
    }

    /**
     *
     * @param request
     * @param direction
     * @param connectInterface
     * @param assertion
     */
    protected void auditDeferredRetrieveMessage(RetrieveDocumentSetRequestType request, String direction, String connectInterface, AssertionType assertion) {
        gov.hhs.fha.nhinc.common.auditlog.DocRetrieveMessageType message = new gov.hhs.fha.nhinc.common.auditlog.DocRetrieveMessageType();
        message.setRetrieveDocumentSetRequest(request);
        message.setAssertion(assertion);
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logDocRetrieve(message, direction, connectInterface);
        if (auditLogMsg != null) {
            auditMessage(auditLogMsg, assertion);
        }
    }

}
