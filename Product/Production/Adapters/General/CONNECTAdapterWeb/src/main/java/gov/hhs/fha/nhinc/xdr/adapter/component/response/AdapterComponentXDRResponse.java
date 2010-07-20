/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.adapter.component.response;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterComponentXDRResponse_Service", portName = "AdapterComponentXDRResponse_Port", endpointInterface = "gov.hhs.fha.nhinc.adaptercomponentxdrresponse.AdapterComponentXDRResponsePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adaptercomponentxdrresponse", wsdlLocation = "WEB-INF/wsdl/AdapterComponentXDRResponse/AdapterComponentXDRResponse.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterComponentXDRResponse {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterRegistryResponseType body) {
        return new AdapterComponentXDRResponseImpl().provideAndRegisterDocumentSetBResponse(body, context);
    }

}
