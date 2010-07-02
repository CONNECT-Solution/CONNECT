package gov.hhs.fha.nhinc.docretrieve.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import gov.hhs.fha.nhinc.nhincproxydocretrievesecured.NhincProxyDocRetrieveSecured;
import gov.hhs.fha.nhinc.nhincproxydocretrievesecured.NhincProxyDocRetrieveSecuredPortType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

/**
 *
 *
 * @author Neil Webb
 */
public class NhincProxyDocRetrieveImpl
{
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(NhincProxyDocRetrieveImpl.class);
    private static NhincProxyDocRetrieveSecured service = new NhincProxyDocRetrieveSecured();

    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveRequestType respondingGatewayCrossGatewayRetrieveRequest)
    {
        RetrieveDocumentSetResponseType result = null;
        log.debug("Begin NhincProxyDocRetrieveImpl.respondingGatewayCrossGatewayRetrieve");

        try
        {
            // Get endpoint
            String url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.NHINC_PROXY_DOC_RETRIEVE_SECURED_SERVICE_NAME);
            NhincProxyDocRetrieveSecuredPortType port = getPort(url);

            // Set Assertion
            AssertionType assertIn = respondingGatewayCrossGatewayRetrieveRequest.getAssertion();
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.DOC_QUERY_ACTION);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);
            
            // Create body
            gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType body = new gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType();
            body.setRetrieveDocumentSetRequest(respondingGatewayCrossGatewayRetrieveRequest.getRetrieveDocumentSetRequest());
            body.setNhinTargetSystem(respondingGatewayCrossGatewayRetrieveRequest.getNhinTargetSystem());

            // Send message
            log.debug("Calling secure NHIN Proxy doc retrieve.");
            result = port.respondingGatewayCrossGatewayRetrieve(body);
        }
        catch (Exception ex)
        {
            log.error("Error calling NHIN Proxy doc retrieve secured service: " + ex.getMessage(), ex);
        }

        log.debug("End NhincProxyDocRetrieveImpl.respondingGatewayCrossGatewayRetrieve");
        return result;
    }
    
    private NhincProxyDocRetrieveSecuredPortType getPort(String url) {
        NhincProxyDocRetrieveSecuredPortType port = service.getNhincProxyDocRetrieveSecuredPortSoap11();
        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        return port;
    }
}
