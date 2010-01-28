package gov.hhs.fha.nhinc.adaptermpi;

import javax.jws.WebService;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterMpiService", portName = "AdapterMpiPort", endpointInterface = "gov.hhs.fha.nhinc.adaptermpi.AdapterMpiPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adaptermpi", wsdlLocation = "WEB-INF/wsdl/AdapterMpi/AdapterMpi.wsdl")
public class AdapterMpi {

    public org.hl7.v3.PRPAIN201306UV02 findCandidates(org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType findCandidatesRequest) {
        return new AdapterMpiProxyImpl().query(findCandidatesRequest);
    }

}
