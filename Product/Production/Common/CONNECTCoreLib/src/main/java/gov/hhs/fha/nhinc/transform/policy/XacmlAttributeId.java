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
    public static final String PurposeOfUseCode = "urn:oasis:names:tc:xspa:1.0:subject:purposeofuse";
    public static final String PurposeOfUseCodeSystem = "urn:gov:hhs:fha:nhinc:purpose-of-use-code-system";
    public static final String PurposeOfUseCodeSystemName = "urn:gov:hhs:fha:nhinc:purpose-of-use-code-system-name";
    public static final String PurposeOfUseCodeDisplayName = "urn:gov:hhs:fha:nhinc:purpose-of-use-display-name";
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
    public static final String ATTRIBUTE_ACCESS_CONSENT_POLICY = "urn:gov:hhs:fha:nhinc:saml-attribute-access-consent";
    public static final String ATTRIBUTE_INSTANCE_ACCESS_CONSENT_POLICY = "urn:gov:hhs:fha:nhinc:saml-attribute-instance-access-consent";
    

}
