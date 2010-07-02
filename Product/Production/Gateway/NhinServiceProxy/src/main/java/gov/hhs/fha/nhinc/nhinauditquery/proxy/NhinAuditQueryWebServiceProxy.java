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

            List<AuditMessageType> auditMsgList = port.findAuditEvents(patientId, userId, beginDateTime, endDateTime);

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
