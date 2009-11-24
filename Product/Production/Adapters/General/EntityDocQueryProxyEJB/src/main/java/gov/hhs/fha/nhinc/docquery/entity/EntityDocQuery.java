package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.entitydocquery.EntityDocQueryPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 *
 * @author Neil Webb
 */
@WebService(serviceName = "EntityDocQuery", portName = "EntityDocQueryPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.entitydocquery.EntityDocQueryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitydocquery", wsdlLocation = "META-INF/wsdl/EntityDocQuery/EntityDocQuery.wsdl")
@Stateless
public class EntityDocQuery implements EntityDocQueryPortType
{

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse respondingGatewayCrossGatewayQuery(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType respondingGatewayCrossGatewayQueryRequest)
    {
        return new EntityDocQueryImpl().respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayQueryRequest);
    }
    
}
