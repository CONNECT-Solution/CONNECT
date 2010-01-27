package gov.hhs.fha.nhinc.adapter.patientdiscovery;

import javax.jws.WebService;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterPatientDiscoverySecured", portName = "AdapterPatientDiscoverySecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterpatientdiscoverysecured.AdapterPatientDiscoverySecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpatientdiscoverysecured", wsdlLocation = "WEB-INF/wsdl/AdapterPatientDiscoverySecured/AdapterPatientDiscoverySecured.wsdl")
public class AdapterPatientDiscoverySecured {

    public org.hl7.v3.PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType respondingGatewayPRPAIN201305UV02Request) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
