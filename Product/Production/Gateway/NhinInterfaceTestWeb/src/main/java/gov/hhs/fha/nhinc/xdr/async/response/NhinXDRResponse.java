package gov.hhs.fha.nhinc.xdr.async.response;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "XDRResponse_Service", portName = "XDRResponse_Port_Soap", endpointInterface = "ihe.iti.xdr.async.response._2007.XDRResponsePortType", targetNamespace = "urn:ihe:iti:xdr:async:response:2007", wsdlLocation = "WEB-INF/wsdl/NhinXDRResponse/NhinXDRResponse.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhinXDRResponse
{

    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBResponse(oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType body)
    {
        return new NhinXDRResponseImpl().provideAndRegisterDocumentSetBResponse(body);
    }

}
