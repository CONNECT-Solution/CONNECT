/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author mweaver
 */
public abstract class OutboundDocRetrieveStrategyBase implements OrchestrationStrategy {

    private static Log log = LogFactory.getLog(OutboundDocRetrieveStrategyBase.class);

    @Override
    public void execute(Orchestratable message) {
        if (message instanceof OutboundDocRetrieveOrchestratable) {
            execute((OutboundDocRetrieveOrchestratable) message);
        }
    }

    protected Log getLogger() {
        return log;
    }

    public void execute(OutboundDocRetrieveOrchestratable message) {
        getLogger().debug("Begin OutboundDocRetrieveStrategyBase.execute");
        if (message == null) {
            getLogger().debug("NhinOrchestratable was null");
            return;
        }

        if (message instanceof OutboundDocRetrieveOrchestratable) {
            OutboundDocRetrieveOrchestratable NhinDRMessage = (OutboundDocRetrieveOrchestratable) message;
            String requestCommunityID = HomeCommunityMap.getCommunityIdForRDRequest(NhinDRMessage.getRequest());

            getLogger().debug("Calling audit log for doc retrieve request (a0) sent to nhin (g0)");
            auditRequestMessage(NhinDRMessage.getRequest(), NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                    NhincConstants.AUDIT_LOG_NHIN_INTERFACE, NhinDRMessage.getAssertion(), requestCommunityID);

            NhinDRMessage.setResponse(callProxy(NhinDRMessage));

            getLogger().debug("Calling audit log for doc retrieve response received from nhin (g0)");
            auditResponseMessage(NhinDRMessage.getResponse(), NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                    NhincConstants.AUDIT_LOG_NHIN_INTERFACE, NhinDRMessage.getAssertion(), requestCommunityID);
        } else {
            getLogger()
                    .error("OutboundDocRetrieveStrategyBase.execute recieved a message which was not of type NhinDocRetrieveOrchestratableImpl_g0.");
        }
        getLogger().debug("End OutboundDocRetrieveStrategyBase.execute");
    }

    protected abstract RetrieveDocumentSetResponseType callProxy(OutboundDocRetrieveOrchestratable message);

    protected void auditRequestMessage(RetrieveDocumentSetRequestType request, String direction,
            String connectInterface, AssertionType assertion, String requestCommunityID) {
        gov.hhs.fha.nhinc.common.auditlog.DocRetrieveMessageType message = new gov.hhs.fha.nhinc.common.auditlog.DocRetrieveMessageType();
        message.setRetrieveDocumentSetRequest(request);
        message.setAssertion(assertion);
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logDocRetrieve(message, direction, connectInterface,
                requestCommunityID);
        if (auditLogMsg != null) {
            auditMessage(auditLogMsg, assertion);
        }
    }

    protected void auditResponseMessage(RetrieveDocumentSetResponseType response, String direction,
            String connectInterface, AssertionType assertion, String requestCommunityID) {
        gov.hhs.fha.nhinc.common.auditlog.DocRetrieveResponseMessageType message = new gov.hhs.fha.nhinc.common.auditlog.DocRetrieveResponseMessageType();
        message.setRetrieveDocumentSetResponse(response);
        message.setAssertion(assertion);
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logDocRetrieveResult(message, direction, connectInterface,
                requestCommunityID);
        if (auditLogMsg != null) {
            auditMessage(auditLogMsg, assertion);
        }
    }

    protected AcknowledgementType auditMessage(LogEventRequestType auditLogMsg, AssertionType assertion) {
        AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
        AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
        return proxy.auditLog(auditLogMsg, assertion);
    }

}
