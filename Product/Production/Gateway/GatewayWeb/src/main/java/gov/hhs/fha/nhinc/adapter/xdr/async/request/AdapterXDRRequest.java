/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.xdr.async.request;

import ihe.iti.xdr._2007.AcknowledgementType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterXDRRequest_Service", portName = "AdapterXDRRequest_Port", endpointInterface = "gov.hhs.fha.nhinc.adapterxdrrequest.AdapterXDRRequestPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterxdrrequest", wsdlLocation = "WEB-INF/wsdl/AdapterXDRRequest/AdapterXDRRequest.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterXDRRequest {
    @Resource
    private WebServiceContext context;

    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBRequest(gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestType body) {
        return new AdapterXDRRequestImpl().provideAndRegisterDocumentSetBRequest(body, context);
    }
}
