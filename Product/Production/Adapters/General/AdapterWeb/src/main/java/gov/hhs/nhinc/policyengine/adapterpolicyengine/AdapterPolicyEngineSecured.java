package gov.hhs.nhinc.policyengine.adapterpolicyengine;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
//import javax.xml.ws.WebServiceRef;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterPolicyEngineSecured", portName = "AdapterPolicyEngineSecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterpolicyenginesecured.AdapterPolicyEngineSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpolicyenginesecured", wsdlLocation = "WEB-INF/wsdl/AdapterPolicyEngineSecured/AdapterPolicyEngineSecured.wsdl")
public class AdapterPolicyEngineSecured {

    @Resource
    private WebServiceContext context;
    
    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicy(gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestSecuredType body) {
        return new AdapterPolicyEngineSecuredImpl().checkPolicy(body, context);
    }

}
