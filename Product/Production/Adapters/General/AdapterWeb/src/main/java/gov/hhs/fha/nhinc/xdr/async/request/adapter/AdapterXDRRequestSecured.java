/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.async.request.adapter;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterXDRRequestSecured_Service", portName = "AdapterXDRRequestSecured_Port_Soap", endpointInterface = "gov.hhs.fha.nhinc.adapterxdrrequestsecured.AdapterXDRRequestSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterxdrrequestsecured", wsdlLocation = "WEB-INF/wsdl/AdapterXDRRequestSecured/AdapterXDRRequestSecured.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class AdapterXDRRequestSecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body) {
        return new AdapterXDRRequestSecuredImpl().provideAndRegisterDocumentSetBRequest(body, context);
    }

}
