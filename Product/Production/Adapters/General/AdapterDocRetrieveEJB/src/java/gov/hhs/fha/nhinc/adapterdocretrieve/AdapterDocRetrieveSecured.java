package gov.hhs.fha.nhinc.adapterdocretrieve;

import gov.hhs.fha.nhinc.adapterdocretrievesecured.AdapterDocRetrieveSecuredPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 *
 * @author Neil Webb
 */
@WebService(serviceName = "AdapterDocRetrieveSecured", portName = "AdapterDocRetrieveSecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterdocretrievesecured.AdapterDocRetrieveSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterdocretrievesecured", wsdlLocation = "META-INF/wsdl/AdapterDocRetrieveSecured/AdapterDocRetrieveSecured.wsdl")
@Stateless
public class AdapterDocRetrieveSecured implements AdapterDocRetrieveSecuredPortType
{

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body)
    {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet - yo.");
    }
    
}
