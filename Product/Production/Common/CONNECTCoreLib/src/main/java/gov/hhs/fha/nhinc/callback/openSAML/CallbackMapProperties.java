/**
 *
 */
package gov.hhs.fha.nhinc.callback.openSAML;

import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * @author bhumphrey
 *
 */
public class CallbackMapProperties implements CallbackProperties {

	private final Map<Object, Object> map = new HashMap<Object, Object>();
	private static final DateTimeFormatter XML_DATE_TIME_FORMAT = ISODateTimeFormat.dateTimeParser();

	/**
	 * Puts the properties into the callback map.
	 * @param properties
	 */
	public CallbackMapProperties(Map<?, Object> properties) {
		map.putAll(properties);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getAssertionIssuerFormat()
	 */
	@Override
	public String getAssertionIssuerFormat() {
		return getNullSafeString(SamlConstants.ASSERTION_ISSUER_FORMAT_PROP);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getIssuer()
	 */
	@Override
	public String getIssuer() {
		return getNullSafeString(SamlConstants.ASSERTION_ISSUER_PROP);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getUsername()
	 */
	@Override
	public String getUsername() {
		return getNullSafeString(SamlConstants.USER_NAME_PROP);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getAuthenicationContextClass()
	 */
	@Override
	public String getAuthenticationContextClass() {
		return getNullSafeString(SamlConstants.AUTHN_CONTEXT_CLASS_PROP);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getAuthenicationSessionIndex()
	 */
	@Override
	public String getAuthenticationSessionIndex() {
		return getNullSafeString(SamlConstants.AUTHN_SESSION_INDEX_PROP);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getAuthenicationInstant()
	 */
	@Override
	public DateTime getAuthenticationInstant() {
		return getNullSafeDateTime(SamlConstants.AUTHN_INSTANT_PROP, null);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getSubjectLocality()
	 */
	@Override
	public String getSubjectLocality() {
		return getNullSafeString(SamlConstants.SUBJECT_LOCALITY_ADDR_PROP);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getSubjectDNS()
	 */
	@Override
	public String getSubjectDNS() {
		return getNullSafeString(SamlConstants.SUBJECT_LOCALITY_DNS_PROP);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getAuthenicationStatementExists()
	 */
	@Override
	public Boolean getAuthenicationStatementExists() {
		return getNullSafeBoolean(SamlConstants.AUTHZ_STATEMENT_EXISTS_PROP,
				Boolean.FALSE);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getAuthnicationResource()
	 */
	@Override
	public String getAuthnicationResource() {
		return getNullSafeString(SamlConstants.RESOURCE_PROP);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getAuthenicationDecision()
	 */
	@Override
	public String getAuthenicationDecision() {
		return getNullSafeString(SamlConstants.AUTHZ_DECISION_PROP);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getEvidenceID()
	 */
	@Override
	public String getEvidenceID() {
		return getNullSafeString(SamlConstants.EVIDENCE_ID_PROP);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getEvidenceInstant()
	 */
	@Override
	public DateTime getEvidenceInstant() {
		return getNullSafeDateTime(SamlConstants.EVIDENCE_INSTANT_PROP, null);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getEvidenceIssuerFormat()
	 */
	@Override
	public String getEvidenceIssuerFormat() {
		return getNullSafeString(SamlConstants.EVIDENCE_ISSUER_FORMAT_PROP);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getEvidenceIssuer()
	 */
	@Override
	public String getEvidenceIssuer() {
		return getNullSafeString(SamlConstants.EVIDENCE_ISSUER_PROP);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getEvidenceConditionNotBefore()
	 */
	@Override
	public DateTime getEvidenceConditionNotBefore() {
		return getNullSafeDateTime(
				SamlConstants.EVIDENCE_CONDITION_NOT_BEFORE_PROP, null);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getEvidenceConditionNotAfter()
	 */
	@Override
	public DateTime getEvidenceConditionNotAfter() {
		return getNullSafeDateTime(
				SamlConstants.EVIDENCE_CONDITION_NOT_AFTER_PROP, null);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getEvidenceAccessConstent()
	 */
	@Override
	public List<Object> getEvidenceAccessConstent() {
		return getNullSafeList(SamlConstants.EVIDENCE_ACCESS_CONSENT_PROP);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getEvidenceInstanctAccessConsent()
	 */
	@Override
	public List<Object> getEvidenceInstantAccessConsent() {
		return getNullSafeList(SamlConstants.EVIDENCE_INST_ACCESS_CONSENT_PROP);
	}

	/* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getEvidenceSubject()
     */
    @Override
    public String getEvidenceSubject() {
        return getNullSafeString(SamlConstants.EVIDENCE_SUBJECT_PROP);
    }

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getUserCode()
	 */
	@Override
	public String getUserCode() {
		return getNullSafeString(SamlConstants.USER_CODE_PROP);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getUserSystem()
	 */
	@Override
	public String getUserSystem() {
		return getNullSafeString(SamlConstants.USER_SYST_PROP);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getUserSystemName()
	 */
	@Override
	public String getUserSystemName() {
		return getNullSafeString(SamlConstants.USER_SYST_NAME_PROP);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getUserDisplay()
	 */
	@Override
	public String getUserDisplay() {
		return getNullSafeString(SamlConstants.USER_DISPLAY_PROP);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getPurposeCode()
	 */
	@Override
	public String getPurposeCode() {
		return getNullSafeString(SamlConstants.PURPOSE_CODE_PROP);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getPurposeSystem()
	 */
	@Override
	public String getPurposeSystem() {
		return getNullSafeString(SamlConstants.PURPOSE_SYST_PROP);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getPurposeSystemName()
	 */
	@Override
	public String getPurposeSystemName() {
		return getNullSafeString(SamlConstants.PURPOSE_SYST_NAME_PROP);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getPurposeDisplay()
	 */
	@Override
	public String getPurposeDisplay() {
		return getNullSafeString(SamlConstants.PURPOSE_DISPLAY_PROP);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getUserOrganization()
	 */
	@Override
	public String getUserOrganization() {
		return getNullSafeString(SamlConstants.USER_ORG_PROP);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getHomeCommunity()
	 */
	@Override
	public String getHomeCommunity() {
		return getNullSafeString(SamlConstants.HOME_COM_PROP);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getPatientID()
	 */
	@Override
	public String getPatientID() {
		return getNullSafeString(SamlConstants.PATIENT_ID_PROP);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getUserFullName()
	 */
	@Override
	public String getUserFullName() {
		StringBuffer nameConstruct = new StringBuffer();

		String firstName = getNullSafeString(SamlConstants.USER_FIRST_PROP);
		if (firstName != null) {
			nameConstruct.append(firstName);
		}

		String middleName = getNullSafeString(SamlConstants.USER_MIDDLE_PROP);
		if (middleName != null) {
			if (nameConstruct.length() > 0) {
				nameConstruct.append(" ");
			}
			nameConstruct.append(middleName);
		}

		String lastName = getNullSafeString(SamlConstants.USER_LAST_PROP);
		if (lastName != null) {
			if (nameConstruct.length() > 0) {
				nameConstruct.append(" ");
			}
			nameConstruct.append(lastName);
		}
		return nameConstruct.toString();
	}
	
    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getTargetHomeCommunityId()
     */
    @Override
    public String getTargetHomeCommunityId() {
        return getNullSafeString(NhincConstants.WS_SOAP_TARGET_HOME_COMMUNITY_ID);
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getAction()
     */
    @Override
    public String getAction() {
        return getNullSafeString(NhincConstants.SERVICE_NAME);
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties#getTargetApiLevel()
     */
    @Override
    public GATEWAY_API_LEVEL getTargetApiLevel() {
        return (GATEWAY_API_LEVEL) getNullSafeObject(NhincConstants.TARGET_API_LEVEL, null);
    }

	private Boolean getNullSafeBoolean(final String property,
			Boolean defaultValue) {
		Boolean value = defaultValue;
		if (map.containsKey(property) && map.get(property) != null) {
			value = Boolean.valueOf(map.get(property).toString());
		}
		return value;
	}

	private DateTime getNullSafeDateTime(final String property,
			DateTime defaultValue) {
		String dateTimeTxt = getNullSafeString(property);

		DateTime dateTime = defaultValue;
		if (dateTimeTxt != null) {
			dateTime = XML_DATE_TIME_FORMAT.parseDateTime(dateTimeTxt);
		}
		return dateTime;

	}

	private String getNullSafeString(final String property) {
		return getNullSafeString(property, null);
	}

	private String getNullSafeString(final String property, String defaultValue) {
		String value = defaultValue;
		if (map.containsKey(property) && map.get(property) != null) {
			value = map.get(property).toString();
		}
		return value;
	}

	private List<Object> getNullSafeList(final String property) {
		List<Object> list = null;
		if (map.containsKey(property) && map.get(property) != null) {
			Object value = map.get(property);
			if (value instanceof List<?>) {
				list = (List<Object>) value;
			} else {
				list = new ArrayList<Object>();
				list.add(value);
			}
		}

		return list;
	}

    private Object getNullSafeObject(final String property, Object defaultValue) {
        Object value = defaultValue;
        if (map.containsKey(property) && map.get(property) != null) {
            value = map.get(property);
        }
        return value;
    }

}
