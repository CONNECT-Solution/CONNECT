/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.auditrepository.proxy;

import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventSecureRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinccomponentauditrepository.AuditRepositoryManagerSecuredPortType;
import gov.hhs.fha.nhinc.nhinccomponentauditrepository.AuditRepositoryManagerSecuredService;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jon Hoppesch
 */
public class AuditRepositorySecuredWebServiceProxy implements AuditRepositoryProxy {

    private static Log log = LogFactory.getLog(AuditRepositorySecuredWebServiceProxy.class);
    static AuditRepositoryManagerSecuredService auditRepoService = new AuditRepositoryManagerSecuredService();

    public FindCommunitiesAndAuditEventsResponseType auditQuery(FindCommunitiesAndAuditEventsRequestType request) {
        String url = null;
        FindCommunitiesAndAuditEventsResponseType result = new FindCommunitiesAndAuditEventsResponseType();

        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.AUDIT_REPO_SECURE_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.AUDIT_REPO_SECURE_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }

        if (NullChecker.isNotNullish(url)) {
            AuditRepositoryManagerSecuredPortType port = getPort(url);

            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(request.getAssertion(), url, NhincConstants.AUDIT_REPO_ACTION);

            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            result = port.queryAuditEvents(request);
        }

        return result;
    }

    public AcknowledgementType auditLog(LogEventRequestType request, AssertionType assertion) {
        log.debug("Entering AuditRepositorySecuredWebServiceProxy.auditLog(...)");
        String url = null;
        AcknowledgementType result = new AcknowledgementType();
        LogEventSecureRequestType secureRequest = new LogEventSecureRequestType();
        if(request.getAuditMessage() == null)
        {
            log.error("Audit Request is null");
        }
        secureRequest.setAuditMessage(request.getAuditMessage());
        secureRequest.setAssertion(assertion);
        secureRequest.setDirection(request.getDirection());
        secureRequest.setInterface(request.getInterface());

        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.AUDIT_REPO_SECURE_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.AUDIT_REPO_SECURE_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }

        log.debug("In AuditRepositorySecuredWebServiceProxy.auditLog(...) - completed called to ConnectionManager to retrieve endpoint.");

        if (NullChecker.isNotNullish(url)) {
            AuditRepositoryManagerSecuredPortType port = getPort(url);

            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.AUDIT_REPO_ACTION);

            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            log.debug("In AuditRepositorySecuredWebServiceProxy.auditLog(...) - calling AuditRepositoryManagerSecuredPortType.logEvent(...).");
            result = port.logEvent(secureRequest);
            log.debug("In AuditRepositorySecuredWebServiceProxy.auditLog(...) - returned from calling AuditRepositoryManagerSecuredPortType.logEvent(...).");
        }
        log.debug("Exiting AuditRepositorySecuredWebServiceProxy.auditLog(...)");

        return result;
    }

    private AuditRepositoryManagerSecuredPortType getPort(String url) {
        AuditRepositoryManagerSecuredPortType port = auditRepoService.getAuditRepositoryManagerSecuredPort();
		gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        return port;
    }
}
