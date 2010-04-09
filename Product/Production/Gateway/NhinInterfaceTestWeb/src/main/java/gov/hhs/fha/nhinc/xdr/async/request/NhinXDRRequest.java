package gov.hhs.fha.nhinc.xdr.async.request;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "XDRRequest_Service", portName = "XDRRequest_Port_Soap12", endpointInterface = "ihe.iti.xdr.async.request._2007.XDRRequestPortType", targetNamespace = "urn:ihe:iti:xdr:async:request:2007", wsdlLocation = "WEB-INF/wsdl/NhinXDRRequest/NhinXDRRequest.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class NhinXDRRequest
{

    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBRequest(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body)
    {
        return new NhinXDRRequestImpl().provideAndRegisterDocumentSetBRequest(body);
    }

}
