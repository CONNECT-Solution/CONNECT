package gov.hhs.fha.nhinc.docretrieve.nhinc.deferred.request;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.docretrieve.DocRetrieveDeferredAuditLogger;
import gov.hhs.fha.nhinc.docretrievedeferred.nhin.proxy.request.NhinDocRetrieveDeferredReqObjectFactory;
import gov.hhs.fha.nhinc.docretrievedeferred.nhin.proxy.request.NhinDocRetrieveDeferredReqProxy;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Proxy Orchestration Request implementation class
 * @author Sai Valluripalli
 */
public class NhincProxyDocRetrieveDeferredReqOrchImpl {

    private Log log = null;
    private boolean debugEnabled = false;

    /**
     * default constructor
     */
    public NhincProxyDocRetrieveDeferredReqOrchImpl() {
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
     * @param crossGatewayRetrieveRequest
     * @param context
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest, WebServiceContext context) {
        if (debugEnabled) {
            log.debug("Begin NhincProxyDocRetrieveDeferredReqOrchImpl.crossGatewayRetrieveRequest(...Unsecured Request...)");
        }
        AssertionType assertion = null;
        NhinTargetSystemType nhinTargetSystem = null;
        RetrieveDocumentSetRequestType retrieveDocumentSetRequest = null;
        DocRetrieveAcknowledgementType ack = null;
        if (null != crossGatewayRetrieveRequest) {
            assertion = crossGatewayRetrieveRequest.getAssertion();
            if (null != assertion) {
                assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
                assertion.getRelatesToList().addAll(AsyncMessageIdExtractor.GetAsyncRelatesTo(context));
            }
            nhinTargetSystem = crossGatewayRetrieveRequest.getNhinTargetSystem();
            retrieveDocumentSetRequest = crossGatewayRetrieveRequest.getRetrieveDocumentSetRequest();
            ack = crossGatewayRetrieveRequest(retrieveDocumentSetRequest, assertion, nhinTargetSystem);
        } else {
            ack = createErrorAckResponse("Inbound Proxy request is null unable to process");
        }
        if (debugEnabled) {
            log.debug("End NhincProxyDocRetrieveDeferredReqOrchImpl.crossGatewayRetrieveRequest(...Unsecured Request...)");
        }
        return ack;
    }

    /**
     * 
     * @param body
     * @param context
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RespondingGatewayCrossGatewayRetrieveSecuredRequestType body, WebServiceContext context) {
        if (debugEnabled) {
            log.debug("Begin NhincProxyDocRetrieveDeferredReqOrchImpl.crossGatewayRetrieveRequest(...Secured Request...)");
        }
        AssertionType assertion = null;
        NhinTargetSystemType nhinTargetSystem = null;
        RetrieveDocumentSetRequestType retrieveDocumentSetRequest = null;
        DocRetrieveAcknowledgementType ack = null;
        if (null != body) {
            assertion = SamlTokenExtractor.GetAssertion(context);
            if (null != assertion) {
                assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
                assertion.getRelatesToList().addAll(AsyncMessageIdExtractor.GetAsyncRelatesTo(context));
            }
            nhinTargetSystem = body.getNhinTargetSystem();
            retrieveDocumentSetRequest = body.getRetrieveDocumentSetRequest();
            ack = crossGatewayRetrieveRequest(retrieveDocumentSetRequest, assertion, nhinTargetSystem);
        } else {
            ack = createErrorAckResponse("Inbound Proxy request is null unable to process");
        }
        if (debugEnabled) {
            log.debug("End NhincProxyDocRetrieveDeferredReqOrchImpl.crossGatewayRetrieveRequest(...Secured Request...)");
        }
        return ack;
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
            log.debug("Begin NhincProxyDocRetrieveDeferredReqOrchImpl.processCrossGatewayRetrieveRequest(...)");
        }
        DocRetrieveAcknowledgementType ack = null;
        // Audit request message
        DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();
        auditLog.auditDocRetrieveDeferredRequest(request, assertion);
        try
        {
            if (debugEnabled) {
                log.debug("Creating NHIN doc retrieve proxy");
            }
            NhinDocRetrieveDeferredReqObjectFactory objFactory = new NhinDocRetrieveDeferredReqObjectFactory();
            NhinDocRetrieveDeferredReqProxy docRetrieveProxy = objFactory.getDocumentDeferredRequestProxy();
            RespondingGatewayCrossGatewayRetrieveSecuredRequestType req = new RespondingGatewayCrossGatewayRetrieveSecuredRequestType();
            req.setNhinTargetSystem(target);
            req.setRetrieveDocumentSetRequest(request);
            if (debugEnabled) {
                log.debug("Calling NHIN doc retrieve proxy");
            }
            ack = docRetrieveProxy.sendToRespondingGateway(req, assertion);
        }
        catch(Exception ex)
        {
            ack = createErrorAckResponse("Error Processing NHIN Proxy document retrieve deferred :"+ex.getMessage());
        }
        if (ack != null) {
            // Audit response message
            auditLog.auditDocRetrieveDeferredAckResponse(ack.getMessage(), assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
        }
        if (debugEnabled) {
            log.debug("End NhincProxyDocRetrieveDeferredReqOrchImpl.processCrossGatewayRetrieveRequest(...)");
        }
        return ack;
    }

    /**
     * 
     * @param errorContext
     * @return DocRetrieveAcknowledgementType
     */
    private DocRetrieveAcknowledgementType createErrorAckResponse(String errorContext) {
        log.error("Error occured sending doc retrieve deferred to NHIN target: ");
        DocRetrieveAcknowledgementType ack = new DocRetrieveAcknowledgementType();
        RegistryResponseType responseType = new RegistryResponseType();
        ack.setMessage(responseType);
        responseType.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
        RegistryErrorList regErrList = new RegistryErrorList();
        responseType.setRegistryErrorList(regErrList);
        RegistryError regErr = new RegistryError();
        regErrList.getRegistryError().add(regErr);
        regErr.setCodeContext(errorContext);
        regErr.setErrorCode("XDSRepositoryError");
        regErr.setSeverity("Error");
        return ack;
    }
}
