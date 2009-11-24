package gov.hhs.nhinc.policyengine.adapterpolicyengine;

import gov.hhs.fha.nhinc.adapterpolicyenginesecured.AdapterPolicyEngineSecuredPortType;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

/**
 *
 *
 * @author Neil Webb
 */
@WebService(serviceName = "AdapterPolicyEngineSecured", portName = "AdapterPolicyEngineSecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterpolicyenginesecured.AdapterPolicyEngineSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpolicyenginesecured", wsdlLocation = "META-INF/wsdl/AdapterPolicyEngineSecured/AdapterPolicyEngineSecured.wsdl")
@Stateless
public class AdapterPolicyEngineSecured implements AdapterPolicyEngineSecuredPortType
{
    @Resource
    private WebServiceContext context;

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicy(gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestSecuredType body)
    {
        return new AdapterPolicyEngineSecuredImpl().checkPolicy(body, context);
    }
    
}
