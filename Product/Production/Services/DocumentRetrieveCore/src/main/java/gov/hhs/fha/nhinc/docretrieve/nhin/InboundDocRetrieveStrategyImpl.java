/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docretrieve.nhin;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryDocumentRetrieveLogger;
import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docretrieve.DocRetrieveFileUtils;
import gov.hhs.fha.nhinc.docretrieve.MessageGenerator;
import gov.hhs.fha.nhinc.docretrieve.adapter.proxy.AdapterDocRetrieveProxy;
import gov.hhs.fha.nhinc.docretrieve.adapter.proxy.AdapterDocRetrieveProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

import org.apache.log4j.Logger;

/**
 * 
 * @author mweaver
 */
public class InboundDocRetrieveStrategyImpl implements InboundDocRetrieveStrategy {

    private static final Logger LOG = Logger.getLogger(InboundDocRetrieveStrategyImpl.class);
    AdapterDocRetrieveProxy proxy;
    AuditRepositoryDocumentRetrieveLogger auditLogger;

    /**
     * Default constructor.
     */
    public InboundDocRetrieveStrategyImpl() {
        proxy = new AdapterDocRetrieveProxyObjectFactory().getAdapterDocRetrieveProxy();
        auditLogger = new AuditRepositoryLogger();
    }

    /**
     * DI constructor.
     * 
     * @param proxy AdapterDocRetrieveProxy
     * @param auditLogger the auditLogger
     */
    InboundDocRetrieveStrategyImpl(AdapterDocRetrieveProxy proxy, AuditRepositoryDocumentRetrieveLogger auditLogger) {
        super();
        this.proxy = proxy;
        this.auditLogger = auditLogger;
    }

    public void execute(InboundDocRetrieveOrchestratable message) {
        LOG.debug("Begin NhinDocRetrieveOrchestratableImpl_g0.process");
        if (message == null) {
            LOG.debug("NhinOrchestratable was null");
            return;
        }
        
        if (! message.isPassthru()){
        auditOutboundRequestMessage(message);
        }

        RetrieveDocumentSetResponseType adapterResponse = sendToAdapter(message);

        message.setResponse(adapterResponse);

        if(!message.isPassthru()) {
        auditInboundResponseMessage(message);
        }

        LOG.debug("End NhinDocRetrieveOrchestratableImpl_g0.process");
    }

    /**
     * @param message
     * @return
     */
    public RetrieveDocumentSetResponseType sendToAdapter(InboundDocRetrieveOrchestratable message) {
        RetrieveDocumentSetResponseType adapterResponse = proxy.retrieveDocumentSet(message.getRequest(),
                message.getAssertion());

        try {
            DocRetrieveFileUtils.getInstance().convertFileLocationToDataIfEnabled(adapterResponse);
        } catch (Exception e) {
            LOG.error("Failed to retrieve data from the file uri in the payload.", e);
            adapterResponse = MessageGenerator.getInstance().createRegistryResponseError(
                    "Adapter Document Retrieve Processing");
        }
        return adapterResponse;
    }

    private void auditResponseMessage(RetrieveDocumentSetResponseType response, AssertionType assertion,
            String requestCommunityID) {
        gov.hhs.fha.nhinc.common.auditlog.DocRetrieveResponseMessageType message = new gov.hhs.fha.nhinc.common.auditlog.DocRetrieveResponseMessageType();
        message.setRetrieveDocumentSetResponse(response);
        message.setAssertion(assertion);
        LogEventRequestType auditLogMsg = auditLogger.logDocRetrieveResult(message,
                NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE,
                requestCommunityID);
        if (auditLogMsg != null) {
            auditMessage(auditLogMsg, assertion);
        }
    }

    /**
     * @param message
     */
    public void auditInboundResponseMessage(InboundDocRetrieveOrchestratable message) {
        String requestCommunityID = HomeCommunityMap.getLocalHomeCommunityId();

        LOG.debug("Calling audit log for doc retrieve response received from adapter (a0)");
        auditResponseMessage(message.getResponse(), message.getAssertion(), requestCommunityID);
    }


    private void auditRequestMessage(RetrieveDocumentSetRequestType request, AssertionType assertion,
            String requestCommunityID) {
        gov.hhs.fha.nhinc.common.auditlog.DocRetrieveMessageType message = new gov.hhs.fha.nhinc.common.auditlog.DocRetrieveMessageType();
        message.setRetrieveDocumentSetRequest(request);
        message.setAssertion(assertion);
        LogEventRequestType auditLogMsg = auditLogger.logDocRetrieve(message,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE,
                requestCommunityID);
        if (auditLogMsg != null) {
            auditMessage(auditLogMsg, assertion);
        }
    }
    /**
     * @param message
     * @return
     */
    public void auditOutboundRequestMessage(InboundDocRetrieveOrchestratable message) {
        String requestCommunityID = HomeCommunityMap.getLocalHomeCommunityId();
        
        LOG.debug("Calling audit log for doc retrieve request (g0) sent to adapter (a0)");
        auditRequestMessage(message.getRequest(), message.getAssertion(), requestCommunityID);
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
