package gov.hhs.fha.nhinc.xdr.adapter;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author dunnek
 */
@WebService(serviceName = "AdapterXDRSecured_Service", portName = "AdapterXDRSecured_Port", endpointInterface = "gov.hhs.fha.nhinc.nhincadapterxdrsecured.AdapterXDRSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincadapterxdrsecured", wsdlLocation = "WEB-INF/wsdl/AdapterComponentXDRSecured/AdapterComponentXDRSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterComponentXDRSecured {
    @Resource
    private WebServiceContext context;
    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType provideAndRegisterDocumentSetb(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body) {
        return new AdapterComponentXDRSecuredImpl().provideAndRegisterDocumentSetb(body, context);
    }

}
