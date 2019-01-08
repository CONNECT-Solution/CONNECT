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
package gov.hhs.fha.nhinc.saml.extraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthnStatementType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceAssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceConditionsType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlIssuerType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

/**
 * @author achidambaram
 *
 */
public class SamlTokenCreatorTest {

    private final String RESOURCE_URL = "http://CONNECT/Endpoint";

    @Test
    public void CreateRequestContext() {
        SamlTokenCreator token = new SamlTokenCreator();
        String action = null;
        Map<String, Object> expectedrequestContext;
        expectedrequestContext = token.createRequestContext(createAssertionInfo(), RESOURCE_URL, action);

        // TODO: Remove system.out
        System.out.println(expectedrequestContext);

        testUserName(expectedrequestContext);
        testUserHomeCommunityId(expectedrequestContext);
        testUserOrganizationName(expectedrequestContext);
        testUserInfoCode(expectedrequestContext);
        testUserInfoCodeSystem(expectedrequestContext);
        testUserInfoCodeSystemName(expectedrequestContext);
        testUserInfoCodedisplayName(expectedrequestContext);
        testPersonNameTypeFirstName(expectedrequestContext);
        testPersonNameTypeMiddleName(expectedrequestContext);
        testPersonNameTypeLastName(expectedrequestContext);
        testPurposeOfUseRoleCode(expectedrequestContext);
        testPurposeOfUseCodeSystem(expectedrequestContext);
        testPurposeOfUseCodeSystemName(expectedrequestContext);
        testPurposeOfUseDisplayName(expectedrequestContext);
        testHomeCommunity(expectedrequestContext);
        testAuthnInstant(expectedrequestContext);
        testAuthnSessionIndex(expectedrequestContext);
        testAuthnContextClass(expectedrequestContext);
        testSubjectLocalityAddress(expectedrequestContext);
        testSubjectLocalityDNS(expectedrequestContext);
        testauthzAction(expectedrequestContext);
        testauthzResource(expectedrequestContext);
        testauthzDecision(expectedrequestContext);
        testEvidenceAssertionId(expectedrequestContext);
        testEvidenceAssertionInstant(expectedrequestContext);
        testEvidenceAssertionVersion(expectedrequestContext);
        testEvidenceAssertionIssuer(expectedrequestContext);
        testEvidenceAssertionIssuerFormat(expectedrequestContext);
        testEvidenceSubject(expectedrequestContext);
        testEvidenceConditionNotBefore(expectedrequestContext);
        testEvidenceConditionNotAfter(expectedrequestContext);
        testAssertionIssuer(expectedrequestContext);
        testAssertionIssuerFormat(expectedrequestContext);
    }

    @Test
    public void createRequestContextAssertionNull() {
        AssertionType assertion = null;
        String url = null;
        String action = null;
        SamlTokenCreator token = new SamlTokenCreator();

        Map<String, Object> requestContext = token.createRequestContext(assertion, url, action);
        assertEquals("requestContext created with null params should have exactly one entry", requestContext.size(), 1);
    }

    @Test
    public void createRequestContextUserNameNull() {
        AssertionType assertion = new AssertionType();
        UserType userInfo = new UserType();
        PersonNameType person = null;
        userInfo.setPersonName(person);
        assertion.setUserInfo(userInfo);
        String url = null;
        String action = null;
        SamlTokenCreator token = new SamlTokenCreator();
        Map<String, Object> expectedrequestContext = token.createRequestContext(assertion, url, action);
        assertFalse(expectedrequestContext.containsKey("userName"));
    }

    @Test
    public void createRequestContextUserInfoOrgNull() {
        AssertionType assertion = new AssertionType();
        UserType userInfo = new UserType();
        HomeCommunityType home = new HomeCommunityType();
        userInfo.setOrg(home);
        assertion.setUserInfo(userInfo);
        String url = null;
        String action = null;
        SamlTokenCreator token = new SamlTokenCreator();
        Map<String, Object> expectedrequestContext = token.createRequestContext(assertion, url, action);
        assertFalse(expectedrequestContext.containsKey("userOrganization"));
    }

