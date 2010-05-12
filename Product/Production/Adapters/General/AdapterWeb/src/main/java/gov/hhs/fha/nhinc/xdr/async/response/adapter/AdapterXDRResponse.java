/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.async.response.adapter;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterXDRResponse_Service", portName = "AdapterXDRResponse_Port", endpointInterface = "gov.hhs.fha.nhinc.adapterxdrresponse.AdapterXDRResponsePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterxdrresponse", wsdlLocation = "WEB-INF/wsdl/AdapterXDRResponse/AdapterXDRResponse.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterXDRResponse {

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterRegistryResponseType body) {
        return new AdapterXDRResponseImpl().provideAndRegisterDocumentSetBResponse(body);
    }

}
