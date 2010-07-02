package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.entitydocquery.EntityDocQuerySecured;
import gov.hhs.fha.nhinc.entitydocquery.EntityDocQuerySecuredPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

/**
 *
 *
 * @author Neil Webb
 */
public class EntityDocQueryImpl
{
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(EntityDocQueryImpl.class);
    private static EntityDocQuerySecured service = new EntityDocQuerySecured();

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse respondingGatewayCrossGatewayQuery(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType respondingGatewayCrossGatewayQueryRequest)
    {
        oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse response = null;

        try
        {
            String url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ENTITY_DOC_QUERY_SECURED_SERVICE_NAME);
            
            EntityDocQuerySecuredPortType port = getPort(url);

            AssertionType assertIn = respondingGatewayCrossGatewayQueryRequest.getAssertion();
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.DOC_QUERY_ACTION);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType body = new gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType();
            body.setAdhocQueryRequest(respondingGatewayCrossGatewayQueryRequest.getAdhocQueryRequest());
            body.setNhinTargetCommunities(respondingGatewayCrossGatewayQueryRequest.getNhinTargetCommunities());
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
    
    private EntityDocQuerySecuredPortType getPort(String url) {
        EntityDocQuerySecuredPortType port = service.getEntityDocQuerySecuredPortSoap11();
        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        return port;
    }
}
