package gov.hhs.fha.nhinc.docretrievedeferred.nhinc.proxy.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.fha.nhinc.docretrievedeferred.DocRetrieveDeferredAuditLogger;
import gov.hhs.fha.nhinc.docretrievedeferred.nhin.proxy.response.NhinDocRetrieveDeferredRespObjectFactory;
import gov.hhs.fha.nhinc.docretrievedeferred.nhin.proxy.response.NhinDocRetrieveDeferredRespProxy;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Doc retrieve deferred response Webservice implementation class
 * @author Sai Valluripalli
 */
public class NhincProxyDocRetrieveDeferredRespImpl {
    private Log log = null;
    private boolean debugEnabled = false;

    public NhincProxyDocRetrieveDeferredRespImpl()
    {
        log = createLogger();
    }

    /**
     *
     * @return Log
     */
    protected Log createLogger()
    {
        return (log != null)?log:LogFactory.getLog(this.getClass());
    }

    /**
     * Doc retrive deferred Webservice implementation class
     * @param retrieveDocumentSetResponse
     * @param assertion
     * @param target
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RetrieveDocumentSetResponseType retrieveDocumentSetResponse, AssertionType assertion, NhinTargetSystemType target)
    {
        if (debugEnabled) {
            log.debug("Begin NhincProxyDocRetrieveDeferredRespImpl.crossGatewayRetrieveResponse(...)");
        }
        DocRetrieveAcknowledgementType ack = null;
        // Audit request message
        DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();
        auditLog.auditDocRetrieveDeferredResponse (retrieveDocumentSetResponse, assertion);
        
        try
        {
        if (debugEnabled) {
            log.debug("End NhincProxyDocRetrieveDeferredRespImpl.crossGatewayRetrieveResponse(...)");
        }
            NhinDocRetrieveDeferredRespObjectFactory objFactory = new NhinDocRetrieveDeferredRespObjectFactory();
            NhinDocRetrieveDeferredRespProxy docRetrieveProxy = objFactory.getDocumentDeferredResponseProxy();
            RespondingGatewayCrossGatewayRetrieveSecuredResponseType req = new RespondingGatewayCrossGatewayRetrieveSecuredResponseType();
            req.setRetrieveDocumentSetResponse(retrieveDocumentSetResponse);
            ack = docRetrieveProxy.sendToRespondingGateway(req, assertion);
        }catch(Throwable t)
        {
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
        return ack;
    }
}
