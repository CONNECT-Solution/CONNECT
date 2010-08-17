package gov.hhs.fha.nhinc.policyengine.adapter;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 *
 * @author Neil Webb
 */
public class AdapterPolicyEngineImpl
{
    private Log log = null;

    public AdapterPolicyEngineImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicy(gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType checkPolicyRequest, WebServiceContext context)
    {
        log.debug("Begin AdapterPolicyEngineImpl.checkPolicy (unsecure)");
        CheckPolicyResponseType result = null;
        if(checkPolicyRequest != null)
        {
            result = checkPolicy(checkPolicyRequest.getRequest(), checkPolicyRequest.getAssertion());
        }
        else
        {
            log.error("AdapterPolicyEngineImpl.checkPolicy (unsecure) - request was null");
        }
        log.debug("End AdapterPolicyEngineImpl.checkPolicy (unsecure)");
        return result;
    }

    public CheckPolicyResponseType checkPolicy(gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestSecuredType body, WebServiceContext context)
    {
        log.debug("Begin AdapterPolicyEngineImpl.checkPolicy (secure)");
        CheckPolicyResponseType response = null;
        if(body != null)
        {
            AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
            response = checkPolicy(body.getRequest(), assertion);
        }
        else
        {
            log.error("AdapterPolicyEngineImpl.checkPolicy (secure) - request was null");
        }
        log.debug("End AdapterPolicyEngineImpl.checkPolicy (secure)");
        return response;
    }

    private CheckPolicyResponseType checkPolicy(RequestType request, AssertionType assertion)
    {
        CheckPolicyResponseType checkPolicyResp = null;

        gov.hhs.fha.nhinc.policyengine.adapter.AdapterPolicyEngineProcessorImpl oPolicyEngine = new gov.hhs.fha.nhinc.policyengine.adapter.AdapterPolicyEngineProcessorImpl();
        try
        {
            gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType checkPolicyRequest = new gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType();
            checkPolicyRequest.setAssertion(assertion);
            checkPolicyRequest.setRequest(request);
            checkPolicyResp = oPolicyEngine.checkPolicy(checkPolicyRequest, assertion);
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
