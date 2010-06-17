/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.policy;

import gov.hhs.fha.nhinc.common.eventcommon.PatDiscReqEventType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import java.util.List;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;

/**
 * This class is used to transform 201305 request message to a CheckPolicyRequestType
 * @author svalluripalli
 */
public class PatientDiscoveryPolicyTransformHelper {

    private static Log log = null;
    private static final String ActionInValue = "PatientDiscoveryIn";
    private static final String ActionOutValue = "PatientDiscoveryOut";
    private static final String PatientAssigningAuthorityAttributeId = Constants.AssigningAuthorityAttributeId;
    private static final String PatientIdAttributeId = Constants.ResourceIdAttributeId;

    /**
     * Default Constructor
     */
    public PatientDiscoveryPolicyTransformHelper() {
        log = createLogger();
    }

    /**
     * Transform method to create a CheckPolicyRequest object from a 201306 message
     * @param request
     * @return CheckPolicyRequestType
     */
    public CheckPolicyRequestType transformPatientDiscoveryNhincToCheckPolicy(PatDiscReqEventType event) {
        addDebugLog("Begin -- PatientDiscoveryPolicyTransformHelper.transformPatientDiscoveryNhincToCheckPolicy()");
        CheckPolicyRequestType checkPolicyRequest = null;

        if (event == null) {
            addErrorLog("Request is null.");
            return checkPolicyRequest;
        } else {
            checkPolicyRequest = new CheckPolicyRequestType();
        }

        PRPAIN201306UV02 patDiscReq = event.getPRPAIN201306UV02();
        RequestType request = new RequestType();

        SubjectHelper subjHelp = new SubjectHelper();
        SubjectType subject = subjHelp.subjectFactory(event.getSendingHomeCommunity(), event.getAssertion());
        log.debug("transformPatientDiscoveryNhincToCheckPolicy - adding subject");
        request.getSubject().add(subject);

        II qualifiedPatientIdentifier = extractPatientIdentifier(patDiscReq);
        if (qualifiedPatientIdentifier != null) {
            ResourceType resource = new ResourceType();
            AttributeHelper attrHelper = new AttributeHelper();
            resource.getAttribute().add(attrHelper.attributeFactory(PatientAssigningAuthorityAttributeId, Constants.DataTypeString, qualifiedPatientIdentifier.getRoot()));
            String sStrippedPatientId = PatientIdFormatUtil.parsePatientId(qualifiedPatientIdentifier.getExtension());
            log.debug("transformPatientDiscoveryNhincToCheckPolicy: sStrippedPatientId = " + sStrippedPatientId);
            resource.getAttribute().add(attrHelper.attributeFactory(PatientIdAttributeId, Constants.DataTypeString, sStrippedPatientId));

            request.getResource().add(resource);
        }

        log.debug("transformPatientDiscoveryNhincToCheckPolicy - adding assertion data");
        AssertionHelper assertHelp = new AssertionHelper();
        assertHelp.appendAssertionDataToRequest(request, event.getAssertion());

        request.setAction(ActionHelper.actionFactory(ActionInValue));

        checkPolicyRequest.setRequest(request);
        checkPolicyRequest.setAssertion(event.getAssertion());
        addDebugLog("End -- PatientDiscoveryPolicyTransformHelper.transformPatientDiscoveryNhincToCheckPolicy()");
        return checkPolicyRequest;
    }

