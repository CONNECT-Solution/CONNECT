package gov.hhs.fha.nhinc.docsubmission.adapter.deferred.request;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.annotation.Resource;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author westberg
 */
@WebService(serviceName = "AdapterXDRRequestSecured_Service", portName = "AdapterXDRRequestSecured_Port_Soap", endpointInterface = "gov.hhs.fha.nhinc.adapterxdrrequestsecured.AdapterXDRRequestSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterxdrrequestsecured", wsdlLocation = "WEB-INF/wsdl/AdapterXDRRequestSecured/AdapterXDRRequestSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled=true)
public class AdapterXDRRequestSecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetSecuredRequestType body)
    {
        return new AdapterXDRRequestImpl().provideAndRegisterDocumentSetBRequest(body, context);
    }

}