    @Test
    public void createRequestContextUserInfoRoleCodedNull() {
        AssertionType assertion = new AssertionType();
        UserType userInfo = new UserType();
        HomeCommunityType home = new HomeCommunityType();
        home.setHomeCommunityId("1.1");
        home.setName("CONNECT");
        userInfo.setOrg(home);
        CeType ce = new CeType();
        userInfo.setRoleCoded(ce);
        assertion.setUserInfo(userInfo);
        String url = null;
        String action = null;
        SamlTokenCreator token = new SamlTokenCreator();
        Map<String, Object> expectedrequestContext = token.createRequestContext(assertion, url, action);
        assertFalse(expectedrequestContext.containsKey("userRoleCode"));
    }

    private void testAssertionIssuerFormat(Map<String, Object> expectedrequestContext) {
        String assertionIssuerFormat = "assertionIssuerFormat";
        assertEquals(testHashmapValues(expectedrequestContext, assertionIssuerFormat),
            createAssertionInfo().getSamlIssuer().getIssuerFormat());
    }

    private void testAssertionIssuer(Map<String, Object> expectedrequestContext) {
        String assertionIssuer = "assertionIssuer";
        assertEquals(testHashmapValues(expectedrequestContext, assertionIssuer),
            createAssertionInfo().getSamlIssuer().getIssuer());
    }

    private void testEvidenceConditionNotAfter(Map<String, Object> expectedrequestContext) {
        String evidenceConditionNotAfter = "evidenceConditionNotAfter";
        assertEquals(testHashmapValues(expectedrequestContext, evidenceConditionNotAfter), createAssertionInfo()
            .getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().getNotOnOrAfter());
    }

    private void testEvidenceConditionNotBefore(Map<String, Object> expectedrequestContext) {
        String evidenceConditionNotBefore = "evidenceConditionNotBefore";
        assertEquals(testHashmapValues(expectedrequestContext, evidenceConditionNotBefore), createAssertionInfo()
            .getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().getNotBefore());
    }

    private void testEvidenceSubject(Map<String, Object> expectedrequestContext) {
        String evidenceSubject = "evidenceSubject";
        assertEquals(testHashmapValues(expectedrequestContext, evidenceSubject),
            createAssertionInfo().getSamlAuthzDecisionStatement().getEvidence().getAssertion().getSubject());
    }

    private void testEvidenceAssertionIssuerFormat(Map<String, Object> expectedrequestContext) {
        String evidenceAssertionIssuerFormat = "evidenceAssertionIssuerFormat";
        assertEquals(testHashmapValues(expectedrequestContext, evidenceAssertionIssuerFormat),
            createAssertionInfo().getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssuerFormat());
    }

    private void testEvidenceAssertionIssuer(Map<String, Object> expectedrequestContext) {
        String evidenceAssertionIssuer = "evidenceAssertionIssuer";
        assertEquals(testHashmapValues(expectedrequestContext, evidenceAssertionIssuer),
            createAssertionInfo().getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssuer());
    }

    private void testEvidenceAssertionVersion(Map<String, Object> expectedrequestContext) {
        String evidenceAssertionVersion = "evidenceAssertionVersion";
        assertEquals(testHashmapValues(expectedrequestContext, evidenceAssertionVersion),
            createAssertionInfo().getSamlAuthzDecisionStatement().getEvidence().getAssertion().getVersion());
    }

    private void testEvidenceAssertionInstant(Map<String, Object> expectedrequestContext) {
        String evidenceAssertionInstant = "evidenceAssertionInstant";
        assertEquals(testHashmapValues(expectedrequestContext, evidenceAssertionInstant),
            createAssertionInfo().getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssueInstant());
    }

    private void testEvidenceAssertionId(Map<String, Object> expectedrequestContext) {
        String evidenceAssertionId = "evidenceAssertionId";
        assertEquals(testHashmapValues(expectedrequestContext, evidenceAssertionId),
            createAssertionInfo().getSamlAuthzDecisionStatement().getEvidence().getAssertion().getId());
    }

