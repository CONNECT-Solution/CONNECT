/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.xdr.async.request.error;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterXDRRequestError_Service", portName = "AdapterXDRRequestError_Port", endpointInterface = "gov.hhs.fha.nhinc.adapterxdrrequesterror.AdapterXDRRequestErrorPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterxdrrequesterror", wsdlLocation = "WEB-INF/wsdl/AdapterXDRRequestError/AdapterXDRRequestError.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class AdapterXDRRequestError {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBRequestError(gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestErrorType body) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
