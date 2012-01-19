package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docquery.nhin.proxy.NhinDocQueryProxy;
import gov.hhs.fha.nhinc.docquery.nhin.proxy.NhinDocQueryProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Implements the DocQuery client strategy for spec g1 endpoint
 *
 * @author paul.eftis
 */
public class NhinDocQueryStrategyImpl_g1 implements OrchestrationStrategy{

    private static Log log = LogFactory.getLog(NhinDocQueryStrategyImpl_g1.class);
    
    public NhinDocQueryStrategyImpl_g1(){
        
    }

    private Log getLogger()
    {
        return log;
    }


    @Override
    public void execute(Orchestratable message){
        getLogger().debug("NhinDocQueryStrategyImpl_g1::execute");
        if(message == null){
            getLogger().debug("NhinDocQueryStrategyImpl_g1 EntityOrchestratable was null!!!");
            // throw new Exception("NhinDocQueryStrategyImpl_g0 input message was null!!!");
        }
        if(message instanceof EntityDocQueryOrchestratable){
            if(message instanceof EntityDocQueryOrchestratable_a1){
                EntityDocQueryOrchestratable_a1 m = (EntityDocQueryOrchestratable_a1)message;
                EntityDocQueryOrchestratable_a1 response = executeStrategy_g1(m);
                m.setResponse(response.getResponse());
            }else{
                // shouldn't get here
                getLogger().debug("NhinDocQueryStrategyImpl_g1 EntityOrchestratable was not an EntityDocQueryOrchestratable_a0!!!");
                // throw new Exception("NhinDocQueryStrategyImpl_g0 EntityOrchestratable was not an EntityDocQueryOrchestratable_a0!!!");
            }
        }else{
            // shouldn't get here
            getLogger().debug("NhinDocQueryStrategyImpl_g1 EntityOrchestratable was not an EntityDocQueryOrchestratable!!!");
            // throw new Exception("EntityDocQueryOrchestratable input message was not an EntityDocQueryOrchestratable!!!");
        }
    }

    
    public EntityDocQueryOrchestratable_a1 executeStrategy_g1(EntityDocQueryOrchestratable_a1 message){
        getLogger().debug("NhinDocQueryStrategyImpl_g1::executeStrategy_g1");

        EntityDocQueryOrchestratable_a1 nhinDQResponse = new EntityDocQueryOrchestratable_a1(
                null, message.getResponseProcessor(), message.getAuditTransformer(),
                message.getPolicyTransformer(), message.getAssertion(),
                message.getServiceName(), message.getTarget(), message.getRequest());

        NhinTargetSystemType targetSystem = message.getTarget();
        String requestCommunityID = targetSystem.getHomeCommunity().getHomeCommunityId();

        auditRequestMessage(message.getRequest(), NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
                message.getAssertion(), requestCommunityID);

        NhinDocQueryProxy proxy = new NhinDocQueryProxyObjectFactory().getNhinDocQueryProxy();
        getLogger().debug("NhinDocQueryStrategyImpl_g1::executeStrategy_g1 sending nhin doc query request to "
                + " target hcid=" + requestCommunityID);

        nhinDQResponse.setResponse(proxy.respondingGatewayCrossGatewayQuery(
                message.getRequest(), message.getAssertion(), targetSystem));

        auditResponseMessage(nhinDQResponse.getResponse(), NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
                nhinDQResponse.getAssertion(), requestCommunityID);

        getLogger().debug("NhinDocQueryStrategyImpl_g1::executeStrategy_g0 returning response");
        return nhinDQResponse;
    }

    
    private void auditRequestMessage(AdhocQueryRequest request, String direction, String connectInterface, AssertionType assertion, String requestCommunityID) {
        gov.hhs.fha.nhinc.common.auditlog.AdhocQueryMessageType message = new gov.hhs.fha.nhinc.common.auditlog.AdhocQueryMessageType();
        message.setAdhocQueryRequest(request);
        message.setAssertion(assertion);
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logAdhocQuery(message, direction, connectInterface);
        if (auditLogMsg != null) {
            auditMessage(auditLogMsg, assertion);
        }
    }


    private void auditResponseMessage(AdhocQueryResponse response, String direction, String connectInterface, AssertionType assertion, String requestCommunityID) {
        gov.hhs.fha.nhinc.common.auditlog.AdhocQueryResponseMessageType message = new gov.hhs.fha.nhinc.common.auditlog.AdhocQueryResponseMessageType();
        message.setAdhocQueryResponse(response);
        message.setAssertion(assertion);
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logAdhocQueryResult(message, direction, connectInterface);
        if (auditLogMsg != null) {
            auditMessage(auditLogMsg, assertion);
        }
    }


    private AcknowledgementType auditMessage(LogEventRequestType auditLogMsg, AssertionType assertion) {
        AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
        AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
        return proxy.auditLog(auditLogMsg, assertion);
    }
}
