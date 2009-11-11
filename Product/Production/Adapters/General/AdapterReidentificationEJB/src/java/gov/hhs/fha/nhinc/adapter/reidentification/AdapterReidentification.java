/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.reidentification;

import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "AdapterReidentification", portName = "AdapterReidentificationBindingSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterreidentification.AdapterReidentificationPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterreidentification", wsdlLocation = "META-INF/wsdl/AdapterReidentification/AdapterReidentification.wsdl")
@Stateless
public class AdapterReidentification {

    public org.hl7.v3.PIXConsumerPRPAIN201310UVRequestType getRealIdentifier(org.hl7.v3.PIXConsumerPRPAIN201309UVRequestType getRealIdentifierRequest) {
        AdapterReidentificationImpl impl = new AdapterReidentificationImpl();
        return impl.getRealIdentifier(getRealIdentifierRequest);
    }

}
