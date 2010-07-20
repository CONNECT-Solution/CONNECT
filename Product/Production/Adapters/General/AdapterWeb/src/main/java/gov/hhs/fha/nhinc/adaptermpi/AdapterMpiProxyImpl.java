/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adaptermpi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201306UV02;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import java.util.StringTokenizer;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;

/**
 *
 * @author Jon Hoppesch
 */

public class AdapterMpiProxyImpl {
   private static Log log = LogFactory.getLog(AdapterMpiProxyImpl.class);

   private static AdapterMpiSecuredService service = new gov.hhs.fha.nhinc.adaptermpi.AdapterMpiSecuredService();

   /**
    * Send the patient query request to the actual MPI that is implemented
    *
    * @param
    * @return
    */
   public  PRPAIN201306UV02 query(RespondingGatewayPRPAIN201305UV02RequestType findCandidatesRequest)
   {
       log.debug("Entering AdapterMpiProxyImpl.query method...");
       String url = getURL();
       AdapterMpiSecuredPortType port = getPort(url);
       SamlTokenCreator tokenCreator = new SamlTokenCreator();
       Map requestContext = tokenCreator.CreateRequestContext(findCandidatesRequest.getAssertion(), url, NhincConstants.ADAPTER_MPI_ACTION);
       ((BindingProvider) port).getRequestContext().putAll(requestContext);
       log.debug("Calling Secured Endpoint");
	   PRPAIN201306UV02 result = null;
		int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
		int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        String exceptionText = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getExceptionText();
        javax.xml.ws.WebServiceException catchExp = null;
        if (retryCount > 0 && retryDelay > 0 && exceptionText != null && !exceptionText.equalsIgnoreCase("")) {
            int i = 1;
            while (i <= retryCount) {
                try {
                    result = port.findCandidates(findCandidatesRequest.getPRPAIN201305UV02());
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
                            log.error("Thread Got Interrupted while waiting on AdapterMpiSecuredService call :" + iEx);
                        } catch (IllegalArgumentException iaEx) {
                            log.error("Thread Got Interrupted while waiting on AdapterMpiSecuredService call :" + iaEx);
                        }
                        retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                    } else {
                        log.error("Unable to call AdapterMpiSecuredService Webservice due to  : " + e);
                        throw e;
                    }
                }
            }

            if (i > retryCount) {
                log.error("Unable to call AdapterMpiSecuredService Webservice due to  : " + catchExp);
                throw catchExp;
            }

        } else {
            result = port.findCandidates(findCandidatesRequest.getPRPAIN201305UV02());
        }
       return result;
   }
    private  String getURL()
    {
        String url = "";
        try
        {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_MPI_PROXY_SECURED_SERVICE_NAME);
        }
        catch(Exception ex)
        {
            log.error(ex.getMessage(), ex);
        }
        return url;
    }
    private  AdapterMpiSecuredPortType getPort(String url)
    {
        AdapterMpiSecuredPortType port = service.getAdapterMpiSecuredPortType();
        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        return port;
    }
}