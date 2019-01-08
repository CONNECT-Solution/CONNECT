/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package gov.hhs.fha.nhinc.transform.policy;

import gov.hhs.fha.nhinc.common.eventcommon.PatDiscReqEventType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import java.util.List;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * This class is used to transform 201305 request message to a CheckPolicyRequestType.
 *
 * @author svalluripalli
 */
public class PatientDiscoveryPolicyTransformHelper {

    private static final Logger LOG = LoggerFactory.getLogger(PatientDiscoveryPolicyTransformHelper.class);
    private static final String ACTION_IN_VALUE = "PatientDiscoveryIn";
    private static final String ACTION_OUT_VALUE = "PatientDiscoveryOut";
    private static final String PATIENT_ASSIGNING_AUTHORITY_ATTRIBUTE_ID = Constants.AssigningAuthorityAttributeId;
    private static final String PATIENT_ID_ATTRIBUTE_ID = Constants.ResourceIdAttributeId;

    /**
     * Transform method to create a CheckPolicyRequest object from a 201306 message.
     *
     * @param event
     * @return CheckPolicyRequestType
     */
    public CheckPolicyRequestType transformPatientDiscoveryNhincToCheckPolicy(PatDiscReqEventType event) {
        CheckPolicyRequestType checkPolicyRequest = null;

        if (event == null) {
            LOG.error("Request is null.");

            return checkPolicyRequest;
        } else {
            checkPolicyRequest = new CheckPolicyRequestType();
        }

        PRPAIN201306UV02 patDiscReq = event.getPRPAIN201306UV02();
        RequestType request = new RequestType();
        SubjectHelper subjHelp = new SubjectHelper();
        SubjectType subject = subjHelp.subjectFactory(event.getSendingHomeCommunity(), event.getAssertion());

        LOG.debug("Adding subject.");

        request.getSubject().add(subject);

        II qualifiedPatientIdentifier = extractPatientIdentifier(patDiscReq);

        if (qualifiedPatientIdentifier != null) {
            ResourceType resource = new ResourceType();
            AttributeHelper attrHelper = new AttributeHelper();

            resource.getAttribute().add(attrHelper.attributeFactory(PATIENT_ASSIGNING_AUTHORITY_ATTRIBUTE_ID,
                Constants.DataTypeString, qualifiedPatientIdentifier.getRoot()));

            String sStrippedPatientId = PatientIdFormatUtil.parsePatientId(qualifiedPatientIdentifier.getExtension());

            LOG.debug("transformPatientDiscoveryNhincToCheckPolicy: sStrippedPatientId = " + sStrippedPatientId);

            resource.getAttribute().add(
                attrHelper.attributeFactory(PATIENT_ID_ATTRIBUTE_ID, Constants.DataTypeString, sStrippedPatientId));

            request.getResource().add(resource);

        }

        LOG.debug("Adding assertion data.");

        AssertionHelper assertHelp = new AssertionHelper();
        assertHelp.appendAssertionDataToRequest(request, event.getAssertion());

        request.setAction(ActionHelper.actionFactory(ACTION_IN_VALUE));

        checkPolicyRequest.setRequest(request);
        checkPolicyRequest.setAssertion(event.getAssertion());

        return checkPolicyRequest;
    }

    /**
     * Transform method to create a CheckPolicyRequest object from a 201305 request
     *
     * @param prpain201305UV02
     * @param assertion
     * @return CheckPolicyRequestType
     */
    public CheckPolicyRequestType transformPRPAIN201305UV02ToCheckPolicy(PRPAIN201305UV02 prpain201305UV02,
        AssertionType assertion) {

        CheckPolicyRequestType checkPolicyRequest = null;

        if (prpain201305UV02 == null) {
            LOG.error("Request is null.");

            return checkPolicyRequest;
        } else {
            checkPolicyRequest = new CheckPolicyRequestType();
        }

        RequestType request = new RequestType();
        SubjectHelper subjHelp = new SubjectHelper();

        SubjectType subject = new SubjectType();
        subject.setSubjectCategory(SubjectHelper.SubjectCategory);

        LOG.debug("Adding subject.");

        request.getSubject().add(subject);

        ResourceType resource = new ResourceType();
        AttributeHelper attrHelper = new AttributeHelper();

        II qualifiedPatientIdentifier = extractPatientIdentifier(prpain201305UV02);

        if (qualifiedPatientIdentifier != null) {
            resource.getAttribute().add(attrHelper.attributeFactory(PATIENT_ASSIGNING_AUTHORITY_ATTRIBUTE_ID,
                Constants.DataTypeString, qualifiedPatientIdentifier.getRoot()));

            String sStrippedPatientId = PatientIdFormatUtil.parsePatientId(qualifiedPatientIdentifier.getExtension());

            LOG.debug("transformPRPAIN201305UV02ToCheckPolicy: sStrippedPatientId = " + sStrippedPatientId);

            resource.getAttribute().add(
                attrHelper.attributeFactory(PATIENT_ID_ATTRIBUTE_ID, Constants.DataTypeString, sStrippedPatientId));
        }

        HomeCommunityType homeCommunityId = extractHomeCommunityId(prpain201305UV02);

        if (homeCommunityId != null) {
            resource.getAttribute().add(attrHelper.attributeFactory(Constants.HomeCommunityAttributeId,
                Constants.DataTypeString, subjHelp.determineSendingHomeCommunityId(homeCommunityId, assertion)));
        }

        if (!resource.getAttribute().isEmpty()) {
            request.getResource().add(resource);
        }

        LOG.debug("transformPRPAIN201305UV02ToCheckPolicy - adding assertion data");

        AssertionHelper assertHelp = new AssertionHelper();
        assertHelp.appendAssertionDataToRequest(request, assertion);

        request.setAction(ActionHelper.actionFactory(ACTION_OUT_VALUE));

        checkPolicyRequest.setRequest(request);
        checkPolicyRequest.setAssertion(assertion);

        return checkPolicyRequest;
    }

