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

        try
        {
            String url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_AUDIT_QUERY_SECURED_SERVICE_NAME);
            AdapterAuditLogQuerySamlPortType port = getPort(url);

            AssertionType assertIn = findAuditEventsRequest.getAssertion();
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.AUDIT_QUERY_ACTION);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            com.services.nhinc.schema.auditmessage.FindAuditEventsType request = new com.services.nhinc.schema.auditmessage.FindAuditEventsType();

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

            results = port.findAuditEvents(request);
        }
        catch (Exception ex)
        {
            log.error("Error calling adapter audit query secured service: " + ex.getMessage(), ex);
        }

        return results;
    }

    private AdapterAuditLogQuerySamlPortType  getPort(String url) {
        AdapterAuditLogQuerySamlPortType  port = service.getAdapterAuditLogQuerySamlPortTypeBindingPort();

        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);

        return port;
    }
}
