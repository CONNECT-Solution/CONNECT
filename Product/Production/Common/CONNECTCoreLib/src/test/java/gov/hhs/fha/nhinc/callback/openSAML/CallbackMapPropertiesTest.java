package gov.hhs.fha.nhinc.callback.openSAML;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class CallbackMapPropertiesTest {

	private Map<String, Object> properties;
	private final String TEST_VALUE = "test saml value";
	private final String FIRST_NAME = "Jim";
	private final String MIDDLE_NAME = "Bob";
	private final String LAST_NAME = "Johnson";
	private final String FULL_NAME = "Jim Bob Johnson";
	private final String TARGET_HCID = "1.1";
	private final String ACTION = "Nhin Action";
	private final String SERVICE = "Patient Discovery";
	private final String TEST_DATE = "2013-01-01T01:01:01.000";
	private List<String> testList = new ArrayList<String>();

	private CallbackMapProperties callbackProperties;

	@Before
	public void setUp() {
		testList.add(TEST_VALUE);
		properties = new HashMap<String, Object>();

		properties.put(SamlConstants.ASSERTION_ISSUER_FORMAT_PROP, TEST_VALUE);
		properties.put(SamlConstants.ASSERTION_ISSUER_PROP, TEST_VALUE);
		properties.put(SamlConstants.USER_NAME_PROP, TEST_VALUE);
		properties.put(SamlConstants.AUTHN_CONTEXT_CLASS_PROP, TEST_VALUE);
		properties.put(SamlConstants.AUTHN_SESSION_INDEX_PROP, TEST_VALUE);
		properties.put(SamlConstants.AUTHN_INSTANT_PROP, TEST_DATE);
		properties.put(SamlConstants.SUBJECT_LOCALITY_ADDR_PROP, TEST_VALUE);
		properties.put(SamlConstants.SUBJECT_LOCALITY_DNS_PROP, TEST_VALUE);
		properties.put(SamlConstants.AUTHZ_STATEMENT_EXISTS_PROP, "true");
		properties.put(SamlConstants.RESOURCE_PROP, TEST_VALUE);
		properties.put(SamlConstants.AUTHZ_DECISION_PROP, TEST_VALUE);
		properties.put(SamlConstants.EVIDENCE_ID_PROP, TEST_VALUE);
		properties.put(SamlConstants.EVIDENCE_INSTANT_PROP, TEST_DATE);
		properties.put(SamlConstants.EVIDENCE_ISSUER_FORMAT_PROP, TEST_VALUE);
		properties.put(SamlConstants.EVIDENCE_ISSUER_PROP, TEST_VALUE);
		properties.put(SamlConstants.EVIDENCE_CONDITION_NOT_BEFORE_PROP,
				TEST_DATE);
		properties.put(SamlConstants.EVIDENCE_CONDITION_NOT_AFTER_PROP,
				TEST_DATE);
		properties.put(SamlConstants.EVIDENCE_ACCESS_CONSENT_PROP, testList);
		properties.put(SamlConstants.EVIDENCE_INST_ACCESS_CONSENT_PROP,
				testList);
		properties.put(SamlConstants.EVIDENCE_SUBJECT_PROP, TEST_VALUE);
		properties.put(SamlConstants.USER_CODE_PROP, TEST_VALUE);
		properties.put(SamlConstants.USER_SYST_PROP, TEST_VALUE);
		properties.put(SamlConstants.USER_SYST_NAME_PROP, TEST_VALUE);
		properties.put(SamlConstants.USER_DISPLAY_PROP, TEST_VALUE);
		properties.put(SamlConstants.PURPOSE_CODE_PROP, TEST_VALUE);
		properties.put(SamlConstants.PURPOSE_SYST_PROP, TEST_VALUE);
		properties.put(SamlConstants.PURPOSE_SYST_NAME_PROP, TEST_VALUE);
		properties.put(SamlConstants.PURPOSE_DISPLAY_PROP, TEST_VALUE);
		properties.put(SamlConstants.USER_ORG_PROP, TEST_VALUE);
		properties.put(SamlConstants.HOME_COM_PROP, TEST_VALUE);
		properties.put(SamlConstants.PATIENT_ID_PROP, TEST_VALUE);
		properties.put(SamlConstants.USER_FIRST_PROP, FIRST_NAME);
		properties.put(SamlConstants.USER_MIDDLE_PROP, MIDDLE_NAME);
		properties.put(SamlConstants.USER_LAST_PROP, LAST_NAME);
		properties.put(NhincConstants.WS_SOAP_TARGET_HOME_COMMUNITY_ID,
				TARGET_HCID);
		properties.put(NhincConstants.ACTION_PROP, ACTION);
		properties.put(NhincConstants.SERVICE_NAME, SERVICE);
		properties.put(NhincConstants.TARGET_API_LEVEL,
				GATEWAY_API_LEVEL.LEVEL_g1);

		callbackProperties = new CallbackMapProperties(properties);
	}

	@Test
	public void testCallbackMapPropertiesGetters() {
		assertEquals(callbackProperties.getAssertionIssuerFormat(), TEST_VALUE);
		assertEquals(callbackProperties.getAuthenicationDecision(), TEST_VALUE);
		assertTrue(callbackProperties.getAuthenicationStatementExists());
		assertEquals(callbackProperties.getAuthenticationContextClass(),
				TEST_VALUE);
		assertEquals(callbackProperties.getAuthenticationSessionIndex(),
				TEST_VALUE);
		assertEquals(callbackProperties.getAuthnicationResource(), TEST_VALUE);
		assertEquals(callbackProperties.getEvidenceID(), TEST_VALUE);
		assertEquals(callbackProperties.getEvidenceIssuer(), TEST_VALUE);
		assertEquals(callbackProperties.getEvidenceIssuerFormat(), TEST_VALUE);
		assertEquals(callbackProperties.getEvidenceSubject(), TEST_VALUE);
		assertEquals(callbackProperties.getHomeCommunity(), TEST_VALUE);
		assertEquals(callbackProperties.getIssuer(), TEST_VALUE);
		assertEquals(callbackProperties.getPatientID(), TEST_VALUE);
		assertEquals(callbackProperties.getPurposeCode(), TEST_VALUE);
		assertEquals(callbackProperties.getPurposeDisplay(), TEST_VALUE);
		assertEquals(callbackProperties.getPurposeSystem(), TEST_VALUE);
		assertEquals(callbackProperties.getPurposeSystemName(), TEST_VALUE);
		assertEquals(callbackProperties.getServiceName(), SERVICE);
		assertEquals(callbackProperties.getSubjectDNS(), TEST_VALUE);
		assertEquals(callbackProperties.getSubjectLocality(), TEST_VALUE);
		assertEquals(callbackProperties.getTargetApiLevel(),
				GATEWAY_API_LEVEL.LEVEL_g1);
		assertEquals(callbackProperties.getTargetHomeCommunityId(), TARGET_HCID);
		assertEquals(callbackProperties.getUserCode(), TEST_VALUE);
		assertEquals(callbackProperties.getUserDisplay(), TEST_VALUE);
		assertEquals(callbackProperties.getUserFullName(), FULL_NAME);
		assertEquals(callbackProperties.getUsername(), TEST_VALUE);
		assertEquals(callbackProperties.getUserOrganization(), TEST_VALUE);
		assertEquals(callbackProperties.getUserSystem(), TEST_VALUE);
		assertEquals(callbackProperties.getUserSystemName(), TEST_VALUE);
		assertEquals(callbackProperties.getAction(), ACTION);

		List<Object> evidenceAccess = callbackProperties
				.getEvidenceAccessConstent();
		assertEquals((String) evidenceAccess.get(0), TEST_VALUE);

		List<Object> evidenceInstAccess = callbackProperties
				.getEvidenceInstantAccessConsent();
		assertEquals((String) evidenceInstAccess.get(0), TEST_VALUE);

		DateTime authnInstant = callbackProperties.getAuthenticationInstant();
		assertTrue(StringUtils.lowerCase(authnInstant.toString()).contains(
				StringUtils.lowerCase(TEST_DATE)));

		DateTime evidenceInstant = callbackProperties.getEvidenceInstant();
		assertTrue(StringUtils.lowerCase(evidenceInstant.toString()).contains(
				StringUtils.lowerCase(TEST_DATE)));

		DateTime evidenceConditionNotBefore = callbackProperties
				.getEvidenceConditionNotBefore();
		assertTrue(StringUtils.lowerCase(evidenceConditionNotBefore.toString())
				.contains(StringUtils.lowerCase(TEST_DATE)));

		DateTime evidenceConditionNotAfter = callbackProperties
				.getEvidenceConditionNotAfter();
		assertTrue(StringUtils.lowerCase(evidenceConditionNotAfter.toString())
				.contains(StringUtils.lowerCase(TEST_DATE)));
	}

}
