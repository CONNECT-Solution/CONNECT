/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docretrieve.nhin;

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
public class NhinDocRetrieveAuditTransformer_g0 implements AuditTransformer {

    public LogEventRequestType transformRequest(Orchestratable message) {
        LogEventRequestType auditLogMsg = null;
        if (message instanceof NhinDocRetrieveOrchestratableImpl_g0) {
            NhinDocRetrieveOrchestratableImpl_g0 NhinDROrchImp_g0Message = (NhinDocRetrieveOrchestratableImpl_g0) message;

            DocRetrieveMessageType DRAuditTransformerMessage = new DocRetrieveMessageType();
            DRAuditTransformerMessage.setRetrieveDocumentSetRequest(NhinDROrchImp_g0Message.getRequest());
            DRAuditTransformerMessage.setAssertion(NhinDROrchImp_g0Message.getAssertion());

            String requestCommunityID = HomeCommunityMap.getCommunityIdForRDRequest(NhinDROrchImp_g0Message.getRequest());

            AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
            auditLogMsg = auditLogger.logDocRetrieve(DRAuditTransformerMessage, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, requestCommunityID);
        }
        return auditLogMsg;
    }

    public LogEventRequestType transformResponse(Orchestratable message) {
        LogEventRequestType auditLogMsg = null;
        if (message instanceof NhinDocRetrieveOrchestratableImpl_g0) {
            NhinDocRetrieveOrchestratableImpl_g0 NhinDROrchImp_g0Message = (NhinDocRetrieveOrchestratableImpl_g0) message;

            DocRetrieveResponseMessageType DRAuditTransformerMessage = new DocRetrieveResponseMessageType();
            DRAuditTransformerMessage.setRetrieveDocumentSetResponse(NhinDROrchImp_g0Message.getResponse());
            DRAuditTransformerMessage.setAssertion(NhinDROrchImp_g0Message.getAssertion());

            String requestCommunityID = HomeCommunityMap.getCommunityIdForRDRequest(NhinDROrchImp_g0Message.getRequest());

            AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
            auditLogMsg = auditLogger.logDocRetrieveResult(DRAuditTransformerMessage, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, requestCommunityID);
        }
        return auditLogMsg;
    }
}
