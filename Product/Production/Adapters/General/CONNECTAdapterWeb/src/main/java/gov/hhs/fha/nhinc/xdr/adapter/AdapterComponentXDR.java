package gov.hhs.fha.nhinc.xdr.adapter;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
//import oasis.names.tc.ebxml_regrep.*;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterXDR_Service", portName = "AdapterXDR_Port", endpointInterface = "gov.hhs.fha.nhinc.nhincadapterxdr.AdapterXDRPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincadapterxdr", wsdlLocation = "WEB-INF/wsdl/AdapterComponentXDR/AdapterComponentXDR.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterComponentXDR {
    @Resource
    private WebServiceContext context;
    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType provideAndRegisterDocumentSetb(gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestType body) {
        return new AdapterComponentXDRImpl().provideAndRegisterDocumentSetb(body);
    }

}
