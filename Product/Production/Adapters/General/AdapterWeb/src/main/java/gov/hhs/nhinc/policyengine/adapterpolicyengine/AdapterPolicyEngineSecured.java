package gov.hhs.nhinc.policyengine.adapterpolicyengine;

import javax.jws.WebService;
//import javax.xml.ws.WebServiceRef;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterPolicyEngineSecured", portName = "AdapterPolicyEngineSecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterpolicyenginesecured.AdapterPolicyEngineSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpolicyenginesecured", wsdlLocation = "WEB-INF/wsdl/AdapterPolicyEngineSecured/AdapterPolicyEngineSecured.wsdl")
public class AdapterPolicyEngineSecured {
    //@WebServiceRef(wsdlLocation = "WEB-INF/wsdl/AdapterPolicyEngineSecured/AdapterPolicyEngineSecured.wsdl")
    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicy(gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestSecuredType body) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
