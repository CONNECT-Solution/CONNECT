package gov.hhs.fha.nhinc.policyengine.adapterpepservice;

import javax.jws.WebService;
//import javax.xml.ws.WebServiceRef;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterPEP", portName = "AdapterPEPPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterpep.AdapterPEPPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpep", wsdlLocation = "WEB-INF/wsdl/AdapterPEPService/AdapterPEP.wsdl")
public class AdapterPEPService {
   // @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/AdapterPEPService/AdapterPEP.wsdl")
    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicy(gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType checkPolicyRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
