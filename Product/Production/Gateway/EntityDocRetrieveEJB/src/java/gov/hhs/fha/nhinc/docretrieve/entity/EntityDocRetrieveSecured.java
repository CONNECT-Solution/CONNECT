package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.entitydocretrievesecured.EntityDocRetrieveSecuredPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;

/**
 *
 *
 * @author Neil Webb
 */
@WebService(serviceName = "EntityDocRetrieveSecured", portName = "EntityDocRetrieveSecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.entitydocretrievesecured.EntityDocRetrieveSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitydocretrievesecured", wsdlLocation = "META-INF/wsdl/EntityDocRetrieveSecured/EntityDocRetrieveSecured.wsdl")
@Stateless
public class EntityDocRetrieveSecured implements EntityDocRetrieveSecuredPortType
{
    @Resource
    private WebServiceContext context;

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body)
    {
        return new EntityDocRetrieveSecuredImpl().respondingGatewayCrossGatewayRetrieve(body, context);
    }
    
}
