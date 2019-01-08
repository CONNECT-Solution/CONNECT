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
package gov.hhs.fha.nhinc.callback.opensaml;

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
     * @return The Username
     */
    public Boolean getAuthenticationStatementExists();

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
     * @return Saml Conditions Not Before
     */
    public DateTime getSamlConditionsNotBefore();

    /**
     * @return Saml Conditions Not After
     */
    public DateTime getSamlConditionsNotAfter();

    /**
     * @return Authorization Statement Exists
     */
    public Boolean getAuthorizationStatementExists();

    /**
     * @return Authorization Resource
     */
    public String getAuthorizationResource();

    /**
     * @return Authorization Decision
     */
    public String getAuthorizationDecision();

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

    /**
     * @return
     */
    public String getServiceName();

    /**
     * @return
     */
    public String getNPI();

    /**
     *
     * @return
     */
    public String getAcpAttribute();

    /**
     *
     * @return
     */
    public String getIacpAttribute();

    /**
     * @return
     */
    String getUserOrganizationId();

    /**
     *
     * @return List of subject confirmation
     */
    List<SAMLSubjectConfirmation> getSubjectConfirmations();

    /**
     * @return the requested Signature Algorithm
     */
    public String getSignatureAlgorithm();

    /**
     * @return The requested Digest Algorithm
     */
    public String getDigestAlgorithm();

}
