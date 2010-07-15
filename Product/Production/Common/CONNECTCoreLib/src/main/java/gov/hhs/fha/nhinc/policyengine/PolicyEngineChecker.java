/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.policyengine;

import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestEventType;
import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveEventType;
import gov.hhs.fha.nhinc.common.eventcommon.FindAuditEventsEventType;
import gov.hhs.fha.nhinc.common.eventcommon.NotifyEventType;
import gov.hhs.fha.nhinc.common.eventcommon.PatDiscReqEventType;
import gov.hhs.fha.nhinc.common.eventcommon.SubjectAddedEventType;
import gov.hhs.fha.nhinc.common.eventcommon.SubjectReidentificationEventType;
import gov.hhs.fha.nhinc.common.eventcommon.SubjectRevisedEventType;
import gov.hhs.fha.nhinc.common.eventcommon.SubscribeEventType;
import gov.hhs.fha.nhinc.common.eventcommon.UnsubscribeEventType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.transform.policy.PolicyEngineTransformer;
import gov.hhs.fha.nhinc.common.eventcommon.XDREventType;
import gov.hhs.fha.nhinc.common.eventcommon.XDRResponseEventType;

/**
 *
 * @author Jon Hoppesch
 */
public class PolicyEngineChecker {

    /**
     * This method will create the generic Policy Check Request Message from
     * a subject discovery announce request
     *
     * @param request Policy check request message for the subject discovery announce
     * @return A generic policy check request message that can be passed to the Policy Engine
     */
    public CheckPolicyRequestType checkPolicySubjectAdded(SubjectAddedEventType request) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformSubjectAddedToCheckPolicy(request);
    }

    /**
     * This method will create the generic Policy Check Request Message from
     * a subject discovery announce request
     *
     * @param request Policy check request message for the subject discovery announce
     * @return A generic policy check request message that can be passed to the Policy Engine
     */
    public CheckPolicyRequestType checkPolicyPatDiscRequest(PatDiscReqEventType request) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformPatDiscReqToCheckPolicy(request);
    }

    /**
     * This method will create the generic Policy Check Request Message from
     * a subject discovery revised request
     *
     * @param request Policy check request message for the subject discovery revised
     * @return A generic policy check request message that can be passed to the Policy Engine
     */
    public CheckPolicyRequestType checkPolicySubjectRevised(SubjectRevisedEventType request) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformSubjectRevisedToCheckPolicy(request);
    }

    /**
     * This method will create the generic Policy Check Request Message from
     * a subject discovery reidentification request
     *
     * @param request Policy check request message for the subject discovery reidentification
     * @return A generic policy check request message that can be passed to the Policy Engine
     */
    public CheckPolicyRequestType checkPolicySubjectReidentification(SubjectReidentificationEventType request) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformSubjectReidentificationToCheckPolicy(request);
    }

    /**
     * This method will create the generic Policy Check Request Message from
     * a document query request
     *
     * @param request Policy check request message for the document query
     * @return A generic policy check request message that can be passed to the Policy Engine
     */
    public CheckPolicyRequestType checkPolicyAdhocQuery(AdhocQueryRequestEventType request) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformAdhocQueryToCheckPolicy(request);
    }

    /**
     * This method will create the generic Policy Check Request Message from
     * a document retrieve request
     *
     * @param request Policy check request message for the document retrieve
     * @return A generic policy check request message that can be passed to the Policy Engine
     */
    public CheckPolicyRequestType checkPolicyDocRetrieve(DocRetrieveEventType request) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformDocRetrieveToCheckPolicy(request);
    }

    /**
     * This method will create the generic Policy Check Request Message from
     * an audit query request
     *
     * @param request Policy check request message for the audit query
     * @return A generic policy check request message that can be passed to the Policy Engine
     */
    public CheckPolicyRequestType checkPolicyFindAuditEvents(FindAuditEventsEventType request) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformFindAuditEventsToCheckPolicy(request);
    }

    /**
     * This method will create the generic Policy Check Request Message from
     * a subscribe request
     *
     * @param request Policy check request message for the subscribe
     * @return A generic policy check request message that can be passed to the Policy Engine
     */
    public CheckPolicyRequestType checkPolicySubscribe(SubscribeEventType checkPolicySubscribeRequest) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformSubscribeToCheckPolicy(checkPolicySubscribeRequest);
    }

    /**
     * This method will create the generic Policy Check Request Message from
     * an unsubscribe request.
     *
     * @param request Policy check request message for the unsubscribe
     * @return A generic policy check request message that can be passed to the Policy Engine
     */
    public CheckPolicyRequestType checkPolicyUnsubscribe(UnsubscribeEventType request) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformUnsubscribeToCheckPolicy(request);
    }

    /**
     * This method will create the generic Policy Check Request Message from
     * a notify request
     *
     * @param request Policy check request message for the notify request
     * @return A generic policy check request message that can be passed to the Policy Engine
     */
    public CheckPolicyRequestType checkPolicyNotify(NotifyEventType request) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformNotifyToCheckPolicy(request);
    }
    public CheckPolicyRequestType checkPolicyXDRRequest(XDREventType request) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformXDRRequestToCheckPolicy(request);
    }

    public CheckPolicyRequestType checkPolicyXDRResponse(XDRResponseEventType request) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformXDRResponseInputToCheckPolicy(request);
    }

}
