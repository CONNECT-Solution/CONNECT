/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.policy;

import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;
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
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
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
import org.junit.Ignore;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class AssertionHelperTest {

    private static Log log = LogFactory.getLog(AssertionHelperTest.class);
    public static final String STRING_DATATYPE = "http://www.w3.org/2001/XMLSchema#string";
    public static final String ANY_URI_DATATYPE = "http://www.w3.org/2001/XMLSchema#anyURI";
    public static final String DATE_DATATYPE = "http://www.w3.org/2001/XMLSchema#date";
    public static final String II_DATATYPE = "urn:hl7-org:v3#II";
    private static final String EMPTY_STRING = "";
    private static final String TEST_HC_VAL = "urn:oid:1.1.1.1.1.1.1";
    private static final String TEST_PAT_ID_VAL = "12345^^^&1.2.333&ISO";
    private static final String TEST_USER_FIRST_NAME_VAL = "Henry";
    private static final String TEST_USER_MIDDLE_NAME_VAL = "The";
    private static final String TEST_USER_LAST_NAME_VAL = "Eighth";
    private static final String TEST_USER_NAME_VAL = "henry8";
    private static final String TEST_USER_HC_VAL = "Wales";
    private static final String TEST_USER_HC_ID_VAL = "urn:oid:2.2.2.2.2.2.2";
    private static final String TEST_USER_ROLE_CODE_VAL = "Tudor2";
    private static final String TEST_USER_ROLE_CODE_SY_VAL = "Royality";
    private static final String TEST_USER_ROLE_CODE_SY_NM_VAL = "House of Tudor";
    private static final String TEST_USER_ROLE_CODE_DSP_NM_VAL = "Henry VIII King of England";
    private static final String TEST_USER_PURPOSE_CODE_VAL = "Separation1";
    private static final String TEST_USER_PURPOSE_CODE_SY_VAL = "Religion";
    private static final String TEST_USER_PURPOSE_CODE_SY_NM_VAL = "Church of England";
    private static final String TEST_USER_PURPOSE_CODE_DSP_NM_VAL = "Split from Roman Catholic Authority";
    private static final String TEST_AUTH_INSTANCE_VAL = "1509-06-24T12:00:00Z";
    private static final String TEST_SESSION_IDX_VAL = "1509";
    private static final String TEST_AUTH_CNTX_CLS_VAL = "Coronation";
    private static final String TEST_AUTH_SUBJ_LOC_VAL = "Westminister Abbey";
    private static final String TEST_AUTH_SUBJ_DNS_VAL = "London";
    private static final String TEST_DECISION_VAL = "Permit";
    private static final String TEST_RESOURCE_VAL = "RoyalTreasury";
    private static final String TEST_ACTION_VAL = "MarryBrothersWife";
    private static final String TEST_EV_ID_VAL = "Firstborn";
    private static final String TEST_EV_INSTANCE_VAL = "1516-02-18T12:00:00Z";
    private static final String TEST_EV_VERSION_VAL = "1";
    private static final String TEST_EV_ISSUER_VAL = "Catherine of Aragon";
    private static final String TEST_EV_ACCESS_CONSENT_VAL = "Bloody_Mary_1";
    private static final String TEST_EV_INST_ACCESS_CONSENT_VAL = "Daughter";
    private static final String TEST_EV_COND_BEFORE_VAL = "1553-07-19T12:00:00Z";
    private static final String TEST_EV_COND_AFTER_VAL = "1558-11-17T12:00:00Z";
    private AssertionType defaultAssertion;

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
        defaultAssertion = createTestAssertion();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of appendAssertionDataToRequest method, of class AssertionHelper.
     */
    @Ignore //move this to INtegration test folder
    @Test
    public void testAppendAssertionDataToRequest() {
        RequestType policyRequest = new RequestType();
        AssertionHelper assertHelp = new AssertionHelper();
        assertHelp.appendAssertionDataToRequest(policyRequest, defaultAssertion);
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

    @Test
    public void userNameFromAssertionToXacml() {
        AssertionType assertion = createTestAssertion();
        String sourceValue = assertion.getUserInfo().getUserName();
        assertFalse(sourceValue.contentEquals(""));
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:oasis:names:tc:xacml:1.0:subject:subject-id", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void userNameFromAssertionToXacmlWithBlankValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getUserInfo().setUserName("");
        String sourceValue = assertion.getUserInfo().getUserName();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:oasis:names:tc:xacml:1.0:subject:subject-id", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void userNameFromAssertionToXacmlWithNullValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getUserInfo().setUserName(null);
        String sourceValue = assertion.getUserInfo().getUserName();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:oasis:names:tc:xacml:1.0:subject:subject-id", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthnStatementAuthInstantFromAssertionToXacml() {
        AssertionType assertion = createTestAssertion();
        String sourceValue = assertion.getSamlAuthnStatement().getAuthInstant();
        assertFalse(sourceValue.contentEquals(""));
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:gov:hhs:fha:nhinc:saml-authn-statement:auth-instant", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthnStatementAuthInstantFromAssertionToXacmlWithBlankValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getSamlAuthnStatement().setAuthInstant("");
        String sourceValue = assertion.getSamlAuthnStatement().getAuthInstant();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:gov:hhs:fha:nhinc:saml-authn-statement:auth-instant", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthnStatementAuthInstantFromAssertionToXacmlWithNullValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getSamlAuthnStatement().setAuthInstant(null);
        String sourceValue = assertion.getSamlAuthnStatement().getAuthInstant();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:gov:hhs:fha:nhinc:saml-authn-statement:auth-instant", sourceValue, STRING_DATATYPE);
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
        samlAuthzDecisionStatementAssertion.setAccessConsentPolicy(TEST_EV_ACCESS_CONSENT_VAL);
        samlAuthzDecisionStatementAssertion.setInstanceAccessConsentPolicy(TEST_EV_INST_ACCESS_CONSENT_VAL);

        samlAuthzDecisionStatementAssertion.setConditions(samlAuthzDecisionStatementEvidenceConditions);
        samlAuthzDecisionStatementEvidenceConditions.setNotBefore(TEST_EV_COND_BEFORE_VAL);
        samlAuthzDecisionStatementEvidenceConditions.setNotOnOrAfter(TEST_EV_COND_AFTER_VAL);

        byte[] formRaw = EMPTY_STRING.getBytes();
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
                            AttributeValueType expectedAttrVal = new AttributeValueType();
                            if (attr.getDataType().equals(STRING_DATATYPE)) {
                                String expectedVal = (String) expectSubjAttr.get(attr.getAttributeId());
                                expectedAttrVal.getContent().add(expectedVal);
                            } else if (attr.getDataType().equals(ANY_URI_DATATYPE)) {
                                try {
                                    URI expectedVal = new URI((String) expectSubjAttr.get(attr.getAttributeId()));
                                    expectedAttrVal.getContent().add(expectedVal.toString());
                                } catch (URISyntaxException ex) {
                                    fail("Expected value of " + attr.getAttributeId() + " should be a valid URI form");
                                }
                            } else {
                                fail("Datatype: " + attr.getDataType() + " was not expected as a Subject");
                            }

                            String noAttrValMessage = "Request Subject Attribute " + attr.getAttributeId() + "has no AttributeValues";
                            assertNotNull(noAttrValMessage, attr.getAttributeValue());
                            assertFalse(noAttrValMessage, attr.getAttributeValue().isEmpty());
                            for (AttributeValueType actAttrVal : attr.getAttributeValue()) {
                                String valMessage = "Subject Attribute: " + attr.getAttributeId() + " has unexpected content: " + actAttrVal.getContent().get(0);
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
            "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-access-consent",
            "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-instance-access-consent"};
        String[] vals = {TEST_DECISION_VAL,
            TEST_RESOURCE_VAL,
            TEST_ACTION_VAL,
            TEST_EV_ID_VAL,
            TEST_EV_INSTANCE_VAL,
            TEST_EV_VERSION_VAL,
            TEST_EV_ISSUER_VAL,
            TEST_EV_COND_BEFORE_VAL,
            TEST_EV_COND_AFTER_VAL,
            TEST_EV_ACCESS_CONSENT_VAL,
            TEST_EV_INST_ACCESS_CONSENT_VAL};
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
                            verifyUniquePatientId(attr, TEST_PAT_ID_VAL);
                        } else {
                            String idMessage = "Resource Attribute: " + attr.getAttributeId() + " not found";
                            assertTrue(idMessage, expectResAttr.containsKey(attr.getAttributeId()));
                            AttributeValueType expectedAttrVal = new AttributeValueType();
                            if (expectResAttr.containsKey(attr.getAttributeId())) {
                                if (attr.getDataType().equals(STRING_DATATYPE)) {
                                    String expectedVal = (String) expectResAttr.get(attr.getAttributeId());
                                    expectedAttrVal.getContent().add(expectedVal);
                                } else if (attr.getDataType().equals(DATE_DATATYPE)) {
                                    try {
                                        //times must be in UTC format as specified by the XML Schema type (dateTime)
                                        DatatypeFactory xmlDateFactory = DatatypeFactory.newInstance();
                                        XMLGregorianCalendar xmlDate = xmlDateFactory.newXMLGregorianCalendar((String) expectResAttr.get(attr.getAttributeId()));
                                        expectedAttrVal.getContent().add(xmlDate.toXMLFormat());
                                    } catch (DatatypeConfigurationException ex) {
                                        fail("Expected value of " + attr.getAttributeId() + " should be a valid XML dateTime form");
                                    }
                                } else if (attr.getDataType().equals(ANY_URI_DATATYPE)) {
                                    try {
                                        URI expectedVal = new URI((String) expectResAttr.get(attr.getAttributeId()));
                                        expectedAttrVal.getContent().add(expectedVal.toString());
                                    } catch (URISyntaxException ex) {
                                        fail("Expected value of " + attr.getAttributeId() + " should be a valid URI form");
                                    }
                                } else {
                                    fail("Datatype: " + attr.getDataType() + " was not expected as a Resource");
                                }

                                String noAttrValMessage = "Request Resource Attribute " + attr.getAttributeId() + "has no AttributeValues";
                                assertNotNull(noAttrValMessage, attr.getAttributeValue());
                                assertFalse(noAttrValMessage, attr.getAttributeValue().isEmpty());
                                for (AttributeValueType actAttrVal : attr.getAttributeValue()) {
                                    String valMessage = "Resource Attribute: " + attr.getAttributeId() + " has unexpected content: " + actAttrVal.getContent().get(0);
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
    }

    private void verifyUniquePatientId(AttributeType attr, String expectedPatId) {

        if (expectedPatId != null && !expectedPatId.isEmpty()) {
            String patRoot = PatientIdFormatUtil.parsePatientId(expectedPatId);
            String patExt = PatientIdFormatUtil.parseCommunityId(expectedPatId);

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
                                            assertTrue(patExt.equals(attrNode.getNodeValue()));
                                        } else if (attrNode.getNodeName().equals("root")) {
                                            assertTrue(patRoot.equals(attrNode.getNodeValue()));
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
        } else {
            fail("Expected Patient Id is either null or empty");
        }
    }

    private void executeAssertionToXacmlSingleFieldTest(AssertionType assertion, String xacmlGroup, String xacmlAttributeId, Object expectedDestinationValue, String expectedDataType) {
        RequestType policyRequest = new RequestType();
        AssertionHelper assertionHelper = new AssertionHelper();
        assertionHelper.appendAssertionDataToRequest(policyRequest, assertion);

        if (xacmlGroup.toLowerCase().contentEquals("subject")) {
            verifyPolicySubjectField(policyRequest, xacmlAttributeId, expectedDestinationValue, expectedDataType);
        } else if (xacmlGroup.toLowerCase().contentEquals("resource")) {
            verifyPolicyResourceField(policyRequest, xacmlAttributeId, expectedDestinationValue, expectedDataType);
        } else {
            fail("unknown xacmlGroup " + xacmlGroup);
        }
    }

    private void verifyPolicySubjectField(RequestType policyRequest, String attributeID, Object expectedValue, String expectedDataType) {
        //in this particular assertion, it is assuming/asserting that there is a single subject.  If that changes test would need to be refactored
        assertNotNull(policyRequest.getSubject());
        assertFalse(policyRequest.getSubject().isEmpty());
        assertEquals(1, policyRequest.getSubject().size());
        SubjectType subject = policyRequest.getSubject().get(0);

        AttributeHelper attrHelper = new AttributeHelper();
        AttributeType attribute = attrHelper.getSingleAttribute(subject.getAttribute(), attributeID);
        verifyAttributeField(attribute, expectedValue, expectedDataType);
    }

    private void verifyPolicyResourceField(RequestType policyRequest, String attributeID, Object expectedValue, String expectedDataType) {
        //in this particular assertion, it is assuming/asserting that there is a single resource.  If that changes test would need to be refactored
        assertNotNull(policyRequest.getResource());
        assertFalse(policyRequest.getResource().isEmpty());
        assertEquals(1, policyRequest.getResource().size());
        ResourceType resource = policyRequest.getResource().get(0);

        AttributeHelper attrHelper = new AttributeHelper();
        AttributeType attribute = attrHelper.getSingleAttribute(resource.getAttribute(), attributeID);
        verifyAttributeField(attribute, expectedValue, expectedDataType);
    }

    private void verifyAttributeField(AttributeType attribute, Object expectedValue, String expectedDataType) {
        AttributeHelper attrHelper = new AttributeHelper();
        Object contentValue = attrHelper.getSingleContentValue(attribute);
        if (expectedValue != null) {
            String notExpected = "Expected: " + expectedValue + " but was null";
            assertNotNull(notExpected, attribute);
            String wrongDataTypeMsg = "Attribute: " + attribute.getAttributeId() +
                    " data type: " + attribute.getDataType() +
                    " was not as expected: " + expectedDataType;
            assertEquals(wrongDataTypeMsg, expectedDataType, attribute.getDataType());

            if (expectedDataType.equals(STRING_DATATYPE) ||
                    expectedDataType.equals(ANY_URI_DATATYPE) ||
                    expectedDataType.equals(DATE_DATATYPE)) {
                String wrongValueMsg = "Attribute: " + attribute.getAttributeId() +
                        " content: " + contentValue +
                        " was not as expected: " + expectedValue;
                assertEquals(wrongValueMsg, expectedValue, contentValue);
            } else if (expectedDataType.equals(II_DATATYPE)) {
                verifyUniquePatientId(attribute, (String) expectedValue);
            } else {
                fail("verifyAttributeField expectedDataType: " + expectedDataType.getClass() + " contentValue: " + contentValue.getClass());
            }
        } else {
            assertNull("Expected attribute to be null, but found one with value of " + contentValue + ".", attribute);
        }
    }

    @Test
    public void AssertionSamlAuthnStatementSessionIndexFromAssertionToXacml() {
        AssertionType assertion = createTestAssertion();
        String sourceValue = assertion.getSamlAuthnStatement().getSessionIndex();
        assertFalse(sourceValue.contentEquals(""));
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:gov:hhs:fha:nhinc:saml-authn-statement:session-index", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthnStatementSessionIndexFromAssertionToXacmlWithBlankValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getSamlAuthnStatement().setSessionIndex("");
        String sourceValue = assertion.getSamlAuthnStatement().getSessionIndex();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:gov:hhs:fha:nhinc:saml-authn-statement:session-index", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthnStatementSessionIndexFromAssertionToXacmlWithNullValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getSamlAuthnStatement().setSessionIndex(null);
        String sourceValue = assertion.getSamlAuthnStatement().getSessionIndex();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:gov:hhs:fha:nhinc:saml-authn-statement:session-index", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthnStatementAuthContextClassRefFromAssertionToXacml() {
        AssertionType assertion = createTestAssertion();
        String sourceValue = assertion.getSamlAuthnStatement().getAuthContextClassRef();
        assertFalse(sourceValue.contentEquals(""));
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:gov:hhs:fha:nhinc:saml-authn-statement:auth-context-class-ref", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthnStatementAuthContextClassRefFromAssertionToXacmlWithBlankValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getSamlAuthnStatement().setAuthContextClassRef("");
        String sourceValue = assertion.getSamlAuthnStatement().getAuthContextClassRef();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:gov:hhs:fha:nhinc:saml-authn-statement:auth-context-class-ref", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthnStatementAuthContextClassRefFromAssertionToXacmlWithNullValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getSamlAuthnStatement().setAuthContextClassRef(null);
        String sourceValue = assertion.getSamlAuthnStatement().getAuthContextClassRef();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:gov:hhs:fha:nhinc:saml-authn-statement:auth-context-class-ref", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthnStatementSubjectLocalityAddressFromAssertionToXacml() {
        AssertionType assertion = createTestAssertion();
        String sourceValue = assertion.getSamlAuthnStatement().getSubjectLocalityAddress();
        assertFalse(sourceValue.contentEquals(""));
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:oasis:names:tc:xacml:1.0:subject:authn-locality:ip-address", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthnStatementSubjectLocalityAddressFromAssertionToXacmlWithBlankValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getSamlAuthnStatement().setSubjectLocalityAddress("");
        String sourceValue = assertion.getSamlAuthnStatement().getSubjectLocalityAddress();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:oasis:names:tc:xacml:1.0:subject:authn-locality:ip-address", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthnStatementSubjectLocalityAddressFromAssertionToXacmlWithNullValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getSamlAuthnStatement().setSubjectLocalityAddress(null);
        String sourceValue = assertion.getSamlAuthnStatement().getSubjectLocalityAddress();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:oasis:names:tc:xacml:1.0:subject:authn-locality:ip-address", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthnStatementSubjectLocalityDNSNameFromAssertionToXacml() {
        AssertionType assertion = createTestAssertion();
        String sourceValue = assertion.getSamlAuthnStatement().getSubjectLocalityDNSName();
        assertFalse(sourceValue.contentEquals(""));
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:oasis:names:tc:xacml:1.0:subject:authn-locality:dns-name", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthnStatementSubjectLocalityDNSNameFromAssertionToXacmlWithBlankValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getSamlAuthnStatement().setSubjectLocalityDNSName("");
        String sourceValue = assertion.getSamlAuthnStatement().getSubjectLocalityDNSName();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:oasis:names:tc:xacml:1.0:subject:authn-locality:dns-name", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthnStatementSubjectLocalityDNSNameFromAssertionToXacmlWithNullValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getSamlAuthnStatement().setSubjectLocalityDNSName(null);
        String sourceValue = assertion.getSamlAuthnStatement().getSubjectLocalityDNSName();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:oasis:names:tc:xacml:1.0:subject:authn-locality:dns-name", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionUserInfoUserNameFromAssertionToXacml() {
        AssertionType assertion = createTestAssertion();
        String sourceValue = assertion.getUserInfo().getUserName();
        assertFalse(sourceValue.contentEquals(""));
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:oasis:names:tc:xacml:1.0:subject:subject-id", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionUserInfoUserNameFromAssertionToXacmlWithBlankValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getUserInfo().setUserName("");
        String sourceValue = assertion.getUserInfo().getUserName();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:oasis:names:tc:xacml:1.0:subject:subject-id", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionUserInfoUserNameFromAssertionToXacmlWithNullValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getUserInfo().setUserName(null);
        String sourceValue = assertion.getUserInfo().getUserName();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:oasis:names:tc:xacml:1.0:subject:subject-id", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionUserInfoOrgHomeCommunityIdFromAssertionToXacml() {
        AssertionType assertion = createTestAssertion();
        String sourceValue = assertion.getUserInfo().getOrg().getHomeCommunityId();
        assertFalse(sourceValue.contentEquals(""));
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:oasis:names:tc:xspa:1.0:subject:organization-id", sourceValue, ANY_URI_DATATYPE);
    }

    @Test
    public void AssertionUserInfoOrgHomeCommunityIdFromAssertionToXacmlWithBlankValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getUserInfo().getOrg().setHomeCommunityId("");
        String sourceValue = assertion.getUserInfo().getOrg().getHomeCommunityId();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:oasis:names:tc:xspa:1.0:subject:organization-id", sourceValue, ANY_URI_DATATYPE);
    }

    @Test
    public void AssertionUserInfoOrgHomeCommunityIdFromAssertionToXacmlWithNullValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getUserInfo().getOrg().setHomeCommunityId(null);
        String sourceValue = assertion.getUserInfo().getOrg().getHomeCommunityId();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:oasis:names:tc:xspa:1.0:subject:organization-id", sourceValue, ANY_URI_DATATYPE);
    }

    @Test
    public void AssertionHomeCommunityHomeCommunityIdFromAssertionToXacml() {
        AssertionType assertion = createTestAssertion();
        String sourceValue = assertion.getHomeCommunity().getHomeCommunityId();
        assertFalse(sourceValue.contentEquals(""));
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "http://www.hhs.gov/healthit/nhin#HomeCommunityId", sourceValue, ANY_URI_DATATYPE);
    }

    @Test
    public void AssertionHomeCommunityHomeCommunityIdFromAssertionToXacmlWithBlankValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getHomeCommunity().setHomeCommunityId("");
        String sourceValue = assertion.getHomeCommunity().getHomeCommunityId();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "http://www.hhs.gov/healthit/nhin#HomeCommunityId", sourceValue, ANY_URI_DATATYPE);
    }

    @Test
    public void AssertionHomeCommunityHomeCommunityIdFromAssertionToXacmlWithNullValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getHomeCommunity().setHomeCommunityId(null);
        String sourceValue = assertion.getHomeCommunity().getHomeCommunityId();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "http://www.hhs.gov/healthit/nhin#HomeCommunityId", sourceValue, ANY_URI_DATATYPE);
    }

    @Ignore //MOve to integration test suite commented out by Sri
    @Test
    public void AssertionUniquePatientIdFromAssertionToXacml() {
        AssertionType assertion = createTestAssertion();
        String sourceValue = assertion.getUniquePatientId().get(0);
        assertFalse(sourceValue.contentEquals(""));
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "http://www.hhs.gov/healthit/nhin#subject-id", sourceValue, II_DATATYPE);
    }

    @Test
    public void AssertionUniquePatientIdFromAssertionToXacmlWithBlankValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getUniquePatientId().set(0, "");
        String sourceValue = null;
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "http://www.hhs.gov/healthit/nhin#subject-id", sourceValue, II_DATATYPE);
    }

    @Test
    public void AssertionUniquePatientIdFromAssertionToXacmlWithNullValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getUniquePatientId().set(0, null);
        String sourceValue = assertion.getUniquePatientId().get(0);
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "http://www.hhs.gov/healthit/nhin#subject-id", sourceValue, II_DATATYPE);
    }

    @Test
    public void AssertionUserInfoOrgNameFromAssertionToXacml() {
        AssertionType assertion = createTestAssertion();
        String sourceValue = assertion.getUserInfo().getOrg().getName();
        assertFalse(sourceValue.contentEquals(""));
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:gov:hhs:fha:nhinc:user-organization-name", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionUserInfoOrgNameFromAssertionToXacmlWithBlankValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getUserInfo().getOrg().setName("");
        String sourceValue = assertion.getUserInfo().getOrg().getName();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:gov:hhs:fha:nhinc:user-organization-name", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionUserInfoOrgNameFromAssertionToXacmlWithNullValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getUserInfo().getOrg().setName(null);
        String sourceValue = assertion.getUserInfo().getOrg().getName();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:gov:hhs:fha:nhinc:user-organization-name", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionUserInfoRoleCodedCodeFromAssertionToXacml() {
        AssertionType assertion = createTestAssertion();
        String sourceValue = assertion.getUserInfo().getRoleCoded().getCode();
        assertFalse(sourceValue.contentEquals(""));
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:oasis:names:tc:xacml:2.0:subject:role", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionUserInfoRoleCodedCodeFromAssertionToXacmlWithBlankValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getUserInfo().getRoleCoded().setCode("");
        String sourceValue = assertion.getUserInfo().getRoleCoded().getCode();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:oasis:names:tc:xacml:2.0:subject:role", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionUserInfoRoleCodedCodeFromAssertionToXacmlWithNullValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getUserInfo().getRoleCoded().setCode(null);
        String sourceValue = assertion.getUserInfo().getRoleCoded().getCode();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:oasis:names:tc:xacml:2.0:subject:role", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionUserInfoRoleCodedCodeSystemFromAssertionToXacml() {
        AssertionType assertion = createTestAssertion();
        String sourceValue = assertion.getUserInfo().getRoleCoded().getCodeSystem();
        assertFalse(sourceValue.contentEquals(""));
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:gov:hhs:fha:nhinc:user-role-code-system", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionUserInfoRoleCodedCodeSystemFromAssertionToXacmlWithBlankValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getUserInfo().getRoleCoded().setCodeSystem("");
        String sourceValue = assertion.getUserInfo().getRoleCoded().getCodeSystem();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:gov:hhs:fha:nhinc:user-role-code-system", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionUserInfoRoleCodedCodeSystemFromAssertionToXacmlWithNullValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getUserInfo().getRoleCoded().setCodeSystem(null);
        String sourceValue = assertion.getUserInfo().getRoleCoded().getCodeSystem();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:gov:hhs:fha:nhinc:user-role-code-system", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionUserInfoRoleCodedCodeSystemNameFromAssertionToXacml() {
        AssertionType assertion = createTestAssertion();
        String sourceValue = assertion.getUserInfo().getRoleCoded().getCodeSystemName();
        assertFalse(sourceValue.contentEquals(""));
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:gov:hhs:fha:nhinc:user-role-code-system-name", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionUserInfoRoleCodedCodeSystemNameFromAssertionToXacmlWithBlankValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getUserInfo().getRoleCoded().setCodeSystemName("");
        String sourceValue = assertion.getUserInfo().getRoleCoded().getCodeSystemName();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:gov:hhs:fha:nhinc:user-role-code-system-name", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionUserInfoRoleCodedCodeSystemNameFromAssertionToXacmlWithNullValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getUserInfo().getRoleCoded().setCodeSystemName(null);
        String sourceValue = assertion.getUserInfo().getRoleCoded().getCodeSystemName();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:gov:hhs:fha:nhinc:user-role-code-system-name", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionUserInfoRoleCodedDisplayNameFromAssertionToXacml() {
        AssertionType assertion = createTestAssertion();
        String sourceValue = assertion.getUserInfo().getRoleCoded().getDisplayName();
        assertFalse(sourceValue.contentEquals(""));
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:gov:hhs:fha:nhinc:user-role-description", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionUserInfoRoleCodedDisplayNameFromAssertionToXacmlWithBlankValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getUserInfo().getRoleCoded().setDisplayName("");
        String sourceValue = assertion.getUserInfo().getRoleCoded().getDisplayName();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:gov:hhs:fha:nhinc:user-role-description", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionUserInfoRoleCodedDisplayNameFromAssertionToXacmlWithNullValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getUserInfo().getRoleCoded().setDisplayName(null);
        String sourceValue = assertion.getUserInfo().getRoleCoded().getDisplayName();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:gov:hhs:fha:nhinc:user-role-description", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionPurposeOfDisclosureCodeFromAssertionToXacml() {
        AssertionType assertion = createTestAssertion();
        String sourceValue = assertion.getPurposeOfDisclosureCoded().getCode();
        assertFalse(sourceValue.contentEquals(""));
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:oasis:names:tc:xspa:1.0:subject:purposeofuse", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionPurposeOfDisclosureCodeFromAssertionToXacmlWithBlankValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getPurposeOfDisclosureCoded().setCode("");
        String sourceValue = assertion.getPurposeOfDisclosureCoded().getCode();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:oasis:names:tc:xspa:1.0:subject:purposeofuse", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionPurposeOfDisclosureCodeFromAssertionToXacmlWithNullValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getPurposeOfDisclosureCoded().setCode(null);
        String sourceValue = assertion.getPurposeOfDisclosureCoded().getCode();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:oasis:names:tc:xspa:1.0:subject:purposeofuse", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionPurposeOfDisclosureCodeSystemFromAssertionToXacml() {
        AssertionType assertion = createTestAssertion();
        String sourceValue = assertion.getPurposeOfDisclosureCoded().getCodeSystem();
        assertFalse(sourceValue.contentEquals(""));
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:gov:hhs:fha:nhinc:purpose-of-use-code-system", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionPurposeOfDisclosureCodeSystemFromAssertionToXacmlWithBlankValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getPurposeOfDisclosureCoded().setCodeSystem("");
        String sourceValue = assertion.getPurposeOfDisclosureCoded().getCodeSystem();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:gov:hhs:fha:nhinc:purpose-of-use-code-system", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionPurposeOfDisclosureCodeSystemFromAssertionToXacmlWithNullValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getPurposeOfDisclosureCoded().setCodeSystem(null);
        String sourceValue = assertion.getPurposeOfDisclosureCoded().getCodeSystem();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:gov:hhs:fha:nhinc:purpose-of-use-code-system", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionPurposeOfDisclosureCodeSystemNameFromAssertionToXacml() {
        AssertionType assertion = createTestAssertion();
        String sourceValue = assertion.getPurposeOfDisclosureCoded().getCodeSystemName();
        assertFalse(sourceValue.contentEquals(""));
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:gov:hhs:fha:nhinc:purpose-of-use-code-system-name", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionPurposeOfDisclosureCodeSystemNameFromAssertionToXacmlWithBlankValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getPurposeOfDisclosureCoded().setCodeSystemName("");
        String sourceValue = assertion.getPurposeOfDisclosureCoded().getCodeSystemName();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:gov:hhs:fha:nhinc:purpose-of-use-code-system-name", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionPurposeOfDisclosureCodeSystemNameFromAssertionToXacmlWithNullValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getPurposeOfDisclosureCoded().setCodeSystemName(null);
        String sourceValue = assertion.getPurposeOfDisclosureCoded().getCodeSystemName();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:gov:hhs:fha:nhinc:purpose-of-use-code-system-name", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionPurposeOfDisclosureDisplayNameFromAssertionToXacml() {
        AssertionType assertion = createTestAssertion();
        String sourceValue = assertion.getPurposeOfDisclosureCoded().getDisplayName();
        assertFalse(sourceValue.contentEquals(""));
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:gov:hhs:fha:nhinc:purpose-of-use-display-name", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionPurposeOfDisclosureDisplayNameFromAssertionToXacmlWithBlankValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getPurposeOfDisclosureCoded().setDisplayName("");
        String sourceValue = assertion.getPurposeOfDisclosureCoded().getDisplayName();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:gov:hhs:fha:nhinc:purpose-of-use-display-name", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionPurposeOfDisclosureDisplayNameFromAssertionToXacmlWithNullValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getPurposeOfDisclosureCoded().setDisplayName(null);
        String sourceValue = assertion.getPurposeOfDisclosureCoded().getDisplayName();
        executeAssertionToXacmlSingleFieldTest(assertion, "Subject", "urn:gov:hhs:fha:nhinc:purpose-of-use-display-name", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthzDecisionStatementDecisionFromAssertionToXacml() {
        AssertionType assertion = createTestAssertion();
        String sourceValue = assertion.getSamlAuthzDecisionStatement().getDecision();
        assertFalse(sourceValue.contentEquals(""));
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-decision", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthzDecisionStatementDecisionFromAssertionToXacmlWithBlankValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getSamlAuthzDecisionStatement().setDecision("");
        String sourceValue = assertion.getSamlAuthzDecisionStatement().getDecision();
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-decision", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthzDecisionStatementDecisionFromAssertionToXacmlWithNullValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getSamlAuthzDecisionStatement().setDecision(null);
        String sourceValue = assertion.getSamlAuthzDecisionStatement().getDecision();
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-decision", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthzDecisionStatementResourceFromAssertionToXacml() {
        AssertionType assertion = createTestAssertion();
        String sourceValue = assertion.getSamlAuthzDecisionStatement().getResource();
        assertFalse(sourceValue.contentEquals(""));
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-resource", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthzDecisionStatementResourceFromAssertionToXacmlWithBlankValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getSamlAuthzDecisionStatement().setResource("");
        String sourceValue = assertion.getSamlAuthzDecisionStatement().getResource();
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-resource", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthzDecisionStatementResourceFromAssertionToXacmlWithNullValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getSamlAuthzDecisionStatement().setResource(null);
        String sourceValue = assertion.getSamlAuthzDecisionStatement().getResource();
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-resource", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthzDecisionStatementActionFromAssertionToXacml() {
        AssertionType assertion = createTestAssertion();
        String sourceValue = assertion.getSamlAuthzDecisionStatement().getAction();
        assertFalse(sourceValue.contentEquals(""));
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-action", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthzDecisionStatementActionFromAssertionToXacmlWithBlankValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getSamlAuthzDecisionStatement().setAction("");
        String sourceValue = assertion.getSamlAuthzDecisionStatement().getAction();
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-action", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthzDecisionStatementActionFromAssertionToXacmlWithNullValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getSamlAuthzDecisionStatement().setAction(null);
        String sourceValue = assertion.getSamlAuthzDecisionStatement().getAction();
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-action", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthzDecisionStatementEvidenceAssertionIdFromAssertionToXacml() {
        AssertionType assertion = createTestAssertion();
        String sourceValue = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getId();
        assertFalse(sourceValue.contentEquals(""));
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-id", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthzDecisionStatementEvidenceAssertionIdFromAssertionToXacmlWithBlankValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().setId("");
        String sourceValue = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getId();
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-id", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthzDecisionStatementEvidenceAssertionIdFromAssertionToXacmlWithNullValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().setId(null);
        String sourceValue = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getId();
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-id", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthzDecisionStatementEvidenceAssertionIssueInstantFromAssertionToXacml() {
        AssertionType assertion = createTestAssertion();
        String sourceValue = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssueInstant();
        assertFalse(sourceValue.contentEquals(""));
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-issue-instant", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthzDecisionStatementEvidenceAssertionIssueInstantFromAssertionToXacmlWithBlankValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().setIssueInstant("");
        String sourceValue = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssueInstant();
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-issue-instant", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthzDecisionStatementEvidenceAssertionIssueInstantFromAssertionToXacmlWithNullValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().setIssueInstant(null);
        String sourceValue = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssueInstant();
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-issue-instant", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthzDecisionStatementEvidenceAssertionVersionFromAssertionToXacml() {
        AssertionType assertion = createTestAssertion();
        String sourceValue = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getVersion();
        assertFalse(sourceValue.contentEquals(""));
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-version", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthzDecisionStatementEvidenceAssertionVersionFromAssertionToXacmlWithBlankValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().setVersion("");
        String sourceValue = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getVersion();
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-version", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthzDecisionStatementEvidenceAssertionVersionFromAssertionToXacmlWithNullValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().setVersion(null);
        String sourceValue = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getVersion();
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-version", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthzDecisionStatementEvidenceAssertionIssuerFromAssertionToXacml() {
        AssertionType assertion = createTestAssertion();
        String sourceValue = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssuer();
        assertFalse(sourceValue.contentEquals(""));
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-issuer", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthzDecisionStatementEvidenceAssertionIssuerFromAssertionToXacmlWithBlankValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().setIssuer("");
        String sourceValue = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssuer();
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-issuer", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthzDecisionStatementEvidenceAssertionIssuerFromAssertionToXacmlWithNullValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().setIssuer(null);
        String sourceValue = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssuer();
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-issuer", sourceValue, STRING_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthzDecisionStatementEvidenceAssertionConditionsNotBeforeFromAssertionToXacml() {
        AssertionType assertion = createTestAssertion();
        String sourceValue = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().getNotBefore();
        assertFalse(sourceValue.contentEquals(""));
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-not-before", sourceValue, DATE_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthzDecisionStatementEvidenceAssertionConditionsNotBeforeFromAssertionToXacmlWithBlankValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().setNotBefore("");
        String sourceValue = null;
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-not-before", sourceValue, DATE_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthzDecisionStatementEvidenceAssertionConditionsNotBeforeFromAssertionToXacmlWithNullValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().setNotBefore(null);
        String sourceValue = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().getNotBefore();
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-not-before", sourceValue, DATE_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthzDecisionStatementEvidenceAssertionConditionsNotOnOrAfterFromAssertionToXacml() {
        AssertionType assertion = createTestAssertion();
        String sourceValue = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().getNotOnOrAfter();
        assertFalse(sourceValue.contentEquals(""));
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-not-on-or-after", sourceValue, DATE_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthzDecisionStatementEvidenceAssertionConditionsNotOnOrAfterFromAssertionToXacmlWithBlankValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().setNotOnOrAfter("");
        String sourceValue = null;
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-not-on-or-after", sourceValue, DATE_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthzDecisionStatementEvidenceAssertionConditionsNotOnOrAfterFromAssertionToXacmlWithNullValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().setNotOnOrAfter(null);
        String sourceValue = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().getNotOnOrAfter();
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-not-on-or-after", sourceValue, DATE_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthzDecisionStatementEvidenceAssertionAccessConsentFromAssertionToXacml() {
        AssertionType assertion = createTestAssertion();
        String sourceValue = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getAccessConsentPolicy();
        assertFalse(sourceValue.contentEquals(""));
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-access-consent", sourceValue, ANY_URI_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthzDecisionStatementEvidenceAssertionAccessConsentFromAssertionToXacmlWithBlankValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().setAccessConsentPolicy("");
        String sourceValue = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getAccessConsentPolicy();
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-access-consent", sourceValue, ANY_URI_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthzDecisionStatementEvidenceAssertionAccessConsentFromAssertionToXacmlWithNullValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().setAccessConsentPolicy(null);
        String sourceValue = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getAccessConsentPolicy();
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-access-consent", sourceValue, ANY_URI_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthzDecisionStatementEvidenceAssertionInstanceAccessConsentFromAssertionToXacml() {
        AssertionType assertion = createTestAssertion();
        String sourceValue = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getInstanceAccessConsentPolicy();
        assertFalse(sourceValue.contentEquals(""));
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-instance-access-consent", sourceValue, ANY_URI_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthzDecisionStatementEvidenceAssertionInstanceAccessConsentFromAssertionToXacmlWithBlankValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().setInstanceAccessConsentPolicy("");
        String sourceValue = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getInstanceAccessConsentPolicy();
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-instance-access-consent", sourceValue, ANY_URI_DATATYPE);
    }

    @Test
    public void AssertionSamlAuthzDecisionStatementEvidenceAssertionInstanceAccessConsentFromAssertionToXacmlWithNullValue() {
        AssertionType assertion = createTestAssertion();
        assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().setInstanceAccessConsentPolicy(null);
        String sourceValue = assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getInstanceAccessConsentPolicy();
        executeAssertionToXacmlSingleFieldTest(assertion, "Resource", "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-instance-access-consent", sourceValue, ANY_URI_DATATYPE);
    }
}
