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
package gov.hhs.fha.nhinc.messaging.builder.impl;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.messaging.builder.AssertionBuilder;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tjafri
 */
public abstract class AbstractAssertionBuilder implements AssertionBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractAssertionBuilder.class);

    private static final String PROPERTY_FILE_NAME = "assertioninfo";
    private static final String PROPERTY_KEY_PURPOSE_CODE = "PurposeOfUseRoleCode";
    private static final String PROPERTY_KEY_PURPOSE_SYSTEM = "PurposeOfUseCodeSystem";
    private static final String PROPERTY_KEY_PURPOSE_SYSTEM_NAME = "PurposeOfUseCodeSystemName";
    private static final String PROPERTY_KEY_PURPOSE_DISPLAY = "PurposeOfUseDisplayName";
    private static final String PROPERTY_KEY_USER_FIRST = "UserFirstName";
    private static final String PROPERTY_KEY_USER_MIDDLE = "UserMiddleName";
    private static final String PROPERTY_KEY_USER_LAST = "UserLastName";
    private static final String PROPERTY_KEY_USER_NAME = "UserName";
    private static final String PROPERTY_KEY_USER_ORG = "UserOrganization";
    private static final String PROPERTY_KEY_USER_CODE = "UserRoleCode";
    private static final String PROPERTY_KEY_USER_SYSTEM = "UserRoleCodeSystem";
    private static final String PROPERTY_KEY_USER_SYSTEM_NAME = "UserRoleCodeSystemName";
    private static final String PROPERTY_KEY_USER_DISPLAY = "UserRoleDisplayName";
    private static final String PROPERTY_KEY_ACCESS_CONSENT = "AccessPolicyConsent";
    private static final String PROPERTY_KEY_SAML_AUTH_INSTANT = "SamlAuthInstant";
    private static final String PROPERTY_KEY_SAML_AUTH_CLASS = "AuthContextClassRef";

    protected AssertionType assertionType = null;
    protected String purposeCode = null;
    protected String purposeCodeSystem = null;
    protected String purposeCodeSystemName = null;
    protected String purposeDisplayName = null;
    protected String userFirstName = null;
    protected String userMiddleName = null;
    protected String userLastName = null;
    protected String userName = null;
    protected String userOrganization = null;
    protected String userCode = null;
    protected String userSystem = null;
    protected String userSystemName = null;
    protected String userDisplay = null;
    protected String expiration = null;
    protected String signDate = null;
    protected String accessConsentPolicy = null;
    protected String samlAuthInstant = null;
    protected String samlAuthClass = null;
    protected String homeCommunityId = null;

    @Override
    public void build() {
        readAssertionFromPropertyFile();
    }

    public void setSamlAuthInstant(String samlAuthInstant) {
        this.samlAuthInstant = samlAuthInstant;
    }

    public void setSamlAuthClass(String samlAuthClass) {
        this.samlAuthClass = samlAuthClass;
    }

    public void setAssertionType(AssertionType assertionType) {
        this.assertionType = assertionType;
    }

    public void setPurposeCode(String purposeCode) {
        this.purposeCode = purposeCode;
    }

    public void setPurposeCodeSystem(String purposeCodeSystem) {
        this.purposeCodeSystem = purposeCodeSystem;
    }

    public void setPurposeCodeSystemName(String purposeCodeSystemName) {
        this.purposeCodeSystemName = purposeCodeSystemName;
    }

    public void setPurposeDisplayName(String purposeDisplayName) {
        this.purposeDisplayName = purposeDisplayName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public void setUserMiddleName(String userMiddleName) {
        this.userMiddleName = userMiddleName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserOrganization(String userOrganization) {
        this.userOrganization = userOrganization;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public void setUserSystem(String userSystem) {
        this.userSystem = userSystem;
    }

    public void setUserSystemName(String userSystemName) {
        this.userSystemName = userSystemName;
    }

    public void setUserDisplay(String userDisplay) {
        this.userDisplay = userDisplay;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }

    public void setAccessConsentPolicy(String accessConsentPolicy) {
        this.accessConsentPolicy = accessConsentPolicy;
    }

    public String getHomeCommunityId() {
        return homeCommunityId;
    }

    public void setHomeCommunityId(String homeCommunityId) {
        this.homeCommunityId = homeCommunityId;
    }

    private void readAssertionFromPropertyFile() {
        try {
            PropertyAccessor propertyAccessor = PropertyAccessor.getInstance();

            setUserFirstName(propertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_FIRST));
            setUserLastName(propertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_LAST));
            setUserMiddleName(propertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_MIDDLE));
            setUserOrganization(propertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_ORG));
            setUserName(propertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_NAME));
            setUserCode(propertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_CODE));
            setUserSystem(propertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_SYSTEM));
            setUserSystemName(propertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_SYSTEM_NAME));
            setUserDisplay(propertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_DISPLAY));

            setPurposeCode(propertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_PURPOSE_CODE));
            setPurposeCodeSystem(propertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_PURPOSE_SYSTEM));
            setPurposeCodeSystemName(
                    propertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_PURPOSE_SYSTEM_NAME));
            setPurposeDisplayName(propertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_PURPOSE_DISPLAY));
            setAccessConsentPolicy(propertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_ACCESS_CONSENT));
            setSamlAuthInstant(propertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_SAML_AUTH_INSTANT));
            setSamlAuthClass(propertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_SAML_AUTH_CLASS));
            setHomeCommunityId(propertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY));
            
        } catch (PropertyAccessException ex) {
            LOG.error("AdminGUI can not access assertioninfo property file: {}", ex.getLocalizedMessage(), ex);
        }
    }
}
