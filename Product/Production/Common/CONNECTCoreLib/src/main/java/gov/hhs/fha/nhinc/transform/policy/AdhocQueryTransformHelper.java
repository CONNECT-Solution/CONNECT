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

import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestEventType;
import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryResultEventType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;

/**
 *
 * @author rayj
 */
public class AdhocQueryTransformHelper {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(AdhocQueryTransformHelper.class);
    private static final String ACTIONVALUEIN = "DocumentQueryIn";
    private static final String ACTIONVALUEOUT = "DocumentQueryOut";
    private static final String PatientAssigningAuthorityAttributeId = Constants.AssigningAuthorityAttributeId;
    private static final String PatientIdAttributeId = Constants.ResourceIdAttributeId;

    public static CheckPolicyRequestType transformAdhocQueryToCheckPolicy(AdhocQueryRequestEventType event) {
        log.debug("begin transformAdhocQueryToCheckPolicy");

        CheckPolicyRequestType result = null;
        if (InboundOutboundChecker.IsInbound(event.getDirection())) {
            result = transformAdhocQueryInboundToCheckPolicy(event);
        }
        if (InboundOutboundChecker.IsOutbound(event.getDirection())) {
            result = transformAdhocQueryOutboundToCheckPolicy(event);
        }

        log.debug("end transformAdhocQueryToCheckPolicy");
        return result;
    }

    public static CheckPolicyRequestType transformAdhocQueryResponseToCheckPolicy(AdhocQueryResultEventType event) {
        log.debug("begin transformAdhocQueryResponseToCheckPolicy");

        CheckPolicyRequestType result = null;
        if (InboundOutboundChecker.IsInbound(event.getDirection())) {
            result = transformAdhocQueryResponseInboundToCheckPolicy(event);
        }
        if (InboundOutboundChecker.IsOutbound(event.getDirection())) {
            result = transformAdhocQueryResponseOutboundToCheckPolicy(event);
        }

        log.debug("end transformAdhocQueryToCheckPolicy");
        return result;
    }

    private static CheckPolicyRequestType transformAdhocQueryResponseToCheckPolicyBase(AdhocQueryResultEventType event) {
        CheckPolicyRequestType genericPolicyRequest = new CheckPolicyRequestType();

        AdhocQueryResponse docQuery = event.getMessage().getAdhocQueryResponse();

        RequestType request = new RequestType();

        if (InboundOutboundChecker.IsInbound(event.getDirection())) {
            request.setAction(ActionHelper.actionFactory(ACTIONVALUEIN));
        }

        if (InboundOutboundChecker.IsOutbound(event.getDirection())) {
            request.setAction(ActionHelper.actionFactory(ACTIONVALUEOUT));
        }

        ResourceType resource = new ResourceType();
        AttributeHelper attrHelper = new AttributeHelper();
        String sPatientId = null;
        String sStrippedPatientId = null;
        String aaId = null;
        if (event != null &&
                event.getMessage() != null &&
                event.getMessage().getAssertion() != null &&
                NullChecker.isNotNullish(event.getMessage().getAssertion().getUniquePatientId()) &&
                NullChecker.isNotNullish(event.getMessage().getAssertion().getUniquePatientId().get(0))) {
            sPatientId = event.getMessage().getAssertion().getUniquePatientId().get(0);
            sStrippedPatientId = PatientIdFormatUtil.parsePatientId(sPatientId);
            aaId = PatientIdFormatUtil.parseCommunityId(sPatientId);
        }
        log.debug("transformAdhocQueryResponseToCheckPolicyBase aaId: " + aaId);
        log.debug("transformAdhocQueryResponseToCheckPolicyBase PatientId: " + sStrippedPatientId);

        resource.getAttribute().add(attrHelper.attributeFactory(PatientAssigningAuthorityAttributeId, Constants.DataTypeString, aaId));

        resource.getAttribute().add(attrHelper.attributeFactory(PatientIdAttributeId, Constants.DataTypeString, sStrippedPatientId));
        request.getResource().add(resource);

        SubjectHelper subjHelp = new SubjectHelper();
        SubjectType subject = subjHelp.subjectFactory(event.getSendingHomeCommunity(), event.getMessage().getAssertion());
        request.getSubject().add(subject);

        AssertionHelper assertHelp = new AssertionHelper();
        assertHelp.appendAssertionDataToRequest(request, event.getMessage().getAssertion());

        CheckPolicyRequestType policyRequest = new CheckPolicyRequestType();
        policyRequest.setRequest(request);
        genericPolicyRequest.setRequest(request);
        genericPolicyRequest.setAssertion(event.getMessage().getAssertion());
        return genericPolicyRequest;
    }

    private static CheckPolicyRequestType transformAdhocQueryResponseInboundToCheckPolicy(AdhocQueryResultEventType event) {
        return transformAdhocQueryResponseToCheckPolicyBase(event);
    }

