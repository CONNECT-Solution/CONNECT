/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.transform.policy;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.eventcommon.SubjectRevokedEventType;
import gov.hhs.fha.nhinc.common.eventcommon.SubjectRevokedMessageType;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201303UV;
import org.hl7.v3.PRPAIN201303UVMFMIMT700701UV01Subject1;
/**
 *
 * @author svalluripalli
 */
public class SubjectRevokedTransformHelper {
    private static final String ActionValue = "SubjectRevokedIn";
    private static final String PatientAssigningAuthorityAttributeId = Constants.AssigningAuthorityAttributeId;
    private static final String PatientIdAttributeId = Constants.ResourceIdAttributeId;
    
    public static CheckPolicyRequestType transformSubjectRevokedToCheckPolicy(SubjectRevokedEventType event) {
        CheckPolicyRequestType genericPolicyRequest = new CheckPolicyRequestType();
        SubjectRevokedMessageType message = event.getMessage();
        PRPAIN201303UV subjectRevoked = message.getPRPAIN201303UV();
        RequestType request = new RequestType();
        request.setAction(ActionHelper.actionFactory(ActionValue));
        SubjectType subject = SubjectHelper.subjectFactory(event.getSendingHomeCommunity(), event.getMessage().getAssertion());
        request.getSubject().add(subject);
        II ii = extractPatientIdentifier(subjectRevoked);
        if (ii != null) {
            ResourceType resource = new ResourceType();
            resource.getAttribute().add(AttributeHelper.attributeFactory(PatientAssigningAuthorityAttributeId, Constants.DataTypeString, ii.getRoot()  ));
            resource.getAttribute().add(AttributeHelper.attributeFactory(PatientIdAttributeId, Constants.DataTypeString, ii.getExtension()));
            request.getResource().add(resource);
        }
        genericPolicyRequest.setRequest(request);
        return genericPolicyRequest;
    }
    
    private static II extractPatientIdentifier(PRPAIN201303UV message) {
        II ii = null;

        if ((message != null) && (message.getControlActProcess() != null)) {
            if (message.getControlActProcess().getSubject().size() >= 1) {
                PRPAIN201303UVMFMIMT700701UV01Subject1 subject;
                subject = message.getControlActProcess().getSubject().get(0);
                //todo: check each sub-class for null
                ii = subject.getRegistrationEvent().getSubject1().getPatient().getId().get(0);
            }
        }
        return ii;
    }
}
