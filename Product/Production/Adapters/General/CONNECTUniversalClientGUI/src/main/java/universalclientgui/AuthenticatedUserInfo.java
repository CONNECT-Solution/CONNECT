/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 *
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 *
 * All other rights reserved.
 *
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
package universalclientgui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import gov.hhs.fha.nhinc.saml.creation.SAMLAssertionCreator;
import gov.hhs.fha.nhinc.saml.creation.UserRolesSNOMED;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import java.util.Map;

/*
 * This code is currently duplicated in these projects
 *  CONNECTUniversalClientGUI
 *  CONNECTConsumerPreferencesProfileGUI
 *
 * It would be nice if there were an ADAPTERCoreLib for this to go in
 *
 * This code does the following
 *  (1) maps the keys the attributes that are provided by the local authentication/authorization system
 *      as reparesented by the HashMap localAuthenticationData into the keys used by the standard mapping
 *      of attribute names to authenticated user values
 *  (2) Processes the contet of the standardAuthenticationDate into new values to be updated in teh SAMLHeader
 *      including the user's personal name and the user's role in the medical care world. That role is later
 *      mapped into a set of official SNOMED codes.
 *  (3) Calls are made to the SAMLAssertikonCreation to update these user specific values into the
 *      AssertionType obkect which is then used to populate the SAML header
 */

/**
 *
 * @author rhaslam
 *
 */
public class AuthenticatedUserInfo {

    private Map<String, List> localAuthenticationData;
    private Map<String, String> standardAuthenticationData;
    private static Map<String, String> mapCHSOpenDSToStandard;
    private final SAMLAssertionCreator assertionCreator;
    private final AssertionType assertions;
    
    /*
     * Establish the mapping between local keys and standard keys
     */
    static {
        mapCHSOpenDSToStandard = new HashMap<String, String>();
        mapCHSOpenDSToStandard.put("givenname", "firstName");
        mapCHSOpenDSToStandard.put("sn", "lastName");
        mapCHSOpenDSToStandard.put("cn", "userName");
        mapCHSOpenDSToStandard.put("employeetype", "userRole");
    }

    /*
     * Establish the base assertions
     */
    /**
     *
     */
    public AuthenticatedUserInfo() {
        assertionCreator = new SAMLAssertionCreator();
        assertions = assertionCreator.createAssertion();
    }

    /**
     *
     * @param localData HashMap
     */
    public void setLocalUserData(Map<String, List> localData) {
        localAuthenticationData = localData;
        standardAuthenticationData = new HashMap<String, String>();
        mapLocalToStandardData(mapCHSOpenDSToStandard);
    }

    /**
     *
     * @return AssertionType
     */
    public AssertionType getAssertions() {
        return assertions;
    }

    private List getFromLocalMap(String name) {
        return (List) localAuthenticationData.get(name);
    }

    /**
     *
     * @param name String
     * @return String
     */
    public String getFromStandardMap(String name) {
        if (standardAuthenticationData != null) {
            return (String) standardAuthenticationData.get(name);
        } else {
            return null;
        }
    }

    private void mapLocalToStandardData(Map<String, String> nameMap) {
        Iterator it = nameMap.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            List localDataList = getFromLocalMap(key);
            if (localDataList != null) {
                String localDataString = (String) localDataList.get(0);
                String newKey = nameMap.get(key);
                if (newKey != null) {
                    standardAuthenticationData.put(newKey, localDataString);
                }
            }
        }
    }

    /**
     *
     * @return AssertionType
     */
    public AssertionType establishSAMLHeaderValues() {
        assertionCreator.getSAMLDynamicUpdatePersonAndUserName(
            assertions,
            getFromStandardMap("firstName"),
            getFromStandardMap("middleName"),
            getFromStandardMap("lastName"),
            getFromStandardMap("userName"));

        String userRole = (String) getFromStandardMap("userRole");
        String[] nameAndCode = UserRolesSNOMED.getRoleNameAndCode(userRole);
        assertionCreator.getSAMLDynamicUpdateUserRole(
            assertions,
            nameAndCode[1],
            UserRolesSNOMED.SNOMED_USER_ROLES_CODE_SYSTEM,
            UserRolesSNOMED.SNOMED_USER_ROLES_CODE_SYSTEM_NAME,
            nameAndCode[0]);

        return assertions;
    }

    /**
     *
     * @param id String
     */
    public void updateAssertedPatientId(String id) {
        assertionCreator.getSAMLDynamicUpdatePatientId(assertions, id);
    }
}
