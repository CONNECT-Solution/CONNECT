/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.auditquery;

import com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType;
import com.services.nhinc.schema.auditmessage.FindAuditEventsType;
import gov.hhs.fha.nhinc.common.nhinccommoninternalorch.FindAuditEventsRequestType;
import gov.hhs.fha.nhinc.nhincauditlogquery.NhincAuditLogQueryPortType;
import gov.hhs.fha.nhinc.nhincauditlogquery.NhincAuditLogQueryService;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractorHelper;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class AuditQueryImpl {

    private static Log log = LogFactory.getLog(AuditQueryImpl.class);
    private static final String SERVICE_NAME = "mockauditquery";

    public static FindAuditEventsResponseType auditQuery(FindAuditEventsType query, WebServiceContext context) {
        log.debug("Entering AuditQueryImpl.auditQuery");
        FindAuditEventsResponseType resp = new FindAuditEventsResponseType();
        FindAuditEventsRequestType request = new FindAuditEventsRequestType();

        request.setFindAuditEvents(query);
        request.setAssertion(SamlTokenExtractor.GetAssertion(context));

        String homeCommunityId = SamlTokenExtractorHelper.getHomeCommunityId();

        if (NullChecker.isNotNullish(homeCommunityId)) {
            NhincAuditLogQueryService service = new NhincAuditLogQueryService();
            NhincAuditLogQueryPortType port = service.getNhincAuditLogcQueryPortTypeBindingPort();
			gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, SamlTokenExtractorHelper.getEndpointURL(homeCommunityId, SERVICE_NAME));
            resp = port.nhincAuditLogQuery(request);
        } else {
            return null;
        }

        log.debug("Exiting AuditQueryImpl.auditQuery");
        return resp;
    }
}
