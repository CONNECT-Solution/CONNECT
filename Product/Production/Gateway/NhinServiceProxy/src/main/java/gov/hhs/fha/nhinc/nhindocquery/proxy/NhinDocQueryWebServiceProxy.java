package gov.hhs.fha.nhinc.nhindocquery.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.service.ServiceUtil;
import ihe.iti.xds_b._2007.RespondingGatewayQueryPortType;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class NhinDocQueryWebServiceProxy implements NhinDocQueryProxy {
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:ihe:iti:xds-b:2007";
    private static final String SERVICE_LOCAL_PART = "RespondingGateway_Query_Service";
    private static final String PORT_LOCAL_PART = "RespondingGateway_Query_Port_Soap";
    private static final String WSDL_FILE = "NhinDocQuery.wsdl";
    private Log log = null;

    public NhinDocQueryWebServiceProxy()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQueryRequestType request) {
        String url = null;
        AdhocQueryResponse response = new AdhocQueryResponse();

        log.info("Before target system called");

        if (request.getNhinTargetSystem() != null) {
            try {
                log.info("Target Sys properties Home Comm ID:"+request.getNhinTargetSystem().getHomeCommunity().getHomeCommunityId());
                log.info("Target Sys properties Home Comm Description"+request.getNhinTargetSystem().getHomeCommunity().getDescription());
                log.info("Target Sys properties Home Comm Name"+request.getNhinTargetSystem().getHomeCommunity().getName());
                url = ConnectionManagerCache.getEndpontURLFromNhinTarget(request.getNhinTargetSystem(), NhincConstants.DOC_QUERY_SERVICE_NAME);
                log.info("SAI URL is:"+url);
            } catch (ConnectionManagerException ex) {
                log.error("Error: Failed to retrieve url for service: " + NhincConstants.DOC_QUERY_SERVICE_NAME);
                log.error(ex.getMessage());
            }
        } else {
            log.error("Target system passed into the proxy is null");
        }
        log.info("After target system called"+url);
        if (NullChecker.isNotNullish(url)) {
            if(log.isDebugEnabled())
            {
                log.debug("URL for NHIN Proxy call: " + url);
            }
            RespondingGatewayQueryPortType port = getPort(url);

            AssertionType assertIn = request.getAssertion();
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.DOC_QUERY_ACTION);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);
            
            response = port.respondingGatewayCrossGatewayQuery(request.getAdhocQueryRequest());
        } else {
            log.error("The URL for service: " + NhincConstants.DOC_QUERY_SERVICE_NAME + " is null");
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

    protected RespondingGatewayQueryPortType getPort(String url) {

        RespondingGatewayQueryPortType port = null;
        Service service = getService();
        if(service != null)
        {
            log.debug("Obtained service - creating port.");
            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), RespondingGatewayQueryPortType.class);
            setEndpointAddress(port, url);
        }
        else
        {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

    protected void setEndpointAddress(RespondingGatewayQueryPortType port, String url)
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
            log.info("Setting endpoint address to Nhin Document Query Service to " + url);
            ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        }
    }

}
