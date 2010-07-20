package gov.hhs.fha.nhinc.xdr.proxy;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "ProxyXDR_Service", portName = "ProxyXDR_Port", endpointInterface = "gov.hhs.fha.nhinc.nhincproxyxdr.ProxyXDRPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxyxdr", wsdlLocation = "WEB-INF/wsdl/ProxyXDR/NhincProxyXDR.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class ProxyXDR {

    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType provideAndRegisterDocumentSetB(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetRequestType body) {
        //TODO implement this method
        return new ProxyXDRImpl().provideAndRegisterDocumentSetB(body);
    }

}
