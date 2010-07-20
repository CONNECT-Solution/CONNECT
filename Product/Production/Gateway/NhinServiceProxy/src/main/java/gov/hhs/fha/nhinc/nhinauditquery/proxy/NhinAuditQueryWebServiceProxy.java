/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.nhinauditquery.proxy;

import com.nhin.services.AuditLogQuery;
import com.nhin.services.FindAuditEvents;
import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.FindAuditEventsRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jon Hoppesch
 */
public class NhinAuditQueryWebServiceProxy implements NhinAuditQueryProxy {

    private static Log log = LogFactory.getLog(NhinAuditQueryWebServiceProxy.class);
    static FindAuditEvents nhinService = new FindAuditEvents();

    public FindAuditEventsResponseType auditQuery(FindAuditEventsRequestType request) {
        String url = null;
        FindAuditEventsResponseType results = new FindAuditEventsResponseType();

        if (request.getNhinTargetSystem() != null) {
            try {
                url = ConnectionManagerCache.getEndpontURLFromNhinTarget(request.getNhinTargetSystem(), NhincConstants.AUDIT_QUERY_SERVICE_NAME);
            } catch (ConnectionManagerException ex) {
                log.error("Error: Failed to retrieve url for service: " + NhincConstants.AUDIT_QUERY_SERVICE_NAME);
                log.error(ex.getMessage());
            }
        } else {
            log.error("Target system passed into the proxy is null");
        }

        if (NullChecker.isNotNullish(url)) {
            AuditLogQuery port = getPort(url);

            AssertionType assertIn = request.getAssertion();
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.AUDIT_QUERY_ACTION);

            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            // Set up the parameters to the Nhin Audit Query call
            String patientId = null;
            if (NullChecker.isNotNullish(request.getFindAuditEvents().getPatientId())) {
                patientId = request.getFindAuditEvents().getPatientId();
            }

            String userId = null;
            if (NullChecker.isNotNullish(request.getFindAuditEvents().getUserId())) {
                userId = request.getFindAuditEvents().getUserId();
            }

            XMLGregorianCalendar beginDateTime = null;
            if (request.getFindAuditEvents().getBeginDateTime() != null) {
                beginDateTime = request.getFindAuditEvents().getBeginDateTime();
            }

            XMLGregorianCalendar endDateTime = null;
            if (request.getFindAuditEvents().getEndDateTime() != null) {
                endDateTime = request.getFindAuditEvents().getEndDateTime();
            }

            List<AuditMessageType> auditMsgList = null;
			
		int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
		int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        String exceptionText = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getExceptionText();
        javax.xml.ws.WebServiceException catchExp = null;
        if (retryCount > 0 && retryDelay > 0 && exceptionText != null && !exceptionText.equalsIgnoreCase("")) {
            int i = 1;
            while (i <= retryCount) {
                try {
                    auditMsgList = port.findAuditEvents(patientId, userId, beginDateTime, endDateTime);
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
                            log.error("Thread Got Interrupted while waiting on FindAuditEvents call :" + iEx);
                        } catch (IllegalArgumentException iaEx) {
                            log.error("Thread Got Interrupted while waiting on FindAuditEvents call :" + iaEx);
                        }
                        retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                    } else {
                        log.error("Unable to call FindAuditEvents Webservice due to  : " + e);
                        throw e;
                    }
                }
            }

            if (i > retryCount) {
                log.error("Unable to call FindAuditEvents Webservice due to  : " + catchExp);
                throw catchExp;
            }

        } else {
            auditMsgList = port.findAuditEvents(patientId, userId, beginDateTime, endDateTime);
        }
		    // Set up the return value
            results.getFindAuditEventsReturn().addAll(auditMsgList);

        } else {
            log.error("The URL for service: " + NhincConstants.AUDIT_QUERY_SERVICE_NAME + " is null");
        }

        return results;
    }

    private AuditLogQuery getPort(String url) {
        AuditLogQuery port = nhinService.getAuditLogQuery();
        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        return port;
    }
}
