package gov.hhs.fha.nhinc.xdr.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.nhincproxyxdrsecured.async.response.ProxyXDRSecuredResponseService;
import gov.hhs.fha.nhinc.nhincproxyxdrsecured.async.response.ProxyXDRSecuredResponsePortType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import ihe.iti.xdr._2007.AcknowledgementType;
import java.util.Map;
import javax.xml.ws.BindingProvider;

/**
 *
 * @author Neil Webb
 */
public class NhincProxyXDRResponseImpl
{
    private static Log log = null;
    private static ProxyXDRSecuredResponseService service;

    public NhincProxyXDRResponseImpl()
    {
        log = createLogger();
        service = createService();
    }

    public AcknowledgementType provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType provideAndRegisterResponseRequest)
    {
        AcknowledgementType response = null;

        String url = getURL();
        if(NullChecker.isNotNullish(url))
        {
            try
            {
                ProxyXDRSecuredResponsePortType port = getPort(url);

                RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType provideAndRegisterResponseRequestSecured = new RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType();
                provideAndRegisterResponseRequestSecured.setRegistryResponse(provideAndRegisterResponseRequest.getRegistryResponse());
                provideAndRegisterResponseRequestSecured.setNhinTargetSystem(provideAndRegisterResponseRequest.getNhinTargetSystem());

                AssertionType assertion = provideAndRegisterResponseRequest.getAssertion();
                setRequestContext(assertion, url, port);

                // TODO: Audit log

                // TODO: Policy check

                response = port.provideAndRegisterDocumentSetBResponse(provideAndRegisterResponseRequestSecured);
            }
            catch (Exception ex)
            {
                log.error("Error in Unsecured NHIN Proxy for XDR Response: " + ex.getMessage(), ex);
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

    protected void setRequestContext(AssertionType assertion, String url, ProxyXDRSecuredResponsePortType port)
    {
        SamlTokenCreator tokenCreator = new SamlTokenCreator();
        Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.NHINC_PROXY_XDR_RESPONSE_SERVICE_NAME);
        ((BindingProvider) port).getRequestContext().putAll(requestContext);
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected ProxyXDRSecuredResponseService createService()
    {
        return ((service != null) ? service : new ProxyXDRSecuredResponseService());
    }

    protected String getURL()
    {
        String url = "";

        try
        {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.NHINC_PROXY_XDR_RESPONSE_SECURED_SERVICE_NAME);
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage(), ex);
        }

        return url;
    }

    protected ProxyXDRSecuredResponsePortType getPort(String url)
    {
        ProxyXDRSecuredResponsePortType port = service.getProxyXDRSecuredResponsePort();

        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);

        return port;
    }
}
