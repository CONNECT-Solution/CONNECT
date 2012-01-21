package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author bhumphrey/paul
 *
 */
public abstract class NhinDocQueryStrategy implements OrchestrationStrategy{
	
    private static Log log = LogFactory.getLog(NhinDocQueryStrategy.class);

     private Log getLogger(){
         return log;
     }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy#execute(gov.hhs.fha.nhinc.orchestration.Orchestratable)
     */
    @Override
    public void execute(Orchestratable message){
         if(message == null){
            getLogger().debug("NhinDocQueryStrategy EntityOrchestratable was null!!!");
            // throw new Exception("NhinDocQueryStrategy input message was null!!!");
        }
        if(message instanceof EntityDocQueryOrchestratable){
            execute((EntityDocQueryOrchestratable)message);
        }else{
            // shouldn't get here
            getLogger().debug("NhinDocQueryStrategy EntityOrchestratable was not an EntityDocQueryOrchestratable!!!");
            // throw new Exception("EntityDocQueryOrchestratable input message was not an EntityDocQueryOrchestratable!!!");
        }
    }

    
    abstract public void execute(EntityDocQueryOrchestratable message);


    protected void auditRequestMessage(AdhocQueryRequest request, String direction, String connectInterface,
            AssertionType assertion, String requestCommunityID){

        gov.hhs.fha.nhinc.common.auditlog.AdhocQueryMessageType message = new gov.hhs.fha.nhinc.common.auditlog.AdhocQueryMessageType();
        message.setAdhocQueryRequest(request);
        message.setAssertion(assertion);
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logAdhocQuery(message, direction, connectInterface);
        if (auditLogMsg != null) {
            auditMessage(auditLogMsg, assertion);
        }
    }


    protected void auditResponseMessage(AdhocQueryResponse response, String direction, String connectInterface, AssertionType assertion, String requestCommunityID) {
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
