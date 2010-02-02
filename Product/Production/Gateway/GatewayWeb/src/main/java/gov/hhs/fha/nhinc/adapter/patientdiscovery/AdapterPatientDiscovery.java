package gov.hhs.fha.nhinc.adapter.patientdiscovery;

import javax.jws.WebService;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "AdapterPatientDiscovery", portName = "AdapterPatientDiscoveryPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterpatientdiscovery.AdapterPatientDiscoveryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpatientdiscovery", wsdlLocation = "WEB-INF/wsdl/AdapterPatientDiscovery/AdapterPatientDiscovery.wsdl")
public class AdapterPatientDiscovery
{

    public org.hl7.v3.PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType respondingGatewayPRPAIN201305UV02Request)
    {
        return new AdapterPatientDiscoveryImpl().respondingGatewayPRPAIN201305UV02(respondingGatewayPRPAIN201305UV02Request);
    }

}
