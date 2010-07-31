package gov.hhs.fha.nhinc.nhindocquery.proxy;

import gov.hhs.fha.nhinc.async.AsyncMessageIdCreator;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xds_b._2007.RespondingGatewayQueryPortType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is the component proxy for calling the NHIN doc query web service.
 *
 * @author jhoppesc, Les Westberg
 */
public class NhinDocQueryWebServiceProxy implements NhinDocQueryProxy
{

    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:ihe:iti:xds-b:2007";
    private static final String SERVICE_LOCAL_PART = "RespondingGateway_Query_Service";
    private static final String PORT_LOCAL_PART = "RespondingGateway_Query_Port_Soap";
    private static final String WSDL_FILE = "NhinDocQuery.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:ihe:iti:2007:CrossGatewayQuery";
    private Log log = null;
    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    /**
     * Default constructor.
     */
    public NhinDocQueryWebServiceProxy()
    {
        log = createLogger();
    }

    /**
     * Creates the log object for logging.
     *
     * @return The log object.
     */
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    /**
     * Calls the respondingGatewayCrossGatewayQuery method of the web service.
     *
     * @param request The information for the web service.
     * @return The response from the web service.
     */
    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQueryRequestType request)
    {
        String url = null;
        AdhocQueryResponse response = new AdhocQueryResponse();

        try
        {
            if (request != null)
            {
                log.debug("Before target system URL look up.");
                url = oProxyHelper.getUrlFromTargetSystem(request.getNhinTargetSystem(), NhincConstants.DOC_QUERY_SERVICE_NAME);
                log.debug("After target system URL look up. URL for service: " + NhincConstants.DOC_QUERY_SERVICE_NAME + " is: " + url);

                if (NullChecker.isNotNullish(url))
                {
                    AssertionType assertIn = request.getAssertion();
                    RespondingGatewayQueryPortType port = getPort(url, NhincConstants.DOC_QUERY_ACTION, assertIn);
                    response = (AdhocQueryResponse) oProxyHelper.invokePort(port, RespondingGatewayQueryPortType.class, "respondingGatewayCrossGatewayQuery", request.getAdhocQueryRequest());
                }
                else
                {
                    log.error("Failed to call the web service (" + NhincConstants.DOC_QUERY_SERVICE_NAME + ").  The URL is null.");
                }
            }
            else
            {
                log.error("Failed to call the web service (" + NhincConstants.DOC_QUERY_SERVICE_NAME + ").  The input parameter is null.");
            }
        }
        catch (Exception e)
        {
            log.error("Failed to call the web service (" + NhincConstants.DOC_QUERY_SERVICE_NAME + ").  An unexpected exception occurred.  " +
                      "Exception: " + e.getMessage(), e);
        }

        return response;
    }

    /**
     * Retrieve the service class for this web service.
     *
     * @return The service class for this web service.
     */
    protected Service getService()
    {
        if (cachedService == null)
        {
            try
            {
                cachedService = oProxyHelper.createService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
            }
            catch (Throwable t)
            {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }

    /**
     * This method retrieves and initializes the port.
     *
     * @param url The URL for the web service.
     * @param serviceAction The action for the web service.
     * @param assertion The assertion information for the web service
     * @return The port object for the web service.
     */
    protected RespondingGatewayQueryPortType getPort(String url, String serviceAction, AssertionType assertion)
    {
        RespondingGatewayQueryPortType port = null;
        Service service = getService();
        if (service != null)
        {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), RespondingGatewayQueryPortType.class);
            oProxyHelper.initializePort((javax.xml.ws.BindingProvider) port, url, serviceAction, WS_ADDRESSING_ACTION, assertion);
        }
        else
        {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }
}
