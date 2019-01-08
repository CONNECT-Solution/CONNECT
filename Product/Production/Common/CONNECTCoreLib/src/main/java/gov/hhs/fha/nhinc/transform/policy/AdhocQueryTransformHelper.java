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

import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestEventType;
import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryResultEventType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author rayj
 */
public class AdhocQueryTransformHelper {

    private static final Logger LOG = LoggerFactory.getLogger(AdhocQueryTransformHelper.class);
    private static final String ACTIONVALUEIN = "DocumentQueryIn";
    private static final String ACTIONVALUEOUT = "DocumentQueryOut";
    private static final String PatientAssigningAuthorityAttributeId = Constants.AssigningAuthorityAttributeId;
    private static final String PatientIdAttributeId = Constants.ResourceIdAttributeId;

    public static CheckPolicyRequestType transformAdhocQueryToCheckPolicy(AdhocQueryRequestEventType event) {
        LOG.debug("begin transformAdhocQueryToCheckPolicy");

        CheckPolicyRequestType result = null;
        if (event != null && InboundOutboundChecker.isInbound(event.getDirection())) {
            result = transformAdhocQueryInboundToCheckPolicy(event);
        }
        if (event != null && InboundOutboundChecker.isOutbound(event.getDirection())) {
            result = transformAdhocQueryOutboundToCheckPolicy(event);
        }

        LOG.debug("end transformAdhocQueryToCheckPolicy");
        return result;
    }

    public static CheckPolicyRequestType transformAdhocQueryResponseToCheckPolicy(AdhocQueryResultEventType event) {
        LOG.debug("begin transformAdhocQueryResponseToCheckPolicy");

        CheckPolicyRequestType result = null;
        if (event != null && InboundOutboundChecker.isInbound(event.getDirection())) {
            result = transformAdhocQueryResponseInboundToCheckPolicy(event);
        }
        if (event != null && InboundOutboundChecker.isOutbound(event.getDirection())) {
            result = transformAdhocQueryResponseOutboundToCheckPolicy(event);
        }

        LOG.debug("end transformAdhocQueryToCheckPolicy");
        return result;
    }

    private static CheckPolicyRequestType transformAdhocQueryResponseToCheckPolicyBase(
        AdhocQueryResultEventType event) {
        CheckPolicyRequestType genericPolicyRequest = new CheckPolicyRequestType();

        RequestType request = new RequestType();

        if (event != null && InboundOutboundChecker.isInbound(event.getDirection())) {
            request.setAction(ActionHelper.actionFactory(ACTIONVALUEIN));
        }

        if (event != null && InboundOutboundChecker.isOutbound(event.getDirection())) {
            request.setAction(ActionHelper.actionFactory(ACTIONVALUEOUT));
        }

        ResourceType resource = new ResourceType();
        AttributeHelper attrHelper = new AttributeHelper();
        String sPatientId;
        String sStrippedPatientId = null;
        String aaId = null;
        if (event != null && event.getMessage() != null && event.getMessage().getAssertion() != null
            && NullChecker.isNotNullish(event.getMessage().getAssertion().getUniquePatientId())
            && NullChecker.isNotNullish(event.getMessage().getAssertion().getUniquePatientId().get(0))) {
            sPatientId = event.getMessage().getAssertion().getUniquePatientId().get(0);
            sStrippedPatientId = PatientIdFormatUtil.parsePatientId(sPatientId);
            aaId = PatientIdFormatUtil.parseCommunityId(sPatientId);
        }
        LOG.debug("transformAdhocQueryResponseToCheckPolicyBase aaId: " + aaId);
        LOG.debug("transformAdhocQueryResponseToCheckPolicyBase PatientId: " + sStrippedPatientId);

        resource.getAttribute()
        .add(attrHelper.attributeFactory(PatientAssigningAuthorityAttributeId, Constants.DataTypeString, aaId));

        resource.getAttribute()
        .add(attrHelper.attributeFactory(PatientIdAttributeId, Constants.DataTypeString, sStrippedPatientId));
        request.getResource().add(resource);

        SubjectHelper subjHelp = new SubjectHelper();
        SubjectType subject = null;
        if (event != null && event.getMessage() != null) {
            subject = subjHelp.subjectFactory(event.getSendingHomeCommunity(), event.getMessage().getAssertion());
        }
        request.getSubject().add(subject);

        AssertionHelper assertHelp = new AssertionHelper();
        if (event != null && event.getMessage() != null) {
            assertHelp.appendAssertionDataToRequest(request, event.getMessage().getAssertion());
        }

        CheckPolicyRequestType policyRequest = new CheckPolicyRequestType();
        policyRequest.setRequest(request);
        genericPolicyRequest.setRequest(request);
        if (event != null && event.getMessage() != null) {
            genericPolicyRequest.setAssertion(event.getMessage().getAssertion());
        }
        return genericPolicyRequest;
    }

    private static CheckPolicyRequestType transformAdhocQueryResponseInboundToCheckPolicy(
        AdhocQueryResultEventType event) {
        return transformAdhocQueryResponseToCheckPolicyBase(event);
    }