    private void testauthzDecision(Map<String, Object> expectedrequestContext) {
        String authzdecision = "authzDecision";
        assertEquals(testHashmapValues(expectedrequestContext, authzdecision),
            createAssertionInfo().getSamlAuthzDecisionStatement().getDecision());
    }

    private void testauthzResource(Map<String, Object> expectedrequestContext) {
        String resource = "resource";
        assertEquals(testHashmapValues(expectedrequestContext, resource), RESOURCE_URL);
    }

    private void testauthzAction(Map<String, Object> expectedrequestContext) {
        String action = "action";
        assertEquals(testHashmapValues(expectedrequestContext, action),
            createAssertionInfo().getSamlAuthzDecisionStatement().getAction());
    }

    private void testSubjectLocalityDNS(Map<String, Object> expectedrequestContext) {
        String subjectLocalityDNS = "subjectLocalityDNS";
        assertEquals(testHashmapValues(expectedrequestContext, subjectLocalityDNS),
            createAssertionInfo().getSamlAuthnStatement().getSubjectLocalityDNSName());
    }

    private void testSubjectLocalityAddress(Map<String, Object> expectedrequestContext) {
        String subjectLocalityAddress = "subjectLocalityAddress";
        assertEquals(testHashmapValues(expectedrequestContext, subjectLocalityAddress),
            createAssertionInfo().getSamlAuthnStatement().getSubjectLocalityAddress());
    }

    private void testAuthnContextClass(Map<String, Object> expectedrequestContext) {
        String authnContextClass = "authnContextClass";
        assertEquals(testHashmapValues(expectedrequestContext, authnContextClass),
            createAssertionInfo().getSamlAuthnStatement().getAuthContextClassRef());
    }

    private void testAuthnSessionIndex(Map<String, Object> expectedrequestContext) {
        String authnSessionIndex = "authnSessionIndex";
        assertEquals(testHashmapValues(expectedrequestContext, authnSessionIndex),
            createAssertionInfo().getSamlAuthnStatement().getSessionIndex());
    }

    private void testAuthnInstant(Map<String, Object> expectedrequestContext) {
        String authnInstant = "authnInstant";
        assertEquals(testHashmapValues(expectedrequestContext, authnInstant),
            createAssertionInfo().getSamlAuthnStatement().getAuthInstant());
    }

    private void testHomeCommunity(Map<String, Object> expectedrequestContext) {
        String homeCommunity = "homeCommunity";
        assertEquals(testHashmapValues(expectedrequestContext, homeCommunity),
            createAssertionInfo().getHomeCommunity().getHomeCommunityId());

    }

    private void testPurposeOfUseDisplayName(Map<String, Object> expectedrequestContext) {
        String purposeOfUseDisplayName = "purposeOfUseDisplayName";
        assertEquals(testHashmapValues(expectedrequestContext, purposeOfUseDisplayName),
            createAssertionInfo().getPurposeOfDisclosureCoded().getDisplayName());
    }

    private void testPurposeOfUseCodeSystemName(Map<String, Object> expectedrequestContext) {
        String purposeOfUseCodeSystemName = "purposeOfUseCodeSystemName";
        assertEquals(testHashmapValues(expectedrequestContext, purposeOfUseCodeSystemName),
            SamlConstants.PURPOSE_SYSTEMNAME_ATTR);
    }

    private void testPurposeOfUseCodeSystem(Map<String, Object> expectedrequestContext) {
        String purposeOfUseCodeSystem = "purposeOfUseCodeSystem";
        assertEquals(testHashmapValues(expectedrequestContext, purposeOfUseCodeSystem),
            SamlConstants.PURPOSE_SYSTEM_ATTR);
    }

