/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.entity;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author shawc
 */
@WebService(serviceName = "EntityPatientDiscoverySecured", portName = "EntityPatientDiscoverySecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.entitypatientdiscoverysecured.EntityPatientDiscoverySecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitypatientdiscoverysecured", wsdlLocation = "META-INF/wsdl/EntityPatientDiscoverySecured/EntityPatientDiscoverySecured.wsdl")
@Stateless
public class EntityPatientDiscoverySecured {
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType respondingGatewayPRPAIN201305UV02(org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType respondingGatewayPRPAIN201305UV02Request) {
        return new EntityPatientDiscoverySecuredImpl().respondingGatewayPRPAIN201305UV02(respondingGatewayPRPAIN201305UV02Request, context);
    }

}
