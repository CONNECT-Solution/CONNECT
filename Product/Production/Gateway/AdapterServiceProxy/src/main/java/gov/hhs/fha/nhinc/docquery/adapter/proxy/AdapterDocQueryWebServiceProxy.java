package gov.hhs.fha.nhinc.docquery.adapter.proxy;

import gov.hhs.fha.nhinc.adapterdocquerysecured.AdapterDocQuerySecured;
import gov.hhs.fha.nhinc.adapterdocquerysecured.AdapterDocQuerySecuredPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;

/**
 *
 *
 * @author Neil Webb
 */
public class AdapterDocQueryWebServiceProxy implements AdapterDocQueryProxy
{
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(AdapterDocQueryWebServiceProxy.class);
    private static AdapterDocQuerySecured service = new AdapterDocQuerySecured();

    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQueryRequestType respondingGatewayCrossGatewayQueryRequest)
    {
        AdhocQueryResponse response = null;
        try
        {
            String url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_DOC_QUERY_SECURED_SERVICE_NAME);

            AdapterDocQuerySecuredPortType port = getPort(url);

            log.debug("Setting assertion");
            AssertionType assertIn = respondingGatewayCrossGatewayQueryRequest.getAssertion();
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.DOC_RETRIEVE_ACTION);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);
            
            response = port.respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayQueryRequest.getAdhocQueryRequest());
        }
        catch (Exception ex)
        {
            log.error("Error sending NHIN Proxy message: " + ex.getMessage(), ex);
            response = new AdhocQueryResponse();
            response.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");

            RegistryError registryError = new RegistryError();
            registryError.setCodeContext("Processing NHIN Proxy document retrieve");
            registryError.setErrorCode("XDSRepositoryError");
            registryError.setSeverity("Error");
            response.getRegistryErrorList().getRegistryError().add(registryError);
        }

        return response;
    }
    
    private AdapterDocQuerySecuredPortType getPort(String url)
    {
        AdapterDocQuerySecuredPortType port = service.getAdapterDocQuerySecuredPortSoap11();
        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        return port;
    }
}
