package gov.hhs.fha.nhinc.docretrieve.proxy;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "NhincProxyDocRetrieveSecured", portName = "NhincProxyDocRetrieveSecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.nhincproxydocretrievesecured.NhincProxyDocRetrieveSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxydocretrievesecured", wsdlLocation = "WEB-INF/wsdl/NhincProxyDocRetrieveSecured/NhincProxyDocRetrieveSecured.wsdl")
public class NhincProxyDocRetrieveSecured
{
    @Resource
    private WebServiceContext context;

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType body)
    {
        return new NhincProxyDocRetrieveSecuredImpl().respondingGatewayCrossGatewayRetrieve(body, context);
    }

}
