package gov.hhs.fha.nhinc.policyengine.adapterpep.proxy;

import gov.hhs.fha.nhinc.adapterpep.AdapterPEP;
import gov.hhs.fha.nhinc.adapterpep.AdapterPEPPortType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.policyengine.adapterpep.AdapterPEPException;
import java.util.StringTokenizer;
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
			
			
			int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
	int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        String exceptionText = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getExceptionText();
        javax.xml.ws.WebServiceException catchExp = null;
        if (retryCount > 0 && retryDelay > 0 && exceptionText != null && !exceptionText.equalsIgnoreCase("")) {
            int i = 1;
            while (i <= retryCount) {
                try {
                    checkPolicyResponse = pepPort.checkPolicy(checkPolicyRequest);
                    break;
                } catch (javax.xml.ws.WebServiceException e) {
                    catchExp = e;
                    int flag = 0;
                    StringTokenizer st = new StringTokenizer(exceptionText, ",");
                    while (st.hasMoreTokens()) {
                        if (e.getMessage().contains(st.nextToken())) {
                            flag = 1;
                        }
                    }
                    if (flag == 1) {
                        log.warn("Exception calling ... web service: " + e.getMessage());
                        System.out.println("retrying the connection for attempt [ " + i + " ] after [ " + retryDelay + " ] seconds");
                        log.info("retrying attempt [ " + i + " ] the connection after [ " + retryDelay + " ] seconds");
                        i++;
                        try {
                            Thread.sleep(retryDelay);
                        } catch (InterruptedException iEx) {
                            log.error("Thread Got Interrupted while waiting on AdapterPEP call :" + iEx);
                        } catch (IllegalArgumentException iaEx) {
                            log.error("Thread Got Interrupted while waiting on AdapterPEP call :" + iaEx);
                        }
                        retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                    } else {
                        log.error("Unable to call AdapterPEP Webservice due to  : " + e);
                        throw e;
                    }
                }
            }

            if (i > retryCount) {
                log.error("Unable to call AdapterPEP Webservice due to  : " + catchExp);
                throw catchExp;
            }

        } else {
            checkPolicyResponse = pepPort.checkPolicy(checkPolicyRequest);
        }
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