    /**
     * Extraction method to retrieve HomeCommunityId from message
     *
     * @param PRPAIN201305UV02 prpain201305UV02
     * @return HomeCommunityType
     */
    private static HomeCommunityType extractHomeCommunityId(PRPAIN201305UV02 prpain201305UV02) {
        HomeCommunityType homeCommunityId = null;

        if (prpain201305UV02 != null && NullChecker.isNotNullish(prpain201305UV02.getReceiver())
            && prpain201305UV02.getReceiver().get(0) != null
            && prpain201305UV02.getReceiver().get(0).getDevice() != null
            && prpain201305UV02.getReceiver().get(0).getDevice().getId() != null
            && prpain201305UV02.getReceiver().get(0).getDevice().getId().get(0) != null
            && NullChecker.isNotNullish(prpain201305UV02.getReceiver().get(0).getDevice().getId().get(0).getRoot())) {

            homeCommunityId = new HomeCommunityType();

            homeCommunityId
                .setHomeCommunityId(prpain201305UV02.getReceiver().get(0).getDevice().getId().get(0).getRoot());
        }

        return homeCommunityId;
    }

    /**
     * Transform method to create a CheckPolicyRequest object from a 201305 request
     *
     * @param event
     * @param request
     * @return CheckPolicyRequestType
     */
    public CheckPolicyRequestType transformPatientDiscoveryEntityToCheckPolicy(
        RespondingGatewayPRPAIN201305UV02RequestType event) {

        LOG.debug("Begin -- PatientDiscoveryPolicyTransformHelper.transformPatientDiscoveryEntityToCheckPolicy()");

        if (event == null) {

            LOG.error("Request is null.");

            return null;

        }

        CheckPolicyRequestType checkPolicyRequest = transformPRPAIN201305UV02ToCheckPolicy(event.getPRPAIN201305UV02(),
            event.getAssertion());

        LOG.debug("End -- PatientDiscoveryPolicyTransformHelper.transformPatientDiscoveryEntityToCheckPolicy()");

        return checkPolicyRequest;

    }

    protected HomeCommunityType getHomeCommunityFrom201305(RespondingGatewayPRPAIN201305UV02RequestType event) {
        HomeCommunityType senderHomeCommunity = new HomeCommunityType();

        if (event != null && event.getPRPAIN201305UV02().getSender() != null
            && event.getPRPAIN201305UV02().getSender().getDevice() != null
            && event.getPRPAIN201305UV02().getSender().getDevice().getAsAgent() != null
            && event.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue() != null
            && event.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue()
                .getRepresentedOrganization() != null
            && event.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization()
                .getValue() != null
            && NullChecker.isNotNullish(event.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue()
                .getRepresentedOrganization().getValue().getId())
            && event.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization()
                .getValue().getId().get(0) != null
            && NullChecker.isNotNullish(event.getPRPAIN201305UV02().getSender().getDevice().getAsAgent().getValue()
                .getRepresentedOrganization().getValue().getId().get(0).getRoot())) {

            senderHomeCommunity.setHomeCommunityId(event.getPRPAIN201305UV02().getSender().getDevice().getAsAgent()
                .getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot());
        }

        return senderHomeCommunity;
    }

