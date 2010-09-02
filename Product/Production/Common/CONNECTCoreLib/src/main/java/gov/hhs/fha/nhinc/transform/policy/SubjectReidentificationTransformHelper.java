/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.policy;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.eventcommon.SubjectReidentificationEventType;
import gov.hhs.fha.nhinc.common.eventcommon.SubjectReidentificationMessageType;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.PRPAMT201307UV02ParameterList;
import org.hl7.v3.PRPAMT201307UV02PatientIdentifier;
import org.hl7.v3.PRPAMT201307UV02QueryByParameter;

/**
 *
 * @author svalluripalli
 */
public class SubjectReidentificationTransformHelper {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(SubjectReidentificationTransformHelper.class);
    private static final String ActionInValue = "SubjectDiscoveryReidentificationIn";
    private static final String ActionOutValue = "SubjectDiscoveryReidentificationOut";
    private static final String PatientAssigningAuthorityAttributeId = Constants.AssigningAuthorityAttributeId;
    private static final String PatientIdAttributeId = Constants.ResourceIdAttributeId;

    public static CheckPolicyRequestType transformSubjectReidentificationToCheckPolicy(SubjectReidentificationEventType event) {
        CheckPolicyRequestType genericPolicyRequest = new CheckPolicyRequestType();
        SubjectReidentificationMessageType message = event.getMessage();
        PRPAIN201309UV02 subjectReidentification = message.getPRPAIN201309UV02();
        RequestType request = new RequestType();
        if (event.getDirection() != null) {
            System.out.println("event.getDirection() is " + event.getDirection());
        }
        if (InboundOutboundChecker.IsInbound(event.getDirection())) {
            request.setAction(ActionHelper.actionFactory(ActionInValue));
        }
        if (InboundOutboundChecker.IsOutbound(event.getDirection())) {
            request.setAction(ActionHelper.actionFactory(ActionOutValue));
        }
        System.out.println("Action is >>>" + request.getAction().getAttribute().get(0).getAttributeId() + "<<<");
        SubjectHelper subjHelp = new SubjectHelper();
        SubjectType subject = subjHelp.subjectFactoryReident(event.getSendingHomeCommunity(), event.getMessage().getAssertion());
        request.getSubject().add(subject);
        II ii = extractPatientIdentifier(subjectReidentification);
        if (ii != null) {
            ResourceType resource = new ResourceType();
            AttributeHelper attrHelper = new AttributeHelper();
            resource.getAttribute().add(attrHelper.attributeFactory(PatientAssigningAuthorityAttributeId, Constants.DataTypeString, ii.getRoot()));
            String sStrippedPatientId = PatientIdFormatUtil.parsePatientId(ii.getExtension());
            log.debug("transformSubjectReidentificationToCheckPolicy: sStrippedPatientId = " + sStrippedPatientId);
            resource.getAttribute().add(attrHelper.attributeFactory(PatientIdAttributeId, Constants.DataTypeString, sStrippedPatientId));
            request.getResource().add(resource);
        }
        AssertionHelper assertHelp = new AssertionHelper();
        assertHelp.appendAssertionDataToRequest(request, event.getMessage().getAssertion());

        genericPolicyRequest.setRequest(request);
        genericPolicyRequest.setAssertion(event.getMessage().getAssertion());
        return genericPolicyRequest;
    }

    /**
     * Extract a Patient from 201309
     * @param message
     * @return II (PatientId root and extension)
     */
    private static II extractPatientIdentifier(PRPAIN201309UV02 message) {
        II ii = null;
        PRPAMT201307UV02QueryByParameter queryByParameter = null;
        PRPAMT201307UV02ParameterList parameterList = null;
        PRPAMT201307UV02PatientIdentifier patientIdentifier = null;
        if ((message != null) && (message.getControlActProcess() != null)) {
            JAXBElement<PRPAMT201307UV02QueryByParameter> queryByParamterElement = message.getControlActProcess().getQueryByParameter();
            if (null != queryByParamterElement) {
                queryByParameter = queryByParamterElement.getValue();
                if (null != queryByParameter) {
                    parameterList = queryByParameter.getParameterList();
                    if (null != parameterList) {
                        patientIdentifier = (parameterList.getPatientIdentifier() != null) ? parameterList.getPatientIdentifier().get(0) : null;
                        if (null != patientIdentifier) {
                            if (null != patientIdentifier.getValue() && patientIdentifier.getValue().size() >= 1) {
                                ii = patientIdentifier.getValue().get(0);
                                System.out.println(">>>>> patientIdentifier " + ii.getExtension() + " " + ii.getRoot());
                            }
                        }
                    }
                }
            }
        }
        return ii;
    }
}
