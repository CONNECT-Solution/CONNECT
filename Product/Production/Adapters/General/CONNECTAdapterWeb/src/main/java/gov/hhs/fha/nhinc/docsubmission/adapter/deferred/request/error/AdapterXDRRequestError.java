package gov.hhs.fha.nhinc.docsubmission.adapter.deferred.request.error;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "AdapterXDRRequestError_Service", portName = "AdapterXDRRequestError_Port", endpointInterface = "gov.hhs.fha.nhinc.adapterxdrrequesterror.AdapterXDRRequestErrorPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterxdrrequesterror", wsdlLocation = "WEB-INF/wsdl/AdapterXDRRequestError/AdapterXDRRequestError.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true)
public class AdapterXDRRequestError
{
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBRequestError(gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestErrorType body)
    {
        return new AdapterXDRSecuredRequestErrorImpl().provideAndRegisterDocumentSetBRequestError(body, context);
    }
}
