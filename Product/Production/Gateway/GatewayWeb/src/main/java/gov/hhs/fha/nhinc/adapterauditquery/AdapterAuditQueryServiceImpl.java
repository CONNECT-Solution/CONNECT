/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adapterauditquery;

import com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType;
import gov.hhs.fha.nhinc.adapterauditlogquerysaml.AdapterAuditLogQuerySamlPortType;
import gov.hhs.fha.nhinc.adapterauditlogquerysaml.AdapterAuditLogQuerySamlService;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindAuditEventsRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import java.util.StringTokenizer;
import javax.xml.ws.BindingProvider;

/**
 *
 * @author Jon Hoppesch
 */
public class AdapterAuditQueryServiceImpl {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(AdapterAuditQueryServiceImpl.class);
    private static AdapterAuditLogQuerySamlService service = new AdapterAuditLogQuerySamlService();

    public FindAuditEventsResponseType findAuditEvents(FindAuditEventsRequestType findAuditEventsRequest) {
        FindAuditEventsResponseType results = new FindAuditEventsResponseType();
         String url = null;
         Map requestContext = null;
         com.services.nhinc.schema.auditmessage.FindAuditEventsType request = null;
        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_AUDIT_QUERY_SECURED_SERVICE_NAME);
            AssertionType assertIn = findAuditEventsRequest.getAssertion();
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.AUDIT_QUERY_ACTION);
            request = new com.services.nhinc.schema.auditmessage.FindAuditEventsType();

            if (NullChecker.isNotNullish(findAuditEventsRequest.getFindAuditEvents().getPatientId())) {
                request.setPatientId(findAuditEventsRequest.getFindAuditEvents().getPatientId());
            }

            if (NullChecker.isNotNullish(findAuditEventsRequest.getFindAuditEvents().getUserId())) {
                request.setUserId(findAuditEventsRequest.getFindAuditEvents().getUserId());
            }

            if (findAuditEventsRequest.getFindAuditEvents().getBeginDateTime() != null) {
                request.setBeginDateTime(findAuditEventsRequest.getFindAuditEvents().getBeginDateTime());
            }

            if (findAuditEventsRequest.getFindAuditEvents().getEndDateTime() != null) {
                request.setEndDateTime(findAuditEventsRequest.getFindAuditEvents().getEndDateTime());
            }
        } catch (Exception ex) {
            log.error("Error calling adapter audit query secured service: " + ex.getMessage(), ex);
        }
        AdapterAuditLogQuerySamlPortType port = getPort(url);
        ((BindingProvider) port).getRequestContext().putAll(requestContext);
				
        int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
		int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        String exceptionText = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getExceptionText();
        javax.xml.ws.WebServiceException catchExp = null;
        if (retryCount > 0 && retryDelay > 0 && exceptionText != null && !exceptionText.equalsIgnoreCase("")) {
            int i = 1;
            while (i <= retryCount) {
                try {
                    results = port.findAuditEvents(request);
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
                            log.error("Thread Got Interrupted while waiting on AdapterAuditLogQuerySamlService call :" + iEx);
                        } catch (IllegalArgumentException iaEx) {
                            log.error("Thread Got Interrupted while waiting on AdapterAuditLogQuerySamlService call :" + iaEx);
                        }
                        retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                    } else {
                        log.error("Unable to call AdapterAuditLogQuerySamlService Webservice due to  : " + e);
                        throw e;
                    }
                }
            }

            if (i > retryCount) {
                log.error("Unable to call AdapterAuditLogQuerySamlService Webservice due to  : " + catchExp);
                throw catchExp;
            }

        } else {
            results = port.findAuditEvents(request);
        }
        return results;
    }

    private AdapterAuditLogQuerySamlPortType getPort(String url) {
        AdapterAuditLogQuerySamlPortType port = service.getAdapterAuditLogQuerySamlPortTypeBindingPort();

        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);

        return port;
    }
}
