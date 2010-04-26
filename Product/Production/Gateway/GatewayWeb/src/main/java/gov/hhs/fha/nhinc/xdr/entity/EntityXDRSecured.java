package gov.hhs.fha.nhinc.xdr.entity;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author dunnek
 */
@WebService(serviceName = "EntityXDRSecured_Service", portName = "EntityXDRSecured_Port", endpointInterface = "gov.hhs.fha.nhinc.nhincentityxdrsecured.EntityXDRSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincentityxdrsecured", wsdlLocation = "WEB-INF/wsdl/EntityXDRSecured/EntityXDRSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class EntityXDRSecured {

    @Resource
    private WebServiceContext context;

    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType provideAndRegisterDocumentSetB(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request) {
        return new EntityXDRSecuredImpl().provideAndRegisterDocumentSetB(request, context);
    }

}
