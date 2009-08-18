/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapterauditquery;

import com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType;
import gov.hhs.fha.nhinc.adapterauditquery.proxy.AdapterAuditQueryProxy;
import gov.hhs.fha.nhinc.adapterauditquery.proxy.AdapterAuditQueryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinccomponentauditrepository.AuditRepositoryManagerPortType;
import gov.hhs.fha.nhinc.nhinccomponentauditrepository.AuditRepositoryManagerService;
import javax.xml.ws.BindingProvider;

/**
 * This class performs the actual work of class methods to query the audit log.
 *
 * @author Clark Shaw
 */
public class AdapterAuditQueryImpl
{
    private static Log log = LogFactory.getLog(AdapterAuditQueryImpl.class);
    private static AuditRepositoryManagerService auditRepositoryManagerservice = new AuditRepositoryManagerService();

    /**
     * This method will perform an audit log query to the local audit repository.  A list of audit
     * log records will be returned to the calling community that match the search criteria.
     *
     * @param query The audit log query search criteria
     * @return A list of Audit Log records that match the specified criteria
     */
    public FindAuditEventsResponseType queryAdapter(gov.hhs.fha.nhinc.common.nhinccommonadapter.FindAuditEventsRequestType query)
    {
        log.debug("Entering AdapterAuditQueryImpl.queryAdapter method...");

        log.debug("incomming adapter audit query request: " + query.toString());

        FindAuditEventsResponseType response = new FindAuditEventsResponseType();

        // Audit the Audit Log Query Request Message received on the Adapter Interface
        AcknowledgementType ack = audit(query, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);

        // Set up the audit query request message for the nhinc audit repository manager
        gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsRequestType findAuditEventsRequest =
                new gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsRequestType();
        findAuditEventsRequest.setAssertion(query.getAssertion());
        findAuditEventsRequest.setFindAuditEvents(query.getFindAuditEvents());

        //call the nhinc auditmangager for now.
        String url = null;
        gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType nhincResponse = null;

        try
        {
            log.debug("NhincConstants.AUDIT_LOG_ADAPTER_SERVICE_NAME: " + NhincConstants.AUDIT_LOG_ADAPTER_SERVICE_MANAGER_NAME);
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.AUDIT_LOG_ADAPTER_SERVICE_MANAGER_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.AUDIT_LOG_ADAPTER_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }

        AuditRepositoryManagerPortType port = getRepositoryMangerPort(url);
        nhincResponse = port.queryAuditEvents(findAuditEventsRequest);
        response = nhincResponse.getFindAuditEventResponse();

        log.debug("outgoing adapter audit query response: " + response);

        log.debug("Exiting AdapterAuditQueryImpl.queryAdapter method...");
        return response;
    }

    private AcknowledgementType audit(gov.hhs.fha.nhinc.common.nhinccommonadapter.FindAuditEventsRequestType auditMsg, String direction, String _interface)
    {
        AcknowledgementType ack = new AcknowledgementType();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

        // Need to resolve the namespace issues here because this type is defined in multiple schemas
        gov.hhs.fha.nhinc.common.auditlog.FindAuditEventsMessageType message = new gov.hhs.fha.nhinc.common.auditlog.FindAuditEventsMessageType();
        message.setAssertion(auditMsg.getAssertion());
        message.setFindAuditEvents(auditMsg.getFindAuditEvents());

        LogEventRequestType auditLogMsg = auditLogger.logFindAuditEvents(message, direction, _interface);

        if (auditLogMsg != null) {
            AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
            AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
            ack = proxy.auditLog(auditLogMsg);
        }

        return ack;
    }

    private AuditRepositoryManagerPortType getRepositoryMangerPort(String url)
    {
        AuditRepositoryManagerPortType port = auditRepositoryManagerservice.getAuditRepositoryManagerPort();
        log.info("Setting endpoint address to Nhin Audit Repository Manager Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        return port;
    }
}
