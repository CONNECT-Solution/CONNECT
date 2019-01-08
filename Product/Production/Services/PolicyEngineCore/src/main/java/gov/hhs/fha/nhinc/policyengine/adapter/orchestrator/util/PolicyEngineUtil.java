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
package gov.hhs.fha.nhinc.policyengine.adapter.orchestrator.util;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResponseType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResultType;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author achidambaram
 *
 */
public class PolicyEngineUtil {

    private static final Logger LOG = LoggerFactory.getLogger(PolicyEngineUtil.class);

    public CheckPolicyResponseType checkAssertionAttributeStatement(AssertionType assertion) {
        StringBuilder sBuilder = new StringBuilder();
        boolean passesPolicyCheck;
        if (assertion != null) {
            passesPolicyCheck = checkUserInfo(assertion.getUserInfo(), sBuilder)
                && checkPurposeOfDisclosure(assertion.getPurposeOfDisclosureCoded(), sBuilder)
                && checkHomeCommunity(assertion.getHomeCommunity(), sBuilder);
        } else {
            sBuilder.append("| Assertion check failed. |");
            passesPolicyCheck = false;
        }

        DecisionType decision;
        if (passesPolicyCheck) {
            decision = DecisionType.PERMIT;
            sBuilder.append("Exiting out from CheckPolicyResponseType without any error");
        } else {
            decision = DecisionType.DENY;
        }
        return createPolicyResponse(decision, sBuilder.toString());
    }

    private static CheckPolicyResponseType createPolicyResponse(DecisionType dType, String message) {
        LOG.info(message);
        ResultType result = new ResultType();
        result.setDecision(dType);
        CheckPolicyResponseType oResponse = new CheckPolicyResponseType();
        ResponseType respType = new ResponseType();
        respType.getResult().add(result);
        oResponse.setResponse(respType);
        return oResponse;
    }

    private static boolean checkUserInfo(UserType userInfo, StringBuilder sBuilder) {
        if (userInfo == null) {
            sBuilder.append("| User Info Check Failed |");
            return false;
        } else {
            return checkPersonName(userInfo.getPersonName(), sBuilder) && checkOrg(userInfo.getOrg(), sBuilder)
                && checkRoleCoded(userInfo.getRoleCoded(), sBuilder);
        }
    }

    private static boolean checkPersonName(PersonNameType personName, StringBuilder sBuilder) {
        if (personName == null) {
            sBuilder.append("| Person Type Check Failed |");
            return false;
        } else {
            return checkString(personName.getFamilyName(), sBuilder, "| FamilyName Check Failed |")
                && checkString(personName.getSecondNameOrInitials(), sBuilder, "| SecondNameOrInitials Check Failed |")
                && checkString(personName.getGivenName(), sBuilder, "| Given Name Check Failed |");
        }
    }

    private static boolean checkOrg(HomeCommunityType org, StringBuilder sBuilder) {
        if (org == null) {
            sBuilder.append("| Org Info Check Failed |");
            return false;
        } else {
            return checkString(org.getName(), sBuilder, "| HomeCommunity Name Check Failed |")
                && checkString(org.getHomeCommunityId(), sBuilder, "| HomeCommunityId Check Failed |");
        }
    }

    private static boolean checkRoleCoded(CeType roleCoded, StringBuilder sBuilder) {
        if (roleCoded == null) {
            sBuilder.append("| RoleCode Check Failed |");
            return false;
        } else {
            return checkString(roleCoded.getCode(), sBuilder, "| RoleCoded Code Check Failed |")
                && checkString(roleCoded.getDisplayName(), sBuilder, "| RoleCoded DisplayName Check Failed |");
        }
    }

    private static boolean checkPurposeOfDisclosure(CeType purposeOfDisclosureCoded, StringBuilder sBuilder) {
        if (purposeOfDisclosureCoded == null) {
            sBuilder.append("| PurposeOfDisclosureCoded Check Failed |");
            return false;
        } else {
            return checkString(purposeOfDisclosureCoded.getCode(), sBuilder, "| PurposeOf Code Check Failed |")
                && checkString(purposeOfDisclosureCoded.getDisplayName(), sBuilder,
                    "| PurposeOf DisplayName Check Failed |");
        }
    }

    private static boolean checkHomeCommunity(HomeCommunityType homeCommunity, StringBuilder sBuilder) {
        if (homeCommunity == null) {
            sBuilder.append("| HomeCommunity Check Failed. |");
            return false;
        } else {
            return checkString(homeCommunity.getHomeCommunityId(), sBuilder, "| HomeCommunity Id Check Failed |");
        }
    }

    private static boolean checkString(String checkedString, StringBuilder sBuilder, String message) {
        if (StringUtils.isBlank(checkedString)) {
            sBuilder.append(message);
            return false;
        }
        return true;
    }
}
