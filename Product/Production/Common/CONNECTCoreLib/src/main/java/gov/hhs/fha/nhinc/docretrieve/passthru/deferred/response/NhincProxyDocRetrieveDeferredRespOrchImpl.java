package gov.hhs.fha.nhinc.docretrieve.passthru.deferred.response;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.fha.nhinc.docretrieve.DocRetrieveDeferredAuditLogger;
import gov.hhs.fha.nhinc.docretrieve.deferred.nhin.proxy.response.NhinDocRetrieveDeferredRespObjectFactory;
import gov.hhs.fha.nhinc.docretrieve.deferred.nhin.proxy.response.NhinDocRetrieveDeferredRespProxy;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.ws.WebServiceContext;

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
     * @param crossGatewayRetrieveResponse
     * @param context
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RespondingGatewayCrossGatewayRetrieveResponseType crossGatewayRetrieveResponse, WebServiceContext context) {
        if (debugEnabled) {
            log.debug("Begin NhincProxyDocRetrieveDeferredRespOrchImpl.crossGatewayRetrieveResponse(...unsecured response...)");
        }
        RetrieveDocumentSetResponseType retrieveDocumentSetResponse = null;
        AssertionType assertion = null;
        NhinTargetSystemType target = null;
        DocRetrieveAcknowledgementType ack = null;
        if (null != crossGatewayRetrieveResponse) {
            retrieveDocumentSetResponse = crossGatewayRetrieveResponse.getRetrieveDocumentSetResponse();
            assertion = crossGatewayRetrieveResponse.getAssertion();
            if (null != assertion) {
                assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
                assertion.getRelatesToList().addAll(AsyncMessageIdExtractor.GetAsyncRelatesTo(context));
            }
            target = crossGatewayRetrieveResponse.getNhinTargetSystem();
            ack = crossGatewayRetrieveResponse(retrieveDocumentSetResponse, assertion, target);
        } else {
            ack = createErrorAck("Proxy Inbound Response message is null");
        }
        if (debugEnabled) {
            log.debug("End NhincProxyDocRetrieveDeferredRespOrchImpl.crossGatewayRetrieveResponse(...unsecured response...)");
        }
        return ack;
    }

    /**
     *
     * @param body
     * @param context
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RespondingGatewayCrossGatewayRetrieveSecuredResponseType body, WebServiceContext context) {
        if (debugEnabled) {
            log.debug("Begin NhincProxyDocRetrieveDeferredRespOrchImpl.crossGatewayRetrieveResponse(...secured response...)");
        }
        RetrieveDocumentSetResponseType retrieveDocumentSetResponse = null;
        AssertionType assertion = null;
        NhinTargetSystemType target;
        DocRetrieveAcknowledgementType ack = null;
        if (null != body) {
            retrieveDocumentSetResponse = body.getRetrieveDocumentSetResponse();
            assertion = SamlTokenExtractor.GetAssertion(context);
            if (null != assertion) {
                assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
                assertion.getRelatesToList().addAll(AsyncMessageIdExtractor.GetAsyncRelatesTo(context));
            }
            target = body.getNhinTargetSystem();
            ack = crossGatewayRetrieveResponse(retrieveDocumentSetResponse, assertion, target);

        } else {
            ack = createErrorAck("Proxy Inbound Response message is null");
        }
        if (debugEnabled) {
            log.debug("End NhincProxyDocRetrieveDeferredRespOrchImpl.crossGatewayRetrieveResponse(...secured response...)");
        }
        return ack;
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
        auditLog.auditDocRetrieveDeferredResponse(retrieveDocumentSetResponse, assertion);
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
        }
        catch (Exception ex)
        {
            ack = createErrorAck("Unable to process Proxy response message :" + ex.getMessage());
        }
        if (ack != null) {
            // Audit response message
            auditLog.auditDocRetrieveDeferredAckResponse(ack.getMessage(), assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
        }
        if (debugEnabled) {
            log.debug("End NhincProxyDocRetrieveDeferredRespOrchImpl.processCrossGatewayRetrieveResponse(...)");
        }
        return ack;
    }

    /**
     *
     * @param errorContext
     * @return DocRetrieveAcknowledgementType
     */
    private DocRetrieveAcknowledgementType createErrorAck(String errorContext) {
        if (debugEnabled) {
            log.debug("Begin NhincProxyDocRetrieveDeferredRespOrchImpl.createErrorAck(...)");
        }
        log.error("Error occured sending doc retrieve deferred to NHIN target:");
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
        if (debugEnabled) {
            log.debug("End NhincProxyDocRetrieveDeferredRespOrchImpl.createErrorAck(...)");
        }
        return ack;
    }
}
