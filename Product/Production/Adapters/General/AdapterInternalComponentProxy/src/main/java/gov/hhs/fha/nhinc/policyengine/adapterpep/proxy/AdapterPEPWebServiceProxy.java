package gov.hhs.fha.nhinc.policyengine.adapterpep.proxy;

import gov.hhs.fha.nhinc.adapterpep.AdapterPEP;
import gov.hhs.fha.nhinc.adapterpep.AdapterPEPPortType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.policyengine.adapterpep.AdapterPEPException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This is the concrete implementation for the Web based call to the
 * AdapterPEP.
 */
public class AdapterPEPWebServiceProxy {

    private static Log log = LogFactory.getLog(AdapterPEPWebServiceProxy.class);
    private static AdapterPEP pepService = null;
    private static String ADAPTER_PEP_SERVICE_NAME = "adapterpep";
    private static String ADAPTER_PEP_DEFAULT_SERVICE_URL = "http://localhost:8080/NhinConnect/AdapterPEP";


    /**
     * Given a request to check the access policy, this service will interface
     * with the PDP to determine if access is to be granted or denied.
     * @param checkPolicyRequest The xacml request to check defined policy
     * @return The xacml response which contains the access decision
     */
    public CheckPolicyResponseType checkPolicy(CheckPolicyRequestType checkPolicyRequest) {

        CheckPolicyResponseType checkPolicyResponse = new CheckPolicyResponseType();

        try
        {
            AdapterPEPPortType pepPort = getAdapterPEPPort();
            checkPolicyResponse = pepPort.checkPolicy(checkPolicyRequest);
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

    /**
     * Return a handle to the AdapterPEP web service.
     *
     * @return The handle to the Adapter PEP port web service.
     */
    private AdapterPEPPortType getAdapterPEPPort()
        throws AdapterPEPException
    {
        AdapterPEPPortType pepPort = null;

        try
        {
            if (pepService == null)
            {
                pepService = new AdapterPEP();
            }

            pepPort = pepService.getAdapterPEPPortSoap11();

            // Get the real endpoint URL for this service.
            String endpointURL = ConnectionManagerCache.getLocalEndpointURLByServiceName(ADAPTER_PEP_SERVICE_NAME);

            if ((endpointURL == null) ||
                (endpointURL.length() <= 0))
            {
                endpointURL = ADAPTER_PEP_DEFAULT_SERVICE_URL;
                String message = "Failed to retrieve the Endpoint URL for service: '" +
                                       ADAPTER_PEP_SERVICE_NAME + "'.  " +
                                       "Setting this to: '" + endpointURL + "'";
                log.warn(message);
            }
			gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) pepPort, endpointURL);
        }
        catch (Exception ex)
        {
            String message = "Failed to retrieve a handle to the Adapter PIP web service.  Error: " +
                                   ex.getMessage();
            log.error(message, ex);
            throw new AdapterPEPException(message, ex);
        }

        return pepPort;
    }
}
