package gov.hhs.fha.nhinc.policyengine.adapterpep.proxy;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.policyengine.adapterpep.AdapterPEPImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This is the concrete implementation for the Java based call to the
 * AdapterPEP.
 */
public class AdapterPEPJavaProxy implements AdapterPEPProxy {

    private static Log log = LogFactory.getLog(AdapterPEPJavaProxy.class);

    /**
     * Given a request to check the access policy, this service will interface
     * with the PDP to determine if access is to be granted or denied.
     * @param checkPolicyRequest The xacml request to check defined policy
     * @return The xacml response which contains the access decision
     */
    public CheckPolicyResponseType checkPolicy(CheckPolicyRequestType checkPolicyRequest) {

        CheckPolicyResponseType checkPolicyResponse = new CheckPolicyResponseType();

        AdapterPEPImpl pepImpl = new AdapterPEPImpl();

        try
        {
            checkPolicyResponse = pepImpl.checkPolicy(checkPolicyRequest);
        }
        catch (Exception ex)
        {
            String message = "Error occurred calling AdapterPEPJavaProxy.checkPolicy.  Error: " +
                                   ex.getMessage();
            log.error(message, ex);
            throw new RuntimeException(message, ex);
        }

        return checkPolicyResponse;
    }
}
