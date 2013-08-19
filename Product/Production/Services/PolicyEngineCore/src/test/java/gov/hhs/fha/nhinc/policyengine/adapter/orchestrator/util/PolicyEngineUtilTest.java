/**
 *
 */
package gov.hhs.fha.nhinc.policyengine.adapter.orchestrator.util;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author msw
 *
 */
public class PolicyEngineUtilTest {
    
    @Test
    public void testEmtpy() {
        AssertionType assertion = new AssertionType();
        policyEngineUtil util = new policyEngineUtil();
        CheckPolicyResponseType resp = util.checkAssertionAttributeStatement(assertion);
        assertDeny(resp);
    }
    
    @Test
    public void testAll() {
        AssertionType assertion = new AssertionType();
        
        PersonNameType personName = new PersonNameType();
        personName.setFamilyName("Prime");
        personName.setGivenName("Optimus");
        personName.setSecondNameOrInitials("T");
        assertion.setPersonName(personName);
        
        UserType userInfo = new UserType();
        HomeCommunityType org = new HomeCommunityType();
        org.setName("Cybertron");
        org.setHomeCommunityId("Org Id");
        userInfo.setOrg(org);
        userInfo.setPersonName(personName);
        
        CeType roleCoded = new CeType();
        roleCoded.setCode("role coded code");
        roleCoded.setCodeSystem("code system");
        roleCoded.setCodeSystemName("code system name");
        roleCoded.setDisplayName("display name");
        userInfo.setRoleCoded(roleCoded);
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
        
        assertion.getUniquePatientId().add("unique patient Id");
        
        assertion.setNationalProviderId("npi");
        policyEngineUtil util = new policyEngineUtil();
        CheckPolicyResponseType resp = util.checkAssertionAttributeStatement(assertion);
        assertPermit(resp);
    }
    
    @Test
    public void testNoOrgId() {
        AssertionType assertion = new AssertionType();
        
        PersonNameType personName = new PersonNameType();
        personName.setFamilyName("Prime");
        personName.setGivenName("Optimus");
        personName.setSecondNameOrInitials("T");
        assertion.setPersonName(personName);
        
        UserType userInfo = new UserType();
        HomeCommunityType org = new HomeCommunityType();
        org.setName("Cybertron");
        userInfo.setOrg(org);
        
        CeType roleCoded = new CeType();
        roleCoded.setCode("role coded code");
        roleCoded.setCodeSystem("code system");
        roleCoded.setCodeSystemName("code system name");
        roleCoded.setDisplayName("display name");
        userInfo.setRoleCoded(roleCoded);
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
        
        assertion.getUniquePatientId().add("unique patient Id");
        
        assertion.setNationalProviderId("npi");
        policyEngineUtil util = new policyEngineUtil();
        CheckPolicyResponseType resp = util.checkAssertionAttributeStatement(assertion);
        assertDeny(resp);
    }
    
    @Test
    public void testNoRoleCodeSystemName() {
        AssertionType assertion = new AssertionType();
        
        PersonNameType personName = new PersonNameType();
        personName.setFamilyName("Prime");
        personName.setGivenName("Optimus");
        personName.setSecondNameOrInitials("T");
        assertion.setPersonName(personName);
        
        UserType userInfo = new UserType();
        HomeCommunityType org = new HomeCommunityType();
        org.setName("Cybertron");
        org.setHomeCommunityId("Org Id");
        userInfo.setOrg(org);
        
        CeType roleCoded = new CeType();
        roleCoded.setCode("role coded code");
        roleCoded.setCodeSystem("code system");
        roleCoded.setDisplayName("display name");
        userInfo.setRoleCoded(roleCoded);
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
        
        assertion.getUniquePatientId().add("unique patient Id");
        
        assertion.setNationalProviderId("npi");
        policyEngineUtil util = new policyEngineUtil();
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
}
