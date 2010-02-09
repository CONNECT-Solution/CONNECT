/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.policy;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.eventcommon.UnsubscribeEventType;
import gov.hhs.fha.nhinc.common.eventcommon.UnsubscribeMessageType;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;

/**
 *
 * @author svalluripalli
 */
public class UnsubscribeTransformHelper {

    private static final String ActionInValue = "HIEMSubscriptionCancelIn";
    private static final String ActionOutValue = "HIEMSubscriptionCancelOut";

    public static CheckPolicyRequestType transformUnsubscribeToCheckPolicy(UnsubscribeEventType event) {
        CheckPolicyRequestType genericPolicyRequest = new CheckPolicyRequestType();
        UnsubscribeMessageType message = event.getMessage();
        RequestType request = new RequestType();
        if (InboundOutboundChecker.IsInbound(event.getDirection())) {
            request.setAction(ActionHelper.actionFactory(ActionInValue));
        }
        if (InboundOutboundChecker.IsOutbound(event.getDirection())) {
            request.setAction(ActionHelper.actionFactory(ActionOutValue));
        }
        SubjectHelper subjHelp = new SubjectHelper();
        SubjectType subject = subjHelp.subjectFactory(event.getSendingHomeCommunity(), event.getMessage().getAssertion());

        AssertionHelper assertHelp = new AssertionHelper();
        assertHelp.appendAssertionDataToRequest(request, event.getMessage().getAssertion());


        request.getSubject().add(subject);
        genericPolicyRequest.setRequest(request);
        genericPolicyRequest.setAssertion(event.getMessage().getAssertion());
        return genericPolicyRequest;
    }
}
