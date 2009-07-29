/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.auditlogdte;

import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.nhinccomponentaditlogdte.NhincComponentAuditLogDtePortType;
import gov.hhs.fha.nhinc.transform.audit.DocumentQueryTransforms;
import gov.hhs.fha.nhinc.transform.audit.DocumentRetrieveTransforms;
import gov.hhs.fha.nhinc.transform.audit.FindAuditEventsTransforms;
import gov.hhs.fha.nhinc.transform.audit.NotifyTransforms;
import gov.hhs.fha.nhinc.transform.audit.SubjectDiscoveryTransforms;
import gov.hhs.fha.nhinc.transform.audit.SubscribeTransforms;
import gov.hhs.fha.nhinc.transform.audit.UnsubscribeTransforms;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "NhincComponentAuditLogDteService", portName = "NhincComponentAuditLogDtePort", endpointInterface = "gov.hhs.fha.nhinc.nhinccomponentaditlogdte.NhincComponentAuditLogDtePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhinccomponentaditlogdte", wsdlLocation = "META-INF/wsdl/NhincAuditLogDTE/NhincComponentAuditLogDte.wsdl")
@Stateless
public class NhincAuditLogDTE implements NhincComponentAuditLogDtePortType {

    public gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType transformSubjectAddedToGenericAudit(gov.hhs.fha.nhinc.common.auditlog.LogSubjectAddedRequestType transformSubjectAddedToGenericAuditRequest) {
        LogEventRequestType response = SubjectDiscoveryTransforms.transformPRPA2013012AuditMsg(transformSubjectAddedToGenericAuditRequest);
        return response;
    }

    public gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType transformSubjectRevisedToGenericAudit(gov.hhs.fha.nhinc.common.auditlog.LogSubjectRevisedRequestType transformSubjectRevisedToGenericAuditRequest) {
        LogEventRequestType response = SubjectDiscoveryTransforms.transformPRPA2013022AuditMsg(transformSubjectRevisedToGenericAuditRequest);
        return response;
    }

    public gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType transformSubjectRevokedToGenericAudit(gov.hhs.fha.nhinc.common.auditlog.LogSubjectRevokedRequestType transformSubjectRevokedToGenericAuditRequest) {
        LogEventRequestType response = SubjectDiscoveryTransforms.transformPRPA2013032AuditMsg(transformSubjectRevokedToGenericAuditRequest);
        return response;
    }

    public gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType transformAdhocQueryRequestToGenericAudit(gov.hhs.fha.nhinc.common.auditlog.LogAdhocQueryRequestType transformAdhocQueryRequestToGenericAuditRequest) {
        LogEventRequestType response = DocumentQueryTransforms.transformDocQueryReq2AuditMsg(transformAdhocQueryRequestToGenericAuditRequest);
        return response;
    }

    public gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType transformAdhocQueryResponseToGenericAudit(gov.hhs.fha.nhinc.common.auditlog.LogAdhocQueryResultRequestType transformAdhocQueryResponseToGenericAuditResponse) {
        LogEventRequestType response = DocumentQueryTransforms.transformDocQueryResp2AuditMsg(transformAdhocQueryResponseToGenericAuditResponse);
        return response;
    }

    public gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType transformDocRetrieveRequestToGenericAudit(gov.hhs.fha.nhinc.common.auditlog.LogDocRetrieveRequestType transformDocRetrieveRequestToGenericAuditRequest) {
        LogEventRequestType response = DocumentRetrieveTransforms.transformDocRetrieveReq2AuditMsg(transformDocRetrieveRequestToGenericAuditRequest);
        return response;
    }

    public gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType transformDocRetrieveResponseToGenericAudit(gov.hhs.fha.nhinc.common.auditlog.LogDocRetrieveResultRequestType transformDocRetrieveResponseToGenericAuditRequest) {
        LogEventRequestType response = DocumentRetrieveTransforms.transformDocRetrieveResp2AuditMsg(transformDocRetrieveResponseToGenericAuditRequest);
        return response;
    }

    public gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType transformFindAuditEventsToGenericAudit(gov.hhs.fha.nhinc.common.auditlog.LogFindAuditEventsRequestType transformFindAuditEventsToGenericAuditRequest) {
        LogEventRequestType response = FindAuditEventsTransforms.transformFindAuditEventsReq2AuditMsg(transformFindAuditEventsToGenericAuditRequest);
        return response;
    }

    public gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType transformAckToGenericAudit(gov.hhs.fha.nhinc.common.auditlog.LogNhinSubjectDiscoveryAckRequestType transformAckToGenericAuditRequest) {
        LogEventRequestType response = SubjectDiscoveryTransforms.transformAck2AuditMsg(transformAckToGenericAuditRequest);
        return response;
    }
    
