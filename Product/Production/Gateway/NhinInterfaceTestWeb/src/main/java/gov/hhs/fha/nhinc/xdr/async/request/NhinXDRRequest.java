package gov.hhs.fha.nhinc.xdr.async.request;

import javax.jws.WebService;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "XDRRequest_Service", portName = "XDRRequest_Port_Soap", endpointInterface = "ihe.iti.xdr.async.request._2007.XDRRequestPortType", targetNamespace = "urn:ihe:iti:xdr:async:request:2007", wsdlLocation = "WEB-INF/wsdl/NhinXDRRequest/NhinXDRRequest.wsdl")
public class NhinXDRRequest {

    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBRequest(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body) {
        return new NhinXDRRequestImpl().provideAndRegisterDocumentSetBRequest(body);
    }

}
