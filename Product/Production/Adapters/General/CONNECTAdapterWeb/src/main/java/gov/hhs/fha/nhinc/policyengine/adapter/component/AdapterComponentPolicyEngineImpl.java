package gov.hhs.fha.nhinc.policyengine.adapter.component;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.policyengine.adapterpolicyengineorchestrator.AdapterPolicyEngineOrchestratorImpl;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class AdapterComponentPolicyEngineImpl
{

    private Log log = null;

    public AdapterComponentPolicyEngineImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    protected AdapterPolicyEngineOrchestratorImpl getAdapterPolicyEngineOrchestratorImpl()
    {
        return new AdapterPolicyEngineOrchestratorImpl();
    }

    protected void loadAssertion(AssertionType assertion, WebServiceContext wsContext) throws Exception
    {
        // TODO: Extract message ID from the web service context for logging.
    }

    /**
     * Given a request to check the access policy, this service will interface
     * with the Adapter PEP to determine if access is to be granted or denied.
     * @param checkPolicyRequest The request to check defined policy
     * @return The response which contains the access decision
     */
    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicy(gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType checkPolicyRequest, WebServiceContext context)
    {
        CheckPolicyResponseType checkPolicyResp = null;

        AdapterPolicyEngineOrchestratorImpl oOrchestrator = getAdapterPolicyEngineOrchestratorImpl();
        try
        {
            AssertionType assertion = checkPolicyRequest.getAssertion();
            loadAssertion(assertion, context);

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
