package gov.hhs.fha.nhinc.docretrievedeferred.nhinc.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.docretrievedeferred.DocRetrieveDeferredAuditLogger;
import gov.hhs.fha.nhinc.docretrievedeferred.nhin.proxy.request.NhinDocRetrieveDeferredReqObjectFactory;
import gov.hhs.fha.nhinc.docretrievedeferred.nhin.proxy.request.NhinDocRetrieveDeferredReqProxy;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Nhinc proxy Request implementation class
 * @author Sai Valluripalli
 */
public class NhincProxyDocRetrieveDeferredReqImpl {

    private Log log = null;
    private boolean debugEnabled = false;

    /**
     * default constructor
     */
    public NhincProxyDocRetrieveDeferredReqImpl() {
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
     * @param request
     * @param assertion
     * @param target
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RetrieveDocumentSetRequestType request, AssertionType assertion, NhinTargetSystemType target) {
        if (debugEnabled) {
            log.debug("Begin NhincProxyDocRetrieveDeferredReqImpl.crossGatewayRetrieveRequest(...)");
        }

        DocRetrieveAcknowledgementType ack = null;
        // Audit request message
        DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();
        auditLog.auditDocRetrieveDeferredRequest(request, assertion);
        try {
            if (debugEnabled) {
                log.debug("Creating NHIN doc retrieve proxy");
            }
            NhinDocRetrieveDeferredReqObjectFactory objFactory = new NhinDocRetrieveDeferredReqObjectFactory();
            NhinDocRetrieveDeferredReqProxy docRetrieveProxy = objFactory.getDocumentDeferredRequestProxy();
            RespondingGatewayCrossGatewayRetrieveSecuredRequestType req = new RespondingGatewayCrossGatewayRetrieveSecuredRequestType();
            req.setRetrieveDocumentSetRequest(request);
            if (debugEnabled) {
                log.debug("Calling doc retrieve proxy");
            }
            ack = docRetrieveProxy.sendToRespondingGateway(req, assertion);
        } catch (Throwable t) {
            log.error("Error occured sending doc retrieve deferred to NHIN target: " + t.getMessage(), t);
            ack = new DocRetrieveAcknowledgementType();
            RegistryResponseType responseType = new RegistryResponseType();
            ack.setMessage(responseType);
            responseType.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
            RegistryErrorList regErrList = new RegistryErrorList();
            responseType.setRegistryErrorList(regErrList);
            RegistryError regErr = new RegistryError();
            regErrList.getRegistryError().add(regErr);
            regErr.setCodeContext("Processing NHIN Proxy document retrieve deferred");
            regErr.setErrorCode("XDSRepositoryError");
            regErr.setSeverity("Error");
        }

        if (ack != null) {
            // Audit response message
            auditLog.auditDocRetrieveDeferredAckResponse(ack.getMessage(), assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
        }

        if (debugEnabled) {
            log.debug("End NhincProxyDocRetrieveDeferredReqImpl.crossGatewayRetrieveRequest(...)");
        }
        return ack;
    }
}
