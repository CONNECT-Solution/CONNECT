/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.auditquery.entity;

import com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.FindAuditEventsRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.entityauditlogquerysaml.EntityAuditLogQuerySamlPortType;
import gov.hhs.fha.nhinc.entityauditlogquerysaml.EntityAuditLogQuerySamlService;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import javax.xml.ws.BindingProvider;

/**
 *
 * @author Jon Hoppesch
 */
public class EntityAuditQueryImpl {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(EntityAuditQueryImpl.class);
    private static EntityAuditLogQuerySamlService service = new EntityAuditLogQuerySamlService();

    public FindAuditEventsResponseType findAuditEvents(FindAuditEventsRequestType findAuditEventsRequest) {
        FindAuditEventsResponseType response = new FindAuditEventsResponseType();

        try
        {
            String url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ENTITY_AUDIT_QUERY_SECURED_SERVICE_NAME);

            EntityAuditLogQuerySamlPortType port = getPort(url);

            AssertionType assertIn = findAuditEventsRequest.getAssertion();
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.AUDIT_QUERY_ACTION);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            gov.hhs.fha.nhinc.common.nhinccommonentity.FindAuditEventsSecuredRequestType body = new gov.hhs.fha.nhinc.common.nhinccommonentity.FindAuditEventsSecuredRequestType();
            body.setFindAuditEvents(findAuditEventsRequest.getFindAuditEvents());
            body.setNhinTargetCommunities(findAuditEventsRequest.getNhinTargetCommunities());
            response = port.findAuditEvents(body);
        }
        catch (Exception ex)
        {
            log.error("Failed to send entity audit query from proxy EJB to secure interface: " + ex.getMessage(), ex);
        }

        return response;
    }

    private EntityAuditLogQuerySamlPortType getPort(String url) {
        EntityAuditLogQuerySamlPortType port = service.getEntityAuditLogQuerySamlPortTypeBindingPort();
        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        return port;
    }
}
