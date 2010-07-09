package gov.hhs.fha.nhinc.patientdiscovery.entity;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "EntityPatientDiscoverySecured", portName = "EntityPatientDiscoverySecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.entitypatientdiscoverysecured.EntityPatientDiscoverySecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitypatientdiscoverysecured", wsdlLocation = "WEB-INF/wsdl/EntityPatientDiscoverySecured/EntityPatientDiscoverySecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class EntityPatientDiscoverySecured
{
    @Resource
    private WebServiceContext context;

    protected EntityPatientDiscoverySecuredImpl getEntityPatientDiscoverySecuredImpl()
    {
        return new EntityPatientDiscoverySecuredImpl();
    }

    protected WebServiceContext getWebServiceContext()
    {
        return context;
    }

    public RespondingGatewayPRPAIN201306UV02ResponseType respondingGatewayPRPAIN201305UV02(RespondingGatewayPRPAIN201305UV02RequestType respondingGatewayPRPAIN201305UV02Request)
    {
        RespondingGatewayPRPAIN201306UV02ResponseType response = null;

        EntityPatientDiscoverySecuredImpl serviceImpl = getEntityPatientDiscoverySecuredImpl();
        if(serviceImpl != null)
        {
            response =  serviceImpl.respondingGatewayPRPAIN201305UV02(respondingGatewayPRPAIN201305UV02Request, getWebServiceContext());
        }
        return response;
    }

}
