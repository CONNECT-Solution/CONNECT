/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universalclientgui;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

    private static Log log = LogFactory.getLog(AssertionCreator.class);

    AssertionType createAssertion() {

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
            userPerson.setGivenName(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_FIRST));
            userPerson.setFamilyName(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_LAST));
            userPerson.setSecondNameOrInitials(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_MIDDLE));
            userHc.setName(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_ORG));
            user.setUserName(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_NAME));
            userRole.setCode(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_CODE));
            userRole.setCodeSystem(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_SYSTEM));
            userRole.setCodeSystemName(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_SYSTEM_NAME));
            userRole.setDisplayName(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_DISPLAY));

            purposeCoded.setCode(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_PURPOSE_CODE));
            purposeCoded.setCodeSystem(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_PURPOSE_SYSTEM));
            purposeCoded.setCodeSystemName(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_PURPOSE_SYSTEM_NAME));
            purposeCoded.setDisplayName(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_PURPOSE_DISPLAY));

//            assertOut.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().setNotBefore(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_SIGN));
//            assertOut.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().setNotOnOrAfter(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_EXPIRE));
//            assertOut.getSamlAuthzDecisionStatement().getEvidence().getAssertion().setAccessConsentPolicy(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_ACCESS_CONSENT));

        } catch (PropertyAccessException ex) {
            log.error("Universal Client can not access property: " + ex.getMessage());
        }
        return assertOut;
    }

}
