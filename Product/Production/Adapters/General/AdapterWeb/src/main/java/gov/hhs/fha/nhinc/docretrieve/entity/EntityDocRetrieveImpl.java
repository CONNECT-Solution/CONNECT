package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import gov.hhs.fha.nhinc.entitydocretrievesecured.EntityDocRetrieveSecured;
import gov.hhs.fha.nhinc.entitydocretrievesecured.EntityDocRetrieveSecuredPortType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

/**
 *
 *
 * @author Neil Webb
 */
public class EntityDocRetrieveImpl
{
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(EntityDocRetrieveImpl.class);
    private static EntityDocRetrieveSecured service = new EntityDocRetrieveSecured();

    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType respondingGatewayCrossGatewayRetrieveRequest)
    {
        RetrieveDocumentSetResponseType result = null;
        log.debug("Begin EntityDocRetrieveImpl.respondingGatewayCrossGatewayRetrieve");

        try
        {
            // Get endpoint
            String url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ENTITY_DOC_RETRIEVE_SECURED_SERVICE_NAME);
            EntityDocRetrieveSecuredPortType port = getPort(url);

            // Set Assertion
            AssertionType assertIn = respondingGatewayCrossGatewayRetrieveRequest.getAssertion();
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.DOC_QUERY_ACTION);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);
            
            // Send message
            log.debug("Calling secure entity doc retrieve.");
            result = port.respondingGatewayCrossGatewayRetrieve(respondingGatewayCrossGatewayRetrieveRequest.getRetrieveDocumentSetRequest());
        }
        catch (Exception ex)
        {
            log.error("Error calling entity doc retrieve secured service: " + ex.getMessage(), ex);
        }

        log.debug("End EntityDocRetrieveImpl.respondingGatewayCrossGatewayRetrieve");
        return result;
    }
    
    private EntityDocRetrieveSecuredPortType getPort(String url) {
        EntityDocRetrieveSecuredPortType port = service.getEntityDocRetrieveSecuredPortSoap11();
        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        return port;
    }
}