    private void testPurposeOfUseRoleCode(Map<String, Object> expectedrequestContext) {
        String purposeOfUseRoleCode = "purposeOfUseRoleCode";
        assertEquals(testHashmapValues(expectedrequestContext, purposeOfUseRoleCode),
            createAssertionInfo().getPurposeOfDisclosureCoded().getCode());
    }

    private void testPersonNameTypeLastName(Map<String, Object> expectedrequestContext) {
        String userLastName = "userLastName";
        assertEquals(testHashmapValues(expectedrequestContext, userLastName),
            createAssertionInfo().getPersonName().getFamilyName());
    }

    private void testPersonNameTypeMiddleName(Map<String, Object> expectedrequestContext) {
        String userMiddleName = "userMiddleName";
        assertEquals(testHashmapValues(expectedrequestContext, userMiddleName),
            createAssertionInfo().getPersonName().getSecondNameOrInitials());
    }

    private void testPersonNameTypeFirstName(Map<String, Object> expectedrequestContext) {
        String userFirstName = "userFirstName";
        assertEquals(testHashmapValues(expectedrequestContext, userFirstName),
            createAssertionInfo().getPersonName().getGivenName());
    }

    private void testUserInfoCodedisplayName(Map<String, Object> expectedrequestContext) {
        String userRoleCodeDisplayName = "userRoleCodeDisplayName";
        assertEquals(testHashmapValues(expectedrequestContext, userRoleCodeDisplayName),
            createAssertionInfo().getUserInfo().getRoleCoded().getDisplayName());
    }

    private void testUserInfoCodeSystemName(Map<String, Object> expectedrequestContext) {
        String userRoleCodeSystemName = "userRoleCodeSystemName";
        assertEquals(testHashmapValues(expectedrequestContext, userRoleCodeSystemName),
            SamlConstants.USER_SYST_NAME_ATTR);
    }

    private void testUserInfoCodeSystem(Map<String, Object> expectedrequestContext) {
        String userRoleCodeSystem = "userRoleCodeSystem";
        assertEquals(testHashmapValues(expectedrequestContext, userRoleCodeSystem), SamlConstants.USER_SYST_ATTR);
    }

    private void testUserInfoCode(Map<String, Object> expectedrequestContext) {
        String userRoleCode = "userRoleCode";
        assertEquals(testHashmapValues(expectedrequestContext, userRoleCode),
            createAssertionInfo().getUserInfo().getRoleCoded().getCode());
    }

    private void testUserOrganizationName(Map<String, Object> expectedrequestContext) {
        String userOrganizationName = "userOrganization";
        assertEquals(testHashmapValues(expectedrequestContext, userOrganizationName),
            createAssertionInfo().getUserInfo().getOrg().getName());
    }

    private void testUserHomeCommunityId(Map<String, Object> expectedrequestContext) {
        String userOrganizationId = "userOrganizationID";
        assertEquals(testHashmapValues(expectedrequestContext, userOrganizationId),
            createAssertionInfo().getUserInfo().getOrg().getHomeCommunityId());
    }

    private void testUserName(Map<String, Object> expectedrequestContext) {
        String userName = "userName";
        assertEquals(testHashmapValues(expectedrequestContext, userName),
            createAssertionInfo().getUserInfo().getUserName());
    }

    private String testHashmapValues(Map<String, Object> map, String attribute) {
        Set<String> keys = map.keySet();
        Iterator<String> itr = keys.iterator();
        String key;

        while (itr.hasNext()) {
            key = itr.next();
            if (key.equals(attribute)) {
                return (String) map.get(key);
            }
        }
        return null;
    }

    private AssertionType createAssertionInfo() {
        AssertionType assertion = new AssertionType();
        assertion.setPersonName(createPerson());
        assertion.setHomeCommunity(createHomeCommunityType());
        assertion.setUserInfo(createUserType());
        assertion.setPurposeOfDisclosureCoded(createCeType());
        assertion.setSamlAuthnStatement(createSamlAuthStatement());
        assertion.setSamlAuthzDecisionStatement(createSamlAuthzDecisionStatementType());
        assertion.setSamlIssuer(createSamlIssuer());
        return assertion;

    }

