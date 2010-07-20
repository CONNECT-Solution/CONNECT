/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.async.response.adapter;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterXDRResponseSecured_Service", portName = "AdapterXDRResponseSecured_Port_Soap", endpointInterface = "gov.hhs.fha.nhinc.adapterxdrresponsesecured.AdapterXDRResponseSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterxdrresponsesecured", wsdlLocation = "WEB-INF/wsdl/AdapterXDRResponseSecured/AdapterXDRResponseSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterXDRResponseSecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType body) {
        return new AdapterXDRResponseSecuredImpl().provideAndRegisterDocumentSetBResponse(body, context);
    }

}
