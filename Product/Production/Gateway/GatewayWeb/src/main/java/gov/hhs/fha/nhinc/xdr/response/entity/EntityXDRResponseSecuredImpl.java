package gov.hhs.fha.nhinc.xdr.response.entity;

import ihe.iti.xdr._2007.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType;
import javax.xml.ws.WebServiceContext;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.xdr.XDRAuditLogger;
import gov.hhs.fha.nhinc.xdr.response.proxy.NhincProxyXDRResponseSecuredImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class EntityXDRResponseSecuredImpl
{
    private Log log = null;
    private XDRAuditLogger auditLogger = null;

    public EntityXDRResponseSecuredImpl()
    {
        log = createLogger();
        auditLogger = createAuditLogger();
    }

    public AcknowledgementType provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType provideAndRegisterDocumentSetSecuredResponseRequest, WebServiceContext context)
    {
        log.info("Begin provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType, WebServiceContext)");
        AcknowledgementType response = null;
        AssertionType assertion = extractAssertion(context);
        response = provideAndRegisterDocumentSetBResponse(provideAndRegisterDocumentSetSecuredResponseRequest, assertion);
        log.info("End provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType, WebServiceContext)");
        return response;
    }

    private AcknowledgementType provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType provideAndRegisterDocumentSetSecuredResponseRequest, AssertionType assertion)
    {
        log.info("Begin provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType, AssertionType)");
        AcknowledgementType response = null;

        logRequest(provideAndRegisterDocumentSetSecuredResponseRequest, assertion);

        if(checkPolicy(provideAndRegisterDocumentSetSecuredResponseRequest, assertion))
        {
            log.info("Policy check successful");
            gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType proxyRequest = new gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType();
            proxyRequest.setRegistryResponse(provideAndRegisterDocumentSetSecuredResponseRequest.getRegistryResponse());
            proxyRequest.setNhinTargetSystem(provideAndRegisterDocumentSetSecuredResponseRequest.getNhinTargetSystem());

            NhincProxyXDRResponseSecuredImpl proxy = createNhinProxy();

            log.debug("Sending request from entity service to NHIN proxy service");
            response = proxy.provideAndRegisterDocumentSetBResponse(proxyRequest, assertion);
        }
        else
        {
            log.info("Policy check unsuccessful");
            response = new AcknowledgementType();
            response.setMessage("Policy rejection");
        }

        logResponse(response, assertion);

        log.info("End provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType, AssertionType)");
        return response;
    }

    private void logRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request, AssertionType assertion)
    {
        log.debug("Begin logRequest");
        auditLogger.auditEntityXDRResponseRequest(request, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
        log.debug("End logRequest");
    }

    private void logResponse(AcknowledgementType response, AssertionType assertion)
    {
        log.debug("Beging logResponse");
        auditLogger.auditAcknowledgement(response, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.XDR_RESPONSE_ACTION);
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

    protected NhincProxyXDRResponseSecuredImpl createNhinProxy()
    {
        return new NhincProxyXDRResponseSecuredImpl();
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

    private boolean checkPolicy(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType provideAndRegisterDocumentSetSecuredResponseRequest, AssertionType assertion)
    {
        log.debug("Begin checkPolicy");
        // TODO: Policy check
        return true;
    }
}
