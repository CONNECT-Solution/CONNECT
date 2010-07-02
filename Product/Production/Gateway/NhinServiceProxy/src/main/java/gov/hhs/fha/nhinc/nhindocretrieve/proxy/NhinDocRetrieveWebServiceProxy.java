package gov.hhs.fha.nhinc.nhindocretrieve.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RespondingGatewayRetrieveService;
import ihe.iti.xds_b._2007.RespondingGatewayRetrievePortType;
import javax.xml.ws.BindingProvider;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;

/**
 *
 *
 * @author Neil Webb
 */
public class NhinDocRetrieveWebServiceProxy implements NhinDocRetrieveProxy
{
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(NhinDocRetrieveWebServiceProxy.class);
    private static RespondingGatewayRetrieveService service = new RespondingGatewayRetrieveService();

    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RespondingGatewayCrossGatewayRetrieveRequestType request)
    {
        log.debug("Begin NhinDocRetrieveWebServiceProxy.respondingGatewayCrossGatewayRetrieve(...)");
        RetrieveDocumentSetResponseType response = null;

        String url = null;
        if (request.getNhinTargetSystem() != null)
        {
            try
            {
                url = ConnectionManagerCache.getEndpontURLFromNhinTarget(request.getNhinTargetSystem(), NhincConstants.DOC_RETRIEVE_SERVICE_NAME);
            }
            catch (ConnectionManagerException ex)
            {
                log.error("Error: Failed to retrieve url for service: " + NhincConstants.DOC_RETRIEVE_SERVICE_NAME);
                log.error(ex.getMessage());
            }
        }
        else
        {
            log.error("Target system passed into the proxy is null");
        }

        if (NullChecker.isNotNullish(url)) {
            if(log.isDebugEnabled())
            {
                log.info("URL for NHIN Proxy call: " + url);
            }
            log.debug("Setting assertion");
            AssertionType assertIn = request.getAssertion();
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.DOC_RETRIEVE_ACTION);

            log.debug("Creating port");
            RespondingGatewayRetrievePortType port = getPort(url);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            log.debug("Calling NHIN doc retrieve service");
            response = port.respondingGatewayCrossGatewayRetrieve(request.getRetrieveDocumentSetRequest());
        }
        else
        {
            log.error("The URL for service: " + NhincConstants.DOC_RETRIEVE_SERVICE_NAME + " is null");
        }

        log.debug("End NhinDocRetrieveWebServiceProxy.respondingGatewayCrossGatewayRetrieve(...)");
        return response;
    }
    
    private RespondingGatewayRetrievePortType getPort(String url)
    {
        RespondingGatewayRetrievePortType port = service.getRespondingGatewayRetrievePortSoap();

        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);

        return port;
    }
}
