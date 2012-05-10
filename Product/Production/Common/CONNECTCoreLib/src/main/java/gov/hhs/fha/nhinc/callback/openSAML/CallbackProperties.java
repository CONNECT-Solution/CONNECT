/**
 * 
 */
package gov.hhs.fha.nhinc.callback.openSAML;

import java.util.List;

import org.joda.time.DateTime;

/**
 * @author bhumphrey
 *
 */
public interface CallbackProperties {

	public abstract String getAssertionIssuerFormat();

	public abstract String getIssuer();

	public abstract String getUsername();

	public abstract String getAuthenicationContextClass();

	public abstract String getAuthenicationSessionIndex();

	public abstract DateTime getAuthenicationInstant();

	public abstract String getSubjectLocality();

	public abstract String getSubjectDNS();

	public abstract Boolean getAuthenicationStatementExists();

	public abstract String getAuthnicationResource();

	public abstract String getAuthenicationDecision();

	public abstract String getEvidenceID();

	public abstract DateTime getEvidenceInstant();

	public abstract String getEvidenceIssuerFormat();

	public abstract String getEvidenceIssuer();

	public abstract DateTime getEvidenceConditionNotBefore();

	public abstract DateTime getEvidenceConditionNotAfter();

	public abstract List getEvidenceAccessConstent();

	public abstract List getEvidenceInstantAccessConsent();

	public abstract String getUserCode();

	public abstract String getUserSystem();

	public abstract String getUserSystemName();

	public abstract String getUserDisplay();

	public abstract String getPurposeCode();

	public abstract String getPurposeSystem();

	public abstract String getPurposeSystemName();

	public abstract String getPurposeDisplay();

	public abstract String getUserOrganization();

	public abstract String getHomeCommunity();

	public abstract String getPatientID();

	public abstract String getUserFullName();

}