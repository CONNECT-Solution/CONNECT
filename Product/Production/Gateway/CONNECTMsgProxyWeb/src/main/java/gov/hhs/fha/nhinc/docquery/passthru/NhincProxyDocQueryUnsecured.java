/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.passthru;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "NhincProxyDocQuery", portName = "NhincProxyDocQueryPortSoap", endpointInterface = "gov.hhs.fha.nhinc.nhincproxydocquery.NhincProxyDocQueryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxydocquery", wsdlLocation = "WEB-INF/wsdl/NhincProxyDocQueryUnsecured/NhincProxyDocQuery.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhincProxyDocQueryUnsecured {
    @Resource
    private WebServiceContext context;

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse respondingGatewayCrossGatewayQuery(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQueryRequestType respondingGatewayCrossGatewayQueryRequest) {
        return new NhincProxyDocQueryImpl().respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayQueryRequest, context);
    }

}
