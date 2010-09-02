package gov.hhs.fha.nhinc.xdr.async.request;

import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import javax.jws.WebService;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "XDRDeferredRequest_Service", portName = "XDRDeferredRequest_Port_Soap", endpointInterface = "ihe.iti.xdr._2007.XDRDeferredRequestPortType", targetNamespace = "urn:ihe:iti:xdr:2007", wsdlLocation = "WEB-INF/wsdl/NhinXDRRequest/NhinXDRDeferredRequest.wsdl")
public class NhinXDRRequest {

    public XDRAcknowledgementType provideAndRegisterDocumentSetBDeferredRequest(ProvideAndRegisterDocumentSetRequestType body) {
        return new NhinXDRRequestImpl().provideAndRegisterDocumentSetBRequest(body);
    }

}
