/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapterauditquery;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinccomponentauditrepository.AuditRepositoryManagerPortType;
import gov.hhs.fha.nhinc.nhinccomponentauditrepository.AuditRepositoryManagerService;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceContext;

/**
 * This class performs the actual work of class methods to query the audit log.
 *
 * @author Clark Shaw
 */
public class AdapterSecuredAuditLogQueryImpl
{
    private static Log log = LogFactory.getLog(AdapterSecuredAuditLogQueryImpl.class);

    /**
     * This method will perform an audit log query to the local audit repository.  A list of audit
     * log records will be returned to the calling community that match the search criteria.
     *
     * @param query The audit log query search criteria
     * @return A list of Audit Log records that match the specified criteria
     */
    public FindAuditEventsResponseType queryAdapter(com.services.nhinc.schema.auditmessage.FindAuditEventsType query, WebServiceContext context)
    {
        log.debug("Entering AdapterAuditQueryImpl.queryAdapter method...");

        log.debug("incomming adapter audit query request: " + query.toString());

        FindAuditEventsResponseType response = new FindAuditEventsResponseType();

        // Set up the audit query request message for the nhinc audit repository manager
        gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsRequestType findAuditEventsRequest =
                new gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsRequestType();
        findAuditEventsRequest.setAssertion(SamlTokenExtractor.GetAssertion(context));
        findAuditEventsRequest.setFindAuditEvents(query);

        // Audit the Audit Log Query Request Message received on the Adapter Interface
        AcknowledgementType ack = audit(query, findAuditEventsRequest.getAssertion(), NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);

        //call the nhinc auditmangager for now.
        String url = null;
        gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType nhincResponse = null;

        try
        {
            log.debug("NhincConstants.AUDIT_LOG_ADAPTER_SERVICE_NAME: " + NhincConstants.AUDIT_REPO_SECURE_SERVICE_NAME);
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.AUDIT_REPO_SECURE_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.AUDIT_REPO_SECURE_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }

        AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
        AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
        FindCommunitiesAndAuditEventsResponseType result  = proxy.auditQuery(findAuditEventsRequest);

        if (result.getFindAuditEventResponse() != null &&
                NullChecker.isNotNullish(result.getFindAuditEventResponse().getFindAuditEventsReturn())) {
           for (AuditMessageType auditMsg:result.getFindAuditEventResponse().getFindAuditEventsReturn()) {
               response.getFindAuditEventsReturn().add(auditMsg);
           }
        }

        log.debug("outgoing adapter audit query response: " + response);

        log.debug("Exiting AdapterAuditQueryImpl.queryAdapter method...");
        return response;
    }

    private AcknowledgementType audit(com.services.nhinc.schema.auditmessage.FindAuditEventsType auditMsg, AssertionType assertion, String direction, String _interface)
    {
        AcknowledgementType ack = new AcknowledgementType();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

        // Need to resolve the namespace issues here because this type is defined in multiple schemas
        gov.hhs.fha.nhinc.common.auditlog.FindAuditEventsMessageType message = new gov.hhs.fha.nhinc.common.auditlog.FindAuditEventsMessageType();
 
        message.setAssertion(assertion);
        message.setFindAuditEvents(auditMsg);

        LogEventRequestType auditLogMsg = auditLogger.logFindAuditEvents(message, direction, _interface);

        if (auditLogMsg != null) {
            AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
            AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
            ack = proxy.auditLog(auditLogMsg, assertion);
        }

        return ack;
    }
}
