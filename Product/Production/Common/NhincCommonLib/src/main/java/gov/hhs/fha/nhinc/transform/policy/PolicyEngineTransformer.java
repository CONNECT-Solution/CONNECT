/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.policy;

import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestEventType;
import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryResultEventType;
import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveEventType;
import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveResultEventType;
import gov.hhs.fha.nhinc.common.eventcommon.FindAuditEventsEventType;
import gov.hhs.fha.nhinc.common.eventcommon.NotifyEventType;
import gov.hhs.fha.nhinc.common.eventcommon.PatDiscReqEventType;
import gov.hhs.fha.nhinc.common.eventcommon.SubjectAddedEventType;
import gov.hhs.fha.nhinc.common.eventcommon.SubjectReidentificationEventType;
import gov.hhs.fha.nhinc.common.eventcommon.SubjectRevisedEventType;
import gov.hhs.fha.nhinc.common.eventcommon.SubscribeEventType;
import gov.hhs.fha.nhinc.common.eventcommon.UnsubscribeEventType;
import gov.hhs.fha.nhinc.common.eventcommon.XDREventType;
import gov.hhs.fha.nhinc.common.eventcommon.XDRResponseEventType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;


/**
 *
 * @author rayj
 */
public class PolicyEngineTransformer {

    public CheckPolicyRequestType transformSubjectAddedToCheckPolicy(SubjectAddedEventType transformSubjectAddedToCheckPolicyRequest) {
        return SubjectAddedTransformHelper.transformSubjectAddedToCheckPolicy(transformSubjectAddedToCheckPolicyRequest);
    }

    public CheckPolicyRequestType transformPatDiscReqToCheckPolicy(PatDiscReqEventType transformPatDiscReqToCheckPolicyRequest) {
        return new PatientDiscoveryPolicyTransformHelper().transformPatientDiscoveryNhincToCheckPolicy(transformPatDiscReqToCheckPolicyRequest);
    }

    public CheckPolicyRequestType transformSubjectRevisedToCheckPolicy(SubjectRevisedEventType transformSubjectRevisedToCheckPolicyRequest) {
        return SubjectRevisedTransformHelper.transformSubjectRevisedToCheckPolicy(transformSubjectRevisedToCheckPolicyRequest);
    }

    public CheckPolicyRequestType transformAdhocQueryToCheckPolicy(AdhocQueryRequestEventType transformAdhocQueryToCheckPolicyRequest) {
        return AdhocQueryTransformHelper.transformAdhocQueryToCheckPolicy(transformAdhocQueryToCheckPolicyRequest);
    }

    public CheckPolicyRequestType transformAdhocQueryResultToCheckPolicy(AdhocQueryResultEventType transformAdhocQueryResultToCheckPolicyRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public CheckPolicyRequestType transformDocRetrieveToCheckPolicy(DocRetrieveEventType transformDocRetrieveToCheckPolicyRequest) {
        return DocRetrieveTransformHelper.transformDocRetrieveToCheckPolicy(transformDocRetrieveToCheckPolicyRequest);
    }

    public CheckPolicyRequestType transformDocRetrieveResultToCheckPolicy(DocRetrieveResultEventType transformDocRetrieveResultToCheckPolicyRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public CheckPolicyRequestType transformFindAuditEventsToCheckPolicy(FindAuditEventsEventType transformFindAuditEventsToCheckPolicyRequest) {
        return FindAuditEventsTransformHelper.transformFindAuditEventsToCheckPolicy(transformFindAuditEventsToCheckPolicyRequest);
    }

    public CheckPolicyRequestType transformSubjectReidentificationToCheckPolicy(SubjectReidentificationEventType transformSubjectReidentificationToCheckPolicyRequest) {
        return SubjectReidentificationTransformHelper.transformSubjectReidentificationToCheckPolicy(transformSubjectReidentificationToCheckPolicyRequest);
    }

    public CheckPolicyRequestType transformSubscribeToCheckPolicy(SubscribeEventType transformSubscribeToCheckPolicyRequest) {
        return SubscribeTransformHelper.transformSubscribeToCheckPolicy(transformSubscribeToCheckPolicyRequest);
    }

    public CheckPolicyRequestType transformUnsubscribeToCheckPolicy(UnsubscribeEventType transformUnsubscribeToCheckPolicyRequest) {
        return UnsubscribeTransformHelper.transformUnsubscribeToCheckPolicy(transformUnsubscribeToCheckPolicyRequest);
    }

    public CheckPolicyRequestType transformNotifyToCheckPolicy(NotifyEventType transformNotifyToCheckPolicyRequest) {
        return NotifyTransformHelper.transformNotifyToCheckPolicy(transformNotifyToCheckPolicyRequest);
    }
    public CheckPolicyRequestType transformXDRRequestToCheckPolicy(XDREventType request) {
        return new XDRPolicyTransformHelper().transformXDRToCheckPolicy(request);
    }
    public CheckPolicyRequestType transformXDRResponseInputToCheckPolicy(XDRResponseEventType request) {
        return new XDRPolicyTransformHelper().transformXDRResponseToCheckPolicy(request);
    }

}
