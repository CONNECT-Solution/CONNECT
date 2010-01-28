package gov.hhs.fha.nhinc.adapter.reidentification;

import javax.jws.WebService;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterReidentification", portName = "AdapterReidentificationBindingSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterreidentification.AdapterReidentificationPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterreidentification", wsdlLocation = "WEB-INF/wsdl/AdapterReidentification/AdapterReidentification.wsdl")
public class AdapterReidentification {

    public org.hl7.v3.PIXConsumerPRPAIN201310UVRequestType getRealIdentifier(org.hl7.v3.PIXConsumerPRPAIN201309UVRequestType getRealIdentifierRequest) {
        AdapterReidentificationImpl impl = new AdapterReidentificationImpl();
        return impl.getRealIdentifier(getRealIdentifierRequest);
    }

}
