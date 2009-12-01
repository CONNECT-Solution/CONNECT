/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.patientdiscovery;

import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "AdapterPatientDiscovery", portName = "AdapterPatientDiscoveryPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterpatientdiscovery.AdapterPatientDiscoveryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpatientdiscovery", wsdlLocation = "META-INF/wsdl/AdapterPatientDiscovery/AdapterPatientDiscovery.wsdl")
@Stateless
public class AdapterPatientDiscovery {

    public org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType respondingGatewayPRPAIN201305UV02(org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType respondingGatewayPRPAIN201305UV02Request) {
        return new AdapterPatientDiscoveryImpl().respondingGatewayPRPAIN201305UV02(respondingGatewayPRPAIN201305UV02Request);
    }

}
