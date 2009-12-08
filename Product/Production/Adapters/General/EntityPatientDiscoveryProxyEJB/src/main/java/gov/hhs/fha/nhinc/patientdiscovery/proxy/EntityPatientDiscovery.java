/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.proxy;

import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *  This class services the EntityPatientDiscovery wsdl interface.
 * 
 * @author shawc
 */
@WebService(serviceName = "EntityPatientDiscovery", portName = "EntityPatientDiscoveryPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.entitypatientdiscovery.EntityPatientDiscoveryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitypatientdiscovery", wsdlLocation = "META-INF/wsdl/EntityPatientDiscovery/EntityPatientDiscovery.wsdl")
@Stateless
public class EntityPatientDiscovery {

    public org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType respondingGatewayPRPAIN201305UV02(org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType respondingGatewayPRPAIN201305UV02Request) {
        return new EntityPatientDiscoveryImpl().respondingGatewayPRPAIN201305UV02(respondingGatewayPRPAIN201305UV02Request);
    }

}
