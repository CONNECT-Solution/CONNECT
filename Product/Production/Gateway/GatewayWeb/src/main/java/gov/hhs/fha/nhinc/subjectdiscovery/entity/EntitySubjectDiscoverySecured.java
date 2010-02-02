package gov.hhs.fha.nhinc.subjectdiscovery.entity;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "EntitySubjectDiscoverySecured", portName = "EntitySubjectDiscoverySecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.entitysubjectdiscoverysecured.EntitySubjectDiscoverySecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitysubjectdiscoverysecured", wsdlLocation = "WEB-INF/wsdl/EntitySubjectDiscoverySecured/EntitySubjectDiscoverySecured.wsdl")
public class EntitySubjectDiscoverySecured
{
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201301UV(org.hl7.v3.PIXConsumerPRPAIN201301UVSecuredRequestType body)
    {
        throw new UnsupportedOperationException("Not implemented.");
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201302UV(org.hl7.v3.PIXConsumerPRPAIN201302UVSecuredRequestType body)
    {
        throw new UnsupportedOperationException("Not implemented.");
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201304UV(org.hl7.v3.PIXConsumerPRPAIN201304UVSecuredRequestType body)
    {
        throw new UnsupportedOperationException("Not implemented.");
    }

    public org.hl7.v3.PIXConsumerPRPAIN201309UVResponseType pixConsumerPRPAIN201309UV(org.hl7.v3.PIXConsumerPRPAIN201309UVSecuredRequestType body)
    {
        return new EntitySubjectDiscoveryImpl().pixConsumerPRPAIN201309UV(body, context);
    }

}
