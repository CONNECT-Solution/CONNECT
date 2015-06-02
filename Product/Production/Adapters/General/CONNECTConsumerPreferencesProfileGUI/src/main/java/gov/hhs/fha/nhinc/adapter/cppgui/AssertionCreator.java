/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.adapter.cppgui;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

import org.apache.log4j.Logger;

/**
 * This class creates a generic assertion class. Some services need this class to proceed.
 *
 * @author westberg
 */
public class AssertionCreator {

    private static final String PROPERTY_FILE_NAME = "universalClient";
    private static final String PROPERTY_KEY_PURPOSE_CODE = "AssertionPurposeCode";
    private static final String PROPERTY_KEY_PURPOSE_SYSTEM = "AssertionPurposeSystem";
    private static final String PROPERTY_KEY_PURPOSE_SYSTEM_NAME = "AssertionPurposeSystemName";
    private static final String PROPERTY_KEY_PURPOSE_DISPLAY = "AssertionPurposeDisplay";
    private static final String PROPERTY_KEY_USER_FIRST = "AssertionUserFirstName";
    private static final String PROPERTY_KEY_USER_MIDDLE = "AssertionUserMiddleName";
    private static final String PROPERTY_KEY_USER_LAST = "AssertionUserLastName";
    private static final String PROPERTY_KEY_USER_NAME = "AssertionUserName";
    private static final String PROPERTY_KEY_USER_ORG = "AssertionUserOrganization";
    private static final String PROPERTY_KEY_USER_CODE = "AssertionUserCode";
    private static final String PROPERTY_KEY_USER_SYSTEM = "AssertionUserSystem";
    private static final String PROPERTY_KEY_USER_SYSTEM_NAME = "AssertionUserSystemName";
    private static final String PROPERTY_KEY_USER_DISPLAY = "AssertionUserDisplay";
    private static final String PROPERTY_KEY_EXPIRE = "AssertionExpiration";
    private static final String PROPERTY_KEY_SIGN = "AssertionSignDate";
    private static final String PROPERTY_KEY_ACCESS_CONSENT = "AssertionAccessConsent";

    private static final Logger LOG = Logger.getLogger(AssertionCreator.class);

    public AssertionType createAssertion() {

        AssertionType assertOut = new AssertionType();
        CeType purposeCoded = new CeType();
        UserType user = new UserType();
        PersonNameType userPerson = new PersonNameType();
        CeType userRole = new CeType();
        HomeCommunityType userHc = new HomeCommunityType();
        user.setPersonName(userPerson);
        user.setOrg(userHc);
        user.setRoleCoded(userRole);
        assertOut.setUserInfo(user);
        assertOut.setPurposeOfDisclosureCoded(purposeCoded);

        try {
            PropertyAccessor propertyAccessor = PropertyAccessor.getInstance();

            userPerson.setGivenName(propertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_FIRST));
            userPerson.setFamilyName(propertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_LAST));
            userPerson.setSecondNameOrInitials(propertyAccessor.getProperty(PROPERTY_FILE_NAME,
                    PROPERTY_KEY_USER_MIDDLE));
            userHc.setName(propertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_ORG));
            user.setUserName(propertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_NAME));
            userRole.setCode(propertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_CODE));
            userRole.setCodeSystem(propertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_SYSTEM));
            userRole.setCodeSystemName(propertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_SYSTEM_NAME));
            userRole.setDisplayName(propertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_DISPLAY));

            purposeCoded.setCode(propertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_PURPOSE_CODE));
            purposeCoded.setCodeSystem(propertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_PURPOSE_SYSTEM));
            purposeCoded.setCodeSystemName(propertyAccessor.getProperty(PROPERTY_FILE_NAME,
                    PROPERTY_KEY_PURPOSE_SYSTEM_NAME));
            purposeCoded.setDisplayName(propertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_PURPOSE_DISPLAY));

            // assertOut.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().setNotBefore(PropertyAccessor.getProperty(PROPERTY_FILE_NAME,
            // PROPERTY_KEY_SIGN));
            // assertOut.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().setNotOnOrAfter(PropertyAccessor.getProperty(PROPERTY_FILE_NAME,
            // PROPERTY_KEY_EXPIRE));
            // assertOut.getSamlAuthzDecisionStatement().getEvidence().getAssertion().setAccessConsentPolicy(PropertyAccessor.getProperty(PROPERTY_FILE_NAME,
            // PROPERTY_KEY_ACCESS_CONSENT));

        } catch (PropertyAccessException ex) {
            LOG.error("Universal Client can not access property: " + ex.getMessage());
        }
        return assertOut;
    }

}
