package gov.hhs.fha.nhinc.docquery.entity;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "EntityDocQuery", portName = "EntityDocQueryPortSoap", endpointInterface = "gov.hhs.fha.nhinc.entitydocquery.EntityDocQueryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitydocquery", wsdlLocation = "WEB-INF/wsdl/EntityDocQuery/EntityDocQuery.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class EntityDocQuery {

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse respondingGatewayCrossGatewayQuery(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType respondingGatewayCrossGatewayQueryRequest) {
        return new EntityDocQueryImpl().respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayQueryRequest);
    }

}
