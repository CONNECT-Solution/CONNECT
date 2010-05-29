/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.adapter;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterComponentXDRRequest_Service", portName = "AdapterXDRRequest_Port", endpointInterface = "gov.hhs.fha.nhinc.adaptercomponentxdrrequest.AdapterComponentXDRRequestPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adaptercomponentxdrrequest", wsdlLocation = "WEB-INF/wsdl/AdapterComponentXDRRequest/AdapterComponentXDRRequest.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterComponentXDRRequest {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestType body) {
        return new AdapterComponentXDRRequestImpl().provideAndRegisterDocumentSetBRequest(body, context);
    }

}
