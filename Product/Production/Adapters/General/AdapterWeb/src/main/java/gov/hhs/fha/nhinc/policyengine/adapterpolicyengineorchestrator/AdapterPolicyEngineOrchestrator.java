package gov.hhs.fha.nhinc.policyengine.adapterpolicyengineorchestrator;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import javax.jws.WebService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterPolicyEngineOrchestrator", portName = "AdapterPolicyEngineOrchestratorPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterpolicyengineorchestrator.AdapterPolicyEngineOrchestratorPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpolicyengineorchestrator", wsdlLocation = "WEB-INF/wsdl/AdapterPolicyEngineOrchestrator/AdapterPolicyEngineOrchestrator.wsdl")
public class AdapterPolicyEngineOrchestrator {
    private static Log log = LogFactory.getLog(AdapterPolicyEngineOrchestrator.class);

    /**
     * Given a request to check the access policy, this service will interface
     * with the Adapter PEP to determine if access is to be granted or denied.
     * @param checkPolicyRequest The request to check defined policy
     * @return The response which contains the access decision
     */
    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicy(gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType checkPolicyRequest) {
        CheckPolicyResponseType checkPolicyResp = new CheckPolicyResponseType();

        AdapterPolicyEngineOrchestratorImpl oOrchestrator = new AdapterPolicyEngineOrchestratorImpl();
        try
        {
            checkPolicyResp = oOrchestrator.checkPolicy(checkPolicyRequest);
        }
        catch (Exception e)
        {
            String sMessage = "Error occurred calling AdapterPolicyEngineOrchestratorLib.checkPolicy.  Error: " +
                    e.getMessage();
            log.error(sMessage, e);
            throw new RuntimeException(sMessage, e);
        }
        return checkPolicyResp;
    }

}
