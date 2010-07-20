package gov.hhs.fha.nhinc.docquery.proxy;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhincproxydocquerysecured.NhincProxyDocQuerySecuredPortType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.service.ServiceUtil;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 *
 * @author Neil Webb
 */
public class NhincProxyDocQueryImpl
{
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:nhincproxydocquerysecured";
    private static final String SERVICE_LOCAL_PART = "NhincProxyDocQuerySecured";
    private static final String PORT_LOCAL_PART = "NhincProxyDocQuerySecuredPortSoap";
    private static final String WSDL_FILE = "NhincProxyDocQuerySecured.wsdl";
    private Log log = null;

    public NhincProxyDocQueryImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQueryRequestType respondingGatewayCrossGatewayQueryRequest)
    {
        AdhocQueryResponse response = null;

        try
        { 
            String url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.NHINC_PROXY_DOC_QUERY_SECURED_SERVICE_NAME);

            NhincProxyDocQuerySecuredPortType port = getPort(url);

            AssertionType assertIn = respondingGatewayCrossGatewayQueryRequest.getAssertion();
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.DOC_QUERY_ACTION);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);
            
            gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQuerySecuredRequestType body = new gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQuerySecuredRequestType();
            body.setAdhocQueryRequest(respondingGatewayCrossGatewayQueryRequest.getAdhocQueryRequest());
            body.setNhinTargetSystem(respondingGatewayCrossGatewayQueryRequest.getNhinTargetSystem());

            response = port.respondingGatewayCrossGatewayQuery(body);
        }
        catch (Exception ex)
        {
            log.error("Failed to send entity doc query from proxy EJB to secure interface: " + ex.getMessage(), ex);
            response = new AdhocQueryResponse();
            // TODO: Populate registry error list?
        }

        return response;
    }

    protected Service getService()
    {
        if(cachedService == null)
        {
            try
            {
                cachedService = new ServiceUtil().createService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
            }
            catch(Throwable t)
            {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }

    protected NhincProxyDocQuerySecuredPortType getPort(String url) {
        
        NhincProxyDocQuerySecuredPortType port = null;
        Service service = getService();
        if(service != null)
        {
            log.debug("Obtained service - creating port.");
            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), NhincProxyDocQuerySecuredPortType.class);
            setEndpointAddress(port, url);
        }
        else
        {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

    protected void setEndpointAddress(NhincProxyDocQuerySecuredPortType port, String url)
    {
        if(port == null)
        {
            log.error("Port was null - not setting endpoint address.");
        }
        else if((url == null) || (url.length() < 1))
        {
            log.error("URL was null or empty - not setting endpoint address.");
        }
        else
        {
            log.info("Setting endpoint address to NHIN Proxy Document Query Secured Service to " + url);
            ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        }
    }

}
