/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.auditlog;

import gov.hhs.fha.nhinc.nhinccomponentinternalauditrepository.NhincComponentInternalAuditRepositoryPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author Jon Hoppesch
 */
@WebService(serviceName = "NhincComponentInternalAuditRepositoryService", portName = "NhincComponentInternalAuditRepositoryPortTypeBindingPort", endpointInterface = "gov.hhs.fha.nhinc.nhinccomponentinternalauditrepository.NhincComponentInternalAuditRepositoryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhinccomponentinternalauditrepository", wsdlLocation = "META-INF/wsdl/AuditLog/NhincComponentInternalAuditRepository.wsdl")
@Stateless
public class AuditLog implements NhincComponentInternalAuditRepositoryPortType {

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType logSubjectAdded(gov.hhs.fha.nhinc.common.auditlog.LogSubjectAddedRequestType logSubjectAddedRequest) {
        AuditLogImpl auditlog = new AuditLogImpl();
        return auditlog.logSubjectAdded(logSubjectAddedRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType logAdhocQuery(gov.hhs.fha.nhinc.common.auditlog.LogAdhocQueryRequestType logAdhocQueryRequest) {
        AuditLogImpl auditlog = new AuditLogImpl();
        return auditlog.logAdhocQuery(logAdhocQueryRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType logSubjectRevised(gov.hhs.fha.nhinc.common.auditlog.LogSubjectRevisedRequestType logSubjectRevisedRequest) {
        AuditLogImpl auditlog = new AuditLogImpl();
        return auditlog.logSubjectRevised(logSubjectRevisedRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType logSubjectRevoked(gov.hhs.fha.nhinc.common.auditlog.LogSubjectRevokedRequestType logSubjectRevokedRequest) {
        AuditLogImpl auditlog = new AuditLogImpl();
        return auditlog.logSubjectRevoked(logSubjectRevokedRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType logNhinSubjectDiscoveryAck(gov.hhs.fha.nhinc.common.auditlog.LogNhinSubjectDiscoveryAckRequestType logNhinSubjectDiscoveryAckRequest) {
        AuditLogImpl auditlog = new AuditLogImpl();
        return auditlog.logNhinSubjectDiscoveryAck(logNhinSubjectDiscoveryAckRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType logAdhocQueryResult(gov.hhs.fha.nhinc.common.auditlog.LogAdhocQueryResultRequestType logAdhocQueryResultRequest) {
        AuditLogImpl auditlog = new AuditLogImpl();
        return auditlog.logAdhocQueryResult(logAdhocQueryResultRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType logDocRetrieve(gov.hhs.fha.nhinc.common.auditlog.LogDocRetrieveRequestType logDocRetrieveRequest) {
        AuditLogImpl auditlog = new AuditLogImpl();
        return auditlog.logDocRetrieve(logDocRetrieveRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType logDocRetrieveResult(gov.hhs.fha.nhinc.common.auditlog.LogDocRetrieveResultRequestType logDocRetrieveResultRequest) {
        AuditLogImpl auditlog = new AuditLogImpl();
        return auditlog.logDocRetrieveResult(logDocRetrieveResultRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType logFindAuditEvents(gov.hhs.fha.nhinc.common.auditlog.LogFindAuditEventsRequestType logFindAuditEventsRequest) {
        AuditLogImpl auditlog = new AuditLogImpl();
        return auditlog.logFindAuditEvents(logFindAuditEventsRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType logFindAuditEventsResult(gov.hhs.fha.nhinc.common.auditlog.LogFindAuditEventsResultRequestType logFindAuditEventsResultRequest) {
        AuditLogImpl auditlog = new AuditLogImpl();
        return auditlog.logFindAuditEventsResult(logFindAuditEventsResultRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType logSubjectReident(gov.hhs.fha.nhinc.common.auditlog.LogSubjectReidentificationRequestType logSubjectReidentRequest) {
        AuditLogImpl auditlog = new AuditLogImpl();
        return auditlog.logSubjectReident(logSubjectReidentRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType logSubjectReidentResult(gov.hhs.fha.nhinc.common.auditlog.LogSubjectReidentificationResponseType logSubjectReidentResponse) {
        AuditLogImpl auditlog = new AuditLogImpl();
        return auditlog.logSubjectReidentResult(logSubjectReidentResponse);
    }

    public java.lang.String logNhinSubscribeRequest(gov.hhs.fha.nhinc.common.nhinccommoninternalorch.SubscribeRequestType message, java.lang.String direction, java.lang.String _interface) {
        AuditLogImpl auditlog = new AuditLogImpl();
        return auditlog.logNhinSubscribeRequest(message, direction, _interface);
    }

    public java.lang.String logNhinNotifyRequest(gov.hhs.fha.nhinc.common.nhinccommoninternalorch.NotifyRequestType message, java.lang.String direction, java.lang.String _interface) {
        AuditLogImpl auditlog = new AuditLogImpl();
        return auditlog.logNhinNotifyRequest(message, direction, _interface);
    }

    public java.lang.String logNhinUnsubscribeRequest(gov.hhs.fha.nhinc.common.nhinccommoninternalorch.UnsubscribeRequestType message, java.lang.String direction, java.lang.String _interface) {
        AuditLogImpl auditlog = new AuditLogImpl();
        return auditlog.logNhinUnsubscribeRequest(message, direction, _interface);
    }

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType logUnsubscribeRespRequest(gov.hhs.fha.nhinc.common.hiemauditlog.LogUnsubscribeResponseType logUnsubscribeRespRequest) {
        AuditLogImpl auditlog = new AuditLogImpl();
        return auditlog.logUnsubscribeRespRequest(logUnsubscribeRespRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType logSubscribeRespRequest(gov.hhs.fha.nhinc.common.hiemauditlog.LogSubscribeResponseType logSubscribeRespRequest) {
        AuditLogImpl auditlog = new AuditLogImpl();
        return auditlog.logSubscribeRespRequest(logSubscribeRespRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType logEntityDocSubscribeRequest(gov.hhs.fha.nhinc.common.hiemauditlog.LogEntityDocumentSubscribeRequestType logmEntityDocSubscribeRequest) {
        AuditLogImpl auditlog = new AuditLogImpl();
        return auditlog.logEntityDocSubscribeRequest(logmEntityDocSubscribeRequest);
    }

    public java.lang.String logEntityCdcSubscribeRequest(gov.hhs.fha.nhinc.common.hiemauditlog.EntityCdcSubscribeRequestMessageType message, java.lang.String direction, java.lang.String _interface) {
        AuditLogImpl auditlog = new AuditLogImpl();
        return auditlog.logEntityCdcSubscribeRequest(message, direction, _interface);
    }

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType logEntityDocNotifyRequest(gov.hhs.fha.nhinc.common.hiemauditlog.LogEntityDocumentNotifyRequestType logEntityDocNotifyRequest) {
        AuditLogImpl auditlog = new AuditLogImpl();
        return auditlog.logEntityDocNotifyRequest(logEntityDocNotifyRequest);
    }

    public java.lang.String logEntityCdcNotifyRequest(gov.hhs.fha.nhinc.common.hiemauditlog.EntityCdcNotifyRequestMessageType message, java.lang.String direction, java.lang.String _interface) {
        AuditLogImpl auditlog = new AuditLogImpl();
        return auditlog.logEntityCdcNotifyRequest(message, direction, _interface);
    }

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType logEntityNotifyRespRequest(gov.hhs.fha.nhinc.common.hiemauditlog.LogEntityNotifyResponseType logEntityNotifyRespRequest) {
        AuditLogImpl auditlog = new AuditLogImpl();
        return auditlog.logEntityNotifyRespRequest(logEntityNotifyRespRequest);
    }

    public java.lang.String logEntityUnsubscribeRequest(gov.hhs.fha.nhinc.common.hiemauditlog.EntityUnsubscribeRequestMessageType message, java.lang.String direction, java.lang.String _interface) {
        AuditLogImpl auditlog = new AuditLogImpl();
        return auditlog.logEntityUnsubscribeRequest(message, direction, _interface);
    }
}
