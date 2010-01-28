package gov.hhs.fha.nhinc.adapter.patientdiscovery;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterPatientDiscoverySecured", portName = "AdapterPatientDiscoverySecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterpatientdiscoverysecured.AdapterPatientDiscoverySecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpatientdiscoverysecured", wsdlLocation = "WEB-INF/wsdl/AdapterPatientDiscoverySecured/AdapterPatientDiscoverySecured.wsdl")
public class AdapterPatientDiscoverySecured {

    @Resource
    private WebServiceContext context;
    
    public org.hl7.v3.PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType respondingGatewayPRPAIN201305UV02Request) {
        return new AdapterPatientDiscoverySecuredImpl().respondingGatewayPRPAIN201305UV02(respondingGatewayPRPAIN201305UV02Request, context);
    }

}