    public gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType transformSubjectReidentRequestToGenericAudit(gov.hhs.fha.nhinc.common.auditlog.LogSubjectReidentificationRequestType transformSubjectReidentRequestToGenericAuditRequest) {
        LogEventRequestType response = SubjectDiscoveryTransforms.transformPRPA2013092AuditMsg(transformSubjectReidentRequestToGenericAuditRequest);
        return response;
    }

    public gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType transformSubjectReidentResponseToGenericAudit(gov.hhs.fha.nhinc.common.auditlog.LogSubjectReidentificationResponseType transformSubjectReidentResponseToGenericAuditRequest) {
        LogEventRequestType response = SubjectDiscoveryTransforms.transformPRPA2013102AuditMsg(transformSubjectReidentResponseToGenericAuditRequest);
        return response;
    }

    public gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType transformNhinSubscribeRequestToGenericAudit(gov.hhs.fha.nhinc.common.hiemauditlog.LogNhinSubscribeRequestType transformNhinSubscribeRequestToGenericAuditRequest) {
        LogEventRequestType response = new SubscribeTransforms().transformNhinSubscribeRequestToAuditMessage(transformNhinSubscribeRequestToGenericAuditRequest);
        return response;
    }

    public gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType transformNhinNotifyRequestToGenericAudit(gov.hhs.fha.nhinc.common.hiemauditlog.LogNhinNotifyRequestType transformNhinNotifyRequestToGenericAuditRequest) {
        LogEventRequestType response = new NotifyTransforms().transformNhinNotifyRequestToAuditMessage(transformNhinNotifyRequestToGenericAuditRequest);
        return response;
    }

    public gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType transformNhinUnsubscribeRequestToGenericAudit(gov.hhs.fha.nhinc.common.hiemauditlog.LogNhinUnsubscribeRequestType transformNhinUnsubscribeRequestToGenericAuditRequest) {
        LogEventRequestType response = new UnsubscribeTransforms().transformNhinUnsubscribeRequestToAuditMessage(transformNhinUnsubscribeRequestToGenericAuditRequest);
        return response;
    }

    public gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType transformUnsubscribeResponseToGenericAudit(gov.hhs.fha.nhinc.common.hiemauditlog.LogUnsubscribeResponseType transformUnsubscribeResponseToGenericAuditRequest) {
        LogEventRequestType response = new UnsubscribeTransforms().transformUnsubscribeResponseToGenericAudit(transformUnsubscribeResponseToGenericAuditRequest);
        return response;
    }

    public gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType transformSubscribeResponseToGenericAudit(gov.hhs.fha.nhinc.common.hiemauditlog.LogSubscribeResponseType transformSubscribeResponseToGenericAuditRequest) {
        LogEventRequestType response = new SubscribeTransforms().transformSubscribeResponseToAuditMessage(transformSubscribeResponseToGenericAuditRequest);
        return response;
    }

    public gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType transformEntityDocSubscribeRequestToGenericAudit(gov.hhs.fha.nhinc.common.hiemauditlog.LogEntityDocumentSubscribeRequestType transformEntityDocSubscribeRequestToGenericAuditRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType transformEntityCdcSubscribeRequestToGenericAudit(gov.hhs.fha.nhinc.common.hiemauditlog.LogEntityCdcSubscribeRequestType transformEntityCdcSubscribeRequestToGenericAuditRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType transformEntityDocNotifyRequestToGenericAudit(gov.hhs.fha.nhinc.common.hiemauditlog.LogEntityDocumentNotifyRequestType transformEntityDocNotifyRequestToGenericAuditRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType transformEntityCdcNotifyRequestToGenericAudit(gov.hhs.fha.nhinc.common.hiemauditlog.LogEntityCdcNotifyRequestType transformEntityCdcNotifyRequestToGenericAuditRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType transformEntityNotifyResponseToGenericAudit(gov.hhs.fha.nhinc.common.hiemauditlog.LogEntityNotifyResponseType transformEntityNotifyResponseToGenericAuditRequest) {
        LogEventRequestType response = new NotifyTransforms().transformEntityNotifyResponseToGenericAudit(transformEntityNotifyResponseToGenericAuditRequest);
        return response;
    }

    public gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType transformEntityUnsubscribeRequestToGenericAudit(gov.hhs.fha.nhinc.common.hiemauditlog.LogEntityUnsubscribeRequestType transformEntityUnsubscribeRequestToGenericAuditRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
