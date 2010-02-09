/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.policy;

/**
 *
 * @author rayj
 */
public class XacmlAttributeId {

    public static final String AuthnStatementAuthnInstant = "urn:gov:hhs:fha:nhinc:saml-authn-statement:auth-instant";
    public static final String AuthnStatementSessionIndex = "urn:gov:hhs:fha:nhinc:saml-authn-statement:session-index";
    public static final String AuthnStatementAthnContextClassRef = "urn:gov:hhs:fha:nhinc:saml-authn-statement:auth-context-class-ref";
    public static final String AuthnStatementSubjectLocalityAddress = "urn:oasis:names:tc:xacml:1.0:subject:authn-locality:ip-address";
    public static final String AuthnStatementDNSName = "urn:oasis:names:tc:xacml:1.0:subject:authn-locality:dns-name";
    public static final String UserPersonName = "urn:oasis:names:tc:xacml:1.0:subject:subject-id";
    public static final String UserOrganizationName = "urn:gov:hhs:fha:nhinc:user-organization-name";
    public static final String UserOrganizationId = "urn:oasis:names:tc:xspa:1.0:subject:organization-id";
    public static final String HomeCommunityName = "http://www.hhs.gov/healthit/nhin#HomeCommunityId";
    public static final String UniquePatientId = "http://www.hhs.gov/healthit/nhin#subject-id";
    public static final String UserRoleCode = "urn:oasis:names:tc:xacml:2.0:subject:role";
    public static final String UserRoleCodeSystem = "urn:gov:hhs:fha:nhinc:user-role-code-system";
    public static final String UserRoleCodeSystemName = "urn:gov:hhs:fha:nhinc:user-role-code-system-name";
    public static final String UserRoleCodeDiplayName = "urn:gov:hhs:fha:nhinc:user-role-description";
    public static final String PurposeForUseCode = "urn:oasis:names:tc:xspa:1.0:subject:purposeofuse";
    public static final String PurposeForUseCodeSystem = "urn:gov:hhs:fha:nhinc:purpose-of-use-code-system";
    public static final String PurposeForUseCodeSystemName = "urn:gov:hhs:fha:nhinc:purpose-of-use-code-system-name";
    public static final String PurposeForUseCodeDisplayName = "urn:gov:hhs:fha:nhinc:purpose-of-use-display-name";
    public static final String AuthzDecisionStatementDecision = "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-decision";
    public static final String AuthzDecisionStatementResource = "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-resource";
    public static final String AuthzDecisionStatementAction = "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-action";
    public static final String AuthzDecisionStatementEvidenceAssertionID = "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-id";
    public static final String AuthzDecisionStatementEvidenceAssertionIssueInstant = "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-issue-instant";
    public static final String AuthzDecisionStatementEvidenceAssertionVersion = "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-version";
    public static final String AuthzDecisionStatementEvidenceAssertionIssuer = "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-issuer";
    public static final String AuthzDecisionStatementEvidenceAssertionConditionsNotBefore = "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-not-before";
    public static final String AuthzDecisionStatementEvidenceAssertionConditionsNotOnOrAfter = "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-not-on-or-after";
    public static final String AuthzDecisionStatementEvidenceAssertionAccessConsentPolicy = "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-access-consent";
    public static final String AuthzDecisionStatementEvidenceAssertionInstanceAccessConsentPolicy = "urn:gov:hhs:fha:nhinc:saml-authz-decision-statement-evidence-assertion-instance-access-consent";
    public static final String SignatureKeyModulus = "urn:gov:hhs:fha:nhinc:saml-signature-rsa-key-value-modulus";
    public static final String SignatureKeyExponent = "urn:gov:hhs:fha:nhinc:saml-signature-rsa-key-value-exponent";
    public static final String SignatureValue = "urn:gov:hhs:fha:nhinc:saml-signature-value";
    
}
