/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.policydteproxy;

import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestEventType;
import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryResultEventType;
import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveEventType;
import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveResultEventType;
import gov.hhs.fha.nhinc.common.eventcommon.FindAuditEventsEventType;
import gov.hhs.fha.nhinc.common.eventcommon.NotifyEventType;
import gov.hhs.fha.nhinc.common.eventcommon.SubjectAddedEventType;
import gov.hhs.fha.nhinc.common.eventcommon.SubjectReidentificationEventType;
import gov.hhs.fha.nhinc.common.eventcommon.SubjectRevisedEventType;
import gov.hhs.fha.nhinc.common.eventcommon.SubscribeEventType;
import gov.hhs.fha.nhinc.common.eventcommon.UnsubscribeEventType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;

/**
 *
 * @author jhoppesc
 */
public interface IPolicyDteProxy {

    public CheckPolicyRequestType transformSubjectAddedToCheckPolicy(SubjectAddedEventType transformSubjectAddedToCheckPolicyRequest);

    public CheckPolicyRequestType transformSubjectRevisedToCheckPolicy(SubjectRevisedEventType transformSubjectRevisedToCheckPolicyRequest);

    public CheckPolicyRequestType transformAdhocQueryToCheckPolicy(AdhocQueryRequestEventType transformAdhocQueryToCheckPolicyRequest);

    public CheckPolicyRequestType transformAdhocQueryResultToCheckPolicy(AdhocQueryResultEventType transformAdhocQueryResultToCheckPolicyRequest);

    public CheckPolicyRequestType transformDocRetrieveToCheckPolicy(DocRetrieveEventType transformDocRetrieveToCheckPolicyRequest);

    public CheckPolicyRequestType transformDocRetrieveResultToCheckPolicy(DocRetrieveResultEventType transformDocRetrieveResultToCheckPolicyRequest);

    public CheckPolicyRequestType transformFindAuditEventsToCheckPolicy(FindAuditEventsEventType transformFindAuditEventsToCheckPolicyRequest);

    public CheckPolicyRequestType transformSubjectReidentificationToCheckPolicy(SubjectReidentificationEventType transformSubjectReidentificationToCheckPolicyRequest);

    public CheckPolicyRequestType transformSubscribeToCheckPolicy(SubscribeEventType transformSubscribeToCheckPolicyRequest);

    public CheckPolicyRequestType transformUnsubscribeToCheckPolicy(UnsubscribeEventType transformUnsubscribeToCheckPolicyRequest);

    public CheckPolicyRequestType transformNotifyToCheckPolicy(NotifyEventType transformNotifyToCheckPolicyRequest);
}
