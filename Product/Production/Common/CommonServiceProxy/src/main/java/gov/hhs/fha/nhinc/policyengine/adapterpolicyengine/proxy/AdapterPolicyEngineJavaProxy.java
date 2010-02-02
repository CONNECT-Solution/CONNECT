package gov.hhs.fha.nhinc.policyengine.adapterpolicyengine.proxy;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.policyengine.adapterpolicyengine.AdapterPolicyEngineImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * This is the concrete implementation for the Java based call to the
 * AdapterPolicyEngine.
 *
 * @author Les Westberg
 */
public class AdapterPolicyEngineJavaProxy implements AdapterPolicyEngineProxy
{
    private static Log log = LogFactory.getLog(AdapterPolicyEngineJavaProxy.class);


    /**
     * Given a request to check the access policy, this service will interface
     * with the Adapter Policy Engine to determine if access is to be granted or denied.
     *
     * @param checkPolicyRequest The request to check defined policy
     * @return The response which contains the access decision
     */
    public CheckPolicyResponseType checkPolicy(CheckPolicyRequestType checkPolicyRequest)
    {
        CheckPolicyResponseType oResponse = new CheckPolicyResponseType();

        AdapterPolicyEngineImpl oPolicyEngineImpl = new AdapterPolicyEngineImpl();

        try
        {
            oResponse = oPolicyEngineImpl.checkPolicy(checkPolicyRequest);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Error occurred calling AdapterPolicyEngineJavaProxy.checkPolicy.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new RuntimeException(sErrorMessage, e);
        }

        return oResponse;
    }


}
