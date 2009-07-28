/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.auditrepository.proxy;

import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinccomponentauditrepository.AuditRepositoryManagerPortType;
import gov.hhs.fha.nhinc.nhinccomponentauditrepository.AuditRepositoryManagerService;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jon Hoppesch
 */
public class AuditRepositoryWebServiceProxy implements AuditRepositoryProxy {

    private static Log log = LogFactory.getLog(AuditRepositoryWebServiceProxy.class);
    static AuditRepositoryManagerService auditRepoService = new AuditRepositoryManagerService();

    public FindCommunitiesAndAuditEventsResponseType auditQuery(FindCommunitiesAndAuditEventsRequestType request) {
        String url = null;
        FindCommunitiesAndAuditEventsResponseType result = new FindCommunitiesAndAuditEventsResponseType();

        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.AUDIT_REPO_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.AUDIT_REPO_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }

        if (NullChecker.isNotNullish(url)) {
            AuditRepositoryManagerPortType port = getPort(url);

            result = port.queryAuditEvents(request);
        }

        return result;
    }

    public AcknowledgementType auditLog(LogEventRequestType request) {
        String url = null;
        AcknowledgementType result = new AcknowledgementType();

        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.AUDIT_REPO_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.AUDIT_REPO_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }

        if (NullChecker.isNotNullish(url)) {
            AuditRepositoryManagerPortType port = getPort(url);

            result = port.logEvent(request);
        }

        return result;
    }

    private AuditRepositoryManagerPortType getPort(String url) {
        AuditRepositoryManagerPortType port = auditRepoService.getAuditRepositoryManagerPort();

        log.info("Setting endpoint address to Audit Repository Service to " + url);
        ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        return port;
    }
}
