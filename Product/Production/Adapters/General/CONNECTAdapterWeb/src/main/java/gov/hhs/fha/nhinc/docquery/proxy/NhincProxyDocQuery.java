package gov.hhs.fha.nhinc.docquery.proxy;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxyDocQuery", portName = "NhincProxyDocQueryPortSoap", endpointInterface = "gov.hhs.fha.nhinc.nhincproxydocquery.NhincProxyDocQueryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxydocquery", wsdlLocation = "WEB-INF/wsdl/NhincProxyDocQuery/NhincProxyDocQuery.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhincProxyDocQuery {

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse respondingGatewayCrossGatewayQuery(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQueryRequestType respondingGatewayCrossGatewayQueryRequest) {
        return new NhincProxyDocQueryImpl().respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayQueryRequest);
    }

}
