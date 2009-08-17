package gov.hhs.nhinc.policyengine.adapterpolicyengine;

import gov.hhs.fha.nhinc.adapterpolicyengine.AdapterPolicyEnginePortType;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 *
 * @author Neil Webb
 */
@WebService(serviceName = "AdapterPolicyEngine", portName = "AdapterPolicyEnginePortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterpolicyengine.AdapterPolicyEnginePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpolicyengine", wsdlLocation = "META-INF/wsdl/AdapterPolicyEngine/AdapterPolicyEngine.wsdl")
@Stateless
public class AdapterPolicyEngine implements AdapterPolicyEnginePortType
{

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicy(gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType checkPolicyRequest)
    {
        return new AdapterPolicyEngineImpl().checkPolicy(checkPolicyRequest);
    }
    
}
