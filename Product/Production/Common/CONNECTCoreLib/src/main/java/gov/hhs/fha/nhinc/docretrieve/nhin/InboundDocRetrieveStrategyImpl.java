/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docretrieve.nhin;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docretrieve.adapter.proxy.AdapterDocRetrieveProxy;
import gov.hhs.fha.nhinc.docretrieve.adapter.proxy.AdapterDocRetrieveProxyObjectFactory;
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
public class InboundDocRetrieveStrategyImpl implements InboundDocRetrieveStrategy, OrchestrationStrategy {

    private static Log log = LogFactory.getLog(InboundDocRetrieveStrategyImpl.class);

    public InboundDocRetrieveStrategyImpl() {

    }

    private Log getLogger() {
        return log;
    }

    public void execute(InboundDocRetrieveOrchestratable message) {
        getLogger().debug("Begin NhinDocRetrieveOrchestratableImpl_g0.process");
        if (message == null) {
            getLogger().debug("NhinOrchestratable was null");
            return;
        }

        if (message instanceof InboundDocRetrieveOrchestratableImpl) {
            InboundDocRetrieveOrchestratableImpl NhinDRMessage = (InboundDocRetrieveOrchestratableImpl) message;
            String requestCommunityID = HomeCommunityMap.getCommunityIdForRDRequest(NhinDRMessage.getRequest());

            getLogger().debug("Calling audit log for doc retrieve request (g0) sent to adapter (a0)");
            auditRequestMessage(NhinDRMessage.getRequest(), NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                    NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, NhinDRMessage.getAssertion(), requestCommunityID);

            getLogger().debug("Creating adapter (a0) doc retrieve proxy");
            AdapterDocRetrieveProxy proxy = new AdapterDocRetrieveProxyObjectFactory().getAdapterDocRetrieveProxy();
            getLogger().debug("Sending adapter doc retrieve to adapter (a0)");
            NhinDRMessage.setResponse(proxy.retrieveDocumentSet(NhinDRMessage.getRequest(),
                    NhinDRMessage.getAssertion()));

            getLogger().debug("Calling audit log for doc retrieve response received from adapter (a0)");
            auditResponseMessage(NhinDRMessage.getResponse(), NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                    NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, NhinDRMessage.getAssertion(), requestCommunityID);
        } else {
            getLogger()
                    .error("NhinDocRetrieve_g0AdapterDelegateImpl_a0.process recieved a message which was not of type NhinDocRetrieveOrchestratableImpl_g0.");
        }
        getLogger().debug("End NhinDocRetrieveOrchestratableImpl_g0.process");
    }

    private void auditRequestMessage(RetrieveDocumentSetRequestType request, String direction, String connectInterface,
            AssertionType assertion, String requestCommunityID) {
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

    private void auditResponseMessage(RetrieveDocumentSetResponseType response, String direction,
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

    private AcknowledgementType auditMessage(LogEventRequestType auditLogMsg, AssertionType assertion) {
        AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
        AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
        return proxy.auditLog(auditLogMsg, assertion);
    }

    @Override
    public void execute(Orchestratable message) {
        if (message instanceof InboundDocRetrieveOrchestratable) {
            execute((InboundDocRetrieveOrchestratable) message);
        }
    }
}
