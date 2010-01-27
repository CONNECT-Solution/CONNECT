package gov.hhs.fha.nhinc.policyengine.adapterpolicyengineorchestrator;

import javax.jws.WebService;
//import javax.xml.ws.WebServiceRef;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterPolicyEngineOrchestrator", portName = "AdapterPolicyEngineOrchestratorPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterpolicyengineorchestrator.AdapterPolicyEngineOrchestratorPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpolicyengineorchestrator", wsdlLocation = "WEB-INF/wsdl/AdapterPolicyEngineOrchestrator/AdapterPolicyEngineOrchestrator.wsdl")
public class AdapterPolicyEngineOrchestrator {
    //@WebServiceRef(wsdlLocation = "WEB-INF/wsdl/AdapterPolicyEngineOrchestrator/AdapterPolicyEngineOrchestrator.wsdl")
    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicy(gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType checkPolicyRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
