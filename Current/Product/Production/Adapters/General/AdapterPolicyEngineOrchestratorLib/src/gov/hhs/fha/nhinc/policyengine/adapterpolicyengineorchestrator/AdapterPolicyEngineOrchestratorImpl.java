package gov.hhs.fha.nhinc.policyengine.adapterpolicyengineorchestrator;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.policyengine.adapterpep.proxy.AdapterPEPProxy;
import gov.hhs.fha.nhinc.policyengine.adapterpep.proxy.AdapterPEPProxyObjectFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is the Java implementation of the AdapterPolicyEngineOrchestrator.   It
 * is orchestrates a call to the policy engine by passing the information to the PEP.
 * 
 * @author Les Westberg
 */
public class AdapterPolicyEngineOrchestratorImpl
{

    private static Log log = LogFactory.getLog(AdapterPolicyEngineOrchestratorImpl.class);

    /**
     * Given a request to check the access policy, this service will interface
     * with the Adapter PEP to determine if access is to be granted or denied.
     * @param checkPolicyRequest The request to check defined policy
     * @return The response which contains the access decision
     */
    public CheckPolicyResponseType checkPolicy(CheckPolicyRequestType checkPolicyRequest)
    {
        CheckPolicyResponseType checkPolicyResp = new CheckPolicyResponseType();

        AdapterPEPProxyObjectFactory factory = new AdapterPEPProxyObjectFactory();
        AdapterPEPProxy adapterPEPProxy = factory.getAdapterPEPProxy();
        log.debug("AdapterPEP Proxy selected: " + adapterPEPProxy.getClass());

        try
        {
            checkPolicyResp = adapterPEPProxy.checkPolicy(checkPolicyRequest);
        }
        catch (Exception ex)
        {
            String message = "Error occurred calling AdapterPEPImpl.checkPolicy.  Error: " +
                ex.getMessage();
            log.error(message, ex);
            throw new RuntimeException(message, ex);
        }
        return checkPolicyResp;
    }
}
