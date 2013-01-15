/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2012(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 *
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 *
 * All other rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * @author Reed B. Haslam - Northrop Grumman IS
 */
package gov.hhs.fha.nhinc.saml.creation;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceAssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementType;
import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.ArrayList;


public class SAMLAssertionCreator {

    private static final String GATEWAY_PROPERTY_FILE_NAME = "gateway";
    private static final String SAML_ASSERTION_PROPERTY_FILE_NAME = "SAMLAssertions";

    private static Log log = LogFactory.getLog(SAMLAssertionCreator.class);

    public AssertionType createAssertion() {

        AssertionType assertOut = new AssertionType();
   
        SamlAuthzDecisionStatementType authzDecision = new SamlAuthzDecisionStatementType();
        SamlAuthzDecisionStatementEvidenceAssertionType evidenceAssertion = new SamlAuthzDecisionStatementEvidenceAssertionType();
        SamlAuthzDecisionStatementEvidenceType evidence = new SamlAuthzDecisionStatementEvidenceType();

        /* Put evidenceAssertion inside evidence and that inside authzDecision and that inside assertion*/
        evidence.setAssertion(evidenceAssertion);
        authzDecision.setEvidence(evidence);
        assertOut.setSamlAuthzDecisionStatement(authzDecision);

        HomeCommunityType homeCommunity = new HomeCommunityType();
        assertOut.setHomeCommunity(homeCommunity);

        UserType user = new UserType();
        PersonNameType userPerson = new PersonNameType();
        user.setPersonName(userPerson);
        user.setOrg(homeCommunity);

        CeType userRole = new CeType();
        user.setRoleCoded(userRole);
        assertOut.setUserInfo(user);

        CeType purposeOfUse= new CeType();
        assertOut.setPurposeOfDisclosureCoded(purposeOfUse);
        try {
            String homeCommunityId = PropertyAccessor.getInstance().getProperty(GATEWAY_PROPERTY_FILE_NAME, SamlConstants.HOME_COMMUNITY_ID_PROP);
            homeCommunity.setDescription(PropertyAccessor.getInstance().getProperty(GATEWAY_PROPERTY_FILE_NAME, SamlConstants.HOME_COMMUNITY_DESCRIPTION_PROP));
            homeCommunity.setHomeCommunityId(homeCommunityId);
            homeCommunity.setName(PropertyAccessor.getInstance().getProperty(SAML_ASSERTION_PROPERTY_FILE_NAME, SamlConstants.USER_ORG_PROP));

            userPerson.setGivenName(PropertyAccessor.getInstance().getProperty(SAML_ASSERTION_PROPERTY_FILE_NAME, SamlConstants.USER_FIRST_PROP));
            userPerson.setFamilyName(PropertyAccessor.getInstance().getProperty(SAML_ASSERTION_PROPERTY_FILE_NAME, SamlConstants.USER_LAST_PROP));
            userPerson.setSecondNameOrInitials(PropertyAccessor.getInstance().getProperty(SAML_ASSERTION_PROPERTY_FILE_NAME,  SamlConstants.USER_MIDDLE_PROP));
            String userFullName = userPerson.getGivenName() + " " + userPerson.getSecondNameOrInitials() + " " +userPerson.getFamilyName();
            userPerson.setFullName(userFullName);
            user.setOrg(homeCommunity);

            /*
             * While this sounds like it should be the username it ends upassociated with
             * urn:oasis:names:tc:xspa:1.0:subject:subject-id Which is clearfined in the NHIN 2.2
             * Authorization Framework Specification as the 
             * User’s plain-text Name Attribute
             */
            user.setUserName(PropertyAccessor.getInstance().getProperty(SAML_ASSERTION_PROPERTY_FILE_NAME,  SamlConstants.USER_NAME_PROP));

            userRole.setCode(PropertyAccessor.getInstance().getProperty(SAML_ASSERTION_PROPERTY_FILE_NAME, SamlConstants.USER_CODE_PROP));
            userRole.setCodeSystem(PropertyAccessor.getInstance().getProperty(SAML_ASSERTION_PROPERTY_FILE_NAME, SamlConstants.USER_SYST_PROP));
            userRole.setCodeSystemName(PropertyAccessor.getInstance().getProperty(SAML_ASSERTION_PROPERTY_FILE_NAME, SamlConstants.USER_SYST_NAME_PROP));
            userRole.setDisplayName(PropertyAccessor.getInstance().getProperty(SAML_ASSERTION_PROPERTY_FILE_NAME, SamlConstants.USER_DISPLAY_PROP));

            purposeOfUse.setCode(PropertyAccessor.getInstance().getProperty(SAML_ASSERTION_PROPERTY_FILE_NAME, SamlConstants.PURPOSE_CODE_PROP));
            log.debug("PURPOSE_OF_USE_CODE_SYSTEM_PROP =" + SamlConstants.PURPOSE_SYST_PROP);
            purposeOfUse.setCodeSystem(PropertyAccessor.getInstance().getProperty(SAML_ASSERTION_PROPERTY_FILE_NAME, SamlConstants.PURPOSE_SYST_PROP));
            purposeOfUse.setCodeSystemName(PropertyAccessor.getInstance().getProperty(SAML_ASSERTION_PROPERTY_FILE_NAME, SamlConstants.PURPOSE_SYST_NAME_PROP));
            purposeOfUse.setDisplayName(PropertyAccessor.getInstance().getProperty(SAML_ASSERTION_PROPERTY_FILE_NAME, SamlConstants.PURPOSE_DISPLAY_PROP));

            evidenceAssertion.setIssuer(PropertyAccessor.getInstance().getProperty(SAML_ASSERTION_PROPERTY_FILE_NAME, SamlConstants.USER_ORG_X509_NAME));
            evidenceAssertion.setIssuerFormat(PropertyAccessor.getInstance().getProperty(SAML_ASSERTION_PROPERTY_FILE_NAME, SamlConstants.EVIDENCE_ISSUER_FORMAT_PROP));
            evidenceAssertion.getAccessConsentPolicy().add(PropertyAccessor.getInstance().getProperty(SAML_ASSERTION_PROPERTY_FILE_NAME, SamlConstants.EVIDENCE_ACCESS_CONSENT_PROP));
        } catch (PropertyAccessException ex) {
            log.error("Cannotaccess property: " + ex.getMessage());
        }

        return assertOut;
    }
    public void SAMLDynamicUpdatePatientId(AssertionType current, String patientId)
    {
        List <String> patientIdList = null;

        if (patientId == null) return;
        if (current != null)
        {
            if ((patientIdList = current.getUniquePatientId()) != null)
            {
                patientIdList.add(patientId);
            }
            else
            {
                patientIdList = new ArrayList();
                patientIdList.add(patientId);
            }
        }
        else
        {
            log.error("Unable to update the SAML patientId. AssertionType not fully initialized.");            
        }
    }

    public void SAMLDynamicUpdateUserRole(AssertionType current, String code, String codeSystemId, String codeSystemName, String displayName)
    {
        if (current != null && current.getUserInfo() != null && current.getUserInfo()!=null)
        {
            CeType userRole = current.getUserInfo().getRoleCoded();
            if (code != null) userRole.setCode(code);
            if (codeSystemId != null) userRole.setCodeSystem(codeSystemId);
            if (codeSystemName != null) userRole.setCodeSystemName(codeSystemName);
            if (displayName != null) userRole.setDisplayName(displayName);
        }
        else
        {
            log.error("Unable to update the SAML role. AssertionType not fullyinitialized.");
        }
    }

    public void SAMLDynamicUpdatePersonAndUserName(AssertionType current, String firstName, String middleName, String lastName, String userId)
    {
        // Create a new PersonNameType object
        PersonNameType person = new PersonNameType();

        String userFullName = null;
        if (current != null)
        {
            if (firstName != null && firstName.length() > 0)
            {
                person.setGivenName(firstName);
                userFullName = firstName;
            }
            else
            {
                person.setGivenName("Not specified");
            }

            if (middleName != null &&  middleName.length() > 0)
            {
                person.setSecondNameOrInitials(middleName);
                if (userFullName.length() > 0)
                {
                    userFullName += " " + middleName;
                }
                else
                {
                    userFullName = middleName;
                }
            }
            else
            {
                person.setSecondNameOrInitials("");
            }
            if (lastName != null && lastName.length() > 0)
            {
                person.setFamilyName(lastName);
                if (userFullName.length() > 0)
                {
                    userFullName += " " + lastName;
                }
                else
                {
                    userFullName = lastName;
                }
            }
            else
            {
                person.setFamilyName("Not Specified");
            }
            person.setFullName(userFullName);

            // PersonType lives in two places inside AssertionTYpe
            current.setPersonName(person);
            UserType user;
            if ((user  = current.getUserInfo()) != null)
            {
                user.setPersonName(person);
                user.setUserName(userId);
            }
            SamlAuthzDecisionStatementEvidenceAssertionType evidenceAssertion = null;
            SamlAuthzDecisionStatementType authzDecision = current.getSamlAuthzDecisionStatement();
            if ((evidenceAssertion = current.getSamlAuthzDecisionStatement().getEvidence().getAssertion()) != null)
            {
                evidenceAssertion.setIssuer("XUID="+ user.getUserName());
            }
            if (authzDecision != null)
            {
                authzDecision.setResource("UID=" + user.getUserName());

            }
        }
    }
}
