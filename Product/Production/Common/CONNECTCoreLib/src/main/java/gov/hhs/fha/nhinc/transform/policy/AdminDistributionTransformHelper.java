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

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageType;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dunnek
 */
public class AdminDistributionTransformHelper {

    private static final String ActionInValue = "AdminDistIn";
    private static final String ActionOutValue = "AdminDistOut";
    private static final Logger LOG = LoggerFactory.getLogger(AdminDistributionTransformHelper.class);

    public CheckPolicyRequestType transformNhinAlertToCheckPolicy(EDXLDistribution message, AssertionType assertion) {
        CheckPolicyRequestType result = new CheckPolicyRequestType();

        RequestType request = new RequestType();

        if (assertion == null) {
            LOG.error("Missing Assertion");
            return result;
        }
        if (message == null) {
            LOG.error("Missing message");
            return result;
        }
        LOG.debug("transformAdminDistributionNhincToCheckPolicy - adding assertion data");
        AssertionHelper assertHelp = new AssertionHelper();
        assertHelp.appendAssertionDataToRequest(request, assertion);

        request.setAction(ActionHelper.actionFactory(ActionInValue));

        result.setAssertion(assertion);
        result.setRequest(request);

        return result;
    }

    public CheckPolicyRequestType transformEntityAlertToCheckPolicy(RespondingGatewaySendAlertMessageType message,
            String target) {

        CheckPolicyRequestType result = new CheckPolicyRequestType();
        if (message == null) {
            LOG.error("Request is null.");
            return result;
        }
        if (target == null || target.isEmpty()) {
            LOG.error("target is missing");
            return result;
        }
        if (message.getEDXLDistribution() == null) {
            LOG.error("missing body");
            return result;
        }
        if (message.getAssertion() == null) {
            LOG.error("missing assertion");
            return result;
        }
        if (message.getAssertion().getHomeCommunity() == null) {
            LOG.error("missing home community");
            return result;
        }

        RequestType request = new RequestType();
        AttributeHelper attrHelper = new AttributeHelper();

        LOG.debug("transformEntityAlertToCheckPolicy - adding subject");
        SubjectType subject = new SubjectType();
        subject.setSubjectCategory(SubjectHelper.SubjectCategory);
        LOG.debug("transformEntityAlertToCheckPolicy - adding subject");
        request.getSubject().add(subject);

        ResourceType resource = new ResourceType();
        resource.getAttribute().add(attrHelper.attributeFactory(Constants.HomeCommunityAttributeId,
                Constants.DataTypeString, message.getAssertion().getHomeCommunity().getHomeCommunityId()));

        request.getResource().add(resource);

        LOG.debug("transformEntityAlertToCheckPolicy - adding assertion data");
        AssertionHelper assertHelp = new AssertionHelper();
        assertHelp.appendAssertionDataToRequest(request, message.getAssertion());

        request.setAction(ActionHelper.actionFactory(ActionOutValue));
        result.setAssertion(message.getAssertion());
        result.setRequest(request);
        return result;

    }

    protected SubjectType createSubject(HomeCommunityType hc, AssertionType assertion) {
        SubjectHelper subjHelp = new SubjectHelper();
        SubjectType subject = subjHelp.subjectFactory(hc, assertion);
        subject.setSubjectCategory(SubjectHelper.SubjectCategory);
        return subject;
    }
}
