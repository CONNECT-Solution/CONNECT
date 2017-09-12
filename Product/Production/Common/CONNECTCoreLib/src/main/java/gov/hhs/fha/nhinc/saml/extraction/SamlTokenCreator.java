/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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

import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlConditionsType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlSubjectsType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.wss4j.common.saml.bean.SubjectConfirmationDataBean;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.opensaml.saml.saml2.core.SubjectConfirmation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jon Hoppesch
 */
public class SamlTokenCreator {

    private static final Logger LOG = LoggerFactory.getLogger(SamlTokenCreator.class);

    /**
     * This method will populate a Map with information from the assertion that is used within the SAML Token. This Map
     * can be used to set up the requestContext prior to sending a message on the Nhin.
     *
     * @param assertion The assertion object that contains information required by the SAML Token.
     * @param url The URL to the destination service.
     * @param action The specified Action for this message.
     * @return A Map containing all of the information needed for creation of the SAML Token.
     */
    public Map<String, Object> createRequestContext(AssertionType assertion, String url, String action) {
        LOG.debug("Entering SamlTokenCreator.CreateRequestContext... version 1234");

        Map<String, Object> requestContext = new HashMap<>();

        if (assertion != null) {
            String NPI = assertion.getNationalProviderId();
            if (NullChecker.isNotNullish(NPI)) {
                requestContext.put(NhincConstants.ATTRIBUTE_NAME_NPI, NPI);
            }
            extractSubjectConfirmation(requestContext, assertion);
            UserType userInfo = assertion.getUserInfo();
            if (userInfo != null) {
                if (NullChecker.isNotNullish(userInfo.getUserName())) {
                    requestContext.put(SamlConstants.USER_NAME_PROP, userInfo.getUserName());
                }
                HomeCommunityType org = userInfo.getOrg();
                if (org != null) {
                    String name = org.getName();
                    if (NullChecker.isNotNullish(name)) {
                        requestContext.put(SamlConstants.USER_ORG_PROP, name);
                    }
                    if (NullChecker.isNotNullish(org.getHomeCommunityId())) {
                        requestContext.put(SamlConstants.USER_ORG_ID_PROP, org.getHomeCommunityId());
                    }
                } else {
                    LOG.error("Error: samlSendOperation input assertion user org is null");
                }
                if (userInfo.getRoleCoded() != null) {
                    if (NullChecker.isNotNullish(userInfo.getRoleCoded().getCode())) {
                        requestContext.put(SamlConstants.USER_CODE_PROP, userInfo.getRoleCoded().getCode());
                    }
                    requestContext.put(SamlConstants.USER_SYST_PROP, SamlConstants.USER_SYST_ATTR);
                    requestContext.put(SamlConstants.USER_SYST_NAME_PROP, SamlConstants.USER_SYST_NAME_ATTR);
                    if (NullChecker.isNotNullish(userInfo.getRoleCoded().getDisplayName())) {
                        requestContext.put(SamlConstants.USER_DISPLAY_PROP, userInfo.getRoleCoded().getDisplayName());
                    }
                } else {
                    LOG.error("Error: samlSendOperation input assertion user info role is null");
                }
                if (userInfo.getPersonName() != null) {
                    if (NullChecker.isNotNullish(userInfo.getPersonName().getGivenName())) {
                        requestContext.put(SamlConstants.USER_FIRST_PROP, userInfo.getPersonName().getGivenName());
                    }
                    if (NullChecker.isNotNullish(userInfo.getPersonName().getSecondNameOrInitials())) {
                        requestContext.put(SamlConstants.USER_MIDDLE_PROP,
                            userInfo.getPersonName().getSecondNameOrInitials());
                    }
                    if (NullChecker.isNotNullish(userInfo.getPersonName().getFamilyName())) {
                        requestContext.put(SamlConstants.USER_LAST_PROP, userInfo.getPersonName().getFamilyName());
                    }
                } else {
                    LOG.error("Error: samlSendOperation input assertion user person name is null");
                }
            } else {
                LOG.error("Error: samlSendOperation input assertion user info is null");
            }

            if (assertion.getPurposeOfDisclosureCoded() != null) {
                if (NullChecker.isNotNullish(assertion.getPurposeOfDisclosureCoded().getCode())) {
                    requestContext.put(SamlConstants.PURPOSE_CODE_PROP,
                        assertion.getPurposeOfDisclosureCoded().getCode());
                }

                requestContext.put(SamlConstants.PURPOSE_SYST_PROP, SamlConstants.PURPOSE_SYSTEM_ATTR);
                requestContext.put(SamlConstants.PURPOSE_SYST_NAME_PROP, SamlConstants.PURPOSE_SYSTEMNAME_ATTR);
                if (NullChecker.isNotNullish(assertion.getPurposeOfDisclosureCoded().getDisplayName())) {
                    requestContext.put(SamlConstants.PURPOSE_DISPLAY_PROP,
                        assertion.getPurposeOfDisclosureCoded().getDisplayName());
                }
            } else {
                LOG.error("Error: samlSendOperation input assertion purpose coded is null");
            }
            if (assertion.getHomeCommunity() != null) {
                if (NullChecker.isNotNullish(assertion.getHomeCommunity().getHomeCommunityId())) {
                    requestContext.put(SamlConstants.HOME_COM_PROP, assertion.getHomeCommunity().getHomeCommunityId());
                }

            } else {
                LOG.error("Error: samlSendOperation input assertion Home Community is null");
            }
            if (CollectionUtils.isNotEmpty(assertion.getUniquePatientId())) {
                // take first non-null item in the List as the identifier
                for (String patId : assertion.getUniquePatientId()) {
                    if (NullChecker.isNotNullish(patId)) {
                        requestContext.put(SamlConstants.PATIENT_ID_PROP, patId);
                        break;
                    }
                }
            }

            if (assertion.getSamlIssuer() != null) {
                if (NullChecker.isNotNullish(assertion.getSamlIssuer().getIssuer())) {
                    requestContext.put(SamlConstants.ASSERTION_ISSUER_PROP, assertion.getSamlIssuer().getIssuer());
                }
                if (NullChecker.isNotNullish(assertion.getSamlIssuer().getIssuerFormat())) {
                    requestContext.put(SamlConstants.ASSERTION_ISSUER_FORMAT_PROP,
                        assertion.getSamlIssuer().getIssuerFormat());
                }
            }

            if (assertion.getSamlConditions() != null) {
                if (NullChecker.isNotNullish(assertion.getSamlConditions().getNotBefore())) {
                    requestContext.put(SamlConstants.SAMLCONDITIONS_NOT_BEFORE_PROP,
                        assertion.getSamlConditions().getNotBefore());
                }
                if (NullChecker.isNotNullish(assertion.getSamlConditions().getNotOnOrAfter())) {
                    requestContext.put(SamlConstants.SAMLCONDITIONS_NOT_AFTER_PROP,
                        assertion.getSamlConditions().getNotOnOrAfter());
                }
            }

            if (assertion.getSamlAuthnStatement() != null) {
                if (NullChecker.isNotNullish(assertion.getSamlAuthnStatement().getAuthInstant())) {
                    requestContext.put(SamlConstants.AUTHN_INSTANT_PROP,
                        assertion.getSamlAuthnStatement().getAuthInstant());
                }
                if (NullChecker.isNotNullish(assertion.getSamlAuthnStatement().getSessionIndex())) {
                    requestContext.put(SamlConstants.AUTHN_SESSION_INDEX_PROP,
                        assertion.getSamlAuthnStatement().getSessionIndex());
                }
                if (NullChecker.isNotNullish(assertion.getSamlAuthnStatement().getAuthContextClassRef())) {
                    requestContext.put(SamlConstants.AUTHN_CONTEXT_CLASS_PROP,
                        assertion.getSamlAuthnStatement().getAuthContextClassRef());
                }
                if (NullChecker.isNotNullish(assertion.getSamlAuthnStatement().getSubjectLocalityAddress())) {
                    requestContext.put(SamlConstants.SUBJECT_LOCALITY_ADDR_PROP,
                        assertion.getSamlAuthnStatement().getSubjectLocalityAddress());
                }
                if (NullChecker.isNotNullish(assertion.getSamlAuthnStatement().getSubjectLocalityDNSName())) {
                    requestContext.put(SamlConstants.SUBJECT_LOCALITY_DNS_PROP,
                        assertion.getSamlAuthnStatement().getSubjectLocalityDNSName());
                }
            } else {
                LOG.error("Error: samlSendOperation input assertion AuthnStatement is null");
            }
            if (assertion.getSamlAuthzDecisionStatement() != null) {
                requestContext.put(SamlConstants.AUTHZ_STATEMENT_EXISTS_PROP, "true");
                if (NullChecker.isNotNullish(assertion.getSamlAuthzDecisionStatement().getAction())) {
                    requestContext.put(SamlConstants.ACTION_PROP,
                        assertion.getSamlAuthzDecisionStatement().getAction());
                }
                if (NullChecker.isNotNullish(assertion.getSamlAuthzDecisionStatement().getDecision())) {
                    requestContext.put(SamlConstants.AUTHZ_DECISION_PROP,
                        assertion.getSamlAuthzDecisionStatement().getDecision());
                }
                if (assertion.getSamlAuthzDecisionStatement().getEvidence() != null
                    && assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion() != null) {
                    if (NullChecker
                        .isNotNullish(assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getId())) {
                        requestContext.put(SamlConstants.EVIDENCE_ID_PROP,
                            assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getId());
                    }
                    if (NullChecker.isNotNullish(
                        assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssueInstant())) {
                        requestContext.put(SamlConstants.EVIDENCE_INSTANT_PROP,
                            assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssueInstant());
                    }
                    if (NullChecker.isNotNullish(
                        assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getVersion())) {
                        requestContext.put(SamlConstants.EVIDENCE_VERSION_PROP,
                            assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getVersion());
                    }
                    if (NullChecker.isNotNullish(
                        assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssuer())) {
                        requestContext.put(SamlConstants.EVIDENCE_ISSUER_PROP,
                            assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssuer());
                    }
                    if (NullChecker.isNotNullish(
                        assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssuerFormat())) {
                        requestContext.put(SamlConstants.EVIDENCE_ISSUER_FORMAT_PROP,
                            assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssuerFormat());
                    }
                    if (!assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getAccessConsentPolicy()
                        .isEmpty()) {
                        requestContext.put(SamlConstants.EVIDENCE_ACCESS_CONSENT_PROP, assertion
                            .getSamlAuthzDecisionStatement().getEvidence().getAssertion().getAccessConsentPolicy());
                    }
                    if (!assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion()
                        .getInstanceAccessConsentPolicy().isEmpty()) {
                        requestContext.put(SamlConstants.EVIDENCE_INST_ACCESS_CONSENT_PROP,
                            assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion()
                            .getInstanceAccessConsentPolicy());
                    }

                    if (NullChecker.isNotNullish(
                        assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getSubject())) {
                        requestContext.put(SamlConstants.EVIDENCE_SUBJECT_PROP,
                            assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getSubject());
                    }

                    if (assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion()
                        .getConditions() != null) {
                        if (NullChecker.isNotNullish(assertion.getSamlAuthzDecisionStatement().getEvidence()
                            .getAssertion().getConditions().getNotBefore())) {
                            requestContext.put(SamlConstants.EVIDENCE_CONDITION_NOT_BEFORE_PROP,
                                assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions()
                                .getNotBefore());
                        }
                        if (NullChecker.isNotNullish(assertion.getSamlAuthzDecisionStatement().getEvidence()
                            .getAssertion().getConditions().getNotOnOrAfter())) {
                            requestContext.put(SamlConstants.EVIDENCE_CONDITION_NOT_AFTER_PROP,
                                assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions()
                                .getNotOnOrAfter());
                        }
                    }
                } else {
                    LOG.error("Error: samlSendOperation input assertion AuthzDecisionStatement Evidence is null");
                }
            } else {
                requestContext.put(SamlConstants.AUTHZ_STATEMENT_EXISTS_PROP, "false");
                LOG.info("AuthzDecisionStatement is null.  It will not be part of the SAML Assertion");
            }

            if (assertion.getSamlIssuer() != null) {
                if (NullChecker.isNotNullish(assertion.getSamlIssuer().getIssuer())) {
                    requestContext.put(SamlConstants.ASSERTION_ISSUER_PROP, assertion.getSamlIssuer().getIssuer());
                }
                if (NullChecker.isNotNullish(assertion.getSamlIssuer().getIssuerFormat())) {
                    requestContext.put(SamlConstants.ASSERTION_ISSUER_FORMAT_PROP,
                        assertion.getSamlIssuer().getIssuerFormat());
                }
            } else {
                LOG.debug("samlSendOperation input assertion Saml Issuer is null");
            }
        } else {
            LOG.error("Error: samlSendOperation input assertion is null");
        }

        // This will be overwrite any value that is available in
        // assertion.getSamlAuthzDecisionStatement().getResource()
        requestContext.put(SamlConstants.RESOURCE_PROP, url);

        // This will be overwrite any value that is available in
        // assertion.getSamlAuthzDecisionStatement().getAction()
        if (NullChecker.isNotNullish(action)) {
            requestContext.put(SamlConstants.ACTION_PROP, action);
        }

        if (LOG.isTraceEnabled()) {
            LOG.trace("Request Context:");
            Set<Entry<String, Object>> allKeys = requestContext.entrySet();
            for (Entry<String, Object> keyValue : allKeys) {
                LOG.trace(keyValue.getKey() + " = " + keyValue.getValue());
            }
        }

        LOG.debug("Exiting SamlTokenCreator.CreateRequestContext...");
        return requestContext;

    }

