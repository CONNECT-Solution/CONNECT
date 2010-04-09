/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.async.request.adapter;

import ihe.iti.xdr._2007.AcknowledgementType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author dunnek
 */
@WebService(serviceName = "AdapterXDRRequest_Service", portName = "AdapterXDRRequest_Port", endpointInterface = "gov.hhs.fha.nhinc.adapterxdrrequest.AdapterXDRRequestPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterxdrrequest", wsdlLocation = "WEB-INF/wsdl/AdapterXDRRequest/AdapterXDRRequest.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class AdapterXDRRequest {
    @Resource
    private WebServiceContext context;
    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBRequest(gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestType body) {
        //TODO implement this method

        return new AdapterXDRRequestImpl().provideAndRegisterDocumentSetBRequest(body, context);
    }

}
