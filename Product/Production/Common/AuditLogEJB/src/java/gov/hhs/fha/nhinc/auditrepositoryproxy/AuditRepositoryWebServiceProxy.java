/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.auditrepositoryproxy;

import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinccomponentauditrepository.AuditRepositoryManagerPortType;
import gov.hhs.fha.nhinc.nhinccomponentauditrepository.AuditRepositoryManagerService;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class AuditRepositoryWebServiceProxy implements IAuditRepositoryProxy {
    private static Log log = LogFactory.getLog(AuditRepositoryWebServiceProxy.class);
    static AuditRepositoryManagerService auditRepoService = new AuditRepositoryManagerService();

    public AcknowledgementType logEvent(LogEventRequestType logEventRequest) {
        String url = null;
        AcknowledgementType result = new AcknowledgementType();

        // Get the local home community id
        String homeCommunityId = null;
        try {
            log.info("Attempting to retrieve property: " + NhincConstants.HOME_COMMUNITY_ID_PROPERTY + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            homeCommunityId = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
            log.info("Retrieve local home community id: " + homeCommunityId);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.HOME_COMMUNITY_ID_PROPERTY + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }

        if (NullChecker.isNotNullish(homeCommunityId)) {
            try {
                url = ConnectionManagerCache.getEndpointURLByServiceName(homeCommunityId, NhincConstants.AUDIT_REPO_SERVICE_NAME);
            } catch (ConnectionManagerException ex) {
                log.error("Error: Failed to retrieve url for service: " + NhincConstants.AUDIT_REPO_SERVICE_NAME + " for community id: " + homeCommunityId);
                log.error(ex.getMessage());
            }
        }

        if (NullChecker.isNotNullish(url)) {
            AuditRepositoryManagerPortType port = getPort(url);

            log.info("Performing an audit log query to community " + homeCommunityId);
            result = port.logEvent(logEventRequest);
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