    protected HomeCommunityType getHomeCommunity(PRPAIN201306UV02 event) {
        HomeCommunityType senderHomeCommunity = new HomeCommunityType();

        if (event != null && event.getSender() != null && event.getSender().getDevice() != null
            && event.getSender().getDevice().getAsAgent() != null
            && event.getSender().getDevice().getAsAgent().getValue() != null
            && event.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization() != null
            && event.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null
            && NullChecker.isNotNullish(
                event.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId())
            && event.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()
                .get(0) != null
            && NullChecker.isNotNullish(event.getSender().getDevice().getAsAgent().getValue()
                .getRepresentedOrganization().getValue().getId().get(0).getRoot())) {

            senderHomeCommunity.setHomeCommunityId(event.getSender().getDevice().getAsAgent().getValue()
                .getRepresentedOrganization().getValue().getId().get(0).getRoot());
        }

        return senderHomeCommunity;
    }

    protected HomeCommunityType getHomeCommunity(PRPAIN201305UV02 event) {
        HomeCommunityType senderHomeCommunity = new HomeCommunityType();

        if (event != null && event.getSender() != null && event.getSender().getDevice() != null
            && event.getSender().getDevice().getAsAgent() != null
            && event.getSender().getDevice().getAsAgent().getValue() != null
            && event.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization() != null
            && event.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null
            && NullChecker.isNotNullish(
                event.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId())
            && event.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()
                .get(0) != null
            && NullChecker.isNotNullish(event.getSender().getDevice().getAsAgent().getValue()
                .getRepresentedOrganization().getValue().getId().get(0).getRoot())) {

            senderHomeCommunity.setHomeCommunityId(event.getSender().getDevice().getAsAgent().getValue()
                .getRepresentedOrganization().getValue().getId().get(0).getRoot());
        }

        return senderHomeCommunity;

    }

    protected RequestType getRequestType(RespondingGatewayPRPAIN201305UV02RequestType event) {
        RequestType request = new RequestType();

        setSubjectToRequestType(event, request);

        ResourceType resource = null;
        AttributeHelper attrHelper = new AttributeHelper();

        II ii = extractPatientIdentifier(event.getPRPAIN201305UV02());

        if (ii != null) {
            resource = new ResourceType();

            resource.getAttribute().add(attrHelper.attributeFactory(PATIENT_ASSIGNING_AUTHORITY_ATTRIBUTE_ID,
                Constants.DataTypeString, ii.getRoot()));

            String sStrippedPatientId = PatientIdFormatUtil.parsePatientId(ii.getExtension());

            LOG.debug("transformSubjectAddedInToCheckPolicy: sStrippedPatientId = " + sStrippedPatientId);

            resource.getAttribute().add(
                attrHelper.attributeFactory(PATIENT_ID_ATTRIBUTE_ID, Constants.DataTypeString, sStrippedPatientId));

            request.getResource().add(resource);
        }

        request.getResource().add(resource);

        return request;
    }

    protected RequestType getRequestType(PRPAIN201306UV02 event, AssertionType assertion) {
        RequestType request = new RequestType();

        setSubjectToRequestType(event, request, assertion);

        ResourceType resource = null;
        AttributeHelper attrHelper = new AttributeHelper();

        II ii = extractPatientIdentifier(event);

        if (ii != null) {
            resource = new ResourceType();

            resource.getAttribute().add(attrHelper.attributeFactory(PATIENT_ASSIGNING_AUTHORITY_ATTRIBUTE_ID,
                Constants.DataTypeString, ii.getRoot()));

            String sStrippedPatientId = PatientIdFormatUtil.parsePatientId(ii.getExtension());

            LOG.debug("transformSubjectAddedInToCheckPolicy: sStrippedPatientId = " + sStrippedPatientId);

            resource.getAttribute().add(
                attrHelper.attributeFactory(PATIENT_ID_ATTRIBUTE_ID, Constants.DataTypeString, sStrippedPatientId));

            request.getResource().add(resource);
        }

        request.getResource().add(resource);

        return request;
    }

    protected RequestType getRequestType(PRPAIN201305UV02 event, AssertionType assertion) {
        RequestType request = new RequestType();

        setSubjectToRequestType(event, request, assertion);

        ResourceType resource = null;
        AttributeHelper attrHelper = new AttributeHelper();

        II ii = extractPatientIdentifier(event);

        if (ii != null) {
            resource = new ResourceType();

            resource.getAttribute().add(attrHelper.attributeFactory(PATIENT_ASSIGNING_AUTHORITY_ATTRIBUTE_ID,
                Constants.DataTypeString, ii.getRoot()));

            String sStrippedPatientId = PatientIdFormatUtil.parsePatientId(ii.getExtension());

            LOG.debug("transformSubjectAddedInToCheckPolicy: sStrippedPatientId = " + sStrippedPatientId);

            resource.getAttribute().add(
                attrHelper.attributeFactory(PATIENT_ID_ATTRIBUTE_ID, Constants.DataTypeString, sStrippedPatientId));

            request.getResource().add(resource);
        }

        request.getResource().add(resource);

        LOG.debug("End -- PatientDiscoveryPolicyTransformHelper.getRequestType()");

        return request;
    }

