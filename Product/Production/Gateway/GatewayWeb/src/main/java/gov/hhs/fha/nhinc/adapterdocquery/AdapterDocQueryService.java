package gov.hhs.fha.nhinc.adapterdocquery;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterDocQuery", portName = "AdapterDocQueryPortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterdocquery.AdapterDocQueryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterdocquery", wsdlLocation = "WEB-INF/wsdl/AdapterDocQueryService/AdapterDocQuery.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterDocQueryService {

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse respondingGatewayCrossGatewayQuery(gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType respondingGatewayCrossGatewayQueryRequest)
    {
        return new AdapterDocQueryServiceImpl().respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayQueryRequest);
    }

}
