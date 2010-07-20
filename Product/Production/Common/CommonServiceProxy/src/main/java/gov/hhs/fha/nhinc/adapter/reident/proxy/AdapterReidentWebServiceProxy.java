/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adapter.reident.proxy;

import gov.hhs.fha.nhinc.adapterreidentification.AdapterReidentification;
import gov.hhs.fha.nhinc.adapterreidentification.AdapterReidentificationPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PIXConsumerPRPAIN201309UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201310UVRequestType;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.PRPAIN201310UV02;

/**
 *
 * @author jhoppesc
 */
public class AdapterReidentWebServiceProxy implements AdapterReidentProxy {

    private static Log log = LogFactory.getLog(AdapterReidentWebServiceProxy.class);
    static AdapterReidentification service = new AdapterReidentification();

    public PRPAIN201310UV02 getRealIdentifier(PRPAIN201309UV02 request, AssertionType assertion, NhinTargetCommunitiesType target) {
        String url = null;
        PRPAIN201310UV02 result = new PRPAIN201310UV02();

        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.SUBJECT_DISCOVERY_REIDENT_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.SUBJECT_DISCOVERY_REIDENT_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }

        if (NullChecker.isNotNullish(url)) {
            AdapterReidentificationPortType port = getPort(url);

            PIXConsumerPRPAIN201309UVRequestType reidentRequest = new PIXConsumerPRPAIN201309UVRequestType();
            reidentRequest.setAssertion(assertion);
            reidentRequest.setNhinTargetCommunities(target);
            reidentRequest.setPRPAIN201309UV02(request);

            PIXConsumerPRPAIN201310UVRequestType reidentResp = null;
			
			int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
	int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        String exceptionText = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getExceptionText();
        javax.xml.ws.WebServiceException catchExp = null;
        if (retryCount > 0 && retryDelay > 0 && exceptionText != null && !exceptionText.equalsIgnoreCase("")) {
            int i = 1;
            while (i <= retryCount) {
                try {
                    reidentResp = port.getRealIdentifier(reidentRequest);
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
                            log.error("Thread Got Interrupted while waiting on AdapterReidentification call :" + iEx);
                        } catch (IllegalArgumentException iaEx) {
                            log.error("Thread Got Interrupted while waiting on AdapterReidentification call :" + iaEx);
                        }
                        retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                    } else {
                        log.error("Unable to call AdapterReidentification Webservice due to  : " + e);
                        throw e;
                    }
                }
            }

            if (i > retryCount) {
                log.error("Unable to call AdapterReidentification Webservice due to  : " + catchExp);
                throw catchExp;
            }

        } else {
            reidentResp = port.getRealIdentifier(reidentRequest);
        }
			
            if (reidentResp != null &&
                    reidentResp.getPRPAIN201310UV02() != null) {
					result = reidentResp.getPRPAIN201310UV02();
            }
        }

        return result;
    }

    private AdapterReidentificationPortType getPort(String url) {
        AdapterReidentificationPortType port = service.getAdapterReidentificationBindingSoap11();
        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        return port;
    }
}