    private SamlIssuerType createSamlIssuer() {
        SamlIssuerType issuer = new SamlIssuerType();
        issuer.setIssuer("https://www.example.com");
        issuer.setIssuerFormat("urn:oasis:names:tc:SAML:2.0:nameid-format:entity");
        return issuer;
    }

    private SamlAuthzDecisionStatementType createSamlAuthzDecisionStatementType() {
        SamlAuthzDecisionStatementType authz = new SamlAuthzDecisionStatementType();
        authz.setAction("TestSaml");
        authz.setDecision("Permit");
        authz.setEvidence(createSamlAuthzDecisionStatemnetEvidenceType());
        authz.setResource("https://1.1.1.1:8181/SamlReceiveService/SamlProcessWS");
        return authz;
    }

    private SamlAuthzDecisionStatementEvidenceType createSamlAuthzDecisionStatemnetEvidenceType() {
        SamlAuthzDecisionStatementEvidenceType evidence = new SamlAuthzDecisionStatementEvidenceType();
        evidence.setAssertion(createAuthzDSEvidenceAssertionType());
        return evidence;
    }

    private SamlAuthzDecisionStatementEvidenceAssertionType createAuthzDSEvidenceAssertionType() {
        SamlAuthzDecisionStatementEvidenceAssertionType evidenceAssertion = new SamlAuthzDecisionStatementEvidenceAssertionType();
        evidenceAssertion.setId("40df7c0a-ff3e-4b26-baeb-f2910f6d05a9");
        evidenceAssertion.setIssueInstant("2009-04-16T13:10:39.093Z");
        evidenceAssertion.setIssuer("CONNECT");
        evidenceAssertion.setIssuerFormat("urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName");
        evidenceAssertion.setVersion("2.0");
        evidenceAssertion.setConditions(createAuthzDSEvidenceConditionType());
        evidenceAssertion.setSubject("Gallow Younger");
        return evidenceAssertion;
    }

    private SamlAuthzDecisionStatementEvidenceConditionsType createAuthzDSEvidenceConditionType() {
        SamlAuthzDecisionStatementEvidenceConditionsType condition = new SamlAuthzDecisionStatementEvidenceConditionsType();
        condition.setNotBefore("2009-04-16T13:10:39.093Z");
        condition.setNotOnOrAfter("2009-12-31T12:00:00.000Z");
        return condition;
    }

    private SamlAuthnStatementType createSamlAuthStatement() {
        SamlAuthnStatementType auth = new SamlAuthnStatementType();
        auth.setAuthInstant("2013-02-14T18:45:10.738Z");
        auth.setSubjectLocalityAddress("123 Fairfax Lane, Fairfax, VA");
        auth.setSubjectLocalityDNSName("CONNECT.org");
        auth.setSessionIndex("unitTestSession");
        auth.setAuthContextClassRef("http://www.example.com/");
        return auth;
    }

    private UserType createUserType() {
        UserType userInfo = new UserType();
        userInfo.setOrg(createHomeCommunityType());
        userInfo.setPersonName(createPerson());
        userInfo.setUserName("CONNECT");
        userInfo.setRoleCoded(createCeType());
        return userInfo;
    }

    private HomeCommunityType createHomeCommunityType() {
        HomeCommunityType home = new HomeCommunityType();
        home.setName("CONNECT");
        home.setHomeCommunityId("1.1");
        home.setDescription("CONNECTCommunity");
        return home;
    }

    private CeType createCeType() {
        CeType ce = new CeType();
        ce.setCode("code");
        ce.setCodeSystem("codesystem");
        ce.setCodeSystemName("Connect");
        ce.setDisplayName("display");
        return ce;
    }

    private PersonNameType createPerson() {
        PersonNameType person = new PersonNameType();
        person.setFamilyName("HopKins");
        person.setFullName("Michael Hunter");
        person.setGivenName("Michael");
        person.setSecondNameOrInitials("Simmons");
        return person;
    }
}
