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

import static org.junit.Assert.assertEquals;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import org.junit.Test;

/**
 * @author msw
 *
 */
public class PolicyEngineUtilTest {

    @Test
    public void testEmtpy() {
        AssertionType assertion = new AssertionType();
        PolicyEngineUtil util = new PolicyEngineUtil();
        CheckPolicyResponseType resp = util.checkAssertionAttributeStatement(assertion);
        assertDeny(resp);
    }

    @Test
    public void testAll() {
        AssertionType assertion = getFullAssertion();

        PolicyEngineUtil util = new PolicyEngineUtil();
        CheckPolicyResponseType resp = util.checkAssertionAttributeStatement(assertion);
        assertPermit(resp);
    }

    @Test
    public void testNoOrgId() {
        AssertionType assertion = getFullAssertion();

        assertion.getUserInfo().getOrg().setHomeCommunityId(null);
        PolicyEngineUtil util = new PolicyEngineUtil();
        CheckPolicyResponseType resp = util.checkAssertionAttributeStatement(assertion);
        assertDeny(resp);
    }

    @Test
    public void testMissingMultiple() {
        AssertionType assertion = getFullAssertion();

        assertion.getPurposeOfDisclosureCoded().setCodeSystem(null);
        assertion.getUserInfo().getPersonName().setSecondNameOrInitials(null);
        assertion.getHomeCommunity().setHomeCommunityId(null);

        PolicyEngineUtil util = new PolicyEngineUtil();
        CheckPolicyResponseType resp = util.checkAssertionAttributeStatement(assertion);
        assertDeny(resp);
    }

    /**
     * @param resp
     */
    private void assertPermit(CheckPolicyResponseType resp) {
        assertEquals(resp.getResponse().getResult().get(0).getDecision(), DecisionType.PERMIT);
    }

    /**
     * @param resp
     */
    private void assertDeny(CheckPolicyResponseType resp) {
        assertEquals(resp.getResponse().getResult().get(0).getDecision(), DecisionType.DENY);
    }

    private AssertionType getFullAssertion() {
        AssertionType assertion = new AssertionType();

        PersonNameType personName = new PersonNameType();
        personName.setFamilyName("Prime");
        personName.setGivenName("Optimus");
        personName.setSecondNameOrInitials("T");

        UserType userInfo = new UserType();
        HomeCommunityType org = new HomeCommunityType();
        org.setName("Cybertron");
        org.setHomeCommunityId("1.1");
        userInfo.setOrg(org);

        CeType roleCoded = new CeType();
        roleCoded.setCode("role coded code");
        roleCoded.setCodeSystem("code system");
        roleCoded.setCodeSystemName("code system name");
        roleCoded.setDisplayName("display name");
        userInfo.setRoleCoded(roleCoded);
        userInfo.setPersonName(personName);
        assertion.setUserInfo(userInfo);

        CeType purposeCoded = new CeType();
        purposeCoded.setCode("purpose code");
        purposeCoded.setCodeSystem("purpose code system");
        purposeCoded.setCodeSystemName("purpose code system name");
        purposeCoded.setDisplayName("purpose display name");
        assertion.setPurposeOfDisclosureCoded(purposeCoded);

        HomeCommunityType homeCommunityId = new HomeCommunityType();
        homeCommunityId.setHomeCommunityId("hcid");
        assertion.setHomeCommunity(homeCommunityId);

        return assertion;
    }
}
