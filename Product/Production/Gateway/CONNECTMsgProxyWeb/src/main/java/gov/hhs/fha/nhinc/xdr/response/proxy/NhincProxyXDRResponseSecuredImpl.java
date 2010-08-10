package gov.hhs.fha.nhinc.xdr.response.proxy;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.xdr.async.response.proxy.NhinXDRResponseObjectFactory;
import gov.hhs.fha.nhinc.xdr.async.response.proxy.NhinXDRResponseProxy;
import javax.xml.ws.WebServiceContext;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.docsubmission.XDRAuditLogger;
import gov.hhs.fha.nhinc.service.WebServiceHelper;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class NhincProxyXDRResponseSecuredImpl
{

    private Log log = null;
    private XDRAuditLogger auditLogger = null;

    public NhincProxyXDRResponseSecuredImpl()
    {
        log = createLogger();
        auditLogger = createAuditLogger();
    }

    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType provideAndRegisterResponseRequest, WebServiceContext context)
    {
        log.debug("Begin provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType, WebServiceContext)");
        XDRAcknowledgementType response = null;
        WebServiceHelper oHelper = createWebServiceHelper();

        try
        {
            if (provideAndRegisterResponseRequest != null)
            {
                response = (XDRAcknowledgementType) oHelper.invokeSecureDeferredResponseWebService(this, this.getClass(), "provideAndRegisterDocumentSetBResponse", provideAndRegisterResponseRequest, context);
            } else
            {
                log.error("Failed to call the web orchestration (" + this.getClass() + ".provideAndRegisterDocumentSetBResponse).  The input parameter is null.");
            }
        } catch (Exception e)
        {
            log.error("Failed to call the web orchestration (" + this.getClass() + ".provideAndRegisterDocumentSetBResponse).  An unexpected exception occurred.  " +
                    "Exception: " + e.getMessage(), e);
        }

        log.debug("Begin provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType, WebServiceContext)");
        return response;
    }

    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType provideAndRegisterResponseRequest, AssertionType assertion)
    {
        log.debug("Begin provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType, AssertionType)");
        XDRAcknowledgementType response = new XDRAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
        response.setMessage(regResp);

        logRequest(provideAndRegisterResponseRequest, assertion);

        NhinXDRResponseProxy proxy = createNhinProxy();

        log.debug("Calling NHIN Proxy");
        response = proxy.provideAndRegisterDocumentSetBResponse(provideAndRegisterResponseRequest.getRegistryResponse(), assertion, provideAndRegisterResponseRequest.getNhinTargetSystem());

        logResponse(response, assertion);

        log.debug("End provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType, AssertionType)");
        return response;
    }

    private void logRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request, AssertionType assertion)
    {
        log.debug("Begin logRequest");
        auditLogger.auditNhinXDRResponseRequest(request, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
        log.debug("End logRequest");
    }

    private void logResponse(XDRAcknowledgementType response, AssertionType assertion)
    {
        log.debug("Begin logResponse");
        auditLogger.auditAcknowledgement(response, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.XDR_RESPONSE_ACTION);
        log.debug("End logResponse");
    }

    protected XDRAuditLogger createAuditLogger()
    {
        return new XDRAuditLogger();
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected NhinXDRResponseProxy createNhinProxy()
    {
        NhinXDRResponseObjectFactory factory = new NhinXDRResponseObjectFactory();
        return factory.getNhinXDRResponseProxy();
    }

    protected WebServiceHelper createWebServiceHelper()
    {
        return new WebServiceHelper();
    }
}
