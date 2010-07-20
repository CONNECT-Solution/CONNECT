package gov.hhs.fha.nhinc.adapterauditquery.proxy;

import com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType;
import gov.hhs.fha.nhinc.adapterauditlogquery.AdapterAuditLogQuery;
import gov.hhs.fha.nhinc.adapterauditlogquery.AdapterAuditLogQueryPortType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindAuditEventsRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jon Hoppesch
 */
public class AdapterAuditQueryWebServiceProxy implements AdapterAuditQueryProxy {

    private static Log log = LogFactory.getLog(AdapterAuditQueryWebServiceProxy.class);
    static AdapterAuditLogQuery adapterService = new AdapterAuditLogQuery();

    public FindAuditEventsResponseType auditQuery(FindAuditEventsRequestType request) {
        String url = null;

        try {
            log.debug("NhincConstants.AUDIT_LOG_ADAPTER_SERVICE_NAME: " + NhincConstants.AUDIT_LOG_ADAPTER_SERVICE_NAME);
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.AUDIT_LOG_ADAPTER_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.AUDIT_LOG_ADAPTER_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }

        AdapterAuditLogQueryPortType port = getAdapterPort(url);
        FindAuditEventsResponseType resp = null;
				
		int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
		int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        String exceptionText = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getExceptionText();
        javax.xml.ws.WebServiceException catchExp = null;
        if (retryCount > 0 && retryDelay > 0 && exceptionText != null && !exceptionText.equalsIgnoreCase("")) {
            int i = 1;
            while (i <= retryCount) {
                try {
                    resp = port.findAuditEvents(request);
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
                            log.error("Thread Got Interrupted while waiting on AdapterAuditLogQuery call :" + iEx);
                        } catch (IllegalArgumentException iaEx) {
                            log.error("Thread Got Interrupted while waiting on AdapterAuditLogQuery call :" + iaEx);
                        }
                        retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                    } else {
                        log.error("Unable to call AdapterAuditLogQuery Webservice due to  : " + e);
                        throw e;
                    }
                }
            }

            if (i > retryCount) {
                log.error("Unable to call AdapterAuditLogQuery Webservice due to  : " + catchExp);
                throw catchExp;
            }

        } else {
            resp = port.findAuditEvents(request);
        }

        return resp;
    }

    private AdapterAuditLogQueryPortType getAdapterPort(String url) {
        AdapterAuditLogQueryPortType port = adapterService.getAdapterAuditLogQueryPortSoap11();

        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);

        return port;
    }
}
