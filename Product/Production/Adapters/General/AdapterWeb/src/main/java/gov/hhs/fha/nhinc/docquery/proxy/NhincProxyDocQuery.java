package gov.hhs.fha.nhinc.docquery.proxy;

import javax.jws.WebService;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxyDocQuery", portName = "NhincProxyDocQueryPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.nhincproxydocquery.NhincProxyDocQueryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxydocquery", wsdlLocation = "WEB-INF/wsdl/NhincProxyDocQuery/NhincProxyDocQuery.wsdl")
public class NhincProxyDocQuery {

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse respondingGatewayCrossGatewayQuery(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQueryRequestType respondingGatewayCrossGatewayQueryRequest) {
        return new NhincProxyDocQueryImpl().respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayQueryRequest);
    }

}
