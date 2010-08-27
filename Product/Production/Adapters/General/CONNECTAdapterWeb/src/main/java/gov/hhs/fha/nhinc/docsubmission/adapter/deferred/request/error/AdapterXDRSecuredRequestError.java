package gov.hhs.fha.nhinc.docsubmission.adapter.deferred.request.error;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterXDRRequestErrorSecured_Service", portName = "AdapterXDRRequestErrorSecured_Port_Soap", endpointInterface = "gov.hhs.fha.nhinc.adapterxdrrequesterrorsecured.AdapterXDRRequestErrorSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterxdrrequesterrorsecured", wsdlLocation = "WEB-INF/wsdl/AdapterXDRSecuredRequestError/AdapterXDRRequestSecuredError.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true)
public class AdapterXDRSecuredRequestError
{
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBRequestError(gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestErrorSecuredType body)
    {
        return new AdapterXDRSecuredRequestErrorImpl().provideAndRegisterDocumentSetBRequestError(body, context);
    }
}
