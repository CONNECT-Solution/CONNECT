/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.common.auditlog.DocRetrieveMessageType;
import gov.hhs.fha.nhinc.common.auditlog.DocRetrieveResponseMessageType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;

/**
 *
 * @author mweaver
 */
public class OutboundDocRetrieveAuditTransformer_a0 implements AuditTransformer {

    public LogEventRequestType transformRequest(Orchestratable message) {
        LogEventRequestType auditLogMsg = null;
        if (message instanceof EntityDocRetrieveOrchestratableImpl_a0) {
            EntityDocRetrieveOrchestratableImpl_a0 EntityDROrchImp_g0Message = (EntityDocRetrieveOrchestratableImpl_a0) message;

            DocRetrieveMessageType DRAuditTransformerMessage = new DocRetrieveMessageType();
            DRAuditTransformerMessage.setRetrieveDocumentSetRequest(EntityDROrchImp_g0Message.getRequest());
            DRAuditTransformerMessage.setAssertion(EntityDROrchImp_g0Message.getAssertion());

            String requestCommunityID = HomeCommunityMap.getCommunityIdForRDRequest(EntityDROrchImp_g0Message.getRequest());

            AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
            auditLogMsg = auditLogger.logDocRetrieve(DRAuditTransformerMessage, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, requestCommunityID);
        }
        return auditLogMsg;
    }

    public LogEventRequestType transformResponse(Orchestratable message) {
        LogEventRequestType auditLogMsg = null;
        if (message instanceof EntityDocRetrieveOrchestratableImpl_a0) {
            EntityDocRetrieveOrchestratableImpl_a0 EntityDROrchImp_g0Message = (EntityDocRetrieveOrchestratableImpl_a0) message;

            DocRetrieveResponseMessageType DRAuditTransformerMessage = new DocRetrieveResponseMessageType();
            DRAuditTransformerMessage.setRetrieveDocumentSetResponse(EntityDROrchImp_g0Message.getResponse());
            DRAuditTransformerMessage.setAssertion(EntityDROrchImp_g0Message.getAssertion());

            String requestCommunityID = HomeCommunityMap.getCommunityIdForRDRequest(EntityDROrchImp_g0Message.getRequest());

            AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
            auditLogMsg = auditLogger.logDocRetrieveResult(DRAuditTransformerMessage, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, requestCommunityID);
        }
        return auditLogMsg;
    }
}
