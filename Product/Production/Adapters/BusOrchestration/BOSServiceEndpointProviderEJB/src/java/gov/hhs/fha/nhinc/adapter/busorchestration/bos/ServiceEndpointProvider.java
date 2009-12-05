/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.busorchestration.bos;

import gov.hhs.fha.nhinc.bosserviceenpointprovider.BOSServiceEndpointProviderPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author Jerry Goodnough
 */
@WebService(serviceName = "BOSServiceEndpointProvider", portName =
                                                        "BOSServiceEndpointProviderSoap11", endpointInterface =
                                                                                            "gov.hhs.fha.nhinc.bosserviceenpointprovider.BOSServiceEndpointProviderPortType", targetNamespace =
                                                                                                                                                                              "urn:gov:hhs:fha:nhinc:bosserviceenpointprovider", wsdlLocation =
                                                                                                                                                                                                                                 "META-INF/wsdl/ServiceEndpointProvider/BOSServiceEndpointProvider.wsdl")
@Stateless
public class ServiceEndpointProvider implements BOSServiceEndpointProviderPortType {

    public org.netbeans.xml.schema.endpoint.CreateEPRResponse findEndpoint(org.netbeans.xml.schema.endpoint.CreateEPRRequest bosFindEndPointRequest)
    {
        return ServiceEndpointProviderImpl.getInstance().findEndpoint(
                bosFindEndPointRequest);
    }

}
