/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.async.request.adapter;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.annotation.Resource;

/**
 *
 * @author westberg
 */
@WebService(serviceName = "AdapterXDRRequestSecured_Service", portName = "AdapterXDRRequestSecured_Port_Soap", endpointInterface = "gov.hhs.fha.nhinc.adapterxdrrequestsecured.AdapterXDRRequestSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterxdrrequestsecured", wsdlLocation = "WEB-INF/wsdl/AdapterXDRRequestSecured/AdapterXDRRequestSecured.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class AdapterXDRRequestSecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetSecuredRequestType body)
    {
        return new AdapterXDRRequestSecuredImpl().provideAndRegisterDocumentSetBRequest(body, context);
    }

}
