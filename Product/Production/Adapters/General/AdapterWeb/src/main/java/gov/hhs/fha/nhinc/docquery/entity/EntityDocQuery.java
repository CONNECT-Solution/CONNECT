package gov.hhs.fha.nhinc.docquery.entity;

import javax.jws.WebService;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "EntityDocQuery", portName = "EntityDocQueryPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.entitydocquery.EntityDocQueryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitydocquery", wsdlLocation = "WEB-INF/wsdl/EntityDocQuery/EntityDocQuery.wsdl")
public class EntityDocQuery {

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse respondingGatewayCrossGatewayQuery(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType respondingGatewayCrossGatewayQueryRequest) {
        return new EntityDocQueryImpl().respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayQueryRequest);
    }

}
