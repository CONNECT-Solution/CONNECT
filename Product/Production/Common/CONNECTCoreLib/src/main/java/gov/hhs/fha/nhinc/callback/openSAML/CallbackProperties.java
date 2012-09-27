/**
 * 
 */
package gov.hhs.fha.nhinc.callback.openSAML;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;

import java.util.List;

import org.joda.time.DateTime;

/**
 * @author bhumphrey
 * 
 */
public interface CallbackProperties {

    /**
     * @return AssertionIssuerFormat
     */
    public String getAssertionIssuerFormat();

    /**
     * @return The Issuer
     */
    public String getIssuer();

    /**
     * @return The Username
     */
    public String getUsername();

    /**
     * @return Authentication Context Class
     */
    public String getAuthenticationContextClass();

    /**
     * @return Authentication Session Index
     */
    public String getAuthenticationSessionIndex();

    /**
     * @return Authentication Instant
     */
    public DateTime getAuthenticationInstant();

    /**
     * @return Subject Locality
     */
    public String getSubjectLocality();

    /**
     * @return Subject DNS
     */
    public String getSubjectDNS();

    /**
     * @return Authenication Statement Exists
     */
    public Boolean getAuthenicationStatementExists();

    /**
     * @return Authnication Resource
     */
    public String getAuthnicationResource();

    /**
     * @return Authenication Decision
     */
    public String getAuthenicationDecision();

    /**
     * @return Evidence ID
     */ 
    public String getEvidenceID();

    /**
     * @return Evidence Instant
     */
    public DateTime getEvidenceInstant();

    /**
     * @return Evidence Issuer Format
     */
    public String getEvidenceIssuerFormat();

    /**
     * @return Evidence Issuer
     */
    public String getEvidenceIssuer();

    /**
     * @return Evidence Condition Not Before
     */
    public DateTime getEvidenceConditionNotBefore();

    /**
     * @return Evidence Condition Not After
     */
    public DateTime getEvidenceConditionNotAfter();

    /**
     * @return Evidence Access Constent
     */
    public List<Object> getEvidenceAccessConstent();

    /**
     * @return Evidence Instant Access Consent
     */
    public List<Object> getEvidenceInstantAccessConsent();

    /**
     * @return Evidence Subject
     */
    public String getEvidenceSubject();

    /**
     * @return User Code
     */
    public String getUserCode();

    /**
     * @return User System
     */
    public String getUserSystem();

    /**
     * @return User System Name
     */
    public String getUserSystemName();

    /**
     * @return User Display
     */
    public String getUserDisplay();

    /**
     * @return Purpose Code
     */
    public String getPurposeCode();

    /**
     * @return Purpose System
     */
    public String getPurposeSystem();

    /**
     * @return Purpose System Name
     */
    public String getPurposeSystemName();

    /**
     * @return Purpose Display
     */
    public String getPurposeDisplay();

    /**
     * @return User Organization
     */
    public String getUserOrganization();

    /**
     * @return Home Community
     */
    public String getHomeCommunity();

    /**
     * @return Patient ID
     */
    public String getPatientID();

    /**
     * @return user's full name
     */
    public String getUserFullName();

    /**
     * @return Target Home Community ID (HCID)
     */
    public String getTargetHomeCommunityId();

    /**
     * @return Service Name
     */
    public String getAction();

    /**
     * @return Target API Level
     */
    public GATEWAY_API_LEVEL getTargetApiLevel();

}