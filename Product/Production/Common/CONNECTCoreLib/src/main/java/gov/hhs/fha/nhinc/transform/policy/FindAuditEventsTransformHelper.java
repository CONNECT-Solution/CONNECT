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

import com.services.nhinc.schema.auditmessage.FindAuditEventsType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.eventcommon.FindAuditEventsEventType;
import gov.hhs.fha.nhinc.common.eventcommon.FindAuditEventsMessageType;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;

/**
 *
 * @author svalluripalli
 */
public class FindAuditEventsTransformHelper {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(FindAuditEventsTransformHelper.class);
    private static final String ActionInValue = "AuditLogQueryIn";
    private static final String ActionOutValue = "AuditLogQueryOut";
    private static final String PatientIdAttributeId = Constants.ResourceIdAttributeId;
    private static final String AssigningAuthorityAttributeId = Constants.AssigningAuthorityAttributeId;

    public static CheckPolicyRequestType transformFindAuditEventsToCheckPolicy(FindAuditEventsEventType event) {
        CheckPolicyRequestType genericPolicyRequest = new CheckPolicyRequestType();
        RequestType request = new RequestType();

        if (event != null) {
            if (InboundOutboundChecker.IsInbound(event.getDirection())) {
                request.setAction(ActionHelper.actionFactory(ActionInValue));
            }
            if (InboundOutboundChecker.IsOutbound(event.getDirection())) {
                request.setAction(ActionHelper.actionFactory(ActionOutValue));
            }
            SubjectHelper subjHelp = new SubjectHelper();
            SubjectType subject = subjHelp.subjectFactory(event.getSendingHomeCommunity(), event.getMessage().getAssertion());
            request.getSubject().add(subject);
            FindAuditEventsMessageType message = event.getMessage();
            if (message != null) {
                FindAuditEventsType findAudit = message.getFindAuditEvents();
                if (findAudit != null) {
                    findAudit.getPatientId();
                    ResourceType resource = new ResourceType();
                    AttributeHelper attrHelper = new AttributeHelper();
                    String sPatientId = findAudit.getPatientId();
                    log.debug("transformFindAuditEventsToCheckPolicy: sPatientId = " + sPatientId);

                    String sAssigningAuthority = PatientIdFormatUtil.parseCommunityId(sPatientId);
                    log.debug("transformFindAuditEventsToCheckPolicy: sAssigningAuthority = " + sAssigningAuthority);
                    resource.getAttribute().add(attrHelper.attributeFactory(AssigningAuthorityAttributeId, Constants.DataTypeString, sAssigningAuthority));

                    String sStrippedPatientId = PatientIdFormatUtil.parsePatientId(findAudit.getPatientId());
                    log.debug("transformFindAuditEventsToCheckPolicy: sStrippedPatientId = " + sStrippedPatientId);
                    resource.getAttribute().add(attrHelper.attributeFactory(PatientIdAttributeId, Constants.DataTypeString, sStrippedPatientId));
                    request.getResource().add(resource);
                }
            }
            AssertionHelper assertHelp = new AssertionHelper();
            assertHelp.appendAssertionDataToRequest(request, event.getMessage().getAssertion());
        }

        genericPolicyRequest.setRequest(request);
        genericPolicyRequest.setAssertion(event.getMessage().getAssertion());
        return genericPolicyRequest;
    }
}
