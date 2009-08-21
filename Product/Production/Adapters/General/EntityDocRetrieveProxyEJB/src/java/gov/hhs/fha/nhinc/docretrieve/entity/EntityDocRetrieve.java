package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.entitydocretrieve.EntityDocRetrievePortType;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 *
 * @author Neil Webb
 */
@WebService(serviceName = "EntityDocRetrieve", portName = "EntityDocRetrievePortSoap11", endpointInterface = "gov.hhs.fha.nhinc.entitydocretrieve.EntityDocRetrievePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitydocretrieve", wsdlLocation = "META-INF/wsdl/EntityDocRetrieve/EntityDocRetrieve.wsdl")
@Stateless
public class EntityDocRetrieve implements EntityDocRetrievePortType
{

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType respondingGatewayCrossGatewayRetrieveRequest)
    {
        return new EntityDocRetrieveImpl().respondingGatewayCrossGatewayRetrieve(respondingGatewayCrossGatewayRetrieveRequest);
    }
    
}
