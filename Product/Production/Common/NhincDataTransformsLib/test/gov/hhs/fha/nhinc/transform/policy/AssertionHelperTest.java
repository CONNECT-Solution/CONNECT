/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.policy;

import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;
import gov.hhs.fha.nhinc.callback.Base64Coder;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthnStatementType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceAssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceConditionsType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlSignatureKeyInfoType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlSignatureType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oasis.names.tc.xacml._2_0.context.schema.os.AttributeType;
import oasis.names.tc.xacml._2_0.context.schema.os.AttributeValueType;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class AssertionHelperTest {

    private static Log log = LogFactory.getLog(AssertionHelperTest.class);
    private static final String EMPTY_STRING = "";
    private static final String TEST_HC_VAL = "England";
    private static final String TEST_PAT_ID_VAL = "12345^^^&1.2.333&ISO";
    private static final String TEST_USER_FIRST_NAME_VAL = "Henry";
    private static final String TEST_USER_MIDDLE_NAME_VAL = "The";
    private static final String TEST_USER_LAST_NAME_VAL = "Eighth";
    private static final String TEST_USER_NAME_VAL = "henry8";
    private static final String TEST_USER_HC_VAL = "Wales";
    private static final String TEST_USER_HC_ID_VAL = "UK2";
    private static final String TEST_USER_ROLE_CODE_VAL = "Tudor2";
    private static final String TEST_USER_ROLE_CODE_SY_VAL = "Royality";
    private static final String TEST_USER_ROLE_CODE_SY_NM_VAL = "House of Tudor";
    private static final String TEST_USER_ROLE_CODE_DSP_NM_VAL = "Henry VIII King of England";
    private static final String TEST_USER_PURPOSE_CODE_VAL = "Separation1";
    private static final String TEST_USER_PURPOSE_CODE_SY_VAL = "Religion";
    private static final String TEST_USER_PURPOSE_CODE_SY_NM_VAL = "Church of England";
    private static final String TEST_USER_PURPOSE_CODE_DSP_NM_VAL = "Split from Roman Catholic Authority";
    private static final String TEST_SIGN_DATE_VAL = "June 28, 1491";
    private static final String TEST_EXP_DATE_VAL = "Jan 28, 1547";
    private static final String TEST_CLAIM_REF_VAL = "Henry7";
    private static final String TEST_AUTH_INSTANCE_VAL = "June 24, 1509";
    private static final String TEST_SESSION_IDX_VAL = "1509";
    private static final String TEST_AUTH_CNTX_CLS_VAL = "Coronation";
    private static final String TEST_AUTH_SUBJ_LOC_VAL = "Westminister Abbey";
    private static final String TEST_AUTH_SUBJ_DNS_VAL = "London";
    private static final String TEST_DECISION_VAL = "Permit";
    private static final String TEST_RESOURCE_VAL = "RoyalTreasury";
    private static final String TEST_ACTION_VAL = "MarryBrothersWife";
    private static final String TEST_EV_ID_VAL = "Firstborn";
    private static final String TEST_EV_INSTANCE_VAL = "Feb 18, 1516";
    private static final String TEST_EV_VERSION_VAL = "1";
    private static final String TEST_EV_ISSUER_VAL = "Catherine of Aragon";
    private static final String TEST_EV_CONTENT_REF_VAL = "Bloody_Mary_1";
    private static final String TEST_EV_CONTENT_TYPE_VAL = "Daughter";
    private static final String TEST_EV_COND_BEFORE_VAL = "July 19, 1553";
    private static final String TEST_EV_COND_AFTER_VAL = "Nov 17 1558";
    private AssertionType assertion;

    public AssertionHelperTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        assertion = createTestAssertion();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of appendAssertionDataToRequest method, of class AssertionHelper.
     */
    @Test
    public void testAppendAssertionDataToRequest() {
        log.info("testAppendAssertionDataToRequest");

        RequestType policyRequest = new RequestType();
        AssertionHelper assertHelp = new AssertionHelper();
        assertHelp.appendAssertionDataToRequest(policyRequest, assertion);

        verifyPolicySubject(policyRequest);
        verifyPolicyResource(policyRequest);
    }

    /**
     * Test of appendAssertionDataToRequest method, of class AssertionHelper.
     */
    @Test
    public void testNullAssertionDataToRequest() {
        log.info("testNullAssertionDataToRequest");
        Mockery mockery = new Mockery();
        final Log mockLog = mockery.mock(Log.class);

        RequestType policyRequest = new RequestType();
        AssertionHelper assertHelp = new AssertionHelper() {

            @Override
            protected Log createLogger() {
                return mockLog;
            }
        };
        mockery.checking(new Expectations() {

            {
                oneOf(mockLog).debug("begin appending assertion data to xacml request");
                oneOf(mockLog).warn("assertion was not set - unable to extract assertion related data to send to policy engine");
                oneOf(mockLog).debug("end appending assertion data to xacml request");
            }
        });
        assertHelp.appendAssertionDataToRequest(policyRequest, null);

    }

    private AssertionType createTestAssertion() {
        AssertionType assertOut = new AssertionType();

        CeType purposeCoded = new CeType();
        UserType user = new UserType();
        PersonNameType userPerson = new PersonNameType();
        CeType userRole = new CeType();
        HomeCommunityType userHc = new HomeCommunityType();
        HomeCommunityType homeCom = new HomeCommunityType();
        SamlAuthnStatementType samlAuthnStatement = new SamlAuthnStatementType();
        SamlAuthzDecisionStatementType samlAuthzDecisionStatement = new SamlAuthzDecisionStatementType();
        SamlAuthzDecisionStatementEvidenceType samlAuthzDecisionStatementEvidence = new SamlAuthzDecisionStatementEvidenceType();
        SamlAuthzDecisionStatementEvidenceAssertionType samlAuthzDecisionStatementAssertion = new SamlAuthzDecisionStatementEvidenceAssertionType();
        SamlAuthzDecisionStatementEvidenceConditionsType samlAuthzDecisionStatementEvidenceConditions = new SamlAuthzDecisionStatementEvidenceConditionsType();
        SamlSignatureType samlSignature = new SamlSignatureType();
        SamlSignatureKeyInfoType samlSignatureKeyInfo = new SamlSignatureKeyInfoType();

        assertOut.setHomeCommunity(homeCom);
        homeCom.setHomeCommunityId(TEST_HC_VAL);

        assertOut.getUniquePatientId().clear();
        assertOut.getUniquePatientId().add(TEST_PAT_ID_VAL);

        user.setPersonName(userPerson);
        user.setOrg(userHc);
        user.setRoleCoded(userRole);
        assertOut.setUserInfo(user);
        assertOut.setPurposeOfDisclosureCoded(purposeCoded);

        userPerson.setGivenName(TEST_USER_FIRST_NAME_VAL);
        userPerson.setFamilyName(TEST_USER_LAST_NAME_VAL);
        userPerson.setSecondNameOrInitials(TEST_USER_MIDDLE_NAME_VAL);
        userPerson.setFullName(TEST_USER_FIRST_NAME_VAL + TEST_USER_MIDDLE_NAME_VAL + TEST_USER_LAST_NAME_VAL);

        userHc.setName(TEST_USER_HC_VAL);
        userHc.setHomeCommunityId(TEST_USER_HC_ID_VAL);
        user.setUserName(TEST_USER_NAME_VAL);
        userRole.setCode(TEST_USER_ROLE_CODE_VAL);
        userRole.setCodeSystem(TEST_USER_ROLE_CODE_SY_VAL);
        userRole.setCodeSystemName(TEST_USER_ROLE_CODE_SY_NM_VAL);
        userRole.setDisplayName(TEST_USER_ROLE_CODE_DSP_NM_VAL);

        purposeCoded.setCode(TEST_USER_PURPOSE_CODE_VAL);
        purposeCoded.setCodeSystem(TEST_USER_PURPOSE_CODE_SY_VAL);
        purposeCoded.setCodeSystemName(TEST_USER_PURPOSE_CODE_SY_NM_VAL);
        purposeCoded.setDisplayName(TEST_USER_PURPOSE_CODE_DSP_NM_VAL);

        assertOut.setDateOfSignature(TEST_SIGN_DATE_VAL);
        assertOut.setExpirationDate(TEST_EXP_DATE_VAL);
        assertOut.setClaimFormRef(TEST_CLAIM_REF_VAL);

        byte[] formRaw = EMPTY_STRING.getBytes();
        assertOut.setClaimFormRaw(formRaw);

        assertOut.setSamlAuthnStatement(samlAuthnStatement);
        samlAuthnStatement.setAuthInstant(TEST_AUTH_INSTANCE_VAL);
        samlAuthnStatement.setSessionIndex(TEST_SESSION_IDX_VAL);
        samlAuthnStatement.setAuthContextClassRef(TEST_AUTH_CNTX_CLS_VAL);
        samlAuthnStatement.setSubjectLocalityAddress(TEST_AUTH_SUBJ_LOC_VAL);
        samlAuthnStatement.setSubjectLocalityDNSName(TEST_AUTH_SUBJ_DNS_VAL);

        assertOut.setSamlAuthzDecisionStatement(samlAuthzDecisionStatement);
        samlAuthzDecisionStatement.setDecision(TEST_DECISION_VAL);
        samlAuthzDecisionStatement.setResource(TEST_RESOURCE_VAL);
        samlAuthzDecisionStatement.setAction(TEST_ACTION_VAL);

        samlAuthzDecisionStatement.setEvidence(samlAuthzDecisionStatementEvidence);

        samlAuthzDecisionStatementEvidence.setAssertion(samlAuthzDecisionStatementAssertion);
        samlAuthzDecisionStatementAssertion.setId(TEST_EV_ID_VAL);
        samlAuthzDecisionStatementAssertion.setIssueInstant(TEST_EV_INSTANCE_VAL);
        samlAuthzDecisionStatementAssertion.setVersion(TEST_EV_VERSION_VAL);
        samlAuthzDecisionStatementAssertion.setIssuer(TEST_EV_ISSUER_VAL);
        samlAuthzDecisionStatementAssertion.setContentReference(TEST_EV_CONTENT_REF_VAL);
        samlAuthzDecisionStatementAssertion.setContentType(TEST_EV_CONTENT_TYPE_VAL);
        samlAuthzDecisionStatementAssertion.setContent(formRaw);

        samlAuthzDecisionStatementAssertion.setConditions(samlAuthzDecisionStatementEvidenceConditions);
        samlAuthzDecisionStatementEvidenceConditions.setNotBefore(TEST_EV_COND_BEFORE_VAL);
        samlAuthzDecisionStatementEvidenceConditions.setNotOnOrAfter(TEST_EV_COND_AFTER_VAL);

        assertOut.setSamlSignature(samlSignature);
        samlSignature.setSignatureValue(formRaw);

        samlSignature.setKeyInfo(samlSignatureKeyInfo);
        samlSignatureKeyInfo.setRsaKeyValueExponent(formRaw);
        samlSignatureKeyInfo.setRsaKeyValueModulus(formRaw);

        return assertOut;
    }

    private void verifyPolicySubject(RequestType policyRequest) {

        Map expectSubjAttr = new HashMap();
        String[] ids = {"urn:gov:hhs:fha:nhinc:saml-authn-statement:auth-instant",
            "urn:gov:hhs:fha:nhinc:saml-authn-statement:session-index",
            "urn:gov:hhs:fha:nhinc:saml-authn-statement:auth-context-class-ref",
            "urn:oasis:names:tc:xacml:1.0:subject:authn-locality:ip-address",
            "urn:oasis:names:tc:xacml:1.0:subject:authn-locality:dns-name",
            "urn:oasis:names:tc:xacml:1.0:subject:subject-id",
            "urn:oasis:names:tc:xspa:1.0:subject:organization-id",
            "http://www.hhs.gov/healthit/nhin#HomeCommunityId",
            "urn:gov:hhs:fha:nhinc:user-organization-name",
            "urn:oasis:names:tc:xacml:2.0:subject:role",
            "urn:gov:hhs:fha:nhinc:user-role-code-system",
            "urn:gov:hhs:fha:nhinc:user-role-code-system-name",
            "urn:gov:hhs:fha:nhinc:user-role-description",
            "urn:oasis:names:tc:xspa:1.0:subject:purposeofuse",
            "urn:gov:hhs:fha:nhinc:purpose-of-use-code-system",
            "urn:gov:hhs:fha:nhinc:purpose-of-use-code-system-name",
            "urn:gov:hhs:fha:nhinc:purpose-of-use-display-name"};
        String[] vals = {TEST_AUTH_INSTANCE_VAL,
            TEST_SESSION_IDX_VAL,
            TEST_AUTH_CNTX_CLS_VAL,
            TEST_AUTH_SUBJ_LOC_VAL,
            TEST_AUTH_SUBJ_DNS_VAL,
            TEST_USER_NAME_VAL,
            TEST_USER_HC_ID_VAL,
            TEST_HC_VAL,
            TEST_USER_HC_VAL,
            TEST_USER_ROLE_CODE_VAL,
            TEST_USER_ROLE_CODE_SY_VAL,
            TEST_USER_ROLE_CODE_SY_NM_VAL,
            TEST_USER_ROLE_CODE_DSP_NM_VAL,
            TEST_USER_PURPOSE_CODE_VAL,
            TEST_USER_PURPOSE_CODE_SY_VAL,
            TEST_USER_PURPOSE_CODE_SY_NM_VAL,
            TEST_USER_PURPOSE_CODE_DSP_NM_VAL};
        List matchIdList = new ArrayList();

        for (int idx = 0; idx < ids.length; idx++) {
            expectSubjAttr.put(ids[idx], vals[idx]);
        }

        String noSubjMessage = "Request has no Subject";
        assertNotNull(noSubjMessage, policyRequest.getSubject());
        assertFalse(noSubjMessage, policyRequest.getSubject().isEmpty());
        if (policyRequest.getSubject() != null && !policyRequest.getSubject().isEmpty()) {
            for (SubjectType subj : policyRequest.getSubject()) {
                String noAttrMessage = "Request Subject has no Attributes";
                assertNotNull(noAttrMessage, subj.getAttribute());
                assertFalse(noAttrMessage, subj.getAttribute().isEmpty());
                if (subj.getAttribute() != null && !subj.getAttribute().isEmpty()) {
                    for (AttributeType attr : subj.getAttribute()) {
                        String idMessage = "Subject Attribute: " + attr.getAttributeId() + " not found";
                        assertTrue(idMessage, expectSubjAttr.containsKey(attr.getAttributeId()));
                        if (expectSubjAttr.containsKey(attr.getAttributeId())) {
                            matchIdList.add(attr.getAttributeId());
                            String expectedVal = (String) expectSubjAttr.get(attr.getAttributeId());
                            AttributeValueType expectedAttrVal = new AttributeValueType();
                            expectedAttrVal.getContent().add(expectedVal);

                            String noAttrValMessage = "Request Subject Attribute " + attr.getAttributeId() + "has no AttributeValues";
                            assertNotNull(noAttrValMessage, attr.getAttributeValue());
                            assertFalse(noAttrValMessage, attr.getAttributeValue().isEmpty());
                            for (AttributeValueType actAttrVal : attr.getAttributeValue()) {
                                String valMessage = "Subject Attribute: " + attr.getAttributeId() + " expects value: " + expectedVal;
                                assertArrayEquals(valMessage, expectedAttrVal.getContent().toArray(), actAttrVal.getContent().toArray());
                            }
                        }
                    }
                }
            }
        }

        for (int idx = 0; idx < ids.length; idx++) {
            String missingAttrMessage = "Constructed Subject is missing attribute: " + ids[idx];
            assertTrue(missingAttrMessage, matchIdList.contains(ids[idx]));
        }
    }

    private void verifyPolicyResource(RequestType policyRequest) {
        Map expectResAttr = new HashMap();
        String[] ids = {"urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-decision",
            "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-resource",
            "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-action",
            "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-id",
            "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-issue-instant",
            "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-version",
            "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-issuer",
            "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-not-before",
            "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-not-on-or-after",
            "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-content-reference",
            "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-content-type"};
        String[] vals = {TEST_DECISION_VAL,
            TEST_RESOURCE_VAL,
            TEST_ACTION_VAL,
            TEST_EV_ID_VAL,
            TEST_EV_INSTANCE_VAL,
            TEST_EV_VERSION_VAL,
            TEST_EV_ISSUER_VAL,
            TEST_EV_COND_BEFORE_VAL,
            TEST_EV_COND_AFTER_VAL,
            TEST_EV_CONTENT_REF_VAL,
            TEST_EV_CONTENT_TYPE_VAL};
        List matchIdList = new ArrayList();

        for (int idx = 0; idx < ids.length; idx++) {
            expectResAttr.put(ids[idx], vals[idx]);
        }

        String noResMessage = "Request has no Resource";
        assertNotNull(noResMessage, policyRequest.getResource());
        assertFalse(noResMessage, policyRequest.getResource().isEmpty());
        if (policyRequest.getResource() != null && !policyRequest.getResource().isEmpty()) {
            for (ResourceType res : policyRequest.getResource()) {
                String noAttrMessage = "Request Resource has no Attributes";
                assertNotNull(noAttrMessage, res.getAttribute());
                assertFalse(noAttrMessage, res.getAttribute().isEmpty());
                if (res.getAttribute() != null && !res.getAttribute().isEmpty()) {
                    for (AttributeType attr : res.getAttribute()) {
                        matchIdList.add(attr.getAttributeId());
                        //handle II and binary types separately
                        if ("http://www.hhs.gov/healthit/nhin#subject-id".equals(attr.getAttributeId())) {
                            verifyUniquePatientId(attr);
                        } else if ("urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-asssertion-content".equals(attr.getAttributeId())) {
                            verifyEvidenceContent(attr);
                        } else {
                            String idMessage = "Resource Attribute: " + attr.getAttributeId() + " not found";
                            assertTrue(idMessage, expectResAttr.containsKey(attr.getAttributeId()));
                            if (expectResAttr.containsKey(attr.getAttributeId())) {
                                String expectedVal = (String) expectResAttr.get(attr.getAttributeId());
                                AttributeValueType expectedAttrVal = new AttributeValueType();
                                expectedAttrVal.getContent().add(expectedVal);

                                String noAttrValMessage = "Request Resource Attribute " + attr.getAttributeId() + "has no AttributeValues";
                                assertNotNull(noAttrValMessage, attr.getAttributeValue());
                                assertFalse(noAttrValMessage, attr.getAttributeValue().isEmpty());
                                for (AttributeValueType actAttrVal : attr.getAttributeValue()) {
                                    String valMessage = "Resource Attribute: " + attr.getAttributeId() + " expects value: " + expectedVal;
                                    assertArrayEquals(valMessage, expectedAttrVal.getContent().toArray(), actAttrVal.getContent().toArray());
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int idx = 0; idx < ids.length; idx++) {
            String missingAttrMessage = "Constructed Resource is missing attribute: " + ids[idx];
            assertTrue(missingAttrMessage, matchIdList.contains(ids[idx]));
        }
        String missingAttrMessage = "Constructed Resource is missing attribute http://www.hhs.gov/healthit/nhin#subject-id";
        assertTrue(missingAttrMessage, matchIdList.contains("http://www.hhs.gov/healthit/nhin#subject-id"));
        missingAttrMessage = "Constructed Resource is missing attribute urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-asssertion-content";
        assertTrue(missingAttrMessage, matchIdList.contains("urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-asssertion-content"));
    }

    private void verifyUniquePatientId(AttributeType attr) {

        for (AttributeValueType actAttrVal : attr.getAttributeValue()) {
            if (actAttrVal.getContent() != null && !actAttrVal.getContent().isEmpty()) {
                for (Object obj : actAttrVal.getContent()) {
                    if (obj instanceof ElementNSImpl) {
                        ElementNSImpl elem = (ElementNSImpl) obj;
                        NamedNodeMap attrMap = elem.getAttributes();
                        if ((attrMap != null) &&
                                (attrMap.getLength() > 0)) {
                            int numMapNodes = attrMap.getLength();
                            for (int attrIdx = 0; attrIdx < numMapNodes; attrIdx++) {
                                Node attrNode = attrMap.item(attrIdx);
                                if ((attrNode != null) &&
                                        (attrNode.getNodeName() != null) &&
                                        (!attrNode.getNodeName().isEmpty())) {
                                    if (attrNode.getNodeName().equals("extension")) {
                                        assertTrue("1.2.333".equals(attrNode.getNodeValue()));
                                    } else if (attrNode.getNodeName().equals("root")) {
                                        assertTrue("12345".equals(attrNode.getNodeValue()));
                                    } else {
                                        fail("Unique Patient Element should have 2 attributes: extension and root");
                                    }
                                } else {
                                    fail("Unique Patient Element should have 2 attributes: extension and root");
                                }
                            }
                        } else {
                            fail("Unique Patient Element should have 2 attributes: extension and root");
                        }
                    } else {
                        fail("Unique Patient should be an Element type");
                    }
                }
            } else {
                fail("No content was found for the Unique Patient Id");
            }
        }
    }

    private void verifyEvidenceContent(AttributeType attr) {
        AttributeValueType expectedAttrVal = new AttributeValueType();
        byte[] formRaw = EMPTY_STRING.getBytes();
        String sValue = new String(formRaw);
        String sEncodedValue = Base64Coder.encodeString(sValue);
        expectedAttrVal.getContent().add(sEncodedValue);

        for (AttributeValueType actAttrVal : attr.getAttributeValue()) {
            String valMessage = "Resource Attribute: " + attr.getAttributeId() + " expects value: " + sEncodedValue;
            assertArrayEquals(valMessage, expectedAttrVal.getContent().toArray(), actAttrVal.getContent().toArray());
        }
    }
}