    /**
     * Transform method to create a CheckPolicyRequest object from a 201305 request
     * @param request
     * @return CheckPolicyRequestType
     */
    public CheckPolicyRequestType transformPatientDiscoveryEntityToCheckPolicy(RespondingGatewayPRPAIN201305UV02RequestType event) {
        addDebugLog("Begin -- PatientDiscoveryPolicyTransformHelper.transformPatientDiscoveryEntityToCheckPolicy()");

        CheckPolicyRequestType checkPolicyRequest = null;

        if (event == null) {
            addErrorLog("Request is null.");
            return checkPolicyRequest;
        } else {
            checkPolicyRequest = new CheckPolicyRequestType();
        }

        PRPAIN201305UV02 patDiscReq = event.getPRPAIN201305UV02();
        //RequestType request = getRequestType(patDiscReq, event.getAssertion());
        RequestType request = new RequestType();

        SubjectHelper subjHelp = new SubjectHelper();
        //SubjectType subject = subjHelp.subjectFactory(event.getAssertion().getHomeCommunity(), event.getAssertion());
        SubjectType subject = new SubjectType();
        subject.setSubjectCategory(SubjectHelper.SubjectCategory);
        log.debug("transformPatientDiscoveryNhincToCheckPolicy - adding subject");
        request.getSubject().add(subject);

        II qualifiedPatientIdentifier = extractPatientIdentifier(patDiscReq);
        if (qualifiedPatientIdentifier != null) {
            ResourceType resource = new ResourceType();
            AttributeHelper attrHelper = new AttributeHelper();
            resource.getAttribute().add(attrHelper.attributeFactory(PatientAssigningAuthorityAttributeId, Constants.DataTypeString, qualifiedPatientIdentifier.getRoot()));
            String sStrippedPatientId = PatientIdFormatUtil.parsePatientId(qualifiedPatientIdentifier.getExtension());
            log.debug("transformPatientDiscoveryNhincToCheckPolicy: sStrippedPatientId = " + sStrippedPatientId);
            resource.getAttribute().add(attrHelper.attributeFactory(PatientIdAttributeId, Constants.DataTypeString, sStrippedPatientId));

            HomeCommunityType homeCommunityId = null;

            if (event != null &&
                    event.getPRPAIN201305UV02() != null &&
                    NullChecker.isNotNullish(event.getPRPAIN201305UV02().getReceiver()) &&
                    event.getPRPAIN201305UV02().getReceiver().get(0) != null &&
                    event.getPRPAIN201305UV02().getReceiver().get(0).getDevice() != null &&
                    event.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getAsAgent() != null &&
                    event.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getAsAgent().getValue() != null &&
                    event.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
                    event.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
                    NullChecker.isNotNullish(event.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
                    event.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null &&
                    NullChecker.isNotNullish(event.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
                homeCommunityId = new HomeCommunityType();
                homeCommunityId.setHomeCommunityId(event.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot());
            }
            resource.getAttribute().add(attrHelper.attributeFactory(Constants.HomeCommunityAttributeId, Constants.DataTypeString, subjHelp.determineSendingHomeCommunityId(homeCommunityId, event.getAssertion())));

            request.getResource().add(resource);
        }

        log.debug("transformPatientDiscoveryNhincToCheckPolicy - adding assertion data");
        AssertionHelper assertHelp = new AssertionHelper();
        assertHelp.appendAssertionDataToRequest(request, event.getAssertion());

        request.setAction(ActionHelper.actionFactory(ActionOutValue));

        checkPolicyRequest.setRequest(request);
        checkPolicyRequest.setAssertion(event.getAssertion());


        addDebugLog("End -- PatientDiscoveryPolicyTransformHelper.transformPatientDiscoveryEntityToCheckPolicy()");
        return checkPolicyRequest;
    }

    protected HomeCommunityType getHomeCommunityFrom201305(RespondingGatewayPRPAIN201305UV02RequestType event) {
        HomeCommunityType senderHomeCommunity = new HomeCommunityType();

        if (event != null &&
                event.getPRPAIN201305UV02().getSender() != null &&
                event.getPRPAIN201305UV02().getSender().getDevice() != null &&
                event.getPRPAIN201305UV02().getSender().getDevice().getAsAgent() != null &&
                event.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue() != null &&
                event.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
                event.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
                NullChecker.isNotNullish(event.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
                event.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null &&
                NullChecker.isNotNullish(event.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
            senderHomeCommunity.setHomeCommunityId(event.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot());
        }

        return senderHomeCommunity;
    }

    protected HomeCommunityType getHomeCommunityFrom201306(PRPAIN201306UV02 event) {
        HomeCommunityType senderHomeCommunity = new HomeCommunityType();

        if (event != null &&
                event.getSender() != null &&
                event.getSender().getDevice() != null &&
                event.getSender().getDevice().getAsAgent() != null &&
                event.getSender().getDevice().getAsAgent().getValue() != null &&
                event.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
                event.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
                NullChecker.isNotNullish(event.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
                event.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null &&
                NullChecker.isNotNullish(event.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
            senderHomeCommunity.setHomeCommunityId(event.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot());
        }

        return senderHomeCommunity;
    }

    protected HomeCommunityType getHomeCommunityFrom201306(PRPAIN201305UV02 event) {
        HomeCommunityType senderHomeCommunity = new HomeCommunityType();

        if (event != null &&
                event.getSender() != null &&
                event.getSender().getDevice() != null &&
                event.getSender().getDevice().getAsAgent() != null &&
                event.getSender().getDevice().getAsAgent().getValue() != null &&
                event.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
                event.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
                NullChecker.isNotNullish(event.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
                event.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null &&
                NullChecker.isNotNullish(event.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
            senderHomeCommunity.setHomeCommunityId(event.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot());
        }

        return senderHomeCommunity;
    }

    protected RequestType getRequestType(RespondingGatewayPRPAIN201305UV02RequestType event) {
        addDebugLog("Begin -- PatientDiscoveryPolicyTransformHelper.getRequestType()");
        RequestType request = new RequestType();
        setSubjectToRequestType(event, request);
        ResourceType resource = null;
        AttributeHelper attrHelper = new AttributeHelper();
        II ii = extractPatientIdentifier(event.getPRPAIN201305UV02());
        if (ii != null) {
            resource = new ResourceType();
            resource.getAttribute().add(attrHelper.attributeFactory(PatientAssigningAuthorityAttributeId, Constants.DataTypeString, ii.getRoot()));
            String sStrippedPatientId = PatientIdFormatUtil.parsePatientId(ii.getExtension());
            log.debug("transformSubjectAddedInToCheckPolicy: sStrippedPatientId = " + sStrippedPatientId);
            resource.getAttribute().add(attrHelper.attributeFactory(PatientIdAttributeId, Constants.DataTypeString, sStrippedPatientId));
            request.getResource().add(resource);
        }
        request.getResource().add(resource);
        addDebugLog("End -- PatientDiscoveryPolicyTransformHelper.getRequestType()");
        return request;
    }

    protected RequestType getRequestType(PRPAIN201306UV02 event, AssertionType assertion) {
        addDebugLog("Begin -- PatientDiscoveryPolicyTransformHelper.getRequestType()");
        RequestType request = new RequestType();

        setSubjectToRequestType(event, request, assertion);

        ResourceType resource = null;
        AttributeHelper attrHelper = new AttributeHelper();
        II ii = extractPatientIdentifier(event);
        if (ii != null) {
            resource = new ResourceType();
            resource.getAttribute().add(attrHelper.attributeFactory(PatientAssigningAuthorityAttributeId, Constants.DataTypeString, ii.getRoot()));
            String sStrippedPatientId = PatientIdFormatUtil.parsePatientId(ii.getExtension());
            log.debug("transformSubjectAddedInToCheckPolicy: sStrippedPatientId = " + sStrippedPatientId);
            resource.getAttribute().add(attrHelper.attributeFactory(PatientIdAttributeId, Constants.DataTypeString, sStrippedPatientId));
            request.getResource().add(resource);
        }
        request.getResource().add(resource);
        addDebugLog("End -- PatientDiscoveryPolicyTransformHelper.getRequestType()");
        return request;
    }

    protected RequestType getRequestType(PRPAIN201305UV02 event, AssertionType assertion) {
        addDebugLog("Begin -- PatientDiscoveryPolicyTransformHelper.getRequestType()");
        RequestType request = new RequestType();

        setSubjectToRequestType(event, request, assertion);

        ResourceType resource = null;
        AttributeHelper attrHelper = new AttributeHelper();
        II ii = extractPatientIdentifier(event);
        if (ii != null) {
            resource = new ResourceType();
            resource.getAttribute().add(attrHelper.attributeFactory(PatientAssigningAuthorityAttributeId, Constants.DataTypeString, ii.getRoot()));
            String sStrippedPatientId = PatientIdFormatUtil.parsePatientId(ii.getExtension());
            log.debug("transformSubjectAddedInToCheckPolicy: sStrippedPatientId = " + sStrippedPatientId);
            resource.getAttribute().add(attrHelper.attributeFactory(PatientIdAttributeId, Constants.DataTypeString, sStrippedPatientId));
            request.getResource().add(resource);
        }
        request.getResource().add(resource);
        addDebugLog("End -- PatientDiscoveryPolicyTransformHelper.getRequestType()");
        return request;
    }

    protected void setSubjectToRequestType(RespondingGatewayPRPAIN201305UV02RequestType event, RequestType request) {
        //change to get the info from the receiver node
        HomeCommunityType receiverHomeCommunity = getReceiverHomeCommunityFrom201305(event);
        SubjectType subject = new SubjectHelper().subjectFactory(receiverHomeCommunity, event.getAssertion());
        request.getSubject().add(subject);
    }

    protected void setSubjectToRequestType(PRPAIN201306UV02 event, RequestType request, AssertionType assertion) {
        HomeCommunityType senderHomeCommunity = getHomeCommunityFrom201306(event);
        SubjectType subject = new SubjectHelper().subjectFactory(senderHomeCommunity, assertion);
        request.getSubject().add(subject);
    }

    protected void setSubjectToRequestType(PRPAIN201305UV02 event, RequestType request, AssertionType assertion) {
        HomeCommunityType senderHomeCommunity = getHomeCommunityFrom201306(event);
        SubjectType subject = new SubjectHelper().subjectFactory(senderHomeCommunity, assertion);
        request.getSubject().add(subject);
    }

    /**
     * log4j info messages are logged here
     * @param infoMessage
     */
    private void addInfoLog(String infoMessage) {
        log.info(infoMessage);
    }

    /**
     * log4j error messages are logged here
     * @param errorMessage
     */
    private void addErrorLog(String errorMessage) {
        log.error(errorMessage);
    }

    /**
     * log4j debug messages are logged here
     * @param debugMessage
     */
    private void addDebugLog(String debugMessage) {
        log.debug(debugMessage);
    }

    /**
     * Instantiating log4j logger
     * @return
     */
    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    private static II extractPatientIdentifier(PRPAIN201305UV02 message) {
        II ii = null;
        String assigningAuthority = null;

        if (message.getControlActProcess() != null &&
                NullChecker.isNotNullish(message.getControlActProcess().getAuthorOrPerformer()) &&
                message.getControlActProcess().getAuthorOrPerformer().get(0) != null &&
                message.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice() != null &&
                message.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue() != null &&
                NullChecker.isNotNullish(message.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId()) &&
                message.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId().get(0) != null &&
                NullChecker.isNotNullish(message.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId().get(0).getRoot())) {
            assigningAuthority = message.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId().get(0).getRoot();
        }

        if ((message != null) && (message.getControlActProcess() != null) && (message.getControlActProcess().getQueryByParameter() != null)) {
            JAXBElement<PRPAMT201306UV02QueryByParameter> queryParam = message.getControlActProcess().getQueryByParameter();
            PRPAMT201306UV02QueryByParameter queryParam201306 = queryParam.getValue();
            if (queryParam201306.getParameterList() != null && queryParam201306.getParameterList().getLivingSubjectId() != null) {
                List<PRPAMT201306UV02LivingSubjectId> livingSubjectIdList = queryParam201306.getParameterList().getLivingSubjectId();
                if (NullChecker.isNotNullish(livingSubjectIdList)) {
                    for (PRPAMT201306UV02LivingSubjectId livingSubId : livingSubjectIdList) {
                        for (II id : livingSubId.getValue()) {
                            if (id != null &&
                                    NullChecker.isNotNullish(id.getExtension()) &&
                                    NullChecker.isNotNullish(id.getRoot()) &&
                                    id.getRoot().equalsIgnoreCase(assigningAuthority)) {
                                ii = id;

                                // Break out of the inner loop
                                break;
                            }
                        }

                        // If the id was found break out of the outer loop
                        if (ii != null) {
                            break;
                        }
                    }
                }
            }
        }
        return ii;
    }

    private static II extractPatientIdentifier(PRPAIN201306UV02 message) {
        II ii = null;

        if (message != null &&
                message.getControlActProcess() != null &&
                NullChecker.isNotNullish(message.getControlActProcess().getSubject()) &&
                message.getControlActProcess().getSubject().get(0) != null &&
                message.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null &&
                NullChecker.isNotNullish(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId()) &&
                message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0) != null) {
            ii = message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0);
        }
        return ii;
    }

    private HomeCommunityType getReceiverHomeCommunityFrom201305(RespondingGatewayPRPAIN201305UV02RequestType event) {
        HomeCommunityType receiverHomeCommunity = new HomeCommunityType();

        if (event != null &&
                event.getPRPAIN201305UV02().getReceiver() != null &&
                !event.getPRPAIN201305UV02().getReceiver().isEmpty() &&
                event.getPRPAIN201305UV02().getReceiver().get(0) != null &&
                event.getPRPAIN201305UV02().getReceiver().get(0).getDevice() != null &&
                event.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getId() != null &&
                event.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getId().get(0) != null &&
                NullChecker.isNotNullish(event.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getId().get(0).getRoot())) {
            receiverHomeCommunity.setHomeCommunityId(event.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getId().get(0).getRoot());
        }

        return receiverHomeCommunity;

    }
}
