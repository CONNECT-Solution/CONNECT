package gov.hhs.fha.nhinc.patientdiscovery.entity;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "EntityPatientDiscoverySecured", portName = "EntityPatientDiscoverySecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.entitypatientdiscoverysecured.EntityPatientDiscoverySecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitypatientdiscoverysecured", wsdlLocation = "WEB-INF/wsdl/EntityPatientDiscoverySecured/EntityPatientDiscoverySecured.wsdl")
public class EntityPatientDiscoverySecured
{
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType respondingGatewayPRPAIN201305UV02(org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType respondingGatewayPRPAIN201305UV02Request)
    {
        return new EntityPatientDiscoverySecuredImpl().respondingGatewayPRPAIN201305UV02(respondingGatewayPRPAIN201305UV02Request, context);
    }

}
