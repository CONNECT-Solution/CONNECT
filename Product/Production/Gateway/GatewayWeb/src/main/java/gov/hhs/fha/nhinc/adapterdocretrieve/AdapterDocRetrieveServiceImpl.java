package gov.hhs.fha.nhinc.adapterdocretrieve;

import gov.hhs.fha.nhinc.adapterdocretrievesecured.AdapterDocRetrieveSecured;
import gov.hhs.fha.nhinc.adapterdocretrievesecured.AdapterDocRetrieveSecuredPortType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import javax.xml.ws.BindingProvider;

/**
 *
 *
 * @author Neil Webb
 */
public class AdapterDocRetrieveServiceImpl
{
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(AdapterDocRetrieveServiceImpl.class);

    AdapterDocRetrieveSecured service = new AdapterDocRetrieveSecured();

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveRequestType respondingGatewayCrossGatewayRetrieveRequest)
    {
        log.debug("Begin AdapterDocRetrieveServiceImpl.respondingGatewayCrossGatewayRetrieve");
        RetrieveDocumentSetResponseType result = null;
        try
        {
            String url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_DOC_RETRIEVE_SECURED_SERVICE_NAME);
            AdapterDocRetrieveSecuredPortType port = getPort(url);

            AssertionType assertIn = respondingGatewayCrossGatewayRetrieveRequest.getAssertion();
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.DOC_QUERY_ACTION);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);
            
            log.debug("Calling secure adapter doc retrieve.");
            result = port.respondingGatewayCrossGatewayRetrieve(respondingGatewayCrossGatewayRetrieveRequest.getRetrieveDocumentSetRequest());
            System.out.println("Result = "+result);
        }
        catch (Exception ex)
        {
            log.error("Error calling adapter doc retrieve secured service: " + ex.getMessage(), ex);
        }

        log.debug("End AdapterDocRetrieveServiceImpl.respondingGatewayCrossGatewayRetrieve");
        return result;
    }
    
    private AdapterDocRetrieveSecuredPortType getPort(String url) {
        AdapterDocRetrieveSecuredPortType port = service.getAdapterDocRetrieveSecuredPortSoap11();

        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);

        return port;
    }
}
