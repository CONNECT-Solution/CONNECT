/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.async.response;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "XDRDeferredResponse_Service", portName = "XDRDeferredResponse_Port_Soap", endpointInterface = "ihe.iti.xdr._2007.XDRDeferredResponsePortType", targetNamespace = "urn:ihe:iti:xdr:2007", wsdlLocation = "WEB-INF/wsdl/NhinXDRResponse/NhinXDRDeferredResponse.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhinXDRResponse {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBDeferredResponse(oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType body) {
        return new NhinXDRResponseImpl().provideAndRegisterDocumentSetBResponse(body, context);
    }

}
