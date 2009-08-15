/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.subjectdiscovery;

import gov.hhs.fha.nhinc.common.eventcommon.SubjectAddedEventType;
import gov.hhs.fha.nhinc.common.eventcommon.SubjectAddedMessageType;
import gov.hhs.fha.nhinc.common.eventcommon.SubjectReidentificationEventType;
import gov.hhs.fha.nhinc.common.eventcommon.SubjectReidentificationMessageType;
import gov.hhs.fha.nhinc.common.eventcommon.SubjectRevokedEventType;
import gov.hhs.fha.nhinc.common.eventcommon.SubjectRevokedMessageType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import gov.hhs.fha.nhinc.policyengine.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201301Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PatientTransforms;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;
import org.hl7.v3.PIXConsumerPRPAIN201301UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201302UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201303UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201309UVRequestType;
import org.hl7.v3.PRPAMT201301UVPatient;
import org.hl7.v3.PRPAMT201301UVPerson;

/**
 *
 * @author Jon Hoppesch
 */
public class SubjectDiscoveryPolicyChecker {

    private static Log log = LogFactory.getLog(SubjectDiscoveryPolicyChecker.class);

    public boolean check201301Policy(PIXConsumerPRPAIN201301UVRequestType message, II patIdOverride) {
        boolean policyIsValid = false;
        SubjectAddedMessageType request = new SubjectAddedMessageType();
        String oid = message.getPRPAIN201301UV().getReceiver().get(0).getDevice().getId().get(0).getRoot();
        log.info("Patient Id Right before Override: " + message.getPRPAIN201301UV().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension());
        log.info("AA Id Right before Override: " + message.getPRPAIN201301UV().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot());

        JAXBElement<PRPAMT201301UVPerson> patientPerson = HL7PatientTransforms.create201301PatientPerson(message.getPRPAIN201301UV().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getName().get(0), null, null, null);
        PRPAMT201301UVPatient patient = HL7PatientTransforms.create201301Patient(patientPerson, patIdOverride);
        request.setPRPAIN201301UV(HL7PRPA201301Transforms.createPRPA201301(patient, null, oid, oid));
        request.setAssertion(message.getAssertion());

        // Check to see if the patient id should be overwritten
//        if (patIdOverride != null) {
//            request.getPRPAIN201301UV().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().remove(0);
//            request.getPRPAIN201301UV().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().add(patIdOverride);
//        }
        log.info("Patient Id Right after Override: " + message.getPRPAIN201301UV().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension());
        log.info("AA Id Right after Override: " + message.getPRPAIN201301UV().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot());

        SubjectAddedEventType policyCheckReq = new SubjectAddedEventType();
        policyCheckReq.setDirection(NhincConstants.POLICYENGINE_INBOUND_DIRECTION);

        policyCheckReq.setMessage(request);

        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        CheckPolicyRequestType policyReq = policyChecker.checkPolicySubjectAdded(policyCheckReq);
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq);

        if (policyResp.getResponse() != null &&
                NullChecker.isNotNullish(policyResp.getResponse().getResult()) &&
                policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT) {
            policyIsValid = true;
        }

        return policyIsValid;
    }

    public boolean check201302Policy(PIXConsumerPRPAIN201302UVRequestType message) {
        boolean policyIsValid = false;

//        SubjectRevisedEventType policyCheckReq = new SubjectRevisedEventType();
//        policyCheckReq.setDirection(NhincConstants.POLICYENGINE_INBOUND_DIRECTION);
//        SubjectRevisedMessageType request = new SubjectRevisedMessageType();
//        request.setAssertion(message.getAssertion());
//        request.setPRPAIN201302UV(message.getPRPAIN201302UV());
//        policyCheckReq.setMessage(request);
//
//        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
//        CheckPolicyRequestType policyReq = policyChecker.checkPolicySubjectRevised(policyCheckReq);
//        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
//        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
//        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq);
//
//        if (policyResp.getResponse() != null &&
//                NullChecker.isNotNullish(policyResp.getResponse().getResult()) &&
//                policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT) {
//            policyIsValid = true;
//        }

        // For a 201302 we currently always return PERMIT
        return true;
    }

    public boolean check201303Policy(PIXConsumerPRPAIN201303UVRequestType message) {
        boolean policyIsValid = false;

        SubjectRevokedEventType policyCheckReq = new SubjectRevokedEventType();
        policyCheckReq.setDirection(NhincConstants.POLICYENGINE_INBOUND_DIRECTION);
        SubjectRevokedMessageType request = new SubjectRevokedMessageType();
        request.setAssertion(message.getAssertion());
        request.setPRPAIN201303UV(message.getPRPAIN201303UV());
        policyCheckReq.setMessage(request);

        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        CheckPolicyRequestType policyReq = policyChecker.checkPolicySubjectRevoked(policyCheckReq);
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq);

        if (policyResp.getResponse() != null &&
                NullChecker.isNotNullish(policyResp.getResponse().getResult()) &&
                policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT) {
            policyIsValid = true;
        }

        return policyIsValid;
    }

    public boolean check201309Policy(PIXConsumerPRPAIN201309UVRequestType message) {
        boolean policyIsValid = false;

        SubjectReidentificationEventType policyCheckReq = new SubjectReidentificationEventType();
        policyCheckReq.setDirection(NhincConstants.POLICYENGINE_INBOUND_DIRECTION);
        SubjectReidentificationMessageType request = new SubjectReidentificationMessageType();
        request.setAssertion(message.getAssertion());
        request.setPRPAIN201309UV(message.getPRPAIN201309UV());
        policyCheckReq.setMessage(request);

        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        CheckPolicyRequestType policyReq = policyChecker.checkPolicySubjectReidentification(policyCheckReq);
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq);

        if (policyResp.getResponse() != null &&
                NullChecker.isNotNullish(policyResp.getResponse().getResult()) &&
                policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT) {
            policyIsValid = true;
        }

        return policyIsValid;
    }
}
