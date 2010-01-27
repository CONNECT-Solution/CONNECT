package gov.hhs.fha.nhinc.adaptermpi;

import javax.jws.WebService;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterMpiSecuredService", portName = "AdapterMpiSecuredPortType", endpointInterface = "gov.hhs.fha.nhinc.adaptermpi.AdapterMpiSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adaptermpi", wsdlLocation = "WEB-INF/wsdl/AdapterMpiSecured/AdapterMpiSecured.wsdl")
public class AdapterMpiSecured {

    public org.hl7.v3.PRPAIN201306UV02 findCandidates(org.hl7.v3.PRPAIN201305UV02 findCandidatesRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
