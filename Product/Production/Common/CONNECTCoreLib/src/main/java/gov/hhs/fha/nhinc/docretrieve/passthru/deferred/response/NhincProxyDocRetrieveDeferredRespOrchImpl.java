package gov.hhs.fha.nhinc.docretrieve.passthru.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.fha.nhinc.docretrieve.DocRetrieveDeferredAuditLogger;
import gov.hhs.fha.nhinc.docretrieve.deferred.nhin.proxy.response.NhinDocRetrieveDeferredRespObjectFactory;
import gov.hhs.fha.nhinc.docretrieve.deferred.nhin.proxy.response.NhinDocRetrieveDeferredRespProxy;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sai Valluripalli
 */
public class NhincProxyDocRetrieveDeferredRespOrchImpl {

    private Log log = null;
    private boolean debugEnabled = false;

    /**
     * default constructor
     */
    public NhincProxyDocRetrieveDeferredRespOrchImpl() {
        log = createLogger();
        debugEnabled = log.isDebugEnabled();
    }

    /**
     *
     * @return Log
     */
    protected Log createLogger() {
        return (log != null) ? log : LogFactory.getLog(this.getClass());
    }

    /**
     *
     * @param retrieveDocumentSetResponse
     * @param assertion
     * @param target
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RetrieveDocumentSetResponseType retrieveDocumentSetResponse, AssertionType assertion, NhinTargetSystemType target) {
        if (debugEnabled) {
            log.debug("Begin NhincProxyDocRetrieveDeferredRespOrchImpl.processCrossGatewayRetrieveResponse(...)");
        }
        DocRetrieveAcknowledgementType ack = null;
        // Audit request message
        DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();
        auditLog.auditDocRetrieveDeferredResponse(retrieveDocumentSetResponse,NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, assertion);
        try {
            if (debugEnabled) {
                log.debug("Creating NHIN Proxy Component");
            }
            NhinDocRetrieveDeferredRespObjectFactory objFactory = new NhinDocRetrieveDeferredRespObjectFactory();
            NhinDocRetrieveDeferredRespProxy docRetrieveProxy = objFactory.getDocumentDeferredResponseProxy();
            RespondingGatewayCrossGatewayRetrieveSecuredResponseType req = new RespondingGatewayCrossGatewayRetrieveSecuredResponseType();
            req.setRetrieveDocumentSetResponse(retrieveDocumentSetResponse);
            if (debugEnabled) {
                log.debug("Calling NHIN Proxy Component");
            }
            ack = docRetrieveProxy.sendToRespondingGateway(req, assertion);
        } catch (Exception ex) {
            log.error(ex);
            ack = createErrorAck();
        }
        if (ack != null) {
            // Audit response message
            auditLog.auditDocRetrieveDeferredAckResponse(ack.getMessage(), assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        }
        if (debugEnabled) {
            log.debug("End NhincProxyDocRetrieveDeferredRespOrchImpl.processCrossGatewayRetrieveResponse(...)");
        }
        return ack;
    }

    /**
     *
     * @return DocRetrieveAcknowledgementType
     */
    private DocRetrieveAcknowledgementType createErrorAck() {
        if (debugEnabled) {
            log.debug("Begin NhincProxyDocRetrieveDeferredRespOrchImpl.createErrorAck(...)");
        }
        log.error("Error occured sending doc retrieve deferred to NHIN target:");
        DocRetrieveAcknowledgementType ack = new DocRetrieveAcknowledgementType();
        RegistryResponseType responseType = new RegistryResponseType();
        ack.setMessage(responseType);
        responseType.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
        if (debugEnabled) {
            log.debug("End NhincProxyDocRetrieveDeferredRespOrchImpl.createErrorAck(...)");
        }
        return ack;
    }
}
