package gov.hhs.fha.nhinc.xdr.proxy;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
/**
 *
 * @author dunnek
 */
@WebService(serviceName = "ProxyXDRSecured_Service", portName = "ProxyXDRSecured_Port", endpointInterface = "gov.hhs.fha.nhinc.nhincproxyxdrsecured.ProxyXDRSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxyxdrsecured", wsdlLocation = "WEB-INF/wsdl/NhincProxyXDRSecured/NhincProxyXDRSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhincProxyXDRSecured {
    @Resource
    private WebServiceContext context;

    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType provideAndRegisterDocumentSetB(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType body) {
        //TODO implement this method
        return new NhincProxyXDRSecuredImpl().provideAndRegisterDocumentSetB(body, context);
    }

}
