/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.proxy;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhincproxyxdrsecured.ProxyXDRSecuredPortType;
import gov.hhs.fha.nhinc.nhincproxyxdrsecured.ProxyXDRSecuredService;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersElements;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersHelper;
import gov.hhs.fha.nhinc.hiem.dte.SoapUtil;
import com.sun.xml.ws.developer.WSBindingProvider;

/**
 *
 * @author dunnek
 */
public class ProxyXDRImpl {
    private static Log log = null;

    private static ProxyXDRSecuredService service = null;
    
    
    public ProxyXDRImpl()
    {
        log = createLogger();
        service = createService();
    }
    public RegistryResponseType provideAndRegisterDocumentSetB(
            RespondingGatewayProvideAndRegisterDocumentSetRequestType body) {
        String url = "";
        ProxyXDRSecuredPortType port;
        RegistryResponseType result = null;
        log.debug("begin provideAndRegisterDocumentSetB()");
               
        AssertionType assertIn = body.getAssertion();

        url = getURL();
        port = getPort(url);

        SamlTokenCreator tokenCreator = new SamlTokenCreator();
        Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.NHINC_PROXY_XDR_SERVICE_NAME);
        ((BindingProvider) port).getRequestContext().putAll(requestContext);

        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType securedRequest = new RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
       
        securedRequest.setNhinTargetSystem(body.getNhinTargetSystem());
        securedRequest.setProvideAndRegisterDocumentSetRequest(body.getProvideAndRegisterDocumentSetRequest());
        
        result = port.provideAndRegisterDocumentSetB(securedRequest);
        
        return result;
    }
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }
    protected ProxyXDRSecuredService createService()
    {
        return ((service != null) ? service : new ProxyXDRSecuredService());

    }
    private String getURL()
    {
        String url = "";

        try
        {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.NHINC_PROXY_XDR_SECURED_SERVICE_NAME);
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage(), ex);
        }

        return url;
    }
    private ProxyXDRSecuredPortType getPort(String url)
    {
        ProxyXDRSecuredPortType port = service.getProxyXDRSecuredPort();

        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);

        return port;
    }
}