    private static CheckPolicyRequestType transformAdhocQueryResponseOutboundToCheckPolicy(
        AdhocQueryResultEventType event) {
        CheckPolicyRequestType checkPolicy = transformAdhocQueryResponseToCheckPolicyBase(event);
        AttributeHelper attrHelper = new AttributeHelper();
        if (event != null) {
            checkPolicy.getRequest().getResource().get(0).getAttribute()
            .add(attrHelper.attributeFactory(Constants.HomeCommunityAttributeId, Constants.DataTypeString,
                CommunityHelper.extractCommunityId(event.getReceivingHomeCommunity())));
        }
        return checkPolicy;
    }

    private static CheckPolicyRequestType transformAdhocQueryToCheckPolicyBase(AdhocQueryRequestEventType event) {
        CheckPolicyRequestType genericPolicyRequest = new CheckPolicyRequestType();

        AdhocQueryRequest docQuery = null;
        AssertionType assertion = null;
        if (event != null && event.getMessage() != null) {
            docQuery = event.getMessage().getAdhocQueryRequest();
            assertion = event.getMessage().getAssertion();
        }

        RequestType request = new RequestType();

        String aaId = extractPatientIdentifierAssigningAuthority(docQuery);
        String sPatientId = extractPatientIdentifierId(docQuery);
        String sStrippedPatientId = PatientIdFormatUtil.parsePatientId(sPatientId);

        if (event != null && InboundOutboundChecker.isInbound(event.getDirection())) {
            request.setAction(ActionHelper.actionFactory(ACTIONVALUEIN));
        }

        if (event != null && InboundOutboundChecker.isOutbound(event.getDirection())) {
            request.setAction(ActionHelper.actionFactory(ACTIONVALUEOUT));
            if (assertion != null
                && CollectionUtils.isNotEmpty(assertion.getUniquePatientId())) {
                aaId = PatientIdFormatUtil.parseCommunityId(assertion.getUniquePatientId().get(0));
                sStrippedPatientId = PatientIdFormatUtil.parsePatientId(assertion.getUniquePatientId().get(0));

            } else {
                LOG.info("Unique patientid is null in the assertion.");
            }
        }
        if (event != null) {
            LOG.debug("transformAdhocQueryToCheckPolicyBase: event direction: " + event.getDirection());
        }
        LOG.debug("transformAdhocQueryToCheckPolicyBase: aaId: " + aaId);
        LOG.debug("transformAdhocQueryToCheckPolicyBase: PatientId: " + sStrippedPatientId);

        ResourceType resource = new ResourceType();
        AttributeHelper attrHelper = new AttributeHelper();
        resource.getAttribute()
        .add(attrHelper.attributeFactory(PatientAssigningAuthorityAttributeId, Constants.DataTypeString, aaId));

        resource.getAttribute()
        .add(attrHelper.attributeFactory(PatientIdAttributeId, Constants.DataTypeString, sStrippedPatientId));
        request.getResource().add(resource);

        SubjectHelper subjHelp = new SubjectHelper();
        if (event != null && event.getMessage() != null) {
            SubjectType subject = subjHelp.subjectFactory(event.getSendingHomeCommunity(),
                event.getMessage().getAssertion());
            request.getSubject().add(subject);
        }

        AssertionHelper assertHelp = new AssertionHelper();
        if (event != null && event.getMessage() != null) {
            assertHelp.appendAssertionDataToRequest(request, event.getMessage().getAssertion());
        }

        CheckPolicyRequestType policyRequest = new CheckPolicyRequestType();
        policyRequest.setRequest(request);
        genericPolicyRequest.setRequest(request);
        if (event != null && event.getMessage() != null) {
            genericPolicyRequest.setAssertion(event.getMessage().getAssertion());
        }
        return genericPolicyRequest;
    }

    private static CheckPolicyRequestType transformAdhocQueryInboundToCheckPolicy(AdhocQueryRequestEventType event) {
        return transformAdhocQueryToCheckPolicyBase(event);
    }

    private static CheckPolicyRequestType transformAdhocQueryOutboundToCheckPolicy(AdhocQueryRequestEventType event) {
        CheckPolicyRequestType checkPolicy = transformAdhocQueryToCheckPolicyBase(event);
        AttributeHelper attrHelper = new AttributeHelper();
        if (event != null) {
            checkPolicy.getRequest().getResource().get(0).getAttribute()
            .add(attrHelper.attributeFactory(Constants.HomeCommunityAttributeId, Constants.DataTypeString,
                CommunityHelper.extractCommunityId(event.getReceivingHomeCommunity())));
        }
        return checkPolicy;
    }

    public static String extractPatientIdentifierId(AdhocQueryRequest docQuery) {
        return extractPatientIdentifier(docQuery);
    }

    public static String extractPatientIdentifierAssigningAuthority(AdhocQueryRequest docQuery) {
        return PatientIdFormatUtil.parseCommunityId(extractPatientIdentifier(docQuery));
    }

    private static String extractPatientIdentifier(AdhocQueryRequest docQuery) {
        String patientIdentifier = null;

        if (docQuery != null && docQuery.getAdhocQuery() != null) {
            List<SlotType1> slots = docQuery.getAdhocQuery().getSlot();
            for (SlotType1 slot : slots) {
                if (slot.getName() != null && slot.getName().contentEquals("$XDSDocumentEntryPatientId")) {
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
