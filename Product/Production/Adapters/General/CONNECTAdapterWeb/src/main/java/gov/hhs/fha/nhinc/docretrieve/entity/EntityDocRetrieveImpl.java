package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import gov.hhs.fha.nhinc.entitydocretrievesecured.EntityDocRetrieveSecured;
import gov.hhs.fha.nhinc.entitydocretrievesecured.EntityDocRetrieveSecuredPortType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;

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

        if (result != null &&
                NullChecker.isNotNullish(result.getDocumentResponse())) {
            for (DocumentResponse response : result.getDocumentResponse()) {
                log.debug("Found File of Size: " + response.getDocument().length);
            }
        }
        return result;
    }
    
    private EntityDocRetrieveSecuredPortType getPort(String url) {
        EntityDocRetrieveSecuredPortType port = service.getEntityDocRetrieveSecuredPortSoap();

        log.info("Setting endpoint address to Entity Document Retrieve Secured Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }
}
