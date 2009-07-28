/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.reidentification;

import gov.hhs.fha.nhinc.adapterreidentification.AdapterReidentificationPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 * This provides the web service access to the data store that maps the pseudo 
 * identities to the identities know to this gateway.
 */
@WebService(serviceName = "AdapterReidentification", portName = "AdapterReidentificationBindingSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterreidentification.AdapterReidentificationPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterreidentification", wsdlLocation = "META-INF/wsdl/AdapterReidentification/AdapterReidentification.wsdl")
@Stateless
public class AdapterReidentification implements AdapterReidentificationPortType {

    public org.hl7.v3.PIXConsumerPRPAIN201310UVRequestType getRealIdentifier(org.hl7.v3.PIXConsumerPRPAIN201309UVRequestType getRealIdentifierRequest) {
        AdapterReidentificationImpl impl = new AdapterReidentificationImpl();
        return impl.getRealIdentifier(getRealIdentifierRequest);
    }

}
