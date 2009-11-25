package gov.hhs.fha.nhinc.docretrieve.proxy;

import gov.hhs.fha.nhinc.nhincproxydocretrieve.NhincProxyDocRetrievePortType;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 *
 * @author Neil Webb
 */
@WebService(serviceName = "NhincProxyDocRetrieve", portName = "NhincProxyDocRetrievePortSoap11", endpointInterface = "gov.hhs.fha.nhinc.nhincproxydocretrieve.NhincProxyDocRetrievePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxydocretrieve", wsdlLocation = "META-INF/wsdl/NhincProxyDocRetrieve/NhincProxyDocRetrieve.wsdl")
@Stateless
public class NhincProxyDocRetrieve implements NhincProxyDocRetrievePortType
{

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveRequestType respondingGatewayCrossGatewayRetrieveRequest)
    {
        return new NhincProxyDocRetrieveImpl().respondingGatewayCrossGatewayRetrieve(respondingGatewayCrossGatewayRetrieveRequest);
    }
    
}
