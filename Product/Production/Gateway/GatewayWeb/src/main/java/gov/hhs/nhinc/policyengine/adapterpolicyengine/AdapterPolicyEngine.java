package gov.hhs.nhinc.policyengine.adapterpolicyengine;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterPolicyEngine", portName = "AdapterPolicyEnginePortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterpolicyengine.AdapterPolicyEnginePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpolicyengine", wsdlLocation = "WEB-INF/wsdl/AdapterPolicyEngine/AdapterPolicyEngine.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterPolicyEngine {

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicy(gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType checkPolicyRequest)
    {
        return new AdapterPolicyEngineImpl().checkPolicy(checkPolicyRequest);
    }

}
