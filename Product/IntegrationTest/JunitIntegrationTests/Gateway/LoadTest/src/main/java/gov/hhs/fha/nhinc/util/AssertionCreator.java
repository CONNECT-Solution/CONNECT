/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.util;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;

/**
 *
 * @author Neil Webb
 */
public class AssertionCreator
{
    private static final String PROPERTY_USER_FIRST = "defaultUserFirstName";
    private static final String PROPERTY_USER_LAST = "defaultUserLastName";
    private static final String PROPERTY_USER_MIDDLE = "defaultUserMiddleName";
    private static final String PROPERTY_USER_ORG = "defaultUserOrganization";
    private static final String PROPERTY_USER_NAME = "defaultUserName";
    private static final String PROPERTY_USER_CODE = "defaultUserRoleCode";
    private static final String PROPERTY_USER_SYSTEM = "defaultUserRoleCodeSystem";
    private static final String PROPERTY_USER_SYSTEM_NAME = "defaultUserRoleCodeSystemName";
    private static final String PROPERTY_USER_DISPLAY = "defaultUserRoleCodeDisplayName";
    private static final String PROPERTY_PURPOSE_CODE = "defaultPurposeForUseRoleCode";
    private static final String PROPERTY_PURPOSE_SYSTEM = "defaultPurposeForUseCodeSystem";
    private static final String PROPERTY_PURPOSE_SYSTEM_NAME = "defaultPurposeForUseCodeSystemName";
    private static final String PROPERTY_PURPOSE_DISPLAY = "defaultPurposeForUseDisplayName";

    public AssertionType createAssertion()
    {
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

        userPerson.setGivenName(PROPERTY_USER_FIRST);
        userPerson.setFamilyName(PROPERTY_USER_LAST);
        userPerson.setSecondNameOrInitials(PROPERTY_USER_MIDDLE);
        userHc.setName(PROPERTY_USER_ORG);
        user.setUserName(PROPERTY_USER_NAME);
        userRole.setCode(PROPERTY_USER_CODE);
        userRole.setCodeSystem(PROPERTY_USER_SYSTEM);
        userRole.setCodeSystemName(PROPERTY_USER_SYSTEM_NAME);
        userRole.setDisplayName(PROPERTY_USER_DISPLAY);

        purposeCoded.setCode(PROPERTY_PURPOSE_CODE);
        purposeCoded.setCodeSystem(PROPERTY_PURPOSE_SYSTEM);
        purposeCoded.setCodeSystemName(PROPERTY_PURPOSE_SYSTEM_NAME);
        purposeCoded.setDisplayName(PROPERTY_PURPOSE_DISPLAY);

        return assertOut;
    }
}
