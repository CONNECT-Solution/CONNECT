package gov.hhs.fha.nhinc.xdr.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.nhincproxyxdrsecured.async.request.ProxyXDRSecuredRequestService;
import gov.hhs.fha.nhinc.nhincproxyxdrsecured.async.request.ProxyXDRSecuredRequestPortType;
import ihe.iti.xdr._2007.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import javax.xml.ws.BindingProvider;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;

/**
 *
 * @author Neil Webb
 */
public class NhincProxyXDRRequestImpl
{
    private static Log log = null;
    private static ProxyXDRSecuredRequestService service;

    public NhincProxyXDRRequestImpl()
    {
        log = createLogger();
        service = createService();
    }

    public AcknowledgementType provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetRequestType provideAndRegisterRequestRequest)
    {
        AcknowledgementType response = null;

        String url = getURL();
        if(NullChecker.isNotNullish(url))
        {
            try
            {
                ProxyXDRSecuredRequestPortType port = getPort(url);

                RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType provideAndRegisterRequestSecuredRequest = new RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
                provideAndRegisterRequestSecuredRequest.setProvideAndRegisterDocumentSetRequest(provideAndRegisterRequestRequest.getProvideAndRegisterDocumentSetRequest());
                provideAndRegisterRequestSecuredRequest.setNhinTargetSystem(provideAndRegisterRequestRequest.getNhinTargetSystem());

                AssertionType assertion = provideAndRegisterRequestRequest.getAssertion();
                setRequestContext(assertion, url, port);

                // TODO: Audit log

                // TODO: Policy check

                response = port.provideAndRegisterDocumentSetBRequest(provideAndRegisterRequestSecuredRequest);
            }
            catch (Exception ex)
            {
                log.error("Error in Unsecured NHIN Proxy for XDR Request: " + ex.getMessage(), ex);
                response = new AcknowledgementType();
                response.setMessage("Error");
            }
        }
        else
        {
            log.error("The URL for service: " + NhincConstants.NHINC_PROXY_XDR_REQUEST_SECURED_SERVICE_NAME + " is null");
            response = new AcknowledgementType();
            response.setMessage("Error");
        }

        return response;
    }

    protected void setRequestContext(AssertionType assertion, String url, ProxyXDRSecuredRequestPortType port)
    {
        SamlTokenCreator tokenCreator = new SamlTokenCreator();
        Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.NHINC_PROXY_XDR_REQUEST_SERVICE_NAME);
        ((BindingProvider) port).getRequestContext().putAll(requestContext);
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected ProxyXDRSecuredRequestService createService()
    {
        return ((service != null) ? service : new ProxyXDRSecuredRequestService());
    }

    protected String getURL()
    {
        String url = "";

        try
        {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.NHINC_PROXY_XDR_REQUEST_SECURED_SERVICE_NAME);
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage(), ex);
        }

        return url;
    }

    protected ProxyXDRSecuredRequestPortType getPort(String url)
    {
        ProxyXDRSecuredRequestPortType port = service.getProxyXDRSecuredRequestPort();

        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);

        return port;
    }
}