    protected void setSubjectToRequestType(RespondingGatewayPRPAIN201305UV02RequestType event, RequestType request) {
        // change to get the info from the receiver node
        HomeCommunityType receiverHomeCommunity = getReceiverHomeCommunityFrom201305(event);
        if (event != null) {
            SubjectType subject = new SubjectHelper().subjectFactory(receiverHomeCommunity, event.getAssertion());
            request.getSubject().add(subject);
        }
    }

    protected void setSubjectToRequestType(PRPAIN201306UV02 event, RequestType request, AssertionType assertion) {
        HomeCommunityType senderHomeCommunity = getHomeCommunity(event);

        SubjectType subject = new SubjectHelper().subjectFactory(senderHomeCommunity, assertion);

        request.getSubject().add(subject);
    }

    protected void setSubjectToRequestType(PRPAIN201305UV02 event, RequestType request, AssertionType assertion) {
        HomeCommunityType senderHomeCommunity = getHomeCommunity(event);

        SubjectType subject = new SubjectHelper().subjectFactory(senderHomeCommunity, assertion);

        request.getSubject().add(subject);
    }

    private static II extractPatientIdentifier(PRPAIN201305UV02 message) {
        II ii = null;
        String assigningAuthority = null;

        if (message != null && message.getControlActProcess() != null
            && NullChecker.isNotNullish(message.getControlActProcess().getAuthorOrPerformer())
            && message.getControlActProcess().getAuthorOrPerformer().get(0) != null
            && message.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice() != null
            && message.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue() != null
            && NullChecker.isNotNullish(
                message.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId())
            && message.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId()
                .get(0) != null
            && NullChecker.isNotNullish(message.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice()
                .getValue().getId().get(0).getRoot())) {

            assigningAuthority = message.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice()
                .getValue().getId().get(0).getRoot();
        }

        if (message != null && message.getControlActProcess() != null
            && message.getControlActProcess().getQueryByParameter() != null) {

            JAXBElement<PRPAMT201306UV02QueryByParameter> queryParam = message.getControlActProcess()
                .getQueryByParameter();
            PRPAMT201306UV02QueryByParameter queryParam201306 = queryParam.getValue();

            if (queryParam201306.getParameterList() != null
                && queryParam201306.getParameterList().getLivingSubjectId() != null) {

                List<PRPAMT201306UV02LivingSubjectId> livingSubjectIdList = queryParam201306.getParameterList()
                    .getLivingSubjectId();

                if (NullChecker.isNotNullish(livingSubjectIdList)) {
                    for (PRPAMT201306UV02LivingSubjectId livingSubId : livingSubjectIdList) {
                        for (II id : livingSubId.getValue()) {
                            if (id != null && NullChecker.isNotNullish(id.getExtension())
                                && NullChecker.isNotNullish(id.getRoot())
                                && id.getRoot().equalsIgnoreCase(assigningAuthority)) {

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

        if (message != null && message.getControlActProcess() != null
            && NullChecker.isNotNullish(message.getControlActProcess().getSubject())
            && message.getControlActProcess().getSubject().get(0) != null
            && message.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null
            && message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null
            && message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                .getPatient() != null
            && NullChecker.isNotNullish(message.getControlActProcess().getSubject().get(0).getRegistrationEvent()
                .getSubject1().getPatient().getId())
            && message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient()
                .getId().get(0) != null) {

            ii = message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient()
                .getId().get(0);
        }

        return ii;
    }

    private HomeCommunityType getReceiverHomeCommunityFrom201305(RespondingGatewayPRPAIN201305UV02RequestType event) {
        HomeCommunityType receiverHomeCommunity = new HomeCommunityType();

        if (event != null && event.getPRPAIN201305UV02().getReceiver() != null
            && !event.getPRPAIN201305UV02().getReceiver().isEmpty()
            && event.getPRPAIN201305UV02().getReceiver().get(0) != null
            && event.getPRPAIN201305UV02().getReceiver().get(0).getDevice() != null
            && event.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getId() != null
            && event.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getId().get(0) != null && NullChecker
                .isNotNullish(event.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getId().get(0).getRoot())) {

            receiverHomeCommunity.setHomeCommunityId(
                event.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getId().get(0).getRoot());
        }

        return receiverHomeCommunity;
    }
}
