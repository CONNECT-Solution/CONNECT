/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.policy;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.eventcommon.NotifyEventType;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;

/**
 *
 * @author svalluripalli
 */
public class NotifyTransformHelper {

    private static final String ActionInValue = "HIEMNotifyIn";
    private static final String ActionOutValue = "HIEMNotifyOut";

    public static CheckPolicyRequestType transformNotifyToCheckPolicy(NotifyEventType event) {
        CheckPolicyRequestType genericPolicyRequest = new CheckPolicyRequestType();
        RequestType request = new RequestType();
        if (InboundOutboundChecker.IsInbound(event.getDirection())) {
            request.setAction(ActionHelper.actionFactory(ActionInValue));
        }
        if (InboundOutboundChecker.IsOutbound(event.getDirection())) {
            request.setAction(ActionHelper.actionFactory(ActionOutValue));
        }
        SubjectHelper subjHelp = new SubjectHelper();
        SubjectType subject = subjHelp.subjectFactory(event.getSendingHomeCommunity(), event.getMessage().getAssertion());
        request.getSubject().add(subject);

        AssertionHelper assertHelp = new AssertionHelper();
        assertHelp.appendAssertionDataToRequest(request, event.getMessage().getAssertion());

        genericPolicyRequest.setRequest(request);
        genericPolicyRequest.setAssertion(event.getMessage().getAssertion());
        return genericPolicyRequest;
    }
}
