/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.policy;

import gov.hhs.fha.nhinc.common.eventcommon.SubjectAddedEventType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201301UV;
import org.hl7.v3.PRPAIN201301UVMFMIMT700701UV01Subject1;

/**
 *
 * @author rayj
 */
public class SubjectAddedTransformHelper {

    private static final String ActionValue = "SubjectDiscoveryIn";
    private static final String PatientAssigningAuthorityAttributeId = Constants.AssigningAuthorityAttributeId;
    private static final String PatientIdAttributeId = Constants.ResourceIdAttributeId;

    public static CheckPolicyRequestType transformSubjectAddedToCheckPolicy(SubjectAddedEventType event) {
        return transformSubjectAddedInToCheckPolicy(event);
    }

    public static CheckPolicyRequestType transformSubjectAddedInToCheckPolicy(SubjectAddedEventType event) {
        CheckPolicyRequestType genericPolicyRequest = new CheckPolicyRequestType();
        PRPAIN201301UV subjectAdded = event.getMessage().getPRPAIN201301UV();
        RequestType request = new RequestType();
        request.setAction(ActionHelper.actionFactory(ActionValue));

        SubjectType subject = SubjectHelper.subjectFactory(event.getSendingHomeCommunity(), event.getMessage().getAssertion());
        request.getSubject().add(subject);

        II ii = extractPatientIdentifier(subjectAdded);
        if (ii != null) {
            ResourceType resource = new ResourceType();
            resource.getAttribute().add(AttributeHelper.attributeFactory(PatientAssigningAuthorityAttributeId, Constants.DataTypeString, ii.getRoot()  ));
            resource.getAttribute().add(AttributeHelper.attributeFactory(PatientIdAttributeId, Constants.DataTypeString, ii.getExtension()));
            request.getResource().add(resource);
        }
        CheckPolicyRequestType oPolicyRequest = new CheckPolicyRequestType();
        oPolicyRequest.setRequest(request);
        PurposeForUseHelper.appendPurposeForUse(oPolicyRequest, event.getMessage().getAssertion());
        genericPolicyRequest.setRequest(request);
        genericPolicyRequest.setAssertion(event.getMessage().getAssertion());
        return genericPolicyRequest;
    }

    private static II extractPatientIdentifier(PRPAIN201301UV message) {
        II ii = null;

        if ((message != null) && (message.getControlActProcess() != null)) {
            if (message.getControlActProcess().getSubject().size() >= 1) {
                PRPAIN201301UVMFMIMT700701UV01Subject1 subject;
                subject = message.getControlActProcess().getSubject().get(0);
                //todo: check each sub-class for null
                ii = subject.getRegistrationEvent().getSubject1().getPatient().getId().get(0);
            }
        }
        return ii;
    }
}
