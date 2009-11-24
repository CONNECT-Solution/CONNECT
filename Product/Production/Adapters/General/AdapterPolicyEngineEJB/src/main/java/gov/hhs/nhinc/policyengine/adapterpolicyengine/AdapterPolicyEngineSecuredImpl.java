package gov.hhs.nhinc.policyengine.adapterpolicyengine;

import javax.xml.ws.WebServiceContext;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.policyengine.adapterpolicyengine.AdapterPolicyEngineImpl;

/**
 *
 *
 * @author Neil Webb
 */
public class AdapterPolicyEngineSecuredImpl
{
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(AdapterPolicyEngineSecuredImpl.class);
    
    public CheckPolicyResponseType checkPolicy(gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestSecuredType body, WebServiceContext context)
    {
        // Collect assertion
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
        CheckPolicyResponseType checkPolicyResp = null;

        AdapterPolicyEngineImpl oPolicyEngine = new AdapterPolicyEngineImpl();
        try
        {
            gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType checkPolicyRequest = new gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType();
            checkPolicyRequest.setAssertion(assertion);
            checkPolicyRequest.setRequest(body.getRequest());
            checkPolicyResp = oPolicyEngine.checkPolicy(checkPolicyRequest);
        }
        catch (Exception e)
        {
            String sMessage = "Error occurred calling AdapterPolicyEngineImpl.checkPolicy.  Error: " +
                    e.getMessage();
            log.error(sMessage, e);
            throw new RuntimeException(sMessage, e);
        }
        return checkPolicyResp;
    }
}
