package gov.hhs.fha.nhinc.adapterdocquery;

import gov.hhs.fha.nhinc.adapterdocquerysecured.AdapterDocQuerySecured;
import gov.hhs.fha.nhinc.adapterdocquerysecured.AdapterDocQuerySecuredPortType;
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
public class AdapterDocQueryServiceImpl
{
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(AdapterDocQueryServiceImpl.class);
    private static AdapterDocQuerySecured service = new AdapterDocQuerySecured();

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse respondingGatewayCrossGatewayQuery(gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType respondingGatewayCrossGatewayQueryRequest)
    {
        oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse response = null;

        try
        { 
            String url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_DOC_QUERY_SECURED_SERVICE_NAME);
            AdapterDocQuerySecuredPortType port = getPort(url);

            AssertionType assertIn = respondingGatewayCrossGatewayQueryRequest.getAssertion();
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.DOC_QUERY_ACTION);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);
            
            response = port.respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayQueryRequest.getAdhocQueryRequest());
        }
        catch (Exception ex)
        {
            log.error("Error calling adapter doc query secured service: " + ex.getMessage(), ex);
        }

        return response;
    }
    
    private AdapterDocQuerySecuredPortType getPort(String url) {
        AdapterDocQuerySecuredPortType port = service.getAdapterDocQuerySecuredPortSoap11();

        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);

        return port;
    }
}
