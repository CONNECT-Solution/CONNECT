package gov.hhs.fha.nhinc.docquery.proxy;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxyDocQuerySecured", portName = "NhincProxyDocQuerySecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.nhincproxydocquerysecured.NhincProxyDocQuerySecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxydocquerysecured", wsdlLocation = "WEB-INF/wsdl/NhincProxyDocQuerySecured/NhincProxyDocQuerySecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhincProxyDocQuerySecured {

    @Resource
    private WebServiceContext context;

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse respondingGatewayCrossGatewayQuery(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQuerySecuredRequestType body)
    {
        return new NhincProxyDocQueryImpl().respondingGatewayCrossGatewayQuery(body, context);
    }

}
