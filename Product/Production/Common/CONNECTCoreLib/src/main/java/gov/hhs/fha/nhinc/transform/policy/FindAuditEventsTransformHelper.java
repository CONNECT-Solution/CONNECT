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

import com.services.nhinc.schema.auditmessage.FindAuditEventsType;
import gov.hhs.fha.nhinc.common.eventcommon.FindAuditEventsEventType;
import gov.hhs.fha.nhinc.common.eventcommon.FindAuditEventsMessageType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author svalluripalli
 */
public class FindAuditEventsTransformHelper {

    private static final Logger LOG = LoggerFactory.getLogger(FindAuditEventsTransformHelper.class);
    private static final String ActionInValue = "AuditLogQueryIn";
    private static final String ActionOutValue = "AuditLogQueryOut";
    private static final String PatientIdAttributeId = Constants.ResourceIdAttributeId;
    private static final String AssigningAuthorityAttributeId = Constants.AssigningAuthorityAttributeId;

    public static CheckPolicyRequestType transformFindAuditEventsToCheckPolicy(FindAuditEventsEventType event) {
        CheckPolicyRequestType genericPolicyRequest = new CheckPolicyRequestType();
        RequestType request = new RequestType();
        genericPolicyRequest.setRequest(request);

        if (event != null) {
            if (InboundOutboundChecker.isInbound(event.getDirection())) {
                request.setAction(ActionHelper.actionFactory(ActionInValue));
            }
            if (InboundOutboundChecker.isOutbound(event.getDirection())) {
                request.setAction(ActionHelper.actionFactory(ActionOutValue));
            }
            SubjectHelper subjHelp = new SubjectHelper();
            SubjectType subject = subjHelp.subjectFactory(event.getSendingHomeCommunity(),
                    event.getMessage().getAssertion());
            request.getSubject().add(subject);
            FindAuditEventsMessageType message = event.getMessage();
            if (message != null) {
                FindAuditEventsType findAudit = message.getFindAuditEvents();
                if (findAudit != null) {
                    findAudit.getPatientId();
                    ResourceType resource = new ResourceType();
                    AttributeHelper attrHelper = new AttributeHelper();
                    String sPatientId = findAudit.getPatientId();
                    LOG.debug("transformFindAuditEventsToCheckPolicy: sPatientId = " + sPatientId);

                    String sAssigningAuthority = PatientIdFormatUtil.parseCommunityId(sPatientId);
                    LOG.debug("transformFindAuditEventsToCheckPolicy: sAssigningAuthority = " + sAssigningAuthority);
                    resource.getAttribute().add(attrHelper.attributeFactory(AssigningAuthorityAttributeId,
                            Constants.DataTypeString, sAssigningAuthority));

                    String sStrippedPatientId = PatientIdFormatUtil.parsePatientId(findAudit.getPatientId());
                    LOG.debug("transformFindAuditEventsToCheckPolicy: sStrippedPatientId = " + sStrippedPatientId);
                    resource.getAttribute().add(attrHelper.attributeFactory(PatientIdAttributeId,
                            Constants.DataTypeString, sStrippedPatientId));
                    request.getResource().add(resource);
                }
            }
            AssertionHelper assertHelp = new AssertionHelper();
            assertHelp.appendAssertionDataToRequest(request, event.getMessage().getAssertion());
            genericPolicyRequest.setAssertion(event.getMessage().getAssertion());
        }
        return genericPolicyRequest;
    }
}
