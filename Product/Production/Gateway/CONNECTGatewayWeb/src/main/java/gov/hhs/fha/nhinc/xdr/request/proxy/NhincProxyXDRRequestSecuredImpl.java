package gov.hhs.fha.nhinc.xdr.request.proxy;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.xdr.async.request.proxy.NhinXDRRequestObjectFactory;
import gov.hhs.fha.nhinc.xdr.async.request.proxy.NhinXDRRequestProxy;
import javax.xml.ws.WebServiceContext;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.xdr.XDRAuditLogger;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class NhincProxyXDRRequestSecuredImpl
{
    private Log log = null;
    private XDRAuditLogger auditLogger = null;

    public NhincProxyXDRRequestSecuredImpl()
    {
        log = createLogger();
        auditLogger = createAuditLogger();
    }

    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType provideAndRegisterRequestRequest, WebServiceContext context)
    {
        log.debug("Begin provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType, WebServiceContext)");
        AssertionType assertion = extractAssertion(context);

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            assertion.setAsyncMessageId(extractMessageId(context));
        }

        log.debug("End provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType, WebServiceContext)");
        return provideAndRegisterDocumentSetBRequest(provideAndRegisterRequestRequest, assertion);
    }

    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType provideAndRegisterRequestRequest, AssertionType assertion)
    {
        log.debug("Begin provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType, AssertionType)");

        logRequest(provideAndRegisterRequestRequest, assertion);

        NhinXDRRequestProxy proxy = createNhinProxy();

        log.debug("Calling NHIN proxy");
        XDRAcknowledgementType response = proxy.provideAndRegisterDocumentSetBRequest(provideAndRegisterRequestRequest.getProvideAndRegisterDocumentSetRequest(), assertion, provideAndRegisterRequestRequest.getNhinTargetSystem());

        logResponse(response, assertion);

        log.debug("End provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType, AssertionType)");
        return response;
    }

    private void logRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, AssertionType assertion)
    {
        log.debug("Begin logRequest");
        auditLogger.auditXDR(request, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
        log.debug("End logRequest");
    }

    private void logResponse(XDRAcknowledgementType response, AssertionType assertion)
    {
        log.debug("Begin logResponse");
        auditLogger.auditAcknowledgement(response, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.XDR_REQUEST_ACTION);
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

    protected AssertionType extractAssertion(WebServiceContext context)
    {
        log.debug("Begin extractAssertion");
        AssertionType assertion = null;
        if(context != null)
        {
            assertion = SamlTokenExtractor.GetAssertion(context);
        }
        else
        {
            log.warn("Attempted to extract assertion from null web service context.");
        }
        log.debug("End extractAssertion");
        return assertion;
    }

    protected NhinXDRRequestProxy createNhinProxy()
    {
        NhinXDRRequestObjectFactory factory = new NhinXDRRequestObjectFactory();
        return factory.getNhinXDRRequestProxy();
    }

    protected String extractMessageId (WebServiceContext context) {
        AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
        return msgIdExtractor.GetAsyncMessageId(context);
    }

}