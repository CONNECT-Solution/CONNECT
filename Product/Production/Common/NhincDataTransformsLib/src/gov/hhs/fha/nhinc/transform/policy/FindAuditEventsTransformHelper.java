/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.policy;

import com.services.nhinc.schema.auditmessage.FindAuditEventsType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.eventcommon.FindAuditEventsEventType;
import gov.hhs.fha.nhinc.common.eventcommon.FindAuditEventsMessageType;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;

/**
 *
 * @author svalluripalli
 */
public class FindAuditEventsTransformHelper {

    private static final String ActionInValue = "AuditLogQueryIn";
    private static final String ActionOutValue = "AuditLogQueryOut";
    private static final String PatientIdAttributeId = Constants.ResourceIdAttributeId;

    public static CheckPolicyRequestType transformFindAuditEventsToCheckPolicy(FindAuditEventsEventType event) {
        CheckPolicyRequestType genericPolicyRequest = new CheckPolicyRequestType();
        RequestType request = new RequestType();
        
        if (event != null) {
            if (InboundOutboundChecker.IsInbound(event.getDirection())) {
                request.setAction(ActionHelper.actionFactory(ActionInValue));
            }
            if (InboundOutboundChecker.IsOutbound(event.getDirection())) {
                request.setAction(ActionHelper.actionFactory(ActionOutValue));
            }
            SubjectType subject = SubjectHelper.subjectFactory(event.getSendingHomeCommunity(), event.getMessage().getAssertion());
            request.getSubject().add(subject);
            FindAuditEventsMessageType message = event.getMessage();
            if (message != null) {
                FindAuditEventsType findAudit = message.getFindAuditEvents();
                if(findAudit != null)
                {
                    findAudit.getPatientId();
                    ResourceType resource = new ResourceType();
                    resource.getAttribute().add(AttributeHelper.attributeFactory(PatientIdAttributeId, Constants.DataTypeString, findAudit.getPatientId()));
                    request.getResource().add(resource);    
                }
            }
        }
        genericPolicyRequest.setRequest(request);
        return genericPolicyRequest;
    }
}
