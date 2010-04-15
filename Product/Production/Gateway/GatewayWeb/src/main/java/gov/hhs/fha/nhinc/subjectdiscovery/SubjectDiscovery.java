package gov.hhs.fha.nhinc.subjectdiscovery;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "PIXConsumer_Service", portName = "PIXConsumer_Port_Soap", endpointInterface = "ihe.iti.pixv3._2007.PIXConsumerPortType", targetNamespace = "urn:ihe:iti:pixv3:2007", wsdlLocation = "WEB-INF/wsdl/SubjectDiscovery/NhinSubjectDiscovery.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class SubjectDiscovery
{
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201301UV(org.hl7.v3.PRPAIN201301UV02 body)
    {
        throw new UnsupportedOperationException("Not implemented.");
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201302UV(org.hl7.v3.PRPAIN201302UV02 body)
    {
        throw new UnsupportedOperationException("Not implemented.");
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201304UV(org.hl7.v3.PRPAIN201304UV02 body)
    {
        throw new UnsupportedOperationException("Not implemented.");
    }

    public org.hl7.v3.PRPAIN201310UV02 pixConsumerPRPAIN201309UV(org.hl7.v3.PRPAIN201309UV02 body)
    {
        return (new SubjectDiscoveryImpl().pixConsumerPRPAIN201309UV(body, context));
    }

}
