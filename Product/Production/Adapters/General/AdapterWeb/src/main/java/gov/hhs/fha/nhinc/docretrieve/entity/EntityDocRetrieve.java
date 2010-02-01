package gov.hhs.fha.nhinc.docretrieve.entity;

import javax.jws.WebService;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "EntityDocRetrieve", portName = "EntityDocRetrievePortSoap11", endpointInterface = "gov.hhs.fha.nhinc.entitydocretrieve.EntityDocRetrievePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitydocretrieve", wsdlLocation = "WEB-INF/wsdl/EntityDocRetrieve/EntityDocRetrieve.wsdl")
public class EntityDocRetrieve {

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType respondingGatewayCrossGatewayRetrieveRequest) {
        return new EntityDocRetrieveImpl().respondingGatewayCrossGatewayRetrieve(respondingGatewayCrossGatewayRetrieveRequest);
    }

}