    private static CheckPolicyRequestType transformAdhocQueryResponseOutboundToCheckPolicy(AdhocQueryResultEventType event) {
        CheckPolicyRequestType checkPolicy = transformAdhocQueryResponseToCheckPolicyBase(event);
        AttributeHelper attrHelper = new AttributeHelper();
        checkPolicy.getRequest().getResource().get(0).getAttribute().add(attrHelper.attributeFactory(Constants.HomeCommunityAttributeId, Constants.DataTypeString, CommunityHelper.extractCommunityId(event.getReceivingHomeCommunity())));
        return checkPolicy;
    }

    private static CheckPolicyRequestType transformAdhocQueryToCheckPolicyBase(AdhocQueryRequestEventType event) {
        CheckPolicyRequestType genericPolicyRequest = new CheckPolicyRequestType();

        AdhocQueryRequest docQuery = event.getMessage().getAdhocQueryRequest();
        AssertionType assertion = event.getMessage().getAssertion();

        RequestType request = new RequestType();

        String aaId = extractPatientIdentifierAssigningAuthority(docQuery);
        String sPatientId = extractPatientIdentifierId(docQuery);
        String sStrippedPatientId = PatientIdFormatUtil.parsePatientId(sPatientId);

        if (InboundOutboundChecker.IsInbound(event.getDirection())) {
            request.setAction(ActionHelper.actionFactory(ACTIONVALUEIN));
        }

        if (InboundOutboundChecker.IsOutbound(event.getDirection())) {
            request.setAction(ActionHelper.actionFactory(ACTIONVALUEOUT));
            if ((assertion.getUniquePatientId() != null) &&
                    (assertion.getUniquePatientId().size() > 0)) {
                aaId = PatientIdFormatUtil.parseCommunityId(assertion.getUniquePatientId().get(0));
                sStrippedPatientId = PatientIdFormatUtil.parsePatientId(assertion.getUniquePatientId().get(0));

            } else {
                log.info("Unique patientid is null in the assertion.");
            }
        }

        log.debug("transformAdhocQueryToCheckPolicyBase: event direction: " + event.getDirection());
        log.debug("transformAdhocQueryToCheckPolicyBase: aaId: " + aaId);
        log.debug("transformAdhocQueryToCheckPolicyBase: PatientId: " + sStrippedPatientId);

        ResourceType resource = new ResourceType();
        AttributeHelper attrHelper = new AttributeHelper();
        resource.getAttribute().add(attrHelper.attributeFactory(PatientAssigningAuthorityAttributeId, Constants.DataTypeString, aaId));

        resource.getAttribute().add(attrHelper.attributeFactory(PatientIdAttributeId, Constants.DataTypeString, sStrippedPatientId));
        request.getResource().add(resource);

        SubjectHelper subjHelp = new SubjectHelper();
        SubjectType subject = subjHelp.subjectFactory(event.getSendingHomeCommunity(), event.getMessage().getAssertion());
        request.getSubject().add(subject);

        AssertionHelper assertHelp = new AssertionHelper();
        assertHelp.appendAssertionDataToRequest(request, event.getMessage().getAssertion());

        CheckPolicyRequestType policyRequest = new CheckPolicyRequestType();
        policyRequest.setRequest(request);
        genericPolicyRequest.setRequest(request);
        genericPolicyRequest.setAssertion(event.getMessage().getAssertion());
        return genericPolicyRequest;
    }

    private static CheckPolicyRequestType transformAdhocQueryInboundToCheckPolicy(AdhocQueryRequestEventType event) {
        return transformAdhocQueryToCheckPolicyBase(event);
    }

    private static CheckPolicyRequestType transformAdhocQueryOutboundToCheckPolicy(AdhocQueryRequestEventType event) {
        CheckPolicyRequestType checkPolicy = transformAdhocQueryToCheckPolicyBase(event);
        AttributeHelper attrHelper = new AttributeHelper();
        checkPolicy.getRequest().getResource().get(0).getAttribute().add(attrHelper.attributeFactory(Constants.HomeCommunityAttributeId, Constants.DataTypeString, CommunityHelper.extractCommunityId(event.getReceivingHomeCommunity())));
        return checkPolicy;
    }

    public static String extractPatientIdentifierId(AdhocQueryRequest docQuery) {
        //return PatientIdFormatUtil.parsePatientId(extractPatientIdentifier(docQuery));
        return extractPatientIdentifier(docQuery);
    }

    public static String extractPatientIdentifierAssigningAuthority(AdhocQueryRequest docQuery) {
        return PatientIdFormatUtil.parseCommunityId(extractPatientIdentifier(docQuery));
    }

    private static String extractPatientIdentifier(AdhocQueryRequest docQuery) {
        String patientIdentifier = null;

        if ((docQuery != null) && (docQuery.getAdhocQuery() != null)) {
            List<SlotType1> slots = docQuery.getAdhocQuery().getSlot();
            for (SlotType1 slot : slots) {
                if ((slot.getName() != null) && (slot.getName().contentEquals("$XDSDocumentEntryPatientId"))) {
                    if (slot.getValueList() != null) {
                        if (slot.getValueList().getValue().size() == 1) {
                            patientIdentifier = slot.getValueList().getValue().get(0);
                        }
                    }
                }
            }
        }
        return patientIdentifier;
    }
}
