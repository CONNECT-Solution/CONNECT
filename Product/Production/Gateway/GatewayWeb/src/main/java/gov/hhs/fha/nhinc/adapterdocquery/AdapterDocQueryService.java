package gov.hhs.fha.nhinc.adapterdocquery;

import javax.jws.WebService;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "AdapterDocQuery", portName = "AdapterDocQueryPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterdocquery.AdapterDocQueryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterdocquery", wsdlLocation = "WEB-INF/wsdl/AdapterDocQueryService/AdapterDocQuery.wsdl")
public class AdapterDocQueryService
{

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse respondingGatewayCrossGatewayQuery(gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType respondingGatewayCrossGatewayQueryRequest)
    {
        return new AdapterDocQueryServiceImpl().respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayQueryRequest);
    }

}
