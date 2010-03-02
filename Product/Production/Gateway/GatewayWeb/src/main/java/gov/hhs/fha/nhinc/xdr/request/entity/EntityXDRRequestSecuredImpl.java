package gov.hhs.fha.nhinc.xdr.request.entity;

import ihe.iti.xdr._2007.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import javax.xml.ws.WebServiceContext;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.xdr.XDRAuditLogger;
import gov.hhs.fha.nhinc.xdr.request.proxy.NhincProxyXDRRequestSecuredImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class EntityXDRRequestSecuredImpl
{
    private Log log = null;
    private XDRAuditLogger auditLogger = null;

    public EntityXDRRequestSecuredImpl()
    {
        log = createLogger();
        auditLogger = createAuditLogger();
    }

    public AcknowledgementType provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType provideAndRegisterRequestRequest, WebServiceContext context)
    {
        log.info("Begin provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType, WebServiceContext)");
        AcknowledgementType response = null;
        AssertionType assertion = extractAssertion(context);
        response = provideAndRegisterDocumentSetBRequest(provideAndRegisterRequestRequest, assertion);
        log.info("End provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType, WebServiceContext)");
        return response;
    }

    private AcknowledgementType provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType provideAndRegisterRequestRequest, AssertionType assertion)
    {
        log.info("Begin provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType, AssertionType)");
        AcknowledgementType response = null;

        logRequest(provideAndRegisterRequestRequest, assertion);

        if(checkPolicy(provideAndRegisterRequestRequest, assertion))
        {
            log.info("Policy check successful");
            gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType proxyRequest = new gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
            proxyRequest.setProvideAndRegisterDocumentSetRequest(provideAndRegisterRequestRequest.getProvideAndRegisterDocumentSetRequest());
            proxyRequest.setNhinTargetSystem(provideAndRegisterRequestRequest.getNhinTargetSystem());

            NhincProxyXDRRequestSecuredImpl proxy = createNhinProxy();

            log.debug("Sending request from entity service to NHIN proxy service");
            response = proxy.provideAndRegisterDocumentSetBRequest(proxyRequest, assertion);
        }
        else
        {
            log.info("Policy check unsuccessful");
            response = new AcknowledgementType();
            response.setMessage("Policy rejection");
        }

        logResponse(response, assertion);
        
        log.info("End provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType, AssertionType)");
        return response;
    }

    private void logRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, AssertionType assertion)
    {
        log.debug("Begin logRequest");
        auditLogger.auditEntityXDR(request, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
        log.debug("End logRequest");
    }

    private void logResponse(AcknowledgementType response, AssertionType assertion)
    {
        log.debug("Beging logResponse");
        auditLogger.auditAcknowledgement(response, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.XDR_REQUEST_ACTION);
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

    protected NhincProxyXDRRequestSecuredImpl createNhinProxy()
    {
        return new NhincProxyXDRRequestSecuredImpl();
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

    private boolean checkPolicy(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType provideAndRegisterRequestRequest, AssertionType assertion)
    {
        log.debug("Begin checkPolicy");
        // TODO: Policy check
        return true;
    }

}