    private void extractSubjectConfirmation(Map<String, Object> requestContext, AssertionType assertion) {
        List<SubjectConfirmationDataBean> senderBeans = new ArrayList<>();
        List<SubjectConfirmationDataBean> bearBeans = new ArrayList<>();
        DateTimeFormatter dateFormat = ISODateTimeFormat.dateTimeParser();
        for (SamlSubjectsType samlSubject : assertion.getSamlSubjects()){
            String method = samlSubject.getMethod();
            SubjectConfirmationDataBean bean = new SubjectConfirmationDataBean();
            bean.setAddress(samlSubject.getAddress());
            bean.setInResponseTo(samlSubject.getInResponseTo());
            bean.setRecipient(samlSubject.getRecipient());
            SamlConditionsType subjectCondition = samlSubject.getSubjectCondition();
            if (subjectCondition != null) {
                bean.setNotAfter(dateFormat.parseDateTime(subjectCondition.getNotOnOrAfter()));
                bean.setNotBefore(dateFormat.parseDateTime(subjectCondition.getNotBefore()));
            }
            if (SubjectConfirmation.METHOD_SENDER_VOUCHES.equalsIgnoreCase(method)){
                //create sender voches
                senderBeans.add(bean);
            }
            if (SubjectConfirmation.METHOD_BEARER.equalsIgnoreCase(method)){
                //create bear voches
                bearBeans.add(bean);
            }
        }
        requestContext.put(SamlConstants.SUBJECT_SENDER_VOUCHES, senderBeans);
        requestContext.put(SamlConstants.SUBJECT_BEARER, bearBeans);
    }

}
