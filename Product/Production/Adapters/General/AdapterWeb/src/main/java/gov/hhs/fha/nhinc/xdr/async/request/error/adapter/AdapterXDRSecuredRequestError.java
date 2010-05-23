/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.async.request.error.adapter;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterXDRRequestErrorSecured_Service", portName = "AdapterXDRRequestErrorSecured_Port_Soap", endpointInterface = "gov.hhs.fha.nhinc.adapterxdrrequesterrorsecured.AdapterXDRRequestErrorSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterxdrrequesterrorsecured", wsdlLocation = "WEB-INF/wsdl/AdapterXDRSecuredRequestError/AdapterXDRRequestSecuredError.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class AdapterXDRSecuredRequestError {

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBRequestError(gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestErrorSecuredType body) {
        return new AdapterXDRSecuredRequestErrorImpl().provideAndRegisterDocumentSetBRequestError(body);
    }

}
