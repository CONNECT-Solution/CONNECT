/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docsubmission.adapter.component;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterComponentXDRSecured_Service", portName = "AdapterComponentXDRSecured_Port", endpointInterface = "gov.hhs.fha.nhinc.adaptercomponentxdrsecured.AdapterComponentXDRSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adaptercomponentxdrsecured", wsdlLocation = "WEB-INF/wsdl/AdapterComponentDocSubmissionSecured/AdapterComponentXDRSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled=true)
public class AdapterComponentDocSubmissionSecured {
    @Resource
    private WebServiceContext context;

    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType provideAndRegisterDocumentSetb(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body) {
        return new AdapterComponentDocSubmissionImpl().provideAndRegisterDocumentSetb(body, context);
    }

}
