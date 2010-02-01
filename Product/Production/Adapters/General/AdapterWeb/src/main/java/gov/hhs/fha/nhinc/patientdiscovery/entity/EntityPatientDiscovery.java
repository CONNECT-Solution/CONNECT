package gov.hhs.fha.nhinc.patientdiscovery.entity;

import javax.jws.WebService;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "EntityPatientDiscovery", portName = "EntityPatientDiscoveryPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.entitypatientdiscovery.EntityPatientDiscoveryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitypatientdiscovery", wsdlLocation = "WEB-INF/wsdl/EntityPatientDiscovery/EntityPatientDiscovery.wsdl")
public class EntityPatientDiscovery {

    public org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType respondingGatewayPRPAIN201305UV02(org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType respondingGatewayPRPAIN201305UV02Request) {
        return new EntityPatientDiscoveryImpl().respondingGatewayPRPAIN201305UV02(respondingGatewayPRPAIN201305UV02Request);
    }

}
