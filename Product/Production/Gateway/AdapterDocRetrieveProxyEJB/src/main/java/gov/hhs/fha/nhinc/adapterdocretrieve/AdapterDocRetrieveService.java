package gov.hhs.fha.nhinc.adapterdocretrieve;

import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 *
 * @author Neil Webb
 */
@WebService(serviceName = "AdapterDocRetrieve", portName = "AdapterDocRetrievePortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterdocretrieve.AdapterDocRetrievePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterdocretrieve", wsdlLocation = "META-INF/wsdl/AdapterDocRetrieveService/AdapterDocRetrieve.wsdl")
@Stateless
public class AdapterDocRetrieveService implements AdapterDocRetrievePortType
{

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveRequestType respondingGatewayCrossGatewayRetrieveRequest)
    {
        return new AdapterDocRetrieveServiceImpl().respondingGatewayCrossGatewayRetrieve(respondingGatewayCrossGatewayRetrieveRequest);
    }
    
}
